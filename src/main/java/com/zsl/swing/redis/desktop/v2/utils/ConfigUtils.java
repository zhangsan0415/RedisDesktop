package com.zsl.swing.redis.desktop.v2.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.zsl.swing.redis.desktop.utils.CollectionUtils;
import com.zsl.swing.redis.desktop.v2.data.ConnectConfig;

/**
 * 配置操作的封装
 * @author zsl
 *
 */
public class ConfigUtils {
	
	private static final String CHAR_SET = "utf-8";
	
	private static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + ".redisdesktop" + File.separator;
	
	private static List<ConnectConfig> currentConfigList = readConfigList();
	
	private ConfigUtils() {}
	
	public static void main(String[] args) throws IOException {
		ConnectConfig config = new ConnectConfig();
		config.setId(generateId());
		config.setHost("localhost");
		config.setName("本地redis");
		config.setPort(6379);
		config.setDbIndex(0);
		
		saveConfig(config);
		
		saveConfig(config);
		
//		removeConfig(config.getId());
		
		List<ConnectConfig> configList = readConfigList();
		System.out.println(JSON.toJSONString(configList));
	}
	
	public static List<ConnectConfig> getConfigList(){
		return currentConfigList;
	}
	
	public static void saveConfig(ConnectConfig config) throws IOException{
		String jsonString = JSON.toJSONString(config);
		byte[] bytes = jsonString.getBytes(CHAR_SET);
		
		File file = new File(CONFIG_PATH + config.getId());
		if(file.exists()) {
			file.delete();
		}
		
		try (
				FileOutputStream fos = new FileOutputStream(file);
				){
			fos.write(bytes);
			updateCurrentConfigList(config);
		} 
		
	}
	
	private static synchronized void updateCurrentConfigList(ConnectConfig config) {
		deleteFromCurrentConfigList(config.getId());
		currentConfigList.add(config);
		sortConfigList();
	}
	
	private static void sortConfigList() {
		Collections.sort(currentConfigList, defaultComparator());
	}
	
	private static synchronized void deleteFromCurrentConfigList(String id) {
		currentConfigList = currentConfigList.stream().filter(obj -> !obj.getId().equals(id)).collect(Collectors.toList());
	}
	
	public static void removeConfig(String id) {
		File file = new File(CONFIG_PATH + id);
		if(file.exists()) {
			file.delete();
			deleteFromCurrentConfigList(id);
			sortConfigList();
		}
	}
	
	private static List<ConnectConfig>  readConfigList(){
		File directory = getConfigDirectory();
		
		File[] configFiles = directory.listFiles();
		if(CollectionUtils.isEmpty(configFiles)) {
			return Collections.emptyList();
		}
		
		return Arrays.stream(configFiles).map(file -> readConfig(file)).sorted(defaultComparator()).collect(Collectors.toList());
		
	}
	
	private static Comparator<ConnectConfig> defaultComparator(){
		return (o1,o2) -> o1.getShowName().compareTo(o2.getShowName());
	}
	
	private static String generateId() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
	
	private static ConnectConfig readConfig(File file) {
		try (
				FileInputStream fis = new FileInputStream(file);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis, CHAR_SET));
				){
			
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			
			return JSON.parseObject(sb.toString(), ConnectConfig.class);
		} catch (Exception e) {
			return null;
		}
	}
	private static File getConfigDirectory() {
		File file = new File(CONFIG_PATH);
		if(!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

}
