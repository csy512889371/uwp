<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
<!-- 系统缓存页面 -->
<body>
	<div class="oper_menu">
		<auth:auth ifAllGranted="sysCache:clear">
			<div style="display: inline;">
				<button class="btn-style4" onclick="clearCache()"><i class="icon-trash"></i>清除缓存</button>
			</div>
		</auth:auth>
		<div style="display: inline;">&nbsp;&nbsp;&nbsp;&nbsp;</div>
	</div>
		<!-- 活动列表 -->
		<div>
			<table id="cacheInfoListtt" class="mmg">
                <tr>
                    <th rowspan="" colspan=""></th>
                </tr>
            </table>
		</div>
	<script >
		/**
		* 窗口自适应
		*/
		function adjustWindow(){
			if(cacheInfoListtt!=null) cacheInfoListtt.resize($(window).height()-180);
		}
	
		var cacheInfoListtt;
		var cols = [
             { title:'已使用内存(M)', name:'TOTALMEMORY' ,width:200, align:'left', sortable: false, type: 'text'},
             { title:'最大可用内存(M)', name:'MAXMEMORY' ,width:200, align:'left', sortable: false, type: 'text'},
             { title:'剩余内存(M)', name:'FREEMEMORY' ,width:200, align:'left', sortable: false, type: 'text'},
             { title:'缓存数', name:'CACHESIZE' ,width:200, align:'left', sortable: false, type: 'text'},
             { title:'缓存存储路径', name:'DISKSTOREPATH' ,width:200, align:'left', sortable: false, type: 'text'}
          ];
          var cacheInfo_item=[];
   			cacheInfo_item.push({'TOTALMEMORY':'${totalMemory}','MAXMEMORY':'${maxMemory}','FREEMEMORY':'${freeMemory}','CACHESIZE':'${cacheSize}','DISKSTOREPATH':'${diskStorePath}'});
   			
          $(document).ready(function(){
          
			cacheInfoListtt = $('#cacheInfoListtt').mmGrid({
                    height: commonTalbeHeight
                    , cols: cols
                    , method: 'get'	
                    , remoteSort:true
                    , items: cacheInfo_item
                    , sortName: 'SECUCODE'
                    , sortStatus: 'asc'
                    , multiSelect: false
                    , fullWidthRows: true
                    , checkCol: false
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


                cacheInfoListtt.on('cellSelected', function(e, item, rowIndex, colIndex){
                      if($(e.target).is('.btn-info, .btnPrice')){
                        e.stopPropagation();  //阻止事件冒泡
                        alert(JSON.stringify(item));
                  	 }
                }).load();
				
		});
		
		function clearCache(){
		openConfirm('确定清除缓存？',function(){
			$('#mainContent').load('<%=basePath%>sys/syscache/clear.do',{});
		});
			
		}
		/**
		 * 更新所有综合查询数据
		 */
		function updateAll() {
			$('#updateAll').text('正在更新...');
			$("#updateAll").attr("disabled", true);
			$("#updateAll").css('color','#D3D3D3');
			$.ajax({
				url:'<%=basePath%>unit/integrateSearch/updateAllIntegrate.do',
				data:{},
				type : 'post',
			    success: function(data){
				     openAlert(data.content);
					 if(data.type == 'success'){
						 $('#updateAll').text('更新综合数据');
						 $("#updateAll").attr("disabled", false);
						 $("#updateAll").css('color','#000000');
					 }
				 }
			});
		}
		
	</script>
</body>