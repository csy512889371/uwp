//动态信息集-多记录-表格列
function buildDynaTableCols(columnList) {
	var j=0;
	//添加mmgrid列项
	for (var i = 0; i < length; i ++) {
		var attr=columnList[i];
		if(attr.isGridItem){
			var item={title:attr.name,align : 'left',sortable : false, type : 'text'};
			if(attr.type=="dataList"){
				item.name=attr.propertyCode + '_desc';
				item.renderer=function(val, item){
					return val;
				}
			}else if(attr.type=="datetemp"){
				item.name=attr.propertyName;
				var dataFormat = attr.dataFormat
				item.renderer=function(val,item){
					if(val!=null) {
                        var beforeParse = val.substring(0,val.length-2).replace(new RegExp(/-/gm) ,"/");
                        var date = new Date(beforeParse);
                        if ( dataFormat== 'yyyy') {
                            val = date.getFullYear();
                        } else {
                        	val = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                    	}
					}
					return val;//formats(val);
				}
			}else if(attr.type=="radio"){
                item.name=attr.propertyName;
                var text1 = "是";
                var text2 = "否";
                if(attr.propertyName == 'ENTPLI006'){
                    text1 = "国有";
                    text2 = "集体";
                }
                item.renderer=function(val,item){

                    return val==1?text1:(val==0?text2:'');//formats(val);
                }
            }else{
				item.name=attr.propertyName;
			}
			dynaTableCols[j]=item;
			j++;
		}
	}
}


//动态信息集-多记录-表格
function loadDynaTable(enterpriseId, selectId){
	var searchUrl = null;
	var updateUrl = null;
	if(fromType=='admin'){
        searchUrl = EnvBase.base + 'ent/manage/searchDynaInfoList.do';
        updateUrl = EnvBase.base + 'ent/manage/loadDynaInfoUpdatePage.do';
	}else if(fromType == 'member'){
        searchUrl = EnvBase.base + 'member/ent/searchDynaInfoList.do';
        updateUrl = EnvBase.base + 'member/ent/loadDynaInfoUpdatePage.do';
	}
	dynaInfoList = $('#dynaInfoist').mmGrid({
		height : 300,
		cols : dynaTableCols,
		url : searchUrl,
		method : 'post',
		remoteSort : true,
		sortName : 'SECUCODE',
		sortStatus : 'asc',
		multiSelect : false,
		checkCol : true,
		fullWidthRows : true,
		autoLoad : false,
		showBackboard : false,
		plugins : [$('#pg').mmPaginator({})],
		params : function() {
			//如果这里有验证，在验证失败时返回false则不执行AJAX查询。
			return {
				secucode: $('#secucode').val()
			};
	    }
	});

	dynaInfoList.on('cellSelected', function(e, item, rowIndex, colIndex) {
		if(changeFlag){
			openConfirmNotSave(saveTip,function(){
				$('#dynaInfoFormDiv').load(updateUrl,{infoSet:infoSet,id:item[infoSetKeyName],priv:priv});
				dynaInfoList.select(rowIndex);
			});
		}else{
			$('#dynaInfoFormDiv').load(updateUrl,{infoSet:infoSet,id:item[infoSetKeyName],priv:priv});
		}
		if(changeFlag){
			e.stopPropagation();
		}
	}).load({enterpriseId : enterpriseId,infoSet:infoSet});
	
	dynaInfoList.on('loadSuccess', function(e, data){
			if (isChangeTabUrl) {
				dynaInfoList.select(0);
				var rows = dynaInfoList.selectedRows();
				var item = rows[0];
				if (typeof(item) != 'undefined') {
					$('#dynaInfoFormDiv').load(updateUrl,{infoSet:infoSet,id:item[infoSetKeyName],priv:priv});
				}
			} else {
				if(selectId != null && typeof(selectId) != 'undefined' && selectId != ''){
					var allRows = dynaInfoList.rows();
					var rowsData = [];
					//清空无数据时allRows变成的[undefined]
					for(var i in allRows){
						if(typeof(allRows[i]) != 'undefined'){
							rowsData.push(allRows[i]);
						}
					}
					var paramArr = selectId.split(',');
					if(paramArr.length >= 2){
						for(var i = 0; i < rowsData.length; i++){
							//遍历map
							for(var key in rowsData[i]){
								if(rowsData[i][paramArr[0]] == paramArr[1]){
									dynaInfoList.select(i);
									$('#dynaInfoFormDiv').load(updateUrl,{infoSet:infoSet,id:rowsData[i][infoSetKeyName],priv:priv});
									break;
								}	
							}
						}
					}
				}
			}
			isChangeTabUrl = false;
		});
};

