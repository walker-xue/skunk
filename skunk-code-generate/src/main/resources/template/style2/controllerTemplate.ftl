package ${controllerPackage};

import ${entityPackage}.${entity};
import ${servicePackage}.${entity}Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.com.bluemoon.common.web.response.ResponseBean;

import java.util.HashMap;

/**
 * @Program: ${projectName}
 * @Description: 描述
 * @Author: ${author}
 * @Date: ${date}
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value="/${entity?uncap_first}")
public class ${entity}Controller {

    @Autowired
    private ${entity}Service ${entity?uncap_first}Service;

    /**
     * 列表
     * @param json
     * @return
     */
    @PostMapping("/list")
    public ResponseBean list(@RequestBody JSONObject json){
        log.info(" --- >> 列表查询：条件 -- {}", json.toJSONString());
        Integer pageNum = json.containsKey("pageNum") ? json.getInteger("pageNum") : 0;
        Integer pageSize = json.containsKey("pageSize") ? json.getInteger("pageSize") : 15;

        PageInfo<${entity}> pageInfo = ${entity?uncap_first}Service.list(pageSize,pageNum);

        HashMap<String, Object> data = new HashMap<>(2);
        data.put("total",pageInfo.getTotal());
        data.put("list",pageInfo.getList());
        ResponseBean responseBean = ResponseBean.success();
        responseBean.setData(data);
        return responseBean;
    }

    /**
     * 详情
     * @param json
     * @return
     */
    @PostMapping(value = "/detail")
    public ResponseBean detail(@RequestBody JSONObject json){

        ${tbKeyType} ${tbKey} = json.get${tbKeyType}("${tbKey}");
        log.info("--- 查询详情 ${tbKey} = {}" , ${tbKey});
        ${entity} vo = ${entity?uncap_first}Service.detail(${tbKey});
        ResponseBean responseBean = ResponseBean.success();
        responseBean.setData(vo);
        return responseBean;
    }


}
