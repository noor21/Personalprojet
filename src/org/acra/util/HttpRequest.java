package org.acra.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.log.ACRALog;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public final class HttpRequest
{
  private int connectionTimeOut = 3000;
  private Map<String, String> headers;
  private String login;
  private int maxNrRetries = 3;
  private String password;
  private int socketTimeOut = 3000;
  
  private UsernamePasswordCredentials getCredentials()
  {
    UsernamePasswordCredentials localUsernamePasswordCredentials;
    if ((this.login == null) && (this.password == null)) {
      localUsernamePasswordCredentials = null;
    } else {
      localUsernamePasswordCredentials = new UsernamePasswordCredentials(this.login, this.password);
    }
    return localUsernamePasswordCredentials;
  }
  
  private HttpClient getHttpClient()
  {
    BasicHttpParams localBasicHttpParams = new BasicHttpParams();
    localBasicHttpParams.setParameter("http.protocol.cookie-policy", "rfc2109");
    HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, this.connectionTimeOut);
    HttpConnectionParams.setSoTimeout(localBasicHttpParams, this.socketTimeOut);
    HttpConnectionParams.setSocketBufferSize(localBasicHttpParams, 8192);
    Object localObject = new SchemeRegistry();
    ((SchemeRegistry)localObject).register(new Scheme("http", new PlainSocketFactory(), 80));
    if (!ACRA.getConfig().disableSSLCertValidation()) {
      ((SchemeRegistry)localObject).register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
    } else {
      ((SchemeRegistry)localObject).register(new Scheme("https", new FakeSocketFactory(), 443));
    }
    localObject = new DefaultHttpClient(new ThreadSafeClientConnManager(localBasicHttpParams, (SchemeRegistry)localObject), localBasicHttpParams);
    ((DefaultHttpClient)localObject).setHttpRequestRetryHandler(new SocketTimeOutRetryHandler(localBasicHttpParams, this.maxNrRetries, null));
    return localObject;
  }
  
  private HttpEntityEnclosingRequestBase getHttpRequest(URL paramURL, HttpSender.Method paramMethod, String paramString, HttpSender.Type paramType)
    throws UnsupportedEncodingException, UnsupportedOperationException
  {
    Object localObject1;
    switch (1.$SwitchMap$org$acra$sender$HttpSender$Method[paramMethod.ordinal()])
    {
    default: 
      throw new UnsupportedOperationException("Unknown method: " + paramMethod.name());
    case 1: 
      localObject1 = new HttpPost(paramURL.toString());
      break;
    case 2: 
      localObject1 = new HttpPut(paramURL.toString());
    }
    Object localObject2 = getCredentials();
    if (localObject2 != null) {
      ((HttpEntityEnclosingRequestBase)localObject1).addHeader(BasicScheme.authenticate((Credentials)localObject2, "UTF-8", false));
    }
    ((HttpEntityEnclosingRequestBase)localObject1).setHeader("User-Agent", "Android");
    ((HttpEntityEnclosingRequestBase)localObject1).setHeader("Accept", "text/html,application/xml,application/json,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
    ((HttpEntityEnclosingRequestBase)localObject1).setHeader("Content-Type", paramType.getContentType());
    if (this.headers != null) {
      localObject2 = this.headers.keySet().iterator();
    }
    for (;;)
    {
      if (!((Iterator)localObject2).hasNext())
      {
        ((HttpEntityEnclosingRequestBase)localObject1).setEntity(new StringEntity(paramString, "UTF-8"));
        return localObject1;
      }
      String str = (String)((Iterator)localObject2).next();
      ((HttpEntityEnclosingRequestBase)localObject1).setHeader(str, (String)this.headers.get(str));
    }
  }
  
  public static String getParamsAsFormString(Map<?, ?> paramMap)
    throws UnsupportedEncodingException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramMap.keySet().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return localStringBuilder.toString();
      }
      Object localObject1 = localIterator.next();
      if (localStringBuilder.length() != 0) {
        localStringBuilder.append('&');
      }
      Object localObject2 = paramMap.get(localObject1);
      if (localObject2 != null) {
        localObject2 = localObject2;
      } else {
        localObject2 = "";
      }
      localStringBuilder.append(URLEncoder.encode(localObject1.toString(), "UTF-8"));
      localStringBuilder.append('=');
      localStringBuilder.append(URLEncoder.encode(localObject2.toString(), "UTF-8"));
    }
  }
  
  public void send(URL paramURL, HttpSender.Method paramMethod, String paramString, HttpSender.Type paramType)
    throws IOException
  {
    HttpClient localHttpClient = getHttpClient();
    Object localObject1 = getHttpRequest(paramURL, paramMethod, paramString, paramType);
    ACRA.log.d(ACRA.LOG_TAG, "Sending request to " + paramURL);
    HttpResponse localHttpResponse = null;
    try
    {
      localHttpResponse = localHttpClient.execute((HttpUriRequest)localObject1, new BasicHttpContext());
      if (localHttpResponse == null) {
        break label208;
      }
      if (localHttpResponse.getStatusLine() != null)
      {
        localObject1 = Integer.toString(localHttpResponse.getStatusLine().getStatusCode());
        if ((!((String)localObject1).equals("409")) && (!((String)localObject1).equals("403")) && ((((String)localObject1).startsWith("4")) || (((String)localObject1).startsWith("5")))) {
          throw new IOException("Host returned error code " + (String)localObject1);
        }
      }
    }
    finally
    {
      if (localHttpResponse != null) {
        localHttpResponse.getEntity().consumeContent();
      }
    }
    EntityUtils.toString(localHttpResponse.getEntity());
    label208:
    if (localHttpResponse != null) {
      localHttpResponse.getEntity().consumeContent();
    }
  }
  
  public void setConnectionTimeOut(int paramInt)
  {
    this.connectionTimeOut = paramInt;
  }
  
  public void setHeaders(Map<String, String> paramMap)
  {
    this.headers = paramMap;
  }
  
  public void setLogin(String paramString)
  {
    this.login = paramString;
  }
  
  public void setMaxNrRetries(int paramInt)
  {
    this.maxNrRetries = paramInt;
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  public void setSocketTimeOut(int paramInt)
  {
    this.socketTimeOut = paramInt;
  }
  
  private static class SocketTimeOutRetryHandler
    implements HttpRequestRetryHandler
  {
    private final HttpParams httpParams;
    private final int maxNrRetries;
    
    private SocketTimeOutRetryHandler(HttpParams paramHttpParams, int paramInt)
    {
      this.httpParams = paramHttpParams;
      this.maxNrRetries = paramInt;
    }
    
    public boolean retryRequest(IOException paramIOException, int paramInt, HttpContext paramHttpContext)
    {
      if ((paramIOException instanceof SocketTimeoutException))
      {
        if (paramInt > this.maxNrRetries) {
          ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut but exceeded max number of retries : " + this.maxNrRetries);
        }
      }
      else
      {
        i = 0;
        break label139;
      }
      if (this.httpParams == null)
      {
        ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut - no HttpParams, cannot increase time out. Trying again with current settings");
      }
      else
      {
        i = 2 * HttpConnectionParams.getSoTimeout(this.httpParams);
        HttpConnectionParams.setSoTimeout(this.httpParams, i);
        ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut - increasing time out to " + i + " millis and trying again");
      }
      int i = 1;
      label139:
      return i;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.HttpRequest
 * JD-Core Version:    0.7.0.1
 */