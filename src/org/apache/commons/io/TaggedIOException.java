package org.apache.commons.io;

import java.io.IOException;
import java.io.Serializable;

public class TaggedIOException
  extends IOExceptionWithCause
{
  private static final long serialVersionUID = -6994123481142850163L;
  private final Serializable tag;
  
  public TaggedIOException(IOException paramIOException, Serializable paramSerializable)
  {
    super(paramIOException.getMessage(), paramIOException);
    this.tag = paramSerializable;
  }
  
  public static boolean isTaggedWith(Throwable paramThrowable, Object paramObject)
  {
    boolean bool;
    if ((paramObject == null) || (!(paramThrowable instanceof TaggedIOException)) || (!paramObject.equals(((TaggedIOException)paramThrowable).tag))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static void throwCauseIfTaggedWith(Throwable paramThrowable, Object paramObject)
    throws IOException
  {
    if (!isTaggedWith(paramThrowable, paramObject)) {
      return;
    }
    throw ((TaggedIOException)paramThrowable).getCause();
  }
  
  public IOException getCause()
  {
    return (IOException)super.getCause();
  }
  
  public Serializable getTag()
  {
    return this.tag;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.TaggedIOException
 * JD-Core Version:    0.7.0.1
 */