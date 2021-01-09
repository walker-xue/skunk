/**
 * @author 张新林
 * 时间 2017年7月7日下午2:43:31
 * 包名 com.xinhuo.codegenerator.generator
 * 类名 CodeFactory
 */
package com.skunk.code.generate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.skunk.code.generate.config.GlobalConfig;
import com.skunk.core.file.FileUtils;
import com.skunk.core.utils.String2Utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author walker
 */
public class CodeGenerateFactory {

    private ICallBack callBack;
    private GlobalConfig globalConfig;
    public static String getProjectPath() {
        return System.getProperty("user.dir").replace("\\", "/") + "/";
    }
    /**
     * 把配置数据注入模版，生成代码文件
     *
     * @param templateFileName
     * @param type
     * @param data
     * @throws TemplateException
     * @throws IOException
     */
    public void generateFile(String templateFileName, String type, Map data) throws IOException {

        String entityName = data.get("entity").toString();
        String genFilePath = getCodePath(type, entityName);//获取生成的文件路径
        File genFile = new File(genFilePath);
        Template template = getConfiguration().getTemplate(templateFileName);//获取模版信息
        FileUtils.mkDirs(new File(genFile.getParent()));
        Writer out = new OutputStreamWriter(
            new FileOutputStream(genFilePath), globalConfig.getSystem_encoding());//生成的文件编码
        try {
            template.process(data, out);//结合模版生成代码文件
            out.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

    }
    public Configuration getConfiguration() {
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setClassForTemplateLoading(super.getClass(), globalConfig.getTemplatePath());
        cfg.setLocale(Locale.CHINA);
        cfg.setDefaultEncoding("UTF-8");
        return cfg;
    }
    public String getClassPath() {
        return Thread.currentThread().getContextClassLoader().getResource("./").getPath();
    }

    /**
     * 获取代码生成的文件路径
     *
     * @param type
     * @param entityName
     * @return
     */
    public String getCodePath(String type, String entityName) {
        StringBuilder path = new StringBuilder();
        if (String2Utils.isNotBlank(type)) {
            String codeType = Enum.valueOf(CodeType.class, type).toString();

            //开头，项目路径
            if (String2Utils.isEmpty(this.globalConfig.getOutputDir())) {
                String projectPath = getProjectPath();//没有设置outputDir的话默认用当前项目resources/code路径下
                path.append(projectPath + "src/main/resources/code");//项目名
            } else {
                path.append(this.globalConfig.getOutputDir());//项目名
            }
            if ("entity".equals(codeType)) {
                //包名 package.path
                path.append("/java/").append(globalConfig.getEntityPackage()).append("/");
                //文件名
                path.append(entityName).append(".java");
            } else if ("mapper".equals(codeType)) {
                //包名 package.path
                path.append("/java/").append(globalConfig.getMapperPackage()).append("/");
                //文件名
                path.append(entityName).append("Mapper").append(".java");
            } else if ("mapperXml".equals(codeType)) {
                //包名 package.path
                path.append("/resources/").append(globalConfig.getMapperXmlPath());
                path.append("/");
                //文件名
                path.append(entityName).append("Mapper").append(".xml");
            } else if ("service".equals(codeType)) {
                //包名 package.path
                path.append("/java/").append(globalConfig.getServicePackage()).append("/");
                //文件名
                path.append(entityName).append("Service").append(".java");
            } else if ("serviceImpl".equals(codeType)) {
                //包名 package.path
                path.append("/java/").append(globalConfig.getServiceImplPackage()).append("/");
                //文件名
                path.append(entityName).append("ServiceImpl").append(".java");
            } else if ("controller".equals(codeType)) {
                //包名 package.path
                path.append("/java/").append(globalConfig.getControllerPackage()).append("/");
                //文件名
                path.append(entityName).append("Controller").append(".java");
            } else {
                throw new IllegalArgumentException("type is not found");
                //其他类型文件生成
            }

        } else {
            throw new IllegalArgumentException("type is null");
        }
        return path.toString();
    }

    public void invoke(String templateFileName, String type) throws Exception {
        Map data = new HashMap();
        data = this.callBack.execute();
        generateFile(templateFileName, type, data);
    }

    public ICallBack getCallBack() {
        return this.callBack;
    }

    public void setCallBack(ICallBack callBack) {
        this.callBack = callBack;
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public enum CodeType {
        controller, service, serviceImpl, mapper, mapperXml, entity;

        private String type;

        public String getValue() {
            return this.type;
        }

    }
}
