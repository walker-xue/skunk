# Maven Release Plugin

## mvn release:prepare
```shell

mvn release:prepare

# 这条命令主要做的是：
# a.把你项目打一个release版本
# b.在git的tag中打一个tag
# c.自动升级SNAPSHOT 并提交更新后的pom文件到git

```

## mvn release:perform
```shell
 
 mvn release:perform
 
# 这条命令主要做的是：
# a.去git的tag上拿代码
# b.用tag上的代码，打一个release版的包
# c.deploy上你的maven私服
```

# 推送项目至 Maven 中央仓库

```shell
mvn clean deploy -Dmaven.test.skip=true  -Prelease -Dgpg.passphrase
```
