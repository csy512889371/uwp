package rongji.cmis.controller.common;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.sys.SysFieldInfo;
import rongji.cmis.service.common.FieldInfoService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

@Controller("fieldInfoController")
@RequestMapping("/sys/fieldinfo")
public class FieldInfoController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(FieldInfoController.class);

	@Resource(name = "fieldInfoServiceImpl")
	private FieldInfoService fieldInfoService;

	/**
	 * 
	 * @Title: fieldInfoList
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author spongebob
	 */
	@RequestMapping("fieldInfoList")
	public String fieldInfoList() {
		return "/admin/fieldinfo/fieldInfoList";
	}

	/**
	 * 
	 * @Title: getfieldInfoList
	 * @param paramRequest
	 * @return
	 * @return Page<SysFieldInfo> 返回类型
	 * @throws
	 * @author spongebob
	 */
	@ResponseBody
	@RequestMapping("getFieldInfoList")
	public Page<SysFieldInfo> getFieldInfoList(String sort, String serachName, String serachValue, ParamRequest paramRequest) {
		if (!StringUtils.isEmpty(serachValue) && !StringUtils.isEmpty(serachName)) {
			Map<String, String> likeFilters = new LinkedHashMap<String, String>();
			likeFilters.put(serachName, serachValue);
			paramRequest.setLikeFilters(likeFilters);
		}
		if (!StringUtils.isEmpty(sort)) {
			String[] params = sort.split("\\.");
			paramRequest.setOrderProperty(params[0]);
			paramRequest.setOrderDirection(params.length == 2 ? Direction.fromString(params[1]) : Direction.asc);
		}
		Page<SysFieldInfo> fieldInfos = fieldInfoService.findAllForPage(paramRequest);
		return fieldInfos;
	}

	/**
	 * 
	 * @Title: fieldInfoAdd
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author spongebob
	 */
	@RequestMapping("fieldInfoAdd")
	public String fieldInfoAdd() {
		return "/admin/fieldinfo/fieldInfoAdd";
	}

	/**
	 * 
	 * @Title: fieldInfoUpdate
	 * @Description: (跳转到更新页面)
	 * @param fieldInfoId
	 * @param model
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author spongebob
	 */
	@RequestMapping("fieldInfoUpdate")
	public String fieldInfoUpdate(String fieldInfoId, Model model) {
		SysFieldInfo fieldInfo = fieldInfoService.find(fieldInfoId);
		model.addAttribute("fieldinfo", fieldInfo);
		return "/admin/fieldinfo/fieldInfoUpdate";
	}

	/**
	 * 
	 * @Title: saveFieldInfo
	 * @param fieldInfo
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author spongebob
	 */
	@ResponseBody
	@RequestMapping("saveFieldInfo")
	public ResultModel saveFieldInfo(SysFieldInfo fieldInfo) {
		try {
			fieldInfoService.save(fieldInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("保存成功！");
	}

	/**
	 * 
	 * @Title: deleteFieldInfo
	 * @param fieldInfoId
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author spongebob
	 */
	@RequestMapping("deleteFieldInfo")
	@ResponseBody
	public ResultModel deleteFieldInfo(String fieldInfoId) {
		try {
			fieldInfoService.delete(fieldInfoId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("删除成功");
	}

	@RequestMapping("updateFieldInfo")
	@ResponseBody
	public ResultModel updateFieldInfo(SysFieldInfo fieldInfo) {
		try {
			fieldInfo = (SysFieldInfo)fieldInfoService.merge(fieldInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("更新失败！");
		}
		return ResultModel.success("更新成功！");
	}

}
