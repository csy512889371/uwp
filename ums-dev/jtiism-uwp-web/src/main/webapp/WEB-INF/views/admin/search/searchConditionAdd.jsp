<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<link href="<%=basePath%>resources/admin/modules/enterprise/enterprise.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=basePath%>resources/admin/modules/enterprise/enterprise.js"></script>
<style>
	#conditionDiv .input{ width: 175px; }
	/* #searchTab ul{margin-bottom: 5px;} */
	.viewChooseDiv{width:100%;height:50px;line-height: 50px;}
	.viewChooseDiv select{width:130px;}
	#searchTab ul li{border: none;}
	#switchBar{height:100%;cursor: pointer;float: left;width:13px;background-color: #fff;border-right:none;}
    #switchBar:hover{background-color: #f7f7f7;}
    #switchHref{top: 50%;position: absolute;}
    #conditionDiv, #resultDiv{border:none;}
    #conditionDiv{margin-top: 0px;}
</style>
<body>
	<div id="searchTab" style="float:left;width:100%;border: none;">
		<ul>
			<li><a href="#conditionDiv">定制查询</a></li>
			<li><a href="#resultDiv">查询结果</a></li>
		</ul>
		<div id="conditionDiv">
			<div id="etpTableList" style="float:left;width:270px;border-right: 1px solid #ccc;">
				<div id="fixedSearch" style="overflow: auto;/* background-color: #f5f5f5; */" class="cadre_search">
					<ul id="infoTableTree" class="ztree" style="width:95%;"></ul>
				</div>
			</div>
			<!-- 切换显示隐藏的bar -->
			<div id="switchBar" onclick="switchShow()">
				<i id="switchHref" class="icon-chevron-left"></i>&nbsp;&nbsp;&nbsp;
			</div>
			<div id="condtionTable" style="margin-left: 285px; position: relative;">
				<div class="oper_menu">
					<div style="display: inline;text-align: right;">
						<auth:auth ifAllGranted="fixSearch:up">
							<button class="btn-style2" onclick="moveUp()"><i class="icon-arrow-up"></i> 上移</button>
						</auth:auth>
						<auth:auth ifAllGranted="fixSearch:down">
							<button class="btn-style2" onclick="moveDown()"><i class="icon-arrow-down"></i> 下移</button>
						</auth:auth>
						<auth:auth ifAllGranted="fixSearch:delete">
							<button class="btn-style2" onclick="deleteChooseField()"><i class="icon-trash"></i> 删除</button>
						</auth:auth>
						<auth:auth ifAllGranted="fixSearch:deleteAll">
							<button class="btn-style2" onclick="deleteAllChooseField()"><i class="icon-remove"></i> 全删</button>
						</auth:auth>
						<auth:auth ifAllGranted="fixSearch:search">
							<button class="btn-style2" onclick="queryByConditions()">执行查询</button>
						</auth:auth>
						
						<auth:auth ifAllGranted="fixSearch:saveToTemp">
							<button class="btn-style2" onclick="saveCondtions()"><i class="icon-ok"></i> 存为常用查询</button>
						</auth:auth>

					</div>
				</div>
				<div style="width:100%;background-color: #00BBBB;">
					<table id="conditionList" class="mmg">
		                <tr>
		                    <th rowspan="" colspan=""></th>
		                </tr>
		            </table>
				</div>
				<div class="viewChooseDiv" style="display:none;">
				</div>
				<div id="realTimeConditionInfo" style="height: 35px;font-size: 13px;line-height: 15px;overflow: auto;text-align: left;border: 1px solid #ddd;padding-top: 5px;"></div>
			</div>
					
		</div>
	
		<!-- 查询结果 -->
		<div id="resultDiv"></div>
	</div>
	
	<script>
	//select模板
	var opUDSelected = Hogan.compile(LE_TEMPLATE.optionSelected);
	var opUD=Hogan.compile(LE_TEMPLATE.option);
	var commonUDInput=Hogan.compile(LE_TEMPLATE.commonInput);
	var dateUDInput=Hogan.compile(LE_TEMPLATE.dateInput);
	var codeUDInput=Hogan.compile(LE_TEMPLATE.codeInput);
	var mtInput=Hogan.compile(LE_TEMPLATE.multiTypeInput);
	var logicCode=JSON.parse('${rop}');//${rop};
	var operaCode=JSON.parse('${cop}');
	var lbCode=JSON.parse('${lbreaket}');
	var rbCode=JSON.parse('${rbreaket}');
	
	var token='${token}';
	
	var queryTemplateList = null;//查询模板列表
	var enterpriseList = null; //查询结果列表
	
	var showLeft = true;
	
	var submitType=0;
		$( "#searchTab" ).tabs({
	      collapsible: false
	    });
	
	     $('#etpTableList').height($(window).height()-146);
	     $('#condtionTable').height($('#etpTableList').height());
	     $('.cadre_search').height($('#etpTableList').height()-50);
	     $('#switchBar').height($(window).height()-145);
	     
	     /**
	     * 窗口自适应
	     */
	     function adjustWindow(){
	    	 $('#etpTableList').height($(window).height()-146);
		     $('#condtionTable').height($('#etpTableList').height());
		     $('.cadre_search').height($('#etpTableList').height()-25);
		     if(conditionList!=null) conditionList.resize($(window).height()-200);
		     if(queryTemplateList!=null) queryTemplateList.resize($(window).height()-240);
		     if(enterpriseList!=null) enterpriseList.resize($(window).height()-240);
		     $('#switchBar').height($(window).height()-145);
	     }
	     
	     function switchShow(){
			if(showLeft){
				showLeft = false;
				$('#etpTableList').hide();
				$('#condtionTable').css('margin-left','13px');
				$('#switchHref').removeClass('icon-chevron-left');
				$('#switchHref').addClass('icon-chevron-right');
			}else{
				showLeft = true;
				$('#etpTableList').show();
				$('#condtionTable').css('margin-left','285px');
				$('#switchHref').removeClass('icon-chevron-right');
				$('#switchHref').addClass('icon-chevron-left');
			}
		}
	     
	     showInfoTableTree();
	     
	     var separator='${separaotr}';
	     var conditionList;
	     var conditonData=[];
	     var submitDatas='';//提交的数据集
	      var cols = [
             { title:'序号', name:'order' ,width:30, align:'left', sortable: false, type: 'text', renderer: function(val){
                 return val;
             }},
             { title:'组合', name:'lbreaket' ,width:80, align:'center', sortable: false,type: 'text', renderer: function(val){
             	var select='';
             	select+="<select style='width:70px;'>";
             	for(var i in lbCode){
             		if(lbCode[i].val==val){
             			select+=opUDSelected.render({value: lbCode[i].val,text:lbCode[i].valDesc});
             		}else{
             			select+=opUD.render({value: lbCode[i].val,text:lbCode[i].valDesc});
             		}
             	}
             	select+="</select>";
             	return select;
             } },
             { title:'名称', name:'name' ,width:150, align:'center', sortable: false, type: 'text'} ,
             { title:'操作符', name:'operator' ,width:90, align:'center', sortable: false, type: 'text',renderer: function(val,item,rowIndex){
            	 if(val.vtype=='sql'){
            		 return '';
	             }
             	var select='';
             	select+="<select style='width:90px;'  onchange='changeOper(this,this.value)'>";
             	for(var i in operaCode){
	             	if(val.vtype=='number'||val.vtype=='datetemp'){
	             		if(operaCode[i].val=='like'||operaCode[i].val=='notLike'||operaCode[i].val=='in'||operaCode[i].val=='notIn'){
	             			continue;
	             		}
	             	}else if(val.vtype=='codeType'){
	             		if(operaCode[i].val=='gt'||operaCode[i].val=='ge'||operaCode[i].val=='lt'||operaCode[i].val=='le'||operaCode[i].val=='between'||operaCode[i].val=='like'||operaCode[i].val=='notLike'||operaCode[i].val=='in'||operaCode[i].val=='notIn'){
	             			continue;
	             		}
	             	}else if(val.vtype=='multiType'){
	             		if(operaCode[i].val=='eq'||operaCode[i].val=='ne'||operaCode[i].val=='gt'||operaCode[i].val=='ge'||operaCode[i].val=='lt'||operaCode[i].val=='le'||operaCode[i].val=='between'||operaCode[i].val=='like'||operaCode[i].val=='notLike'||operaCode[i].val=='notIn'){
	             			continue;
	             		}
	             	}else if(val.vtype=='text'){
	             		if(operaCode[i].val=='eq'||operaCode[i].val=='ne'||operaCode[i].val=='gt'||operaCode[i].val=='ge'||operaCode[i].val=='lt'||operaCode[i].val=='le'||operaCode[i].val=='between'||operaCode[i].val=='in'||operaCode[i].val=='notIn'){
	             			continue;
	             		}
	             	}else{
	             		if(operaCode[i].val=='gt'||operaCode[i].val=='ge'||operaCode[i].val=='lt'||operaCode[i].val=='le'||operaCode[i].val=='between'||operaCode[i].val=='in'||operaCode[i].val=='notIn'){
	             			continue;
	             		}
	             	}
             		if(operaCode[i].val==val.value){
             			select+=opUDSelected.render({value: operaCode[i].val,text:operaCode[i].valDesc});
             		}else{
             			select+=opUD.render({value: operaCode[i].val,text:operaCode[i].valDesc});
             		}
             	}
             	select+="</select>";
             	return select;
             }},
             { title:'值1', name:'value1' ,width:180, align:'center', sortable: false, type: 'text',renderer: function(val,item,rowIndex){
             	var input='';
             	if(val.vtype=="varchar"||val.vtype=="text"){
             		input+=commonUDInput.render({value: val.value[0]});//"<input class='input' type='text' value='"+ val.value[0]+"' />";
             	}else if(val.vtype=="number"){
             		input+=commonUDInput.render({value: val.value[0],onkeyUp:'this.value=this.value.replace(/[^\\.\\d]/g,"")'});//;this.value=this.value.replace(".","")
             	}else if(val.vtype=="datetemp"){
             		input+=dateUDInput.render({dateId:"dateo"+rowIndex,dateFormat:'yyyy-MM-dd',value: val.value[0]});//input+="<input class='input' id='dateo"+rowIndex+"' type='text' onclick='getDate(this)' onkeyup=\"this.value=''\" value='"+ val.value[0]+"' readOnly='readOnly' />";
             	}else if(val.vtype=="codeType"){
             	input+=codeUDInput.render({
             		nameId:val.nameObj+rowIndex,
             		tableName:val.tableName,
             		codeName:val.fieldName,
             		title:val.title,
             		forNameId:val.nameObj+rowIndex,
             		forCodeId:val.codeObj+rowIndex,
             		value1: val.value[0],
             		codeId:val.codeObj+rowIndex,
             		value2:val.value[1]});
             	}else if(val.vtype=="multiType"){
             		input+=mtInput.render({
             			tableName:val.tableName,
                 		codeName:val.fieldName,
                 		title:val.title,
                 		forNameId:val.nameObj+rowIndex,
                 		forCodeId:val.codeObj+rowIndex,
                 		value1:val.value[0],
                 		value2:val.value[1]});
             	}
             	return input;
             }},
             { title:'值2', name:'value2' ,width:180, align:'center', sortable: false, type: 'text',renderer: function(val,item,rowIndex){
            	 var input='';
            	 if(val.type=='show'){
            	 	if(val.vtype=="varchar"){
	             		input+=commonUDInput.render({value: val.value[0]});
	             	}else if(val.vtype=="number"){
	             		input+=commonUDInput.render({value: val.value[0]});
	             	}else if(val.vtype=="datetemp"){
	             		input+=dateUDInput.render({dateId:"datet"+rowIndex,dateFormat:'yyyy-MM-dd',value: val.value[0]});//"<input class='input' id='datet"+rowIndex+"' type='text' onclick='getDate(this)' onkeyup=\"this.value=''\" value='"+ val.value[0]+"' readOnly='readOnly' />";
	             	}//值2中不会出现代码集
            	 }
             	return input;
             }},
             { title:'组合', name:'rbreaket' ,width:80, align:'center', sortable: false, type: 'text', renderer: function(val){
             	var select='';
             	select+="<select style='width:70px;'>";
             	for(var i in rbCode){
             		if(rbCode[i].val==val){
             			select+=opUDSelected.render({value: rbCode[i].val,text:rbCode[i].valDesc});
             		}else{
             			select+=opUD.render({value: rbCode[i].val,text:rbCode[i].valDesc});
             		}
             	}
             	select+="</select>";
             	return select;
             }},
             { title:'逻辑', name:'logic' ,width:80, align:'center', sortable: false, type: 'text', renderer: function(val){
             	var select='';
             	select+="<select style='width:70px;'>";
             	for(var i in logicCode){
             		if(logicCode[i].val==val){
             			select+=opUDSelected.render({value: logicCode[i].val,text:logicCode[i].valDesc});
             		}else{
             			select+=opUD.render({value: logicCode[i].val,text:logicCode[i].valDesc});
             		}
             	}
             	select+="</select>";
             	return select;
             }}
         ];
         var map=[];
	     $(document).ready(function(){
			var name = $('#actName').val();
			conditionList = $('#conditionList').mmGrid({
                    height: commonTalbeHeight-55
                    , cols: cols
                    , items:map
                    , method: 'get'
                    , remoteSort:true
                    , sortName: 'activityname'
                    , sortStatus: 'asc'
                    , multiSelect: false
                    , checkCol: true
                    , fullWidthRows: true
                    , autoLoad: false
                    , showBackboard : false
                    , plugins: [
                        $('#pg').mmPaginator({})
                    ]
                    , params: function(){
                        //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
                      return {
                          secucode: $('#secucode').val()
                      };
                    }
                });
                conditionList.on('cellSelected', function(e, item, rowIndex, colIndex){
                	
                }).load();
                deleteAllChooseField();
                virtul();
                $("#conditionList input,#conditionList select").on("change",function(){
                	realTimeConditionInfo();
            	});
		});
		
	     function virtul(){
	    	  realTimeConditionInfo();
	     }
		var infoTableTree;
		function showInfoTableTree() {
			var infoTableTreeSetting = {
				check : {
					enable : false,
					nocheckInherit : true
				},
				data : {
					simpleData : {
						enable : true
					}
				},
				callback : {
					//onDblClick : chooseField
					onClick: chooseField
				}
			};
			var data =JSON.parse('${tranList}');
			var infoTableNodes = [];
			for ( var s in data) {
				infoTableNodes.push({
					'id' : data[s].id,
					'pId' : data[s].parentid,
					'name' : data[s].cname,
					'ename':data[s].ename,
					'type':data[s].valueType,
					'sql':data[s].sql,
					'open' : false
				});
			}
			infoTableTree = $.fn.zTree.init($("#infoTableTree"), infoTableTreeSetting,infoTableNodes);
		}
		
		function chooseField(event, treeId, treeNode){
	     	if(treeNode.isParent==true){
	     		return;
	     	}
	     	var parentNode=treeNode.getParentNode();
			var item={
				order:conditionList.rowsLength()+1,
				lbreaket:'NONE',//'NONE'初始值
				name:treeNode.name,
				operator:{vtype:treeNode.type,value:'eq'},//value是初始值
				value1:{tableName:parentNode.ename,fieldName:treeNode.ename,title:treeNode.name,nameObj:treeNode.ename+'af',codeObj:treeNode.ename+'f',value:['',''],vtype:treeNode.type},//value是初始值
				value2:{tableName:'',fieldName:'',title:'',nameObj:'',codeObj:'',value:[],type:''},//value是初始值
				rbreaket:'NONE',//'NONE'初始值
				logic:'and'//'and'初始值
				};
			conditonData.push({tname:parentNode.ename,fname:treeNode.ename,cname:treeNode.name,vtype:treeNode.type,sql:treeNode.sql});
	     	conditionList.addRow(item,conditionList.rowsLength());
	     	//@TODO 修改提示
	     	 $("#conditionList input,#conditionList select").on("change",function(){
            		//@TODO
                	realTimeConditionInfo();
            	});
	     	realTimeConditionInfo();
		}
		
	  	function saveCondtions(){
	     	//1 检测合法性 左右括号是否合法
	     	if(conditionList.rowsLength()==0){
	     		openAlert('请选择查询条件！');
	     		return;
	     	}
	     	if(!checkLegal()){
	     		openAlert('括号使用不合法！');
	     		return;
	     	}
	     	//2 提取数据
	   	 	if(saveCondtionsToSubmitData()){
	   	 	//3 弹框
		     	openWindow('保存数据','admin/udsearch/conditionInfoPage.do',1,500,270);
	   	 	}
	     
	     }
	  	
	  	
	    
	     
	     function saveCondtionsToSubmitData(){
	     	submitDatas='';
	      	for(var i=0;i<conditionList.rowsLength();i++){
	      		var data=conditonData[i];
	      		var item={orderNum:i+1};
	      		var temp;
	      		var a_lbreaket=$("#conditionList tr:eq("+i+") td:eq(2) span select").val();
	      		var operator=$("#conditionList tr:eq("+i+") td:eq(4) span select").val();
	      		temp=$("#conditionList tr:eq("+i+") td:eq(5) span input:eq(0)").val();
	      		var a_firValue=typeof(temp)=='undefined'?'':temp;
	      		temp=$("#conditionList tr:eq("+i+") td:eq(5) span input:eq(1)").val();
	      		var a_firCode=typeof(temp)=='undefined'?'':temp;
	      		temp=$("#conditionList tr:eq("+i+") td:eq(6) span input:eq(0)").val();
	      		var a_secValue=typeof(temp)=='undefined'?'':temp;
	      		temp=$("#conditionList tr:eq("+i+") td:eq(6) span input:eq(1)").val();
	      		var a_secCode=typeof(temp)=='undefined'?'':temp;
	      		var a_rbreaket=$("#conditionList tr:eq("+i+") td:eq(7) span select").val();
	      		var logic=$("#conditionList tr:eq("+i+") td:eq(8) span select").val();
	      		var valueType='varchar';
	      		if(data.vtype=='varchar'){//时间也是字符串存储
	      			valueType='varchar';
	      		}else if(data.vtype=='sql'){//代码集形式
	      			valueType='sql';
	      			operator='sql';
	      		}else if(data.vtype=='text'){//代码集形式
	      			valueType='text';
	      		}else if(data.vtype=='datetemp'){
	      			valueType='datetemp';
	      			if(operator!='isNull'&&operator!='isNotNull'){
	      				if(a_firValue==''){
		      				openAlert("时间类型不允许为空！");
		      				return false;
		      			}
		      			if(operator=='between' && a_secValue==''){
		      				openAlert("时间类型不允许为空！");
		      				return false;
		      			}
	      			}
	      		}else if(data.vtype=='number'){//数字
	      			valueType='number';
	      			if(operator!='isNull'&&operator!='isNotNull'){
	      				if(a_firValue==''){
		      				openAlert("整数类型不允许为空！");
		      				return false;
		      			}
		      			if(operator=='between' && a_secValue==''){
		      				openAlert("整数类型不允许为空！");
		      				return false;
		      			}
	      			}
	      		}else if(data.vtype=='codeType'){//代码集形式
	      			valueType='codeType';
	      		}else if(data.vtype=='multiType'){//多选弹框
	      			if(operator=='in'||operator=='notIn'){
	      				if(a_firValue==''){
		      				openAlert("包含操作内值不允许为空！");
		      				return false;
		      			}
	      			}
	      			valueType='multiType';
	      		}
	      		
	      		item='{"ordernum":"'+(i+1)+'"'+separator
	      			+'"lbreaket":"'+a_lbreaket+'"'+separator
	      			+'"dname":"'+data.tname+'"'+separator
	      			+'"fname":"'+data.fname+'"'+separator
	      			+'"cop":"'+operator+'"'+separator
	      			+'"firValue":"'+a_firValue+'"'+separator
	      			+'"firCode":"'+a_firCode+'"'+separator
	      			+'"secValue":"'+a_secValue+'"'+separator
	      			+'"secCode":"'+a_secCode+'"'+separator
	      			+'"rbreaket":"'+a_rbreaket+'"'+separator
	      			+'"rop":"'+logic+'"'+separator
	      			+'"valueType":"'+valueType+'"'+separator
	      			+'"sql":"'+data.sql+'"}';
	      		submitDatas+=item.replace(/,/g,separator)+',';
	      	}
	      
	   		realTimeConditionInfo();
	      	return true;
	     }
	     function queryByConditions(){
	     	if(!checkLegal()){
	     		openAlert('括号使用不合法！');
	     		return;
	     	}
	     	//1提取数据
	     	if(saveCondtionsToSubmitData()){
	     		submitType=0;
		   	 	//载入页面
		   	 	 $('#resultDiv').load('<%=basePath%>admin/udsearch/searchResultPage.do',{page:1});
		   	 	//searchList(1);
		   	 	$('#searchTab li:eq(1) a').click();
	     	}
	     }
	     function checkLegal(){
	     	 var num=0;//num小于0则不合法
	     	for(var i=0;i<conditionList.rowsLength();i++){
	    		var lbreaket=$("#conditionList tr:eq("+i+") td:eq(2) span select").val();
	    		var rbreaket=$("#conditionList tr:eq("+i+") td:eq(7) span select").val();
	    		if(lbreaket=='SINGLE'){
	    			num+=1;
	    		}else if(lbreaket=='DOUBLE'){
	    			num+=2;
	    		}else if(lbreaket=='THREE'){
	    			num+=3;
	    		}else if(lbreaket=='FOUR'){
	    			num+=4;
	    		}else if(lbreaket=='FIVE'){
	    			num+=5;
	    		}
	    		if(rbreaket=='SINGLE'){
	    			num-=1;
	    		}else if(rbreaket=='DOUBLE'){
	    			num-=2;
	    		}else if(rbreaket=='THREE'){
	    			num-=3;
	    		}else if(rbreaket=='FOUR'){
	    			num-=4;
	    		}else if(rbreaket=='FIVE'){
	    			num-=5;
	    		}
	    		if(num<0){
	    		return false;
	    		}
	     	}
	     	if(num!=0){
	     		return false;
	     	}
	     	return true;
	     }
	     //选中项下移
	      function moveDown(){
	     	var index=conditionList.selectedRowsIndex()[0];//
	     	if(typeof(index)=='undefined'){
	     		openAlert('请选择一条记录！');
	     		return;
	     	}
	     	if(index==conditionList.rowsLength()-1){
	     		return;
	     	}
	     	var item1=getItemByOrder(index);//获取index下标的数据集
	     	item1["order"]=index+2;
	     	var item2=getItemByOrder(index+1);//获取index+1下标的数据集
	     	item2["order"]=index+1;
			//表格里面交换一下
	     	conditionList.removeRow([index,index+1]);//删除本行
	     	conditionList.addRow(item2,index);//移到下一行
	     	conditionList.addRow(item1,index+1);//移到下一行
	     	conditionList.select(index+1);
	     	//conditonData里的数据交换
	     	var data=conditonData[index];
	     	conditonData[index]=conditonData[index+1];
	     	conditonData[index+1]=data;
	     	
	     	//@TODO 修改提示
	     	 $("#conditionList input,#conditionList select").on("change",function(){
            		//@TODO
                	realTimeConditionInfo();
            	});
	     	realTimeConditionInfo();
	     }
	    //选中项上移
	     function moveUp(){
	     	var index=conditionList.selectedRowsIndex()[0];//
	     	if(typeof(index)=='undefined'){
	     		openAlert('请选择一条记录！');
	     		return;
	     	}
	     	if(index==0){
	     		return;
	     	}
	     	var item1=getItemByOrder(index);//获取index下标的数据集
	     	item1["order"]=index;
	     	var item2=getItemByOrder(index-1);//获取index+1下标的数据集
	     	item2["order"]=index+1;
			//表格里面交换一下
	     	conditionList.removeRow([index-1,index]);//删除本行
	     	conditionList.addRow(item1,index-1);//移到下一行
	     	conditionList.addRow(item2,index);//移到下一行
	     	conditionList.select(index-1);
	     	//conditonData里的数据交换
	     	var data=conditonData[index];
	     	conditonData[index]=conditonData[index-1];
	     	conditonData[index-1]=data;
	     	
	     	//@TODO 修改提示
	     	 $("#conditionList input,#conditionList select").on("change",function(){
            		//@TODO
                	realTimeConditionInfo();
            	});
	     	realTimeConditionInfo();
	     	
	     }
	    
	     
	     //删除选定数据
	     function deleteChooseField(){
	     	var index=conditionList.selectedRowsIndex()[0];
	     	if(typeof(index)=='undefined'){
	     		openAlert('请选择一条记录！');
	     		return;
	     	}
	     	conditionList.removeRow(index);
	     	conditonData.splice(index,1);
	     	for(var i=0;i<conditionList.rowsLength();i++){
	     		$("#conditionList tr:eq("+i+") td:eq(1) span").html(i+1);
	     	}
	     	//@TODO 修改提示
	     	realTimeConditionInfo();
	     }
	     //删除全部数据
	      function deleteAllChooseField(){
	     	while(conditionList.rowsLength()>0){
	     		conditionList.removeRow(0);
	     		conditonData=[];
	     	}
	     	//@TODO 修改提示
	     	realTimeConditionInfo();
	     }
	     function changeOper(obj,value){
	    	  var $this = $(obj);
	    	  var rowIndex=$this.parents('tr').index();
	    	 if(value=='isNull'||value=='isNotNull'){
		     		$("#conditionList tr:eq("+rowIndex+") td:eq(5) span input:eq(0)").val('');
		     		$("#conditionList tr:eq("+rowIndex+") td:eq(5) span input:eq(0)").attr('disabled', true);
		     	}else{
		     		$("#conditionList tr:eq("+rowIndex+") td:eq(5) span input:eq(0)").removeAttr('disabled');
		     	}
	    	var data=conditonData[rowIndex];
	     	if(data.vtype=='number'||data.vtype=='datetemp'){//有可能用到值2框
	     		if(value!='between'){//如果不是between 取消数据就好
	     			$("#conditionList tr:eq("+rowIndex+") td:eq(6) span").html('');
		     		return;
		     	}
		     	var item=getItemByOrder(rowIndex);
		     	item["order"]=rowIndex+1;
		     	conditionList.removeRow(rowIndex);
		     	conditionList.addRow(item,rowIndex);
	     	}
	     }
	     
	       //根据下标获取一列数据的item
	     function getItemByOrder(index){ 
	    	var data=conditonData[index];
	     	var showType='disabled';
	     	var a_lbreak=$("#conditionList tr:eq("+index+") td:eq(2) span select").val();
	     	var a_opearate=$("#conditionList tr:eq("+index+") td:eq(4) span select").val();
	     	var a_value1=$("#conditionList tr:eq("+index+") td:eq(5) span input:eq(0)").val();
	     	var a_valCode1=$("#conditionList tr:eq("+index+") td:eq(5) span input:eq(1)").val();
	     	var a_value2=$("#conditionList tr:eq("+index+") td:eq(6) span input:eq(0)").val();
	     	var a_valCode2=$("#conditionList tr:eq("+index+") td:eq(6) span input:eq(1)").val();
	     	var a_rbreak=$("#conditionList tr:eq("+index+") td:eq(7) span select").val();
	     	var a_logic=$("#conditionList tr:eq("+index+") td:eq(8) span select").val();
	     	if(a_opearate=='between'){
	     		showType='show';
	     	}
	     	if(typeof(a_valCode2)=='undefined'){
				a_valCode2='';
			}
			if(typeof(a_value2)=='undefined'){
				a_value2='';
			}
	     	var item={
	     			order:index,
					lbreaket:a_lbreak,
					name:data.cname,//
					operator:{vtype:data.vtype,value:a_opearate},//
					value1:{tableName:data.tname,fieldName:data.fname,title:data.cname,nameObj:data.fname+'af',codeObj:data.fname+'f',value:[a_value1,a_valCode1],vtype:data.vtype},
					value2:{tableName:data.tname,fieldName:data.fname,title:data.cname,nameObj:data.fname+'as',codeObj:data.fname+'s',value:[a_value2,a_valCode2],vtype:data.vtype,type:showType},
					rbreaket: a_rbreak,
					logic:a_logic
				};
			return item;
	    }

	     function onlyNumber(val){
	    	 var e = window.event || arguments.callee.caller.arguments[0];
	    	 var keyCode=e.keyCode;
	    	 if(keyCode<48||keyCode>57){
	    		 e.keyCode=0;
	    		 return false;
	    	 }
	    	 return true;
	     }
	     
	     function searchByMultiType(tableName,codeName,title,forNameId,forCodeId){
	    	//所属行业类别。 一个字段存储多个值且用“,” 隔开
	    	if(codeName=='ENTINF012'){
	    		 var method = function(treeObj){
	    	    		// 选中的ID
	    	    		var checkId = "";
	    	    		// 名称
	    	       		var checkName = "";
	    	       		if (typeof(selTreeObject) != 'undefined' ) {
	    	       			for (var i = 0; i < selTreeObject.length; i ++) {
	    	           			checkId += selTreeObject[i].id + ",";
	    	           			checkName += selTreeObject[i].name + "，";
	    	    			}
	    	           		checkId = checkId.substring(0, checkId.length - 1);
	    	           		checkName = checkName.substring(0, checkName.length - 1);
	    	           		$('#' + nameObj).val(checkName);
	    	    			$('#' + idObj).val(checkId);
	    	    			cadreTypeId = checkId;
	    	       		} else {
	    	       			$('#' + nameObj).val('');
	    	    			$('#' + idObj).val('');
	    	       			cadreTypeId = null;
	    	       		}
	    	       		selTreeObject = undefined;
	    	    	};
	    	    	commonWindow('选择', 6, forNameId, forCodeId, 300, 400, method, "");
	    	 }else{
	    		 dictWindow(tableName, codeName, title, forNameId, forCodeId, null, 3);
	    	 }
	     }
	     
	 	function realTimeConditionInfo(){
			var infoStr="";
			for(var i=0;i<conditionList.rowsLength();i++){
				var data=conditonData[i];
				var a_lbreaket=$("#conditionList tr:eq("+i+") td:eq(2) span select option:selected").text();
				var name=$("#conditionList tr:eq("+i+") td:eq(3) span").html();
	      		var operator=$("#conditionList tr:eq("+i+") td:eq(4) span select").val();
	      		var opName=$("#conditionList tr:eq("+i+") td:eq(4) span select option:selected").text();
	      		temp=$("#conditionList tr:eq("+i+") td:eq(5) span input:eq(0)").val();
	      		var a_firValue=typeof(temp)=='undefined'?'':temp;
	      		temp=$("#conditionList tr:eq("+i+") td:eq(6) span input:eq(0)").val();
	      		var a_secValue=typeof(temp)=='undefined'?'':temp;
	      		var a_rbreaket=$("#conditionList tr:eq("+i+") td:eq(7) span select option:selected").text();
	      		var logic=$("#conditionList tr:eq("+i+") td:eq(8) span select").val();
	      		infoStr+="&nbsp;"+a_lbreaket+"&nbsp;"+name+"("+data.fname.toUpperCase()+")";
	      		if(data.vtype=='sql'){
	      			
	      		}else if(operator=='isNull'||operator=='isNotNull'){
	      			infoStr+="&nbsp;<span style='color:#4338ff'>"+opName+"</span>";
	      		}else if(operator=='between'){
	      			infoStr+="&nbsp;<span style='color:#4338ff'>在 </span><span style='color:red'>"
	      			+a_firValue+"</span>&nbsp;<span style='color:#4338ff'>与</span><span style='color:red'>"+a_secValue+
	      			"</span>&nbsp;<span style='color:#4338ff'>之间</span>";
	      		}else {
	      			infoStr+="&nbsp;<span style='color:#4338ff'>"+opName+"</span>&nbsp;<span style='color:red'>\""+a_firValue+"\"</span>";
	      		}
	      		infoStr+="&nbsp;"+a_rbreaket;
	      		
	      		logic=logic=='and'?'并且':'或者';
	      		if(i<conditionList.rowsLength()-1){
	      			infoStr+="&nbsp;<span style='color:#4338ff'>"+logic+"</span>";
	      		}
			}
			$('#realTimeConditionInfo').html("&nbsp;&nbsp;&nbsp;查询条件:&nbsp;&nbsp;"+infoStr);
		
		}
	</script>
</body>
