package com.y3tu.tool.filesystem;

import com.y3tu.tool.core.exception.UtilException;
import com.y3tu.tool.core.io.FilePathUtil;
import com.y3tu.tool.core.io.MimeTypeFileExtensionEnum;
import com.y3tu.tool.core.lang.UUID;
import com.y3tu.tool.core.text.StringUtils;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传对象
 *
 * @author vakin
 */
public class UploadObject {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String mimeType;
    /**
     * 分类
     */
    private String catalog;
    /**
     * 文件路径
     */
    private String url;
    /**
     * 文件字节
     */
    private byte[] bytes;

    /**
     * 文件
     */
    private File file;
    /**
     * 文件输入流
     */
    private InputStream inputStream;

    /**
     * 文件元数据
     */
    private Map<String, Object> metadata = new HashMap<String, Object>();

    public UploadObject(String filePath) {
        if (filePath.startsWith(FilePathUtil.HTTP_PREFIX) || filePath.startsWith(FilePathUtil.HTTPS_PREFIX)) {
            this.url = filePath;
            this.fileName = FilePathUtil.getFileName(this.url);
        } else {
            this.file = new File(filePath);
            this.fileName = file.getName();
        }
    }

    public UploadObject(File file) {
        this.fileName = file.getName();
        this.file = file;
    }

    public UploadObject(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public UploadObject(String fileName, InputStream inputStream, String mimeType) {
        this.fileName = fileName;
        this.inputStream = inputStream;
        this.mimeType = mimeType;
    }

    public UploadObject(String fileName, byte[] bytes, String mimeType) {
        this.fileName = fileName;
        this.bytes = bytes;
        this.mimeType = mimeType;
    }

    public UploadObject(String fileName, byte[] bytes) {
        this.fileName = fileName;
        this.bytes = bytes;
        this.mimeType = parseMimeType(bytes);
    }

    public String getFileName() {
        if (StringUtils.isBlank(fileName)) {
            fileName = UUID.randomUUID().toString().replaceAll("\\-", "");
        }
        if (mimeType != null && !fileName.contains(".")) {
            String fileExtension = MimeTypeFileExtensionEnum.getFileExtension(mimeType);
            if (fileExtension != null) {
                fileName = fileName + fileExtension;
            }
        }
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public File getFile() {
        return file;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public UploadObject setString(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public UploadObject setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public UploadObject addMetaData(String key, Object value) {
        metadata.put(key, value);
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }


    public String getCatalog() {
        return catalog;
    }

    public UploadObject setCatalog(String catalog) {
        this.catalog = catalog;
        return this;
    }

    /**
     * 获取Mime类型
     *
     * @param bytes
     * @return
     */
    private static String parseMimeType(byte[] bytes) {
        try {
            MagicMatch match = Magic.getMagicMatch(bytes);
            String mimeType = match.getMimeType();
            return mimeType;
        } catch (Exception e) {
            throw new UtilException("获取Mime类型失败！", e);
        }
    }

}
