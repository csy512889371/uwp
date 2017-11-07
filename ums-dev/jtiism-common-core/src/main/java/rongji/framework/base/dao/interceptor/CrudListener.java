package rongji.framework.base.dao.interceptor;

import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rongji.framework.base.model.LogConfig;
import rongji.framework.util.DateUtil;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.SpringUtils;
import rongji.framework.util.StringUtil;

/**
 * Hibernate增删改监听器，记录审记日志 类描述：post方法在数据更新后执行,pre方法在数据更新前执行
 * 
 * @Title: CrudListener.java
 * @Package rongji.framework.base.dao.interceptor
 * @author chenshiying
 * @date 2016年6月2日 上午9:43:05
 * @version V1.0
 */
public class CrudListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

	/** 默认忽略参数 */
	private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] { "password", "rePassword", "currentPassword", "briefInfo", "a1701", "atschoolResume" };

	/** 忽略参数 */
	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

	private static final long serialVersionUID = 1L;

	private static final String INSERT = "INSERT";

	private static final String UPDATE = "UPDATE";

	private static final String DELETE = "DELETE";

	/**
	 * 允许或不允许全部时，指定all即可
	 */
	public static final String ALL = "all";

	private static final Logger logger = LoggerFactory.getLogger(CrudListener.class);

	/**
	 * 配置审计对象的记录策略
	 */
	private Map<String, Map<String, String>> auditableEntitys;

	/**
	 * 审计日志服务类
	 */
