$(function () {

})

function login() {
    var username = $("#username").val();
    var password = $("#password").val();
    alert(username+":"+password)
    $.ajax({
        type: "post",
        url: ctx + "login",
        data: {"username": username,"password": password},
        dataType: "json",
        success: function (data) {
            if (data.msg==""||data.msg==null) {
                location.href = ctx + 'index';
            } else {
                alert(data.msg);
            }
        }
    });
}