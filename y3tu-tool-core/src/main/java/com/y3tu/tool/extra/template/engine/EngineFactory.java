package com.y3tu.tool.extra.template.engine;

import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.extra.template.Engine;
import com.y3tu.tool.extra.template.TemplateConfig;
import com.y3tu.tool.extra.template.TemplateException;
import com.y3tu.tool.extra.template.engine.beetl.BeetlEngine;
import com.y3tu.tool.extra.template.engine.enjoy.EnjoyEngine;
import com.y3tu.tool.extra.template.engine.freemarker.FreemarkerEngine;
import com.y3tu.tool.extra.template.engine.rythm.RythmEngine;
import com.y3tu.tool.extra.template.engine.thymeleaf.ThymeleafEngine;
import com.y3tu.tool.extra.template.engine.velocity.VelocityEngine;
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
     * @return {@link Engine}
     */
    public static Engine create(TemplateConfig config) {
        final Engine engine = doCreate(config);
        log.debug("Use [{}] Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
        return engine;
    }

    /**
     * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
     *
     * @param config 模板配置，包括编码、模板文件path等信息
     * @return {@link Engine}
     */
    private static Engine doCreate(TemplateConfig config) {
        try {
            return new BeetlEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new FreemarkerEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new VelocityEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new RythmEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new EnjoyEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        try {
            return new ThymeleafEngine(config);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        throw new TemplateException("No template found ! Please add one of [Beetl,Freemarker,Velocity,Rythm] jar to your project !");
    }
}
