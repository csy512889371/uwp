<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../common.inc" %>
<head>
    <title>企业政策编辑</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <script src="<%=basePath%>resources/admin/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/admin/js/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/admin/js/ueditor/ueditor.config.policy.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/admin/js/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=basePath%>resources/admin/js/layui/layui.js"></script>
    <link rel="stylesheet" href="<%=basePath%>resources/admin/js/layui/css/layui.css">
    <link href="<%=basePath%>resources/admin/modules/policy/policy.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<%=basePath%>resources/admin/css/common.css">
    <script>
        var tempFileUrl  ="${tempFileUrl}";
    </script>
    <style>
        .webuploader-pick {
          /*padding: 5px!important;*/
            display: inline-block!important;
            height: 38px!important;
            line-height: 38px!important;
            padding: 0 18px!important;
            background-color: #1E9FFF!important;
            color: #fff!important;
            white-space: nowrap!important;
            text-align: center!important;
            font-size: 14px!important;
            border: none!important;
            border-radius: 2px!important;
            cursor: pointer!important;
            opacity: .9!important;
        }
    </style>
    <script type="text/javascript" src="<%=basePath%>resources/admin/js/webuploader-0.1.5/webuploader.js"></script>
    <link href="<%=basePath%>resources/admin/js/webuploader-0.1.5/webuploader.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=basePath%>resources/admin/modules/policy/policy.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/admin/modules/policy/fileUtil.js"></script>
</head>
<body onload="if($('#plcy003').val()!='')$('#btn-submit').html('保存');" style="overflow-x:hidden;">

<form id="policyEditForm" class="layui-form"   action="<%=basePath%>admin/policy/savePolicy.do">

    <div class="layui-form-item" style="padding-top: 20px;padding-buttom:0px">


    </div>

    <div class="layui-form-item" style="padding-top: 20px;padding-buttom:0px">
        <div class="layui-inline">
        <label class="layui-form-label" ><span class="spanRequired">*</span>政策标题</label>
        <div class="layui-input-block" style="width: 380px">
            <input type="text" name="plcy002" value="${shjtPolicy.plcy002}" lay-verify="plcy002"
                   placeholder="请输入政策标题" autocomplete="off" class="layui-input">
        </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label"><span class="spanRequired">*</span>政策类型</label>
            <div class="layui-input-block" style="width: 120px">
                <c:forEach items="${policyTypeList}" varStatus="i" var="item">
                    <c:if test="${item.code ==shjtPolicy.plcy001}">
                        <input type="text" value="${item.codeAbr1}" class="layui-input" disabled="">
                        <input type="hidden" name="plcy001" value="${shjtPolicy.plcy001}" lay-verify="plcy001"
                               class="layui-input">
                    </c:if>
                </c:forEach>
                <c:if test="${empty shjtPolicy.plcy001}">
                    <select id="select_plcy001" name="plcy001" lay-verify="plcy001" value="${shjtPolicy.plcy001}">
                        <option value=""></option>
                        <c:forEach items="${policyTypeList}" varStatus="i" var="item">
                            <option value="${item.code}">${item.codeAbr1}</option>
                        </c:forEach>
                    </select>
                </c:if>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label"><span class="spanRequired">*</span>发布来源</label>
            <div class="layui-input-block" style="width: 120px">
                <c:forEach items="${policySrcList}" varStatus="i" var="item">
                    <c:if test="${item.code ==shjtPolicy.plcy008}">
                        <input type="text" value="${item.codeAbr1}" class="layui-input" disabled="">
                        <input type="hidden" name="plcy008" value="${shjtPolicy.plcy008}" lay-verify="plcy008"
                               class="layui-input">
                    </c:if>
                </c:forEach>
                <c:if test="${empty shjtPolicy.plcy008}">
                    <select id="select_plcy008" name="plcy008" lay-verify="plcy008" value="${shjtPolicy.plcy008}">
                        <option value=""></option>
                        <c:forEach items="${policySrcList}" varStatus="i" var="item">
                            <option value="${item.code}">${item.codeAbr1}</option>
                        </c:forEach>
                    </select>
                </c:if>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label"><span class="spanRequired">*</span>状态</label>
            <div class="layui-input-block" style="width: 80px">
                    <select id="select_plcy006" name="plcy006" lay-verify="plcy006" value="${shjtPolicy.plcy006}">
                        <option value=""></option>
                        <option value=0  <c:if test="${shjtPolicy.plcy006==0}">selected="selected" </c:if>>草稿</option>
                        <option value=1  <c:if test="${shjtPolicy.plcy006==1}">selected="selected" </c:if>>发布</option>
                    </select>
            </div>
        </div>
        <div class="layui-inline">
            <button id="btn-submit"  class="layui-btn layui-btn-normal" lay-submit lay-filter="policyEditForm"
                    onclick="if(!checkValues())return false;">提交
            </button>
        </div>
        <div class="layui-inline">
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
        <%--文件上传--%>
        <div class="layui-inline">
            <label class="layui-form-label" >附件上传</label>
            <div class="layui-input-block">
                <div id="picker" ></div>
            </div>
        </div>
        <div class="layui-inline" >
            <label class="layui-form-label" style="height: 0px;padding-bottom: 1px"></label>
            <div id="divFileProgressContainer" class="txwrp" style="width: 1000px">
                <span id="infoSpan" border="0" width="530" style="line-height:0px;display: inline;1px #7FAAFF; padding: 2px;margin-top:8px;"></span>
            </div>
        </div>
    </div>
    <input id="plcy000" type="hidden" name="plcy000" value="${shjtPolicy.plcy000}">
    <input id="plcy003" type="hidden" name="plcy003" value="${shjtPolicy.plcy003}">
    <input id="plcy007" type="hidden" name="plcy007" value="${shjtPolicy.plcy007}">
    <input id="uploadFileStr" type="hidden" name="uploadFileStr">
    <input id="deleteFileStr" type="hidden" name="deleteFileStr">
    <div id="editor" type="text/plain" style="width:100%;height:550px"></div>
    </form>
