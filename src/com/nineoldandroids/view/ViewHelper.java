package com.nineoldandroids.view;

import android.view.View;
import com.nineoldandroids.view.animation.AnimatorProxy;

public final class ViewHelper
{
  public static float getAlpha(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getAlpha(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getAlpha();
    }
    return f;
  }
  
  public static float getPivotX(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getPivotX(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getPivotX();
    }
    return f;
  }
  
  public static float getPivotY(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getPivotY(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getPivotY();
    }
    return f;
  }
  
  public static float getRotation(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getRotation(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getRotation();
    }
    return f;
  }
  
  public static float getRotationX(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getRotationX(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getRotationX();
    }
    return f;
  }
  
  public static float getRotationY(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getRotationY(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getRotationY();
    }
    return f;
  }
  
  public static float getScaleX(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getScaleX(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getScaleX();
    }
    return f;
  }
  
  public static float getScaleY(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getScaleY(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getScaleY();
    }
    return f;
  }
  
  public static float getScrollX(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getScrollX(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getScrollX();
    }
    return f;
  }
  
  public static float getScrollY(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getScrollY(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getScrollY();
    }
    return f;
  }
  
  public static float getTranslationX(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getTranslationX(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getTranslationX();
    }
    return f;
  }
  
  public static float getTranslationY(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getTranslationY(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getTranslationY();
    }
    return f;
  }
  
  public static float getX(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getX(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getX();
    }
    return f;
  }
  
  public static float getY(View paramView)
  {
    float f;
    if (!AnimatorProxy.NEEDS_PROXY) {
      f = Honeycomb.getY(paramView);
    } else {
      f = AnimatorProxy.wrap(paramView).getY();
    }
    return f;
  }
  
  public static void setAlpha(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setAlpha(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setAlpha(paramFloat);
    }
  }
  
  public static void setPivotX(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setPivotX(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setPivotX(paramFloat);
    }
  }
  
  public static void setPivotY(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setPivotY(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setPivotY(paramFloat);
    }
  }
  
  public static void setRotation(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setRotation(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setRotation(paramFloat);
    }
  }
  
  public static void setRotationX(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setRotationX(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setRotationX(paramFloat);
    }
  }
  
  public static void setRotationY(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setRotationY(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setRotationY(paramFloat);
    }
  }
  
  public static void setScaleX(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setScaleX(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setScaleX(paramFloat);
    }
  }
  
  public static void setScaleY(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setScaleY(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setScaleY(paramFloat);
    }
  }
  
  public static void setScrollX(View paramView, int paramInt)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setScrollX(paramView, paramInt);
    } else {
      AnimatorProxy.wrap(paramView).setScrollX(paramInt);
    }
  }
  
  public static void setScrollY(View paramView, int paramInt)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setScrollY(paramView, paramInt);
    } else {
      AnimatorProxy.wrap(paramView).setScrollY(paramInt);
    }
  }
  
  public static void setTranslationX(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setTranslationX(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setTranslationX(paramFloat);
    }
  }
  
  public static void setTranslationY(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setTranslationY(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setTranslationY(paramFloat);
    }
  }
  
  public static void setX(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setX(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setX(paramFloat);
    }
  }
  
  public static void setY(View paramView, float paramFloat)
  {
    if (!AnimatorProxy.NEEDS_PROXY) {
      Honeycomb.setY(paramView, paramFloat);
    } else {
      AnimatorProxy.wrap(paramView).setY(paramFloat);
    }
  }
  
  private static final class Honeycomb
  {
    static float getAlpha(View paramView)
    {
      return paramView.getAlpha();
    }
    
    static float getPivotX(View paramView)
    {
      return paramView.getPivotX();
    }
    
    static float getPivotY(View paramView)
    {
      return paramView.getPivotY();
    }
    
    static float getRotation(View paramView)
    {
      return paramView.getRotation();
    }
    
    static float getRotationX(View paramView)
    {
      return paramView.getRotationX();
    }
    
    static float getRotationY(View paramView)
    {
      return paramView.getRotationY();
    }
    
    static float getScaleX(View paramView)
    {
      return paramView.getScaleX();
    }
    
    static float getScaleY(View paramView)
    {
      return paramView.getScaleY();
    }
    
    static float getScrollX(View paramView)
    {
      return paramView.getScrollX();
    }
    
    static float getScrollY(View paramView)
    {
      return paramView.getScrollY();
    }
    
    static float getTranslationX(View paramView)
    {
      return paramView.getTranslationX();
    }
    
    static float getTranslationY(View paramView)
    {
      return paramView.getTranslationY();
    }
    
    static float getX(View paramView)
    {
      return paramView.getX();
    }
    
    static float getY(View paramView)
    {
      return paramView.getY();
    }
    
    static void setAlpha(View paramView, float paramFloat)
    {
      paramView.setAlpha(paramFloat);
    }
    
    static void setPivotX(View paramView, float paramFloat)
    {
      paramView.setPivotX(paramFloat);
    }
    
    static void setPivotY(View paramView, float paramFloat)
    {
      paramView.setPivotY(paramFloat);
    }
    
    static void setRotation(View paramView, float paramFloat)
    {
      paramView.setRotation(paramFloat);
    }
    
    static void setRotationX(View paramView, float paramFloat)
    {
      paramView.setRotationX(paramFloat);
    }
    
    static void setRotationY(View paramView, float paramFloat)
    {
      paramView.setRotationY(paramFloat);
    }
    
    static void setScaleX(View paramView, float paramFloat)
    {
      paramView.setScaleX(paramFloat);
    }
    
    static void setScaleY(View paramView, float paramFloat)
    {
      paramView.setScaleY(paramFloat);
    }
    
    static void setScrollX(View paramView, int paramInt)
    {
      paramView.setScrollX(paramInt);
    }
    
    static void setScrollY(View paramView, int paramInt)
    {
      paramView.setScrollY(paramInt);
    }
    
    static void setTranslationX(View paramView, float paramFloat)
    {
      paramView.setTranslationX(paramFloat);
    }
    
    static void setTranslationY(View paramView, float paramFloat)
    {
      paramView.setTranslationY(paramFloat);
    }
    
    static void setX(View paramView, float paramFloat)
    {
      paramView.setX(paramFloat);
    }
    
    static void setY(View paramView, float paramFloat)
    {
      paramView.setY(paramFloat);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.view.ViewHelper
 * JD-Core Version:    0.7.0.1
 */