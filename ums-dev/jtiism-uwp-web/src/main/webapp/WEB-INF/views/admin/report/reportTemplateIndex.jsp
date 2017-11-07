<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
.input_tdWidth {
	width: 75%;
}

.input_title {
	text-align: left;
	width: 16%;
}
</style>
<body>
<div style="width: 100%;text-align: center;">
	<div id="reportTemplateDiv" style="width: 77%; margin:0 auto; padding: 10px;">
		<table id="reportTemplateList" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div style="width: 99.8%; height: 30px; padding-top: 5px; padding-bottom: 5px; border: 1px solid #ccc;">
				<div class="left_btn">
					<button class="btn-style1" onclick="addReportTempl()">增加</button>
					<button class="btn-style4" onclick="deleteReportTempl()">删除</button>
					<input type="button" class="btn-style3" onclick="upReportTemplSeq()" value="上移"/>
			        <input type="button" class="btn-style3" onclick="downReportTemplSeq()" value="下移"/>
			        <input type="button" class="btn-style3" onclick="saveReportTemplSort()" value="保存排序"/>
				</div>
		</div>
	</div>
	<div id="reportTemplateFormDiv" style="width: 77%;padding: 10px; margin:0 auto;"></div>
</div>
	<script type="text/javascript">
  var reportTemplateList=null;
  var reportTemplateCols = [
        {title : '序号', name : 'inino' , width : 10, align : 'center', sortable : false, type: 'text',renderer: function(val,item){
    		return val;
    	}},
     	 {title : '模板名称', name : 'templateName' , width : 350, align : 'center', sortable : false, type: 'text',renderer: function(val,item){
     		return val;
     	}},
	   	 {title : '类别', name : 'templateType' , width : 50, align : 'center', sortable : false, type: 'text',renderer: function(val,item){
			 if("muster"==val){
				 return "名册类";
			 }else if("appointDismiss"==val){
				 return "调研表类";
			 }else if("statistics"==val){
				 return "统计类";
			 }
	 		return val;
	 	}},
     	 {title : '状态', name : 'isvalid' , width : 100, align : 'center', sortable : false, type: 'text',renderer: function(val,item){
     		 if(val){
     			 return "启用";
     		 }
     		return "不启用";
     	}},
     	 {title : '模板文件', name : 'template' , width : 50, align : 'center', sortable : false, type: 'text',renderer: function(val,item){
      		return "<button class='btn-style3' onclick='loadTemplate(\""+item.id+"\")'>下载</button>";
      	}},
     	 {title : 'id', name : 'id' , width : 10, align : 'center', sortable : false, type: 'text',hidden: true,renderer: function(val,item){
     		return val;
     	}}
       ];
  
	 function loadReportTemplateList(){
		 reportTemplateList = $('#reportTemplateList').mmGrid({
				height : 350,
				cols : reportTemplateCols,
				url : '<%=basePath%>report/engine/searchReportTemplateList.do',
				method : 'post',
				remoteSort : true,
				sortName : 'SECUCODE',
				sortStatus : 'asc',
				multiSelect : false,
				checkCol : true,
				fullWidthRows : true,
				autoLoad : false,
				showBackboard : false,
				plugins : [$('#pg').mmPaginator({})],
				params : function() {
					//如果这里有验证，在验证失败时返回false则不执行AJAX查询。
			        return {
						secucode: $('#secucode').val()
			        };
			    }
			});
			
		 reportTemplateList.on('cellSelected', function(e, item, rowIndex, colIndex){
				//点击选中记录，把记录值填写到页面中去
				$('#reportTemplateFormDiv').load('<%=basePath%>report/engine/reportTemplUpdatePage.do',{templateId:item.id});
			 	//事件冒泡:mmgrid 表格选中
		}).load({});
		reportTemplateList.on('loadSuccess', function(e, data){
			if (isChangeTabUrl) {
				reportTemplateList.select(0);
				var rows = reportTemplateList.selectedRows();
				var item = rows[0];
				if (typeof(item) != 'undefined') {
					$('#reportTemplateFormDiv').load('<%=basePath%>report/engine/reportTemplUpdatePage.do',{templateId:item.id});
				}else{
					$('#reportTemplateFormDiv').load('<%=basePath%>report/engine/reportTemplAddPage.do');
				}
			} else {
				var templateId = $("#templateId").val();
				if(typeof(templateId) != 'undefined'){
					reportTemplateList.select(function(item,index){
						if (typeof(item) != 'undefined') {
							if(item.id == templateId){
								 $('#reportTemplateFormDiv').load('<%=basePath%>report/engine/reportTemplUpdatePage.do',{templateId:item.id});
								return true;
							}
						}
					});
				}
			}
			isChangeTabUrl = false;
		});
	}
    $(document).ready(function(){
    	isChangeTabUrl = true;
    	loadReportTemplateList();
	});
    
    function addReportTempl(){
		$('#reportTemplateFormDiv').load('<%=basePath%>report/engine/reportTemplAddPage.do');
    }
    
    function deleteReportTempl(){
    	 var rows = reportTemplateList.selectedRows();
 	    var item = rows[0];
 	    if(typeof(item) == 'undefined'){
 		    openAlert('请选择一条记录！');
 		    return;
 	    }
 	    openConfirm('确定删除该记录？',function(){
 	        $.ajax({
 		        type: 'post',
 			    url: '<%=basePath%>report/engine/deleteReportTemplate.do',
 			    data: {templateId: item.id},
 			    success: function(result){
 			        openAlert(result.content);
 				    if(result.type == 'success'){
 				    	isChangeTabUrl = true;
 				    	loadReportTemplateList();
 				    }
 			    }
 		    });				
 	    });    	
    }
    
	function upReportTemplSeq(){
		 resetOrder(reportTemplateList,'up');
	}
	
	function downReportTemplSeq(){
		 resetOrder(reportTemplateList,'down');
	}
	
	function saveReportTemplSort(){
		if(reportTemplateList.rowsLength()<=1){
			 openAlert('无需排序！');
			    return;
		} 
		openConfirm('确定要保存排序吗?',function(){
			var templId='';
			var rows=reportTemplateList.rows();
			for(var i in rows){
				templId+=rows[i].id+",";
			 }
			 if(templId.length>0){
				 templId = templId.substring(0,templId.length-1);
			 }
			$.ajax({
				type:'post',
				url:'<%=basePath%>report/engine/sortReportTemplInino.do',
				data:{templIds:templId},
				success:function(result){
					openAlert(result.content);
					if(result.type=="success"){
						loadReportTemplateList();
					}
				}
			});
		});
	}
	
	function loadTemplate(id){
		 $.ajax({
		        type: 'post',
			    url: '<%=basePath%>report/engine/getReportTemplFilePath.do',
			    data: {templateId: id},
			    success: function(result){
				    if(result.type == 'success'){
				    	$('#loadfileNameOfCadreInfo').val(result.params);
		    		    $('#loadFileOfCadreInfo').submit();
				    }
			    }
		    });				
	}
    
	</script>
</body>