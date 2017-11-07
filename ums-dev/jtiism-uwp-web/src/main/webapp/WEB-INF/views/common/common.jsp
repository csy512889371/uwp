<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String pt = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<style>
    .ui-autocomplete {
        max-height: 200px;
        overflow-y: auto;
        /* prevent horizontal scrollbar */
        overflow-x: hidden;
    }

    /* IE 6 doesn't support max-height
	   * we use height instead, but this forces the menu to always be this tall
	   */
    * html .ui-autocomplete {
        height: 200px;
    }

    .search {
        background-color: #ecf2f6;
        border: #2898fd;
        text-align: left;
    }

    .search a:hover {
        /* background-color: #2898fd; */
        border: none;
    }

    .transparent_class {
        width: 100%;
        position: absolute;
        background: #000;
        z-index: 998;
        top: 0;
        left: 0;
        height: 2000px;
        opacity: 0.2;
        /*This works in IE 8 & 9 too*/
        -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=20)";
    }

    .waitDiv {
        background-color: #cdcdcd;
        /* filter:alpha(opacity:30); */
        position: absolute;
        z-index: 998;
        top: 0px;
        left: 0px;
        display: none;
        filter: alpha(Opacity=90);
        -moz-opacity: 0.9;
        opacity: 0.9;
    }

    .imgDiv img {
        width: 50px;
    }

    .imgDiv {
        margin: auto;
        position: absolute;
        z-index: 999;
        display: none;
        left: 45%;
        font-size: 14px;
    }

    .trCheck {
        background-color: #a4bdff;
    }

    .trHover:hover {
        background-color: #cccccc;
    }
</style>
<!-- 等待提示层 -->
<div class="waitDiv"></div>
<div class="imgDiv"><img src="<%=pt%>resources/admin/img/output/wait.gif"><br><span id="waitTip">数据加载中，请稍候</span></div>
<div style="display: none;" id="dialogDiv">
    <!-- alert弹框 -->
    <div id="alert_dialog" title="提示"
         style="text-align: center; padding: 50px 5px 5px 5px;">
        <p id="alert_dialog_content"></p>
    </div>

    <!-- confirm弹框 -->
    <div id="confirm_dialog" title="提示"
         style="text-align: center; padding: 50px 5px 5px 5px;">
        <p id="confirm_dialog_content"></p>
    </div>

    <!-- confirm 未保存弹框 -->
    <div id="confirm_dialog_not_save" title="数据保存提示"
         style="text-align: left;">
        <p id="confirm_dialog_content_not_save" style="padding-top: 28px; padding-left: 42px; font-size: 13px;"></p>
    </div>

    <!-- 公共弹窗  支持三层弹窗-->
    <div id="pub_window1" title="提示">
        <div id="pub_window_content1"></div>
    </div>
    <div id="pub_window2" title="提示">
        <div id="pub_window_content2"></div>
    </div>
    <div id="pub_window3" title="提示">
        <div id="pub_window_content3"></div>
    </div>
    <div id="pub_rmb_window" title="提示">
        <div id="pub_rmb_window_content" style="background-color: #e5e5e5;"></div>
    </div>


    <!-- 公共弹窗 -->
    <div id="common_window" title="选择">
        <div id="common_window_content"></div>
    </div>

    <!-- 调取中组部数据字典 -->
    <div id="dict_window" title="选择">
        <div id="dict_window_content"></div>
    </div>
</div>
<div id="hbg" class="transparent_class" style="display: none;"></div>
<!-- 公共下拉列表 -->
<div id="common_seldiv"
     style="height: 350px; width: 200px; background-color: #fff; position: absolute; z-index: 999; display: none;"></div>
<div style="display: none">
    <form method="post" id="loadFileOfCadreInfo" action="<%=pt%>sys/file/loadFile.do">
        <input name="fileName" id="loadfileNameOfCadreInfo">
        <input name="relFolderName" id="loadRelFolderNameOfInfo">
        
    </form>
