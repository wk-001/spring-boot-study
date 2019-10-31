
function kickout(){
    var href=location.href;
    if(href.indexOf("kickout")>0){
        alert("您的账号在另一台设备上登录,如非本人操作，请立即修改密码！");
    }
}
window.οnlοad=kickout();

function toRegistered() {
    location.href="toRegister";
}

layui.use(['jquery', 'element', 'form', 'layer'], function () {
        var $ = layui.jquery
        , element = layui.element
        , form = layui.form
        , layer = layui.layer
    $(function () {

    });

    form.on('submit(loginBtn)', function(data) {

        var username = data.field.username;
        var password = data.field.password;
        var rememberMe = data.field.rememberMe;

        $.ajax({
            type: "post",
            url: "login",
            data: {"userName": username,"password": password,"rememberMe": rememberMe},
            dataType: "json",
            success: function (data) {
                if (data.msg==""||data.msg==null) {
                    location.href = data.path;
                } else {
                    alert(data.msg);
                }
            }
        });
        return false;
    });
});

/* 纯ajax登录
function login() {
    var username = $("#username").val();
    var password = $("#password").val();
    var rememberMe = $("input[name='rememberMe']").is(':checked');
    if(username==""){
        alert("请输入用户名");
        return ;
    }
    if(password==""){
        alert("请输入密码");
        return ;
    }
    $.ajax({
        type: "post",
        url: "login",
        data: {"userName": username,"password": password,"rememberMe": rememberMe},
        dataType: "json",
        success: function (data) {
            if (data.msg==""||data.msg==null) {
                location.href = data.path;
            } else {
                alert(data.msg);
            }
        }
    });
}*/
