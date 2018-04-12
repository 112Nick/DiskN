package ru.mail.park.diskn.model;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

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
    private final Integer size;
    @SerializedName("media_type")
    private final String media_type;
    @SerializedName("type")
    private final String type;
    @SerializedName("preview")
    private final String preview;
    @SerializedName("path")
    private final String path;
    @SerializedName("origin_path")
    private final String originPath;

    public ResourceItem(String resourceId, String name, String created, String path, String modified,
                        String fileURL, Integer size, String media_type, String type, String preview, String originPath) {
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
        this.originPath = originPath;
//        this.originPath = path.substring(5, path.length());
    }

    public String getPreview() {
        return preview;
    }

    public String getPath() {
        if (path.charAt(0) == 'd') {
            return path.substring(5, path.length());
        }
        return path.substring(6, path.length());

    }

    public String getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }

    public String getFolder() {
        String fullPath = getPath();
        return fullPath.substring(0, fullPath.length() - getName().length());
    }

    public String getOriginPath() {
        return originPath.substring(5, originPath.length());
    }

    public String getCreated() {
        return created.substring(0, 10) + " / " + created.substring(11, 16);
    }

    public String getModified() {
        return modified.substring(0, 10) + " / " + modified.substring(11, 16);
    }

    public String getFileURL() {
        return fileURL;
    }

    public String getSize() {
        Double sizeF = this.size.doubleValue();
        String sizeClass = "Kb";
        Double divisor = Double.valueOf("1024");
        sizeF = sizeF / divisor; //KiloBytes
        if (sizeF >= divisor) {
            sizeF = sizeF / divisor; // MegaBytes
            sizeClass = "Mb";

        }
        if (sizeF >= divisor) {
            sizeF = sizeF / divisor; //GigaBytes
            sizeClass = "Gb";
        }

        return String.format(Locale.getDefault(), "%.2f", sizeF) + " " + sizeClass;
    }

    public String getMedia_type() {
        return media_type;
    }

    private String getType() {
        return type;
    }

    public boolean isDirectory() {
        return type.equals("dir");
    }
}
