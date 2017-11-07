package rongji.cmis.controller.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.sys.SysFieldInfo;
import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.cmis.service.cadreUnit.UnitIndexService;
import rongji.cmis.service.common.DataDictService;
import rongji.cmis.service.common.FieldInfoService;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.ParamRequest;

/**
 * 
 * @Title: 对应中组织部颁标准代码 controller
 * @version V1.0
 */
@Controller("dictController")
@RequestMapping("common/dict")
public class DictController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(DictController.class);
	

	@Resource(name = "dataDictServiceImpl")
	DataDictService dataDictService;

	@Resource(name = "fieldInfoServiceImpl")
	private FieldInfoService fieldInfoService;


	@Resource(name = "unitIndexServiceImpl")
	private UnitIndexService unitIndexService;

	/**
	 * 
	 * @Title: dictIndex
	 * @Description: 跳转数据字典选择页面
	 * @param tableName
	 *            表名
	 * @param model
	 * @return String 返回类型
	 */
	@RequestMapping("dictIndex")
	public String dictIndex(String tableName, String fieldName, String selType, String title, String busType, String ext, String valType, Model model, HttpServletRequest request) {

		if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(fieldName)) {
			return null;
		}

		SysFieldInfo fieldInfo = fieldInfoService.findFieldInfo(tableName, fieldName);
		if (fieldInfo == null || StringUtils.isEmpty(fieldInfo.getCodeTable())) {
			return null;
		}

		String codeTable = fieldInfo.getCodeTable();
		logger.info("codeTable: " + codeTable);
		if ("ZB02".equalsIgnoreCase(codeTable)) {
			model.addAttribute("isZB02", "1");
		}
		String checkedIds = null;
		if (StringUtils.isNotEmpty(selType) && "3".equals(selType)) {// 参数回填
			checkedIds = request.getParameter("checkedId");
		}
		model.addAttribute("checkedIds", checkedIds);// 选中Id
		model.addAttribute("tableName", tableName);
		model.addAttribute("fieldName", fieldName);
		model.addAttribute("busType", busType);
		model.addAttribute("selType", selType);
		model.addAttribute("codeTable", codeTable);
		model.addAttribute("title", title + " (" + codeTable + ")");
		model.addAttribute("valType", valType);
		return "common/dict";
	}

	@RequestMapping("findDictByCode")
	@ResponseBody
	public ZtreeDictNote findDictByCode(String srcTable, String srcField, String code) {
		return dataDictService.findByFieldInfo(srcTable, srcField, code);
	}

	@RequestMapping("dictData")
	@ResponseBody
	public List<ZtreeDictNote> dictData(String tableName, String fieldName, String busType) {
		if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(fieldName)) {
			return null;
		}

		SysFieldInfo fieldInfo = fieldInfoService.findFieldInfo(tableName, fieldName);
		if (fieldInfo == null || StringUtils.isEmpty(fieldInfo.getCodeTable())) {
			return null;
		}

		List<ZtreeDictNote> treeNoteList = null;
		String codeTable = fieldInfo.getCodeTable();

		logger.info("codeTable: " + codeTable);
		if ("UNIT_INDEX".equalsIgnoreCase(codeTable)) {
			treeNoteList = unitIndexService.buildUnitTreeNoteList();
		} else {
			treeNoteList = dataDictService.findZtreeNoteList(fieldInfo.getCodeTable());
			
			/**
			 * 设置isParent
			 */
			List<ZtreeDictNote> treeNoteListCopy = new ArrayList<ZtreeDictNote>(Arrays.asList(new ZtreeDictNote[treeNoteList.size()]));
			Collections.copy(treeNoteListCopy, treeNoteList);
			for (ZtreeDictNote treeNote : treeNoteList) {
				updateIsParent(treeNoteListCopy, treeNote.getSupCode());
			}
		}

		return treeNoteList;
	}

	private void updateIsParent(List<ZtreeDictNote> treeNoteListCopy, String supCode) {
		if (StringUtils.isEmpty(supCode)) {
			return;
		}

		for (ZtreeDictNote treeNote : treeNoteListCopy) {
			if (supCode.equals(treeNote.getCode())) {
				treeNote.setIsParent(true);
				treeNoteListCopy.remove(treeNote);
				break;
			}
		}
	}

	@RequestMapping("getAllDictData")
	@ResponseBody
	public List<ZtreeDictNote> getAllDictData(String codeTable) {
		if (StringUtils.isEmpty(codeTable)) {
			return null;
		}
		List<ZtreeDictNote> treeNoteList = null;
		logger.info("codeTable: " + codeTable);
		treeNoteList = dataDictService.findZtreeNoteList(codeTable);
		return treeNoteList;
	}


	/**
	 * 根据机构ID查询机构<br>
	 * 监督系统专用
	 * 
	 * @param code
	 *            机构ID
	 * @param paramRequest
	 * @return
	 */
	@RequestMapping("findUnitsByCode")
	@ResponseBody
	public Map<String, Object> findUnitsByCode(String code, ParamRequest paramRequest) {

		return dataDictService.findUnitsByCode(code, paramRequest);
	}

	/**
	 * 福州专用，根据机构代码查询机构
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping("findUnitByUnitCode")
	@ResponseBody
	public List<CfgUmsDataDict> findUnitByUnitCode(String code) {
		List<CfgUmsDataDict> list = dataDictService.findByUnitCode(code);
		if (list == null)
			list = new ArrayList<CfgUmsDataDict>();
		return list;
	}
}
