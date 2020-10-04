package commons.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cglib.beans.BeanCopier;

import commons.test.entity.Bean2;
import commons.test.entity.BeanA;

public class ObjectUtilTest {
    public static void main(String[] args) {

        List<BeanA> list = new ArrayList<>();

        BeanA beanA = new BeanA();
        beanA.setAge(1);
        beanA.setEmail("xueyanjun@163.com");
        beanA.setName("xueyanjun");

        list.add(beanA);

        BeanA beanB = new BeanA();
        beanB.setAge(11);
        beanB.setEmail("xueyanjun2@163.com");
        beanB.setName("xueyanjun2");
        list.add(beanB);

        long startTime = System.currentTimeMillis();
        List<Bean2> serviceTrees = beanCopyPropertiesForList(list, Bean2.class);
        System.out.println(serviceTrees);
        System.out.println(System.currentTimeMillis() - startTime);
    }

    /**
     * 将List中的对象拷贝到目标对象的List中(标准Bean)
     *
     * @param sourceList 源List<T>
     * @param targetCls  目标对象类型
     * @param <T>        源类型
     * @param <R>        目标类型
     * @return 目标类型List数组
     */
    public static <T, R> List<R> beanCopyPropertiesForList(List<T> sourceList, Class<R> targetCls) {
        List<R> targetList = new ArrayList<R>();
        if (sourceList != null && !sourceList.isEmpty()) {
            for (T source : sourceList) {
                targetList.add(beanCopyProperties(source, targetCls));
            }
        }
        return targetList;
    }

    /**
     * 属性值拷贝(标准Bean)
     *
     * @param source    源对象
     * @param targetCls 目标对象类
     * @Return 拷贝目标类的实体
     */
    public static <R> R beanCopyProperties(Object source, Class<R> targetCls) {
        try {
            BeanCopier copier = BeanCopier.create(source.getClass(), targetCls, false);
            R target = targetCls.getDeclaredConstructor().newInstance();
            if (source != null) {
                //BeanUtils.copyProperties(target, source);
                copier.copy(source, target, null);
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
