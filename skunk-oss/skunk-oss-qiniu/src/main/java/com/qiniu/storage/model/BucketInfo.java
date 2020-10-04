package com.qiniu.storage.model;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

public class BucketInfo {
    @JSONField(name = "source")
    private String imageSource;
    @JSONField(name = "host")
    private String imageHost;
    // CHECKSTYLE:OFF
    @JSONField(name = "protected")
    private int _protected;
    @JSONField(name = "private")
    private int _private;
    // CHECKSTYLE:ON
    @JSONField(name = "no_index_page")
    private int noIndexPage;
    private int imgsft;
    private String separator;
    private Map<String, String> styles;
    private String zone;
    private String region;
    private boolean global;

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public int getProtected() {
        return _protected;
    }

    public void setProtected(int _protected) {
        this._protected = _protected;
    }

    public int getPrivate() {
        return _private;
    }

    public void setPrivate(int _private) {
        this._private = _private;
    }

    public int getNoIndexPage() {
        return noIndexPage;
    }

    public void setNoIndexPage(int noIndexPage) {
        this.noIndexPage = noIndexPage;
    }

    public int getImgsft() {
        return imgsft;
    }

    public void setImgsft(int imgsft) {
        this.imgsft = imgsft;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public Map<String, String> getStyles() {
        return styles;
    }

    public void setStyles(Map<String, String> styles) {
        this.styles = styles;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
}
