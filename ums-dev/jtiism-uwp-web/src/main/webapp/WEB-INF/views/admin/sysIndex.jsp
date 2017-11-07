<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="../common.inc"%>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<meta content="yes" name="apple-mobile-web-app-capable" />
		<meta content="black" name="apple-mobile-web-app-status-bar-style" />
		<meta content="telephone=no" name="format-detection" />
		<meta content="email=no" name="format-detection" />
		<title>信息管理平台</title>

		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/css/initStyle.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/css/sysIndex/sysIndex.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/css/common.css">

	    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/js/jqueryui/jquery-ui.min.css"  />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/css/showLoading.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/admin/js/opentip/css/opentip.css"/>

		<!--[if lte IE 8]>
		<script src="<%=basePath%>resources/admin/js/respond.js"></script>
		<![endif]-->
		<script src="<%=basePath%>resources/admin/js/jquery-1.11.3.min.js"></script>
		<script src="<%=basePath%>resources/admin/css/style/blue/js/web_js.js" type="text/javascript"></script>
		<script src="<%=basePath%>resources/admin/js/bootstrap.js"></script>
		<script src="<%=basePath%>resources/admin/js/jqueryui/jquery-ui.js"></script>
		<script src="<%=basePath%>resources/admin/js/index.js"></script>
		<script src="<%=basePath%>resources/admin/js/jquery.validate.min.js"></script>
		<script src="<%=basePath%>resources/admin/js/jquery-validate-methods.js"></script>
		<script src="<%=basePath%>resources/admin/js/form/jquery.form.js"></script>
		<script src="<%=basePath%>resources/admin/js/cookie/jquery.cookie.js"></script>
		<script src="<%=basePath%>resources/admin/js/hogan/Hogan.js"></script>
		<script src="<%=basePath%>resources/admin/js/hogan/lj_template.js"></script>
		<script src="<%=basePath%>resources/admin/js/hogan/lj_template_update.js"></script>
		<script src="<%=basePath%>resources/admin/js/opentip/js/opentip-jquery.min.js"></script>
		<script src="<%=basePath%>resources/admin/js/opentip/js/opentip-jquery-excanvas.min.js"></script>
		<script src="<%=basePath%>resources/admin/js/common.js"></script>
		<script src="<%=basePath%>resources/admin/js/jquery.messager.js"></script>
		<script src="<%=basePath%>resources/admin/js/jquery.showLoading.js"></script>

	</head>
	
	<body onresize="resetWinHeight()">
		<div class="top">
			<div class="header-title" style="display: none;">
				<h1 id="header-title-text">&nbsp-&nbsp信息管理平台</h1>
				<span style="display: none;">
					<a href="javascript:void(0);" class="rent" onclick="optSSO('${appAdminUrl}')">访问百度</a>
				</span>

			</div>
			<ul class="tool">
				<li>欢迎&nbsp!&nbsp${sessionScope.current_user.username}</li>
				<li class="personal-center">
					<span >个人中心</span>
					<ul class="personal-center-items">
						<li class="edit-password" onclick="currentUserInfo()">修改密码</li>
					</ul>
				</li>
				<li><a class="exit" href="javascript:exit()">关闭</a></li>
			<ul/>
		</div>

		<div class="message-box">
			<ul class="message-tip">
				<c:forEach var="announce" items="${sysAnnounces}">
					<li>${announce.saninf001}</li>
				</c:forEach>
			</ul>
		</div>

		
		<div class="adm_body cle_aft">
			<a class="ico_img lef"></a>
			<a class="ico_img rig"></a>
			<div class="foot_ico"></div>
			<div class="adm_b">
				<div class="adm_z" id="allMenuDiv">
				</div>
			</div>
		</div>
		
		<!-- 公共页面 -->
		<%@ include file="../common/common.jsp"%>
		<script>
			var allMenu = [];
			//添加快捷入口html
			var kjStr = '';
			//添加自定义快捷入口按钮html
			var addStr = '';
			//传递首页显示的快捷入口id
			var selectIds = [];
			var firstLogin = '${firstLogin}';
			//var isTipsChangePwd = ${isTipsChangePwd};
            var alertMsg = '';//IE浏览器下Flash安装信息提示。
			$(document).ready(function () {
				alertMsg = browserChecker();
				alertMsg =flashChecker()+alertMsg;
				if(alertMsg!=='') {
					openAlert(alertMsg);
				}
            });

			//检测IE浏览器版本
			function browserChecker() {
				var browser_name = navigator.appName,
					browser_version = navigator.appVersion,
					version_arr = browser_version.split(";"),
					version = version_arr[1].replace(/\s/g, "");
				if (browser_name == "Microsoft Internet Explorer") {
				   if (version == "MSIE5.0" || version == "MSIE7.0") {
					   return "您的浏览器版本过低，请下载IE8及以上版本！";
				   } else {
				       return "";
				   }
				} else {
				    return "";
				}
            }
			//检测Flash
            function flashChecker() {
                var hasFlash = 0; //是否安装了flash
                var flashVersion = 0; //flash版本
                //IE浏览器
                if ("ActiveXObject" in window) {
                    try {
                        var swf = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
                        hasFlash = 1;
                        VSwf = swf.GetVariable("$version");
                        flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
                    } catch (e) {}
                    //非IE浏览器
                } else {
                    //只检测IE浏览器，非IE浏览器直接赋值1。
                    hasFlash = 1;
                }
                if(hasFlash==0) {
                    return "请安装Flash！";
				} else {
                    return "";
				}
            }

            /**
			* 退出系统
			*/
			function exit(){
				openConfirm('确定退出系统？',function(){
					window.location.href="<%=basePath%>login/logout.do";		
				});
			}
			resetWinHeight();
			var dlWidth = 115;//单列宽度
			var twoWidth = 250;//双列宽度
			var maxCol = 6;
			function resetWinHeight(){
				//页面高度自适应浏览器高度
//				$('.adm_body').height($(window).height()-90);
//				console.log($(window).height());
//                console.log("+++++"+$(document).height());
				//小分辨率中，切换菜单的圆形按钮要靠底部一些
				if($(window).height()<700){
					$('.adm_body .foot_ico').css('bottom','10px');	
				}else{
					$('.adm_body .foot_ico').css('bottom','30px');
				}
				
				//.adm_child dl    .adm_child dl.two
			}
			loadMenu();
			function loadMenu(){
					allMenu = eval('('+'${allMenu}'+')'),
					menuOne = [],
					menuTwo = [],
					menuThree = [],
					threeLength = {},
					menuStr = '';
				for(var i in allMenu){
					if(allMenu[i].menutype==1){
						menuOne.push(allMenu[i]);
					}else if(allMenu[i].menutype==2){
						menuTwo.push(allMenu[i]);
						threeLength[allMenu[i].id] = 0;
					}else{
						menuThree.push(allMenu[i]);
					}
				}
				
				for(var i in menuTwo){
					for(var j in menuThree){
						if(menuTwo[i].id==menuThree[j].parent.id){
							threeLength[menuTwo[i].id]++;
						}
					}
				}
				//console.log(threeLength);
				
				var kj = 0;
				var screenHegiht = $(window).height();
				var colNum = 4;
				if(screenHegiht<600) colNum = 3;
				var topMenuIds = eval('('+'${topMenuIdList}'+')');
				if($(window).width()>1024){
					maxCol = 7;
				}

				for(var i in menuOne){
					menuStr = '<div class="adm_child"><div class="adm_c">';
					var num = 0;//计算二级菜单个数，以配置相应的样式
					var noClass  = 0;//页面展示1列的菜单个数
					var classTwo = 0;//页面展示2列的菜单个数
					var classThree = 0;//页面展示3列的菜单个数
					var display  = "block";
					for(var j in menuTwo){
						if((noClass+classTwo*2+classThree*3)>=maxCol){//前边菜单如果超过最大列树，那么后面的菜单就不再显示
							display = "none";
						}
						if(menuOne[i].id==menuTwo[j].parent.id){
							num++;
							if(parseInt(threeLength[menuTwo[j].id])>8){
								menuStr += '<dl class="three" style="display:'+display+'"><dt>'+menuTwo[j].title+'</dt><dd class="a'+num+'">';
								classThree++;
							}else if(parseInt(threeLength[menuTwo[j].id])>4){//二级菜单大于4个的时候就分2列
								menuStr += '<dl class="two" style="display:'+display+'"><dt>'+menuTwo[j].title+'</dt><dd class="a'+num+'">';
								classTwo++;
							}else{
								menuStr += '<dl style="display:'+display+'"><dt>'+menuTwo[j].title+'</dt><dd class="a'+num+'">';
								noClass++;
							}
							
							for(var k in menuThree){
								if(menuTwo[j].id==menuThree[k].parent.id){
//								    //是否需要消息通知图标
								    if(menuThree[k].id==24226||menuThree[k].id==20140){
                                        menuStr += '<a style="display:'+display+'" href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><em><i>1</i><img src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].bigicon+'" onerror="\'this.src=<%=basePath%>resources/admin/img/menuicon/ico_b01.png\'"></em><span>'+menuThree[k].title+'</span></a>';
									}else{
                                        menuStr += '<a style="display:'+display+'" href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><em><img src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].bigicon+'" onerror="\'this.src=<%=basePath%>resources/admin/img/menuicon/ico_b01.png\'"></em><span>'+menuThree[k].title+'</span></a>';
									}

									//默认情况下加载前六项作为快捷入口
									if(topMenuIds.length == 0 && kj<6){
										kj++;
										kjStr += '<a href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><em><img src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].smallicon+'" onerror="\'this.src=<%=basePath%>resources/admin/img/menuicon/ico_m01.png\'"></em>'+menuThree[k].title+'</a>';
										selectIds.push(menuThree[k].id); 
									}
									//选出符合id的菜单进行添加
									if($.inArray(menuThree[k].id,topMenuIds) != -1 && kj<topMenuIds.length){
										kj++;
										kjStr += '<a href="javascript:loadSub(\''+menuThree[k].id+'\',\''+menuThree[k].menutype+'\')"><em><img src="<%=basePath%>resources/admin/img/menuicon/'+menuThree[k].smallicon+'" onerror="\'this.src=<%=basePath%>resources/admin/img/menuicon/ico_m01.png\'"></em>'+menuThree[k].title+'</a>';
										selectIds.push(menuThree[k].id); 
									}
								}
							}
							menuStr += '</dd></dl>';
						}
					}
					menuStr+='</div></div>';
					$('#allMenuDiv').append(menuStr);
				}
				
				addStr += '<a href="#" onclick="quickEntry()"><em><img src="<%=basePath%>resources/admin/css/style/blue/cs/img/ico_add.png" style="width:16px;height:16px"></em></a>';
				kjStr += addStr;
				$('#kjDIv').append(kjStr);

			
				if($(window).width()<=1024){
					$('.adm_child dl').css('width','12.5%');
					$('.adm_child dl.two').css('width','25.5%');
				}
				
				$('.adm_child dl.three').css('width','38%');
			}
			
			function loadSub(menuId,menuType){
				window.location.href="<%=basePath%>index/subIndex.do?id="+menuId+"&type="+menuType;
			}
			
			document.onkeydown = function(event){
				var e = event || window.event || arguments.callee.caller.arguments[0]; 
				var d = e.srcElement || e.target; 
				if(e&&e.keyCode==8){
					return (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA' || d.readOnly == true || d.disabled == true) ? true : false ;
				}
			}
			
			//显示当前用户信息窗口
			function currentUserInfo(){
				openWindow('当前用户信息','sys/user/currentUserInfo.do',1,500,300,{});
			}
			
			//显示自定义快捷入口窗口
			function quickEntry(){
				openWindow('自定义快捷入口','sys/menu/addQuickEntry.do',1,500,400,{});
			}
			
			if (firstLogin == 'true') {
				showMsg();
				firstLogin = 'false';
			}
			
			function loadMsg(msgId){
				var menuId = '';
				$.ajax({
					type: 'post',
					async: false,
					url: '<%=basePath%>sys/msgRemind/getMenuIdByMsg.do',
					data: {msgId : msgId},
					success: function(data) {
						menuId = data;
					}
				});
				var url = "sys/msgRemind/msgIndex.do?msgId="+msgId;
				url = encodeURI(url);
				window.location.href="<%=basePath%>index/subIndex.do?id="+menuId+"&url="+url;
			}
			
			/**
			 * 右下角消息提示框
			 */
			function showMsg() {
				// URL
				var url = 'javascript:loadSub(20140,3)';
				// 显示条数
				var maxNum = 5;
				// 内容
				var content = '';
				$.ajax({
					type: 'post',
					url: '<%=basePath%>sys/msgRemind/findTop.do',
					data: {maxNum : maxNum},
					success: function(data) {
						if (data.msgList.length > 0) {
							for (var i = 0; i < data.msgList.length; i++) {
								if (data.msgList[i].skipUrl == null) {
									content += '<a href="#">' + data.msgList[i].title + ':' + data.msgList[i].contents + '</a><br>';
								} else {
									//content += '<a href="javascript:loadSub('+data.menuIdMap[i]+','+data.menuTypeMap[i]+',\''+data.msgList[i].skipUrl+'\',\''+data.msgList[i].param+'\')">' + data.msgList[i].title + ':' + data.msgList[i].contents + '</a><br>';
									content += '<a href="javascript:loadMsg(\''+data.msgList[i].msgRemindId+'\')">' + data.msgList[i].title + ':' + data.msgList[i].contents + '</a><br>';
								}
							}
							// 消息提示
							$.messager.lays(250, 200);
						    $.messager.anim('slow', 1000);
						    $.messager.show("短信息", content, 5000, url);
						} else {
							
						}
					}
				});
			}

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
