package com.lee.manageplatform.common.utils.multiwaytree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TreeNodeUtil {
    private TreeNode root;
    private List<TreeNode> nodeList;
    private boolean isValidTree = true;

    public TreeNodeUtil() {
    }

    public TreeNodeUtil(List<TreeNode> treeNodeList) {
        nodeList = treeNodeList;
        createTree();
    }

    public static TreeNode getTreeNodeById(TreeNode tree, int id) {
        if (tree == null) {
            return null;
        }

        TreeNode treeNode = tree.findTreeNodeById(id);
        return treeNode;
    }

    /**
     * 从给定的treeNode或实体列表中生成一个树
     */
    public void createTree() {
        HashMap nodeMap = traverseNodeToMap();
        relevancyNodeToMap(nodeMap);
    }

    /**
     * 便利list保存node节点到Map中
     */
    protected HashMap traverseNodeToMap() {
        int maxId = Integer.MAX_VALUE;
        HashMap nodeMap = new HashMap<String, TreeNode>();
        Iterator it = nodeList.iterator();
        while (it.hasNext()) {
            TreeNode treeNode = (TreeNode) it.next();
            int id = treeNode.getOwnId();
            if (id < maxId) {
                maxId = id;
                this.root = treeNode;
            }
            String keyId = String.valueOf(id);

            nodeMap.put(keyId, treeNode);
        }
        return nodeMap;
    }

    /**
     * 把list中的TreeNode进行父亲孩子节点互相关联
     */
    protected void relevancyNodeToMap(HashMap nodeMap) {
        Iterator it = nodeMap.values().iterator();
        while (it.hasNext()) {
            TreeNode treeNode = (TreeNode) it.next();
            int parentId = treeNode.getParentId();
            String parentKeyId = String.valueOf(parentId);
            if (nodeMap.containsKey(parentKeyId)) {
                TreeNode parentNode = (TreeNode) nodeMap.get(parentKeyId);
                if (parentNode == null) {
                    this.isValidTree = false;
                    return;
                } else {
                    //这里需要把父亲节点做关联用于查找使用
                    parentNode.addChildNode(treeNode);
                }
            }
        }
    }


    /**
     * 判断是否有效的树
     */
    public boolean isValidTree() {
        return this.isValidTree;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<TreeNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<TreeNode> nodeList) {
        this.nodeList = nodeList;
    }
}

