package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOCase;

public class FileFilterUtils
{
  private static final IOFileFilter cvsFilter;
  private static final IOFileFilter svnFilter;
  
  static
  {
    IOFileFilter[] arrayOfIOFileFilter = new IOFileFilter[2];
    arrayOfIOFileFilter[0] = directoryFileFilter();
    arrayOfIOFileFilter[1] = nameFileFilter("CVS");
    cvsFilter = notFileFilter(and(arrayOfIOFileFilter));
    arrayOfIOFileFilter = new IOFileFilter[2];
    arrayOfIOFileFilter[0] = directoryFileFilter();
    arrayOfIOFileFilter[1] = nameFileFilter(".svn");
    svnFilter = notFileFilter(and(arrayOfIOFileFilter));
  }
  
  public static IOFileFilter ageFileFilter(long paramLong)
  {
    return new AgeFileFilter(paramLong);
  }
  
  public static IOFileFilter ageFileFilter(long paramLong, boolean paramBoolean)
  {
    return new AgeFileFilter(paramLong, paramBoolean);
  }
  
  public static IOFileFilter ageFileFilter(File paramFile)
  {
    return new AgeFileFilter(paramFile);
  }
  
  public static IOFileFilter ageFileFilter(File paramFile, boolean paramBoolean)
  {
    return new AgeFileFilter(paramFile, paramBoolean);
  }
  
  public static IOFileFilter ageFileFilter(Date paramDate)
  {
    return new AgeFileFilter(paramDate);
  }
  
  public static IOFileFilter ageFileFilter(Date paramDate, boolean paramBoolean)
  {
    return new AgeFileFilter(paramDate, paramBoolean);
  }
  
  public static IOFileFilter and(IOFileFilter... paramVarArgs)
  {
    return new AndFileFilter(toList(paramVarArgs));
  }
  
  @Deprecated
  public static IOFileFilter andFileFilter(IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2)
  {
    return new AndFileFilter(paramIOFileFilter1, paramIOFileFilter2);
  }
  
  public static IOFileFilter asFileFilter(FileFilter paramFileFilter)
  {
    return new DelegateFileFilter(paramFileFilter);
  }
  
  public static IOFileFilter asFileFilter(FilenameFilter paramFilenameFilter)
  {
    return new DelegateFileFilter(paramFilenameFilter);
  }
  
  public static IOFileFilter directoryFileFilter()
  {
    return DirectoryFileFilter.DIRECTORY;
  }
  
  public static IOFileFilter falseFileFilter()
  {
    return FalseFileFilter.FALSE;
  }
  
  public static IOFileFilter fileFileFilter()
  {
    return FileFileFilter.FILE;
  }
  
  private static <T extends Collection<File>> T filter(IOFileFilter paramIOFileFilter, Iterable<File> paramIterable, T paramT)
  {
    if (paramIOFileFilter != null)
    {
      Iterator localIterator;
      if (paramIterable != null) {
        localIterator = paramIterable.iterator();
      }
      for (;;)
      {
        if (!localIterator.hasNext()) {
          return paramT;
        }
        File localFile = (File)localIterator.next();
        if (localFile == null) {
          break;
        }
        if (paramIOFileFilter.accept(localFile)) {
          paramT.add(localFile);
        }
      }
      throw new IllegalArgumentException("file collection contains null");
    }
    throw new IllegalArgumentException("file filter is null");
  }
  
  public static File[] filter(IOFileFilter paramIOFileFilter, Iterable<File> paramIterable)
  {
    List localList = filterList(paramIOFileFilter, paramIterable);
    return (File[])localList.toArray(new File[localList.size()]);
  }
  
  public static File[] filter(IOFileFilter paramIOFileFilter, File... paramVarArgs)
  {
    if (paramIOFileFilter != null)
    {
      if (paramVarArgs != null)
      {
        localObject = new ArrayList();
        int j = paramVarArgs.length;
        for (int i = 0;; i++)
        {
          if (i >= j)
          {
            localObject = (File[])((List)localObject).toArray(new File[((List)localObject).size()]);
            break label100;
          }
          File localFile = paramVarArgs[i];
          if (localFile == null) {
            break;
          }
          if (paramIOFileFilter.accept(localFile)) {
            ((List)localObject).add(localFile);
          }
        }
        throw new IllegalArgumentException("file array contains null");
      }
      Object localObject = new File[0];
      label100:
      return localObject;
    }
    throw new IllegalArgumentException("file filter is null");
  }
  
  public static List<File> filterList(IOFileFilter paramIOFileFilter, Iterable<File> paramIterable)
  {
    return (List)filter(paramIOFileFilter, paramIterable, new ArrayList());
  }
  
  public static List<File> filterList(IOFileFilter paramIOFileFilter, File... paramVarArgs)
  {
    return Arrays.asList(filter(paramIOFileFilter, paramVarArgs));
  }
  
  public static Set<File> filterSet(IOFileFilter paramIOFileFilter, Iterable<File> paramIterable)
  {
    return (Set)filter(paramIOFileFilter, paramIterable, new HashSet());
  }
  
