package ru.mail.park.diskn.APIs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.mail.park.diskn.Models.Disk;

/**
 * Created by nick on 23.03.18.
 */

public interface YandexApi {


    //Get metainformation about user's disk
    @GET("/v1/disk")
    Call<Disk> getDiskInfo();
}
