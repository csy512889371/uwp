package rongji.framework.web.tag;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.cmis.service.system.RoleInfosetPriService;
import rongji.framework.util.SpringUtils;
import rongji.framework.util.StringUtil;

public class GridAuthorityTag extends TagSupport {

	private static final long serialVersionUID = 1168589821389353962L;
	private String privCode;// 权限编码
	private String infoSetCode;// 信息集权限编码
	private String optType;// add、update
	private String fromType;//访问类型 如果为字符串 admin 表示后台用户

	@Override
	public int doStartTag() throws JspException {
		RoleInfosetPriService roleInfosetPriService = SpringUtils.getBean("roleInfosetPriServiceImpl", RoleInfosetPriService.class);
		if (!this.infoSetCode.equals(this.privCode)) {
			RoleInfosetPri rip = roleInfosetPriService.getInfoSetPriv(this.infoSetCode, this.privCode,this.fromType);
			if (null != rip && null != rip.getPriv()) {// 可读或可写权限
				return TagSupport.SKIP_BODY;
			}
			return TagSupport.EVAL_BODY_INCLUDE;
		} else {// 用于信息集录入按钮控制
			ServletRequest request = pageContext.getRequest();
			String autoCadreId = (String) request.getAttribute("autoCadreId");
			if (StringUtil.isEmpty(autoCadreId)) {
				return TagSupport.EVAL_BODY_INCLUDE;
			}
			Boolean hasPriv = roleInfosetPriService.getInfoSetPriv2(this.infoSetCode, this.privCode, autoCadreId);
			if (hasPriv) {// 写权限
				return TagSupport.EVAL_BODY_INCLUDE;
			}
			return TagSupport.SKIP_BODY;
		}
	}

	public String getPrivCode() {
		return privCode;
	}

	public void setPrivCode(String privCode) {
		this.privCode = privCode;
	}

	public String getInfoSetCode() {
		return infoSetCode;
	}

	public void setInfoSetCode(String infoSetCode) {
		this.infoSetCode = infoSetCode;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
}
