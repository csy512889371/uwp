<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
.btn-selected{background-color: #e7e7e7;}
</style>
<body>
  <div id="msgRemindListDiv">
    <div class="oper_menu" style="border-bottom: 1px solid #ccc;padding-bottom: 6px;">
      &nbsp;&nbsp;
	  <span>&nbsp;类别：</span>
	  <select id="msgType" name="msgType" style="text-align: center;width:110px;">
	    <option value="">--请选择--</option>
	    <option value="general">一般消息</option>
	    <option value="flow">流程消息</option>
	    <option value="job">事务提醒</option>
	  </select>
	  <span>&nbsp;&nbsp;标题：</span>
	  <input id="title" name="title" type="text">
	  <span>&nbsp;&nbsp;提醒时间范围：</span>
	  <input type="text" id="startTime" name="startTime" onfocus="getDate(this,'yyyy-MM-dd')" style="width: 100px;" readonly="readonly">
	  <span>至</span>
	  <input type="text" id="endTime" name="endTime" onfocus="getDate(this,'yyyy-MM-dd')" style="width: 100px;" readonly="readonly">
	  <button class="btn-style1" onclick="searchList()">查询</button>
	  <button class="btn-style2" id="clear" onclick="clearCondition();">重置</button>
	</div>
	<div class="oper_menu">
	  &nbsp;&nbsp;
	  <button class="btn-style4" onclick="markingMsg()">
	    <i class="icon-edit"></i> 标记为已读
	  </button>
	  <button class="btn-style4" id="unReadMsgBtn" onclick="unReadMsg()">
	    <i class="icon-cog"></i> 未读提醒
	  </button>
	  <button class="btn-style4" id="readMsgBtn" onclick="readMsg()">
	    <i class="icon-cog"></i> 已读提醒
	  </button>
	  <button class="btn-style4" id="" onclick="deleteMsg()">
	    <i class="icon-trash"></i> 删除
	  </button>
	</div>
	<!-- 消息列表 -->
   	<div>
	  <table id="msgRemindList" class="mmg">
        <tr>
          <th rowspan="" colspan=""></th>
        </tr>
      </table>
      <div id="pg" style="text-align: right;"></div>
	</div>
  </div>
  <script type="text/javascript">
  /**
  * 窗口自适应
  */
  function adjustWindow(){
	  if(msgRemindList!=null) msgRemindList.resize($(window).height()-240);
  }
  
  var msgStatus = null;
  //表头
  var msgRemindCols = [
      {title : '标题', name : 'title', width : 100, align : 'center', sortable : true, type : 'text', renderer : function(val, item) {
    	  var returnVal = '';
    	  if (item.skipUrl == null || item.skipUrl == '') {
    		  returnVal = '<span style="text-decoration:underline;color:#000;cursor:pointer;">'+val+'</span>';
    	  } else {
    		  returnVal = '<a href="javascript:void(0);" style="text-decoration:underline;color:#000;cursor:pointer;" onclick="loadMsgPage(\''+item.msgRemindId+'\')">'+val+'</a>';
    	  }
	      return returnVal;
      }},
      {title : '内容', name : 'contents', width : 100, align : 'center', sortable : true, type : 'text'},
      {title : '提醒时间', name : 'publictime', width : 100, align : 'center', sortable : false, type: 'text'},
      {title : '类别', name : 'msgType', width : 100, align : 'center', sortable : false, type : 'text', renderer : function(val) {
    	  if (val == 'general') {
    		  return '一般消息';
    	  } else if(val == 'flow') {
    		  return '流程消息';
    	  } else if (val == 'job') {
    		  return "事务提醒";
    	  }
      }},
      {title : '状态', name:'msgStatus' ,width : 100, align : 'center', sortable : false, type : 'text', renderer : function(val) {
    	  if (val == '0') {
    		  return '未读';
    	  } else {
    		  return '已读';
    	  }
      }},
      {title : 'msgRemindId', name : 'msgRemindId', width : 50, align : 'center', sortable : false, type : 'text', hidden : true}
  ];
  var msgRemindList;
  $(document).ready(function() {
	  msgRemindList = $('#msgRemindList').mmGrid({
		  height : commonTalbeHeight-50,
		  cols : msgRemindCols,
		  url : '<%=basePath%>sys/msgRemind/findMsgRemindList.do',
		  method : 'get',
		  remoteSort : true,
		  sortName : 'SECUCODE',
		  sortStatus : 'asc',
		  root : 'result',
		  multiSelect : true,
		  checkCol : true,
		  fullWidthRows : true,
		  autoLoad : false,
		  showBackboard : false,
		  plugins : [$('#pg').mmPaginator({})],
		  params : function() {
			  //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
			  return {
				  secucode : $('#secucode').val()
			  };
	      }
	  });
	  msgRemindList.on('cellSelected', function(e, item) {
		  //查看
		  if($(e.target).is('.btn-info, .btnPrice')){
			  e.stopPropagation();  //阻止事件冒泡
			  alert(JSON.stringify(item));
	      }
	  }).load({page : 1});
  });

  /**
   * 条件查询消息列表
   */
  function searchList(page) {
	  if (!page) {
		  page = 1;
	  }
	  
	  msgRemindList.load({pageNumber : page, msgType : $('#msgType').val(), title : $('#title').val(), startTime : $('#startTime').val(), endTime : $('#endTime').val(), msgStatus : msgStatus});
  }
  
  /**
   * 标记为已读
   */
  function markingMsg() {
	  var msgRemindObj = msgRemindList.selectedRows();
	  var msgIds = '';
	  if (msgRemindObj.length < 1) {
		  openAlert('请选择记录！');
		  return;
	  } else {
		  for (var i = 0; i < msgRemindObj.length; i++) {
			  msgIds += msgRemindObj[i].msgRemindId + ',';
		  }
		  msgIds = msgIds.substring(0, msgIds.length - 1);
		  openConfirm('是否标记为已读状态？', function() {
			  $.ajax({
				  type : 'post',
				  url : '<%=basePath%>sys/msgRemind/signMsgStatus.do',
				  data : {msgIds : msgIds},
				  success : function(data) {
					  openAlert(data.content);
					  if (data.type == 'success') {
						  // 重新加载数据
						  searchList();
						  $('#unReadMsgCount').text(data.params);
					  }
				  }
			  });
		  });
	  }
  }
  
  /**
   * 未读提醒
   */
  var unReadMsgBtn = 1;
  function unReadMsg(page) {
	  if (unReadMsgBtn == 1) {
		  unReadMsgBtn = 0;
		  msgStatus = 0;
  		  $('#unReadMsgBtn').addClass('btn-selected');
  		  readMsgBtn = 1;
  		  $('#readMsgBtn').removeClass('btn-selected');
  	  } else {
  		  unReadMsgBtn = 1;
  		  msgStatus = null;
  		  $('#unReadMsgBtn').removeClass('btn-selected');
  	  }
	  searchList();
  }
  
  /**
   * 已读提醒
   */
  var readMsgBtn = 1;
  function readMsg(page) {
	  if (readMsgBtn == 1) {
		  readMsgBtn = 0;
		  msgStatus = 1;
  		  $('#readMsgBtn').addClass('btn-selected');
  		  unReadMsgBtn = 1;
  		  $('#unReadMsgBtn').removeClass('btn-selected');
  	  } else {
  		  readMsgBtn = 1;
  		  msgStatus = null;
  		  $('#readMsgBtn').removeClass('btn-selected');
  	  }
	  searchList();
  }
  
  /**
   * 删除消息
   */
  function deleteMsg() {
	  var msgObj = msgRemindList.selectedRows();
	  var msgRemindIds = '';
	  if (msgObj.length < 1) {
		  openAlert('请选择记录！');
		  return;
	  } else {
		  for (var i = 0; i < msgObj.length; i++) {
			  msgRemindIds += msgObj[i].msgRemindId + ',';
		  }
		  msgRemindIds = msgRemindIds.substring(0, msgRemindIds.length - 1);
		  openConfirm('是否删除消息？',function() {
			  $.ajax({
				  type : 'post',
				  url : '<%=basePath%>sys/msgRemind/deleteMsg.do',
				  data : {msgRemindIds : msgRemindIds},
				  success : function(data) {
					  openAlert(data.content);
					  if (data.type == 'success') {
						  // 重新加载数据
						  searchList();
					  }
				  }
			  });
		  });
	  }
  }
  /**
   * 清除查询条件
   */
  function clearCondition() {
	  $('#title').val('');
	  $('#startTime').val('');
	  $('#endTime').val('');
	  $('#msgType').find('option:selected').text('--请选择--');
	  $('#msgType').find('option:selected').val('');
  }
  
  /**
   * 跳转到相应页面
   */
  function loadMsgPage(msgId){
	  var menuId = '';
		$.ajax({
			type: 'post',
			async: false,
			url: '<%=basePath%>sys/msgRemind/getMenuIdByMsg.do',
			data: {msgId : msgId},
			success: function(data) {
				menuId = data;
			}
		});
		var url = "sys/msgRemind/msgIndex.do?msgId="+msgId;
		url = encodeURI(url);
		window.location.href="<%=basePath%>index/subIndex.do?id="+menuId+"&url="+url;
	}
  </script>
</body>