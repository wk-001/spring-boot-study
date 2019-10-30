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
//日期依赖laydate模块
//表单依赖form模块
//富文本依赖'layedit'模块
layui.use(['element','jquery','laydate','form','layedit'], function(){
    var $ = layui.jquery
    ,element = layui.element
    ,laydate = layui.laydate
    ,form = layui.form
    ,layedit = layui.layedit
    //创建一个编辑器，'LAY_demo_editor'是富文本编辑器文本域的ID
    var editIndex = layedit.build('LAY_demo_editor');
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

    //常规用法
    laydate.render({
        elem: '#test1'      //指定元素 test1是input的id
        ,type: 'date'   //时间类型；year month date(default) time datetime
        //,range: true        开启左右面板时间范围选择，如果设置 true，将默认采用 “ - ”分割。 也可以直接设置分割字符
        ,format: 'yyyy年MM月dd日'  //时间格式可任意组合
        ,value:new Date()       //指定初始值为当前时间
        ,min:'2010-1-1'         //可选择的最早日期，或-7：最早选择7天前
        ,max:0                  //可选择的最大日期，0表示当前日期
    });

    //下拉框追加数据
    $("#btn1").click(function () {
        //获取select对象
        var addr = $("#addr");
        addr.append("<option value=5>河北</option>")
        //追加完成后需要重新渲染下拉框或重新渲染全部表单
        form.render("select");      //只渲染下拉框
        //form.render();              //渲染所有
    });
    //表单赋值初始值,根据form表单的lay-filter和表单的name属性赋值
    $("#btn2").click(function () {
        form.val("formTest",{
             "title":'tom'
            // ,"pwd":'123456'  给密码赋值会刷新页面？？？
            ,"city":1
            ,"hobby[write]":true    //根据多选框name属性回显
            ,"hobby[play]":true
        });
        //模拟从数据库获得的数据进行回显
       /* var hobby = "1,2,3";
        var hobbys = hobby.split(",")
        //得到页面name=hobby的属性值
        var hobbyElem = $("[name='hobby']");
        $.each(hobbys,function (i, str) {
            $.each(hobbyElem,function (j, item) {
                var jdom = $(item);     //dom转成jdom
                if(jdom.val()==str){
                    jdom.attr("checked","checked");
                }
            });
        });
        form.render();*/
    });
        //自定义验证规则
    form.verify({
        username: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
                return '用户名不能有特殊字符';
            }
            if(/(^\_)|(\__)|(\_+$)/.test(value)){
                return '用户名首尾不能出现下划线\'_\'';
            }
            if(/^\d+\d+\d$/.test(value)){
                return '用户名不能全为数字';
            }
        }

        //我们既支持上述函数式的方式，也支持下述数组的形式
        //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
        ,pass: [
            /^[\S]{6,12}$/
            ,'密码必须6到12位，且不能出现空格'
        ]
    });

    //信息框弹窗
    $("#t0").click(function () {
        var x = layer.open({
            type:0      //设置类型 默认0 可以不写 1：页面层 2：iframe层
            ,title:"提示"
            ,content:"信息框内容"        //也可以显示$("#mydiv")中的内容
            ,skin:"layui-layer-molv"        //皮肤
            //,time: 2000         2秒后自动关闭
            ,shade: [0.8, '#393D49']          //遮罩层透明度和自定义颜色
            ,shadeClose:true            //点击遮罩层关闭弹窗
            ,area: ['800px', '500px']    //弹窗宽高
            ,btn: ['按钮一', '按钮二', '按钮三']
            ,yes: function(index, layero){
                //按钮【按钮一】的回调
                alert(1);
            }
            ,btn2: function(index, layero){
                //按钮【按钮二】的回调
                alert(2);
                return false// 开启该代码可禁止点击该按钮关闭
            }
            ,btn3: function(index, layero){
                //按钮【按钮三】的回调
                alert(3);
                //return false 开启该代码可禁止点击该按钮关闭
            }
            ,cancel: function(){
                //右上角关闭回调
                alert("closed");
                //return false 开启该代码可禁止点击该按钮关闭
            }
        })
        alert(x);
    });

    //页面层弹窗
    $("#t1").click(function () {
        layer.open({
            type: 1      //设置类型 默认0 可以不写 1：页面层 2：iframe层
            , title: "提示"
            , content: $("#mydiv")     //内容
            , skin: "layui-layer-molv"        //皮肤
            ,area: ['800px', '500px']     //弹窗宽高
            ,maxmin:true        //是否显示最大化最小化按钮，仅type=1 or type=2 有效
        });
    });

    //iframe层弹窗
    $("#t2").click(function () {
        layer.open({
            type: 2      //设置类型 默认0 可以不写 1：页面层 2：iframe层
            , title: "提示"
            , content: "layout.html"     //url
            , skin: "layui-layer-molv"        //皮肤
            ,area: ['800px', '500px']     //弹窗宽高
            ,maxmin:true        //是否显示最大化最小化按钮，仅type=1 or type=2 有效
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
