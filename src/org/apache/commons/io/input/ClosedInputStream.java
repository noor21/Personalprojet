package org.apache.commons.io.input;

import java.io.InputStream;

public class ClosedInputStream
  extends InputStream
{
  public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();
  
  public int read()
  {
    return -1;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.ClosedInputStream
 * JD-Core Version:    0.7.0.1
 */