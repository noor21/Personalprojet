package group.pals.android.lib.ui.filechooser.io;

import android.os.Parcelable;

public abstract interface IFile
  extends Parcelable
{
  public abstract boolean canRead();
  
  public abstract IFile clone();
  
  public abstract boolean delete();
  
  public abstract boolean equalsToPath(IFile paramIFile);
  
  public abstract boolean exists();
  
  public abstract String getAbsolutePath()
    throws SecurityException;
  
  public abstract String getName();
  
  public abstract boolean isDirectory()
    throws SecurityException;
  
  public abstract boolean isFile()
    throws SecurityException;
  
  public abstract long lastModified()
    throws SecurityException;
  
  public abstract long length()
    throws SecurityException;
  
  public abstract boolean mkdir();
  
  public abstract IFile parentFile();
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.io.IFile
 * JD-Core Version:    0.7.0.1
 */