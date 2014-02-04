package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.io.FileUtils;

public class AgeFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final boolean acceptOlder;
  private final long cutoff;
  
  public AgeFileFilter(long paramLong)
  {
    this(paramLong, true);
  }
  
  public AgeFileFilter(long paramLong, boolean paramBoolean)
  {
    this.acceptOlder = paramBoolean;
    this.cutoff = paramLong;
  }
  
  public AgeFileFilter(File paramFile)
  {
    this(paramFile, true);
  }
  
  public AgeFileFilter(File paramFile, boolean paramBoolean)
  {
    this(paramFile.lastModified(), paramBoolean);
  }
  
  public AgeFileFilter(Date paramDate)
  {
    this(paramDate, true);
  }
  
  public AgeFileFilter(Date paramDate, boolean paramBoolean)
  {
    this(paramDate.getTime(), paramBoolean);
  }
  
  public boolean accept(File paramFile)
  {
    boolean bool = FileUtils.isFileNewer(paramFile, this.cutoff);
    if (this.acceptOlder) {
      if (bool) {
        bool = false;
      } else {
        bool = true;
      }
    }
    return bool;
  }
  
  public String toString()
  {
    String str;
    if (!this.acceptOlder) {
      str = ">";
    } else {
      str = "<=";
    }
    return super.toString() + "(" + str + this.cutoff + ")";
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.AgeFileFilter
 * JD-Core Version:    0.7.0.1
 */