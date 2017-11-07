<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
  <style>
	  .operbtn_div{text-align: center;}
	  .table .td1{width:25%;text-align: right;}
  </style>
   <script type="text/javascript">
   var type='${type}';
   $(document).ready(function() {
   if(type==0){
	   $("#parent").hide();
   }
   if(type==1){
	   $("#parent").show();
   }
   });
   </script>
  <body>
 <!--  <div style="text-align:center">父类名称：&nbsp;&nbsp;&nbsp;<font color="red">${tCodeName}</font></div> -->
  
    <div>
     <form action="" method="post" id="codeAddForm">
     <div class="table_div">
	      <table class="table table-bordered">    
		      <tr id="parent">
		      	<td class="td1">父类名称: </td>
				<td><input type="text" value="${tCodeName}" disabled="disabled"></td>
		      </tr>  	     	
	        <tr>
				<td class="td1">代码：</td>
				<td><input type="text" name="code" ></td>
			</tr>
			<tr>
				<td class="td1">名称：</td>
				<td><input type="text" name="codeName" ></td>
			</tr>
			<tr>
				<td class="td1">简称：</td>
				<td><input type="text" name="codeAbr1" ></td>
			</tr>
			<tr>
				<td class="td1" >是否显示：</td>
				<td><input type='checkbox' onclick='checkBoxChange(this)' />
				<input type="hidden" name="yesPrv" value="0"/></td>
			</tr>
	      </table>
      </div>
      <input type="hidden" id="supCode" name="supCode" value="${tId}">
      <div class="btn_div">
        <button class="btn-style1" type="submit">保&nbsp;存</button>
      </div>
     </form>
    </div>
    
	 <script type="text/javascript"> 
		var tCode2='${tCode}';	
		
			
		 $(document).ready(function(){
			 $('#codeAddForm').validate({				
				 rules:{
						code:{
							required: true,	
							remote:{
								url: "<%=basePath%>sys/code/checkCodeOnly.do",
		                        type: "Post",
		                        data: {
		                            itemCode:function(){return $("#codeAddForm input[name='code']").val();},
									codeTable:tCode2
		                        }
							}
						},
						codeAbr1:{
							required: true,
						},
						codeName:{
							required: true
						}
					},
					messages: {
						code:{
							required:"*请输入代码",	
							remote:"该代码已经存在！"
						},
						codeAbr1:{
							required:"*请输入名称"
						},
						codeName:{
							required:"*请输入简称"
						}
					}
			 });			 
			 var options = {
				        success: function(result){
		    				openAlert(result.content);
		    				if(result.type == "success"){
		    					//重新加载这颗树
		    					loadTree(tCode2);	
		    					closeWindow();	    					
							}
						},
				        dataType:  'json',
				        data:{codeTable:tCode2},
				        url:'<%=basePath%>sys/code/saveCode.do', 
				    };  
				    $("#codeAddForm").ajaxForm(options);
				    
			});
		 function loadTree(tCode2){
				$.ajax({
					type:'post',
					url:'<%=basePath%>sys/code/getCodeList2.do',
					data:{tName:tCode2},
					success:function(result){
						if(result.type=="success"){
							var data = JSON.parse(result.content);
							var dictNodes = [];						
							for(var i in data){
								
								dictNodes.push({
						    		'id':data[i].code, 
						    		'pId':data[i].supCode, 
						    		'name':data[i].code+'  '+data[i].codeName,
						    		'fullName':data[i].codeAbr2,
						    		'codeName':data[i].codeName,
						    		'open':true,
						    		'isCommon':data[i].isCommon,
						    		'isStand':data[i].isStand
						    		});
					       	}
							var coderTreeSetting = {
						    		check: {
						    			enable: true,
						    			nocheckInherit: true	    			
						    		},
						    		data: {
						    			simpleData: {
						    				enable: true
						    			}
						    		},
						    		callback: {
						    			onClick: zTreeOnClick
						    		},view: {
						    			fontCss: setFontCss
						    		}					    		
								};
							coderTree = $.fn.zTree.init($("#coderTree"), coderTreeSetting, dictNodes);
							//loadSelectNodes();
						}
					}
				});
		 }
		 
		 function setFontCss(treeId, treeNode) {
			    return treeNode.isCommon == 1 ? {color:"blue"} : {};
		 };
		 
		 function checkBoxChange(obj){
		   		if(obj.checked==true){
					$('#codeAddForm input[name="yesPrv"]').val(1);
				}else{
					$('#codeAddForm input[name="yesPrv"]').val(0);
				}
		   	}
	
	 </script>   
  </body>
