package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.IOCase;

public class NameFileComparator
  extends AbstractFileComparator
  implements Serializable
{
  public static final Comparator<File> NAME_COMPARATOR = new NameFileComparator();
  public static final Comparator<File> NAME_INSENSITIVE_COMPARATOR = new NameFileComparator(IOCase.INSENSITIVE);
  public static final Comparator<File> NAME_INSENSITIVE_REVERSE = new ReverseComparator(NAME_INSENSITIVE_COMPARATOR);
  public static final Comparator<File> NAME_REVERSE = new ReverseComparator(NAME_COMPARATOR);
  public static final Comparator<File> NAME_SYSTEM_COMPARATOR = new NameFileComparator(IOCase.SYSTEM);
  public static final Comparator<File> NAME_SYSTEM_REVERSE = new ReverseComparator(NAME_SYSTEM_COMPARATOR);
  private final IOCase caseSensitivity;
  
  public NameFileComparator()
  {
    this.caseSensitivity = IOCase.SENSITIVE;
  }
  
  public NameFileComparator(IOCase paramIOCase)
  {
    if (paramIOCase == null) {
      paramIOCase = IOCase.SENSITIVE;
    }
    this.caseSensitivity = paramIOCase;
  }
  
  public int compare(File paramFile1, File paramFile2)
  {
    return this.caseSensitivity.checkCompareTo(paramFile1.getName(), paramFile2.getName());
  }
  
  public String toString()
  {
    return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.comparator.NameFileComparator
 * JD-Core Version:    0.7.0.1
 */