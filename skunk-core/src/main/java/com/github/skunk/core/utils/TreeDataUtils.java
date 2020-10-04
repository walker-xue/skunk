package com.github.skunk.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.cglib.beans.BeanMap;

import com.github.skunk.core.collectors.CollectorUtils;
import com.github.skunk.core.entity.TreeNode;

/**
 * 数据转换成树形结构
 *
 * @author walker
 * @since 0.0.1
 */
@Slf4j
public class TreeDataUtils {

    /**
     * 封装树
     *
     * @param pNodes
     *     要封装为树的父对象集合
     * @param originaNodes
     *     原始list数据
     * @param keyName
     *     作为唯一标示的字段名称
     * @param pFieldName
     *     模型中作为parent字段名称
     * @param subFieldName
     *     模型中作为children的字段名称
     */
    public static <T> void buildTree(List<T> pNodes, List<T> originaNodes, String keyName, String pFieldName, String subFieldName) {
        buildTree(pNodes, originaNodes, keyName, pFieldName, subFieldName, null);
    }

    /**
     * 封装树
     *
     * @param pNodes
     *     要封装为树的父对象集合
     * @param originaNodes
     *     原始list数据
     * @param keyName
     *     作为唯一标示的字段名称
     * @param pFieldName
     *     模型中作为parent字段名称
     * @param subFieldName
     *     模型中作为children的字段名称
     */
    public static <T> void buildTree(List<T> pNodes, List<T> originaNodes, String keyName, String pFieldName, String subFieldName, String orderFieldName) {

        CollectorUtils.forEach(pNodes, (index, node) -> {
            try {
                List<T> subNodes = getChildren(pNodes.get(index), originaNodes, keyName, pFieldName, subFieldName, orderFieldName);
                if (!subNodes.isEmpty()) {
                    originaNodes.removeAll(subNodes);
                    buildTree(subNodes, originaNodes, keyName, pFieldName, subFieldName, orderFieldName);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 封装子对象
     *
     * @param pNode
     *     父对象
     * @param originals
     *     待处理对象集合
     * @param keyName
     *     作为唯一标示的字段名称
     * @param pFieldName
     *     模型中作为parent字段名称
     * @param subFieldName
     *     模型中作为children的字段名称
     */
    private static <T> List<T> getChildren(T pNode, List<T> originals, String keyName, String pFieldName, String subFieldName, String orderFieldName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        List<T> nodes = new ArrayList<>();
        String pIdVal = BeanMap.create(pNode).get(keyName).toString();
        for (T node : originals) {
            String subPId = BeanMap.create(node).get(pFieldName).toString();
            if (pIdVal.equals(subPId)) {
                nodes.add(node);
            }
        }
        if (nodes.isEmpty()) {
            return nodes;
        }

        FieldUtils.writeDeclaredField(pNode, subFieldName, nodes, true);
        if (StringUtils.isNoneBlank(orderFieldName)) {
            nodes.sort((e1, e2) -> {
                try {
                    return Integer.parseInt(FieldUtils.readField(e1, orderFieldName, true).toString()) - (Integer.parseInt(FieldUtils.readField(e2, orderFieldName, true).toString()));
                } catch (IllegalAccessException ex) {
                    log.warn("Read Field Value fail. {}", ex.getMessage());
                    return 0;
                }
            });
        }
        return nodes;
    }

    /**
     * 两层循环实现建树
     *
     * @param treeNodes
     *     传入的树节点列表
     * @return
     */
    public static <T extends TreeNode> List<T> bulid(List<T> treeNodes, Object root) {

        List<T> trees = new ArrayList<T>();

        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }
            for (T it : treeNodes) {
                if (it.getParentId() == treeNode.getId()) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<TreeNode>());
                    }
                    treeNode.add(it);
                }
            }
        }
        return trees;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId() == it.getParentId()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<TreeNode>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
}
