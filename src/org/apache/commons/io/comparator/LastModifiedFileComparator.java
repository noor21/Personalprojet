package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class LastModifiedFileComparator
  extends AbstractFileComparator
  implements Serializable
{
  public static final Comparator<File> LASTMODIFIED_COMPARATOR = new LastModifiedFileComparator();
  public static final Comparator<File> LASTMODIFIED_REVERSE = new ReverseComparator(LASTMODIFIED_COMPARATOR);
  
  public int compare(File paramFile1, File paramFile2)
  {
    long l = paramFile1.lastModified() - paramFile2.lastModified();
    int i;
    if (l >= 0L)
    {
      if (l <= 0L) {
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
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.comparator.LastModifiedFileComparator
 * JD-Core Version:    0.7.0.1
 */