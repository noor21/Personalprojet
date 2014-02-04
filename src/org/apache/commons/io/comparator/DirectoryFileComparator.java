package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class DirectoryFileComparator
  extends AbstractFileComparator
  implements Serializable
{
  public static final Comparator<File> DIRECTORY_COMPARATOR = new DirectoryFileComparator();
  public static final Comparator<File> DIRECTORY_REVERSE = new ReverseComparator(DIRECTORY_COMPARATOR);
  
  private int getType(File paramFile)
  {
    int i;
    if (!paramFile.isDirectory()) {
      i = 2;
    } else {
      i = 1;
    }
    return i;
  }
  
  public int compare(File paramFile1, File paramFile2)
  {
    return getType(paramFile1) - getType(paramFile2);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.comparator.DirectoryFileComparator
 * JD-Core Version:    0.7.0.1
 */