/**
 * 
 * 生成页面表单(含有固定表格)
 */
function generateDynaFormHasFix(columnList, dynStyle) {
	
	var commonSpan = Hogan.compile(eval(dynStyle + '.commonSpan'));
	var commonTd = Hogan.compile(eval(dynStyle + '.commonTd'));
	
	for (var i = 0; i < length; i ++) {
		var row = $("<tr></tr>");
		$('#enterpriseInfoBasicTable tr:eq('+rowlength+')').after(row);
		rowlength=rowlength+1;
		//左边
		var column = columnList[i];
		var td_0 = $(commonTd.render({text : column.name}));
	    var td_1 = $(commonTd.render({colspan : 2}));
	    var td_2 = $(commonTd.render({}));
	    var td_3 = $(commonTd.render({colspan : 3}));
	    td_1.html(makeTdHtml(column, i, dynInforStyle));
	    if (column.isRequired == true) {
	    	td_0.prepend($(commonSpan.render({style:"font-size:15px;color:red;",text:"*"})));
	    }
	    row.append(td_0);
	    row.append(td_1);
	    row.append(td_2);
	    row.append(td_3);
	    
	    i++;
	    if (i >= length) {
	    	break;
	    }
	    //右边
	    column = columnList[i];
	    td_2.html(column.name);
	    td_3.html(makeTdHtml(column, i, dynInforStyle));
	    if(column.isRequired == true) {
	    	td_2.prepend($(commonSpan.render({style : "font-size:15px;color:red;", text : "*"})));
	    }
	}
}

/**
 * 
 * 生成页面表单(含有固定表格) 
 * 额外字段
 */
function generateDynaFormForExtra(columnList, dynStyle ,selector,type) {
    var extralength = columnList.length;
	var commonSpan = Hogan.compile(eval(dynStyle + '.commonSpan'));
	var commonTd = Hogan.compile(eval(dynStyle + '.commonTd'));
	if (!extralength) {
		return;
	}
	for (var i = 0; i < extralength; i ++) {
		var row = $("<tr></tr>");
        if(!type||type=='append'){
            $(selector).append(row);
        }else if(type=='after'){
            $(selector).after(row);
        }else if(type=='before'){
            $(selector).before(row);
        }
		//左边
		var column=columnList[i];
		var td_0 = $(commonTd.render({text : column.name}));
		if (column.isRequired == true) {
			td_0.prepend($(commonSpan.render({style:"font-size:18px;color:red;",text:"*"})));
		}
		if(column.type=='textarea'){
			var td_1 = $(commonTd.render({colspan : 6}));
			td_1.html(makeTdContent(column, i+extralength, dynStyle));
			row.append(td_0);
			row.append(td_1);
			continue;
		}else{
			var td_1 = $(commonTd.render({colspan : 1}));
		    var td_2 = $(commonTd.render({}));
		    var td_3 = $(commonTd.render({colspan : 1}));
			td_1.html(makeTdContent(column, i+extralength, dynStyle));
			row.append(td_0);
			row.append(td_1);
			row.append(td_2);
			row.append(td_3);
			if (i+1 >= extralength) {
				break;
			}
			column = columnList[i+1];
			//如果是textarea 换下一行
			if(column.type=='textarea'){
				continue;
			}
			//右边
			td_2.html(column.name);
			td_3.html(makeTdContent(column, i+extralength, dynStyle));
			if (column.isRequired == true) {
				td_2.prepend($(commonSpan.render({style : "font-size:18px;color:red;", text : "*"})));
			}
			i++;
		}
	}
}




