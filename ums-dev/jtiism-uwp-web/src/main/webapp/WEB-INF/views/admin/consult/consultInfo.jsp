<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/ueditor/ueditor.config.consultAns.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/admin/modules/consult/consult.js"></script>
	<link rel="stylesheet" href="<%=basePath%>resources/admin/js/layui/css/layui.css">
	<link href="<%=basePath%>resources/admin/modules/enterprise/enterprise.css" rel="stylesheet" type="text/css">
	<script>
		var consultList;
        var ue = UE.getEditor('editor',{
            UEDITOR_HOME_URL: parent.EnvBase.base + 'resources/admin/js/ueditor/',
            //是否自动长高,默认true
            autoHeightEnabled:false,
            //是否启用元素路径，默认是显示
            elementPathEnabled : false,
            //是否开启字数统计
            wordCount:false
        });
        $(document).ready(function () {
            document.getElementById("consultTreeContainer").oncontextmenu = mouseClick;
            clickSearchArea(2);
            loadConsultTypeTree("ent");
            consultList = loadConsultList();
            $("#consultDetailDiv").hide();
            $("#backtolistbtn").hide();
        });

        function checkUeditorHasText() {
            var text = ue.getContentTxt();
            layui.use('layer', function () {
                var layer = layui.layer;
                if (text==''){
                    ue.focus();
                    layer.msg('请输入答案！');
                    return false;
                }
                $('#cstans002').val(UE.utils.unhtml(ue.getContent()) );
                var cstinf000 = $('#cstinf000').val();
                $.ajax({
                    type : 'post',
                    url  : EnvBase.base + 'ent/consult/answerQuestion.do',
                    data : {"cstans002": UE.utils.unhtml(ue.getContent()), "cstinf000": cstinf000},
                    async:false,
                    success: function(data){
                        //答案保存成功后清空UEditor中的内容。
                        ue.setContent("");
                        if(data=="1") {
                            layer.msg('保存成功');
                            //重新载入数据
                            openConsultDetail(cstinf000,'ent');
                        }
                    }
                });
                return true;
            });
        }

        /** 滑出富文本输入框status(0：闭合，1：展开) **/
        function toggle() {
            if($("#editorInput").is(":hidden")){
                //更改回答列表高度
                $('#ans').css('height','350px');
                $("#editorInput").slideDown();
            }else{
                $('#ans').css('height','600px');
                $("#editorInput").slideUp();
            }
        }

	</script>
	<style>
		.oper_menu{font-size:12px;}
		#cosultTreeDiv{border: none;}
		#switchBar{height:100%;cursor: pointer;float: left;width:13px;background-color: #fff;border-right: 1px solid #b8c0cc;}
		#switchBar:hover{background-color: #f7f7f7;}
		#switchHref{top: 50%;position: absolute;}
		#consultTreeContainer{width:270px;float: left;border-right: 1px solid #b8c0cc;}

		.bd-form {
			border: 1px solid rgb(225, 225, 225)
		}

		.pl-ue-content {

		}

		.pl-ue-content table {
			border-collapse: collapse
		}

		.pl-ue-content td {
			padding: 5px 10px;
			border: 1px solid #ddd
		}

		.km_consult_nav {
			background: url(../../resources/admin/img/question_nav.png) no-repeat left top;
			padding-left: 60px;
		}


		.bd-form {
			border: 1px solid rgb(225, 225, 225)
		}

		.bd-notop {
			border-top: 0 !important
		}
		.km_answer_count {
			height: 36px;
			line-height: 36px;
			margin-top: 24px;
			padding: 0px 12px;
			font-size: 15px;
			background-color: #FAFAFA
		}

		.km_answer_bg {
			border-color: #D9D9D9;
		}
		.layui-input-block {
			margin-left: 0;
		}
		.ansScroll {
			overflow :auto;
			height:350px;
		}
	</style>
</head>

