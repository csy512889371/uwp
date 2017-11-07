<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>

<body>
  <div class="oper_menu">
	<auth:auth ifAllGranted="dept:save">
	    <button class="btn-style4" onclick="deptMoveUp()"><i class="icon-arrow-up"></i> 上移</button>
		<button class="btn-style4" onclick="deptMoveDown()"><i class="icon-arrow-down"></i> 下移	</button>
		<button class="btn-style4" onclick="saveDeptSort()"><i class="icon-print"></i> 保存排序</button>
	</auth:auth>
	<auth:auth ifAllGranted="dept:add">
	    <button class="btn-style4" onclick="deptAddPage()"><i class="icon-plus"></i> 新增</button>
	</auth:auth>
	<auth:auth ifAllGranted="dept:edit">
		<button class="btn-style4" onclick="updateDeptPage()"><i class="icon-pencil"></i> 编辑</button>
	</auth:auth>
	<auth:auth ifAllGranted="dept:delete">
		<button class="btn-style4" onclick="deleteDept()"><i class="icon-trash"></i> 删除</button>
	</auth:auth>
  </div>
  <div>
	<table id="deptList" class="mmg">
      <tr>
        <th rowspan="" colspan=""></th>
      </tr>
    </table>
    <div id="deptpg" style="text-align: right;"></div>
  </div>
  <script type="text/javascript">
  var deptSelectedId = null;
  // 表头
  var deptCols = [
      { title:'管理权限名称', name:'deptname' ,width:100, align:'left', sortable: false, type: 'text'},
      { title:'权限编码', name:'code' ,width:300, align:'center', sortable: false, type: 'text'},
      { title:'管理权限描述', name:'description' ,width:100, align:'left', sortable: false, type: 'text'},
      { title:'拼音缩写', name:'depBspelling' ,width:100, align:'left', sortable: false, type: 'text'},

  ];
  
  /**
  * 窗口自适应
  */
  function adjustWindow(){
	if(deptList!=null) deptList.resize($(window).height()-200);
  }
  
  var deptList;
  
  $(document).ready(function(){
  	deptList = $('#deptList').mmGrid({
             height: commonTalbeHeight-20
             , cols: deptCols
             , url: '<%=basePath%>sys/dept/loadDeptList.do'
             , method: 'get'
             , remoteSort:true
             , sortName: 'SECUCODE'
             , sortStatus: 'asc'
             , root: 'result'
             , multiSelect: false
             , checkCol: true
             , fullWidthRows: true
             , autoLoad: false
             , showBackboard : false
             , plugins: [
                 $('#deptpg').mmPaginator({})
             ]
             , params: function(){
                 return {
                     secucode: $('#secucode').val()
                 };
             }
         });


  	     deptList.on('cellSelected', function(e, item, rowIndex, colIndex){
  	    	deptSelectedId = item.code;
             //查看
             if($(e.target).is('.btn-info, .btnPrice')){
                 e.stopPropagation();  //阻止事件冒泡
                 alert(JSON.stringify(item));
             }
         }).load({page: 1});
  });
	function searchList(page){
		if(!page){
			page=1;
		}
		deptList.load({pageNumber:page});
	}
  /**
   * 跳转到管理权限新增页面
   */
  function deptAddPage(){
	  openWindow('新增管理权限','sys/dept/deptAddPage.do',1,400,220);
  }
  
  /**
   * 检查是否选中记录
   */
  function checkSelected(){
		var rows = deptList.selectedRows();
		var item = rows[0];
		if(typeof(item)=='undefined'){
			return null;
		}
		return item;
  }
  
  /**
   * 跳转到管理权限更新页面
   */
  function updateDeptPage(){
		var item = checkSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		openWindow('修改管理权限','sys/dept/deptUpdatePage.do?deptId='+item.code,1,400,220);
  }
  
  /**
   * 删除管理权限
   */
  function deleteDept(){
	  var item = checkSelected();
	  if(item==null){
		  openAlert('请选择一条记录！');
		  return;
	  }
	  openConfirm('你确定要删除该行记录吗?',function(){
	      $.ajax({
	          type :'post',
		      url  :'<%=basePath%>sys/dept/deleteDept.do',
		      data :{deptId : item.code},
		      success: function(data){
			      openAlert(data.content);
			      if(data.type == 'success'){
			          // 重新加载一次页面
				      $('#mainContent').load('<%=basePath%>sys/dept/deptIndex.do',{});
			      }
		      }
	      });	
      });
  }
  
  /**
   * 选中管理权限上移
   */
  function deptMoveUp(){
	  resetOrder(deptList,'up');
  }
  
  /**
   * 选中管理权限下移
   */
  function deptMoveDown(){
	  resetOrder(deptList,'down');
  }
  
  /**
   * 保存管理权限排序
   */
  function saveDeptSort(){
	  var deptObj = deptList.rows();
	  if(typeof(deptObj[0]) == "undefined" || deptObj[0] == null){
		  openAlert("无需排序！");
		  return;
	  }
	  var sortNums = "";
	  var deptIds = "";
	  for (var int = 0; int < deptObj.length; int++) {
		  sortNums += int+1 + ",";
		  deptIds += deptObj[int].code + ",";
	  }
	  sortNums = sortNums.substring(0, sortNums.length-1);
	  deptIds = deptIds.substring(0, deptIds.length-1);
	  
	  $.ajax({
          type :'post',
	      url  :'<%=basePath%>sys/dept/saveDeptSort.do',
	      dataType: 'json',
	      data :{sortNums : sortNums,deptIds : deptIds},
	      success: function(data){
		      openAlert(data.content);
		      if(data.type == 'success'){
		          // 重新加载一次页面
			      $('#mainContent').load('<%=basePath%>sys/dept/deptIndex.do',{});
		      }
	      }
      });
  }
  </script>
</body>