package rongji.framework.web.tag;

import rongji.cmis.service.system.RoleUserService;
import rongji.framework.util.SpringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Collection;

public class SystemAuthorityTag extends TagSupport {

	private static final long serialVersionUID = -3766894288918910436L;

	// 多个权限用, 隔开 要求全部权限满足
	private String ifAllGranted;

	// 多个权限用, 隔开 满足其中一个权限即可
	private String ifAnyGranted;
	/**
	 * 根据操作权限标签控制用户角色操作权限
	 */
	@Override
	public int doStartTag() throws JspException {
		
		Collection<String> grantedAuths = getUserAuthorityString();
		if (null != ifAllGranted && !"".equals(ifAllGranted)) {
			if (grantedAuths.contains(ifAllGranted)) {//包含该操作权限
				return TagSupport.EVAL_BODY_INCLUDE;
			}
		}
		return TagSupport.SKIP_BODY;
		
//		return TagSupport.EVAL_BODY_INCLUDE;
	}

	/**
	 * 
	 * @Title: getUserAuthorityString
	 * @Description: 查看当前用户权限
	 * @return
	 * @return Collection<String> 返回类型
	 * @throws
	 * @author LFG
	 */
	private Collection<String> getUserAuthorityString() {
		RoleUserService roleUserService = (RoleUserService) SpringUtils.getBean("roleUserServiceImpl");
		return roleUserService.getCurrentUserRightWithRedis();
	}


	public String getIfAllGranted() {
		return ifAllGranted;
	}

	public void setIfAllGranted(String ifAllGranted) {
		this.ifAllGranted = ifAllGranted;
	}

	public String getIfAnyGranted() {
		return ifAnyGranted;
	}

	public void setIfAnyGranted(String ifAnyGranted) {
		this.ifAnyGranted = ifAnyGranted;
	}

}
