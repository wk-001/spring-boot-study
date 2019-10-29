function test() {
    alert(1)
}
/* 单独引入jQuery可以直接使用*/
/*$(function () {
    $(".layui-btn").click(function () {
        alert($(this).html());
    })
    $("#test").click(function () {
        alert($(this).html()+123);
    })
})*/
//导航依赖的element模块
layui.use(['element','jquery'], function(){
    var $ = layui.jquery;
    var element = layui.element;
    $("#add").click(function () {
        element.tabAdd('demo', {    //demo：lay-filter="demo"
            title: $(this).html()+ (Math.random()*1000|0)
            ,content: '选项卡的内容' //支持传入html
            ,id: '选项卡标题的lay-id属性值'
        });
    });
    $("#change").click(function () {
        element.tabChange('demo', '2'); //切换到 lay-id="yyy" 的这一项
    });
    $("#delete").click(function () {
        element.tabDelete('demo', '4'); //删除 lay-id="xxx" 的这一项
    });
    $("#delAll").click(function () {
       var lis = $(".mydemo");
       // $.each(要循环的集合,function (循环变量, 循环项)
        $.each(lis,function (index, item) {
            var layid = item.getAttribute("lay-id");
            element.tabDelete('demo', layid); //删除 lay-id="xxx" 的这一项
        });
    });
});

/*使用layui自带的jQuery,前提是项目中只有layui自带的jQuery，引入其他jQuery会冲突
layui.use(["jquery"],function () {
    var $ = layui.jquery;
    $(".layui-btn").click(function () {
        alert($(this).html());
    })
})*/
