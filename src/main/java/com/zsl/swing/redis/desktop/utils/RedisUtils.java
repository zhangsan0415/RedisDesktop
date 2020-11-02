package com.zsl.swing.redis.desktop.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;

import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Protocol.Command;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * 
 * @author 张帅令
 * @description  与redis服务器交互入口
 *
 */
public class RedisUtils {
	
	
	private static final ConcurrentHashMap<String, ConnectionEntity> connectionMap = new ConcurrentHashMap<>(32);
	
	private static final ConcurrentHashMap<String, Jedis> ConnectedJedis = new ConcurrentHashMap<>(32);
	
	public static boolean testConn(ConnectionEntity entity) {
		Jedis jedis = null;
		try {
			jedis = new Jedis(entity.getHost(), entity.getPort());
			String result = StringUtils.isEmpty(entity.getPassword())?jedis.ping(Constants.OK):jedis.auth(entity.getPassword());
			return Constants.OK.equalsIgnoreCase(result);
		} catch (Exception e) {
			ContextHolder.logError(e);
			return false;
		}finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public static boolean connect(String uniqueId) {
		Jedis jedis = null;
		try {
			jedis = buildJedis(uniqueId);
			return jedis.isConnected();
		}catch (Exception e) {
			ContextHolder.logError(e);
			return false;
		}finally {
			close(jedis);
		}
	}
	
	private static void close(Jedis jedis) {
		if(jedis != null) {
			jedis.close();
		}
	}
	
	private static Jedis buildJedis(String uniqueId) {
		ConnectionEntity entity = connectionMap.get(uniqueId);

		Jedis jedis = new Jedis(entity.getHost(), entity.getPort());
		
		jedis.getClient().setConnectionTimeout(10000);
		jedis.getClient().setSoTimeout(10000);
		
		if(!StringUtils.isEmpty(entity.getPassword())) {
			jedis.getClient().setPassword(entity.getPassword());
		}
		
		jedis.connect();
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
	}

	public static boolean selectDb(String uniqueId, int dbIndex) {
		Jedis jedis = null;
		try {
			jedis = buildJedis(uniqueId);
			jedis.select(dbIndex);
			return true;
		}catch (Exception e) {
			ContextHolder.logError(e);
			return false;
		}finally {
			close(jedis);
		}
		
	}
	
	public static ScanResult<String> scanDb(String uniqueId,int dbIndex,String key,String cursor) {
		Jedis jedis = null;
		try {
			jedis = buildJedis(uniqueId);
			jedis.select(dbIndex);
			cursor = StringUtils.isEmpty(cursor)?ScanParams.SCAN_POINTER_START:cursor;
			ScanParams params = new ScanParams();
			params.match(key);
			params.count(Constants.DEFAULT_SCAN_COUNT);
			return jedis.scan(cursor,params);
		}catch (Exception e) {
			ContextHolder.logError(e);
			return null;
		}finally {
			close(jedis);
		}
	}
	
	public static String get(String uniqueId,int dbIndex,String key) {
		Jedis jedis = null;
		try {
			jedis = buildJedis(uniqueId);
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
		}finally {
			close(jedis);
		}
	}
	
	public static Set<String> keys(String uniqueId) {
		Jedis jedis = null;
		try {
			jedis = buildJedis(uniqueId);
			return jedis.keys(Constants.REDIS_ALL_PATTERN);
		}catch (Exception e) {
			ContextHolder.logError(e);
			return null;
		}finally {
			close(jedis);
		}
	}
	
	public static void releaseConnectedJedis() {
		ConnectedJedis.forEach((k,v) -> v.close());
	}

	public static void flushDb(String uniqueId, int dbIndex) {
		Jedis jedis = null;
		try {
			jedis = buildJedis(uniqueId);
			jedis.select(dbIndex);
			jedis.flushDB();
		}catch (Exception e) {
			ContextHolder.logError(e);
		}finally {
			close(jedis);
		}
	}
	
	public static String execute(String target,String uniqueId) {
		String[] split = target.split(" ");
		Command command = Protocol.Command.valueOf(split[0].toUpperCase());
		
		List<String> list = new ArrayList<>(split.length);
		for(int i=1;i<split.length;i++) {
			if(!StringUtils.isEmpty(split[i])) {
				list.add(split[i]);
			}
		}
		Jedis jedis = null;
		try {
			jedis = buildJedis(uniqueId);
			Client client = jedis.getClient();
			client.sendCommand(command, list.toArray(new String[list.size()]));
			
			switch (command) {
			case PING:
			case GET:
			case RANDOMKEY:
			case GETSET:
			case INCRBYFLOAT:
			case SUBSTR:
			case HGET:
			case HINCRBYFLOAT:
			case LINDEX:
			case LPOP:
			case RPOP:
			case RPOPLPUSH:
			case SPOP:	
			case ECHO:
			case BRPOPLPUSH:
			case GETRANGE:
				return client.getBulkReply();
			case SET:
			case TYPE:
			case RENAME:
			case SETEX:
			case MSET:
			case HMSET:
			case LTRIM:
			case LSET:
			case WATCH:
				return client.getStatusCodeReply();
			case EXISTS:
			case DEL:
			case UNLINK:
			case RENAMENX:
			case EXPIRE:
			case EXPIREAT:
			case TTL:
			case TOUCH:
			case MOVE:
			case SETNX:
			case MSETNX:
			case DECRBY:
			case DECR:
			case INCR:
			case INCRBY:
			case APPEND:
			case HSET:
			case HSETNX:
			case HINCRBY:
			case HEXISTS:
			case HDEL:
			case HLEN:
			case RPUSH:
			case LPUSH:
			case LLEN:
			case LREM:
			case SADD:
			case SREM:
			case SMOVE:
			case SCARD:
			case SISMEMBER:
			case SINTERSTORE:
			case SUNIONSTORE:
			case SDIFFSTORE:
			case ZADD:
			case ZREM:
			case ZRANK:
			case ZREVRANK:
			case ZCARD:
			case ZCOUNT:
			case ZREMRANGEBYRANK:
			case ZREMRANGEBYSCORE:
			case ZUNIONSTORE:
			case ZINTERSTORE:
			case ZLEXCOUNT:
			case ZREMRANGEBYLEX:
			case STRLEN:
			case LPUSHX:
			case PERSIST:
			case RPUSHX:
			case LINSERT:
			case SETBIT:
			case GETBIT:
			case SETRANGE:
			case BITPOS:
			case PUBLISH:
				return String.valueOf(client.getIntegerReply());
			case ZINCRBY:
			case ZSCORE:
				return BuilderFactory.STRING.build(client.getOne());
			case HGETALL:
				return JSON.toJSONString(BuilderFactory.STRING_MAP.build(client.getBinaryMultiBulkReply()));
			case KEYS:
			case HKEYS:
			case SDIFF:
				return JSON.toJSONString(BuilderFactory.STRING_SET.build(client.getBinaryMultiBulkReply()));
			case MGET:
			case HMGET:
			case HVALS:
			case LRANGE:
			case SMEMBERS:
			case SINTER:
			case SUNION:
			case SRANDMEMBER:
			case ZRANGE:
			case ZREVRANGE:
			case SORT:
			case BLPOP:
			case BRPOP:
			case ZRANGEBYSCORE:
			case ZREVRANGEBYSCORE:
			case ZRANGEBYLEX:
			case ZREVRANGEBYLEX:
			case CONFIG:
				return JSON.toJSONString(client.getMultiBulkReply());
			default:
				return "not support!";
			}
		}catch (Exception e) {
			ContextHolder.logError(e);
			return e.getMessage();
		}finally {
			close(jedis);
		}
		
	}
}
