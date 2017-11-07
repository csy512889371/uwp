package rongji.redis.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @Title: ListTranscoder.java
* @Description: 将list 序列化 和 反序列化（序列化后的数据 保持到 redis）
* @version V1.0
 */
public class ListTranscoder<M extends Serializable> extends SerializeTranscoder {

	private static final Logger logger = LoggerFactory.getLogger(ListTranscoder.class);

	@SuppressWarnings("unchecked")
	public List<M> deserialize(byte[] in) {

		List<M> list = new ArrayList<>();

		ByteArrayInputStream bis = null;

		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				while (true) {
					M m = (M) is.readObject();
					if (m == null) {
						break;
					}

					list.add(m);
				}
			}
		} catch (EOFException e) {
			logger.info("Read file Done!", e);
		} catch (IOException e) {
			logger.error("Caught IOException decoding bytes of data", e);
		} catch (ClassNotFoundException e) {
			logger.error("Caught CNFE decoding bytes of data", e);
		} finally {
			close(is);
			close(bis);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] serialize(Object value) {

		if (value == null) {
			throw new NullPointerException("Can't serialize null");
		}

		List<M> values = (List<M>) value;

		byte[] results = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;

		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			for (M m : values) {
				os.writeObject(m);
			}

			// 增加null 对象作为 反序列化结尾的标志
			os.writeObject(null);
			os.close();
			bos.close();
			results = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			close(os);
			close(bos);
		}

		return results;
	}

}