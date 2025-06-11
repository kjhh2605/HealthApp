package com.example.healthapp.model.chatbot;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PerplexityApi {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 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"
    })
    @POST("chat/completions")
    Call<ChatResponse> getChatResponse(@Body ChatRequest request);
}
