package com.anusha.notification_engine.ai;

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
  "decision": "HIGH_PRIORITY or NORMAL",
  "reason": "short explanation"
}

Message:
""" + message;

        try {

            Map<String, Object> response =
                    webClient.post()
                            .uri(apiUrl)
                            .header("Authorization", "Bearer " + apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of(
                                    "model", "llama3-8b-8192",
                                    "temperature", 0,
                                    "messages", List.of(
                                            Map.of(
                                                    "role", "user",
                                                    "content", prompt
                                            )
                                    )
                            ))
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                            .timeout(Duration.ofSeconds(20))
                            .block();

            return parseAiResponse(response);

        }
        catch (WebClientResponseException e) {

            System.out.println("===== GROQ API ERROR =====");
            System.out.println(e.getResponseBodyAsString());

            return fallback("AI API request failed");
        }
        catch (Exception e) {

            System.out.println("===== AI EXECUTION ERROR =====");
            e.printStackTrace();

            return fallback("AI parsing failure");
        }
    }

    // ======================================================
    // RESPONSE PARSER
    // ======================================================
    private DecisionResult parseAiResponse(Map<String, Object> response) {

        try {

            if (response == null || !response.containsKey("choices")) {
                return fallback("Empty AI response");
            }

            List<?> choices = (List<?>) response.get("choices");
            if (choices.isEmpty()) {
                return fallback("No choices returned");
            }

            Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
            Map<?, ?> messageObj = (Map<?, ?>) firstChoice.get("message");

            if (messageObj == null) {
                return fallback("Missing message object");
            }

            String content = String.valueOf(messageObj.get("content"));

            System.out.println("\n===== RAW AI RESPONSE =====");
            System.out.println(content);

            String json = extractJson(content);

            if (json == null) {
                return fallback("AI returned non-JSON text");
            }

            System.out.println("\n===== CLEAN JSON =====");
            System.out.println(json);

            AiResponse aiResponse =
                    objectMapper.readValue(json, AiResponse.class);

            return new DecisionResult(
                    aiResponse.getDecision(),
                    aiResponse.getReason()
            );

        } catch (Exception e) {

            System.out.println("===== PARSE ERROR =====");
            e.printStackTrace();

            return fallback("JSON parse failed");
        }
    }

    // ======================================================
    // JSON CLEANER (LLM SAFE)
    // ======================================================
    private String extractJson(String text) {

        if (text == null) return null;

        // remove markdown formatting
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

        return new DecisionResult(
                "NORMAL",
                "Fallback decision (" + reason + ")"
        );
    }
}