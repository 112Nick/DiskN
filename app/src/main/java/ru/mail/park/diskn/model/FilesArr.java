package ru.mail.park.diskn.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nick on 26.03.18.
 */

public class FilesArr {

    @SerializedName("_embedded")
    private final Embedded embedded;

    public FilesArr(Embedded embedded) {
        this.embedded = embedded;
    }

    public Embedded getEmbedded() {
        return embedded;
    }
}
