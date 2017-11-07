package rongji.cmis.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rongji.cmis.model.sys.SysAnnounce;
import rongji.cmis.service.system.SysAnnounceService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Order;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;


@Controller("sysAnnounceController")
@RequestMapping("sys/announce")
public class SysAnnounceController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SysAnnounceController.class);

    @Resource(name = "sysAnnounceServiceImpl")
    SysAnnounceService sysAnnounceService;
    @Autowired
    UserService userService;

    @RequestMapping("announceInfo")
    public String announceInfo(HttpServletRequest request, Model model) {
        return "system/sysAnnounce/announce";
    }


    /**
     *
     * @Title: loadSanList
     * @Description: 加载公告列表
     * @param paramRequest
     * @param request
     * @return
     * @return Page<SysAnnounce> 返回类型
     * @throws
     * @author Laishifeng
     */
    @ResponseBody
    @RequestMapping("loadSanList")
    public Page<SysAnnounce> loadSanList(ParamRequest paramRequest, HttpServletRequest request) {

        paramRequest.setOrderDirection(Order.Direction.desc);
        paramRequest.setOrderProperty("SANINF003");
        Page<SysAnnounce> sanList = sysAnnounceService.findAllForPage(paramRequest);

        return sanList;
    }

    /**
     *
     * @Title: sanAddPage
     * @Description: 跳转到系统公告新增页面
     * @return
     * @return String 返回类型
     * @throws
     * @author Laishifeng
     */
    @RequestMapping("sanAddPage")
    public String sanAddPage() {
        return "system/sysAnnounce/sanAdd";
    }

    /**
     * @Description: 保存新增的公告
     * @param sysAnnounce
     * @param request
     * @return
     */
    @RequestMapping("saveNewAnnounce")
    @ResponseBody
    public ResultModel saveNewAnnounce(SysAnnounce sysAnnounce, HttpServletRequest request) {
        sysAnnounce.setSaninf000(UUID.randomUUID().toString());
        sysAnnounce.setSaninf003(new Date());
        sysAnnounce.setSaninf005(userService.getCurrentUserId().toString());
        sysAnnounce.setSaninf006(userService.getCurrentUsername());
        try {
            sysAnnounceService.save(sysAnnounce);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.error("common.save.fail");
        }
        return ResultModel.success("common.save.success");
    }

    /**
     * @Description: 跳转到公告编辑页面
     * @param sanId
     * @param request
     * @return
     */
    @RequestMapping("sanUpdatePage")
    public String sanUpdatePage(String sanId , HttpServletRequest request) {
        SysAnnounce announce = sysAnnounceService.find(sanId);
        request.setAttribute("announce", announce);
        return "system/sysAnnounce/sanUpdate";
    }

    /**
     *
     * @Title: saveUpdateSan
     * @Description: 更新公告信息
     * @param 	 sysAnnounce
     * @return
     * @return ResultModel 返回类型
     * @throws
     * @author Laishifeng
     */
    @ResponseBody
    @RequestMapping("saveUpdateSan")
    public ResultModel saveUpdateSan(SysAnnounce sysAnnounce) {
        try {
            SysAnnounce OriginSan = sysAnnounceService.find(sysAnnounce.getSaninf000());
            OriginSan.setSaninf001(sysAnnounce.getSaninf001());
            OriginSan.setSaninf004(new Date());
            OriginSan.setSaninf002(sysAnnounce.getSaninf002());
            sysAnnounceService.update(OriginSan);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.error("common.save.fail");
        }

        return ResultModel.success("common.save.success");
    }

    /**
     *
     * @Title: deleteSan
     * @Description: 删除公告
     * @param sanId
     * @param request
     * @return
     * @return ResultModel 返回类型
     * @throws
     * @author Laishifeng
     */
    @ResponseBody
    @RequestMapping("deleteSan")
    public ResultModel deleteSan(String sanId, HttpServletRequest request) {
        try {
            sysAnnounceService.delete(sanId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.error("common.delete.fail");
        }

        return ResultModel.success("common.delete.success");
    }
}
