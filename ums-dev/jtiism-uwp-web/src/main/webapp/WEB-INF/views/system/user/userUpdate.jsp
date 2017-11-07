<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
  
  <body>
    <div>
     <form action="<%=basePath%>sys/user/updateUser.do" method="post" id="userUpdateForm">
      <input type="hidden" id="id" name="id" value="${cfgUmsUser.id}">
      
      <div class="table_div">
      <table class="table table-bordered">
        <tr>
          <td>帐号：</td>
          <td><input class="required" type="text" id="username" name="username" maxlength="50" value="${cfgUmsUser.username}"></td>
        </tr>
        <tr>
        	<td>归属所属权限</td>
        	<td>
        		<select name="deptId">
        			<option value="">--请选择所属权限--</option>
        			<c:forEach items="${deptList}" var="dept">
        				<option value="${dept.code}" <c:if test="${dept.code == selectDeptId}">selected="selected"</c:if>>${dept.deptname}
        			</c:forEach>
        		</select>
        	</td>
        </tr>
         <tr>
	        <td><span style="color:red;">*</span>所属组:</td>
	        <td>
	          <input class="required" type="text" id="groupName" onclick="selectGroup()" value="${cfgUmsUser.cfgUmsGroup.groupName}"  readonly="readonly">
	          <input  type="hidden" name="groupId" id="groupId" value="${cfgUmsUser.cfgUmsGroup.id}"  >
	        </td>
        </tr>
        <tr>
        <tr>
          <td>状态：</td>
          <td>
            <label class="radio inline"><input type="radio" name="state" id="state" <c:if test="${cfgUmsUser.state == true}">checked</c:if> value='1'>有效</label>
            <label class="radio inline"><input type="radio" name="state" id="state" <c:if test="${cfgUmsUser.state == false}">checked</c:if> value="0">无效</label>
          </td>
        </tr>
        <tr>
          <td>排序：</td>
          <td><input class="required number" type="text" id="sort" name="sort" maxlength="4" value="${cfgUmsUser.sort}"></td>
        </tr>
        <tr>
          <td>备注：</td>
          <td><textarea rows="4" id="remark" name="remark" style="width:96%;" maxlength="300">${cfgUmsUser.remark}</textarea></td>
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
	$('#userUpdateForm').validate({
		submitHandler: function(){
			saveUserInfo();
		}
	});
});
function saveUserInfo(){
	$('#userUpdateForm').ajaxSubmit(function(data){
		closeWindow();
   	    openAlert(data.content);
   	    if(data.type == "success"){
   		    $('#mainContent').load('<%=basePath%>sys/user/searchUser.do',{});
   	    }
	});
}

function selectGroup(){
	 openWindow('用户组','sys/user/selectGroup.do',2,250,350,{inputCodeId:'groupId',inputShowId:'groupName',rootSelected:false});
}
</script>
  </body>
