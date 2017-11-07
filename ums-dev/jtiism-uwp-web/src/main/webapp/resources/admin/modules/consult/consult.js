var consultCols = [
    {title: 'id', name: 'cstinf000', width: 100, align: 'center', sortable: false, type: 'text', hidden: true},
    {
        title: '标题',
        name: 'cstinf003',
        width: 150,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var returnVal = '<span class="name_link name_color" onclick="openConsultDetail(\'' + item.cstinf000 + '\',\'ent\')">' + item.cstinf003 + '</span>';
            return returnVal;
        }
    },
    {
        title: '企业名称',
        name: 'cstinf001',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return (item.cstinf001);
        }
    },
    {
        title: '类    型',
        name: 'cstinf002',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var result = findDictByCode('SHJT_CST_INF', 'cstinf002', item.cstinf002);
            return result;
        }
    },
    {
        title: '回答数',
        name: 'cstinf007',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return (item.cstinf007);
        }
    },
    {
        title: '提问时间',
        name: 'cstinf006',
        width: 80,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var date = new Date(item.cstinf006);
            return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        }
    },
    {
        title: '状   态',
        name: 'cstinf008',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return (item.cstinf008==1?"已解决":"未解决");
        }
    },
    {
        title: '操作',
        name: '',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            if(item.cstinf008!=1) {
                var btn = "<button class='btn-style3' onclick='resolved(\""+item.cstinf000+"\",\"ent\")'>解决</button>"
            } else {
                var btn = ""
            }
            return btn +
                "<button class='btn-style3 center' onclick='openConsultDetail(\""+ item.cstinf000 + "\",\"ent\")'>详情</button>";
            /*var returnVal = '<span class="name_link name_color" onclick="resolved(\'' + item.cstinf000 + '\')">解决'+'</span>';
            return returnVal;*/
        }
    },
];

var consultMemberCols = [
    {title: 'id', name: 'cstinf000', width: 100, align: 'center', sortable: false, type: 'text', hidden: true},
    {
        title: '标题',
        name: 'cstinf003',
        width: 150,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var returnVal = '<span class="name_link name_color" onclick="openConsultDetail(\'' + item.cstinf000 + '\',\'member\')">' + item.cstinf003 + '</span>';
            return returnVal;
        }
    },
    {
        title: '类    型',
        name: 'cstinf002',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var result = findDictByCode('SHJT_CST_INF', 'cstinf002', item.cstinf002);
            return result;
        }
    },
    {
        title: '回答数',
        name: 'cstinf007',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return (item.cstinf007);
        }
    },
    {
        title: '提问时间',
        name: 'cstinf006',
        width: 80,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var date = new Date(item.cstinf006);
            return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        }
    },
    {
        title: '状   态',
        name: 'cstinf008',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return (item.cstinf008==1?"已解决":"未解决");
        }
    },
    {
        title: '操作',
        name: '',
        width: 50,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            if(item.cstinf008!=1) {
                var btn = "<button class='btn-style3' onclick='resolved(\""+item.cstinf000+"\",\"member\")'>解决</button>"
            } else {
                var btn = ""
            }
            return btn +
                "<button class='btn-style3 center' onclick='openConsultDetail(\""+ item.cstinf000 + "\",\"member\")'>详情</button>";
            /*var returnVal = '<span class="name_link name_color" onclick="resolved(\'' + item.cstinf000 + '\')">解决'+'</span>';
            return returnVal;*/
        }
    },
];


var showLeft = true;
/**
 * 咨询类型ID条件
 */
var consultTypeId = "";
$('#consultTreeTab').height($(window).height()-60);
$('#switchBar').height($(window).height()-60);


/**
 * 窗口自适应
 */
function adjustWindow(){
    $('#consultTreeTab').height($(window).height()-60);
    $('#switchBar').height($(window).height()-60);

}

function switchShow(){
    if(showLeft){
        showLeft = false;
        $('#consultTreeContainer').hide();
        $('#rightDiv').css('margin-left','13px');
        $('#switchHref').removeClass('icon-chevron-left');
        $('#switchHref').addClass('icon-chevron-right');
    }else{
        showLeft = true;
        $('#consultTreeContainer').show();
        $('#rightDiv').css('margin-left','284px');
        $('#switchHref').removeClass('icon-chevron-right');
        $('#switchHref').addClass('icon-chevron-left');
    }
}

