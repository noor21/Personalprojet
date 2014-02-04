package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.FileUtils;

public class SizeFileComparator
  extends AbstractFileComparator
  implements Serializable
{
  public static final Comparator<File> SIZE_COMPARATOR = new SizeFileComparator();
  public static final Comparator<File> SIZE_REVERSE = new ReverseComparator(SIZE_COMPARATOR);
  public static final Comparator<File> SIZE_SUMDIR_COMPARATOR = new SizeFileComparator(true);
  public static final Comparator<File> SIZE_SUMDIR_REVERSE = new ReverseComparator(SIZE_SUMDIR_COMPARATOR);
  private final boolean sumDirectoryContents;
  
  public SizeFileComparator()
  {
    this.sumDirectoryContents = false;
  }
  
  public SizeFileComparator(boolean paramBoolean)
  {
    this.sumDirectoryContents = paramBoolean;
  }
  
  public int compare(File paramFile1, File paramFile2)
  {
    long l2;
    if (!paramFile1.isDirectory()) {
      l2 = paramFile1.length();
    } else if ((!this.sumDirectoryContents) || (!paramFile1.exists())) {
      l2 = 0L;
    } else {
      l2 = FileUtils.sizeOfDirectory(paramFile1);
    }
    if (!paramFile2.isDirectory()) {
      l1 = paramFile2.length();
    } else if ((!this.sumDirectoryContents) || (!paramFile2.exists())) {
      l1 = 0L;
    } else {
      l1 = FileUtils.sizeOfDirectory(paramFile2);
    }
    long l1 = l2 - l1;
    int i;
    if (l1 >= 0L)
    {
      if (l1 <= 0L) {
        i = 0;
      } else {
        i = 1;
      }
    }
    else {
      i = -1;
    }
    return i;
  }
  
  public String toString()
  {
    return super.toString() + "[sumDirectoryContents=" + this.sumDirectoryContents + "]";
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.comparator.SizeFileComparator
 * JD-Core Version:    0.7.0.1
 */