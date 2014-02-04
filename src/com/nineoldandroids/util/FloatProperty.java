package com.nineoldandroids.util;

public abstract class FloatProperty<T>
  extends Property<T, Float>
{
  public FloatProperty(String paramString)
  {
    super(Float.class, paramString);
  }
  
  public final void set(T paramT, Float paramFloat)
  {
    setValue(paramT, paramFloat.floatValue());
  }
  
  public abstract void setValue(T paramT, float paramFloat);
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.util.FloatProperty
 * JD-Core Version:    0.7.0.1
 */