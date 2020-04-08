package com.mybiblestudywebapp.feign;

import com.mybiblestudywebapp.utils.UserContext;
import com.mybiblestudywebapp.utils.UserContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Assert;

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

        template.header("Authorization", authHeader);

       /* AccessTokenRequest accessTokenRequest = oauth2ClientContext.getAccessTokenRequest();
        OAuth2AccessToken oAuth2AccessToken = accessTokenRequest.getExistingToken();

        String authHeader = String.valueOf(template.headers().get(AUTHORIZATION_HEADER));
        template.header(AUTHORIZATION_HEADER, authHeader);


        if (template.headers().containsKey(AUTHORIZATION_HEADER)) {
            LOGGER.warn("The Authorization token has been already set");
        } else if (oAuth2AccessToken == null) {
            LOGGER.warn("Can not obtain existing token for request, if it is a non secured request, ignore.");
        } else {
            LOGGER.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE,
                    oAuth2AccessToken.toString()));
        }*/
    }
}
