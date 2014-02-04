package com.nineoldandroids.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectiveProperty<T, V>
  extends Property<T, V>
{
  private static final String PREFIX_GET = "get";
  private static final String PREFIX_IS = "is";
  private static final String PREFIX_SET = "set";
  private Field mField;
  private Method mGetter;
  private Method mSetter;
  
  public ReflectiveProperty(Class<T> paramClass, Class<V> paramClass1, String paramString)
  {
    super(paramClass1, paramString);
    char c = Character.toUpperCase(paramString.charAt(0));
    Object localObject1 = paramString.substring(1);
    localObject1 = c + (String)localObject1;
    Object localObject2 = "get" + (String)localObject1;
    String str;
    try
    {
      this.mGetter = paramClass.getMethod((String)localObject2, (Class[])null);
      localObject2 = this.mGetter.getReturnType();
      if (!typesMatch(paramClass1, (Class)localObject2)) {
        throw new NoSuchPropertyException("Underlying type (" + localObject2 + ") " + "does not match Property type (" + paramClass1 + ")");
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException1)
    {
      try
      {
        this.mGetter = paramClass.getDeclaredMethod((String)localObject2, (Class[])null);
        this.mGetter.setAccessible(true);
      }
      catch (NoSuchMethodException localNoSuchMethodException2)
      {
        for (;;)
        {
          localObject2 = "is" + (String)localObject1;
          try
          {
            this.mGetter = paramClass.getMethod((String)localObject2, (Class[])null);
          }
          catch (NoSuchMethodException localNoSuchMethodException3)
          {
            try
            {
              this.mGetter = paramClass.getDeclaredMethod((String)localObject2, (Class[])null);
              this.mGetter.setAccessible(true);
            }
            catch (NoSuchMethodException localNoSuchMethodException4)
            {
              try
              {
                this.mField = paramClass.getField(paramString);
                localObject1 = this.mField.getType();
                if (typesMatch(paramClass1, (Class)localObject1)) {
                  break label397;
                }
                throw new NoSuchPropertyException("Underlying type (" + localObject1 + ") " + "does not match Property type (" + paramClass1 + ")");
              }
              catch (NoSuchFieldException localNoSuchFieldException)
              {
                throw new NoSuchPropertyException("No accessor method or field found for property with name " + paramString);
              }
            }
          }
        }
      }
      str = "set" + (String)localObject1;
    }
    try
    {
      localObject1 = new Class[1];
      localObject1[0] = localObject2;
      this.mSetter = paramClass.getDeclaredMethod(str, (Class[])localObject1);
      this.mSetter.setAccessible(true);
      label397:
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException5)
    {
      break label397;
    }
  }
  
  private boolean typesMatch(Class<V> paramClass, Class paramClass1)
  {
    boolean bool = false;
    if (paramClass1 == paramClass) {
      bool = true;
    } else if ((paramClass1.isPrimitive()) && (((paramClass1 == Float.TYPE) && (paramClass == Float.class)) || ((paramClass1 == Integer.TYPE) && (paramClass == Integer.class)) || ((paramClass1 == Boolean.TYPE) && (paramClass == Boolean.class)) || ((paramClass1 == Long.TYPE) && (paramClass == Long.class)) || ((paramClass1 == Double.TYPE) && (paramClass == Double.class)) || ((paramClass1 == Short.TYPE) && (paramClass == Short.class)) || ((paramClass1 == Byte.TYPE) && (paramClass == Byte.class)) || ((paramClass1 == Character.TYPE) && (paramClass == Character.class)))) {
      bool = true;
    }
    return bool;
  }
  
  public V get(T paramT)
  {
    if (this.mGetter != null) {}
    for (;;)
    {
      try
      {
        Object localObject1 = this.mGetter.invoke(paramT, (Object[])null);
        localObject1 = localObject1;
        return localObject1;
      }
      catch (IllegalAccessException localIllegalAccessException1)
      {
        throw new AssertionError();
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        throw new RuntimeException(localInvocationTargetException.getCause());
      }
      if (this.mField != null) {
        try
        {
          Object localObject2 = this.mField.get(paramT);
          localObject2 = localObject2;
        }
        catch (IllegalAccessException localIllegalAccessException2)
        {
          throw new AssertionError();
        }
      }
    }
    throw new AssertionError();
  }
  
  public boolean isReadOnly()
  {
    boolean bool;
    if ((this.mSetter != null) || (this.mField != null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void set(T paramT, V paramV)
  {
    if (this.mSetter != null) {}
    for (;;)
    {
      try
      {
        Method localMethod = this.mSetter;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = paramV;
        localMethod.invoke(paramT, arrayOfObject);
        return;
      }
      catch (IllegalAccessException localIllegalAccessException1)
      {
        throw new AssertionError();
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        throw new RuntimeException(localInvocationTargetException.getCause());
      }
      if (this.mField != null) {
        try
        {
          this.mField.set(paramT, paramV);
        }
        catch (IllegalAccessException localIllegalAccessException2)
        {
          throw new AssertionError();
        }
      }
    }
    throw new UnsupportedOperationException("Property " + getName() + " is read-only");
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.util.ReflectiveProperty
 * JD-Core Version:    0.7.0.1
 */