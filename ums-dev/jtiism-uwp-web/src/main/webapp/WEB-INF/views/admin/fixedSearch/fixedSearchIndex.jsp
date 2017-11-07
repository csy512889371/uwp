<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<link href="<%=basePath%>resources/admin/modules/enterprise/enterprise.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=basePath%>resources/admin/modules/enterprise/enterprise.js"></script>

<!-- 问题信息输出 -->
<style>
	.zt li a{width:260px;}
	.oper_menu{padding-left: 5px;}
	.oper_menu input{height:25px;width:90px;}
	#reportDiv{margin-left: 313px;position: relative;}
	#conditionListDiv{width:300px;float: left;overflow: auto;border-right: 1px solid #b8c0cc;}
	#switchBar{height:100%;cursor: pointer;float: left;width:13px;background-color: #fff;}
    #switchBar:hover{background-color: #f7f7f7;}
    #switchHref{top: 50%;position: absolute;}
    .tempBtn{position: fixed;background-color: #fff;padding:5px 0 5px 5px;z-index:1;}
</style>
<body>
	<div>
		<!-- 左侧条件 -->
		<div id="conditionListDiv">
			<div class="oper_menu">
				<div class="btn-group dropdown">
					<button class="btn dropdown-toggle" data-toggle="dropdown">
						<i class="icon-th-list"></i> 分组管理 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu" style="text-align:left;">
						<li><a onclick="addGroup()"> <i class="icon-plus"></i> 新增分组</a></li>
					    <li><a onclick="updateGroup()"> <i class="icon-pencil"></i> 编辑分组</a></li>
					    <li><a onclick="deleteGroup()"> <i class="icon-trash"></i> 删除分组</a></li>
					</ul>
				</div>
				<div class="btn-group dropdown">
					<button class="btn dropdown-toggle" data-toggle="dropdown">
						<i class="icon-th-list"></i> 模板管理 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu" style="text-align:left;">
						<li><a onclick="chooseResource()"> <i class="icon-plus"></i> 新增模板</a></li>
					    <li><a onclick="deleteTemplate()"> <i class="icon-trash"></i> 删除模板</a></li>
					</ul>
				</div>
				<button onclick="templateOrder()" class="btn-style4"><i class="icon-random"></i>排序</button>
			</div>
			<div style="margin-top: 15px;text-align: left;" id="groupNameDiv">
				&nbsp;&nbsp;<span style="font-weight: bolder;">已选分组：</span><span id="selGroupName"></span>
			</div>
			<div style="margin-top: 1px;padding-left: 1px;">
			<ul id="conditionList" class="ztree ztree1 zt"></ul>
			</div>
		</div>
		
		<!-- 切换显示隐藏的bar -->
		<div id="switchBar" onclick="switchShow()">
			<i id="switchHref" class="icon-chevron-left"></i>&nbsp;&nbsp;&nbsp;
		</div>
		
		<!-- 右侧内容 -->
		<div id="reportDiv">
			 <div class="oper_menu" id="operDiv">

				<div class="btn-group dropdown">
						<button class="btn dropdown-toggle" data-toggle="dropdown">
							<i class="icon-th-list"></i> 企业统计&nbsp;<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu" style="text-align:left;">
						    <li><a onclick="checkCadreCount()"> <i class="icon-signal"></i>统计选中企业</a></li>
							<li><a onclick="allCadreCount()"> <i class="icon-signal"></i>统计查询企业</a></li>
						</ul>
					<button class="btn-style4" onclick="excelEnterpriseTemplate()">
						<i class="icon-edit"></i> 表单输出
					</button>
				</div>

			 </div>	
			 <table id="enterpriseList" class="mmg">
				<tr>
					<th rowspan="" colspan=""></th>
				</tr>
			</table>
			<div id="page" style="text-align: right;"></div>
		</div>
	</div>
	
	<script>
		$('#conditionListDiv').height($(window).height()-115);
		$('#switchBar').height($(window).height()-115);
		var token='${token}';
		var basicNodes =[];
		
		var showLeft = true;
        fixedAttrs = JSON.parse('${fixedAttrs}');
        extraAttrs = JSON.parse('${extraAttrs}');
		var currentGroupId = '';//当前被选中的分组ID
		var currentGroupName = '';//当前被选中的分组名称
		var currentTemplateNode = null;//当前被选中模板
		var currentTempId = '';//当前被选中的模板ID
		/**
		* 窗口自适应
		*/
		function adjustWindow(){
			$('#conditionListDiv').height($(window).height()-115);
			enterpriseList.resize($(window).height()-200);
			$('#switchBar').height($(window).height()-115);
		}
		
		/**
		* 左侧列表切换显示隐藏
		*/
		function switchShow(){
			if(showLeft){
				showLeft = false;
				$('#conditionListDiv').hide();
				$('#reportDiv').css('margin-left','13px');
				$('#switchHref').removeClass('icon-chevron-left');
				$('#switchHref').addClass('icon-chevron-right');
			}else{
				showLeft = true;
				$('#conditionListDiv').show();
				$('#reportDiv').css('margin-left','313px');
				$('#switchHref').removeClass('icon-chevron-right');
				$('#switchHref').addClass('icon-chevron-left');
			}
		}
		
		//var queryTemps =JSON.parse('${queryTemps}');
		//左边的树结构
		var conditionListSetting = {
			 view: {
				/* showLine: false,
				showIcon: false,
				selectedMulti: false,
				dblClickExpand: false */
			}, 
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick,
				onClick : changeInfo
			}
		};
		

		// 模板id
		var templateId="";
		
		function changeInfo(event, treeId, treeNode){
			if (treeNode.lurl == '') {
				return ;
			}
			//currentTempId = treeId;
			currentTemplateNode = treeNode;
			
			templateId="";
			var lurl = treeNode.lurl;
			var str = lurl.split("&");
			for ( var i in str) {
				var key = str[i].split("=")[0];
				var value = str[i].split("=")[1];
				if(key=="templateId"){
					templateId=value;
				}
			}
			searchList();
		}
	
		function beforeClick(treeId, treeNode) {
			if (treeNode.level == 0 ) {
				currentGroupId = treeNode.id;
				currentGroupName = treeNode.name;
				$('#selGroupName').html(currentGroupName);
				var zTree = $.fn.zTree.getZTreeObj("conditionList");
				zTree.expandNode(treeNode);
				return false;
			}
			return true;
		}
		
		/**
		* 加载模板树
		*/
		function loadTemplateTree(){
			basicNodes = [];
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>admin/fixedSearch/findGroupAndTemplate.do',
				success : function(data){
					for(var i in data){
						basicNodes.push({
							'id'   : data[i].id,
							'pId'  : data[i].pId,
							'name' : data[i].name,
							'lurl' : data[i].lurl,
							'user' : data[i].user
						});	
					}
					var treeObj = $("#conditionList");
					$.fn.zTree.init(treeObj, conditionListSetting, basicNodes);
					zTree_Menu = $.fn.zTree.getZTreeObj("conditionList");
					var nodes = zTree_Menu.getNodes();
                    //zTree_Menu.selectNode(nodes[0]);
                    beforeClick(conditionListSetting.treeId,nodes[0]);
				}
			});
		}
		
		$(document).ready(function(){
			loadTemplateTree();
		});
		
		var cols=[
            { title:'企业名称', name:'ENTINF001', width:200, align:'center', sortable: false, type:'text', renderer: function(val,item){
                var returnVal = '<span class="name_link name_color" onclick="showEnterpriseInfo(\'' + item.ENTINF000 + '\')">'+ item.ENTINF001 +'</span>';
                return returnVal;
            }},
            { title:'所有制类别', name:'ENTINF019', width:120, align:'center', sortable: false, type:'text', renderer : function (val, item) {
                return findDictByCode("SHJT_ENT_INF", "ENTINF019", item.ENTINF019);
            }},
            { title: '类型', name: 'ENTINF026', width: 120, align: 'center', sortable: false, type: 'text',renderer: function(val,item){
                return findDictByCode("SHJT_ENT_INF", "ENTINF026", item.ENTINF026);
            }},
            { title: '企业类型', name: 'ENTINF003', width: 120, align: 'center', sortable: false, type: 'text',renderer: function(val,item){
                return findDictByCode("SHJT_ENT_INF", "ENTINF003", item.ENTINF003);
            }},
            { title: '投资方分类', name: 'ENTINF020', width: 120, align: 'center', sortable: false, type: 'text',renderer: function(val,item){
		    	return findDictByCode("SHJT_ENT_INF", "ENTINF020", item.ENTINF020);
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
            { title:'注册资本', name:'ENTINF009', width:120, align:'center', sortable: false, type:'text', renderer: function(val,item){
                return item.ENTINF009;}},
            { title:'成立日期', name:'ENTINF010', width:120, align:'center', sortable: false, type:'text', renderer: function(val,item){
                var date = new Date(val);
                val = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                return val; }},
            { title:'营业期限', name:'ENTINF011', width:120, align:'center', sortable: false, type:'text', renderer: function(val,item){
                var date = new Date(val);
                val = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                return val; }},

            { title: 'id', name: 'ENTINF000', width: 100, align: 'center', sortable: false, type: 'text', hidden: true }
           ];
		
		var enterpriseList;
		$(document).ready(function(){
			enterpriseList=$('#enterpriseList').mmGrid({
				height: commonTalbeHeight-20
		        , cols: cols
		        , url: '<%=basePath%>admin/fixedSearch/getFixedSearchList.do'
		        , method: 'get'
		        , remoteSort:true
		        , sortName: 'SECUCODE'
		        , sortStatus: 'asc'
		        , root: 'resultMapList'
		        , multiSelect: true
		        , checkCol: true
		        /* , indexCol:true */
		        , fullWidthRows: true
		        , autoLoad: false
		        , showBackboard : false
		        ,loadingText:'正在载入数据'
		        , plugins: [
		            $('#page').mmPaginator({limitList: [20,50,200,500,800]})
		        ]
		        , params: function(){
		            //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
		          return {
		              secucode: $('#secucode').val()
		          };
		        }
			});
		});
		
		function searchList(page){
			if (!page) {
	    		page = 1;
	    	}
			enterpriseList.load({
				pageNumber : page,
				templateId : templateId,
				token:token
			});
		}
		
		/**
		 * 查看企业信息
		 */
		function showEtpInfo(cadreSearchId,isChangeCadre){
			//点击企业姓名的时候，所有信息都不要勾选
			enterpriseList.deselect('all');
			loadCadreInfo(cadreSearchId,enterpriseList,isChangeCadre);
		}
		
		/**
		 * 检验是否选择数据
		 */
		function checkSelected() {
			var rows = enterpriseList.selectedRows();
			var item = rows[0];
			if (typeof(item) == 'undefined') {
				return null;
			}
			return item;
		}
		
		/**
		 * 企业统计
		 */
		function cadreCount() {
			var rows = enterpriseList.rows();
			if(typeof(rows[0]) == 'undefined'){
				openAlert('没有数据！');
				return;
			}
			var ids = '';
			var item = checkSelected();
			if (item == null) {
				$.ajax({
					type: 'get',
					url: '<%=basePath%>admin/fixedSearch/getAllFixedSearchList.do',
					data: {
						templateId : templateId
					},
					success: function(data){
						var ids = "";
						for(var i in data){
							ids += data[i].a0000 + ',';
						}
						ids = ids.substring(0,ids.length);
						selectCadreIds = ids;
						openWindow('企业统计','/cadre/cadreOper/cadreCountIndex.do',1,'max','max',{ids:ids});
					}
				});
			} else {
				var rows = enterpriseList.selectedRows();
				for (var i in rows) {
					ids += rows[i].a0000 + ',';
				}
				ids = ids.substring(0,ids.length);
				selectCadreIds = ids;
				openWindow('企业统计','/cadre/cadreOper/cadreCountIndex.do',1,'max','max',{ids:ids});
			}
		}

	
	    /**
	    * 新增分组
	    */
	    function addGroup(){
	    	openWindow('新增分组','/admin/fixedSearch/groupAddIndex.do',1,400,200);
	    }
	    
	    /**
	    * 修改分组
	    */
	    function updateGroup(){
	    	if(currentGroupId==''){
	    		openAlert('请选择分组！');
	    		return;
	    	}
	    	openWindow('编辑分组','/admin/fixedSearch/groupUpdateIndex.do',1,400,200,{id:currentGroupId});
	    }
	    
	    /**
	    * 删除分组
	    */
	    function deleteGroup(){
	    	
	    	if(currentGroupId==''){
	    		openAlert('请选择分组！');
	    		return;
	    	}
	    	delGroup();
	    	
	    }
	    
	    function delGroup(){
	    	for(var i in basicNodes){
	    		if(currentGroupId == basicNodes[i].pId){
	    			openAlert('该分组下存在模板信息，您不能删除！');
	    			return;
	    		}
	    	}
	    	
	    	openConfirm('确定删除分组"'+currentGroupName+'"?',function(){
	    		$.ajax({
	    			type : 'post',
	    			url  : '<%=basePath%>admin/fixedSearch/deleteGroup.do',
	    			data : {id:currentGroupId},
	    			success : function(data){
	    				//closeWindow();
	    				openAlert(data.content);
	    				if(data.type == "success"){
	    					$('#selGroupName').html('');
	    		    		loadTemplateTree();
	    		    	}
	    			} 
	    		});
	    	});
	    }
	    
	    /**
	    * 选择模板来源
	    */
	    function chooseResource(){
	    	openWindow('模板选择','/admin/fixedSearch/templateResource.do',1,300,160);
	    }
	    
	    
	    /**
	    * 删除模板
	    */
	    function deleteTemplate(){
	    	if(currentTemplateNode==null){
	    		openAlert('请选择一个模板！');
	    		return;
	    	}
	    	
	    	/*if(currentTemplateNode.user!='${sessionScope.current_user.id}'){
	    		openAlert('您不是该模板的创建者，无权限删除！');
	    		return;
	    	}*/
	    	
	    	openConfirm('确定删除模板"'+currentTemplateNode.name+'"?',function(){
	    		$.ajax({
		    		type : 'post',
		    		url  : '<%=basePath%>admin/fixedSearch/deleteTemplate.do',
		    		data : {id : currentTemplateNode.id},
		    		success : function(data){
	    				openAlert(data.content);
	    				if(data.type == "success"){
	    		    		loadTemplateTree();
	    		    	}
		    		}
		    	});
	    	});
	    }
	    
	    /**
	    * 排序
	    */
	    function templateOrder(){
	    	openWindow('排序','/admin/fixedSearch/templateOrder.do',1,600,600);
	    }
	    
	   
	    /**
		 * 选择企业统计
		 */
		function checkCadreCount(){
	    	var rows = enterpriseList.selectedRows();
	    	if(rows.length == 0){
	    		openAlert('请选择企业信息记录！');
				return ;
	    	}
	    	var personIds = '';
	    	for (var i in rows) {
	    		personIds += rows[i].a0000 + ',';
			}
	    	showWait('数据生成中，请稍候');
			openWindow('企业统计','/cadre/cadreOper/cadreCountIndex.do',1,'max','max',{ids:personIds});
		}
		
		function allCadreCount(){
			var rows = enterpriseList.rows();
			var item = rows[0];
			if (typeof(item) == 'undefined'||item==null) {
				openAlert('暂无企业统计，请先查询企业信息');
				return null;
			}
			showWait('数据生成中，请稍候');
			openWindow('企业统计','/cadre/cadreOper/cadreCountIndex.do',1,'max','max',{token:token});
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