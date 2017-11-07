<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<!-- 单位列表  -->
<body>
	<div id="unitInfoDiv">
	<!-- 菜单 -->
	<div class="oper_menu">
		<auth:auth ifAllGranted="unitSort:sortSave">
		    <button class="btn-style4" onclick="unitMoveUp()">
				<i class="icon-arrow-up"></i> 上移
			</button>
			<button class="btn-style4" onclick="unitMoveDown()">
				<i class="icon-arrow-down"></i> 下移
			</button>
			<button class="btn-style4" onclick="saveUnitSort()">
				<i class="icon-print"></i> 保存排序
			</button>
		</auth:auth>
	</div>
		<table id="unitInfoList" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
	</div>
</body>
<script type="text/javascript">

	/**
	* 窗口自适应
	*/
	function adjustWindow(){
		$('#unitTreeTab').height($(window).height()-170);
		unitInfoList.resize($(window).height()-175);
	}

	var unitGroupCols = [
		{ title:'CODE', name:'CODE' ,width:10, align:'left', sortable: false, type: 'text',hidden:true},
		{ title:'序号', name:'ININO' ,width:5, align:'left', sortable: false, type: 'text',hidden:true},
		{ title:'b01HiberId', name:'b01HiberId' ,width:50, align:'left', sortable: false, type: 'text',hidden:true},
		{ title:'单位全称', name:'B0101' ,width:50, align:'left', sortable: false, type: 'text'},
	];
  	var type = '${type}';
  	var unitHiberId = '${unitHiberId}';
  	var unitId = '${unitId}';
  	var libraryId = '${libraryId}';
  	var dmcod = '${dmcod}';
  	var libId = '${libId}';
	var unitInfoList;
	$(document).ready(function(){
		if (type == '') {
			type = 'null';
		}
		unitInfoList = $('#unitInfoList').mmGrid({
			height: commonTalbeHeight+10
			, cols: unitGroupCols
			, url: '<%=basePath%>unit/unit/loadUnVulUnitList.do?type='+type+'&unitHiberId='+unitHiberId+'&libraryId='+libraryId
			, method: 'get'
			, remoteSort:true
			, root: 'result'
			, multiSelect: false
			, checkCol: true
			, fullWidthRows: true
			, autoLoad: false
			, showBackboard : false
			, plugins: [
				//$('#unitInfoPg').mmPaginator({})
			]
			, params: function(){
				return {
					 secucode: $('#secucode').val()
				};
			}
		});

		unitInfoList.on('cellSelected', function(e, item, rowIndex, colIndex){
			if($(e.target).is('.btn-info, .btnPrice')){
				e.stopPropagation();  //阻止事件冒泡
				alert(JSON.stringify(item));
			}
		}).load({page: 1});
	});

	/**
	 * 是否选中记录
	 */
	function checkSelected(){
		var rows = unitInfoList.selectedRows();
		var item = rows[0];
		if(typeof(item)=='undefined'){
			return null;
		}
		return item;
	}	
	/**
	 * 选中归口上移
	 */
	function unitMoveUp(){
	 resetOrder(unitInfoList,'up');
	}
	
	/**
	 * 选中归口下移
	 */
	function unitMoveDown(){
	 resetOrder(unitInfoList,'down');
	}
	//更新选中的节点名称
	/**
	 * 保存排序
	 */
	function saveUnitSort(){
		var unitInfoListObj = unitInfoList.rows();
		if(typeof(unitInfoListObj[0]) == "undefined" || unitInfoListObj[0] == null){
			openAlert("无需排序！");
			return;
		}
		openConfirm('你确定要保存排序吗?',function(){
			var sortNums = "";
			var ininos = "";
			var code = "";
			for (var i = 0; i < unitInfoListObj.length; i++) {
				sortNums += i+1 + ",";
				code += unitInfoListObj[i].b01HiberId + ",";
				ininos += unitInfoListObj[i].ININO + ",";
			}
			sortNums = sortNums.substring(0, sortNums.length-1);
			code = code.substring(0, code.length-1);
			ininos = ininos.substring(0, ininos.length-1);
			//alert("code:"+code);
			//alert("unitInfoIds:"+unitInfoIds);
			
			$.ajax({
				type :'post',
				url  :'<%=basePath%>unit/unit/unitSortSave.do',
				dataType: 'json',
				data : {codes:code,'type':type,'unitHiberId':unitHiberId},
				success: function(data){
					openAlert(data.content);
					if(data.type == 'success'){
						// 重新加载一次页面
						var libraId = $("#indexLibraryId option:selected").val();
						loadCode(libraId);
						$('#unitInfoDiv').load('<%=basePath%>unit/unit/unitSortInfoPage.do',{'type':type,'unitHiberId':unitHiberId,'unitId':unitId,'libraryId':libraryId,'dmcod':dmcod,'libId':libId});
					}
				}
			});
		});
	}	

</script>