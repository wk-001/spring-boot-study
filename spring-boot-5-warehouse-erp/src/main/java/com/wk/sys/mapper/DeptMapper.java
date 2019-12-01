package com.wk.sys.mapper;

import com.wk.sys.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
public interface DeptMapper extends BaseMapper<Dept> {

    int getMaxOrderNum();
}
