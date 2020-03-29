package com.mybiblestudywebapp.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybiblestudywebapp.dashboard.users.JwtToken;
import com.mybiblestudywebapp.utils.Constants;
import com.mybiblestudywebapp.utils.http.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/28/20
 */
@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.server.token-endpoint}")
    private String oauthEndpoint;

    @Value("${signing.key}")
    private String signingKey;

    public LoginResponse login(Map<String, String> headers, String username, HttpSession session) {
        LoginResponse loginResponse = new LoginResponse();
        String accessToken = getAccessToken(username, headers);
        String sql = "SELECT user_id FROM users WHERE email = :username";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", username);
        long userId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        session.setAttribute("userId", userId);
        session.setAttribute(Constants.ACCESS_TOKEN.toString(), accessToken);
        loginResponse.setUserId(userId);
        return loginResponse;
    }

    /**
     * Send login request to auth server and get the JWT
     * @param username
     * @param headers
     * @return
     */
    private String getAccessToken(String username, Map<String, String> headers) {

        // prepare headers
        HttpHeaders restHeaders = new HttpHeaders();
        String encodedAuth =
                Base64.getEncoder().encodeToString(String.format("%s:%s", clientId, clientSecret).getBytes());
        restHeaders.add("Authorization", String.format("Basic %s", encodedAuth));


        // prepare form data args
        String pass = decodePass(headers);
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> args = new LinkedMultiValueMap<>();
        args.add("grant_type", "password");
        args.add("scope", "webclient");
        args.add("username", username);
        args.add("password", pass);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(args, restHeaders);

        // send request
        ResponseEntity<String> result = restTemplate.exchange(oauthEndpoint, HttpMethod.POST, entity, String.class);

        return parseAccessToken(result.getBody());
    }

    private String decodePass(Map<String, String> headers) {

        headers.forEach((key, value) ->
                logger.debug(String.format("Header '%s' = %s", key, value)));
        String encodedAuthHeader = headers.get("authorization");/*.replace("Basic ", "");*/
        String base64Credentials = encodedAuthHeader.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        // credentials = username:password
        final String[] values = credentials.split(":", 2);
        return values[1];
    }

    /**
     *
     * @param token JWT returned from auth server
     * @return The access token that needs to be appended as Authorization: Bearer for further
     * requests
     */
    private String parseAccessToken(String token) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JwtToken jwtToken = mapper.readValue(token, JwtToken.class);
            return jwtToken.getAccess_token();
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return "";
        }

        /*try {
            Claims claims = Jwts.parser().setSigningKey(signingKey.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();

            String result = (String)claims.get("access_token");
            return result;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return "";
        }*/
    }



}
