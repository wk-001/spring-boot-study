package com.wk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wk.pojo.Resource;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author wk
 * @since 2019-09-29
 */
public interface ResourceService extends IService<Resource> {

	/**
	 * 根据认证成功的userId查询该用户的所有权限
	 * @param userId
	 * @return
	 */
	List<Resource> getAllResourcesByUserId(int userId);
}
