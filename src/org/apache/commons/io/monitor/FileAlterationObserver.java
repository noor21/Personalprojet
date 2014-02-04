package org.apache.commons.io.monitor;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.comparator.NameFileComparator;

public class FileAlterationObserver
  implements Serializable
{
  private final Comparator<File> comparator;
  private final FileFilter fileFilter;
  private final List<FileAlterationListener> listeners = new CopyOnWriteArrayList();
  private final FileEntry rootEntry;
  
  public FileAlterationObserver(File paramFile)
  {
    this(paramFile, (FileFilter)null);
  }
  
  public FileAlterationObserver(File paramFile, FileFilter paramFileFilter)
  {
    this(paramFile, paramFileFilter, (IOCase)null);
  }
  
  public FileAlterationObserver(File paramFile, FileFilter paramFileFilter, IOCase paramIOCase)
  {
    this(new FileEntry(paramFile), paramFileFilter, paramIOCase);
  }
  
  public FileAlterationObserver(String paramString)
  {
    this(new File(paramString));
  }
  
  public FileAlterationObserver(String paramString, FileFilter paramFileFilter)
  {
    this(new File(paramString), paramFileFilter);
  }
  
  public FileAlterationObserver(String paramString, FileFilter paramFileFilter, IOCase paramIOCase)
  {
    this(new File(paramString), paramFileFilter, paramIOCase);
  }
  
  protected FileAlterationObserver(FileEntry paramFileEntry, FileFilter paramFileFilter, IOCase paramIOCase)
  {
    if (paramFileEntry != null)
    {
      if (paramFileEntry.getFile() != null)
      {
        this.rootEntry = paramFileEntry;
        this.fileFilter = paramFileFilter;
        if ((paramIOCase != null) && (!paramIOCase.equals(IOCase.SYSTEM)))
        {
          if (!paramIOCase.equals(IOCase.INSENSITIVE)) {
            this.comparator = NameFileComparator.NAME_COMPARATOR;
          } else {
            this.comparator = NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
          }
        }
        else {
          this.comparator = NameFileComparator.NAME_SYSTEM_COMPARATOR;
        }
        return;
      }
      throw new IllegalArgumentException("Root directory is missing");
    }
    throw new IllegalArgumentException("Root entry is missing");
  }
  
  private void checkAndNotify(FileEntry paramFileEntry, FileEntry[] paramArrayOfFileEntry, File[] paramArrayOfFile)
  {
    int j = 0;
    FileEntry[] arrayOfFileEntry;
    if (paramArrayOfFile.length <= 0) {
      arrayOfFileEntry = FileEntry.EMPTY_ENTRIES;
    } else {
      arrayOfFileEntry = new FileEntry[paramArrayOfFile.length];
    }
    int i = paramArrayOfFileEntry.length;
    int k = 0;
    if (k >= i) {
      for (;;)
      {
        if (j >= paramArrayOfFile.length)
        {
          paramFileEntry.setChildren(arrayOfFileEntry);
          return;
        }
        arrayOfFileEntry[j] = createFileEntry(paramFileEntry, paramArrayOfFile[j]);
        doCreate(arrayOfFileEntry[j]);
        j++;
      }
    }
    FileEntry localFileEntry = paramArrayOfFileEntry[k];
    for (;;)
    {
      if ((j >= paramArrayOfFile.length) || (this.comparator.compare(localFileEntry.getFile(), paramArrayOfFile[j]) <= 0))
      {
        if ((j >= paramArrayOfFile.length) || (this.comparator.compare(localFileEntry.getFile(), paramArrayOfFile[j]) != 0))
        {
          checkAndNotify(localFileEntry, localFileEntry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
          doDelete(localFileEntry);
        }
        else
        {
          doMatch(localFileEntry, paramArrayOfFile[j]);
          checkAndNotify(localFileEntry, localFileEntry.getChildren(), listFiles(paramArrayOfFile[j]));
          arrayOfFileEntry[j] = localFileEntry;
          j++;
        }
        k++;
        break;
      }
      arrayOfFileEntry[j] = createFileEntry(paramFileEntry, paramArrayOfFile[j]);
      doCreate(arrayOfFileEntry[j]);
      j++;
    }
  }
  
  private FileEntry createFileEntry(FileEntry paramFileEntry, File paramFile)
  {
    FileEntry localFileEntry = paramFileEntry.newChildInstance(paramFile);
    localFileEntry.refresh(paramFile);
    File[] arrayOfFile = listFiles(paramFile);
    FileEntry[] arrayOfFileEntry;
    if (arrayOfFile.length <= 0) {
      arrayOfFileEntry = FileEntry.EMPTY_ENTRIES;
    } else {
      arrayOfFileEntry = new FileEntry[arrayOfFile.length];
    }
    for (int i = 0;; i++)
    {
      if (i >= arrayOfFile.length)
      {
        localFileEntry.setChildren(arrayOfFileEntry);
        return localFileEntry;
      }
      arrayOfFileEntry[i] = createFileEntry(localFileEntry, arrayOfFile[i]);
    }
  }
  
  private void doCreate(FileEntry paramFileEntry)
  {
    Iterator localIterator = this.listeners.iterator();
    for (;;)
    {
      int i;
      if (!localIterator.hasNext())
      {
        localObject = paramFileEntry.getChildren();
        int j = localObject.length;
        for (i = 0;; i++)
        {
          if (i >= j) {
            return;
          }
          doCreate(localObject[i]);
        }
      }
      Object localObject = (FileAlterationListener)i.next();
      if (!paramFileEntry.isDirectory()) {
        ((FileAlterationListener)localObject).onFileCreate(paramFileEntry.getFile());
      } else {
        ((FileAlterationListener)localObject).onDirectoryCreate(paramFileEntry.getFile());
      }
    }
  }
  
  private void doDelete(FileEntry paramFileEntry)
  {
    Iterator localIterator = this.listeners.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      FileAlterationListener localFileAlterationListener = (FileAlterationListener)localIterator.next();
      if (!paramFileEntry.isDirectory()) {
        localFileAlterationListener.onFileDelete(paramFileEntry.getFile());
      } else {
        localFileAlterationListener.onDirectoryDelete(paramFileEntry.getFile());
      }
    }
  }
  
  private void doMatch(FileEntry paramFileEntry, File paramFile)
  {
    Iterator localIterator;
    if (paramFileEntry.refresh(paramFile)) {
      localIterator = this.listeners.iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      FileAlterationListener localFileAlterationListener = (FileAlterationListener)localIterator.next();
      if (!paramFileEntry.isDirectory()) {
        localFileAlterationListener.onFileChange(paramFile);
      } else {
        localFileAlterationListener.onDirectoryChange(paramFile);
      }
    }
  }
  
  private File[] listFiles(File paramFile)
  {
    File[] arrayOfFile = null;
    if (paramFile.isDirectory()) {
      if (this.fileFilter != null) {
        arrayOfFile = paramFile.listFiles(this.fileFilter);
      } else {
        arrayOfFile = paramFile.listFiles();
      }
    }
    if (arrayOfFile == null) {
      arrayOfFile = FileUtils.EMPTY_FILE_ARRAY;
    }
    if ((this.comparator != null) && (arrayOfFile.length > 1)) {
      Arrays.sort(arrayOfFile, this.comparator);
    }
    return arrayOfFile;
  }
  
  public void addListener(FileAlterationListener paramFileAlterationListener)
  {
    if (paramFileAlterationListener != null) {
      this.listeners.add(paramFileAlterationListener);
    }
  }
  
  public void checkAndNotify()
  {
    Object localObject = this.listeners.iterator();
    for (;;)
    {
      if (!((Iterator)localObject).hasNext())
      {
        localObject = this.rootEntry.getFile();
        if (!((File)localObject).exists())
        {
          if (this.rootEntry.isExists()) {
            checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
          }
        }
        else {
          checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), listFiles((File)localObject));
        }
        localObject = this.listeners.iterator();
        for (;;)
        {
          if (!((Iterator)localObject).hasNext()) {
            return;
          }
          ((FileAlterationListener)((Iterator)localObject).next()).onStop(this);
        }
      }
      ((FileAlterationListener)((Iterator)localObject).next()).onStart(this);
    }
  }
  
  public void destroy()
    throws Exception
  {}
  
  public File getDirectory()
  {
    return this.rootEntry.getFile();
  }
  
  public FileFilter getFileFilter()
  {
    return this.fileFilter;
  }
  
  public Iterable<FileAlterationListener> getListeners()
  {
    return this.listeners;
  }
  
  public void initialize()
    throws Exception
  {
    this.rootEntry.refresh(this.rootEntry.getFile());
    File[] arrayOfFile = listFiles(this.rootEntry.getFile());
    FileEntry[] arrayOfFileEntry;
    if (arrayOfFile.length <= 0) {
      arrayOfFileEntry = FileEntry.EMPTY_ENTRIES;
    } else {
      arrayOfFileEntry = new FileEntry[arrayOfFile.length];
    }
    for (int i = 0;; i++)
    {
      if (i >= arrayOfFile.length)
      {
        this.rootEntry.setChildren(arrayOfFileEntry);
        return;
      }
      arrayOfFileEntry[i] = createFileEntry(this.rootEntry, arrayOfFile[i]);
    }
  }
  
  public void removeListener(FileAlterationListener paramFileAlterationListener)
  {
    while ((paramFileAlterationListener != null) && (this.listeners.remove(paramFileAlterationListener))) {}
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append("[file='");
    localStringBuilder.append(getDirectory().getPath());
    localStringBuilder.append('\'');
    if (this.fileFilter != null)
    {
      localStringBuilder.append(", ");
      localStringBuilder.append(this.fileFilter.toString());
    }
    localStringBuilder.append(", listeners=");
    localStringBuilder.append(this.listeners.size());
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.monitor.FileAlterationObserver
 * JD-Core Version:    0.7.0.1
 */