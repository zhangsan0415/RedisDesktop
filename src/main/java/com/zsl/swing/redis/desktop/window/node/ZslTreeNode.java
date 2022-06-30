package com.zsl.swing.redis.desktop.window.node;

import com.zsl.swing.redis.desktop.model.NodeEntity;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Vector;

public class ZslTreeNode<T> implements TreeNode {

    private ZslTreeNode<T> parent;

    private T entity;

    private Vector<ZslTreeNode> children = new Vector<>();

    private boolean isLeaf = false;

    private boolean isRoot = false;

    public ZslTreeNode(T entity){
        this(entity,false);
    }

    public ZslTreeNode(T entity, boolean isLeaf){
        this.entity = entity;
        this.isLeaf = isLeaf;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.elementAt(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return this.parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        if(Objects.isNull(node)){
            return -1;
        }

        int size = children.size();
        ZslTreeNode<NodeEntity> zslNode = (ZslTreeNode<NodeEntity>) node;
        for(int i = 0; i < size; i++){
            if(children.get(i).getEntity().equals(zslNode.getEntity())){
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean getAllowsChildren() {
        return !this.isLeaf;
    }

    @Override
    public boolean isLeaf() {
        return this.isLeaf;
    }

    @Override
    public Enumeration children() {
        return children.elements();
    }

    public void setParent(ZslTreeNode<T> parent) {
        this.parent = parent;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Vector<ZslTreeNode> getChildren() {
        return children;
    }

    public void setChildren(Vector<ZslTreeNode> children) {
        this.children = children;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }
}
