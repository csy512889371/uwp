<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
	#menuListDiv .input1 {margin-bottom: 0px;width: 150px;font-size: 12px;padding-left: 6px;}
	/* .divLeft {float: left;width: 30%;background-color: #f5f5f5;}
	.divRight {float: right;width: 70%;background-color: #f5f5f5;} */
	#menuListDiv{height:40px;line-height: 40px;text-align: left;}
</style> 
<body>
	<div>
		<div id="menuListDiv">
			<!-- <div class="divLeft"> -->
				<auth:auth ifAllGranted="menu:add">
					<button class="btn-style4" onclick="addMenu()"><i class="icon-plus"></i> 新增菜单</button>
				</auth:auth>
				<auth:auth ifAllGranted="menu:addSup">
					<button class="btn-style4" onclick="addChildrenMenu()"><i class="icon-plus"></i> 增加下级</button>
				</auth:auth>
				<auth:auth ifAllGranted="menu:edit">
					<button class="btn-style4" onclick="updateMenu()"><i class="icon-pencil"></i> 编辑</button>
				</auth:auth>
				<auth:auth ifAllGranted="menu:setGrant">
					<button class="btn-style4" onclick="operMenuOper()"><i class="icon-edit"></i> 操作权限</button>
				</auth:auth>
				<auth:auth ifAllGranted="menu:delete">
					<button class="btn-style4" onclick="deleteMenu()"><i class="icon-trash"></i> 删除</button>
				</auth:auth>
			<!-- </div> -->
			<auth:auth ifAllGranted="menu:search">
			<!-- <div class="divRight"> -->
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="text" id="menuName" name="menuName" placeholder="请输入菜单名称" class="input1">
				<button class="btn-style1" onclick="searchList()">查询</button>
				<button class="btn-style2" id="clear" onclick="clearSer();">重置</button>
			<!-- </div> -->
			</auth:auth>
		</div>
		<!-- 菜单列表 -->
		<div>
			<table id="menuList" class="mmg">
				<tr>
					<th rowspan="" colspan=""></th>
				</tr>
			</table>
		</div>
	</div>
	<div id="power_menu" title="选择">
		<div id="power_menu_content"></div>
	</div>
	<script>
				/**
				* 窗口自适应
				*/
				function adjustWindow(){
					if(menuList!=null) menuList.resize($(window).height()-180);
				}
	
				loadMenuData($('#menuName').val());
                var selectedId = null;
                //表头
                var cols = [
                    { title:'菜单标题', name:'title' ,width:150, align:'left', sortable: false, type: 'text', renderer: function(val,item,rowIndex){//rowIndex从0开始
                        return '<span style="cursor:default;">'+val+'</span>';
                    }},
                    { title:'菜单类型', name:'menutype' ,width:20, align:'left', sortable: false, type: 'text', renderer: function(val){
                        if(val==1){
                        	return "系统菜单";
                        }else if(val==2){
                        	return "二级菜单";
                        }else if(val==3){
                        	return "三级菜单";
                        };
                    }},
                    { title:'URL', name:'url' ,width:180, align:'left', sortable: false, type: 'text', renderer: function(val){
                        return val;
                    }},
                    { title:'大图标', name:'bigicon' ,width:50, align:'left', sortable: false, type: 'text', hidden:true, renderer: function(val){
                    	if(val==null||val==''){
                    		return val;
                    	}else{
                    		return '<img src="<%=basePath%>resources/admin/img/menuicon/'+val+'" style="height:25px;">';                    		
                    	}
                    }},
                    { title:'菜单图标', name:'smallicon' ,width:50, align:'left', sortable: false, type: 'text', renderer: function(val){
                    	if(val==null||val==''){
                    		return val;
                    	}else{
                    		return '<img src="<%=basePath%>resources/admin/img/menuicon/'+val+'" style="height:18px;">';                    		
                    	}
                    }},
                    { title:'状态', name:'state' ,width:20, align:'left', sortable: false, type: 'text', renderer: function(val){
                        if(val==true){
                        	return "有效";
                        }else{
                        	return "无效";
                        }
                    }},
                    { title:'备注', name:'remark' ,width:120, align:'left', sortable: false, type: 'text', renderer: function(val){
                        return val;
                    }},
                    { title:'id', name:'id' ,width:300, align:'center', sortable: false, type: 'text', hidden: true
                    }
                ];
       	
       		function searchList(page){
       			if(!page) page = 1;
       			searchMenuData($('#menuName').val());
       			//menuList.load({title:$('#menuName').val(),pageNumber:page});
       		}
       		
       		function searchMenuData(menuName){
       			$.ajax({
	       			type : 'post',
	       			url  : '<%=basePath%>sys/menu/menuList.do',
	       			data : {title:menuName},
	       			success: function(data){
	       				if(data!=null && typeof(data)!='undefined'){
	       					var dt = data.result;
	       					initMenuList(dt);
	       				}
	       			}	
       			});
       		}
       		
       		var menuGradeOne = [],
       			menuGradeTwo = {},
       			menuGradeThree = {};
       		
       		function loadMenuData(menuName){
       			$.ajax({
	       			type : 'post',
	       			url  : '<%=basePath%>sys/menu/menuList.do',
	       			success: function(data){
	       				if(data!=null && typeof(data)!='undefined'){
	       					var dt = data.result;
	       					var tempTwo = [],
	       						tempThree = [];
	       					
	       					for(var i in dt){//加载一级菜单
	       						if(dt[i].menutype==1){
	       							menuGradeOne.push(dt[i]);
	       							
	       						}else if(dt[i].menutype==2){//加载二级菜单
	       							if(typeof(menuGradeTwo[dt[i].parent.id])=='undefined'){
	       								tempTwo = [];
	       								tempTwo.push(dt[i]);
	       								menuGradeTwo[dt[i].parent.id]=tempTwo;
	       							}else{
	       								tempTwo = menuGradeTwo[dt[i].parent.id];
	       								tempTwo.push(dt[i]);
	       								menuGradeTwo[dt[i].parent.id]=tempTwo;
	       							}
	       						}else{//加载三级菜单
	       							if(typeof(menuGradeThree[dt[i].parent.id])=='undefined'){
	       								tempThree = [];
	       								tempThree.push(dt[i]);
	       								menuGradeThree[dt[i].parent.id]=tempThree;
	       							}else{
	       								tempThree = menuGradeThree[dt[i].parent.id];
	       								tempThree.push(dt[i]);
	       								menuGradeThree[dt[i].parent.id]=tempThree;
	       							}
	       						}
	       					}
	       					
	       					//为一级菜单添加标签
	       					for(var i in menuGradeOne){
	       						for(var j in menuGradeTwo){
	       							if(menuGradeOne[i].id == j){
	       								menuGradeOne[i].title = '<i class="icon-chevron-right"></i>'+menuGradeOne[i].title; //onclick="loadMenuGradeTwo('+menuGradeOne[i].id+','+i+',this)"
	       								break;
	       							}
	       						}
	       					}
	       					
	       					//为二级菜单添加标签
	       					for(var i in menuGradeTwo){
	       						for(var s in menuGradeTwo[i]){
	       							for(var j in menuGradeThree){
		       							if(menuGradeTwo[i][s].id == j){
		       								menuGradeTwo[i][s].title = '<span style="color:#fff;">&nbsp;&nbsp;&nbsp;</span><i class="icon-chevron-right"></i>'+menuGradeTwo[i][s].title;// onclick="loadMenuGradeThree('+menuGradeTwo[i][s].id+')" 
		       								break;
		       							}
		       						}
	       						}
	       					}
	       					
	       					//为三级菜单添加空格
	       					for(var i in menuGradeThree){
	       						for(var j in menuGradeThree[i]){
	       							menuGradeThree[i][j].title = '<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>'+menuGradeThree[i][j].title;
	       						}
	       					}
	       				}
	       				initMenuList(menuGradeOne);
	       			}
	       		});
       		
       		}
       		
       		/**
       		* 加载二级菜单
       		* @param parentid 父类ID
       		*/
       		function loadMenuGradeTwo(parentid, index){
       			var item = menuGradeTwo[parentid];
       			/* menuList.deselect('all');
       			menuList.select(index); */
                menuList.addRow(item, index+1);
                changeMenuIcon(1,'open',index,parentid);
       		}
       		
       		/**
       		* 加载三级菜单
       		* @param parentid 父类ID
       		*/
       		function loadMenuGradeThree(parentid, index){
       			var item = menuGradeThree[parentid];
       			/* menuList.deselect('all');
       			menuList.select(index); */
                menuList.addRow(item, index+1);
                changeMenuIcon(2,'open',index,parentid);
       		}
       		
       		/**
       		* 变更菜单图标
       		* @param grade 菜单等级（1：一级，2：二级，3：三级）
       		* @param type 变更类型（open：打开，close：关闭）
       		* @param index 该菜单在表中的序号
       		* @param parentid 该菜单ID
       		*/
       		function changeMenuIcon(grade, type, index, parentid){
       			var str1 = "icon-chevron-right",str2="icon-chevron-down";
       			if(type=='close'){
       				str1 = "icon-chevron-down";
       				str2 = "icon-chevron-right";
       			}
       			if(grade==1){
       				for(var i in menuGradeOne){
                    	if(menuGradeOne[i].id==parentid){
                    		menuGradeOne[i].title = menuGradeOne[i].title.replace(str1,str2);
                    		menuList.updateRow(menuGradeOne[i], index);
                    	}
                    }
       			}else if(grade==2){
       				for(var i in menuGradeTwo){
                    	for(var j in menuGradeTwo[i]){
                    		if(menuGradeTwo[i][j].id==parentid){
                        		menuGradeTwo[i][j].title = menuGradeTwo[i][j].title.replace(str1,str2);
                        		menuList.updateRow(menuGradeTwo[i][j], index);
                        	}
                    	}
                    }
       			}
       		}
       		
       		var clickIds = [];
       		var menuList;
       		function initMenuList(items){
       			var title = $('#menuName').val();
       			menuList = $('#menuList').mmGrid({
                    height: commonTalbeHeight
                    , cols: cols
                    , items : items
                    , remoteSort:true
                    , sortName: 'SECUCODE'
                    , sortStatus: 'asc'
                    , root: 'result'
                    , multiSelect: false
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
                         }
                    }
                    });

       			menuList.on('cellSelected', function(e, item, rowIndex, colIndex){
       				var id = item.id;
       				for(var i in clickIds){
       					if(clickIds[i]==item.id){//id已经存在于clickIds，表示该菜单已经被点开了
       						if(item.menutype==1){
       							removeMenu(item.id, rowIndex);
       						}else if(item.menutype==2){
       							removeMenuGradeThree(item.id, rowIndex);
       						}
       						return;
       					}
       				}
       				clickIds.push(id);
                    //查看
                    if($(e.target).is('.mmg-check')){

                        e.stopPropagation();  //阻止事件冒泡
                        return;
						/*$(e).attr("checked",true);
						 alert($(e).closest("tr"));//JSON.stringify(item)
						 console.log($(e).closest("tr"));*/
                    }
       				
       				if(item.menutype==1){
       					loadMenuGradeTwo(item.id, rowIndex);
       				}else if(item.menutype==2){
       					loadMenuGradeThree(item.id, rowIndex);
       				}
                    

                }).load({page: 1});
       		}
       		
       		/**
       		* 删除二级与三级菜单
       		* @param parentid 选中菜单的ID
       		* @param rowIndex 选中菜单在表格中的序号
       		*/
       		function removeMenu(parentid, rowIndex){
       			var rowNum = [];
       			var rows = menuList.rows();
       			var idIndex = -1;
       			//console.log(rows);
       			for(var i in menuGradeTwo){
       				if(i==parentid){
       					for(var j in menuGradeTwo[i]){
       						//一级菜单下的所有二级菜单图标都设置为缩起来的样式
							if(menuGradeTwo[i][j].title!=null && menuGradeTwo[i][j].title!='undefined')
       							menuGradeTwo[i][j].title = menuGradeTwo[i][j].title.replace('icon-chevron-down','icon-chevron-right');
       						for(var s in rows){
       							if(rows[s].id==menuGradeTwo[i][j].id){//二级菜单在列表中存在，那么就去除它的位置
       								rowNum.push(s);
       							}
       						}
       						getGradeThreeIndex(menuGradeTwo[i][j].id, rowNum);//获取三级菜单序号，并放入rowNum中
       						arryBug();
       						idIndex = clickIds.indexOf(menuGradeTwo[i][j].id);
       						if(idIndex>-1){
       							clickIds.splice(idIndex,1);
       						}
       					}
       				}
       			}
       			//console.log(rowNum);
       			menuList.removeRow(rowNum);
       			arryBug();
       			var index = clickIds.indexOf(parentid);
       			clickIds.splice(index,1);//从已点击ID数组中去除取消选择的ID
       			changeMenuIcon(1,'close',rowIndex,parentid);
       		}
       		
       		/**
       		* 获取三级菜单ID并放入数组
       		* @param parentid 选中菜单的ID
       		* @param rowIndex 选中菜单在表格中的序号
       		*/
       		function getGradeThreeIndex(parentid, rowNum){
       			var rows = menuList.rows();
       			for(var i in menuGradeThree){
       				if(i==parentid){
       					for(var j in menuGradeThree[i]){
       						for(var s in rows){
       							if(rows[s].id==menuGradeThree[i][j].id){
       								rowNum.push(s);
       							}
       						}
       					}
       				}
       			}
       		}
       		
       		/**
       		* 删除三级菜单
       		* @param parentid 选中菜单的ID
       		* @param rowIndex 选中菜单在表格中的序号
       		*/
       		function removeMenuGradeThree(parentid, rowIndex){
       			var rows = menuList.rows();
       			var rowNum = [];
       			for(var i in menuGradeThree){
       				if(i==parentid){
       					for(var j in menuGradeThree[i]){
       						for(var s in rows){
       							if(rows[s].id==menuGradeThree[i][j].id){
       								rowNum.push(s);
       							}
       						}
       					}
       				}
       			}
       			//console.log(rowNum);
       			menuList.removeRow(rowNum);
       			arryBug();
       			var index = clickIds.indexOf(parentid);
       			clickIds.splice(index,1);//从已点击ID数组中去除取消选择的ID
       			changeMenuIcon(2,'close',rowIndex,parentid);
       		}
       	
       		/**
       		* 修改菜单
       		*/
       		function updateMenu(){
       			var item = checkSelected();
				if(item==null){
					openAlert('请选择一条记录！');
					return;
				}
       			openWindow('修改菜单信息','sys/menu/menuUpdate.do?menuId=' + item.id,1,500,480);
       		}
       	
       		/**
       		* 删除菜单
       		*/
       		function deleteMenu(){
       			var item = checkSelected();
				if(item==null){
					openAlert('请选择一条记录！');
					return;
				}
       			openConfirm('确定删除该记录？',function(){
       				$.ajax({
       					type : 'post',
       					url  : '<%=basePath%>sys/menu/deleteMenu.do',
       					data : {menuId : item.id},
       					success: function(data){
       						openAlert(data.content);
       						if(data.type == 'success'){
       							//重新加载一次页面
           						$('#mainContent').load('<%=basePath%>sys/menu/searchMenu.do',{});
       						}
       					}
       				});				
       			});
       		}
       		
       		/**
       		* 新增菜单
       		*/
       		function addMenu(){
       			openWindow('新增菜单','sys/menu/menuAdd.do',1,500,480);
       		}
       		
       		/**
       		* 增加下级菜单
       		*/
       		function addChildrenMenu(){
       			var item = checkSelected();
				if(item==null){
					openAlert('请选择一条记录！');
					return;
				}
       			openWindow('增加下级菜单','sys/menu/addChildrenMenu.do?parentId='+item.id,1,500,480);
       		}
       		
       		/**
			* 检验是否选择数据
			*/
			function checkSelected(){
				var rows = menuList.selectedRows();
				var item = rows[0];
				if(typeof(item)=='undefined'){
					return null;
				}
				return item;
			}
       		/**
       		 * 操作权限列表
       		 */
			function operMenuOper(){
       	    	var item = checkSelected();
				if(item==null){
					openAlert('请选择一条记录！');
					return;
				}
				openWindow('菜单操作项管理','sys/menu/menuOperList.do?menuId='+item.id);
			}
	     	/**
			* 清除查询条件
			*/
			function clearSer(){
				$('#menuName').val('');
				searchList();
			}
	     	
	     	/**
	     	* 处理IE8下数组indexof的BUG
	     	*/
	     	function arryBug(){
	     	    if (!Array.prototype.indexOf){  
	                Array.prototype.indexOf = function(elt /*, from*/){  
	                var len = this.length >>> 0;  
	                var from = Number(arguments[1]) || 0;  
	                from = (from < 0)  
	                     ? Math.ceil(from)  
	                     : Math.floor(from);  
	                if (from < 0)  
	                  from += len;  
	                for (; from < len; from++)  
	                {  
	                  if (from in this &&  
	                      this[from] === elt)  
	                    return from;  
	                }  
	                return -1;  
	              };  
	            }  
	     	}
	     	/**
			  * 选择图标
			  */
			  function chooseIcon(){
			 	 openWindow('选择图标','sys/menu/chooseIcon.do',2,360,250);
			  }
	</script>
</body>
