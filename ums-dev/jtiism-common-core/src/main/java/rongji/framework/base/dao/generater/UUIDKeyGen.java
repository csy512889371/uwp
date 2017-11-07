package rongji.framework.base.dao.generater;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rongji.framework.util.SpringUtils;

public class UUIDKeyGen implements IdentifierGenerator {

	private static final Logger logger = LoggerFactory.getLogger(UUIDKeyGen.class);

	@Override
	public Serializable generate(SessionImplementor paramSessionImplementor, Object paramObject) throws HibernateException {

		String idval = null;
		try {
			SessionFactory sessionFactory = (SessionFactory) SpringUtils.getBean("sessionFactory");
			ClassMetadata meta = sessionFactory.getClassMetadata(paramObject.getClass());

			idval = (String) PropertyUtils.getProperty(paramObject, meta.getIdentifierPropertyName());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (StringUtils.isEmpty(idval)) {
			idval = UUID.randomUUID().toString();
		}

		return idval;
	}

}
