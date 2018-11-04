package com.y3tu.tool.extra.template;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.io.IoUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;

/**
 * 抽象模板，提供将模板融合后写出到文件、返回字符串等方法
 *
 * @author looly
 */
public abstract class AbstractTemplate implements Template {

    @Override
    public void render(Map<String, Object> bindingMap, File file) {
        BufferedOutputStream out = null;
        try {
            out = FileUtil.getOutputStream(file);
            this.render(bindingMap, out);
        } finally {
            IoUtil.close(out);
        }
    }

    @Override
    public String render(Map<String, Object> bindingMap) {
        final StringWriter writer = new StringWriter();
        render(bindingMap, writer);
        return writer.toString();
    }
}
