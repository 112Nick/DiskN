package ru.mail.park.diskn.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nick on 26.03.18.
 */

public class Test {

    @SerializedName("_embedded")
    Embedded embedded;

    public Test(Embedded embedded) {
        this.embedded = embedded;
    }

    public Embedded getEmbedded() {
        return embedded;
    }
}
