package com.wk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wk.pojo.Resource;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-09-29
 */
public interface ResourceMapper extends BaseMapper<Resource> {

	List<Resource> getAllResourcesByUserId(int userId);
}
