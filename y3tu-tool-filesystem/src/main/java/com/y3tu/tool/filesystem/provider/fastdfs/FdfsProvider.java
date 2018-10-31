package com.y3tu.tool.filesystem.provider.fastdfs;

import com.y3tu.tool.filesystem.FileSystemException;
import com.y3tu.tool.filesystem.UploadObject;
import com.y3tu.tool.filesystem.UploadTokenParam;
import com.y3tu.tool.filesystem.provider.AbstractProvider;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * FastDFS 文件服务
 *
 * @author vakin
 */
public class FdfsProvider extends AbstractProvider {

    public static final String NAME = "fastDFS";

    private String groupName;
    private StorageClient1 client;

    public FdfsProvider(Properties props) {
        this.groupName = props.getProperty("groupName");
        try {
            ClientGlobal.initByProperties(props);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = tracker.getStoreStorage(trackerServer);
            client = new StorageClient1(trackerServer, storageServer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String upload(UploadObject object) {
        NameValuePair[] metaDatas = new NameValuePair[object.getMetadata().size()];
        int index = 0;
        for (String key : object.getMetadata().keySet()) {
            metaDatas[index++] = new NameValuePair(key, object.getMetadata().get(key).toString());
        }
        try {
            if (object.getFile() != null) {
                client.upload_file1(groupName, object.getFile().getAbsolutePath(), object.getMimeType(), metaDatas);
            } else if (object.getBytes() != null) {

            } else if (object.getInputStream() != null) {

            }
        } catch (Exception e) {
            throw new FileSystemException("上传文件异常！", e);
        }
        return null;
    }

    @Override
    public Map<String, Object> createUploadToken(UploadTokenParam param) {
        return null;
    }

    @Override
    public boolean delete(String fileKey) {
        return false;
    }

    @Override
    public String getDownloadUrl(String fileKey) {
        return getFullPath(fileKey);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void close() throws IOException {

    }
}
