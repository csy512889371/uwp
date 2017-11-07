<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
	#operDiv{padding-left: 5px;}
</style>
<body>
	<div class="oper_menu" id="operDiv">
		<button class="btn-style4" onclick="excelEnterpriseTemplate()">
			<i class="icon-edit"></i> 表单输出
		</button>
	</div>	
	<div>
		<table id="etpSearchList" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="searchConditionInfo" style="height: 35px;font-size: 13px;line-height: 15px;overflow: auto;text-align: left;border: 1px solid #ddd;padding-top: 5px;"></div>
		 <div id="pg1" style="text-align: right;"></div>
	</div>
	<script type="text/javascript">
        fixedAttrs = JSON.parse('${fixedAttrs}');
        extraAttrs = JSON.parse('${extraAttrs}');
		var cutIds = '';
		var cols = [
            { title:'企业名称', name:'ENTINF001', width:200, align:'center', sortable: false, type:'text', renderer: function(val,item){
                var returnVal = '<span class="name_link name_color" onclick="showEnterpriseInfo(\'' + item.ENTINF000 + '\')">'+ item.ENTINF001 +'</span>';
                return returnVal;
            }},
            { title:'所有制类别', name:'ENTINF019', width:120, align:'center', sortable: false, type:'text', renderer : function (val, item) {
                return findDictByCode("SHJT_ENT_INF", "ENTINF019", val);
            }},
            { title: '类型', name: 'ENTINF026', width: 120, align: 'center', sortable: false, type: 'text',renderer: function(val){
                return findDictByCode("SHJT_ENT_INF", "ENTINF026", val);
            }},
            { title: '企业类型', name: 'ENTINF003', width: 120, align: 'center', sortable: false, type: 'text',renderer: function(val){
                return findDictByCode("SHJT_ENT_INF", "ENTINF003", val);
            }},
            { title: '投资方分类', name: 'ENTINF020', width: 200, align: 'center', sortable: false, type: 'text',renderer: function(val){
                return findDictByCode("SHJT_ENT_INF", "ENTINF020", val);
            }},
            {
                title: '统一社会信用代码',
                name: 'entInf015',
                width: 120,
                align: 'center',
                sortable: false,
                type: 'text',
                renderer: function (val, item) {
                    return item.ENTINF015;//'某法人';
                }
            },
            { title:'注册资本', name:'ENTINF009', width:120, align:'center', sortable: false, type:'text', renderer: function(val){
                return val;}},
            { title:'成立日期', name:'ENTINF010', width:120, align:'center', sortable: false, type:'text', renderer: function(val){
                var date = new Date(val);
                val = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                return val; }},
            { title:'营业期限', name:'ENTINF011', width:120, align:'center', sortable: false, type:'text', renderer: function(val){
                var date = new Date(val);
                val = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                return val; }},
            { title: 'id', name: 'ENTINF000', width: 100, align: 'center', sortable: false, type: 'text', hidden: true }
             ];
             
         function searchList(page){
        	 $('#etpSearchList').html('<tr><th rowspan="" colspan=""></th></tr>');
			if(!page){
				page=1;
			}
			enterpriseList.load({pageNumber:page,datas:submitDatas,submitType:submitType,token:token});
		}
         
		$(document).ready(function(){
			showConditionInfo();
            //var serachListUrl = encodeURI('<%=basePath%>admin/udsearch/getEtpsSearchList.do?token='+token);
			enterpriseList = $('#etpSearchList').mmGrid({
                    height: commonTalbeHeight-85
                    , cols: cols
                    , url: '<%=basePath%>admin/udsearch/getEtpsSearchList.do'
                    , method: 'post'
                    , remoteSort:true
                    , sortName: 'topic'
                    , sortStatus: 'asc'
                    , root: 'resultMapList'
                    , multiSelect: true
                    , checkCol: true
                    , fullWidthRows: true
                    , autoLoad: false
                    , showBackboard : false
                    , plugins: [
                        $('#pg1').mmPaginator({limitList: [20,50,200,500,800]})
                    ]
                    , params: function(){
                        //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
                      return {
                          secucode: $('#secucode').val()
                      };
                    }
                });

                enterpriseList.on('cellSelected', function(e, item, rowIndex, colIndex){
                	selectedId = item.id;
                    //查看
                  
                });
                searchList(parseInt('${page}'));
		});
		
		function showConditionInfo(){
			var infoStr="";
			if(submitType=="1"){
				$.ajax({
					type:'post',
					url:'<%=basePath%>admin/udsearch/getConditionInfoInfo.do',
					data:{},
					success:function(result){
						$('#searchConditionInfo').html("&nbsp;&nbsp;&nbsp;查询条件:&nbsp;&nbsp;"+result.content);
					}
				});
			}else{
				for(var i=0;i<conditionList.rowsLength();i++){
					var data=conditonData[i];
					var a_lbreaket=$("#conditionList tr:eq("+i+") td:eq(2) span select option:selected").text();
					var name=$("#conditionList tr:eq("+i+") td:eq(3) span").html();
		      		var operator=$("#conditionList tr:eq("+i+") td:eq(4) span select").val();
		      		var opName=$("#conditionList tr:eq("+i+") td:eq(4) span select option:selected").text();
		      		temp=$("#conditionList tr:eq("+i+") td:eq(5) span input:eq(0)").val();
		      		var a_firValue=typeof(temp)=='undefined'?'':temp;
		      		temp=$("#conditionList tr:eq("+i+") td:eq(6) span input:eq(0)").val();
		      		var a_secValue=typeof(temp)=='undefined'?'':temp;
		      		var a_rbreaket=$("#conditionList tr:eq("+i+") td:eq(7) span select option:selected").text();
		      		var logic=$("#conditionList tr:eq("+i+") td:eq(8) span select").val();
		      		infoStr+="&nbsp;"+a_lbreaket+"&nbsp;"+name+"("+data.fname.toUpperCase()+")";
		      		if(data.vtype=='sql'){
		      			
		      		}else if(operator=='isNull'||operator=='isNotNull'){
		      			infoStr+="&nbsp;<span style='color:#4338ff'>"+opName+"</span>";
		      		}else if(operator=='between'){
		      			infoStr+="&nbsp;<span style='color:#4338ff'>在 </span><span style='color:red'>"
		      			+a_firValue+"</span>&nbsp;<span style='color:#4338ff'>与</span><span style='color:red'>"+a_secValue+
		      			"</span>&nbsp;<span style='color:#4338ff'>之间</span>";
		      		}else {
		      			infoStr+="&nbsp;<span style='color:#4338ff'>"+opName+"</span>&nbsp;<span style='color:red'>\""+a_firValue+"\"</span>";
		      		}
		      		infoStr+="&nbsp;"+a_rbreaket;
		      		
		      		logic=logic=='and'?'并且':'或者';
		      		if(i<conditionList.rowsLength()-1){
		      			infoStr+="&nbsp;<span style='color:#4338ff'>"+logic+"</span>";
		      		}
				}
				$('#searchConditionInfo').html("&nbsp;&nbsp;&nbsp;查询条件:&nbsp;&nbsp;"+infoStr);
			}
		
		}
		
		/**
		 * 查看人员信息
		 */
		function showCadreInfo(cadreSearchId,isChangeCadre){
			//点击人员姓名的时候，所有信息都不要勾选
			enterpriseList.deselect('all');
			loadCadreInfo(cadreSearchId,enterpriseList,isChangeCadre);
		}
		function getPersonIds(){
			var rows = enterpriseList.selectedRows();
			var item = rows[0];
			if (typeof(item) == 'undefined'||item==null) {
				openAlert('请选择干部人员信息记录！');
				return null;
			}
			var personIds='';
			for (var i in rows) {
	    		personIds += rows[i].a0000 + ',';
			}
			return personIds;
		}

        /**
         * 查看企业信息
         */
        function showEnterpriseInfo(entId){
            //点击企业姓名的时候，所有信息都不要勾选
			/*enterpriseList.deselect('all');
			 loadEnterpriseInfo(cadreSearchId,enterpriseList,isChangeCadre);*/
            var items = enterpriseList.rows();
            var arr = [];
            for (i = 0; i < items.length; i++) {
                arr[i] = items[i]["ENTINF000"];
            }
            entIds = arr.join(",");

            if (entId) {
                currentEntId = entId;
            } else {
                if (enterpriseList.selectedRows().length == 0) {
                    openAlert('请选择下面一行记录！');
                    return;
                } else {
                    currentEntId = enterpriseList.selectedRows()[0];
                }
            }
            openRmbWin('查看企业信息', 'ent/manage/showEntUpdateMain.do', {
                entIds: entIds,
                currentEntId: currentEntId
            }, function () {
            });
        }

        /**
         * excel导出
         */
        function excelEnterpriseTemplate(){
            var enterpriseIds = '';
            var rows = enterpriseList.selectedRows();
            for (var i in rows) {
                enterpriseIds += rows[i].ENTINF000 + ',';
            }
            openWindow('Excel表单-导出','/report/engine/excelCadreTemplate.do',1,400,395,{type:"bySelEnterprise",enterprises:enterpriseIds,token:token});
        }
	    

	</script>
</body>