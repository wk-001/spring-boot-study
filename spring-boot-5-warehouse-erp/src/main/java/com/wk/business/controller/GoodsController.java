package com.wk.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Goods;
import com.wk.business.service.GoodsService;
import com.wk.business.vo.GoodsVo;
import com.wk.sys.common.AppFileUtils;
import com.wk.sys.common.Constast;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
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
	 * 根据路径删除图片
	 */
	@RequestMapping("delete")
	public ResultObj delete(Goods goods){
		try {
			goodsService.removeById(goods.getId());
			AppFileUtils.removeFileByPath(goods.getGoodsimg());
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
	 * 添加商品信息
	 */
	@RequestMapping("save")
	public ResultObj save(Goods goods){
		try {
			//去掉临时文件后缀
			if(goods.getGoodsimg()!= null && goods.getGoodsimg().endsWith("_temp")){
				String newFileName = AppFileUtils.renameFile(goods.getGoodsimg());
				goods.setGoodsimg(newFileName);
			}
			goodsService.save(goods);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}

	/**
	 * 修改商品信息
	 */
	@RequestMapping("update")
	public ResultObj update(Goods goods){
		try {
			//如果是默认图片不做修改
			if(!(goods.getGoodsimg()!= null && goods.getGoodsimg().equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG))) {
				//不是默认图片就去掉临时后缀
				if (goods.getGoodsimg().endsWith("_temp")) {
					String newFileName = AppFileUtils.renameFile(goods.getGoodsimg());
					goods.setGoodsimg(newFileName);
					//删除原来的图片
					String oldPath = goodsService.getById(goods.getId()).getGoodsimg();
					AppFileUtils.removeFileByPath(oldPath);
				}
			}
			goodsService.updateById(goods);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}

	/**
	 * 查询商品的名称、规格、供应商
	 */
	@RequestMapping("queryName")
	public DataGridView queryName(Integer providerId){
		QueryWrapper<Goods> wrapper = new QueryWrapper<Goods>()
				.select("id","goodsname","size")
				.eq("available",Constast.AVAILABLE_TRUE)
				.eq("providerid",providerId);
		List<Goods> list = goodsService.list(wrapper);
		return new DataGridView(list);
	}
}

