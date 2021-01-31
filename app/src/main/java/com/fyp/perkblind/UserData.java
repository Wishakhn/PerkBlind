package com.fyp.perkblind;

public class UserData {
    private String id ="";
    private String username ="";
    private String email = "";
    private String qrCode = "";
    private String password = "";
    private String isLoggedIn = "";

    public UserData() {
    }

    public UserData(String id, String username, String email, String qrCode, String password, String isLoggedIn) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.qrCode = qrCode;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(String loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
