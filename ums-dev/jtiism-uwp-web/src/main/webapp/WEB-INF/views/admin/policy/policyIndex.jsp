<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../common.inc" %>
<link href="<%=basePath%>resources/admin/modules/enterprise/enterprise.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<%=basePath%>resources/admin/modules/common/util.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/admin/modules/policy/policy.js"></script>

<script type="text/javascript">
    var policyList;
    $(document).ready(function () {
        document.getElementById("policyShowTab").oncontextmenu = mouseClick;
        loadPolicyTree("admin");
        clickSearchArea(2);
        policyList = loadPolicyList();
//        currSearchType = 2;
    });
</script>

<body>
<div style="overflow: hidden">
    <div style="float: left;width: 260px;" id="policyTreeDiv">
        <!-- 库 -->
        <div class="oper_menu subSys bck" id="policy_group_select"
             style="margin-bottom: 0px;border-right: 1px solid #b8c0cc;">
        </div>
        <!-- 机构树 -->
        <div id="policyShowTab" class="border-right" style="margin-top: 0px;" onclick="clickSearchArea(1)">
            <div id="colorBlock" class="bck1"></div>
            <div id="policyTreeTab"
                 style="overflow: auto;border-bottom:1px solid #b8c0cc;border-top: 1px solid #b8c0cc;"
                 class="policy_search">
                <ul id="coderTree" class="ztree" style="width:95%;"></ul>
            </div>
        </div>
        <div style="margin-bottom: 0px;width:260px;height:5px;position: fixed;" class="bck1">

        </div>
    </div>
    <!-- 切换显示隐藏的bar -->
    <div id="switchBar" class="bck1" onclick="switchShow()">
        <i id="switchHref" class="icon-chevron-left"></i>&nbsp;&nbsp;&nbsp;
    </div>
    <!-- 查询条件 -->
    <div id="enterpriseSearch" style="margin-left: 273px; position: relative;padding-top: 0px;">
        <div id="etpSearchMenu" class="oper_menu bck" onclick="clickSearchArea(2)">
            &nbsp;&nbsp;
            <span style="font-size:13px;">政策类型：</span>
            <input class="input1 selector1" id="policyType" type="text" style="width: 100px"
                   onblur="parent.cleanNull('policyType','plcy001')"
                   onfocus="parent.loadDict('SHJT_POLICY','plcy001','policyType','plcy001')"
                   ondblclick="parent.dictWindow('SHJT_POLICY','plcy001','政策类型','policyType','plcy001')"
            >
            <input type="hidden" id="plcy001" name="plcy001">
            <span style="font-size:13px;">发布来源：</span>
            <input class="input1 selector1" id="policySrc" type="text" style="width: 100px"
                   onblur="parent.cleanNull('policySrc','plcy008')"
                   onfocus="parent.loadDict('SHJT_POLICY','plcy008','policySrc','plcy008')"
                   ondblclick="parent.dictWindow('SHJT_POLICY','plcy008','政策类型','policySrc','plcy008')"
            >
            <input type="hidden" id="plcy008" name="plcy008">
            <span style="font-size:13px;">标题：</span>
            <input type="text" id="plcy002" name="plcy002" class="input4" class="searchName"
                   onkeydown="keydownEvent()" onmouseover="this.title=this.value" style="width: 130px">
            <button class="btn-style1" onclick="searchBtns()">查询</button>
            <button class="btn-style2" onclick="cleanEnterprise()">重置</button>
        </div>
        <!-- 复选框、按钮 -->
        <div class="oper_menu button_bar bck">
            &nbsp;&nbsp;
            <div style="display: inline;">
                <span id="handleBtn">
                      <button class="btn-style5 slef-style" onclick="openPolicyEdit()">
					  	<i></i> 新增政策
				    </button>
                        <button class="btn-style5 slef-style" onclick="deletePolicy()">
					  	<i class="icon-trash"></i> 删除
				    </button>
			   </span>
            </div>
        </div>
        <!-- 企业信息列表 -->
        <div>
            <div id="policyListDiv">
                <table id="policyList" class="mmg">
                    <tr>
                        <th rowspan="" colspan=""></th>
                    </tr>
                </table>
            </div>
            <div id="pgs" style="text-align: right;" class="bck1"></div>
        </div>
    </div>
</div>
</body>