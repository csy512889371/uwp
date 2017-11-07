<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<body>
	<div id="menuOperInput">
		<form action="" method="post" id="menOperInputForm">
			<table class="table table-bordered">
				<caption  id="menuOperTitle" align="top">查看</caption>
				<tr>
					<td width="40%"><span style="font-size:16px;color:red;">*</span>请输入操作名称：</td>
					<td><input type="text" id="opername" name="opername" disabled="disabled" maxlength="15" /></td>
				</tr>
				<tr>
					<td width="40%"><span style="font-size:16px;color:red;">*</span>请输入权限标识:</td>
					<td><input type="text" id="permission" name="permission" disabled="disabled" maxlength="100" />
					<input type="hidden" id="operId" name="id">
					<input type="hidden" name="menuid" value="${menuId}"></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: right;"><button id="menuOperSave" class="btn-style1" type="submit" disabled="disabled">保 存</button></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="display: inline;text-align: left;">
		<button class="btn-style4" onclick="newMenuOper()">新增操作</button>
		<button class="btn-style4" onclick="updateMenuOper()">修改操作</button>
		<button class="btn-style4" onclick="deleteMenuOper()">删除操作</button>
	</div>
	<!-- 菜单列表 -->
		<div style="margin-top:5px;">
			<table id="menuOperList" class="mmg">
				<tr>
					<th rowspan="" colspan=""></th>
				</tr>
			</table>
		</div>
	<script>
	var menuOperList;
	var menuId=${menuId};
	 var cols = [ { title:'操作名称', name:'opername' ,width:100, align:'left', sortable: false, type: 'text'},
			 { title:'权限标识', name:'permission' ,width:100, align:'left', sortable: false, type: 'text'},
			 { title:'ID', name:'id' ,width:100, align:'left', sortable: false, type: 'text',hidden:true}];
	$(document).ready(function(){
		clickOperAction(true,'查看');
		menuOperList = $('#menuOperList').mmGrid({
                    height: 300
                    , cols: cols
                    , url:'<%=basePath%>sys/menu/getMenuOperList.do?menuId='+menuId
                    , method: 'get'
                    , remoteSort:true
                    , sortName: 'topic'
                    , sortStatus: 'asc'
                    , root: 'result'
                    , multiSelect: false
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
           menuOperList.on('cellSelected', function(e, item, rowIndex, colIndex){
                    //查看
                    if($(e.target).is('.btn-info, .btnPrice')){
                        e.stopPropagation();  //阻止事件冒泡
                        alert(JSON.stringify(item));
                    }else if($(e.target).is('.btn-danger') && confirm('你确定要删除该行记录吗?')){
                        e.stopPropagation(); //阻止事件冒泡
                        converseList.removeRow(rowIndex);
                    }
                }).load();
         $('#menOperInputForm').validate({
         	rules:{
         		opername:{
         			required:true,
         			maxlength: 15
         		},
         		permission:{
         			required:true,
         			maxlength:100
         		}
         	},
         	messages:{
         	}
         });
         var options = {
		        success: function(result){
            		openAlert(result.content);
            		if(result.type=="success"){
            			menuOperList.load();//列表重新加载
            			clickOperAction(true,'查看');
						$('#opername').val('');
						$('#permission').val('');
						$('#operId').val('');
            		}
           		},
		        dataType: 'json',
		        url:'<%=basePath%>sys/menu/saveOrUpdateMenuOper.do',
	    };  
	    $('#menOperInputForm').ajaxForm(options);
                
	});
	
	function newMenuOper(){
		clickOperAction(false,'新增操作');
		$('#opername').val('');
		$('#permission').val('');
		$('#operId').val('');
	}
	function updateMenuOper(){
		var item=checkMenuOperSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		clickOperAction(false,'更改操作');
		
		$('#opername').val(item.opername);
		$('#permission').val(item.permission);
		$('#operId').val(item.id);
		
	}
	function deleteMenuOper(){
		var item=checkMenuOperSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		clickOperAction(true,'删除操作');
		$('#opername').val('');
		$('#permission').val('');
		$('#operId').val('');
		openConfirm('确定删除该操作项？',function(){
				$.ajax({
					type : 'post',
					url  : '<%=basePath%>sys/menu/deleteMenuOper.do',
					data : {menuOperId:item.id},
					success: function(result){
						openAlert(result.content);
						if(result.type == "success"){
							menuOperList.load();//列表重新加载
						}
					}
				});				
			});
	}
	
	function clickOperAction(disabled,title){
		$('#opername').attr('disabled',disabled);
		$('#permission').attr('disabled',disabled);
		$('#menuOperSave').attr('disabled',disabled);
		$('#menuOperTitle').text(title);
		
	}
	/**
		* 检验是否选择数据
		*/
	function checkMenuOperSelected(){
		var rows = menuOperList.selectedRows();
		var item = rows[0];
		if(typeof(item)=='undefined'){
			return null;
		}
		return item;
	}
	</script>
</body>
