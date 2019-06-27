package com.y3tu.tool.extra.template.engine.rythm;

import com.y3tu.tool.extra.template.TemplateEngine;
import com.y3tu.tool.extra.template.Template;
import com.y3tu.tool.extra.template.TemplateConfig;

import java.util.Properties;

/**
 * Rythm模板引擎<br>
 * 文档：http://rythmengine.org/doc/index
 *
 * @author looly
 */
public class RythmTemplateEngine implements TemplateEngine {

    org.rythmengine.RythmEngine engine;

    // --------------------------------------------------------------------------------- Constructor start

    /**
     * 默认构造
     */
    public RythmTemplateEngine() {
        this(new TemplateConfig());
    }

    /**
     * 构造
     *
     * @param config 模板配置
     */
    public RythmTemplateEngine(TemplateConfig config) {
        this(createEngine(config));
    }

    /**
     * 构造
     *
     * @param engine {@link org.rythmengine.RythmEngine}
     */
    public RythmTemplateEngine(org.rythmengine.RythmEngine engine) {
        this.engine = engine;
    }
    // --------------------------------------------------------------------------------- Constructor end

    @Override
    public Template getTemplate(String resource) {
        return RythmTemplate.wrap(engine.getTemplate(resource));
    }

    /**
     * 创建引擎
     *
     * @param config 模板配置
     * @return {@link org.rythmengine.RythmEngine}
     */
    private static org.rythmengine.RythmEngine createEngine(TemplateConfig config) {
        if (null == config) {
            config = new TemplateConfig();
        }

        final Properties props = new Properties();
        final String path = config.getPath();
        if (null != path) {
            props.put("home.template", path);
        }

        final org.rythmengine.RythmEngine engine = new org.rythmengine.RythmEngine(props);
        return engine;
    }
}