function loadConsultList() {
    var consultList = $('#consultList').mmGrid({
        height: commonTalbeHeight - 61,
        cols: consultCols,
        url: EnvBase.base + 'ent/consult/consultList.do',
        method: 'post',
        remoteSort: true,
        sortName: 'SECUCODE',
        sortStatus: 'asc',
        root: 'resultMapList',
        multiSelect: false,
        checkCol : true,
        nowrap: true,
        fullWidthRows: true,
        autoLoad: false,
        showBackboard: false,
        plugins: [$('#pgs').mmPaginator({limitList: [50, 100, 150, 200, 250]})],
        params: function () {
            // 如果这里有验证，在验证失败时返回false则不执行AJAX查询。
            return {
                secucode: $('#secucode').val(),
                searchType: searchType,
                include: 1
            };
        }
    });

    consultList.on('cellSelected', function (e, item, rowIndex, colIndex) {
        doubleClickRow(item, rowIndex, e,"ent");
        if ($(e.target).is('.btn-info, .btnPrice')) {
            // 阻止事件冒泡
            e.stopPropagation();
        }
    }).load({pageNumber: 1});

    consultList.resize($(window).height() - 120);
    return consultList;

    /**
     * 视图切换
     */
    consultList.on('loadSuccess', function (e, item) {

    });
}

function loadMemberConsultList() {
    var consultList = $('#consultList').mmGrid({
        height: commonTalbeHeight - 61,
        cols: consultMemberCols,
        url: EnvBase.base + 'member/consult/consultList.do',
        method: 'post',
        remoteSort: true,
        sortName: 'SECUCODE',
        sortStatus: 'asc',
        root: 'resultMapList',
        nowrap: true,
        fullWidthRows: true,
        autoLoad: false,
        showBackboard: false,
        plugins: [$('#pgs').mmPaginator({limitList: [50, 100, 150, 200, 250]})],
        params: function () {
            // 如果这里有验证，在验证失败时返回false则不执行AJAX查询。
            return {
                secucode: $('#secucode').val(),
                searchType: searchType,
                include: 1
            };
        }
    });

    consultList.on('cellSelected', function (e, item, rowIndex, colIndex) {
        doubleClickRow(item, rowIndex, e,"member");
        if ($(e.target).is('.btn-info, .btnPrice')) {
            // 阻止事件冒泡
            e.stopPropagation();
        }
    }).load({pageNumber: 1});
    //consultList.resize($(window).height() - 145);
    return consultList;
    /**
     * 视图切换
     */
    consultList.on('loadSuccess', function (e, item) {

    });
}

/**
 * 查询按钮
 */
function searchList(page, cstinf002, cstinf003, consultTypeId) {
    backtolist();
    if (!page) {
        page = 1;
    }
    var include = 1;
    if ($('#include').is(':checked')) {
        include = 2;
    }
    if (searchType == 1) {
        consultList.load({
            pageNumber: page,
            cstinf002: cstinf002,
            cstinf003: cstinf003,
            consultTypeId : consultTypeId
        });
    } else if(searchType == 2) {
        consultList.load({
            pageNumber: page,
            cstinf002: cstinf002,
            cstinf003: cstinf003,
            consultTypeId : consultTypeId
        });
    }

    $(".checkAll").attr("checked", false);
}

/**提问**/
function openConsultEdit(consultId){
    if(!consultId || consultId == '') consultId = "###";
        layui.use('layer', function () {
            var $ = layui.jquery, layer = layui.layer;
            //iframe层
            layer.open({
                anim: -1,
                isOutAnim:false,
                type: 2,
                title: ['企业咨询提问','background-color:rgb(40, 152, 253);color:rgb(255, 255, 255);font-size:15px;font-weight:bold;'],
                shadeClose: false,
                shade: 0.1,
                area: ['1200px', '90%'],
                content: [EnvBase.base + 'member/consult/consultEdit.do?consultId='+consultId],
                cancel: function(){
                    layer.closeAll();
                }
            });
        });
}


/**搜索按钮**/
function searchConsult(){
    clickSearchArea(2);
    $(".checkAll").attr("checked", false);//重置全选复选框
    var cstinf003 = $.trim($('#cstinf003').val());
    searchList(null, null, cstinf003, null);
}

/**
 * 重置按钮
 */
function cleanSearch() {
    $('#cstinf003').val('');
    searchList();
}


