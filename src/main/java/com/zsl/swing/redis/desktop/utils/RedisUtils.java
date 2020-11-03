package com.zsl.swing.redis.desktop.utils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;

import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Client;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Protocol.Command;
import redis.clients.jedis.Protocol.Keyword;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.util.SafeEncoder;
import redis.clients.jedis.util.Slowlog;

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
			key = "*" + key.trim() + "*";
			jedis = buildJedis(uniqueId);
			jedis.select(dbIndex);
			cursor = StringUtils.isEmpty(cursor)?ScanParams.SCAN_POINTER_START:cursor;
			ScanParams params = new ScanParams();
			params.match(key);
			params.count(Constants.DEFAULT_SCAN_COUNT);
			
			List<String> resultList = new ArrayList<>(Constants.DEFAULT_SCAN_COUNT);
			
			ScanResult<String> scan;
			do {
				scan = jedis.scan(cursor,params);
				if(!CollectionUtils.isEmpty(scan.getResult())) {
					resultList.addAll(scan.getResult());
				}
				
				cursor = scan.getCursor();
			} while (resultList.size() < Constants.DEFAULT_SCAN_COUNT && !ScanParams.SCAN_POINTER_START.equals(scan.getCursor()));
			
			
			return new ScanResult<>(cursor, resultList);
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
	
	public static boolean del(String uniqueId,int dbIndex, String key) {
		Jedis jedis = null;
		try {
			jedis = buildJedis(uniqueId);
			jedis.select(dbIndex);
			return jedis.del(key) == 1L;
		}catch (Exception e) {
			ContextHolder.logError(e);
			return false;
		} finally {
			close(jedis);
		}
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
				if(list.isEmpty()) {
					return client.getStatusCodeReply();
				}else {
					return SafeEncoder.encode(client.getBinaryBulkReply());
				}
			case SPOP:
			case SRANDMEMBER:
				if(list.size() == 1) {
					return SafeEncoder.encode(client.getBinaryBulkReply());
				}else if(list.size() == 2) {
					Set<String> keySet = BuilderFactory.STRING_SET.build(client.getBinaryMultiBulkReply());
					return JSON.toJSONString(keySet);
				}else {
					return null;
				}
			case GET:
			case RANDOMKEY:
			case GETSET:
			case SUBSTR:
			case HGET:
			case LINDEX:
			case LPOP:
			case RPOP:
			case RPOPLPUSH:
			case ECHO:
			case BRPOPLPUSH:
			case GETRANGE:
			case DUMP:
			case MEMORY:
			case XADD:
				return SafeEncoder.encode(client.getBinaryBulkReply());
			case QUIT:
				String quitReturn = client.getStatusCodeReply();
				client.disconnect();
				return quitReturn;
			case SELECT:
				int index = Integer.parseInt(list.get(0));
				String statusCodeReply = client.getStatusCodeReply();
				client.setDb(index);
				return statusCodeReply;
			case SLOWLOG:
				String keyword = list.get(0);
				if(Keyword.GET.name().equalsIgnoreCase(keyword)) {
					List<Slowlog> from = Slowlog.from(client.getObjectMultiBulkReply());
					return JSON.toJSONString(from);
				}else if(Keyword.RESET.name().equalsIgnoreCase(keyword)) {
					return client.getBulkReply();
				}else if(Keyword.LEN.name().equals(keyword)) {
					return String.valueOf(client.getIntegerReply());
				}else {
					return "not support!";
				}
			case MGET:
			case HMGET:
			case HVALS:
			case LRANGE:
			case CONFIG:
			case GEOHASH:
			case XREAD:
			case XREADGROUP:
			case XRANGE:
			case XREVRANGE:
			case XPENDING:
			case XCLAIM:
				List<String> build = BuilderFactory.STRING_LIST.build(client.getBinaryMultiBulkReply());
				return JSON.toJSONString(build);
			case GEOPOS:
				List<GeoCoordinate> coordinates = BuilderFactory.GEO_COORDINATE_LIST.build(client.getObjectMultiBulkReply());
				return JSON.toJSONString(coordinates);
			case GEORADIUS:
			case GEORADIUS_RO:
			case GEORADIUSBYMEMBER:
			case GEORADIUSBYMEMBER_RO:
				List<GeoRadiusResponse> radiusResponses = BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(client.getObjectMultiBulkReply());
				return JSON.toJSONString(radiusResponses);
			case BITFIELD:
				return JSON.toJSONString(client.getIntegerMultiBulkReply());
			case HGETALL:
				List<String> flatHash = BuilderFactory.STRING_LIST.build(client.getBinaryMultiBulkReply());
				Iterator<String> iterator = flatHash.iterator();
				
				Map<String,String> map = new HashMap<>();
				while(iterator.hasNext()) {
					map.put(iterator.next(), iterator.next());
				}
				
				return JSON.toJSONString(map);
			case INCRBYFLOAT:
			case HINCRBYFLOAT:
			case INFO:
			case GEODIST:
				return client.getBulkReply();
			case SET:
			case TYPE:
			case RENAME:
			case SWAPDB:
			case SETEX:
			case MSET:
			case HMSET:
			case LTRIM:
			case LSET:
			case WATCH:
			case FLUSHDB:
			case FLUSHALL:
			case SAVE:
			case BGSAVE:
			case BGREWRITEAOF:
			case SHUTDOWN:
			case SLAVEOF:
			case DEBUG:
			case RESTORE:
			case PSETEX:
			case CLIENT:
			case MIGRATE:
			case PFMERGE:
			case XGROUP:
				return client.getStatusCodeReply();
			case ZINCRBY:
			case ZSCORE:
				return BuilderFactory.STRING.build(client.getOne());
			case ZREVRANGEBYLEX:
				if(list.size() == 3) {
					return String.valueOf(client.getIntegerReply());
				}else if(list.size() == 5) {
					Set<String> keySet = BuilderFactory.STRING_SET.build(client.getBinaryMultiBulkReply());
					return JSON.toJSONString(keySet);
				}
			case KEYS:
			case HKEYS:
			case SMEMBERS:
			case SINTER:
			case SUNION:
			case SDIFF:
			case ZRANGE:
			case ZREVRANGE:
			case ZREVRANGEBYSCORE:
			case ZRANGEBYLEX:
				Set<String> keySet = BuilderFactory.STRING_SET.build(client.getBinaryMultiBulkReply());
				return JSON.toJSONString(keySet);
			case EXISTS:
			case DEL:
			case UNLINK:
			case RENAMENX:
			case DBSIZE:
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
			case PEXPIRE:
			case PEXPIREAT:
			case LASTSAVE:
			case BITCOUNT:
			case BITOP:
			case PTTL:
			case WAIT:
			case PFADD:
			case PFCOUNT:
			case GEOADD:
			case HSTRLEN:
			case XLEN:
			case XACK:
			case XDEL:
			case XTRIM:
				return String.valueOf(client.getIntegerReply());
			case SYNC:
			case SUBSCRIBE:
			case PSUBSCRIBE:
				return null;
			case SCAN:
			case SSCAN:
				List<Object> result = client.getObjectMultiBulkReply();
				byte[] newcursor = (byte[]) result.get(0);
			    @SuppressWarnings("unchecked") List<byte[]> rawResults = (List<byte[]>) result.get(1);
			    
			    JSONObject jsonObject = new JSONObject(4);
			    jsonObject.put("cursor",SafeEncoder.encode(newcursor));
			    jsonObject.put("result", BuilderFactory.STRING_SET.build(rawResults));
			    return jsonObject.toJSONString();
			case HSCAN:
			case ZSCAN:
				List<Object> rlt = client.getObjectMultiBulkReply();
				String cor = BuilderFactory.STRING.build(rlt.get(0));
				List<String> rltList = BuilderFactory.STRING_LIST.build(rlt.get(1));
				
				List<Map.Entry<String, String>> targetList = new ArrayList<Map.Entry<String,String>>(rltList.size());
				
				Iterator<String> itor = rltList.iterator();
				while(itor.hasNext()) {
					targetList.add(new AbstractMap.SimpleEntry<>(itor.next(),itor.next()));
				}
				
				JSONObject object = new JSONObject();
				object.put("cursor", cor);
				object.put("result", target);
				return object.toJSONString();
			case SORT:
			case BLPOP:
			case BRPOP:
			case ZRANGEBYSCORE:
			case TIME:
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
