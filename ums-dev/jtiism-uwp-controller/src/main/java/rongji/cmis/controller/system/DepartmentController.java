package rongji.cmis.controller.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import rongji.cmis.model.unit.CmisDepartment;
import rongji.cmis.service.cadreUnit.InfoUnitBasicService;
import rongji.cmis.service.system.DepartmentService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.LetterMatchUtil;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

@Controller("departmentController")
@RequestMapping("/sys/dept")
public class DepartmentController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

	@Resource(name = "departmentServiceImpl")
	DepartmentService departmentService;

	@Resource(name = "infoUnitBasicServiceImpl")
	InfoUnitBasicService infoUnitBasicService;

	/**
	 * 
	 * @Title: deptIndex
	 * @Description: 跳转到管理权限管理页面
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("deptIndex")
	public String deptIndex() {

		return "system/dept/deptIndex";
	}

	/**
	 * 
	 * @Title: loadDeptList
	 * @Description: 加载管理权限列表
	 * @param paramRequest
	 * @param request
	 * @return
	 * @return Page<CmisDepartment> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("loadDeptList")
	public Page<CmisDepartment> loadDeptList(ParamRequest paramRequest, HttpServletRequest request) {

		paramRequest.setOrderDirection(Direction.asc);
		paramRequest.setOrderProperty("inino");
		Page<CmisDepartment> deptList = departmentService.findAllForPage(paramRequest);

		return deptList;
	}

	/**
	 * 
	 * @Title: deptAddPage
	 * @Description: 跳转到管理权限新增页面
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("deptAddPage")
	public String deptAddPage() {

		return "system/dept/deptAdd";
	}
	
	/**
	 * 
	 * @Title: saveNewDeptCheck
	 * @Description:
	 * @param code
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("isCodeExists")
	public boolean isCodeExists(String code) {
		Boolean isCodeExists = true;
		List<CmisDepartment> cmisDepartmentList = new ArrayList<>();
		cmisDepartmentList = departmentService.findAll();
		for(CmisDepartment department : cmisDepartmentList){
			if(department.getCode().equals(code)){
				isCodeExists = false;
			}
		}
		return isCodeExists;
	}

	/**
	 * 
	 * @Title: saveNewDept
	 * @Description:
	 * @param cmisDepartment
	 * @param request
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("saveNewDept")
	public ResultModel saveNewDept(CmisDepartment cmisDepartment, HttpServletRequest request) {
		
		cmisDepartment.setDepSpelling(LetterMatchUtil.getFirstSpell(cmisDepartment.getDeptname()));// 拼音首字母
		cmisDepartment.setDepBspelling(cmisDepartment.getDeptname());//对应CODE_ABR1
		cmisDepartment.setDepBspelling2(cmisDepartment.getDeptname());//对应CODE_ABR2
		cmisDepartment.setCodeAName(cmisDepartment.getDeptname());//对应CODE_ANAME
//		cmisDepartment.setDmGrp("cmis_department");
		
		cmisDepartment.setInino(0);// 默认排序
		if (cmisDepartment.getDepSpelling().length() > 100) {
			cmisDepartment.setDepSpelling(cmisDepartment.getDepSpelling().substring(0, 100));
		}
		try {
			departmentService.save(cmisDepartment);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}

	/**
	 * 
	 * @Title: deptUpdatePage
	 * @Description: 跳转到管理权限修改页面
	 * @param deptId
	 * @param request
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("deptUpdatePage")
	public String deptUpdatePage(String deptId, HttpServletRequest request) {

		CmisDepartment cmisDepartment = departmentService.find(deptId);
		request.setAttribute("cmisDepartment", cmisDepartment);

		return "system/dept/deptUpdate";
	}

	/**
	 * 
	 * @Title: saveUpdateDept
	 * @Description: 更新管理权限信息
	 * @param 	 cmisDepartment
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("saveUpdateDept")
	public ResultModel saveUpdateDept(CmisDepartment cmisDepartment) {
		try {
			CmisDepartment OriginCmisDepartment = departmentService.find(cmisDepartment.getCode());
			OriginCmisDepartment.setDeptname(cmisDepartment.getDeptname());
			OriginCmisDepartment.setDescription(cmisDepartment.getDescription());
			OriginCmisDepartment.setDepSpelling(StringUtils.lowerCase(LetterMatchUtil.getFirstSpell(cmisDepartment.getDeptname())));// 拼音首字母
			OriginCmisDepartment.setDepBspelling(cmisDepartment.getDeptname());//对应CODE_ABR1
			OriginCmisDepartment.setDepBspelling2(cmisDepartment.getDeptname());//对应CODE_ABR2
			OriginCmisDepartment.setCodeAName(cmisDepartment.getDeptname());//对应CODE_ANAME
			departmentService.update(OriginCmisDepartment);
			//departmentService.updateDepts(code,deptname,description);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}

	/**
	 * 
	 * @Title: deleteDept
	 * @Description: 删除管理权限
	 * @param deptId
	 * @param request
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("deleteDept")
	public ResultModel deleteDept(String deptId, HttpServletRequest request) {
		try {
			if(infoUnitBasicService.isAuthInUnit(deptId)){
				departmentService.delete(deptId);
			}else {
				return ResultModel.error("机构管理有对其引用，无法删除。");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.delete.fail");
		}

		return ResultModel.success("common.delete.success");
	}

	/**
	 * 
	 * @Title: saveDeptSort
	 * @Description: 保存排序信息
	 * @param sortNums
	 * @param deptIds
	 * @param request
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("saveDeptSort")
	public ResultModel saveDeptSort(String sortNums, String deptIds, HttpServletRequest request) {

		// 排序号
		String [] sortNumArr = sortNums.split(",");
		// 管理权限ID
		String [] deptIdArr = deptIds.split(",");
		try {
			for (int i = 0; i < deptIdArr.length; i++) {
				CmisDepartment department = departmentService.find(deptIdArr[i]);
				department.setInino(Integer.parseInt(sortNumArr[i]));
				departmentService.update(department);
				//departmentService.saveDeptOrder(deptIdArr[i], sortNumArr[i]);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success");
	}

	/**
	 * 新增、修改时,管理权限管理下的管理权限名称是否重复
	 * 
	 * @throws IOException
	 * **/
	@ResponseBody
	@RequestMapping("/isExistDeptName")
	public boolean isExistDeptName(HttpServletRequest request, ParamRequest paramRequest) throws IOException {
		String deptname = request.getParameter("deptname");
		String newDeptname = request.getParameter("newDeptname");
		String oldDeptname = request.getParameter("oldDeptname");
		if(StringUtil.isNotEmpty(newDeptname) && StringUtil.isNotEmpty(oldDeptname)){
			if(newDeptname.equals(oldDeptname)){
				return true;
			}
		}
		Boolean isDeptNameExists = true;
		List<CmisDepartment> cmisDepartmentList = new ArrayList<>();
		cmisDepartmentList = departmentService.findAll();
		for(CmisDepartment department : cmisDepartmentList){
			if(department.getDeptname().equals(deptname)){
				isDeptNameExists = false;
			}
		}
		return isDeptNameExists;
//		return departmentService.isPropertyUnique("deptname", newDeptname, oldDeptname);
	}
	
}
