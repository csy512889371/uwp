package rongji.redis.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @Title: ListTranscoder.java
* @Description: 将Object 序列化 和 反序列化 （序列化后的数据 保持到 redis）
* @version V1.0
 */
public class ObjectsTranscoder<M extends Serializable> extends SerializeTranscoder {

	private static final Logger logger = LoggerFactory.getLogger(ObjectsTranscoder.class);

	@SuppressWarnings("unchecked")
	@Override
	public byte[] serialize(Object value) {
		if (value == null) {
			throw new NullPointerException("Can't serialize null");
		}
		byte[] result = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			M m = (M) value;
			os.writeObject(m);
			os.close();
			bos.close();
			result = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			close(os);
			close(bos);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public M deserialize(byte[] in) {
		M result = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				result = (M) is.readObject();
				is.close();
				bis.close();
			}
		} catch (IOException e) {
			logger.error(String.format("Caught IOException decoding bytes of data"), e);
		} catch (ClassNotFoundException e) {
			logger.error(String.format("Caught CNFE decoding bytes of data"), e);
		} finally {
			close(is);
			close(bis);
		}
		return result;
	}
}
