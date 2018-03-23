package ru.mail.park.diskn.Models;


import com.google.gson.annotations.SerializedName;


/**
 *        max_file_size (integer, optional): <Максимальный поддерживаемый размер файла.>,
 *        unlimited_autoupload_enabled (boolean, optional): <Признак включенной безлимитной автозагрузки с мобильных устройств.>,
 *  used  total_space (integer, optional): <Общий объем диска (байт)>,
 *  used  trash_size (integer, optional): <Общий размер файлов в Корзине (байт). Входит в used_space.>,
 *        is_paid (boolean, optional): <Признак наличия купленного места.>,
 *  used  used_space (integer, optional): <Используемый объем диска (байт)>,
 *  used  system_folders (SystemFolders, optional): <Адреса системных папок в Диске пользователя.>,
 *  used  user (User, optional): <Владелец Диска>,
 *        revision (integer, optional): <Текущая ревизия Диска>
 */

public class Disk {
    @SerializedName("user")
    private final User user;
    @SerializedName("used_space")
    private final Integer usedSpace;
    @SerializedName("total_space")
    private final Integer totalSpace;
    @SerializedName("trash_size")
    private final Integer trashSize;
//    @SerializedName("system_folders")
//    private final SystemFolders systemFolders;

    public Disk(User user, Integer usedSpace, Integer totalSpace, Integer trashSize) {
        this.user = user;
        this.usedSpace = usedSpace;
        this.totalSpace = totalSpace;
        this.trashSize = trashSize;
       // this.systemFolders = systemFolders;
    }


    public User getUser() {
        return user;
    }

    public Integer getUsedSpace() {
        return usedSpace;
    }

    public Integer getTotalSpace() {
        return totalSpace;
    }

    public Integer getTrashSize() {
        return trashSize;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "user=" + user +
                ", usedSpace=" + usedSpace +
                ", totalSpace=" + totalSpace +
                ", trashSize=" + trashSize +
                '}';
    }

//    public SystemFolders getSystemFolders() {
//        return systemFolders;
//    }

//    @Override
//    public String toString() {
//        return "Disk{" +
//                "user=" + user +
//                ", usedSpace=" + usedSpace +
//                ", totalSpace=" + totalSpace +
//                ", trashSize=" + trashSize +
//                ", systemFolders=" + systemFolders +
//                '}';
//    }
}
