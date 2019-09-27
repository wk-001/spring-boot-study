package com.wk.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component      //使本类成为Spring上下文中的一个Bean
public class TimeInterceptor implements HandlerInterceptor {

    /*处理拦截之前执行
    * 预处理回调方法，实现处理器的预处理
    * 返回值：true表示继续流程；false表示流程中断，不会继续调用其他的拦截器或处理器*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        log.info("处理拦截之前");
        request.setAttribute("startTime", new Date().getTime());
        log.info("类名"+((HandlerMethod) o).getBean().getClass().getName());
        log.info("方法名"+((HandlerMethod) o).getMethod().getName());
        ObjectMapper mapper = new ObjectMapper();

        //获取restful风格请求的所有参数
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String pathVariable = mapper.writeValueAsString(pathVariables);
        log.info("restful请求的参数:"+pathVariable);

        /* 获取非restful风格请求的所有参数
        Map ParameterMap =  request.getParameterMap();
        Map reqMap = new HashMap();
        Set<Map.Entry<String,String[]>> entry = ParameterMap.entrySet();
        Iterator<Map.Entry<String,String[]>> it = entry.iterator();
        while (it.hasNext()){
            Map.Entry<String,String[]>  me = it.next();
            String key = me.getKey();
            String value = me.getValue()[0];
            reqMap.put(key,value);
        }
        String queryString = mapper.writeValueAsString(reqMap);
        log.info("请求参数："+queryString);*/

        return true;
    }

    /*只有当被拦截的方法没有抛出异常成功时才会处理
     * 后处理回调方法，实现处理器（controller）的后处理，但在渲染视图之前
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理*/
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("开始处理拦截");
        Long start = (Long) httpServletRequest.getAttribute("startTime");
        log.info("【拦截器】耗时 " + (new Date().getTime() - start));
    }

    /*无论被拦截的方法是否抛出异常都会执行
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，
     * 但仅调用处理器执行链中*/
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("处理拦截之后");
        Long start = (Long) httpServletRequest.getAttribute("startTime");
        log.info("【拦截器】耗时 " + (new Date().getTime() - start));
        log.info("异常信息 " + e);
    }


}