/**树形展示**/
var consTree;
/**加载类别信息**/
function loadConsultTypeTree(fromType){
    $.ajax({
        type : 'post',
        url  : EnvBase.base + fromType + '/consult/getConsultTypeTreeList.do',
        data : {},
        async:false,
        success: function(data){
            var dictNodes = [];

            dictNodes.push({
                'id': 0,
                'pId': null,
                'name': "咨询类型",
                'fullName': "咨询类型",
                open : true,
                /*children : [ {
                    id : "1",
                    name : "产业政策"
                }, {
                    id : "2",
                    name : "系统使用"
                } ]*/
            });

            for(var i in data){
                dictNodes.push({
                    'id': data[i].code,
                    'pId': 0,
                    'name': data[i].codeAbr1,
                    'fullName': data[i].codeAbr2,
                });
            }

            /*var dictNodes = [ {
                name : "问题",
                open : true,
                children : [ {
                    name : "系统使用"
                }, {
                    name : "产业政策"
                } ]
            } ];*/

            var coderTreeSetting = {
                check: {
                    enable: true,
                    nocheckInherit: true,
                    chkboxType: {"Y": "s", "N": "s"},
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback : {
                    onClick : zTreeOnclick,
                    onCheck: zTreeOncheck
                }
            };
            consTree = $.fn.zTree.init($("#cosultTree"), coderTreeSetting, dictNodes);
        }
    });

}



function zTreeOncheck(event, treeId, treeNode) {
    /*//如果勾选/取消勾选的是父节点，则循环操作子节点
        childrenNodes = [];
        if (treeNode.isParent) {
            childrenNodes = getChildrenNodes(treeNode, childrenNodes);//递归查询树节点下所有子孙节点
        } else {
            childrenNodes = [treeNode];
        }
        if (treeNode.checked) {
            for (var i = 0; i < childrenNodes.length; i++) {
                var radio = document.getElementById("rid_1_" + childrenNodes[i].id);
                radio.checked = true;
                childrenNodes[i].oper = 1;
            }
        } else {
            for (var i = 0; i < childrenNodes.length; i++) {
                var radio = document.getElementById("rid_0_" + childrenNodes[i].id);
                radio.checked = true;
                childrenNodes[i].oper = 0;
            }
        }*/
}

/**点击节点事件**/
var type=null;
var selectedNode=null;

function zTreeOnclick(event, treeId, treeNode){
    var cstinf002 = treeNode.id;
    var selectNodes = consTree.getCheckedNodes(true);
    if (cstinf002.length == 36) {
        for (var i = 0; i < selectNodes.length; i++) {
            if (selectNodes[i].id != cstinf002) {
                selectNodes[i].checked = false;
            }
        }
    }
    searchList(null,cstinf002,null,null);
    consTree.checkAllNodes(false);//取消勾选其他
    consTree.checkNode(treeNode, true, false);
    consTree.refresh();
    consTree.selectNode(treeNode, true);
}
/** 打开咨询详情 **/
function openConsultDetail(consultId,fromType) {
    $('#backtolistbtn').show();
    $('#searchDiv').hide();
    $('#buttonBar').hide();
    //更改回答列表高度
    if($('#editorInput').is(":hidden")){
        $('#ans').css('height','600px');
    }
    $.ajax({
        type : 'post',
        url  : EnvBase.base + fromType + '/consult/consultDetail.do',
        data : {"cstinf000":consultId},
        async:false,
        success: function(data){
            var mapdata = data;
            $('#cstinfList').hide();
            $('#consultDetailDiv').show();
            var cons = mapdata.consult;
            var listans = mapdata.ans;
            var gaishu = cons.cstinf003;
            var detail = UE.utils.html(cons.cstinf005);
            var type = findDictByCode('SHJT_CST_INF', 'cstinf002', cons.cstinf002);
            //alert(JSON.stringify(mapdata));
            $('#detailTitle').text(gaishu);
            $('#cstinf005').html(detail);
            $('#cstinf006').text(cons.cstinf006);
            $('#cstinf001').text("提问企业："+cons.cstinf001);
            $('#cstinf002').text("分类："+type);
            $('#cstinf000').val(cons.cstinf000);
            $('#ans_num').text("共"+cons.cstinf007+"条回答");
            var htmlContent = "";
            for(var i = 0;i<listans.length;i++) {
                var ansDetail = UE.utils.html(listans[i].cstans002);
                var date = new Date(listans[i].cstans003).toLocaleDateString().replace(/\//g, "-");
                htmlContent = htmlContent + "<div class=\"pl-ue-content bd-form bd-notop km_answer_bg \">\n" +
                    "<div>\n" +
                    "<div\n" +
                    "style=\"padding: 12px 24px 0px 12px; font-size: 14px; padding-top: 10px;text-align: left;\">\n" +
                    "<p>\n" +ansDetail+
                    "<br>\n" +
                    "</p>\n" +
                    "</div>\n" +
                    "<div class=\"km_answer_content\">\n" +
                    "<a style=\"color: #5d7895\" href=\"javascript:void(0)\">"+listans[i].cstans004+"</a><i class=\"s-bar\">&nbsp;|</i>\n" +
                    "<span style=\"font-size: 12px; font-weight: normal;\">"+date+"</span>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</div>";
            }

            //$('#anssum').after(htmlContent);
            $('#ans').html(htmlContent);
                //$("#detailTitle").val(cstinf003);
                //$("#cstans002").val("这是赋值");
            /*for(var i in data) {
                alert(data[i].cstinf000);
            }*/
            /*if (data.type == "success") {
                var param = data.cstinf000;
                alert(param);
            }*/
        }
    });
}

function backtolist() {
    $("#consultDetailDiv").hide();
    $("#cstinfList").show();
    $("#backtolistbtn").hide();
    $('#searchDiv').show();
    $('#buttonBar').show();
    //每次点击返回按钮后，不管ueditor输入框状态如何都将样式置为none。
    $("#editorInput").css('display','none');
}
/** 双击行事件 **/
function doubleClickRow(item, rowIndex, e,fromType) {
    consultId = item["cstinf000"];
    //双击行事件
    var trs = $("#consultList tr:eq(" + rowIndex + ")");
    trs.each(function () {
        $("#consultList tr:eq(" + rowIndex + ")").dblclick(function () {
            openConsultDetail(consultId,fromType);
            return;
        });
        return;
    });
}

/**解决按钮**/
function resolved(consultId,fromType) {
    openConfirm('是否标记问题为已解决?',function(){
        if(!consultId || consultId == '') consultId = "###";
        $.ajax({
            url:EnvBase.base + fromType + "/consult/resolved.do",
            async:false,
            data:{"cstinf000":consultId},
            success: function(data){
                searchList();
            }
        });

    });
}

function mouseClick(event) {
    event = event || window.event;
    fRightClickSearch();
    return false;
}

/** 回答问题 **/
function answerQuestion(){
    var rows = consultList.selectedRows();
    var item = rows[0];
    var consultId = "";
    var ID = $('#cstinf000').val();
    if(typeof(item) == 'undefined'){
        //以下是为了解决第二次点击标题后当前项变成未选中item=undefined导致的回答问题按钮不能用的问题。
        if(ID==null||ID==''||ID=='undefined') {
            openAlert('请选择一条记录！');
            return;
        } else {
            consultId = ID;
        }
    } else {
        consultId = item.cstinf000;
    }
    openConsultDetail(consultId,"ent");
    toggle();
}

/** 删除 **/
function deleteConsult(fromType) {
    var rows = consultList.selectedRows();
    var item = rows[0];
    if(typeof(item) == 'undefined'){
        openAlert('请选择一条记录！');
        return;
    }
    openConfirm ('确认删除？',function () {
        $.ajax({
            url:EnvBase.base + fromType + "/consult/deleteConsult.do",
            async:false,
            data:{cstinf000:item.cstinf000},
            success: function (data) {
                if(data.type='success') {
                    openAlert('删除成功!');
                    searchList();//重新加载列表
                } else {
                    openAlert('删除失败!');
                }

            }
        });
    });
}

function getCheckedNodesId() {
    var checkId = "";
    var checkNode = consTree.getCheckedNodes(true);
    for (var i = 0; i < checkNode.length; i++) {
            checkId += checkNode[i].id + ',';
    }
    if (checkId.length > 0) {
        checkId = checkId.substring(0, checkId.length - 1);
    }
    return checkId;
}

function fRightClickSearch() {
    clickSearchArea(1);
    consultTypeId = getCheckedNodesId();
    if (consultTypeId.length > 0) {
        searchList(null,null,null,consultTypeId);
        return true;
    } else {
        consultTypeId = "";
        openAlert('请选择咨询类型');
        return false;
    }

}

function clickSearchArea(type) {
    if (type == 1) {
        searchType = 2;
    } else if (type == 2) {
        searchType = 1;
    }
}

function changeStatus(type) {
    if (type == 1) {
        status = 2;
    } else if (type == 2) {
        status = 1;
    }
}