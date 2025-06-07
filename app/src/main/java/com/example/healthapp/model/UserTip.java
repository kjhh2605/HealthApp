package com.example.healthapp.model;

import java.util.Objects;

public class UserTip {
    private String nickname;
    private String machine;
    private String context;
    private int likes;

    private int dislikes;

    public UserTip() {}

    public UserTip(String nickname, String machine, String context) {
        this.nickname = nickname;
        this.machine = machine;
        this.context = context;
        this.likes = 0;
        this.dislikes = 0;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTip userTip = (UserTip) o;
        return Objects.equals(nickname, userTip.nickname) && Objects.equals(context, userTip.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, context);
    }


}
