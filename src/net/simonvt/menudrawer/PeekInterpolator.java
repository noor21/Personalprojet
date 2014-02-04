package net.simonvt.menudrawer;

import android.view.animation.Interpolator;

class PeekInterpolator
  implements Interpolator
{
  private static final SinusoidalInterpolator SINUSOIDAL_INTERPOLATOR = new SinusoidalInterpolator();
  private static final String TAG = "PeekInterpolator";
  
  public float getInterpolation(float paramFloat)
  {
    float f;
    if (paramFloat >= 0.3333333F)
    {
      if (paramFloat <= 0.6666667F)
      {
        f = 1.0F;
      }
      else
      {
        f = 3.0F * (paramFloat + 0.3333333F - 1.0F);
        f = 1.0F - SINUSOIDAL_INTERPOLATOR.getInterpolation(f);
      }
    }
    else {
      f = SINUSOIDAL_INTERPOLATOR.getInterpolation(paramFloat * 3.0F);
    }
    return f;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.PeekInterpolator
 * JD-Core Version:    0.7.0.1
 */