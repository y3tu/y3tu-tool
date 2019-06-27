package com.y3tu.tool.extra.template.engine.freemarker;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.io.IORuntimeException;
import com.y3tu.tool.core.util.ClassUtil;
import com.y3tu.tool.extra.template.TemplateEngine;
import com.y3tu.tool.extra.template.Template;
import com.y3tu.tool.extra.template.TemplateConfig;
import com.y3tu.tool.extra.template.TemplateException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

import java.io.IOException;

/**
 * Beetl模板引擎封装
 *
 * @author looly
 */
public class FreemarkerTemplateEngine implements TemplateEngine {

    Configuration cfg;

    // --------------------------------------------------------------------------------- Constructor start

    /**
     * 默认构造
     */
    public FreemarkerTemplateEngine() {
        this(new TemplateConfig());
    }

    /**
     * 构造
     *
     * @param config 模板配置
     */
    public FreemarkerTemplateEngine(TemplateConfig config) {
        this(createCfg(config));
    }

    /**
     * 构造
     *
     * @param freemarkerCfg {@link Configuration}
     */
    public FreemarkerTemplateEngine(Configuration freemarkerCfg) {
        this.cfg = freemarkerCfg;
    }
    // --------------------------------------------------------------------------------- Constructor end

    @Override
    public Template getTemplate(String resource) {
        try {
            return FreemarkerTemplate.wrap(this.cfg.getTemplate(resource));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }

    /**
     * 创建配置项
     *
     * @param config 模板配置
     * @return {@link Configuration }
     */
    private static Configuration createCfg(TemplateConfig config) {
        if (null == config) {
            config = new TemplateConfig();
        }

        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setDefaultEncoding(config.getCharset().toString());

        switch (config.getResourceMode()) {
            case CLASSPATH:
                cfg.setTemplateLoader(new ClassTemplateLoader(ClassUtil.getClassLoader(), config.getPath()));
                break;
            case FILE:
                try {
                    cfg.setTemplateLoader(new FileTemplateLoader(FileUtil.file(config.getPath())));
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
                break;
            case WEB_ROOT:
                // cfg.setTemplateLoader(new WebappTemplateLoader(null, config.getPath()));
                break;
            case STRING:
                cfg.setTemplateLoader(new StringTemplateLoader());
                break;
            default:
                break;
        }

        return cfg;
    }
}
