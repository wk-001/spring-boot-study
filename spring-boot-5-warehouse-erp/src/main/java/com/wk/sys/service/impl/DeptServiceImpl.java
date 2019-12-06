package com.wk.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.sys.common.DataGridView;
import com.wk.sys.common.TreeNode;
import com.wk.sys.entity.Dept;
import com.wk.sys.mapper.DeptMapper;
import com.wk.sys.service.DeptService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public int getMaxOrderNum() {
        int maxOrderNum = this.getBaseMapper().getMaxOrderNum();
        if (maxOrderNum>0){
            return maxOrderNum+1;
        }else {
           return 1;
        }
    }

    @Cacheable(cacheNames = "deptTree")
	@Override
	public DataGridView deptTree() {
        List<Dept> list = getBaseMapper().selectList(null);
        List<TreeNode> nodes = new ArrayList<>();
        for (Dept dept : list) {
            nodes.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),dept.getOpen()==1));
        }
        return new DataGridView(nodes);
	}
}
