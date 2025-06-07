package com.example.healthapp.model;

public class YoutubeLink {
    private final String thumbnailUrl;
    private final String youtubeUrl;
    public YoutubeLink(String thumbnailUrl, String youtubeUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.youtubeUrl = youtubeUrl;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public String getYoutubeUrl(){
        return youtubeUrl;
    }
}