</div>
<script>
    var currentEntId =null;
    var changeFlag = false;
    var saveTip = '当前页面数据已发生修改，请确认是否需要保存！<br><br>如果单击“不保存”,将会丢失本次修改内容，并关闭当前窗口。<br>如果单击“取消”，返回编辑页面。';
    var saveTip2 = '您录入的内容尚未保存！\n\n是否忽略该提示？';

    var curDictFull = null,//调用者
        curDictCode = null,//代码存储
        curDictName = null;//名称存储
    curDictCodeName = null;//代码codeName
    var curWindow = 0;
    var dictCallbacks = null;
    var unitCallbacks = null;
    var groupCallbacks = null;
    var adjustCallbacks = null;
    var dictMethod = null;//字典回调方法
    $(document).tooltip({
        content: function () {
            if ($(this).is("#egXa0110a")) {
                return $(this).val();
            }
            if ($(this).is('#egXa0109a')) {
                return $(this).val();
            }
            if ($(this).is("#egXa0110_9")) {
                return $(this).val();
            }
            if ($(this).is("#cadreType")) {
                return $(this).val();
            }
            if ($(this).is("#egxb0115a")) {
                return $(this).val();
            }
            if ($(this).is("#egxb0115aa")) {
                return $(this).val();
            }
        }
    });
    //$('#dialogDiv').show();

    $('.waitDiv').width($(window).width());
    $('.waitDiv').height($(window).height());
    $('.imgDiv').css('top', $(window).height() / 2 - 50 + 'px');

    /**
     * 显示等待提示层
     */
    function showWait(tip) {
        if (tip != null) {
            $('#waitTip').html(tip);
        }
        $('.waitDiv').show();
        $('.imgDiv').show();
    }
    /**
     * 关闭等待提示曾
     */
    function closeWait() {
        $('.waitDiv').hide();
        $('.imgDiv').hide();
    }

    var comMethod;//dictWindow使用的回调方法


    /**
     * 公用数据字典窗口
     * @tableName 表名
     * @fieldName 字段名
     * @title     窗口标题
     * @nameObj   接收字典数据名称 （例:北京市东城区）
     * @codeObj   接收字典数据代码 （例:110101）
     * @fullObj   接收字典代码名称组合 （例:110101-东城区）
     * @selType   业务类型（1：查询单位 存在于ZB02 and 不存在于B01的数据）
     * @method    回调方法 仅限于选择代码项后调用，没选择或者取消则不调用
     */
    function dictWindow(tableName, fieldName, title, nameObj, codeObj, fullObj, selType, method, valType) {

        clearTimeout(timeFn);

        curDictFull = fullObj;
        curDictCode = codeObj;
        curDictName = nameObj;
        if (selType == null) {
            selType = 1;
        }

        if (method != null && typeof(method) != 'undefined') {
            comMethod = method;
        } else {
            comMethod = null;
        }

        var data = {
            "tableName": tableName,
            "fieldName": fieldName,
            "selType": selType,
            "title": title,
            "valType": valType
        }

        $('#dict_window_content').load('<%=pt%>common/dict/dictIndex.do', data);
        $("#dict_window").dialog({
            resizable: false,
            draggable: true,
            height: 550,
            width: 400,
            modal: true,
            title: title,
            buttons: {},
            close: function () {
                $('#dict_window_content').html('');//关闭后要清空字典数据，否则下次弹出时会显示上一次代码内容
                $('#' + nameObj).change();
                $('#' + nameObj).attr("title",$('#' + nameObj).val());//增加title显示
            }
        });
    }



    /**
     * 公用数据字典窗口
     * @tableName 表名
     * @fieldName 字段名
     * @title     窗口标题
     * @nameObj   接收字典数据名称 （例:北京市东城区）
     * @codeObj   接收字典数据代码 （例:110101）
     * @fullObj   接收字典代码名称组合 （例:110101-东城区）
     * @selType   类型（1：单选，2：多选，没有回填，3：多选回填参数）
     * @checkedId 已选中的ID
     */
    function dictWindow3(tableName, fieldName, title, nameObj, codeObj, fullObj, selType, checkedId) {
        curDictFull = fullObj;
        curDictCode = codeObj;
        curDictName = nameObj;
        if (selType == null) {
            selType = 3;
        }

        var data = {
            "tableName": tableName,
            "fieldName": fieldName,
            "selType": selType,
            "title": title
        }
        var url = '<%=pt%>common/dict/dictIndex.do';
        if (checkedId) {
            url = url + '?checkedId=' + checkedId;
        }
        $('#dict_window_content').load(url, data);
        $("#dict_window").dialog({
            resizable: false,
            draggable: true,
            height: 550,
            width: 400,
            modal: true,
            title: title,
            buttons: {},
            close: function () {
                $('#dict_window_content').html('');//关闭后要清空字典数据，否则下次弹出时会显示上一次代码内容
                $('#' + nameObj).change();
                $('#' + nameObj).attr("title",$('#' + nameObj).val());//增加title显示
            }
        });
    }


    /**
     * 单位选择窗口
     * @title  窗口标题
     * @treeType  树类型 (1:完整树  2:实单位与分组   3:虚单位与分组 ，4:分组信息   5:归口信息 6:库不可选)
     * @selType   指标树选择标志（1：单选，2：多选）,暂时只支持单选，多选可后续扩展
     * @callbackMethod  回调方法
     * @defLibraryId 默认选中库
     */

    function unitWindow(title, treeType, selType, defLibraryId, callbackMethod) {
        if (selType == null) {
            selType = 1;
        }
        //添加回调函数
        if (callbackMethod != null && typeof(callbackMethod) != 'undefined') {
            unitCallbacks = $.Callbacks();
            unitCallbacks.add(callbackMethod);
        }

        var data = {
            "treeType": treeType,
            "selType": selType,
            "defLibraryId": defLibraryId
        }

        $('#dict_window_content').load('<%=pt%>common/unitTree/treePage.do', data);
        $("#dict_window").dialog({
            resizable: false,
            draggable: true,
            height: 550,
            width: 400,
            modal: true,
            title: title,
            buttons: {}
        });
    }


    /**
     * 关闭公共弹窗
     */
    function closeWindow(index) {
        if (index == null || typeof(index) == 'undefined') {
            index = 1;
        }
        $("#pub_window" + index).dialog('close');
        $('#pub_window_content' + index).html('');
    }

    /**
     * 公共弹窗，新增修改数据时调用
     * @title  弹窗标题
     * @url    内容页面链接
     * @index  打开的弹窗序号，总共三层（1,2,3）
     * @width  宽度
     * @height 高度
     * @data 请求参数 存在的时候 post方式提交
     * @closeMethod 窗口关闭时调用的方法
     */
    function openWindow(title, url, index, width, height, data, closeMethod) {
        //默认开第一个窗口
        if (index == null || typeof(index) == 'undefined') {
            index = 1;
        }
        $('#pub_window_content' + index).css('padding-top', '10px');

        var getTimestamp = new Date().getTime();

        if (data == null || typeof(data) == 'undefined') {
            if (url.indexOf("?") > -1) {
                url = url + "&timestamp=" + getTimestamp;
            } else {
                url = url + "?timestamp=" + getTimestamp;
            }
        }

        $('#pub_window_content' + index).html('');
        if (data != null) {
            $('#pub_window_content' + index).load('<%=pt%>' + url, data);
        } else {
            $('#pub_window_content' + index).load('<%=pt%>' + url);
        }


        var dragFlag = true;
        if (height == '' || height == null) {
            height = 600;
        } else if (height == 'max') {
            height = $(window).height() - 5;
            dragFlag = false;
        } else if (height.toString().substring(height.length - 1, height.length) == '%') {
            height = $(window).height() * (height.substring(0, height.length - 1)) / 100;
        }
        if (width == '' || width == null) {
            width = 900;
        } else if (width == 'max') {
            width = $(window).width() - 5;
        } else if (width.toString().substring(width.length - 1, width.length) == '%') {
            width = $(window).width() * (width.substring(0, width.length - 1)) / 100;
        }

        $("#pub_window" + index).dialog({
            resizable: false,
            draggable: dragFlag,
            height: height,
            width: width,
            modal: true,
            title: title,
            buttons: {},
            close: function (event, ui) {
                if (typeof(closeMethod) != 'undefined' && closeMethod != null) {//调用回调方法,该方法从dictWindow方法中的参数来
                    var callbacks = $.Callbacks();
                    callbacks.add(closeMethod);
                    callbacks.fireWith(window, new Array());
                }
            }
        });
    }

    function openRmbWin(title, url, data, closeMethod, winWidth) {
        $('#pub_rmb_window_content').css('padding-top', '10px');
        var rmbWinWidth = 1000;
        var getTimestamp = new Date().getTime();

        if (data == null || typeof(data) == 'undefined') {
            if (url.indexOf("?") > -1) {
                url = url + "&timestamp=" + getTimestamp;
            } else {
                url = url + "?timestamp=" + getTimestamp;
            }
        }

        $('#pub_rmb_window_content').html('');
        if (data != null) {
            $('#pub_rmb_window_content').load('<%=pt%>' + url, data);
        } else {
            $('#pub_rmb_window_content').load('<%=pt%>' + url);
        }
        if (typeof(winWidth) != 'undefined') {
            rmbWinWidth = winWidth;
        }
        $("#pub_rmb_window").dialog({
            resizable: false,
            draggable: true,
            height: $(window).height() - 5,
            width: rmbWinWidth,
            modal: true,
            title: title,
            buttons: {},
            close: function (event, ui) {
                if (closeMethod != null && typeof(closeMethod) != 'undefined') {
                    closeMethod();
                }
                $("#pub_rmb_window").dialog('destroy');
            }
        });

        $("#pub_rmb_window").bind('dialogbeforeclose', function (event, ui) {
            if (changeFlag != null && typeof(changeFlag) != 'undefined' && changeFlag) {
                openConfirmNotSave(saveTip, function () {
                    changeFlag = false;
                    $("#pub_rmb_window").dialog('close');
                    $('#pub_rmb_window_content').html('');
                });
                return false;
            } else {
                return true;
            }
        });
    }

    function openWindowHasTip(title, url, index, width, height, data, closeMethod) {
        //默认开第一个窗口
        if (index == null || typeof(index) == 'undefined') {
            index = 1;
        }
        $('#pub_window_content' + index).css('padding-top', '10px');
        var getTimestamp = new Date().getTime();

        if (data == null) {
            if (url.indexOf("?") > -1) {
                url = url + "&timestamp=" + getTimestamp;
            } else {
                url = url + "?timestamp=" + getTimestamp;
            }
        }

        $('#pub_window_content' + index).html('');
        if (data != null) {
            $('#pub_window_content' + index).load('<%=pt%>' + url, data);
        } else {
            $('#pub_window_content' + index).load('<%=pt%>' + url);
        }


        var dragFlag = true;
        if (height == 'max') {
            height = $(window).height() - 5;
            dragFlag = false;
        } else if (height == '' || height == null) {
            height = 600;
        }
        if (width == 'max') {
            width = $(window).width() - 5;
        } else if (width == '' || width == null) {
            width = 900;
        }

        $("#pub_window" + index).dialog({
            resizable: false,
            draggable: dragFlag,
            height: height,
            width: width,
            modal: true,
            title: title,
            buttons: {},
            close: function (event, ui) {
                $("#pub_window" + index).unbind('dialogbeforeclose');
                if (closeMethod != null && typeof(closeMethod) != 'undefined') {
                    closeMethod();
                }
            }
        });

        $("#pub_window" + index).bind('dialogbeforeclose', function (event, ui) {
            if (changeFlag != null && typeof(changeFlag) != 'undefined' && changeFlag) {
                openConfirmNotSave(saveTip, function () {
                    changeFlag = false;
                    $("#pub_window" + index).dialog('close');
                    $('#pub_window_content' + index).html('');
                });
                return false;
            } else {
                return true;
            }
        });
    }


    function openWindow3(title, url, index, width, height, data, closeMethod) {
        //默认开第一个窗口
        if (index == null || typeof(index) == 'undefined') {
            index = 1;
        }
        $('#pub_window_content' + index).css('padding-top', '10px');
        var getTimestamp = new Date().getTime();

        if (data == null) {
            if (url.indexOf("?") > -1) {
                url = url + "&timestamp=" + getTimestamp;
            } else {
                url = url + "?timestamp=" + getTimestamp;
            }
        }

        $('#pub_window_content' + index).html('');
        if (data != null) {
            $('#pub_window_content' + index).load('<%=pt%>' + url, data);
        } else {
            $('#pub_window_content' + index).load('<%=pt%>' + url);
        }


        var dragFlag = true;
        if (height == 'max') {
            height = $(window).height() - 5;
            dragFlag = false;
        } else if (height == '' || height == null) {
            height = 600;
        }
        if (width == 'max') {
            width = $(window).width() - 5;
        } else if (width == '' || width == null) {
            width = 900;
        }

        $("#pub_window" + index).dialog({
            resizable: false,
            draggable: dragFlag,
            height: height,
            width: width,
            modal: true,
            title: title,
            buttons: {},
            close: function (event, ui) {
                if (closeMethod != null && typeof(closeMethod) != 'undefined') {
                    closeMethod();
                }
            }
        });
    }
    function openWindow2(title, url, index, selType, width, height, data, callbackMethod, closeMethod) {
        if (selType == null || typeof(selType) == 'undefined') {
            selType = 2;
        }

        //默认开第一个窗口
        if (index == null || typeof(index) == 'undefined') {
            index = 1;
        }
        $('#pub_window_content' + index).css('padding-top', '10px');
        var getTimestamp = new Date().getTime();
        if (url.indexOf("?") > -1) {
            url = url + "&timestamp=" + getTimestamp;
        } else {
            url = url + "?timestamp=" + getTimestamp;
        }
        url = url + "&selType=" + selType;
        //添加回调函数
        if (callbackMethod != null && typeof(callbackMethod) != 'undefined') {
            groupCallbacks = $.Callbacks();
            groupCallbacks.add(callbackMethod);
            adjustCallbacks = $.Callbacks();
            adjustCallbacks.add(callbackMethod);
        }
        $('#pub_window_content' + index).html('');
        if (data != null) {
            $('#pub_window_content' + index).load('<%=pt%>' + url, data);
        } else {
            $('#pub_window_content' + index).load('<%=pt%>' + url);
        }
        var dragFlag = true;
        if (height == 'max') {
            height = $(window).height() - 5;
            dragFlag = false;
        } else if (height == '' || height == null) {
            height = 600;
        }
        if (width == 'max') {
            width = $(window).width() - 5;
        } else if (width == '' || width == null) {
            width = 900;
        }

        $("#pub_window" + index).dialog({
            resizable: false,
            draggable: true,
            height: height,
            width: width,
            modal: true,
            title: title,
            buttons: {},
            close: function (event, ui) {
                if (closeMethod != null && typeof(closeMethod) != 'undefined') {
                    closeMethod();
                }
            }
        });
    }


    function openWebOfficeWindow(title, officeSetting, index, width, height) {
        var url = '<%=pt%>common/webOffice/docPreview.do';
        //默认开第一个窗口
        if (index == null || typeof(index) == 'undefined') {
            index = 1;
        }
        $('#pub_window_content' + index).css('padding-top', '10px');
        var getTimestamp = new Date().getTime();
        if (url.indexOf("?") > -1) {
            url = url + "&timestamp=" + getTimestamp;
        } else {
            url = url + "?timestamp=" + getTimestamp;
        }

        var dragFlag = true;
        if (height == 'max') {
            height = $(window).height() - 5;
            dragFlag = false;
        } else if (height == '' || height == null) {
            height = 600;
        }
        if (width == 'max') {
            width = $(window).width() - 5;
        } else if (width == '' || width == null) {
            width = 900;
        }

        $('#pub_window_content' + index).html('');

        //$('#pub_window_content'+index).load(docUrl, officeSetting);
        var webofficeHtml = '<iframe frameborder="no" name="webOfficeIframe" id="webOfficeIframe" width="98%" height="' + (height - 70) + '"></iframe>';
        webofficeHtml = webofficeHtml + '<form id="webOfficeForm" name="webOfficeForm"  method="post" target="webOfficeIframe" action="' + url + '">';
        for (var key in officeSetting) {

            var val = officeSetting[key];
            //alert(key + ":" + val);
            if ('bussData' == key) {
                webofficeHtml = webofficeHtml + '<textarea style="display:none;" name="' + key + '">' + val + '</textarea>';
            } else {
                webofficeHtml = webofficeHtml + '<input type="hidden" name="' + key + '" value="' + val + '">';
            }

        }
        webofficeHtml = webofficeHtml + '</form>';
        //alert(webofficeHtml);
        $('#pub_window_content' + index).html(webofficeHtml);

        $("#pub_window" + index).dialog({
            resizable: false,
            draggable: dragFlag,
            height: height,
            width: width,
            modal: true,
            title: title,
            buttons: {},
            close: function (event, ui) {
                var el = document.getElementById("webOfficeIframe"),
                    iframe = el.contentWindow;
                //1. 调用控件close方法
                //$("#webOfficeIframe")[0].contentWindow.closeCurrentDoc();
                if (el) {
                    try {
                        iframe.closeCurrentDoc();
                    } catch (e) {
                    }
                    ;

                    try {
                        iframe.document.write('');
                        iframe.document.clear();
                    } catch (e) {
                    }
                    ;
                    $(el).remove();
                    //document.body.removeChild(el);
                }
            }

        });
        //提交隐藏表单,表单的target属性指向iframe
        $("#webOfficeForm").submit();

        /*
         //使用ajax form target属性无效
         var ajaxFormOptions = {
         target: '#webOfficeIframe',
         dataType : 'json',
         type : "post",
         data : officeSetting,
         url : url
         };
         //$("#webOfficeForm").ajaxForm(ajaxFormOptions);
         //$("#webOfficeForm").submit();
         */

    }

    var nameObj;//选中树返回的名称
    var idObj;	//选中树返回的ID
    var method;//选中树对象
    var selTreeObject;
    /**
     * 公共弹窗，获取树形数据时调用
     * @param title
     * @param type    类型（1:菜单单选树 2:菜单多选树 3: 4：）
     * @param nameObj 接收名称的对象ID
     * @param idObj   接收ID的对象ID
     * @param width   宽度(px)
     * @param height  高度(px)
     * @param method  自定义的回调方法
     * @param checkedId 已选中的节点ID
     * @param optionId 对应type,自定义的复选框id集
     */
    function commonWindow(title, type, nameObj, idObj, width, height, method, checkedId, optionId) {
        this.nameObj = nameObj;
        this.idObj = idObj;
        this.method = method;
        if (width == null || typeof(width) == 'undefined') width = "400";
        if (height == null || typeof(height) == 'undefined') height = "500";
        if (title == null || typeof(title) == 'undefined') title = "选择";
        var url = '<%=pt%>commonTree/getAllTreePage.do?type=' + type;
        if (checkedId) {
            url = url + '&checkedId=' + checkedId;
        }
        if (type == 9 && optionId) {
            url = url + '&optionId=' + optionId;
        }
        var getTimestamp = new Date().getTime();
        url = url + "&timestamp=" + getTimestamp;

        $('#common_window_content').load(url);
        var btn = {
            "确定": function () {
                if (changeFlag != null && typeof(changeFlag) != 'undefined') {
                    changeFlag = true;
                }
                if (method != null && typeof(method) != 'undefined') {//回调方法
                    var callbacks = $.Callbacks();
                    callbacks.add(method);
                    callbacks.fireWith(window, new Array(selTreeObject));
                }
                $(this).dialog("close");
            },
            "取消": function () {
                $(this).dialog("close");
            }
        };
        if (type == 1) btn = {};
        $("#common_window").dialog({
            resizable: false,
            draggable: true,
            height: height,
            width: width,
            modal: true,
            title: title,
            buttons: btn
        });
    }

    /**
     * alert弹框
     * @content 提示信息
     */
    function openAlert(content) {
        $('#alert_dialog_content').html(content);
        $("#alert_dialog").dialog({
            resizable: false,
            draggable: false,
            height: 200,
            width: 350,
            modal: true,
            buttons: {
                "确定": function () {
                    $(this).dialog("close");
                    $('#alert_dialog_content').html('');
                }
            }
        });
    }

    /**
     * confirm弹框
     * @content 提示信息
     *
     *
     * @method  "确定"回调函数
     * @method2 "取消"回调函数
     */
    function openConfirm2(content, button1, button2, method1, method2) {
        $('#confirm_dialog_content').html(content);
        var buts = {};
        buts[button1] = function () {
            method1.call();
            $(this).dialog("close");
            $('#confirm_dialog_content').html('');
        };
        buts[button2] = function () {
            method2.call();
            $(this).dialog("close");
            $('#confirm_dialog_content').html('');
        };

        $("#confirm_dialog").dialog({
            resizable: false,
            draggable: false,
            height: 200,
            width: 350,
            modal: true,
            buttons: buts
        });
    }
    /**
     * confirm弹框
     * @content 提示信息
     * @method  回调函数
     * @closeMethod 关闭的回调函数
     */
    function openConfirm(content, method, closeMethod) {
        $('#confirm_dialog_content').html(content);
        $("#confirm_dialog").dialog({
            resizable: false,
            draggable: false,
            height: 200,
            width: 350,
            modal: true,
            buttons: {
                "确定": function () {
                    method.call();
                    $(this).dialog("close");
                    $('#confirm_dialog_content').html('');
                },
                "取消": function () {
                    $(this).dialog("close");
                }
            },
            close: function (event, ui) {
                if (closeMethod != null && typeof(closeMethod) != 'undefined') {
                    closeMethod();
                }
            }
        });
    }


    /**
     * confirm弹框
     * @content 提示信息
     * @method  回调函数
     * @closeMethod 关闭的回调函数
     */
    function openConfirmNotSave(content, method, closeMethod) {
        $('#confirm_dialog_content_not_save').html(content);
        $("#confirm_dialog_not_save").dialog({
            resizable: false,
            draggable: false,
            height: 200,
            width: 400,
            modal: true,
            buttons: {
                "不保存": function () {
                    method.call();
                    $(this).dialog("close");
                    $('#confirm_dialog_content_not_save').html('');
                },
                "取消": function () {
                    $(this).dialog("close");
                }
            },
            close: function (event, ui) {
                if (closeMethod != null && typeof(closeMethod) != 'undefined') {
                    closeMethod();
                }
            }
        });
    }

    /**
     * 弹出日期控件
     * @obj 调用者对象
     */

    function getDate(obj, format, realId) {
        if ($.parseHTML(obj.id) != null) {
            $('#' + obj.id).attr('readonly', false);
        }
        if (format == null || format == '' || typeof(format) == 'undefined') {
            format = 'yyyy.MM.dd';
            WdatePicker({
                dateFmt: format, onpicking: function (dp) {
                    $('#' + realId).val(checkAndFormatDate(dp.cal.getDateStr()));
                }, position: {left: -200, top: -250}
            });
        } else if (format == 'yyyy.MM.dd') {
            WdatePicker({
                dateFmt: format, onpicking: function (dp) {
                    $('#' + realId).val(checkAndFormatDate(dp.cal.getDateStr()));
                }, position: {left: -200, top: -250}
            });
        } else {
            WdatePicker({
                dateFmt: format, onpicking: function (dp) {
                    $('#' + realId).val(checkAndFormatDate(dp.cal.getDateStr()));
                }
            });
        }
        return;
    }

    /**
     * 日期内容变更时，修改标识重置
     */
    function resetChangeState() {
        if (changeFlag != null && typeof(changeFlag) != 'undefined') {
            changeFlag = true;
        }
    }

    function formatRealDate(obj, realId) {
        var date = $('#' + obj.id).val();
        if (date == '') {
            $('#' + realId).val('');
        } else {
            $('#' + realId).val(checkAndFormatDate(date));
        }
    }

    function showDate(obj) {
        $("#" + obj.id).datepicker({
            yearRange: '1920:2030',
            monthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
            dateFormat: 'yy-MM-dd',
            dayNamesMin: ['日', '一', '二', '三', '四', '五', '六', '七'],
            dayNames: ['日', '一', '二', '三', '四', '五', '六', '七'],
            changeYear: true,
            changeMonth: true,
            showMonthAfterYear: true
        });
    }

    /*
     * 格式化日期字符串
     * @strObj 日期字符串对象
     * @srcPattern 原字符串格式
     * @destPattern 目标字符串格式
     * 示例: formatDateStr('20160311','yymmdd','yy-mm-dd'),  结果返回2016-03-11
     * 此方法依赖于jquery ui datepicker
     */
    function formatDateStr(strObj, srcPattern, destPattern) {
        if (strObj == null) {
            return strObj;
        }
        if (srcPattern == null) {
            srcPattern = "yymmdd";
        }
        var temp = $.datepicker.parseDate(srcPattern, strObj);
        if (temp == null) {
            return strObj;
        }
        if (destPattern == null) {
            destPattern = "yy-mm-dd";
        }
        return $.datepicker.formatDate(destPattern, temp);
    }


    /**
     * 上移下移功能
     * @param obj 表格对象
     * @param flag ('up':上移标志，'down':下移标志)
     */
    function moveTheOrder(obj, order) {
        if (order == null || typeof(order) == 'undefined') {
            openAlert('请填写排序号！');
            return;
        }
        if (typeof(order) == 'string') {
            if (order == '') {
                openAlert('请填写排序号！');
                return;
            }
            order = parseInt(order);
        }
        if (isNaN(order)) {
            openAlert('请填写正确的排序号！');
            return;
        }
        var selectedObj = []
        selectedObj = obj.selectedRows();		 //获得选中数据
        if (selectedObj.length == 0) {
            openAlert('请选择一条记录！');
            return;
        }
        if (obj.selectedRowsIndex().length > 1) {
            openAlert('只能选择一条记录！');
            return;
        }
        var allRowsNum = obj.rows().length;
        if (order < 1 || order > allRowsNum) {
            openAlert('要移动到的位置超出列表的范围！');
            return;
        }
        var item = selectedObj[0];
        order = order - 1;
        var selectedIndex = obj.selectedRowsIndex()[0]; //获得选中数据所在行
        obj.removeRow(selectedIndex);
        obj.addRow(item, order);
        obj.select(order);
    }

    /**
     * 上移下移功能
     * @param obj 表格对象
     * @param flag ('up':上移标志，'down':下移标志)
     */
    function resetOrder(obj, flag) {
        if (flag == null || typeof(flag) == 'undefined') {
            return;
        }

        var selectedObj = []
        selectedObj = obj.selectedRows();		 //获得选中数据
        if (selectedObj.length == 0) {
            openAlert('请选择一条记录！');
            return;
        }
        var selectedIndex = [];
        var allRowsNum = obj.rows().length;	     //所有行数
        var allData = obj.rows();				 //所有数据
        selectedIndex = obj.selectedRowsIndex(); //获得选中数据所在行
        if (flag == 'up') {//上移
            for (var i in selectedIndex) {
                if (selectedIndex[0] == 0)return;//已经是第一行，不再上移
                obj.removeRow(selectedIndex[i]);
                obj.addRow(selectedObj[i], selectedIndex[i] - 1);
                obj.select(selectedIndex[i] - 1);
            }
        } else {//下移
            for (var i in selectedIndex) {
                if (selectedIndex[selectedIndex.length - 1] == (allRowsNum - 1))return;//已经是最后一行，不再下移
                obj.removeRow(selectedIndex[selectedIndex.length - 1 - i]);
                obj.addRow(selectedObj[selectedIndex.length - 1 - i], selectedIndex[selectedIndex.length - 1 - i] + 1);
                obj.select(selectedIndex[selectedIndex.length - 1 - i] + 1);
            }
        }
    }

    var cachesTreeData = [];//输入框双击显示Tree缓存

    // 下拉缓存
    var cacheDictProject = {};
    // 所有代码缓存
    var cacheDict = {};
    // 常用代码缓存
    var cacheCommonDict = {};
    function getDictData(tableName, fieldName, async) {
        if (typeof(async) == "undefined") {
            async = false;
        }

        var cacheKey = tableName + fieldName;
        var projects = [];
        var codeNameArray = [];
        if (cacheKey == 'A11a1131') {
            return;
        }
        $.ajax({
            type: 'post',
            async: false,
            url: '<%=pt%>common/dict/dictData.do',
            data: {"tableName": tableName, "fieldName": fieldName},
            success: function (data) {
                var distData = [];
                var isExistCommon = false;

                for (var i = 0; i < data.length; i++) {//var i = data.length-1;i!=-1;i--  //为啥之前要倒序输出？谁能给个说法？
                    if (data[i].isCommon == 1) {
                        isExistCommon = true;
                    }

                    //去除codeName相同的代码
                    if (!isArrayContain(codeNameArray, data[i])) {
                        distData.push(data[i]);
                        codeNameArray.push(data[i].codeName);
                    }
                }

                for (var i = 0; i < distData.length; i++) {
                    var elem = distData[i];
                    if (isExistCommon && elem.isCommon != 1) {
                        continue;
                    }

                    var noteType = elem.noteType;
                    if ("library" == noteType || "unitGroup" == noteType || "subGroup" == noteType) {
                        continue;
                    }
                    projects.push({
                        value: elem.code,
                        label: elem.codeName + ' ' + elem.codeSpelling,
                        desc: elem.codeName,
                        codeAbr1: elem.codeAbr1
                    });
                }

                if (cacheKey == 'A01a0111b' || cacheKey == 'A01a0114b') {
                    $.data(cacheDictProject, 'A01a0111b', projects);
                    $.data(cacheDict, 'A01a0111b', data);

                    $.data(cacheDictProject, 'A01a0114b', projects);
                    $.data(cacheDict, 'A01a0114b', data);
                } else {
                    $.data(cacheDictProject, cacheKey, projects);
                    $.data(cacheDict, cacheKey, data);
                }
            }
        });

    }

    function isArrayContain(commonData, data) {

        var length = commonData.length;
        var indexName = data.codeName;
        if (length == 0) {
            return false;
        }

        if (length == 1) {
            if (commonData[0] == indexName) {
                return true;
            }

            return false;
        }


        if (commonData[0] == indexName || commonData[length - 1] == indexName) {
            return true;
        }

        indexName = "," + data.codeName + ",";
        var datas = commonData.toString();
        if (datas.indexOf(indexName) > -1) {
            return true;
        }
        return false;
    }

    /**
     * @returnType 返回类型 1 返回全称 null或其它返回简称
     */
    var timeFn = null;
    function loadDict(tableName, fieldName, selName, selId, returnType) {
        clearTimeout(timeFn);
        //延迟300执行， 如果是有触发双击事件则不执行相应代码
        timeFn = setTimeout(function () {
            var hash = {};
            var cacheKey = tableName + fieldName;
            var projects = $.data(cacheDictProject, cacheKey);

            if (typeof(projects) == 'undefined') {
                //重新从服务器加载
                getDictData(tableName, fieldName);
                projects = $.data(cacheDictProject, cacheKey);
            }

            var minLength = 0;
            if (fieldName == 'a0114b' || fieldName == 'a0111b' || fieldName == 'a1201') {
                minLength = 2;
            } else {
                minLength = 0;
            }
            $("#" + selName).autocomplete({
                minLength: minLength,
                source: projects,
                focus: function (event, ui) {
                    //$( "#"+selName ).val( ui.item.label );
                    return false;
                },
                select: function (event, ui) {
                    if (changeFlag != null && typeof(changeFlag) != 'undefined') {
                        changeFlag = true;
                    }
                    if (typeof(returnType) != 'undefined' && returnType == 1) {
                        $("#" + selName).val(ui.item.desc);
                    } else {
                        $("#" + selName).val(ui.item.codeAbr1);
                    }
                    $("#" + selId).val(ui.item.value);
                    //数据清洗去除边框
                    //$('#'+selId).deactivate();
                    //$('#'+selName).deactivate();
                    $('#' + selId).removeClass('unFix');
                    $('#' + selName).removeClass('unFix');
                    return false;
                }
            })
                .autocomplete("instance")._renderItem = function (ul, item) {
                return $("<li class='search'>").data("item.autocomplete", item)
                    .append("<a>" + item.desc + "</a>")
                    .appendTo(ul);
            };
        }, 300);

    }

    function cleanNull(selName, selId) {
        if ($('#' + selName).val() == '') $('#' + selId).val('');
        if ($('#' + selId).val() == '') $('#' + selName).val('');
    }

    function findCadres(selName) {
        var keywords = $('#' + selName).val();
        var projects = [];
        var repeat = {};
        $.ajax({
            type: 'post',
            url: '<%=pt%>common/dict/cadreData.do',
            data: {"keywords": keywords},
            success: function (data) {
                for (var i in data) {
                    projects.push({
                        value: data[i].a0102,
                        label: data[i].a0101 + ' ' + data[i].a0102,
                        desc: data[i].a0101
                    });
                }
            }
        });

        $("#" + selName).autocomplete({
            minLength: 0,
            source: projects,
            focus: function (event, ui) {
                return false;
            },
            select: function (event, ui) {
                $("#" + selName).val(ui.item.desc);
                return false;
            }
        })
            .autocomplete("instance")._renderItem = function (ul, item) {
            return $("<li class='search'>").data("item.autocomplete", item)
                .append("<a>" + item.desc + "</a>")
                .appendTo(ul);
        };
    }

    function excelStatisticTemplate() {
        openWindow('简要数据统计', '/report/engine/excelStatisticTemplate.do', 2, 250, 365, {});
    }

    function showExcelProUnitCadre(personIds, position) {
        showWait('数据生成中，请稍候');
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/getProUnitCadreExcel.do',
            data: {cadreIds: personIds},
            success: function (result) {
                closeWait();
                if (result.type == "success") {
                    var filePath = '<%=pt%>' + result.content;
                    var officeSetting = {
                        'docUrl': filePath,	//文件url, 可以是输出流的url,也可以是服务器文件的url
                        'docName': result.params,			//文档名字,1.作为上传保存到服务器的文件名  2.用户保存到本地的默认文件名
                        'docType': 'Excel.Sheet',
                        'readOnly': false,			//是否只读, 只读将因此工具栏和菜单栏, 非office的只读模式
                        'uploadSupport': false,		//上传使用, 表示是否支持编辑后保存到服务器(true:展示保存到服务器按钮,且必须指定uploadUrl参数),此参数为false的话将忽略uploadUrl,uploadUrl,bussData这3个参数
                    }
                    if (position == null) {
                        openWebOfficeWindow('省组干部名册', officeSetting, 1, 1200, 'max');
                    } else {
                        openWebOfficeWindow('省组干部名册', officeSetting, position, 1200, 'max');
                    }
                }
            }
        });
    }
    function showExcelRoster(personIds, position, isThisDuty, upCenterCommittee) {
        showWait('数据生成中，请稍候');
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/getCadreRosterExcel.do',
            data: {cadreIds: personIds, isThisDuty: isThisDuty, upCenterCommittee: upCenterCommittee},
            success: function (result) {
                closeWait();
                if (result.type == "success") {
                    //"resource/doc/cadreInfo.doc"
                    var filePath = '<%=pt%>' + result.content;
                    var officeSetting = {
                        //'docNo':32,			//如果是第二次编辑,则可以传递文号，系统自动根据文号加载文档，如果设置此参数,则自动忽略docUrl参数。
                        'docUrl': filePath,	//文件url, 可以是输出流的url,也可以是服务器文件的url
                        'docName': '',			//文档名字,1.作为上传保存到服务器的文件名  2.用户保存到本地的默认文件名
                        'docType': 'Excel.Sheet',
                        'readOnly': false,			//是否只读, 只读将因此工具栏和菜单栏, 非office的只读模式
                        'uploadSupport': false,		//上传使用, 表示是否支持编辑后保存到服务器(true:展示保存到服务器按钮,且必须指定uploadUrl参数),此参数为false的话将忽略uploadUrl,uploadUrl,bussData这3个参数
                        //'uploadUrl':'http://localhost:8080/common/webOffice/saveDocUpload.do',			 //上传使用, 指定上传数据处理url
                        //'operater':'sessionUser',	//上传使用, 由于ocx控件本身不保存会话状态, 所以如果上传文件建议提供上传操作人.
                        //'bussData':'jsonData'		//上传使用, 业务数据json
                    }
                    if (position == null) {
                        openWebOfficeWindow('文件预览', officeSetting, 1, 1200, 'max');
                    } else {
                        openWebOfficeWindow('文件预览', officeSetting, position, 1200, 'max');
                    }
                }
            }
        });
    }

    function showWordCadreInfoExcel(personIds, position) {
        showWait('数据生成中，请稍候');
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/createAppointAndRemovalOfTheExcel.do',
            data: {personIds: personIds},
            success: function (result) {
                closeWait();
                if (result.type == "success") {
                    var filePath = '<%=pt%>' + result.content;
                    var officeSetting = {
                        //'docNo':32,			//如果是第二次编辑,则可以传递文号，系统自动根据文号加载文档，如果设置此参数,则自动忽略docUrl参数。
                        'docUrl': filePath,	//文件url, 可以是输出流的url,也可以是服务器文件的url
                        'docName': result.params,			//文档名字,1.作为上传保存到服务器的文件名  2.用户保存到本地的默认文件名
                        'docType': 'Excel.Sheet',
                        'readOnly': false,			//是否只读, 只读将因此工具栏和菜单栏, 非office的只读模式
                        'uploadSupport': false,		//上传使用, 表示是否支持编辑后保存到服务器(true:展示保存到服务器按钮,且必须指定uploadUrl参数),此参数为false的话将忽略uploadUrl,uploadUrl,bussData这3个参数
                        //'uploadUrl':'http://localhost:8080/common/webOffice/saveDocUpload.do',			 //上传使用, 指定上传数据处理url
                        //'operater':'sessionUser',	//上传使用, 由于ocx控件本身不保存会话状态, 所以如果上传文件建议提供上传操作人.
                        //'bussData':'jsonData'		//上传使用, 业务数据json
                    }
                    if (position == null) {
                        openWebOfficeWindow('文件预览', officeSetting, 1, 1200, 'max');
                    } else {
                        openWebOfficeWindow('文件预览', officeSetting, position, 1200, 'max');
                    }
                }
            }
        });
    }
    function exportExcelCadreInfoZip(personIds) {
        var data = {title: "选择导出类型", values: "single,mulit", text: "单文件导出,多文件压缩包导出"};
        openWindow3("文件导出类型", 'index/valueChoosePage.do', 3, 350, 150, data, function () {
                if (vcbackVal != "") {
                    $.ajax({
                        type: "POST",
                        url: '<%=pt%>unit/cadre/getZipOfCadreInfoExcels.do',
                        data: {personIds: personIds, fileStyle: vcbackVal},
                        success: function (result) {
                            if (result.type == "success") {
                                $('#loadfileNameOfCadreInfo').val(result.content);
                                $('#loadFileOfCadreInfo').submit();
                            }
                        }
                    });
                }
            }
        );
    }
    /**
     *导出单张excel任免表
     */
    function exportAExcelCadreInfo(personIds) {
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/getZipOfCadreInfoExcels.do',
            data: {personIds: personIds, fileStyle: "single"},
            success: function (result) {
                if (result.type == "success") {
                    $('#loadfileNameOfCadreInfo').val(result.content);
                    $('#loadFileOfCadreInfo').submit();
                }
            }
        });
    }
    /**
     *导出人员lrm文件压缩包
     */
    function showCadreLrmOut(personIds) {
        showWait('数据生成中，请稍候');
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/getCadreLRMInfos.do',
            data: {personIds: personIds},
            success: function (result) {
                closeWait();
                if (result.type == "success") {
                    $('#loadfileNameOfCadreInfo').val(result.content);
                    $('#loadFileOfCadreInfo').submit();
                }
            }
        });
    }
    function exportRealReport(ids) {
        $.ajax({
            type: "POST",
            url: '<%=pt%>supervise/real/exportRealReportFile.do',
            data: {ids: ids},
            success: function (result) {
                if (result.type == "success") {
                    $('#loadfileNameOfCadreInfo').val(result.content);
                    $('#loadFileOfCadreInfo').submit();
                }
            }
        });
    }
    function exportCasesReport(caseId) {
        $.ajax({
            type: "POST",
            url: '<%=pt%>supervise/cases/exportCadreCasesReportFile.do',
            data: {caseId: caseId},
            success: function (result) {
                if (result.type == "success") {
                    $('#loadfileNameOfCadreInfo').val(result.content);
                    $('#loadFileOfCadreInfo').submit();
                }
            }
        });
    }

    function printDoc(personId, isSpecialAudit, position) {
        var wordFormStyle = '';
        if (isSpecialAudit) {
            wordFormStyle = 'specialAudit';
        }
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/getCadreInfoWords.do',
            data: {personIds: personId, wordFormStyle: wordFormStyle},
            success: function (result) {
                if (result.type == "success") {
                    var filePath = '<%=pt%>' + result.content;
                    var officeSetting = {
                        //'docNo':32,			//如果是第二次编辑,则可以传递文号，系统自动根据文号加载文档，如果设置此参数,则自动忽略docUrl参数。
                        'docUrl': filePath,	//文件url, 可以是输出流的url,也可以是服务器文件的url
                        'docName': result.params,			//文档名字,1.作为上传保存到服务器的文件名  2.用户保存到本地的默认文件名
                        'docType': 'Word.Document',
                        'readOnly': false,			//是否只读, 只读将因此工具栏和菜单栏, 非office的只读模式
                        'print': true,				//是否打开文件就执行打印
                        'fullScreen': false			//是否默认全屏

                    }
                    if (position == null) {
                        openWebOfficeWindow('文件预览', officeSetting, 1, 1200, 'max');
                    } else {
                        openWebOfficeWindow('文件预览', officeSetting, position, 1200, 'max');
                    }

                }
            }
        });
    }

    function printExcel(personId, position) {
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/createAppointAndRemovalOfTheExcel.do',
            data: {personIds: personId},
            success: function (result) {
                if (result.type == "success") {
                    var filePath = '<%=pt%>' + result.content;
                    var officeSetting = {
                        //'docNo':32,			//如果是第二次编辑,则可以传递文号，系统自动根据文号加载文档，如果设置此参数,则自动忽略docUrl参数。
                        'docUrl': filePath,	//文件url, 可以是输出流的url,也可以是服务器文件的url
                        'docName': result.params,			//文档名字,1.作为上传保存到服务器的文件名  2.用户保存到本地的默认文件名
                        'docType': 'Excel.Sheet',
                        'readOnly': false,			//是否只读, 只读将因此工具栏和菜单栏, 非office的只读模式
                        'print': true,				//是否打开文件就执行打印
                        'fullScreen': false			//是否默认全屏
                    }
                    if (position == null) {
                        openWebOfficeWindow('文件预览', officeSetting, 1, 1200, 'max');
                    } else {
                        openWebOfficeWindow('文件预览', officeSetting, position, 1200, 'max');
                    }

                }
            }
        });
    }
    function showWordCadreInfo(personIds, isSpecialAudit, position) {
        var wordFormStyle = '';
        if (isSpecialAudit) {
            wordFormStyle = 'specialAudit';
        }
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/getCadreInfoWords.do',
            data: {personIds: personIds, wordFormStyle: wordFormStyle},
            success: function (result) {
                if (result.type == "success") {
                    var filePath = '<%=pt%>' + result.content;
                    var officeSetting = {
                        //'docNo':32,			//如果是第二次编辑,则可以传递文号，系统自动根据文号加载文档，如果设置此参数,则自动忽略docUrl参数。
                        'docUrl': filePath,	//文件url, 可以是输出流的url,也可以是服务器文件的url
                        'docName': result.params,			// 文档名字,1.作为上传保存到服务器的文件名  2.用户保存到本地的默认文件名
                        'docType': 'Word.Document',
                        'readOnly': false,			//是否只读, 只读将因此工具栏和菜单栏, 非office的只读模式
                        'uploadSupport': false,		//上传使用, 表示是否支持编辑后保存到服务器(true:展示保存到服务器按钮,且必须指定uploadUrl参数),此参数为false的话将忽略uploadUrl,uploadUrl,bussData这3个参数
                        //'uploadUrl':'http://localhost:8080/common/webOffice/saveDocUpload.do',			 //上传使用, 指定上传数据处理url
                        //'operater':'sessionUser',	//上传使用, 由于ocx控件本身不保存会话状态, 所以如果上传文件建议提供上传操作人.
                        //'bussData':'jsonData'		//上传使用, 业务数据json
                    }
                    if (position == null) {
                        openWebOfficeWindow('文件预览', officeSetting, 1, 1200, 'max');
                    } else {
                        openWebOfficeWindow('文件预览', officeSetting, position, 1200, 'max');
                    }

                }
            }
        });
    }

    function exportWordCadreInfoZip(personIds, isSpecialAudit) {
        var wordFormStyle = '';
        if (isSpecialAudit) {
            wordFormStyle = 'specialAudit';
        }

        var data = {title: "选择导出类型", values: "single,mulit", text: "单文件导出,多文件压缩包导出"};
        openWindow3("文件导出类型", 'index/valueChoosePage.do', 3, 350, 150, data, function () {
            if (vcbackVal != null && vcbackVal != "") {
                $.ajax({
                    type: "POST",
                    url: '<%=pt%>unit/cadre/getZipOfCadreInfoWords.do',
                    data: {personIds: personIds, wordFormStyle: wordFormStyle, fileStyle: vcbackVal},
                    success: function (result) {
                        if (result.type == "success") {
                            $('#loadfileNameOfCadreInfo').val(result.content);
                            $('#loadFileOfCadreInfo').submit();
                        }
                    }
                });
            }
        });
    }

    /*
     * 导出单张任免表word
     */
    function exportWordCadreInfo(personIds, isSpecialAudit) {
        var wordFormStyle = '';
        if (isSpecialAudit) {
            wordFormStyle = 'specialAudit';
        }
        $.ajax({
            type: "POST",
            url: '<%=pt%>unit/cadre/getZipOfCadreInfoWords.do',
            data: {personIds: personIds, wordFormStyle: wordFormStyle, fileStyle: "single"},
            success: function (result) {
                if (result.type == "success") {
                    $('#loadfileNameOfCadreInfo').val(result.content);
                    $('#loadFileOfCadreInfo').submit();
                }
            }
        });
    }

    // 获取查询出的干部Name
    function getSearchCadreName(cadreList) {
        var searchCadreName = [];
        var rows = cadreList.rows();
        var rowsData = [];
        //清空无数据时rows变成的[undefined]
        for (var i in rows) {
            if (typeof(rows[i]) != 'undefined') {
                rowsData.push(rows[i]);
            }
        }
        for (var i in rowsData) {
            searchCadreName.push(rowsData[i].a0101);
        }
        return searchCadreName;
    }

    // 获取查询出的干部ID
    function getSearchCadreId(cadreList) {
        var searchCadreId = [];
        var rows = cadreList.rows();
        var rowsData = [];
        //清空无数据时rows变成的[undefined]
        for (var i in rows) {
            if (typeof(rows[i]) != 'undefined') {
                rowsData.push(rows[i]);
            }
        }
        for (var i in rowsData) {
            searchCadreId.push(rowsData[i].a0000);
        }
        return searchCadreId;
    }

    //获取mmGrid表格的权限
    function getIsHasChangeAuth(cadreList) {
        var isHasChangeAuthArr = [];
        var rows = cadreList.rows();
        for (var i in rows) {
            isHasChangeAuthArr[i] = rows[i].isChange;
        }
        return isHasChangeAuthArr;
    }

    //点击列表含有下划线的企业显示任免表信息页面
    function loadEnterpriseInfo(enterSearchId,cadreList,isHasChangeAuth){
        var searchCadreId = getSearchCadreId(cadreList);
        var searchCadreName = getSearchCadreName(cadreList);
        //var isHasChangeAuthArr = getIsHasChangeAuth(cadreList);
        searchCadreId = searchCadreId.join(",");
        searchCadreName = searchCadreName.join(",");
        openRmbWin('查看企业信息','ent/manage/showEntUpdateMain.do',{a0000:enterSearchId,searchCadreId:searchCadreId,searchCadreName:searchCadreName},function(){
        });
    }

    //点击列表含有下划线的人名显示任免表信息页面
    function loadOneCadreInfo(cadreSearchId) {
        openWindow('查看企业信息', 'unit/cadre/showOneCadreUpdate.do', 1, 1000, 'max', {a0000: cadreSearchId});
    }

    function showApptCadreInfo(appointId) {
        // 拟任免职务前判断任免表是否完善（即CmisCandidate中a01是否存在）
        $.ajax({
            type: 'post',
            url: '<%=pt%>apptDismiss/adDuty/isExistA01.do',
            data: {appointId: appointId},
            success: function (data) {
                if (data.type == 'success') {
                    openWindow('企业基础信息', 'apptDismiss/candidateReportContent/showApptCadreInfo.do', 1, 1000, 'max', {appointId: appointId});
                    return;
                }
                openAlert(data.content);
            }
        });
        //openWindow('企业基础信息','apptDismiss/candidateReportContent/showApptCadreInfo.do',1,1000,'max',{appointId : appointId});

    }

    /**
     * 转换日期格式为yyyy.mm格式 表格日期展示用
     * @param date 日期
     */
    function formatDate(date) {
        if (date == null || date == 'null' || date == '' || typeof(date) == 'undefined') return '';
        var dates = date.split('、'),
            retuDate = '';
        for (var i in dates) {

            if (retuDate != '') {
                retuDate += '<br>' + formats(dates[i]);
            } else {
                retuDate += formats(dates[i]);
            }
        }
        return retuDate;
    };

    function formats(date) {
        if (date == null || date == '') return;
        var y = date.substring(0, 4);
        var m = date.substring(5, 7);
        var d = date.substring(8, 10);
        return y + '.' + m+ '.' + d;
    }

    function checkAndFormatDate(date) {
        if (date == '') return;
        if (date.length > 7) {
            return date.replace(/\./g, '-');
        }
        var dt = '';
        date = date.replace('.', '-');
        dt = date + '-01';
        return dt;
    }

    /**
     * 根据code查询机构名称
     * 监督系统专用
     * @param code 机构ID
     */
    function getUnitByCode(code) {
        var name = "";
        if (code == null) return "";
        $.ajax({
            type: 'post',
            url: '<%=pt%>common/dict/findUnitsByCode.do',
            data: {code: code},
            async: false,
            success: function (data) {
                name = data.name;
            }
        });
        return name;
    }

    function showCadreMdbOut(personIds) {
        showWait('数据生成中，请稍候');
        $.ajax({
            data: 'json',
            url: '<%=pt%>unit/exchange/exportMdbFile.do',
            data: {personIds: personIds},
            success: function (result) {
                closeWait();
                if (result.type == 'success') {
                    $('#loadfileNameOfCadreInfo').val(result.content);
                    $('#loadFileOfCadreInfo').submit();
                }
            }
        });
    }



    /**
     * 处理IE8下数组indexof的BUG
     */
    function arryBug() {
        if (!Array.prototype.indexOf) {
            Array.prototype.indexOf = function (elt /*, from*/) {
                var len = this.length >>> 0;
                var from = Number(arguments[1]) || 0;
                from = (from < 0)
                    ? Math.ceil(from)
                    : Math.floor(from);
                if (from < 0)
                    from += len;
                for (; from < len; from++) {
                    if (from in this &&
                        this[from] === elt)
                        return from;
                }
                return -1;
            };
        }
    }


    /**
     * 导出显示信息
     */
    function exportRealInfoReport(ids) {
        $.ajax({
            type: "POST",
            url: '<%=pt%>supervise/real/exportRealInfoReportFile.do',
            data: {ids: ids},
            success: function (result) {
                if (result.type == "success") {
                    $('#loadfileNameOfCadreInfo').val(result.content);
                    $('#loadFileOfCadreInfo').submit();
                }
            }
        });
    }

</script>