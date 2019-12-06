package com.wk.business.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wk.business.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
public interface GoodsMapper extends BaseMapper<Goods> {

	IPage<Goods> queryList(Page<Goods> page, @Param("goods") GoodsVo goodsVo);
}
