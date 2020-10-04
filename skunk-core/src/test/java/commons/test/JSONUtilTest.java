package commons.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class JSONUtilTest {
    public static void main(String[] args) {

        List<String> ss = new ArrayList<>();
        ss.add("2");

        String aj = JSON.toJSONString(ss);


        System.out.print(aj);
        List<String> list = JSON.parseArray(aj, String.class);


        list.forEach(e -> {
            System.out.print(e);
        });
    }


}
