package com.wk.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Inport;
import com.wk.business.vo.InportVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
public interface InportMapper extends BaseMapper<Inport> {

    IPage<Inport> queryList(Page<Inport> page,@Param("inport") InportVo inportVo);
}
