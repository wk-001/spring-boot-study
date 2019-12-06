package com.wk.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.sys.common.Constast;
import com.wk.sys.common.PasswordUtils;
import com.wk.sys.entity.User;
import com.wk.sys.mapper.RoleUserMapper;
import com.wk.sys.mapper.UserMapper;
import com.wk.sys.service.UserService;
import com.wk.sys.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@CacheConfig(cacheNames = "user")       //抽取缓存公共配置 指定统一缓存组件名称；和value作用一样
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private RoleUserMapper roleUserMapper;

	@Override
	public IPage<User> queryList(Page<User> page, UserVo userVo) {
		return this.getBaseMapper().queryList(page, userVo);
	}

	@Override
	public void deleteUserById(Integer id) {
		Map<String,Object> param = new HashMap<>();
		param.put("uid", id);
		this.getBaseMapper().deleteById(id);
		roleUserMapper.deleteByMap(param);
	}

	@Override
	public Integer getMaxOrderNum() {
		int maxOrderNum = this.getBaseMapper().getMaxOrderNum();
		if (maxOrderNum>0){
			return maxOrderNum+1;
		}else {
			return 1;
		}
	}

	@Override
	public void resetPwd(Integer id) {
		User user = new User();
		user.setId(id);
		user.setPwd(Constast.USER_DEFAULT_PWD);
		user = PasswordUtils.encryptPassword(user);
		this.getBaseMapper().updateById(user);
	}
}
