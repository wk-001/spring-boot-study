package com.wk.sys.controller;

import com.wk.sys.common.ActiverUser;
import com.wk.sys.common.ResultObj;
import com.wk.sys.common.WebUtils;
import com.wk.sys.entity.Loginfo;
import com.wk.sys.service.LoginfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class LoginController {

    @Autowired
    private LoginfoService loginfoService;

    @RequestMapping("/")
    public String index(){
        return "system/index/login";
    }

    @RequestMapping("login")
    @ResponseBody
    public ResultObj login(String loginname,String pwd){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginname,pwd);
        try {
            subject.login(token);
            //获取UserRealm中登录认证成功放入SimpleAuthenticationInfo中的用户信息
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            //将用户信息放入session
            WebUtils.getSession().setAttribute("user",activerUser.getUser());
            //记录登录日志
            Loginfo loginfo = new Loginfo();
            loginfo.setLoginname(activerUser.getUser().getName()+"-"+activerUser.getUser().getLoginname());
            //获取IP地址
            loginfo.setLoginip(WebUtils.getRequest().getRemoteAddr());
            loginfo.setLogintime(new Date());
            loginfoService.save(loginfo);
            return ResultObj.LOGIN_SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResultObj.LOGIN_ERROR_PASS;
        }
    }


}
