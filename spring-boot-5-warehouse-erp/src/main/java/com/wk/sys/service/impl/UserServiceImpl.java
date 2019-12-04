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

	/**
	 * @Cacheable 将方法的运行结果进行缓存，需要相同的数据直接取缓存中取，避免重复查库
	 */
	@Cacheable
	@Override
	public User getById(Serializable id) {
		return super.getById(id);
	}

	/**
	 * @CachePut 缓存的是方法的返回值 每次都会真实调用函数
	 * 缓存默认key是参数；查询方法的参数是id，修改方法的参数是对象 为了避免取到的缓存不一致的问题，统一指定key
	 */
	@CachePut(key = "#eneity.id")
	@Override
	public boolean updateById(User entity) {
		return super.updateById(entity);
	}

	/**
	 * @CachePut 缓存的是方法的返回值 每次都会真实调用函数
	 * 缓存默认key是参数；查询方法的参数是id，修改方法的参数是对象 为了避免取到的缓存不一致的问题，统一指定key
	 */
	@CachePut(key = "#eneity.id")
	@Override
	public boolean save(User entity) {
		return super.save(entity);
	}

	/**
	 *
	 * @CacheEvict 清除缓存
	 * beforeInvocation = true：方法执行之前清除缓存，无论方法是否执行成功都会清除缓存
	 * 默认是在方法执行之后清除缓存，如果方法异常，缓存不会清除
	 */
	@CacheEvict(beforeInvocation = true)
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}
	
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
