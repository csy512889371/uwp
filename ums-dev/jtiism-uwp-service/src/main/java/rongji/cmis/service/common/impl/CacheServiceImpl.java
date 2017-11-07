package rongji.cmis.service.common.impl;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import rongji.cmis.service.common.CacheService;
import rongji.framework.util.SettingUtils;
import freemarker.template.TemplateModelException;

/**
 * Service - 缓存
 * 
 * @version 1.0
 */
@Service("cacheServiceImpl")
public class CacheServiceImpl implements CacheService {

	private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;

	@Resource(name = "messageSource")
	private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	public String getDiskStorePath() {
		return cacheManager.getConfiguration().getDiskStoreConfiguration().getPath();
	}

	public int getCacheSize() {
		int cacheSize = 0;
		String[] cacheNames = cacheManager.getCacheNames();
		if (cacheNames != null) {
			for (String cacheName : cacheNames) {
				Ehcache cache = cacheManager.getEhcache(cacheName);
				if (cache != null) {
					cacheSize += cache.getSize();
				}
			}
		}
		return cacheSize;
	}

	@CacheEvict(value = { "setting", "template", "logConfig", "coder", "fieldInfo", "dataDict", "authorization", "loginTickets" }, allEntries = true)
	public void clear() {
		reloadableResourceBundleMessageSource.clearCache();
		try {
			freeMarkerConfigurer.getConfiguration().setSharedVariable("setting", SettingUtils.get());
		} catch (TemplateModelException e) {
			logger.error("update setting variable for freeMarker fail!", e);
		}
		freeMarkerConfigurer.getConfiguration().clearTemplateCache();
	}

}