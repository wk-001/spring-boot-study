package com.wk.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @description: 解决单次请求需要多次访问redis
 */
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {

    /**
     * 获取session
     * 优化单次请求需要多次访问redis的问题
     * 直接把 session 对象怼进 request 里去！那么在单次请求周期内我们都可以
     * 从 request 中取 session 了，而且请求结束后 request 被销毁，作用域和生命周期的
     * 问题都不需要我们考虑了。
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);

        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }

        if (request != null && null != sessionId) {
            Object sessionObj = request.getAttribute(sessionId.toString());
            if (sessionObj != null) {
                log.debug("read session from request");
                return (Session) sessionObj;
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if (request != null && null != sessionId) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
