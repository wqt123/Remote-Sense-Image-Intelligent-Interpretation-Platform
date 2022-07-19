package com.remote.user.entities;

public class LoginInfo {
    public String userName_or_email;

    public String password;

    public String getUserName_or_email() {
        return userName_or_email;
    }

    public void setUserName_or_email(String userName_or_email) {
        this.userName_or_email = userName_or_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
