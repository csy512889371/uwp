/*index*/
$(document).ready(function(){
	var page=0;
	var len_p = $(".adm_body .adm_child").length;
	var p_zong;
	for(var n=0;n<len_p;n++){$(".foot_ico").append("<a></a>")}
	win_res();
	page_res();
	$(window).resize(function() {
		win_res();
		// page_res();
	});
	function win_res(){
		var win_h = $(window).height();
		var win_w = $(window).width();
		// if($(window).width()<1100){
		// 	$(".tool font").hide();
		// 	$(".nav dl dt a").animate({paddingLeft:10+'px',paddingRight:10+'px'},"fast");
		// }else{
		// 	$(".tool font").show();
		// 	$(".nav dl dt a").animate({paddingLeft:19+'px',paddingRight:19+'px'},"fast");
		// }
	};


	function page_res(){
		p_zong = $(".adm_body .adm_b").width();
		$(".adm_body .adm_z").css("width",len_p*p_zong+'px');
		$(".adm_body .adm_child").css("width",p_zong+'px');

		page_go(page);
	};
	$(".adm_body .foot_ico a").mouseover(function($title){
		page = $(".adm_body .foot_ico a").index(this);
		page_go(page);
	});	
	$(".adm_body .ico_img.lef").click(function ($title) {
		page -= 1;
		if (page == -1) { page = len_p - 1; }
		page_go(page);
	});
	$(".adm_body .ico_img.rig").click(function ($title) {
		page += 1;
		if (page == len_p) { page = 0; }
		page_go(page);
	});
	// $(".adm_body").hover(function(){
	// 	if(page_time){
	// 	   clearInterval(page_time);
	// 	}
	// },function(){
	// 	page_time = setInterval(function(){
	// 	  // page_go(page);
	// 	  page++;
	// 	  if(page==len_p){page=0;}
	// 	} , 5000);
	// });
	// var page_time = setInterval(function(){
	//    // page_go(page);
	//    page++;
	//    if(page==len_p){page=0;}
	// } , 5000);
	function page_go(page){
        var $title=$("#header-title-text");
		switch (page){
			case 0:$title.html("&nbsp-&nbsp信息管理平台");break;
			case 1:$title.html("&nbsp-&nbsp后台管理系统");break;
			default:$title.html("&nbsp-&nbsp信息管理平台");
		}
		$(".adm_body .foot_ico a").eq(page).addClass("hover").siblings("a").removeClass("hover");
		$(".adm_body .adm_z").animate({left:0-page*p_zong+"px"},"fast");
	};	
	
	/*-------------------------消息滚动通知------------------------*/
	var $messageUl=$(".message-tip ");
    var $messageLi=$(".message-tip li");
	var $messageSize=$messageLi.size();
	if($messageSize>1){
		setInterval(function () {
            $messageUl.animate({top:'-=40'},500,function () {
            	if($messageUl.css("top") == -40+'px'){
                    $(".message-tip li").eq(0).appendTo($messageUl);
                    $messageUl.css("top",0);
				}
            });
        },5000)
	}


	
	$(".tool dl").hover(function(){
		$(this).addClass('hover');
		$(this).find("dd").addClass('box_sha');
     },function(){
        $(this).removeClass('hover');
    });
	$(".logo").hover(function(){
        $(this).toggleClass('hover');
     },function(){
    	 $(this).toggleClass('hover');
     });
	$(".nav dl").hover(function(){
		$(this).find("dt").addClass('hover');
		$(this).find("dd").addClass('box_sha');
        $(this).find("dd").stop(true,false).show('fast');
    },function(){
		$(this).find("dt").removeClass('hover');
        $(this).find("dd").stop(true,false).hide('fast');
    });
});

