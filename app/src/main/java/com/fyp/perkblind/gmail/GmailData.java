package com.fyp.perkblind.gmail;

import android.net.Uri;

public class GmailData {
    String id;
    String email;
    String username;
    Uri pic;

    public GmailData(String id, String email, String username, Uri pic) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Uri getPic() {
        return pic;
    }

    public void setPic(Uri pic) {
        this.pic = pic;
    }
}
