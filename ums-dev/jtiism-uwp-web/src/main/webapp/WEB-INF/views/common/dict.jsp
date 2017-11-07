<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../common.inc" %>
<%
	String pt = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
 %>
<body>
	<div style="padding: 5px 5px;">
		<!-- 快速查找 -->
		<div style="height:35px;line-height: 35px;margin: 5px auto auto 10px;margin-right:10px;">
			快速查找：<input id="fastSearch" type="text" onkeyup="dictSearch(this)">
		</div>
		
		<!-- 数据列表 -->
		<div id="dictShowTab" style="float:left;width:100%;margin: auto;">
			 <ul>
			 	<li id="commonCodeLi"><a href="#commonCode">常用代码</a></li>
			    <li><a href="#allCode">所有代码</a></li>
			    <li><a href="#searchResult">查询结果</a></li>
			 </ul>
		 	 <!-- 常用代码字典树 -->
			 <div id="commonCode" style="overflow: auto;border: none;">
			 	<div class="commonDictTreeDiv">
					 <ul id="commonDictTree" class="ztree" style="width:90%;"></ul>
				</div>
			 </div>
			 <!-- 所有代码字典树 -->
			 <div id="allCode" style="overflow: auto;border: none;">
			 	<div class="dictTreeDiv">
					<ul id="dictTree" class="ztree" style="width:90%;"></ul>
				</div>
			 </div>
			 <!-- 查询结果 -->
			 <div id="searchResult" style="border: none;">
			 	<table id="dictTableList" class="mmg">
			    	<tr>
			        	<th rowspan="" colspan=""></th>
			        </tr>
			 	</table>
			 </div>
		</div>
		<div style="margin-top: 415px;height:40px;">
			<button class="btn-style1" onclick="getDict()">确定</button>&nbsp;
			<button class="btn-style4" onclick="closeDict()">取消</button>&nbsp;
			<button class="btn-style4" onclick="cleanDict()">重置</button>
		</div>
	</div>
	
	<script>
		var dictListCache;
		var commonDictListCache;
		var tableName = "${tableName}";
		var fieldName = "${fieldName}";
		var busType = "${busType}";
		var checkedIds = "${checkedIds}";//选中Id
		var checkIdArr;
		//初始化标签
		var $dictTabs = $("#dictShowTab").tabs({
	      collapsible: false
	    });
		$(document).ready(function(){
			checkIdArr = checkedIds.split(",");
			//初始化结果表
			initDictTable();
			var cacheKey = tableName + fieldName;
	   		var treeData = $.data(cacheDict, cacheKey);
	   		var commonTreeData = [];
	   		if(cacheKey != 'A11a1131'){
	   			//判断是否有缓存
		   		if(typeof(treeData)=='undefined' || treeData == ''){
		   			treeData = [];
		   			commonTreeData = [];
		   			getDictData(tableName,fieldName);
		   			treeData = $.data(cacheDict, cacheKey);
		   		} 
		   		
		   		for (var i = 0; i < treeData.length; i++) {
		   			if (treeData[i].isCommon == 1) {
		   				commonTreeData.push(treeData[i]);
		   			}
		   		}
			} else {
				$.ajax({
					type : 'post',
					async: false,
					url  : '<%=pt%>common/dict/dictData.do',
					data : {"tableName":tableName,"fieldName":fieldName},
					success:function(data){
						treeData = data;
					}
				});
			}
	   		
	   		if(commonTreeData.length == 0){
				$("#commonCodeLi").hide();
				$("#commonCode").hide();
				$dictTabs.tabs('option', 'active',1);
			}
	   		
	   		dictListCache = treeData;
			loadDictTree(treeData);//加载所有代码数据
			if(commonTreeData.length > 0){
				commonDictListCache = commonTreeData;
				loadCommonDictTree(commonTreeData);//加载常用代码数
			}
		});
	
	
		
	    $('#dictShowTab').height(400);
	    $('#commonCode').height($('#dictShowTab').height()-40);
	    $('#allCode').height($('#dictShowTab').height()-40);
	    $('#searchResult').height($('#dictShowTab').height()-40);
	    
	    //修改 dislog 名称
	    $('#dict_window').dialog("option","title",'${title}').dialog('open');
	    
	    
	    var dictCode = null,    //树形选中的代码
	    	dictName = null,    //树形选中的名称
	    	dictFullName = null,//树形选中的全称
	    	dictCodeName = null,//codeName
	    	currentDictTabs = 0,//默认第0个标签被选中
	    	isChild = true,     //选中节点是否为末端子节点标志
	    	dictTableList;		//快速搜索结果表格
	    	noteType = null;    //节点类型
	    	

	    var timer = null;
	    /**
	    * 快速搜索
	    */
	    function dictSearch(obj){
	    	if(obj.value=='') return;
	    	$("#dictShowTab").tabs('option', 'active', 2);
	    	currentDictTabs = 1;
	    	clearInterval(timer);
	    	timer = setInterval(function(){
	    		loadDictTable(dictListCache, obj.value);	
	    	}, 800);
	    	
	    }
	    
	    /**
	    * 清除内容
	    */
	    function cleanDict(){
	    	var dictCode = $('#'+curDictCode).val();;
	    	if(typeof(dictCode)!='undefined' && dictCode != null && dictCode != '' && typeof(changeFlag)!='undefined' && changeFlag != null){//信息集录入页面修改标识
				changeFlag = true;
			}
	    	$('#'+curDictFull).val('');
	    	$('#'+curDictCode).val('');
	    	$('#'+curDictName).val('');
	    	
	    	if(curDictCodeName!=null && typeof(curDictCodeName)!='undefined') {
	    		$('#'+curDictCodeName).val('');
			}
	    	
	    }
	    
	    var dictTree;
	    
	    function buildCommonDictNodes(commonDictNodes, dictData) {
	    	if (null != dictData.unitHiberId && '' != dictData.unitHiberId) {//ZB02
		    	if($.inArray(dictData.code, checkIdArr) != -1) {//包含选中ID
		    		commonDictNodes.push({'code':dictData.code,'codeName':dictData.codeName,'id':dictData.unitHiberId, 'pId':dictData.supCode, 'name':dictData.codeName,'abr1':dictData.codeAbr1, 'fullName':dictData.codeAbr2, 'noteType':dictData.noteType,'checked':true, isParent:dictData.isParent});
		    	}else {
		    		commonDictNodes.push({'code':dictData.code,'codeName':dictData.codeName,'id':dictData.unitHiberId, 'pId':dictData.supCode, 'name':dictData.codeName, 'abr1':dictData.codeAbr1, 'fullName':dictData.codeAbr2, 'noteType':dictData.noteType, isParent:dictData.isParent});
		    	}
	    	} else {
		    	if($.inArray(dictData.code, checkIdArr) != -1) {//包含选中ID
		    		commonDictNodes.push({'code':dictData.code,'codeName':dictData.codeName,'id':dictData.code, 'pId':dictData.supCode, 'name':dictData.codeName,'abr1':dictData.codeAbr1, 'fullName':dictData.codeAbr2, 'noteType':dictData.noteType,'checked':true, isParent:dictData.isParent});
		    	}else {
		    		commonDictNodes.push({'code':dictData.code,'codeName':dictData.codeName,'id':dictData.code, 'pId':dictData.supCode, 'name':dictData.codeName, 'abr1':dictData.codeAbr1, 'fullName':dictData.codeAbr2, 'noteType':dictData.noteType, isParent:dictData.isParent});
		    	}
	    	}
	    }
	    
	    function buildDictNodes(dictNodes, dictData) {
	    	
	    	var showName = "";
	    	// 当表为ZB02时，如果机构有简称显示简称，否则显示全称
	    	if('${isZB02}' == '1' && dictData.codeAbr1){
	    		showName = dictData.codeAbr1;
	    	} else {
	    		showName = dictData.codeName;
	    	}
    		if (dictData.codeNameSuffix) {
    			showName = showName + "("+ dictData.codeNameSuffix +")";
    		}
	    	if (null != dictData.unitHiberId && '' != dictData.unitHiberId) {//ZB02
		    	if($.inArray(dictData.code, checkIdArr) != -1) {//包含选中ID
			    	dictNodes.push({'code':dictData.code,'codeName':dictData.codeName,'id':dictData.unitHiberId, 'pId':dictData.supCode, 'name':showName,'abr1':dictData.codeAbr1, 'fullName':dictData.codeAbr2, 'noteType':dictData.noteType,'myHiddenSign':dictData.isHidden,'codeNameSuffix':dictData.codeNameSuffix,'checked':true, isParent:dictData.isParent});
		    	}else {
			    	dictNodes.push({'code':dictData.code,'codeName':dictData.codeName,'id':dictData.unitHiberId, 'pId':dictData.supCode, 'name':showName, 'abr1':dictData.codeAbr1, 'fullName':dictData.codeAbr2, 'noteType':dictData.noteType,'codeNameSuffix':dictData.codeNameSuffix,'myHiddenSign':dictData.isHidden, isParent:dictData.isParent});
		    	}
	    	} else {
		    	if($.inArray(dictData.code, checkIdArr) != -1) {//包含选中ID
			    	dictNodes.push({'code':dictData.code,'codeName':dictData.codeName,'id':dictData.code, 'pId':dictData.supCode, 'name':showName,'abr1':dictData.codeAbr1, 'fullName':dictData.codeAbr2, 'noteType':dictData.noteType,'myHiddenSign':dictData.isHidden,'codeNameSuffix':dictData.codeNameSuffix,'checked':true, isParent:dictData.isParent});
		    	}else {
			    	dictNodes.push({'code':dictData.code,'codeName':dictData.codeName,'id':dictData.code, 'pId':dictData.supCode, 'name':showName, 'abr1':dictData.codeAbr1, 'fullName':dictData.codeAbr2, 'noteType':dictData.noteType,'codeNameSuffix':dictData.codeNameSuffix,'myHiddenSign':dictData.isHidden, isParent:dictData.isParent});
		    	}
	    	}
	    }
	    /**
	    * 加载字典树
	    * @dictList 树数据
	    *
	    * myHiddenSign 以及 isHidden 都只用于ZB02表时
	    */
	    function loadDictTree(dictList){
	    	var dictNodes = [];
		    for(var i in dictList){
		    	if('${selType}'==9){//多选
		    		buildDictNodes(dictNodes, dictList[i]);
		    	} else {
		    		//获取顶级节点
			    	if (dictList[i].supCode == '' || dictList[i].supCode == null || dictList[i].supCode == 0) {
			    		buildDictNodes(dictNodes, dictList[i]);
			    	}
		    	}
		    }
		    
		    var setting = null;
		    if('${selType}'==1){//单选
		    	setting = {
	    			async: {
	    				enable: true,
	    				dataFilter:filter
	    			},
	    			data: {
	    				simpleData: {
	    					enable: true
	    				}
	    			},
	    			check: {
	    				enable: false
	    			},
					callback: {
						onAsyncSuccess: onAsyncSuccess,
						onClick: getDictValue,
						onDblClick: getValueToObj
					},
					view:{showIcon:false}
				};
		    }else if('${selType}'==3){//多选
		    	setting = {
		    			async: {
		    				enable: true,
		    				dataFilter:filter
		    			},
						data: {
							simpleData: {
								enable: true
							}
						},
						check: {
							enable: true,
							chkStyle: "checkbox",
							nocheckInherit: true,
							chkboxType: {"Y": "s", "N": "s"}
						},
						callback: {
							onAsyncSuccess: onAsyncSuccess,
							onClick: getDictValue2,
							onCheck: getDictValue3
						},
						view:{showIcon:false}
					};
		    }else if('${selType}'==7){//单选:显示全称
		    	setting = {
		    			async: {
		    				enable: true,
		    				dataFilter:filter
		    			},
						data: {
							simpleData: {
								enable: true
							}
						},
						callback: {
							onAsyncSuccess: onAsyncSuccess,
							onClick: getDictValue,
							onDblClick: getValueToObj2
						},
						view:{showIcon:false}
					};
			}else if('${selType}'==9){//多选
		    	setting = {
		    			async: {
		    				enable: true,
		    				dataFilter:filter
		    			},
						data: {
							simpleData: {
								enable: true
							}
						},
						check: {
							enable: true,
							chkStyle: "checkbox",
							nocheckInherit: true,
							chkboxType: {"Y": "s", "N": "s"}
						},
						callback: {
							onClick: getDictValue2,
							onCheck: getDictValue3
						},
						view:{showIcon:false}
					};
		    }else{//多选
		    	setting = {
	    			async: {
	    				enable: true,
	    				dataFilter:filter
	    			},
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
						onAsyncSuccess: onAsyncSuccess,
						onClick: getDictValue,
						onCheck: getDictValue1
					},
					view:{showIcon:false}
				};
		    }
		    
		    $("#dictTree").empty();
			dictTree = $.fn.zTree.init($("#dictTree"), setting, dictNodes);
			
			if('${isZB02}' == '1'){
				var treeObj = $.fn.zTree.getZTreeObj("dictTree");
				var nodes = treeObj.getNodes();
				treeObj.expandNode(nodes[0], true, false, true);//展开第一个节点的一级节点
				var nodehs = treeObj.getNodesByParam("myHiddenSign", true);
				treeObj.hideNodes(nodehs);
			}
			
	    }

	    var commonDictTree;
	    /**
	    * 加载常用代码字典树
	    * @commonDictList 树数据
	    */
	    function loadCommonDictTree(commonDictList){

	    	var commonDictNodes = [];
		    for(var i in commonDictList){
		    	//获取顶级节点
		    	if (commonDictList[i].supCode == '' || commonDictList[i].supCode == null || commonDictList[i].supCode == 0) {
		    		buildCommonDictNodes(commonDictNodes, commonDictList[i]);
		    	}
		    }
		    var setting = null;
		    if('${selType}'==1){//单选
		    	setting = {
	    			async: {
	    				enable: true,
	    				dataFilter:filter
	    			},
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						onAsyncSuccess: onAsyncCommonSuccess,
						onClick: getDictValue,
						onDblClick: getValueToObj
					},
					view:{showIcon:false}
				};
		    }else if('${selType}'==3){//多选
		    	setting = {
		    			async: {
		    				enable: true,
		    				dataFilter:filter
		    			},
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
							onAsyncSuccess: onAsyncCommonSuccess,
							onClick: getDictValue2,
							onCheck: getDictValue3
						},
						view:{showIcon:false}
					};
		    }else if('${selType}'==7){//单选:显示全称
		    	setting = {
		    			async: {
		    				enable: true,
		    				dataFilter:filter
		    			},
						data: {
							simpleData: {
								enable: true
							}
						},
						callback: {
							onAsyncSuccess: onAsyncCommonSuccess,
							onClick: getDictValue,
							onDblClick: getValueToObj2
						},
						view:{showIcon:false}
					};
			}else{//多选
		    	setting = {
	    			async: {
	    				enable: true,
	    				dataFilter:filter
	    			},
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
						onAsyncSuccess: onAsyncCommonSuccess,
						onClick: getDictValue,
						onCheck: getDictValue1
					},
					view:{showIcon:false}
				};
		    }
			commonDictTree = $.fn.zTree.init($("#commonDictTree"), setting, commonDictNodes);
			if('${isZB02}' == '1'){
				var treeObj = $.fn.zTree.getZTreeObj("commonDictTree");
				var nodes = treeObj.getNodes();
				for(var i = 0; i < nodes.length; i++){
					treeObj.expandNode(nodes[i], true, false, true);
				}
			}
	    }
	    
		/**
		* 获得选中的字典值
		*/
		function getDict(){
			if($dictTabs.tabs('option', 'active')==0){//从"常用代码"中获取数据
				getSelectDict();
			} else  if($dictTabs.tabs('option', 'active')==1){//从"所有代码"中获取数据
				getSelectDict();
			} else {//从"查询结果"中获取数据
				var rows = dictTableList.selectedRows();
				var item = rows[0];
				if(typeof(item)=='undefined'){
					openAlert('请选择一条记录！');
					return;
				}
				setValueIntoCaller(item);
			}
			if(typeof(changeFlag)!='undefined'){//信息集录入页面修改标识
				changeFlag = true;
			}
			closeDict();
			$('#dict_window_content').html('');
			if (typeof(comMethod)!='undefined'&&comMethod != null) {//调用回调方法,该方法从dictWindow方法中的参数来
				var callbacks = $.Callbacks();
				callbacks.add(comMethod);
				callbacks.fireWith( window, new Array());
			}
		}
		
		function setValueIntoCaller(item){
			if(curDictCodeName!=null && typeof(curDictCodeName)!='undefined') {
				$('#'+curDictCodeName).val(item.codeName);
			}
			
			if(curDictFull!=null && typeof(curDictFull)!='undefined') $('#'+curDictFull).val(item.code+'-'+item.codeAbr2);
			if(curDictCode!=null && typeof(curDictCode)!='undefined') $('#'+curDictCode).val(item.code);
			if(curDictName!=null && typeof(curDictName)!='undefined') {
				if ('${selType}' == 7) {
					$('#'+curDictName).val(item.codeName);
				} else {
					$('#'+curDictName).val(item.codeAbr1);
				}
			}
			$('#'+curDictCode).removeClass('unFix');
			$('#'+curDictName).removeClass('unFix');
		}
		
		function getSelectDict(){
			if(dictName==null){
				dictName = dictFullName;
			}
			if(dictCode==null){
				openAlert('请选择一条记录！');
				return;
			}
			if ("library" == noteType || "unitGroup" == noteType) {
				openAlert('这一级代码不能选择！');
				return;
			}
			if("subGroup" == noteType){
				openAlert('这是分组,不能选择！');
				return;
			}
		
			if (typeof(dictMethod)!='undefined'&&dictMethod != null) {
				var callbacks = $.Callbacks();
				callbacks.add(dictMethod);
				callbacks.fireWith( window, new Array(dictCode, dictName));
			}
			if('${valType}' == 1){
				dictName = dictName.replace(/,/g,'、');
			}
			if(curDictFull!=null && typeof(curDictFull)!='undefined') $('#'+curDictFull).val(dictFullName);
			if(curDictCode!=null && typeof(curDictCode)!='undefined') $('#'+curDictCode).val(dictCode);
			if(curDictName!=null && typeof(curDictName)!='undefined') $('#'+curDictName).val(dictName);
			if(curDictCodeName!=null && typeof(curDictCodeName)!='undefined') {
				$('#'+curDictCodeName).val(dictCodeName);
			}
			
			//数据清洗去除边框
			$('#'+curDictCode).removeClass('unFix');
			$('#'+curDictName).removeClass('unFix');
			dictMethod = null;
		}
		
		/**
		* 取消选择
		*/
		function closeDict(){
			$('#dict_window_content').html('');
			$("#dict_window").dialog('close');
		}
		
		/**
		* 单击时获取字典值
		*/
		function getDictValue(event, treeId, treeNode){
			//只能选择子节点，父节点不能选择
			if(typeof(treeNode.children)!='undefined'){
				isChild = false;
			}else{
				isChild = true;
			}
			
			// 是否勾选
	    	if('${selType}'==2 ||'${selType}'==3){
	    		if (treeNode.checked == true) {
		    		dictTree.checkNode(treeNode, false, true);
		    	} else {
		    		dictTree.checkNode(treeNode, true, true);
		    	}
	    	}
	    	
       	    // 选中的ID
       		var name1 = "",code1="",fullName1="", noteType1="", codeName1="";
       		if('${selType}'==1){//单选
       			name1 += treeNode.abr1;
       			code1 += treeNode.code;
       			fullName1 += treeNode.code + '-' + treeNode.name;
       			noteType1 += treeNode.noteType;
       			codeName1 += treeNode.codeName;
       		}else if('${selType}'==7){//单选:全称
       			name1 += treeNode.name;
       			code1 += treeNode.code;
       			fullName1 += treeNode.code + '-' + treeNode.name;
       			noteType1 += treeNode.noteType;
       			codeName1 += treeNode.codeName;
       		}else{//多选
       			var nodes = dictTree.getCheckedNodes(true);
	       		if (nodes.length > 0) {
	       			if(nodes.length > 1){
	       				for (var i = 0; i < nodes.length; i ++) {
		           			name1 += nodes[i].abr1 + ";";
			       			code1 += nodes[i].code + ";";
			       			fullName1 += nodes[i].code+'-'+nodes[i].name + ";";
			       			codeName1 += nodes[i].codeName + ";";
		    			}
	       			}else{
	       				name1 += treeNode.abr1;
		       			code1 += treeNode.code;
		       			fullName1 += treeNode.code + '-' + treeNode.name;
		       			codeName1 += treeNode.codeName;
	       			}
	       		}
       		}
	    	
			dictName = name1;
			dictCode = code1;
			dictFullName = fullName1;
			noteType = noteType1;
			dictCodeName = codeName1;
		}
		
		/**
		* 勾选时获取字典值
		*/
		function getDictValue1(event, treeId, treeNode){
			var name1 = "",code1="",fullName1="",codeName1="";
       		var nodes = dictTree.getCheckedNodes(true);
       		if (nodes.length > 0) {
       			for (var i = 0; i < nodes.length; i ++) {
           			name1 += nodes[i].abr1 + ";";
	       			code1 += nodes[i].code + ";";
	       			fullName1 += nodes[i].code+'-'+nodes[i].name + ";";
	       			codeName1 += nodes[i].codeName + ";";
    			}
       		}
	    	
			dictName = name1;
			dictCode = code1;
			dictFullName = fullName1;
			dictCodeName = codeName1;
		}
		/**
		* 勾选时获取字典值
		*/
		function getDictValue2(event, treeId, treeNode){
			var curTree;
			if($dictTabs.tabs('option', 'active')==0){//从"常用代码"中获取数据
				curTree=commonDictTree;
			} else{//从"所有代码"中获取数据
				curTree=dictTree;
			}
			
			if (treeNode.checked == true) {
				curTree.checkNode(treeNode, false, true);
	    	} else {
	    		curTree.checkNode(treeNode, true, true);
	    	}
			var name1 = "",code1="",fullName1="", codeName1="";
       		var nodes = curTree.getCheckedNodes(true);
       		if (nodes.length > 0) {
       			for (var i = 0; i < nodes.length; i ++) {
           			name1 += nodes[i].abr1 + ",";
	       			code1 += nodes[i].code + ",";
	       			fullName1 += nodes[i].code+'-'+nodes[i].name + ",";
	       			codeName1 += nodes[i].codeName + ",";
    			}
       			if(name1 != null && name1.length > 0) {
       				name1 = name1.substring(0,name1.length - 1);
       				code1 = code1.substring(0,code1.length - 1);
       				codeName1 = codeName1.substring(0,codeName1.length - 1);
       			}
       		}
			dictName = name1;
			dictCode = code1;
			dictFullName = fullName1;
			dictCodeName = codeName1;
		}
		/**
		* 点击字典值时改变复选框勾选状态，获取字典值
		*/
		function getDictValue3(event, treeId, treeNode){
			var curTree;
			if($dictTabs.tabs('option', 'active')==0){//从"常用代码"中获取数据
				curTree=commonDictTree;
			} else{//从"所有代码"中获取数据
				curTree=dictTree;
			}
			var name1 = "",code1="",fullName1="",codeName1="";
       		var nodes = curTree.getCheckedNodes(true);
       		if (nodes.length > 0) {
       			for (var i = 0; i < nodes.length; i ++) {
           			name1 += nodes[i].abr1 + ",";
	       			code1 += nodes[i].code + ",";
	       			fullName1 += nodes[i].code+'-'+nodes[i].name + ",";
	       			codeName1 += nodes[i].codeName + ",";
    			}
       			if(name1 != null && name1.length > 0) {
       				name1 = name1.substring(0,name1.length - 1);
       				code1 = code1.substring(0,code1.length - 1);
       				codeName1 = codeName1.substring(0,codeName1.length - 1);
       			}
       		}
	    	
			dictName = name1;
			dictCode = code1;
			dictFullName = fullName1;
			dictCodeName = codeName1;
		}
		
		function filter(treeId, parentNode, childNodes) {
        }
		
		function onAsyncSuccess(event, treeId, treeNode, msg) {
	
			var dictNodes = [];
			var dictList = dictListCache;
			//根据父Id查找子节点
		    for(var i in dictList){
		    	if (dictList[i].supCode != treeNode.id) {
		    		continue;
		    	}
		    	buildDictNodes(dictNodes, dictList[i]);
		    }
		    
		    asynGetNodes(treeNode, dictNodes, 'dictTree');
		}
		
		function onAsyncCommonSuccess(event, treeId, treeNode, msg) {
			
			var commonDictNodes = [];
			var dictList = commonDictListCache;
			//根据父Id查找子节点
		    for(var i in dictList){
		    	if (dictList[i].supCode != treeNode.id) {
		    		continue;
		    	}
		    	buildCommonDictNodes(commonDictNodes, dictList[i]);
		    }
		    
		    asynGetNodes(treeNode, commonDictNodes,'commonDictTree');
		}

		
		function asynGetNodes(treeNode, dictNodes, treeDivId) {
		    var zTree = $.fn.zTree.getZTreeObj(treeDivId);
		    if(dictNodes != null && dictNodes.length != 0){
				if(treeNode == undefined){
					zTree.addNodes(null,dictNodes,true);// 如果是根节点，那么就在null后面加载数据
				}
				else{
					zTree.addNodes(treeNode,dictNodes,true);//如果是加载子节点，那么就是父节点下面加载
				}
			}
		    zTree.expandNode(treeNode,true, false, false);// 将新获取的子节点展开
		}
		
		
		/**
		* 双击时值直接带入页面
		*/
		function getValueToObj(event, treeId, treeNode){
			//只能选择子节点，父节点不能选择
			if(typeof(treeNode.children)!='undefined'){
				isChild = false;
			}else{
				isChild = true;
			}
			dictName = treeNode.abr1;
			dictCode = treeNode.code;
			dictFullName = treeNode.code+'-'+treeNode.name;
			dictCodeName = treeNode.codeName;
			getDict();
		}
		/**
		* 双击时值直接带入页面（全称）
		*/
		function getValueToObj2(event, treeId, treeNode){
			//只能选择子节点，父节点不能选择
			if(typeof(treeNode.children)!='undefined'){
				isChild = false;
			}else{
				isChild = true;
			}
			dictName = treeNode.name;
			dictCode = treeNode.code;
			dictFullName = treeNode.code+'-'+treeNode.name;
			dictCodeName = treeNode.codeName;
			getDict();
		}
		
		var clickCount = 0;
		
		/**
		* 初始化表格信息
		*/
		function initDictTable(){
			var dictCols; 
			var multiSelectFlag = false;
			if('${isZB02}' == '1'){
				dictCols = [
						    { title:'名称', name:'codeName' ,width:320, align:'left', sortable: false, type: 'text'},
						    { title:'简称', name:'codeAbr2' ,width:230, align:'left', sortable: false, type: 'text',hidden:true}
						   ];
			}else{
				dictCols = [
						    { title:'代码', name:'code' ,width:90, align:'left', sortable: false, type: 'text'},
						    { title:'名称', name:'codeName' ,width:230, align:'left', sortable: false, type: 'text'},
						    { title:'简称1', name:'codeAbr1' ,width:230, align:'left', sortable: false, type: 'text',hidden:true},
						    { title:'简称2', name:'codeAbr2' ,width:230, align:'left', sortable: false, type: 'text',hidden:true}
						   ];
			}
			dictTableList = $('#dictTableList').mmGrid({
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
			dictTableList.on('cellSelected', function(e, item, rowIndex, colIndex) {
				//双击行事件
				/* var trs = $("#dictTableList tr:eq("+rowIndex+")");
				trs.each(function(i){
					$("#dictTableList tr:eq("+rowIndex+")").eq(i).dblclick(function () {
						setValueIntoCaller(item);
						if(typeof(changeFlag)!='undefined') changeFlag = true;//信息集录入页面修改标识
						closeDict();
						$('#dict_window_content').html('');
						if (typeof(comMethod)!='undefined'&&comMethod != null&&clickCount==0) {//调用回调方法,该方法从dictWindow方法中的参数来
							clickCount++;//回调方法调用一次就行了，这个参数来做控制
							var callbacks = $.Callbacks();
							callbacks.add(comMethod);
							callbacks.fireWith( window, new Array());
						}
					});
				}); */
			});
			dictTableList.on('dbCellClicked', function(e, item, rowIndex, colIndex) {
				//双击行事件
				setValueIntoCaller(item);
				if(typeof(changeFlag)!='undefined') changeFlag = true;//信息集录入页面修改标识
				closeDict();
				$('#dict_window_content').html('');
				if (typeof(comMethod)!='undefined'&&comMethod != null&&clickCount==0) {//调用回调方法,该方法从dictWindow方法中的参数来
					clickCount++;//回调方法调用一次就行了，这个参数来做控制
					var callbacks = $.Callbacks();
					callbacks.add(comMethod);
					callbacks.fireWith( window, new Array());
				}
			});
		}
	       
		/**
		* 加载查询结果
		* @dictList 所有结果
		* @key 	    搜索关键字
		*/		
		function loadDictTable(dictList, key){
			clearInterval(timer);
			if(key=='') return;
			var dictItems = [];
			for(var i in dictList){
				var noteType = dictList[i].noteType;
				if ("library" == noteType || "unitGroup" == noteType || "subGroup" == noteType) {
					continue;
				}
				if('${isZB02}' == '1'&&!$('#dictShowHided').is(":checked")){
					if(dictList[i].isHidden!=null&&dictList[i].isHidden==true){
						continue;
					}
				}
				if((dictList[i].code != null && dictList[i].code.indexOf(key)>-1) || (dictList[i].codeName != null && dictList[i].codeName.indexOf(key)>-1) || (dictList[i].codeSpelling != null && dictList[i].codeSpelling.indexOf(key.toLowerCase())>-1) || (dictList[i].codeSpelling != null && dictList[i].codeSpelling.indexOf(key.toUpperCase())>-1)){
					dictItems.push(dictList[i]);
				}
			}
			dictTableList.load(dictItems);
		}
		function isContains(checkedIds, checkedId) {
			if(checkedIds == null || checkedIds == '')
				return false;
		    return checkedIds.indexOf(checkedId) >= 0;
		}
		
		function showOrHideInDict(obj){
			if(obj.checked==true){
				var nodes = dictTree.getNodesByParam("isHidden", true);
				dictTree.showNodes(nodes);
				if($dictTabs.tabs('option', 'active')==2){
					if($('#fastSearch').val()!=''){
						loadDictTable(dictListCache, $('#fastSearch').val());
					}
				}
			}else{
				var nodes = dictTree.getNodesByParam("myHiddenSign", true);
				dictTree.hideNodes(nodes);
				if($dictTabs.tabs('option', 'active')==1){//从"所有代码"中获取数据
					for (var i = 0; i < nodes.length; i++) {
			   			if (nodes[i].id==dictCode) {
			   				dictName = null;
			   				dictCode = null;
			   				dictFullName = null;
			   				dictCodeName = null;
			   				noteType = null;
			   				dictTree.checkNode(nodes[i], false, true);
			   			} 
					}
				}else if($dictTabs.tabs('option', 'active')==2){
					loadDictTable(dictListCache, $('#fastSearch').val());
				}
			}
		}
	</script>
</body>