package ${controllerPackage};

import ${entityPackage}.${entity};
import ${servicePackage}.${entity}Service;
import com.xinhuo.demo.Constants;
import com.xinhuo.demo.common.model.PageResponseMsg;
import com.xinhuo.demo.common.model.ResponseMsg;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
//import com.xinhuo.demo.common.utils.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 描述: ${table.comment}
 * author: ${author}
 * date: ${date}
 */
@RestController
@RequestMapping(value="/${entity?uncap_first}")
public class ${entity}Controller {

    @Autowired
    private ${entity}Service ${entity?uncap_first}Service;

    @RequestMapping(value="/", method=RequestMethod.GET)
    @ResponseBody
    public PageResponseMsg view(int pageNum,${entity} ${entity?uncap_first}From){
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper();
//        if(StringUtils.isNotBlank(${entity?uncap_first}From.getName())){
//            queryWrapper.eq("name",${entity?uncap_first}From.getName());//填写查询条件
//        }
        //...，更多查询条件
        IPage<${entity}> page = ${entity?uncap_first}Service.page(new Page<>(pageNum,Constants.PAGE_SIZE),queryWrapper);

        PageResponseMsg result = new PageResponseMsg(page);

        return result;
     }
    @RequestMapping(value="/{${tbKey}}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseMsg view(@PathVariable ${tbKeyType} ${tbKey}){
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Service.getById(${tbKey});
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setData(${entity?uncap_first});
        return responseMsg;
    }

    @RequestMapping(value="/",method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg add(@RequestBody ${entity} ${entity?uncap_first}){
        ResponseMsg responseMsg = new ResponseMsg();
        boolean result = ${entity?uncap_first}Service.save(${entity?uncap_first});
        if(result){
            responseMsg.setMessage("保存成功");
        }else{
            responseMsg.setCode(-1);
            responseMsg.setMessage("保存失败");
        }

        return responseMsg;
    }

    @RequestMapping(value="/{${tbKey}}",method = RequestMethod.PUT)
    @ResponseBody
    public ResponseMsg update(@RequestBody ${entity} ${entity?uncap_first})throws Exception{
        ResponseMsg responseMsg = null;

        ${entity} pre${entity} = ${entity?uncap_first}Service.getById(${entity?uncap_first}.get${tbKey?cap_first}());
        if(pre${entity} != null){
            //将${entity?uncap_first}不为空的copy到pre${entity},更新${entity?uncap_first}
            //MyBeanUtils.copyProperties(${entity?uncap_first},pre${entity});
            boolean result = ${entity?uncap_first}Service.updateById(${entity?uncap_first});
            if(result){
                responseMsg.setMessage("保存成功");
            }else{
                responseMsg = new ResponseMsg(-1,"保存失败");
            }

        }else{
            responseMsg = new ResponseMsg(-1,"该数据不存在");
        }

        return responseMsg;
    }

    @ResponseBody
    @RequestMapping(value="/{${tbKey}}", method=RequestMethod.DELETE)
    public ResponseMsg delete(@PathVariable ${tbKeyType} ${tbKey}){
        ResponseMsg responseObject = null;
<#if tbKeyType=="Integer">
        if(${tbKey} > 0){
<#else>
        if(StringUtils.isNotBlank(${tbKey})){
</#if>
            boolean result = ${entity?uncap_first}Service.removeById(${tbKey});
            if(result){
                responseObject = new ResponseMsg(0,"删除成功");
            }else{
                responseObject = new ResponseMsg(-1,"删除失败");
            }

        }else{
            responseObject = new ResponseMsg(-1,"参数错误");
        }
        return responseObject;
    }



}
