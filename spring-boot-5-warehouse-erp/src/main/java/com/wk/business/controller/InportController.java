package com.wk.business.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Inport;
import com.wk.business.service.InportService;
import com.wk.business.vo.InportVo;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import com.wk.sys.common.WebUtils;
import com.wk.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/inport")
public class InportController {

    @Autowired
    private InportService inportService;
    
    /**
     * 带条件查询所有进货商品
     */
    @RequestMapping("list")
    public DataGridView list(InportVo inportVo){
        Page<Inport> page = new Page<>(inportVo.getPage(), inportVo.getLimit());
        inportService.queryList(page, inportVo);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 根据ID删除单条数据
     * 更新商品数量
     */
    @RequestMapping("delete")
    public ResultObj delete(Integer id){
        try {
            inportService.removeInport(id);
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
            inportService.removeByIds(Arrays.asList(ids));
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 添加进货信息和商品信息
     */
    @RequestMapping("save")
    public ResultObj save(Inport inport){
        try {
            inport.setInporttime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            inport.setOperateperson(user.getName());
            inportService.saveInport(inport);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 更新进货信息和商品信息
     */
    @RequestMapping("update")
    public ResultObj update(Inport inport){
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            inport.setOperateperson(user.getName());
            inportService.updateInport(inport);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }
}

