package com.y3tu.tool.extra.template.engine;

import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.extra.template.TemplateEngine;
import com.y3tu.tool.extra.template.TemplateConfig;
import com.y3tu.tool.extra.template.TemplateException;
import com.y3tu.tool.extra.template.engine.beetl.BeetlTemplateEngine;
import com.y3tu.tool.extra.template.engine.enjoy.EnjoyTemplateEngine;
import com.y3tu.tool.extra.template.engine.freemarker.FreemarkerTemplateEngine;
import com.y3tu.tool.extra.template.engine.rythm.RythmTemplateEngine;
import com.y3tu.tool.extra.template.engine.thymeleaf.ThymeleafTemplateEngine;
import com.y3tu.tool.extra.template.engine.velocity.VelocityTemplateEngine;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单模板工厂，用于根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
 *
 * @author looly
 */
@Slf4j
public class EngineFactory {
    /**
     * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
     *
     * @param config 模板配置，包括编码、模板文件path等信息
     * @return {@link TemplateEngine}
     */
    public static TemplateEngine create(TemplateConfig config) {
        final TemplateEngine templateEngine = doCreate(config);
        log.debug("Use [{}] TemplateEngine As Default.", StrUtil.removeSuffix(templateEngine.getClass().getSimpleName(), "TemplateEngine"));
        return templateEngine;
    }

    /**
     * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
     *
     * @param config 模板配置，包括编码、模板文件path等信息
     * @return {@link TemplateEngine}
     */
    private static TemplateEngine doCreate(TemplateConfig config) {
        try {
            return new BeetlTemplateEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new FreemarkerTemplateEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new VelocityTemplateEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new RythmTemplateEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new EnjoyTemplateEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new ThymeleafTemplateEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        throw new TemplateException("No template found ! Please add one of [Beetl,Freemarker,Velocity,Rythm] jar to your project !");
    }
}
