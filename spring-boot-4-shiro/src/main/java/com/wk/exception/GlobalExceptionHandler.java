package com.wk.exception;

//自定义全局异常捕获类
/*@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)*/
public class GlobalExceptionHandler  {

	//无访问权限跳转到403页面
//	@ExceptionHandler(AuthorizationException.class)
	public String handleAuthorizationException() {
		return "403";
	}

}
