package com.github.skunk.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.skunk.core.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 树型数据结构的节点
 *
 * @author walker
 * @since 2019年6月28日
 */
@Setter
@Getter
public class TreeNode extends BaseEntity {

    private static final long serialVersionUID = 1L;

    protected int id;
    protected int parentId;
    /**
     *
     */
    protected List<TreeNode> children = new ArrayList<TreeNode>();

    public void add(TreeNode node) {
        children.add(node);
    }
}
