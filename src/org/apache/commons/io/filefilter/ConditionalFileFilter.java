package org.apache.commons.io.filefilter;

import java.util.List;

public abstract interface ConditionalFileFilter
{
  public abstract void addFileFilter(IOFileFilter paramIOFileFilter);
  
  public abstract List<IOFileFilter> getFileFilters();
  
  public abstract boolean removeFileFilter(IOFileFilter paramIOFileFilter);
  
  public abstract void setFileFilters(List<IOFileFilter> paramList);
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.ConditionalFileFilter
 * JD-Core Version:    0.7.0.1
 */