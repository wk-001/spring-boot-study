$(function () {

})

function login() {
    var username = $("#username").val();
    var password = $("#password").val();
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
        url: ctx+"login",
        data: {"userName": username,"password": password},
        dataType: "json",
        success: function (data) {
            if (data.msg==""||data.msg==null) {
                location.href = '/index';
            } else {
                alert(data.msg);
            }
        }
    });
}