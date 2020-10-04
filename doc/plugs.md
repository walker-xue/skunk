# Donkey 插件说明  
- [Donkey Core](#donkey-core)  
- [Donkey Data](#donkey-data)  
- [Donkey Interface](#donkey-interface)  
- [Donkey Explorer](#donkey-explorer)  
- [Donkey Oss](#donkey-oss)  
- [Donkey Generator](#donkey-generator)  

 

## Donkey Core

提供了一些常用的函数，比如字符串处理、日期格式化、计算日期等。并且也数据校验


## donkey-data

基于Mybatis ORM 框架 和 通用 Mapper 封装的数据持久化插件，通过使用 HikariCP 数据连接池实现高性能的数据持久化操作。并且集成了 SpringBoot 的 jdbc 插件。使用此包无须编写各插件的配置代码，底层默认实现。

## donkey-interface

通过分析以往后台管理系统，所有对资源操作分为两大类，其中一类是查询数据，另一类是修改数据。因此通用接口定义为 IDataQueryComponent（资源查询）和 IDataEditComponent（资源修改）。

## donkey-explorer

Donkey Explorer 是对Excel文件操作接口的实现，本插件通过使用 POI 对Excel文档进行读写操作的。

Java 解析与生成Excel比较有名的是Apache POI、jxl。

XSSFCell:基于内存 
SXSSFCell:基于内存+磁盘的写入方式 
HSSFCell:高水平的写入方式，对于单元格的数据类型有很高的限制，比如:字符串格式只有字符，不能有数字


POI读取Excel有两种模式，一种是用户模式，一种是SAX事件驱动模式，将xlsx格式的文档转换成CSV格式后进行读取。
用户模式API接口丰富，使用POI的API可以很容易读取Excel，但用户模式消耗的内存很大，当遇到很大sheet、大数据网格，假空行、公式等问题时，很容易导致内存溢出。
POI官方推荐解决内存溢出的方式使用CVS格式解析，即SAX事件驱动模式。下面主要是讲解如何读取大批量数据：
参考资料[POI读写大数据量Excel，解决超过几万行而导致内存溢出的问题](https://www.cnblogs.com/swordfall/p/8298386.html)

## donkey-oss

为了提供服务可用性、高性能等，把资源分解为了静态资源和动态资源，把静态资源放在文件服务器上，然后通过控制台管理资源。

目前市场上有多种"文件对象储存"产品，比如阿里、七牛等。我们实现常用的“文件对象储存”客户端工具。

## donkey-generator

Web 应用项目开发都有分层，目的是为了代码逻辑清晰和架构简单，而且这个分层模式已经成为常用的模式，所以这种模式可以生成通用的代码结构，然后开发人员在基础上改改就能快速的完成次模块功能。
