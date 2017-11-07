<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
	.table .td1{width:40%;text-align: right;}
</style>
<body>
<form action='' method="post" id="codeUpdateForm">
	<div class="table_div">
		<table class="table table-bordered">
			<tr>
				<td class="td1">代码：</td>
				<%-- 
				<c:choose>
					<c:when test="${dataDict.isStand==1}">
						<td><input type="text" name="code" value="${dataDict.code}"></td>
					</c:when>
					<c:otherwise>
						<td><input type="text" name="code" value="${dataDict.code}" readOnly="readOnly"></td>
					</c:otherwise>
				</c:choose>
				--%>
				<td><input type="text" name="code" value="${dataDict.code}" readOnly="readOnly"></td>
			</tr>
			<tr>
				<td class="td1">名称：</td>
				<td><input type="text" name="codeName" class="required" value="${dataDict.codeName}"></td>
			</tr>
			<tr>
				<td class="td1">简称：</td>
				<td><input type="text" name="codeAbr1" value="${dataDict.codeAbr1}"></td>
			</tr>
			<tr>
				<td class="td1" >是否显示：</td>
				<td><input id="checkbox_yesPry" type='checkbox' onclick='checkBoxChange(this)'
				<c:if test= "${dataDict.yesPrv==1}">checked</c:if> />
				<input type="hidden" name="yesPrv" value="${dataDict.yesPrv}"/></td>
			</tr>
		</table>
	</div>
	<div class="btn_div">
		<button class="btn-style1" type="submit">保 存</button>&nbsp;
		<!-- <button class="btn-style4" type="reset">重置</button> -->
	</div>
	</form>
	<script type="text/javascript">	
	var tCode2='${dataDict.dmGrp}';
	console.log(tCode);
	  $(document).ready(function(){
			 $('#codeUpdateForm').validate({				
				 rule:{					
					 codeName:{
							required: true,							
						},
			 		codeAbr1:{
							required: true,
						}
				 },
				 messages: {
						code:{
							required:"*请输入名称",							
						},
				 }
			 });
			 var options = {
				        success: function(result){
		    				openAlert(result.content);
		    				if(result.type == "success"){
		    					//重新加载这颗树
		    					loadTree(tCode);
		    					closeWindow();		    					
							}
						},
				        dataType:  'json',
				        data:{codeTable:tCode},
				        url:'<%=basePath%>sys/code/updateCode.do',
				    };
				    $("#codeUpdateForm").ajaxForm(options);
			});
		
		 //更新完加载树
		function loadTree(tCode){
				$.ajax({
					type:'post',
					url:'<%=basePath%>sys/code/getCodeList2.do',
					data:{tName:tCode},
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
						    		'isCommon':data[i].isCommon
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
							var nodeId='${dataDict.code}';//原节点id	
							//加载完成后选中原先节点
							var treeObj = $.fn.zTree.getZTreeObj("coderTree");
							var node = treeObj.getNodeByParam("id", nodeId, null);
							treeObj.selectNode(node);
							loadSelectNodes();
						}
					}
				});
		 }
		 
		function setFontCss(treeId, treeNode) {
			return treeNode.isCommon == 1 ? {color:"blue"} : {};
		};
		 
		 function checkBoxChange(obj){
		   		if(obj.checked==true){
					$('#codeUpdateForm input[name="yesPrv"]').val(1);
				}else{
					$('#codeUpdateForm input[name="yesPrv"]').val(0);
				}
		   	}
	</script>
</body>