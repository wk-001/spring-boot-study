package com.wk.sys.service;

import com.wk.sys.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
public interface PermissionService extends IService<Permission> {

	/**
	 * 获取最大的排序码
	 */
	Integer getMaxOrderNum();

	/**
	 * 根据ID删除资源表和角色资源表的数据
	 */
	void deleteMenuById(Integer id);
}
