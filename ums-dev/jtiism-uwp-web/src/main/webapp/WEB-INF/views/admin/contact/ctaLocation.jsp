<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../common.inc" %>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=SuehmuQ2fmAifdzcrjqRexSVd0afAOcO"></script>
<body onload="asyncLoading();">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <link rel="stylesheet" href="<%=basePath%>resources/admin/js/layui/css/layui.css">
    <style type="text/css">
        body, html {
            width: 100%;
            height: 100%;
            margin: 0;
            font-family: "微软雅黑";
        }

        #allmap {
            height: 400px;
            width: 100%;
        }

        #r-result {
            width: 100%;
            font-size: 14px;
        }

        html {
            height: 100%
        }

        body {
            height: 100%;
            margin: 0px;
            padding: 0px
        }

        #container {
            height: 100%
        }

        .BMap_cpyCtrl {
            display: none;
        }

        .anchorBL {
            display: none;
        }

        .locationFont {
            font-size: 12px;
            padding: 0 5px;
            line-height: 38px;
            width: 48px;
            text-align: center;
        }
    </style>

    <title>地理位置</title>
</head>
<body>
<script src="<%=basePath%>resources/admin/js/jquery-1.11.3.min.js"></script>
<script src="<%=basePath%>resources/admin/js/layui/layui.js"></script>
<form class="layui-form" action="" id="ctaLocationForm">
    <div id="allmap"></div>
    <div class="layui-form-item">
        <div class="layui-form-item" style="margin-top:15px">
            <div id="locationMsg" class="layui-inline">
                <label class="layui-form-label locationFont">位置信息</label>
                <div class="layui-input-inline locationFont">
                    <input id="address" type="text" name="address" lay-verify="phone" autocomplete="off"
                           class="layui-input">
                </div>
            </div>
            <div id="lnglatMsg" class="layui-inline">
                <label class="layui-form-label locationFont">经纬度</label>
                <div class="layui-input-inline">
                    <input id="label_lng_lat" type="text" class="layui-input "
                           value="<c:if test="${not empty lat and not empty lng }"> ${lng},${lat}</c:if> ">
                </div>
            </div>
            <div id="searchButton" class="layui-inline">
                <input type="button" value="查询" onclick="theLocation()" class="layui-btn layui-btn-normal"/>

            </div>
            <input id="input_ctaId" type="hidden" value="${ctaId}" name="ctaId"/>
            <input id="input_lng" type="hidden" value="${lng}" name="lng"/>
            <input id="input_lat" type="hidden" value="${lat}" name="lat"/>
        </div>
    </div>
