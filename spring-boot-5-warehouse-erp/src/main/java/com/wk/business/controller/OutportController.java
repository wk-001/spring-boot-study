package com.wk.business.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.business.entity.Inport;
import com.wk.business.entity.Outport;
import com.wk.business.service.OutportService;
import com.wk.business.vo.OutportVo;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/outport")
public class OutportController {

    @Autowired
    private OutportService outportService;

    /**
     * 带条件查询所有进货商品
     */
    @RequestMapping("list")
    public DataGridView list(OutportVo outportVo){
        Page<Outport> page = new Page<>(outportVo.getPage(), outportVo.getLimit());
        outportService.queryList(page, outportVo);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加退货信息
     */
    @RequestMapping("save")
    public ResultObj save(Inport inport){
        try {
            outportService.saveOutport(inport);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

}

