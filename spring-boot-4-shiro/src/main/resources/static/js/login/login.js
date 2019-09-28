$(function () {

})

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
}