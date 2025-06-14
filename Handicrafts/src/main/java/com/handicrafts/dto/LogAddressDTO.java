package com.handicrafts.dto;

public class LogAddressDTO {
    private String function;
    private int userId;
    private String content;

    public LogAddressDTO(String function, int userId, String content) {
        this.function = function;
        this.userId = userId;
        this.content = content;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
