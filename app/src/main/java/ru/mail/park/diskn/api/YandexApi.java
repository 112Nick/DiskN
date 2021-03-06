package ru.mail.park.diskn.api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.mail.park.diskn.model.Disk;
import ru.mail.park.diskn.model.FilesArr;

/**
 * Created by nick on 23.03.18.
 */

public interface YandexApi {

    String YANDEX_BASE_URL = "https://cloud-api.yandex.net:443";

    //Get meta information about user's disk
    @GET("/v1/disk")
    Call<Disk> getDiskInfo();

    //Files
    @GET("/v1/disk/resources")
    Call<FilesArr> getResources(@Query("path") String path);

    @DELETE("/v1/disk/resources")
    Call<Void> deleteResource(@Query("path") String path);

    @POST("/v1/disk/resources/copy")
    Call<Void> copyResource(@Query("from") String from, @Query("path") String path);


    //Trash
    @PUT("/v1/disk/trash/resources/restore")
    Call<Void> restoreResource(@Query("path") String path);

    @DELETE("/v1/disk/trash/resources")
    Call<Void> deleteAllFromTrash();

    @GET("/v1/disk/trash/resources")
    Call<FilesArr> getTrashResources(@Query("path") String path);
}
