package com.donkeycode.boot.data;

import com.donkeycode.boot.BaseTest;
import com.skunk.core.filter.PageFilter;
import com.skunk.core.filter.PageFilterHelper;
import com.skunk.core.filter.SortOrder;
import com.skunk.oss.data.OssFileService;
import com.skunk.oss.data.domain.OssFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;

public class OssFileServiceTest extends BaseTest {

    @Autowired
    OssFileService ossFileService;

    @Test
    public void insertRecode() {
        OssFile ossFile = new OssFile();
        ossFile.setFileType("image");
        ossFile.setOssKey("/adfasdf/");
        ossFile.setName("adfasdf");
        ossFile.setStatus(OssFile.FAILED);
        ossFile.setMeta("{}");
        ossFile.setFileSize(1000l);
        ossFile.setCreateTime(new Date());
        ossFile.setFileMD5("adasdfsafasdfsdfasdf");
        ossFile.setLocalPath("asdfasdfasdfasdfasdfsadf");
        ossFileService.insertRecode(ossFile);
    }

    @Test
    public void getPageList() {

        PageFilter pageProvider = new PageFilterHelper.Builder()
            .pageNo(3)
            .pageSize(3)
            .orderField("createTime")
            .orderMethod(SortOrder.Type.DESC)
            .params(Collections.singletonMap("fileName", "微信")).build();

        ossFileService.getPageList(pageProvider);
    }

    @Test
    public void updateStatus() {
        ossFileService.updateStatus("1", OssFile.DELETING);
    }

    @Test
    public void getOssFile() {
        ossFileService.getOssFile("1");
    }

    @Test
    public void deleteById() {
        ossFileService.deleteById("6");
    }
}