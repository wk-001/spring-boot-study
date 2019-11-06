layui.extend({
    dtree: '/plugin/layui-v2.5.5/layui_ext/dist/dtree'   // dtree.js所在位置；{/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['dtree','layer','jquery'], function(){
    var $ = layui.jquery,
    dtree = layui.dtree,
    layer = layui.layer;

    // 初始化树
    var DemoTree = dtree.render({
        elem: "#demoTree",  //页面容器
        method:'get',           //访问本地json文件必须用get请求，默认是post请求！！！

        /*layui+list集合数据格式（dataStyle + dataFormat）*/
        url: "/plugin/layui-v2.5.5/layui_ext/json/case/styleTree13.json",
        dataStyle: "layuiStyle",  //使用layui风格的数据格式
        dataFormat: "list",  //配置data的风格为list
        response:{message:"msg",statusCode:0},  //修改response中返回数据的定义

        /*dataFormat:"list",      //使用list风格的数据格式,从数据库查出的数据可以直接使用
        url:'/plugin/layui-v2.5.5/layui_ext/json/case/styleTree12.json',*/

        /*dataStyle: "layuiStyle",  //使用layui风格的数据格式
        url:'/plugin/layui-v2.5.5/layui_ext/json/case/asyncTree10.json',     //layui风格的数据格式
        response:{message:"msg",statusCode:0},  //使用layui风格需要修改response中返回数据的定义*/

        //url:'/plugin/layui-v2.5.5/layui_ext/json/case/asyncTree1.json',     //标准数据格式

        initLevel: 1,
        checkbar: true,
        checkbarType: "no-all",// 默认就是all上下级联，其他的值为： no-all半选  p-casc   self  only
        skin: "layui"
    });

    //监听父节点打开/关闭操作
    dtree.on("changeTree('demoTree')" ,function(obj){
        console.log(obj.param); // 点击当前节点传递的参数
        console.log(obj.dom); // 当前节点的jquery对象
        console.log(obj.show); // 节点展开为true，关闭为false
    });

    // 节点单击事件
    dtree.on("node('demoTree')" ,function(obj){
        layer.msg(JSON.stringify(obj.param));
        console.log(obj.param); // 点击当前节点传递的参数
        console.log(obj.dom); // 当前节点的jquery对象
        console.log(obj.childParams); // 当前节点的所有子节点参数
        console.log(obj.parentParam); // 当前节点的父节点参数
    });

    /* 节点双击事件
    dtree.on("nodedblclick('demoTree')" ,function(obj){
        console.log(obj.param); // 点击当前节点传递的参数
        console.log(obj.dom); // 当前节点的jquery对象
        console.log(obj.childParams); // 当前节点的所有子节点参数
        console.log(obj.parentParam); // 当前节点的父节点参数
    });*/

    //选中指定节点
    $("#btn1").click(function () {
        var params = dtree.chooseDataInit("demoTree", "001001,002001");
    });

    //获取选中节点
    $("#btn2").click(function () {
        var params = dtree.getCheckbarNodesParam("demoTree");
        console.log(params);
    });
});
