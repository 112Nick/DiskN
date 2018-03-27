package ru.mail.park.diskn.APIs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.mail.park.diskn.Models.Disk;
import ru.mail.park.diskn.Models.Embedded;
import ru.mail.park.diskn.Models.ResourceItem;
import ru.mail.park.diskn.Models.Test;

/**
 * Created by nick on 23.03.18.
 */

public interface YandexApi {


    //Get metainformation about user's disk
    @GET("/v1/disk")
    Call<Disk> getDiskInfo();

    @GET("/v1/disk/resources?path=%2F")
    Call<Test> getResources();
}
