package rongji.cmis.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rongji.cmis.model.sys.ScriptVersion;
import rongji.cmis.model.sys.VersionPublic;
import rongji.cmis.service.system.ScriptVersionService;
import rongji.cmis.service.system.VersionPublicDetService;
import rongji.cmis.service.system.VersionPublicService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("versionController")
@RequestMapping("/sys/version")
public class VersionController extends BaseController {



	private static final Logger logger = LoggerFactory.getLogger(VersionController.class);

	@Resource(name = "versionPublicServiceImpl")
	private VersionPublicService versionPublicService;

	@Resource(name = "scriptVersionServiceImpl")
	private ScriptVersionService scriptVersionService;

	@Resource(name = "versionPublicDetServiceImpl")
	private VersionPublicDetService versionPublicDetService;

	/**
	 *
	 * @Title: versionIndex
	 * @Description: 访问版本发布历史页面
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("versionIndex")
	public String versionIndex() {
		return "system/version/versionIndex";
	}

	/**
	 *
	 * @Title: getAllVersionPublicPage
	 * @Description: 版本发布历史列表
	 * @param paramRequest
	 * @param request
	 * @return
	 * @return Page<VersionPublic> 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("getAllVersionPublicPage")
	public Page<VersionPublic> getAllVersionPublicPage(ParamRequest paramRequest, HttpServletRequest request) {
		Page<VersionPublic> page = new Page<VersionPublic>();
		paramRequest.setOrderProperty("publicId");
		paramRequest.setOrderDirection(Direction.desc);
		page = versionPublicService.findAllForPage(paramRequest);
		return page;
	}

	/**
	 *
	 * @Title: addVersionPublic
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("addVersionPublic")
	public String addVersionPublic(Model model) {

		List<ScriptVersion> scriptVersionList = scriptVersionService.findAll();

		List<Map<String, Object>> scriptVersionMapList = new ArrayList<Map<String, Object>>();
		for (ScriptVersion sv : scriptVersionList) {
			Map<String, Object> scriptVersionMap = new HashMap<String, Object>();
			scriptVersionMap.put("parentId", "");
			scriptVersionMap.put("id", sv.getSeqno());
			scriptVersionMap.put("seqno", sv.getSeqno());
			scriptVersionMap.put("chnScript", sv.getChnScript());
			scriptVersionMap.put("chnDesc", sv.getChnDesc());
			scriptVersionMap.put("chnTime", sv.getChnTime());
			scriptVersionMap.put("chnType", sv.getChnType());
			scriptVersionMap.put("svnVerno", sv.getSvnVerno());
			scriptVersionMap.put("devdbUp", sv.getDevdbUp());
			scriptVersionMap.put("demodbUp", sv.getDemodbUp());
			scriptVersionMap.put("fzdbUp", sv.getFzdbUp());
			scriptVersionMap.put("fjdbUp", sv.getFjdbUp());
			scriptVersionMap.put("testdbUp", sv.getTestdbUp());
			scriptVersionMap.put("pdmUp", sv.getPdmUp());
			scriptVersionMap.put("depVersion", sv.getDepVersion());
			scriptVersionMapList.add(scriptVersionMap);
		}

		model.addAttribute("scriptVersionList", FastjsonUtils.toJson(scriptVersionList));

		return "system/version/addVersionPublic";
	}

	/**
	 *
	 * @Title: getScriptVersionList
	 * @Description: 添加版本页面脚本列表
	 * @param pubType
	 * @return
	 * @return List<ScriptVersion> 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("getScriptVersionList")
	public List<ScriptVersion> getScriptVersionList(Integer pubType) {

		List<ScriptVersion> scriptVersionList = scriptVersionService.getScriptVersionList(pubType);

		return scriptVersionList;

	}

	/**
	 *
	 * @Title: saveVersionPublicAdd
	 * @Description: 保存版本发布
	 * @param versionPublic
	 * @param selectedSeqnos
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("saveVersionPublicAdd")
	public ResultModel saveVersionPublicAdd(VersionPublic versionPublic, String selectedSeqnos) {
		String returnData = "";
		try {
			versionPublic = versionPublicService.saveVersionPublic(versionPublic, selectedSeqnos);
			returnData = versionPublic.getPublicId() + "";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success", returnData);
	}

	/**
	 *
	 * @Title: getChnScript
	 * @Description: 查看脚本
	 * @param seqno
	 * @param scriptType
	 *            脚本类型 0：WEB 1：桌面
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("getChnScript")
	public ResultModel getChnScript(String seqno, Integer scriptType) {
		String chnScript = "";
		if (StringUtil.isNotEmpty(seqno)) {
			chnScript = scriptVersionService.getChnScript(Integer.parseInt(seqno), scriptType);
		}
		return ResultModel.success("", chnScript);
	}

	/**
	 *
	 * @Title: updateVersionPublic
	 * @Description: 修改版本发布信息
	 * @param publicId
	 * @param model
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("updateVersionPublic")
	public String updateVersionPublic(Integer publicId, Model model) {

		VersionPublic versionPublic = versionPublicService.find(publicId);
		model.addAttribute("versionPublic", versionPublic);

		return "system/version/updateVersionPublic";
	}

	@RequestMapping("addVersionScriptPage")
	public String addVersionScriptPage() {
		return "system/version/versionScriptAdd";
	}

	@ResponseBody
	@RequestMapping("addVersionScript")
	public ResultModel addVersionScript(ScriptVersion script) {
		try {
			script.setChnScript(StringUtil.formatWrap(script.getChnScript()));
			scriptVersionService.save(script);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("保存失败");
		}
		return ResultModel.success("保存成功");
	}

	/**
	 *
	 * @Title: getUpdateScriptVersionList
	 * @Description: 获取修改页面脚本列表
	 * @param pubType
	 * @param publicId
	 * @return
	 * @return List<ScriptVersion> 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("getUpdateScriptVersionList")
	public List<ScriptVersion> getUpdateScriptVersionList(Integer pubType, Integer publicId) {

		List<ScriptVersion> scriptVersionList = scriptVersionService.getUpdateScriptVersionList(pubType, publicId);
		List<ScriptVersion> resultScriptVersionList = new ArrayList<ScriptVersion>();
		List<Integer> selectedScriptVerionList = versionPublicDetService.getSeqnoByPublicId(publicId);
		for (ScriptVersion sv : scriptVersionList) {
			for (Integer seqno : selectedScriptVerionList) {
				if (seqno.equals(sv.getSeqno())) {
					sv.setIsSelected(1);
					break;
				}
			}
			resultScriptVersionList.add(sv);
		}
		return resultScriptVersionList;
	}

	/**
	 *
	 * @Title: saveVersionPublicUpdate
	 * @Description: 修改版本发布信息
	 * @param versionPublic
	 * @param selectedSeqnos
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("saveVersionPublicUpdate")
	public ResultModel saveVersionPublicUpdate(VersionPublic versionPublic, String selectedSeqnos) {
		String returnData = "";
		try {
			versionPublic = versionPublicService.updateVersionPublic(versionPublic, selectedSeqnos);
			returnData = versionPublic.getPublicId() + "";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success", returnData);
	}

	/**
	 *
	 * @Title: delVersionPublic
	 * @Description: 删除版本发布
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("delVersionPublic")
	public ResultModel delVersionPublic(String publicIds) {

		try {
			versionPublicService.delVersionPublic(publicIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.delete.fail");
		}
		return ResultModel.success("common.delete.success");
	}

	@ResponseBody
	@RequestMapping("expVersionPublic")
	public ResultModel expVersionPublic(String publicIds, HttpServletResponse response, HttpServletRequest request) {

		String exportFile = "";
		try {
			exportFile = versionPublicService.expVersionPublic(publicIds, request);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ResultModel.success(exportFile);
	}

}
