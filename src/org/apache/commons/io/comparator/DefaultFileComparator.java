package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class DefaultFileComparator
  extends AbstractFileComparator
  implements Serializable
{
  public static final Comparator<File> DEFAULT_COMPARATOR = new DefaultFileComparator();
  public static final Comparator<File> DEFAULT_REVERSE = new ReverseComparator(DEFAULT_COMPARATOR);
  
  public int compare(File paramFile1, File paramFile2)
  {
    return paramFile1.compareTo(paramFile2);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.comparator.DefaultFileComparator
 * JD-Core Version:    0.7.0.1
 */