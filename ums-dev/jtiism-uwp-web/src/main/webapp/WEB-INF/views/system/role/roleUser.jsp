<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
  
  <body>
    <div style="margin-top:10px;">
	  <table id="roleUserList" class="mmg">
        <tr>
          <th rowspan="" colspan=""></th>
        </tr>
      </table>
    </div>
    <script type="text/javascript">
    var roleUserList;
    var cols = [{ title:'用户名称', name:'USERNAME' ,width:100, align:'left', sortable: false, type: 'text'},
                { title:'管理权限', name:'DEPTNAME' ,width:100, align:'left', sortable: true, type: 'text'},
        		{ title:'备注', name:'REMARK' ,width:120, align:'left', sortable: false, type: 'text'},
        		{ title:'ID', name:'ID' ,width:300, align:'center', sortable: false, type: 'text', hidden: true}];
    
    $(document).ready(function(){
    	roleUserList = $('#roleUserList').mmGrid({
               height: 400
               , cols: cols
               , url: '<%=basePath%>sys/role/getRoleUser.do?roleId='+'${roleId}'
               , method: 'post'
               , remoteSort:false
               , sortName: 'SECUCODE'
               , sortStatus: 'asc'
               , multiSelect: true
               , checkCol: true
               , fullWidthRows: true
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

    	roleUserList.on('cellSelected', function(e, item, rowIndex, colIndex){
               //查看
               if($(e.target).is('.btn-info, .btnPrice')){
                   e.stopPropagation();  //阻止事件冒泡
                   alert(JSON.stringify(item));
               }
           }).on('loadSuccess',function(e, data){
           		var rows = roleUserList.rows();
    			for(var s in rows){
    				if(rows[s].ID == rows[s].USERROLEID){
    					roleUserList.select(parseInt(s));
    				}
    			}
           });
    		
    	roleUserList.load();
    	
    });
    </script>
  </body>