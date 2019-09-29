package com.wk.service.impl;

import com.wk.pojo.Resource;
import com.wk.dao.ResourceMapper;
import com.wk.pojo.Role;
import com.wk.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-09-29
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public List<Resource> getAllResourcesByUserId(int userId) {
		return resourceMapper.getAllResourcesByUserId(userId);
	}
}
