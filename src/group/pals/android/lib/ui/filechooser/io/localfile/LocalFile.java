package group.pals.android.lib.ui.filechooser.io.localfile;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import group.pals.android.lib.ui.filechooser.io.IFile;
import java.io.File;

public class LocalFile
  extends File
  implements IFile
{
  public static final Parcelable.Creator<LocalFile> CREATOR = new Parcelable.Creator()
  {
    public LocalFile createFromParcel(Parcel paramAnonymousParcel)
    {
      return new LocalFile(paramAnonymousParcel, null);
    }
    
    public LocalFile[] newArray(int paramAnonymousInt)
    {
      return new LocalFile[paramAnonymousInt];
    }
  };
  private static final long serialVersionUID = 2068049445895497580L;
  
  private LocalFile(Parcel paramParcel)
  {
    this(paramParcel.readString());
  }
  
  public LocalFile(File paramFile)
  {
    this(paramFile.getAbsolutePath());
  }
  
  public LocalFile(String paramString)
  {
    super(paramString);
  }
  
  public IFile clone()
  {
    return new LocalFile(getAbsolutePath());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (this != paramObject) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean equalsToPath(IFile paramIFile)
  {
    boolean bool;
    if (paramIFile != null) {
      bool = getAbsolutePath().equals(paramIFile.getAbsolutePath());
    } else {
      bool = false;
    }
    return bool;
  }
  
  public IFile parentFile()
  {
    LocalFile localLocalFile;
    if (getParent() != null) {
      localLocalFile = new LocalFile(getParent());
    } else {
      localLocalFile = null;
    }
    return localLocalFile;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(getAbsolutePath());
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.io.localfile.LocalFile
 * JD-Core Version:    0.7.0.1
 */