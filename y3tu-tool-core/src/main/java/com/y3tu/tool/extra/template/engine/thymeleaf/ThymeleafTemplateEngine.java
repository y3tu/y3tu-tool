package com.y3tu.tool.extra.template.engine.thymeleaf;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.extra.template.TemplateEngine;
import com.y3tu.tool.extra.template.Template;
import com.y3tu.tool.extra.template.TemplateConfig;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.*;

/**
 * Thymeleaf模板引擎实现
 *
 * @author looly
 */
public class ThymeleafTemplateEngine implements TemplateEngine {

    org.thymeleaf.TemplateEngine templateEngine;
    TemplateConfig config;

    // --------------------------------------------------------------------------------- Constructor start

    /**
     * 默认构造
     */
    public ThymeleafTemplateEngine() {
        this(new TemplateConfig());
    }

    /**
     * 构造
     *
     * @param config 模板配置
     */
    public ThymeleafTemplateEngine(TemplateConfig config) {
        this(createEngine(config));
        this.config = config;
    }

    /**
     * 构造
     *
     * @param templateEngine {@link org.thymeleaf.TemplateEngine}
     */
    public ThymeleafTemplateEngine(org.thymeleaf.TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    // --------------------------------------------------------------------------------- Constructor end

    @Override
    public Template getTemplate(String resource) {
        return ThymeleafTemplate.wrap(this.templateEngine, resource, (null == this.config) ? null : this.config.getCharset());
    }

    /**
     * 创建引擎
     *
     * @param config 模板配置
     * @return {@link org.thymeleaf.TemplateEngine}
     */
    private static org.thymeleaf.TemplateEngine createEngine(TemplateConfig config) {
        if (null == config) {
            config = new TemplateConfig();
        }

        ITemplateResolver resolver = null;
        switch (config.getResourceMode()) {
            case CLASSPATH:
                final ClassLoaderTemplateResolver classLoaderResolver = new ClassLoaderTemplateResolver();
                classLoaderResolver.setCharacterEncoding(config.getCharsetStr());
                classLoaderResolver.setTemplateMode(TemplateMode.HTML);
                resolver = classLoaderResolver;
                break;
            case FILE:
                final FileTemplateResolver fileResolver = new FileTemplateResolver();
                fileResolver.setCharacterEncoding(config.getCharsetStr());
                fileResolver.setTemplateMode(TemplateMode.HTML);
                resolver = fileResolver;
                break;
            case WEB_ROOT:
                final FileTemplateResolver webRootResolver = new FileTemplateResolver();
                webRootResolver.setCharacterEncoding(config.getCharsetStr());
                webRootResolver.setTemplateMode(TemplateMode.HTML);
                webRootResolver.setPrefix(FileUtil.getWebRoot().getAbsolutePath());
                resolver = webRootResolver;
                break;
            case STRING:
                resolver = new StringTemplateResolver();
                break;
            case COMPOSITE:
                resolver = new DefaultTemplateResolver();
                break;
            default:
                resolver = new DefaultTemplateResolver();
                break;
        }

        final org.thymeleaf.TemplateEngine templateEngine = new org.thymeleaf.TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }
}
