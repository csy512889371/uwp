package rongji.cmis.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.sys.SysColumnShow;
import rongji.cmis.model.sys.SysColumnShow.PropertyType;
import rongji.cmis.model.sys.SysColumnShow.Type;
import rongji.cmis.model.sys.SysColumnShowList;
import rongji.cmis.model.ums.CfgUmsCode;
import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.cmis.service.cadreUnit.InfoCadreBasicAttributeService;
import rongji.cmis.service.system.CfgUmsCodeAttributeService;
import rongji.cmis.service.system.CfgUmsCodeService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.ParamRequest;

/**
 * @author spongebob
 * @version V1.0
 * @Title: CadreInfoStructController.java
 * @Package rongji.cmis.controller.cadreUnit
 * @Description: (用一句话描述该文件做什么)
 * @date 2016-1-19 下午4:49:57
 */
@Controller("cadreInfoStructController")
@RequestMapping("/sys/structure")
public class InfoSetStructController {

    private static final Logger logger = LoggerFactory.getLogger(InfoSetStructController.class);

    @Resource(name = "infoCadreBasicAttributeServiceImpl")
    private InfoCadreBasicAttributeService infoCadreBasicAttributeService;

    @Resource(name = "cfgUmsCodeServiceImpl")
    private CfgUmsCodeService cfgUmsCodeService;

    @Resource(name = "cfgUmsCodeAttributeServiceImpl")
    private CfgUmsCodeAttributeService cfgUmsCodeAttributeService;

    @RequestMapping("etpInfoBankStruct")
    public String conditionInfoPage(Model model) {
        ParamRequest param = new ParamRequest();
        param.addFilter(Filter.eq("code", "cadreListCode"));
        List<CfgUmsCode> codes = cfgUmsCodeService.findAllByParamRequest(param);
        if (codes.size() > 0) {
            Set<CfgUmsCodeAttribute> codeAttrs = codes.get(0).getCfgUmsCodeAttribute();
            model.addAttribute("CODEATTRS", FastjsonUtils.toJson(codeAttrs));
        }
        return "system/structure/etpInfoBankStruct";
    }

    @RequestMapping("getCadreAttrList")
    public String getCadreAttrList(Model model, String attrId, String infoSet) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.addFilter(Filter.eq("infoSet", infoSet));
        paramRequest.setOrderProperty("seq");
        paramRequest.setOrderDirection(Direction.asc);
        List<SysColumnShow> infosetAttrs = infoCadreBasicAttributeService.findAllByParamRequest(paramRequest);
        CfgUmsCodeAttribute codeAttr = cfgUmsCodeAttributeService.find(attrId);
        if ("cadretype".equals(codeAttr.getInfoSetType()) && "multiRecord".equals(codeAttr.getRecordType())) {
            model.addAttribute("hasItemGrid", true);
        } else {
            model.addAttribute("hasItemGrid", false);
        }

        model.addAttribute("codeAttr", codeAttr);
        model.addAttribute("INFO", FastjsonUtils.toJson(infosetAttrs).replace("\r", "\\r").replace("\t", "\\t"));

