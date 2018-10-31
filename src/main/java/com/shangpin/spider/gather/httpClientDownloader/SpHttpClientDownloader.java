package com.shangpin.spider.gather.httpClientDownloader;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverPool;
import com.shangpin.spider.gather.utils.DownloaderUtils;
import com.shangpin.spider.gather.utils.GatherUtil;
import com.shangpin.spider.gather.utils.UserAgentUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.HttpClientRequestContext;
import us.codecraft.webmagic.downloader.HttpUriRequestConverter;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.CharsetUtils;
import us.codecraft.webmagic.utils.HttpClientUtils;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


/**
 * The http downloader based on HttpClient.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class SpHttpClientDownloader extends AbstractDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private SpHttpClientGenerator httpClientGenerator = new SpHttpClientGenerator();

    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();
    
    private ProxyProvider proxyProvider;

    private boolean responseHeader = true;
    
    private SpiderRules spiderRuleInfo;
    
    private SpChromeDriverPool pool;
    
    private static volatile Boolean clientFlag = true;
    
    public SpHttpClientDownloader(SpiderRules spiderRuleInfo, SpChromeDriverPool pool) {
		super();
		this.spiderRuleInfo = spiderRuleInfo;
		this.pool = pool;
	}

	public void setHttpUriRequestConverter(HttpUriRequestConverter httpUriRequestConverter) {
        this.httpUriRequestConverter = httpUriRequestConverter;
    }

    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }

    private CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return httpClientGenerator.getClient(null);
        }
        String domain = site.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    @Override
    public Page download(Request request, Task task) {
    	
    	String url = request.getUrl();
    	if(spiderRuleInfo!=null) {
			if (GatherUtil.isLieUrl(url, spiderRuleInfo)&&spiderRuleInfo.getLieAjaxFlag()) {
				Page page = null;
		    	RemoteWebDriver webDriver = null;
				Boolean uniqueFlag = true;
				return DownloaderUtils.driverNextPage(url, webDriver, spiderRuleInfo, page, request, task, uniqueFlag, pool);
			}
    	}
    	if(!clientFlag) {
//    		jsoup的处理，默认失败重试3次
    		Page page = handleByJsoup(url, task.getSite(), request);
    		if(page.getStatusCode()==404) {
    			int i = 1;
    			while(i<3) {
    				page = handleByJsoup(url, task.getSite(), request);
    				i++;
    				if(page.getStatusCode()==200) {
    					break;
    				}
    			}
    		}
    		return page;
    	}
    	
        if (task == null || task.getSite() == null) {
            throw new NullPointerException("task or site can not be null");
        }
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = getHttpClient(task.getSite());
        Proxy proxy = proxyProvider != null ? proxyProvider.getProxy(task) : null;
        HttpClientRequestContext requestContext = httpUriRequestConverter.convert(request, task.getSite(), proxy);
        Page page = Page.fail();
        try {
            httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());
            page = handleResponse(request, request.getCharset() != null ? request.getCharset() : task.getSite().getCharset(), httpResponse, task);
            onSuccess(request);
            logger.info("downloading page success {}", request.getUrl());
            return page;
        } catch (IOException e) {
        	clientFlag = false;
        	page = handleByJsoup(url, task.getSite(), request);
        	logger.warn("download page {} error", request.getUrl(), e);
        	onError(request);
        } finally {
            if (httpResponse != null) {
                //ensure the connection is released back to pool
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
            if (proxyProvider != null && proxy != null) {
                proxyProvider.returnProxy(proxy, page, task);
            }
        }
        return page;
    }
    
    private Page handleByJsoup(String url, Site site, Request request) {
    	Connection connect = Jsoup.connect(url);
    	connect.timeout(site.getTimeOut());
    	connect.userAgent(UserAgentUtil.getUserAgent());
    	Page page = new Page();
    	page.setUrl(new PlainText(url));
    	try {
			Document document = connect.get();
			String content = document.html().toString();
			page.setRawText(content);
			page.setHtml(new Html(content, url));
			page.setRequest(request);
			page.setDownloadSuccess(true);
		} catch (IOException e) {
			page.setStatusCode(404);
			logger.warn("---jsoup download page {} error", url, e);
		}
    	return page;
    }
    
    @Override
    public void setThread(int thread) {
        httpClientGenerator.setPoolSize(thread);
    }

    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task) throws IOException {
        byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();
        Page page = new Page();
        page.setBytes(bytes);
        if (!request.isBinaryContent()){
            if (charset == null) {
                charset = getHtmlCharset(contentType, bytes);
            }
            page.setCharset(charset);
            page.setRawText(new String(bytes, charset));
        }
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        page.setDownloadSuccess(true);
        if (responseHeader) {
            page.setHeaders(HttpClientUtils.convertHeaders(httpResponse.getAllHeaders()));
        }
        return page;
    }

    private String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
        String charset = CharsetUtils.detectCharset(contentType, contentBytes);
        if (charset == null) {
            charset = Charset.defaultCharset().name();
            logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
        }
        return charset;
    }
}
