/*
* 
* Credits to http://css-tricks.com/long-dropdowns-solution/
*
*/

var maxHeight = 400;

$(function(){

	showSubMenu();
    
});

function showSubMenu(){
	$(".dropdown > li").hover(function() {
    	
        var $container = $(this),
            $list = $container.find("ul"),
            $anchor = $container.find("a"),
            height = $list.height() * 1.1,       // make sure there is enough room at the bottom
            multiplier = height / maxHeight;     // needs to move faster if list is taller
       
       // need to save height here so it can revert on mouseout            
       $container.data("origHeight", $container.height());
       
       // so it can retain it's rollover color all the while the dropdown is open
       $anchor.addClass("hover");
       
       // make sure dropdown appears directly below parent list item    
       $list
           .show()
           .css({
               paddingTop: $container.data("origHeight")
           });
       
       // don't do any animation if list shorter than max
       if (multiplier > 1) {
           $container
               .css({
                   height: maxHeight,
                   overflow: "hidden"
               })
               .mousemove(function(e) {
                   var offset = $container.offset();
                   var relativeY = ((e.pageY - offset.top) * multiplier) - ($container.data("origHeight") * multiplier);
                   if (relativeY > $container.data("origHeight")) {
                       $list.css("top", -relativeY + $container.data("origHeight"));
                   };
               });
       }
       
   }, function() {
   
       var $el = $(this);
       
       // put things back to normal
       $el
           .height($(this).data("origHeight"))
           .find("ul")
           .css({ top: 0 })
           .hide()
           .end()
           .find("a")
           .removeClass("hover");
   
   }); 
	
}


/**
 * 设置未来(全局)的AJAX请求默认选项
 * 主要设置了AJAX请求遇到Session过期的情况
 * 如果ajax重写了complete方法，那么这里的设置将失效
 */
$.ajaxSetup({
    type: 'POST',
    complete: function(xhr,status) {
        var sessionStatus = xhr.getResponseHeader('loginStatus');
        if(sessionStatus == 'accessDenied') {
            //var top = getTopWinow();
        	window.top.location.href = '/';
            /*
            var yes = confirm('由于您长时间没有操作, session已过期, 请重新登录.');
            if (yes) {
                top.location.href = '/';            
            }
            */
        }
    }
});

/**
 * 在页面中任何嵌套层次的窗口中获取顶层窗口
 * @return 当前页面的顶层窗口对象
 */
/*
function getTopWinow(){
    var p = window;
    while(p != p.parent){
        p = p.parent;
    }
    return p;
}
*/

/*
jQuery(function($){
    // 备份jquery的ajax方法  
    var _ajax=$.ajax;
    // 重写ajax方法，先判断登录在执行success函数 
    $.ajax=function(opt){
    	var _success = opt && opt.success || function(a, b){};
        var _opt = $.extend(opt, {
        	success:function(data, textStatus){
        		// 如果后台将请求重定向到了登录页，则data里面存放的就是登录页的源码，这里需要找到data是登录页的证据(标记)
        		if(data.indexOf('weinianjie') != -1) {
        			window.location.href= Globals.ctx + "/login.action";
        			return;
        		}
        		_success(data, textStatus);  
            }  
        });
        _ajax(_opt);
    };
});
*/