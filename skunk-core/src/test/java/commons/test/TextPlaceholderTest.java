package commons.test;

import java.util.HashMap;
import java.util.Map;


/**
 * 占位符替换
 */
public class TextPlaceholderTest {

    /**
     * 测试
     */
    public static void test() {
        //替换字符串中的占位符
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("user", "admin");
        params.put("password", "123456");
        params.put("system-version", "windows 10");
        params.put("版本", "version");//中文也可以
        params.put("详", "翔");//中文也可以
    }

}