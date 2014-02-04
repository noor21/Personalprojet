package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

public class TaggedOutputStream
  extends ProxyOutputStream
{
  private final Serializable tag = UUID.randomUUID();
  
  public TaggedOutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }
  
  protected void handleIOException(IOException paramIOException)
    throws IOException
  {
    throw new TaggedIOException(paramIOException, this.tag);
  }
  
  public boolean isCauseOf(Exception paramException)
  {
    return TaggedIOException.isTaggedWith(paramException, this.tag);
  }
  
  public void throwIfCauseOf(Exception paramException)
    throws IOException
  {
    TaggedIOException.throwCauseIfTaggedWith(paramException, this.tag);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.TaggedOutputStream
 * JD-Core Version:    0.7.0.1
 */