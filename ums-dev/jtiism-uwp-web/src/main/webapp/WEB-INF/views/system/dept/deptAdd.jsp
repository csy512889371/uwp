<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
  
  <body>
    <form action="<%=basePath%>sys/dept/saveNewDept.do" id="deptAddForm" method="post">
	    <div style="width:96%;margin: auto;">
		      <table class="table table-bordered">
		        <tr>
		          <td><span style="color:red;">*</span>管理权限名称</td>
		          <td><input class="required" id="deptname" name="deptname" type="text" maxlength="100"></td>
		        </tr>
		        <tr>
		          <td>管理权限描述</td>
		          <td><input id="description" name="description" type="text" maxlength="200"></td>
		        </tr>
		        <tr>
		          <td><span style="color:red;">*</span>权限编码</td>
		          <td><input class="required" id="code" name="code" type="text" maxlength="100"></td>
		        </tr>
		      </table>
	      </div>
      <div style="float:right;margin-right: 6px;">
      		<input type="submit" class="btn-style1" value="保存"/>
	  </div>
    </form>
    <script type="text/javascript">
    $().ready(function(){
   	    $('#deptAddForm').validate({
   	    	rules:{
   	    		deptname:{
   	    			remote:{
   	    			 	type:"post",
				        url:"<%=basePath%>sys/dept/isExistDeptName.do", 
				        datatype:'json',
				        data:{
                            deptname: function() {return $("#deptname").val();}
				        }
   	    			}
   	    		},
                code:{
   	    			remote:{
   	    			 	type:"post",
				        url:"<%=basePath%>sys/dept/isCodeExists.do", 
				        datatype:'json',
				        data:{
                            code: function() {return $("#code").val();}
				        }
   	    			}
   	    		}
   	    	},
   	    	messages:{
   	    		deptname:{
   	    			remote:jQuery.format("该管理权限名称已存在"),
   	    		},
                code:{
   	    			remote:jQuery.format("该权限编码已存在"),
   	    		}
   	    	},
   		    submitHandler: function(){
   			    saveNewDept();
   		    }
   	    });
    });
    
    /**
     * 保存新增管理权限
     */
    function saveNewDept(){
   	    $('#deptAddForm').ajaxSubmit(function(data){
   		    closeWindow();
       	    openAlert(data.content);
       	    if(data.type == "success"){
       		    $('#mainContent').load('<%=basePath%>sys/dept/deptIndex.do',{});
       	    }
   	    });
    }
    </script>
  </body>