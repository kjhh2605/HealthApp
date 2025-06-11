package com.example.healthapp.model.chatbot;

public class ChatMessage {
    private String role;
    private String content; // API 요구 필드명에 맞춤

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}
