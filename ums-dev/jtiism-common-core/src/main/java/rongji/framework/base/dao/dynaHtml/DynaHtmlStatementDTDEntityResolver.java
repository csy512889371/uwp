package rongji.framework.base.dao.dynaHtml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.Serializable;

/**
 * html generate dtd解析器
 * 
 *
 */
public class DynaHtmlStatementDTDEntityResolver implements EntityResolver, Serializable {

	private static final long serialVersionUID = -2667481543811269180L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DynaHtmlStatementDTDEntityResolver.class);
	private static final String HOP_DYNAMIC_STATEMENT = "http://www.jtiism.com/dtd/";

	public InputSource resolveEntity(String publicId, String systemId) {
		InputSource source = null; // returning null triggers default behavior
		if (systemId != null) {
			LOGGER.debug("trying to resolve system-id [" + systemId + "]");
			if (systemId.startsWith(HOP_DYNAMIC_STATEMENT)) {
				LOGGER.debug("recognized hop dyanmic statement namespace; attempting to resolve on classpath under rongji/framework/base/dao/dynaHtml/");
				source = resolveOnClassPath(publicId, systemId, HOP_DYNAMIC_STATEMENT);
			}
		}
		return source;
	}

	private InputSource resolveOnClassPath(String publicId, String systemId, String namespace) {
		InputSource source = null;
		String path = "rongji/framework/base/dao/dynaHtml/" + systemId.substring(namespace.length());
		InputStream dtdStream = resolveInHibernateNamespace(path);
		if (dtdStream == null) {
			LOGGER.debug("unable to locate [" + systemId + "] on classpath");
			if (systemId.substring(namespace.length()).indexOf("2.0") > -1) {
				LOGGER.error("Don't use old DTDs, read the Hibernate 3.x Migration Guide!");
			}
		} else {
			LOGGER.debug("located [" + systemId + "] in classpath");
			source = new InputSource(dtdStream);
			source.setPublicId(publicId);
			source.setSystemId(systemId);
		}
		return source;
	}

	protected InputStream resolveInHibernateNamespace(String path) {
		return this.getClass().getClassLoader().getResourceAsStream(path);
	}


}
