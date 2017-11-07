<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="../../common.inc"%>
<body>
	<div style="width:100%;">
		<!-- 标签 -->
		<div id="converseDetailTab" style="height:100%;border: none;">
				<!-- 日志信息 -->
				<div id="basic_div" class="table_div">
					<table class="table table-bordered">
					<tbody>
					<tr>
							<td width="30%">操作员：</td>
							<td width="70%"><div id="operator">${log.operator}</div></td>
							
						</tr>
						<tr>
							<td>IP：</td>
							<td colspan="3"><div id="ip">${log.ip}</div>
							</td>
						</tr>
						<tr>
							<td>操作集：</td>
							<td><div id="operateSet">${log.operateSet}</div>
							</td>
							
						</tr>
						<tr>
							<td>dataid：</td>
							<td><div id="operateSet">${log.dataId}</div>
							</td>
							
						</tr>
						<tr>
							<td>操作：</td>
							<td>
								<div id="operateModule">
									<c:choose>
										<c:when test="${log.operateType=='update'}">更新信息</c:when>
										<c:when test="${log.operateType=='insert'}">新增信息</c:when>
										<c:when test="${log.operateType=='delete'}">删除信息</c:when>
										<c:otherwise>${log.operateType}</c:otherwise>
									</c:choose>
								</div>
							</td>
						</tr>
						<tr>
							<td>操作前的值：</td>
							<td><div id="operateModule"> ${log.oldVals}</div>
							</td>
						</tr>
						<tr>
							<td>操作后的值：</td>
							<td><div id="operateModule"> ${log.newVals}</div>
							</td>
						</tr>
						<tr>
							<td>操作日期：</td>
							<td><div id="operateDate">${log.operateDate}</div>
							</td>
						</tr>
						<tr>
							<td>备注：</td>
							<td><div id="operateDate">${fn:replace(log.remark,";",";<br/>")}</div>
							</td>
						</tr>
					</tbody>
						
					</table>
				</div>
		</div>
	</div>
	<script>
		
		
	</script>
</body>