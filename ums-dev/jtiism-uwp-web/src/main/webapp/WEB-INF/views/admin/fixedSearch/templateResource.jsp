<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
	.resource_div{padding-left: 20px;text-align: left;}
</style>
<body>
	<div class="resource_div">
		<label class="radio inline">
			<input type="radio" name="choose" checked="checked" value="1" title="将跳转至自定义条件查询页面">通过自定义条件查询添加
		</label><br><br>
		<label class="radio inline" style="display: none;">
			<input type="radio" name="choose" value="2" title="将跳转至组合条件查询页面">通过组合条件查询添加
		</label>
	</div>
	<div class="btn_div">
		<input type="button" class="btn-style1" value="确 定" onclick="gotoSearchPage()">
	</div>
	<script>
		function gotoSearchPage(){
			var choose = $('input[name="choose"]:checked ').val();
			if(choose==1){//先写死。此处应查询菜单表来获取数据
				window.location.href="<%=basePath%>index/subIndex.do?id=6049&type=3";
			}else{
				window.location.href="<%=basePath%>index/subIndex.do?id=6053&type=3";
			}
		}
	</script>
</body>