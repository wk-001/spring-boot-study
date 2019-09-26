package com.wk.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Date;

/**
 * 过滤器，拦截所有请求，启动Tomcat查看效果
 * 通过过滤器我们只可以获取到servletRequest对象，所以并不能获取到方法的名称，所属类，参数等额外的信息。
 * 参考页面：https://mrbird.cc/Spring-Boot-Filter-Interceptor.html
 * @Component注解让TimeFilter成为Spring上下文中的一个Bean，
 * @WebFilter注解的urlPatterns属性配置了哪些请求可以进入该过滤器，/*表示所有请求。
 */
@Component
@WebFilter(urlPatterns = {"/*"})
@Slf4j
public class TimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("开始执行过滤器");
        Long start = new Date().getTime();
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("【过滤器】耗时 " + (new Date().getTime() - start));
        log.info("结束执行过滤器");
    }

    @Override
    public void destroy() {
        log.info("过滤器销毁");
    }
}
