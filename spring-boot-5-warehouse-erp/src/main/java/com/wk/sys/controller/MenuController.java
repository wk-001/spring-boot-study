package com.wk.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.common.Constast;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import com.wk.sys.common.WebUtils;
import com.wk.sys.entity.Permission;
import com.wk.sys.entity.User;
import com.wk.sys.service.PermissionService;
import com.wk.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 查询主页面左侧菜单
     * https://www.cnblogs.com/yueshutong/p/9381540.html
     */
    @RequestMapping("leftMenuJson")
    public DataGridView leftMenuJson(){
        //获取session中的用户信息
        User user = (User) WebUtils.getSession().getAttribute("user");
        return permissionService.menuList(user);
    }

    //菜单管理左侧菜单树 查询所有菜单 有菜单管理权限的用户才能操作
    @RequestMapping("menuTree")
    public DataGridView leftMenuTree(){
        return permissionService.menuTree();
    }

    /**
     * 带条件查询所有菜单
     */
    @RequestMapping("list")
    public DataGridView list(PermissionVo permissionVo){
        Page<Permission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        //如果ID不为空，用数据库中的id和pid匹配ID
        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
                .eq(null!=permissionVo.getId(),"id",permissionVo.getId())
                .or()
                .eq(null!=permissionVo.getId(),"pid",permissionVo.getId())
                .eq("type", Constast.TYPE_MNEU)
                .like(StringUtils.isNotBlank(permissionVo.getTitle()),"title",permissionVo.getTitle())
                .orderByAsc("ordernum");
        permissionService.page(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 根据ID删除资源表和角色资源表的数据
     */
    @RequestMapping("delete")
    public ResultObj delete(Integer id){
        try {
            permissionService.deleteMenuById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 存在更新记录，否插入一条记录
     */
    @RequestMapping("saveOrUpdate")
    public ResultObj saveOrUpdate(Permission permission){
        try {
            permission.setType(Constast.TYPE_MNEU);     //设置添加类型为菜单
            permissionService.saveOrUpdate(permission);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 查找最大的排序码
     */
    @RequestMapping("getMaxOrderNum")
    public Integer getMaxOrderNum(){
        return permissionService.getMaxOrderNum();
    }

    /**
     * 删除前先判断是否有子菜单
     */
    @RequestMapping("hasSubMenu")
    public Integer hasSubMenu(Integer id){
        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
                .eq("pid",id);
        return permissionService.count(wrapper);
    }
}
