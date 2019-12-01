package com.wk.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import com.wk.sys.entity.Loginfo;
import com.wk.sys.service.LoginfoService;
import com.wk.sys.vo.LogInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * <p>
 *  日志信息前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@RestController
@RequestMapping("/loginfo")
public class LoginfoController {

    @Autowired
    private LoginfoService loginfoService;

    /**
     * 查询所有日志信息
     */
    @RequestMapping("list")
    public DataGridView list(LogInfoVo logInfoVo){
        IPage<Loginfo> page = new Page<>(logInfoVo.getPage(),logInfoVo.getLimit());
        //条件构造器：loginname和loginip不为空的情况下进行匹配；输入的时间大于等于开始时间，小于等于结束时间
        QueryWrapper<Loginfo> wrapper = new QueryWrapper<Loginfo>()
                .like(StringUtils.isNotBlank(logInfoVo.getLoginname()),"loginname",logInfoVo.getLoginname())
                .like(StringUtils.isNotBlank(logInfoVo.getLoginip()),"loginip",logInfoVo.getLoginip())
                .ge(null!=logInfoVo.getStartTime(),"logintime",logInfoVo.getStartTime())
                .le(null!=logInfoVo.getEndTime(),"logintime",logInfoVo.getEndTime())
                .orderByDesc("logintime");
        loginfoService.page(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 根据ID删除单条数据
     */
    @RequestMapping("delete")
    public ResultObj delete(Integer id){
        try {
            loginfoService.removeById(id);
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
            loginfoService.removeByIds(Arrays.asList(ids));
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

