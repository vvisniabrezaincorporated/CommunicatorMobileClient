package com.example.mbreza.wnb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    @SerializedName("user_id")
    private long user_id;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("active")
    private boolean active;

    @Expose
    @SerializedName("public_key")
    private boolean publicKey;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = true;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPublicKey() {
        return publicKey;
    }

    public void setPublicKey(boolean publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", publicKey=" + publicKey +
                '}';
    }
}
