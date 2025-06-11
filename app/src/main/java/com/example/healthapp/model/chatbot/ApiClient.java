package com.example.healthapp.model.chatbot;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.perplexity.ai/";
    private static Retrofit retrofit;

    public static PerplexityApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.perplexity.ai/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(PerplexityApi.class);
    }
}