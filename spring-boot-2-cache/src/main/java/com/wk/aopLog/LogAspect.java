package com.wk.aopLog;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wk.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

//打印请求的控制器类名、方法名、传入的参数
@Aspect
//@Component
@Slf4j
public class LogAspect {

	@Autowired
	private ObjectMapper mapper;

	//配置aop切点
	@Pointcut("execution(* com.wk.service..*(..))")
	public void pointcutService(){}

	@Pointcut("execution(* com.wk.controller..*(..))")
	public void pointcutController(){}


	//记录对数据进行操作的用户名
	@Around("pointcutController()")
	public Object aroundControllerMethod(ProceedingJoinPoint pjd) throws JsonProcessingException {
		Object result = null;

		//获取request
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		//请求url
		String url = request.getRequestURI();
		//如果请求不为空并且是查询类请求就直接返回
		if(!StringUtils.isEmpty(url)){
			try {
				result =  pjd.proceed();
				if(url.contains("get")||url.contains("select")||url.contains("search")){
					return result;
				}
			} catch (Throwable throwable) {
				log.error(throwable.toString());
			}
		}

		log.debug("拦截了员工从操作,url是:"+url);
		log.info("返回的结果是"+result);

		String methodName = pjd.getSignature().getName();
		log.info("方法：" + methodName + " 开始执行，参数： " + Arrays.asList(pjd.getArgs()));
		log.info("方法：" + methodName + " 返回值： " + result);


		//请求参数列表
		String param = null;
		Map<String, String[]> parameterMap = request.getParameterMap();
		if(parameterMap!=null&& parameterMap.size()>0 ){
			param = mapper.writeValueAsString(parameterMap);
			if(!StringUtils.isEmpty(param)){
				log.info("请求中的参数param"+param);
			}
		}

		//对数据进行操作的人
		User user = (User)request.getSession().getAttribute("user");
		String userName = null;
		if(user!=null){
			userName = user.getUserName();
			log.info("操作人："+userName);
		}

		return result;
	}
	@Around("pointcutService()")
	public Object aroundServiceMethod(ProceedingJoinPoint pjd) {

		Object result = null;
		String methodName = pjd.getSignature().getName();

		try {
			//前置通知
			log.info("类名："+pjd.getTarget().getClass().getName());
			log.info("方法：" + methodName + " 开始执行，参数： " + Arrays.asList(pjd.getArgs()));
			//执行目标方法
			result = pjd.proceed();
			//返回通知
			log.info("方法：" + methodName + " 返回值： " + result);
		} catch (Throwable e) {
			//异常通知
			log.info("方法：" + methodName + "出现异常：" + e);
			throw new RuntimeException(e);
		}
		//后置通知
		log.info("方法：" + methodName + "执行完毕");

		return result;
	}
}
