# code-generate

#### Description
简单java代码生成器，生成springboot+mybatis-plus的增删查改的基本代码（开发利器，附源码）

我觉得有一个这样的工具，可以节约不少时间，提升开发效率。针对单表的增删查改会很快


在上篇<a href="https://blog.csdn.net/qq_21187515/article/details/92182434">简单java代码生成器的开发教程(一)</a>讲了代码生成器的基本流程，生成引擎+生成模板，这篇在上一篇的基础上进行拓展，使得代码生成器不仅仅生成实体类bean,还可以生成该实体类相关的增删查改的接口

问题：不同的项目，如何进行兼容，写一套公用的模板？
我觉得不能，不同项目，不同的架构，肯定是不行的，但是我们可以针对特定项目，把通用的部分总结成模板，不同的项目就用不同的模板

我这里搭建了一套springboot+mybatis-plus的demo项目，针对这个项目写了一个模板


1. 首先，在代码生成引擎里面新增支持生成controllerTemplate、serviceTemplate、serviceImplTemplate、mapperTemplate
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190617202424132.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzIxMTg3NTE1,size_16,color_FFFFFF,t_70)
2.  编写相关的模板
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190617203029279.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzIxMTg3NTE1,size_16,color_FFFFFF,t_70)
3. 代码生成器的使用
在com.xinlin.code.Demo中的main有使用demo

```java
    public static void main(String[] args)
    {
        GlobalConfig globalConfig = new GlobalConfig();//全局配置
        globalConfig.setAuthor("新林");
        globalConfig.setEntityPackage("com.xinhuo.demo.model");//实体包名
        globalConfig.setMapperPackage("com.xinhuo.demo.dao");//dao包名
        globalConfig.setServicePackage("com.xinhuo.demo.service");
        globalConfig.setServiceImplPackage("com.xinhuo.demo.service.impl");
        globalConfig.setControllerPackage("com.xinhuo.demo.controller");
        globalConfig.setTableNames(new String[]{"pre_user","pre_student"});//需要生成的实体
        globalConfig.setPrefix(new String[]{"pre_"});//生成的实体移除前缀
//        globalConfig.setOutputDir("D://code/");//文件输出路径，不配置的话默认输出当前项目的resources/code目录下

        DataSourceConfig dsc = new DataSourceConfig();//数据库配置
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://192.168.33.76:3306/test?useUnicode=true&amp;characterEncoding=UTF-8&amp;&useSSL=false");
        dsc.setUsername("root");//填写自己的数据库账号
        dsc.setPassword("root");//填写自己的数据库密码
        CodeGenerate codeGenerate = new CodeGenerate(globalConfig,dsc);
        //生成代码
        codeGenerate.generateToFile();
    }
```

> 注：
> 可以自己根据需要修改模板，也可以自己新增一套模板，把template目录下的模板复制出来放到resources目录下其他的路径，修改即可，然后用globalConfig.setTemplatepath设置自定义模板路径

<br>
<h3>具体使用流程</h3>
1. 我先设计好表（比如我现在有几个权限相关的基础表）

![在这里插入图片描述](https://img-blog.csdnimg.cn/201906172051232.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzIxMTg3NTE1,size_16,color_FFFFFF,t_70)
2. 修改com.xinlin.code.Demo的相关配置，执行main方法
   可以看到快速生成了相关的代码文件
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190617205602715.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzIxMTg3NTE1,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190617210254691.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzIxMTg3NTE1,size_16,color_FFFFFF,t_70)
3.把相关的代码复制到我们demo项目的相关目录下即可