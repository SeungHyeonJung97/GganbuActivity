package com.example.gganbuactivity;

public class Post {
    private String title;
    private String location;
    private String type;
    private String deposit;
    private String month;
    private String age_start;
    private String age_end;
    private String gender;
    private String eatingHabits;
    private String lifePattern;
    private String mbti;
    private String idToken;
    private String url;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String link;

    public String getAge_start() {
        return age_start;
    }

    public void setAge_start(String age_start) {
        this.age_start = age_start;
    }

    public String getAge_end() {
        return age_end;
    }

    public void setAge_end(String age_end) {
        this.age_end = age_end;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Post() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEatingHabits() {
        return eatingHabits;
    }

    public void setEatingHabits(String eatingHabits) {
        this.eatingHabits = eatingHabits;
    }

    public String getLifePattern() {
        return lifePattern;
    }

    public void setLifePattern(String lifePattern) {
        this.lifePattern = lifePattern;
    }

    public String getMbti() {
        return mbti;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