/**
 * 生成页面表单(全动态)
 * type: update 修改页面 , add 新增页面
 */
function generateDynaForm(columnList, dynStyle, type) {

    var commonSpan = Hogan.compile(eval(dynStyle + '.commonSpan'));
    var commonTd = Hogan.compile(eval(dynStyle + '.commonTd'));
    var checkboxtdTest = Hogan.compile(eval(dynStyle + '.checkbox'));
    var checkboxChecked = Hogan.compile(eval(dynStyle + '.checkboxChecked'));
    var inputDate = Hogan.compile(eval(dynStyle + '.inputDate'));
    /*var inputfile = Hogan.compile(eval(dynStyle + '.inputFile'));*/

    $('#infoDynaTable').html('');

    for (var i = 0; i < length; i ++) {
        var column=columnList[i];

        //1.整个tr 在后台生成。
        if(isEntireTr(column.type)) {
            var html = decodeURIComponent(column.htmlStr,"UTF-8")
            var entireTr = $(html);
            var row = $(entireTr);
            $('#infoDynaTable').append(row);
            continue;
		}

		//2.tr在前端js 生成
        var row = $("<tr></tr>");
        $('#infoDynaTable').append(row);

        //1.如果是textarea 这占一行
        if(column.type=='textarea'){
            var td_0 = $(commonTd.render({text : column.name,style:"width:18%;"}));
            if (column.isRequired == true) {
                td_0.prepend($(commonSpan.render({style:"font-size:18px;color:red;",text:"*"})));
            }

            var td_1 = $(commonTd.render({colspan : 3}));
            if ("update" ==  type) {
                td_1.html(makeTdContent(column, i, dynStyle));
            } else if ("add" ==  type) {
                td_1.html(makeTdHtml(column, i, dynStyle));
            }
            row.append(td_0);
            row.append(td_1);
            continue;
        }

        //1.生成左边TD
        if(column.type=='checkbox'){
			if(type == "add"){
                var td_checkbox = $(checkboxtdTest.render({text: column.name, name:column.propertyName}));
			}else{
				if(enterpriseInfoBasics[column.propertyName]=='1'){
					if(column.columnPriv == 0){
                        var td_checkbox = $(checkboxChecked.render({text: column.name, name:column.propertyName,readOnlyPriv:"disabled='disabled'"}));
					}else {
                        var td_checkbox = $(checkboxChecked.render({text: column.name, name: column.propertyName}));
                    }
				}else{
                    if(column.columnPriv == 0){
                        var td_checkbox = $(checkboxtdTest.render({text: column.name, name:column.propertyName,readOnlyPriv:"disabled='disabled'"}));
                    }else {
                        var td_checkbox = $(checkboxtdTest.render({text: column.name, name: column.propertyName}));
                    }
				}
			}
            row.append(td_checkbox);
        }else{
            var td_0 = $(commonTd.render({text : column.name,style:"width:18%;"}));
            if (column.isRequired == true) {
                td_0.prepend($(commonSpan.render({style:"font-size:18px;color:red;",text:"*"})));
            }
            row.append(td_0);

            var td_1 = $(commonTd.render({style:"width:27%;"}));
            if ("update" ==  type) {
                td_1.html(makeTdContent(column, i, dynStyle));
            } else if ("add" ==  type) {
                td_1.html(makeTdHtml(column, i, dynStyle));
            }

            row.append(td_1);
        }

        var td_2 = $(commonTd.render({style:"width:18%;"}));
        var td_3 = $(commonTd.render({}));

        //2.判断生成左边 是否已经结束
        if (i+1 >= length) {
            row.append(td_2);
            row.append(td_3);
            break;
        }

        //3. 如果是textarea 换下一行
        column = columnList[i+1];
        if(column.type=='textarea' || isEntireTr(column.type)){
            row.append(td_2);
            row.append(td_3);
            continue;
        }

        //4.生成右边TD
        if(column.type=='checkbox'){
            if(type == "add"){
                if(column.columnPriv == 0){
                    var td_checkbox = $(checkboxtdTest.render({text: column.name, name:column.propertyName,readOnlyPriv:"disabled='disabled'"}));
                }else {
                    var td_checkbox = $(checkboxtdTest.render({text: column.name, name: column.propertyName}));
                }
            }else{
                if(enterpriseInfoBasics[column.propertyCode]=='1'){
                    if(column.columnPriv == 0){
                        var td_checkbox = $(checkboxChecked.render({text: column.name, name:column.propertyName,readOnlyPriv:"disabled='disabled'"}));
                    }else {
                        var td_checkbox = $(checkboxChecked.render({text: column.name, name: column.propertyName}));
                    }
                }else{
                    if(column.columnPriv == 0){
                        var td_checkbox = $(checkboxtdTest.render({text: column.name, name:column.propertyName,readOnlyPriv:"disabled='disabled'"}));
                    }else {
                        var td_checkbox = $(checkboxtdTest.render({text: column.name, name: column.propertyName}));
                    }
                }
            }
            row.append(td_checkbox);
        } else {
            row.append(td_2);
            row.append(td_3);

            td_2.html(column.name);
            if (column.isRequired == true) {
                td_2.prepend($(commonSpan.render({style : "font-size:18px;color:red;", text : "*"})));
            }

            if ("update" ==  type) {
                td_3.html(makeTdContent(column, i, dynStyle));
            } else if ("add" ==  type) {
                td_3.html(makeTdHtml(column, i, dynStyle));
            }
        }

        i++;
    }
}