</form>
</body>
</html>
<script type="text/javascript">
    INIT_ADDRESS = null;
    LOCAL_SEARCH_RESUTS = null;
    AUTO_COMPLETE = null;
    var map;
    //设置父页面中经纬度的值
    $('#ctaInf012', window.parent.document).val(${lat});
    $('#ctaInf013', window.parent.document).val(${lng});

    //初始化百度地图
    function initMap() {
        map = new BMap.Map("allmap");
//        map.centerAndZoom(point, 15);
        map.setDefaultCursor("url('bird.cur')");   //设置地图默认的鼠标指针样式
        map.addEventListener("click", clearAndSetMarker);
        //单击获取点击的经纬度
        map.addEventListener("click", function (e) {
            var lng = e.point.lng;
            var lat = e.point.lat;
            setLocationLabel(e.point);
//            alert(e.point.lng + "," + e.point.lat);
        });
        // 添加带有定位的导航控件 start
        var navigationControl = new BMap.NavigationControl({
            // 靠左上角位置
            anchor: BMAP_ANCHOR_TOP_LEFT,
            // LARGE类型
            type: BMAP_NAVIGATION_CONTROL_LARGE,
            // 启用显示定位
            enableGeolocation: true
        });
        map.addControl(navigationControl);
        // 添加定位控件
        var geolocationControl = new BMap.GeolocationControl();
        geolocationControl.addEventListener("locationSuccess", function (e) {
            // 定位成功事件
            var address = '';
            address += e.addressComponent.province;
            address += e.addressComponent.city;
            address += e.addressComponent.district;
            address += e.addressComponent.street;
            address += e.addressComponent.streetNumber;
        });
        geolocationControl.addEventListener("locationError", function (e) {
            // 定位失败事件
            alert(e.message);
        });
        map.addControl(geolocationControl);
        // 添加带有定位的导航控件 end
//        map.enableScrollWheelZoom();   //启用滚轮放大缩小，默认禁用
        map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
        /*   if (document.createElement('canvas').getContext) {  // 判断当前浏览器是否支持绘制海量点
               //pointCollection(map, data, 'red');
               //pointCollection(map, data1, '#0075c7');
           } else {
               alert('请在chrome、safari、IE8+以上浏览器查看本示例');
           }*/
        //初始化基础信息
        var point = new BMap.Point($('#input_lat').val(),$('#input_lng').val());
        if ($('#input_lng').val() == "" || $('#input_lat').val() == "") {
            point = new BMap.Point(121.328961, 31.129356);
        }
//        setLocationLabel(point);
        zoomLocation(point, "")
        setGeoCoder(point);

        $("#address").click(function () {
            if (AUTO_COMPLETE == null) {
                AUTO_COMPLETE = new BMap.Autocomplete(    //建立一个自动完成的对象
                    {
                        "input": "address"
                        , "location": map
                    });
                AUTO_COMPLETE.addEventListener("onconfirm", function (e) {    //鼠标点击下拉列表后的事件
                    var _value = e.item.value;
                    myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
                    setPlace();
                });

                function setPlace() {
                    map.clearOverlays();    //清除地图上所有覆盖物
                    function myFun() {
                        var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
                        map.centerAndZoom(pp, 18);
                        map.addOverlay(new BMap.Marker(pp));    //添加标注
                        setLocationLabel(pp);
                    }

                    var local = new BMap.LocalSearch(map, { //智能搜索
                        onSearchComplete: myFun
                    });
                    local.search(myValue);
                }

            }
        });
        $('#address').bind('keypress', function (event) {
            if (event.keyCode == "13") {
                theLocation();
            }
        });
    }

    //异步加载地图
    function asyncLoading() {
        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = "http://api.map.baidu.com/api?v=2.0&ak=SuehmuQ2fmAifdzcrjqRexSVd0afAOcO&callback=initMap";
        document.body.appendChild(script);
    }

    //设置经纬度坐标文字
    function setLocationLabel(point) {
//        $("#label_lng_lat").html(point.lng + "," + point.lat);//经纬度
        $("#label_lng_lat").val(point.lng + "," + point.lat);//经纬度
        $("#input_lng").val(point.lng);
        $("#input_lat").val(point.lat);
        //设置父页面中经纬度的值
        $('#ctaInf012', window.parent.document).val(point.lng);
        $('#ctaInf013', window.parent.document).val(point.lat);
        $(".layui-form-danger").removeClass(".layui-form-danger");
    }


    function zoomLocation(point, text) {
        map.centerAndZoom(point, 18);
        map.addOverlay(new BMap.Marker(point));    //添加标注
        var opts = {
            width: 200,     // 信息窗口宽度
            height: 50,     // 信息窗口高度
            title: " ", // 信息窗口标题
            enableMessage: false,//设置允许信息窗发送短息
            message: " "
        }
        var infoWindow = new BMap.InfoWindow(text, opts);  // 创建信息窗口对象
//        map.openInfoWindow(infoWindow,point); //开启信息窗口
    }

    //逆向地理解析
    function setGeoCoder(point) {
        var geoc = new BMap.Geocoder();
        geoc.getLocation(point, function (rs) {
            var addComp = rs.addressComponents;
            var text = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
            text = text.replace(", ,", "");
            INIT_ADDRESS = text;
            $("#address").val(text);
        });
    }

    //清除并且重新设置地图上的标记
    function clearAndSetMarker(e) {
        AUTO_COMPLETE = null;
        var pt = e.point;
        setGeoCoder(pt);
        if (LOCAL_SEARCH_RESUTS != null) {
            for (var i = 0; i < LOCAL_SEARCH_RESUTS.getCurrentNumPois(); i++) {
                var _pt = LOCAL_SEARCH_RESUTS.getPoi(i).point;
                if (Math.abs(pt.lng.toFixed(4) - _pt.lng.toFixed(4)) < 0.0005 &&
                    Math.abs(pt.lat.toFixed(4) - _pt.lat.toFixed(4)) < 0.0005
                ) {
                    return;
                }
            }
        }
        map.clearOverlays();
        var point = new BMap.Point(pt.lng, pt.lat);
        var marker = new BMap.Marker(point);
//            marker.disableMassClear();
//            marker.enableMassClear();
        map.addOverlay(marker);
    }


    //根据输入的条件查询地理位置信息
    function theLocation() {
        $(".layui-form-danger").removeClass(".layui-form-danger");
        if (!map) {
            history.go(0);
        }
        var city = $("#address").val();
        if (city != "") {
            map.clearOverlays();
            var local = new BMap.LocalSearch(map, {
                renderOptions: {map: map},
                onSearchComplete: function (results) {
                    // 判断状态是否正确
                    if (local.getStatus() == BMAP_STATUS_SUCCESS) {
                        LOCAL_SEARCH_RESUTS = results;
                        for (var i = 0; i < results.getCurrentNumPois(); i++) {
                            var pt = results.getPoi(i).point;
                            setLocationLabel(pt);
                            break;
                        }
                        /* var s = [];
                         for (var i = 0; i < results.getCurrentNumPois(); i ++){
                             console.log(results.getPoi(i));
                             s.push(results.getPoi(i).title + ", " + results.getPoi(i).address);
                         }*/
                    }
                }
            });
            local.search(city);
        }else{
            $("#address").addClass(".layui-form-danger");
            $("#address").focus();
        }
    }


    function pointCollection(map, data, color) {
        var points = [];  // 添加海量点数据
        for (var i = 0; i < data.data.length; i++) {
            points.push(new BMap.Point(data.data[i][0], data.data[i][1]));
        }
        var options = {
            size: BMAP_POINT_SIZE_SMALL,
            shape: BMAP_POINT_SHAPE_CIRCLE,
            color: color
        }
        var pointCollection = new BMap.PointCollection(points, options);  // 初始化PointCollection
        pointCollection.addEventListener('click', function (e) {
            var Name = "";//名称
            var ads = "";//地址
            var tel = "88888-8888";//电话
            //关于自定义信息 修改json [[经度,维度,1,名称,地址,电话]]
            //循环查出值
            for (var i = 0; i < data.data.length; i++) {
                if (data.data[i][0] == e.point.lng && data.data[i][1] == e.point.lat) {//经度==点击的,维度
                    Name = data.data[i][3];
                    ads = data.data[i][4];
                    tel = data.data[i][5];
                    break;
                }
            }
            var point = new BMap.Point(e.point.lng, e.point.lat);
            var opts = {
                width: 250, // 信息窗口宽度
                height: 70, // 信息窗口高度
                title: "", // 信息窗口标题
                enableMessage: false,//设置允许信息窗发送短息
            }
            var infowindow = new BMap.InfoWindow("名称:" + Name + "<br/>地址:" + ads + "<br/>电话:" + tel + "<a target='_blank'  href='http://www.baidu.com'>详情</a>", opts);
            map.openInfoWindow(infowindow, point);
            //alert('111111单击点的坐标为：' + e.point.lng + ',' + e.point.lat);  // 监听点击事件
        });
        map.addOverlay(pointCollection);  // 添加Overlay
    }

    //删除地图上所有覆盖物
    function deleteAllPoint() {
        var allOverlay = map.getOverlays();
        for (var i = 0; i < allOverlay.length - 1; i++) {
            map.removeOverlay(allOverlay[i]);
        }
    }


    // 编写自定义函数,创建标注
    function addMarker(point, label) {
//        CURRENT_MARKER_LABEL = label;
        var marker = new BMap.Marker(point);
        map.addOverlay(marker);
        marker.setLabel(label);
    }


    //检查地理位置是否输入
    function onLocationInput() {
        var lng = '';
        var lat = '';
        var val = $("#label_lng_lat").val();
        if (val.indexOf(",") > -1) {
            lng = val.split(",")[0];
            lat = val.split(",")[1];
        }
        $("#input_lng").val(lng);
        $("#input_lat").val(lat);
    }


    //检查地理位置是否输入
    function checkLocationData() {
        $(".layui-form-danger").removeClass(".layui-form-danger");
        var lng = $("#input_lng").val();
        var lat = $("#input_lat").val();
        if (lng == '' || lat == '') {
            $("#label_lng_lat").addClass("layui-form-danger");
            $("#label_lng_lat").focus();
            return false;
        }
        parent.$('#ctaInf012').val(lng);
        parent.$('#ctaInf013').val(lat);
        return true;
    }



    function setAddressAndToLocation(address) {
        $("#address").val(address);
        theLocation();
    }


    $("#label_lng_lat").change(function () {
        onLocationInput();
    });

    $("#label_lng_lat").blur(function () {
        onLocationInput();
    });
</script>
</body>