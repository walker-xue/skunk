package com.skunk.code.generate.config;

/**
 * author 张新林
 * 时间 2019/6/11 23:31
 * 描述
 */
public class GlobalConfig {

    private String system_encoding = "utf-8";//文件编码
    private String templatePath = "/template";//模板
    private String outputDir;//文件输出路径，不配置的话默认输出当前项目的resources/code目录下
    private String entityPackage = "entity";//实体包
    private String mapperPackage = "mapper";//dao层包名
    private String mapperXmlPath = "";//dao层xml路径
    private String serviceImplPackage = "service.Impl";//serviceImpl层包名
    private String servicePackage = "service";//service层包名
    private String controllerPackage = "controller";//控制层包名
    private String[] tableNames;//表名
    private String[] prefix;//表前缀
    private String author = "";//作者
    private String projectName = "";//项目名称

    public String getSystem_encoding() {
        return system_encoding;
    }

    public void setSystem_encoding(String system_encoding) {
        this.system_encoding = system_encoding;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    public void setTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String[] getPrefix() {
        return prefix;
    }

    public void setPrefix(String[] prefix) {
        this.prefix = prefix;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public String getServiceImplPackage() {
        return serviceImplPackage;
    }

    public void setServiceImplPackage(String serviceImplPackage) {
        this.serviceImplPackage = serviceImplPackage;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    public String getMapperXmlPath() {
        return mapperXmlPath;
    }

    public void setMapperXmlPath(String mapperXmlPath) {
        this.mapperXmlPath = mapperXmlPath;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
