package rongji.framework.web.tag;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.cmis.service.system.RoleInfosetPriService;
import rongji.framework.util.SpringUtils;
import rongji.framework.util.StringUtil;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class InfoSetAuthorityTag extends BodyTagSupport {

    private static final long serialVersionUID = -6874972956024083493L;
    private String infoSetCode;// 信息集权限编码
    private String dealType;// 处理类型：1.多个DIV
    private String entId;//企业ID
    private String fromType;//访问类型 如果为字符串 admin 表示后台用户


    RoleInfosetPriService roleInfosetPriService = null;

    public InfoSetAuthorityTag() {
        if (null == roleInfosetPriService) {
            roleInfosetPriService = SpringUtils.getBean("roleInfosetPriServiceImpl", RoleInfosetPriService.class);
        }
    }

    @Override
    public int doEndTag() throws JspException {
      /*  if (1==1) {
            return EVAL_PAGE;
        }*/
        JspWriter out = pageContext.getOut();
        RoleInfosetPri rip = null;
        Element element = null;
        String classStr = "";// 样式字符串（未定义）
        Element inputElement = null;
        String styleStr = null;
        Elements elements = null;
        if (StringUtil.isEmpty(this.entId)) {
            rip = roleInfosetPriService.getInfoSetPriv(this.infoSetCode, this.fromType);
        } else {
            rip = roleInfosetPriService.getInfoSetPrivByEntId(this.entId, this.infoSetCode, this.fromType, null,null);
        }
        if (StringUtil.isEmpty(this.dealType)) {
            Document doc = Jsoup.parseBodyFragment("<table><tr>" + bodyContent.getString() + "</tr></table>");
            elements = doc.getElementsByTag("td");
            if (elements.size() > 1) {// 正常input
                element = elements.get(1);
                inputElement = element.child(0);
                styleStr = element.attr("style");
            } else {
                inputElement = elements.get(0);
                styleStr = inputElement.attr("style");
            }
            if (null != rip && rip.getPriv() != null) {
                if (0 != rip.getPriv() && 1 != rip.getPriv()) {// 信息集读权限 0读 1 写
                    inputElement.attr("style", styleStr + "display: none;");
                }
            } else {// 信息集不可见
                inputElement.attr("style", styleStr + "display: none;");
            }
        } else if ("1".equals(this.dealType)) {
            Element childElement = null;
            String childStyle = "";
            String bodyStr = bodyContent.getString();
            Document doc = Jsoup.parseBodyFragment(bodyStr);
            elements = doc.getElementsByTag("div");
            if (elements.size() == 1) {
                inputElement = elements.get(0);
                childElement = inputElement.child(0);
                styleStr = inputElement.attr("style");
                childStyle = childElement.attr("style");
            }
            if (null != rip && null != rip.getPriv()) {
                // 不作控制
            } else {// 信息集不可见
                if (childElement.hasAttr("ondblclick")) {
                    childElement.removeAttr("ondblclick");
                }
                if (childElement.hasAttr("onclick")) {
                    childElement.removeAttr("onclick");
                }
                if (inputElement.hasAttr("id")) {
                    inputElement.removeAttr("id");
                }
                inputElement.attr("style", styleStr + "display: none;");
                childElement.attr("style", childStyle + "background-color: #F5F5F5;");
            }
        } else if ("2".equals(this.dealType)) {// <div ondblclick="">value</div>
            String bodyStr = bodyContent.getString();
            Document doc = Jsoup.parseBodyFragment(bodyStr);
            elements = doc.getElementsByTag("div");
            if (elements.size() == 1) {
                inputElement = elements.get(0);
            }
            if (null == rip || null == rip.getPriv()) {
                // 信息集不可见
                inputElement.text("");
                if (inputElement.hasAttr("ondblclick")) {
                    inputElement.removeAttr("ondblclick");
                }
                if (inputElement.hasAttr("onclick")) {
                    inputElement.removeAttr("onclick");
                }
            }
        } else if ("3".equals(this.dealType)) {// value
            String bodyStr = bodyContent.getString();
            Document doc = Jsoup.parseBodyFragment(bodyStr);
            elements = doc.getElementsByTag("textarea");
            if (elements.size() == 1) {
                inputElement = elements.get(0);
            }
            if (null == rip || null == rip.getPriv()) {
                inputElement.text("");// 信息集不可见
            }
        } else if ("4".equals(this.dealType)) {// <td>title<td><td><div><table></table></div></td>
            Document doc = Jsoup.parseBodyFragment("<table><tr>" + bodyContent.getString() + "</tr></table>");
            elements = doc.getElementsByTag("td");
            inputElement = elements.get(1);
            Element childElement = inputElement.child(0).child(0);
            if (null == rip || null == rip.getPriv()) {
                childElement.addClass("mmgrid_table");//添加隐藏mmgrid table
            }
        } else if ("5".equals(this.dealType)) {// value
            ServletRequest request = pageContext.getRequest();
            String autoCadreId = (String) request.getAttribute("autoCadreId");
            String bodyStr = bodyContent.getString();
            Document doc = Jsoup.parseBodyFragment(bodyStr);
            elements = doc.getElementsByTag("span");
            if (elements.size() == 1) {
                inputElement = elements.get(0);
                styleStr = inputElement.attr("style");
            }
            if (StringUtil.isNotEmpty(autoCadreId)) {
                Boolean hasPriv = roleInfosetPriService.getInfoSetPriv2(this.infoSetCode, this.infoSetCode, autoCadreId);
                if (!hasPriv) {
                    inputElement.attr("style", styleStr + "display: none;");// 信息集只读或不可读
                }
            }
        } else if ("6".equals(this.dealType)) {// 隐藏div内元素
            ServletRequest request = pageContext.getRequest();
            String autoCadreId = (String) request.getAttribute("autoCadreId");
            String bodyStr = bodyContent.getString();
            Document doc = Jsoup.parseBodyFragment(bodyStr);
            elements = doc.getElementsByTag("div");
            if (elements.size() == 1) {
                inputElement = elements.get(0);
                styleStr = inputElement.attr("style");
            }
            if (StringUtil.isNotEmpty(autoCadreId)) {
                Boolean hasPriv = roleInfosetPriService.getInfoSetPriv2(this.infoSetCode, this.infoSetCode, autoCadreId);
                if (!hasPriv) {
                    inputElement.attr("style", styleStr + "display: none;");
                    // 信息集只读或不可读
                }
            }
        }
        try {
            if (StringUtil.isNotEmpty(classStr)) {
                inputElement.addClass(classStr);
            }
            out.print(elements.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getInfoSetCode() {
        return infoSetCode;
    }

    public void setInfoSetCode(String infoSetCode) {
        this.infoSetCode = infoSetCode;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }
}
