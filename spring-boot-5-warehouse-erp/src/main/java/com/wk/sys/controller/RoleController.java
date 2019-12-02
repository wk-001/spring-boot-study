package com.wk.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.common.Constast;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import com.wk.sys.common.TreeNode;
import com.wk.sys.entity.Permission;
import com.wk.sys.entity.Role;
import com.wk.sys.service.PermissionService;
import com.wk.sys.service.RolePermissionService;
import com.wk.sys.service.RoleService;
import com.wk.sys.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RolePermissionService rolePermissionService;

	/**
	 * 带条件查询所有角色
	 */
	@RequestMapping("list")
	public DataGridView list(RoleVo roleVo){
		Page<Role> page = new Page<>(roleVo.getPage(), roleVo.getLimit());
		QueryWrapper<Role> wrapper = new QueryWrapper<Role>()
				.like(StringUtils.isNotBlank(roleVo.getName()),"name",roleVo.getName())
				.like(StringUtils.isNotBlank(roleVo.getRemark()),"remark",roleVo.getRemark())
				.eq(roleVo.getAvailable()!=null,"available",roleVo.getAvailable())
				.orderByAsc("createtime");;
		roleService.page(page,wrapper);
		return new DataGridView(page.getTotal(),page.getRecords());
	}

	/**
	 * 根据ID删除角色、角色权限、用户角色数据
	 */
	@RequestMapping("delete")
	public ResultObj delete(Integer id){
		try {
			roleService.removeRoleById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

	/**
	 * 存在更新记录，否插入一条记录
	 */
	@RequestMapping("saveOrUpdate")
	public ResultObj saveOrUpdate(Role role){
		try {
			if(role.getId()==null){
				role.setCreatetime(new Date());
			}
			roleService.saveOrUpdate(role);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}

	/**
	 * 根据角色ID查询角色拥有的权限
	 */
	@RequestMapping("queryPermissionByRoleid")
	public DataGridView queryPermissionByRoleid(Integer roleId){
		//查询所有可用的菜单和权限
		QueryWrapper<Permission> perWrapper = new QueryWrapper<Permission>()
				.eq("available", Constast.AVAILABLE_TRUE);
		List<Permission> allPermissions = permissionService.list(perWrapper);
		//根据角色ID查询角色拥有的权限id
		List<Integer> hasPermissions = permissionService.queryHasResources(roleId);
			//构造List<TreeNode>
		List<TreeNode> treeNodes = new ArrayList<>();
		for (Permission p1 : allPermissions) {
			String checkArr="0";
			for (Integer p2 : hasPermissions) {
				if(p1.getId().equals(p2)){
					checkArr="1";
					break;			//两个集合中只有一组数据匹配，如果匹配直接结束本次循环
				}
			}

			treeNodes.add(new TreeNode(p1.getId(),p1.getPid(),p1.getTitle(),p1.getOpen()==1,checkArr));
		}
		return new DataGridView(treeNodes);
	}

	/**
	 * 重置角色和权限的关系
	 */
	@RequestMapping("assignPermission")
	public ResultObj assignPermission(Integer roleId,Integer[] ids){
		try {
			rolePermissionService.assignPermission(roleId,ids);
			return ResultObj.DISPATCH_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DISPATCH_ERROR;
		}
	}
	
}

