package com.wk;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import net.sf.jsqlparser.statement.delete.Delete;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@MapperScan("com.wk.dao")
@SpringBootApplication
public class SpringBoot1MybatisPlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot1MybatisPlusApplication.class, args);
	}

	/**
	 * 分页插件
	 * 设置每页获取最大数据量(默认500)，具体数据量在selectPage()参数中的Page对象中设置
	 */
	/*@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setLimit(20);		//设置页面默认显示数据条数

		//攻击 SQL 阻断解析器，作用：阻止恶意的全表更新删除，开发环境使用
		List<ISqlParser> sqlParserList = new ArrayList<>();
		sqlParserList.add(new BlockAttackSqlParser() {
			@Override
			public void processDelete(Delete delete) {
				// 如果你想自定义做点什么，可以重写父类方法像这样子
				*//*if ("user".equals(delete.getTable().getName())) {
					// 自定义跳过某个表，其他关联表可以调用 delete.getTables() 判断
					return ;
				}*//*
				super.processDelete(delete);
			}
		});
		paginationInterceptor.setSqlParserList(sqlParserList);
		return paginationInterceptor;
	}*/

	/*	只配置分页插件*/
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/*乐观锁插件
	* 	当要更新一条记录的时候，希望这条记录没有被别人更新
		更新数据前对比数据版本号，每次更新版本号自增1
		注解实体字段必须加上 @Version 注解	*/
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}
}
