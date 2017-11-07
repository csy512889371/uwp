<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<!-- 单位列表 -->
<style>
	#basicUnitInfoDetail input, #subGroupInfo input{border: none;}
	#basicUnitInfoDetail .td1 {font-weight: bolder;}
	#basicUnitInfoDetail{border:none;}
</style>
<body>
		   <div style="overflow-y: auto; overflow-x: auto;border: none;" id="unitInfoDiv"> 
				 <div>
					<ul>
						<li id="basicUnitDetail"><a href="#basicUnitInfoDetail">单位基本信息集 </a></li>
					</ul>
				</div>  
				<!-- 单位基本信息集 单位-->
				<div id="basicUnitInfoDetail" style="overflow: auto;">
				 	<form action="" method="post" id="basicUnitInfoForm" name="basicUnitInfoForm">
				 		<table class="table table-bordered" style="width: 100%;">
							<tr class="hidde2">
								<td class="td1">上级单位名称</td>
								<td><input type="text" id="unitName" name="unitName" readonly="readonly" value="${b01Hiber.parentB01Hiber.unit.b0101}"></td>
								<td class="td1"><span style="color:red;">*</span>单位隶属关系</td>
								<td ><input type="text" id="b0124n3" name="b0124n" readonly value="${b01Hiber.unit.b0124n}"><%--${indexDict:getCodeName('UNIT_INFO','b0124',b01Hiber.unit)}--%>
								<input type="hidden" name="b0124n2" value="${ b01Hiber.unit.b0124}">
								</td>
							</tr>
							<tr class="show1">
								<td class="td1" ><span style="color:red;">*</span><c:choose><c:when test="${b01Hiber.unit.unitType=='SUBGROUP'}">分组全称</c:when><c:otherwise>单位全称</c:otherwise></c:choose></td>
								<td ><input  type="text"	id="b0101" name="b0101" readonly value="${b01Hiber.unit.b0101}"></td>
								<td class="td1" ><c:choose><c:when test="${b01Hiber.unit.unitType=='SUBGROUP'}">分组简称</c:when><c:otherwise>单位简称</c:otherwise></c:choose></td>
								<td ><input  type="text" id="b0104" name="b0104" readonly value="${b01Hiber.unit.b0104}"></td>
							</tr>
							<tr class="hidde1">
								<td class="td1" ><span style="color:red;">*</span>单位性质类别</td>
								<td ><input type="text" id="b0131n3" name="b0131n" readonly value="${b01Hiber.unit.b0131n}"></td>
								<td class="td1" >单位代码</td>
								<td ><input type="text" id="b0111" name="b0111" readonly value="${b01Hiber.unit.b0111}"></td>
							</tr>
							<tr>
								<td class="td1" >管理权限</td>
								<td ><input type="text" id="b0120n" name="b0120" readonly value="${b01Hiber.unit.b0120n}"></td>
								<td class="hidde3 td1" style="display: none">单位类型</td>
								<td class="hidde3" colspan="3"><input type="text"  id="unitType" name="unitType"  readonly  value="分组" style="display: none"></td>
							</tr>
					  </table>
					 </form>
				 </div>
			</div>
	</body>
	<script>
	
	$("#unitInfoDiv").tabs({
		  collapsible: false
		}); 
	
	$('#basicUnitInfoDetail').height(commonTalbeHeight-20);
	
	/**
	* 窗口自适应
	*/
	function adjustWindow(){
		//$('#unitTreeTab').height($(window).height()-175);
		$('#basicUnitInfoDetail').height($(window).height()-200);
	}
	
	/*$(".hidde3").hide();*/
	$("#basicUnitDetail").show();
	
	
</script>