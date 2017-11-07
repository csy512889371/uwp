package rongji.cmis.service.common.impl;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import rongji.cmis.dao.common.FieldInfoDao;
import rongji.cmis.model.sys.SysFieldInfo;
import rongji.cmis.service.common.FieldInfoService;
import rongji.framework.base.exception.ApplicationException;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.ParamRequest;

/**
 * Service - 表字段 对应的数据字典
 * 
 * @version 1.0
 */
@Service("fieldInfoServiceImpl")
public class FieldInfoServiceImpl extends BaseServiceImpl<SysFieldInfo> implements FieldInfoService {

	private static final Logger logger = LoggerFactory.getLogger(FieldInfoServiceImpl.class);

	@Resource(name = "fieldInfoDaoImpl")
	private FieldInfoDao fieldInfoDao;

	/**
	 * 
	 * @Description: 根据codeTable和code 获取对应的FieldInfo
	 * 
	 * @param codeTable
	 * 
	 * @param code
	 * 
	 * @return
	 */
	@Override
	@Cacheable("fieldInfo")
	public SysFieldInfo findFieldInfo(String srcTable, String srcField) {

		SysFieldInfo filedInfo = null;

		try {

			if (StringUtils.isEmpty(srcTable) || StringUtils.isEmpty(srcField)) {
				return filedInfo;
			}

			// 1. get field info by srcTable srcField
			ParamRequest param = new ParamRequest();
			Map<String, Object> equalFilters = new LinkedHashMap<String, Object>();
			equalFilters.put("srcTable", srcTable);
			equalFilters.put("srcField", srcField);
			param.setEqualFilters(equalFilters);

			List<SysFieldInfo> fieldInfoList = this.findAllByParamRequest(param);
			if (fieldInfoList == null || fieldInfoList.isEmpty()) {
				logger.error("query filed Info is null.");
				return filedInfo;
			}

			// 取第一个 codeTable不为空的 fieldInfo
			for (SysFieldInfo field : fieldInfoList) {
				if (!StringUtils.isEmpty(field.getCodeTable())) {
					filedInfo = field;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("find field Info error!", e);
			throw new ApplicationException("查询 field Info异常", e);
		}
		return filedInfo;
	}

	@Override
	public SysFieldInfo findFieldInfoBySrcField(String srcField) {
		SysFieldInfo filedInfo = null;
		try {

			if (StringUtils.isEmpty(srcField)) {
				return filedInfo;
			}
			// 1. get field info by srcTable srcField
			ParamRequest param = new ParamRequest();
			Map<String, Object> equalFilters = new LinkedHashMap<String, Object>();
			equalFilters.put("srcField", srcField);
			param.setEqualFilters(equalFilters);

			List<SysFieldInfo> fieldInfoList = this.findAllByParamRequest(param);
			if (fieldInfoList == null || fieldInfoList.isEmpty()) {
				logger.error("query filed Info is null.");
				return filedInfo;
			}

			// 取第一个 codeTable不为空的 fieldInfo
			for (SysFieldInfo field : fieldInfoList) {
				if (!StringUtils.isEmpty(field.getCodeTable())) {
					filedInfo = field;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("find field Info error!", e);
			throw new ApplicationException("查询 field Info异常", e);
		}
		return filedInfo;
	}

	/**
	 * 
	 */
	@Override
	@CacheEvict(value = "fieldInfo", allEntries = true)
	public void save(SysFieldInfo entity) {
		super.save(entity);
	}

	@Override
	@CacheEvict(value = "fieldInfo", allEntries = true)
	public void delete(Serializable... ids) {
		// Auto-generated method stub
		super.delete(ids);
	}

	@Override
	@CacheEvict(value = "fieldInfo", allEntries = true)
	public void update(SysFieldInfo entity) {
		// Auto-generated method stub
		super.update(entity);
	}

	/**
	 * 获取codeTable列的不为空的唯一值
	 */
	@Override
	public List<String> findCodeTableDist() {
		List<String> codeTables = fieldInfoDao.findCodeTableDist();
		return codeTables;
	}


}