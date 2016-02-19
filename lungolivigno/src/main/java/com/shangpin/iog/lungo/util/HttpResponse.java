package com.shangpin.iog.lungo.util;

import org.apache.commons.httpclient.Header;

public class HttpResponse
{
  private int status;
  private String response;
  private Header[] headers;
  private String sessionId;

  public HttpResponse()
  {
    this.status = 404;
  }

  public Header[] getHeaders()
  {
    return this.headers;
  }

  public void setHeaders(Header[] headers) {
    this.headers = headers;
  }

  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getResponse() {
    return this.response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public String getSessionId() {
    return this.sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}