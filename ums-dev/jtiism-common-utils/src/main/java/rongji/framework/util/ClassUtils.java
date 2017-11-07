package rongji.framework.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rongji.framework.base.model.FieldProxy;

public final class ClassUtils {

	private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

	private static final String ALLFIELD = "ALL";
	private static final String RENEWABLEFIELD = "NEW";
	private static final String IDFIELD = "ID";
	private static final String TREEFIELD = "TREE";
	private static final String READ = "READ";
	private static final String WRITE = "WRITE";
	private static final Set<Class<? extends Object>> RESETCLASSSET = new HashSet<Class<? extends Object>>();
	private static Map<Class<?>, Map<String, Field[]>> cacheClass = new HashMap<Class<?>, Map<String, Field[]>>();

	private static Map<Class<?>, Map<String, Map<String, Method[]>>> cacheClassMethod = new HashMap<Class<?>, Map<String, Map<String, Method[]>>>();

	public static FieldProxy getDeclareFieldProxy(Class<?> clazz, String fileName) {
		Field[] fieldArr = cacheClass.get(clazz).get(ALLFIELD);
		Method[] readArr = cacheClassMethod.get(clazz).get(ALLFIELD).get(READ);
		Method[] writeArr = cacheClassMethod.get(clazz).get(ALLFIELD).get(WRITE);
		for (int i = 0; i < fieldArr.length; i++) {
			Field f = fieldArr[i];
			if (fileName.equals(f.getName())) {
				return new FieldProxy(fieldArr[i], readArr[i], writeArr[i]);
			}
		}
		return null;
	}

	public static Field getDeclareField(Class<?> clazz, String fileName) {
		Field[] fieldArr = cacheClass.get(clazz).get(ALLFIELD);
		for (int i = 0; i < fieldArr.length; i++) {
			Field f = fieldArr[i];
			if (fileName.equals(f.getName())) {
				return fieldArr[i];
			}
		}
		return null;
	}

	/**
	 * 获得改class所有属性包括父类属性
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getAllDeclareField(Class<?> clazz) {
		return cacheClass.get(clazz).get(ALLFIELD);
	}

	public static Method[] getAllDeclareFieldReadMethod(Class<?> clazz) {
		return cacheClassMethod.get(clazz).get(ALLFIELD).get(READ);
	}

	public static Method[] getAllDeclareFieldWriteMethod(Class<?> clazz) {
		return cacheClassMethod.get(clazz).get(ALLFIELD).get(WRITE);
	}

	/*	*//**
	 * 获得class所有标有renewable注解的属性，包括父类
	 * 
	 * @param clazz
	 * @return
	 */
	/*
	 * public static Field[] getRenewableField(Class<?> clazz){ return
	 * cacheClass.get(clazz).get(RENEWABLEFIELD); }
	 * 
	 * public static Method[] getRenewableReadMethod(Class<?> clazz){ return
	 * cacheClassMethod.get(clazz).get(RENEWABLEFIELD).get(READ); } public
	 * static Method[] getRenewableWriteMethod(Class<?> clazz){ return
	 * cacheClassMethod.get(clazz).get(RENEWABLEFIELD).get(WRITE); }
	 */

	public static Field getTreeTextField(Class<?> clazz) {
		Field[] fs = cacheClass.get(clazz).get(TREEFIELD);
		if (fs.length == 0) {
			return null;
		}
		return fs[0];
	}

	public static Method getTreeTextReadMethod(Class<?> clazz) {
		Method[] ms = cacheClassMethod.get(clazz).get(TREEFIELD).get(READ);
		if (ms.length == 0) {
			return null;
		}
		return ms[0];
	}

	public static Method getTreeTextWriteMethod(Class<?> clazz) {
		Method[] ms = cacheClassMethod.get(clazz).get(TREEFIELD).get(WRITE);
		if (ms.length == 0) {
			return null;
		}
		return ms[0];
	}