<body>
	<div>
		<!-- 库及机构树 -->
		<div id="consultTreeContainer">
			<!-- 机构树 -->
		    <div id="cosultTreeDiv">
			    <div id="consultTreeTab" style="overflow: auto;">
					<ul id="cosultTree" class="ztree" style="width:95%;"></ul>
				</div>
			</div>
		</div>
		<!-- 切换显示隐藏的bar -->
		<div id="switchBar" onclick="switchShow()">
			<i id="switchHref" class="icon-chevron-left"></i>&nbsp;&nbsp;&nbsp;
	</div>
		<!-- 菜单及单位列表 -->
		<div style="margin-left: 284px;position: relative;" id="rightDiv">
			<!-- 菜单 -->
			<div class="oper_menu" style="border-left:1px solid #b8c0cc;text-align: left;">
				<%--<button class="btn-style4" onclick="addConsult()"><i class="icon-pencil"></i>提问</button>--%>
				<span>
					<div id="searchDiv" style="display: inline-block;">
						<span style="font-size:14px;">标题：</span>
						<input type="text" id="cstinf003" name="cstinf003" class="input4 searchName" value="">
						<button class="btn-style1" onclick="searchConsult()">搜索</button>
						<button class="btn-style1" onclick="cleanSearch()">重置</button>
					</div>
					<button id="backtolistbtn" class="btn-style1" onclick="backtolist()">返回</button>
					<button class="btn-style1" onclick="answerQuestion(1)">回答问题</button>
				</span>
			</div>
			<div id="buttonBar" class="oper_menu button_bar bck">
				&nbsp;&nbsp;
				<div style="display: inline;">
					<span id="handleBtn">
						<auth:auth ifAllGranted="enterpriseView:delete">
							<button class="btn-style5 slef-style" onclick="deleteConsult('ent')">
							<i class="icon-trash"></i> 删除
						</button>
						</auth:auth>
			   		</span>
				</div>
			</div>
			<!-- 咨询列表 -->
			<div id="cstinfList">
				<div id="consultListDiv">
					<table id="consultList" class="mmg">
						<tr>
							<th rowspan="" colspan=""></th>
						</tr>
					</table>
				</div>
				<div id="pgs" style="text-align: right;" class="bck1"></div>
			</div>
			<!-- 咨询详情 -->
			<div>
				<input id="cstinf000" type="hidden" name="cstinf000" value="">
				<div id="consultDetailDiv" style="margin-top: 20px; width: 95%;height: 95%;margin-left: 25px;">
					<div id="consultinfo" class="pl-ue-content bd-form km_answer_bg">
						<div>
							<div style="padding-top: 12px;">
								<div class="km_consult_nav" style="margin: 0px 24px 0px 12px; min-height: 48px; text-align: left;">
									<div>
										<span id="detailTitle" style="font-weight: bold; font-size: 16px;"></span>
									</div>
									<div id="cstinf005" style="margin-top: 6px; font-size: 14px; padding-top: 10px;">
									</div>
								</div>
							</div>
							<div clase="km_consult_content" style="text-align: right;padding: 0px 24px;">
								<span id="cstinf001" style="color: #5d7895; cursor: pointer;"></span>
								<i class="s-bar">|</i>
								<span id="toanswer" style="color: #5d7895; cursor: pointer;" onclick="toggle()">我来回答</span>
								<i class="s-bar">|</i>
								<span id="cstinf002" style="color: #5d7895; cursor: pointer;"></span>
								<i class="s-bar">|</i>
								<span id="cstinf006" style="font-size: 12px; font-weight: normal;"></span>
							</div>
						</div>
					</div>

					<div id="editorInput" class="pl-ue-content bd-form km_answer_bg" style="display: none;">
						<input id="cstans002" type="hidden" name="cstans002" value="">
						<div class="pl-ue-content layui-input-block">
							<div id="editor" type="text/plain" style="width:99%;height:200px;"></div>
						</div>
						<button id="btn-submit"  class="layui-btn layui-btn-normal" lay-submit lay-filter="consultEditForm"
								onclick="if(!checkUeditorHasText())return false;">保存</button>
						<input type="button" class="layui-btn layui-btn-primary" onclick="toggle()" value="关闭"></input>
					</div>


					<div id="anssum" style="text-align: left;">
						<div id="ans_num" class="bd-form bold km_answer_count"></div>
					</div>

					<div id="ans" class="pl-ue-content bd-form bd-notop km_answer_bg ansScroll"></div>
				</div>
			</div>
		</div>
	</div>
</body>