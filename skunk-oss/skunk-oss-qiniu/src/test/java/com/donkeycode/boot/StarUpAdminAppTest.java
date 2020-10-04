package com.donkeycode.boot;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import lombok.extern.slf4j.Slf4j;


import java.net.UnknownHostException;


@Slf4j
public class StarUpAdminAppTest {

    public static void main(String[] args) throws UnknownHostException {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "g_6jby29LIi2jC_532ta1LIr1bAnhU40048MFfoM";
        String secretKey = "1GMRhDhmpFGg8TkxH5wHPvZv4Cy4SXfl03jRXpWm";
        String bucket = "open-class";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:\\Users\\TEMP\\ossfile\\7e630b77d44a4305a3292aa24ce2e945.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

}
