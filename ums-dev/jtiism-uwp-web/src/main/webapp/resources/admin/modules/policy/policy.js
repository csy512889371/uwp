var showLeft = true;

// 后台政策表头
var plcyoCols = [
    {title: 'id', name: 'plcy000', width: 100, align: 'center', sortable: false, type: 'text', hidden: true},
    {
        title: '标题',
        name: 'plcy002',
        width: 150,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var returnVal = '<span class="name_link name_color" onclick="openPolicyEdit(\'' + item.plcy000 + '\')">' + item.plcy002 + '</span>';
            return returnVal;
            // return item.plcy002;
        }
    }, {
        title: '来    源',
        name: 'plcy008',
        width: 100,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var result = findDictByCode('SHJT_POLICY', 'plcy008', item.plcy008);
            return result;
        }
    },

    {
        title: '类    型',
        name: 'plcy001',
        width: 100,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var result = findDictByCode('SHJT_POLICY', 'plcy001', item.plcy001);
            return result;
        }
    },
    {
        title: '更新日期',
        name: 'plcy005',
        width: 80,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return formatDate(item.plcy005);
        }
    },
    {
        title: '状   态',
        name: 'plcy006',
        width: 100,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return (item.plcy006 == 1 ? "发布" : "草稿");
        }
    },
];


// 门户政策表头
var memberPlcyoCols = [
    {
        title: '标题',
        name: 'plcy002',
        width: 150,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var returnVal = '<span class="name_link name_color" onclick="openPolicyMemberView(\'' + item.plcy000 + '\')">' + item.plcy002 + '</span>';
            return returnVal;
        }
    },
    {title: 'id', name: 'plcy000', width: 100, align: 'center', sortable: false, type: 'text', hidden: true},
    {
        title: '来    源',
        name: 'plcy008',
        width: 100,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var result = findDictByCode('SHJT_POLICY', 'plcy008', item.plcy008);
            return result;
        }
    },
    {
        title: '类    型',
        name: 'plcy001',
        width: 100,
        align: 'left',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            var result = findDictByCode('SHJT_POLICY', 'plcy001', item.plcy001);
            return result;
        }

    },
    {
        title: '更新日期',
        name: 'plcy005',
        width: 80,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return formatDate(item.plcy005);
        }
    }
];


function switchShow() {
    if (showLeft) {
        showLeft = false;
        $('#policyTreeDiv').hide();
        $('#policySearch').css('margin-left', '13px');
        $('#switchHref').removeClass('icon-chevron-left');
        $('#switchHref').addClass('icon-chevron-right');
    } else {
        showLeft = true;
        $('#policyTreeDiv').show();
        $('#policySearch').css('margin-left', '273px');
        $('#switchHref').removeClass('icon-chevron-right');
        $('#switchHref').addClass('icon-chevron-left');
    }
}

$('#policyShowTab').height($(window).height() - 100);
$('.policy_search').height($('#policyShowTab').height() - 2);
$('#switchBar').height($(window).height() - 62);

jQuery('[placeholder]').focus(function () {
    var input = jQuery(this);
    if (input.val() == input.attr('placeholder')) {
        input.val('');
        input.removeClass('placeholder');
    }
}).blur(function () {
    var input = jQuery(this);
    if (input.val() == '' || input.val() == input.attr('placeholder')) {
        input.addClass('placeholder');
        input.val(input.attr('placeholder'));
    }
}).blur().parents('form').submit(function () {
    jQuery(this).find('[placeholder]').each(function () {
        var input = jQuery(this);
        if (input.val() == input.attr('placeholder')) {
            input.val('');
        }
    })
});


// 视图切换
var showType = '<div class="view_div">' + '<a id="listLink" class="link_btn link1 fontClass" onclick="showList()">列表</a>';
$('#pgs').append(showType);
$('#listLink').css('border', '1px solid #2898fd');
// 当前视图
var curView = 'list';
var showOther = false;

// 展示列表
function showList() {
    $('#policyListDiv').show();
    $('#infoLink').removeClass('fontClass').css('border', '1px solid #bfc4c8');
    $('#picLink').removeClass('fontClass').css('border', '1px solid #bfc4c8');
    $('#listLink').addClass('fontClass').css('border', '1px solid #2898fd');
    curView = 'list';
}

