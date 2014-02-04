package net.simonvt.menudrawer;

import android.view.animation.Interpolator;

class SinusoidalInterpolator
  implements Interpolator
{
  public float getInterpolation(float paramFloat)
  {
    return (float)(0.5D + 0.5D * Math.sin(3.141592653589793D * paramFloat - 1.570796326794897D));
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.SinusoidalInterpolator
 * JD-Core Version:    0.7.0.1
 */