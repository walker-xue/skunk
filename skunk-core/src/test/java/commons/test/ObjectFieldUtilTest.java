package commons.test;

import org.apache.commons.lang3.reflect.FieldUtils;

import commons.test.entity.BeanA;

public class ObjectFieldUtilTest {
    public static void main(String[] args) {


        BeanA beanA = new BeanA();
        beanA.setAge(1);
        beanA.setEmail("xueyanjun@163.com");
        beanA.setName("xueyanjun");


        try {
            System.out.println(FieldUtils.readField(beanA, "email", true).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
