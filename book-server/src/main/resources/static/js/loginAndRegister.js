/**
 * Created by zhouwei on 2016/12/27.
 */
/**login start**/
(function () {
    //如果已经登录了，则直接跳转到首页
    var loginToken = $.cookie('token');
    if (loginToken) {
        let item = sessionStorage.getItem("auth");
        console.log(item);
        if (item){
            window.location.href = '/';
            return ;
        }
    }

    var emailTest = /^\w+([-_+.]\w+)*@\w+([-_.]\w+)*\.\w+([-_.]\w+)*$/
    ,   passTest = /^[A-Za-z0-9~\!\@\#\$\%\^\&\*\(\)\-\=\+\_\`\.\。\,\，]{6,20}$/
    ,   username = $('#account_id')
    ,   password = $('#account_password')
    ,   loginBtn = $('#is-login')
    ,   usernameState = false
    ,   passwordState = false
    ;

    //检测邮箱
    username.on('focus', function () {
        focusTest($(this));
    });

    username.on('blur', function () {
        usernameState = blurTest($(this), emailTest, '账号不能为空！', '输入的邮箱有误,请核对后输入');
    });

    //检测密码
    password.on('focus', function () {
        focusTest($(this));
    });

    password.on('blur', function () {
        passwordState = blurTest($(this), passTest, '密码不能为空！', '格式有误,请使用数字或字母');
    });

    loginBtn.on('click', function (ev) {
        ev.preventDefault();
        ev.stopPropagation();
        if (usernameState && passwordState) {
            var _data = {
                username: username.val(),
                password: password.val()
            };
            $.post('/login', _data, function (data) {
                $.removeCookie('token');
                var code = data.code;
                if (code===200){
                    $.cookie('token', data.data, {expires: 365 * 100, path: '/'});
                    sessionStorage.setItem("auth",data.data);
                    window.location.href = '/';
                }
            }, 'json');
        } else {
            usernameState = blurTest(username, emailTest, '账号不能为空！', '输入的邮箱有误,请核对后输入');
            passwordState = blurTest(password, passTest, '密码不能为空！', '格式有误,请使用数字或字母');
        }
    });

})();
/**login end**/

/** public method **/
//失去焦点时检测
function blurTest(obj, regTest, str1, str2) {
    if (obj.val() === null || obj.val() === '') {
        obj.parents('.form-group').addClass('has-error');
        obj.parent().next('.help-block').text(str1);
        return false;
    }

    if (!regTest.test(obj.val())) {
        obj.parents('.form-group').addClass('has-error');
        obj.parent().next('.help-block').text(str2);

        return false;
    }

    obj.parents('.form-group').addClass('has-success');

    return true;
}

//获取焦点时检测
function focusTest(obj) {
    obj.parents('.form-group').removeClass('has-error').removeClass('has-success');
    obj.parent().next('.help-block').text('');
}

//ajax请求验证
function ajaxTest(obj, str) {
    obj.parents('.form-group').addClass('has-error').removeClass('has-success');
    obj.parent().next('.help-block').text(str);
}

//验证两次输入密码
function passwordAgainTest (obj, passwordTest, $newPassword) {
    if (obj.val() === null || obj.val() === '') {
        obj.parents('.form-group').addClass('has-error');
        obj.parent().next('.help-block').text('密码不能为空');

        return false;
    }

    if (!passwordTest.test(obj.val())) {
        obj.parents('.form-group').addClass('has-error');
        obj.parent().next('.help-block').text('格式有误,请使用数字或字母');

        return false;
    }

    if ($newPassword.val() !== obj.val()) {
        obj.parents('.form-group').addClass('has-error');
        obj.parent().next('.help-block').text('两次输入的密码不同，请核对后输入');

        return false;
    }

    obj.parents('.form-group').addClass('has-success');

    return true;
}
/****/

/** reset password start **/
(function () {
    //重置密码第一部分
    var $sendEmail = $('#send-email')
    ,   $sendBtn = $('#send-btn')
    ,   emailTest = /^\w+([-_+.]\w+)*@\w+([-_.]\w+)*\.\w+([-_.]\w+)*$/
    ,   emailState = false
    ,   timer = null
    ;

    //重置密码第二部分选择器
    var $sendCode = $('#send-code')
    ,   $newPassword = $('#new-password')
    ,   $againPassword = $('#again-password')
    ,   $resetBtn = $('#reset-password-btn')
    ,   $againBtn = $('#again-send-btn')
    ,   codeTest = /^\w{4}$/
    ,   passwordTest = /^[A-Za-z0-9~\!\@\#\$\%\^\&\*\(\)\-\=\+\_\`\.\。\,\，]{6,20}$/
    ,   codeState = false
    ,   newPasswordState = false
    ,   againPasswordState = false
    ,   time = 60
    ,   bStart = true
    ;

    //发送邮件文本框失去焦点
    $sendEmail.on('focus', function () {
        focusTest($(this));
    });

    //发送邮件文本框获取焦点
    $sendEmail.on('blur', function () {
        emailState = blurTest($(this), emailTest, '邮箱不能为空', '输入的邮箱有误,请核对后输入');
    });

    //发送验证码
    $sendBtn.on('click', function () {

        if (emailState) {
            $.post('/app/api_v2/verificationCode', {userName: $sendEmail.val(), lang: 'chinese'}, function (data) {
                var code = data.code;

                if (code) {
                    $('.reset-box').eq(0).addClass('hide');
                    $('.reset-box').eq(1).removeClass('hide');
                    $.cookie('userId', data.userId);

                    bStart = false;

                    timer = setInterval(function () {
                        time--;

                        $('#again-send-btn').find('.time').text(time);

                        if (time <= 0) {
                            clearInterval(timer);
                            time = 60;
                            $('#again-send-btn').find('.time').text(time);
                            bStart = true;
                        }

                    }, 1000);
                } else {
                    ajaxTest($sendEmail, '用户不存在，请核对后输入');
                }
            }, 'json');

        }

    });

    //重置密码第二部分
    /*--------验证码获取焦点检测--------*/
    $sendCode.on('focus', function () {
        focusTest($(this));
    });

    /*--------验证码失去焦点检测--------*/
    $sendCode.on('blur', function () {
        codeState = blurTest($(this), codeTest, '验证码不能为空', '输入的验证码有误,请核对后输入');
    });

    /*--------新密码获取焦点检测--------*/
    $newPassword.on('focus', function () {
        focusTest($(this));
    });

    /*--------新密码获取焦点检测--------*/
    $newPassword.on('blur', function () {
        newPasswordState = blurTest($(this), passwordTest, '密码不能为空', '格式有误,请使用数字或字母');
    });

    /*--------再次输入密码获取焦点检测--------*/
    $againPassword.on('focus', function () {
        focusTest($(this));
    });

    /*--------再次输入密码失去焦点检测--------*/
    $againPassword.on('blur', function () {
        againPasswordState = passwordAgainTest($(this), passwordTest, $newPassword);
    });



    /*--------重置密码-------*/
    $resetBtn.on('click', function () {
        if (codeState && newPasswordState && againPasswordState) {
            var _data = {
                userId: $.cookie('userId'),
                userName: $sendEmail.val().toLowerCase(),
                checkedCode: $sendCode.val(),
                password: md5($newPassword.val()),
                passwordAgain: md5($againPassword.val())
            };

            //发送修改密码请求
            $.post('/api/resetPassword', _data, function (data) {
                console.log(data);
                if (data.code === 1) {

                    $('#reset-success').submit();

                } else {
                    ajaxTest($sendCode, '验证码不正确，请核对后输入');
                }
            }, 'json');
        }
    });

    /*-------------再次发送验证码--------------*/
    $againBtn.on('click', function () {

        if (!bStart) return;

        $.post('/app/api_v2/verificationCode', {userName: $sendEmail.val(), lang: 'chinese'}, function (data) {
            var code = data.code;

            if (code) {
                $('.reset-box').eq(0).addClass('hide');
                $('.reset-box').eq(1).removeClass('hide');
                $.cookie('userId', data.userId);

                bStart = false;

                timer = setInterval(function () {
                    time--;

                    $('#again-send-btn').find('.time').text(time);

                    if (time <= 0) {
                        clearInterval(timer);
                        time = 60;
                        $('#again-send-btn').find('.time').text(time);
                        bStart = true;
                    }

                }, 1000);
            } else {
                ajaxTest($sendEmail, '用户不存在，请核对后输入');
            }
        }, 'json');
    });

})();
/** reset password end **/

/** 注册会员 start **/
(function () {
    var $username = $('#register_email')
    ,   $password = $('#register_pass_one')
    ,   $passwordAgain = $('#register_pass-again')
    ,   $registerBtn = $('#register_btn')
    ,   emailTest = /^\w+([-_+.]\w+)*@\w+([-_.]\w+)*\.\w+([-_.]\w+)*$/
    ,   passwordTest = /^[A-Za-z0-9~\!\@\#\$\%\^\&\*\(\)\-\=\+\_\`\.\。\,\，]{6,20}$/
    ,   usernameState = false
    ,   passwordState = false
    ,   passwordAgainState = false
    ;

    /***账号输入框获取焦点操作***/
    $username.on('focus', function () {
        focusTest($(this));
    });

    /***账号输入框失去焦点操作***/
    $username.on('blur', function () {
        usernameState = blurTest($(this), emailTest, '账号不能为空', '输入的账号格式有误,请核对后输入');
    });

    /***密码输入框获取焦点操作***/
    $password.on('focus', function () {
        focusTest($(this));
    });

    /***密码输入框失去焦点操作***/
    $password.on('blur', function () {
        passwordState = blurTest($(this), passwordTest, '密码不能为空', '格式有误,请使用数字或字母');
    });

    /***再次输入密码框获取焦点操作***/
    $passwordAgain.on('focus', function () {
        focusTest($(this));
    });

    /***再次输入密码框失去焦点操作***/
    $passwordAgain.on('blur', function () {
        passwordAgainState = passwordAgainTest($(this), passwordTest, $password);
    });

    //注册
    $registerBtn.on('click', function (ev) {
        ev.preventDefault();
        ev.stopPropagation();
        var _this = $(this)
        $(this).attr('disabled', true)
        if (usernameState && passwordState && passwordAgainState) {
            //传给后台的数据
            var _data = {
                username : $username.val(),
                password : md5($password.val()),
                passwordAgain : md5($passwordAgain.val()),
                deviceId: Guid.raw(),
                deviceType: 'web-desk',
                deviceInfo: JSON.stringify({
                    userAgent: navigator.userAgent,
                    language: navigator.language,
                    vendor: navigator.vendor
                })
            };

            $.post('/app/api_v2/registerAndGetTrialMembership', _data, function (data) {
                var code = data.code;

                switch (code) {
                    case 1:
                        $.cookie('token', data.token, {expires: 365 * 100, path: '/'});
                        $.cookie('deviceId', _data.deviceId, {expires: 365 * 100, path: '/'});
                        $('#register_success').submit();
                        break;
                    case -1:
                        ajaxTest($username, '用户名已存在，请重新输入');
                        _this.removeAttr('disabled')
                        break;
                    default:
                        ajaxTest($username, '用户名已存在，服务器错误，请重试');
                        _this.removeAttr('disabled')
                        break;
                }
            }, 'json');
        } else {
            $(this).attr('disabled', '')
            usernameState = blurTest($username, emailTest, '账号不能为空', '输入的账号格式有误,请核对后输入');
            passwordState = blurTest($password, passwordTest, '密码不能为空', '格式有误,请使用数字或字母');
            passwordAgainState = passwordAgainTest($passwordAgain, passwordTest, $password);
        }
    });
})();
/** 注册会员 end **/

/**第三方登录 start**/
(function () {

    /* QQ 登录 */
    $('.qq-login-btn').on('click', function () {
        window.location.href = '/users/qq_login';
    });
    /* QQ 登录 */

    /* 微信 登录 */
    $('.wx-login-btn').on('click', function () {
        window.location.href = '/users/weixin_login';
    });
    /* 微信 登录 */

    /* google 登录 */
    $('.gg-login-btn').on('click', function () {
        window.location.href = '/users/google_login';
    });
    /* google 登录 */
})();
/**第三方登录 end**/

/**从别的页面跳来的三方登录检测**/
(function () {
    /*
     * 检测登录来源，跳转相关页面
     *
     * */

    var type = getQueryString('loginType');

    if (type === 'wx') {
        $('.wx-login-btn').find(' > a').click();
    }

})();
/**从别的页面跳来的三方登录检测**/

//获取url中的值
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

/*推荐码*/
(function () {

    var refererWrap = $('.g-dialog-referer');
    var refererShadow = $('.g-dialog-shadow');
    var refererEnter = refererWrap.find('.referer-code-enter');
    var refererCode = null;             //输入的推荐码
    var bCheck = false;                 //验证是否成功
    var toggerBtn1 = $('#togger-btn-1');    //注册neat账号按钮
    var toggerBtn2 = $('#togger-btn-2');    //注册三方账号按钮
    var loginMain = $('.login-dialog-main');

    toggerBtn1.on('click', function () {
        loginMain.addClass('slide-left');
    });

    toggerBtn2.on('click', function () {
        loginMain.removeClass('slide-left');
    });

    //推荐码按钮点击
    $('.register-click').on('click', function () {

        refererWrap.stop().fadeIn();
        refererShadow.stop().fadeIn();

    });

    //取消操作
    refererWrap.find('.referer-cancel').on('click', function () {

        refererWrap.stop().fadeOut();
        refererShadow.stop().fadeOut();

        //隐藏所有提示信息
        $('.referer-code-tips').hide();

        bCheck = false;

    });

    //确定操作
    refererWrap.find('.referer-confirm').on('click', function () {

        //如果已经验证通过推荐码
        if ($(this).hasClass('active')) {
            //隐藏所有提示信息
            $('.referer-code-tips').hide();

            refererWrap.stop().fadeOut();
            refererShadow.stop().fadeOut();

            $('.register-click').html(refererCode);

            bCheck = false;

            $.cookie('referer', refererCode, {path: '/'});
        }

    });

    //推荐码输入框操作
    refererEnter.on('keyup', function () {

        refererCode = $(this).val();

        //推荐码输入框操作
        refererEnterHandle(refererCode);

    });

    //推荐码输入框获取焦点时操作
    refererEnter.on('focus', function () {

        $('.referer-code-tips').hide();

        //如果已经验证成功
        if (bCheck) {
            $('.success-tips').show();
        }

    });

    //推荐码输入框失去焦点时操作
    refererEnter.on('blur', function () {

        refererCode = $(this).val();

        //推荐码输入框操作
        checkRefererCode(refererCode);

    });

    //推荐码输入框操作
    function refererEnterHandle (refererCode) {

        //如果推荐码有6位数
        if (refererCode.length == 6) {
            //发送请求，验证推荐码
            checkRefererCode(refererCode);
        } else {    //验证码长度不够6位时
            //验证码输入框操作
            refererEnterFocus();
        }

    }

    //验证码输入框操作
    function refererEnterFocus () {
        $('.success-tips').hide();
        $('.error-tips').show();

        $('.referer-confirm').removeClass('active');

        bCheck = false;
    }

    //验证推荐码
    function checkRefererCode(refererCode) {

        //如果已经获取到正确的推荐码，则不再发送请求
        if (!bCheck) {

            //验证用户输入的推荐码
            $.post('/api/checkRecommendCode', {refererCode: refererCode}, function (data) {

                if (data.code > 0) {
                    $('.error-tips').hide();
                    $('.success-tips').show();

                    $('.referer-confirm').addClass('active');

                    bCheck = true;
                } else {
                    $('.error-tips').show();
                }

            }, 'json');

        }

    }


})();
