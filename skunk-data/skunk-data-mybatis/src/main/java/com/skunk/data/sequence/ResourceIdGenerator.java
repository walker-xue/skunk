package com.skunk.data.sequence;

import com.skunk.core.utils.JdkUUIDGenerator;

import tk.mybatis.mapper.genid.GenId;

public class ResourceIdGenerator implements GenId<String> {

    @Override
    public String genId(String table, String column) {
        return JdkUUIDGenerator.generateRandom12();
    }
}