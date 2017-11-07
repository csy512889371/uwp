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

    #totalDiv {
        width: 100%;
        background-color: #fff;
        padding-left: 10px;
        height: 40px;
        line-height: 40px;
        background-color: #fcfcfc;
        margin-bottom: 10px;
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

    #totalTable tr .noborder {
        border: none;
    }

    #sum, #avg {
        font-weight: bolder;
        color: red;
    }
</style>
<body>
<!-- ECharts单文件引入 -->
<script src="<%=basePath%>resources/admin/js/echarts-2.2.7/build/dist/echarts.js"></script>
<script type="text/javascript">
    // 路径配置
    require.config({
        paths: {
            echarts: '<%=basePath%>resources/admin/js/echarts-2.2.7/build/dist'
        }
    });
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('main'));
            option = {
                title: {
                    /*text: '企业基本情况',*/
                    subtext: '收入',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data: ['超过2000(包含)万元', '200(包含)万元-2000万元', '100(包含)万元-200万元']
                },
                calculable: true,
                series: [
                    {
                        name: '收入',
                        type: 'pie',
                        radius: '55%',
                        center: ['60%', '60%'],
                        data: [
//                            {value:163, name:'新入驻'},
                            {value: 10, name: '超过2000(包含)万元'},
                            {value: 100, name: '200(包含)万元-2000万元'},
                            {value: 53, name: '100(包含)万元-200万元'},
                        ]
                    }
                ]
            };
            // 为echarts对象加载数据
            myChart.setOption(option);
        }
    );
    // 折线图
    require(
        [
            'echarts',
            'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('main2'));
            option = {

                grid:{
                    x:'15%'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['产值(万元)', '销售/营业收入(万元)', '盈利（万元）', '税收（万元）']
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: ['2016第1季度', '2016第2季度', '2016第3季度', '2016第4季度', '2017第1季度', '2017第2季度', '2017第3季度', '2017第4季度']
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} (万元)'
                        }
                    }
                ],
                series: [
                    {
                        name: '产值(万元)',
                        type: 'line',
                        stack: '总量',
                        data: invertedArr([27483199.90, 678.00, 1145.00, 1145.00, 13616350.32, 13697149.84, 123.00, 123.00])
                    },
                    {
                        name: '销售/营业收入(万元)',
                        type: 'line',
                        stack: '总量',
                        data: invertedArr([82419402.09, 768.00, 558.00, 558.00, 40988594.61, 25758879.28, 123132.00, 123132.00])
                    },
                    {
                        name: '盈利（万元）',
                        type: 'line',
                        stack: '总量',
                        data: invertedArr([82520733.04, 678.00, 578.00, 558.00, 24239477.21, 22234070.99, 22234070.99, 13.00])
                    },
                    {
                        name: '税收（万元）',
                        type: 'line',
                        stack: '总量',
                        data: invertedArr([27161321.09, 678.00, 558.00, 558.00, 10501064.57, 9236231.22, 312321.00, 312321.00])
                    }
                ]
            };
            // 为echarts对象加载数据
            myChart.setOption(option);
        }
    );

    function invertedArr(arr) {
        var str = "";
        for (var i = arr.length - 1; i >= 0; i--) {
            str += (arr[i] + ',');
        }
        return str.split(",");
    }

    // 条形图
    require(
        [
            'echarts',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('main3'));
            option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                legend: {
                    data: ['销售/营业收入(万元)', '盈利（万元）', '税收（万元）', '增值税（万元）']
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} (万元)'
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'category',
//                        data: '国际组织,公共管理、社会保障和社会组织,文化、体育和娱乐业,卫生和社会工作,教育,居民服务、修理和其它服务业,水利、环境和公共设施管理业,科学研究和技术服务业,租赁和商务服务业,房地产业,金融业,信息传输、软件和信息技术服务业,住宿和餐饮业,交通运输、仓库和邮政业,批发和零售业,建筑业,电力、热力、燃气及水生产和供应业,采矿业,农、林、牧、渔业,制造业\t,其他'.split(',')
                        data: '农、林、牧、渔业,制造业,其他'.split(',')
                    }
                ],
                series: [
                    {
                        name: '销售/营业收入(万元)',
                        type: 'bar',
                        stack: '销售/营业收入(万元)',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [227, 66872797]
                    },
                    {
                        name: '盈利（万元）',
                        type: 'bar',
                        stack: '盈利（万元）',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [ -89, 47586792]
                    },
                    {
                        name: '税收（万元）',
                        type: 'bar',
                        stack: '税收（万元）',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [13, 21162834]
                    },
                    {
                        name: '增值税（万元）',
                        type: 'bar',
                        stack: '增值税（万元）',
                        itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                        data: [367, 27316967]
                    },
                ]
            };
            // 为echarts对象加载数据
            myChart.setOption(option);
        }
    );


</script>


