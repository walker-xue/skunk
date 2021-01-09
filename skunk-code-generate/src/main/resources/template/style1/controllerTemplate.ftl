package ${controllerPackage};

import ${entityPackage}.${entity};
import ${servicePackage}.${entity}Service;
import cn.com.pcauto.shangjia.base.exception.ErrorCodeException;
import cn.com.pcauto.shangjia.base.dto.RequestMsg;
import cn.com.pcauto.shangjia.base.dto.ResultMsg;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
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

     /**
     * 获取列表
     * @param requestMsg
     * @return
     */
    @PostMapping("/list")
    public ResultMsg list(@RequestBody RequestMsg requestMsg){
        ResultMsg res = new ResultMsg(0, "success");
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper();
//        JSONObject query = res.getData().getJSONObeject("query");
//        if(StringUtils.isNotBlank(${entity?uncap_first}From.getName())){
//            queryWrapper.eq("name",${entity?uncap_first}From.getName());//填写查询条件
//        }
        //...，更多查询条件
        int pageNo = requestMsg.getData().getIntValue("_pageNo");
        int pageSize = requestMsg.getData().getIntValue("_pageSize");
        if(pageSize<=0 || pageSize>200){
            pageSize=20;//默认一次查询20条数据
        }
        IPage<${entity}> page = ${entity?uncap_first}Service.page(new Page<>(pageNo,pageSize),queryWrapper);

        res.pubData("list",page.getRecords());
        res.pubData("_pageNo",page.getCurrent());
        res.pubData("_total",page.getTotal());
        res.pubData("_totalPage", (page.getTotal()-1)/page.getSize()+1);
        return res;
    }

    /**
     * 保存数据
     * @param requestMsg
     * @return
     */
    @PostMapping("/save")
    public ResultMsg save(@RequestBody RequestMsg requestMsg){

        boolean result = ${entity?uncap_first}Service.save${entity}(requestMsg);
        if(result){
            return new ResultMsg(0,"success");
        }else{
            throw new ErrorCodeException(-1,
                    "save-${entity?uncap_first}-error","保存失败");
        }
    }

    /**
     * 删除数据
     * @param requestMsg
     * @return
     */
    @PostMapping("/delete")
    public ResultMsg delete(@RequestBody RequestMsg requestMsg){
        int id = requestMsg.getData().getIntValue("id");
        boolean result = ${entity?uncap_first}Service.removeById(id);
        if(result){
            return new ResultMsg(0,"success");
        }else{
            throw new ErrorCodeException(-1,
                    "delete-${entity?uncap_first}-error","删除失败");
        }
    }

    @PostMapping("/get")
    public ResultMsg get(@RequestBody RequestMsg requestMsg){
        ResultMsg resultMsg =  new ResultMsg(0,"success");
        int id = requestMsg.getData().getIntValue("id");
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Service.getById(id);
        resultMsg.setData(BeanMap.create(${entity?uncap_first}));
        return resultMsg;
    }


}
