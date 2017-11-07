package rongji.framework.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class ExChangeEntityUntils {
	private static final String FILENAME = "/resources/converse.xml";
	private static final Logger logger = LoggerFactory.getLogger(ExChangeEntityUntils.class);
	private static Element root;
	static {
		try {
			File cmisXmlFile = new ClassPathResource(FILENAME).getFile();
			Document document = new SAXReader().read(cmisXmlFile);
			root = document.getRootElement().element("transfrom");
		} catch (DocumentException | IOException e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * 
	 * @Title: ExChangeAEntity
	 * @Description: (目标库到cmis的单个实体数据转换)
	 * @param target
	 * @param cmis
	 * @param targetEntity
	 * @return
	 * @throws Exception
	 * @return Object 返回类型
	 * @throws
	 * @author spongebob
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Object aTargetEntityToCmisEntity(Class<?> target, Class<?> cmis, Object targetEntity) throws Exception {
		Element rootEntity = getEntityProperty(target.getSimpleName(), cmis.getSimpleName());
		List columnNodes = rootEntity.elements("column");
		Object cmisObj = targetEntityToCmisEntity(cmis, targetEntity, columnNodes);
		return cmisObj;
	}

	/**
	 * 
	 * @Title: ExChangeEntitys
	 * @Description: (目标库实体到cmis库实体集的数据转换)
	 * @param target
	 * @param cmis
	 * @param targetEntitys
	 * @return
	 * @throws Exception
	 * @return List<Object> 返回类型
	 * @throws
	 * @author spongebob
	 */
	@SuppressWarnings({ "rawtypes" })
	public static List<Object> listTargetEntityToCmiss(Class<?> target, Class<?> cmis, List<Object> targetEntitys) throws Exception {
		Element rootEntity = getEntityProperty(target.getSimpleName(), cmis.getSimpleName());
		List columnNodes = rootEntity.elements("column");

		List<Object> cmisObjs = new ArrayList<Object>();

		for (Object targetEntity : targetEntitys) {
			Object cmisObj = targetEntityToCmisEntity(cmis, targetEntity, columnNodes);
			cmisObjs.add(cmisObj);
		}

		return cmisObjs;
	}

	/**
	 * 
	 * @Title: aCmistEntityToTargeEntity
	 * @Description: (单个cmis库实体到目标库实体的数据转换)
	 * @param target
	 * @param cmis
	 * @param cmisEntity
	 * @return
	 * @throws Exception
	 * @return Object 返回类型
	 * @throws
	 * @author spongebob
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Object aCmistEntityToTargeEntity(Class<?> target, Class<?> cmis, Object cmisEntity) throws Exception {
		Element rootEntity = getEntityProperty(target.getSimpleName(), cmis.getSimpleName());
		List columnNodes = rootEntity.elements("column");
		Object cmisObj = cmisEntityToTargetEntity(target, cmisEntity, columnNodes);
		return cmisObj;
	}

	/**
	 * 
	 * @Title: listCmisEntityToTargets
	 * @Description: (cmis库实体集到目标库实体集的数据转换)
	 * @param target
	 * @param cmis
	 * @param cmisEntitys
	 * @return
	 * @throws Exception
	 * @return List<Object> 返回类型
	 * @throws
	 * @author spongebob
	 */
	@SuppressWarnings({ "rawtypes" })
	public static List<Object> listCmisEntityToTargets(Class<?> target, Class<?> cmis, List<Object> cmisEntitys) throws Exception {
		Element rootEntity = getEntityProperty(target.getSimpleName(), cmis.getSimpleName());
		List columnNodes = rootEntity.elements("column");

		List<Object> targetObjs = new ArrayList<Object>();

		for (Object cmisEntity : cmisEntitys) {
			Object targetObj = cmisEntityToTargetEntity(target, cmisEntity, columnNodes);
			targetObjs.add(targetObj);
		}

		return targetObjs;
	}

	/**
	 * 
	 * @Title: targetEntityToCmisEntity
	 * @Description: (目标库实体转化成cmis库实体)
	 * @param cmis
	 * @param targetEntity
	 * @param columnNodes
	 * @return
	 * @throws Exception
	 * @return Object 返回类型
	 * @throws
	 * @author spongebob
	 */
	@SuppressWarnings("rawtypes")
	private static Object targetEntityToCmisEntity(Class<?> cmis, Object targetEntity, List columnNodes) throws Exception {
		Object cmisObj = cmis.newInstance();
		for (Iterator it = columnNodes.iterator(); it.hasNext();) {
			Element column = (Element) it.next();
			String dateFormate = column.attribute("dateFormate").getText();// 时间类型
			if (dateFormate != null && !dateFormate.equals("")) {
				SimpleDateFormat format = new SimpleDateFormat(dateFormate);
				Object val = PropertyUtils.getProperty(targetEntity, column.attribute("targetField").getText());
				PropertyUtils.setProperty(cmisObj, column.attribute("cmisField").getText(), format.format(val));
			} else {
				Object val = PropertyUtils.getProperty(targetEntity, column.attribute("targetField").getText());
				PropertyUtils.setProperty(cmisObj, column.attribute("cmisField").getText(), val);
			}

		}
		return cmisObj;
	}

	/**
	 * 
	 * @Title: cmisEntityToTargetEntity
	 * @Description: (cmis库实体转化成目标库实体)
	 * @param target
	 * @param cmisEntity
	 * @param columnNodes
	 * @return
	 * @throws Exception
	 * @return Object 返回类型
	 * @throws
	 * @author spongebob
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private static Object cmisEntityToTargetEntity(Class<?> target, Object cmisEntity, List columnNodes) throws Exception {
		Object targetObj = target.newInstance();
		for (Iterator it = columnNodes.iterator(); it.hasNext();) {
			Element column = (Element) it.next();
			String dateFormate = column.attribute("dateFormate").getText();// 时间类型
			if (dateFormate != null && !dateFormate.equals("")) {
				SimpleDateFormat format = new SimpleDateFormat(dateFormate);
				Object val = PropertyUtils.getProperty(cmisEntity, column.attribute("cmisField").getText());
				PropertyUtils.setProperty(targetObj, column.attribute("targetField").getText(), format.parse(String.valueOf(val)));
			} else {
				Object val = PropertyUtils.getProperty(cmisEntity, column.attribute("cmisField").getText());

			}

		}
		return targetObj;
	}

	@SuppressWarnings("rawtypes")
	private static Element getEntityProperty(String targetName, String cmisName) {
		List nodes = root.elements("entity");
		for (Iterator it = nodes.iterator(); it.hasNext();) {
			Element elm = (Element) it.next();
			if (elm.attribute("targetEntity").getText().equals(targetName) && elm.attribute("cmisEntity").getText().equals(cmisName)) {
				return elm;
			}
		}
		return null;
	}
}
