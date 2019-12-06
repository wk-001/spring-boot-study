package com.wk.business.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Goods;
import com.wk.business.service.GoodsService;
import com.wk.business.vo.GoodsVo;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
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
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	/**
	 * 带条件查询所有商品
	 */
	@RequestMapping("list")
	public DataGridView list(GoodsVo goodsVo){
		Page<Goods> page = new Page<>(goodsVo.getPage(), goodsVo.getLimit());
		goodsService.queryList(page, goodsVo);
		return new DataGridView(page.getTotal(),page.getRecords());
	}

	/**
	 * 根据ID删除单条数据
	 */
	@RequestMapping("delete")
	public ResultObj delete(Integer id){
		try {
			goodsService.removeById(id);
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
			goodsService.removeByIds(Arrays.asList(ids));
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
	public ResultObj saveOrUpdate(Goods goods){
		try {
			goodsService.saveOrUpdate(goods);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}
}

