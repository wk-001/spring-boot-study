package com.wk.sys.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wk.sys.vo.UserVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
public interface UserMapper extends BaseMapper<User> {

	IPage<User> queryList(Page<User> page,@Param("userVo") UserVo userVo);
}
