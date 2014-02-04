package org.acra.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class FakeSocketFactory
  implements SocketFactory, LayeredSocketFactory
{
  private SSLContext sslcontext = null;
  
  private static SSLContext createEasySSLContext()
    throws IOException
  {
    try
    {
      SSLContext localSSLContext = SSLContext.getInstance("TLS");
      TrustManager[] arrayOfTrustManager = new TrustManager[1];
      arrayOfTrustManager[0] = new NaiveTrustManager();
      localSSLContext.init(null, arrayOfTrustManager, null);
      return localSSLContext;
    }
    catch (Exception localException)
    {
      throw new IOException(localException.getMessage());
    }
  }
  
  private SSLContext getSSLContext()
    throws IOException
  {
    if (this.sslcontext == null) {
      this.sslcontext = createEasySSLContext();
    }
    return this.sslcontext;
  }
  
  public Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams)
    throws IOException
  {
    int j = HttpConnectionParams.getConnectionTimeout(paramHttpParams);
    int i = HttpConnectionParams.getSoTimeout(paramHttpParams);
    InetSocketAddress localInetSocketAddress = new InetSocketAddress(paramString, paramInt1);
    if (paramSocket == null) {
      localObject = createSocket();
    } else {
      localObject = paramSocket;
    }
    Object localObject = (SSLSocket)localObject;
    if ((paramInetAddress != null) || (paramInt2 > 0))
    {
      if (paramInt2 < 0) {
        paramInt2 = 0;
      }
      ((SSLSocket)localObject).bind(new InetSocketAddress(paramInetAddress, paramInt2));
    }
    ((SSLSocket)localObject).connect(localInetSocketAddress, j);
    ((SSLSocket)localObject).setSoTimeout(i);
    return localObject;
  }
  
  public Socket createSocket()
    throws IOException
  {
    return getSSLContext().getSocketFactory().createSocket();
  }
  
  public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
    throws IOException
  {
    return getSSLContext().getSocketFactory().createSocket(paramSocket, paramString, paramInt, paramBoolean);
  }
  
  public boolean isSecure(Socket paramSocket)
    throws IllegalArgumentException
  {
    return true;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.FakeSocketFactory
 * JD-Core Version:    0.7.0.1
 */