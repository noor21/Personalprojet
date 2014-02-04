package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

class ReverseComparator
  extends AbstractFileComparator
  implements Serializable
{
  private final Comparator<File> delegate;
  
  public ReverseComparator(Comparator<File> paramComparator)
  {
    if (paramComparator != null)
    {
      this.delegate = paramComparator;
      return;
    }
    throw new IllegalArgumentException("Delegate comparator is missing");
  }
  
  public int compare(File paramFile1, File paramFile2)
  {
    return this.delegate.compare(paramFile2, paramFile1);
  }
  
  public String toString()
  {
    return super.toString() + "[" + this.delegate.toString() + "]";
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.comparator.ReverseComparator
 * JD-Core Version:    0.7.0.1
 */