package com.y3tu.tool.extra.template.engine.enjoy;

import com.jfinal.template.source.FileSourceFactory;
import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.extra.template.TemplateEngine;
import com.y3tu.tool.extra.template.Template;
import com.y3tu.tool.extra.template.TemplateConfig;
import com.y3tu.tool.extra.template.TemplateConfig.ResourceMode;
import org.beetl.core.GroupTemplate;

/**
 * Enjoy库的引擎包装
 *
 * @author looly
 */
public class EnjoyTemplateEngine implements TemplateEngine {

    private com.jfinal.template.Engine engine;
    private ResourceMode resourceMode;

    // --------------------------------------------------------------------------------- Constructor start

    /**
     * 默认构造
     */
    public EnjoyTemplateEngine() {
        this(new TemplateConfig());
    }

    /**
     * 构造
     *
     * @param config 模板配置
     */
    public EnjoyTemplateEngine(TemplateConfig config) {
        this(createEngine(config));
        this.resourceMode = config.getResourceMode();
    }

    /**
     * 构造
     *
     * @param engine {@link com.jfinal.template.Engine}
     */
    public EnjoyTemplateEngine(com.jfinal.template.Engine engine) {
        this.engine = engine;
    }
    // --------------------------------------------------------------------------------- Constructor end

    @Override
    public Template getTemplate(String resource) {
        if (ObjectUtil.equal(ResourceMode.STRING, this.resourceMode)) {
            return EnjoyTemplate.wrap(this.engine.getTemplateByString(resource));
        }
        return EnjoyTemplate.wrap(this.engine.getTemplate(resource));
    }

    /**
     * 创建引擎
     *
     * @param config 模板配置
     * @return {@link GroupTemplate}
     */
    private static com.jfinal.template.Engine createEngine(TemplateConfig config) {
        Assert.notNull(config, "Template config is null !");
        final com.jfinal.template.Engine engine = com.jfinal.template.Engine.create("Hutool-Enjoy-TemplateEngine");
        engine.setEncoding(config.getCharset().toString());

        switch (config.getResourceMode()) {
            case CLASSPATH:
                engine.setToClassPathSourceFactory();
                engine.setBaseTemplatePath(null);
                break;
            case FILE:
                engine.setSourceFactory(new FileSourceFactory());
                break;
            default:
                break;
        }

        return engine;
    }
}
