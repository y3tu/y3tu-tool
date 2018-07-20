package com.y3tu.tool.excel.pojo;

import lombok.Data;

import java.util.*;

/**
 * @author y3tu
 * @date 2018/6/19
 */
@Data
public class Node {
    private String text = null;
    private Integer width = 0;
    private Integer deep = 0;
    private Map<String, Node> map = new LinkedHashMap<>();

    /**
     * 根据表达式，解析出该表达式对应的Node结点，并存入
     *
     * @param text
     * @author LinkedBear
     */
    public void addNodeByStringExpressionAndWidth(String text, Integer width) {
        //从根目录开始
        Map<String, Node> rootMap = map;
        String[] arr = text.split("\\.");

        Node node = null;
        //读到叶子结点的前一个结点处
        for (int i = 0; i < arr.length - 1; i++) {
            //逐层目录读取，如果没有get到，就创建一个新的目录
            node = rootMap.get(arr[i]);
            if (node == null) {
                node = new Node(arr[i]);
                rootMap.put(arr[i], node);
            }
            //新目录的大小要同步上
            node.setWidth(node.getWidth() + width);
            rootMap = node.getMap();
        }
        //此时的rootMap就是叶子结点所在的目录
        rootMap.put(arr[arr.length - 1], new Node(arr[arr.length - 1], width, arr.length - 1));
        //还要给这个文件的父文件夹设置deep
        if (node != null) {
            node.setDeep(arr.length - 2);
        }
    }

    /**
     * 遍历自己的存储空间，将所有结点按顺序整理成List
     *
     * @return
     * @author LinkedBear
     * @Time 2018年3月26日 下午2:50:05
     */
    public List<Node> parseToSeqNodes() {
        List<Node> list = new ArrayList<>();
        for (Map.Entry<String, Node> entry : map.entrySet()) {
            //先把自己保存进去
            list.add(entry.getValue());
            //如果该节点的map不是空集合，证明这是一个“文件夹”（根节点）
            //需要把自己add进去的同时，把它的孩子也全部add进去
            if (entry.getValue().getMap() != null && entry.getValue().getMap().size() > 0) {
                List<Node> nodes = entry.getValue().parseToSeqNodes();//递归调用
                list.addAll(nodes);
            }
        }
        return list;
    }

    /**
     * 计算深度
     *
     * @return
     * @author LinkedBear
     */
    public int getDeepLength() {
        if (map.isEmpty()) {
            return 0;
        }
        List<Integer> list = new ArrayList<>();
        for (Map.Entry<String, Node> entry : map.entrySet()) {
            list.add(entry.getValue().getDeepLength());
        }
        Collections.sort(list);
        return list.get(list.size() - 1) + 1;
    }

    /**
     * 获取该结点下的所有叶子节点的数量
     *
     * @return
     * @author LinkedBear
     */
    public int getChildrenCount() {
        //如果map为空，证明是叶子结点，要计数
        if (map.isEmpty()) {
            return 1;
        }
        //由于不是叶子结点，所以不能计当前的数，这里的基数为0
        int count = 0;
        for (Map.Entry<String, Node> entry : map.entrySet()) {
            count += entry.getValue().getChildrenCount();
        }
        return count;
    }

    public Node(String text, Integer width, Integer deep) {
        this.text = text;
        this.width = width;
        this.deep = deep;
    }

    public Node(String text, Integer width) {
        this.text = text;
        this.width = width;
    }

    public Node(String text) {
        this.text = text;
    }

    public Node() {
    }
}
