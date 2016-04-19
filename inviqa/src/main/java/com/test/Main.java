package com.test;

import java.net.URLEncoder;
import java.util.Scanner;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

public class Main {

	private static OAuthService service = null;
	private static Token requestToken = null; 
	
	private static final String BASE_URL = "http://www.glamorous.com/api/rest/shangpin/";
	public static void main(String[] args) {
//		fetchProductAndSave();
		final String MAGENTO_API_KEY = "drmuhfgxtg9uyfc0gd0zr9r9litnl1p6";
		final String MAGENTO_API_SECRET = "vqtk7uipwa67a9wde31k5db4k1qbeela";

		 service = new ServiceBuilder()
				.provider(MagentoThreeLeggedOAuth.class)
				.apiKey(MAGENTO_API_KEY)
				.apiSecret(MAGENTO_API_SECRET)
//				.debug()
				.build();
		
		
		Scanner in = new Scanner(System.in);
		System.out.println("Magento'srkflow");
		System.out.println();
		// Obtain the Request Token
		System.out.println("FetchingRequest Token...");
		 requestToken = service.getRequestToken();
		System.out.println("GotRequest Token!");
		System.out.println();
		
		
		
		System.out.println("FetchingAuthorization URL...");
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		System.out.println("GotAuthorization URL!");
		System.out.println("Nownd authorize Main here:");
		System.out.println(authorizationUrl);
		System.out.println("Ande the authorization code here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();
		
		
		System.out.println("TradingRequest Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("GotAccess Token!");
		System.out.println("(if curious it looks like this: "
				+ accessToken + " )");
		System.out.println();
		 
		
	}
    public static final class MagentoThreeLeggedOAuth extends DefaultApi10a {
		private static final String BASE = "http://www.glamorous.com/";
		
		
		@Override
		public String getRequestTokenEndpoint() {
			return BASE + "oauth/initiate";
		}

		@Override
		public String getAccessTokenEndpoint() {
			return BASE + "oauth/token";
		}
//http://admin.glamorous.com/gladmin9pUTvoBLqx7b0zhT/oauth_authorize?oauth_token=49w83p33u97yi3x18csvrul43ipwjor8
		@Override
		public String getAuthorizationUrl(Token requestToken) {
			return "http://www.glamorous.com/gladmin9pUTvoBLqx7b0zhT/oauth_authorize?oauth_token="
					+ requestToken.getToken(); 
		}

	}
}
