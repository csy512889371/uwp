<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
  <body>
    <div>
     <form action="<%=basePath%>sys/role/updateRole.do" method="post" id="roleUpdateForm">
      <input type="hidden" id="id" name="id" value="${cfgUmsRole.id}">
      <div class="table_div">
      <table class="table table-bordered">
        <tr>
          <td>角色名：</td>
          <td><input class="required" type="text" name="rolename" id="rolename" maxlength="60" value="${cfgUmsRole.rolename}"></td>
        </tr>
        <tr>
          <td>状态：</td>
          <td>
            <label class="radio inline"><input type="radio" name="state" id="state" <c:if test="${cfgUmsRole.state == true}">checked</c:if> value="true">有效</label>
            <label class="radio inline"><input type="radio" name="state" id="state" <c:if test="${cfgUmsRole.state == false}">checked</c:if> value="false">无效</label>
          </td>
        </tr>
        <%-- <tr>
          <td>权限标识：</td>
          <td><input class="required" type="text" name="code" id="code" maxlength="100" value="${cfgUmsRole.code}"></td>
        </tr> --%>
        <tr>
          <td>排序：</td>
          <td><input class="required number" type="text" name="sort" id="sort" maxlength="4" value="${cfgUmsRole.sort}"></td>
        </tr>
        <tr>
          <td>备注：</td>
          <td><textarea rows="4" name="remark" id="remark" style="width:96%;" maxlength="300">${cfgUmsRole.remark}</textarea></td>
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
	 $('#roleUpdateForm').validate({
		 submitHandler: function(){
			 saveRoleInfo();
		 }
	 });
 });
 function saveRoleInfo(){	
	 $('#roleUpdateForm').ajaxSubmit(function(data){
		 closeWindow();
		 openAlert(data.content);
		 if(data.type == 'success'){
			 $('#mainContent').load('<%=basePath%>sys/role/searchRole.do',{});
		 }
	 });
 }
 </script> 
  </body>
