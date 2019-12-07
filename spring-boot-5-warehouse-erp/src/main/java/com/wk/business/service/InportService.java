package com.wk.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Inport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wk.business.vo.InportVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
public interface InportService extends IService<Inport> {

    IPage<Inport> queryList(Page<Inport> page, InportVo inportVo);

    void saveInport(Inport inport);

    void updateInport(Inport inport);

    void removeInport(Integer id);
}
