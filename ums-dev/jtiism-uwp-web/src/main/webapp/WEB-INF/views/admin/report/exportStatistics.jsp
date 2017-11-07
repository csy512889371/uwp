<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>

  <body >
  <div style="text-align: center;">
			<table id="reportTemplateList" class="mmg">
				<tr>
					<th rowspan="" colspan=""></th>
				</tr>
			</table>
		<div  style="padding-top: 15px;text-align: center;">
			<input class="btn-style2"  style="width:100px;" type="button" onclick="outPutFrom('lib')" value="按子库输出">
			<input class="btn-style2"  style="width:100px;" type="button" onclick="outPutFrom('cut')" value="按剪切库输出">
		</div>
	</div>
    <script type="text/javascript">
    var ids='';
    var reportTemplateList=null;
    var reportTemplateCols = [
       	 {title : '模板名称', name : 'templateName' , width : 80, align : 'left', sortable : false, type: 'text',renderer: function(val,item){
       		return val;
       	}},
        {title : '类别', name : 'templateType' , width : 10, align : 'center', sortable : false, type: 'text',hidden: true,renderer: function(val,item){
       		return val;
       	}},
       	 {title : 'id', name : 'id' , width : 10, align : 'center', sortable : false, type: 'text',hidden: true,renderer: function(val,item){
       		return val;
       	}}
         ];
    
  	 function loadReportTemplateList(){
  		 reportTemplateList = $('#reportTemplateList').mmGrid({
  				height : 255,
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
  				params : function() {
  					//如果这里有验证，在验证失败时返回false则不执行AJAX查询。
  			        return {
  						secucode: $('#secucode').val()
  			        };
  			    }
  			});
  			
  		 reportTemplateList.on('cellSelected', function(e, item, rowIndex, colIndex){
  			 
  		}).load({isvalid:true,isStastistic:true});
  	 }
    
    $().ready(function(){
    	loadReportTemplateList();
    });
    
    function outPutFrom(type){
    	var rows = reportTemplateList.selectedRows();
    	var item = rows[0];
		if (typeof(item) == 'undefined') {
			openAlert("请选择导出模板");
			return ;
		}
		
		var title="";
		if("lib"==type){
			title="子库信息列表";
		}else if("cut"==type){
			title="剪切库信息列表";
		}
		ids='';
		openWindow3(title,'/report/engine/getStatisticsDataSource.do',2,280,385,{type:type},function(){
			if($.trim(ids)!=''){
				showWait('数据生成中，请稍候');
				 $.ajax({
						type:'post',
						url:'<%=basePath%>report/engine/createStatisticsByReportEngine.do',
						data:{libIds:ids,reportTemp:item.id,type:type},
						success : function(result) {
							closeWait();
							if (result.type == "success") {
								closeWindow(1);
				        		if(result.content!=null&&$.trim(result.content)!=''){
					        		$('#loadfileNameOfCadreInfo').val(result.content);
					    		    $('#loadFileOfCadreInfo').submit();
				        		}else{
				        			openAlert("文件未生成");
				        		}
				        	}else{
				        		openAlert(result.content);
				        	}
						}
					});
			}
			
		});
    }
    
    
    
    </script>
  </body>