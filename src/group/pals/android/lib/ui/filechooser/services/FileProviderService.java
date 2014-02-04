package group.pals.android.lib.ui.filechooser.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public abstract class FileProviderService
  extends Service
  implements IFileProvider
{
  private final IBinder mBinder = new LocalBinder();
  private boolean mDisplayHiddenFiles = false;
  private IFileProvider.FilterMode mFilterMode = IFileProvider.FilterMode.FilesOnly;
  private int mMaxFileCount = 1024;
  private String mRegexFilenameFilter = null;
  private IFileProvider.SortOrder mSortOrder = IFileProvider.SortOrder.Ascending;
  private IFileProvider.SortType mSortType = IFileProvider.SortType.SortByName;
  
  public IFileProvider.FilterMode getFilterMode()
  {
    return this.mFilterMode;
  }
  
  public int getMaxFileCount()
  {
    return this.mMaxFileCount;
  }
  
  public String getRegexFilenameFilter()
  {
    return this.mRegexFilenameFilter;
  }
  
  public IFileProvider.SortOrder getSortOrder()
  {
    return this.mSortOrder;
  }
  
  public IFileProvider.SortType getSortType()
  {
    return this.mSortType;
  }
  
  public boolean isDisplayHiddenFiles()
  {
    return this.mDisplayHiddenFiles;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return this.mBinder;
  }
  
  public void setDisplayHiddenFiles(boolean paramBoolean)
  {
    this.mDisplayHiddenFiles = paramBoolean;
  }
  
  public void setFilterMode(IFileProvider.FilterMode paramFilterMode)
  {
    this.mFilterMode = paramFilterMode;
  }
  
  public void setMaxFileCount(int paramInt)
  {
    this.mMaxFileCount = paramInt;
  }
  
  public void setRegexFilenameFilter(String paramString)
  {
    this.mRegexFilenameFilter = paramString;
  }
  
  public void setSortOrder(IFileProvider.SortOrder paramSortOrder)
  {
    this.mSortOrder = paramSortOrder;
  }
  
  public void setSortType(IFileProvider.SortType paramSortType)
  {
    this.mSortType = paramSortType;
  }
  
  public class LocalBinder
    extends Binder
  {
    public LocalBinder() {}
    
    public IFileProvider getService()
    {
      return FileProviderService.this;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.services.FileProviderService
 * JD-Core Version:    0.7.0.1
 */