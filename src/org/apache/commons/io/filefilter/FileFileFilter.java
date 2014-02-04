package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class FileFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  public static final IOFileFilter FILE = new FileFileFilter();
  
  public boolean accept(File paramFile)
  {
    return paramFile.isFile();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.FileFileFilter
 * JD-Core Version:    0.7.0.1
 */