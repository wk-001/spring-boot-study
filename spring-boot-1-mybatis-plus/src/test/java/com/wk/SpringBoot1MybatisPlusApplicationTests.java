package com.wk;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.pojo.User;
import com.wk.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.*;

/**
 * SpringBoot整合mybatisPlus
 * 参考页面：https://mp.baomidou.com/guide/
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoot1MybatisPlusApplicationTests {

	@Autowired
	private UserService userService;

	//使用map添加条件查询
	@Test
	public void contextLoads() {
		/* 查询所有
		List<User> list = userService.list();
		list.forEach(a-> System.out.println("a = " + a));*/

		//根据数据库字段进行查询
		Map<String,Object> map = new HashMap<>();
		map.put("name","张三");
		Collection<User> users = userService.listByMap(map);
		users.forEach(a-> System.out.println("a = " + a));
	}

	//添加；MyBatisPlus完成插入数据操作后自动将主键返回到对象中
	@Test
	public void addUser(){
		User user = new User("张三",25,1,"1234@qq.com");
		userService.save(user);
		System.out.println("user = " + user.getId());
	}

	//	分页
	@Test
	public void pageTest(){
		Page<User> page = new Page<>(0, 2);
		//查询结果会放到page对象中
		IPage<User> pages = userService.page(page);
		/*查询出的分页数据*/
		List<User> records = pages.getRecords();
		for (User record : records) {
			System.out.println("record = " + record);
		}
		/*------------------分页信息--------------------------*/
		System.out.println("数据总数 = " + page.getTotal());
		System.out.println("当前页码 = " + page.getCurrent());
		System.out.println("总页数 = " + page.getPages());
		System.out.println("每页数据条数 = " + page.getSize());
		System.out.println("是否有上一页 = " + page.hasPrevious());
		System.out.println("是否有下一页 = " + page.hasNext());
		//将查询结果封装到page对象中
		page.setRecords(records);
	}

	//全表删除测试，配置SQL阻断器后删除全表会报错
	@Test
	public void delAll(){
		userService.remove(null);
	}

	//条件构造器；,查询条件的字段是数据库字段，不是实体类属性！！！！
	@Test
	public void conditionTest(){
		/*分页查询年龄在18~30之间并且名字是tom的人
		QueryWrapper<User> wrapper = new QueryWrapper<User>()
				.between("age", 18, 30)
				.eq("user_name","tom");
		IPage<User> pages = userService.page(new Page<>(0, 2), wrapper);
		pages.getRecords().forEach(System.out::println);*/

		/*查询性别是女(0)，名字带有a或者邮箱带有qq的人*/
		QueryWrapper<User> wrapper = new QueryWrapper<User>()
				.eq("gender",0)
				//嵌套查询
				.nested(i->i.like("user_name","a").or().like("email","qq"));

				 /*相当于普通查询，不带括号
				.like("user_name","a")   //eq和like之间是and连接
				.or()	//默认是and连接，or要显式的指定 两个like之间是or连接
				.like("email","qq")	;*/
		userService.list(wrapper).forEach(System.out::println);
	}

	//AR模式 实体类继承Model<T>进行CRUD操作，本质还是调用mapper接口对象的对应方法
	@Test
	public void testAR() throws ParseException {
		User user = new User();
		/*添加
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1988-10-1");
		User user = new User("李四",30,1,"abc@163.com",date);
		user.insert();
		System.out.println("user = " + user.getId());*/

		/*根据ID查询
		User user1 = user.selectById(5);
		System.out.println("user1 = " + user1);*/

		/*查询所有
		List<User> users = user.selectAll();
		users.forEach(System.out::println);*/

		/*条件查询
		List<User> users = user.selectList(
				new QueryWrapper<User>()
						.eq("gender", 0)
						.lt("age",25)
		);
		users.forEach(System.out::println);*/

		/*根据ID删除的两种方法
		user.setId(7);
		boolean result = user.deleteById();
		boolean result = user.deleteById(7);
		System.out.println("result = " + result);*/
	}

	/*乐观锁,实体类必须加@Version注解才能实现。
	版本号与数据库一样才进行更新操作
	更新失败不会报错，数据库数据也不会更新*/
	@Test
	public void happyLock(){
		User user = new User(6,"fasdfd",22,4);
		userService.updateById(user);
	}

	//逻辑删除测试；使用mp自带方法删除和查找都会附带逻辑删除功能 (自己写的xml不会)
	@Test
	public void logicDelete(){
//		userService.removeById(5);
		userService.getById(5);     //自带方法查不到被逻辑删除的数据
	}
}
