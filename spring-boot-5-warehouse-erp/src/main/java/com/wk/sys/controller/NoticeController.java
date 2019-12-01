package com.wk.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import com.wk.sys.common.WebUtils;
import com.wk.sys.entity.Notice;
import com.wk.sys.entity.User;
import com.wk.sys.service.NoticeService;
import com.wk.sys.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2019-11-29
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 带条件查询所有公告
     */
    @RequestMapping("list")
    public DataGridView list(NoticeVo noticeVo){
        Page<Notice> page = new Page<>(noticeVo.getPage(), noticeVo.getLimit());
        QueryWrapper<Notice> wrapper = new QueryWrapper<Notice>()
                .like(StringUtils.isNotBlank(noticeVo.getTitle()),"title",noticeVo.getTitle())
                .like(StringUtils.isNotBlank(noticeVo.getOpername()),"opername",noticeVo.getOpername())
                .ge(null!=noticeVo.getStartTime(),"createtime",noticeVo.getStartTime())
                .le(null!=noticeVo.getEndTime(),"createtime",noticeVo.getEndTime())
                .orderByDesc("createtime");
        noticeService.page(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 根据ID删除单条数据
     */
    @RequestMapping("delete")
    public ResultObj delete(Integer id){
        try {
            noticeService.removeById(id);
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
            noticeService.removeByIds(Arrays.asList(ids));
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
    public ResultObj saveOrUpdate(Notice notice){
        try {
            if(notice.getId()==null){
                notice.setCreatetime(new Date());
            }
            User user = (User) WebUtils.getSession().getAttribute("user");
            notice.setOpername(user.getName());
            noticeService.saveOrUpdate(notice);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }
}