function isEntireTr(type) {
	if (type == 'singleimage' || type == 'multiFile') {
		return true;
	}
	return false;
}


/**
 * 生成新增页面的TD
 */
function makeTdHtml(attr,i, dynStyle){
	
	var codeInputHasPriv = Hogan.compile(eval(dynStyle + '.codeInputHasPriv'));
	var dateInput = Hogan.compile(eval(dynStyle + '.dateInput'));
	var inputdate = Hogan.compile(eval(dynStyle + '.inputDate'));
	var YNradio = Hogan.compile(eval(dynStyle + '.YNradio'));
	
	var html="";
	var minLen = -1;
	var maxLen = -1;
	var rowHeight= 1 ;
	
	if(attr.type=="text"){
		var textInput = Hogan.compile(LE_TEMPLATE.commonInput);
		if(maxLen!=-1) {
			html=textInput.render({name:attr.propertyName,value:"",maxLength:maxLen});
		} else {
			html=textInput.render({name:attr.propertyName,value:""});
		}
	}else if(attr.type=="textarea"){
		var textareaInput = Hogan.compile(LE_TEMPLATE.commonTextarea);
		if(attr.rowHeight!=null&& attr.rowHeight!='') {
			rowHeight =  parseInt(attr.rowHeight);
		}
		if(maxLen!=-1) {
			html=textareaInput.render({name:attr.propertyName,rows:rowHeight,value:"",maxLength:maxLen});
		} else {
			html=textareaInput.render({name:attr.propertyName,rows:rowHeight,value:""});
		}
	}else if(attr.type=="datetemp"){
        var formatType = attr.dataFormat;
        if(formatType!='yyyy'){
            formatType = 'yyyy-MM-dd';
        }
		html=/*dateInput.render({
			name:attr.propertyName,
			dateId:attr.propertyName+"_"+i,
			value:"",
			dateId_2:attr.propertyName+"__"+i,
		});*/
		inputdate.render({name:attr.propertyName,formatType:formatType});
	}else if(attr.type=="dataList" && attr.propertyName != ''){
		html=codeInputHasPriv.render({
		nameId:attr.propertyName+"_"+i,
		showName:attr.propertyName,
		tableName:attr.infoSet,
		fieldName:attr.propertyCode,
		title:attr.name,
		forNameId:attr.propertyName+"_"+i,
		forCodeId:attr.propertyCode+"_"+i,
		value1:"",
        codeId:attr.propertyCode+"_"+i,
        codeName:attr.propertyCode,
        value2:""});
	}else if(attr.type=="dataList" && attr.propertyName == ''){
		html = codeInputHasPriv.render({
			nameId : attr.propertyCode + "_" + i,
			tableName : attr.infoSet,
            fieldName : attr.propertyCode,
			title : attr.name,
			forNameId : attr.propertyCode + "__" + i,
			forCodeId : attr.propertyCode + "_" + i,
			value1 : "",
	        codeName : attr.propertyCode,
	        value2 : ""});
	}else if(attr.type=="select"){
		
	}else if(attr.type=="radio"){
        var text1 = "是";
        var text2 = "否";
        if(attr.propertyName == 'ENTPLI006'){
            text1 = "国有";
            text2 = "集体";
        }
		html = YNradio.render({
			name : attr.propertyName,
        	text1 : text1,
            text2 : text2,
		});
	}else if(attr.type == "readonly"){
		html = readonlyInput.render({
			codeName : attr.propertyName,
			value1 : '',
			value2 : '',
		});
	}
	if(attr.isRequired == true){
		if(attr.propertyName != ''){
    		if (typeof(rules[attr.propertyName]) == "undefined") {
				rules[attr.propertyName] = {};
			}
    		rules[attr.propertyName]["required"]=true;
    		rules[attr.propertyName + "__" + i] = "required";
    	}else{
    		//rules[attr.propertyCode + "__" + i]="required";
    		if (typeof(rules[attr.propertyCode + "__" + i]) == "undefined") {
		    	rules[attr.propertyCode + "__" + i] = {};
			}
    		rules[attr.propertyCode + "__" + i]["required"]=true;
    	}
    } 
	if(attr.validateRule!=null) {
		var ruleArray = attr.validateRule.split(',');
		
		$.each(ruleArray, function(idx, ruleItem) {  
			
			if(ruleItem==''){
				return;
			}
			//邮箱地址校验
			//if(ruleItem=='email') {
				if(attr.propertyName != ''){
					if (typeof(rules[attr.propertyName]) == "undefined") {
						rules[attr.propertyName] = {};
					}
					rules[attr.propertyName][ruleItem]=true;
		    	}else{
		    		if (typeof(rules[attr.propertyCode + "__" + i]) == "undefined") {
		    			rules[attr.propertyCode + "__" + i] = {};
					}
		    		rules[attr.propertyCode + "__" + i][ruleItem]=true;
		    	}	
			//}
        });
	}
	//文本框最大长度,最小长度验证
	if(attr.lengthLimit!=null) {
		if(attr.propertyName != ''){
			if (typeof(rules[attr.propertyName]) == "undefined") {
				rules[attr.propertyName] = {};
			}
			if(maxLen!=-1){
				rules[attr.propertyName]["maxlength"]=maxLen;
			}
			if(minLen!=-1){
				rules[attr.propertyName]["minlength"]=minLen;
			}
    	}else{
    		if (typeof(rules[attr.propertyCode + "__" + i]) == "undefined") {
    			rules[attr.propertyCode + "__" + i] = {};
			}
    		if(maxLen!=-1){
    			rules[attr.propertyCode + "__" + i]["maxlength"]=maxLen;
			}
			if(minLen!=-1){
				rules[attr.propertyCode + "__" + i]["minlength"]=minLen;
			}
    	}	
	}
	return html;
}


