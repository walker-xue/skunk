package com.skunk.mail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XWPFTemplateTest {

    public static void main(String[] args) throws IOException {
        Map<String, Object> data = new HashMap<>();
        //        data.put("name", "司天宏");
        data.put("name", "xuey");
        data.put("sex", true);
        data.put("empty", null);
        data.put("empty2", null);
        data.put("start_time", "2020-09-17");

        ConfigureBuilder builder = Configure.newBuilder();

        builder.setElMode(Configure.ELMode.SPEL_MODE);
        log.trace(data.toString());
        XWPFTemplate template = XWPFTemplate
            .compile("/home/walker/Desktop/test.docx", builder.build())
            .render(data);

        FileOutputStream out = new FileOutputStream("test2.docx");
        template.write(out);
        out.flush();
        out.close();
        template.close();

    }

}