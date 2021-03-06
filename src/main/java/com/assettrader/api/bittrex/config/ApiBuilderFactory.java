package com.assettrader.api.bittrex.config;

import javax.annotation.Nullable;

import com.assettrader.api.bittrex.util.ApiKeySigningUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Feign;
import feign.RequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author contact@elbatya.de
 */
public class ApiBuilderFactory {

    private String baseUrl;

    public ApiBuilderFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Feign.Builder createApiBuilder(){
        return createApiBuilder(null);
    }


    public Feign.Builder createApiBuilder(@Nullable ApiCredentials credentials){
        ObjectMapper mapper = ObjectaMapperConfigurer.configure(new ObjectMapper());

        Feign.Builder apiBuilder = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder(mapper));

        signRequestsIfCredentialsNotNull(apiBuilder, credentials);

        return apiBuilder;
    }

    private void signRequestsIfCredentialsNotNull(Feign.Builder builder, @Nullable ApiCredentials credentials){
        if (credentials != null) {
            RequestInterceptor signAllRequests = requestTemplate -> {

                requestTemplate.query("apikey", credentials.getKey());
                requestTemplate.query("nonce", ApiKeySigningUtil.createNonce());

                String requestUrl = baseUrl+requestTemplate.request().url();
                String sign = ApiKeySigningUtil.createSign(requestUrl, credentials.getSecret());
                requestTemplate.header("apisign", sign);
            };

            builder.requestInterceptor(signAllRequests);
        }
    }
    
}
