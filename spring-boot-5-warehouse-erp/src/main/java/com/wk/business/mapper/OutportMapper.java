package com.wk.business.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Outport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wk.business.vo.OutportVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
public interface OutportMapper extends BaseMapper<Outport> {

    IPage<Outport> queryList(Page<Outport> page,@Param("outport") OutportVo outportVo);
}
