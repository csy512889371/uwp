<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
  <body>
    <div>
      <span style="position:relative;right:75px;">请选择快捷入口：</span>
      <span style="position:relative;left:75px;">已选的快捷入口：</span><br>
      <div style="border:1px solid #cccccc;width:230px;height:280px;overflow:auto;float:left;position:relative;left:10px;">
        <div class="ztopMenuTreeBackground left" >
		  <ul id="topMenuTree" class="ztree"></ul>
	    </div>
      </div>
      <div style="border:1px solid #cccccc;width:230px;height:280px;overflow:auto;float:right;position:relative;right:10px;">
        <ul id="checkResult" style="text-align:left"></ul>
      </div>
      <button class="btn-style1" type="button" onclick="selectQuickEntry()" style="position:relative;bottom:-7px;">确定</button>
    </div>
 <script type="text/javascript">
   $(document).ready(function(){
	   loadUserMenu();
	   loadSelectNodes();
   });
   var unitTree;
   function loadUserMenu(){
	   var node = [];
	   var setting = {
			check: {
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onCheck: zTreeOnCheck
			}
		};
	   //console.log(allMenu);
	   for(var i in allMenu){
		   if(typeof(allMenu[i].parent)=='undefined'){
			   node.push({id:allMenu[i].id,pId:0,name:allMenu[i].title});
		   }else{
			   node.push({id:allMenu[i].id,pId:allMenu[i].parent.id,name:allMenu[i].title});
		   }
	   }
	   $.fn.zTree.init($("#topMenuTree"), setting, node);
   }
   
   //为需要勾选的菜单打勾
   function loadSelectNodes(){
	   var treeObj = $.fn.zTree.getZTreeObj("topMenuTree");
	   for(var i in selectIds){
		   treeObj.checkNode(treeObj.getNodeByParam("id",selectIds[i],null),true,true);
	   }
	   showCheckMenu();
   }
   
   function selectQuickEntry(){
	   var treeObj = $.fn.zTree.getZTreeObj("topMenuTree");//树控件
	   var nodes = treeObj.getCheckedNodes(true);//获取所有选择的节点
	   var childNodes;
	   var checkedQuickIds = [];//清空数组
	   for(var i in nodes){
		   childNodes = treeObj.transformToArray(nodes[i]);//取得当前节点下面的所有子节点
		   if (childNodes.length == 1){//选取最底层节点（没有子节点的节点）
			   checkedQuickIds.push(nodes[i].id);
		   }
	   }
	   
	   if(checkedQuickIds.length > 6){
		   openAlert("温馨提示：自定义快捷入口最多6个！");
		   return;
	   }
	   
	   var kj = 1;
	   kjStr = '';
	   selectIds = [];
	   kjStr += '<font>快捷入口：</font>';
	   $('#kjDIv').empty();

	   for(var i in menuOne){
		   menuStr = '<div class="adm_child"><h3>'+menuOne[i].title+'</h3><div class="adm_c">';
		   var num = 0;//计算二级菜单个数，以配置相应的样式
		   for(var j in menuTwo){
			  if(menuOne[i].id==menuTwo[j].parent.id){
				  num++;
				  if(parseInt(threeLength[menuTwo[j].id])>3){
					  menuStr += '<dl class="two"><dt>'+menuTwo[j].title+'</dt><dd class="a'+num+'">';	
				  }else{
					  menuStr += '<dl><dt>'+menuTwo[j].title+'</dt><dd class="a'+num+'">';
				  }
		          for(var k in menuThree){
		        	  if(menuTwo[j].id==menuThree[k].parent.id){
		        		  menuStr += '<a href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><em><img src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].bigicon+'" onerror="\'this.src=<%=basePath%>resources/admin/img/menuicon/ico_b01.png\'"></em><span>'+menuThree[k].title+'</span></a>';
						  if($.inArray(menuThree[k].id,checkedQuickIds) != -1 && kj<7){
							  kj++;
							  kjStr += '<a href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><em><img src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].smallicon+'" onerror="\'this.src=<%=basePath%>resources/admin/img/menuicon/ico_m01.png\'"></em>'+menuThree[k].title+'</a>';
							  selectIds.push(menuThree[k].id);
						  }
					  }
				  }
		          menuStr += '</dd></dl>';
			  }
		   }
		   menuStr+='</div></div>';
		   $('#allMenuDiv').append(menuStr);
		}
		kjStr += addStr;
		$('#kjDIv').append(kjStr);
		
		var quickMenuIds = selectIds.join(",");
		$.ajax({
			type : 'post',
			url  : '<%=basePath%>sys/menu/saveQuickEntry.do',
			data : {quickMenuIds : quickMenuIds},
			success: function(result){
			}
		});
		
	    closeWindow();
   }
   
   function zTreeOnCheck(e, treeId, treeNode){
	   showCheckMenu();
   }
   
   //在右边显示左边已勾选的项
   function showCheckMenu(){
	   var treeObj = $.fn.zTree.getZTreeObj("topMenuTree");//树控件
	   var nodes = treeObj.getCheckedNodes(true);//获取所有选择的节点
	   var childNodes;
	   var checkNames = [];
	   var index = 0;
	   for(var i in nodes){
		   childNodes = treeObj.transformToArray(nodes[i]);//取得当前节点下面的所有子节点
		   if (childNodes.length == 1){//选取最底层节点（没有子节点的节点）
			   checkNames.push(nodes[i].name);
		   }
	   }
	   $('#checkResult').empty();
	   for(var i = 0; i < checkNames.length; i++){
		   index = i + 1;
		   $('#checkResult').append('<li>' + index + '.' + checkNames[i] + '</li>');
	   }
   }
 </script>
  </body>
