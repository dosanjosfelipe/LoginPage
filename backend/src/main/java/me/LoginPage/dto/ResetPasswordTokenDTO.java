package me.LoginPage.dto;

import java.util.Date;

public class ResetPasswordTokenDTO {
    private String token;
    private Date expiryDate;

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