<script>
    var ue = UE.getEditor('editor',{
        UEDITOR_HOME_URL: parent.EnvBase.base + 'resources/admin/js/ueditor/'
    });
    ue.ready(function () {
        UE.getEditor('editor').setContent($('#plcy003').val(),false);
    });
    $(document).ready(function () {
        initFileAttrs("${shjtPolicy.plcy007}");
        var config = {
            // swf文件路径
            swf: '<%=basePath%>resources/admin/js/webuploader-0.1.5/Uploader.swf',
            server: '<%=basePath%>/upload',
            // 选完文件后，是否自动上传。
            auto: true,
            // 文件接收服务端。
//            server: 'http://webuploader.duapp.com/server/fileupload.php',
            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: {
                id: '#picker',
                label: '添加附件(最大50MB)'
            },
            // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            resize: false
        }
        initWebUploader(config);
    });

    $('#policyEditForm').submit(function (data) {
        setUploadFileStr();
        $.ajax({
            url: $('#policyEditForm').attr("action"),
            type: 'POST', //GET
            async: true,    //或false,是否异步
            data: $("#policyEditForm").serialize(),
            timeout: 5000,    //超时时间
            dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
            beforeSend: function (xhr) {
                /*console.log(xhr)
                console.log('发送前')*/
            },
            success: function (data, textStatus, jqXHR) {
                if (data.type == "success") {
                    $("#plcy000").val(data.params);
                    parent.layer.msg('保存成功');
                    parent.searchList();
                }
            },
            error: function (xhr, textStatus) {
                parent.layer.msg('保存失败');
            },
            complete: function () {
            }
        });
        return false;
    })

    layui.use('form', function(){
        var form = layui.form();
        //监听提交
        form.on('submit(policyEditForm)', function(data){
//            return false;
        });
        //自定义验证规则
       /* form.verify({
            plcy001: function(value){
                if(value==''){
                    return '请选择政策类型';
                }
            },
            plcy002: function(value){
                if(value==''){
                    return '请输入政策标题';
                }
                if(value.length > 100){
                    return '标题不能超过100个字符';
                }
            }, plcy006: function(value){
                if(value==''){
                    return '请选择状态';
                }
            }
        });*/
    });

    function checkUeditorHasText() {
      var text =   ue.getContentTxt();
        if (text==''){
            ue.focus();
            layer.msg('请输入政策内容');
            return false;
        }
        $('#plcy003').val(UE.utils.unhtml(ue.getContent()) );
        return true;
    }


    //校验表单提交
    function checkValues() {
        var $plcy001 = $("[name='plcy001']");
        var $plcy002 = $("[name='plcy002']");
        var $plcy006 = $("[name='plcy006']");
        if ($plcy002.val() == '') {
            layer.msg('请输入政策标题');
            $plcy002.focus();
            return false;
        }
        else if ($plcy002.val().length > 100) {
            layer.msg('标题不能超过100个字符');
            $plcy002.focus();
            return false;
        }
        else if ($plcy001.val() == '') {
            layer.msg('请选择政策类型');
            $plcy002.focus();
            return false;
        } else if ($plcy006.val() == '') {
            layer.msg('请选择状态');
            $plcy006.focus();
            return false;
        }
        else if (!checkUeditorHasText()) {
            return false;
        }
        return true;
    }


     </script>
</body>