<div style="height:800px;width: 100%;" id="countDiv">
    <div id="totalDiv">
			<span id="sexTitle">
			</span>
        <span style="float: right;margin-right: 4%;display: none;">
				<button class="btn-style4" onclick="excelOutPersonnelStatistics()">
					<i class="icon-share"></i> 导出Excel文件
				</button>
			</span>
    </div>
    <table class="table" id="totalTable" style="border:0px solid #ddd;width:90%;margin: 30px auto;">

        <tr>
            <td class="noborder" style="padding: 10px;">
                <div class="container-fluid">
                    <div class="row-fluid">
                        <div class="span4">
                            <span><a id="a_data_report" href="javascript:void (0) "  style="float: right" onclick=" showHideRep('#a_data_report','#tr_data_report')" >展开</a></span>
                            <div id="main" style="width:100%;height:400px"></div>
                        </div>
                        <div class="span6">
                            <span><a id="a_ind_report" href="javascript:void (0) "  style="float: right" onclick=" showHideRep('#a_ind_report','#tr_ind_report')" >展开</a></span>
                            <div id="main2" style="width:100%;height:400px"></div>
                        </div>
                    </div>
                </div>
            </td>
        </tr>
        <tr id="tr_data_report" style="display: none" >
            <td class="noborder" colspan="2">
                <div style="padding-left: 80px;">
                    <table id="educate" class="table table-bordered" style="width:90%;border:2px solid #ddddd;">
                        <tr>
                            <td class="title_td" colspan="5">
                                <span class="title_span">数据报表</span>
                                <button style="float: right;" id="btn_refresh_data" class="btn-style4"
                                        onclick="doRefreshData()">
                                    <i class="icon-share"></i> 刷新
                                </button>
                            </td>
                        </tr>
                        <tr class="title_tr">
                            <td style="width:300px;">项目名称</td>
                            <td style="width:1000px;">内容</td>
                        </tr>
                        <tr>
                            <td>企业基本情况</td>
                            <td style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;全镇共有<a
                                    id="REPDATA001" <%--onclick="analyEptList('type1');"--%>>${repData.REPDATA001}</a>
                                家企业, 上季度新入驻<a
                                        id="REPDATA002" <%--onclick="analyEptList('type1');"--%>>${repData.REPDATA002}</a>家;<%--迁出 <a id="REPDATA003" onclick="analyEptList('type1');">${repData.REPDATA003}</a>家。--%>
                                本镇收入超过2000(包含)万元
                                <a id="REPDATA004">${repData.REPDATA004}</a>家,收入在200(包含)万元-2000万元有
                                <a id="REPDATA005">${repData.REPDATA005}</a>家,收入在100(包含)万元-200万元有
                                <a id="REPDATA006">${repData.REPDATA006}</a>家
                            </td>
                        </tr>
                        <tr>
                            <td>企业经营情况</td>
                            <td style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;上一季度全区企业销售（营业）收入 <span
                                    id="REPDATA007">${repData.REPDATA007}</span>万元，环比增长 <span
                                    id="REPDATA008">${repData.REPDATA008}</span>；总利润<span
                                    id="REPDATA009">${repData.REPDATA009}</span>万元，环比增长<span
                                    id="REPDATA010">${repData.REPDATA010}</span>；总税收<span
                                    id="REPDATA011">${repData.REPDATA011}</span>万元，环比增长<span
                                    id="REPDATA012">${repData.REPDATA012}</span></td>
                        </tr>
                        <tr>
                            <td>企业分类信息</td>
                            <td style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;全镇共有商贸型企业<a><span
                                    id="REPDATA013">${repData.REPDATA013}</span></a>家，实体型企业<a><span
                                    id="REPDATA014">${repData.REPDATA014}</span></a>家
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>

        <tr  id="tr_ind_report" style="display: none" >
            <td class="noborder" colspan="2">
                <div style="padding-left: 80px;">
                    <table id="educate2" class="table table-bordered" style="width:90%;border:2px solid #ddddd;">
                        <%--筛选--%>
                        <span style="float: left">
                            筛选：
                             年度：
                            <input style="width: 100px;" type="text" id="yearInput" class="i-date"
                                   value="<fmt:formatDate  value="${nowYear}" pattern="yyyy" />"
                                   onfocus="WdatePicker({dateFmt:'yyyy',minDate:'<fmt:formatDate value="${lastYear}"  pattern="yyyy"/>',maxDate:'${nowYear}'})"
                            />
                            &nbsp;&nbsp;
                        </span>
                        <tr>
                            <td class="title_td" colspan="9">
                                <span class="title_span">行业类别分析</span>
                                <button style="float: right;" id="btn_refresh" class="btn-style4"
                                        onclick="doRefreshInd()">
                                    <i class="icon-share"></i> 刷新
                                </button>
                            </td>
                        </tr>
                        <tr id="tr_ind_title" class="title_tr">
                            <td style="width:1000px;">类别</td>
                            <td style="width:1000px;">企业数(家)</td>
                            <td style="width:1000px;">产值(万元)</td>
                            <td style="width:1000px;">销售/营业收入(万元)</td>
                            <td style="width:1000px;">盈利（万元）</td>
                            <td style="width:1000px;">税收（万元）</td>
                            <td style="width:1000px;">增值税（万元）</td>
                            <td style="width:1000px;">企业所得税（万元）</td>
                            <td style="width:1000px;">个人所得税（万元）</td>
                        </tr>
                        <c:forEach items="${repIndList}" var="item">
                            <tr>
                                <td>${item.REPIND002}</td>
                                <td>${item.REPIND010}</td>
                                <td>${item.REPIND003}</td>
                                <td>${item.REPIND004}</td>
                                <td>${item.REPIND005}</td>
                                <td>${item.REPIND006}</td>
                                <td>${item.REPIND007}</td>
                                <td>${item.REPIND008}</td>
                                <td>${item.REPIND009}</td>
                            </tr>
                        </c:forEach>


                    </table>
                </div>
            </td>
        </tr>


        <tr>
            <td class="noborder">
                <div style="padding: 10px;">
                    <div id="main3" style="width:1400px;height:1000px"></div>
                </div>
            </td>
        </tr>

    </table>

    <div style="display:none">
        <form method="post" id="loadFile" action="<%=basePath%>unit/cadre/loadFile.do">
            <input name="fileName" id="loadfileName">
        </form>
    </div>
