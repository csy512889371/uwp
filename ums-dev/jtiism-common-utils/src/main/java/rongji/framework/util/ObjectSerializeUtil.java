package rongji.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ObjectSerializeUtil {
	
	public static Object decodeObject(byte[] b){
		return deserialize(decompress(b));
	}
	
	public static byte[] encodeObject(Object o){
		byte[] b = serialize(o);
		return compress(b);
	}
	private static byte[] serialize(Object o){
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
//			((StandardSession)o).
			oos.writeObject(o);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(bos!=null)
					bos.close();
				if(oos!=null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}
	
	private static Object deserialize(byte[] in){
		Object o = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try{
			bis = new ByteArrayInputStream(in);
			ois = new ObjectInputStream(bis);
			o = (Object)ois.readObject();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}finally{
			try {
				if(bis!=null)
					bis.close();
				if(ois!=null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return o;
	}
	
	private static byte[] compress(byte[] in){
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(bos);
			gos.write(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(bos!=null)
					bos.close();
				if(gos!=null)
					gos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}
	
	private static byte[] decompress(byte[] in){
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		if(in!=null){
			ByteArrayInputStream bis = new ByteArrayInputStream(in);
			bos = new ByteArrayOutputStream();
			GZIPInputStream gis = null;
			try {
				gis = new GZIPInputStream(bis);
				byte[] buf = new byte[8192];
				int r = -1;
				while((r = gis.read(buf))>0){
					bos.write(buf,0,r);
				}
				bytes = bos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {
					if(bis!=null)
						bis.close();
					if(gis!=null)
						gis.close();
					if(bos!=null)
						bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}
	
}
