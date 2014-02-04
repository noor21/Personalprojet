package org.apache.commons.io.input;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.ByteOrderMark;

public class XmlStreamReader
  extends Reader
{
  private static final ByteOrderMark[] BOMS;
  private static final int BUFFER_SIZE = 4096;
  private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
  private static final String EBCDIC = "CP1047";
  public static final Pattern ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
  private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
  private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
  private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";
  private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
  private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
  private static final String US_ASCII = "US-ASCII";
  private static final String UTF_16 = "UTF-16";
  private static final String UTF_16BE = "UTF-16BE";
  private static final String UTF_16LE = "UTF-16LE";
  private static final String UTF_32 = "UTF-32";
  private static final String UTF_32BE = "UTF-32BE";
  private static final String UTF_32LE = "UTF-32LE";
  private static final String UTF_8 = "UTF-8";
  private static final ByteOrderMark[] XML_GUESS_BYTES;
  private final String defaultEncoding;
  private final String encoding;
  private final Reader reader;
  
  static
  {
    ByteOrderMark[] arrayOfByteOrderMark = new ByteOrderMark[5];
    arrayOfByteOrderMark[0] = ByteOrderMark.UTF_8;
    arrayOfByteOrderMark[1] = ByteOrderMark.UTF_16BE;
    arrayOfByteOrderMark[2] = ByteOrderMark.UTF_16LE;
    arrayOfByteOrderMark[3] = ByteOrderMark.UTF_32BE;
    arrayOfByteOrderMark[4] = ByteOrderMark.UTF_32LE;
    BOMS = arrayOfByteOrderMark;
    arrayOfByteOrderMark = new ByteOrderMark[6];
    int[] arrayOfInt = new int[4];
    arrayOfInt[0] = 60;
    arrayOfInt[1] = 63;
    arrayOfInt[2] = 120;
    arrayOfInt[3] = 109;
    arrayOfByteOrderMark[0] = new ByteOrderMark("UTF-8", arrayOfInt);
    arrayOfInt = new int[4];
    arrayOfInt[0] = 0;
    arrayOfInt[1] = 60;
    arrayOfInt[2] = 0;
    arrayOfInt[3] = 63;
    arrayOfByteOrderMark[1] = new ByteOrderMark("UTF-16BE", arrayOfInt);
    arrayOfInt = new int[4];
    arrayOfInt[0] = 60;
    arrayOfInt[1] = 0;
    arrayOfInt[2] = 63;
    arrayOfInt[3] = 0;
    arrayOfByteOrderMark[2] = new ByteOrderMark("UTF-16LE", arrayOfInt);
    arrayOfInt = new int[16];
    arrayOfInt[0] = 0;
    arrayOfInt[1] = 0;
    arrayOfInt[2] = 0;
    arrayOfInt[3] = 60;
    arrayOfInt[4] = 0;
    arrayOfInt[5] = 0;
    arrayOfInt[6] = 0;
    arrayOfInt[7] = 63;
    arrayOfInt[8] = 0;
    arrayOfInt[9] = 0;
    arrayOfInt[10] = 0;
    arrayOfInt[11] = 120;
    arrayOfInt[12] = 0;
    arrayOfInt[13] = 0;
    arrayOfInt[14] = 0;
    arrayOfInt[15] = 109;
    arrayOfByteOrderMark[3] = new ByteOrderMark("UTF-32BE", arrayOfInt);
    arrayOfInt = new int[16];
    arrayOfInt[0] = 60;
    arrayOfInt[1] = 0;
    arrayOfInt[2] = 0;
    arrayOfInt[3] = 0;
    arrayOfInt[4] = 63;
    arrayOfInt[5] = 0;
    arrayOfInt[6] = 0;
    arrayOfInt[7] = 0;
    arrayOfInt[8] = 120;
    arrayOfInt[9] = 0;
    arrayOfInt[10] = 0;
    arrayOfInt[11] = 0;
    arrayOfInt[12] = 109;
    arrayOfInt[13] = 0;
    arrayOfInt[14] = 0;
    arrayOfInt[15] = 0;
    arrayOfByteOrderMark[4] = new ByteOrderMark("UTF-32LE", arrayOfInt);
    arrayOfInt = new int[4];
    arrayOfInt[0] = 76;
    arrayOfInt[1] = 111;
    arrayOfInt[2] = 167;
    arrayOfInt[3] = 148;
    arrayOfByteOrderMark[5] = new ByteOrderMark("CP1047", arrayOfInt);
    XML_GUESS_BYTES = arrayOfByteOrderMark;
  }
  
  public XmlStreamReader(File paramFile)
    throws IOException
  {
    this(new FileInputStream(paramFile));
  }
  
  public XmlStreamReader(InputStream paramInputStream)
    throws IOException
  {
    this(paramInputStream, true);
  }
  
  public XmlStreamReader(InputStream paramInputStream, String paramString)
    throws IOException
  {
    this(paramInputStream, paramString, true);
  }
  
  public XmlStreamReader(InputStream paramInputStream, String paramString, boolean paramBoolean)
    throws IOException
  {
    this(paramInputStream, paramString, paramBoolean, null);
  }
  
  public XmlStreamReader(InputStream paramInputStream, String paramString1, boolean paramBoolean, String paramString2)
    throws IOException
  {
    this.defaultEncoding = paramString2;
    BOMInputStream localBOMInputStream2 = new BOMInputStream(new BufferedInputStream(paramInputStream, 4096), false, BOMS);
    BOMInputStream localBOMInputStream1 = new BOMInputStream(localBOMInputStream2, true, XML_GUESS_BYTES);
    this.encoding = doHttpStream(localBOMInputStream2, localBOMInputStream1, paramString1, paramBoolean);
    this.reader = new InputStreamReader(localBOMInputStream1, this.encoding);
  }
  
  public XmlStreamReader(InputStream paramInputStream, boolean paramBoolean)
    throws IOException
  {
    this(paramInputStream, paramBoolean, null);
  }
  
  public XmlStreamReader(InputStream paramInputStream, boolean paramBoolean, String paramString)
    throws IOException
  {
    this.defaultEncoding = paramString;
    BOMInputStream localBOMInputStream2 = new BOMInputStream(new BufferedInputStream(paramInputStream, 4096), false, BOMS);
    BOMInputStream localBOMInputStream1 = new BOMInputStream(localBOMInputStream2, true, XML_GUESS_BYTES);
    this.encoding = doRawStream(localBOMInputStream2, localBOMInputStream1, paramBoolean);
    this.reader = new InputStreamReader(localBOMInputStream1, this.encoding);
  }
  
  public XmlStreamReader(URL paramURL)
    throws IOException
  {
    this(paramURL.openConnection(), null);
  }
  
  public XmlStreamReader(URLConnection paramURLConnection, String paramString)
    throws IOException
  {
    this.defaultEncoding = paramString;
    String str = paramURLConnection.getContentType();
    BOMInputStream localBOMInputStream2 = new BOMInputStream(new BufferedInputStream(paramURLConnection.getInputStream(), 4096), false, BOMS);
    BOMInputStream localBOMInputStream1 = new BOMInputStream(localBOMInputStream2, true, XML_GUESS_BYTES);
    if ((!(paramURLConnection instanceof HttpURLConnection)) && (str == null)) {
      this.encoding = doRawStream(localBOMInputStream2, localBOMInputStream1, true);
    } else {
      this.encoding = doHttpStream(localBOMInputStream2, localBOMInputStream1, str, true);
    }
    this.reader = new InputStreamReader(localBOMInputStream1, this.encoding);
  }
  
  private String doHttpStream(BOMInputStream paramBOMInputStream1, BOMInputStream paramBOMInputStream2, String paramString, boolean paramBoolean)
    throws IOException
  {
    String str4 = paramBOMInputStream1.getBOMCharsetName();
    String str1 = paramBOMInputStream2.getBOMCharsetName();
    String str3 = getXmlProlog(paramBOMInputStream2, str1);
    try
    {
      str1 = calculateHttpEncoding(paramString, str4, str1, str3, paramBoolean);
      str1 = str1;
    }
    catch (XmlStreamReaderException localXmlStreamReaderException)
    {
      String str2;
      while (paramBoolean) {
        str2 = doLenientDetection(paramString, localXmlStreamReaderException);
      }
      throw str2;
    }
    return str1;
  }
  
  private String doLenientDetection(String paramString, XmlStreamReaderException paramXmlStreamReaderException)
    throws IOException
  {
    String str1;
    if ((paramString != null) && (paramString.startsWith("text/html")))
    {
      str1 = paramString.substring("text/html".length());
      str1 = "text/xml" + str1;
    }
    do
    {
      try
      {
        str1 = calculateHttpEncoding(str1, paramXmlStreamReaderException.getBomEncoding(), paramXmlStreamReaderException.getXmlGuessEncoding(), paramXmlStreamReaderException.getXmlEncoding(), true);
        str1 = str1;
        return str1;
      }
      catch (XmlStreamReaderException localXmlStreamReaderException)
      {
        paramXmlStreamReaderException = localXmlStreamReaderException;
      }
      str2 = paramXmlStreamReaderException.getXmlEncoding();
      if (str2 == null) {
        str2 = paramXmlStreamReaderException.getContentTypeEncoding();
      }
    } while (str2 != null);
    if (this.defaultEncoding == null) {}
    for (String str2 = "UTF-8";; str2 = this.defaultEncoding) {
      break;
    }
  }
  
  private String doRawStream(BOMInputStream paramBOMInputStream1, BOMInputStream paramBOMInputStream2, boolean paramBoolean)
    throws IOException
  {
    String str1 = paramBOMInputStream1.getBOMCharsetName();
    String str3 = paramBOMInputStream2.getBOMCharsetName();
    String str4 = getXmlProlog(paramBOMInputStream2, str3);
    try
    {
      str1 = calculateRawEncoding(str1, str3, str4);
      str1 = str1;
    }
    catch (XmlStreamReaderException localXmlStreamReaderException)
    {
      String str2;
      while (paramBoolean) {
        str2 = doLenientDetection(null, localXmlStreamReaderException);
      }
      throw str2;
    }
    return str1;
  }
  
  static String getContentTypeEncoding(String paramString)
  {
    String str = null;
    if (paramString != null)
    {
      int i = paramString.indexOf(";");
      if (i > -1)
      {
        Object localObject = paramString.substring(i + 1);
        localObject = CHARSET_PATTERN.matcher((CharSequence)localObject);
        if (!((Matcher)localObject).find()) {
          localObject = null;
        } else {
          localObject = ((Matcher)localObject).group(1);
        }
        if (localObject == null) {
          str = null;
        } else {
          str = ((String)localObject).toUpperCase(Locale.US);
        }
      }
    }
    return str;
  }
  
  static String getContentTypeMime(String paramString)
  {
    Object localObject = null;
    String str;
    if (paramString != null)
    {
      int i = paramString.indexOf(";");
      if (i < 0) {
        str = paramString;
      } else {
        str = paramString.substring(0, str);
      }
      str = str.trim();
    }
    return str;
  }
  
  private static String getXmlProlog(InputStream paramInputStream, String paramString)
    throws IOException
  {
    String str1 = null;
    byte[] arrayOfByte;
    int n;
    int i;
    int i2;
    String str3;
    if (paramString != null)
    {
      arrayOfByte = new byte[4096];
      paramInputStream.mark(4096);
      n = 0;
      int k = 4096;
      i = paramInputStream.read(arrayOfByte, 0, k);
      i2 = -1;
      str3 = null;
    }
    for (;;)
    {
      BufferedReader localBufferedReader;
      Object localObject;
      String str2;
      if ((i == -1) || (i2 != -1) || (n >= 4096))
      {
        if (i2 != -1)
        {
          if (n > 0)
          {
            paramInputStream.reset();
            localBufferedReader = new BufferedReader(new StringReader(str3.substring(0, i2 + 1)));
            localObject = new StringBuffer();
          }
          for (str2 = localBufferedReader.readLine();; str2 = localBufferedReader.readLine())
          {
            if (str2 == null)
            {
              localObject = ENCODING_PATTERN.matcher((CharSequence)localObject);
              if (((Matcher)localObject).find())
              {
                str1 = ((Matcher)localObject).group(1).toUpperCase();
                str1 = str1.substring(1, -1 + str1.length());
              }
              return str1;
            }
            ((StringBuffer)localObject).append(str2);
          }
        }
        if (localObject != -1) {
          throw new IOException("XML prolog or ROOT element not found on first " + str2 + " bytes");
        }
        throw new IOException("Unexpected end of XML stream");
      }
      int i1;
      str2 += localObject;
      int m;
      localBufferedReader -= localObject;
      int j = paramInputStream.read(arrayOfByte, i1, m);
      str3 = new String(arrayOfByte, 0, i1, paramString);
      i2 = str3.indexOf('>');
    }
  }
  
  static boolean isAppXml(String paramString)
  {
    boolean bool;
    if ((paramString == null) || ((!paramString.equals("application/xml")) && (!paramString.equals("application/xml-dtd")) && (!paramString.equals("application/xml-external-parsed-entity")) && ((!paramString.startsWith("application/")) || (!paramString.endsWith("+xml"))))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static boolean isTextXml(String paramString)
  {
    boolean bool;
    if ((paramString == null) || ((!paramString.equals("text/xml")) && (!paramString.equals("text/xml-external-parsed-entity")) && ((!paramString.startsWith("text/")) || (!paramString.endsWith("+xml"))))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  String calculateHttpEncoding(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
    throws IOException
  {
    String str1;
    if ((!paramBoolean) || (paramString4 == null))
    {
      String str2 = getContentTypeMime(paramString1);
      str1 = getContentTypeEncoding(paramString1);
      boolean bool2 = isAppXml(str2);
      boolean bool1 = isTextXml(str2);
      Object[] arrayOfObject;
      if ((bool2) || (bool1))
      {
        if (str1 != null)
        {
          if ((!str1.equals("UTF-16BE")) && (!str1.equals("UTF-16LE")))
          {
            if (!str1.equals("UTF-16"))
            {
              if ((!str1.equals("UTF-32BE")) && (!str1.equals("UTF-32LE")))
              {
                if (str1.equals("UTF-32"))
                {
                  if ((paramString2 == null) || (!paramString2.startsWith("UTF-32")))
                  {
                    arrayOfObject = new Object[5];
                    arrayOfObject[0] = str2;
                    arrayOfObject[1] = str1;
                    arrayOfObject[2] = paramString2;
                    arrayOfObject[3] = paramString3;
                    arrayOfObject[4] = paramString4;
                    throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", arrayOfObject), str2, str1, paramString2, paramString3, paramString4);
                  }
                  str1 = paramString2;
                }
              }
              else if (paramString2 != null)
              {
                arrayOfObject = new Object[5];
                arrayOfObject[0] = str2;
                arrayOfObject[1] = str1;
                arrayOfObject[2] = paramString2;
                arrayOfObject[3] = paramString3;
                arrayOfObject[4] = paramString4;
                throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", arrayOfObject), str2, str1, paramString2, paramString3, paramString4);
              }
            }
            else
            {
              if ((paramString2 == null) || (!paramString2.startsWith("UTF-16")))
              {
                arrayOfObject = new Object[5];
                arrayOfObject[0] = str2;
                arrayOfObject[1] = str1;
                arrayOfObject[2] = paramString2;
                arrayOfObject[3] = paramString3;
                arrayOfObject[4] = paramString4;
                throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", arrayOfObject), str2, str1, paramString2, paramString3, paramString4);
              }
              str1 = paramString2;
            }
          }
          else if (paramString2 != null)
          {
            arrayOfObject = new Object[5];
            arrayOfObject[0] = str2;
            arrayOfObject[1] = str1;
            arrayOfObject[2] = paramString2;
            arrayOfObject[3] = paramString3;
            arrayOfObject[4] = paramString4;
            throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", arrayOfObject), str2, str1, paramString2, paramString3, paramString4);
          }
        }
        else if (!bool2)
        {
          if (this.defaultEncoding != null) {
            str1 = this.defaultEncoding;
          } else {
            str1 = "US-ASCII";
          }
          str1 = str1;
        }
        else
        {
          str1 = calculateRawEncoding(paramString2, paramString3, paramString4);
        }
      }
      else
      {
        arrayOfObject = new Object[5];
        arrayOfObject[0] = str2;
        arrayOfObject[1] = str1;
        arrayOfObject[2] = paramString2;
        arrayOfObject[3] = paramString3;
        arrayOfObject[4] = paramString4;
        throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME", arrayOfObject), str2, str1, paramString2, paramString3, paramString4);
      }
    }
    else
    {
      str1 = paramString4;
    }
    return str1;
  }
  
  String calculateRawEncoding(String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    Object localObject;
    if (paramString1 != null)
    {
      if (!paramString1.equals("UTF-8"))
      {
        if ((!paramString1.equals("UTF-16BE")) && (!paramString1.equals("UTF-16LE")))
        {
          if ((!paramString1.equals("UTF-32BE")) && (!paramString1.equals("UTF-32LE")))
          {
            localObject = new Object[3];
            localObject[0] = paramString1;
            localObject[1] = paramString2;
            localObject[2] = paramString3;
            throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM", (Object[])localObject), paramString1, paramString2, paramString3);
          }
          if ((paramString2 == null) || (paramString2.equals(paramString1)))
          {
            if ((paramString3 != null) && (!paramString3.equals("UTF-32")) && (!paramString3.equals(paramString1)))
            {
              localObject = new Object[3];
              localObject[0] = paramString1;
              localObject[1] = paramString2;
              localObject[2] = paramString3;
              throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", (Object[])localObject), paramString1, paramString2, paramString3);
            }
          }
          else
          {
            localObject = new Object[3];
            localObject[0] = paramString1;
            localObject[1] = paramString2;
            localObject[2] = paramString3;
            throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", (Object[])localObject), paramString1, paramString2, paramString3);
          }
        }
        else if ((paramString2 == null) || (paramString2.equals(paramString1)))
        {
          if ((paramString3 != null) && (!paramString3.equals("UTF-16")) && (!paramString3.equals(paramString1)))
          {
            localObject = new Object[3];
            localObject[0] = paramString1;
            localObject[1] = paramString2;
            localObject[2] = paramString3;
            throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", (Object[])localObject), paramString1, paramString2, paramString3);
          }
        }
        else
        {
          localObject = new Object[3];
          localObject[0] = paramString1;
          localObject[1] = paramString2;
          localObject[2] = paramString3;
          throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", (Object[])localObject), paramString1, paramString2, paramString3);
        }
      }
      else if ((paramString2 == null) || (paramString2.equals("UTF-8")))
      {
        if ((paramString3 != null) && (!paramString3.equals("UTF-8")))
        {
          localObject = new Object[3];
          localObject[0] = paramString1;
          localObject[1] = paramString2;
          localObject[2] = paramString3;
          throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", (Object[])localObject), paramString1, paramString2, paramString3);
        }
      }
      else
      {
        localObject = new Object[3];
        localObject[0] = paramString1;
        localObject[1] = paramString2;
        localObject[2] = paramString3;
        throw new XmlStreamReaderException(MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", (Object[])localObject), paramString1, paramString2, paramString3);
      }
    }
    else if ((paramString2 != null) && (paramString3 != null))
    {
      if ((!paramString3.equals("UTF-16")) || ((!paramString2.equals("UTF-16BE")) && (!paramString2.equals("UTF-16LE")))) {
        paramString1 = paramString3;
      } else {
        paramString1 = paramString2;
      }
    }
    else
    {
      if (this.defaultEncoding != null) {
        localObject = this.defaultEncoding;
      } else {
        localObject = "UTF-8";
      }
      paramString1 = (String)localObject;
    }
    return paramString1;
  }
  
  public void close()
    throws IOException
  {
    this.reader.close();
  }
  
  public String getDefaultEncoding()
  {
    return this.defaultEncoding;
  }
  
  public String getEncoding()
  {
    return this.encoding;
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    return this.reader.read(paramArrayOfChar, paramInt1, paramInt2);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.XmlStreamReader
 * JD-Core Version:    0.7.0.1
 */