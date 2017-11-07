<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
<style>
	#codeInput{width:140px;}
	.oper_menu{padding-left: 5px;font-size:12px;}
	#coderTreeDiv{border: 1px solid #ccc;overflow: scroll;}
</style>
<body>
	<div>	
		<!-- 指标列表 -->
		<div style="width:350px;float: left;" id="leftPage">
			<div class="oper_menu" style="margin-top: 0px;">
				<auth:auth ifAllGranted="codeUpt:edit">
					<button class="btn-style4"  onclick="updateCodeAttrbute()" style="margin-left:12px"><i class="icon-pencil"></i>编辑指标</button>
				</auth:auth>
				<auth:auth ifAllGranted="codeUpt:add">
					<button class="btn-style4"  onclick="addCodeAttrbute()" style="margin-left:12px"><i class="icon-plus"></i>新增指标</button>
				</auth:auth><br/>
				<!-- <button class="btn" onclick="addCodeAttrbute()"><i class="icon-plus"></i>新增</button> -->
				 <span style="font-size: 14px;">代码或名称：</span>
				<input type="text" id="codeInput">
				<auth:auth ifAllGranted="codeUpt:search">
					<button class="btn-style1" onclick="searchCode()">查询</button>
				</auth:auth>

			</div>
			<table id="codeList" class="mmg">
		        <tr>
		           <th rowspan="" colspan=""></th>
		        </tr>
		    </table>
		    </div>
		<!-- 指标项树 -->
		<div style="margin-left: 350px;position: relative;">
			<div class="oper_menu" style="padding-left: 0px;">
				<auth:auth ifAllGranted="codeUpt:add">
					<button class="btn-style4" onclick="addCode()"><i class="icon-plus"></i>新增指标项</button>
				</auth:auth>
				<auth:auth ifAllGranted="codeUpt:addSup">
					<button class="btn-style4" onclick="addNextCode()"><i class="icon-plus"></i>新增下级指标项</button>
				</auth:auth>
				<auth:auth ifAllGranted="codeUpt:editSup">
					<button class="btn-style4" onclick="updateCode()"><i class="icon-pencil"></i>编辑指标项</button>
				</auth:auth>
				<auth:auth ifAllGranted="codeUpt:comSup">
					<button class="btn-style4" onclick="commonCodeSettings()"><i class="icon-cog"></i>设置常用指标项</button>
				</auth:auth>
				<auth:auth ifAllGranted="codeUpt:delete">
					<button class="btn-style4" onclick="deleteCode()"><i class="icon-trash"></i>删除指标项</button>
				</auth:auth>
			</div>
			<div style="text-align: left;color: red;height: 20px;">蓝字的指标项为常用指标项</div>
			<div id="coderTreeDiv">
				<ul id="coderTree" class="ztree" style="width:100%;"></ul>
			</div>
		</div>
	</div>
	
	<script>
		$('#coderTreeDiv').height(commonTalbeHeight-10);
		
		/**
		* 窗口自适应
		*/
		function adjustWindow(){
			$('#coderTreeDiv').height($(window).height()-170);
			codeList.resize($(window).height()-170);
		}
	
		//是否选中列表数据
		function checkSelected(){
			var rows=codeList.selectedRows();
			var item=rows[0];
			if(typeof(item)=='undefined'){
				return null;
			}
			return item;
		}
		
		var selName = '',selCode = '', selId = '';
		//编辑：左边代码
		function updateCodeAttrbute(){			
			var item=checkSelected();			
			if(item==null){
				openAlert('请选择下面一行记录！');
				return; 
			}
			selName = tName;
			selCode = tCode;
			selId = item.id;
			openWindow('编辑指标','sys/code/updatePage.do',1,350,180,{attrCode:tCode,attrName:tName,id:item.id});
		}

        //新增：左边代码
        function addCodeAttrbute(){
            openWindow('新增指标','sys/code/addPage.do',1,350,180);
        }
		
		//新增：右边指标代码
		function addCode(){
			var item=checkSelected();			
			if(item==null){
				openAlert('请选择左边的一条记录！');
				return; 
			}
			add(0);					
		}
		
		//新增下级
		function addNextCode(){
			/* if(selectedNode==null){
				openAlert('请选择一个节点！');
				return;
			} */
			var item=checkSelected();			
			if(item==null){
				openAlert('请选择左边的一条记录！');
				return; 
			}
			var treeObj = $.fn.zTree.getZTreeObj("coderTree");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes.length != 1){
				openAlert('请选择一条指标项！');
				return;
			}
			add(1);			
		}
		
		//新增和新增下级判断
		function add(type){
			var treeObj = $.fn.zTree.getZTreeObj("coderTree");	
			var sNodes = treeObj.getSelectedNodes();
			var id="";
			var codeName="";
			var height = 310;
			if(type==0) height = 280;
			if(sNodes.length>0){
				if(type==1){
					id=sNodes[0].id;
					codeName=sNodes[0].codeName;				
				}else{
					id=null;
					codeName=null;
				}
			}
			openWindow('新增指标项','sys/code/codeAdd.do',1,400,height,{tCode:tCode,parentId:id,parentCodeName:codeName,type:type});
		}
		
		//编辑：右边指标代码
		function updateCode(){	
			/* if(selectedNode==null){
				openAlert('请选择一条代码！');
				return;
			} */
			var item=checkSelected();			
			if(item==null){
				openAlert('请选择左边的一条记录！');
				return; 
			}
			var treeObj = $.fn.zTree.getZTreeObj("coderTree");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes.length != 1){
				openAlert('请选择一条指标项！');
				return;
			}
			openWindow('编辑指标项','sys/code/codeUpdatePage.do',1,450,270,{itemCode:selectedNode.id,tCode:tCode});
		}
		
		//常用指标代码设置
		function commonCodeSettings(){
			var item=checkSelected();			
			if(item==null){
				openAlert('请选择左边的一条记录！');
				return; 
			}
			var treeObj = $.fn.zTree.getZTreeObj("coderTree");
			var selectNodes = treeObj.getCheckedNodes(true);
			var selectCodes = [];
			for(var i in selectNodes){
				selectCodes.push(selectNodes[i].id);
			}
			if(selectCodes.length > 0){
				selectCodes = selectCodes.join(",");
			}
			if(selectCodes.length == 0){
				openAlert('请选择指标项！');
				return;
			}
			openWindow('设置常用指标项','sys/code/commonCodeSettingsPage.do',1,400,165,{selectCodes : selectCodes});
		}
		
		//删除	
		var children=0;
		function deleteCode(){
			
			if(selectedNode==null){
				openAlert('请选择右边一条代码');
				return;
			}
			var treeObj = $.fn.zTree.getZTreeObj("coderTree");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes.length != 1){
				openAlert('请选择一条指标项！');
				return;
			}
			if(selectedNode.isParent){				
				children=1;	
			} 
			 openConfirm('确定删除该代码吗？',function(){				 
				$.ajax({
					type:'post',
					url:'<%=basePath%>sys/code/deleteCode.do',
					data:{codeTable:tCode,id:selectedNode.id,children:children},
					success:function(result){
						if(result.type=="success"){
							//删除节点
							 var treeObj = $.fn.zTree.getZTreeObj("coderTree");							
								treeObj.removeNode(selectedNode);
								//删除后选择根目录
								var sNodes = treeObj.getNodes();
        						if (sNodes.length>0) {
        							treeObj.selectNode(sNodes[0]);        							
        						}
							 
						}
					}
				
				});
			});	
		}
		
		//查询
		 function searchCode(){			
			var cond = $('#codeInput').val();
			var rows = items;
			if(cond==''){
				codeList.load('${codeList}');				
			}else{
//alert("222");
				var newRows = [];
				for(var i in rows){
					if(rows[i].attrCode.indexOf(cond.toUpperCase())>-1 || rows[i].attrName.indexOf(cond)>-1){
						newRows.push(rows[i]);
//alert(newRows);问题
					}
				}				
				codeList.load(newRows);
			}
		}
	
		var cols = [		            		    
				    { title:'id', name:'id' ,width:50, align:'left', hidden:true,sortable: true, type: 'text'},				
				    { title:'代码', name:'attrCode' ,width:80, align:'left', sortable: true, type: 'text'},
				    { title:'名称', name:'attrName' ,width:340, align:'left', sortable: true, type: 'text'},				    			   
				   ];		
		
		$(document).ready(function(){
			loadCodeList();
		});
		
		var itemId;
		var codeList;
		var isFrist = true;
		var items = [];
		function loadCodeList(){			
			codeList = $('#codeList').mmGrid({
		           height: commonTalbeHeight+10
		           , cols: cols
		           , url: '<%=basePath%>sys/code/getCodeAttributeList.do'		           
		           , method: 'get'
		           , remoteSort:true
		           , sortName: 'SCUCODE'
		           , sortStatus: 'asc'
		           , root: 'resultMapList'//'result'
		           , multiSelect: false
		           , checkCol: true
		           , fullWidthRows: true
		           , autoLoad: false
		           , showBackboard : false
		           , plugins: [
		               //$('#pg').mmPaginator({})
		           ]
		           , params: function(){
		               //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
		             return {
		                 secucode: $('#secucode').val()
		             };
		           }
		       });
		
		
		       codeList.on('cellSelected', function(e, item, rowIndex, colIndex){
		    	   tCode=item.attrCode;
		    	   tName=item.attrName;		    	   
		    	   //加载树
		       	   loadCode(tCode);
		           //查看
		           if($(e.target).is('.btn-info, .btnPrice')){
		               e.stopPropagation();  //阻止事件冒泡
		               alert(JSON.stringify(item));
		           }
		       }).load({page: 1});
		       
		       codeList.on('loadSuccess',function(e,date){
   	   				if (isFrist) {
   	   					var rowss = codeList.rows();
   	   				
   	   					for(var i in rowss){
   	   						items.push({attrCode:rowss[i].attrCode,attrName:rowss[i].attrName});
   	   					}   	
   	   				
   	   					isFrist = false;
   	   				}
   	   			});
		}
		
		//选择一个节点
		var selectedNode = null;
		function zTreeOnClick(event,treeId,treeNode){			
			selectedNode = treeNode;
			coderTree.checkAllNodes(false);//取消勾选其他
			coderTree.checkNode(treeNode, true, false);
			coderTree.refresh();
			coderTree.selectNode(treeNode, true);
		}
		
		//获得代码数据，并使用树的形式展示
		var codeTree;
		function loadCode(tCode){			
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>sys/code/getCodeList.do',
				data : {tName : tCode},
				success: function(data){
					codeTree = data;
					loadCoderTree(data);
				}
			});
		}
		//加载当前代码树的数据
		var coderTree;
		function loadCoderTree(data){
			var dictNodes = [];
		    for(var i in data){
		    	dictNodes.push({
		    		'id':data[i].code, 
		    		'pId':data[i].supCode, 
		    		'name':data[i].code+'  '+data[i].codeName,
		    		'fullName':data[i].codeAbr2,
		    		'codeName':data[i].codeName,
		    		'isCommon':data[i].isCommon,
		    		'isStand':data[i].isStand
		    		});
		    }
			var coderTreeSetting = {
	    		check: {
	    			enable: true,
	    			nocheckInherit: true,	    			
	    			chkboxType: { "Y": "s", "N": "s" }
	    		},
	    		data: {
	    			simpleData: {
	    				enable: true
	    			}
	    		},
	    		callback: {
	    			onClick: zTreeOnClick
	    		},view: {
	    			fontCss: setFontCss
	    		}
			};
			coderTree = $.fn.zTree.init($("#coderTree"), coderTreeSetting, dictNodes);			
		}
		
		function setFontCss(treeId, treeNode) {
			return treeNode.isCommon == 1 ? {color:"blue"} : {};
		};
	</script>
</body>