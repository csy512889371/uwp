<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %> 
<%
	String pt = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
 %>
	<body>
	<div>
	    <div id="versionPublicListDiv" class="oper_menu">
				<button class="btn-style4" onclick="addVersionPublic()"><i class="icon-plus"></i> 新版本发布</button>
				<button class="btn-style4" onclick="editVersionPublic()"><i class="icon-pencil"></i> 修改版本发布</button>
				<button class="btn-style4" onclick="delVersionPublic()"><i class="icon-trash"></i> 删除版本</button>
				<button class="btn-style4" onclick="expVersionPublic('')"><i class="icon-share"></i> 导出版本</button>
				<button class="btn-style4" onclick="addVersionScript()"><i class="icon-pencil"></i> 新增脚本</button>
		</div>
		<div>
			<table id="versionPublicList" class="mmg">
	        	<tr><th rowspan="" colspan=""></th></tr>
	      	</table>
	      	<div id="pg" style="text-align: right;"></div>
		</div>
	</div>
	<div style="display:none">
		<form method="post" id="loadFileOfVersionPublic" action="<%=pt%>sys/file/loadFile.do">
			<input name="fileName" id="loadfileNameOfVersionPublic">
			<input name="relFolderName" id="loadRelFolderNameOfInfo">
		</form>
	</div>
	<script type="text/javascript">
	
		/**
		* 窗口自适应
		*/
		function adjustWindow(){
			if(versionPublicList!=null) versionPublicList.resize($(window).height()-200);
		}
		var selectedId = null;
		//表头
		var cols = [
		    { title:'发布流水', name:'publicId' ,width:15, align:'center', sortable: false, type: 'text', hidden: false},
		    { title:'版本号', name:'versionNo' ,width:30, align:'center', sortable: false, type: 'text'},
		    { title:'版本类型', name:'pubType' ,width:20, align:'center', sortable: false, type: 'text', renderer : function(val) {
		    	if (val == '0') {
		    		return 'FZ';
		    	} else if (val == '1') {
		    		return 'FJ';
		    	} else if (val == '100') {
		    		return '桌面';
		    	}  else {
		    		return '';
		    	}
		    }},
		    { title:'发布日期', name:'pubDate' ,width:60, align:'center', sortable: false, type: 'text'},
		    { title:'发布说明', name:'pubDesc' ,width:200, align:'left', sortable: false, type: 'text'},
		    { title:'备注', name:'memo' ,width:200, align:'left', sortable: false, type: 'text'},
		];
		
		function searchList(page){
			if(!page) page = 1;
			versionPublicList.load({pageNumber:page});
		}
		
		var versionPublicList;
		$(document).ready(function(){
			versionPublicList = $('#versionPublicList').mmGrid({
		           height: commonTalbeHeight-20
		           , cols: cols
		           , url: '<%=basePath%>sys/version/getAllVersionPublicPage.do'
		           , method: 'get'
		           , remoteSort:true
		           , sortName: 'SECUCODE'
		           , sortStatus: 'asc'
		           , root: 'result'
		           , multiSelect: true
		           , checkCol: true
		           , fullWidthRows: true
		           , autoLoad: false
		           , showBackboard : false
		           , plugins: [
		               $('#pg').mmPaginator({})
		           ]
		           , params: function(){
		               //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
		             return {
		                 secucode: $('#secucode').val()
		             };
		           }
		       });
		       versionPublicList.on('cellSelected', function(e, item, rowIndex, colIndex){
		       	   selectedId = item.id;
		           //查看
		           if($(e.target).is('.btn-info, .btnPrice')){
		               e.stopPropagation();  //阻止事件冒泡
		               alert(JSON.stringify(item));
		           }
		       }).load({page: 1});
		});
		
		/**
		 * 新增
		 */
		function addVersionPublic() {
			openWindow('新版本发布','sys/version/addVersionPublic.do',1,'max','max');
		}
		
		/**
		* 检验是否选择数据
		*/
		function checkSelected(){
			var rows = versionPublicList.selectedRows();
			var item = rows[0];
			if(typeof(item)=='undefined'){
				return null;
			}
			return item;
		}
		/**
		 * 编辑
		 */
		function editVersionPublic() {
			var item = checkSelected();
			var rows = versionPublicList.selectedRows();
			if(item==null || rows.length > 1){
				openAlert('请选择一条记录！');
				return;
			}
			openWindow('新版本发布','sys/version/updateVersionPublic.do',1,'max','max',{publicId:item.publicId});
		}
		/**
		 * 删除
		 */
		function delVersionPublic(){
			var publicIds = "";
			var item = checkSelected();
			if(item==null){
				openAlert('请选择记录！');
				return;
			}
			var rows = versionPublicList.selectedRows();
			for (var i in rows) {
				publicIds += rows[i].publicId + ',';
			}
			openConfirm('你确定要删除该行记录吗?',function(){
				$.ajax({
					type : 'post',
					url  : '<%=basePath%>sys/version/delVersionPublic.do',
					data : {publicIds: publicIds},
					success : function(data){
						openAlert(data.content);
						searchList();
					}
				});
			});
		}
		function expVersionPublic(publicIds) {
			if (publicIds == '') {
				var item = checkSelected();
				if(item==null){
					openAlert('请选择记录！');
					return;
				}
				var rows = versionPublicList.selectedRows();
				for (var i in rows) {
					publicIds += rows[i].publicId + ',';
				}
			}
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>sys/version/expVersionPublic.do',
				data : {publicIds: publicIds},
				success : function(data){
					if(data.type == "success"){

                        $('#loadfileNameOfVersionPublic').val(data.content);
                        $('#loadRelFolderNameOfInfo').val('${setting:get().dynaReportTempDoc}');
                        $('#loadFileOfVersionPublic').submit();
					}
				}
			});
		}
		
		function addVersionScript(){
			openWindow('新增变更脚本','sys/version/addVersionScriptPage.do',1,600,450);
		}
	</script>   
	</body>