        model.addAttribute("attrId", attrId);
        return "system/structure/cadreAttrList";
    }


    /**
     * 获取数据集信息json字符串
     * @param attrId
     * @param infoSet
     * @return
     */
    @RequestMapping("getCadreAttrListJson")
    @ResponseBody
    public ResultModel getCadreAttrListJson(String attrId, String infoSet) {
        Map<String, Object> data = new HashMap<String, Object>();
        ResultModel resultModel = new ResultModel();
        try {
            ParamRequest paramRequest = new ParamRequest();
            paramRequest.addFilter(Filter.eq("infoSet", infoSet));
            paramRequest.setOrderProperty("seq");
            paramRequest.setOrderDirection(Direction.asc);
            List<SysColumnShow> a01Attrs = infoCadreBasicAttributeService.findAllByParamRequest(paramRequest);
            CfgUmsCodeAttribute codeAttr = cfgUmsCodeAttributeService.find(attrId);
            if ("cadretype".equals(codeAttr.getInfoSetType()) && "multiRecord".equals(codeAttr.getRecordType())) {
                data.put("hasItemGrid", true);
            } else {
                data.put("hasItemGrid", false);
            }
            Boolean addAble = codeAttr.getIsBasic();
            data.put("addAble", addAble);
            data.put("codeAttr", codeAttr);
            data.put("INFO", a01Attrs);
            resultModel .setContent(FastjsonUtils.toJson(data));
            return resultModel;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultModel.error("请求失败！");
        }

    }


    @RequestMapping("cadreAttrAdd")
    public String cadreAttrAdd(Model model, String attrId) {
        CfgUmsCodeAttribute codeAttr = cfgUmsCodeAttributeService.find(attrId);
        model.addAttribute("codeAttr", codeAttr);
        return "system/structure/cadreAttrAdd";
    }

    @RequestMapping("selectValidateRules")
    public String selectValidateRulesPage(String inputId, Model model) {
        model.addAttribute("inputId", inputId);
        return "system/structure/validateRulesPage";
    }

    @RequestMapping("addCadreAttr")
    @ResponseBody
    public ResultModel addCadreAttr(SysColumnShow attr, ParamRequest paramRequest) {
        try {
            paramRequest.addFilter(Filter.eq("infoSet", attr.getInfoSet()));
            if (Type.dataList.equals(attr.getType())) {
                paramRequest.addFilter(Filter.eq("propertyCode", attr.getPropertyCode()));
            } else {
                paramRequest.addFilter(Filter.eq("propertyName", attr.getPropertyName()));
                if (Type.singleimage.equals(attr.getType())){
                    attr.setHtmlType("singeImage");
                } else if (Type.multiFile.equals(attr.getType())){
                    attr.setHtmlType("multiFile");
                }
            }
            List<SysColumnShow> attrs = infoCadreBasicAttributeService.findAllByParamRequest(paramRequest);

            if (attrs.size() > 0) {
                return ResultModel.error("保存失败,该属性名已存在，请确保属性名的唯一");
            }

            infoCadreBasicAttributeService.saveExtraAttr(attr);
            return ResultModel.success("保存成功", FastjsonUtils.toJson(attr));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.error("common.save.fail");
        }

    }

    @RequestMapping("deleteCadreAttr")
    @ResponseBody
    public ResultModel deleteCadreAttr(String id) {
        try {
            SysColumnShow sysColumnShow = infoCadreBasicAttributeService.find(id);
            if (!sysColumnShow.getPropertyType().equals(PropertyType.extra)) {
                return ResultModel.error("该字段是基础字段，不允许删除");
            }
            infoCadreBasicAttributeService.delete(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.error("删除失败");
        }
        return ResultModel.success("删除成功");
    }

    @RequestMapping("cadreAttrUpdate")
    public String cadreAttrUpdate(Model model, String id, String operType) {
        SysColumnShow attr = infoCadreBasicAttributeService.find(id);
        
        model.addAttribute("operType", operType);
        model.addAttribute("ATTR", attr);
        return "system/structure/cadreAttrUpdate";
    }

    @RequestMapping("saveCadreAttrUpdate")
    @ResponseBody
    public ResultModel saveCadreAttrUpdate(SysColumnShow attr) {
        try {
            infoCadreBasicAttributeService.updateAPartOfExtra(attr);
            SysColumnShow attrNew = infoCadreBasicAttributeService.find(attr.getId());
            return ResultModel.success("修改成功", FastjsonUtils.toJson(attrNew));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.error("修改失败");
        }
    }

    @RequestMapping("saveCaderInfoBasicAttrs")
    @ResponseBody
    public ResultModel saveCaderInfoBasicAttrs(SysColumnShowList columns) {
        try {
            columns.getColumns().forEach(p->infoCadreBasicAttributeService.saveOrUpdate(p));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.error("common.save.fail");
        }
        return ResultModel.success("common.save.success");
    }
}
