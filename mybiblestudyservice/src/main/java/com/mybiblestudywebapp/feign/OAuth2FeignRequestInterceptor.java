package com.mybiblestudywebapp.feign;

import com.mybiblestudywebapp.utils.UserContext;
import com.mybiblestudywebapp.utils.UserContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextListener;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/26/20
 */
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_TOKEN_TYPE = "Bearer";

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2FeignRequestInterceptor.class);

    private final OAuth2ClientContext oauth2ClientContext;

    public OAuth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext) {
        Assert.notNull(oauth2ClientContext, "Context can not be null");
        this.oauth2ClientContext = oauth2ClientContext;
    }

    @Override
    public void apply(RequestTemplate template) {
        LOGGER.debug("Inside apply method for feign");

        UserContext context = UserContextHolder.getContext();
        String authHeader = context.getAuthToken();

        //template.header("Authorization", authHeader);
    }
}
