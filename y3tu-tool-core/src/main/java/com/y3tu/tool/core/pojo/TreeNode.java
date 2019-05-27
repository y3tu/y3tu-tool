package com.y3tu.tool.core.pojo;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 树节点
 *
 * @author y3tu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode<T> {
    /**
     * 节点Id
     */
    private String id;

    /**
     * 图标
     */
    private String icon;
    /**
     * url
     */
    private String url;
    /**
     * 显示节点文本
     */
    private String name;
    /**
     * 节点状态，open closed
     */
    private Map<String, Object> state;
    /**
     * 节点是否被选中 true false
     */
    private boolean checked = false;
    /**
     * 节点属性
     */
    private T data;

    /**
     * 节点的子节点
     */
    private List<TreeNode<T>> children = new ArrayList<>();
    /**
     * 父ID
     */
    private String parentId;
    /**
     * 是否有父节点
     */
    private boolean hasParent = false;
    /**
     * 是否有子节点
     */
    private boolean hasChildren = false;

    public TreeNode(String id, String name, String parentId, T data) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
