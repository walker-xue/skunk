package com.skunk.code;

import com.skunk.code.generate.CodeGenerate;
import com.skunk.code.generate.config.DataSourceConfig;
import com.skunk.code.generate.config.GlobalConfig;

/**
 * @Auther: zhangxinlin
 * @Date: 2019/6/17 20:20:35
 * @Description:
 */
public class Demo {

    public static void main(String[] args) {
        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //自定义模板路径
        globalConfig.setTemplatePath("/template/style2");
        globalConfig.setAuthor("ZhangXinLin");
        globalConfig.setProjectName("bm-logistics-oa-parent");
        //实体包名
        globalConfig.setEntityPackage("com.xinhuo.demo.model");
        //mapper包名
        globalConfig.setMapperPackage("com.xinhuo.demo.mapper");
        //mapper的xml路径
        globalConfig.setMapperXmlPath("mapper");
        //service包名
        globalConfig.setServicePackage("com.xinhuo.demo.service");
        globalConfig.setServiceImplPackage("com.xinhuo.demo.service.impl");
        globalConfig.setControllerPackage("com.xinhuo.demo.controller");
        //需要生成的实体
        //        globalConfig.setTableNames(new String[]{"diary", "upload_file","user"});
        //需要生成的实体
        globalConfig.setTableNames(new String[] { "group_purchase_club_goods" });
        //生成的实体移除前缀
        globalConfig.setPrefix(new String[] { "group_purchase_" });
        //文件输出路径，不配置的话默认输出当前项目的resources/code目录下
        //        globalConfig.setOutputDir("D://code/");
        //文件输出路径，不配置的话默认输出当前项目的resources/code目录下
        //        globalConfig.setOutputDir("D://WorkSpace/IDEA/diary/src/main/java/");

        //数据库配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=UTF-8&amp;&useSSL=false");
        //填写自己的数据库账号
        dsc.setUsername("root");
        //填写自己的数据库密码
        dsc.setPassword("root");
        CodeGenerate codeGenerate = new CodeGenerate(globalConfig, dsc);
        //生成代码
        codeGenerate.generateToFile();
    }
}
