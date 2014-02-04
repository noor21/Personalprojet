package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AndFileFilter
  extends AbstractFileFilter
  implements ConditionalFileFilter, Serializable
{
  private final List<IOFileFilter> fileFilters;
  
  public AndFileFilter()
  {
    this.fileFilters = new ArrayList();
  }
  
  public AndFileFilter(List<IOFileFilter> paramList)
  {
    if (paramList != null) {
      this.fileFilters = new ArrayList(paramList);
    } else {
      this.fileFilters = new ArrayList();
    }
  }
  
  public AndFileFilter(IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2)
  {
    if ((paramIOFileFilter1 != null) && (paramIOFileFilter2 != null))
    {
      this.fileFilters = new ArrayList(2);
      addFileFilter(paramIOFileFilter1);
      addFileFilter(paramIOFileFilter2);
      return;
    }
    throw new IllegalArgumentException("The filters must not be null");
  }
  
  public boolean accept(File paramFile)
  {
    boolean bool = false;
    if (!this.fileFilters.isEmpty())
    {
      Iterator localIterator = this.fileFilters.iterator();
      do
      {
        if (!localIterator.hasNext())
        {
          bool = true;
          break;
        }
      } while (((IOFileFilter)localIterator.next()).accept(paramFile));
    }
    return bool;
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    boolean bool = false;
    if (!this.fileFilters.isEmpty())
    {
      Iterator localIterator = this.fileFilters.iterator();
      do
      {
        if (!localIterator.hasNext())
        {
          bool = true;
          break;
        }
      } while (((IOFileFilter)localIterator.next()).accept(paramFile, paramString));
    }
    return bool;
  }
  
  public void addFileFilter(IOFileFilter paramIOFileFilter)
  {
    this.fileFilters.add(paramIOFileFilter);
  }
  
  public List<IOFileFilter> getFileFilters()
  {
    return Collections.unmodifiableList(this.fileFilters);
  }
  
  public boolean removeFileFilter(IOFileFilter paramIOFileFilter)
  {
    return this.fileFilters.remove(paramIOFileFilter);
  }
  
  public void setFileFilters(List<IOFileFilter> paramList)
  {
    this.fileFilters.clear();
    this.fileFilters.addAll(paramList);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append("(");
    if (this.fileFilters != null) {}
    for (int i = 0;; i++)
    {
      if (i >= this.fileFilters.size())
      {
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      }
      if (i > 0) {
        localStringBuilder.append(",");
      }
      Object localObject = this.fileFilters.get(i);
      if (localObject != null) {
        localObject = localObject.toString();
      } else {
        localObject = "null";
      }
      localStringBuilder.append((String)localObject);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.AndFileFilter
 * JD-Core Version:    0.7.0.1
 */