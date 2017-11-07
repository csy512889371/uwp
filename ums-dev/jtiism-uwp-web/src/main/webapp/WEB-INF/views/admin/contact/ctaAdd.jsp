<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
<head>
	<style>
		input{
			margin: 3px;
		}
	</style>
</head>
  <body>
  <form action="<%=basePath%>ent/contact/saveNewCta.do" id="ctaAddForm" method="post">
	  <div style="width:96%;margin: auto;">
		  <table class="table table-bordered">
			  <tr>
				  <td style="text-align: right;vertical-align: middle;"><span style="color:red;">*</span>反馈邮箱：</td>
				  <td><input class="required" id="ctainf001" name="ctainf001" type="text" maxlength="400"></td>
			  </tr>
			  <tr>
				  <td style="text-align: right;vertical-align: middle;"><span style="color:red;">*</span>联系电话：</td>
				  <td><input class="required" id="ctainf002" name="ctainf002" type="text" maxlength="400"></td>
			  </tr>
			  <tr>
				  <td style="text-align: right;vertical-align: middle;"><span style="color:red;">*</span>联系人：</td>
				  <td><input class="required" name="ctainf003" type="text" maxlength="400"></td>
			  </tr>
			  <tr>
				  <td style="text-align: right;vertical-align: middle;"><span style="color:red;">*</span>QQ：</td>
				  <td><input class="required" id="ctainf004" name="ctainf004" type="text" maxlength="400"></td>
			  </tr>
			  <tr>
				  <td style="text-align: right;vertical-align: middle;"><span style="color:red;">*</span>微信：</td>
				  <td><input class="required" id="ctainf005" name="ctainf005" type="text" maxlength="400"></td>
			  </tr>
			  <tr>
				  <td style="text-align: right;vertical-align: middle;"><span style="color:red;">*</span>地址：</td>
				  <td><input class="required" id="ctainf006" name="ctainf006" type="text" maxlength="400"></td>
			  </tr>
			  <tr>
				  <td style="text-align: right;"><span style="color:red;">*</span>状态：</td>
				  <td>
					  <select style="width: 100px" id="ctainf007" name="ctainf007">
						  <option value=1>启用</option>
						  <option value=0>不启用</option>
					  </select>
				  </td>
			  </tr>
			  <tr>
				  <%--<td colspan="1">地理位置</td>--%>
				  <td colspan="2">
					  <iframe id="ctaLocaionFrame" scrolling="no" style="height: 465px;width: 100%"
							  src="<%=basePath%>ent/contact/loadCtaLocation.do?fromType=add"
							  frameborder="0"></iframe>
					  <input id="ctaInf012" name="ctainf012" type="hidden"><%--经度--%>
					  <input id="ctaInf013" name="ctainf013" type="hidden"><%--纬度--%>
				  </td>
			  </tr>
		  </table>
		  <div style="float:right;margin-right: 6px;">
			  <input type="submit" class="btn-style1" value="保存"/>
		  </div>
	  </div>
  </form>
    <script type="text/javascript">
    $().ready(function(){
        jQuery.validator.addMethod("isCorrectPhoneNum", function (value, element) {
            var tm=/(^1[3|4|5|7|8]\d{9}$)|(^\d{3,4}-\d{7,8}$)|(^\d{7,8}$)|(^\d{3,4}-\d{7,8}-\d{1,4}$)|(^\d{7,8}-\d{1,4}$)/;
            return this.optional(element) || (tm.test(value));
        }, "请检查格式是否正确！");

   	    $('#ctaAddForm').validate({
			rules: {
                ctainf001: {
                    email : true,
                },
                ctainf002: {
                    isCorrectPhoneNum: true,
				},
				ctainf004: {
                    number: true,
				}
			},

            messages: {
                ctainf001: {
                    email: "请输入正确的邮箱地址！",
				},
                ctainf002: {
                    isCorrectPhoneNum: "请输入正确的联系号码！",
				},
				ctainf004: {
                    number: "请输入数字！",
				}
			},
   		    submitHandler: function(){
   			    saveNewCta();
   		    }
   	    });
    });
    
    /**
     * 保存新增联系我们信息
     */
    function saveNewCta(){
		$('#ctaAddForm').ajaxSubmit(function(data){
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