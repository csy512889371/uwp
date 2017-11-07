<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="../../common.inc" %>
<body>
<div class="oper_menu">
    <div style="display: inline;">
        <auth:auth ifAllGranted="sysParCache:edit">
            <button class="btn-style4" onclick="editSetting()"><i class="icon-pencil"></i>编辑系统设置</button>
        </auth:auth>
        <button class="btn-style4" onclick="resertSetting()"><i class="icon-refresh"></i>刷新</button>
    </div>
    <div style="display: inline;">&nbsp;&nbsp;&nbsp;&nbsp;</div>
</div>
<form action="" method="post" id="updateSettingForm">
    <div style="margin-top:10px;">
        <table id="SettingInfoList" class="table table-bordered">


            <tr>
                <td width="15%">资源文件路径:</td>
                <td width="35%"><input type="text" name="resourcePath" value="${ setting.resourcePath}"/></td>
                <td width="15%">导出时临时生成文件路径:</td>
                <td width="35%"><input type="text" name="accountLockTime" value="${setting.dynaReportTempDoc}"/></td>
            </tr>

            <tr>

                <td width="15%">系统地址:</td>
                <td width="35%"><input type="text" name="siteUrl" value="${setting.siteUrl}"/></td>
            </tr>
            <tr>
                <td width="15%">营业执照路径:</td>
                <td width="35%"><input type="text" name="licensePicPath" value="${setting.licensePicPath}"/></td>
                <td width="15%">营业执照缩略图路径:</td>
                <td width="35%"><input type="text" name="licenseThumbPath" value="${ setting.licenseThumbPath}"/></td>
            </tr>
            <tr>
                <td width="15%">宣传照路径:</td>
                <td width="35%"><input type="text" name="publicPicPath" value="${setting.publicPicPath}"/></td>
                <td width="15%">宣传照缩略图路径:</td>
                <td width="35%"><input type="text" name="publicThumbPath" value="${ setting.publicThumbPath}"/></td>
            </tr>

            <tr>
                <td width="15%">用户组根节点名:</td>
                <td width="35%"><input type="text" name="userGroupRootName" value="${setting.userGroupRootName}"/></td>
                <td width="15%"></td>
                <td width="35%"></td>
            </tr>
            <tr>
                <td width="15%">锁定时间（单位分钟）:</td>
                <td width="35%"><input type="text" name="accountLockTime" value="${setting.accountLockTime}"/></td>
                <td width="15%">密码输入失败次数锁定:</td>
                <td width="35%"><input type="text" name="accountLockCount" value="${setting.accountLockCount}"/></td>
            </tr>

            <tr>
                <td width="15%">企业重置密码:</td>
                <td width="35%"><input type="text" name="originPassword" value="${setting.originPassword}"/></td>
                <td width="15%">租赁后台管理地址</td>
                <td width="35%"><input type="text" name="accountLockCount" value="${setting.appAdminUrl}"/></td>
            </tr>
        </table>
        <div>
        </div>
    </div>
    <div style="margin-left:87%;">
        <button type="submit" id="submitSetting" class="btn-style1">&nbsp;保存&nbsp;</button>
    </div>
</form>
<div id="loading" style="display: none;"></div>
<div id="model" style="display: none;background-color: #f5f5f5;height:30px;width:200px;">
    <font size="5px"> &nbsp;&nbsp;&nbsp;更新中。。。</font></div>
<script>
    var editting = false;
    $(document).ready(function () {
        $('#SettingInfoList input').attr("disabled", "disabled");
        $('#SettingInfoList select').attr("disabled", "disabled");
        $('#submitSetting').attr("disabled", "disabled");

        $('#updateSettingForm').validate({
            rules: {
                cadreActivityDocName: {
                    required: true,
                    maxlength: 50
                }
            }
        });
        var options = {
            success: function (result) {
                openAlert(result.content);
                if (result.type == 'success') {
                    $('#SettingInfoList input').attr("disabled", "disabled");
                    $('#SettingInfoList select').attr("disabled", "disabled");
                }
            },
            dataType: 'json',
            url: "<%=basePath%>sys/setting/update.do"
        };
        $('#updateSettingForm').ajaxForm(options);
    });

    function editSetting() {
        $('#SettingInfoList input').removeAttr("disabled");
        $('#SettingInfoList select').removeAttr("disabled");
        $('#submitSetting').removeAttr("disabled");

    }

    function resertSetting() {
        $('#mainContent').load('<%=basePath%>sys/setting/manage.do', {});
    }

    function selectDept() {
        openWindow('管理权限选择', 'unit/exchange/selectDept.do', 2, 300, 400, {});
    }
</script>
</body>