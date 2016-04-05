package com.test;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class API extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "http://glamorous-staging.space48.com/glaadmin/oauth_authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_ENDPOINT = "http://glamorous-staging.space48.com/oauth/initiate";
    private static final String ACCESS_TOKEN_ENDPOINT = "http://glamorous-staging.space48.com/oauth/token";
	private static final String BASE_URL = "http://glamorous-staging.space48.com/";
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
