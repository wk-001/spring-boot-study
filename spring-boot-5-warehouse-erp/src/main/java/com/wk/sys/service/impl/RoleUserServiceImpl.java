package com.wk.sys.service.impl;

import com.wk.sys.entity.RoleUser;
import com.wk.sys.mapper.RoleUserMapper;
import com.wk.sys.service.RoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-12-02
 */
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {

    @Override
    public void editUserRole(Integer userId, Integer[] ids) {
        //先删除用户之前拥有的角色
        Map<String,Object> param = new HashMap<>();
        param.put("uid",userId);
        this.baseMapper.deleteByMap(param);
        //重新添加用户角色
        if(ids.length>0&&null!=ids){
            this.getBaseMapper().batchInsert(userId,ids);
        }
    }
}
