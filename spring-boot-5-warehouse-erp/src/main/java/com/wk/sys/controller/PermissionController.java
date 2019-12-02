package com.wk.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.common.Constast;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import com.wk.sys.entity.Permission;
import com.wk.sys.service.PermissionService;
import com.wk.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	/**
	 * 带条件查询所有权限
	 */
	@RequestMapping("list")
	public DataGridView list(PermissionVo permissionVo){
		Page<Permission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
		//如果ID不为空，用数据库中的id和pid匹配ID
		QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>()
				.eq("type", Constast.TYPE_PERMISSION)		//查询权限
				.like(StringUtils.isNotBlank(permissionVo.getTitle()),"title",permissionVo.getTitle())
				.like(StringUtils.isNotBlank(permissionVo.getPercode()),"percode",permissionVo.getPercode())
				.eq(null!=permissionVo.getId(),"pid",permissionVo.getId())
				.orderByAsc("ordernum");
		permissionService.page(page,wrapper);
		return new DataGridView(page.getTotal(),page.getRecords());
	}

	/**
	 * 存在更新记录，否插入一条记录
	 */
	@RequestMapping("saveOrUpdate")
	public ResultObj saveOrUpdate(Permission permission){
		try {
			permission.setType(Constast.TYPE_PERMISSION);     //设置添加类型为权限
			permissionService.saveOrUpdate(permission);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}

	/**
	 * 查找最大的排序码
	 */
	@RequestMapping("getMaxOrderNum")
	public Integer getMaxOrderNum(){
		return permissionService.getMaxOrderNum();
	}

	/**
	 * 根据ID删除单条数据
	 */
	@RequestMapping("delete")
	public ResultObj delete(Integer id){
		try {
			permissionService.removeById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
}

