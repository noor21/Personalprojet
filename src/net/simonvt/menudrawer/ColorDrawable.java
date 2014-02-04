package net.simonvt.menudrawer;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;

class ColorDrawable
  extends Drawable
{
  private final Paint mPaint = new Paint();
  private ColorState mState;
  
  public ColorDrawable()
  {
    this(null);
  }
  
  public ColorDrawable(int paramInt)
  {
    this(null);
    setColor(paramInt);
  }
  
  private ColorDrawable(ColorState paramColorState)
  {
    this.mState = new ColorState(paramColorState);
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (this.mState.mUseColor >>> 24 != 0)
    {
      this.mPaint.setColor(this.mState.mUseColor);
      paramCanvas.drawRect(getBounds(), this.mPaint);
    }
  }
  
  public int getAlpha()
  {
    return this.mState.mUseColor >>> 24;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | this.mState.mChangingConfigurations;
  }
  
  public int getColor()
  {
    return this.mState.mUseColor;
  }
  
  public Drawable.ConstantState getConstantState()
  {
    this.mState.mChangingConfigurations = getChangingConfigurations();
    return this.mState;
  }
  
  public int getOpacity()
  {
    int i;
    switch (this.mState.mUseColor >>> 24)
    {
    default: 
      i = -3;
      break;
    case 0: 
      i = -2;
      break;
    case 255: 
      i = -1;
    }
    return i;
  }
  
  public void setAlpha(int paramInt)
  {
    int i = (paramInt + (paramInt >> 7)) * (this.mState.mBaseColor >>> 24) >> 8;
    int j = this.mState.mUseColor;
    this.mState.mUseColor = (this.mState.mBaseColor << 8 >>> 8 | i << 24);
    if (j != this.mState.mUseColor) {
      invalidateSelf();
    }
  }
  
  public void setColor(int paramInt)
  {
    if ((this.mState.mBaseColor != paramInt) || (this.mState.mUseColor != paramInt))
    {
      invalidateSelf();
      ColorState localColorState = this.mState;
      this.mState.mUseColor = paramInt;
      localColorState.mBaseColor = paramInt;
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {}
  
  static final class ColorState
    extends Drawable.ConstantState
  {
    int mBaseColor;
    int mChangingConfigurations;
    int mUseColor;
    
    ColorState(ColorState paramColorState)
    {
      if (paramColorState != null)
      {
        this.mBaseColor = paramColorState.mBaseColor;
        this.mUseColor = paramColorState.mUseColor;
      }
    }
    
    public int getChangingConfigurations()
    {
      return this.mChangingConfigurations;
    }
    
    public Drawable newDrawable()
    {
      return new ColorDrawable(this, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new ColorDrawable(this, null);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.ColorDrawable
 * JD-Core Version:    0.7.0.1
 */