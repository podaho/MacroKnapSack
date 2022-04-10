package com.pogrammerlin.macroknapsack.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import static com.pogrammerlin.macroknapsack.constant.Constants.FAT_SECRET_SERVER_NAME;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OAuthFeignClientConfiguration {
    private final OAuth2Provider oAuth2Provider;

    @Bean
    public RequestInterceptor fatSecretRequestInterceptor() {
        return (requestTemplate) ->
                requestTemplate.header(
                        HttpHeaders.AUTHORIZATION, oAuth2Provider.getAuthenticationToken(FAT_SECRET_SERVER_NAME));
    }
}
