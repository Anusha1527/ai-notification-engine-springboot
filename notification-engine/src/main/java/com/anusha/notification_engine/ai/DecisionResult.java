package com.anusha.notification_engine.ai;

public class DecisionResult {

    private String decision;
    private String reason;

    public DecisionResult(String decision, String reason) {
        this.decision = decision;
        this.reason = reason;
    }

    public String getDecision() {
        return decision;
    }

    public String getReason() {
        return reason;
    }
}