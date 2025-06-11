package com.example.healthapp.model.chatbot;

import java.util.List;

public class ChatRequest {
    public String model = "sonar";
    public List<ChatMessage> messages;
    public int max_tokens = 500;
    public double temperature = 0.7;

    public ChatRequest(List<ChatMessage> messages) {
        this.messages = messages;
    }
}