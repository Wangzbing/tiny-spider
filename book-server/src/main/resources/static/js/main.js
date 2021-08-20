$(".icon.catalog-icon").click(function () {
    let cas = $(".catalog-container");
    if (cas.hasClass("catalog-open-left")) {
        cas.removeClass("catalog-open-left")
    } else {
        cas.addClass("catalog-open-left")
    }
})



let $color = $(".color-list");
$color.delegate('li', 'click', function() {
    let j = $(this).index();
    let css={
        "background-color": r[j].bgColor,
        "color":r[j].textColor
    };
    let contentCss={
        "background-color": r[j].tocBgColor,
    };
    $(".book-catalog").css(css);
    $(".epub-view").css(contentCss);
    $(".catalog-open-right").css(css);
    $("section p").css("color",r[j].textColor)
    $("section h1").css("color",r[j].textColor)
});

let allContents = $(".content-parent");
allContents.delegate('span', 'click', function() {
    let j = $(this).attr("id");
    console.log(j);
    $.get("/content?id="+j,function(result){
        $(".realContent").html(result);
    });
});

const r = [{
        bgColor: "rgb(238,238,244)",
        tocBgColor: "rgb(248,248,250)",
        textColor: "rgb(51,51,51)",
        borderColor: "rgba(51, 51, 51, .1)"
    },
     {
        bgColor: "rgba(244,236,216,0.56)",
        tocBgColor: "rgb(246,239,225)",
        textColor: "rgb(91,70,54)",
        borderColor: "rgba(91, 70, 54, .1)"
    },  {
        bgColor: "#ceeaba99",
        tocBgColor: "#D6EDC6",
        textColor: "#333",
        borderColor: "rgba(51, 51, 51, .1)"
    }, {
        bgColor: "#353739f5",
        tocBgColor: "#434649",
        textColor: "#9d9fa3",
        borderColor: "rgba(157, 159, 163, .1)"
    },  {
        bgColor: "#242424f5",
        tocBgColor: "#363636",
        textColor: "#b5aca2",
        borderColor: "rgba(181, 172, 162, .1)"
    }]
