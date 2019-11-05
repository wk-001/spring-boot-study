layui.extend({
    dtree: '/plugin/layui-v2.5.5/layui_ext/dist/dtree'   // dtree.js所在位置；{/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['dtree','layer','jquery'], function(){
    var $ = layui.jquery,
    dtree = layui.dtree,
    layer = layui.layer;

    // 初始化树
    var DemoTree = dtree.render({
        elem: "#demoTree",  //页面容器
        // url: '/json/asyncTree1.json' 使用url加载（可与data加载同时存在）
        url:'/plugin/layui-v2.5.5/layui_ext/json/case/asyncTree2.json'
    });

    // 绑定节点点击
    /*dtree.on("node('demoTree')" ,function(obj){
        layer.msg(JSON.stringify(obj.param));
    });*/
});
