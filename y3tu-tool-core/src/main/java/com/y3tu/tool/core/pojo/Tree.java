package com.y3tu.tool.core.pojo;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 树节点
 *
 * @author y3tu
 * @date 2018/7/2
 */
@Data
public class Tree<T> {
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
    private String text;
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
    private Map<String, Object> attributes;

    /**
     * 节点的子节点
     */
    private List<Tree<T>> children = new ArrayList<Tree<T>>();
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

    public Tree(String id, String text, Map<String, Object> state, boolean checked, Map<String, Object> attributes,
                List<Tree<T>> children, String icon, String url, boolean isParent, boolean isChildren, String parentID) {
        super();
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.url = url;
        this.state = state;
        this.checked = checked;
        this.attributes = attributes;
        this.children = children;
        this.hasParent = isParent;
        this.hasChildren = isChildren;
        this.parentId = parentID;
    }

    public Tree() {
        super();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
