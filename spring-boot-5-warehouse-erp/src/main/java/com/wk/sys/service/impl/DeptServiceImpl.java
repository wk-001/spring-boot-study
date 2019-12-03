package com.wk.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.sys.entity.Dept;
import com.wk.sys.mapper.DeptMapper;
import com.wk.sys.service.DeptService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@CacheConfig(cacheNames = "dept")       //抽取缓存公共配置 指定统一缓存组件名称；和value作用一样
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    /**
     * @Cacheable 将方法的运行结果进行缓存，需要相同的数据直接取缓存中取，避免重复查库
     */
    @Cacheable
    @Override
    public Dept getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * @CachePut 缓存的是方法的返回值 每次都会真实调用函数
     * 缓存默认key是参数；查询方法的参数是id，修改方法的参数是对象 为了避免取到的缓存不一致的问题，统一指定key
     */
    @CachePut(key = "#eneity.id")
    @Override
    public boolean updateById(Dept entity) {
        return super.updateById(entity);
    }

    /**
     * @CachePut 缓存的是方法的返回值 每次都会真实调用函数
     * 缓存默认key是参数；查询方法的参数是id，修改方法的参数是对象 为了避免取到的缓存不一致的问题，统一指定key
     */
    @CachePut(key = "#eneity.id")
    @Override
    public boolean save(Dept entity) {
        return super.save(entity);
    }

    /**
     *
     * @CacheEvict 清除缓存
     * beforeInvocation = true：方法执行之前清除缓存，无论方法是否执行成功都会清除缓存
     * 默认是在方法执行之后清除缓存，如果方法异常，缓存不会清除
     */
    @CacheEvict(beforeInvocation = true)
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public int getMaxOrderNum() {
        int maxOrderNum = this.getBaseMapper().getMaxOrderNum();
        if (maxOrderNum>0){
            return maxOrderNum+1;
        }else {
           return 1;
        }
    }
}
