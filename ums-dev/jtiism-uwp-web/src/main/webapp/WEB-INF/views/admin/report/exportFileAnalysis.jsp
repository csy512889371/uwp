<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>

<body >
<div style="text-align: center;">
	<table class="table table-bordered" style="margin-bottom: 10px;padding-bottom: 0px;">
		<tr height="240px;">
			<td style="margin: 0px;padding: 0px;">
				<div style="height: 230px; position:absolute; top:0; left:0; width:100%; overflow:hidden;">
					<table id="reportTemplateList" class="mmg">
						<tr>
							<th rowspan="" colspan=""></th>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<c:if test="${type=='bySelEnterprise'}">
		<%--	<tr>
				<td>
					&nbsp;导出范围 &nbsp;&nbsp;
					<label class="radio inline"><input type="radio" name="enterpriseSelected" value="selected" checked="checked">选择的企业</label>&nbsp;&nbsp;
					<label class="radio inline"><input type="radio" name="enterpriseSelected" value="all">查询结果</label>
				</td>
			</tr>--%>
		</c:if>
		<tr style="display: none;">
			<td>
				&nbsp;导出类型&nbsp;&nbsp;&nbsp;
				<label class="radio inline"><input type="radio" name="outFileType" value="singleFile" checked="checked">单文件</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label class="radio inline"><input type="radio" name="outFileType" value="mulitFile">多文件</label>
			</td>
		</tr>
	</table>
	<div class="right_btn" style="margin-right: 20px;padding-top: 0px;">
		<input class="btn-style1"  style="width:60px;" type="button" onclick="createExcel()" value="输出">
		<input class="btn-style1"  style="width:60px;" type="button" onclick="closeWindow(2)" value="取消">
	</div>
</div>
<script type="text/javascript">

    var reportTemplateList=null;
    var reportTemplateCols = [
        {title : '模板名称', name : 'templateName' , width : 80, align : 'left', sortable : false, type: 'text',renderer: function(val,item){
            return val;
        }},
        {title : '类别', name : 'templateType' , width : 6, align : 'center', sortable : false, type: 'text',renderer: function(val,item){
            if ('muster' == val) {
                val = "名册";
            } else if ('appointDismiss' == val) {
                val = "呈报表";
            }
            return val;
        }},
        {title : 'id', name : 'id' , width : 10, align : 'center', sortable : false, type: 'text',hidden: true,renderer: function(val,item){
            return val;
        }}
    ];

    function loadReportTemplateList(){
        reportTemplateList = $('#reportTemplateList').mmGrid({
            height : 240,
            cols : reportTemplateCols,
            url : '<%=basePath%>report/engine/searchReportTemplateList.do',
            method : 'post',
            remoteSort : true,
            sortName : 'SECUCODE',
            sortStatus : 'asc',
            multiSelect : false,
            checkCol : true,
            fullWidthRows : true,
            autoLoad : false,
            showBackboard : false,
            params : function() {
                //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
                return {
                    secucode: $('#secucode').val()
                };
            }
        });

        reportTemplateList.on('cellSelected', function(e, item, rowIndex, colIndex){
            if(item.templateType=='muster'){
                $('input[name="outFileType"]:eq(0)').attr("checked","checked");
                $('input[name="outFileType"]:eq(1)').attr("disabled",true);
            }else{
                $('input[name="outFileType"]:eq(1)').removeAttr("disabled");
            }
        }).load({isvalid:true,isStastistic:false});
    }

    $().ready(function(){
        loadReportTemplateList();
    });

    function createExcel(){
        var rows = reportTemplateList.selectedRows();
        var item = rows[0];
        if (typeof(item) == 'undefined') {
            openAlert("请选择导出模板");
            return ;
        }

        var outFileType= $('input[name="outFileType"]:checked').val();

        if("${type}"=="bySelEnterprise"){
            createExcelByEnterprise(item.id,outFileType);
        }else if("${type}"=="byUnit"){
            createExcelByUnit(item.id,outFileType);
        }
    }

    function createExcelByUnit(templId,outFileType){
        showWait('数据生成中，请稍候');
        $.ajax({
            type:'post',
            url:'<%=basePath%>report/engine/createByReportEngineWithUnitIds.do',
            data:{unitIds:"${unitIds}",reportTemp:templId,outFileType:outFileType},
            success : function(result) {
                closeWait();
                if (result.type == "success") {
                    closeWindow(1);
                    if(result.content!=null&&$.trim(result.content)!=''){
                        $('#loadfileNameOfCadreInfo').val(result.content);
                        $('#loadFileOfCadreInfo').submit();
                    }else{
                        openAlert("文件未生成");
                    }
                }else{
                    openAlert(result.content);
                }
            }
        });
    }

    function createExcelByEnterprise(templId,outFileType){
  /*      var enterpriseSelected= $('input[name="enterpriseSelected"]:checked').val();
        console.log(enterpriseSelected);*/
        var enterpriseIds='';
//        if(enterpriseSelected=="selected"){
		if("${enterprises}"){
		enterpriseIds= "${enterprises}";
        }else{
            $.ajax({
                type:'post',
                url:'<%=basePath%>ent/basic/getEtpIdsByToken.do',
                data:{token:"${token}"},
                async: false,
                success : function(result) {
                    enterpriseIds = result.content;
                }
            });
        }
        if(enterpriseIds == null || enterpriseIds == ''){
            openAlert('请选择企业！');
            return;
        }
        showWait('数据生成中，请稍候');
        $.ajax({
            type:'post',
            url:'<%=basePath%>report/engine/createByReportEngine.do',
            data:{enterprises:enterpriseIds, reportTemp:templId,outFileType:outFileType},
            success : function(result) {
                closeWait();
                if (result.type == "success") {
                    closeWindow(1);
                    if(result.content!=null&&$.trim(result.content)!=''){
                        $('#loadfileNameOfCadreInfo').val(result.content);
                        $('#loadRelFolderNameOfInfo').val('${setting:get().dynaReportTempDoc}');
                        $('#loadFileOfCadreInfo').submit();

                    }else{
                        openAlert("文件未生成");
                    }
                }else{
                    openAlert(result.content);
                }
            }
        });
    }
</script>
</body>