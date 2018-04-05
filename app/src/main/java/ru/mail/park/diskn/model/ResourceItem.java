package ru.mail.park.diskn.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * ----    antivirus_status (undefined, optional): <Статус проверки антивирусом>,
 * used    resource_id (string, optional): <Идентификатор ресурса>,
 * ----    share (ShareInfo, optional): <Информация об общей папке>,
 * used    file (string, optional): <URL для скачивания файла>,
 * used    size (integer, optional): <Размер файла>,
 * ----    _embedded (ResourceList, optional): <Список вложенных ресурсов>,
 * ----    exif (Exif, optional): <Метаданные медиафайла (EXIF)>,
 * ----    custom_properties (object, optional): <Пользовательские атрибуты ресурса>,
 * used    media_type (string, optional): <Определённый Диском тип файла>,
 * ----    sha256 (string, optional): <SHA256-хэш>,
 * ----    used    type (string): <Тип>,
 * ----    mime_type (string, optional): <MIME-тип файла>,
 * ----    revision (integer, optional): <Ревизия Диска в которой этот ресурс был изменён последний раз>,
 * ----    public_url (string, optional): <Публичный URL>,
 * used    path (string): <Путь к ресурсу>,
 * ----    md5 (string, optional): <MD5-хэш>,
 * ----    public_key (string, optional): <Ключ опубликованного ресурса>,
 * used    preview (string, optional): <URL превью файла>,
 * used    name (string): <Имя>,
 * used    created (string): <Дата создания>,
 * used    modified (string): <Дата изменения>,
 * ----    comment_ids (CommentIds, optional): <Идентификаторы комментариев>
 */

public class ResourceItem {
    @SerializedName("resource_id")
    private final String resourceId;
    @SerializedName("name")
    private final String name;
    @SerializedName("created")
    private final String created;
    @SerializedName("modified")
    private final String modified;
    @SerializedName("file")
    private final String fileURL;
    @SerializedName("size")
    private final BigDecimal size;
    @SerializedName("media_type")
    private final String media_type;
    @SerializedName("type")
    private final String type;
    @SerializedName("preview")
    private final String preview;
    @SerializedName("path")
    private final String path;

    public ResourceItem(String resourceId, String name, String created, String path, String modified, String fileURL, BigDecimal size, String media_type, String type, String preview) {
        this.resourceId = resourceId;
        this.name = name;
        this.created = created;
        this.modified = modified;
        this.fileURL = fileURL;
        this.size = size;
        this.media_type = media_type;
        this.type = type;
        this.preview = preview;
        this.path = path;
    }

    public String getPreview() {
        return preview;
    }

    public String getPath() {
        return path.substring(5, path.length());
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }

    public String getFileURL() {
        return fileURL;
    }

    public BigDecimal getSize() {
        return size;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getType() {
        return type;
    }

    public boolean isDirectory() {
        if (this.getType().equals("dir")) {
            return true;
        }
        return false;
    }
}
