<%@ page language="java" pageEncoding="utf-8" %>
<%@ include file="../../common.inc" %>
<script type="text/javascript" src="<%=basePath%>企业性质"></script>
<link rel="stylesheet" href="<%=basePath%>resources/admin/js/layui/css/layui.css">

<body class="update_body">
<style>
    #pub_rmb_window {
        overflow-x: hidden;
    }
</style>
<c:if test="${checkRuleErr != null}">
    <div>
        <div class="alert alert-error checkRuleErrCla" onclick="hideCheckRuleErrTip()">${checkRuleErr}
            <button type="button" class="close" data-dismiss="alert" id="checkRuleErrId">&times;</button>
        </div>
    </div>
</c:if>
<div class="oper_menu" style="position: relative;top: -10px;height:23px;">
    <button style="visibility: hidden;width: 35px;"></button>
    <%--<select id="entSelect" style="width: 100px;margin-left: -27px;"></select>--%>
    <button id="btn_last" class="btn-style4" onclick="checkSaveState(1)">
        <i class="icon-arrow-up"></i> 上一人
    </button>
    <button id="btn_next" class="btn-style4" onclick="checkSaveState(2)">
        <i class="icon-arrow-down"></i> 下一人
    </button>
    <td style="text-align: center;">
        <button id="saveAARButton" class="btn-style1" style="width:100px;" onclick="">
            保存
        </button>
    </td>
</div>
<div id="entUpdateTab" class="td_tab"
     style="float:left;width:985px;height:35px;border: none;position: relative;top:-5px;">
    <div>
  		<span class="total_tab">
			<ul>
				<li><a href="#entUpdateSecDiv">企业信息录入</a></li>
			</ul>
		</span>
    </div>
    <!-- 任免表方式录入 -->
    <div id="entUpdateSecDiv" style="width:98%; margin:5px auto;border:0;">
    </div>
</div>


<script type="text/javascript">
    $(document).ready(function () {
//        initiallize("showEntUpdateMain");
        $('#saveAARButton').show();
        $('#print-btn').css('margin-left', '514px');
        $("#entUpdateSecDiv").load(EnvBase.base + 'admin/policy/policyEdit.do', {});
//        window.location.href=   EnvBase.base + 'admin/policy/policyEdit.do';
        $("#cadreAddTab").tabs({
            collapsible: true,
            beforeActivate: function (event, ui) {
                var returnFlag = false;
                if (changeFlag) {
                    if (confirm(saveTip2)) {
                        returnFlag = true;
                    }
                } else {
                    returnFlag = true;
                }
                if (!returnFlag) {
                    changeFlag = true;
                } else {
                    changeFlag = false;
                }
                return returnFlag;
            }
        });
    });
</script>
</body>
