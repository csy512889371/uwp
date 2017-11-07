<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../common.inc" %>

<!-- ECharts单文件引入 -->
<script src="<%=basePath%>resources/admin/js/echarts/echarts-all.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/admin/modules/analysis/etpsAnalysisIndex.js"></script>
<link rel="stylesheet" href="<%=basePath%>resources/admin/css/bootstrap.css">
<style>
    .title_tr td {
        font-weight: bolder;
        background-color: #fcfcfc;
    }

    .title_td {
        background-color: #2898fd;
        color: #fff;
        text-align: left;
        font-weight: bolder;
    }

    .title_span {
        float: left;
        margin-top: 5px;
        margin-left: 5px;
    }

    tbody {
        width: 100px;
    }

    .row-fluid .span24-11 {
        width: 2.083333333333334%;
        *width: 2.083333333333334%;
    }

    .row-fluid .span24-1 {
        width: 4.166666666666667%;
        *width: 4.166666666666667%;
    }

    .row-fluid .span24-22 {
        width: 91.66666666666667%;
        *width: 91.66666666666667%;
    }

    .header-table {
        border: 0px solid #ddd;
        width: 89%;
        margin-left: 5.6%;
        margin-bottom: 3px;
    }

</style>

<body>

<%--税收环比、同比--%>
<div>
    <table class="table header-table">
        <tr>
            <td class="title_td" colspan="5">
                <span class="title_span">税收统计</span>
                <span class="title_span" style="float: left; ">年度：</span>
                <input style="width: 100px;float: left;" type="text" id="yearInput" class="i-date"
                       value="<fmt:formatDate  value="${nowYear}" pattern="yyyy" />"
                       onfocus="WdatePicker({dateFmt:'yyyy',minDate:'<fmt:formatDate value="${lastYear}"
                                                                                     pattern="yyyy"/>',maxDate:'${nowYear}'})"
                />
                <span style="float: right;margin-right: 4%">
            <button class="btn-style4" onclick="exportAnaForm()">
            <i class="icon-share"></i> 导出Excel文件
            </button>
        </span>
            </td>
        </tr>
    </table>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span24-11">
            </div>
            <div class="span24-22" style="overflow-x: hidden">
                <table id="etpsStaticYosListRevenue" class="mmg" STYLE="height: 750px">
                    <tr>
                        <th rowspan="" colspan=""></th>
                    </tr>
                </table>
                <div id="pgs1" style="text-align: right;" class="bck1"></div>
            </div>
            <div class="span24-1 ">
            </div>
        </div>
    </div>
</div>


<%--销售环比、同比--%>
<div>
    <table class="table header-table">
        <tr>
            <td class="title_td" colspan="5">
                <span class="title_span">销售统计</span>
                <span class="title_span" style="float: left; ">年度：</span>
                <input style="width: 100px;float: left;" type="text" id="yearInputSale" class="i-date"
                       value="<fmt:formatDate  value="${nowYear}" pattern="yyyy" />"
                       onfocus="WdatePicker({dateFmt:'yyyy',minDate:'<fmt:formatDate value="${lastYear}"
                                                                                     pattern="yyyy"/>',maxDate:'${nowYear}'})"
                />
                <span style="float: right;margin-right: 4%">

        </span>
            </td>
        </tr>
    </table>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span24-11">
            </div>
            <div class="span24-22" style="overflow-x: hidden">
                <table id="etpsStaticYosListSale" class="mmg" STYLE="height: 750px">
                    <tr>
                        <th rowspan="" colspan=""></th>
                    </tr>
                </table>
                <div id="pgs2" style="text-align: right;overflow:hidden" class="bck1"></div>
            </div>
            <div class="span24-1">
            </div>
        </div>
    </div>
</div>


