package com.github.skunk.office.word;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.deepoove.poi.XWPFTemplate;

public class XWPFTemplateTest {

    public static void main(String[] args) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "司天宏");
        data.put("start_time", "2020-09-17");
        XWPFTemplate template = XWPFTemplate.compile("F:/template.docx")
                .render(data);
        FileOutputStream out;
        out = new FileOutputStream("F:/template2.docx");
        template.write(out);
        out.flush();
        out.close();
        template.close();

    }

}