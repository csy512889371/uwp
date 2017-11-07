<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<%-- 单位机构 列表页面 --%>
<style>
	#switchBar{height:100%;cursor: pointer;float: left;width:13px;background-color: #fff;}
    #switchBar:hover{background-color: #f7f7f7;}
    #switchHref{top: 50%;position: absolute;}
    #basicTreeDiv{width:190px;float: left;overflow: auto;border-right: 1px solid #b8c0cc;}
    #cadreAttrList{margin-top:0px;margin-left:203px}
</style>
<body>
	<div id="searchItems">
		<div id="basicTreeDiv">
			<ul id="basicTree" class="ztree ztree1"></ul>
		</div>
		<!-- 切换显示隐藏的bar -->
		<div id="switchBar" onclick="switchShow()">
			<i id="switchHref" class="icon-chevron-left"></i>&nbsp;&nbsp;&nbsp;
		</div>
		<div id="cadreAttrList">
			<!-- 基本信息项 -->
		</div>
 		<div id="validate_rule_page" title="选择"><div id="validate_rule_page_content"></div></div>
	</div>

	<script type="text/javascript">
		var showLeft = true;
		
		$('#searchItems').height($(window).height() - 170);
		$('#cadreAttrList').height($(window).height() - 335);
		$('#basicTreeDiv').height($(window).height() - 120);
		$('#switchBar').height($(window).height()-115);
		
		/**
		* 窗口自适应
		*/
		function adjustWindow(){
			$('#searchItems').height($(window).height() - 170);
			$('#cadreAttrList').height($(window).height() - 335);
			$('#basicTreeDiv').height($(window).height() - 120);
			if($('#basicInfoField')!=null) $('#basicInfoField').height($(window).height()-200);
			$('#switchBar').height($(window).height()-115);
		}
		
		function switchShow(){
			if(showLeft){
				showLeft = false;
				$('#basicTreeDiv').hide();
				$('#cadreAttrList').css('margin-left','13px');
				$('#switchHref').removeClass('icon-chevron-left');
				$('#switchHref').addClass('icon-chevron-right');
			}else{
				showLeft = true;
				$('#basicTreeDiv').show();
				$('#cadreAttrList').css('margin-left','203px');
				$('#switchHref').removeClass('icon-chevron-right');
				$('#switchHref').addClass('icon-chevron-left');
			}
		}
		
		var codeAttrs=JSON.parse('${CODEATTRS}');
		var cadreAttrSetting = {
				view: {
					showLine: false,
					showIcon: false,
					selectedMulti: false,
					dblClickExpand: false
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					beforeClick: beforeClick,
					onClick : changeInfo
				}
			};
			/**以下数据需要配置在表中**/
			var basicNodes =[
			];
			for(var i in codeAttrs){
				if(codeAttrs[i].parentId=="0"){
					continue;
				}else if(codeAttrs[i].parentId=="241"){
					basicNodes.push({id:codeAttrs[i].id,pId:codeAttrs[i].parentId,name:codeAttrs[i].attrName,infoSet:"",open:true});
				}else{
					basicNodes.push({id:codeAttrs[i].id,pId:codeAttrs[i].parentId,name:codeAttrs[i].attrName,infoSet:codeAttrs[i].attrCode});
				}
			}
			function beforeClick(treeId, treeNode) {
				if (treeNode.level == 0 ) {
					var zTree = $.fn.zTree.getZTreeObj("basicTree");
					zTree.expandNode(treeNode);
					return false;
				}
				return true;
			}
			function changeInfo(event, treeId, treeNode){
				$('#cadreAttrList').load('<%=basePath%>sys/structure/getCadreAttrList.do',{attrId:treeNode.id,infoSet:treeNode.infoSet});
			}
		$('document').ready(function() {
				var treeObj = $("#basicTree");
				$.fn.zTree.init(treeObj, cadreAttrSetting, basicNodes);
				zTree_Menu = $.fn.zTree.getZTreeObj("basicTree");
			});
			function selectValidateRules(vrItem){
				$('#validate_rule_page_content').load('<%=basePath%>sys/structure/selectValidateRules.do',{'inputId':vrItem.id});
				$("#validate_rule_page").dialog({
			        resizable: false,
			        draggable: true,
			        height: 500,
			        width: 400,
			        modal: true,
			        title: '选择验证',
			        buttons: {
			        	"确定": function () {
			        		var selectRows=validateRuleList.selectedRows();
			        		var vrDatas='';
			        		for(var i in selectRows){
			        			vrDatas+=selectRows[i].validateCode+",";
			        		}
			        		if(vrDatas.length>0){
			        			vrDatas=vrDatas.substring(0, vrDatas.length-1);
			        		}
			        		$('#'+vrItem.id).val(vrDatas);
			        		$(this).dialog("close");
			        	}
			        }
			    });
			}
		
	</script>
</body>
