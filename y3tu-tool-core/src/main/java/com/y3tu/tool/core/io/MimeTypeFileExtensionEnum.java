package com.y3tu.tool.core.io;

/**
 * Mime类型和文件后缀枚举映射
 *
 * @author y3tu
 * @date 2018/10/29
 */
public enum MimeTypeFileExtensionEnum {
    /**
     * 文件 Mime对应关系
     */
    JPG("image/jpeg", ".jpg"),
    GIF("image/gif", ".gif"),
    PNG("image/png", ".png"),
    BMP("image/bmp", ".bmp"),
    TXT("text/plain", ".txt"),
    ZIP("application/zip", ".zip"),
    ZIP_XZC("application/x-zip-compressed", ".zip"),
    ZIP_XZ("multipart/x-zip", ".zip"),
    ZIP_XC("application/x-compressed", ".zip"),
    MP3("audio/mpeg3", ".mp3"),
    AVI("video/avi", ".avi"),
    WAV("audio/wav", ".wav"),
    GZIP("application/x-gzip", ".gzip"),
    GZ("application/x-gzip", ".gz"),
    HTML("text/html", ".html"),
    SVG("application/x-shockwave-flash", ".svg"),
    PDF("application/pdf", ".pdf"),
    DOC("application/msword", ".doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    XLS("application/vnd.ms-excel", ".xls"),
    PPT("application/vnd.ms-powerpoint", ".ppt"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");

    String mimeType;
    String fileExtension;

    MimeTypeFileExtensionEnum(String mimeType, String fileExtension) {
        this.mimeType = mimeType;
        this.fileExtension = fileExtension;
    }

    /**
     * 根据文件后缀名获取对应的mime类型
     * 如果有多条对应 获取第一条
     *
     * @param fileExtension
     * @return
     */
    public static String getMimeType(String fileExtension) {
        for (MimeTypeFileExtensionEnum mimeTypeFileExtensionEnum : MimeTypeFileExtensionEnum.values()) {
            if (mimeTypeFileExtensionEnum.fileExtension.equals(fileExtension)) {
                return mimeTypeFileExtensionEnum.mimeType;
            }
        }
        return fileExtension;
    }

    /**
     * 根据mime类型获取文件后缀名
     *
     * @param mimeType
     * @return
     */
    public static String getFileExtension(String mimeType) {
        for (MimeTypeFileExtensionEnum mimeTypeFileExtensionEnum : MimeTypeFileExtensionEnum.values()) {
            if (mimeTypeFileExtensionEnum.mimeType.equals(mimeType)) {
                return mimeTypeFileExtensionEnum.fileExtension;
            }
        }
        return mimeType;
    }
}
