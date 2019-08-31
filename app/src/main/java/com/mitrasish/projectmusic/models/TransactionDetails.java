package com.mitrasish.projectmusic.models;

import java.util.Date;

public class TransactionDetails {
    private String Title;
    private String Desription;
    private Float Token;
    private String Type;
    private Long Date;

    public TransactionDetails() {
    }

    public TransactionDetails(String title, String desription, Float token, String type, Long date) {
        Title = title;
        Desription = desription;
        Token = token;
        Type = type;
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesription() {
        return Desription;
    }

    public void setDesription(String desription) {
        Desription = desription;
    }

    public Float getToken() {
        return Token;
    }

    public void setToken(Float token) {
        Token = token;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Long getDate() {
        return Date;
    }

    public void setDate(Long date) {
        Date = date;
    }
}