<%--利润环比、同比--%>
<div>
    <table class="table header-table">
        <tr>
            <td class="title_td" colspan="5">
                <span class="title_span">利润统计</span>
                <span class="title_span" style="float: left; ">年度：</span>
                <input style="width: 100px;float: left;" type="text" id="yearInputProfit" class="i-date"
                       value="<fmt:formatDate  value="${nowYear}" pattern="yyyy" />"
                       onfocus="WdatePicker({dateFmt:'yyyy',minDate:'<fmt:formatDate value="${lastYear}"
                                                                                     pattern="yyyy"/>',maxDate:'${nowYear}'})"
                />
                <span style="float: right;margin-right: 4%">

        </span>
            </td>
        </tr>
    </table>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span24-11">
            </div>
            <div class="span24-22" style="overflow-x: hidden">
                <table id="etpsStaticYosListProfit" class="mmg" STYLE="height: 750px">
                    <tr>
                        <th rowspan="" colspan=""></th>
                    </tr>
                </table>
                <div id="pgs3" style="text-align: right;" class="bck1"></div>
            </div>
            <div class="span24-1">
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">

    //    showWait('数据生成中，请稍候');
    var token = '${token}';
    var entIds = "${entIds}";
    $(document).ready(function () {
        analyYear = $("#yearInput").val();
        analyYearSale = $("#yearInputSale").val();
        analyYearProfit = $("#yearInputProfit").val();

        loadAnaList();
        loadAnaListProfit();
        loadAnaListSale();
        bindYearChangeEvent("#yearInput", revenueList, analyYear, entIds);
        bindYearChangeEvent("#yearInputProfit", profitList, analyYearProfit, entIds);
        bindYearChangeEvent("#yearInputSale", saleList, analyYearSale, entIds);
        closeWait();
    });


    function loadAnaList() {
        revenueList = $('#etpsStaticYosListRevenue').mmGrid({
            fullWidthRows: true,
            height: commonTalbeHeight - 300,
            cols: revennueCols,
            url: EnvBase.base + 'ent/analy/repRevenueDatas.do',
            method: 'post',
            remoteSort: true,
            sortName: 'SECUCODE',
            sortStatus: 'asc',
            root: 'resultMapList',
            nowrap: true,
            autoLoad: false,
            showBackboard: false,
            plugins: [$('#pgs1').mmPaginator({
                limitList: [50, 100, 150, 200, 250], mySearch: function (page) {
                    revenueList.load({
                        pageNumber: page ? page : 1,
                        entIds: entIds,
                        year: analyYear,
                        token: token
                    });
                }
            })],
            params: function () {
                // 如果这里有验证，在验证失败时返回false则不执行AJAX查询。
                return {
                    year: analyYear,
                    entIds: "${entIds}"
                };
            }
        });
        revenueList.on('cellSelected', function (e, item, rowIndex, colIndex) {
            /* doubleClickRow(item, rowIndex, e);
             if ($(e.target).is('.btn-info, .btnPrice')) {
                 // 阻止事件冒泡
                 e.stopPropagation();
             }*/
        }).load({pageNumber: 1});
        return revenueList;
    }

    function loadAnaListSale() {
        saleList = $('#etpsStaticYosListSale').mmGrid({
            fullWidthRows: true,
            height: commonTalbeHeight - 300,
            cols: saleCols,
            url: EnvBase.base + 'ent/analy/repSaleDatas.do',
            method: 'post',
            remoteSort: true,
            sortName: 'SECUCODE',
            sortStatus: 'asc',
            root: 'resultMapList',
            nowrap: true,
            autoLoad: false,
            showBackboard: false,
            plugins: [$('#pgs2').mmPaginator({
                limitList: [50, 100, 150, 200, 250], mySearch: function (page) {
                    saleList.load({
                        pageNumber: page ? page : 1,
                        entIds: entIds,
                        year: analyYearSale,
                        token: token
                    });
                }
            })],
            params: function () {
                return {
                    year: analyYearSale,
                    entIds: "${entIds}"
                };
            }
        });
        saleList.on('cellSelected', function (e, item, rowIndex, colIndex) {
            doubleClickRow(item, rowIndex, e);
            if ($(e.target).is('.btn-info, .btnPrice')) {
                e.stopPropagation();
            }
        }).load({pageNumber: 1});
        return saleList;
    }

    function loadAnaListProfit() {
        profitList = $('#etpsStaticYosListProfit').mmGrid({
            fullWidthRows: true,
            height: commonTalbeHeight - 300,
            cols: profitCols,
            url: EnvBase.base + 'ent/analy/repProfitDatas.do',
            method: 'post',
            remoteSort: true,
            sortName: 'SECUCODE',
            sortStatus: 'asc',
            root: 'resultMapList',
            nowrap: true,
            autoLoad: false,
            showBackboard: false,
            plugins: [$('#pgs3').mmPaginator({
                limitList: [50, 100, 150, 200, 250], mySearch: function (page) {
                    profitList.load({
                        pageNumber: page ? page : 1,
                        entIds: entIds,
                        year: analyYearProfit,
                        token: token
                    });
                }
            })],
            params: function () {
                return {
                    year: analyYearProfit,
                    entIds: "${entIds}"
                };
            }
        });
        profitList.on('cellSelected', function (e, item, rowIndex, colIndex) {
            doubleClickRow(item, rowIndex, e);
            if ($(e.target).is('.btn-info, .btnPrice')) {
                e.stopPropagation();
            }
        }).load({pageNumber: 1});
        return profitList;
    }

    function bindYearChangeEvent(selector, mmgridList, selectYear, entIds) {
        $(selector).bind('focus blur propertychange', function() {
            if ($(selector).val() != selectYear) {
                selectYear = $(selector).val();
                mmgridList.load(
                    {
                        entIds: entIds,
                        year: selectYear
                    }
                );
            }
        });
    }


    function exportAnaForm() {
        openWindow('Excel表单-导出', '/report/engine/excelCadreTemplateAnalysis.do', 2, 400, 345, {
            type: "bySelEnterprise",
            enterprises: entIds,
            token: token
        });
    }


</script>
</body>