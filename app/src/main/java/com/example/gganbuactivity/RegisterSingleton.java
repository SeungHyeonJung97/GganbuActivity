package com.example.gganbuactivity;

public class RegisterSingleton {
    private String email;
    private String password;
    private String nickname;
    private String birthday;
    private String gender;
    private String eatingHabits;
    private String lifePattern;
    private String mbti;
    private int idToken;

    public int getIdToken() {
        return idToken;
    }

    public void setIdToken(int idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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
    private static RegisterSingleton instance = null;

    public static RegisterSingleton getInstance(){
        if(instance == null){
            instance = new RegisterSingleton();
        }
        return instance;
    }
}
