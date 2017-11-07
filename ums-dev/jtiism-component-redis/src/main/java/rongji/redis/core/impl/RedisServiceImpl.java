package rongji.redis.core.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import rongji.redis.core.RedisService;

@Service(value = "redisServiceImpl")
public class RedisServiceImpl implements RedisService {

	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;

	private static String redisCode = "utf-8";
	
	// 不能用 :true 能用:false
	private boolean canNotUseFlag = false;

	public RedisServiceImpl() {

	}

	private static final Logger LOG = Logger.getLogger(RedisServiceImpl.class);

	/**
	 * @param key
	 */
	public long del(final String... keys) {

		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				for (int i = 0; i < keys.length; i++) {
					result = connection.del(keys[i].getBytes());
				}
				return result;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void set(final byte[] key, final byte[] value, final long liveTime) {
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void set(String key, String value, long liveTime) {
		this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(byte[] key, byte[] value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					byte[] values = connection.get(key.getBytes());
					if (values != null && values.length > 0) {
						return new String(values, redisCode);
					}
				} catch (UnsupportedEncodingException e) {
					LOG.error("redis get error");
					// e.printStackTrace();
				}
				return "";
			}
		});
	}

	public byte[] get(final byte[] key) {
		return redisTemplate.execute(new RedisCallback<byte[]>() {
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] values = connection.get(key);
				return values;
			}
		});
	}

	/**
	 * @param pattern
	 * @return
	 */
	public Set<Serializable> keys(String pattern) {
		return redisTemplate.keys(pattern);

	}

	/**
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(key.getBytes());
			}
		});
	}

	/**
	 * @return
	 */
	public String flushDB() {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * @return
	 */
	public long dbSize() {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	/**
	 * @return
	 */
	public String ping() {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.ping();
			}
		});
	}

	@Override
	public boolean isLive() {
		boolean result = true;
		try {
			ping();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 压栈
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public Long listPush(Serializable key, Serializable value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	@Override
	public Serializable listPop(Serializable key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 入队
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public Long listIn(Serializable key, Serializable value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 出队
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Serializable listOut(Serializable key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 栈/队列长
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Long listLength(Serializable key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 范围检索
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public List<Serializable> listRange(Serializable key, int start, int end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 移除
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	@Override
	public void listRemove(Serializable key, long i, Serializable value) {
		redisTemplate.opsForList().remove(key, i, value);
	}

	/**
	 * 检索
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	@Override
	public Serializable listIndex(Serializable key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 置值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	@Override
	public void listSet(Serializable key, long index, Serializable value) {
		redisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * 裁剪
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	@Override
	public void listTrim(Serializable key, long start, int end) {
		redisTemplate.opsForList().trim(key, start, end);
	}
	
	/**
	 * 设置过期时间
	 * @param key
	 * @param liveTime 单位秒
	 */
	public void expire(final byte[] key, final long liveTime) {
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}
	
	/**
	 * 
	 * @Title: canNotUse
	 * @return 不能用 true 能用 false
	 * 
	 */
	public boolean canNotUse(boolean isNeedRedis) {
		try {
			// 配置项目 是否使用redis
			if (!isNeedRedis) {
				return true;
			}

			// 通过定时器来改变状态
			if (!canNotUseFlag) {
				// redis 不存活
				if (!this.isLive()) {
					canNotUseFlag = true;
					return true;
				}
				return false;
			}
		} catch (Exception e) {
			// 异常
			canNotUseFlag = true;
		}

		return true;
	}

	public boolean isCanNotUseFlag() {
		return canNotUseFlag;
	}

	public void setCanNotUseFlag(boolean canNotUseFlag) {
		this.canNotUseFlag = canNotUseFlag;
	}
	
	

}