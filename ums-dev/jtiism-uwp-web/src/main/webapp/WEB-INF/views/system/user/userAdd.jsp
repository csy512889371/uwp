<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
  <body>
    <div>
     <form action="<%=basePath%>sys/user/saveNewUser.do" method="post" id="userAddForm" name="userAddForm">
      <div class="table_div">
      <table class="table table-bordered">
        <tr>
          <td><span style="color:red;">*</span>帐号：</td>
          <td><input class="required" type="text" name="username" id="username" placeholder="请输入用户名" maxlength="50"></td>
        </tr>
        <tr>
        	<td>所属权限</td>
        	<td>
        		<select name="deptId">
        			<option value="">--请选择所属权限--</option>
        			<c:forEach items="${deptList}" var="dept">
        				<option value="${dept.code}">${dept.deptname}
        			</c:forEach>
        		</select>
        	</td>
        </tr>
        <!-- <tr>
          <td>密码：</td>
          <td><input type="password" name="password" id="password" placeholder="请输入密码"></td>
        </tr> -->
        <tr>
        <td><span style="color:red;">*</span>所属组:</td>
        <td>
          <input class="required" type="text" id="groupName" onclick="selectGroup()"  readonly="readonly">
          <input  type="hidden" name="groupId" id="groupId"  >
        </td>
        </tr>
        <tr>
          <td>状态：</td>
          <td>
            <label class="radio inline"><input type="radio" name="state" id="state" value="true" checked="checked">有效</label>
            <label class="radio inline"><input type="radio" name="state" id="state" value="false">无效</label>
          </td>
        </tr>
        <tr>
          <td><span style="color:red;">*</span>排序：</td>
          <td><input class="required number" type="text" name="sort" id="sort" maxlength="4"></td>
        </tr>
        <tr>
          <td>备注：</td>
          <td><textarea rows="4" name="remark" id="remark" style="width:96%;" maxlength="300"></textarea></td>
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
	 $('#userAddForm').validate({
		 rules:{
			 groupId:{
				 required:true
			 },
			 username:{
				 required:true
			 }
		 },
		 submitHandler: function(){
			 addNewUser();
		 }
	 });
 });
 function addNewUser(){
	$('#userAddForm').ajaxSubmit(function(data){
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

