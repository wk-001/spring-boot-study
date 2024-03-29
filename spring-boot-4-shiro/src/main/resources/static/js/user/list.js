layui.use(['jquery','element','form','layer','table','laydate'], function(){
    var $ = layui.jquery
        ,element = layui.element
        ,form = layui.form
        ,layer = layui.layer
        ,table = layui.table
        ,laydate = layui.laydate

    //渲染数据表格
    var tableIns = table.render({
        elem: '#userTable'      //渲染目标对象 数据表格对应ID
        ,height: 'full-400'            //数据表格高度 可用高度-指定高度
        //,url:'/user/list'     //真实数据
        ,url: '/json/user.json/' //测试数据
        ,page: true //开启分页
        ,toolbar:"#toolBar"     //引用表头工具栏 toolBar是div的ID
        ,defaultToolbar: ['filter', 'print', 'exports']     //修改默认工具栏的功能和顺序
        ,limits: [10, 20, 30]   //可显示的数据条数
        ,limit: 10              //每页默认显示的数量
        ,even: true             //开启隔行背景
        //,totalRow:true	    //开启合并行
        ,text: {
            none: '暂无相关数据' //无数据时显示的内容 默认：无数据。
        }
        /*,done: function(res, curr, count){      //数据渲染完的回调
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
            console.log(res);
            console.log(curr);      //得到当前页码
            console.log(count);     //得到数据总量
        }*/
        ,cols: [[ //表头
            {type:'checkbox', fixed: 'left'}    //fixed冻结列的位置
            ,{field: 'id', title: 'ID',  sort: true}
            ,{field: 'username', title: '用户名',edit:true}    //编辑单元格
            ,{field: 'sex', title: '性别',  sort: true,align:'center',templet: function(d){
                // return d.sex=='女'?'<span style="color: #c00;">'+ d.sex +'</span>':d.sex;
                return d.sex==1?'男':'女';
            }}
            ,{field: 'city', title: '城市'}
            ,{field: 'sign', title: '签名'}
            ,{field: 'experience', title: '积分', sort: true/*,totalRow:true*/}   //合计数量，必须开启合并行才能使用
            ,{field: 'score', title: '评分', sort: true}
            ,{field: 'classify', title: '职业'}
            ,{field: 'wealth', title: '财富', sort: true}
            ,{fixed: 'right', title:'操作', toolbar: '#editDemo',align:'center'}     //引用编辑删除栏 editDemo是div的ID
        ]]
    });

    //监听头部工具栏事件 userTable:数据表格的ID
    table.on('toolbar(userTable)', function(obj){
        switch(obj.event){
            case 'add':             //工具栏按钮lay-event属性的值
                openAddUser();
                break;
            case 'delete':
                layer.msg('删除');
                break;
            case 'update':
                layer.msg('编辑');
                break;
            case 'refreshTable':        //在不刷新页面的情况刷新表格数据
                /*table.reload("userTable", {
                    url: '/json/user.json/'
                });*/
                tableIns.reload();      //在不刷新页面的情况刷新表格数据
                break;
            case 'getSelect':   //获取checkbox选中行的数据
                var checkStatus = table.checkStatus('userTable'); //userTable:数据表格的ID
                console.log(checkStatus.data) //获取选中行的数据
                console.log(checkStatus.data.length) //获取选中行数量，可作为是否有选中行的条件
                console.log(checkStatus.isAll ) //表格是否全选
                break;
        };
    });

    // 监听复选框事件 userTable:数据表格的ID
    /*table.on('checkbox(userTable)', function(obj){
        console.log(obj.checked); //当前是否选中状态
        console.log(obj.data); //选中行的相关数据
        console.log(obj.type); //如果触发的是全选，则为：all，如果触发的是单选，则为：one
    });*/

    //监听单元格编辑 单元格被编辑，且值发生改变时触发，前提是单元格设置为可编辑
    // 回调函数返回一个object参数 userTable:数据表格的ID
    table.on('edit(userTable)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
        console.log(obj.value); //得到修改后的值
        console.log(obj.field); //当前编辑的字段名
        console.log(obj.data); //所在行的所有相关数据
        //发送post请求更新数据库数据
    });

    //监听行单击事件
    table.on('row(userTable)', function(obj){
        console.log(obj.tr) //得到当前行元素对象
        console.log(obj.data) //得到当前行数据
        //obj.del(); //删除当前行
        //obj.update(fields) //修改当前行数据
    });

//监听行双击事件
    /*table.on('rowDouble(userTable)', function(obj){
        console.log(obj.tr) //得到当前行元素对象
        console.log(obj.data) //得到当前行数据
        //obj.del(); //删除当前行
        //obj.update(fields) //修改当前行数据
    });*/

    //监听工具条  注：tool 是工具条事件名，userTable 是 table 原始容器的属性 lay-filter="对应的值"
    table.on('tool(userTable)', function(obj){
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

        if(layEvent === 'del'){ //删除
            layer.confirm('真的删除行么', function(index){
                obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                layer.close(index);
                //向服务端发送删除指令
            });
        } else if(layEvent === 'edit'){ //编辑
            openUpdUser(data);      //修改当前行数据
            //同步更新缓存对应的值
            obj.update({
                username: '123'
                ,title: 'xxx'
            });
        } else if(layEvent === 'LAYTABLE_TIPS'){
            layer.alert('Hi，头部工具栏扩展的右侧图标。');
        }
    });

    //绑定事件选择器
    laydate.render({
        elem: '#startdate'
    });
    laydate.render({
        elem: '#enddate'
    });

    var url = "";       //区分添加和修改提交的URL
    var mainModel = "";     //弹窗对象
    //打开添加用户弹出框
    function openAddUser(){
        mainModel = layer.open({
            type: 1
            ,title:'添加用户'
            ,content:$("#saveOrUpdate")
            ,area: ['750px','320px']     //弹窗宽高
            ,shade: 0           //设置超过0的值遮罩层会把弹窗覆盖？？？
            ,success:function (index) {
                //打开弹窗清空整个form表单,jquery对象获取的是所有对象的数组，数组中是dom对象，dom对象才有reset();方法
                $("#userForm")[0].reset();
                url = "/user/add";
            }
        });
    }

    //打开修改用户弹出框
    function openUpdUser(data) {
        mainModel = layer.open({
            type: 1
            , title: '修改用户信息'
            , content: $("#saveOrUpdate")
            , area: ['750px', '320px']     //弹窗宽高
            , shade: 0           //设置超过0的值遮罩层会把弹窗覆盖？？？
            ,success:function (index) {         //弹窗成功后回调
                //给lay-filter="userForm"的表单赋值,name相同可以直接赋值
                form.val("userForm",data);
                url = "/user/update";
            }
        });
    }

    //保存数据，监听submit
    form.on('submit(addUser)', function(obj) {
        //序列化表单数据
        var params = $("#userForm").serialize();
        layer.msg(params);
        $.post(url,params,function (obj) {
            layer.msg(obj);
            //关闭弹窗
            layer.close(mainModel);
            // 刷新数据表格
            tableIns.reload();      //在不刷新页面的情况刷新表格数据
        })
    })
});