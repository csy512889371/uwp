<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
<body>
	<div class="oper_menu">
		<div style="display: inline;">
			<auth:auth ifAllGranted="codeCache:update">
				<button class="btn-style4" onclick="updateCache()"><i class="icon-pencil"></i> 更新缓存</button>
			</auth:auth>
			<auth:auth ifAllGranted="codeCache:updateAll">
				<button class="btn-style4" onclick="updateAllCache()"><i class="icon-pencil"></i>全部更新</button>
			</auth:auth>
		</div>
		<div style="display: inline;">&nbsp;&nbsp;&nbsp;&nbsp;</div>
	</div>
		<!-- 活动列表 -->
		<div>
			<table id="codeTableInfoList" class="mmg">
                <tr>
                    <th rowspan="" colspan=""></th>
                </tr>
            </table>
            <div id="pg" style="text-align: right;"></div>
		</div>
	<div id="loading" style="display: none;"></div>
	<div id="model" style="display: none;background-color: #f5f5f5;height:30px;width:200px;"> 
	<font size="5px" > &nbsp;&nbsp;&nbsp;更新中。。。</font></div> 
	<script >
		/**
		* 窗口自适应
		*/
		function adjustWindow(){
			if(codeTableInfoList!=null) codeTableInfoList.resize($(window).height()-180);
		}
	
		var codeTableInfoList;
		var cols = [
             { title:'对应标准代码表表名', name:'CODETABLE' ,width:760, align:'left', sortable: true, type: 'text'}
          ];
          var codeTable_item=[];
     	<c:forEach var="codetable" items="${codeTables}" varStatus="status"> 
     	if('${not empty fn:trim(codetable)}'=='true'){
     		codeTable_item.push({'CODETABLE':'${codetable}'});
     		}
   			
		</c:forEach> 
          $(document).ready(function(){
			var name = $('#actName').val();
			codeTableInfoList = $('#codeTableInfoList').mmGrid({
                    height: commonTalbeHeight
                    , cols: cols
                    , method: 'get'	
                    , remoteSort:true
                    , items: codeTable_item
                    , sortName: 'CODETABLE'
                    , sortStatus: 'asc'
                    , multiSelect: true
                    , fullWidthRows: true
                    , checkCol: true
                    , autoLoad: false
                    , showBackboard : false
                    , plugins: [
                        //$('#pg').mmPaginator({})
                    ]
                    , params: function(){
                        //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
                      return {
                          secucode: $('#secucode').val()
                      };
                    }
                });


                codeTableInfoList.on('cellSelected', function(e, item, rowIndex, colIndex){
                      if($(e.target).is('.btn-info, .btnPrice')){
                        e.stopPropagation();  //阻止事件冒泡
                        alert(JSON.stringify(item));
                  	 }
                }).load({page:1});
				
		});
		
		/**
		*更新选定的codeTable
		*/
		function updateCache(){
			var rows=codeTableInfoList.selectedRows();
			if(rows.length==0){
				openAlert('至少选择一条记录！');
				return;
			}
			var rows2=[];
			for(var i in rows){
				rows2.push(rows[i].CODETABLE);
			}
			console.log(rows2);
			openConfirm('确定更新选中项？',function(){
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>sys/datadictcache/updateChooseCaches.do',
				data : {"codeTables[]":rows2},
				success: function(result){
					 $('#model').fadeOut('slow');
					 $('#loading').fadeOut('slow');
					openAlert(result.content);
				},
				beforeSend:function(){
					 var docHeight = $(document).height(); //获取窗口高度
					    $('#loading').height(docHeight).css({
						      'opacity': .5, //透明度
						      'position': 'absolute',
						      'top': 0,
						      'left': 0,
						      'background-color': 'black',
						      'width': '100%',
						      'display':'block',
						      'z-index': 5000 //保证这个悬浮层位于其它内容之上
						    });
						    letDivCenter("#model");
				}
			});		
			});		
			
		}
		function letDivCenter(divName){   
	        var top = ($(window).height() - $(divName).height())/2;   
	        var left = ($(window).width() - $(divName).width())/2;   
	        var scrollTop = $(document).scrollTop();   
	        var scrollLeft = $(document).scrollLeft();   
	        $(divName).css( { position : 'absolute', 'top' : top + scrollTop, left : left + scrollLeft} ).show();
        }
		
		/**
		*更新所有的codeTable
		*/
		function updateAllCache(){
		openConfirm('确定全部更新？',function(){
			$.ajax({
					type : 'post',
					url  : '<%=basePath%>sys/datadictcache/updateAllCaches.do',
					success: function(result){
					 $('#model').fadeOut('slow');
					 $('#loading').fadeOut('slow');
						openAlert(result.content);
					},
					beforeSend:function(){
						 var docHeight = $(document).height(); //获取窗口高度
						    $('#loading').height(docHeight).css({
							      'opacity': .5, //透明度
							      'position': 'absolute',
							      'top': 0,
							      'left': 0,
							      'background-color': 'black',
							      'width': '100%',
							      'display':'block',
							      'z-index': 5000 //保证这个悬浮层位于其它内容之上
							    });
							    letDivCenter("#model");
					}
			});
		});	
		}
	</script>
</body>