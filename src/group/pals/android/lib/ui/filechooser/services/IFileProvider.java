package group.pals.android.lib.ui.filechooser.services;

import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.io.IFileFilter;
import java.util.List;

public abstract interface IFileProvider
{
  public abstract boolean accept(IFile paramIFile);
  
  public abstract IFile defaultPath();
  
  public abstract IFile fromPath(String paramString);
  
  public abstract FilterMode getFilterMode();
  
  public abstract int getMaxFileCount();
  
  public abstract String getRegexFilenameFilter();
  
  public abstract SortOrder getSortOrder();
  
  public abstract SortType getSortType();
  
  public abstract boolean isDisplayHiddenFiles();
  
  public abstract List<IFile> listAllFiles(IFile paramIFile)
    throws Exception;
  
  public abstract List<IFile> listAllFiles(IFile paramIFile, IFileFilter paramIFileFilter);
  
  public abstract List<IFile> listAllFiles(IFile paramIFile, boolean[] paramArrayOfBoolean)
    throws Exception;
  
  public abstract IFile[] listFiles(IFile paramIFile, boolean[] paramArrayOfBoolean)
    throws Exception;
  
  public abstract void setDisplayHiddenFiles(boolean paramBoolean);
  
  public abstract void setFilterMode(FilterMode paramFilterMode);
  
  public abstract void setMaxFileCount(int paramInt);
  
  public abstract void setRegexFilenameFilter(String paramString);
  
  public abstract void setSortOrder(SortOrder paramSortOrder);
  
  public abstract void setSortType(SortType paramSortType);
  
  public static enum FilterMode
  {
    FilesAndDirectories,  DirectoriesOnly,  FilesOnly;
  }
  
  public static enum SortOrder
  {
    Descending(false),  Ascending(true);
    
    final boolean mAsc;
    
    private SortOrder(boolean paramBoolean)
    {
      this.mAsc = paramBoolean;
    }
    
    public boolean isAsc()
    {
      return this.mAsc;
    }
  }
  
  public static enum SortType
  {
    SortByDate,  SortBySize,  SortByName;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.services.IFileProvider
 * JD-Core Version:    0.7.0.1
 */