package com.y3tu.tool.extra.template.engine.thymeleaf;

import com.y3tu.tool.core.io.IoUtil;
import com.y3tu.tool.core.util.CharsetUtil;
import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.extra.template.AbstractTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

/**
 * Thymeleaf模板实现
 *
 * @author looly
 */
public class ThymeleafTemplate extends AbstractTemplate implements Serializable {
    private static final long serialVersionUID = 781284916568562509L;

    private TemplateEngine engine;
    private String template;
    private Charset charset;

    /**
     * 包装Thymeleaf模板
     *
     * @param engine   Thymeleaf的模板引擎对象 {@link TemplateEngine}
     * @param template 模板路径或模板内容
     * @param charset  编码
     * @return {@link ThymeleafTemplate}
     */
    public static ThymeleafTemplate wrap(TemplateEngine engine, String template, Charset charset) {
        return (null == engine) ? null : new ThymeleafTemplate(engine, template, charset);
    }

    /**
     * 构造
     *
     * @param engine   Thymeleaf的模板对象 {@link TemplateEngine}
     * @param template 模板路径或模板内容
     * @param charset  编码
     */
    public ThymeleafTemplate(TemplateEngine engine, String template, Charset charset) {
        this.engine = engine;
        this.template = template;
        this.charset = ObjectUtil.defaultIfNull(charset, CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    public void render(Map<String, Object> bindingMap, Writer writer) {
        final Context context = new Context(Locale.getDefault(), bindingMap);
        this.engine.process(this.template, context, writer);
    }

    @Override
    public void render(Map<String, Object> bindingMap, OutputStream out) {
        render(bindingMap, IoUtil.getWriter(out, this.charset));
    }

}
