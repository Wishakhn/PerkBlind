package com.example.perkblind;

public class UserData {
    private String userName ="";
    private String email = "";
    private String qrStr = "";
    private String password = "";
    private Boolean isLoggedIn = false;



    void  UserData(String userName, String email, String qrStr, String password, Boolean isLoggedIn){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.qrStr = qrStr;
        this.isLoggedIn = isLoggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQrStr() {
        return qrStr;
    }

    public void setQrStr(String qrStr) {
        this.qrStr = qrStr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
