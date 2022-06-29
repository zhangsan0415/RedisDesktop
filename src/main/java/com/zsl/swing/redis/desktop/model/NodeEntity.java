package com.zsl.swing.redis.desktop.model;

import com.zsl.swing.redis.desktop.type.MenuEnum;
import com.zsl.swing.redis.desktop.type.NodeTypeEnum;
import com.zsl.swing.redis.desktop.utils.UniqueIdUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author 张帅令
 * @description 连接节点对应的实体
 *
 */
public class NodeEntity {
	
	private String host;
	
	private int port;
	
	private String password;
	
	private String showName;

	private String uniqueId;

	private int dbIndex;

	private NodeTypeEnum nodeType;

	private String nextCursor;

	private List<NodeEntity> sonList;

	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode(getRootEntity());

	private static NodeEntity rootEntity = null;

	public static DefaultMutableTreeNode getServerTreeRootNode(){
		return root;
	}

	public synchronized static NodeEntity getRootEntity(){
		if(Objects.isNull(rootEntity)){
			rootEntity = new NodeEntity();
			rootEntity.setShowName("servers");
			rootEntity.setNodeType(NodeTypeEnum.ROOT);
			rootEntity.setUniqueId(UniqueIdUtils.getUniqueId());
		}

		return rootEntity;
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}

	public boolean isRoot(){
		return NodeTypeEnum.ROOT == this.nodeType;
	}

	public boolean isServer(){
		return NodeTypeEnum.CONNECTION == this.nodeType;
	}

	public boolean isDb(){
		return NodeTypeEnum.DB == this.nodeType;
	}

	public List<NodeEntity> getSonList() {
		return sonList;
	}

	public void setSonList(List<NodeEntity> sonList) {
		this.sonList = sonList;
	}

	public String getNextCursor() {
		return nextCursor;
	}

	public void setNextCursor(String nextCursor) {
		this.nextCursor = nextCursor;
	}

	public NodeTypeEnum getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeTypeEnum nodeType) {
		this.nodeType = nodeType;
	}

	public NodeEntity() {
	}
	
	
	public NodeEntity(String showName) {
		this.showName = showName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}


	public String getUniqueId() {
		return uniqueId;
	}


	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public int hashCode() {
		return this.uniqueId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj)){
			return false;
		}

		if(! (obj instanceof NodeEntity)){
			return false;
		}

		NodeEntity target = (NodeEntity) obj;
		return this.uniqueId.equals(target.getUniqueId());
	}
}
