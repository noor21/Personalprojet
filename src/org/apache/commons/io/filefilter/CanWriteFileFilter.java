package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class CanWriteFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  public static final IOFileFilter CANNOT_WRITE = new NotFileFilter(CAN_WRITE);
  public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();
  
  public boolean accept(File paramFile)
  {
    return paramFile.canWrite();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.CanWriteFileFilter
 * JD-Core Version:    0.7.0.1
 */