function loadPolicyList() {
    var policyList = $('#policyList').mmGrid({
        height: commonTalbeHeight - 61,
        cols: plcyoCols,
        url: EnvBase.base + 'admin/policy/policyList.do',
        method: 'post',
        remoteSort: true,
        sortName: 'SECUCODE',
        sortStatus: 'asc',
        root: 'resultMapList',
        multiSelect: true,
        checkCol: true,
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

    policyList.on('cellSelected', function (e, item, rowIndex, colIndex) {
        doubleClickRow(item, rowIndex, e);
        if ($(e.target).is('.btn-info, .btnPrice')) {
            // 阻止事件冒泡
            e.stopPropagation();
        }
    }).load({pageNumber: 1});

    policyList.resize($(window).height() - 117);
    return policyList;
}


function loadMemberPolicyList() {
    var policyList = $('#policyList').mmGrid({
        height: commonTalbeHeight - 61,
        cols: memberPlcyoCols,
        url: EnvBase.base + 'member/policy/policyList.do',
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

    policyList.on('cellSelected', function (e, item, rowIndex, colIndex) {
        doubleMemberClickRow(item, rowIndex, e);
        if ($(e.target).is('.btn-info, .btnPrice')) {
            // 阻止事件冒泡
            e.stopPropagation();
        }
    }).load({pageNumber: 1});
    policyList.resize($(window).height() - 217);
    return policyList;
}


var policyTree;


function zTreeOncheck(event, treeId, treeNode) {
    //如果勾选/取消勾选的是父节点，则循环操作子节点
    childrenNodes = [];
    if (treeNode.isParent) {
        childrenNodes = getChildrenNodes(treeNode, childrenNodes);//递归查询树节点下所有子孙节点
    } else {
        var selectNodes = policyTree.getCheckedNodes(true);
        childrenNodes = selectNodes;
    }
    var plcyTypes = [];
    for (var i = 0; i < childrenNodes.length; i++) {
        if (childrenNodes[i].id) {
            plcyTypes[i] = childrenNodes[i].id;
        }
    }
    var plcyType = plcyTypes.join(",");
    searchList(null, null, null, null, plcyType);
}


function getChildrenNodes(parentNode, childrenNodes) {
    childrenNodes.push(parentNode);
    if (parentNode.isParent) {
        for (var obj in parentNode.children) {
            getChildrenNodes(parentNode.children[obj], childrenNodes);
        }
    }
    return childrenNodes;
}


function zTreeOnclick(event, treeId, treeNode) {
    var plcy001 = treeNode.id;
    var selectNodes = policyTree.getCheckedNodes(true);
    if (plcy001.length == 36) {
        for (var i = 0; i < selectNodes.length; i++) {
            if (selectNodes[i].id != plcy001) {
                selectNodes[i].checked = false;
            }
        }
    }
    searchList(null, plcy001, null, null, null);
    policyTree.checkAllNodes(false);//取消勾选其他
    policyTree.checkNode(treeNode, true, false);
    policyTree.refresh();
    policyTree.selectNode(treeNode, true);
}


/**
 * 加载机构信息
 */
function loadPolicyTree(fromType) {
    $.ajax({
        type: 'post',
        url: EnvBase.base + fromType + "/policy/getPolicyTypeTreeList.do",
        data: {},
        success: function (data) {
            var dictNodes = [];
            dictNodes.push({
                'id': 0,
                'pId': null,
                'name': "政策类型",
                'fullName': "政策类型"
            });

            for (var i in data) {
                dictNodes.push({
                    'id': data[i].code,
                    'pId': 0,
                    'name': data[i].codeAbr1,
                    'fullName': data[i].codeAbr2,
                });
            }
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
                callback: {
                    onClick: zTreeOnclick,
                    onCheck: zTreeOncheck
                },
                view: {
                    fontCss: getFontCss
                }
            };
            policyTree = $.fn.zTree.init($("#coderTree"), coderTreeSetting, dictNodes);
        }
    });
}


/**
 * 检验是否选择数据
 */
function checkSelected() {
    var rows = policyList.selectedRows();
    var item = rows[0];
    if (typeof(item) == 'undefined') {
        return null;
    }
    return item;
}


/**
 * 查询按钮
 */
function searchBtns() {
    if (curView == 'list') {//当前展示的是列表，那么showother就False
        showOther = false;
    }
    clickSearchArea(2);
    $(".checkAll").attr("checked", false);//重置全选复选框
    var plcyType = getZtreeSelStr("coderTree", "id");

    // var plcySrc = getZtreeSelStr("coderTree", "id");

    var plcyType = $("#plcy001").val();
    var plcy008 = $("#plcy008").val();
    var plcy002 = $("#plcy002").val();
    searchList(null, null, plcy002, plcy008, plcyType);
}

/**
 * 查询按钮
 */
function searchList(page, plcy001, plcy002, plcy008, plcyType) {

    if (curView == 'list') {//当前展示的是列表，那么showother就False
        showOther = false;
    }
    if (!page) {
        page = 1;
    }
    var include = 1;
    if ($('#include').is(':checked')) {
        include = 2;
    }
    policyList.load({
        pageNumber: page,
        plcy001: plcy001,
        plcy002: plcy002,
        plcy008: plcy008,
        plcyType: plcyType
    });
    $(".checkAll").attr("checked", false);
}


/**
 * 重置查询条件
 */
function cleanEnterprise() {
    $('#policyInfo').val('');
    $('#plcyo019Id').val('');
    $('#plcyo019').val('');
    $('#plcy002').val('');
    $("#plcy003Id").val('');
    $("#policy").val('');
    $("#policyIds").val('');
}


/**
 * 回车查询
 */
function keydownEvent() {
    var e = window.event || arguments.callee.caller.arguments[0];
    if (e && e.keyCode == 13) {
        if (e && e.preventDefault) {
            e.preventDefault();
        } else {
            window.event.returnValue = false;
        }
        setTimeout(function () {
            searchBtns();
        }, 200);
    }

}


function mouseClick(event) {
    event = event || window.event;
    fRightClickSearch();
    return false;
}


function clickSearchArea(type) {
    if (type == 1) {
        searchType = 2;
    } else if (type == 2) {
        searchType = 1;
    }
}

var nodes = [];

function searchMenu() {
    var keyWords = $.trim($('#menuName').val());
    if (keyWords == '') return;
    if (nodes.length > 0) {//第二次查询的时候，将上一次查询出的节点颜色还原
        for (var i = 0; i < nodes.length; i++) {
            nodes[i].highlight = false;
            policyTree.updateNode(nodes[i]);
        }
    } else {
        var node = policyTree.getNodes();
        for (var i = 0; i < node.length; i++) {
            node[i].highlight = false;
            policyTree.updateNode(node[i]);
        }
    }
    nodes = policyTree.getNodesByParamFuzzy('name', keyWords);
    if (nodes.length > 0) {
        policyTree.expandAll(false);
    }
    setTimeout(function () {
        $('#mCount').html(nodes.length);
        for (var i = 0; i < nodes.length; i++) {
            nodes[i].highlight = true;
            policyTree.updateNode(nodes[i]);
            var nd = nodes[i].getParentNode();
            while (nd != null) {
                policyTree.expandNode(nd, true);
                nd = nd.getParentNode();
            }
        }
    }, 300);

}

function getFontCss(treeId, treeNode) {
    if (!!treeNode.highlight) {
        return {color: "#A60000", "font-weight": "bold"};
    } else {
        if (treeNode.id == '-1') {
            return {color: "#2898FD", "font-weight": "normal"}
        } else {
            return {color: "#333", "font-weight": "normal"};
        }
    }
}


function onCadreCountBodyDown(event) {
    if (!(event.target.id == "cadreCountBtn" || event.target.id == "cadreCountContent" || $(event.target).parents("#cadreCountContent").length > 0)) {
        hideCadreCountMenu();
    }
}

function hideCadreCountMenu() {
    $("#cadreCountContent").hide();
    $("body").unbind("mousedown", onCadreCountBodyDown);
}

function doubleClickRow(item, rowIndex, e) {
    policyId = item["plcy000"];
    openPolicyEdit(policyId);
}

function doubleMemberClickRow(item, rowIndex, e) {
    policyId = item["plcy000"];
    //双击行事件
    var trs = $("#policyList tr:eq(" + rowIndex + ")");
    trs.each(function () {
        $("#policyList tr:eq(" + rowIndex + ")").dblclick(function () {
            openPolicyMemberView(policyId);
            return;
        });
        return;
    });
}


function showPolicyEditMain() {
    openRmbWin('查看企业信息', 'admin/policy/showPolicyEditMain.do', {}, function () {
    });
}

function deletePolicy() {
    var policyIds = getPolicyIds();
    if (policyIds == '') {
        return;
    }
    openConfirm('请确定是否要删除该记录？', function () {
        $.ajax({
            type: 'post',
            url: EnvBase.base + 'admin/policy/deletePolicy.do',
            data: {policyIds: policyIds},
            success: function (data) {
                openAlert(data.content);
                if (data.type == 'success') {
                    // 重新加载数据
                    searchList();
                }
            }
        });
    });
}

function getPolicyIds() {
    var policyIds = '';
    var rows = policyList.selectedRows();
    if (rows.length < 1) {
        openAlert('请至少选择一条记录！');
        return '';
    }
    var item = rows[0];
    if (typeof(item) != 'undefined') {
        for (var i = 0; i < rows.length; i++) {
            console.log(rows[i]);
            policyIds += rows[i].plcy000 + ",";
        }
    }
    if (policyIds == '') {
        openAlert('请至少选择一条记录！');
        return '';
    }
    policyIds = policyIds.substring(0, policyIds.length - 1);
    return policyIds;
}


function openPolicyMemberView(policyId) {
    window.open(EnvBase.base + "member/policy/policyMemberView.do?policyId=" + policyId);
}


function openPolicyEdit(policyId) {
    if (!policyId || policyId == '') policyId = "###";
    layui.use('layer', function () {
        var $ = layui.jquery, layer = layui.layer;
        //iframe层
        var layerIsOpen = !(typeof  policyLayerIndex == "undefined") && policyLayerIndex;
        if (!layerIsOpen) {
            policyLayerIndex = layer.open({
                anim: -1,
                isOutAnim: false,
                type: 2,
                title: ['企业政策编辑', 'background-color:rgb(40, 152, 253);color:rgb(255, 255, 255);font-size:15px;font-weight:bold;'],
                shadeClose: false,
                shade: 0.1,
                area: ['1200px', '90%'],
                content: [EnvBase.base + 'admin/policy/policyEdit.do?policyId=' + policyId],
                cancel: function () {
                    policyLayerIndex = undefined;
                    layer.closeAll();
                }
            });
        }
    });
}