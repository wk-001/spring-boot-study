package com.wk.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wk.business.entity.Inport;
import com.wk.business.entity.Outport;
import com.wk.business.vo.OutportVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
public interface OutportService extends IService<Outport> {

    void saveOutport(Inport inport);

    IPage<Outport> queryList(Page<Outport> page, OutportVo outportVo);
}
