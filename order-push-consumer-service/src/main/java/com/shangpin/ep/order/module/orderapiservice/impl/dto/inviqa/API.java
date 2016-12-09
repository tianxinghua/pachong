package com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class API extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "http://www.glamorous.com/gladmin9pUTvoBLqx7b0zhT/oauth_authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_ENDPOINT = "http://www.glamorous.com/oauth/initiate";
    private static final String ACCESS_TOKEN_ENDPOINT = "http://www.glamorous.com/oauth/token";

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_ENDPOINT;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
