# Skunk
帮助Web快速构建项目，并通过插件的方式实现快速开发。

# 接口参数校验

## Web接口复合对象参数校验
Web接口可以使用Hibernate Validate框架实现入参校验。

注解方式：
```java

// 校验Bean 编写
@Setter
@Getter
public class ValBean {
    
    /**
     * Bean Validation 中内置的 constraint       
     * @Null   被注释的元素必须为 null       
     * @NotNull    被注释的元素必须不为 null       
     * @AssertTrue     被注释的元素必须为 true       
     * @AssertFalse    被注释的元素必须为 false       
     * @Min(value)     被注释的元素必须是一个数字，其值必须大于等于指定的最小值       
     * @Max(value)     被注释的元素必须是一个数字，其值必须小于等于指定的最大值       
     * @DecimalMin(value)  被注释的元素必须是一个数字，其值必须大于等于指定的最小值       
     * @DecimalMax(value)  被注释的元素必须是一个数字，其值必须小于等于指定的最大值       
     * @Size(max=, min=)   被注释的元素的大小必须在指定的范围内       
     * @Digits (integer, fraction)     被注释的元素必须是一个数字，其值必须在可接受的范围内       
     * @Past   被注释的元素必须是一个过去的日期       
     * @Future     被注释的元素必须是一个将来的日期       
     * @Pattern(regex=,flag=)  被注释的元素必须符合指定的正则表达式       
     * Hibernate Validator 附加的 constraint       
     * @NotBlank(message =)   验证字符串非null，且长度必须大于0       
     * @Email  被注释的元素必须是电子邮箱地址       
     * @Length(min=,max=)  被注释的字符串的大小必须在指定的范围内       
     * @NotEmpty   被注释的字符串的必须非空       
     * @Range(min=,max=,message=)  被注释的元素必须在合适的范围内 
     */
    private Long id;

    @Max(value=20, message="{val.age.message}")   
    private Integer age;
    
    @NotBlank(message="{username.not.null}")
    @Length(max=6, min=3, message="{username.length}")
    private String username;

    @NotBlank(message="{pwd.not.null}")
    @Pattern(regexp="/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$/", message="密码必须是6~10位数字和字母的组合")
    private String password;
    
    
    @Pattern(regexp="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message="手机号格式不正确")
    private String phone;

    @Email(message="{email.format.error}")
    private String email;
}


//  Web Controller 使用
@RequestMapping(value = "/val", method=RequestMethod.POST)
@ResponseBody
public LeeJSONResult val(@Valid @RequestBody ValBean bean, BindingResult result) throws Exception {
    
    //如果没有通过,跳转提示 
    if(result.hasErrors()){ 
        Map<String, String> map = getErrors(result);
        return LeeJSONResult.error(map);
    }
    return LeeJSONResult.ok();
}

```

编码方式：
```java
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationUtils {

	private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

	static {
		if (validator == null) {
			validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
	}

	public static <T> Set<ConstraintViolation<T>> validate(T obj) {
		return validator.validate(obj);
	}
}


//使用方式 
User user = JSON.parseObject(dataEditInfo.editInfo, User.class);
ValidationUtils.validate(user);

```

## 方法内单个字段参数校验
方法参数校验目前推荐使用commons-long3进行校验, 校验失败时会抛出 IllegalArgumentException 异常, 应用可以捕获全局异常进行处理。

实现方式：
```java
// 判断Null
Validate.notNull(loginUser, "提示错误信息");

// 判断Blank
Validate.notBlank(resourceType, "提示错误信息.");

// 判断集合不为空
Validate.notEmpty(param, "提示错误信息");

```

# 工作流
flowable


# 缓存实现
项目采用的是Spring ConcurrentMapCacheManager插件

# WebMvcConfigurer 功能
WebMvcConfigurer自定义一些Handler、intercept、ViewResolve、MessageConverter

