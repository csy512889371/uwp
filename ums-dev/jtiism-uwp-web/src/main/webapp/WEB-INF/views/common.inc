<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>

<%@ taglib uri="/WEB-INF/tld/setting.tld" prefix="setting" %>
<%@ taglib uri="/WEB-INF/tld/myTag.tld" prefix="my" %>
<%@ taglib uri="/WEB-INF/tld/indexDict.tld" prefix="indexDict" %>
<%@ taglib uri="/WEB-INF/tld/systemSecurity.tld" prefix="auth" %>
<%@ taglib uri="/WEB-INF/tld/gridAuthority.tld" prefix="gridAuth" %>
<%@ taglib uri="/WEB-INF/tld/infosetAuth.tld" prefix="infosetAuth" %>
<%@ taglib uri="/WEB-INF/tld/menuOperAuth.tld" prefix="menuOper" %>

<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
