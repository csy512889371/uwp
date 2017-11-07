/**
 * 获取模块的json参数
 * @param bathPath
 * @param moduelName
 * @param jsonName
 * @returns {{}}
 */
function getMouduleData(bathPath,moduelName, jsonName) {
    var _data = {};
    var tmpSet = $.ajaxSettings.async;
    $.ajaxSettings.async = false;
    var jsonPath =bathPath + "resources/admin/modules/"+moduelName+"/"+jsonName+".json";
    $.getJSON(jsonPath
        , function (data) {
            _data = data;
        }
    );
    $.ajaxSettings.async = tmpSet;
    return _data;
}





function renderAddForm(basePath, attrId, infoSet, formDivId, formId, formAction) {
    var existsForm =$("#"+formId);
    if(existsForm){
        existsForm.remove();
    }

    $.ajax({
        type: 'post',
        url: basePath+'sys/structure/getCadreAttrListJson.do',
        data: {
            "attrId": 24,
            "infoSet": "A01"
        },
        success: function (result) {
            var content = $.parseJSON(result.content);
            var INFO = content.INFO;
            var addAble = content.addAble;//是否可新增
            var hasItemGrid = content.hasItemGrid;//是否有表格项
            var gridItems = [];
            if (hasItemGrid) {
                $.each(INFO, function (item) {
                    if (item.isGridItem) {
                        gridItems.push(item);
                    }
                })
            }
            console.log(INFO);
            formStartStr = "<form action='' method='post' id='"+formId+"'> <div style='margin-left: 0px;margin-right: 0px;width:99.8%;'> <table class='table table-bordered'";
            formEndStr = "</form></div></table>";
            formBodyStr = "";
            var count= 0;
            //渲染表单
            $.each(INFO, function (i,n) {
                if(count%2==0){
                    formBodyStr+= "<tr>";
                }
                var label = n.name;
                var name  = n.propertyName;
                var required  = n.isRequired;


                var requireStr =required? "<span style='font-size:16px;color:red;'>*</span>":"";

                console.log(n);
                formBodyStr +=" <td class='input_title'>"+requireStr+label+"</td><td><input class='input_tdWidth' name='"+name+"' type='text'></td>";
                count++
                if(count%2==0){
                    formBodyStr+= "</tr>";
                }
            });
            count=0;
            var formStr = formStartStr+formBodyStr+formEndStr;
            console.log(formStr);
            var formDiv = $("#"+formDivId);
            formDiv.append(formStr);
        }
    });
}

function getZtreeSelStr(treeId,key){
    var unitTreeObj = $.fn.zTree.getZTreeObj(treeId);
    var nodes = unitTreeObj.getCheckedNodes(true);
    var unitArr=[],j=0;
    for ( var i = 0; i < nodes.length; i++) {
        unitArr[j++] =nodes[i][key];
    }
    return unitArr.join(",");
}



function formatDate(val,pattern){
    if (val) {
        var beforeParse = val.substring(0,val.length-2).replace(new RegExp(/-/gm) ,"/");
        var date = new Date(beforeParse);
        if (pattern == 'yyyy') {
            val = date.getFullYear();
        }
        else {
            val = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        }
    }else{
        val='';
    }
    return val

}



//格式化经营情况数据
function yosValFmr(val) {
    var result ='';
    if (val) {
        if (val > 0) {
            result = val;
        }
    }
    return result;
}