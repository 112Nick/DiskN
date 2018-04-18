package ru.mail.park.diskn.model;

import com.google.gson.annotations.SerializedName;

/**
 * used   country (string, optional): <Страна>,
 * used   login (string, optional): <Логин>,
 * used   display_name (string, optional): <Отображаемое имя>,
 * used   uid (string, optional): <Идентификатор пользователя>
 */

public class User {
    @SerializedName("country")
    private final String country;

    @SerializedName("display_name")
    private final String displayName;


    public User(String country, String displayName) {
        this.country = country;
        this.displayName = displayName;
    }

    public String getCountry() {
        return country;
    }


    public String getDisplayName() {
        return displayName;
    }

}
