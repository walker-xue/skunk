package com.qiniu.rtc.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jemy on 2018/4/18.
 */
public class RoomAccess {

    @JSONField(name = "appId")
    private String appId;
    @JSONField(name = "roomName")
    private String roomName;
    @JSONField(name = "userId")
    private String userId;
    @JSONField(name = "expireAt")
    private long expireAt;
    @JSONField(name = "permission")
    private String permission;

    public RoomAccess(String appId, String roomName, String userId, long expireAt, String permission) {
        this.appId = appId;
        this.roomName = roomName;
        this.userId = userId;
        this.expireAt = expireAt;
        this.permission = permission;
    }
}
