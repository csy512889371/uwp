package rongji.framework.web.tag;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import rongji.cmis.service.system.RoleInfosetPriService;
import rongji.framework.util.SpringUtils;
import rongji.framework.util.StringUtil;

public class CadreMenuOperAuthorityTag extends TagSupport {
	

	private static final long serialVersionUID = -2587189878395197849L;
	
	private String operAuthCode;
	
	RoleInfosetPriService roleInfosetPriService = null;

	public CadreMenuOperAuthorityTag() {
		if (null == roleInfosetPriService) {
			roleInfosetPriService = SpringUtils.getBean("roleInfosetPriServiceImpl", RoleInfosetPriService.class);
		}
	}

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		String autoCadreId = (String) request.getAttribute("autoCadreId");
		if (StringUtil.isNotEmpty(autoCadreId) && StringUtil.isNotEmpty(operAuthCode)) {
			Boolean hasPriv = roleInfosetPriService.isHasCadreMenuOperAuth(autoCadreId, operAuthCode);
			//有权限
			if (hasPriv) {
				//logger.error("查询数据时间：" + (endTime1 - startTime));
				return TagSupport.EVAL_BODY_INCLUDE;
			}
		}
		
		return TagSupport.SKIP_BODY;
	}

	public String getOperAuthCode() {
		return operAuthCode;
	}

	public void setOperAuthCode(String operAuthCode) {
		this.operAuthCode = operAuthCode;
	}

}
