package com.wk.sys.mapper;

import com.wk.sys.entity.RoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-12-02
 */
public interface RoleUserMapper extends BaseMapper<RoleUser> {

    void batchInsert(@Param("userId") Integer userId,@Param("ids") Integer[] ids);
}
