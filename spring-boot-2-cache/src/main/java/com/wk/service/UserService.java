package com.wk.service;

import com.wk.pojo.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
@CacheConfig(cacheNames = "user")  //抽取缓存公共配置 指定统一缓存组件名称
public interface UserService {

	List<User> selectAll();


	/**
	 * @Cacheable将方法的运行结果进行缓存，需要相同的数据直接取缓存中取，避免重复查库
	 * CacheManager管理多个cache组件，对缓存的真正CRUD操作在cache组件中，每个缓存组件有自己唯一的名字
	 * 		@CacheConfig/value：指定缓存组件的名字,将方法的返回值放入那个缓存中，可以指定多个缓存.如果两者同时存在，则以value为准
	 * 		key：指定缓存数据的key，key默认是按照函数的所有参数组合作为key值若自己配置需使用SpEL表达式，
	 * 		比如：@Cacheable(key = "#p0")：使用函数第一个参数作为缓存的key值
	 *
	 * 		keyGenerator：key生成器，可以自己指定key生成器的组件ID。需要自己手动实现
	 * 		org.springframework.cache.interceptor.KeyGenerator接口，并使用该参数来指定；	和key二选一
	 *
	 * 		CacheManager：指定缓存管理器
	 * 		condition：符合指定条件才缓存
	 * 			#id>0：id>0时缓存
	 * 			#a0>1：第一个参数的值大于1时缓存
	 * 		unless：该条件是在函数被调用之后才做判断的，所以它可以通过对result进行判断；符合指定条件不缓存，可以获取到结果进行判断，结果不为null时缓存
	 *  自动配置类：CacheAutoConfiguration
	 *  	默认生效配置类：SimpleCacheConfiguration在容器中注册了一个ConcurrentMapCacheManager
	 *		可以获取和创建ConcurrentMapCacheManager类型的缓存组件，将数据保存在ConcurrentMap中
	 *	运行流程：
	 *	1.方法运行前，先去查询cache缓存组件，按照value指定的名字获取数据，(CacheManager先获取相应的缓存)
	 *		第一次获取缓存如果没有cache组件会自动创建
	 *	2.去cache中查找缓存的内容，key默认是方法的参数，生成策略是keyGenerator
	 *		如果没有参数 key = new simpleKey();
	 *		有一个参数：key=参数值
	 *		有多个参数：key = new simpleKey(params);
	 *  3.如果没有查到缓存就调用目标方法
	 *  4.将目标方法返回的结果放入缓存
	 *  总结：
	 *  	标注@Cacheable注解的方法在执行前先检查缓存中是否有对应数据，没有就调用目标方法并将返回值放入缓存
	 *  	缓存的key默认是方法的参数
	 *  核心：
	 *  	1.使用CacheManager(默认是ConcurrentMapCacheManager)按照名字获取cache(ConcurrentMapCache)组件
	 */
	@Cacheable(key = "#id",/*value = "emp",*/condition = "#id>0",unless = "#result==null")
	User getById(int id);


	/**
	 * @CachePut：调用方法同时更新缓存数据
	 * 	适用场景：修改数据库的数据，同时更新缓存
	 * 	能够根据参数定义条件来进行缓存，其缓存的是方法的返回值，它与@Cacheable不同的是，
	 * 	它每次都会真实调用函数，所以主要用于数据新增和修改操作上。它的参数与@Cacheable类似
	 *
	 * 	更新数据的时候，使用@CachePut(key="#p0.sno")进行缓存数据的更新，
	 * 	否则将查询到脏数据，因为该注解保存的是方法的返回值，所以这里应该返回user对象。
	 * 	运行流程：
	 * 		1.先调用目标方法
	 * 		2.将目标方法的结果放入缓存
	 * 	测试步骤：
	 * 		1.查询1号员工，查询到的结果会放入缓存
	 * 		2.更新1号员工数据，并将更新的数据放入缓存
	 * 		3.再次查询1号员工，发现查询到的数据是更新前的数据
	 * 			原因：缓存的key默认是参数的值，查询方法的参数是ID，而更新方法的参数是对象
	 * 			查询方法：key：1，value：user对象
	 * 			更新方法：key：user对象，value：user对象
	 * 			更新的是key为user对象的值，所以无法获取更新后的数据
	 * 		解决方法：统一指定key
	 * 			key = "#user.id"：传入参数的ID
	 * 			key = "#result.id"：使用返回值的ID，此处返回的是传入的参数，所以id一样
	 */
	@CachePut(key = "#user.id")
	User updUser(User user);

	/**
	 * @CacheEvict：清除缓存
	 * 	key：指定要清除的数据，默认是参数的值
	 * 	allEntries = true：删除所有缓存
	 * 	beforeInvocation = true：方法执行之前清除缓存，无论方法是否执行成功都会清除缓存
	 * 		默认false是在方法执行之后清除缓存，如果方法异常，缓存不会清除
	 * @param id
	 */
	@CacheEvict
	void delUser(int id);
}
