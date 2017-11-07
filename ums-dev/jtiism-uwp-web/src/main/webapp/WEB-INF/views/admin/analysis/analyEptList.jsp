<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
	.cadreCountCheckFont{color:black !important;}
	a:hover{color:#005580 !important;}
	#pub_window_content2{padding-top: 0px !important;}
</style>
<body>
	<div class="oper_menu">

	</div>
	<div>
		<div id="etpAnalyListDiv" style="margin:auto;width:96%;">
			<table id="etpAnalyList" class="mmg">
				<tr>
					<th rowspan="" colspan=""></th>
				</tr>
			</table>
		</div>
		<div id="etpAnalyPgs" style="text-align: right;margin-right: 2%;"></div>
	</div>
	<script type="text/javascript">
		// 表头
		var etpsAnalyCols = [
		    { title: '企业名称', name: 'a0101', width: 50, align: 'center', sortable: false, type: 'text', renderer: function(val,item){
		    	var returnVal = '<a href="javascript:void(0);" style="text-decoration:underline;color:#000;cursor:pointer;" onclick="showEtpAnalyInfo(\''+item.a0000+'\',\''+item.isChange+'\')">'+val+'</a>';
		        return returnVal;
		    }},
		    { title: '类型', name: 'unitName', width: 120, align: 'center', sortable: false, type: 'text', hidden: true },
		    { title: '法人代表', name: 'unitCode', width: 120, align: 'center', sortable: false, type: 'text', hidden: true},
		    { title: '成立日期', name: 'thisDuty', width: 100, align: 'center', sortable: false, type: 'text' },
		    { title: '营业期限', name: 'a0104', width: 30, align: 'center', sortable: false, type: 'text' , renderer : function (val, item) {
				return findDictByCode('A01', 'a0104', val);
			}},
		    { title: '企业类型', name: 'a0107', width: 70, align: 'center', sortable: false, type: 'text' , renderer : function (val, item) {
				return formatDate(val);
			}},
		    { title: '主营产品', name: 'a0111b', width: 100, align: 'center', sortable: false, type: 'text' , renderer : function (val, item) {
				return findDictByCode('A01', 'a0111b', val);
			}}
		];

		var etpIds = JSON.parse('${etpIds}');
		etpIds = etpIds.join(",");
		var etpAnalyList;
		function loadEtpAnalyList() {
			etpAnalyList = $('#etpAnalyList').mmGrid({
			    height: 560,
			    cols: etpsAnalyCols,
			    url: '<%=basePath%>ent/analy/etpAnalyDatas.do',
				method: 'post',
				remoteSort: true,
				sortName: 'SECUCODE',
				sortStatus: 'asc',
				root: 'resultMapList',
				multiSelect: true,
				checkCol: true,
				fullWidthRows: true,
				autoLoad: false,
				showBackboard: false,
				plugins: [$('#etpAnalyPgs').mmPaginator({limitList: [50,100,150,200,250],mySearch:function(page){
	    			if(!page){
	    				page=1;
	    			}
	    			searchEtpAnalyList(page);
	    		}})],
				params: function() {
					// 如果这里有验证，在验证失败时返回false则不执行AJAX查询。
					return {
						secucode: $('#secucode').val()
					};
				}
			});

			etpAnalyList.on('cellSelected', function(e, item, rowIndex, colIndex) {
				cadreSelectedId = item.ENTINF000;
				// 查看
				if ($(e.target).is('.btn-info, .btnPrice')) {
					// 阻止事件冒泡
					e.stopPropagation();
					alert(JSON.stringify(item));
				}
			}).load({pageNumber: 1, etpIds : etpIds});
		}

		$(document).ready(function() {
			loadEtpAnalyList();
		});

		function searchEtpAnalyList(page){
			if (!page) {
	    		page = 1;
	    	}
			etpAnalyList.load({pageNumber: page, etpIds : etpIds});
		}

	    /**
		 * 检验是否选择数据
		 */
		function etpAnalySelected() {
			var rows = etpAnalyList.selectedRows();
			var item = rows[0];
			if (typeof(item) == 'undefined') {
				return null;
			}
			return item;
		}

		/**
		 * 获取选择的id
		 */
		function getCountCheckIds(){
			var etpIds = '';
			var item = etpAnalySelected()
			if (item == null) {
				openAlert('请选择干部人员信息记录！');
				return null;
			}
			var rows = etpAnalyList.selectedRows();
			for (var i in rows) {
	    		etpIds += rows[i].ENTINF000 + ',';
			}
			return etpIds;
		}

		/**
	     * 查看企业信息
	     */
	    function showEtpAnalyInfo(enterpriseId,isChange){
	    	//点击人员姓名的时候，所有信息都不要勾选
			etpAnalyList.deselect('all');
			loadEnterpriseInfo(enterpriseId,etpAnalyList,isChange);
	    }



	</script>
</body>