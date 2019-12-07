package com.wk.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Goods;
import com.wk.business.entity.Inport;
import com.wk.business.mapper.GoodsMapper;
import com.wk.business.mapper.InportMapper;
import com.wk.business.service.InportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.business.vo.InportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
@Service
public class InportServiceImpl extends ServiceImpl<InportMapper, Inport> implements InportService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public IPage<Inport> queryList(Page<Inport> page, InportVo inportVo) {
        return getBaseMapper().queryList(page, inportVo);
    }

    @Transactional
    @Override
    public void saveInport(Inport inport) {
        //保存进货信息
        getBaseMapper().insert(inport);
        //修改商品数量
        Goods goods = goodsMapper.selectById(inport.getGoodsid());
        goods.setNumber(goods.getNumber()+inport.getNumber());
        goodsMapper.updateById(goods);

    }

    @Override
    public void updateInport(Inport inport) {
        //根据ID查询未修改的进货信息
        Inport inport1 = getBaseMapper().selectById(inport.getId());
        //根据商品ID查询商品信息
        Goods goods = goodsMapper.selectById(inport.getGoodsid());
        //修改库存算法：当前库存-库存信息修改前的数量+库存信息修改后的数量
        int count = goods.getNumber()-inport1.getNumber()+inport.getNumber();
        goods.setNumber(count);
        goodsMapper.updateById(goods);
        getBaseMapper().updateById(inport);
    }

    @Override
    public void removeInport(Integer id) {
        //根据ID查询未修改的进货信息
        Inport inport = getBaseMapper().selectById(id);
        //根据商品ID查询商品信息
        Goods goods = goodsMapper.selectById(inport.getGoodsid());
        //修改库存算法：当前库存-进货单数量
        int count = goods.getNumber()-inport.getNumber();
        goods.setNumber(count);
        goodsMapper.updateById(goods);
        getBaseMapper().deleteById(id);
    }
}
