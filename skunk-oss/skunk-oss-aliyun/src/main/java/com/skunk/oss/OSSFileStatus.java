package com.skunk.oss;

import lombok.Getter;

/**
 * OSS 文件状态
 *
 * @author walker
 */
@Getter
public enum OSSFileStatus {
    UPLOAD("UPLOAD", "待上传"),
    UPLOADING("UPLOADING", "上传中"),
    COMPLETE("COMPLETE", "完整"),
    FAILED("FAILED", "失败败"),
    DELETE("DELETE", "待删除"),
    DELETING("DELETING", "删除中"),
    DELETED("DELETED", "已删除中");

    private String code;
    private String remarks;

    OSSFileStatus(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }
}
