<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>

<style>
	#codeInput{width:100px;height:18px;}
	.oper_menu{padding-left: 5px;font-size:12px;}
	#coderTreeDiv{border: none;}
	.subSys{margin-bottom: 5px;border-bottom: 1px solid #ddd;padding-bottom: 5px;margin-left: 0px;margin-right: 0px;padding-top:0px;height:30px;line-height: 30px;}
	#switchBar{height:100%;cursor: pointer;float: left;width:13px;background-color: #fff;}
    #switchBar:hover{background-color: #f7f7f7;}
    #switchHref{top: 50%;position: absolute;}
    #unitTreeDiv{width:270px;float: left;border-right: 1px solid #b8c0cc;}
</style>
<body>
	<div>
		<!-- 库及机构树 -->
		<div id="unitTreeDiv">
			<!-- 机构树 -->
		    <div id="coderTreeDiv">
		    	<!-- <ul>
				  <li><a href="#unitTreeTab">机构树</a></li>
			    </ul> -->
				<div id="unitTreeTab" style="overflow: auto;">
					<ul id="coderTree" class="ztree" style="width:95%;"></ul>
				</div>
			</div>
		</div>
		
		<!-- 切换显示隐藏的bar -->
		<div id="switchBar" onclick="switchShow()">
			<i id="switchHref" class="icon-chevron-left"></i>&nbsp;&nbsp;&nbsp;
		</div>
		
		<!-- 菜单及单位列表 -->
		<div style="margin-left: 283px;position: relative;" id="rightDiv">
			<div id="unitInfoDiv2" style="overflow-y: auto; overflow-x: auto; display:none;"></div>
		</div>
	</div>
	
	<script>
		/* $( "#coderTreeDiv" ).tabs({
				collapsible: false
		}); */
		$('#unitTreeTab').height($(window).height()-155);
		$('#switchBar').height($(window).height()-115);
		var showLeft = true;
		
		/**
		* 窗口自适应
		*/
		function adjustWindow(){
			$('#unitTreeTab').height($(window).height()-155);
			$('#switchBar').height($(window).height()-115);
		}
		
		function switchShow(){
			if(showLeft){
				showLeft = false;
				$('#unitTreeDiv').hide();
				$('#rightDiv').css('margin-left','13px');
				$('#switchHref').removeClass('icon-chevron-left');
				$('#switchHref').addClass('icon-chevron-right');
			}else{
				showLeft = true;
				$('#unitTreeDiv').show();
				$('#rightDiv').css('margin-left','283px');
				$('#switchHref').removeClass('icon-chevron-right');
				$('#switchHref').addClass('icon-chevron-left');
			}
		}
		
		$(document).ready(function(){
			
			var library='${codeList}';
			if(library!=null){
				//默认展示第一个库的树信息
				var libId = $("#indexLibraryId option:first").val();
				loadCode(libId);
			}
			
			$("#indexLibraryId").change(function(){
				var libId = $("#indexLibraryId").val();
				$('#unitInfoDiv2').load('<%=basePath%>unit/unit/unitSortInfoPage.do',{'unitId':libId});
				$("#unitInfoDiv2").show();
				loadCode(libId);
			});
			$('#unitInfoDiv2').load('<%=basePath%>unit/unit/unitSortInfoPage.do',{'unitId':libId});
			$("#unitInfoDiv2").show();
		});
		
		/**加载单位信息**/
		function loadCode(libId){
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>unit/unit/getUnVulUnitList.do',
				data : {libraryId : libId},
				success: function(data){
					loadCoderTree(data);
				}
			});
		}
		
		/**树形展示**/
		function loadCoderTree(data){
			var dictNodes = [];
		    for(var i in data){
		    	dictNodes.push({'id':data[i].id, 'pId':data[i].pid, 'name':data[i].name, 'fullName':data[i].fullName,'type':data[i].type,
		    		'groupId':data[i].groupId, 	'groupName':data[i].groupName, 'unitId':data[i].unitId, 'unitName':data[i].unitName,
		    		'libraryId':data[i].libraryId, 'libraryName':data[i].libraryName, 'dmcod':data[i].dmcod, 'punitLev':data[i].punitLev
		    	});
		    }
			var coderTreeSetting = {
	    		check: {
	    			enable: false,
	    			nocheckInherit: true
	    		},
	    		data: {
	    			simpleData: {
	    				enable: true
	    			}
	    		},
	    		callback : {
					onClick : zTreeOnclick
				}
	    	};
			$.fn.zTree.init($("#coderTree"), coderTreeSetting, dictNodes);
		}
		
		/**点击节点事件**/
		var type=null;
		var selectedNode=null;
		
		function zTreeOnclick(event, treeId, treeNode){
			//selectedNode = treeNode;
			//alert(selectedNode.id);
			//console.log(treeNode);
			type = treeNode.type;
			//if(type=="unitGroup"){
				$('#unitInfoDiv2').load('<%=basePath%>unit/unit/unitSortInfoPage.do',{'type':treeNode.type,'unitHiberId':treeNode.id,'unitId':treeNode.unitId,'libraryId':treeNode.id,'dmcod':treeNode.dmcod,'libId':$("#indexLibraryId").val()});
				$("#unitInfoDiv2").show();
			//}else{
			//	openAlert('只能选择归口进行排序!');
			//	return;
			//}
		}
	</script>
</body>