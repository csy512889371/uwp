<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>

<style>
	#codeInput{width:100px;height:18px;}
	.oper_menu{font-size:12px;}
	#coderTreeDiv{border: none;}
	.subSys{margin: 0px 0px 5px 0px;border-bottom: 1px solid #ddd;padding: 0px 0px 5px 5px;height:30px;line-height: 30px;}
	#switchBar{height:100%;cursor: pointer;float: left;width:13px;background-color: #fff;border-right: 1px solid #b8c0cc;}
    #switchBar:hover{background-color: #f7f7f7;}
    #switchHref{top: 50%;position: absolute;}
    #unitTreeDiv{width:270px;float: left;border-right: 1px solid #b8c0cc;}
</style>
<body>
	<div>
		<!-- 库及机构树 -->
		<div id="unitTreeDiv">
			<!-- 库 -->
			<div class="oper_menu subSys" id="chooseLibraryPage">
				<span style="font-size:14px;">组织机构：</span>
			</div>
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
		<div style="margin-left: 284px;position: relative;" id="rightDiv">
			<!-- 菜单 -->
			<div class="oper_menu" style="border-left:1px solid #b8c0cc;">
				<auth:auth ifAllGranted="unit:add">
					<button class="btn-style4" onclick="addUnit()"><i class="icon-plus"></i>新增单位</button>
				</auth:auth>
				<auth:auth ifAllGranted="unit:edit">
					<button class="btn-style4" onclick="updateUnit()"><i class="icon-pencil"></i>编辑</button>
				</auth:auth>
				<auth:auth ifAllGranted="unit:delete">
					<button class="btn-style4" onclick="deleteUnit()"><i class="icon-trash"></i> 删除</button>
				</auth:auth>
			</div>
			<div id="unitInfoDiv2" style="/* overflow-y: scroll; overflow-x: auto; */ display:none;"></div>
		</div>
	</div>
	
	<script>
		/* $( "#coderTreeDiv" ).tabs({
	      		collapsible: false
	    }); */
	    
	    var showLeft = true;
	    
		$('#unitTreeTab').height($(window).height()-155);
	    $('#switchBar').height($(window).height()-115);
	    
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
				$('#rightDiv').css('margin-left','284px');
				$('#switchHref').removeClass('icon-chevron-right');
				$('#switchHref').addClass('icon-chevron-left');
			}
		}
		
		/**新增单位**/
		function addUnit(){
			var id="";
			var selType = "noselect";
			if(selectedNode!=null){
				id = selectedNode.id;
				selType = selectedNode.type;
			}
			openWindow('添加单位','unit/unit/addUnit.do?id=' + id + "&selType=" + selType, 1, 800, 300);
		}
		
		
		/**编辑单位信息**/
		function updateUnit(){
			if(selectedNode==null){
				openAlert("请选择一条单位或分组信息！");
				return;
			}
			openWindow('编辑单位信息','unit/unit/editUnit.do?b01HiberId='+selectedNode.id,1,920,300,null,function(){
				
			});
		}
		
		
		/**删除**/
		function deleteUnit(){
			var info = '';
			if(selectedNode == null){
				openAlert("请选择一条单位信息!");
				return;
			}
			if(selectedNode.isParent){
				openAlert("该机构下面有单位！");
				return;
			} 
			
			openConfirm('确定要删除【'+selectedNode.name+'】这条记录吗？',function(){
				$.ajax({
					type:'post',
					url:'<%=basePath%>unit/unit/deleteUnit.do',
					data:{b01HiberId:selectedNode.id, unitId:selectedNode.unitId},
					success:function(result){
						openAlert(result.content);
						if(result.type=="success"){
							//删除节点
							 var treeObj = $.fn.zTree.getZTreeObj("coderTree");							
								treeObj.removeNode(selectedNode);
								//删除后选择根目录
								var sNodes = treeObj.getNodes();
       						if (sNodes.length>0) {
       							sNodes[0].type ='unitGroup';
       							type = sNodes[0].type;
       							treeObj.selectNode(sNodes[0]);  
       							selectedNode = sNodes[0];
       							$("#unitInfoDiv2").hide();
       						}
						}
					}
				});
			});
		}
		
		$(document).ready(function(){	
			loadCode();
		});
		
		/**加载单位信息**/
		function loadCode(){		
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>unit/unit/getUnitList.do',
				data : {},
				async:false,
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
		    		'unitId':data[i].unitId, 'unitName':data[i].unitName
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
			selectedNode = treeNode;
			type = selectedNode.type;
			if(type=="unit"||type=="vunit"||type=="subGroup"){
				$('#unitInfoDiv2').load('<%=basePath%>unit/unit/unitInfoPage.do',{'b01HiberId':selectedNode.id});
				$("#unitInfoDiv2").show();
			}
			if(type=="unitGroup"){
				$("#unitInfoDiv2").hide();
				$("#unitInfoDiv2").html('');
			}
		}
		
		function updateTreeNode(data){
			var treeObj = $.fn.zTree.getZTreeObj("coderTree");	
			selectNewChlidren(treeObj,data.id);
		}
		//在选择下新增节点
		function addTreeNode(data){
			var treeObj = $.fn.zTree.getZTreeObj("coderTree");	
			var treeNode =  treeObj.getNodeByParam('id',data.pid,null);
			if(treeNode==null){
				return;
			}
			var newNode={'id':data.id, 'pId':data.pid, 'name':data.name, 'fullName':data.fullName,'type':data.type,
					'unitId':data.unitId, 'unitName':data.unitName
			};
			treeObj.addNodes(treeNode, newNode);
			selectNewChlidren(treeObj,data.id);
		 }
		function selectNewChlidren(treeObj,nodeId){
			var treeNode =  treeObj.getNodeByParam('id',nodeId,null);
			if (treeNode != null) {
				treeObj.selectNode(treeNode);
				selectedNode = treeNode;
				type = treeNode.type;
			}
		 }
		
		 function checkBoxChange(obj,vobj) {
				var item= $('#'+vobj);
				if(obj.checked==true){
					item.val(true);
				}else{
					item.val(false);
				}
				
			}
	</script>
</body>