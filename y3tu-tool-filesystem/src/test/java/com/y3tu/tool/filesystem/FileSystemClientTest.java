package com.y3tu.tool.filesystem;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.filesystem.provider.ProviderTypeEnum;
import com.y3tu.tool.setting.Setting;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/10/31
 */
public class FileSystemClientTest {

    @Test
    public void getProvider() {
        Setting setting = new Setting("fs.setting");
        FileSystemClient client = FileSystemClient.createClient(ProviderTypeEnum.QINIU, setting);
        File file = FileUtil.file("/Users/yxy/work/meizi.jpg");
        String path = client.upload(file);
        Console.log(path);
    }

    @Test
    public void TencentProvider() {
        Setting setting = new Setting("fs.setting");
        FileSystemClient client = FileSystemClient.createClient(ProviderTypeEnum.TENCENT, setting);
        File file = FileUtil.file("/Users/yxy/work/meizi.jpg");
        String path = client.upload(file);
        Console.log(path);
    }


}