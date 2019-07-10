package com.y3tu.tool.extra.template.engine.velocity;

import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.extra.template.TemplateEngine;
import com.y3tu.tool.extra.template.Template;
import com.y3tu.tool.extra.template.TemplateConfig;
import org.apache.velocity.app.Velocity;

/**
 * Velocity模板引擎
 *
 * @author looly
 */
public class VelocityTemplateEngine implements TemplateEngine {

    org.apache.velocity.app.VelocityEngine engine;

    // --------------------------------------------------------------------------------- Constructor start

    /**
     * 默认构造
     */
    public VelocityTemplateEngine() {
        this(new TemplateConfig());
    }

    /**
     * 构造
     *
     * @param config 模板配置
     */
    public VelocityTemplateEngine(TemplateConfig config) {
        this(createEngine(config));
    }

    /**
     * 构造
     *
     * @param engine {@link org.apache.velocity.app.VelocityEngine}
     */
    public VelocityTemplateEngine(org.apache.velocity.app.VelocityEngine engine) {
        this.engine = engine;
    }
    // --------------------------------------------------------------------------------- Constructor end

    @Override
    public Template getTemplate(String resource) {
        String charset = engine.getProperty(Velocity.ENCODING_DEFAULT).toString();
        if (StrUtil.isNotEmpty(charset)) {
            return VelocityTemplate.wrap(engine.getTemplate(resource, charset));
        } else {
            return VelocityTemplate.wrap(engine.getTemplate(resource));
        }
    }

    /**
     * 创建引擎
     *
     * @param config 模板配置
     * @return {@link org.apache.velocity.app.VelocityEngine}
     */
    private static org.apache.velocity.app.VelocityEngine createEngine(TemplateConfig config) {
        if (null == config) {
            config = new TemplateConfig();
        }

        final org.apache.velocity.app.VelocityEngine ve = new org.apache.velocity.app.VelocityEngine();
        // 编码
        final String charsetStr = config.getCharset().toString();
        //全局编码,如果以下编码不设置它就生效
        ve.setProperty(Velocity.ENCODING_DEFAULT, charsetStr);
        ve.setProperty(Velocity.INPUT_ENCODING, charsetStr);
        ve.setProperty(Velocity.OUTPUT_ENCODING, charsetStr);
        // 使用缓存
        ve.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);

        // loader
        switch (config.getResourceMode()) {
            case CLASSPATH:
                ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
                break;
            case FILE:
                // path
                final String path = config.getPath();
                if (null != path) {
                    ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
                }
                break;
            case WEB_ROOT:
                ve.setProperty("resource.loader", "webapp");
                ve.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.servlet.WebappLoader");
                ve.setProperty("webapp.resource.loader.path", StrUtil.nullToDefault(config.getPath(), StrUtil.SLASH));
                break;
            case STRING:
                ve.setProperty("resource.loader", "string");
                ve.setProperty("string.resource.loader.class ", StringResourceLoader.class.getName());
                break;
            default:
                break;
        }

        return ve;
    }
}
