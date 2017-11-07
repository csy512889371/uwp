<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common.inc"%>
<!DOCTYPE html >
<html>
	<head>
	    <meta http-equiv="X-UA-Compatible" content="IE=edge" >
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<meta content="yes" name="apple-mobile-web-app-capable" />
		<meta content="black" name="apple-mobile-web-app-status-bar-style" />
		<meta content="telephone=no" name="format-detection" />
		<meta content="email=no" name="format-detection" />
		<title>信息管理平台</title>

		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/css/initStyle.css">
		<link rel="stylesheet" href="<%=basePath%>resources/admin/css/bootstrap.css">
		<link href="<%=basePath%>resources/admin/css/style/blue/cs/cs1.css" rel="stylesheet" type="text/css">

		<link rel="stylesheet" href="<%=basePath%>resources/admin/css/smartMenu/smartMenu.css"  type="text/css">
		<link rel="stylesheet" href="<%=basePath%>resources/admin/css/mmgrid/mmGridAll.css">

		<link rel="stylesheet" href="<%=basePath%>resources/admin/css/ztree/zTreeStyle.css">
		<link rel="stylesheet" href="<%=basePath%>resources/admin/js/jqueryui/jquery-ui.min.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>resources/admin/css/common.css">
		<link rel="stylesheet" href="<%=basePath%>resources/admin/js/tinyeditor/style.css">
		<link rel="stylesheet" href="<%=basePath%>resources/admin/css/webuploader/webuploader.css">
		<link rel="stylesheet" href="<%=basePath%>resources/admin/css/showLoading.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/js/opentip/css/opentip.css"/>


		<script src="<%=basePath%>resources/admin/js/jquery-1.11.3.min.js"></script>
		<script src="<%=basePath%>resources/admin/css/style/blue/js/web_js.js" type="text/javascript"></script>
		<script src="<%=basePath%>resources/admin/js/jqueryui/jquery-ui-1.10.3.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="<%=basePath%>resources/admin/js/My97DatePicker/WdatePicker.js"></script>
		<script src="<%=basePath%>resources/admin/js/smartMenu/jquery-smartMenu.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/admin/js/tinyeditor/tinyeditor.js"></script>



		<style>
			/*操作菜单*/
			.oper_menu{
				margin-left: 0px;
				text-align: left;
				padding:5px;
				background-color: #e6ece6;
			}
			.noborder{border: 1px solid #fff;}
			.sp{color: #3F3F3F;text-decoration: underline;font-size: 12px;cursor: pointer;}
		</style>

		<!--[if lte IE 8]>
		<script src="<%=basePath%>resources/admin/js/respond.js"></script>
		<![endif]-->
		<script src="<%=basePath%>resources/admin/js/bootstrap.js"></script>
		<script src="<%=basePath%>resources/admin/js/mmgrid/plugins.js"></script>
		<script src="<%=basePath%>resources/admin/js/mmgrid/json2.js"></script>
		<script src="<%=basePath%>resources/admin/js/mmgrid/mmGrid.js"></script>
		<script src="<%=basePath%>resources/admin/js/mmgrid/mmPaginator.js"></script>
		<script src="<%=basePath%>resources/admin/js/jqueryui/jquery-ui.js"></script>
		<script src="<%=basePath%>resources/admin/js/index.js"></script>
		<script src="<%=basePath%>resources/admin/js/ztree/jquery.ztree.core-3.5.js"></script>
		<script src="<%=basePath%>resources/admin/js/ztree/jquery.ztree.excheck-3.5.js"></script>
		<script src="<%=basePath%>resources/admin/js/ztree/jquery.ztree.exhide-3.5.js"></script>
		<script src="<%=basePath%>resources/admin/js/jquery.validate.min.js"></script>
		<script src="<%=basePath%>resources/admin/js/jquery-validate-methods.js"></script>
		<script src="<%=basePath%>resources/admin/js/form/jquery.form.js"></script>
		<script src="<%=basePath%>resources/admin/js/cookie/jquery.cookie.js"></script>
		<script src="<%=basePath%>resources/admin/js/hogan/Hogan.js"></script>
		<script src="<%=basePath%>resources/admin/js/hogan/lj_template.js"></script>
		<script src="<%=basePath%>resources/admin/js/opentip/js/opentip-jquery.min.js"></script>
		<script src="<%=basePath%>resources/admin/js/opentip/js/opentip-jquery-excanvas.min.js"></script>
		<script src="<%=basePath%>resources/admin/js/common.js"></script>
		<script src="<%=basePath%>resources/admin/js/lazyload/jquery.lazyload.js"></script>
		<script src="<%=basePath%>resources/admin/js/webuploader/webuploader.js"></script>
		<script src="<%=basePath%>resources/admin/js/layui/layui.js"></script>
		<script src="<%=basePath%>resources/admin/js/jquery.showLoading.js"></script>

	</head>
	
	<body onresize="resizeWindow()">
		<div class="top">
			<div class="logo" id="subSystemDIv">
			</div>
			<span style="display: none;">
				<a href="javascript:void(0);" class="rent" onclick="optSSO('${appAdminUrl}')">访问百度</a>
			</span>

			<div class="nav" id="topMenuDiv">
			</div>
			<ul class="tool">
				<li class="username" title="${sessionScope.current_user.username}">
					欢迎您！${sessionScope.current_user.username}
				</li>
				<li class="personal-center">
					<span >个人中心</span>
					<ul class="personal-center-items">
						<li class="edit-password" onclick="currentUserInfo()">修改密码</li>
					</ul>
				</li>
				<li><a class="exit" href="javascript:exit()">关闭</a></li>
			</ul>
		</div>
		
		<div class="bar_nav ">
			<em></em>
			<a href="javascript:goBackIndex()">首页</a>
			<span id="barSpan">
			</span>
		</div>
		
		
		<div class="adm_body cle_aft adm_page" id="mainContent" style="padding-right: 3px;">
			<div id="mainContent1" style="display:none;">
				
			</div>
				<iframe id="myContentFrame" style="display:none;border:0px;width:100%;height:100%;" src=""></iframe>
			
			
		</div>
		<%@ include file="../common/common.jsp"%>
		<script>
			var commonTalbeHeight = $(window).height() - 80;//表格高度
			var currentSystemId = null;//当前系统ID
			var currentSystem   = null;//当前系统
			var mTwo = [];
			var mThree = [];
			var threeLength = [];
			var selectCadreIds;//干部信息浏览选择过的Id组
			
			loadTopMenu();
			loadContent();
			function loadContent(){
				var currentMenu = eval('('+'${currentMenu}'+')');
				if(currentMenu.menutype==3){
					$('#barSpan').append('<span>&nbsp;&nbsp;-</span><b>'+currentMenu.title+'</b>');	
				}
				if('${url}'!=null&&'${url}'!=''){
				<c:choose>
				<c:when test="${fn:endsWith(url, '.html')}">
					//$('#mainContent').load('<%=basePath%>'+'${url}');	
					$("#mainContent1").hide();
					$("#myContentFrame").show();
					document.getElementById('myContentFrame').src='<%=basePath%>'+'${url}';
				</c:when>
				<c:otherwise>
					$("#myContentFrame").hide();
					$("#mainContent1").show();
					$('#mainContent1').load('<%=basePath%>'+'${url}');
				</c:otherwise>
				</c:choose>
				}else{
					loadSubMenu();
				}
				
			}
			
			resetWinHeight();
			function resetWinHeight(){
			}
			
			function loadSubMenu(){
				var str = '<div class="adm_child_sys">';
				var num = 0;//计算二级菜单个数，以配置相应的样式
				var classTwo = 0;//页面展示2列的菜单个数
				var noClass  = 0;//页面展示1列的菜单个数
				var classThree = 0;//页面展示3列的菜单个数
				var display  = "block";
				var maxCol = 6;

				if($(window).width()>1360){
					maxCol = 8;
				}else if($(window).width()>1024){
					maxCol = 7;
				}


				for(var i in mTwo){
					for(var j in mThree){
						if(mTwo[i].id==mThree[j].parent.id){
							threeLength[mTwo[i].id]++;
						}
					}
				}
				for(var j in mTwo){
					if((noClass+classTwo*2+classThree*3)>=maxCol){//前边菜单如果超过最大列数，那么后面的菜单就不再显示
						display = "none";
					}
					num++;
					if(parseInt(threeLength[mTwo[j].id])>8){
						str += '<dl class="three"><dt>'+mTwo[j].title+'</dt><dd class="a'+num+'">';
						classThree++;
					}else if(parseInt(threeLength[mTwo[j].id])>4){
						str += '<dl class="two"><dt>'+mTwo[j].title+'</dt><dd class="a'+num+'">';
						classTwo++;
					}else{
						str += '<dl><dt>'+mTwo[j].title+'</dt><dd class="a'+num+'">';
						noClass++;
					}

					for(var k in mThree){
						if(mTwo[j].id==mThree[k].parent.id){
							str += '<a  href="#"><em onclick="javascript:loadSub(\''+mThree[k].id+'\',\''+mThree[k].menutype+'\')"><img src="<%=basePath%>resources/admin/img/menuicon/'+mThree[k].bigicon+'"></em><span>'+mThree[k].title+'</span></a>';
						}
					}
					str += '</dd></dl>';
				}
				str += '</div>';
				//console.log(str);
				$('#mainContent').append(str);

			}
			
			function goBackIndex(){
				window.location.href="<%=basePath%>index/index.do";
			}
			
			/**
			* 退出系统
			*/
			function exit(){
				openConfirm('确定退出系统？',function(){
					window.location.href="<%=basePath%>login/logout.do";		
				});
			}
			
			var cacheDictCode = [];
			function findDictByCode(srcTable, srcField, code) {
				if (code == '' || code == null) {
					return "";
				}
				
				var cacheName = srcTable + srcField + code;
		   		for(var i = 0; i< cacheDictCode.length; i++){
		   			if(cacheDictCode[i].cacheName == cacheName){
		   				return cacheDictCode[i].codeName;
		   			}
		   		}
				
				$.ajax({
					url: "<%=basePath%>common/dict/findDictByCode.do",
					type: "POST",
					data: {
						srcTable: srcTable,
						srcField: srcField,
						code: code
					},
					dataType: "json",
					cache: true,
					async: false,
					success: function(data) {
						if (data.codeAbr1 != '') {
							code = data.codeAbr1;
			   				cacheDictCode.push({cacheName:cacheName, codeName:code});
						}
					}
				});
		        return code;
			}
			
			function findUnitByUnitCode(code){
				var name = "";
				$.ajax({
					type : 'post',
					url  : '<%=basePath%>common/dict/findUnitByUnitCode.do',
					data : {code : code},
					dataType : 'json',
					cache: true,
					async: false,
					success : function(data){
						//console.log(data[0][5]);
						if (data[0]!=null&&data[0][5]!=null) {
							name = data[0][5];
						}
					}
				});
				return name;
			}
			
			function loadTopMenu(){
				var menuOne = eval('('+'${menuOne}'+')'),
				menuTwo = eval('('+'${menuTwo}'+')'),
				menuThree = eval('('+'${menuThree}'+')');
				str = '',
				barStr = '',
				menuStr = '',
				flag = true,
				kj = 0,
				kjStr = '';
				
				var topMenuIds = [];
				topMenuIds = eval('('+'${topMenuIdList}'+')');
				
				for(var i in menuOne){
					/*加载左上角子系统列表*/
					if(flag){
						str += '<em></em><b>'+menuOne[i].title+'</b><div class="logo_nav" >';
						//加载面包屑导航
						barStr += '<span>-</span><a href="javascript:loadSub(\''+menuOne[i].id+'\',\''+menuOne[i].menutype+'\')">'+menuOne[i].title+'</a>';
						
						for(var j in menuTwo){	
							if(menuOne[i].id==menuTwo[j].parent.id){
								threeLength[menuTwo[j].id] = 0;
								menuStr += '<dl>' +
									'<dt>' +
										'<a href="#"><div style="height:60px;line-height:60px;">'
									+menuTwo[j].title+'</div>' +
										'</a>' +
									'</dt>' +
									'<dd>';
								mTwo.push(menuTwo[j]);
								for(var k in menuThree){
									if(menuTwo[j].id==menuThree[k].parent.id){
										menuStr += '<a class="menu_two" href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><img width="18" src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].bigicon+'">&nbsp;&nbsp;'+menuThree[k].title+'</a>';
										mThree.push(menuThree[k]);
										//默认情况下加载前六项作为快捷入口
										if(topMenuIds.length == 0 && kj<6){
											kj++;
											kjStr += '<a href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><em><img width="18" src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].bigicon+'"></em>'+menuThree[k].title+'</a>';
										}
									}
									//选出符合id的菜单进行添加
									if($.inArray(menuThree[k].id,topMenuIds) != -1 && kj<topMenuIds.length){
										kj++;
										kjStr += '<a href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><em><img width="26" src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].bigicon+'"></em>'+menuThree[k].title+'</a>';
									}
								}
							}
							menuStr += '</dd></dl>';
						}
						
						
					}else{
						str += '<a href="javascript:loadSub(\''+menuOne[i].id+'\',\''+menuOne[i].menutype+'\')">'+menuOne[i].title+'</a>';
					}
					flag = false;
					/*加载左上角子系统列表*/
					
				}
				str += '</div>';
				$('#subSystemDIv').append(str);
				//console.log(menuStr);
				$('#topMenuDiv').append(menuStr);
				$('#barSpan').append(barStr);
				$('#kjMenu').html(kjStr);
			}
			
			function loadSub(menuId,menuType){
				window.location.href="<%=basePath%>index/subIndex.do?id="+menuId+"&type="+menuType;
			}
			
			//显示当前用户信息窗口
			function currentUserInfo(){
				openWindow('当前用户信息','sys/user/currentUserInfo.do',1,500,300,{});
			}
		   	
		   	function getFormData(type, nowFormId) {
				if (nowFormId != null) {
					var formObject = document.getElementById(nowFormId);
					if (type == 1) {
						oldData = "";
						for (var i = 0; i < formObject.length; i++) {
							if (formObject.elements[i].type == 'checkbox') {
								oldData += formObject.elements[i].checked;
							} else {
								oldData += formObject.elements[i].value;
							}
						}
					} else {
						newData = "";
						for (var i = 0; i < formObject.length; i++) {
							if (formObject.elements[i].type == 'checkbox') {
								newData += formObject.elements[i].checked;
							} else {
								newData += formObject.elements[i].value;
							}
						}
					}
				}
			}
			
			function checkDataChange(oldData, newData) {
				if(oldData != newData) {
					changeFlag = true;
				} else {
					changeFlag = false;
				}
			}
			
			/**
			* 根据窗口大小调整菜单文字尺寸
			*/
			function resetMenuFont(){
				//默认字体大小
				var logoFontSize = 24;
				var menuFontSize = 18;
				var menuWidth = 0;
				
				$('.nav dl dt a').css('font-size', menuFontSize+'px');
				$('.logo b').css('font-size', logoFontSize+'px');
				$('.nav dl').each(function(){
					menuWidth += $(this).width();
					//console.log('菜单字体：'+menuFontSize+' 宽度：'+$(this).width());
					//console.log('the logo font size: '+logoFontSize+' the menu font size: '+menuFontSize+'  and widt: '+$(this).width());
				});
				while(($('#subSystemDIv').width()+menuWidth+$('.tool').width()+50)>=$('.top').width()){
					if(logoFontSize>16){
						logoFontSize--;
					}else{
						break;
					}
					if(menuFontSize>12) menuFontSize--;//字体太小就不能看了
					$('.nav dl dt a').css('font-size', menuFontSize+'px');
					$('.logo b').css('font-size', logoFontSize+'px');
					
					menuWidth=0;
					$('.nav dl').each(function(){
						menuWidth += $(this).width();
					});
				}
			}
			
			$(document).ready(function(){
			});
		    
			/**
			* 默认的窗口自适应方法。不可删
			*/
		    function adjustWindow(){ }
		    function adjustWindow1(){ }
			
			function resizeWindow(){
				adjustWindow();
				adjustWindow1();
				//resetMenuFont();
			}
			
	        $(document).keydown(function(e){  
	            e = window.event || e;  
	        var code = e.keyCode || e.which;  
	        if (code == 8) {  
	            var src = e.srcElement || e.target;  
	            var tag = src.tagName;  
	            if (tag != "INPUT" && tag != "TEXTAREA") {  
	                e.returnValue = false;    
	                return false;  
	            } else if ((tag == "INPUT" || tag == "TEXTAREA") && src.readOnly == true) {  
	                e.returnValue = false;  
	                return false;    
	            }  
	        }  
	        });

            function optSSO(redirectUrl){
                var newTab = window.open('','_blank');
                $.ajax({
                    type: "post",
                    url:'<%=basePath%>login/genLoginTicket.do',
                    success: function(data) {
                        newTab.location = redirectUrl + '?ticket=' + data;
                    }
                });
            }
	        
		</script>
	</body>
</html>
