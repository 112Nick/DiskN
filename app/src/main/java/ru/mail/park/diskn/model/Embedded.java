package ru.mail.park.diskn.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nick on 26.03.18.
 */

public class Embedded {
    @SerializedName("items")
    ArrayList<ResourceItem> items;

    public ArrayList<ResourceItem> getItems() {
        return items;
    }

    public Embedded(ArrayList<ResourceItem> items) {
        this.items = items;
    }
}
