package rongji.framework.web.tag;

import rongji.cmis.service.system.MenuOperService;
import rongji.cmis.service.system.impl.MenuOperServiceImpl;
import rongji.framework.util.SpringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Collection;
import java.util.List;

public class MenuOperAuthTag extends TagSupport {

    // 企业ID
    private String entId;

    // 权限代码
    private String operAuthCode;

    // 访问类型
    private String fromType;


    /**
     * 根据操作权限标签控制用户角色操作权限
     */
    @Override
    public int doStartTag() throws JspException {
        Collection<String> grantedAuths = getCurrentUserEntMenuOper(entId,fromType);
        if (null != operAuthCode && !"".equals(operAuthCode)) {
            if (grantedAuths.contains(operAuthCode)) {//包含该操作权限
                return TagSupport.EVAL_BODY_INCLUDE;
            }
        }
        return TagSupport.SKIP_BODY;
//		return TagSupport.EVAL_BODY_INCLUDE;
    }


    private List<String> getCurrentUserEntMenuOper(String entId,String fromType) {
        MenuOperService menuOperService = (MenuOperServiceImpl) SpringUtils.getBean("menuOperServiceImpl");
        return menuOperService.getCurrentUserMenuOperRight(entId,fromType);
    }


    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getOperAuthCode() {
        return operAuthCode;
    }

    public void setOperAuthCode(String operAuthCode) {
        this.operAuthCode = operAuthCode;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }
}
