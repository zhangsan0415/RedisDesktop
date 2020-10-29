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

import com.alibaba.fastjson.JSONArray;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;

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
	
	public static List<ConnectionEntity> readConnections(){
		String connectionsString = readFile2String(CONFIG_PATH,FILE_NAME);
		
		if(StringUtils.isEmpty(connectionsString)) {
			return Collections.emptyList();
		}else {
			try {
				return JSONArray.parseArray(connectionsString, ConnectionEntity.class);
			} catch (Exception e) {
				ContextHolder.logError(e);
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
	
	
	public static void writeConnections(List<ConnectionEntity> connectionEntities) {
		
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
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
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
	
}
