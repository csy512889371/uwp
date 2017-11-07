<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
<!--谈话页面 -->
<style>
	#logListDiv .input1 {margin-bottom: 0px;width: 150px;padding-left: 6px;}
</style>   
  <body>
    <div>
		<div id="logListDiv" class="oper_menu">
			<auth:auth ifAllGranted="log:delete">
				<button class="btn-style4" onclick="deleteLog()"><i class="icon-trash"></i> 删除</button>
			</auth:auth>
			&nbsp;&nbsp;&nbsp;
			<auth:auth ifAllGranted="log:search">
					<select id="serachName" onchange="changeSearchName()">
						<option value="operateObj">操作对象</option>
						<option value="operator">操作员</option>
						<option value="ip">操作机IP</option>
						<option value="operateSet">操作集</option>
					</select>
					<input type="text" id="searchValue" name="searchValue" placeholder="请输入操作员" class="input1">
		     &nbsp;&nbsp;&nbsp;操作类型:
			<auth:auth ifAllGranted="log:search">
	  			<select id="serachOperaTypeValue" onchange="changeSerachOperaType()">
	  			    <option value="">全部</option>
					<option value="insert">insert</option>
					<option value="update">update</option>
					<option value="delete">delete</option>
				</select>
		   </auth:auth>
					<button class="btn-style1" onclick="searchList()">查询</button>
			</auth:auth>
		
		</div>
		<!-- 活动列表 -->
		<div>
			<table id="logList" class="mmg">
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
			if(logList!=null) logList.resize($(window).height()-200);
		}
	
		var selectedId = null;
         //表头
         var cols = [
				{ title:'操作时间', name:'operateDate' ,width:140, align:'center', sortable: true, type: 'text', renderer: function(val){
				    return val;
				}},
				{ title:'操作员', name:'operator' ,width:60, align:'left', sortable: true, type: 'text', renderer: function(val){
					return val;
				}},
	             { title:'操作集', name:'operateSet' ,width:120, align:'center', sortable: true, type: 'text', renderer: function(val){
	                 return val;
	             }},
	             { title:'操作行为', name:'operateType' ,width:70, align:'left', sortable: true, type: 'text', renderer: function(val){
	                 if(val=='insert'){
	                	 return '新增信息';
	                 }else if(val=='update'){
	                	 return '编辑信息';
	                 }else if(val=='delete'){
	                	 return '删除信息';
	                 }else{
	                	 return val;	 
	                 }
	            	 
	             }},
	             { title:'操作对象', name:'operateObj' ,width:80, align:'left', sortable: true, type: 'text', renderer: function(val){
	                 return val;
	             }},
	             //           { title:'操作机IP', name:'ip' ,width:60, align:'left', sortable: true, type: 'text', renderer: function(val){
	             //             return val;
	              //       }},
	             { title:'操作前的值', name:'oldVals' ,width:230, align:'center', sortable: true, type: 'text', renderer: function(val){
	                 return val;
	             }},
	             { title:'操作后的值', name:'newVals' ,width:230, align:'center', sortable: true, type: 'text', renderer: function(val){
	                 return val;
	             }},
	             { title:'操作', name:'CZ' ,width:200, align:'center', sortable: false, type: 'text', renderer: function(val){
	              	var oper='';
	              		oper += '<auth:auth ifAllGranted="log:detail">';
	              		oper += '<button class="btn-style4 detail">查看详细</button>';
	              		oper += '</auth:auth>';
	                	return oper;
	                  
	              }},
	              { title:'ID', name:'id' ,width:300, align:'center', sortable: false, type: 'text', hidden: true
	              }
         ];
	
		function searchList(page){
			if(!page){
				page=1;
			}
			var sname=$('#serachName option:selected').val();
			var svalue=$('#searchValue').val();
			var operaTypeValue=$('#serachOperaTypeValue option:selected').val();
			logList.load({serachName:sname,serachValue:svalue,serachOperaTypeValue:operaTypeValue,pageNumber:page,sort:curSortName});
		}
		
		var logList;
		$(document).ready(function(){
			logList = $('#logList').mmGrid({
                    height: commonTalbeHeight-20
                    , cols: cols
                    , url: '<%=basePath%>sys/log/getlogList.do'
                    , method: 'get'
                    , remoteSort:true
                    , sortName: 'operateDate'
                    , sortStatus: 'desc'
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
                      }
                    }
                });

                logList.on('cellSelected', function(e, item, rowIndex, colIndex){
                	selectedId = item.id;
                    //查看
                    if($(e.target).is('.btn-info, .btnPrice')){
                        e.stopPropagation();  //阻止事件冒泡
                        alert(JSON.stringify(item));
                    }else if($(e.target).is('.btn-danger') && confirm('你确定要删除该行记录吗?')){
                        e.stopPropagation(); //阻止事件冒泡
                        logList.removeRow(rowIndex);
                    }else if($(e.target).is('.detail')){
                    //console.log(item.id);
                    	toLogView(item.id);
                    }else if($(e.target).is('.export')){
                    	exportLog(item.id);
                    }
                }).load({page: 1});

		});
	
		/**
		*查看详情
		*/
		function toLogView(id){
			openWindow('日志详情','sys/log/logView.do?logId='+id);
		}
	
		
		/**
		* 删除日志
		*/
		function deleteLog(){
			var logIds = '';
			var rows = logList.selectedRows();
			var item = checkSelected();
			if(item==null){
				openAlert('请选择记录！');
				return;
			}
			for(var i in rows){
				logIds+=rows[i].id+",";
			}
			openConfirm('确定删除该日志信息？',function(){
			$.ajax({
       					type : 'post',
       					url  : '<%=basePath%>sys/log/deleteLog.do',
       					data : {logIds : logIds},
       					success: function(data){
       						openAlert(data.content);
       						if(data.type == 'success'){
       							//重新加载一次页面
           						$('#mainContent').load('<%=basePath%>sys/log/logList.do',{});
       						}
       					}
       				});				
			});
		}
		
		/**
		* 检验是否选择数据
		*/
		function checkSelected(){
			var rows = logList.selectedRows();
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
		
		function changeSerachOperaType(){
			var text=$('#serachName option:selected').text();
		}
		
	</script>
  </body>

