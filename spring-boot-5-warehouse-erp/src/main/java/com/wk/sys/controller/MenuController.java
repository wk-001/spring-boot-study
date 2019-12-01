package com.wk.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wk.sys.common.*;
import com.wk.sys.entity.Permission;
import com.wk.sys.entity.User;
import com.wk.sys.service.PermissionService;
import com.wk.sys.vo.PermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 查询左侧菜单
     */
    @RequestMapping("leftMenuJson")
    public DataGridView leftMenuJson(PermissionVo permissionVo){
        //查询所有可用的菜单
        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
                .eq("type", Constast.TYPE_MNEU)
                .eq("available", Constast.AVAILABLE_TRUE);
        List<Permission> list;
        //获取session中的用户信息
        User user = (User) WebUtils.getSession().getAttribute("user");
        //如果是超级管理员就查询所有的菜单
        if(user.getType().equals(Constast.USER_TYPE_SUPER)){
            list = permissionService.list(wrapper);
        }else{
            //普通用户根据用户ID查询可用菜单
            list = permissionService.list(wrapper);
        }
        //将资源数据放入节点对象
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Permission p : list) {
            Integer id = p.getId();
            Integer pid = p.getPid();
            String title = p.getTitle();
            String icon = p.getIcon();
            String href = p.getHref();
            Boolean spread = p.getOpen().equals(Constast.OPEN_TRUE);
            treeNodes.add(new TreeNode(id,pid,title,icon,href,spread));
        }

        //组装树形结构的数据
        List<TreeNode> treeNodeList = TreeNodeBuilder.build(treeNodes, 1);

        return new DataGridView(treeNodeList);
    }
}
