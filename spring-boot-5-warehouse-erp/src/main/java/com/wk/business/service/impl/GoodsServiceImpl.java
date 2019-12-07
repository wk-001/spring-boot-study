package com.wk.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.business.entity.Goods;
import com.wk.business.mapper.GoodsMapper;
import com.wk.business.service.GoodsService;
import com.wk.business.vo.GoodsVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

	@Override
	public IPage<Goods> queryList(Page<Goods> page, GoodsVo goodsVo) {
		return getBaseMapper().queryList(page, goodsVo);
	}
}
