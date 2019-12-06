package com.wk.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wk.business.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
public interface GoodsService extends IService<Goods> {

	IPage<Goods> queryList(Page<Goods> page, GoodsVo goodsVo);
}
