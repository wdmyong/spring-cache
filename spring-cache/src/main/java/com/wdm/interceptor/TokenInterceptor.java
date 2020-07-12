package com.wdm.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wdm.enums.ClientType;
import com.wdm.utils.LoginUserIdContextHolder;
import com.wdm.utils.TokenUtils;

/*
 * @author wdmyong
 * 2020-07-09
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
    private static final Set<String> NO_NEED_LOGIN_URIS = new HashSet<>();

    static {
        NO_NEED_LOGIN_URIS.add("/");
        NO_NEED_LOGIN_URIS.add("/web/login");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.preHandle(request, response, handler);

        ClientType clientType = ClientType.fromValue(NumberUtils.toInt(request.getParameter("clientType")));
        String uri = request.getRequestURI();
        String token = request.getParameter("token");

        logger.info("client:{} uri:{} token:{}", clientType, uri, token);
        if (!NO_NEED_LOGIN_URIS.contains(uri) && StringUtils.isBlank(token)) {
            // web页面重定向到首页
            if (clientType == ClientType.WEB) {
                response.sendRedirect("/");
                return false;
            }
        }

        long userId = 0L;
        if (StringUtils.isNotBlank(token)) {
            userId = TokenUtils.getUserIdByToken(token);
        }
        LoginUserIdContextHolder.setUserId(userId);
        return true;
    }
}
