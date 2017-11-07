<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../common.inc" %>
<style>
    .title_tr td {
        font-weight: bolder;
        background-color: #fcfcfc;
    }

    #countDiv table tr td {
        text-align: center;
    }

    #totalTable tr .noborder {
        border: none;
    }

    #totalTable {
        border: 0px solid #ddd;
        width: 80%;
        margin-left: 50px;
    }

    #refAll {
        height: 300px;
        width: 90%;
        padding-top: 30px;
    }

    #labelTd {
        text-align: left;
        font-weight: bold;
    }

    #rightTd {
        border-bottom: 1px royalblue solid;
    }

    #rightSpan {
        padding-left: 150px;
        padding-right: 10px;
    }

    #yearInput {
        width: 60px;
        padding-right: 10px;
    }
</style>

<body>
<div id="refAll">
    <table class="table" id="totalTable">
        <tr>
            <td id="labelTd" class="noborder">
                <span>经营情况统计分析数据:</span>
            </td>
        </tr>
        <tr id="rightTd">
            <td class="noborder" style="text-align: left;">
                <span id="rightSpan" style="">年度:</span>
                <input type="text"  id="yearInput"   class="i-date"  value="<fmt:formatDate  value="${nowYear}" pattern="yyyy" />"
                       onfocus="WdatePicker({dateFmt:'yyyy',minDate:'<fmt:formatDate  value="${lastYear}" pattern="yyyy" />',maxDate:'<fmt:formatDate  value="${nowYear}" pattern="yyyy" />'})"
                />
                <input type="button" class="btn-style1" value="刷新" onclick="doRrfresh()">
            </td>
        </tr>

    </table>

</div>
<script type="text/javascript">

    $(document).ready(function () {

    });

    function doRrfresh() {
        var year = $("#yearInput").val();
        showWait("正在刷新"+year+"年度报表...");
        var year = $("#yearInput").val();
        if (year == '') {
            openAlert("请输入年度！");
            return;
        }
        $.ajax({
            url: "<%=basePath%>ent/analy/doRefresh.do",
            type: 'POST', //GET
            async: true,    //或false,是否异步
            data: {year: $("#yearInput").val()},
            dataType: 'json',
            success: function (data) {
                if (data.type == "success") {
                    openAlert("刷新成功！");
                } else if (data.type == "error") {
                    openAlert("刷新失败！");
                } else {
                    openAlert("刷新失败！请与管理员联系！");
                }
            },
            error: function () {
                alert("刷新失败！请与管理员联系！")
                closeWait();
            },
            complete: function () {
                closeWait();
            }
        });
    }
</script>
</body>