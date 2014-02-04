package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.lang.reflect.Proxy;

public class ClassLoaderObjectInputStream
  extends ObjectInputStream
{
  private final ClassLoader classLoader;
  
  public ClassLoaderObjectInputStream(ClassLoader paramClassLoader, InputStream paramInputStream)
    throws IOException, StreamCorruptedException
  {
    super(paramInputStream);
    this.classLoader = paramClassLoader;
  }
  
  protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass)
    throws IOException, ClassNotFoundException
  {
    Class localClass = Class.forName(paramObjectStreamClass.getName(), false, this.classLoader);
    if (localClass == null) {
      localClass = super.resolveClass(paramObjectStreamClass);
    }
    return localClass;
  }
  
  protected Class<?> resolveProxyClass(String[] paramArrayOfString)
    throws IOException, ClassNotFoundException
  {
    localObject = new Class[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      localObject[i] = Class.forName(paramArrayOfString[i], false, this.classLoader);
    }
    try
    {
      localObject = Proxy.getProxyClass(this.classLoader, (Class[])localObject);
      localObject = localObject;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        localObject = super.resolveProxyClass(paramArrayOfString);
      }
    }
    return localObject;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.ClassLoaderObjectInputStream
 * JD-Core Version:    0.7.0.1
 */