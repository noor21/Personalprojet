package group.pals.android.lib.ui.filechooser.services;

import android.os.Environment;
import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.io.IFileFilter;
import group.pals.android.lib.ui.filechooser.io.localfile.LocalFile;
import group.pals.android.lib.ui.filechooser.utils.FileComparator;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalFileProvider
  extends FileProviderService
{
  public boolean accept(IFile paramIFile)
  {
    boolean bool = true;
    if ((isDisplayHiddenFiles()) || (!paramIFile.getName().startsWith("."))) {}
    switch (getFilterMode())
    {
    default: 
      if ((getRegexFilenameFilter() != null) && (paramIFile.isFile())) {
        bool = paramIFile.getName().matches(getRegexFilenameFilter());
      }
      break;
    case DirectoriesOnly: 
      if ((getRegexFilenameFilter() != null) && (paramIFile.isFile())) {
        bool = paramIFile.getName().matches(getRegexFilenameFilter());
      }
      break;
    case FilesAndDirectories: 
      bool = paramIFile.isDirectory();
      break;
      bool = false;
    }
    return bool;
  }
  
  public IFile defaultPath()
  {
    Object localObject = Environment.getExternalStorageDirectory();
    if (localObject != null) {
      localObject = new LocalFile((File)localObject);
    } else {
      localObject = fromPath("/");
    }
    return localObject;
  }
  
  public IFile fromPath(String paramString)
  {
    return new LocalFile(paramString);
  }
  
  public List<IFile> listAllFiles(IFile paramIFile)
    throws Exception
  {
    Object localObject;
    if ((!(paramIFile instanceof File)) || (!paramIFile.canRead())) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      try
      {
        localObject = new ArrayList();
        File[] arrayOfFile = ((File)paramIFile).listFiles(new FileFilter()
        {
          public boolean accept(File paramAnonymousFile)
          {
            this.val$_files.add(new LocalFile(paramAnonymousFile));
            return false;
          }
        });
        if (arrayOfFile == null) {
          localObject = null;
        }
      }
      catch (Throwable localThrowable)
      {
        localObject = null;
      }
    }
  }
  
  public List<IFile> listAllFiles(IFile paramIFile, final IFileFilter paramIFileFilter)
  {
    Object localObject;
    if (!(paramIFile instanceof File)) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      localObject = new ArrayList();
      try
      {
        File[] arrayOfFile = ((File)paramIFile).listFiles(new FileFilter()
        {
          public boolean accept(File paramAnonymousFile)
          {
            LocalFile localLocalFile = new LocalFile(paramAnonymousFile);
            if ((paramIFileFilter == null) || (paramIFileFilter.accept(localLocalFile))) {
              this.val$_res.add(localLocalFile);
            }
            return false;
          }
        });
        if (arrayOfFile == null) {
          localObject = null;
        }
      }
      catch (Throwable localThrowable)
      {
        localObject = null;
      }
    }
  }
  
  public List<IFile> listAllFiles(IFile paramIFile, final boolean[] paramArrayOfBoolean)
    throws Exception
  {
    Object localObject;
    if ((!(paramIFile instanceof File)) || (!paramIFile.canRead())) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      if ((paramArrayOfBoolean != null) && (paramArrayOfBoolean.length > 0)) {
        paramArrayOfBoolean[0] = false;
      }
      localObject = new ArrayList();
      try
      {
        if (((File)paramIFile).listFiles(new FileFilter()
        {
          public boolean accept(File paramAnonymousFile)
          {
            LocalFile localLocalFile = new LocalFile(paramAnonymousFile);
            if (LocalFileProvider.this.accept(localLocalFile)) {
              if (this.val$_files.size() < LocalFileProvider.this.getMaxFileCount()) {
                this.val$_files.add(localLocalFile);
              } else if ((paramArrayOfBoolean != null) && (paramArrayOfBoolean.length > 0)) {
                paramArrayOfBoolean[0] = true;
              }
            }
            return false;
          }
        }) == null) {
          break label89;
        }
        Collections.sort((List)localObject, new FileComparator(getSortType(), getSortOrder()));
      }
      catch (Throwable localThrowable)
      {
        localObject = null;
      }
      continue;
      label89:
      localObject = null;
    }
  }
  
  public IFile[] listFiles(IFile paramIFile, boolean[] paramArrayOfBoolean)
    throws Exception
  {
    IFile[] arrayOfIFile = null;
    if (paramIFile.canRead())
    {
      List localList = listAllFiles(paramIFile, paramArrayOfBoolean);
      if (localList != null) {
        arrayOfIFile = (IFile[])localList.toArray(new IFile[localList.size()]);
      }
    }
    return arrayOfIFile;
  }
  
  public void onCreate() {}
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.services.LocalFileProvider
 * JD-Core Version:    0.7.0.1
 */