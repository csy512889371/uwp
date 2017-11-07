<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<body>
	<div>
		<div  class="oper_menu" style="height:30px;">
			<div id="addCadreAttrDiv" style="display: inline;">
				<auth:auth ifAllGranted="codesUpt:add">
					<button class="btn-style4" onclick="toAddCadreAttr()"><i class="icon-plus"></i> 新增属性</button>
				</auth:auth>
			</div>
		</div>
		<form action="" method="post" id="basicInfoAttrsForm">
			<div id="basicInfoField" style="overflow: auto;border: 1px solid #cccccc;">
				<table class="table table-bordered">
					<thead>
						<tr>
							<td style="text-align: center;width:5%;">序列号</td>
							<td style="text-align: center;width:25%;">名称</td>
							<td style="text-align: center;width:10%;">可用</td>
							<td style="text-align: center;width:10%;">必填</td>
							<c:if test="${hasItemGrid}">
							<td style="text-align: center;width:10%;">展示列表项</td>
							</c:if>
							<td style="text-align: center;width:15%;">字段名</td>
							<td style="text-align: center;width:10%;">类型</td>
							<td style="text-align: center;width:15%;">操作</td>
						</tr>
					</thead>
					<tbody id="attrInfo">
	
					</tbody>
				</table>
			</div>
			<div style="margin-left:85%;padding-top: 5px;">
				<auth:auth ifAllGranted="codesUpt:save">
					<button class="btn-style1" type="submit">&nbsp;保 存&nbsp;</button>
				</auth:auth>
			</div>
		</form>
	</div>
	<script type="text/javascript">
		$('#basicInfoField').height(commonTalbeHeight-30);
		var commonInput=Hogan.compile(LE_TEMPLATE.inputSimple);
		var commonCheckBox=Hogan.compile(LE_TEMPLATE.commonCheckBox);
		var commonTd=Hogan.compile(LE_TEMPLATE.commonTd);
		var commonSpan=Hogan.compile(LE_TEMPLATE.commonSpan);
		var commonButton=Hogan.compile(LE_TEMPLATE.commonButton);
        var attrInfo=JSON.parse('${INFO}');
		var attrId='${attrId}';
		var index=0;//新增数据的下标
		var hasItemGrid='${hasItemGrid}';
		$('document').ready(function() {
			var options = {
			       success: function(result){
	            		openAlert(result.content);
	            		if(result.type=="success"){
	            			
	            		}
            		},
			        dataType:  'json',
			        url:'<%=basePath%>sys/structure/saveCaderInfoBasicAttrs.do',
			    };  
			$("#basicInfoAttrsForm").ajaxForm(options);
			initData();

		});
		//这里的各个控件的排列顺序不能改
		function initData() {
			for(var i in attrInfo){
				var row = $("<tr></tr>");
				var td_0 = $(commonTd.render({style:"text-align: center;"}));
		        var td_1 = $(commonTd.render({style:"text-align: center;"}));
		        var td_2 = $(commonTd.render({style:"text-align: center;"}));
		        var td_3 = $(commonTd.render({style:"text-align: center;"}));
		        var td_4 = $(commonTd.render({style:"text-align: center;"}));
		        var td_5 = $(commonTd.render({style:"text-align: center;"}));
		        var td_6 = $(commonTd.render({style:"text-align: center;"}));
		        td_0.html(commonSpan.render({text:attrInfo[i].seq}));
		        td_1.html(commonSpan.render({text:attrInfo[i].name})+
		        commonInput.render({name:"columns["+i+"].id",type:"hidden",value:attrInfo[i].id})+
		        commonInput.render({name:"columns["+i+"].name",type:"hidden",value:attrInfo[i].name})+
		       	commonInput.render({name:"columns["+i+"].propertyName",type:"hidden",value:attrInfo[i].propertyName})+
		       	commonInput.render({name:"columns["+i+"].type",type:"hidden",value:attrInfo[i].type})+
		        commonInput.render({name:"columns["+i+"].infoSet",type:"hidden",value:attrInfo[i].infoSet})+
		        commonInput.render({name:"columns["+i+"].seq",type:"hidden",value:attrInfo[i].seq})+
		        commonInput.render({name:"columns["+i+"].propertyCode",type:"hidden",value:attrInfo[i].propertyCode})+
		        commonInput.render({name:"columns["+i+"].allowBatch",type:"hidden",value:attrInfo[i].allowBatch})+
		        commonInput.render({name:"columns["+i+"].lengthLimit",type:"hidden",value:attrInfo[i].lengthLimit})+
		        commonInput.render({name:"columns["+i+"].validateRule",type:"hidden",value:attrInfo[i].validateRule})+
		        commonInput.render({name:"columns["+i+"].propertyType",type:"hidden",value:attrInfo[i].propertyType})+
				commonInput.render({name:"columns["+i+"].rowHeight",type:"hidden",value:attrInfo[i].rowHeight})+
				commonInput.render({name:"columns["+i+"].fileRelPath",type:"hidden",value:attrInfo[i].fileRelPath})+
				commonInput.render({name:"columns["+i+"].dataFormat",type:"hidden",value:attrInfo[i].dataFormat})+
				commonInput.render({name:"columns["+i+"].comment",type:"hidden",value:attrInfo[i].comment})+
				commonInput.render({name:"columns["+i+"].htmlType",type:"hidden",value:attrInfo[i].htmlType})
		        );
		        if (attrInfo[i].isEnabled == true||attrInfo[i].propertyType!="fixed") {
			        td_2.html(commonCheckBox.render({clickFunc:"checkBoxChange(this)",text:"启用"})+
			        commonInput.render({name:"columns["+i+"].isEnabled",type:"hidden",value:attrInfo[i].isEnabled}));
		        } else {
		        	 td_2.html(commonInput.render({name:"columns["+i+"].isEnabled",type:"hidden",value:attrInfo[i].isEnabled}));
		        }
		        td_3.html(commonCheckBox.render({clickFunc:"checkBoxChange(this)",text:"必填"})+
		        commonInput.render({name:"columns["+i+"].isRequired",type:"hidden",value:attrInfo[i].isRequired}));
		        td_4.html(commonSpan.render({text:attrInfo[i].propertyName}));
		        
		        var propertyType = attrInfo[i].propertyType;
		        var propertyTypeName= "";
		        if(propertyType=="fixed"){
		        	propertyTypeName = '固定';
		        } else if (propertyType=="dynamic") {
		        	propertyTypeName = '动态';
		        } else if (propertyType=="extra") {
		        	propertyTypeName = '额外';
		        }
		        
		        td_5.html(commonSpan.render({text:propertyTypeName}));
		        var td_6Str='';
		        if(attrInfo[i].propertyType=="extra"||attrInfo[i].propertyType=="dynamic"){
		        	td_6Str+=commonButton.render({clickFunc:"updateBasicInfo(this)",value:"修改"});
		        }
		        
		        td_6Str+=commonButton.render({clickFunc:"viewBasicInfo(this)",value:"查看"});
		        
		  //      if(attrInfo[i].propertyType=="extra"){
		   //     	td_6Str+=commonButton.render({clickFunc:"deleteCadreAttr(this)",value:"删除"});
		   //     }
		        td_6.html(td_6Str);
		        row.append(td_0);
		        row.append(td_1);
		        row.append(td_2);
		        row.append(td_3);
		        if(hasItemGrid=='true'){
		        	 var td_3u = $(commonTd.render({style:"text-align: center;"}));
		        	 td_3u.html(commonCheckBox.render({clickFunc:"checkBoxChange(this)",text:"展示为列表项"})+
		        					        commonInput.render({name:"columns["+i+"].isGridItem",type:"hidden",value:attrInfo[i].isGridItem}));
		        	  row.append(td_3u);
		        }
		        row.append(td_4);
		        row.append(td_5);
		        row.append(td_6);
		        $('#attrInfo').append(row);
		        
		        if(attrInfo[i].isEnabled==true){
					$('#attrInfo tr:eq('+i+') td:eq(2) input[type="checkBox"]').prop("checked",true);;
				}
				if(attrInfo[i].isRequired==true){
					$('#attrInfo tr:eq('+i+') td:eq(3) input[type="checkBox"]').prop("checked",true);;
				}
				if(hasItemGrid=='true'&&attrInfo[i].isGridItem==true){
					$('#attrInfo tr:eq('+i+') td:eq(4) input[type="checkBox"]').prop("checked",true);;
				}
				if(attrInfo[i].propertyType=="fixed"){
					$('#attrInfo tr:eq('+i+') td:eq(2) input[type="checkBox"]').attr("disabled","disabled");
				}
			}
			index=attrInfo.length;
		}
		function checkBoxChange(obj) {
			var item= $(obj).parents("td:eq(0)").find('input[type="hidden"]');
			if(obj.checked==true){
				item.val(true);
			}else{
				item.val(false);
			}
		}
		function toAddCadreAttr(){
			openWindow('新增属性','sys/structure/cadreAttrAdd.do',1,500,460,{attrId:attrId});
		}
		
		function updateBasicInfo(obj){
			var item= $(obj).parents("tr:eq(0)").find('td:eq(1) input[type="hidden"]:eq(0)');
			openWindow('修改属性','sys/structure/cadreAttrUpdate.do',1,500,410,{id:item.val()});
		}
		
		function viewBasicInfo(obj){
			var item= $(obj).parents("tr:eq(0)").find('td:eq(1) input[type="hidden"]:eq(0)');
			openWindow('查看属性','sys/structure/cadreAttrUpdate.do?operType=view',1,500,410,{id:item.val()});
		}
		
		
		function deleteCadreAttr(obj){
			var item= $(obj).parents("tr:eq(0)").find('td:eq(1) input[type="hidden"]:eq(0)');
			openConfirm('确定删除该自定义属性？！',function(){
				$.ajax({
					type : 'post',
					url  : '<%=basePath%>sys/structure/deleteCadreAttr.do',
					data : {id:item.val()},
					success: function(result){
						openAlert(result.content);
						if(result.type == "success"){
							$(obj).parents("tr:eq(0)").remove();
						}
					}
				});				
			});
		}
	</script>
</body>
