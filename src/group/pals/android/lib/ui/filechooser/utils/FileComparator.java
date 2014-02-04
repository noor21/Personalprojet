package group.pals.android.lib.ui.filechooser.utils;

import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.SortOrder;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.SortType;
import java.util.Comparator;

public class FileComparator
  implements Comparator<IFile>
{
  private final IFileProvider.SortOrder mSortOrder;
  private final IFileProvider.SortType mSortType;
  
  public FileComparator(IFileProvider.SortType paramSortType, IFileProvider.SortOrder paramSortOrder)
  {
    this.mSortType = paramSortType;
    this.mSortOrder = paramSortOrder;
  }
  
  public int compare(IFile paramIFile1, IFile paramIFile2)
  {
    int i;
    if (((!paramIFile1.isDirectory()) || (!paramIFile2.isDirectory())) && ((!paramIFile1.isFile()) || (!paramIFile2.isFile())))
    {
      if (!paramIFile1.isDirectory()) {
        i = 1;
      } else {
        i = -1;
      }
    }
    else
    {
      i = paramIFile1.getName().compareToIgnoreCase(paramIFile2.getName());
      switch (this.mSortType)
      {
      case SortByName: 
        if (paramIFile1.length() <= paramIFile2.length())
        {
          if (paramIFile1.length() < paramIFile2.length()) {
            i = -1;
          }
        }
        else {
          i = 1;
        }
        break;
      case SortBySize: 
        if (paramIFile1.lastModified() <= paramIFile2.lastModified())
        {
          if (paramIFile1.lastModified() < paramIFile2.lastModified()) {
            i = -1;
          }
        }
        else {
          i = 1;
        }
        break;
      }
      if (this.mSortOrder != IFileProvider.SortOrder.Ascending) {
        i = -i;
      }
      i = i;
    }
    return i;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.FileComparator
 * JD-Core Version:    0.7.0.1
 */