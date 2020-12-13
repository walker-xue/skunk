package com.skunk.office.word;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.MetaTemplate;

/**
 * Word 模板工具类
 * <p>
 * http://deepoove.com/poi-tl
 */
public class XWPFTemplateUtils {

    /**
     * 获取模板中的变量
     *
     * @param templateFile
     * @return
     */
    public static List<String> getTemplateVariables(String templateFile) {

        Validate.notBlank(templateFile, "Word Template File is null.");

        File file = new File(templateFile);

        if (!file.exists()) {
            throw new RuntimeException(
                String.format("The template file does not exist. Template file:%s ", templateFile));
        }
        List<MetaTemplate> template = XWPFTemplate.compile(templateFile).getElementTemplates();
        return template.stream().map(MetaTemplate::variable).collect(Collectors.toList());
    }

}
