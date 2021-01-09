package ${serviceImplPackage};

import ${entityPackage}.${entity};
import ${mapperPackage}.${entity}Mapper;
import ${servicePackage}.${entity}Service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Program: ${projectName}
 * @Description: 描述
 * @Author: ${author}
 * @Date: ${date}
 **/
@Service
public class ${entity}ServiceImpl implements ${entity}Service {

      @Autowired
      private ${entity}Mapper ${entity?uncap_first}Mapper;

    /**
     * 查询列表
     *
     * @param
     * @return
     */
     @Override
     public PageInfo<${entity}> list(Integer pageSize, Integer pageNum){
         if(pageNum != -1){
             PageHelper.startPage(pageNum,pageSize);
         }
         Example example = new Example(${entity}.class);
         example.createCriteria();
         //补充相关查询条件
//         if(){
//              example.andEqualTo(${entity}.字段,"");
//         }
         List<${entity}> list = this.${entity?uncap_first}Mapper.selectByExample(example);
         return new PageInfo<>(list);
     }

     /**
      * 查询详情
      *
      * @param ${tbKey} 主键
      * @return
      */
      @Override
      public ${entity} detail(${tbKeyType} ${tbKey}){
           return this.${entity?uncap_first}Mapper.selectByPrimaryKey(${tbKey});
      }

      /**
      * 保存
      *
      * @param
      * @return
      */
      @Override
      public Boolean save(${entity} ${entity?uncap_first}){
           //待完善
           return Boolean.TRUE;
      }
}
