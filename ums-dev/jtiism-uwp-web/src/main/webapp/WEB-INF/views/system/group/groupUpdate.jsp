<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
  
  <body>
    <div>
     <form action="<%=basePath%>sys/user/updateGroup.do" method="post" id="groupUpdateForm">
      <input type="hidden" id="groupId" name="id" value="${group.id}">
       <input type="hidden"  name="seqno" value="${group.seqno}">
      <div class="table_div">
      <table class="table table-bordered">
        <tr>
           <td><span style="color:red;">*</span>用户组名：</td>
          <td><input class="required" type="text" name="groupName" id="groupName" placeholder="请输入分组户名" maxlength="100" value="${group.groupName }"></td>
        </tr>
        <tr>
          <td>用户组描述：</td>
          <td><input  type="text"  name="groupDesc" id="groupDesc"  maxlength="200" value="${group.groupDesc}"></td>
        </tr>
        <tr>
       	<td><span style="color:red;">*</span>上级分组:</td>
        <td>
        <input type="text"  class="required" id="parentGroupName" onclick="selectGroup()" readonly="readonly" value="${group.parentGroup.groupName }">
        <input type="hidden" id="parentGroupId" name="parentGroup.Id" value="${group.parentGroup.id}"> 
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
	$('#groupUpdateForm').validate({
		submitHandler: function(){
			updateGroupInfo();
		}
	});
});
function updateGroupInfo(){
	$('#groupUpdateForm').ajaxSubmit(function(data){
		closeWindow();
   	    openAlert(data.content);
   	    if(data.type == "success"){
   	    	loadGroupList();
   	    }
	});
}

function selectGroup(){
	 openWindow('上级分组','sys/user/selectGroup.do',2,250,350,{inputCodeId:'parentGroupId',inputShowId:'parentGroupName',filterIds:$('#groupId').val(),rootSelected:true});
}
</script>
  </body>
