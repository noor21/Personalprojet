package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CompositeFileComparator
  extends AbstractFileComparator
  implements Serializable
{
  private static final Comparator<?>[] NO_COMPARATORS = new Comparator[0];
  private final Comparator<File>[] delegates;
  
  public CompositeFileComparator(Iterable<Comparator<File>> paramIterable)
  {
    if (paramIterable != null)
    {
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator = paramIterable.iterator();
      for (;;)
      {
        if (!localIterator.hasNext())
        {
          this.delegates = ((Comparator[])localArrayList.toArray(new Comparator[localArrayList.size()]));
          break;
        }
        localArrayList.add((Comparator)localIterator.next());
      }
    }
    this.delegates = ((Comparator[])NO_COMPARATORS);
  }
  
  public CompositeFileComparator(Comparator<File>... paramVarArgs)
  {
    if (paramVarArgs != null)
    {
      this.delegates = ((Comparator[])new Comparator[paramVarArgs.length]);
      System.arraycopy(paramVarArgs, 0, this.delegates, 0, paramVarArgs.length);
    }
    else
    {
      this.delegates = ((Comparator[])NO_COMPARATORS);
    }
  }
  
  public int compare(File paramFile1, File paramFile2)
  {
    int k = 0;
    Comparator[] arrayOfComparator = this.delegates;
    int i = arrayOfComparator.length;
    for (int j = 0; j < i; j++)
    {
      k = arrayOfComparator[j].compare(paramFile1, paramFile2);
      if (k != 0) {
        break;
      }
    }
    return k;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append('{');
    for (int i = 0;; i++)
    {
      if (i >= this.delegates.length)
      {
        localStringBuilder.append('}');
        return localStringBuilder.toString();
      }
      if (i > 0) {
        localStringBuilder.append(',');
      }
      localStringBuilder.append(this.delegates[i]);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.comparator.CompositeFileComparator
 * JD-Core Version:    0.7.0.1
 */