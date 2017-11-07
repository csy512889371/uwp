<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>  
  <body>
  <script type="text/javascript">
    function checkRoleId(obj){
    	//alert(obj.value);
    	userRoleId = obj.value;
    }
    </script>
    <div style="margin-top:10px;">
		<table id="userRoleList" class="mmg">
               <tr>
                   <th rowspan="" colspan=""></th>
               </tr>
           </table>
	</div>
<script type="text/javascript">
var selectedId = null;
var userRoleList;
//表头
var cols = [
    { title:'角色名称', name:'ROLENAME' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val,item,rowIndex){//rowIndex从0开始
        return val;
    }},
    { title:'备注', name:'REMARK' ,width:120, align:'left', sortable: false, type: 'text', renderer: function(val){
        return val;
    }},
    { title:'ID', name:'ID' ,width:300, align:'center', sortable: false, type: 'text', hidden: true
    }
];

$(document).ready(function(){
	userRoleList = $('#userRoleList').mmGrid({
           height: 400
           , cols: cols
           , url: '<%=basePath%>sys/user/getUserRole.do?userId='+'${userId}'
           , method: 'post'
           , remoteSort:true
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


	userRoleList.on('cellSelected', function(e, item, rowIndex, colIndex){
       	selectedId = item.ID;
       	userRoleId = item.ID;
           //查看
           if($(e.target).is('.btn-info, .btnPrice')){
               e.stopPropagation();  //阻止事件冒泡
               alert(JSON.stringify(item));
           }
       }).on('loadSuccess',function(e, data){
       		var rows = userRoleList.rows();
			//console.log(rows);
			for(var s in rows){
				if(rows[s].ID==rows[s].ROLEUSERID){
					userRoleList.select(parseInt(s));
				}
			}
       });
		
	userRoleList.load();
	
});

</script>
  </body>
