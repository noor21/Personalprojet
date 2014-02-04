package org.apache.commons.io.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class DeferredFileOutputStream
  extends ThresholdingOutputStream
{
  private boolean closed = false;
  private OutputStream currentOutputStream;
  private final File directory;
  private ByteArrayOutputStream memoryOutputStream;
  private File outputFile;
  private final String prefix;
  private final String suffix;
  
  public DeferredFileOutputStream(int paramInt, File paramFile)
  {
    this(paramInt, paramFile, null, null, null);
  }
  
  private DeferredFileOutputStream(int paramInt, File paramFile1, String paramString1, String paramString2, File paramFile2)
  {
    super(paramInt);
    this.outputFile = paramFile1;
    this.memoryOutputStream = new ByteArrayOutputStream();
    this.currentOutputStream = this.memoryOutputStream;
    this.prefix = paramString1;
    this.suffix = paramString2;
    this.directory = paramFile2;
  }
  
  public DeferredFileOutputStream(int paramInt, String paramString1, String paramString2, File paramFile)
  {
    this(paramInt, null, paramString1, paramString2, paramFile);
    if (paramString1 != null) {
      return;
    }
    throw new IllegalArgumentException("Temporary file prefix is missing");
  }
  
  public void close()
    throws IOException
  {
    super.close();
    this.closed = true;
  }
  
  public byte[] getData()
  {
    byte[] arrayOfByte;
    if (this.memoryOutputStream == null) {
      arrayOfByte = null;
    } else {
      arrayOfByte = this.memoryOutputStream.toByteArray();
    }
    return arrayOfByte;
  }
  
  public File getFile()
  {
    return this.outputFile;
  }
  
  protected OutputStream getStream()
    throws IOException
  {
    return this.currentOutputStream;
  }
  
  public boolean isInMemory()
  {
    boolean bool;
    if (isThresholdExceeded()) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  protected void thresholdReached()
    throws IOException
  {
    if (this.prefix != null) {
      this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory);
    }
    FileOutputStream localFileOutputStream = new FileOutputStream(this.outputFile);
    this.memoryOutputStream.writeTo(localFileOutputStream);
    this.currentOutputStream = localFileOutputStream;
    this.memoryOutputStream = null;
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    if (!this.closed) {
      throw new IOException("Stream not closed");
    }
    if (isInMemory()) {
      this.memoryOutputStream.writeTo(paramOutputStream);
    }
    for (;;)
    {
      return;
      FileInputStream localFileInputStream = new FileInputStream(this.outputFile);
      try
      {
        IOUtils.copy(localFileInputStream, paramOutputStream);
        IOUtils.closeQuietly(localFileInputStream);
      }
      finally
      {
        IOUtils.closeQuietly(localFileInputStream);
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.DeferredFileOutputStream
 * JD-Core Version:    0.7.0.1
 */