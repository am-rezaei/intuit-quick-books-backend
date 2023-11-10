package com.intuit.developer.helloworld.controller;

import com.intuit.developer.helloworld.client.OAuth2PlatformClientFactory;
import com.intuit.developer.helloworld.model.UserCredential;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.config.OAuth2Config;
import com.intuit.oauth2.config.Scope;
import com.intuit.oauth2.data.BearerTokenResponse;
import com.intuit.oauth2.exception.InvalidRequestException;
import com.intuit.oauth2.exception.OAuthException;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    final OAuth2PlatformClientFactory factory;
    static HashMap<String, String> requestIds = new HashMap<>();
    private static final Logger logger = Logger.getLogger(AuthorizationController.class);

    @RequestMapping("/getToken")
    public UserCredential callBackFromOAuth(@RequestParam("code") String authCode,
                                            @RequestParam("state") String state,
                                            @RequestParam("unique_request_id") String uniqueRequestId,
                                            @RequestParam("realmId") String realmId
    ) {
        try {
            String csrfToken = requestIds.get(uniqueRequestId);
            if (csrfToken.equals(state)) {
                OAuth2PlatformClient client = factory.getOAuth2PlatformClient();
                String redirectUri = factory.getPropertyValue("OAuth2AppRedirectUri");
                BearerTokenResponse tokenResponse = client.retrieveBearerTokens(authCode, redirectUri);
                return new UserCredential(tokenResponse.getAccessToken(), realmId);
            }
            logger.info("csrf token mismatch ");
        } catch (OAuthException e) {
            logger.error("Exception in callback handler ", e);
        }
        return null;
    }


    @RequestMapping("/getOAuthUrl")
    public List<String> connectToQuickbooks() {
        List<String> res = new ArrayList<>();
        OAuth2Config oauth2Config = factory.getOAuth2Config();
        String redirectUri = factory.getPropertyValue("OAuth2AppRedirectUri");
        String csrf = oauth2Config.generateCSRFToken();
        String uniqueRequestId = UUID.randomUUID().toString();
        try {
            List<Scope> scopes = new ArrayList<>();
            scopes.add(Scope.Accounting);
            res.add(oauth2Config.prepareUrl(scopes, redirectUri, csrf));
            res.add(uniqueRequestId);
            requestIds.put(uniqueRequestId, csrf);
            return res;
        } catch (InvalidRequestException e) {
            logger.error("Exception calling connectToQuickbooks ", e);
        }
        return null;
    }

}
