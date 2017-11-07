package rongji.cmis.service.common.impl;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import rongji.cmis.service.common.RSAService;
import rongji.framework.util.RSAUtils;

/**
 * Service - RSA安全
 * 
 * @version 1.0
 */
@Service("rsaServiceImpl")
public class RSAServiceImpl implements RSAService {
	
	private static final Logger logger = LoggerFactory.getLogger(RSAServiceImpl.class);

	/** "私钥"参数名称 */
	private static final String PRIVATE_KEY_ATTRIBUTE_NAME = "privateKey";

	@Transactional(readOnly = true)
	public RSAPublicKey generateKey(HttpServletRequest request) {
		RSAPublicKey publicKey = null;
		try {
			Assert.notNull(request);
			KeyPair keyPair = RSAUtils.generateKeyPair();
			publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			HttpSession session = request.getSession();
			session.setAttribute(PRIVATE_KEY_ATTRIBUTE_NAME, privateKey);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return publicKey;
	}

	@Transactional(readOnly = true)
	public void removePrivateKey(HttpServletRequest request) {
		try {
			Assert.notNull(request);
			HttpSession session = request.getSession();
			session.removeAttribute(PRIVATE_KEY_ATTRIBUTE_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Transactional(readOnly = true)
	public String decryptParameter(String name, HttpServletRequest request) {
		Assert.notNull(request);
		try {
			if (name != null) {
				HttpSession session = request.getSession();
				RSAPrivateKey privateKey = (RSAPrivateKey) session.getAttribute(PRIVATE_KEY_ATTRIBUTE_NAME);
				String parameter = request.getParameter(name);
				if (privateKey != null && StringUtils.isNotEmpty(parameter)) {
					return RSAUtils.decrypt(privateKey, parameter);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}