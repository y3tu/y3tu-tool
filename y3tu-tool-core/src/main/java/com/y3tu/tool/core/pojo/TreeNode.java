package com.y3tu.tool.core.pojo;

import lombok.Data;

import java.util.List;

/**
 * 树节点
 *
 * @author y3tu
 * @date 2018/2/27
 */
@Data
public class TreeNode {

    private String text;

    private List<String> tags;

    private String id;

    private String parentId;

    private String levelCode;

    private List<TreeNode> nodes;

    private String icon;

}
