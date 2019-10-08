package com.wk.shiro;

import com.wk.pojo.User;
import com.wk.redis.RedisManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 自定义filter实现并发登录控制
 */
public class KickoutSessionControlFilter extends AccessControlFilter {


    //踢出后跳转的路径
    private String kickoutUrl;

    //踢出之前登录的/之后登录的用户，false：踢出之前登录的用户
    private boolean kickoutAfter = false;

    //同一个账号最大的会话数，默认1
    private int maxSession = 1;

    //session管理器
    private SessionManager sessionManager;

    private RedisManager redisManager;

    public static final String KICKOUT_CACHE_KEY_PREFIX = "shiro:cache:kickout:";

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    private String getRedisKickoutKey(String userName) {
        return this.KICKOUT_CACHE_KEY_PREFIX + userName;
    }

    /*是否允许访问，返回true表示允许访问
    * 如果返回 true，则表示“通过”，走到下一个过滤器。如果没有下一个过滤器的话，表示具有了访问某个资源的权限。
    * 如果返回 false，则会调用 onAccessDenied 方法，去实现相应过滤不通过的时候执行的操作，例如检查用户是否已经登陆过,
    * 如果登陆过,根据自定义规则选择踢出前一个用户 还是 后一个用户。
    */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 访问拒绝时是否自己处理，返回true表示自己不处理且继续拦截器链执行，返回false表示自己已经处理，比如重定向到另一个页面
     * 返回 true 表示 自己处理完成,然后继续拦截器链执行。
     * 只有当isAccessAllowed和onAccessDenied都返回false时,才会终止后面的filter执行。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        //如果没有登录，直接进入之后的流程
        if(!subject.isAuthenticated() && !subject.isRemembered()){
            return true;
        }

        Session session = subject.getSession();
        //如果在ShiroRealm.doGetAuthenticationInfo()中传入的是user对象，这里就获取user对象，如果传入的是userName，这里就获取userName

        User user = (User) subject.getPrincipal();
        String userName =user.getUserName();
        Serializable sessionId = session.getId();

        // 初始化用户的队列放到缓存里
        Deque<Serializable> deque = (Deque<Serializable>) redisManager.get(getRedisKickoutKey(userName));
        if(deque == null || deque.size()==0) {
            deque = new LinkedList<Serializable>();
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId=deque.getFirst();
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception
                e.printStackTrace();
            }
        }
        //登录缓存存在30分钟
        redisManager.set(getRedisKickoutKey(userName), deque,3600);

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) {
            }
            WebUtils.issueRedirect(request, response, kickoutUrl);
            return false;
        }
        return true;
    }

}
