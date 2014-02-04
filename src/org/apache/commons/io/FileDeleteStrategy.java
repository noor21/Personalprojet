package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

public class FileDeleteStrategy
{
  public static final FileDeleteStrategy FORCE = new ForceFileDeleteStrategy();
  public static final FileDeleteStrategy NORMAL = new FileDeleteStrategy("Normal");
  private final String name;
  
  protected FileDeleteStrategy(String paramString)
  {
    this.name = paramString;
  }
  
  public void delete(File paramFile)
    throws IOException
  {
    if ((!paramFile.exists()) || (doDelete(paramFile))) {
      return;
    }
    throw new IOException("Deletion failed: " + paramFile);
  }
  
  public boolean deleteQuietly(File paramFile)
  {
    boolean bool;
    if ((paramFile == null) || (!paramFile.exists())) {
      bool = true;
    }
    for (;;)
    {
      return bool;
      try
      {
        bool = doDelete(paramFile);
        bool = bool;
      }
      catch (IOException localIOException)
      {
        bool = false;
      }
    }
  }
  
  protected boolean doDelete(File paramFile)
    throws IOException
  {
    return paramFile.delete();
  }
  
  public String toString()
  {
    return "FileDeleteStrategy[" + this.name + "]";
  }
  
  static class ForceFileDeleteStrategy
    extends FileDeleteStrategy
  {
    ForceFileDeleteStrategy()
    {
      super();
    }
    
    protected boolean doDelete(File paramFile)
      throws IOException
    {
      FileUtils.forceDelete(paramFile);
      return true;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.FileDeleteStrategy
 * JD-Core Version:    0.7.0.1
 */