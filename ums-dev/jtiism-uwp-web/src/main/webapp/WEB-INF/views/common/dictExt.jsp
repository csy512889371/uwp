<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../common.inc" %>
<body>
	<div style="padding: 5px 5px;">
		<!-- 快速查找 -->
		<div style="height:35px;line-height: 35px;margin: 5px auto auto 10px;">
			快速查找：<input type="text" onkeyup="dictExtSearch(this)" id="quicksearch">
		</div>
		
		<!-- 数据列表 -->
		<div id="dictShowTab" style="float:left;width:97%;margin: auto;">
			 <ul>
			    <li><a href="#allCode">所有代码</a></li>
			    <li><a href="#searchResult">查询结果</a></li>
			 </ul>
			 <!-- 字典树 -->
			 <div id="allCode" style="overflow: scroll;">
			 	<div class="dictExtTreeDiv">
					<ul id="dictExtTree" class="ztree" style="width:90%;"></ul>
				</div>
			 </div>
			 <!-- 查询结果 -->
			 <div id="searchResult">
			 	<table id="dictExtTableList" class="mmg">
			    	<tr>
			        	<th rowspan="" colspan=""></th>
			        </tr>
			 	</table>
			 </div>
		</div>
		
		<!-- 操作按钮 -->
		<div>
			<button class="btn btn-style1" onclick="getDictExt()">确定</button>&nbsp;&nbsp;
			
			<button class="btn btn-style4" onclick="closeDictExt()">取消</button>&nbsp;&nbsp;
			<button class="btn btn-style4" onclick="cleanDictExt()">重置</button>
		</div>
	</div>
	
	<script>
		var dictExtList;
		var tableName = "${tableName}";
		var fieldName = "${fieldName}";
		var busType = "${busType}";
		$(document).ready(function(){
			
			initDictExtTable();//初始化结果表
			
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>common/dict/dictData.do',
				data : {"tableName":tableName,"fieldName":fieldName,"busType":busType},
				success:function(data){
					dictExtList = data;
					loadDictExtTree(data);
				}
			});
		});
		
		
		//初始化标签
		var $dictTabs = $("#dictShowTab").tabs({
	      collapsible: false
	    });
	    $('#dictShowTab').height(400);
	    $('#allCode').height($('#dictShowTab').height()-40);
	    $('#searchResult').height($('#dictShowTab').height()-40);
	    
	    //修改 dislog 名称
	    $('#dict_window').dialog("option","title", '${title}').dialog('open');
	    
	    /*
	    var dictCode = null,    //树形选中的代码
	    	dictName = null,    //树形选中的名称
	    	dictFullName = null,//树形选中的全称
	    */
	    var dictExtNodes = null,
	    	currentDictTabs = 0,//默认第0个标签被选中
	    	isExtChild = true,     //选中节点是否为末端子节点标志
	    	dictExtTableList;		//快速搜索结果表格
	    
	    /**
	    * 快速搜索
	    */
	    function dictExtSearch(obj){
	    	if(obj.value=='') return;
	    	$("#dictShowTab").tabs('option', 'active', 1);
	    	currentDictTabs = 1;
	    	loadDictExtTable(dictExtList, obj.value);
	    }
	    
	    /**
	    * 清除内容
	    */
	    function cleanDictExt(){
	    	$("#quicksearch").val("");
	    }
	    
	    var dictExtTree;
	    /**
	    * 加载字典树
	    * @dictList 树数据
	    */
	    function loadDictExtTree(dictList){
	    	var dictNodes = [];
		    for(var i in dictList){
		    	
		    	dictNodes.push({'id':dictList[i].code, 'pId':dictList[i].supCode, 'name':dictList[i].codeName, 
		    		'code':dictList[i].code, 'codeName':dictList[i].codeName, 'codeAbr1':dictList[i].codeAbr1,
		    		'codeAbr2':dictList[i].codeAbr2, 'supCode':dictList[i].supCode, 'codeSpelling':dictList[i].codeSpelling,
		    		'inino':dictList[i].inino, 'inino':dictList[i].inino, 'codeAName':dictList[i].codeAName,
		    		'codeLevel':dictList[i].codeLevel, 'dmGrp':dictList[i].dmGrp, 'noteType':dictList[i].noteType, 'unitCode':dictList[i].unitCode,
		    	});
		    }
		    var setting = null;
		    if('${selType}'==1){//单选
		    	setting = {
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						onClick: getDictExtValue,
						onDblClick: getExtValueToObj
					},
					view:{showIcon:false}
				};
		    }else{//多选
		    	setting = {
					data: {
						simpleData: {
							enable: true
						}
					},
					check: {
						enable: true,
						chkStyle: "checkbox",
						nocheckInherit: true,
						chkboxType: {"Y": "", "N": ""}
					},
					callback: {
						onClick: getDictExtValue,
						onCheck: getDictExtValue1
					},
					view:{showIcon:false}
				};
		    }
			dictExtTree = $.fn.zTree.init($("#dictExtTree"), setting, dictNodes);
	    }
	    
		/**
		* 获得选中的字典值
		*/
		function getDictExt(){
			if($dictTabs.tabs('option', 'active')==0){   //从“所有代码”中获取数据
				if(!isExtChild){
					openAlert('这一级代码不能选择！');
					return;
				}
				
				if(dictExtNodes!=null && typeof(dictExtNodes)!='undefined') {
					if(dictExtNodes.length==0) {
						openAlert('请选择一条记录！');
						return;
					}
					//调用回调方法
					if(dictCallbacks!=null && typeof(dictCallbacks)!='undefined'){
						dictCallbacks.fireWith(window, new Array(dictExtNodes));
						//清空回调
						dictCallbacks.empty();
					}
				} else {
					openAlert('请选择一条记录！');
					return;
				}
			} else {	//从“查询结果”中获取数据
				var rows = dictExtTableList.selectedRows();
				var item = rows[0];
				if(typeof(item)=='undefined'){
					openAlert('请选择一条记录！');
					return;
				}
				//调用回调方法
				if(dictCallbacks!=null && typeof(dictCallbacks)!='undefined'){
					dictCallbacks.fireWith(window, new Array(rows));
					//清空回调
					dictCallbacks.empty();
				}
			}
			
			closeDictExt();
		}
		
		/**
		* 取消选择
		*/
		function closeDictExt(){
			$("#dict_window").dialog('close');
		}
		
		/**
		* 单击时获取字典值
		*/
		function getDictExtValue(event, treeId, treeNode){
			//只能选择子节点，父节点不能选择
			if(typeof(treeNode.children)!='undefined'){
				isExtChild = false;
			}else{
				isExtChild = true;
			}
			// 是否勾选
	    	if('${selType}'==2){
	    		if (treeNode.checked == true) {
		    		dictExtTree.checkNode(treeNode, false, true);
		    	} else {
		    		dictExtTree.checkNode(treeNode, true, true);
		    	}
	    	}
       		if('${selType}'==1){	//单选
       			dictExtNodes = new Array(); 
       			dictExtNodes.push(treeNode);
       		}else{	//多选
       			dictExtNodes = dictExtTree.getCheckedNodes(true);
       		}
		}
		
		/**
		* 勾选时获取字典值
		*/
		function getDictExtValue1(event, treeId, treeNode){
			dictExtNodes = dictExtTree.getCheckedNodes(true);
		}
		
		/**
		* 双击时值直接带入页面
		*/
		function getExtValueToObj(event, treeId, treeNode){
			//只能选择子节点，父节点不能选择
			if(typeof(treeNode.children)!='undefined'){
				isExtChild = false;
			}else{
				isExtChild = true;
			}
			dictExtNodes = new Array(); 
   			dictExtNodes.push(treeNode);
			getDictExt();
		}
		
		/**
		* 初始化表格信息
		*/
		function initDictExtTable(){
			var multiSelectFlag = false;
			//if('${selType}'==2) multiSelectFlag = true;
			var dictCols = [
			                <c:choose>
			                	<c:when test="${codeTable eq 'ZB02' || codeTable eq 'zb02'}">
			                		{ title:'代码', name:'code' ,width:90, align:'left', sortable: false, type: 'text', hidden:true},
			                		{ title:'单位代码', name:'unitCode' ,width:230, align:'left', sortable: false, type: 'text'},
			                	</c:when>
			                	<c:otherwise>
			                		{ title:'代码', name:'code' ,width:90, align:'left', sortable: false, type: 'text'},
			                	</c:otherwise>
			                </c:choose>
						    { title:'名称', name:'codeName' ,width:230, align:'left', sortable: false, type: 'text'},
						    { title:'名称简写二', name:'codeAbr2' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'名称简写一', name:'codeAbr1' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'上级代码', name:'supCode' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'首字母拼音', name:'codeSpelling' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'排序号', name:'inino' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'别名', name:'codeAName' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'所在级别', name:'codeLevel' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'指标表', name:'dmGrp' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'节点类型', name:'noteType' ,width:230, align:'left', sortable: false, type: 'text',hidden:true}
						   ];
			dictExtTableList = $('#dictExtTableList').mmGrid({
		             height: 360
		           , cols: dictCols
		           , items : []
		           , remoteSort:true
		           , sortStatus: 'asc'
		           , multiSelect: multiSelectFlag
		           , checkCol: true
		           , autoLoad: false
		           , showBackboard : false
		           , plugins: [
		           ]
		           , params: function(){
		               //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
		           }
		       });
		}
	       
		/**
		* 加载查询结果
		* @dictList 所有结果
		* @key 	    搜索关键字
		*/		
		function loadDictExtTable(dictList, key){
			
			var dictItems = [];
			for(var i in dictList){
				var noteType = dictList[i].noteType;
				if ("library" == noteType || "unitGroup" == noteType) {
					continue;
				}
				if(dictList[i].code.indexOf(key)>-1 || dictList[i].codeName.indexOf(key)>-1 || dictList[i].codeSpelling.indexOf(key.toLowerCase())>-1 || dictList[i].codeSpelling.indexOf(key.toUpperCase())>-1){
					dictItems.push(dictList[i]);
				}
			}
			dictExtTableList.load(dictItems);
		}
		
	</script>
</body>