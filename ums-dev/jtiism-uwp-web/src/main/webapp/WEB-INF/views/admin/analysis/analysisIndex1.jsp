<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
    .title_tr td{font-weight: bolder;background-color: #fcfcfc;}
    #countDiv table tr td{text-align: center;}
    #totalDiv{width:100%;background-color: #fff;padding-left:10px;
        height:40px;line-height: 40px;background-color: #fcfcfc;margin-bottom: 10px;}
    .title_td{background-color: #2898fd;color: #fff;text-align: left;font-weight: bolder;}
    .title_span{float:left;margin-top: 5px;margin-left: 5px;}
    #totalTable tr .noborder{border: none; }
    #sum,#avg{font-weight: bolder;color:red;}
</style>

<!-- ECharts单文件引入 -->
<script src="<%=basePath%>resources/admin/js/echarts/echarts-all.js"></script>

<body>
<div style="height:800px;width: 100%;" id="countDiv">
    <div id="totalDiv">
			<span id="sexTitle">
			</span>
        <span style="float: right;margin-right: 4%">
				<button class="btn-style4" onclick="excelOutPersonnelStatistics()">
					<i class="icon-share"></i> 导出Excel文件
				</button>
			</span>
    </div>
    <table class="table" id="totalTable" style="border:0px solid #ddd;width:90%;margin: 30px auto;">

        <tr>
            <td class="noborder" colspan="2">
                <div style="padding-left: 80px;">
                    <table id="educate" class="table table-bordered" style="width:90%;border:2px solid #ddddd;">
                        <tr>
                            <td class="title_td" colspan="5">
                                <span class="title_span">数据报表</span>
                            </td>
                        </tr>
                        <tr class="title_tr">
                            <td style="width:300px;">项目名称</td>
                            <td style="width:1000px;">内容</td>
                        </tr>
                        <tr>
                            <td>企业基本情况</td>
                            <td>本季度全镇共有<a onclick="analyEptList('type1');">5000</a>家企业, 新入驻<a onclick="analyEptList('type1');">30</a>家,迁出<a onclick="analyEptList('type1');">0</a>家。
                                <br>其中收入超过2000(包含)元<a>1000</a>家,其中收入在200(包含)万元-2000万元有<a>3000</a>家,其中收入在100(包含)万元-200万元有<a>4000</a>家
                            </td>
                        </tr>
                        <tr>
                            <td>企业经营情况</td>
                            <td>上一季度全区企业销售（营业）收入6666万元，环比增长20%；总利润5555万元，环比增长8%；总税收6868万元，环比增长66%</td>
                        </tr>

                        <tr>
                            <td>企业分类信息</td>
                            <td>全镇共有商贸型企业<a>100</a>家，实体型企业<a>200</a>家</td>
                        </tr>

                    </table>
                </div>
            </td>
        </tr>

        <tr>
            <td class="noborder" style="width: 600px;">
                <div style="padding: 10px;">
                    <div align="right"; style="padding-right: 6px;">
                        <span class="text-primary">年份</span>
                        <input type="text" style="width: 50px;" name="currentYear" id="currentYearBarVer" class="i-date input2" onfocus="WdatePicker({dateFmt:'yyyy'})" value="">
                        <input type="button" class="btn btn-sm btn-success" onclick="changeEtpOutPutQuarter();" value="切换视图"></input>
                    </div>
                    <!-- 季度各类企业销售（营业）收入  柱状图 -->
                    <div id="etpOutPutQuarterBarVer" style="height:400px"></div>
                    <div id="etpOutPutQuarterBarVerTable" style="display: none;padding-top: 80px;padding-left: 80px;" >
                        <table id="educate" class="table table-bordered" style="width:90%;border:2px solid #ddddd;">
                            <tr>
                                <td class="title_td" colspan="3">
                                    <span class="title_span">销售（营业）收入 (万元)</span>
                                </td>
                            </tr>
                            <tr class="title_tr">
                                <td style="width:1000px;">季度</td>
                                <td style="width:1000px;">工业</td>
                                <td style="width:1000px;">商业</td>
                            </tr>
                            <tr>
                                <td>第一季度</td>
                                <td style="text-decoration:underline;color:#000;cursor:pointer;" onclick="cadreCountCheck('')">5000</td>
                                <td style="text-decoration:underline;color:#000;cursor:pointer;" onclick="cadreCountCheck('')">90000</td>
                            </tr>
                            <tr>
                                <td>第二季度</td>
                                <td style="text-decoration:underline;color:#000;cursor:pointer;" onclick="cadreCountCheck('')">456456</td>
                                <td style="text-decoration:underline;color:#000;cursor:pointer;" onclick="cadreCountCheck('')">345345</td>
                            </tr>
                            <tr>
                                <td>第三季度</td>
                                <td style="text-decoration:underline;color:#000;cursor:pointer;" onclick="cadreCountCheck('')">456456</td>
                                <td style="text-decoration:underline;color:#000;cursor:pointer;" onclick="cadreCountCheck('')">34534</td>
                            </tr>
                            <tr>
                                <td>第四季度</td>
                                <td style="text-decoration:underline;color:#000;cursor:pointer;" onclick="cadreCountCheck('')">456546</td>
                                <td style="text-decoration:underline;color:#000;cursor:pointer;" onclick="cadreCountCheck('')">345345</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </td>


            <td class="noborder">
                <div style="padding: 10px;">
                    <!-- 年度各类企业税收收入  柱状图 -->
                    <div id="etpYearTaxLine" style="height:400px"></div>
                </div>
            </td>
        </tr>


        <tr>
            <td class="noborder" colspan="2">
                <div style="padding-left: 80px;">
                    <table id="educate" class="table table-bordered" style="width:90%;border:2px solid #ddddd;">
                        <tr>
                            <td class="title_td" colspan="5">
                                <span class="title_span">行业类别分析</span>
                            </td>
                        </tr>
                        <tr class="title_tr">
                            <td style="width:1000px;">类别</td>
                            <td style="width:1000px;">企业数(家)</td>
                            <td style="width:1000px;">收入(万元)</td>
                            <td style="width:1000px;">盈利(万元)</td>
                            <td style="width:1000px;">税收(万元)</td>
                        </tr>
                        <tr>
                            <td>电子</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>玩具生产</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>物流</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>贸易</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>仪器仪表</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>塑料制品</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>家用制品</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>

                        <tr>
                            <td>电器设备</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>

                        <tr>
                            <td>产房出租</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>其他行业</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>机械</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>五金加工</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>纸制品</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>木制品</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>宾馆酒家</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>汽车配件</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>家具制造</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>纺织</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>娱乐场所</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>超市、卖场</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>
                        <tr>
                            <td>食品生产</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>

                        <tr>
                            <td>服装</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>

                        <tr>
                            <td>印刷包装</td>
                            <td>1000</td>
                            <td>6666</td>
                            <td>88888</td>
                            <td>123231</td>
                        </tr>

                    </table>
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




    //3.季度柱状图垂直begin
    var myChartOutPutBarVer = echarts.init(document.getElementById('etpOutPutQuarterBarVer'));
    var optionOutPutBarVer = {
        title : {
            text: '销售（营业）收入',
            subtext: '单位（万元）'
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['工业','商业']
        },
        toolbox: {
            show : true,
            feature : {
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                myTool : {
                    show : true,
                    title : '刷新报表',
                    icon : 'image://http://echarts.baidu.com/images/favicon.png',
                    onclick : function (){
                        alert('重新生成报表数据');
                    }
                },
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['第一季度','第二季度','第三季度','第四季度']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'工业',
                type:'bar',
                data:[50000, 9000, 6666, 100000]
            },
            {
                name:'商业',
                type:'bar',
                data:[40000, 88888, 50000, 5000]
            }
        ]
    };

    myChartOutPutBarVer.setOption(optionOutPutBarVer);
    //3.季度柱状图垂直end


    //5.环比年度税收 折线 begin
    var myChartYearTaxLine = echarts.init(document.getElementById('etpYearTaxLine'));
    var optionYearTaxLine = {
        title : {
            text: '5年内税收情况',
            subtext: '单位（万元）'
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['工业','商业','总体']
        },
        toolbox: {
            show : true,
            feature : {
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['2013年','2014年','2015年','2016年','2017年']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'工业',
                type:'line',
                stack: '总量',
                data:[1010, 1340, 9000, 2300, 2100]
            },
            {
                name:'商业',
                type:'line',
                stack: '总量',
                data:[2200, 1820, 1910, 2340, 2900]
            },
            {
                name:'总体',
                type:'line',
                stack: '总量',
                data:[3210, 3160, 10910, 4640, 5000]
            }
        ]
    };

    myChartYearTaxLine.setOption(optionYearTaxLine);
    //5.环比年度税收 折线 end

    $(document).ready(function(){
        var date=new Date;
        var year=date.getFullYear();
        $('#currentYearBarVer').val(year);


    });

    function changeEtpOutPutQuarter() {
        if($("#etpOutPutQuarterBarVer").is(":hidden")){
            $('#etpOutPutQuarterBarVer').show();
            $('#etpOutPutQuarterBarVerTable').hide();
        } else {
            $('#etpOutPutQuarterBarVer').hide();
            $('#etpOutPutQuarterBarVerTable').show();
        }

    }

    //点击人数反查企业信息
    function analyEptList(type){
        openWindow('企业列表','ent/analy/analyEptList.do',2,'80%',690,{type:type});
    }

</script>
</body>