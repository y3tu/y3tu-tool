package com.y3tu.tool.core.io.resource;

import java.io.File;

import com.y3tu.tool.core.io.FileUtil;

/**
 * Web root资源访问对象
 *
 * @author looly
 */
public class WebAppResource extends FileResource {

    /**
     * 构造
     *
     * @param path 相对于Web root的路径
     */
    public WebAppResource(String path) {
        super(new File(FileUtil.getWebRoot(), path));
    }

}