	/**
	 * 获得id属性
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field getIdField(Class<?> clazz) {
		return cacheClass.get(clazz).get(IDFIELD)[0];
	}

	public static Method getIdReadMethod(Class<?> clazz) {
		return cacheClassMethod.get(clazz).get(IDFIELD).get(READ)[0];
	}

	public static Method getIdWriteMethod(Class<?> clazz) {
		return cacheClassMethod.get(clazz).get(IDFIELD).get(WRITE)[0];
	}

	public static boolean hasRestIdField(Class<? extends Object> clazz) {
		return RESETCLASSSET.contains(clazz);
	}

	public static void initClassProperty(String path) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
		logger.info("初始化model缓存-----------------开始");
		/*
		 * Set<Class<?>> sets = ClassScanUtils.getClasses(path); for(Class<?>
		 * clazz : sets){ if(!clazz.isAnnotationPresent(Entity.class)){
		 * continue; } clazz.getAnnotation(Entity.class); List<Field> allList=
		 * new LinkedList<Field>(); List<Field> newList= new
		 * LinkedList<Field>(); List<Method> newReadList= new
		 * LinkedList<Method>(); List<Method> newWriteList= new
		 * LinkedList<Method>(); List<Method> allReadList= new
		 * LinkedList<Method>(); List<Method> allWriteList= new
		 * LinkedList<Method>();
		 * 
		 * List<Field> treeList= new LinkedList<Field>(); List<Method>
		 * treeReadList= new LinkedList<Method>(); List<Method> treeWriteList=
		 * new LinkedList<Method>();
		 * 
		 * Method idReadMethod = null; Method idWriteMethod = null; Field
		 * idField = null; Class<?> selfClass = clazz; do{ for(Field field :
		 * clazz.getDeclaredFields()){
		 * //if(field.getAnnotations().length==0||field
		 * .getAnnotation(Transient.class)!=null){
		 * if(field.getAnnotations().length
		 * ==0||(field.getAnnotation(Transient.class)!=null &&
		 * field.getAnnotations().length==1)){ continue; }
		 * if(field.getName().equals("restId")){ RESETCLASSSET.add(selfClass); }
		 * PropertyDescriptor pd = null; try { pd =
		 * PropertyUtils.getPropertyDescriptor(clazz.newInstance(),
		 * field.getName()); } catch (Exception e) {
		 * System.out.println(field.getName()+":错误"); throw e; }
		 * 
		 * if(field.getAnnotation(Id.class)!=null ||
		 * field.getAnnotation(EmbeddedId.class)!=null){ idField = field;
		 * idReadMethod = pd.getReadMethod(); idWriteMethod =
		 * pd.getWriteMethod(); }
		 * if(field.getAnnotation(Renewable.class)!=null){ newList.add(field);
		 * newReadList.add(pd.getReadMethod());
		 * newWriteList.add(pd.getWriteMethod()); }
		 * if(field.getAnnotation(TreeText.class)!=null){ treeList.add(field);
		 * treeReadList.add(pd.getReadMethod());
		 * treeWriteList.add(pd.getWriteMethod()); } allList.add(field);
		 * allReadList.add(pd.getReadMethod());
		 * allWriteList.add(pd.getWriteMethod()); }
		 * }while(clazz.getSuperclass()!=null && !(clazz =
		 * clazz.getSuperclass()).isAssignableFrom(Object.class)); if(idField ==
		 * null && !selfClass.isAnnotationPresent(Embeddable.class)){ continue;
		 * } Map<String,Field[]> map = new HashMap<String, Field[]>();
		 * map.put(ALLFIELD, allList.toArray(new Field[]{}));
		 * map.put(RENEWABLEFIELD, newList.toArray(new Field[]{}));
		 * map.put(TREEFIELD, treeList.toArray(new Field[]{})); map.put(IDFIELD,
		 * new Field[]{idField});
		 * 
		 * Map<String,Map<String,Method[]>> methodMap = new HashMap<String,
		 * Map<String,Method[]>>(); Map<String,Method[]> map1 = new
		 * HashMap<String, Method[]>(); map1.put(READ, allReadList.toArray(new
		 * Method[]{})); map1.put(WRITE, allWriteList.toArray(new Method[]{}));
		 * methodMap.put(ALLFIELD, map1);
		 * 
		 * 
		 * Map<String,Method[]> map2 = new HashMap<String, Method[]>();
		 * map2.put(READ, newReadList.toArray(new Method[]{})); map2.put(WRITE,
		 * newWriteList.toArray(new Method[]{})); methodMap.put(RENEWABLEFIELD,
		 * map2);
		 * 
		 * Map<String,Method[]> map3 = new HashMap<String, Method[]>();
		 * map3.put(READ, new Method[]{idReadMethod}); map3.put(WRITE, new
		 * Method[]{idWriteMethod}); methodMap.put(IDFIELD, map3);
		 * 
		 * Map<String,Method[]> map4 = new HashMap<String, Method[]>();
		 * map4.put(READ, treeReadList.toArray(new Method[]{})); map4.put(WRITE,
		 * treeWriteList.toArray(new Method[]{})); methodMap.put(TREEFIELD,
		 * map4);
		 * 
		 * cacheClassMethod.put(selfClass, methodMap); cacheClass.put(selfClass,
		 * map); }
		 */
		logger.info("初始化model缓存-----------------结束");
	}

	/**
	 * 
	 * @Title: makeObject1ToObject2
	 * @Description: 将对象1转换为对象2，并给相同属性字段复制
	 * @param object1
	 * @param object2
	 * @return
	 * @return Object 返回类型
	 * @throws
	 * @author LFG
	 */
	public static Object makeObject1ToObject2(Object object1, Object object2) {
		Field[] fields1 = object1.getClass().getDeclaredFields();
		Field[] fields2 = object2.getClass().getDeclaredFields();
		if (null != fields1 && null != fields2) {
			for (Field field1 : fields1) {
				try {
					String name1 = field1.getName();
					PropertyDescriptor pd = new PropertyDescriptor(name1, object1.getClass());
					Method getMethod = pd.getReadMethod();
					Object ohh = getMethod.invoke(object1);
					for (Field field2 : fields2) {
						String name2 = field2.getName();
						if (name1.equals(name2)) {
							PropertyDescriptor pd2 = new PropertyDescriptor(name2, object2.getClass());
							Method writeMethod = pd2.getWriteMethod();
							writeMethod.invoke(object2, ohh);
						}
					}
				} catch (Exception e) {
					logger.info(e.getMessage(), e);
				}

			}
		}

		return object2;
	}
}
