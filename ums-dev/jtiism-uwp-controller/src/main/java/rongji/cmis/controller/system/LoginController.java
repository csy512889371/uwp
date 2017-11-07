package rongji.cmis.controller.system;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.service.common.RSAService;
import rongji.cmis.service.system.*;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Principal;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.common.web.utils.WebUtils;
import rongji.framework.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller("loginController")
@RequestMapping("login")
public class LoginController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Resource
	UserService userService;

	@Resource
	RoleService roleService;

	@Resource
	MenuService menuService;

	@Resource(name = "topMenuServiceImpl")
	TopMenuService topMenuService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "msgRemindServiceImpl")
	MsgRemindService msgRemindService;

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;

	/**
	 * 如果登录成功后 有重新点击登录按钮 重新登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String dologin(HttpServletRequest request, HttpServletResponse response, Model model) {

	/*	String password = rsaService.decryptParameter("enPassword", request);
		rsaService.removePrivateKey(request);*/

		String password = request.getParameter("password");
		String username = request.getParameter("username");

		if (username != null && password != null) {
			CfgUmsUser user = userService.findByUsername(username);
			String message = null;

			if (user == null) {
				model.addAttribute("error", SpringUtils.getMessage("admin.login.unknownAccount"));
				//rsaGenerateKey(request, model);
				return "newLogin";
			}

			if (!DigestUtils.md5Hex(password).equals(user.getPassword())) {
				//rsaGenerateKey(request, model);
				message = "admin.login.passwordError";
			}

			if (message != null) {
				model.addAttribute("error", SpringUtils.getMessage(message));
				//rsaGenerateKey(request, model);
				return "newLogin";
			}

			HttpSession session = request.getSession();

			session.setAttribute(Constant.CURRENTUSER, new Principal(user.getId(), username));

			WebUtils.addCookie(request, response, CfgUmsUser.USERNAME_COOKIE_NAME, user.getUsername());
		}

		return "redirect:/index/index.do";
	}

	/**
	 * 跳转到登录页面
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, Model model) {
		try {

			if (userService.getCurrent() != null) {
				return "redirect:/index/index.do";
			}

			//rsaGenerateKey(request, model);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "newLogin";
	}


	/**
	 * 生成ticket
	 * @return ticket
	 */
	@ResponseBody
	@RequestMapping(value = "genLoginTicket")
	public String genLoginTicket() {
		Principal currentUser = userService.getCurrentPrincipal();
		if (currentUser == null) {
			return  "";
		}
		Cache cache = cacheManager.getCache(Constant.LOGINTICKETS);
		String uuid = UUID.randomUUID().toString();
		Element element = new Element(uuid, currentUser);
		cache.put(element);
		return uuid;
	}


	/**
	 * 验证令牌是否合法
	 * @param ticket ticket
	 */
	@ResponseBody
	@RequestMapping(value = "checkTicket")
	public ResultModel checkTicket(String ticket) {
		Cache cache = cacheManager.getCache(Constant.LOGINTICKETS);
		Element element = cache.get(ticket);
		Principal currentUser = null;
		if (null != element) {
			currentUser = (Principal) element.getObjectValue();
			if (currentUser != null) {
				cache.remove(ticket);
				return ResultModel.success("success", FastjsonUtils.toJson(currentUser));
			}
		}

		return  ResultModel.error("ticket not exist!");
	}

	private void rsaGenerateKey(HttpServletRequest request, Model model) {
		RSAService rsaService = SpringUtils.getBean("rsaServiceImpl", RSAService.class);
		RSAPublicKey publicKey = rsaService.generateKey(request);
		String modulus = Base64.encodeBase64String(publicKey.getModulus().toByteArray());
		String exponent = Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray());

		model.addAttribute("modulus", modulus);
		model.addAttribute("exponent", exponent);
	}

	/**
	 * 注销
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		session.removeAttribute(Constant.CURRENTUSER);
		WebUtils.removeCookie(request, response, CfgUmsUser.USERNAME_COOKIE_NAME);
		return "redirect:/login/login.do";
	}



	/**
	 * 系统选择
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("systemChoose")
	public String systemChoose(HttpSession session, Model model) {
		List<CfgUmsMenu> menuList = (List<CfgUmsMenu>) session.getAttribute(Constant.USERMENU);
		List<CfgUmsMenu> retuList = new ArrayList<>();
		for (CfgUmsMenu menu : menuList) {
			if (menu.getParent() == null) {
				retuList.add(menu);
			}
		}
		model.addAttribute("menuList", FastjsonUtils.toJson(retuList));
		return "common/systemChoose";
	}

	@RequestMapping("downloadPlugin")
	@ResponseBody
	public void downloadPlugin(HttpServletRequest request, HttpServletResponse response) {
		File file = new File("");
		String fileName = "ffactivex-setup-r39.exe";
		try {
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
			response.setHeader("Content-Length", String.valueOf(file.length()));
			FileUtils.copyFile(file, response.getOutputStream());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * 权限错误
	 */
	@RequestMapping("/unauthorized")
	public String unauthorized(HttpServletRequest request, HttpServletResponse response) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
			response.addHeader("loginStatus", "unauthorized");
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return null;
		}
		return "/admin/common/unauthorized";
	}

	@ResponseBody
	@RequestMapping("searchFile")
	public ResultModel searchFile(String name) {
		String path = "";
		if ("IE8".equals(name)) {
			path = SettingUtils.get().getiE8PackagePath();
		} else if ("windowsXpSp3".equals(name)) {
			path = SettingUtils.get().getWindowsXpSp3Path();
		} else if ("operationManual".equals(name)) {
			path = SettingUtils.get().getSystemOpManualPath();
		}

		File file = new File(path);
		if (file.exists()) {
			return ResultModel.success("");
		} else {
			return ResultModel.error("文件不存在！请联系管理员");
		}
	}

	@ResponseBody
	@RequestMapping("loadRelatedFile")
	public void loadRelatedFile(String name, HttpServletRequest request, HttpServletResponse response) {
		String path = "";
		if ("IE8".equals(name)) {
			path = SettingUtils.get().getiE8PackagePath();
		} else if ("windowsXpSp3".equals(name)) {
			path = SettingUtils.get().getWindowsXpSp3Path();
		} else if ("operationManual".equals(name)) {
			path = SettingUtils.get().getSystemOpManualPath();
		}

		File file = new File(path);
		OutputStream output = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			solveRandomCode(file.getName(), request, response);// 解决乱码问题
			byte[] buffer = new byte[1024];
			int len = 0;
			output = response.getOutputStream();
			while ((len = bufferedInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, len);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (bufferedInputStream != null) {
				IOUtils.closeQuietly(bufferedInputStream);
			}
			if (output != null) {
				IOUtils.closeQuietly(output);
			}
		}
	}
}
