package rongji.cmis.controller.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rongji.cmis.service.common.CacheService;
import rongji.framework.common.web.controller.BaseController;

/**
 * Controller - 缓存
 * 
 * @version 1.0
 */
@Controller("sysCacheController")
@RequestMapping("/sys/syscache")
public class CacheController extends BaseController {

	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;

	/**
	 * 缓存查看
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.GET)
	public String clear(Model model) {
		Long totalMemory = null;
		Long maxMemory = null;
		Long freeMemory = null;
		try {
			totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;// 以M为单位
																			// 虚拟机内获得内存量
			maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;// 以M为单位
																		// 虚拟机可获得最大使用的内存量
			freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;// 以M为单位
																			// 虚拟机内可用内存量
		} catch (Exception e) {
		}
		model.addAttribute("totalMemory", totalMemory);
		model.addAttribute("maxMemory", maxMemory);
		model.addAttribute("freeMemory", freeMemory);
		model.addAttribute("cacheSize", cacheService.getCacheSize());
		model.addAttribute("diskStorePath", cacheService.getDiskStorePath().replace("\\", "/"));
		return "/admin/syscache/clear";
	}

	/**
	 * 清除缓存
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public String clear(RedirectAttributes redirectAttributes) {
		cacheService.clear();
		return "redirect:clear.do";
	}

}