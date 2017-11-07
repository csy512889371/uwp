package rongji.cmis.service.common.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import rongji.cmis.model.sys.SysCoder;
import rongji.cmis.service.common.CoderService;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.DateUtil;
import rongji.framework.util.ParamRequest;

/**
 * 
 * @Title: 系统数据字典 service
 * @version V1.0
 */
@Service("coderServiceImpl")
public class CoderServiceImpl extends BaseServiceImpl<SysCoder> implements CoderService {

	@Override
	@Cacheable("coder")
	public List<SysCoder> findListByFieldInfo(String tabName, String colName) {
		ParamRequest paramRequest = new ParamRequest();
		Map<String, Object> equalFilters = new LinkedHashMap<String, Object>();
		equalFilters.put("tabName", tabName);
		equalFilters.put("colName", colName);
		paramRequest.setEqualFilters(equalFilters);
		List<SysCoder> sysCoderList = this.findAllByParamRequest(paramRequest);
		return sysCoderList;
	}
	
	@Override
	public Map<String, SysCoder> findMapByFieldInfo(String tabName, String colName) {
		List<SysCoder> sysCoderList = findListByFieldInfo(tabName, colName);
		Map<String, SysCoder> sysCoderMap = new HashMap<String, SysCoder>();
		for (SysCoder coder : sysCoderList) {
			sysCoderMap.put(coder.getVal(), coder);
		}
		return sysCoderMap;
	}

	@Override
	@CacheEvict(value = "coder", allEntries = true)
	public void save(SysCoder entity) {
		entity.setStateChntime(DateUtil.getCurrentDate());
		super.save(entity);
	}

	@Override
	@CacheEvict(value = "coder", allEntries = true)
	public void update(SysCoder entity) {
		super.update(entity);
	}

	@Override
	@CacheEvict(value = "coder", allEntries = true)
	public void saveOrUpdate(SysCoder entity) {
		super.saveOrUpdate(entity);
	}

	@Override
	@CacheEvict(value = "coder", allEntries = true)
	public Object merge(SysCoder entity) {
		return super.merge(entity);
	}

	@Override
	@CacheEvict(value = "coder", allEntries = true)
	public void delete(Serializable... ids) {
		super.delete(ids);
	}

}
