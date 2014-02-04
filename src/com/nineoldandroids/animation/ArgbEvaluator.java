package com.nineoldandroids.animation;

public class ArgbEvaluator
  implements TypeEvaluator
{
  public Object evaluate(float paramFloat, Object paramObject1, Object paramObject2)
  {
    int m = ((Integer)paramObject1).intValue();
    int j = m >> 24;
    int i = 0xFF & m >> 16;
    int k = 0xFF & m >> 8;
    int i2 = m & 0xFF;
    int i3 = ((Integer)paramObject2).intValue();
    int n = i3 >> 24;
    int i1 = 0xFF & i3 >> 16;
    m = 0xFF & i3 >> 8;
    i3 &= 0xFF;
    return Integer.valueOf(j + (int)(paramFloat * (n - j)) << 24 | i + (int)(paramFloat * (i1 - i)) << 16 | k + (int)(paramFloat * (m - k)) << 8 | i2 + (int)(paramFloat * (i3 - i2)));
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.ArgbEvaluator
 * JD-Core Version:    0.7.0.1
 */