package ${servicePackage};

import ${entityPackage}.${entity};
import com.github.pagehelper.PageInfo;


/**
 * @Program: ${projectName}
 * @Description: 描述
 * @Author: ${author}
 * @Date: ${date}
 **/
public interface ${entity}Service {

     /**
      * 查询列表
      *
      * @param
      * @return
      */
     PageInfo<${entity}> list(Integer pageSize, Integer pageNum);

     /**
      * 查询详情
      *
      * @param
      * @return
      */
     ${entity} detail(${tbKeyType} ${tbKey});

     /**
      * 保存
      *
      * @param
      * @return
      */
     Boolean save(${entity} ${entity?uncap_first});
}
