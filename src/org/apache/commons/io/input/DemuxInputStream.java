package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class DemuxInputStream
  extends InputStream
{
  private final InheritableThreadLocal<InputStream> m_streams = new InheritableThreadLocal();
  
  public InputStream bindStream(InputStream paramInputStream)
  {
    InputStream localInputStream = (InputStream)this.m_streams.get();
    this.m_streams.set(paramInputStream);
    return localInputStream;
  }
  
  public void close()
    throws IOException
  {
    InputStream localInputStream = (InputStream)this.m_streams.get();
    if (localInputStream != null) {
      localInputStream.close();
    }
  }
  
  public int read()
    throws IOException
  {
    InputStream localInputStream = (InputStream)this.m_streams.get();
    int i;
    if (localInputStream == null) {
      i = -1;
    } else {
      i = i.read();
    }
    return i;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.DemuxInputStream
 * JD-Core Version:    0.7.0.1
 */