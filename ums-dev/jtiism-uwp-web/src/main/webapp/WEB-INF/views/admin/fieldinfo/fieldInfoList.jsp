<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
<style>
	#fieldInfoListDiv .input1 {margin-bottom: 0px;width: 150px;padding-left: 6px;}
	.divLeft {float: left;width: 25%;background-color: #f5f5f5;}
	.divRight {float: right;width: 75%;background-color: #f5f5f5;}
</style>   
<!-- 元数据管理页面 -->
<body>
	<div>
		<div id="fieldInfoListDiv" class="oper_menu">
			<auth:auth ifAllGranted="field:add">
				<button class="btn-style4" onclick="addFieldInfo()"><i class="icon-plus"></i> 新增对应关系</button>
			</auth:auth>
			<auth:auth ifAllGranted="field:edit">
				<button class="btn-style4" onclick="updateFieldInfo()"><i class="icon-pencil"></i> 修改对应关系</button>
			</auth:auth>
			<auth:auth ifAllGranted="field:delete">
				<button class="btn-style4" onclick="deleteFieldInfo()"><i class="icon-trash"></i> 删除对应关系</button>
			</auth:auth>
			&nbsp;&nbsp;&nbsp;
			<auth:auth ifAllGranted="field:search">
				<select id="serachName" onchange="changeSearchName()">
					<option value="srcTable">数据库表</option>
					<option value="srcField">字段</option>
					<option value="desField">指标字段</option>
					<option value="codeTable">代码表</option>
				</select>
				<input type="text" id="searchValue" name="searchValue" placeholder="请输入数据库表名" class="input1">
				<button class="btn-style1" onclick="searchList()">查询</button>
			</auth:auth>
		</div>
		<!-- 活动列表 -->
		<div id="testdiv">
			<table id="fieldInfoList" class="mmg">
                <tr>
                    <th rowspan="" colspan=""></th>
                </tr>
            </table>
            <div id="pg" style="text-align: right;"></div>
		</div>
	</div>
	<script>
		/**
		* 窗口自适应
		*/
		function adjustWindow(){
			if(fieldInfoList!=null) fieldInfoList.resize($(window).height()-200);
		}
	
		var selectedId = null;
		 //表头
         var cols = [
             { title:'数据库表', name:'srcTable' ,width:20, align:'left', sortable: true, type: 'text'},
             { title:'字段', name:'srcField' ,width:20, align:'left', sortable: true, type: 'text'},
             { title:'指标字段', name:'desField' ,width:20, align:'left', sortable: true, type: 'text'},
             { title:'代码表', name:'codeTable' ,width:20, align:'left', sortable: true, type: 'text'},
             { title:'ID', name:'id' ,width:20, align:'left', sortable: true, type: 'text',hidden:true}
         ];
		
		function searchList(page,sort){
			if(!page){
				page=1;
			}
			var sname=$('#serachName option:selected').val();
			var svalue=$('#searchValue').val();
			fieldInfoList.load({serachName:sname,serachValue:svalue,pageNumber:page,sort:curSortName});
		}
		
		var fieldInfoList;
		$(document).ready(function(){
			fieldInfoList = $('#fieldInfoList').mmGrid({
                    height: commonTalbeHeight-20
                    , cols: cols
                    , url: '<%=basePath%>sys/fieldinfo/getFieldInfoList.do'
                    , method: 'get'
                    , remoteSort:true
                    , sortName: 'srcTable'
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

                fieldInfoList.on('cellSelected', function(e, item, rowIndex, colIndex){
                	selectedId = item.id;
                    //查看
                    if($(e.target).is('.btn-info, .btnPrice')){
                        e.stopPropagation();  //阻止事件冒泡
                        alert(JSON.stringify(item));
                    }
                }).load({page: 1});
		});
		/**
		*新增元数据
		*/
		function addFieldInfo(){
			openWindow('新增元数据','sys/fieldinfo/fieldInfoAdd.do',1,600,250);
		}
		/**
		*删除元数据
		*/
		function deleteFieldInfo(){
			var item = checkSelected();
			if(item==null){
				openAlert('请选择一条记录！');
				return;
			}
			$.ajax({
				url:'<%=basePath%>sys/fieldinfo/deleteFieldInfo.do',
				data:{fieldInfoId:item.id},
				type:'post',
				success:function(result){
					openAlert(result.content);
					if(result.type=='success'){
						$('#mainContent').load('<%=basePath%>sys/fieldinfo/fieldInfoList.do',{});
					}
				}
			});
		}
		/**
		*修改元数据
		*/
		function updateFieldInfo(){
			var item = checkSelected();
			if(item==null){
				openAlert('请选择一条记录！');
				return;
			}
			openWindow('修改元数据','sys/fieldinfo/fieldInfoUpdate.do?fieldInfoId='+item.id,1,600,250);
		}
		/**
		* 检验是否选择数据
		*/
		function checkSelected(){
			var rows = fieldInfoList.selectedRows();
			var item = rows[0];
			if(typeof(item)=='undefined'){
				return null;
			}
			return item;
		}
		
		function changeSearchName(){
			var text=$('#serachName option:selected').text();
			$('#searchValue').attr("placeholder","请输入"+text);
		}
	</script>
</body>
