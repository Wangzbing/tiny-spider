$(".logout-link").click(function (){
    $.get("/logout",function(result){
    });
    $.removeCookie("token");
    sessionStorage.removeItem("auth");
    getPageM();
})

$(function (){
    getPageM();
})

function getPageM(){
    if (sessionStorage.getItem("auth")&&$.cookie("token")){
        $(".isLogout").removeClass("hide");
        $(".isLogin").addClass("hide");
    }else {
        $(".isLogin").removeClass("hide");
        $(".isLogout").addClass("hide");
    }
}