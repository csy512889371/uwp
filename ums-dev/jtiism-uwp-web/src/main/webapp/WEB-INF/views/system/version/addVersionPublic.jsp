<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 新增发布版本信息 -->
<style>
	#scriptVersionTableId{height: 340px;overflow: scroll;width: 98%;}
	.btn_div{text-align: center;}
</style>
<body>
	<form action="<%=basePath%>sys/version/saveVersionPublicAdd.do" method="post" id="addVersionPublicForm">
		<input type="hidden" name="selectedSeqnos" id="selectedSeqnos">
		<input type="hidden" name="scriptType" id="scriptType">
		<div class="table_div">
			<table class="table table-bordered">
				<tr>
					<td style="width: 7%;"><span style="font-size:15px;color:red;">*</span>版本号：</td>
					<td style="width: 26%;">
						<input type="text" name="versionNo" id="versionNo" maxlength="20" class="required">
					</td>
					<td style="width: 7%;"><span style="font-size:15px;color:red;">*</span>发布日期：</td>
					<td style="width: 26%;">
						<input type="text" name="pubDate" id="pubDate" onfocus="getDate(this,'yyyy-MM-dd HH:mm:ss')" class="required">
					</td>
					<td style="width: 7%;"><span style="font-size:15px;color:red;">*</span>版本类型：</td>
					<td style="width: 26%;">
						<select id="pubType" name="pubType" class="required" onchange="loadScriptVersion()">
							<option value="">---请选择---</option>
							<option value="0">发布一</option>
							<option value="1">发布二</option>
							<option value="100">发布三</option>
						</select>
					</td>
				</tr>
				<tr>
				</tr>
				<tr>
					<td>发布说明：</td>
					<td>	
						<textarea rows="3" cols="20" name="pubDesc" id="pubDesc" style="margin: 0px; width: 300px; height: 50px;" maxlength="500"></textarea>
					</td>
					<td>备注：</td>
					<td>
						<textarea rows="3" cols="20" name="memo" id="memo" style="margin: 0px; width: 300px; height: 50px;" maxlength="500"></textarea>
					</td>
					<td><span style="font-size:15px;color:red;">*</span>脚本类型：</td>
					<td>
						<select id="scriptTypeSelect" name="scriptTypeSelect" class="required" disabled="disabled">
							<option value="">---请选择---</option>
							<option value="0">WEB</option>
							<option value="1">桌面</option>
						</select>
					</td>
				</tr>
				<tr>
					<td style="vertical-align: middle;position: relative;">脚本版本：</td>
					<td colspan="5">
						<div id="scriptVersionTableId">
							<div style="margin-top: 150px;text-align:center;">请选择版本类型！</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="btn_div">
			<input type="submit" value="保存" class="btn-style1">
		</div>
	</form>
	<script>
		$(document).ready(function() {
			$('#scriptVersionTableId').height($(window).height()-240);
			 $("#selectAll").click(function() {
			        if ($(this).attr("checked")) {  
			            $("input[name=items]").each(function() {  
			                $(this).attr("checked", true);  
			            });  
			        } else {  
			            $("input[name=items]").each(function() {  
			                $(this).attr("checked", false);  
			            });  
			        }  
			    }); 
			$("#addVersionPublicForm").validate({
				 submitHandler: function() {
					 saveVesionPublic();
				 }
			 });
		});

		function changeSelectedState(){
				var seqnos = document.getElementsByName("selectedSeqno");
				for (i = 0; i < seqnos.length; i++) {
					if(document.getElementById("selectAll").checked) {//选中全部
						seqnos[i].checked = true;
					} else {
						seqnos[i].checked = false;
					}
				}
		}
		function saveVesionPublic(){
			var selectedSeqno = '';
			$("input[name=selectedSeqno]:checked").each(function(){
				selectedSeqno = selectedSeqno + this.value + ',';
			});
			if (selectedSeqno.length > 1) {
				selectedSeqno = selectedSeqno.substring(0,selectedSeqno.length - 1);
			}
			$('#selectedSeqnos').val(selectedSeqno);
			$('#addVersionPublicForm').ajaxSubmit(function(data){
				closeWindow();
		    	openAlert(data.content);
		    	expVersionPublic(data.params);//导出版本
				searchList();
			});
		}
		
		function loadScriptVersion(){
			var pubType = $('#pubType').val();
			if (pubType == null || pubType == '') {
				$('#scriptVersionTableId').html('<div style="margin-top: 150px;text-align:center;">请选择版本类型！</div>');
				$("#scriptTypeSelect option[value='']").attr("selected", true);
				$("#scriptType").val('');
				return ;
			}
			if (pubType < 100) {//WEB
				$("#scriptTypeSelect option[value='0']").attr("selected", true);
			} else {//桌面
				$("#scriptTypeSelect option[value='1']").attr("selected", true);
			}
			$("#scriptType").val($('#scriptTypeSelect').val());
			$('#scriptVersionTableId').html('');
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>sys/version/getScriptVersionList.do',
				data : {pubType : pubType},
				success : function(data){
					var tb = '';
					tb = tb + '<table style="width:98.5%;border:1px solid #CCCCCC;">';
					tb = tb + '<tr style="height:30px;">';
					tb = tb + '<td style="width:6%;"><label><input type="checkbox" id="selectAll" name="selectAll" onclick="changeSelectedState()"> </label></td>';
					tb = tb + '<td style="width:5%;"><B>序列</B></td>';
					tb = tb + '<td style="width:48%;"><B>变更说明</B></td>';
					tb = tb + '<td style="width:10%;"><B>变更时间</B></td>';
					tb = tb + '<td style="width:6%;"><B>变更类型</B></td>';
					//tb = tb + '<td style="width:10%;"><B>SVN版本号</B></td>';
					tb = tb + '<td style="width:5%;"><B>开发库</B></td>';
					tb = tb + '<td style="width:5%;"><B>183库</B></td>';
					tb = tb + '<td style="width:5%;"><B>市组库</B></td>';
					tb = tb + '<td style="width:5%;"><B>省组库</B></td>';
					tb = tb + '<td style="width:5%;"><B>测试库</B></td>';
					//tb = tb + '<td><B>PDM结构维护</td>';
					tb = tb + '</tr>';
					for(var i in data){
						var chnType = data[i].chnType;
						var devdbUp = data[i].devdbUp;
						var demodbUp = data[i].demodbUp;
						var fzdbUp = data[i].fzdbUp;
						var fjdbUp = data[i].fjdbUp;
						var testdbUp = data[i].testdbUp;
						
						if (chnType == '0') {
							chnType = '结构';
				    	} else if (chnType == '1') {
				    		chnType = '数据';
				    	} else {
				    		chnType = '';
				    	}
						if (devdbUp == '0') {
							devdbUp = '未更新';
				    	} else if (devdbUp == '1') {
				    		devdbUp = '已更新';
				    	} else {
				    		devdbUp = '';
				    	}
						if (demodbUp == '0') {
							demodbUp = '未更新';
				    	} else if (demodbUp == '1') {
				    		demodbUp = '已更新';
				    	} else {
				    		demodbUp = '';
				    	}
						if (fzdbUp == '0') {
							fzdbUp = '未更新';
				    	} else if (fzdbUp == '1') {
				    		fzdbUp = '已更新';
				    	} else {
				    		fzdbUp = '';
				    	}
						if (fjdbUp == '0') {
							fjdbUp = '未更新';
				    	} else if (fjdbUp == '1') {
				    		fjdbUp = '已更新';
				    	} else {
				    		fjdbUp = '';
				    	}
						if (testdbUp == '0') {
							testdbUp = '未更新';
				    	} else if (fjdbUp == '1') {
				    		testdbUp = '已更新';
				    	} else {
				    		testdbUp = '';
				    	}
						
						tb = tb + '<tr style="height:30px;">';
						tb = tb + '<td><label><input type="checkbox" name="selectedSeqno" id="selectedSeqno" value="'+data[i].seqno+'"></label></td>';
						tb = tb + '<td><label><a style="color:blue;text-decoration:underline;width:100%;" href="javascript:getChnScript('+data[i].seqno+')">  ' + data[i].seqno + '  </a></label></td>';
						tb = tb + '<td>' + data[i].chnDesc + '</td>';
						tb = tb + '<td>' + data[i].chnTime + '</td>';
						tb = tb + '<td>' + chnType + '</td>';
						//tb = tb + '<td>' + data[i].svnVerno + '</td>';
						tb = tb + '<td>' + devdbUp + '</td>';
						tb = tb + '<td>' + demodbUp + '</td>';
						tb = tb + '<td>' + fzdbUp + '</td>';
						tb = tb + '<td>' + fjdbUp + '</td>';
						tb = tb + '<td>' + testdbUp + '</td>';
						//tb = tb + '<td>' + data[i].pdmUp + '</td>';
						tb = tb + '</tr>';
					}
					tb = tb + '</table>';
					$('#scriptVersionTableId').html(tb);
				}
				
			});
		}
		function getChnScript(seqNo) {
			var scriptType = $('#scriptType').val();
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>sys/version/getChnScript.do',
				data:{seqno : seqNo,scriptType : scriptType},
				success : function(data){
					$('#alert_dialog_content').html('<div style= "text-align:left;">'+data.params+'</div>');
				    $("#alert_dialog").dialog({
				        resizable: false,
				        draggable: false,
				        height: 400,
				        width: 700,
				        modal: true,
				        buttons: {
				            "确定": function () {
				                $(this).dialog("close");
				                $('#alert_dialog_content').html('');
				            }
				        }
				    });
				}
			});
		}
		
	</script>
</body>