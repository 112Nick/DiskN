package ru.mail.park.diskn.model;


import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;


/**
 *        max_file_size (integer, optional): <Максимальный поддерживаемый размер файла.>,
 *        unlimited_autoupload_enabled (boolean, optional): <Признак включенной безлимитной автозагрузки с мобильных устройств.>,
 *  used  total_space (integer, optional): <Общий объем диска (байт)>,
 *  used  trash_size (integer, optional): <Общий размер файлов в Корзине (байт). Входит в used_space.>,
 *        is_paid (boolean, optional): <Признак наличия купленного места.>,
 *  used  used_space (integer, optional): <Используемый объем диска (байт)>,
 *        system_folders (SystemFolders, optional): <Адреса системных папок в Диске пользователя.>,
 *  used  user (User, optional): <Владелец Диска>,
 *        revision (integer, optional): <Текущая ревизия Диска>
 */

public class Disk {
    @SerializedName("user")
    private final User user;
    @SerializedName("used_space")
    private final BigDecimal usedSpace;
    @SerializedName("total_space")
    private final BigDecimal totalSpace;
    @SerializedName("trash_size")
    private final BigDecimal trashSize;


    public Disk(User user, BigDecimal usedSpace, BigDecimal totalSpace, BigDecimal trashSize) {
        this.user = user;
        this.usedSpace = usedSpace;
        this.totalSpace = totalSpace;
        this.trashSize = trashSize;
    }


    public User getUser() {
        return user;
    }

    public BigDecimal getUsedSpace() {
        return usedSpace;
    }

    public BigDecimal getTotalSpace() {
        return totalSpace;
    }

    public BigDecimal getTrashSize() {
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

}
