package com.y3tu.tool.core.util;

import com.y3tu.tool.core.pojo.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点工具类
 * <p>
 * <p>
 * 要使用此工具类，需要用TreeNode构建出List<TreeNode<T>>对象
 *
 * eg:
 *
 * List<TreeNode<Department>> treeNodeList = list.stream().map(department -> {
 * TreeNode<Department> treeNode = new TreeNode<>(department.getId(), department.getName(), department.getParentId(), department);
 * return treeNode;
 * }).collect(Collectors.toList());
 *
 * TreeUtil.buildList(treeNodeList, "0"));
 *
 * @author y3tu
 */
public class TreeUtil {

    /**
     * 构建树结构
     *
     * @param nodes
     * @param <T>
     * @return
     */
    public static <T> TreeNode<T> build(List<TreeNode<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<TreeNode<T>> topNodes = new ArrayList<>();
        for (TreeNode<T> children : nodes) {
            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                topNodes.add(children);
                continue;
            }
            for (TreeNode<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setHasChildren(true);
                    continue;
                }
            }

        }

        TreeNode<T> root = new TreeNode<>();
        root.setId("0");
        root.setParentId("");
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChecked(true);
        root.setChildren(topNodes);
        root.setName("根节点");
        Map<String, Object> state = new HashMap<>(16);
        state.put("opened", true);
        root.setState(state);
        return root;
    }

    /**
     * 构建树结构
     *
     * @param nodes      被构建的数据
     * @param rootNodeId 根节点id
     * @param <T>
     * @return
     */
    public static <T> List<TreeNode<T>> buildList(List<TreeNode<T>> nodes, String rootNodeId) {
        if (nodes == null) {
            return null;
        }
        List<TreeNode<T>> topNodes = new ArrayList<>();
        for (TreeNode<T> children : nodes) {
            String pid = children.getParentId();
            if (pid == null || rootNodeId.equals(pid)) {
                topNodes.add(children);
                continue;
            }
            for (TreeNode<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setHasChildren(true);
                    continue;
                }
            }
        }
        return topNodes;
    }

}
