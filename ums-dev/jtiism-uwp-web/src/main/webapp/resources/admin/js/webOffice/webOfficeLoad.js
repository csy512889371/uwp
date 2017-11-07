var s = "";
if (navigator.userAgent.indexOf("Chrome") > 0) {
	// 360急速模式，谷歌浏览器，搜狗浏览器高速模式，QQ浏览器高速模式，Opera欧朋浏览器
	s = "<object id='WebOffice1' type='application/x-itst-activex' align='baseline' border='0'"
			+ " codebase='WebOffice.cab#Version=7,0,1,0'"
			+ " style='LEFT: 0px; WIDTH: 100%; TOP: 0px; HEIGHT: 100%; min-height: 450px;'"
			+ " clsid='{E77E049B-23FC-4DB8-B756-60529A35FAD5}'"
			+ " event_NotifyCtrlReady='WebOffice1_NotifyCtrlReady'>"
			+ "</object>";
} else if (navigator.userAgent.indexOf("Firefox") > 0) {
	// 火狐浏览器
	s = "<object>"
			+ "<embed id='WebOffice1' clsid='{E77E049B-23FC-4DB8-B756-60529A35FAD5}'"
			+ " codebase='WebOffice.cab#Version=7,0,1,0'"
			+ " event_NotifyCtrlReady='WebOffice1_NotifyCtrlReady'"
			+ " style='left: 0px; width: 100%; top: 0px; height: 100%; min-height: 450px;'"
			+ " type='application/x-itst-activex'></embed></object>";
} else {
	// 360ie兼容模式，搜狗浏览器ie兼容模式，ie浏览器，QQ浏览器ie兼容模式
	s = "<object id=WebOffice1 classid='clsid:E77E049B-23FC-4DB8-B756-60529A35FAD5'"
			+ " codebase='WebOffice.cab#Version=7,0,1,0'"
			+ " style='left: 0px; width: 100%; top: 0px; height: 100%; min-height: 450px;'>"
			+ "</OBJECT>";
}
$("#webOfficeDiv").html(s);
//document.getElementById("webOfficeDiv").write(s);

/**
 * 1、Trident内核：IE最先开发或使用的，也叫IE内核
 * 2、Webkit内核：chrome谷歌浏览器最先开发或使用，也叫谷歌内核
 * 3、Gecko内核： FireFox火狐浏览器采用了该内核，也叫火狐内核
 * 4、Presto内核：目前只有Opera浏览器采用该内核
 */
