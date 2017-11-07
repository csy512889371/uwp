package rongji.cmis.controller.common;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.sys.SysCoder;
import rongji.cmis.service.common.CoderService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.DateUtil;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

/**
 * 系统数据字典
 * 
 * @author liuzhen
 * 
 */
@Controller("coderController")
@RequestMapping("/sys/coder")
public class CoderController extends BaseController {

	@Resource(name = "coderServiceImpl")
	private CoderService coderService;

	private static final Logger logger = LoggerFactory.getLogger(CoderController.class);

	/**
	 * 列表
	 */
	@RequestMapping(value = "coderList")
	public String coderList() {
		return "/admin/coder/coderList";
	}

	/**
	 * 获取sys_coder表数据
	 * 
	 * @param tabName
	 * @param paramRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCoderList")
	public Page<SysCoder> getCoderList(String sort, String tabName, ParamRequest paramRequest) {
		if (!StringUtils.isEmpty(tabName)) {
			Map<String, String> likeFilters = new LinkedHashMap<String, String>();
			likeFilters.put("tabName", tabName);
			paramRequest.setLikeFilters(likeFilters);
		}
		if (!StringUtils.isEmpty(sort)) {
			String[] params = sort.split("\\.");
			paramRequest.setOrderProperty(params[0]);
			paramRequest.setOrderDirection(params.length == 2 ? Direction.fromString(params[1]) : Direction.asc);
		}
		Page<SysCoder> coders = coderService.findAllForPage(paramRequest);
		return coders;
	}

	/**
	 * 新增常量字段页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "coderAdd")
	public String coderAdd() {
		return "/admin/coder/coderAdd";
	}

	/**
	 * 更新数据字典常量字段页面
	 * 
	 * @param coderId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "coderUpdate")
	public String coderUpdate(String coderId, ModelMap model) {
		SysCoder coder = coderService.find(coderId);
		model.addAttribute("coder", coder);
		return "/admin/coder/coderUpdate";
	}

	/**
	 * 保存数据字典常量字段
	 * 
	 * @param coder
	 * @return
	 */
	@RequestMapping(value = "saveCoder")
	@ResponseBody
	public ResultModel saveCoder(SysCoder coder) {
		try {
			coderService.save(coder);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("提交成功！");
	}

	/**
	 * 更新数据字典常量字段
	 * 
	 * @param coder
	 * @return
	 */
	@RequestMapping(value = "updateCoder")
	@ResponseBody
	public ResultModel updateCoder(SysCoder coder) {
		try {
			SysCoder tempCoder = coderService.find(coder.getId());
			if (!tempCoder.getState().equals(coder.getState())) {
				tempCoder.setStateChntime(DateUtil.getCurrentDate());// 状态更改，状态更改时间字段更新
				tempCoder.setState(coder.getState());
			}
			tempCoder.setTabName(coder.getTabName());
			tempCoder.setColName(coder.getColName());
			tempCoder.setColDesc(coder.getColDesc());
			tempCoder.setCodeSpelling(coder.getCodeSpelling());
			tempCoder.setVal(coder.getVal());
			tempCoder.setValDesc(coder.getValDesc());
			coderService.update(tempCoder);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("更新失败！");
		}
		return ResultModel.success("更新成功！");
	}

	/**
	 * 删除数据字典一常量值
	 * 
	 * @param coderId
	 * @return
	 */
	@RequestMapping(value = "deleteCoder")
	@ResponseBody
	public ResultModel deleteCoder(String coderId) {
		try {
			coderService.delete(coderId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("删除失败！");
		}
		return ResultModel.success("删除成功！");
	}
}
