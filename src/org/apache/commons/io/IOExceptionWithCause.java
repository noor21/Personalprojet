package org.apache.commons.io;

import java.io.IOException;

public class IOExceptionWithCause
  extends IOException
{
  private static final long serialVersionUID = 1L;
  
  public IOExceptionWithCause(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    initCause(paramThrowable);
  }
  
  public IOExceptionWithCause(Throwable paramThrowable)
  {
    super(str);
    initCause(paramThrowable);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.IOExceptionWithCause
 * JD-Core Version:    0.7.0.1
 */