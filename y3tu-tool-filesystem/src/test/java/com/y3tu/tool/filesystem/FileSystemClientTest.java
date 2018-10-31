package com.y3tu.tool.filesystem;

import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.filesystem.provider.ProviderTypeEnum;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/10/31
 */
public class FileSystemClientTest {

    @Test
    public void getProvider() {
        Console.log(ProviderTypeEnum.ALIYUN.getType());
    }
}