package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class DirectoryFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  public static final IOFileFilter DIRECTORY = new DirectoryFileFilter();
  public static final IOFileFilter INSTANCE = DIRECTORY;
  
  public boolean accept(File paramFile)
  {
    return paramFile.isDirectory();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.DirectoryFileFilter
 * JD-Core Version:    0.7.0.1
 */