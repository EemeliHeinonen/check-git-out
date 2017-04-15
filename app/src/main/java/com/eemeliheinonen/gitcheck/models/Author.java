package com.eemeliheinonen.gitcheck.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by eemeliheinonen on 02/04/2017.
 */


public class Author {

    private String name;
    private String login;
    private String email;
    private Date date;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getDate() {
        return date;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
