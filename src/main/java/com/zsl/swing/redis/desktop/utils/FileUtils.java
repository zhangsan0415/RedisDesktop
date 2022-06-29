package com.zsl.swing.redis.desktop.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.window.ZslRedisDesktopMainWindow;

/**
 * 
 * @author 张帅令
 * @description 用于预加载连接配置或者最后保存更改后的配置
 *
 */
public class FileUtils {
	
	private static final String CONFIG_PATH = System.getProperty("user.home") + File.separator+ "RedisDesktop";
	
	private static final String FILE_NAME = "config.conn";
	
	private static final Charset DEFAULT_ENCODE = Charset.forName("utf-8");
	
	public static List<NodeEntity> readConnections(){
		String connectionsString = readFile2String(CONFIG_PATH,FILE_NAME);
		
		if(StringUtils.isEmpty(connectionsString)) {
			return Collections.emptyList();
		}else {
			try {
				return JSONArray.parseArray(connectionsString, NodeEntity.class);
			} catch (Exception e) {
				ZslRedisDesktopMainWindow.getZslErrorLogPanel().logError(e);
				return Collections.emptyList();
			}
		}
	}
	
	private static File getFile(String configPath,String fileName) {
		File file = new File(configPath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		File target = new File(configPath + File.separator + fileName);
		if(!target.exists()) {
			try {
				target.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return target;
	}
	
	
	public static boolean writeConnections(List<NodeEntity> connectionEntities) {
		
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(getFile(CONFIG_PATH, FILE_NAME));
			String targetString;
			if(CollectionUtils.isEmpty(connectionEntities)) {
				targetString = "";
			}else {
				targetString = JSONArray.toJSONString(connectionEntities);
			}

			System.out.println("即将保存的连接："+ targetString);
			fos.write(targetString.getBytes(DEFAULT_ENCODE));
			fos.flush();
			return true;
		}catch (Exception e) {
			ZslRedisDesktopMainWindow.getZslErrorLogPanel().logError(e);
			return false;
		}finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					ZslRedisDesktopMainWindow.getZslErrorLogPanel().logError(e);
				}
			}
		}
		
	}
	
	private static String readFile2String(String path,String fileName) {
		File file = getFile(path, fileName);
		if(file == null) {
			return null;
		}
		
		try(
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),DEFAULT_ENCODE));
				){
			StringBuilder sb = new StringBuilder();
			
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			
			return sb.toString();
		}catch (Exception e) {
			ContextHolder.logError(e);
			return null;
		}
	}

	public static boolean saveConnection(NodeEntity entity) {
		List<NodeEntity> nodeEntities = readConnections();
		if(StringUtils.isEmpty(entity.getUniqueId())){
			entity.setUniqueId(UniqueIdUtils.getUniqueId());
			nodeEntities.add(entity);
		}else{
			nodeEntities = nodeEntities.stream().filter(e -> !e.getUniqueId().equals(entity.getUniqueId())).collect(Collectors.toList());
			nodeEntities.add(entity);
		}

		return writeConnections(nodeEntities);
	}

    public static boolean deleteConnection(NodeEntity entity) {
		List<NodeEntity> nodeEntities = readConnections();
		nodeEntities = nodeEntities.stream().filter(e -> !e.getUniqueId().equals(entity.getUniqueId())).collect(Collectors.toList());

		return writeConnections(nodeEntities);

	}
}
