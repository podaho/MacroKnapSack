package com.pogrammerlin.macroknapsack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2Provider {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public String getAuthenticationToken(final String authZServerName) {
        final OAuth2AuthorizeRequest request =
                OAuth2AuthorizeRequest.withClientRegistrationId(authZServerName)
                        .principal(new AnonymousAuthenticationToken(
                                "key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")))
                        .build();
        return "Bearer " + authorizedClientManager.authorize(request).getAccessToken().getTokenValue();
    }
}
