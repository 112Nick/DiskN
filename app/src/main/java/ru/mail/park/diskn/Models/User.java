package ru.mail.park.diskn.Models;

import com.google.gson.annotations.SerializedName;

/**
 *  used   country (string, optional): <Страна>,
 *  used   login (string, optional): <Логин>,
 *  used   display_name (string, optional): <Отображаемое имя>,
 *  used   uid (string, optional): <Идентификатор пользователя>
 */

public class User {
    @SerializedName("country")
    private final String country;
    @SerializedName("login")
    private final String login;
    @SerializedName("display_name")
    private final String displayName;
    @SerializedName("uid")
    private final String uid;

    public User(String country, String login, String displayName, String uid) {
        this.country = country;
        this.login = login;
        this.displayName = displayName;
        this.uid = uid;
    }

    public String getCountry() {
        return country;
    }

    public String getLogin() {
        return login;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUid() {
        return uid;
    }
}
