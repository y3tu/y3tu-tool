package com.y3tu.tool.web.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.core.lang.Platforms;
import com.y3tu.tool.setting.Props;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author y3tu
 * @date 2018/8/2
 */
@Data
@Slf4j
public class MpGenerator {

    /**
     * 代码生成路径
     */
    private static final String MAIN_PATH = Platforms.WORKING_DIR + File.separator + "target" + File.separator + "code";
    /**
     * Java代码生成地址
     */
    private static final String JAVA_PATH = MAIN_PATH + File.separator + "java";
    /**
     *
     */
    private static final String PARENT_PACKAGE = "com.y3tu.tool.web";

    /**
     * 模块名
     */
    private String moduleName;
    /**
     * 表名,多个用","分割
     */
    private String tables;
    /**
     * 作者
     */
    private String author;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;
    /**
     * 数据库连接url
     */
    private String url;
    /**
     * 父包
     */
    private String basePackage;

    /**
     * 数据库类型 mysql oracle
     */
    private String dbType;


    /**
     * 执行生成代码
     */
    public void executeCode() {
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(JAVA_PATH);
        //覆盖前面生成的文件
        gc.setFileOverride(true);
        //不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(true);
        //XML 二级缓存
        gc.setEnableCache(false);
        //XML ResultMap
        gc.setBaseResultMap(true);
        //XML columnList
        gc.setBaseColumnList(false);
        //gc.setKotlin(true) 是否生成 kotlin 代码
        gc.setAuthor(author);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sDao");
        gc.setXmlName("%sDao");
        gc.setServiceName("%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        gc.setBaseColumnList(true);
        gc.setBaseResultMap(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        switch (dbType) {
            case "mysql":
                dsc.setDbType(DbType.MYSQL);
                dsc.setDriverName("com.mysql.jdbc.Driver");
                break;
            case "oracle":
                dsc.setDbType(DbType.ORACLE);
                dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
                break;
            default:
                dsc.setDbType(DbType.MYSQL);
                dsc.setDriverName("com.mysql.jdbc.Driver");
                break;
        }
        dsc.setUsername(username);
        dsc.setPassword(password);
        dsc.setUrl(url);

        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        // 此处可以修改为您的表前缀
        strategy.setTablePrefix(new String[]{"", ""});
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表
        strategy.setInclude(tables.split(","));
        // 排除生成的表
        // strategy.setExclude(new String[]{"test"});
        // 自定义实体父类
        strategy.setSuperEntityClass(PARENT_PACKAGE + ".base.entity.BaseEntity");
        // 自定义实体，公共字段
        //strategy.setSuperEntityColumns(new String[]{"remarks", "create_by", "create_date", "update_by", "update_date", "del_flag"});
        // 自定义 mapper 父类
        strategy.setSuperMapperClass(PARENT_PACKAGE + ".base.dao.BaseMapper");
        // 自定义 service 父类
        strategy.setSuperServiceClass(PARENT_PACKAGE + ".base.service.BaseService");
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass(PARENT_PACKAGE + ".base.service.impl.BaseServiceImpl");
        // 自定义 controller 父类
        strategy.setSuperControllerClass(PARENT_PACKAGE + ".base.controller.BaseController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        //public User setName(String name) {this.name = name; return this;}
        //strategy.setEntityBuilderModel(true);
        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(basePackage);
        pc.setModuleName(moduleName);
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("basePackage", basePackage);
                map.put("moduleName", moduleName);
                this.setMap(map);
            }
        };

        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return MAIN_PATH + "/resources/mapper/" + moduleName + "/" + tableInfo.getEntityName() + ".xml";
            }
        });

        focList.add(new FileOutConfig("/templates/controller.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return JAVA_PATH + "/" + basePackage.replace(".", "/") + "/" + moduleName + "/controller/" + tableInfo.getControllerName() + ".java";
            }
        });

        focList.add(new FileOutConfig("/templates/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return JAVA_PATH + "/" + basePackage.replace(".", "/") + "/" + moduleName + "/entity/" + tableInfo.getEntityName() + ".java";
            }
        });

        focList.add(new FileOutConfig("/templates/mapper.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return JAVA_PATH + "/" + basePackage.replace(".", "/") + "/" + moduleName + "/dao/" + tableInfo.getMapperName() + ".java";
            }
        });
        focList.add(new FileOutConfig("/templates/service.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return JAVA_PATH + "/" + basePackage.replace(".", "/") + "/" + moduleName + "/service/" + tableInfo.getServiceName() + ".java";
            }
        });
        focList.add(new FileOutConfig("/templates/serviceImpl.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return JAVA_PATH + "/" + basePackage.replace(".", "/") + "/" + moduleName + "/service/impl/" + tableInfo.getServiceImplName() + ".java";
            }
        });


        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        tc.setController(null);
        tc.setMapper(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        tc.setEntity(null);

        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();
    }

    public static void main(String[] args) {
        Props props = new Props("config/generator.properties");
        String pwd = props.getProperty("password");
        pwd = JasyptUtil.decyptPwd("y3tu", pwd);
        props.setProperty("password", pwd);
        MpGenerator mpGenerator = new MpGenerator();
        props.toBean(mpGenerator, MpGenerator.class);
        mpGenerator.executeCode();
    }
}
