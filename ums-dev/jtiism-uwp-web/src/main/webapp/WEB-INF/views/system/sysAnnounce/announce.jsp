<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
<%--
  Created by IntelliJ IDEA.
  User: lsf
  Date: 2017/10/24
  Time: 15:01
  To change this template use File | Settings | File Templates.
--%>

<html>
<head>
    <title>系统公告管理</title>
    <script>
        var sanSelectedId = null;
        // 表头
        var sanCols = [
            { title:'saninf000', name:'saninf000' ,width:300, align:'left', sortable: false, hidden: true},
            { title:'公告内容', name:'saninf001' ,width:300, align:'left', sortable: false, type: 'text'},
            { title:'状态', name:'saninf002' ,width:100, align:'center', sortable: false, type: 'text',
                renderer: function (val, item) {
                return (item.saninf002==1?"启用":"未启用");
            }},
            { title:'创建时间', name:'saninf003', width:100, align:'center',sortable: false, type: 'text',
                renderer: function (val, item) {
                var strdate = item.saninf003.replace(/\-/g,"\/");
                var date = new Date(strdate);
                return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
            }}
        ];
        var sanList;
        $(document).ready(function(){
            sanList = $('#sanList').mmGrid({
                height: commonTalbeHeight-20
                , cols: sanCols
                , url: '<%=basePath%>sys/announce/loadSanList.do'
                , method: 'get'
                , remoteSort:true
                , sortName: 'SANINF003'
                , sortStatus: 'desc'
                , root: 'result'
                , multiSelect: false
                , checkCol: true
                , fullWidthRows: true
                , autoLoad: false
                , showBackboard : false
                , plugins: [
                    $('#sanpag').mmPaginator({})
                ]
                , params: function(){
                    return {
                        secucode: $('#secucode').val()
                    };
                }
            });


            sanList.on('cellSelected', function(e, item, rowIndex, colIndex){
                sanSelectedId = item.saninf000;
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
            sanList.load({pageNumber:page});
        }

        /**
         * 跳转到公告新增页面
         */
        function sanAddPage(){
            openWindow('新增公告','sys/announce/sanAddPage.do',1,410,255);
        }

        /**
         * 跳转到公告更新页面
         */
        function updateSanPage(){
            var item = checkSelected();
            if(item==null){
                openAlert('请选择一条记录！');
                return;
            }
            openWindow('修改公告','sys/announce/sanUpdatePage.do?sanId='+item.saninf000,1,410,255);
        }

        /**
         * 删除公告
         */
        function deleteSan(){
            var item = checkSelected();
            if(item==null){
                openAlert('请选择一条记录！');
                return;
            }
            openConfirm('你确定要删除该行记录吗?',function(){
                $.ajax({
                    type :'post',
                    url  :'<%=basePath%>sys/announce/deleteSan.do',
                    data :{sanId : item.saninf000},
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
            var rows = sanList.selectedRows();
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
            <button class="btn-style4" onclick="sanAddPage()"><i class="icon-plus"></i> 新增</button>
        </auth:auth>
        <auth:auth ifAllGranted="dept:edit">
            <button class="btn-style4" onclick="updateSanPage()"><i class="icon-pencil"></i> 编辑</button>
        </auth:auth>
        <auth:auth ifAllGranted="dept:delete">
            <button class="btn-style4" onclick="deleteSan()"><i class="icon-trash"></i> 删除</button>
        </auth:auth>
    </div>
    <div>
        <table id="sanList" class="mmg">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="sanpag" style="text-align: right;"></div>
    </div>
</body>
</html>
