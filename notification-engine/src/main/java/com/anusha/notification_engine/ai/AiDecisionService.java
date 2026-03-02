
package com.anusha.notification_engine.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class AiDecisionService {

    private static final Logger logger = LoggerFactory.getLogger(AiDecisionService.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    // ✅ Constructor Injection (Senior Practice)
    public AiDecisionService(WebClient.Builder builder) {
        this.webClient = builder.build();
        this.objectMapper = new ObjectMapper();
    }

    // ======================================================
    // MAIN AI ANALYSIS
    // ======================================================
        public DecisionResult analyze(String message) {
        String prompt = """
    You are an AI notification classifier.

    Classify notification priority.

    Rules:
    - HIGH_PRIORITY → outages, failures, urgent production issues
    - NORMAL → informational updates

    Return ONLY valid JSON.
    Do NOT add explanations.

    Format:
    {
      \"decision\": \"HIGH_PRIORITY or NORMAL\",
      \"reason\": \"short explanation\"
    }

    Message:
    """ + message;

        try {
            Map<String, Object> requestBody = Map.of(
                "model", "llama3-8b-8192",
                "temperature", 0,
                "messages", List.of(
                    Map.of(
                        "role", "user",
                        "content", prompt
                    )
                )
            );

            Map<String, Object> response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .timeout(Duration.ofSeconds(20))
                .block();

            logger.info("AI raw response: {}", response);
            return parseAiResponse(response);

        } catch (WebClientResponseException e) {
            logger.error("GROQ API error: {}", e.getResponseBodyAsString(), e);
            return fallback("AI API request failed: " + e.getStatusCode());
        } catch (Exception e) {
            logger.error("AI execution error", e);
            return fallback("AI parsing failure: " + e.getMessage());
        }
        }

    // ======================================================
    // RESPONSE PARSER
    // ======================================================
    private DecisionResult parseAiResponse(Map<String, Object> response) {
        try {
            if (response == null || !response.containsKey("choices")) {
                logger.warn("Empty or invalid AI response: {}", response);
                return fallback("Empty AI response");
            }

            List<?> choices = (List<?>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                logger.warn("No choices returned in AI response: {}", response);
                return fallback("No choices returned");
            }

            Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
            if (firstChoice == null || !firstChoice.containsKey("message")) {
                logger.warn("Missing message object in AI response: {}", firstChoice);
                return fallback("Missing message object");
            }

            Map<?, ?> messageObj = (Map<?, ?>) firstChoice.get("message");
            String content = messageObj != null ? String.valueOf(messageObj.get("content")) : null;

            logger.info("AI message content: {}", content);

            String json = extractJson(content);
            if (json == null) {
                logger.warn("AI returned non-JSON text: {}", content);
                return fallback("AI returned non-JSON text");
            }

            logger.info("AI clean JSON: {}", json);

            AiResponse aiResponse = objectMapper.readValue(json, AiResponse.class);
            if (aiResponse.getDecision() == null || aiResponse.getReason() == null) {
                logger.warn("AI response missing fields: {}", aiResponse);
                return fallback("AI response missing fields");
            }

            return new DecisionResult(
                    aiResponse.getDecision(),
                    aiResponse.getReason()
            );

        } catch (Exception e) {
            logger.error("AI response parse error", e);
            return fallback("JSON parse failed: " + e.getMessage());
        }
    }

    // ======================================================
    // JSON CLEANER (LLM SAFE)
    // ======================================================
    private String extractJson(String text) {
        if (text == null) return null;
        // Remove markdown formatting and whitespace
        text = text.replace("```json", "")
                   .replace("```", "")
                   .trim();
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1).trim();
        }
        return null;
    }

    // ======================================================
    // FALLBACK STRATEGY
    // ======================================================
    private DecisionResult fallback(String reason) {
        logger.info("AI fallback triggered: {}", reason);
        return new DecisionResult(
                "NORMAL",
                "Fallback decision (" + reason + ")"
        );
    }
}