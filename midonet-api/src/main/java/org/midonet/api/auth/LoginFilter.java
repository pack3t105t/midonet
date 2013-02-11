/*
 * Copyright 2013 Midokura PTE LTD.
 */
package org.midonet.api.auth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.codec.binary.Base64;
import org.midonet.api.HttpSupport;
import org.midonet.api.rest_api.ResponseUtils;
import org.midonet.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter to authenticate a user with username and password
 */
@Singleton
public class LoginFilter implements Filter {

    private final static Logger log = LoggerFactory.getLogger(
            LoginFilter.class);

    protected ServletContext servletContext;

    @Inject
    private AuthService service;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("LoginFilter.init: entered.");
        servletContext = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        log.debug("LoginFilter.doFilter: entered.");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Get the Authorization header. 'getHeader' is case insensitive
        String authorization = request.getHeader("authorization");
        if (StringUtil.isNullOrEmpty(authorization)) {
            ResponseUtils.setAuthErrorResponse(response,
                    "Authorization header is not set.");
            return;
        }

        // Support only Basic
        if (!authorization.toLowerCase().startsWith(
                HttpSupport.BASIC_AUTH_PREFIX.toLowerCase())) {
            ResponseUtils.setAuthErrorResponse(response,
                    "Authorization header does not contain Basic.");
            return;
        }

        // Get the base64 portion
        String credentialsEnc = authorization.substring(
                HttpSupport.BASIC_AUTH_PREFIX.length());

        // Decode base64
        String credentials = new String(Base64.decodeBase64(credentialsEnc
                .getBytes()));

        // Get the username/password
        String[] credList = credentials.split(":");
        if (credList == null || credList.length != 2) {
            ResponseUtils.setAuthErrorResponse(response,
                    "Authorization header is not valid");
            return;
        }

        try {
            Token token = service.login(credList[0], credList[1], request);
            if (token != null) {
                // Set the Cookie
                ResponseUtils.setCookie(response, token.getKey(),
                        token.getExpires());
            } else {
                ResponseUtils.setAuthErrorResponse(response, "Login failed");
            }
        } catch (AuthException ex) {
            ResponseUtils.setAuthErrorResponse(response, ex.getMessage());
            log.error("LoginFilter: auth error occurred. ", ex);
        }

        log.debug("LoginFilter.doFilter: exiting.");
    }

    @Override
    public void destroy() {
    }
}
