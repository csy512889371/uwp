package rongji.cmis.controller.system;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.cmis.service.cadreUnit.InfoCadreBasicAttributeService;
import rongji.cmis.service.system.CfgUmsCodeAttributeService;
import rongji.cmis.service.system.CfgUmsCodeService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

/**
 * 
 * @Title: CadreInfoStructController.java
 * @Package rongji.cmis.controller.cadreUnit
 * @Description: 管理信息集
 * @author spongebob
 * @date 2016-1-19 下午4:49:57
 * @version V1.0
 */
@Controller("infoSetManageController")
@RequestMapping("/sys/infoSetManage")
public class InfoSetManageController {

	private static final Logger logger = LoggerFactory.getLogger(InfoSetManageController.class);

	@Resource(name = "cfgUmsCodeAttributeServiceImpl")
	CfgUmsCodeAttributeService cfgUmsCodeAttributeService;

	@Resource(name = "cfgUmsCodeServiceImpl")
	private CfgUmsCodeService cfgUmsCodeService;

	@Resource(name = "infoCadreBasicAttributeServiceImpl")
	private InfoCadreBasicAttributeService infoCadreBasicAttributeService;

	@RequestMapping("getInfoSetMangePage")
	public String getInfoSetMangePage(Model model) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(Filter.eq("code_id", "3"));
		paramRequest.addFilter(Filter.eq("parentId", "241"));
		paramRequest.setOrderProperty("seq");
		paramRequest.setOrderDirection(Direction.asc);
		List<CfgUmsCodeAttribute> codeAttrs = cfgUmsCodeAttributeService.findAllByParamRequest(paramRequest);
		model.addAttribute("codeAttrs", FastjsonUtils.toJson(codeAttrs));
		return "system/infoSet/infoSetMangeIndex";
	}

	@RequestMapping("addDynaInfoSet")
	public String addDynaInfoSet(Model model) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(Filter.eq("code_id", "3"));
		paramRequest.addFilter(Filter.eq("parentId", "241"));
		paramRequest.setOrderProperty("seq");
		paramRequest.setOrderDirection(Direction.asc);
		List<CfgUmsCodeAttribute> codeAttrs = cfgUmsCodeAttributeService.findAllByParamRequest(paramRequest);
		model.addAttribute("codeAttrs", codeAttrs);
		return "system/infoSet/addDynaInfoSet";
	}

	@RequestMapping("updataDynaInfoSetPage")
	public String updataDynaInfoSetPage(Model model, String infoSetId) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(Filter.eq("code_id", "3"));
		paramRequest.addFilter(Filter.eq("parentId", "241"));
		paramRequest.setOrderProperty("seq");
		paramRequest.setOrderDirection(Direction.asc);
		List<CfgUmsCodeAttribute> codeAttrs = cfgUmsCodeAttributeService.findAllByParamRequest(paramRequest);
		model.addAttribute("groups", codeAttrs);

		CfgUmsCodeAttribute attr = cfgUmsCodeAttributeService.find(infoSetId);
		model.addAttribute("codeAttr", attr);
		return "system/infoSet/updateDynaInfoSetPage";
	}

	@ResponseBody
	@RequestMapping("updateDynaInfoSet")
	public ResultModel updateDynaInfoSet(CfgUmsCodeAttribute codeAttr, HttpServletRequest request) {
		try {
			cfgUmsCodeAttributeService.updateCodeAttribute(codeAttr,request);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("保存成功！");
	}

	@ResponseBody
	@RequestMapping("searchInfoSetList")
	public List<CfgUmsCodeAttribute> searchInfoSetList(ParamRequest paramRequest, String groupId) {
		paramRequest.addFilter(Filter.eq("code_id", "3"));
		paramRequest.addFilter(Filter.eq("parentId", groupId));
		paramRequest.setOrderProperty("seq");
		paramRequest.setOrderDirection(Direction.asc);
		return cfgUmsCodeAttributeService.findAllByParamRequest(paramRequest);
	}

	@ResponseBody
	@RequestMapping("infoSetSortSave")
	public ResultModel infoSetSortSave(String groupId, String[] infoSetIds) {
		try {
			cfgUmsCodeAttributeService.saveCadreListCodeSort(groupId, infoSetIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("保存排序失败！");
		}
		return ResultModel.success("保存排序成功！");
	}

	@ResponseBody
	@RequestMapping("saveDynaInfoSet")
	public ResultModel saveDynaInfoSet(CfgUmsCodeAttribute attr, HttpServletRequest request) {
		String returnData = "";
		try {
			returnData = attr.getParentId();
			attr.setAttrCode(attr.getAttrCode().toUpperCase());
			cfgUmsCodeAttributeService.addANewDynaInfoSet(attr,request);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("保存成功", returnData);
	}

	@ResponseBody
	@RequestMapping("checkUniqueDynaName")
	public boolean checkUniqueDynaName(String attrCode) {
		if(StringUtil.isEmpty(attrCode)){
			return false;
		}
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(Filter.eq("attrCode", attrCode.toUpperCase()));
		List<CfgUmsCodeAttribute> codeAttrs = cfgUmsCodeAttributeService.findAllByParamRequest(paramRequest);
		if (codeAttrs.size() > 0) {
			return false;
		}
		return true;
	}

	@ResponseBody
	@RequestMapping("deleteDynaInfoSet")
	public ResultModel deleteDynaInfoSet(String id) {
		try {
			CfgUmsCodeAttribute attr = cfgUmsCodeAttributeService.find(id);
			if (attr.getIsBasic()) {
				throw new Exception("基础信息集不允许被删除！");
			}
			cfgUmsCodeAttributeService.deleteDynaInfoSet(attr);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("删除失败");
		}
		return ResultModel.success("删除成功");
	}
}
