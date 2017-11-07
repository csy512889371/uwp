package rongji.redis.core;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface RedisService {

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	long del(String... keys);

	/**
	 * 添加key value 并且设置存活时间(byte)
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	void set(byte[] key, byte[] value, long liveTime);

	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 *            单位秒
	 */
	void set(String key, String value, long liveTime);

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param value
	 */
	void set(String key, String value);

	/**
	 * 添加key value (字节)(序列化)
	 * 
	 * @param key
	 * @param value
	 */
	void set(byte[] key, byte[] value);

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	String get(String key);

	byte[] get(final byte[] key);

	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @return
	 */
	Set<Serializable> keys(String pattern);

	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	boolean exists(String key);

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	public abstract String flushDB();

	/**
	 * 查看redis里有多少数据
	 */
	public abstract long dbSize();

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	public abstract String ping();

	/**
	 * 判断redis服务器是否存活
	 * 
	 * @return
	 */
	public boolean isLive();

	/**
	 * 压栈
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long listPush(Serializable key, Serializable value);

	/**
	 * 出栈
	 * 
	 * @param key
	 * @return
	 */
	public Serializable listPop(Serializable key);

	/**
	 * 入队
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long listIn(Serializable key, Serializable value);

	/**
	 * 出队
	 * 
	 * @param key
	 * @return
	 */
	public Serializable listOut(Serializable key);

	/**
	 * 栈/队列长
	 * 
	 * @param key
	 * @return
	 */
	public Long listLength(Serializable key);

	/**
	 * 范围检索
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Serializable> listRange(Serializable key, int start, int end);

	/**
	 * 移除
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	public void listRemove(Serializable key, long i, Serializable value);

	/**
	 * 检索
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public Serializable listIndex(Serializable key, long index);

	/**
	 * 置值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public void listSet(Serializable key, long index, Serializable value);

	/**
	 * 裁剪
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void listTrim(Serializable key, long start, int end);
	
	
	/**
	 * 设置过期时间
	 * @param key
	 * @param liveTime 单位秒
	 */
	public void expire(final byte[] key, final long liveTime);
	
	/**
	 * 是否可用
	 * @return
	 */
	public boolean canNotUse(boolean isNeedRedis);
	
	public void setCanNotUseFlag(boolean canNotUseFlag);

}