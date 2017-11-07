(function() {    if (jQuery.browser) return;    jQuery.browser = {};    jQuery.browser.mozilla = false;    jQuery.browser.webkit = false;    jQuery.browser.opera = false;    jQuery.browser.msie = false;    var nAgt = navigator.userAgent;    jQuery.browser.name = navigator.appName;    jQuery.browser.fullVersion = '' + parseFloat(navigator.appVersion);    jQuery.browser.majorVersion = parseInt(navigator.appVersion, 10);    var nameOffset, verOffset, ix;    // In Opera, the true version is after "Opera" or after "Version"       if ((verOffset = nAgt.indexOf("Opera")) != -1) {        jQuery.browser.opera = true;        jQuery.browser.name = "Opera";        jQuery.browser.fullVersion = nAgt.substring(verOffset + 6);        if ((verOffset = nAgt.indexOf("Version")) != -1) jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);    }    // In MSIE, the true version is after "MSIE" in userAgent       else if ((verOffset = nAgt.indexOf("MSIE")) != -1) {        jQuery.browser.msie = true;        jQuery.browser.name = "Microsoft Internet Explorer";        jQuery.browser.fullVersion = nAgt.substring(verOffset + 5);    }    // In Chrome, the true version is after "Chrome"       else if ((verOffset = nAgt.indexOf("Chrome")) != -1) {        jQuery.browser.webkit = true;        jQuery.browser.name = "Chrome";        jQuery.browser.fullVersion = nAgt.substring(verOffset + 7);    }    // In Safari, the true version is after "Safari" or after "Version"       else if ((verOffset = nAgt.indexOf("Safari")) != -1) {        jQuery.browser.webkit = true;        jQuery.browser.name = "Safari";        jQuery.browser.fullVersion = nAgt.substring(verOffset + 7);        if ((verOffset = nAgt.indexOf("Version")) != -1) jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);    }    // In Firefox, the true version is after "Firefox"       else if ((verOffset = nAgt.indexOf("Firefox")) != -1) {        jQuery.browser.mozilla = true;        jQuery.browser.name = "Firefox";        jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);    }    // In most other browsers, "name/version" is at the end of userAgent       else if ((nameOffset = nAgt.lastIndexOf(' ') + 1) < (verOffset = nAgt.lastIndexOf('/'))) {        jQuery.browser.name = nAgt.substring(nameOffset, verOffset);        jQuery.browser.fullVersion = nAgt.substring(verOffset + 1);        if (jQuery.browser.name.toLowerCase() == jQuery.browser.name.toUpperCase()) {            jQuery.browser.name = navigator.appName;        }    }    // trim the fullVersion string at semicolon/space if present       if ((ix = jQuery.browser.fullVersion.indexOf(";")) != -1) jQuery.browser.fullVersion = jQuery.browser.fullVersion.substring(0, ix);    if ((ix = jQuery.browser.fullVersion.indexOf(" ")) != -1) jQuery.browser.fullVersion = jQuery.browser.fullVersion.substring(0, ix);    jQuery.browser.majorVersion = parseInt('' + jQuery.browser.fullVersion, 10);    if (isNaN(jQuery.browser.majorVersion)) {        jQuery.browser.fullVersion = '' + parseFloat(navigator.appVersion);        jQuery.browser.majorVersion = parseInt(navigator.appVersion, 10);    }    jQuery.browser.version = jQuery.browser.majorVersion;})(jQuery);function messagerClose() {	$("#pop").remove();}( function(jQuery) {	/*	 * 	 * jQuery Plugin - Messager	 * 	 * Author: corrie Mail: corrie@sina.com Homepage: www.corrie.net.cn	 * 	 * Copyright (c) 2008 corrie.net.cn	 * 	 * @license http://www.gnu.org/licenses/gpl.html [GNU General Public	 * License]	 * 	 * 	 * 	 * $Date: 2008-12-26	 * 	 * $Vesion: 1.5 @ how to use and example: Please Open index.html	 * 	 */	this.version = '@1.3';	this.layer = {'width' : 200, 'height': 100};	this.title = '信息提示';	this.time = 4000;	this.anims = {'type' : 'slide', 'speed' : 600};		this.timer1 = null;	this.closes = function() {		$("#message").hide(this.anims.speed);	}		this.inits = function(title, text, url){		if($("#message").is("div")){ return; }		$(document.body).prepend(				 '<div id="message" style="border:#fff 0px; z-index:100; width:'+this.layer.width+'px; height:'+this.layer.height+'px; position:absolute; display:none; background:#fff; bottom:0; right:0; overflow:hidden;">'				+'  <div style="background:#2898fd; border:1px solid #fff; border-bottom:none; width:100%; height:25px; font-size:16px; overflow:hidden; color:#1f336b;">'				+'    <span id="message_close" style="float:right;padding:0 0 5px 0; width:16px; line-height:auto; color:#fff; font-size:16px; font-weight:bold; text-align:center; cursor:pointer; overflow:hidden;">X</span>'				+'    <div style="padding:5px 0 5px 5px; width:100px; line-height:18px; text-align:left; overflow:hidden; color:#fff;">' + title				+'    </div>'				+'    <div style="clear:both;">'				+'    </div>'				+'  </div>'				+'  <div style="padding-bottom:5px; border:1px solid #fff; border-top:none; width:100%; height:auto; font-size:14px;">'				+'    <div id="message_content" style="margin:0 5px 0 5px; border:#b9c9ef; padding:10px 0 10px 0px; font-size:14px; width:'+(this.layer.width-17) + 'px; height:' + (this.layer.height-72) + 'px; color:#000000; text-align:left; overflow:hidden;">'				+       text				+'    </div>'				+'  </div>'				+'  <div id="footer" style="float:right; overflow:hidden; font-size:14px;"><a href="' + url + '">更多>>&nbsp;</a></div>'				+'</div>');		$("#message_close").click(function(){					setTimeout('this.closes()', 1);		});		$("#message").hover(function(){			clearTimeout(timer1);			timer1 = null;		},function(){			timer1 = setTimeout('this.closes()', time);		});	};		this.show = function(title, text, time, url) {		if($("#message").is("div")){ return; }		if(title == 0 || !title)title = this.title;		this.inits(title, text, url);		if(time >= 0)this.time = time;		switch(this.anims.type){			case 'slide':$("#message").slideDown(this.anims.speed);break;			case 'fade':$("#message").fadeIn(this.anims.speed);break;			case 'show':$("#message").show(this.anims.speed);break;			default:$("#message").slideDown(this.anims.speed);break;		}		if($.browser.is=='chrome'){			setTimeout(function(){				$("#message").remove();				this.inits(title, text, url);				$("#message").css("display","block");			},this.anims.speed-(this.anims.speed/5));		}		this.rmmessage(this.time);	};	this.lays = function(width, height){		if($("#message").is("div")){ return; }		if(width!=0 && width)this.layer.width = width;		if(height!=0 && height)this.layer.height = height;	}	this.anim = function(type,speed){		if($("#message").is("div")){ return; }		if(type!=0 && type)this.anims.type = type;		if(speed!=0 && speed){			switch(speed){				case 'slow' : ;break;				case 'fast' : this.anims.speed = 200; break;				case 'normal' : this.anims.speed = 400; break;				default:										this.anims.speed = speed;			}					}	}	this.rmmessage = function(time){		if(time > 0){			timer1 = setTimeout('this.closes()', time);		}	};	this.close = function() {		switch (this.anims.type) {			case 'slide':$("#message").slideUp(this.anims.speed);break;			case 'fade':$("#message").fadeOut(this.anims.speed);break;			case 'show':$("#message").hide(this.anims.speed);break;			default:$("#message").slideUp(this.anims.speed);break;		};		setTimeout('$("#message").remove();', this.anims.speed);		this.original();		}	this.original = function() {			this.layer = {'width' : 200, 'height': 100};		this.title = '信息提示';		this.time = 4000;		this.anims = {'type' : 'slide', 'speed' : 600};	};    jQuery.messager = this;    return jQuery;})(jQuery);