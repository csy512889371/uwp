<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 新增字段页面 -->
<body>
	<form action="" method="post" id="newCadreAttrForm">
		<div style="width: 96%;margin: auto;">
			<table class="table table-bordered">
				<tr id="attr_codeNameTr">
					<td><span>字段名(code)：</span></td>
					<td><input type="text" value="" name="propertyName" /></td>
				</tr>
				<tr id="attr_codeTr">
					<td><span>代码(code)：</span></td>
					<td><input type="text" name="propertyCode"/></td>
				</tr>
				<tr>
					<td><span style="font-size:16px;color:red;">*</span><span>展示名：</span></td>
					<td><input type="text" value="" name="name" /></td>
				</tr>
				<tr>
					<td><span>所属信息集：</span></td>
					<td><input type="text" value="${codeAttr.attrCode}" name="infoSet" readOnly="readOnly"/></td>
				</tr>
				<tr>
					<td><span>字段类型：</span></td>
					<td><select name="type" onchange="typeChange(this.value)">
					<my:outOptionsByCode tabName="SYS_COLUMN_SHOW"  colName="type" value="text"/>
					</select></td>
				</tr>
				<tr>
					<td><span>可用：</span></td>
					<td><input type='checkbox' onclick='checkBoxChange2(this,"isEnabled")' />
					<input type="hidden" name="isEnabled" value="0"/></td>
					</tr>
				<tr>
					<td><span>必填：</span></td>
					<td><input type='checkbox' onclick='checkBoxChange2(this,"isRequired")' />
					<input type="hidden" name="isRequired" value="0"/></td>
				</tr>
				 <c:if test="${codeAttr.infoSetType=='cadretype'&&codeAttr.recordType=='multiRecord'}">
					<tr id="showForList">
						<td><span>展示列表项：</span></td>
						<td><input type='checkbox' onclick='checkBoxChange2(this,"isGridItem")' />
						<input type="hidden" name="isGridItem" value="0"/></td>
					</tr>
				</c:if>
				<tr id="rowHeightId" style="display: none;">
					<td><span>行高：</span></td>
					<td><input type='text' name="rowHeight" value="" /></td>
				</tr>
				<tr id="dataFormat" style="display: none;">
					<td><span>时间类型：</span></td>
					<td><select name="dataFormat">
						<option value="">请选择时间类型</option>
						<option value="yyyy">年</option>
						<option value="yyyyMmDd">年月日</option>
					</select></td>
				</tr>
				<tr>
					<td><span style="font-size:16px;color:red;" id="lengthIsRequire">*</span><span>长度限制：</span></td>
					<td><input type='text' id="lengthLimit" name="lengthLimit" value=""/></td>
				</tr>
				<tr>
					<td><span>验证规则：</span></td>
					<td><input type='text' id="newCadreAttrForm_validateRule" name="validateRule" value="" onclick="selectValidateRules(this)" /></td>
				</tr>
				<tr>
					<td><span style="font-size:16px;color:red;">*</span><span>序列：</span></td>
					<td><input type='text' name="seq" value="" /></td>
				</tr>
			</table>
			<!-- 批处理设置，暂时先设置成默认不允许-->
			<input type='hidden' name="allowBatch" value="0" />
		</div>
		<div style="text-align: right;margin-right: 8px;">
			<button type="submit" class="btn-style1">&nbsp;保存&nbsp;</button>
		</div>
	</form>
	<script>
        $('document').ready(function() {
        jQuery.validator.addMethod("englishAndNum",function(value, element){

            var returnVal = false;
            if(/^(?=.*[a-z])[a-z0-9]+/ig.test(value)){
                returnVal = true;
            }

            return returnVal;
        },"必须是纯英文或者英文包含数字!");

        $("#newCadreAttrForm").validate({
			rules:{
				propertyName:{
					englishAndNum:true
				},
				name:{
					required:true
				},
				seq:{
					required:true,
					digits:true
				},
				rowHeight:{
					digits:true,
					max:20
				},
				lengthLimit:{
					required:true,
					digits:true
				}
			},
			messages: {
			}
		});
	   var options = {
			success: function(result){
				openAlert(result.content);
				if(result.type == "success"){
					var attr=JSON.parse(result.params);
					var i=index;
					var row = $("<tr></tr>");
					var td_0 = $(commonTd.render({style:"text-align: center;"}));
					var td_1 = $(commonTd.render({style:"text-align: center;"}));
					var td_2 = $(commonTd.render({style:"text-align: center;"}));
					var td_3 = $(commonTd.render({style:"text-align: center;"}));
					var td_4 = $(commonTd.render({style:"text-align: center;"}));
					var td_5 = $(commonTd.render({style:"text-align: center;"}));
                    var td_6 = $(commonTd.render({style:"text-align: center;"}));
					td_0.html(commonSpan.render({text:attr.seq}));
					td_1.html(commonSpan.render({text:attr.name})+
					commonInput.render({name:"attrs["+i+"].id",type:"hidden",value:attr.id})+
					commonInput.render({name:"attrs["+i+"].name",type:"hidden",value:attr.name})+
					commonInput.render({name:"attrs["+i+"].propertyName",type:"hidden",value:attr.propertyName})+
					commonInput.render({name:"attrs["+i+"].type",type:"hidden",value:attr.type})+
					commonInput.render({name:"attrs["+i+"].infoSet",type:"hidden",value:attr.infoSet})+
					commonInput.render({name:"attrs["+i+"].seq",type:"hidden",value:attr.seq})+
					commonInput.render({name:"attrs["+i+"].propertyCode",type:"hidden",value:attr.propertyCode})+
					commonInput.render({name:"attrs["+i+"].allowBatch",type:"hidden",value:attr.allowBatch})+
					commonInput.render({name:"attrs["+i+"].lengthLimit",type:"hidden",value:attr.lengthLimit})+
					commonInput.render({name:"attrs["+i+"].validateRule",type:"hidden",value:attr.validateRule})+
					commonInput.render({name:"attrs["+i+"].propertyType",type:"hidden",value:attr.propertyType})+
					commonInput.render({name:"attrs["+i+"].rowHeight",type:"hidden",value:attr.rowHeight})+
					commonInput.render({name:"columns["+i+"].fileRelPath",type:"hidden",value:attr.fileRelPath})+
					commonInput.render({name:"columns["+i+"].dataFormat",type:"hidden",value:attr.dataFormat})+
					commonInput.render({name:"columns["+i+"].comment",type:"hidden",value:attr.comment})+
					commonInput.render({name:"columns["+i+"].htmlType",type:"hidden",value:attr.htmlType})
					);
					td_2.html(commonCheckBox.render({clickFunc:"checkBoxChange(this)",text:"启用"})+
					commonInput.render({name:"attrs["+i+"].isEnabled",type:"hidden",value:attr.isEnabled}));
					td_3.html(commonCheckBox.render({clickFunc:"checkBoxChange(this)",text:"必填"})+
					commonInput.render({name:"attrs["+i+"].isRequired",type:"hidden",value:attr.isRequired}));
					td_4.html(commonSpan.render({text:attr.propertyName}));
                    var propertyType = attr.propertyType;
                    var propertyTypeName= "";
                    if(propertyType=="fixed"){
                        propertyTypeName = '固定';
                    } else if (propertyType=="dynamic") {
                        propertyTypeName = '动态';
                    } else if (propertyType=="extra") {
                        propertyTypeName = '额外';
                    }
                    td_5.html(commonSpan.render({text:propertyTypeName}));

				 //   +
				 //   commonButton.render({clickFunc:"deleteCadreAttr(this)",value:"删除"}));
                    var td_6Str='';
                    if(attr.propertyType=="extra"||attr.propertyType=="dynamic"){
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
					var td_3u;
					if(hasItemGrid=='true'){
						 td_3u = $(commonTd.render({style:"text-align: center;"}));
						 td_3u.html(commonCheckBox.render({clickFunc:"checkBoxChange(this)",text:"展示为列表项"})+
												commonInput.render({name:"attrs["+i+"].isGridItem",type:"hidden",value:attr.isGridItem}));
						  row.append(td_3u);
					}
					row.append(td_4);
					row.append(td_5);
                    row.append(td_6);
					$('#attrInfo').append(row);
					if(attr.isEnabled==true){
						td_2.find('input[type="checkBox"]').prop("checked",true);
					}
					if(attr.isRequired==true){
						td_3.find('input[type="checkBox"]').prop("checked",true);
					}
					if(hasItemGrid=='true'&&attr.isGridItem==true){
						td_3u.find('input[type="checkBox"]').prop("checked",true);
					}
					index++;
					closeWindow();
				}
			},
			dataType:  'json',
			url:'<%=basePath%>sys/structure/addCadreAttr.do',
		};
		$("#newCadreAttrForm").ajaxForm(options);
		$('#attr_codeTr').hide();
	});
   	function checkBoxChange2(obj,name){
   		if(obj.checked==true){
			$('#newCadreAttrForm input[name="'+name+'"]').val(1);
		}else{
			$('#newCadreAttrForm input[name="'+name+'"]').val(0);
		}
   		
   	}
   	function typeChange(value){
		if(value=='dataList'){
			$('#attr_codeTr').show();
			$('#attr_codeNameTr').hide();
			$('#attr_codeNameTr input').val('');
			$('#showForList').show();
			$('#rowHeightId').hide();
            $('#dataFormat').hide();
			//$('#attr_codeNameTr span').html("字段名(codeName)：");
		}else if(value=='textarea'){
			$('#attr_codeNameTr').show();
			$('#attr_codeTr').hide();
			$('#attr_codeTr input').val('');
			$('#showForList').hide();
			$('#rowHeightId').show();
            $('#dataFormat').hide();
		}else if(value=='datetemp'){
            $('#attr_codeNameTr').show();
            $('#attr_codeTr').hide();
            $('#attr_codeTr input').val('');
            $('#dataFormat').show();
            $('#rowHeightId').hide();
        }else{
			$('#attr_codeNameTr').show();
			$('#attr_codeTr').hide();
			$('#attr_codeTr input').val('');
			$('#showForList').show();
			$('#rowHeightId').hide();
            $('#dataFormat').hide();
			//$('#attr_codeNameTr span').html("字段名：");
		}

		if(value!='datetemp' && value!='textarea' && value!='multiFile'){
            $('#lengthIsRequire').show();
            $('#lengthLimit').rules("add",{required:true,digits:true});
		}else{
            $('#lengthIsRequire').hide();
            $('#lengthLimit').rules("remove");
		}
	}
	</script>
</body>
