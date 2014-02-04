package net.simonvt.menudrawer;

import android.view.animation.Interpolator;

class SmoothInterpolator
  implements Interpolator
{
  public float getInterpolation(float paramFloat)
  {
    float f = paramFloat - 1.0F;
    return 1.0F + f * (f * (f * (f * f)));
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.SmoothInterpolator
 * JD-Core Version:    0.7.0.1
 */