package org.apache.commons.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public abstract class DirectoryWalker<T>
{
  private final int depthLimit;
  private final FileFilter filter;
  
  protected DirectoryWalker()
  {
    this(null, -1);
  }
  
  protected DirectoryWalker(FileFilter paramFileFilter, int paramInt)
  {
    this.filter = paramFileFilter;
    this.depthLimit = paramInt;
  }
  
  protected DirectoryWalker(IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2, int paramInt)
  {
    if ((paramIOFileFilter1 != null) || (paramIOFileFilter2 != null))
    {
      if (paramIOFileFilter1 == null) {
        paramIOFileFilter1 = TrueFileFilter.TRUE;
      }
      if (paramIOFileFilter2 == null) {
        paramIOFileFilter2 = TrueFileFilter.TRUE;
      }
      IOFileFilter localIOFileFilter1 = FileFilterUtils.makeDirectoryOnly(paramIOFileFilter1);
      IOFileFilter localIOFileFilter2 = FileFilterUtils.makeFileOnly(paramIOFileFilter2);
      IOFileFilter[] arrayOfIOFileFilter = new IOFileFilter[2];
      arrayOfIOFileFilter[0] = localIOFileFilter1;
      arrayOfIOFileFilter[1] = localIOFileFilter2;
      this.filter = FileFilterUtils.or(arrayOfIOFileFilter);
    }
    else
    {
      this.filter = null;
    }
    this.depthLimit = paramInt;
  }
  
  private void walk(File paramFile, int paramInt, Collection<T> paramCollection)
    throws IOException
  {
    checkIfCancelled(paramFile, paramInt, paramCollection);
    if (handleDirectory(paramFile, paramInt, paramCollection))
    {
      handleDirectoryStart(paramFile, paramInt, paramCollection);
      int i = paramInt + 1;
      if ((this.depthLimit < 0) || (i <= this.depthLimit))
      {
        checkIfCancelled(paramFile, paramInt, paramCollection);
        File[] arrayOfFile1;
        if (this.filter != null) {
          arrayOfFile1 = paramFile.listFiles(this.filter);
        } else {
          arrayOfFile1 = paramFile.listFiles();
        }
        File[] arrayOfFile2 = filterDirectoryContents(paramFile, paramInt, arrayOfFile1);
        if (arrayOfFile2 != null)
        {
          int j = arrayOfFile2.length;
          for (int k = 0; k < j; k++)
          {
            arrayOfFile1 = arrayOfFile2[k];
            if (!arrayOfFile1.isDirectory())
            {
              checkIfCancelled(arrayOfFile1, i, paramCollection);
              handleFile(arrayOfFile1, i, paramCollection);
              checkIfCancelled(arrayOfFile1, i, paramCollection);
            }
            else
            {
              walk(arrayOfFile1, i, paramCollection);
            }
          }
        }
        handleRestricted(paramFile, i, paramCollection);
      }
      handleDirectoryEnd(paramFile, paramInt, paramCollection);
    }
    checkIfCancelled(paramFile, paramInt, paramCollection);
  }
  
  protected final void checkIfCancelled(File paramFile, int paramInt, Collection<T> paramCollection)
    throws IOException
  {
    if (!handleIsCancelled(paramFile, paramInt, paramCollection)) {
      return;
    }
    throw new CancelException(paramFile, paramInt);
  }
  
  protected File[] filterDirectoryContents(File paramFile, int paramInt, File[] paramArrayOfFile)
    throws IOException
  {
    return paramArrayOfFile;
  }
  
  protected void handleCancelled(File paramFile, Collection<T> paramCollection, CancelException paramCancelException)
    throws IOException
  {
    throw paramCancelException;
  }
  
  protected boolean handleDirectory(File paramFile, int paramInt, Collection<T> paramCollection)
    throws IOException
  {
    return true;
  }
  
  protected void handleDirectoryEnd(File paramFile, int paramInt, Collection<T> paramCollection)
    throws IOException
  {}
  
  protected void handleDirectoryStart(File paramFile, int paramInt, Collection<T> paramCollection)
    throws IOException
  {}
  
  protected void handleEnd(Collection<T> paramCollection)
    throws IOException
  {}
  
  protected void handleFile(File paramFile, int paramInt, Collection<T> paramCollection)
    throws IOException
  {}
  
  protected boolean handleIsCancelled(File paramFile, int paramInt, Collection<T> paramCollection)
    throws IOException
  {
    return false;
  }
  
  protected void handleRestricted(File paramFile, int paramInt, Collection<T> paramCollection)
    throws IOException
  {}
  
  protected void handleStart(File paramFile, Collection<T> paramCollection)
    throws IOException
  {}
  
  protected final void walk(File paramFile, Collection<T> paramCollection)
    throws IOException
  {
    if (paramFile == null) {
      throw new NullPointerException("Start Directory is null");
    }
    try
    {
      handleStart(paramFile, paramCollection);
      walk(paramFile, 0, paramCollection);
      handleEnd(paramCollection);
      return;
    }
    catch (CancelException localCancelException)
    {
      for (;;)
      {
        handleCancelled(paramFile, paramCollection, localCancelException);
      }
    }
  }
  
  public static class CancelException
    extends IOException
  {
    private static final long serialVersionUID = 1347339620135041008L;
    private final int depth;
    private final File file;
    
    public CancelException(File paramFile, int paramInt)
    {
      this("Operation Cancelled", paramFile, paramInt);
    }
    
    public CancelException(String paramString, File paramFile, int paramInt)
    {
      super();
      this.file = paramFile;
      this.depth = paramInt;
    }
    
    public int getDepth()
    {
      return this.depth;
    }
    
    public File getFile()
    {
      return this.file;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.DirectoryWalker
 * JD-Core Version:    0.7.0.1
 */