package com.mybiblestudywebapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/17/20
 */
@Component
public class UserContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String accessToken =
                (String) ((HttpServletRequest) servletRequest).getSession().getAttribute(Constants.ACCESS_TOKEN.toString());


        UserContextHolder
                .getContext()
                .setUserId(httpServletRequest
                        .getHeader(Constants.USER_ID.toString()));

        UserContext context = UserContextHolder.getContext();

        UserContextHolder
                .getContext()
                .setAuthToken(/*httpServletRequest
                        .getHeader(Constants.AUTH_TOKEN.toString())*/"Bearer " + accessToken);

        UserContextHolder
                .getContext()
                .setCorrelationId(httpServletRequest
                .getHeader(Constants.CORRELATION_ID.toString()));

        logger.debug("UserContextFilter Auth token: {}", UserContextHolder.getContext().getAuthToken());
        logger.debug("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}




}
