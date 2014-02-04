package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class EmptyFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  public static final IOFileFilter EMPTY = new EmptyFileFilter();
  public static final IOFileFilter NOT_EMPTY = new NotFileFilter(EMPTY);
  
  public boolean accept(File paramFile)
  {
    boolean bool1 = true;
    boolean bool2 = false;
    if (!paramFile.isDirectory())
    {
      if (paramFile.length() != 0L) {
        bool1 = false;
      }
      bool2 = bool1;
    }
    else
    {
      File[] arrayOfFile = paramFile.listFiles();
      if ((arrayOfFile == null) || (arrayOfFile.length == 0)) {
        bool2 = bool1;
      }
    }
    return bool2;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.EmptyFileFilter
 * JD-Core Version:    0.7.0.1
 */