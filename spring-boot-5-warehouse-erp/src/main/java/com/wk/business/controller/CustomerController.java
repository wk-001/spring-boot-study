package com.wk.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Customer;
import com.wk.business.service.CustomerService;
import com.wk.business.vo.CustomerVo;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	/**
	 * 带条件查询所有客户
	 */
	@RequestMapping("list")
	public DataGridView list(CustomerVo customerVo){
		Page<Customer> page = new Page<>(customerVo.getPage(), customerVo.getLimit());
		QueryWrapper<Customer> wrapper = new QueryWrapper<Customer>()
				.like(StringUtils.isNotBlank(customerVo.getCustomername()),"customername",customerVo.getCustomername())
				.like(StringUtils.isNotBlank(customerVo.getConnectionperson()),"connectionperson",customerVo.getConnectionperson())
				.like(StringUtils.isNotBlank(customerVo.getPhone()),"phone",customerVo.getPhone());
		customerService.page(page,wrapper);
		return new DataGridView(page.getTotal(),page.getRecords());
	}

	/**
	 * 根据ID删除单条数据
	 */
	@RequestMapping("delete")
	public ResultObj delete(Integer id){
		try {
			customerService.removeById(id);
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
			customerService.removeByIds(Arrays.asList(ids));
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
	public ResultObj saveOrUpdate(Customer customer){
		try {
			customerService.saveOrUpdate(customer);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}
}

