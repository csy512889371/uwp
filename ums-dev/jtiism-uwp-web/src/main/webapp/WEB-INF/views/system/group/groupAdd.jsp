<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
  <body>
    <div>
     <form action="<%=basePath%>sys/user/saveNewGroup.do" method="post" id="groupAddForm" name="groupAddForm">
      <div class="table_div">
      <table class="table table-bordered">
        <tr>
          <td><span style="color:red;">*</span>用户组：</td>
          <td><input class="required" type="text" name="groupName" id="groupName" placeholder="请输入分组户名" maxlength="100"></td>
        </tr>
         <tr>
          <td>用户组描述：</td>
          <td><input  type="text" name="groupDesc" id="groupDesc" maxlength="200"></td>
        </tr>
        <tr>
        <td><span style="color:red;">*</span>上级分组:</td>
        <td>
        <input type="text"  class="required" id="parentGroupName" onclick="selectGroup()" readonly="readonly">
        <input type="hidden" id="parentGroupId" name="parentGroup.Id" > 
        </td>
        </tr>
      </table>
      </div>
      <div class="btn_div">
        <button class="btn-style1" type="submit">保 存</button>
      </div>
     </form>
  </div>
  
    
 <script type="text/javascript">
 $().ready(function(){
	 $('#groupAddForm').validate({
		 rules:{
			 groupName:{
				 required:true
			 }
		 },
		 submitHandler: function(){
			 addNewGroup();
		 }
	 });
 });
 function addNewGroup(){
	$('#groupAddForm').ajaxSubmit(function(data){
    	openAlert(data.content);
    	if(data.type == "success"){
    		loadGroupList();
    	}
    	closeWindow();
	});
 }
 
 function selectGroup(){
	 openWindow('上级分组','sys/user/selectGroup.do',2,250,350,{inputCodeId:'parentGroupId',inputShowId:'parentGroupName',rootSelected:true});
 }
 </script>
  </body>

