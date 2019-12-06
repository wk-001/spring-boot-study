package com.wk.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Provider;
import com.wk.business.service.ProviderService;
import com.wk.business.vo.ProviderVo;
import com.wk.sys.common.Constast;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

	@Autowired
	private ProviderService providerService;

	/**
	 * 带条件查询所有客户
	 */
	@RequestMapping("list")
	public DataGridView list(ProviderVo providerVo){
		Page<Provider> page = new Page<>(providerVo.getPage(), providerVo.getLimit());
		QueryWrapper<Provider> wrapper = new QueryWrapper<Provider>()
				.like(StringUtils.isNotBlank(providerVo.getProvidername()),"providername",providerVo.getProvidername())
				.like(StringUtils.isNotBlank(providerVo.getConnectionperson()),"connectionperson",providerVo.getConnectionperson())
				.like(StringUtils.isNotBlank(providerVo.getPhone()),"phone",providerVo.getPhone());
		providerService.page(page,wrapper);
		return new DataGridView(page.getTotal(),page.getRecords());
	}

	/**
	 * 根据ID删除单条数据
	 */
	@RequestMapping("delete")
	public ResultObj delete(Integer id){
		try {
			providerService.removeById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

	/**
	 * 批量删除数据
	 */
	@RequestMapping("batchDelete")
	public ResultObj batchDelete(Integer[] ids){
		try {
			providerService.removeByIds(Arrays.asList(ids));
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
	public ResultObj saveOrUpdate(Provider provider){
		try {
			providerService.saveOrUpdate(provider);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}

	/**
	 * 查询所有可用供应商
	 */
	@RequestMapping("queryName")
	public DataGridView queryName(){
		QueryWrapper<Provider> wrapper = new QueryWrapper<Provider>()
				.select("id","providername")
				.eq("available", Constast.AVAILABLE_TRUE);
		List<Provider> list = providerService.list(wrapper);
		return new DataGridView(list);
	}
}

