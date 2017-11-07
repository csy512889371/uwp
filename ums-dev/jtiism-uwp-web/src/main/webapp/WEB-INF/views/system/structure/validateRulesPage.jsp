<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<body>
	<div style="width: 90%; margin: auto;">
		<table id="validateRuleList" class="mmg">
        <tr>
          <th rowspan="" colspan=""></th>
        </tr>
      </table>
      
	</div>
	<script type="text/javascript">
	 //表头
	 var vrInputId='${inputId}';
	   var cols = [
	        { title:'校验名', name:'validateName' ,width:100, align:'left', sortable: false, type: 'text'},
	        { title:'代码', name:'validateCode' ,width:100, align:'center', sortable: false, type: 'text' }
	    ]
	   var vrdatas=[{validateName:'电子邮件验证',validateCode:'email'},
	                {validateName:'网址验证',validateCode:'url'},
	                {validateName:'日期验证',validateCode:'date'},
	                {validateName:'ISO日期格式验证',validateCode:'dateISO'},
	                {validateName:'数字验证',validateCode:'number'},
	                {validateName:'整数验证',validateCode:'digits'}
	                ];
		var validateRuleList;
		$(document).ready(function(){
			validateRuleList = $('#validateRuleList').mmGrid({
	              height: commonTalbeHeight
	              , cols: cols
	              ,	items:vrdatas
	              ,height:'400px'
	              ,width:'368px'
	              , method: 'get'
	              , remoteSort:true
	              , sortName: ''
	              , sortStatus: 'asc'
	              , multiSelect: true
	              , checkCol: true
	              , fullWidthRows: true
	              , autoLoad: false
	              , showBackboard : false
	              , plugins: [
	              ]
	              , params: function(){
	                  //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
	                return {
	                    secucode: $('#secucode').val()
	                };
	              }
	          });

			validateRuleList.on('cellSelected', function(e, item, rowIndex, colIndex){
	         
	          }).load();
			var dataStr=$('#'+vrInputId).val();
			var vrDataArray= dataStr.split(',');
			$.each(vrDataArray, function(idx, ruleItem) {  
				if($.trim(ruleItem) ==''){
					return;
				}
				var vrRows=validateRuleList.rows();
				for(var i in vrRows){
					if(vrRows[i].validateCode==ruleItem){
						validateRuleList.select(parseInt(i));
					}
				}
			});
			
		});
	</script>
</body>
