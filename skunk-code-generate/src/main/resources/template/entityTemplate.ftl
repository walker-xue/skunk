package ${entityPackage};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;

/**
 * 描述: ${table.comment}
 * author: ${author}
 * date: ${date}
 */
@TableName("${table.name}")
public class ${entity} implements Serializable {


    private static final long serialVersionUID = 1L;
<#-- 循环属性名称 -->
<#list table.fields as field>
<#if field.comment??>
    /**
     * ${field.comment}
     */
</#if>
<#if field.keyIdentityFlag>
    @TableId(value="${field.name}", type= IdType.AUTO)
</#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
<#-- 循环set/get方法 -->
<#list table.fields as field>
<#if field.propertyType == "Boolean">
<#assign getprefix="is"/>
<#else>
<#assign getprefix="get"/>
</#if>
    public ${field.propertyType} ${getprefix}${field.capitalName}() {
	return ${field.propertyName};
    }

    public void set${field.propertyName?cap_first}(${field.propertyType} ${field.propertyName}) {
        this.${field.propertyName} = ${field.propertyName};
    }
</#list>
}