<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
<!-- 菜单图标选择 -->
<style>
	.icon_div{float:left;margin-right:10px;cursor:pointer;margin-bottom: 10px;border: 3px solid #fff;}
	.icon_div img{height:20px;}
</style>
<body>
	<div style="height:290px;overflow: auto;">
		一级菜单图标
		<div style="padding-left: 12px;height:230px;border: 1px solid #e8f2fe;">
			<c:forEach items="${topList}" var="ico" varStatus="i">
				<div class="icon_div" id="${ico}" ondblclick="saveIcon('${ico}')" onclick="chooseIcon('${ico}')">
					<img src="<%=basePath%>resources/admin/img/menuicon/${ico}.png" style="height:20px;" title="${ico}.png"><br>
				</div>
			</c:forEach>
		</div><br>
		二级菜单图标
		<div style="padding-left: 12px;height:200px;border: 1px solid #e8f2fe;">
			
			<c:forEach items="${menuList}" var="ico" varStatus="i">
				<div class="icon_div" id="${ico}" ondblclick="saveIcon('${ico}')" onclick="chooseIcon('${ico}')">
					<img src="<%=basePath%>resources/admin/img/menuicon/${ico}.png" style="height:20px;" title="${ico}.png"><br>
				</div>
			</c:forEach>
		</div>
	</div>
	<div class="btn_div">
		<br>
		<input type="button" class="btn-style1" onclick="iconSelected()" value="确 定">
	</div>
	<script>
		var selectedIcon = '';
		
		
		if('${icon}'!=null&&'${icon}'!=''){
			selectedIcon = '${icon}';
			$('#${icon}').css('border','2px solid #2898fd');
		}
	
		/**
		* 保存选择到界面
		*/
		function iconSelected(){
			if (typeof String.prototype.startsWith != 'function') {//解决IE不支持startsWith 
			    String.prototype.startsWith = function (prefix){  
			    	return this.slice(0, prefix.length) === prefix;  
			    };  
		    } 
			if(selectedIcon.startsWith("nav_")){
				$('#smallicon').val(selectedIcon+'.png');
				$('#bigicon').val(selectedIcon+'.png');
			}else{
				$('#smallicon').val(selectedIcon+'.png');
				$('#bigicon').val(selectedIcon.replace('ico_m','ico').replace('ico2_m','ico2')+'.png');
			}
			$('#menuIcon').attr('src','<%=basePath%>resources/admin/img/menuicon/'+selectedIcon+'.png');
			closeWindow(2);
		}
		
		/**
		* 双击保存图标
		*/
		function saveIcon(id){
			chooseIcon(id);
			iconSelected();
		}

		/**
		* 选中图标
		*/
		function chooseIcon(id){
			selectedIcon = id;
			$('#'+id).css('border','3px solid #2898fd');
			$('#'+id).siblings().css('border','3px solid #fff');
		}
		
	</script>
</body>