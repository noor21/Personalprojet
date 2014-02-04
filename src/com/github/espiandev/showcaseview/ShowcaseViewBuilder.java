package com.github.espiandev.showcaseview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class ShowcaseViewBuilder
{
  private final ShowcaseView showcaseView;
  
  public ShowcaseViewBuilder(Activity paramActivity)
  {
    this.showcaseView = new ShowcaseView(paramActivity);
  }
  
  public ShowcaseViewBuilder(Activity paramActivity, int paramInt)
  {
    this.showcaseView = ((ShowcaseView)paramActivity.getLayoutInflater().inflate(paramInt, null));
  }
  
  public ShowcaseViewBuilder(ShowcaseView paramShowcaseView)
  {
    this.showcaseView = paramShowcaseView;
  }
  
  public ShowcaseViewBuilder animateGesture(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.showcaseView.animateGesture(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    return this;
  }
  
  public ShowcaseView build()
  {
    return this.showcaseView;
  }
  
  public ShowcaseViewBuilder overrideButtonClick(View.OnClickListener paramOnClickListener)
  {
    this.showcaseView.overrideButtonClick(paramOnClickListener);
    return this;
  }
  
  public ShowcaseViewBuilder pointTo(float paramFloat1, float paramFloat2)
  {
    this.showcaseView.pointTo(paramFloat1, paramFloat2);
    return this;
  }
  
  public ShowcaseViewBuilder pointTo(View paramView)
  {
    this.showcaseView.pointTo(paramView);
    return this;
  }
  
  public ShowcaseViewBuilder setShowcaseIndicatorScale(float paramFloat)
  {
    this.showcaseView.setShowcaseIndicatorScale(paramFloat);
    return this;
  }
  
  public ShowcaseViewBuilder setShowcaseItem(int paramInt1, int paramInt2, Activity paramActivity)
  {
    this.showcaseView.setShowcaseItem(paramInt1, paramInt2, paramActivity);
    return this;
  }
  
  public ShowcaseViewBuilder setShowcaseNoView()
  {
    this.showcaseView.setShowcaseNoView();
    return this;
  }
  
  public ShowcaseViewBuilder setShowcasePosition(float paramFloat1, float paramFloat2)
  {
    this.showcaseView.setShowcasePosition(paramFloat1, paramFloat2);
    return this;
  }
  
  public ShowcaseViewBuilder setShowcaseView(View paramView)
  {
    this.showcaseView.setShowcaseView(paramView);
    return this;
  }
  
  public ShowcaseViewBuilder setText(int paramInt1, int paramInt2)
  {
    this.showcaseView.setText(paramInt1, paramInt2);
    return this;
  }
  
  public ShowcaseViewBuilder setText(String paramString1, String paramString2)
  {
    this.showcaseView.setText(paramString1, paramString2);
    return this;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.github.espiandev.showcaseview.ShowcaseViewBuilder
 * JD-Core Version:    0.7.0.1
 */