/*	private LogComputeRecService logComputeRecService;*/

	@Override
	public void onPostInsert(PostInsertEvent event) {/*
		try {
			init();
			if (auditableEntitys.containsKey(event.getEntity().getClass().getSimpleName())) {
				// 保存 插入日志中间表
				LogComputeRec logComputeRec = newAuditLogCR();
				logComputeRec.setOperateSet(event.getEntity().getClass().getSimpleName());
				logComputeRec.setOperateType(LogComputeRec.OperateType.insert);
				{
					Object[] state = event.getState();
					logComputeRec.setDataId((String) event.getId());
					String[] fields = event.getPersister().getPropertyNames();
					Map<String, String> newValMap = new HashMap<String, String>();
					Boolean findOObj = false;
					if (state != null && fields != null && state.length == fields.length) {
						if ("A01".equals(logComputeRec.getOperateSet().toUpperCase()) && event.getEntity() instanceof A01) {
							A01 a01 = (A01) event.getEntity();
							logComputeRec.setOperateObj(a01.getA0101());
							findOObj = true;
						}
						//
						int length=0;
						for (int i = 0; i < fields.length; i++) {
							if (!findOObj && "A01".equals(fields[i].toUpperCase()) && state[i] instanceof A01) {
								A01 a01 = (A01) state[i];
								logComputeRec.setOperateObj(a01.getA0101());
								findOObj = true;
							}
							if (isLog(event.getEntity(), fields[i], INSERT)) {
								if (isNeed(state[i])) {
									// 集合情況
									if (state[i] instanceof Collection) {
										continue;
									}
									String codeStr=valToEncoderStr(state[i]);
									if(StringUtil.isNotEmpty(codeStr)){
										length+=codeStr.length();
										if(length>2800){
											break;
										}
									}
									newValMap.put(fields[i],codeStr);
								}
							}
						}
						logComputeRec.setNewValMap(FastjsonUtils.toJson(newValMap));
					}
				}
				insertLogRc(logComputeRec);
			}
		} catch (Exception e) {
			logger.debug("onPostInsert error!", e);
		}

	*/}

	@Override
	public void onPostDelete(PostDeleteEvent event) {/*
		try {
			init();
			if (auditableEntitys.containsKey(event.getEntity().getClass().getSimpleName())) {
				// 保存 插入日志中间表
				LogComputeRec logComputeRec = newAuditLogCR();
				logComputeRec.setOperateSet(event.getEntity().getClass().getSimpleName());
				logComputeRec.setDataId((String) event.getId());
				logComputeRec.setOperateType(LogComputeRec.OperateType.delete);
				{
					Object[] state = event.getDeletedState();
					String[] fields = event.getPersister().getPropertyNames();
					Map<String, String> oldValMap = new HashMap<String, String>();
					Boolean findOObj = false;
					if (state != null && fields != null && state.length == fields.length) {
						if ("A01".equals(logComputeRec.getOperateSet().toUpperCase()) && event.getEntity() instanceof A01) {
							A01 a01 = (A01) event.getEntity();
							logComputeRec.setOperateObj(a01.getA0101());
							findOObj = true;
						}
						//
						int length=0;
						for (int i = 0; i < fields.length; i++) {
							if (!findOObj && "A01".equals(fields[i].toUpperCase()) && state[i] instanceof A01) {
								A01 a01 = (A01) state[i];
								logComputeRec.setOperateObj(a01.getA0101());
								findOObj = true;
							}
							if (isLog(event.getEntity(), fields[i], DELETE)) {
								if (isNeed(state[i])) {
									// 集合情況
									if (state[i] instanceof Collection) {
										continue;
									}
									String codeStr=valToEncoderStr(state[i]);
									if(StringUtil.isNotEmpty(codeStr)){
										length+=codeStr.length();
										if(length>2800){
											break;
										}
									}
									oldValMap.put(fields[i], codeStr);
								}
							}
						}
						logComputeRec.setOldValMap(FastjsonUtils.toJson(oldValMap));
					}
				}
				insertLogRc(logComputeRec);
			}
		} catch (Exception e) {
			logger.debug("onPostDelete error!", e);
		}

	*/}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {/*
		try {
			init();
			if (auditableEntitys.containsKey(event.getEntity().getClass().getSimpleName())) {
				LogComputeRec logComputeRec = newAuditLogCR();
				logComputeRec.setOperateSet(event.getEntity().getClass().getSimpleName());
				logComputeRec.setDataId((String) event.getId());
				logComputeRec.setOperateType(LogComputeRec.OperateType.update);
				{
					Object[] oldState = event.getOldState();
					Object[] newState = event.getState();
					String[] fields = event.getPersister().getPropertyNames();
					Map<String, String> oldValMap = new HashMap<String, String>();
					Map<String, String> newValMap = new HashMap<String, String>();
					Boolean findOObj = false;
					if (oldState != null && newState != null && fields != null && oldState.length == newState.length && oldState.length == fields.length) {
						if ("A01".equals(logComputeRec.getOperateSet().toUpperCase()) && event.getEntity() instanceof A01) {
							A01 a01 = (A01) event.getEntity();
							logComputeRec.setOperateObj(a01.getA0101());
							findOObj = true;
						}
						int oldLength=0;
						int newLength=0;
						for (int i = 0; i < fields.length; i++) {
							if (!findOObj && "A01".equals(logComputeRec.getOperateSet()) && event.getEntity() instanceof A01) {
								A01 a01 = (A01) event.getEntity();
								logComputeRec.setOperateObj(a01.getA0101());
								findOObj = true;
							}
							if (isLog(event.getEntity(), fields[i], UPDATE)) {
								if (isNeedUpdate(oldState[i], newState[i])) {
									
									String oldCodeStr=valToEncoderStr(oldState[i]);
									String newCodeStr=valToEncoderStr(newState[i]);
									if(StringUtil.isNotEmpty(oldCodeStr)){
										oldLength+=oldCodeStr.length();
										if(oldLength>2800){
											break;
										}
									}
									if(StringUtil.isNotEmpty(newCodeStr)){
										newLength+=newCodeStr.length();
										if(newLength>2800){
											break;
										}
									}
									oldValMap.put(fields[i], oldCodeStr);
									newValMap.put(fields[i], newCodeStr);
								}
							}
						}
						logComputeRec.setOldValMap(FastjsonUtils.toJson(oldValMap));
						logComputeRec.setNewValMap(FastjsonUtils.toJson(newValMap));
					}
				}
				insertLogRc(logComputeRec);
			}
		} catch (Exception e) {
			logger.debug("onPostUpdate error!", e);
		}

	*/}

	private boolean isNeed(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String && StringUtil.isEmpty((String) obj)) {
			return false;
		}
		return true;
	}

	private boolean isNeedUpdate(Object oldObj, Object newObj) {
		boolean oldSign = false;
		boolean newSign = false;
		if (oldObj instanceof Collection || newObj instanceof Collection) {
			return false;
		}
		if (oldObj == null) {
			oldSign = true;
		} else if (oldObj instanceof String && StringUtil.isEmpty((String) oldObj)) {
			oldSign = true;
		}
		if (newObj == null) {
			newSign = true;
		} else if (newObj instanceof String && StringUtil.isEmpty((String) newObj)) {
			newSign = true;
		}
		if (oldSign && newSign) {
			return false;
		}
		if (newObj != null && !newObj.equals(oldObj)) {
			return true;
		}
		if (oldObj != null && !oldObj.equals(newObj)) {
			return true;
		}

		return false;
	}

	/*private void insertLogRc(LogComputeRec logRc) {
		logComputeRecService.insertLogRc(logRc);
	}*/

	/*@SuppressWarnings("unchecked")
	private LogComputeRec newAuditLogCR() {
		LogComputeRec logRc = new LogComputeRec();

		UserService userService = (UserService) SpringUtils.getBean("userServiceImpl");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, String[]> parameterMap = request.getParameterMap();
		StringBuffer parameter = buildParameter(parameterMap);
		String Agent = request.getHeader("User-Agent");
		String path = request.getServletPath();
		StringTokenizer st = new StringTokenizer(Agent, ";");
		String userbrowser = st.nextToken();
		userbrowser += st.nextToken();

		logRc.setOperatorId(userService.getCurrentUserId());
		logRc.setIp(request.getRemoteAddr());
		logRc.setOperateDate(new Date());

		String remark = "URL:" + path + ";\n 浏览器信息：" + userbrowser + " ;\n 参数：" + parameter.toString();
		if (remark.length() > 3000) {
			remark = remark.substring(0, 3000);
		}
		logRc.setRemark(remark);

		return logRc;
	}
*/
	/**
	 * 验证策略是否允许记录日志，规则如下： 可用的关键字有：
	 * insertAllow,insertDeny,updateAllow,updateDeny,deleteAllow,deleteDeny
	 * 没有配置对象的策略，所有字段不记录 allow和deny都配置的按allow验证，并忽略deny allow和deny都允许指定all关键字
	 * 多个字段用英文逗号隔开
	 */
	private boolean isLog(Object entity, String field, String op) {
		Map<String, String> entityConfig = auditableEntitys.get(entity.getClass().getSimpleName());
		if (entityConfig != null) {
			String allAllow = entityConfig.get("allAllow");
			if (StringUtils.isNotEmpty(allAllow)) {
				if ("true".equals(allAllow)) {
					return true;
				} else if ("false".equals(allAllow)) {
					return false;
				}
			}

			String allowFields = entityConfig.get(op.toLowerCase() + "Allow");
			if (allowFields != null) {
				if (allowFields.equals(ALL) || containsField(allowFields, field)) {
					// 配置ALL，所有允许
					return true;
				}
			} else {
				String denyFields = entityConfig.get(op.toLowerCase() + "Deny");
				if (denyFields != null) {
					if (denyFields.equals(ALL) || containsField(denyFields, field)) {
						// 配置ALL，所有不允许
						return false;
					}
				}
				return true;
			}
		} else {
		}
		// 缺省不记录
		return false;
	}

	/**
	 * 配置中是否包含当前字段
	 * 
	 * @Title: containsField
	 * @param fields
	 * @param field
	 * @return boolean
	 * @throws
	 */
	private boolean containsField(String fields, String field) {
		String[] fs = fields.split(",");
		for (String f : fs) {
			if (f.equals(field)) {
				return true;
			}
		}
		return false;
	}

	private String valToEncoderStr(Object state) throws Exception {
		String line = "";
		if (state != null && state instanceof Date) {
			line = DateUtil.getDateString((Date) state, "yyyy-MM-dd");
		} else {
			line = String.valueOf(state);
		}
		return URLEncoder.encode(line, "UTF-8");
	}

	/**
	 * 初始化审计日志服务类
	 * 
	 * @Title: init void
	 * @throws
	 */
	private synchronized void init() {/*
		if (logComputeRecService == null) {
			logComputeRecService = (LogComputeRecService) SpringUtils.getBean("logComputeRecServiceImpl");
		}
		if (auditableEntitys == null) {
			auditableEntitys = new LinkedHashMap<String, Map<String, String>>();
			LogConfigService logConfigService = (LogConfigService) SpringUtils.getBean("logConfigServiceImpl");
			List<LogConfig> logConfigList = logConfigService.getAll();
			for (LogConfig logConfig : logConfigList) {
				putInforSet(logConfig.getOperateSet(), "allAllow", "true");
			}
		}

	*/}

	private void putInforSet(String infoSet, String perm, String permVal) {
		Map<String, String> permMap = auditableEntitys.get(infoSet);
		if (permMap == null) {
			permMap = new LinkedHashMap<String, String>();
		}
		permMap.put(perm, permVal);
		auditableEntitys.put(infoSet, permMap);
	}

	public void setAuditableEntitys(Map<String, Map<String, String>> auditableEntitys) {
		this.auditableEntitys = auditableEntitys;
	}

	/**
	 * 
	 * @Title: 获取请求参数
	 * 
	 * @Description:
	 * 
	 */
	private StringBuffer buildParameter(Map<String, String[]> parameterMap) {

		StringBuffer parameter = new StringBuffer();

		if (parameterMap == null) {
			return parameter;
		}

		for (Entry<String, String[]> entry : parameterMap.entrySet()) {

			String parameterName = entry.getKey();
			if (ArrayUtils.contains(ignoreParameters, parameterName)) {
				continue;
			}

			String[] parameterValues = entry.getValue();
			if (parameterValues == null) {
				continue;
			}

			for (String parameterValue : parameterValues) {
				parameter.append(parameterName + " = " + parameterValue + ";\n ");
			}
		}

		return parameter;
	}

	/**
	 * 设置忽略参数
	 * 
	 * @return 忽略参数
	 */
	public String[] getIgnoreParameters() {
		return ignoreParameters;
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister arg0) {
		return false;
	}
}
