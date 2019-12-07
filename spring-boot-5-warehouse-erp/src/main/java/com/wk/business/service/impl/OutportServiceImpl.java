package com.wk.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.business.entity.Goods;
import com.wk.business.entity.Inport;
import com.wk.business.entity.Outport;
import com.wk.business.mapper.GoodsMapper;
import com.wk.business.mapper.InportMapper;
import com.wk.business.mapper.OutportMapper;
import com.wk.business.service.OutportService;
import com.wk.business.vo.OutportVo;
import com.wk.sys.common.WebUtils;
import com.wk.sys.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
@Service
public class OutportServiceImpl extends ServiceImpl<OutportMapper, Outport> implements OutportService {

    @Resource
    private InportMapper inportMapper;

    @Resource
    private GoodsMapper goodsMapper;


    @Transactional
    @Override
    public void saveOutport(Inport inport) {
        //根据进货单ID查询进货单信息
        Inport inportInfo = inportMapper.selectById(inport.getId());
        //根据进货单信息查询商品信息
        Goods goods = goodsMapper.selectById(inportInfo.getGoodsid());
        //退货后更新商品数量
        goods.setNumber(goods.getNumber()-inport.getNumber());
        goodsMapper.updateById(goods);

        //添加退货单信息
        Outport outport = new Outport();
        outport.setGoodsid(goods.getId());
        outport.setNumber(inport.getNumber());
        User user = (User) WebUtils.getSession().getAttribute("user");
        outport.setOperateperson(user.getName());
        outport.setRemark(inport.getRemark());
        outport.setOutportprice(inportInfo.getInportprice());
        outport.setOutporttime(new Date());
        outport.setPaytype(inportInfo.getPaytype());
        outport.setProviderid(inportInfo.getProviderid());
        getBaseMapper().insert(outport);
    }

    @Override
    public IPage<Outport> queryList(Page<Outport> page, OutportVo outportVo) {
        return getBaseMapper().queryList(page, outportVo);
    }
}
