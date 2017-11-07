<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<script src="<%=basePath%>js/jquery.validate.min.js"></script>
  <body>
    <div>
     <form action="<%=basePath%>sys/role/saveNewRole.do" method="post" id="roleAddForm">
     <div class="table_div">
      <table class="table table-bordered">
        <tr>
          <td>角色名：</td>
          <td><input class="required" type="text" name="rolename" id="rolename" maxlength="60"></td>
        </tr>
        <tr>
          <td>状态：</td>
          <td>
            <label class="radio inline"><input type="radio" name="state" id="state" value="true" checked="checked">有效</label>
            <label class="radio inline"><input type="radio" name="state" id="state" value="false">无效</label>
          </td>
        </tr>
        <!-- <tr>
          <td>权限标识：</td>
          <td><input class="required" type="text" name="code" id="code" maxlength="100"></td>
        </tr> -->
        <tr>
          <td>排序：</td>
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
	 $('#roleAddForm').validate({
		 rules:{
			 rolename:{
				required:true,
				maxlength:60
			 },
			 code:{
				 required:true,
				 maxlength:100
			 }
		 },
		 submitHandler: function(){
			 addNewRole();
		 }
	 });
 });
 function addNewRole(){
	 $('#roleAddForm').ajaxSubmit(function(data){
		 closeWindow();
		 openAlert(data.content);
		 if(data.type == 'success'){
			 $('#mainContent').load('<%=basePath%>sys/role/searchRole.do',{});
		 }
	 });
 }
 
 </script>   
  </body>
