<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
  <body>
    <form action="<%=basePath%>sys/announce/saveUpdateSan.do" id="sanUpdateForm" method="post">
	  <div style="width:96%;margin: auto;">
	      <table class="table table-bordered">
	        <tr>
	          <td style="text-align: right;vertical-align: middle;"><span style="color:red;">*</span>内容：</td>
				<td><textarea class="required" style="width: 310px;height: 95px;" id="saninf001" name="saninf001" type="text" value="${announce.saninf001}"  maxlength="400">${announce.saninf001}</textarea></td>
	        </tr>
			  <tr>
				  <td style="text-align: right;"><span style="color:red;">*</span>状态：</td>
				  <td>
					  <select style="width: 100px" id="saninf002" name="saninf002" lay-verify="saninf002">
						  <option value=1  <c:if test="${announce.saninf002==1}">selected="selected" </c:if>>启用</option>
						  <option value=0  <c:if test="${announce.saninf002==0}">selected="selected" </c:if>>不启用</option>
					  </select>
				  </td>
			  </tr>
			<input id="saninf000" name="saninf000" type="hidden" value="${announce.saninf000}">
	      </table>
      </div>
      <div style="float:right;margin-right: 6px;">
		<input type="submit" class="btn-style1" value="保存"/>
	  </div>
    </form>
    <script type="text/javascript">
    $().ready(function(){
   	    $('#sanUpdateForm').validate({

   		    submitHandler: function(){
                saveUpdateSan();
   		    }
   	    });
    });
    
    /**
     * 更新公告
     */
    function saveUpdateSan(){
   	    $('#sanUpdateForm').ajaxSubmit(function(data){
   		    closeWindow();
       	    openAlert(data.content);
       	    if(data.type == "success"){
       	        //重新加载数据
                searchList();
       	    }
   	    });
    }
    </script>
  </body>