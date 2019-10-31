layui.use(['jquery','element','form','layer','table'], function(){
    var $ = layui.jquery
        ,element = layui.element
        ,form = layui.form
        ,layer = layui.layer
        ,table = layui.table
    //渲染数据表格
    table.render({
        elem: '#userTable'      //数据表格对应ID
        ,height: 624            //full-200 可用高度-指定高度
        //,url:'/user/list'     //真实数据
        ,url: '/json/user.json/' //测试数据
        ,page: true //开启分页
        ,toolbar:"#toolBar"     //引用表头工具栏 toolBar是div的ID
        ,defaultToolbar: ['filter', 'print', 'exports']     //修改默认工具栏的功能和顺序
        ,limits: [10, 20, 30]   //可显示的数据条数
        ,limit: 10              //每页默认显示的数量
        ,even: true             //开启隔行背景
        ,totalRow:true	    //开启合并行
        ,text: {
            none: '暂无相关数据' //无数据时显示的内容 默认：无数据。
        }
        ,done: function(res, curr, count){
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
            console.log(res);

            console.log(curr);      //得到当前页码

            console.log(count);     //得到数据总量
        }
        ,cols: [[ //表头
            {type:'checkbox', fixed: 'left'}    //fixed冻结列的位置
            ,{field: 'id', title: 'ID',  sort: true}
            ,{field: 'username', title: '用户名',edit:true}    //编辑单元格
            ,{field: 'sex', title: '性别',  sort: true,align:'center',templet: function(d){
                return d.sex=='女'?'<span style="color: #c00;">'+ d.sex +'</span>':d.sex;
            }}
            ,{field: 'city', title: '城市'}
            ,{field: 'sign', title: '签名'}
            ,{field: 'experience', title: '积分', sort: true,totalRow:true}   //合计数量，必须开启合并行才能使用
            ,{field: 'score', title: '评分', sort: true}
            ,{field: 'classify', title: '职业'}
            ,{field: 'wealth', title: '财富', sort: true}
            ,{fixed: 'right', title:'操作', toolbar: '#editDemo',align:'center'}     //引用编辑删除栏 editDemo是div的ID
        ]]
    });
});