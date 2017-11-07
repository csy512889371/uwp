<%--
  Created by IntelliJ IDEA.
  User: lsf
  Date: 2017/10/26
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<html>
<head>
    <title>联系我们</title>
    <script>
        var ctaSelectedId = null;
        // 表头
        var ctaCols = [
            { title:'ctainf000', name:'ctainf000' ,width:300, align:'left', sortable: false, hidden: true},
            { title:'反馈邮箱', name:'ctainf001' ,width:150, align:'left', sortable: false, type: 'text'},
            { title:'联系电话', name:'ctainf002' ,width:100, align:'left', sortable: false, type: 'text'},
            { title:'联系人', name:'ctainf003' ,width:50, align:'left', sortable: false, type: 'text'},
            { title:'QQ', name:'ctainf004' ,width:100, align:'left', sortable: false, type: 'text'},
            { title:'微信', name:'ctainf005' ,width:100, align:'left', sortable: false, type: 'text'},
            { title:'地址', name:'ctainf006' ,width:300, align:'left', sortable: false, type: 'text'},
            /*{ title:'创建时间', name:'ctainf008', width:100, align:'center',sortable: false, type: 'text',
                renderer: function (val, item) {
                    var strdate = item.ctainf008.replace(/\-/g,"\/");
                    var date = new Date(strdate);
                    return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                }},*/
            { title:'状态', name:'ctainf007' ,width:100, align:'center', sortable: false, type: 'text',
                renderer: function (val, item) {
                    return (item.ctainf007==1?"启用":"未启用");
                }}

        ];
        var ctaList;
        $(document).ready(function(){
            ctaList = $('#ctaList').mmGrid({
                height: commonTalbeHeight-20
                , cols: ctaCols
                , url: '<%=basePath%>ent/contact/loadCtaList.do'
                , method: 'get'
                , remoteSort:true
                , sortName: 'ctainf008'
                , sortStatus: 'desc'
                , root: 'result'
                , multiSelect: false
                , checkCol: true
                , fullWidthRows: true
                , autoLoad: false
                , showBackboard : false
                , plugins: [
                    $('#ctapag').mmPaginator({})
                ]
                , params: function(){
                    return {
                        secucode: $('#secucode').val()
                    };
                }
            });


            ctaList.on('cellSelected', function(e, item, rowIndex, colIndex){
                ctaSelectedId = item.ctainf000;
                //查看
                if($(e.target).is('.btn-info, .btnPrice')){
                    e.stopPropagation();  //阻止事件冒泡
                    alert(JSON.stringify(item));
                }
            }).load({page: 1});
        });

        function searchList(page){
            if(!page){
                page=1;
            }
            ctaList.load({pageNumber:page});
        }

        /**
         * 跳转到联系我们新增页面
         */
        function ctaAddPage(){
            openWindow('新增','ent/contact/ctaAddPage.do',1,700,865);
        }

        /**
         * 跳转到联系我们信息更新页面
         */
        function updateCtaPage(){
            var item = checkSelected();
            if(item==null){
                openAlert('请选择一条记录！');
                return;
            }
            openWindow('修改','ent/contact/ctaUpdatePage.do?ctaId='+item.ctainf000,1,700,865);
        }

        /**
         * 删除联系我们信息
         */
        function deleteCta(){
            var item = checkSelected();
            if(item==null){
                openAlert('请选择一条记录！');
                return;
            }
            openConfirm('你确定要删除该行记录吗?',function(){
                $.ajax({
                    type :'post',
                    url  :'<%=basePath%>ent/contact/deleteCta.do',
                    data :{ctaId : item.ctainf000},
                    success: function(data){
                        openAlert(data.content);
                        if(data.type == 'success'){
                            /*layui.use('layer', function () {
                                layer = layui.layer;
                                layer.msg("删除成功！")
                            });*/
                            // 重新加载数据
                            searchList();
                        }
                    }
                });
            });
        }
        /**
         * 检查是否选中记录
         */
        function checkSelected(){
            var rows = ctaList.selectedRows();
            var item = rows[0];
            if(typeof(item)=='undefined'){
                return null;
            }
            return item;
        }
    </script>
</head>
<body>
    <div class="oper_menu">
        <auth:auth ifAllGranted="dept:add">
            <button class="btn-style4" onclick="ctaAddPage()"><i class="icon-plus"></i> 新增</button>
        </auth:auth>
        <auth:auth ifAllGranted="dept:edit">
            <button class="btn-style4" onclick="updateCtaPage()"><i class="icon-pencil"></i> 编辑</button>
        </auth:auth>
        <auth:auth ifAllGranted="dept:delete">
            <button class="btn-style4" onclick="deleteCta()"><i class="icon-trash"></i> 删除</button>
        </auth:auth>
    </div>
    <div>
        <table id="ctaList" class="mmg">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="ctapag" style="text-align: right;"></div>
    </div>
</body>
</html>
