package rongji.cmis.controller.cadreUnit;

import java.io.IOException;
import java.util.ArrayList;
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

import rongji.cmis.model.unit.B01;
import rongji.cmis.model.unit.B01Hiber;
import rongji.cmis.model.unit.B01.UnitType;
import rongji.cmis.service.cadreUnit.B01HiberService;
import rongji.cmis.service.cadreUnit.InfoUnitBasicService;
import rongji.cmis.service.cadreUnit.UnitIndexService;
import rongji.cmis.service.system.DepartmentService;
import rongji.framework.base.exception.ApplicationException;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.base.pojo.ZtreeUnitNote;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

/**
 * @Title: UnitController.java
 * @Package rongji.cmis.controller.cadreUnit
 * @Description: (单位管理)
 * @version V1.0
 */
@Controller("unitController")
@RequestMapping("/unit/unit")
public class UnitController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(UnitController.class);

	@Resource(name = "infoUnitBasicServiceImpl")
	InfoUnitBasicService infoUnitBasicService;

	@Resource(name = "b01HiberServiceImpl")
	B01HiberService b01HiberService;

	@Resource(name = "unitIndexServiceImpl")
	UnitIndexService unitIndexService;

	@Resource(name = "departmentServiceImpl")
	DepartmentService departmentService;

	/**
	 * 跳转到单位信息管理页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("unitIndex")
	public String unitIndex(Model model) {
		return "admin/unit/unitIndex";
	}

	/**
	 * DEMO > 干部管理 > 单位管理: 获取归口、单位表
	 * 
	 * @param tName
	 * @return
	 */
	@RequestMapping("getUnitList")
	@ResponseBody
	public List<ZtreeUnitNote> listUnit(ParamRequest paramRequest) {

		List<ZtreeUnitNote> ztreeNotes = new ArrayList<>();

		// 查询单位
		List<B01Hiber> allUnitHiberList = b01HiberService.findAllB01HiberList();
		for (B01Hiber unitHiber : allUnitHiberList) {
			B01 unit = unitHiber.getUnit();
			B01Hiber parentHiber = unitHiber.getParentB01Hiber();
			String pId = null;
			if (parentHiber != null) {
				pId = parentHiber.getId();
			}
			ZtreeUnitNote.Type unitNodeType = ZtreeUnitNote.Type.unit;
			ZtreeUnitNote unitNote = new ZtreeUnitNote(unitHiber.getId(), pId, (StringUtil.isNotEmpty(unit.getB0104()) ? unit.getB0104() : unit.getB0101()), unit.getB0101(), unitNodeType, unit.getB00(), unit.getB0101());
			ztreeNotes.add(unitNote);
		}
		return ztreeNotes;
	}

	/**
	 * 跳转到新增单位页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("addUnit")
	public String addUnit(Model model, String id, String selType) {
		if (StringUtils.isNotEmpty(id)) {
			B01Hiber b01Hiber = b01HiberService.find(id);
			model.addAttribute("b01Hiber", b01Hiber);
		}
		return "admin/unit/addUnit";
	}

	/**
	 * 保存单位
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("saveUnit")
	@ResponseBody
	public ResultModel saveUnit(B01 b01, B01Hiber b01Hiber) {
		ZtreeUnitNote unitNote = null;
		try {
			// 单位
			b01.setUnitType(UnitType.UNIT);
			infoUnitBasicService.saveUnit(b01, b01Hiber);
			unitNote = b01HiberService.getZtreeUnitNote(b01.getB00());
		} catch (ApplicationException e) {
			return ResultModel.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success", FastjsonUtils.toJson(unitNote));
	}

	/**
	 * 跳转到编辑页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("editUnit")
	public String editUnit(HttpServletRequest request, String b01HiberId) {
		B01Hiber b01Hiber = b01HiberService.find(b01HiberId);
		request.setAttribute("b01Hiber", b01Hiber);
		return "admin/unit/updateUnit";
	}

	/**
	 * 
	 * @Title: editUnitBaiscPage
	 * @Description: 编辑单位下的基本信息集
	 * @param request
	 * @param b01HiberId
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("editUnitBaiscPage")
	public String editUnitBaiscPage(HttpServletRequest request, String b01HiberId) {
		B01Hiber b01Hiber = b01HiberService.find(b01HiberId);
		request.setAttribute("b01Hiber", b01Hiber);
		return "admin/unit/updateUnitBasic";
	}


	/**
	 * 编辑基本单位信息
	 */
	@RequestMapping("updateBasicUnit")
	@ResponseBody
	public ResultModel updateBasicUnit(B01 b01, B01Hiber b01Hiber, String b01HiberId) {
		ZtreeUnitNote unitNote = null;
		try {
			infoUnitBasicService.updateUnit(b01, b01Hiber, b01HiberId);
			unitNote = b01HiberService.getZtreeUnitNote(b01.getB00());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("保存成功！", FastjsonUtils.toJson(unitNote));
	}

	/**
	 * 删除单位信息
	 * 
	 * @param unitId
	 * @param ischildren
	 * @return
	 */
	@RequestMapping("deleteUnit")
	@ResponseBody
	public ResultModel deleteUnit(String unitId, String b01HiberId) {
		try {
			b01HiberService.deleteById(unitId, b01HiberId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("删除失败！");
		}
		return ResultModel.success("删除成功！");
	}

	/**
	 * @Title: deleteUnitRecursive
	 * @Description: (递归删除单位信息)
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author wqq
	 */
	@RequestMapping("deleteUnitRecursive")
	@ResponseBody
	public ResultModel deleteUnitRecursive(String b01HiberId) {
		try {
			b01HiberService.deleteRecursiveById(b01HiberId, true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("删除失败！");
		}
		return ResultModel.success("删除成功！");
	}

	/**
	 * 跳转到选择上级单位页面
	 * 
	 * @return
	 */
	@RequestMapping("chooseUnitPage")
	public String chooseUnitPage(HttpServletRequest request, String pageType) {
		if (StringUtil.isNotEmpty(pageType)) {
			request.setAttribute("pageType", pageType);
		}
		return "admin/unit/chooseUnitPage";
	}

	/**
	 * 跳转到首页单位基本信息和单位编制信息显示列表
	 * 
	 * @param request
	 * @param pageType
	 * @return
	 */
	@RequestMapping("unitInfoPage")
	public String unitInfoPage(HttpServletRequest request, String b01HiberId) {
		B01Hiber b01Hiber = b01HiberService.find(b01HiberId);
		if (b01Hiber != null) {
			request.setAttribute("b01Hiber", b01Hiber);
		}
		return "admin/unit/unitInfoPage";
	}

	/**
	 * 跳转到首页 DEMO > 单位管理 > 单位排序
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("unitOrderIndex")
	public String unitOrderIndex(Model model) {
		return "admin/unit/unitOrder";
	}

	/**
	 * 跳转到首页单位基本信息和单位编制信息显示列表
	 * 
	 * @param request
	 * @param pageType
	 * @return
	 */
	@RequestMapping("/unitSortInfoPage")
	public String unitSortInfoPage(HttpServletRequest request, String type, String unitHiberId, String unitId, String libraryId, String libId, String dmcod, ParamRequest paramRequest) {
		request.setAttribute("type", type);
		request.setAttribute("unitHiberId", unitHiberId);
		request.setAttribute("unitId", unitId);
		request.setAttribute("libraryId", libraryId);
		request.setAttribute("libId", libId);
		request.setAttribute("dmcod", dmcod);
		return "admin/unit/unitSortInfoPage";
	}

	/**
	 * 跳转到首页单位基本信息和单位编制信息显示列表
	 * 
	 * @param request
	 * @param pageType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loadUnVulUnitList")
	public List<Map<String, Object>> loadUnVulUnitList(HttpServletRequest request, String unitHiberId, String type) {
		List<Map<String, Object>> unitList = infoUnitBasicService.getSupHiberListByUnitHiberId(unitHiberId, type);
		return unitList;
	}

	/**
	 * 
	 * @Title: unitSortSave
	 * @Description: 单位排序
	 * @param codes
	 *            B01HiberId
	 * @param libraryId
	 * @param request
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("/unitSortSave")
	public ResultModel unitSortSave(String codes, String type,String unitHiberId, HttpServletRequest request) {
		try {
			// 位置数组
			String[] codeArr = codes.split(",");
			unitIndexService.sortUnitIndexAndB01Hiber(codeArr,unitHiberId,type);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}

	/**
	 * ------------------------------end-----------------------------
	 */
	/**
	 * DEMO > 干部管理 > 单位管理: 获取归口、单位表
	 * 
	 * @param tName
	 * @return
	 */
	@RequestMapping("getUnVulUnitList")
	@ResponseBody
	public List<ZtreeUnitNote> getUnVulUnitList(ParamRequest paramRequest) {

		List<ZtreeUnitNote> ztreeUnitNotes = new ArrayList<ZtreeUnitNote>();
		// 查询归口下虚单位
		List<B01Hiber> allUnitHiberList = b01HiberService.findAllB01HiberList();
		for (B01Hiber unitHiber : allUnitHiberList) {
			B01 unit = unitHiber.getUnit();
			B01Hiber parentHiber = unitHiber.getParentB01Hiber();
			String pId = null;
			if (parentHiber != null) {
				pId = parentHiber.getId();
			}

			ZtreeUnitNote unitNote = null;
			ZtreeUnitNote.Type unitNodeType = ZtreeUnitNote.Type.unit;
			unitNote = new ZtreeUnitNote(unitHiber.getId(), pId, (StringUtil.isNotEmpty(unit.getB0104()) ? unit.getB0104() : unit.getB0101()), unit.getB0101(), unitNodeType, unit.getB00(), unit.getB0101());

			unitNote.setPunitLev(unitHiber.getUpLev());
			ztreeUnitNotes.add(unitNote);
		}
		return ztreeUnitNotes;
	}

	/**
	 * 新增、修改单位(虚单位)/分组时，单位管理下的(虚)单位/分组名称是否重复
	 * 
	 * @throws IOException
	 * **/
	@ResponseBody
	@RequestMapping("/isExistUnitName")
	public boolean isExistUnitName(HttpServletRequest request, ParamRequest paramRequest) throws IOException {
		String newUnitName = request.getParameter("newUnitName");
		String oldUnitName = request.getParameter("oldUnitName");
		String libraryId = request.getParameter("libraryId");
		if (StringUtil.isNotEmpty(libraryId)) {
			paramRequest.addFilter(new Filter("UNIT_LIBRARY_ID", Operator.eq, libraryId));
		}
		if (newUnitName == null) {
			return true;
		}
		if (StringUtil.isNotEmpty(oldUnitName)) {
			if (newUnitName.equals(oldUnitName)) {
				return true;
			}
		}

		return true;
	}

	/**
	 * @Title: getUnitTreeList
	 * @return List<ZtreeUnitNote> 返回类型
	 * @author Administrator
	 */
	@RequestMapping("getUnitTreeList")
	@ResponseBody
	public List<ZtreeUnitNote> getUnitTreeList(String treeType, ParamRequest paramRequest) {
		List<ZtreeUnitNote> ztreeUnitNotes = new ArrayList<>();
		List<B01Hiber> allUnitHiberList = b01HiberService.findAllB01HiberList();
		for (B01Hiber unitHiber : allUnitHiberList) {
			B01 unit = unitHiber.getUnit();
			
			String pId = null;
			if (unitHiber.isHasParent()) {
				pId = unitHiber.getParentB01Hiber().getId();
			}
			
			ZtreeUnitNote.Type type = ZtreeUnitNote.Type.unit;
			ZtreeUnitNote unitNote = new ZtreeUnitNote(unitHiber.getId(), pId, (StringUtil.isNotEmpty(unit.getB0104()) ? unit.getB0104() : unit.getB0101()), unit.getB0101(), type, unit.getB00(), unit.getB0101());
			ztreeUnitNotes.add(unitNote);
		}

		return ztreeUnitNotes;
	}

}
