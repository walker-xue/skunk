package ${serviceImplPackage};

import ${entityPackage}.${entity};
import ${mapperPackage}.${entity}Mapper;
import ${servicePackage}.${entity}Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 描述: ${table.comment}
 * author: ${author}
 * date: ${date}
 */
@Service
public class ${entity}ServiceImpl extends ServiceImpl<${entity}Mapper, ${entity}> implements ${entity}Service {

}
