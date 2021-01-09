package com.donkeycode.boot.data;

import com.donkeycode.boot.BaseTest;
import com.skunk.core.filter.PageFilter;
import com.skunk.core.filter.PageFilterHelper;
import com.skunk.core.filter.SortOrder;
import com.skunk.oss.data.OSSFileService;
import com.skunk.oss.data.domain.OSSFile;
import com.skunk.oss.OSSFileStatus;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;

public class OSSFileServiceTest extends BaseTest {

    @Autowired
    OSSFileService ossFileService;

    @Test
    public void insertRecode() {
        OSSFile ossFile = new OSSFile();
        ossFile.setFileType("image");
        ossFile.setOssKey("/adfasdf/");
        ossFile.setName("adfasdf");
        ossFile.setStatus(OSSFileStatus.DELETE);
        ossFile.setMeta("{}");
        ossFile.setFileSize(1000l);
        ossFile.setCreateTime(new Date());
        ossFile.setFileMD5("adasdfsafasdfsdfasdf");
        ossFile.setLocalPath("asdfasdfasdfasdfasdfsadf");
        ossFileService.saveRecode(ossFile);
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
        ossFileService.updateStatus("1", OSSFileStatus.COMPLETE);
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