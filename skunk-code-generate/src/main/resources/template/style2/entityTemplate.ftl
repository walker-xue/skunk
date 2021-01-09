package ${entityPackage};

<#list table.fields as field>
<#if field.propertyType?index_of("BigDecimal")!=-1>
<#assign importBigDecimal=true/>
</#if>
<#if field.propertyType?index_of("Date")!=-1>
<#assign importDate=true/>
</#if>
</#list>
<#if importBigDecimal?exists>
import java.math.BigDecimal;
</#if>
<#if importDate?exists>
import java.util.Date;
</#if>
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: ${table.comment}
 * @author: ${author}
 * @Date: ${date}
 */
@Data
@Accessors(chain = true)
@Table(name = "${table.name}")
public class ${entity} {
<#-- 循环属性名称 -->
<#list table.fields as field>
    <#if field.comment??>
    /**
     * <#if field.comment!="">${field.comment}<#else >主键</#if>
     */
    </#if>
    <#if field.keyIdentityFlag>
    @Id
    @Column(name = "${field.name}")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    <#else>
    @Column(name = "${field.name}")
    </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>

<#list table.fields as field>
    public static final String ${field.name?upper_case} = "${field.propertyName}";

    public static final String DB_${field.name?upper_case} = "${field.name}";

</#list>
}
