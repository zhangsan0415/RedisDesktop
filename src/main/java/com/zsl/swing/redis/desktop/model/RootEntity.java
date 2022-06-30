package com.zsl.swing.redis.desktop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zsl.swing.redis.desktop.utils.CollectionUtils;
import com.zsl.swing.redis.desktop.utils.FileUtils;

/**
 * 
 * @author 张帅令
 * @description 根节点，即为Connections节点，下含多个connection
 *
 */
@Deprecated
public class RootEntity implements Entity{
	
	private String showName = "Connections";
	
	private List<NodeEntity> connections = FileUtils.readConnections();

	@Override
	public ConnectionNodeType nodeType() {
		return ConnectionNodeType.ROOT;
	}

	@Override
	public String getShowName() {
		return this.showName;
	}

	public List<NodeEntity> getConnections() {
		return this.connections;
	}

	public void setConnections(List<NodeEntity> connections) {
		this.connections = connections;
	}
	
	public void addConnectionEntity(NodeEntity conn) {
		if(this.connections == null) {
			this.connections = new ArrayList<>(20);
		}
		
		List<NodeEntity> connectionList = this.connections.stream().filter(entity -> !entity.getUniqueId().equals(conn.getUniqueId())).collect(Collectors.toList());
		
		connectionList.add(conn);
		
		this.connections = connectionList;
	}
	
	
	public void removeConnectionEntity(NodeEntity conn) {
		if(!CollectionUtils.isEmpty(this.connections)) {
			this.connections = this.connections.stream().filter(obj -> !obj.getUniqueId().equals(conn.getUniqueId())).collect(Collectors.toList());
		}
	}

}
