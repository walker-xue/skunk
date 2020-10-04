package commons.test;

import com.github.skunk.core.utils.StringUtils;

public class HtmlUtilTest {
    public static void main(String[] args) {

       String html="http://localhost:9020/api/role/page?pageNo=1&pageSize=10&sortField=roleName&sortOrder=descend&orderBys[]=roleName:descend";
        System.out.println(StringUtils.escapeHtml4(html));

    }


}
