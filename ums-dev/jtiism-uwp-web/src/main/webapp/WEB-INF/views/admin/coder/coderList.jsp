<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
<!-- 数据字典页面 -->
<body>
	<div>
		<div class="oper_menu">
			<auth:auth ifAllGranted="coder:add">
				<button class="btn-style4" onclick="addCoder()"><i class="icon-plus"></i> 新增常量字段</button>
			</auth:auth>
			<auth:auth ifAllGranted="coder:edit">
				<button class="btn-style4" onclick="updateCoder()"><i class="icon-pencil"></i> 修改常量字段</button>
			</auth:auth>
			<auth:auth ifAllGranted="coder:delete">
				<button class="btn-style4" onclick="deleteCoder()"><i class="icon-trash"></i> 删除常量字段</button>
			</auth:auth>
			&nbsp;&nbsp;&nbsp;
			<auth:auth ifAllGranted="coder:search">
				<input type="text" id="tab_Name" name="tab_Name" placeholder="请输入表名" style="margin-bottom: 0px;height:18px;width:180px;">
				<button class="btn-style1" onclick="searchList()">查询</button>
			</auth:auth>
		</div>
		<!-- 活动列表 -->
		<div id="testdiv">
			<table id="coderList" class="mmg">
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
			if(coderList!=null) coderList.resize($(window).height()-200);
		}
	
		var selectedId = null;
		 //表头
         var cols = [
             { title:'表名', name:'tabName' ,width:80, align:'left', sortable: true, type: 'text'},
             { title:'字段名', name:'colName' ,width:20, align:'left', sortable: true, type: 'text'},
             { title:'字段描述', name:'colDesc' ,width:20, align:'left', sortable: false, type: 'text'},
             { title:'字段值', name:'val' ,width:20, align:'left', sortable: false, type: 'text'},
             { title:'值描述', name:'valDesc' ,width:20, align:'left', sortable: false, type: 'text'},
             { title:'值简拼', name:'codeSpelling' ,width:20, align:'left', sortable: false, type: 'text'},
             { title:'状态', name:'state' ,width:20, align:'left', sortable: false, type: 'text'},
             { title:'状态变更时间', name:'stateChntime' ,width:20, align:'left', sortable: true, type: 'text'},
             { title:'ID', name:'id' ,width:20, align:'left', sortable: false, type: 'text',hidden:true}
         ];
	
		function searchList(page){
			if(!page){
				page=1;
			}
			coderList.load({tabName:$('#tab_Name').val(), pageNumber:page,sort:curSortName});
		}
		
		var coderList;
		$(document).ready(function(){
			coderList = $('#coderList').mmGrid({
                    height: commonTalbeHeight-20
                    , cols: cols
                    , url: '<%=basePath%>sys/coder/getCoderList.do'
                    , method: 'get'
                    , remoteSort:true
                    , sortName: 'tabName'
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

                coderList.on('cellSelected', function(e, item, rowIndex, colIndex){
                	selectedId = item.id;
                    //查看
                    if($(e.target).is('.btn-info, .btnPrice')){
                        e.stopPropagation();  //阻止事件冒泡
                        alert(JSON.stringify(item));
                    }
                }).load({page: 1});
		});
		/**
		*新增常量字段
		*/
		function addCoder(){
			openWindow('新增常量字段','sys/coder/coderAdd.do',1,600,300);
		}
		/**
		*更新常量字段
		*/
		function updateCoder(){
			var item = checkSelected();
			if(item==null){
				openAlert('请选择一条记录！');
				return;
			}
			openWindow('更新常量字段','sys/coder/coderUpdate.do?coderId='+item.id,1,600,300);
		}
		/**
		*删除常量字段
		*/
		function deleteCoder(){
			var item = checkSelected();
			if(item==null){
				openAlert('请选择一条记录！');
				return;
			}
			openConfirm('确定删除该常量字段信息？！',function(){
				$.ajax({
					type : 'post',
					url  : '<%=basePath%>sys/coder/deleteCoder.do',
					data : {coderId:item.id},
					success: function(result){
						openAlert(result.content);
						if(result.type == "success"){
							//重新加载一次页面
    						$('#mainContent').load('<%=basePath%>sys/coder/coderList.do',{});
						}
					}
				});				
			});
		}
		/**
		* 检验是否选择数据
		*/
		function checkSelected(){
			var rows = coderList.selectedRows();
			var item = rows[0];
			if(typeof(item)=='undefined'){
				return null;
			}
			return item;
		}
		
	</script>
</body>
