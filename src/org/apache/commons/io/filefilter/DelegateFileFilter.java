package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.Serializable;

public class DelegateFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final FileFilter fileFilter;
  private final FilenameFilter filenameFilter;
  
  public DelegateFileFilter(FileFilter paramFileFilter)
  {
    if (paramFileFilter != null)
    {
      this.fileFilter = paramFileFilter;
      this.filenameFilter = null;
      return;
    }
    throw new IllegalArgumentException("The FileFilter must not be null");
  }
  
  public DelegateFileFilter(FilenameFilter paramFilenameFilter)
  {
    if (paramFilenameFilter != null)
    {
      this.filenameFilter = paramFilenameFilter;
      this.fileFilter = null;
      return;
    }
    throw new IllegalArgumentException("The FilenameFilter must not be null");
  }
  
  public boolean accept(File paramFile)
  {
    boolean bool;
    if (this.fileFilter == null) {
      bool = super.accept(paramFile);
    } else {
      bool = this.fileFilter.accept(paramFile);
    }
    return bool;
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    boolean bool;
    if (this.filenameFilter == null) {
      bool = super.accept(paramFile, paramString);
    } else {
      bool = this.filenameFilter.accept(paramFile, paramString);
    }
    return bool;
  }
  
  public String toString()
  {
    String str;
    if (this.fileFilter == null) {
      str = this.filenameFilter.toString();
    } else {
      str = this.fileFilter.toString();
    }
    return super.toString() + "(" + str + ")";
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.DelegateFileFilter
 * JD-Core Version:    0.7.0.1
 */