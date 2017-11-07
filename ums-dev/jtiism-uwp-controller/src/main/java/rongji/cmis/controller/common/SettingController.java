package rongji.cmis.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rongji.cmis.service.common.CacheService;
import rongji.cmis.service.common.SysConfigService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Setting;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.SettingUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller - 系统设置
 *
 * @version 1.0
 */
@Controller("settingController")
@RequestMapping("/sys/setting")
public class SettingController extends BaseController {

    @Resource(name = "cacheServiceImpl")
    private CacheService cacheService;

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 管理
     */
    @RequestMapping(value = "/manage")
    public String manage(HttpServletRequest request, ModelMap model) {
        SettingUtils.clear();
        model.put("setting", SettingUtils.get());
        return "/admin/setting/manageSetting";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ResultModel update(Setting setting) {
        sysConfigService.saveSysConfigsBySetting(setting);
        cacheService.clear();
        return SUCCESS_RESULT_MODEL;
    }

}