  public static Set<File> filterSet(IOFileFilter paramIOFileFilter, File... paramVarArgs)
  {
    return new HashSet(Arrays.asList(filter(paramIOFileFilter, paramVarArgs)));
  }
  
  public static IOFileFilter magicNumberFileFilter(String paramString)
  {
    return new MagicNumberFileFilter(paramString);
  }
  
  public static IOFileFilter magicNumberFileFilter(String paramString, long paramLong)
  {
    return new MagicNumberFileFilter(paramString, paramLong);
  }
  
  public static IOFileFilter magicNumberFileFilter(byte[] paramArrayOfByte)
  {
    return new MagicNumberFileFilter(paramArrayOfByte);
  }
  
  public static IOFileFilter magicNumberFileFilter(byte[] paramArrayOfByte, long paramLong)
  {
    return new MagicNumberFileFilter(paramArrayOfByte, paramLong);
  }
  
  public static IOFileFilter makeCVSAware(IOFileFilter paramIOFileFilter)
  {
    Object localObject;
    if (paramIOFileFilter != null)
    {
      localObject = new IOFileFilter[2];
      localObject[0] = paramIOFileFilter;
      localObject[1] = cvsFilter;
      localObject = and((IOFileFilter[])localObject);
    }
    else
    {
      localObject = cvsFilter;
    }
    return localObject;
  }
  
  public static IOFileFilter makeDirectoryOnly(IOFileFilter paramIOFileFilter)
  {
    Object localObject;
    if (paramIOFileFilter != null) {
      localObject = new AndFileFilter(DirectoryFileFilter.DIRECTORY, paramIOFileFilter);
    } else {
      localObject = DirectoryFileFilter.DIRECTORY;
    }
    return localObject;
  }
  
  public static IOFileFilter makeFileOnly(IOFileFilter paramIOFileFilter)
  {
    Object localObject;
    if (paramIOFileFilter != null) {
      localObject = new AndFileFilter(FileFileFilter.FILE, paramIOFileFilter);
    } else {
      localObject = FileFileFilter.FILE;
    }
    return localObject;
  }
  
  public static IOFileFilter makeSVNAware(IOFileFilter paramIOFileFilter)
  {
    Object localObject;
    if (paramIOFileFilter != null)
    {
      localObject = new IOFileFilter[2];
      localObject[0] = paramIOFileFilter;
      localObject[1] = svnFilter;
      localObject = and((IOFileFilter[])localObject);
    }
    else
    {
      localObject = svnFilter;
    }
    return localObject;
  }
  
  public static IOFileFilter nameFileFilter(String paramString)
  {
    return new NameFileFilter(paramString);
  }
  
  public static IOFileFilter nameFileFilter(String paramString, IOCase paramIOCase)
  {
    return new NameFileFilter(paramString, paramIOCase);
  }
  
  public static IOFileFilter notFileFilter(IOFileFilter paramIOFileFilter)
  {
    return new NotFileFilter(paramIOFileFilter);
  }
  
  public static IOFileFilter or(IOFileFilter... paramVarArgs)
  {
    return new OrFileFilter(toList(paramVarArgs));
  }
  
  @Deprecated
  public static IOFileFilter orFileFilter(IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2)
  {
    return new OrFileFilter(paramIOFileFilter1, paramIOFileFilter2);
  }
  
  public static IOFileFilter prefixFileFilter(String paramString)
  {
    return new PrefixFileFilter(paramString);
  }
  
  public static IOFileFilter prefixFileFilter(String paramString, IOCase paramIOCase)
  {
    return new PrefixFileFilter(paramString, paramIOCase);
  }
  
  public static IOFileFilter sizeFileFilter(long paramLong)
  {
    return new SizeFileFilter(paramLong);
  }
  
  public static IOFileFilter sizeFileFilter(long paramLong, boolean paramBoolean)
  {
    return new SizeFileFilter(paramLong, paramBoolean);
  }
  
  public static IOFileFilter sizeRangeFileFilter(long paramLong1, long paramLong2)
  {
    return new AndFileFilter(new SizeFileFilter(paramLong1, true), new SizeFileFilter(1L + paramLong2, false));
  }
  
  public static IOFileFilter suffixFileFilter(String paramString)
  {
    return new SuffixFileFilter(paramString);
  }
  
  public static IOFileFilter suffixFileFilter(String paramString, IOCase paramIOCase)
  {
    return new SuffixFileFilter(paramString, paramIOCase);
  }
  
  public static List<IOFileFilter> toList(IOFileFilter... paramVarArgs)
  {
    if (paramVarArgs != null)
    {
      ArrayList localArrayList = new ArrayList(paramVarArgs.length);
      for (int i = 0;; i++)
      {
        if (i >= paramVarArgs.length) {
          return localArrayList;
        }
        if (paramVarArgs[i] == null) {
          break;
        }
        localArrayList.add(paramVarArgs[i]);
      }
      throw new IllegalArgumentException("The filter[" + i + "] is null");
    }
    throw new IllegalArgumentException("The filters must not be null");
  }
  
  public static IOFileFilter trueFileFilter()
  {
    return TrueFileFilter.TRUE;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.FileFilterUtils
 * JD-Core Version:    0.7.0.1
 */