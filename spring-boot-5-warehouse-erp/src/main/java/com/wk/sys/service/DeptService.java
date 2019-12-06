package com.wk.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wk.sys.common.DataGridView;
import com.wk.sys.entity.Dept;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
public interface DeptService extends IService<Dept> {

    int getMaxOrderNum();

	DataGridView deptTree();
}
