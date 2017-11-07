package rongji.framework.util;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import rongji.cmis.model.sys.SysConfig;
import rongji.cmis.service.common.SysConfigService;
import rongji.framework.base.CommonAttributes;
import rongji.framework.base.pojo.EnumConverter;
import rongji.framework.base.pojo.Setting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Utils - 系统设置
 * 
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public final class SettingUtils {

	private static final Logger logger = LoggerFactory.getLogger(SettingUtils.class);

	/** CacheManager */
	private static final CacheManager cacheManager = CacheManager.create();

	/** BeanUtilsBean */
	private static final BeanUtilsBean beanUtils;

	static {

		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean() {
			@Override
			public String convert(Object value) {
				if (value != null) {
					Class<?> type = value.getClass();
					if (type.isEnum() && super.lookup(type) == null) {
						super.register(new EnumConverter(type), type);
					} else if (type.isArray() && type.getComponentType().isEnum()) {
						if (super.lookup(type) == null) {
							ArrayConverter arrayConverter = new ArrayConverter(type, new EnumConverter(type.getComponentType()), 0);
							arrayConverter.setOnlyFirstToString(false);
							super.register(arrayConverter, type);
						}
						Converter converter = super.lookup(type);
						return ((String) converter.convert(String.class, value));
					}
				}
				return super.convert(value);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum() && super.lookup(clazz) == null) {
					super.register(new EnumConverter(clazz), clazz);
				}
				return super.convert(value, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String[] values, Class clazz) {
				if (clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null) {
					super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
				}
				return super.convert(values, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Object value, Class targetType) {
				if (super.lookup(targetType) == null) {
					if (targetType.isEnum()) {
						super.register(new EnumConverter(targetType), targetType);
					} else if (targetType.isArray() && targetType.getComponentType().isEnum()) {
						ArrayConverter arrayConverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
						arrayConverter.setOnlyFirstToString(false);
						super.register(arrayConverter, targetType);
					}
				}
				return super.convert(value, targetType);
			}
		};

		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		convertUtilsBean.register(dateConverter, Date.class);
		beanUtils = new BeanUtilsBean(convertUtilsBean);
	}

	/**
	 * 不可实例化
	 */
	private SettingUtils() {
	}

	/**
	 * 获取系统设置
	 * 
	 * @return 系统设置
	 */
	public static Setting get() {
		Ehcache cache = cacheManager.getEhcache(Setting.CACHE_NAME);
		net.sf.ehcache.Element cacheElement = cache.get(Setting.CACHE_KEY);
		Setting setting;
		if (cacheElement != null) {
			setting = (Setting) cacheElement.getObjectValue();
		} else {
			setting = new Setting();
			try {
				// 读取XML中的设置
				File cmisXmlFile = new ClassPathResource(CommonAttributes.CMIS_XML_PATH).getFile();
				Document document = new SAXReader().read(cmisXmlFile);
				List<Element> elements = document.selectNodes("/cmis/setting");
				for (Element element : elements) {
					String name = element.attributeValue("name");
					String value = element.attributeValue("value");
					try {
						beanUtils.setProperty(setting, name, value);
					} catch (IllegalAccessException e) {
						logger.error(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						logger.error(e.getMessage(), e);
					}
				}
				// 读取系统设置表设置 覆盖XML设置
				SysConfigService sysConfigService = SpringUtils.getBean("sysConfigServiceImpl", SysConfigService.class);
				List<SysConfig> configs = sysConfigService.findAll();
				for (SysConfig config : configs) {
					String name = config.getName();
					String value = config.getValue();
					try {
						beanUtils.setProperty(setting, name, StringUtil.fomartEmpty(value));
					} catch (IllegalAccessException e) {
						logger.error(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						logger.error(e.getMessage(), e);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			cache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY, setting));
		}
		return setting;
	}

	/**
	 * 只更新 value 不为空的 属性
	 */
	public static void setIgnoreNull(Setting setting) {
		set(setting, true);
	}

	/**
	 * 更新全部的 setting 值
	 */
	public static void set(Setting setting) {
		set(setting, false);
	}

	/**
	 * 设置系统设置
	 * 
	 * @param setting
	 *            系统设置
	 */
	private static void set(Setting setting, boolean ignoreNull) {
		try {
			File cmisXmlFile = new ClassPathResource(CommonAttributes.CMIS_XML_PATH).getFile();
			Document document = new SAXReader().read(cmisXmlFile);

			upDateElements(setting, document, ignoreNull);

			FileOutputStream fileOutputStream = null;
			XMLWriter xmlWriter = null;
			try {
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("UTF-8");
				outputFormat.setIndent(true);
				outputFormat.setIndent("	");
				outputFormat.setNewlines(true);
				fileOutputStream = new FileOutputStream(cmisXmlFile);
				xmlWriter = new XMLWriter(fileOutputStream, outputFormat);
				xmlWriter.write(document);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (xmlWriter != null) {
					try {
						xmlWriter.close();
					} catch (IOException e) {
					}
				}
				IOUtils.closeQuietly(fileOutputStream);
			}

			Ehcache cache = cacheManager.getEhcache(Setting.CACHE_NAME);
			cache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY, setting));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static void upDateElements(Setting setting, Document document, boolean ignoreNull) {

		List<Element> elements = document.selectNodes("/cmis/setting");
		for (Element element : elements) {
			try {
				String name = element.attributeValue("name");
				String value = beanUtils.getProperty(setting, name);

				// 对于参数是 null的 不做处理
				if (ignoreNull && StringUtil.isEmpty(value)) {
					continue;
				}

				Attribute attribute = element.attribute("value");
				attribute.setValue(value);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static void clear() {
		Ehcache cache = cacheManager.getEhcache(Setting.CACHE_NAME);
		cache.remove(Setting.CACHE_KEY);
	}

}