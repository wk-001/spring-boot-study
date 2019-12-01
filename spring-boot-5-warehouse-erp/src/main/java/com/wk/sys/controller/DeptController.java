package com.wk.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import com.wk.sys.common.TreeNode;
import com.wk.sys.entity.Dept;
import com.wk.sys.service.DeptService;
import com.wk.sys.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 加载部门管理左侧菜单树
     */
    @RequestMapping("leftMenuTree")
    public DataGridView leftMenuTree(){
        List<Dept> list = deptService.list();
        List<TreeNode> nodes = new ArrayList<>();
        for (Dept dept : list) {
            nodes.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),dept.getOpen()==1));
        }
        return new DataGridView(nodes);
    }

    /**
     * 带条件查询所有公告
     */
    @RequestMapping("list")
    public DataGridView list(DeptVo deptVo){
        Page<Dept> page = new Page<>(deptVo.getPage(), deptVo.getLimit());
        //如果ID不为空，用数据库中的id和pid匹配ID
        QueryWrapper<Dept> wrapper = new QueryWrapper<Dept>()
                .like(StringUtils.isNotBlank(deptVo.getTitle()),"title",deptVo.getTitle())
                .like(StringUtils.isNotBlank(deptVo.getAddress()),"address",deptVo.getAddress())
                .like(StringUtils.isNotBlank(deptVo.getRemark()),"remark",deptVo.getRemark())
                .eq(null!=deptVo.getId(),"id",deptVo.getId())
                .or()
                .eq(null!=deptVo.getId(),"pid",deptVo.getId())
                .orderByAsc("ordernum");
        deptService.page(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 根据ID删除单条数据
     */
    @RequestMapping("delete")
    public ResultObj delete(Integer id){
        try {
            deptService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除数据
     */
    @RequestMapping("batchDelete")
    public ResultObj batchDelete(Integer[] ids){
        try {
            deptService.removeByIds(Arrays.asList(ids));
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
    public ResultObj saveOrUpdate(Dept dept){
        try {
            if(dept.getId()==null){
                dept.setCreatetime(new Date());
            }
            deptService.saveOrUpdate(dept);
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
       return deptService.getMaxOrderNum();
    }

    /**
     * 删除前先判断是否有子部门
     */
    @RequestMapping("hasSubDept")
    public Integer hasSubDept(Integer id){
        QueryWrapper<Dept> wrapper = new QueryWrapper<Dept>()
                .eq("pid",id);
        return deptService.count(wrapper);
    }
}

