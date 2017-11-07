<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
  
  <body>
    <form action="<%=basePath%>sys/dept/saveUpdateDept.do" id="deptUpdateForm" method="post">
	  <div style="width:96%;margin: auto;">
	      <table class="table table-bordered">
	        <tr>
	          <td style="text-align: right;"><span style="color:red;">*</span>管理权限名称</td>
	          <td><input class="required" id="deptname" name="deptname" type="text" value="${cmisDepartment.deptname}"  maxlength="100"></td>
	        </tr>
	        <tr>
	          <td style="text-align: right;">管理权限描述</td>
	          <td><input id="description" name="description" type="text" value="${cmisDepartment.description}" maxlength="200"></td>
	        </tr>
			<input id=code name="code" type="hidden" value="${cmisDepartment.code}">
	      </table>
      </div>
      <div style="float:right;margin-right: 6px;">
		<input type="submit" class="btn-style1" value="保存"/>
	  </div>
    </form>
    <script type="text/javascript">
    $().ready(function(){
   	    $('#deptUpdateForm').validate({
   	    	rules:{
   	    		deptname:{
   	    			remote:{
   	    			 	type:"post",
				        url:"<%=basePath%>sys/dept/isExistDeptName.do", 
				        datatype:'json',
				        data:{
				        	newDeptname: function() {return $("#deptname").val();},
				        	oldDeptname: function() {return '${cmisDepartment.deptname}';}
				        }
   	    			}
   	    		}
   	    	},
   	    	messages:{
   	    		deptname:{
   	    			remote:jQuery.format("该管理权限名称已存在"),
   	    		},
   	    	},
   		    submitHandler: function(){
   			    saveUpdateDept();
   		    }
   	    });
    });
    
    /**
     * 更新管理权限
     */
    function saveUpdateDept(){
   	    $('#deptUpdateForm').ajaxSubmit(function(data){
   		    closeWindow();
       	    openAlert(data.content);
       	    if(data.type == "success"){
       		    $('#mainContent').load('<%=basePath%>sys/dept/deptIndex.do',{});
       	    }
   	    });
    }
    </script>
  </body>