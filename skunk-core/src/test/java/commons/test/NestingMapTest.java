package commons.test;

import java.util.HashMap;
import java.util.Map;

/**
 * 嵌套map遍历
 */
public class NestingMapTest {

    @SuppressWarnings("serial")
    public static void main(String[] args) {
        Map<String, Object> obj = new HashMap<>();

        obj.put("1", "1111");
        obj.put("2", new HashMap<String, Object>() {{
            put("21", "1111");
            put("22", "2222");
            put("23", "3333");
            put("24", new HashMap<String, Object>() {{
                put("241", "1111");
                put("242", "2222");
                put("243", "3333");
                put("244", new HashMap<String, Object>() {{
                    put("2441", "1111");
                    put("2442", "2222");
                    put("2443", "3333");
                    put("2444", "4444");
                }});
            }});
        }});

        Map<String, Object> extract = traverseMap(obj);
        System.out.println(extract);
    }


    private static Map<String, Object> traverseMap(Map<String, Object> obj) {
        Map<String, Object> extract = new HashMap<>();
        obj.forEach((key, value) -> {
            extract.put(key, value);
            System.out.println(value);
        });
        return extract;
    }

}
