package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator
  implements Iterator<String>
{
  private final BufferedReader bufferedReader;
  private String cachedLine;
  private boolean finished = false;
  
  public LineIterator(Reader paramReader)
    throws IllegalArgumentException
  {
    if (paramReader != null)
    {
      if (!(paramReader instanceof BufferedReader)) {
        this.bufferedReader = new BufferedReader(paramReader);
      } else {
        this.bufferedReader = ((BufferedReader)paramReader);
      }
      return;
    }
    throw new IllegalArgumentException("Reader must not be null");
  }
  
  public static void closeQuietly(LineIterator paramLineIterator)
  {
    if (paramLineIterator != null) {
      paramLineIterator.close();
    }
  }
  
  public void close()
  {
    this.finished = true;
    IOUtils.closeQuietly(this.bufferedReader);
    this.cachedLine = null;
  }
  
  public boolean hasNext()
  {
    boolean bool = true;
    if (this.cachedLine != null) {}
    for (;;)
    {
      return bool;
      if (this.finished)
      {
        bool = false;
        continue;
      }
      try
      {
        String str;
        do
        {
          str = this.bufferedReader.readLine();
          if (str == null)
          {
            this.finished = true;
            bool = false;
            break;
          }
        } while (!isValidLine(str));
        this.cachedLine = str;
      }
      catch (IOException localIOException)
      {
        close();
        throw new IllegalStateException(localIOException);
      }
    }
  }
  
  protected boolean isValidLine(String paramString)
  {
    return true;
  }
  
  public String next()
  {
    return nextLine();
  }
  
  public String nextLine()
  {
    if (hasNext())
    {
      String str = this.cachedLine;
      this.cachedLine = null;
      return str;
    }
    throw new NoSuchElementException("No more lines");
  }
  
  public void remove()
  {
    throw new UnsupportedOperationException("Remove unsupported on LineIterator");
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.LineIterator
 * JD-Core Version:    0.7.0.1
 */