</div>
<script type="text/javascript">
    var selectedYear = $('#yearInput').val();
    $(document).ready(function () {
        var date = new Date;
        var year = date.getFullYear();
        $('#currentYearBarVer').val(year);
        bindYearChangeEvent('#yearInput', selectedYear)
    });

    function changeEtpOutPutQuarter() {
        if ($("#etpOutPutQuarterBarVer").is(":hidden")) {
            $('#etpOutPutQuarterBarVer').show();
            $('#etpOutPutQuarterBarVerTable').hide();
        } else {
            $('#etpOutPutQuarterBarVer').hide();
            $('#etpOutPutQuarterBarVerTable').show();
        }
    }

    
    function showHideRep(aseltor,trseltor) {
        if($(aseltor).html()=='展开'){
            $(trseltor).show();
            $(aseltor).html('收起');
        }else {
            $(trseltor).hide();
            $(aseltor).html('展开');
        }
    }
    
    //点击人数反查企业信息
    function analyEptList(type) {
        openWindow('企业列表', 'ent/analy/analyEptList.do', 2, '80%', 690, {type: type});
    }

    function doRefreshInd() {
        showWait("正在刷新报表...");
        $.ajax({
            url: "<%=basePath%>ent/analy/doRefreshInd.do",
            type: 'POST', //GET
            async: true,    //或false,是否异步
            dataType: 'json',
            data: {year: $(selector).val()},
            success: function (data) {
                if (data.type == "success") {
                    dealIndData(data);
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

    function doRefreshData() {
        showWait("正在刷新报表...");
        $.ajax({
            url: "<%=basePath%>ent/analy/doRefreshData.do",
            type: 'POST', //GET
            async: true,    //或false,是否异步
            dataType: 'json',
            success: function (data) {
                if (data.type == "success") {
                    var params = JSON.parse(data.params);
                    for (var item in params) {
                        $("#" + item).html(params[item]);
                    }
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


    function dealIndData(data) {
        $("#tr_ind_title").nextAll().remove();
        var arr = JSON.parse(data.params);
        var str = "";
        for (var i = 0; i < arr.length; i++) {
            str += "<tr>";
            var json = arr[i];
            str += "<td>" + json.REPIND002 + "</td><td>" + json.REPIND010 + "</td><td>" + json.REPIND003 + "</td>" +
                "<td>" + json.REPIND004 + "</td><td>" + json.REPIND005 + "</td><td>" + json.REPIND006 + "</td>" +
                "<td>" + json.REPIND007 + "</td><td>" + json.REPIND008 + "</td><td>" + json.REPIND009 + "</td>";
            str += "</tr>";
        }
        $("#tr_ind_title").after($(str));
    }


    function getIndDataByYear(selectYear) {
        showWait("正在刷新报表...");
        $.ajax({
            url: "<%=basePath%>ent/analy/getRepIndList.do",
            type: 'POST', //GET
            async: true,    //或false,是否异步
            dataType: 'json',
            data: {year: selectYear},
            success: function (data) {
                if (data.type == "success") {
                    dealIndData(data);
                }
                closeWait();
            },
            complete: function () {
                closeWait();
            }
        });
    }

    function bindYearChangeEvent(selector, selectYear) {
        $(selector).bind('focus blur propertychange', function () {
            if ($(selector).val() != selectYear) {
                selectYear = $(selector).val();
                getIndDataByYear(selectYear);
            }

        });
    }

</script>
</body>