package rongji.cmis.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.CfgUmsCodeAttributeDao;
import rongji.cmis.dao.system.CfgUmsCodeDao;
import rongji.cmis.dao.system.DynamicInfoSetDao;
import rongji.cmis.model.ums.CfgUmsCode;
import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.cmis.service.cadreUnit.InfoCadreBasicAttributeService;
import rongji.cmis.service.system.CfgUmsCodeAttributeService;
import rongji.framework.base.exception.ApplicationException;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.base.pojo.Setting;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.CreateAndWriteFile;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.SettingUtils;

@Service("cfgUmsCodeAttributeServiceImpl")
public class CfgUmsCodeAttributeServiceImpl extends BaseServiceImpl<CfgUmsCodeAttribute> implements CfgUmsCodeAttributeService {

	@Resource(name = "cfgUmsCodeAttributeDaoImpl")
	CfgUmsCodeAttributeDao cfgUmsCodeAttributeDao;

	@Resource(name = "cfgUmsCodeDaoImpl")
	CfgUmsCodeDao cfgUmsCodeDao;

	@Resource(name = "dynamicInfoSetDaoImpl")
	DynamicInfoSetDao dynamicInfoSetDao;

	@Resource(name = "infoCadreBasicAttributeServiceImpl")
	private InfoCadreBasicAttributeService infoCadreBasicAttributeService;

	@Override
	public List<CfgUmsCodeAttribute> findAllRequest() {
		return cfgUmsCodeAttributeDao.findAllRequest();
	}

	/**
	 * 后台管理系统 > 信息构建 > 指标代码维护：左边代码编辑修改
	 */
	@Override
	public void updateCodeAttr(CfgUmsCodeAttribute cfgUmsCodeAttribute) {
		cfgUmsCodeAttributeDao.updateCodeAttr(cfgUmsCodeAttribute);

	}

	@Override
	public void saveCadreListCodeSort(String groupId, String[] infoSetIds) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(Filter.eq("code_id", "3"));
		paramRequest.addFilter(Filter.eq("parentId", groupId));
		paramRequest.setOrderProperty("seq");
		paramRequest.setOrderDirection(Direction.asc);
		List<CfgUmsCodeAttribute> codeAttrs = cfgUmsCodeAttributeDao.findAllByParamRequest(paramRequest);

		Integer initSeq = codeAttrs.get(0).getSeq();

		Map<String, Integer> sorts = new HashMap<String, Integer>();
		for (int i = 0; i < infoSetIds.length; i++) {
			sorts.put(infoSetIds[i], initSeq + i);
		}

		for (CfgUmsCodeAttribute codeAttr : codeAttrs) {
			Integer seq = sorts.get(codeAttr.getId());
			if (seq != null) {
				codeAttr.setSeq(seq);
				cfgUmsCodeAttributeDao.merge(codeAttr);
			}
		}
	}

	@Override
	public void addANewDynaInfoSet(CfgUmsCodeAttribute attr, HttpServletRequest request) {
		CfgUmsCode code = cfgUmsCodeDao.find("3");
		attr.setCfgUmsCode(code);
		if ("singleRecord".equals(attr.getRecordType())) {
			attr.setUrl("/ent/manage/dynaSingleInfoMain.do");
		} else {
			attr.setUrl("/ent/manage/dynaInfoSetMain.do");
		}
		attr.setIsBasic(false);
		attr.setSeq(cfgUmsCodeAttributeDao.findLastSeq("3", attr.getParentId()) + 1);
		if (attr.getIsBatch() == null) {
			attr.setIsBatch(false);
		}
		if (attr.getStatus() == null) {
			attr.setStatus(0);
		}
		cfgUmsCodeAttributeDao.save(attr);
		if ("cadretype".equals(attr.getInfoSetType())) {
			dynamicInfoSetDao.createInfoSetFKA01(attr.getAttrCode());
		}
		if(attr.getScript() != null && attr.getScript() != ""){
			Setting setting = SettingUtils.get();
			String fileName = "dynajs_"+attr.getAttrCode().toLowerCase();
			String realPath =  request.getSession().getServletContext().getRealPath(setting.getDynajsPath());
			CreateAndWriteFile.createFile(fileName,attr.getScript(),realPath,"js");
		}

	}

	@Override
	public void updateCodeAttribute(CfgUmsCodeAttribute codeAttr, HttpServletRequest request) {
		if (codeAttr.getIsBatch() == null) {
			codeAttr.setIsBatch(false);
		}
		if (codeAttr.getIsCommon() == null) {
			codeAttr.setIsCommon(false);
		}
		if (codeAttr.getStatus() == null) {
			codeAttr.setStatus(0);
		}
		CfgUmsCodeAttribute oldCodeAttr = cfgUmsCodeAttributeDao.load(codeAttr.getId());
		oldCodeAttr.setStatus(codeAttr.getStatus());
		oldCodeAttr.setIsBatch(codeAttr.getIsBatch());
		oldCodeAttr.setAttrName(codeAttr.getAttrName());
		oldCodeAttr.setIsCommon(codeAttr.getIsCommon());
		oldCodeAttr.setScript(codeAttr.getScript());
		cfgUmsCodeAttributeDao.merge(oldCodeAttr);
		if(codeAttr.getScript() != null && codeAttr.getScript() != ""){
			Setting setting = SettingUtils.get();
			String fileName = "dynajs_"+codeAttr.getAttrCode().toLowerCase();
			String realPath =  request.getSession().getServletContext().getRealPath(setting.getDynajsPath());
			CreateAndWriteFile.createFile(fileName,codeAttr.getScript(),realPath,"js");
		}
	}

	@Override
	public void deleteDynaInfoSet(CfgUmsCodeAttribute attr) {
		infoCadreBasicAttributeService.deleteDynaColumnByInfoSet(attr.getAttrCode());
		cfgUmsCodeAttributeDao.delete(attr.getId());
		// @TODO 删除表
	}

	@Override
	public void createIndexCode(String tableName) {
		try {
			dynamicInfoSetDao.addIndexCode(tableName);
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException("create table error!", e);
		}

	}

	@Override
	public void addCodeAttribute(CfgUmsCodeAttribute cfgUmsCodeAttribute) {
		cfgUmsCodeAttribute.setParentId("0");
		cfgUmsCodeAttribute.setStatus(1);
		cfgUmsCodeAttribute.setSeq(1);
		cfgUmsCodeAttributeDao.addCodeAttribute(cfgUmsCodeAttribute);
	}

	@Override
	public Boolean isTableExists(String tableName) {
		return cfgUmsCodeAttributeDao.isTableExists(tableName);
	}

	@Override
	public List<CfgUmsCodeAttribute> findByAttrCode(String attrCode) {
		return cfgUmsCodeAttributeDao.getTableChineseName(attrCode);
	}

	@Override
	public List<CfgUmsCodeAttribute> findScriptNoNull() {
		return cfgUmsCodeAttributeDao.findScriptNoNull();
	}

}
