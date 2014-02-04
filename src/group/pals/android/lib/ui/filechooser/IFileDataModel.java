package group.pals.android.lib.ui.filechooser;

import group.pals.android.lib.ui.filechooser.io.IFile;

public class IFileDataModel
{
  private IFile mFile;
  private boolean mSelected;
  private boolean mTobeDeleted;
  
  public IFileDataModel(IFile paramIFile)
  {
    this.mFile = paramIFile;
  }
  
  public IFile getFile()
  {
    return this.mFile;
  }
  
  public boolean isSelected()
  {
    return this.mSelected;
  }
  
  public boolean isTobeDeleted()
  {
    return this.mTobeDeleted;
  }
  
  public void setSelected(boolean paramBoolean)
  {
    if (this.mSelected != paramBoolean) {
      this.mSelected = paramBoolean;
    }
  }
  
  public void setTobeDeleted(boolean paramBoolean)
  {
    if (this.mTobeDeleted != paramBoolean) {
      this.mTobeDeleted = paramBoolean;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.IFileDataModel
 * JD-Core Version:    0.7.0.1
 */