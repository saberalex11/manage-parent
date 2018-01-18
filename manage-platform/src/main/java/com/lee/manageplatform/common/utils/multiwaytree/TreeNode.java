package com.lee.manageplatform.common.utils.multiwaytree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形结构
 * zhoukai7
 */
public class TreeNode implements Serializable {
    private int parentId;//父亲ID
    private int ownId;//孩子ID
    private int reviewsign;//部门审核标记value=1
    protected String nodeName;//节点名称
    protected TreeNode parentNode;
    protected List<TreeNode> childList;

    /**
     * 必须进程初始化否则会抛出异常
     */
    public TreeNode() {
        initChildList();
    }

    public TreeNode(TreeNode parentNode) {
        this.getParentNode();
        initChildList();
    }

    public boolean isLeaf() {
        if (childList == null) {
            return true;
        } else {
            if (childList.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 插入一个child节点到当前节点
     *
     * @param treeNode
     */
    public void addChildNode(TreeNode treeNode) {
        initChildList();
        childList.add(treeNode);
    }

    public void initChildList() {
        if (childList == null) {
            childList = new ArrayList<TreeNode>();
        }

    }

    /**
     * 判断是否是有效的节点
     *
     * @return
     */
    public boolean isValidTree() {
        return true;
    }

    /**
     * 返回当前节点的父辈级别的节点列表
     *
     * @return
     */
    public List<TreeNode> getElders() {
        List<TreeNode> elderList = new ArrayList<TreeNode>();
        TreeNode parentNode = this.getParentNode();
        if (parentNode == null) {
            return elderList;
        } else {
            elderList.add(parentNode);
            elderList.addAll(parentNode.getElders());
            return elderList;
        }
    }

    /**
     * 返回当前节点的子孙辈的列表
     *
     * @return
     */
    public List<TreeNode> getJuniors() {
        List<TreeNode> juniorList = new ArrayList<TreeNode>();
        List<TreeNode> childList = this.getChildList();
        if (childList == null) {
            return juniorList;
        } else {
            int childNumber = childList.size();
            for (int i = 0; i < childNumber; i++) {
                TreeNode junior = childList.get(i);
                juniorList.add(junior);
                juniorList.addAll(junior.getJuniors());
            }
            return juniorList;
        }
    }

    /**
     * 返回当前节点的所有的孩子
     *
     * @return
     */
    public List<TreeNode> getChildList() {
        return childList;
    }

    /**
     * 删除当前节点和它的子孙
     */
    public void deleteNode() {
        TreeNode parentNode = this.getParentNode();
        int id = this.getOwnId();

        if (parentNode != null) {
            parentNode.deleteChildNode(id);
        }
    }

    /**
     * 删除当前节点的某个孩子节点
     *
     * @param childId
     */
    public void deleteChildNode(int childId) {
        List<TreeNode> childList = this.getChildList();
        int childNumber = childList.size();
        for (int i = 0; i < childNumber; i++) {
            TreeNode child = childList.get(i);
            if (child.getOwnId() == childId) {
                childList.remove(i);
                return;
            }
        }
    }

    /**
     * 当前节点中插入新节点
     *
     * @param treeNode
     * @return
     */
    public boolean insertNewNode(TreeNode treeNode) {
        int newParentId = treeNode.getParentId();
        //如果相等则新增加list保存该节点
        if (this.parentId == newParentId) {
            addChildNode(treeNode);
            return true;
        } else {
            List<TreeNode> childList = this.getChildList();
            int size = childList.size();
            boolean flag;
            //如果不存在则需要从该节点的子节点列表循环查找
            for (int i = 0; i < size; i++) {
                TreeNode childNode = childList.get(i);
                flag = childNode.insertNewNode(treeNode);
                if (flag == true) {
                    return true;
                }

            }
            return false;
        }
    }

    /**
     * 查找树中某个节点
     *
     * @param id
     * @return
     */
    public TreeNode findTreeNodeById(int id) {
        if (this.ownId == id) {
            return this;
        }

        if (childList.isEmpty() || childList == null) {
            return null;
        } else {
            int size = childList.size();
            for (int i = 0; i < size; i++) {
                TreeNode child = childList.get(i);
                TreeNode result = child.findTreeNodeById(id);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
    }

    /**
     * 遍历树
     */
    public void traverseTree() {
        if (ownId < 0) {
            return;
        }
        if (childList == null || childList.isEmpty()) {
            return;
        }

        int size = childList.size();
        for (int i = 0; i < size; i++) {
            TreeNode child = childList.get(i);
            child.traverseTree();
        }
    }


    /**
     * 遍历树同时保存该节点中的parentNode属性
     *
     * @param treeNode
     */
    public void traverseTreeAndSaveParentNode(TreeNode treeNode) {
        if (ownId < 0) {
            return;
        }
        if (childList == null || childList.isEmpty()) {
            return;
        }

        int size = childList.size();
        for (int i = 0; i < size; i++) {
            TreeNode child = childList.get(i);
            //treeNode.getChildList().clear();//此处可以清除数据如果嫌冗余
            child.setParentNode(treeNode);
            child.traverseTreeAndSaveParentNode(child);
        }
    }


    public void setChildList(List<TreeNode> childList) {
        this.childList = childList;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getOwnId() {
        return ownId;
    }

    public void setOwnId(int ownId) {
        this.ownId = ownId;
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getReviewsign() {
        return reviewsign;
    }

    public void setReviewsign(int reviewsign) {
        this.reviewsign = reviewsign;
    }
}
