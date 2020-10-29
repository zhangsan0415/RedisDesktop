package com.zsl.swing.redis.desktop.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * 
 * @author 张帅令
 * @description  与redis服务器交互入口
 *
 */
public class RedisUtils {
	
	private static final String OK = "OK";
	
	private static final int DEFAULT_SCAN_COUNT = 50;
	
	private static final ConcurrentHashMap<String, ConnectionEntity> connectionMap = new ConcurrentHashMap<>(32);
	
	private static final ConcurrentHashMap<String, Jedis> ConnectedJedis = new ConcurrentHashMap<>(32);
	
	public static boolean testConn(ConnectionEntity entity) {
		Jedis jedis = null;
		try {
			jedis = new Jedis(entity.getHost(), entity.getPort());
			String result = StringUtils.isEmpty(entity.getPassword())?jedis.ping(OK):jedis.auth(entity.getPassword());
			return OK.equalsIgnoreCase(result);
		} catch (Exception e) {
			ContextHolder.logError(e);
			return false;
		}finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public static boolean connect(String uniqueId) {
		Jedis jedis = getJedis(uniqueId);
		try {
			jedis.connect();
			putConnectedJedis(uniqueId, jedis);
			return true;
		}catch (Exception e) {
			ContextHolder.logError(e);
			return false;
		}
	}
	
	public static void disconnect(String uniqueId) {
		Jedis jedis = ConnectedJedis.get(uniqueId);
		if(jedis != null) {
			jedis.close();
			ConnectedJedis.remove(uniqueId);
		}
	}
	
	private static Jedis getJedis(String uniqueId) {
		return ConnectedJedis.get(uniqueId) != null? ConnectedJedis.get(uniqueId):buildJedis(uniqueId);
	}
	
	private static Jedis buildJedis(String uniqueId) {
		ConnectionEntity entity = connectionMap.get(uniqueId);

		Jedis jedis = new Jedis(entity.getHost(), entity.getPort());
		if(!StringUtils.isEmpty(entity.getPassword())) {
			jedis.getClient().setPassword(entity.getPassword());
		}
		
		return jedis;
	}
	
	public static void addConnection(ConnectionEntity connectionEntity) {
		connectionMap.put(connectionEntity.getUniqueId(), connectionEntity);
	}
	
	public static void main(String[] args) {
		String host = "r-2zeruvuesgw2cciozcpd.redis.rds.aliyuncs.com";
		String pass = "M16B$cMF1XNP";
		
		ConnectionEntity entity = new ConnectionEntity("测试一下");
		entity.setUniqueId(UniqueIdUtils.getUniqueId());
		entity.setHost(host);
		entity.setPassword(pass);
		boolean resultString = testConn(entity);
		System.out.println(resultString);
	}

	public static void addConnections(List<ConnectionEntity> connections) {
		if(!CollectionUtils.isEmpty(connections)) {
			Map<String, ConnectionEntity> map = connections.stream().collect(Collectors.toMap(ConnectionEntity::getUniqueId, obj -> obj,(old,now) -> now));
			connectionMap.putAll(map);
		}
	}

	public static void removeConnection(String uniqueId) {
		connectionMap.remove(uniqueId);
		disconnect(uniqueId);
	}

	public static boolean selectDb(String uniqueId, int dbIndex) {
		Jedis jedis = getJedis(uniqueId);
		try {
			jedis.connect();
			putConnectedJedis(uniqueId, jedis);
			jedis.select(dbIndex);
			return true;
		}catch (Exception e) {
			ContextHolder.logError(e);
			return false;
		}
		
	}
	
	public static ScanResult<String> scanDb(String uniqueId,int dbIndex,String key,String cursor) {
		Jedis jedis = getJedis(uniqueId);
		try {
			jedis.connect();
			putConnectedJedis(uniqueId, jedis);
			jedis.select(dbIndex);
			cursor = StringUtils.isEmpty(cursor)?ScanParams.SCAN_POINTER_START:cursor;
			ScanParams params = new ScanParams();
			params.match(key);
			params.count(DEFAULT_SCAN_COUNT);
			return jedis.scan(cursor,params);
		}catch (Exception e) {
			ContextHolder.logError(e);
			return null;
		}
	}
	
	public static String get(String uniqueId,int dbIndex,String key) {
		Jedis jedis = getJedis(uniqueId);
		try {
			jedis.connect();
			putConnectedJedis(uniqueId, jedis);
			jedis.select(dbIndex);
			String type = jedis.type(key);
			switch (type) {
			case "string":
				return jedis.get(key);
			case "list":
				return JSON.toJSONString(jedis.lrange(key, 0L, 5000L));
			case "set":
				return JSON.toJSONString(jedis.smembers(key));
			case "zset":
				return JSON.toJSONString(jedis.zrange(key, 0L, 5000L));
			case "hash":
				return JSON.toJSONString(jedis.hgetAll(key));
			default:
				return null;
			}
		} catch (Exception e) {
			ContextHolder.logError(e);
			return null;
		}
	}
	
	private static void putConnectedJedis(String uniqueId, Jedis jedis) {
		if(!ConnectedJedis.containsKey(uniqueId)) {
			ConnectedJedis.put(uniqueId, jedis);
		}
	}

	public static Set<String> keys(String uniqueId) {
		try {
			Jedis jedis = getJedis(uniqueId);
			return jedis.keys(Constants.REDIS_ALL_PATTERN);
		}catch (Exception e) {
			ContextHolder.logError(e);
			return null;
		}
	}
	
	public static void releaseConnectedJedis() {
		ConnectedJedis.forEach((k,v) -> v.close());
	}
}
