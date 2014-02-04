package org.acra.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

class NaiveTrustManager
  implements X509TrustManager
{
  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {}
  
  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {}
  
  public X509Certificate[] getAcceptedIssuers()
  {
    return new X509Certificate[0];
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.NaiveTrustManager
 * JD-Core Version:    0.7.0.1
 */