package com.hampus.projektuppgiftapi.model.token;

public class RefreshTokenRequest implements IRefreshTokens {

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

