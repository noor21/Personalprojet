package org.apache.commons.io.monitor;

import java.io.File;
import java.io.Serializable;

public class FileEntry
  implements Serializable
{
  static final FileEntry[] EMPTY_ENTRIES = new FileEntry[0];
  private FileEntry[] children;
  private boolean directory;
  private boolean exists;
  private final File file;
  private long lastModified;
  private long length;
  private String name;
  private final FileEntry parent;
  
  public FileEntry(File paramFile)
  {
    this((FileEntry)null, paramFile);
  }
  
  public FileEntry(FileEntry paramFileEntry, File paramFile)
  {
    if (paramFile != null)
    {
      this.file = paramFile;
      this.parent = paramFileEntry;
      this.name = paramFile.getName();
      return;
    }
    throw new IllegalArgumentException("File is missing");
  }
  
  public FileEntry[] getChildren()
  {
    FileEntry[] arrayOfFileEntry;
    if (this.children == null) {
      arrayOfFileEntry = EMPTY_ENTRIES;
    } else {
      arrayOfFileEntry = this.children;
    }
    return arrayOfFileEntry;
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  public long getLastModified()
  {
    return this.lastModified;
  }
  
  public long getLength()
  {
    return this.length;
  }
  
  public int getLevel()
  {
    int i;
    if (this.parent != null) {
      i = 1 + this.parent.getLevel();
    } else {
      i = 0;
    }
    return i;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public FileEntry getParent()
  {
    return this.parent;
  }
  
  public boolean isDirectory()
  {
    return this.directory;
  }
  
  public boolean isExists()
  {
    return this.exists;
  }
  
  public FileEntry newChildInstance(File paramFile)
  {
    return new FileEntry(this, paramFile);
  }
  
  public boolean refresh(File paramFile)
  {
    long l3 = 0L;
    boolean bool1 = false;
    boolean bool2 = this.exists;
    long l1 = this.lastModified;
    boolean bool3 = this.directory;
    long l2 = this.length;
    this.name = paramFile.getName();
    this.exists = paramFile.exists();
    boolean bool4;
    if (!this.exists) {
      bool4 = false;
    } else {
      bool4 = paramFile.isDirectory();
    }
    this.directory = bool4;
    long l4;
    if (!this.exists) {
      l4 = l3;
    } else {
      l4 = paramFile.lastModified();
    }
    this.lastModified = l4;
    if ((this.exists) && (!this.directory)) {
      l3 = paramFile.length();
    }
    this.length = l3;
    if ((this.exists != bool2) || (this.lastModified != l1) || (this.directory != bool3) || (this.length != l2)) {
      bool1 = true;
    }
    return bool1;
  }
  
  public void setChildren(FileEntry[] paramArrayOfFileEntry)
  {
    this.children = paramArrayOfFileEntry;
  }
  
  public void setDirectory(boolean paramBoolean)
  {
    this.directory = paramBoolean;
  }
  
  public void setExists(boolean paramBoolean)
  {
    this.exists = paramBoolean;
  }
  
  public void setLastModified(long paramLong)
  {
    this.lastModified = paramLong;
  }
  
  public void setLength(long paramLong)
  {
    this.length = paramLong;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.monitor.FileEntry
 * JD-Core Version:    0.7.0.1
 */