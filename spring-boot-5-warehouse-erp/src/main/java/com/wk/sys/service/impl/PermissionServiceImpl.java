package com.wk.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.sys.common.Constast;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.TreeNode;
import com.wk.sys.common.TreeNodeBuilder;
import com.wk.sys.entity.Permission;
import com.wk.sys.entity.User;
import com.wk.sys.mapper.PermissionMapper;
import com.wk.sys.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {


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
	public void deleteMenuById(Integer id) {
		super.removeById(id);
		this.getBaseMapper().deleteRolePermissionByPid(id);
	}

    @Override
    public List<Integer> queryHasResources(Integer roleId) {
        return this.getBaseMapper().queryHasResources(roleId);
    }


	@Override
	public DataGridView menuList(User user) {
		//查询所有可用的菜单
		QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
				.eq("type", Constast.TYPE_MNEU)
				.eq("available", Constast.AVAILABLE_TRUE);
		List<Permission> list;
		//如果是超级管理员就查询所有的菜单
		if(user.getType().equals(Constast.USER_TYPE_SUPER)){
			list = this.getBaseMapper().selectList(wrapper);
		}else{
			//普通用户根据用户ID查询可用菜单
			list = this.getBaseMapper().queryPermissionByUserId(user.getId());
		}
		//将资源数据放入节点对象
		List<TreeNode> treeNodes = new ArrayList<>();
		for (Permission p : list) {
			Integer id = p.getId();
			Integer pid = p.getPid();
			String title = p.getTitle();
			String icon = p.getIcon();
			String href = p.getHref();
			Boolean spread = p.getOpen().equals(Constast.OPEN_TRUE);
			treeNodes.add(new TreeNode(id,pid,title,icon,href,spread));
		}

		//组装树形结构的数据
		List<TreeNode> treeNodeList = TreeNodeBuilder.build(treeNodes, 1);
		return new DataGridView(treeNodeList);
	}

	@Override
	public List<String> getCodeByUserId(Integer id) {
		return this.getBaseMapper().getCodeByUserId(id);
	}

}
