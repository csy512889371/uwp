<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
  <body>
    <form action="<%=basePath%>sys/announce/saveNewAnnounce.do" id="sanAddForm" method="post">
	    <div style="width:96%;margin: auto;">
		      <table class="table table-bordered">
		        <tr>
		          <td style="text-align: right;vertical-align: middle;"><span style="color:red;">*</span>内容：</td>
					<td><textarea class="required" style="width: 310px;height: 95px;" id="saninf001" name="saninf001" type="text" maxlength="400"></textarea></td>
		        </tr>
				  <tr>
					  <td style="text-align: right;"><span style="color:red;">*</span>状态：</td>
					  <td>
						  <select style="width: 100px" id="saninf002" name="saninf002" lay-verify="saninf002">
							  <option value=1>启用</option>
							  <option value=0>不启用</option>
						  </select>
					  </td>
				  </tr>
		      </table>
	      </div>
      <div style="float:right;margin-right: 6px;">
      		<input type="submit" class="btn-style1" value="保存"/>
	  </div>
    </form>
    <script type="text/javascript">
    $().ready(function(){
   	    $('#sanAddForm').validate({

   		    submitHandler: function(){
   			    saveNewAnnounce();
   		    }
   	    });
    });
    
    /**
     * 保存新增公告
     */
    function saveNewAnnounce(){
		$('#sanAddForm').ajaxSubmit(function(data){
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