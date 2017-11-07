//本common.js 文件在服务器启动时会被替换. 如果要修改common.js 中内容 请在 main/resources/template/js/common.ftl

var EnvBase = {
	base: "/",
	locale: "zh_CN"
};

/*
jQuery.validator.addMethod("isIdCardNo", function(value, element) {
	return this.optional(element) || isIdCardNo(value);
}, "请输入合法的身份证号码！");
*/

jQuery.validator.addMethod("postCode", function(value, element) {
	var tel = /^[1-9][0-9]{5}$/;
	return this.optional(element) || (tel.test(value));
}, "请输入合法的邮政编码！");

//匹配中文 数字 字母 下划线
jQuery.validator.addMethod("specialCode", function(value, element) {
	var isCode = true;
	if(value != ''){
		var pattern = /^[\w\u4e00-\u9fa5]+$/gi;
		isCode = pattern.test(value);
	}
	return isCode;
}, "*不能包含特殊字符!");
//密码 1.8位以上字符 2.至少包含大小写字母、数字和特殊字符两个以上组合
jQuery.validator.addMethod("passwordCode", function(value, element) {
	var isCode = true;
	if(value != ''){
		var pattern = /^[\w\u4e00-\u9fa5]+$/gi;
		isCode = pattern.test(value);
		var pattern = /^(?!^[0-9]+$)(?!^[a-zA-Z]+$)(?!^[`~!@#$%^&*()+=|{}\\\:;',[].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$).{8,}$/;
		isCode = pattern.test(value);
	}
	return isCode;
}, "*密码需满足： 1、8位以上字符；2、至少包含大小写字母、数字和特殊字符两个以上组合!");
$.validator.addMethod("resumeSupply",function(value,element,params){
	var resume=value;
	if(resume.length==0){
		return true;
	}
	if(resume.length<7){
		return false;
	}
	
	var tempRes=resume.substring(0,7);
	var date1=tempRes.replace(".","/");
	if(!date1.match(/^\d{4}\/\d{1,2}$/)){
		return false;
	} 
	var d1 = new Date(date1+"/01");
	var month=d1.getMonth() + 1;
	month=month<10?"0"+month:month;
	if([d1.getFullYear(), month].join("/") != date1){
		return false;
	}
	
	resume=resume.substring(7);
	if(resume.indexOf('-')!=0){
		//时间点格式
		return true;
	}
	//含有- 时间段格式
	if(resume.length<8){
		return false;
	}
	
	resume=resume.substring(1,8);
	
	var date2=resume.replace(".","/");
	if(!date2.match(/^\d{4}\/\d{1,2}$/)){
		return false;
	} 
	var d2 = new Date(date2+"/01");
	month=d2.getMonth() + 1;
	month=month<10?"0"+month:month;
	if([d2.getFullYear(), month].join("/") != date2){
		return false;
	}
	if(d1>d2){
		return false;
	}
	return true;
},"履历补充(非正式学历)的时间格式(如2010.02-2010.09或2010.02)不正确！");
//月份和日的组合校验
jQuery.validator.addMethod("monthAndDay", function(value, element) {
	var isMonthAndDay = true;
	if(value == ''){
		return true;
	}
	if(value != '' && (new RegExp("^[0-9]{2}-[0-9]{2}$").test(value))){
		var mAd = '';
		var m = value.substr(0, 2);//月份
		var d = value.substr(3, 2);//日期
		
		var mon = parseInt(m);
		if(mon < 0 || mon > 12){
			isMonthAndDay = false;
		}else if(mon < 10 && mon > 0){
			mAd += "0"+mon;
		}else{
			mAd += mon;
		}
		mAd += value.substr(2, 1);
		var day = parseInt(d);
		if(day < 31 && day > 0){
			if(day < 10)
				mAd += "0"+day;
			else{
				mAd += day;
			}
		}else{
			isMonthAndDay = false;
		}
	}else{
		isMonthAndDay = false;
	}
	return isMonthAndDay;
}, "出生日由月份和日组成如:01-01");

// 验证消息
if ($.validator != null) {

	$.extend($.validator.messages, {
		required : "该项为必填项！",
		remote : "请修正该字段！",
		email : "请输入一个有效的邮箱！",
		url : "请输入合法的网址！",
		date : "请输入合法的日期！",
		dateISO : "请输入合法的日期 (ISO)！",
		number : "请输入合法的数字！",
		creditcard : "请输入合法的信用卡号！",
		mobile : "请输入有效的手机号码！",
		spachar : "不能含有特殊字符！",
		equalTo : "请再次输入相同的值！",
		maxlength : $.validator.format("请输入一个长度最多是{0}的字符串！"),
		minlength : $.validator.format("请输入一个长度最少是{0}的字符串！"),
		rangelength : $.validator.format("请输入 一个长度介于{0}和{1}之间的字符串！"),
		range : $.validator.format("请输入一个介于{0}和{1}之间的值！"),
		max : $.validator.format("请输入一个最大为{0}的值！"),
		min : $.validator.format("请输入一个最小为{0}的值！"),
		digits : "只能输入正整数"

	});

	$.validator.setDefaults({
		highlight : function(element) {
			$(element).addClass("error");
		},
		unhighlight : function(element) {
			var elementsOpentips = $(element).data("opentips");
			for ( var item in elementsOpentips) {
				try {
					elementsOpentips[item].deactivate();
				} catch(e) {
					//console.log(e);
				}
			}
			$(element).removeClass("error");
		},
		errorPlacement : function(error, element) {
			var msgbox = $(element);
			new Opentip(msgbox, $(error).text(), {
				style : "alert",
				target : true,
				tipJoint : "left",
				targetJoint : "right",
				containInViewport : true
			});
		}
	});
}

//增加身份证验证
function isIdCardNo(num) {
	var factorArr = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1);
	var parityBit = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
	var varArray = new Array();
	var intValue;
	var lngProduct = 0;
	var intCheckDigit;
	var intStrLen = num.length;
	var idNumber = num;
	// initialize
	if ((intStrLen != 15) && (intStrLen != 18)) {
		return false;
	}
	// check and set value
	for (i = 0; i < intStrLen; i++) {
		varArray[i] = idNumber.charAt(i);
		if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
			return false;
		} else if (i < 17) {
			varArray[i] = varArray[i] * factorArr[i];
		}
	}
	if (intStrLen == 18) {
		// 长度为18位
		var date8 = idNumber.substring(6, 14);
		if (isDate8(date8) == false) {
			return false;
		}
		// calculate the sum of the products
		for (i = 0; i < 17; i++) {
			lngProduct = lngProduct + varArray[i];
		}
		// calculate the check digit
		intCheckDigit = parityBit[lngProduct % 11];
		// check last digit
		if (varArray[17] != intCheckDigit) {
			return false;
		}
	} else { // 长度为15位
		// check date
		var date6 = idNumber.substring(6, 12);
		if (isDate6(date6) == false) {
			return false;
		}
	}
	return true;
}
function isDate6(sDate) {
	if (!/^[0-9]{6}$/.test(sDate)) {
		return false;
	}
	var year, month, day;
	year = sDate.substring(0, 4);
	month = sDate.substring(4, 6);
	if (year < 1700 || year > 2500)
		return false
	if (month < 1 || month > 12)
		return false
	return true
}

function isDate8(sDate) {
	if (!/^[0-9]{8}$/.test(sDate)) {
		return false;
	}
	var year, month, day;
	year = sDate.substring(0, 4);
	month = sDate.substring(4, 6);
	day = sDate.substring(6, 8);
	var iaMonthDays = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ]
	if (year < 1700 || year > 2500)
		return false
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
		iaMonthDays[1] = 29;
	if (month < 1 || month > 12)
		return false
	if (day < 1 || day > iaMonthDays[month - 1])
		return false
	return true
}


/**
 * 带遮罩层的加载面板
 * selector 选择器
 * url 请求地址
 * param 请求参数
 * */
function loadPanel(selector,url,param) {
    $(".loading-indicator").remove();//销毁加载圈
    $(".loading-indicator-overlay").remove();//销毁遮罩层
    jQuery(selector).showLoading();
    jQuery(selector).load(
        url,
        param,
        function() {
            jQuery(selector).hideLoading();
        }
    );
}


/**
 * 带遮罩层的加载面板
 * selector 选择器
 * url 请求地址
 * param 请求参数
 * */
function loadPanel2(loadSelector,showSelector,url,param) {
    $(".loading-indicator").remove();//销毁加载圈
    $(".loading-indicator-overlay").remove();//销毁遮罩层
    jQuery(showSelector).showLoading();
    jQuery(loadSelector).load(
        url,
        param,
        function() {
            jQuery(showSelector).hideLoading();
        }
    );
}