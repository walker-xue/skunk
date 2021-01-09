package ${servicePackage};

import cn.com.pcauto.shangjia.base.dto.RequestMsg;
import cn.com.pcauto.shangjia.base.exception.ErrorCodeException;
import cn.com.pcauto.shangjia.preview.entity.${entity};
import cn.com.pcauto.shangjia.preview.mapper.${entity}Mapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 描述: ${table.comment}
 * author: ${author}
 * date: ${date}
 */
@Service
public class ${entity}Service extends BaseService<${entity}Mapper,${entity}> {


    /**
     * 新增或修改${entity}
     * @return
     */
    public boolean save${entity}(RequestMsg requestMsg){
        JSONObject authInfo = requestMsg.getAuthInfo();
        JSONObject data = requestMsg.getData();
        int id = data.getIntValue("id");
		
		${entity} ${entity?uncap_first} = new ${entity}();
		<#list table.fields as field>
		   <#if !field.keyIdentityFlag>
		${entity?uncap_first}.set${field.propertyName?cap_first}(data.getString("${field.propertyName}"));
		   </#if>
		</#list>

        if(id > 0){//修改
            ${entity} pre${entity} = this.getById(id);
            if(${entity?uncap_first} == null){
                throw new ErrorCodeException(-1,
                        "save-${entity?uncap_first}-null","修改的数据不存在");
            }
            //将${entity?uncap_first}不为空的copy到pre${entity},更新${entity?uncap_first}
            BeanUtils.copyProperties(${entity?uncap_first},pre${entity},"${tbKey}");
            boolean result = this.updateById(pre${entity});//更新
            return result;
        }else{
            //新增
            return this.save(${entity?uncap_first});//新增
        }
    }


}
