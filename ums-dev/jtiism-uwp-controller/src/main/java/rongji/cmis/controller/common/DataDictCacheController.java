package rongji.cmis.controller.common;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.service.common.DataDictService;
import rongji.cmis.service.common.FieldInfoService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.common.web.controller.BaseController;

/**
 * @author spongebob
 */
@Controller("dataDictCacheController")
@RequestMapping("/sys/datadictcache")
public class DataDictCacheController extends BaseController {

    @Resource(name = "fieldInfoServiceImpl")
    private FieldInfoService fieldInfoService;

    @Resource(name = "dataDictServiceImpl")
    private DataDictService dataDictService;

    @RequestMapping("codeTableInfoList")
    public String codeTableInfoList(Model model) {
        List<String> codeTables = fieldInfoService.findCodeTableDist();// 获取distinct
        // codeTable集
        model.addAttribute("codeTables", codeTables);
        return "system/datadictcache/codeTableInfo";
    }

    /**
     * @return ResultModel
     */
    @RequestMapping("updateChooseCaches")
    @ResponseBody
    public ResultModel updateChooseCaches(HttpServletRequest request) {
        String[] codeTables = request.getParameterValues("codeTables[]");
        String message = dataDictService.refreshCaches(Arrays.asList(codeTables));
        return ResultModel.success(message);
    }

    /**
     * @return ResultModel
     */
    @RequestMapping("updateAllCaches")
    @ResponseBody
    public ResultModel updateAllCaches() {
        List<String> codeTables = fieldInfoService.findCodeTableDist();
        String message = dataDictService.refreshCaches(codeTables);
        return ResultModel.success(message);
    }
}
