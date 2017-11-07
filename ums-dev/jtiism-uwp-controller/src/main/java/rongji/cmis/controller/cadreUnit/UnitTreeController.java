package rongji.cmis.controller.cadreUnit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.unit.B01;
import rongji.cmis.model.unit.B01Hiber;
import rongji.cmis.service.cadreUnit.B01HiberService;
import rongji.framework.base.pojo.ZtreeUnitNote;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

@Controller("unitTreeController")
@RequestMapping("common/unitTree")
public class UnitTreeController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(UnitTreeController.class);

	@Resource(name = "b01HiberServiceImpl")
	B01HiberService b01HiberSerice;

	@RequestMapping("treePage")
	public String treePage(Model model, int selType) {
		model.addAttribute("selType", selType);

		return "common/unitTree";
	}

	/**
	 * 
	 * @Title: getTree
	 * @param paramRequest
	 * @return List<ZtreeUnitNote> 机构树
	 * @author Administrator
	 */
	@RequestMapping("getTree")
	@ResponseBody
	public List<ZtreeUnitNote> getTree(ParamRequest paramRequest) {
		logger.debug("get tree info");
		
		List<ZtreeUnitNote> ztreeNotes = new ArrayList<ZtreeUnitNote>();

		// 查询归口下实单位及分组(升序)
		List<B01Hiber> unitHiberList = b01HiberSerice.findAll();

		for (B01Hiber unitHiber : unitHiberList) {
			B01 unit = unitHiber.getUnit();
			String pId = "";
			if (unitHiber.isHasParent()) {
				pId = unitHiber.getParentB01Hiber().getId();
			}


			ZtreeUnitNote.Type unitNodeType = ZtreeUnitNote.Type.unit;
			String unitName = StringUtil.isNotEmpty(unit.getB0104()) ? unit.getB0104() : unit.getB0101();
			ZtreeUnitNote unitNote = new ZtreeUnitNote(unitHiber.getId(), pId, unitName, unit.getB0101(), unitNodeType,
					unit.getB00(), unit.getB0101());
			ztreeNotes.add(unitNote);
		}

		return ztreeNotes;
	}

}
