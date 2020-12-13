package com.skunk.oss.data.domain;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.skunk.core.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author walker
 * @since 2019年5月12日
 */
@SuppressWarnings("serial")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OssFile extends BaseEntity {

    public static final String UPLOADING = "uploading";
    public static final String UPLOADED = "uploaded";
    public static final String UPLOAD = "upload";
    public static final String FAILED = "failed";
    public static final String DELETING = "deleting";

    private String fileId;
    private String name;
    @JSONField(serialize=false)
    private String ossKey;
    private String fileType;
    @JSONField(serialize=false)
    private String fileMD5;
    private Long fileSize;
    @JSONField(serialize=false)
    private String meta;
    @JSONField(serialize=false)
    private String status;
    @JSONField(serialize=false)
    private String localPath;
    private String cdnPath;
    private Date createTime;
}
