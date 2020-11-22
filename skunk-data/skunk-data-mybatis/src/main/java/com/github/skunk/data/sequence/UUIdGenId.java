package com.github.skunk.data.sequence;

import com.github.skunk.core.utils.UUIDUtils;

import tk.mybatis.mapper.genid.GenId;

public class UUIdGenId implements GenId<String> {

    @Override
    public String genId(String table, String column) {
        return UUIDUtils.get12UUID();
    }
}