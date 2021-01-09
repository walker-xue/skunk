package com.skunk.oss.data.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skunk.core.BaseEntity;
import com.skunk.core.bean.SpringBeanFactory;
import com.skunk.core.utils.JdkUUIDGenerator;
import com.skunk.oss.OSSFileStatus;
import com.skunk.oss.config.OssConfigProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * OSS file Object
 *
 * @author walker
 * @since 2019年5月12日
 */
@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OSSFile extends BaseEntity {

    private String fileId;
    @NotBlank
    private String name;
    @JsonIgnore
    @NotBlank
    private String ossKey;
    @NotBlank
    private String fileType;
    @JsonIgnore
    @NotBlank
    private String fileMD5;
    @NotNull
    private Long fileSize;
    @JsonIgnore
    private String meta;
    @JsonIgnore
    private OSSFileStatus status;
    @JsonIgnore
    @NotBlank
    private String localPath;
    private String cdnPath;
    @NotNull
    private Date createTime;

    public OSSFile(
        @NotBlank String name,
        @NotBlank String ossKey,
        @NotBlank String fileType,
        @NotNull Long fileSize,
        String meta,
        @NotBlank String localPath) {

        this.fileId = JdkUUIDGenerator.generateRandom12();
        this.name = name;
        this.ossKey = ossKey;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.meta = meta;
        this.localPath = localPath;
    }
    public String getCdnPath() {
        OssConfigProperties config = SpringBeanFactory.bean(OssConfigProperties.class);
        return config.getCdnPath() + (this.getOssKey().startsWith("/") ? "" : "/") + this.getOssKey();
    }
}
