package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class WriterOutputStream
  extends OutputStream
{
  private static final int DEFAULT_BUFFER_SIZE = 1024;
  private final CharsetDecoder decoder;
  private final ByteBuffer decoderIn = ByteBuffer.allocate(128);
  private final CharBuffer decoderOut;
  private final boolean writeImmediately;
  private final Writer writer;
  
  public WriterOutputStream(Writer paramWriter)
  {
    this(paramWriter, Charset.defaultCharset(), 1024, false);
  }
  
  public WriterOutputStream(Writer paramWriter, String paramString)
  {
    this(paramWriter, paramString, 1024, false);
  }
  
  public WriterOutputStream(Writer paramWriter, String paramString, int paramInt, boolean paramBoolean)
  {
    this(paramWriter, Charset.forName(paramString), paramInt, paramBoolean);
  }
  
  public WriterOutputStream(Writer paramWriter, Charset paramCharset)
  {
    this(paramWriter, paramCharset, 1024, false);
  }
  
  public WriterOutputStream(Writer paramWriter, Charset paramCharset, int paramInt, boolean paramBoolean)
  {
    this(paramWriter, paramCharset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), paramInt, paramBoolean);
  }
  
  public WriterOutputStream(Writer paramWriter, CharsetDecoder paramCharsetDecoder)
  {
    this(paramWriter, paramCharsetDecoder, 1024, false);
  }
  
  public WriterOutputStream(Writer paramWriter, CharsetDecoder paramCharsetDecoder, int paramInt, boolean paramBoolean)
  {
    this.writer = paramWriter;
    this.decoder = paramCharsetDecoder;
    this.writeImmediately = paramBoolean;
    this.decoderOut = CharBuffer.allocate(paramInt);
  }
  
  private void flushOutput()
    throws IOException
  {
    if (this.decoderOut.position() > 0)
    {
      this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
      this.decoderOut.rewind();
    }
  }
  
  private void processInput(boolean paramBoolean)
    throws IOException
  {
    this.decoderIn.flip();
    for (;;)
    {
      CoderResult localCoderResult = this.decoder.decode(this.decoderIn, this.decoderOut, paramBoolean);
      if (!localCoderResult.isOverflow())
      {
        if (!localCoderResult.isUnderflow()) {
          throw new IOException("Unexpected coder result");
        }
        this.decoderIn.compact();
        return;
      }
      flushOutput();
    }
  }
  
  public void close()
    throws IOException
  {
    processInput(true);
    flushOutput();
    this.writer.close();
  }
  
  public void flush()
    throws IOException
  {
    flushOutput();
    this.writer.flush();
  }
  
  public void write(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    arrayOfByte[0] = ((byte)paramInt);
    write(arrayOfByte, 0, 1);
  }
  
  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    for (;;)
    {
      if (paramInt2 <= 0)
      {
        if (this.writeImmediately) {
          flushOutput();
        }
        return;
      }
      int i = Math.min(paramInt2, this.decoderIn.remaining());
      this.decoderIn.put(paramArrayOfByte, paramInt1, i);
      processInput(false);
      paramInt2 -= i;
      paramInt1 += i;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.WriterOutputStream
 * JD-Core Version:    0.7.0.1
 */