/**
 * 生成修改页面的TD
 */
function makeTdContent(attr,i, dynStyle){

	var commonInputHasPriv = Hogan.compile(eval(dynStyle + '.commonInputHasPriv'));
    var commonTextarea = Hogan.compile(eval(dynStyle + '.commonTextarea'));
    var codeInput = Hogan.compile(eval(dynStyle + '.codeInput'));
	var dateInput = Hogan.compile(eval(dynStyle + '.dateInput'));
    var inputdate = Hogan.compile(eval(dynStyle + '.inputDate'));
    var YNradio = Hogan.compile(eval(dynStyle + '.YNradio'));
	var columnPriv = attr.columnPriv;
	var classPriv = '';
	var inputPriv = '';
	var readOnlyPriv = '';
	var checkdisabled='';
	var onfocusPriv = '';//是否有onfocus方法权限
	var ci2OnfocusPriv = '';//是否有onfocus方法权限
	var ondblclickPriv = '';//是否有ondblclick方法权限
	var onblurPriv = '';//是否有onblur方法权限
	var ciOnfocusPriv = '';//是否有onfocus方法权限
	var ciOndblclickPriv = '';//是否有ondblclick方法权限
	var ciOnblurPriv = '';//是否有onblur方法权限
	var dateOnfocusPriv = '';//时间控件是否有onfocus方法权限
	if(columnPriv == null) {//不可见
		inputPriv = "display:none;";
	}else if(columnPriv == 0) {//只读
		readOnlyPriv = "readonly=true";
        checkdisabled  = "disabled";
	}else if(columnPriv == 1) {//可写
		onfocusPriv = "onfocus=getDate(this,'','"+attr.propertyName+"__"+i+"')";
		ci2OnfocusPriv = "onfocus=loadDict('"+attr.infoSet+"','"+attr.propertyCode+"','"+attr.propertyName+"_"+i+"','"+attr.propertyCode+"_"+i+"')";
		ondblclickPriv = "ondblclick=dictWindow('"+attr.infoSet+"','"+attr.propertyCode+"','"+attr.name+"','"+attr.propertyName+"_"+i+"','"+attr.propertyCode+"_"+i+"')";
		onblurPriv = "onblur=cleanNull('"+attr.propertyName+"_"+i+"','"+attr.propertyCode+"_"+i+"')";
		//codeInput
		ciOnfocusPriv = "onfocus=loadDict('"+attr.infoSet+"','"+attr.propertyCode+"','"+attr.propertyCode+"__"+i+"','"+attr.propertyCode+"_"+i+"')";
		ciOndblclickPriv = "ondblclick=dictWindow('"+attr.infoSet+"','"+attr.propertyCode+"','"+attr.name+"','"+attr.propertyCode + "__" + i+"','"+attr.propertyCode+"_"+i+"')";
		ciOnblurPriv = "onblur=cleanNull('"+attr.propertyCode+"__"+i+"','"+attr.propertyCode+"_"+i+"')";
	}
	var html="";
	var minLen = -1;
	var maxLen = -1;
	var rowHeight=1;
	if(attr.lengthLimit!=null&& attr.lengthLimit!='') {
		var lenArray = attr.lengthLimit.split(',');
		if(lenArray.length==2){
			minLen = parseInt(lenArray[0]);
			maxLen = parseInt(lenArray[1]);
		} else if(lenArray.length==1){
			maxLen = parseInt(lenArray[0]);
		} else {
			//error
			maxLen =  parseInt(lenArray[lenArray.length-1]);
		}
	}
		if(attr.type=="text"){
            if(maxLen!=-1) {
				html=commonInputHasPriv.render({
				name:attr.propertyName,
				type:"text",
				value: enterpriseInfoBasics[attr.propertyName],
				infoSetCode:attr.infoSet,
				privCode:attr.infoSet+'_'+attr.propertyName,
				classPriv:classPriv,
				inputPriv:inputPriv,
				readOnlyPriv:readOnlyPriv,
				maxLength:maxLen
				});
			} else {
                html=commonInputHasPriv.render({
				name:attr.propertyName,
				type:"text",
				value: enterpriseInfoBasics[attr.propertyName],
				infoSetCode:attr.infoSet,
				privCode:attr.infoSet+'_'+attr.propertyName,
				classPriv:classPriv,
				inputPriv:inputPriv,
				readOnlyPriv:readOnlyPriv
				});
			}
		}else if(attr.type=="textarea"){
            if(attr.rowHeight!=null&& attr.rowHeight!='') {
				rowHeight = parseInt(attr.rowHeight);
			}
			inputPriv+="width:80%;";
			if(maxLen!=-1) {
				html=commonTextarea.render({
					name:attr.propertyName,
					value: enterpriseInfoBasics[attr.propertyName],
					inputPriv:inputPriv,
					readOnlyPriv:readOnlyPriv,
					rows:rowHeight,
					maxLength:maxLen
				});
			} else {
				html=commonTextarea.render({
					name:attr.propertyName,
					value:enterpriseInfoBasics[attr.propertyName],
					inputPriv:inputPriv,
					rows:rowHeight,
					readOnlyPriv:readOnlyPriv
				});
			}
		}else if(attr.type=="datetemp"){

            var value = enterpriseInfoBasics[attr.propertyName];
            var formatType = attr.dataFormat;
            if(formatType=='yyyy'){
            	if(value){
                    value=value.substring(0,4);
                }
            }else{
                formatType = 'yyyy-MM-dd';
			}
            if(columnPriv == 1){
                dateOnfocusPriv = "onfocus=WdatePicker({dateFmt:'"+formatType+"'})";
            }

			html=/*dateInput.render({
				name:attr.propertyName,
				dateId:attr.propertyName+"_"+i,
				dateId_2:attr.propertyName+"__"+i,
				value: enterpriseInfoBasics[attr.propertyName],
				value2: formatDate(enterpriseInfoBasics[attr.propertyName]),
				classPriv:classPriv,
				inputPriv:inputPriv,
				onfocusPriv:onfocusPriv,
				readOnlyPriv:readOnlyPriv
			});*/
			inputdate.render({name:attr.propertyName,formatType:formatType,value:value, inputPriv:inputPriv,onfocus:dateOnfocusPriv,readOnlyPriv:readOnlyPriv});
		}else if(attr.type=="dataList" && attr.propertyName != ''){
			html=codeInputHasPriv.render({
			nameId:attr.propertyName+"_"+i,
			showName:attr.propertyName,
			tableName:attr.infoSet,
			fieldName:attr.propertyCode,
			title:attr.name,
			forNameId:attr.propertyName+"_"+i,
			forCodeId:attr.propertyCode+"_"+i,
			value1:findDictByCode(attr.infoSet, attr.propertyCode, enterpriseInfoBasics[attr.propertyCode]),
            codeId:attr.propertyCode+"_"+i,
            codeName:attr.propertyCode,
            value2:enterpriseInfoBasics[attr.propertyCode],
			classPriv:classPriv,
			inputPriv:inputPriv,
			onfocusPriv:ci2OnfocusPriv,
			ondblclickPriv:ondblclickPriv,
			onblurPriv:onblurPriv,
			readOnlyPriv:readOnlyPriv
            });
		}else if(attr.type=="dataList" && attr.propertyName == ''){
			html = codeInput.render({
				nameId : attr.propertyCode + "_" + i,
				tableName : attr.infoSet,
				title : attr.name,
				forNameId : attr.propertyCode + "__" + i,
				forCodeId : attr.propertyCode + "_" + i,
				value1 : findDictByCode(attr.infoSet, attr.propertyCode, enterpriseInfoBasics[attr.propertyCode]),
		        codeName : attr.propertyCode,
		        value2 : enterpriseInfoBasics[attr.propertyCode],
		        classPriv:classPriv,
				inputPriv:inputPriv,
				onfocusPriv:ciOnfocusPriv,
				ondblclickPriv:ciOndblclickPriv,
				onblurPriv:ciOnblurPriv,
				readOnlyPriv:readOnlyPriv
		        });
		}else if(attr.type=="select"){
			
		}else if(attr.type=="radio"){
            var check = enterpriseInfoBasics[attr.propertyName];
            var cheked1 = "";
            var cheked2 = "";
            var text1 = "是";
            var text2 = "否";
            if(enterpriseInfoBasics[attr.propertyName]==1){
            	cheked1 = "checked";
			}else if(enterpriseInfoBasics[attr.propertyName]==0){
                cheked2 = "checked";
			}
			if(attr.propertyName == 'ENTPLI006'){
				text1 = "国有";
				text2 = "集体";
			}
			html = YNradio.render({
				name : attr.propertyName,
                text1 : text1,
                text2 : text2,
				disabled :checkdisabled,
				checked1 : cheked1,
                checked2 : cheked2,
			});
		}else if(attr.type == "readonly"){
			
		}
		if(attr.isRequired == true){
			if(attr.propertyName != ''){
	    		if (typeof(rules[attr.propertyName]) == "undefined") {
					rules[attr.propertyName] = {};
				}
	    		rules[attr.propertyName]["required"]=true;
	    		rules[attr.propertyName + "__" + i] = "required";
	    	}else{
	    		if (typeof(rules[attr.propertyCode + "__" + i]) == "undefined") {
			    	rules[attr.propertyCode + "__" + i] = {};
				}
	    		rules[attr.propertyCode + "__" + i]["required"]=true;
	    	}
	    }
	    if(attr.validateRule!=null) {
		
			var ruleArray = attr.validateRule.split(',');
			
			$.each(ruleArray, function(idx, ruleItem) {  
				if(ruleItem==''){
					return;
				}
				//邮箱地址校验
				if(attr.propertyName != ''){
					if (typeof(rules[attr.propertyName]) == "undefined") {
						rules[attr.propertyName] = {};
					}
					rules[attr.propertyName][ruleItem]=true;
		    	}else{
		    		if (typeof(rules[attr.propertyCode + "__" + i]) == "undefined") {
		    			rules[attr.propertyCode + "__" + i] = {};
					}
		    		rules[attr.propertyCode + "__" + i][ruleItem]=true;
		    	}	
	        });
		}
		//文本框最大长度,最小长度验证
		if(attr.lengthLimit!=null) {
			if(attr.propertyName != ''){
				if (typeof(rules[attr.propertyName]) == "undefined") {
					rules[attr.propertyName] = {};
				}
				if(maxLen!=-1){
					rules[attr.propertyName]["maxlength"]=maxLen;
				}
				if(minLen!=-1){
					rules[attr.propertyName]["minlength"]=minLen;
				}
	    	}else{
	    		if (typeof(rules[attr.propertyCode + "__" + i]) == "undefined") {
	    			rules[attr.propertyCode + "__" + i] = {};
				}
	    		if(maxLen!=-1){
	    			rules[attr.propertyCode + "__" + i]["maxlength"]=maxLen;
				}
				if(minLen!=-1){
					rules[attr.propertyCode + "__" + i]["minlength"]=minLen;
				}
	    	}
		}
		
		return html;
	}

	/**
	 *静态表单字段加必填规则
	 */
	function requiredColumn(fixedCadreBasicInfo){
		for (var i = 0; i< fixedCadreBasicInfo.length; i++) {
			var attr = fixedCadreBasicInfo[i];
			if (attr.isRequired == true) {
				if (attr.propertyName != null && attr.propertyName != '') {
					$("#" + attr.propertyName + "_td").html('<span style="font-size:15px;color:red;">*</span>' + attr.name);
					rules[attr.propertyName] = "required";
					rules[attr.propertyName + "__" + attr.seq] = "required";//时间
				} else {
					$("#" + attr.propertyCode + "_td").html('<span style="font-size:15px;color:red;">*</span>' + attr.name);
					rules[attr.propertyCode + "__" + attr.seq] = "required";
				}
			}
		}
	}


