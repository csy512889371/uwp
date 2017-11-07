package rongji.cmis.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.ums.CfgUmsCode;
import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.cmis.service.common.DataDictService;
import rongji.cmis.service.system.CfgUmsCodeAttributeService;
import rongji.cmis.service.system.CfgUmsCodeService;
import rongji.framework.base.exception.ApplicationException;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Logic;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.LetterMatchUtil;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;
import rongji.framework.common.web.controller.BaseController;

/**
 * 指标代码维护类
 *
 * @author HP
 * @date 2016-1-14 上午11:28:42
 * @version V1.0
 */
@Controller("codeController")
@RequestMapping("/sys/code")
public class CodeController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CodeController.class);

	@Resource(name = "dataDictServiceImpl")
	DataDictService dataDictService;

	@Resource(name = "cfgUmsCodeAttributeServiceImpl")
	CfgUmsCodeAttributeService cfgUmsCodeAttributeService;

	@Resource(name = "cfgUmsCodeServiceImpl")
	CfgUmsCodeService cfgUmsCodeService;

	/**
	 * 指标代码维护页面
	 *
	 * @return
	 */
	@RequestMapping("codeIndex")
	public String codeIndex(Model model) {
		return "system/code/codeIndex";
	}

	/**
	 * 获取代码属性
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("getCodeAttributeList")
	@ResponseBody
	public List<CfgUmsCodeAttribute> getCodeAttributeList(CfgUmsCode cfgUmsCode, CfgUmsCodeAttribute cfgUmsCodeAttribute, String codeid, ParamRequest paramRequest) {

		paramRequest.addFilter(new Filter(Logic.and, "CODE_ID", Operator.like, "1"));
		paramRequest.addFilter(new Filter(Logic.and, "STATUS", Operator.eq, "1"));
		paramRequest.addFilter(new Filter(Logic.and, "ATTRNAME", Operator.ne, "''"));// 没有配置名称的指标不显示在界面
		List<CfgUmsCodeAttribute> codeList = cfgUmsCodeAttributeService.findAllByParamRequest(paramRequest);

		return codeList;
	}

	/**
	 * 获取代码数据
	 *
	 * @param tName
	 *            指标表名称
	 * @return List<CfgUmsDataDict>
	 */
	@RequestMapping("getCodeList")
	@ResponseBody
	public List<CfgUmsDataDict> getCodeList(String tName) {
		List<CfgUmsDataDict> dictList = dataDictService.findListFromDbIncludeHidden(tName);
		return dictList;
	}

	/**
	 * 新增完、更新完重新加载树
	 *
	 * @param tName
	 * @return
	 */
	@RequestMapping("getCodeList2")
	@ResponseBody
	public ResultModel getCodeList2(String tName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<CfgUmsDataDict> dictList = dataDictService.findListFromDbIncludeHidden(tName);
		for (CfgUmsDataDict dataDict : dictList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", dataDict.getCode());
			map.put("supCode", dataDict.getSupCode());
			map.put("codeName", dataDict.getCodeName());
			map.put("fullName", dataDict.getCodeAbr2());
			map.put("isCommon", dataDict.getIsCommon());
			map.put("isStand", dataDict.getIsStand());
			list.add(map);
		}
		return ResultModel.success(FastjsonUtils.toJson(list));
	}

	/**
	 * 跳转到新增指标代码页面
	 *
	 * @return String
	 */
	@RequestMapping("codeAdd")
	public String codeAdd(String tCode, String type, String parentId, String parentCodeName, HttpServletRequest request) {

		if (StringUtil.isNotEmpty(tCode)) {
			request.setAttribute("tCode", tCode);
		}
		if (StringUtil.isNotEmpty(parentId)) {
			request.setAttribute("tId", parentId);
		}
		if (StringUtil.isNotEmpty(parentCodeName)) {
			request.setAttribute("tCodeName", parentCodeName);
		}
		if (StringUtil.isNotEmpty(type)) {
			request.setAttribute("type", type);
		}

		return "system/code/codeAdd";
	}

	/**
	 * 保存新增指标代码
	 *
	 * @param dict
	 * @return ResultModel 返回类型
	 */
	@RequestMapping("saveCode")
	@ResponseBody
	public ResultModel saveCode(String codeTable, CfgUmsDataDict dataDict, HttpServletRequest request, HttpServletResponse response) {

		try {
			dataDict.setDmGrp(codeTable);
			dataDict.setInvalid("1");
			dataDict.setIsStand((short)1);	//默认是扩展指标
			if (StringUtils.isNotEmpty(dataDict.getCodeName())) {
				dataDict.setCodeSpelling(LetterMatchUtil.converterToFirstSpell(dataDict.getCodeName()));
			}
			dataDictService.addDataDict(codeTable, dataDict);
		} catch (ApplicationException e) {
			return ResultModel.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}

	/**
	 * 跳转到修改指标代码页面
	 *
	 * @param tName
	 *            指标表名
	 * @param code
	 *            指标属性代码
	 * @return String
	 */
	@RequestMapping("codeUpdatePage")
	public String codeUpdatePage(String itemCode, String tCode, HttpServletRequest request) {
		CfgUmsDataDict dataDict = dataDictService.findDataDictByCode(tCode, itemCode);
		request.setAttribute("dataDict", dataDict);
		return "system/code/codeUpdate";
	}

	/**
	 * 更新指标代码
	 *
	 * @param dict
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateCode")
	@ResponseBody
	public ResultModel updateCode(CfgUmsDataDict dataDict, String codeTable, HttpServletRequest request, HttpServletResponse response) {

		try {
			dataDict.setInvalid(String.valueOf(dataDict.getYesPrv()));

			dataDictService.updateDataDict(codeTable, dataDict);
		} catch (ApplicationException e) {
			return ResultModel.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("更新失败");
		}
		return ResultModel.success("更新成功");

	}

	@RequestMapping("checkCodeOnly")
	@ResponseBody
	public Boolean checkCodeOnly(String itemCode, String codeTable) {
		return dataDictService.isCodeUnique(codeTable, itemCode, null);
	}

	/**
	 * @title commonCodeSettingsPage
	 * @description 跳转到常用指标代码设置页面
	 * @param selectCodes 勾选的项的id
	 * @return String
	 * @author HuJingqiang
	 */
	@RequestMapping("commonCodeSettingsPage")
	public String commonCodeSettingsPage(String[] selectCodes, Model model) {
		model.addAttribute("selectCodes", FastjsonUtils.toJson(selectCodes));
		return "system/code/commonCodeSettings";
	}

	/**
	 * @title commonCodeSettings
	 * @description 常用指标代码设置
	 * @param tCode
	 * @param selectCodes
	 * @return ResultModel
	 * @author HuJingqiang
	 */
	@RequestMapping("commonCodeSettings")
	@ResponseBody
	public ResultModel commonCodeSettings(String tCode, String[] selectCodes, String isCommon) {
		try {
			dataDictService.setCommonCode(tCode, selectCodes, isCommon);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("常用指标项设置失败！");
		}
		return ResultModel.success("常用指标项设置成功！");

	}

	/**
	 * 删除代码
	 *
	 * @param dataDict
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping("deleteCode")
	@ResponseBody
	public ResultModel deleteCode(String codeTable, String id, Integer children, HttpServletRequest request, HttpServletResponse response) {

		try {
			if (children == 1) {
				dataDictService.deleteDataDictAll(codeTable, id);
			}
			dataDictService.deleteDataDict(codeTable, id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("删除失败");
		}
		return ResultModel.success("删除成功");
	}

	/**
	 * 跳转到编辑代码页面:左边代码部分
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("updatePage")
	public String updatePage(HttpServletRequest request, String attrCode, String attrName, String id) {
		if (StringUtil.isNotEmpty(attrCode)) {
			request.setAttribute("attrCode", attrCode);
		}
		try {
			attrName = new String(attrName.getBytes("iso-8859-1"), "utf-8");
			request.setAttribute("attrName", attrName);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		if (StringUtil.isNotEmpty(id)) {
			request.setAttribute("id", id);
		}
		return "system/code/updateCodeAttrbute";
	}

	/**
	 * 跳转到新增代码页面:左边代码部分
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("addPage")
	public String addPage(HttpServletRequest request) {
		return "system/code/addCodeAttrbute";
	}

	/**
	 * 保存编辑：左边代码部分
	 *
	 * @param cfgUmsCodeAttribute
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("saveCodeAttrbute")
	@ResponseBody
	public ResultModel saveCodeAttrbute(CfgUmsCodeAttribute cfgUmsCodeAttribute, HttpServletRequest request, HttpServletResponse response) {
		try {
			cfgUmsCodeAttributeService.updateCodeAttr(cfgUmsCodeAttribute);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("更新失败");
		}
		return ResultModel.success("更新成功");
	}

	/**
	 * 保存新增：左边代码部分
	 *
	 * @param cfgUmsCodeAttribute
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addCodeAttrbute")
	@ResponseBody
	public ResultModel addCodeAttrbute(CfgUmsCodeAttribute cfgUmsCodeAttribute, HttpServletRequest request, HttpServletResponse response) {
		try {
			if(isTableExists(cfgUmsCodeAttribute.getAttrCode())) {
				if (StringUtil.isEmpty(cfgUmsCodeAttribute.getId())) {
					cfgUmsCodeAttribute.setId(UUID.randomUUID().toString());
				}
				cfgUmsCodeAttributeService.createIndexCode(cfgUmsCodeAttribute.getAttrCode());
				cfgUmsCodeAttributeService.addCodeAttribute(cfgUmsCodeAttribute);
			}else{
				return ResultModel.error("表已存在，请更换表名");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("新增失败");
		}
		return ResultModel.success("新增成功");
	}

	public Boolean isTableExists(String tableName){
		return cfgUmsCodeAttributeService.isTableExists(tableName);
	}

}
