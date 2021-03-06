package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout
  extends ViewGroup
{
  private static final int DEFAULT_FADE_COLOR = -858993460;
  private static final int DEFAULT_OVERHANG_SIZE = 32;
  static final SlidingPanelLayoutImpl IMPL;
  private static final int MIN_FLING_VELOCITY = 400;
  private static final String TAG = "SlidingPaneLayout";
  private boolean mCanSlide;
  private int mCoveredFadeColor;
  private final ViewDragHelper mDragHelper;
  private boolean mFirstLayout = true;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private boolean mIsUnableToDrag;
  private final int mOverhangSize;
  private PanelSlideListener mPanelSlideListener;
  private int mParallaxBy;
  private float mParallaxOffset;
  private final ArrayList<DisableLayerRunnable> mPostedRunnables = new ArrayList();
  private boolean mPreservedOpenState;
  private Drawable mShadowDrawable;
  private float mSlideOffset;
  private int mSlideRange;
  private View mSlideableView;
  private int mSliderFadeColor = -858993460;
  private final Rect mTmpRect = new Rect();
  
  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i < 17)
    {
      if (i < 16) {
        IMPL = new SlidingPanelLayoutImplBase();
      } else {
        IMPL = new SlidingPanelLayoutImplJB();
      }
    }
    else {
      IMPL = new SlidingPanelLayoutImplJBMR1();
    }
  }
  
  public SlidingPaneLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    float f = paramContext.getResources().getDisplayMetrics().density;
    this.mOverhangSize = ((int)(0.5F + 32.0F * f));
    ViewConfiguration.get(paramContext);
    setWillNotDraw(false);
    ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
    ViewCompat.setImportantForAccessibility(this, 1);
    this.mDragHelper = ViewDragHelper.create(this, 0.5F, new DragHelperCallback(null));
    this.mDragHelper.setEdgeTrackingEnabled(1);
    this.mDragHelper.setMinVelocity(400.0F * f);
  }
  
  private boolean closePane(View paramView, int paramInt)
  {
    boolean bool = false;
    if ((this.mFirstLayout) || (smoothSlideTo(0.0F, paramInt)))
    {
      this.mPreservedOpenState = false;
      bool = true;
    }
    return bool;
  }
  
  private void dimChildView(View paramView, float paramFloat, int paramInt)
  {
    Object localObject = (LayoutParams)paramView.getLayoutParams();
    if ((paramFloat <= 0.0F) || (paramInt == 0))
    {
      if (ViewCompat.getLayerType(paramView) != 0)
      {
        if (((LayoutParams)localObject).dimPaint != null) {
          ((LayoutParams)localObject).dimPaint.setColorFilter(null);
        }
        localObject = new DisableLayerRunnable(paramView);
        this.mPostedRunnables.add(localObject);
        ViewCompat.postOnAnimation(this, (Runnable)localObject);
      }
    }
    else
    {
      int i = (int)(paramFloat * ((0xFF000000 & paramInt) >>> 24)) << 24 | 0xFFFFFF & paramInt;
      if (((LayoutParams)localObject).dimPaint == null) {
        ((LayoutParams)localObject).dimPaint = new Paint();
      }
      ((LayoutParams)localObject).dimPaint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_OVER));
      if (ViewCompat.getLayerType(paramView) != 2) {
        ViewCompat.setLayerType(paramView, 2, ((LayoutParams)localObject).dimPaint);
      }
      invalidateChildRegion(paramView);
    }
  }
  
  private static boolean hasOpaqueBackground(View paramView)
  {
    boolean bool = false;
    Drawable localDrawable = paramView.getBackground();
    if ((localDrawable != null) && (localDrawable.getOpacity() == -1)) {
      bool = true;
    }
    return bool;
  }
  
  private void invalidateChildRegion(View paramView)
  {
    IMPL.invalidateChildRegion(this, paramView);
  }
  
  private void onPanelDragged(int paramInt)
  {
    LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    this.mSlideOffset = ((paramInt - (getPaddingLeft() + localLayoutParams.leftMargin)) / this.mSlideRange);
    if (this.mParallaxBy != 0) {
      parallaxOtherViews(this.mSlideOffset);
    }
    if (localLayoutParams.dimWhenOffset) {
      dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
    }
    dispatchOnPanelSlide(this.mSlideableView);
  }
  
  private boolean openPane(View paramView, int paramInt)
  {
    boolean bool = true;
    if ((!this.mFirstLayout) && (!smoothSlideTo(1.0F, paramInt))) {
      bool = false;
    } else {
      this.mPreservedOpenState = bool;
    }
    return bool;
  }
  
  private void parallaxOtherViews(float paramFloat)
  {
    LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int m;
    if ((!localLayoutParams.dimWhenOffset) || (localLayoutParams.leftMargin > 0)) {
      m = 0;
    } else {
      m = 1;
    }
    int j = getChildCount();
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      View localView = getChildAt(i);
      if (localView != this.mSlideableView)
      {
        int k = (int)((1.0F - this.mParallaxOffset) * this.mParallaxBy);
        this.mParallaxOffset = paramFloat;
        localView.offsetLeftAndRight(k - (int)((1.0F - paramFloat) * this.mParallaxBy));
        if (m != 0) {
          dimChildView(localView, 1.0F - this.mParallaxOffset, this.mCoveredFadeColor);
        }
      }
    }
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    ViewGroup localViewGroup;
    int j;
    if ((paramView instanceof ViewGroup))
    {
      localViewGroup = (ViewGroup)paramView;
      i = paramView.getScrollX();
      j = paramView.getScrollY();
    }
    for (int k = -1 + localViewGroup.getChildCount();; k--)
    {
      if (k < 0)
      {
        if ((!paramBoolean) || (!ViewCompat.canScrollHorizontally(paramView, -paramInt1)))
        {
          i = 0;
          break label168;
        }
        i = 1;
        break label168;
      }
      View localView = localViewGroup.getChildAt(k);
      if ((paramInt2 + i >= localView.getLeft()) && (paramInt2 + i < localView.getRight()) && (paramInt3 + j >= localView.getTop()) && (paramInt3 + j < localView.getBottom()) && (canScroll(localView, true, paramInt1, paramInt2 + i - localView.getLeft(), paramInt3 + j - localView.getTop()))) {
        break;
      }
    }
    int i = 1;
    label168:
    return i;
  }
  
  @Deprecated
  public boolean canSlide()
  {
    return this.mCanSlide;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    boolean bool;
    if ((!(paramLayoutParams instanceof LayoutParams)) || (!super.checkLayoutParams(paramLayoutParams))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean closePane()
  {
    return closePane(this.mSlideableView, 0);
  }
  
  public void computeScroll()
  {
    if (this.mDragHelper.continueSettling(true)) {
      if (this.mCanSlide) {
        ViewCompat.postInvalidateOnAnimation(this);
      } else {
        this.mDragHelper.abort();
      }
    }
  }
  
  void dispatchOnPanelClosed(View paramView)
  {
    if (this.mPanelSlideListener != null) {
      this.mPanelSlideListener.onPanelClosed(paramView);
    }
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelOpened(View paramView)
  {
    if (this.mPanelSlideListener != null) {
      this.mPanelSlideListener.onPanelOpened(paramView);
    }
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelSlide(View paramView)
  {
    if (this.mPanelSlideListener != null) {
      this.mPanelSlideListener.onPanelSlide(paramView, this.mSlideOffset);
    }
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    View localView;
    if (getChildCount() <= 1) {
      localView = null;
    } else {
      localView = getChildAt(1);
    }
    if ((localView != null) && (this.mShadowDrawable != null))
    {
      int k = this.mShadowDrawable.getIntrinsicWidth();
      int i = localView.getLeft();
      int j = localView.getTop();
      int m = localView.getBottom();
      k = i - k;
      this.mShadowDrawable.setBounds(k, j, i, m);
      this.mShadowDrawable.draw(paramCanvas);
    }
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramCanvas.save(2);
    if ((this.mCanSlide) && (!localLayoutParams.slideable) && (this.mSlideableView != null))
    {
      paramCanvas.getClipBounds(this.mTmpRect);
      this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
      paramCanvas.clipRect(this.mTmpRect);
    }
    boolean bool2;
    if (Build.VERSION.SDK_INT < 11)
    {
      if ((!localLayoutParams.dimWhenOffset) || (this.mSlideOffset <= 0.0F))
      {
        if (paramView.isDrawingCacheEnabled()) {
          paramView.setDrawingCacheEnabled(false);
        }
        boolean bool1 = super.drawChild(paramCanvas, paramView, paramLong);
      }
      else
      {
        if (!paramView.isDrawingCacheEnabled()) {
          paramView.setDrawingCacheEnabled(true);
        }
        Bitmap localBitmap = paramView.getDrawingCache();
        if (localBitmap == null)
        {
          Log.e("SlidingPaneLayout", "drawChild: child view " + paramView + " returned null drawing cache");
          bool2 = super.drawChild(paramCanvas, paramView, paramLong);
        }
        else
        {
          paramCanvas.drawBitmap(bool2, paramView.getLeft(), paramView.getTop(), localLayoutParams.dimPaint);
          bool2 = false;
        }
      }
    }
    else {
      bool2 = super.drawChild(paramCanvas, paramView, paramLong);
    }
    paramCanvas.restoreToCount(i);
    return bool2;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    LayoutParams localLayoutParams;
    if (!(paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
      localLayoutParams = new LayoutParams(paramLayoutParams);
    } else {
      localLayoutParams = new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
    }
    return localLayoutParams;
  }
  
  public int getCoveredFadeColor()
  {
    return this.mCoveredFadeColor;
  }
  
  public int getParallaxDistance()
  {
    return this.mParallaxBy;
  }
  
  public int getSliderFadeColor()
  {
    return this.mSliderFadeColor;
  }
  
  boolean isDimmed(View paramView)
  {
    boolean bool = false;
    if (paramView != null)
    {
      LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
      if ((this.mCanSlide) && (localLayoutParams.dimWhenOffset) && (this.mSlideOffset > 0.0F)) {
        bool = true;
      }
    }
    return bool;
  }
  
  public boolean isOpen()
  {
    boolean bool;
    if ((this.mCanSlide) && (this.mSlideOffset != 1.0F)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isSlideable()
  {
    return this.mCanSlide;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
    int j = 0;
    int i = this.mPostedRunnables.size();
    for (;;)
    {
      if (j >= i)
      {
        this.mPostedRunnables.clear();
        return;
      }
      ((DisableLayerRunnable)this.mPostedRunnables.get(j)).run();
      j++;
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    boolean bool;
    if ((!this.mCanSlide) && (i == 0) && (getChildCount() > 1))
    {
      View localView = getChildAt(1);
      if (localView != null)
      {
        if (this.mDragHelper.isViewUnder(localView, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY())) {
          bool = false;
        } else {
          bool = true;
        }
        this.mPreservedOpenState = bool;
      }
    }
    if ((this.mCanSlide) && ((!this.mIsUnableToDrag) || (i == 0)))
    {
      if ((i != 3) && (i != 1))
      {
        bool = false;
        float f2;
        float f1;
        switch (i)
        {
        case 0: 
          this.mIsUnableToDrag = false;
          f2 = paramMotionEvent.getX();
          f1 = paramMotionEvent.getY();
          this.mInitialMotionX = f2;
          this.mInitialMotionY = f1;
          if ((this.mDragHelper.isViewUnder(this.mSlideableView, (int)f2, (int)f1)) && (isDimmed(this.mSlideableView))) {
            bool = true;
          }
          break;
        case 2: 
          f2 = paramMotionEvent.getX();
          f1 = paramMotionEvent.getY();
          f2 = Math.abs(f2 - this.mInitialMotionX);
          f1 = Math.abs(f1 - this.mInitialMotionY);
          if ((f2 > this.mDragHelper.getTouchSlop()) && (f1 > f2)) {
            break label265;
          }
        }
        if ((!this.mDragHelper.shouldInterceptTouchEvent(paramMotionEvent)) && (!bool))
        {
          bool = false;
        }
        else
        {
          return true;
          label265:
          this.mDragHelper.cancel();
          this.mIsUnableToDrag = true;
          bool = false;
        }
      }
      else
      {
        this.mDragHelper.cancel();
        bool = false;
      }
    }
    else
    {
      this.mDragHelper.cancel();
      bool = super.onInterceptTouchEvent(paramMotionEvent);
    }
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int j = paramInt3 - paramInt1;
    int n = getPaddingLeft();
    int m = getPaddingRight();
    int k = getPaddingTop();
    int i = getChildCount();
    int i1 = n;
    n = i1;
    if (this.mFirstLayout)
    {
      float f;
      if ((!this.mCanSlide) || (!this.mPreservedOpenState)) {
        f = 0.0F;
      } else {
        f = 1.0F;
      }
      this.mSlideOffset = f;
    }
    for (int i3 = 0;; i3++)
    {
      if (i3 >= i)
      {
        if (this.mFirstLayout)
        {
          if (!this.mCanSlide) {
            for (j = 0; j < i; j++) {
              dimChildView(getChildAt(j), 0.0F, this.mSliderFadeColor);
            }
          }
          if (this.mParallaxBy != 0) {
            parallaxOtherViews(this.mSlideOffset);
          }
          if (((LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset) {
            dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
          }
          updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
        return;
      }
      View localView = getChildAt(i3);
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        int i2 = localView.getMeasuredWidth();
        int i4 = 0;
        if (!localLayoutParams.slideable)
        {
          if ((!this.mCanSlide) || (this.mParallaxBy == 0))
          {
            i1 = n;
          }
          else
          {
            i4 = (int)((1.0F - this.mSlideOffset) * this.mParallaxBy);
            i1 = n;
          }
        }
        else
        {
          int i5 = localLayoutParams.leftMargin + localLayoutParams.rightMargin;
          int i6 = Math.min(n, j - m - this.mOverhangSize) - i1 - i5;
          this.mSlideRange = i6;
          if (i6 + (i1 + localLayoutParams.leftMargin) + i2 / 2 <= j - m) {
            i5 = 0;
          } else {
            i5 = 1;
          }
          localLayoutParams.dimWhenOffset = i5;
          i1 += (int)(i6 * this.mSlideOffset) + localLayoutParams.leftMargin;
        }
        i4 = i1 - i4;
        localView.layout(i4, k, i4 + i2, k + localView.getMeasuredHeight());
        n += localView.getWidth();
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int j = View.MeasureSpec.getMode(paramInt1);
    int i = View.MeasureSpec.getSize(paramInt1);
    int i2 = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    if (j == 1073741824)
    {
      if (i2 != 0)
      {
        j = 0;
        int k = -1;
        switch (i2)
        {
        case -2147483648: 
          k = m - getPaddingTop() - getPaddingBottom();
          break;
        case 1073741824: 
          k = m - getPaddingTop() - getPaddingBottom();
          j = k;
        }
        float f = 0.0F;
        m = 0;
        int n = i - getPaddingLeft() - getPaddingRight();
        int i1 = getChildCount();
        if (i1 > 2) {
          Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
        }
        this.mSlideableView = null;
        for (int i4 = 0;; i4++)
        {
          int i5;
          int i6;
          if (i4 >= i1)
          {
            int i3;
            if ((m != 0) || (f > 0.0F)) {
              i3 = i - this.mOverhangSize;
            }
            for (i2 = 0;; i2++)
            {
              if (i2 >= i1)
              {
                setMeasuredDimension(i, j);
                this.mCanSlide = m;
                if ((this.mDragHelper.getViewDragState() != 0) && (m == 0)) {
                  this.mDragHelper.abort();
                }
                return;
              }
              localView = getChildAt(i2);
              if (localView.getVisibility() != 8)
              {
                LayoutParams localLayoutParams2 = (LayoutParams)localView.getLayoutParams();
                if ((localLayoutParams2.width != 0) || (localLayoutParams2.weight <= 0.0F)) {
                  i5 = 0;
                } else {
                  i5 = 1;
                }
                if (i5 == 0) {
                  i4 = localView.getMeasuredWidth();
                } else {
                  i4 = 0;
                }
                if ((m == 0) || (localView == this.mSlideableView))
                {
                  if (localLayoutParams2.weight > 0.0F)
                  {
                    if (localLayoutParams2.width != 0) {
                      i5 = View.MeasureSpec.makeMeasureSpec(localView.getMeasuredHeight(), 1073741824);
                    } else if (localLayoutParams2.height != -2)
                    {
                      if (localLayoutParams2.height != -1) {
                        i5 = View.MeasureSpec.makeMeasureSpec(localLayoutParams2.height, 1073741824);
                      } else {
                        i5 = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
                      }
                    }
                    else {
                      i5 = View.MeasureSpec.makeMeasureSpec(k, -2147483648);
                    }
                    int i7;
                    if (m == 0)
                    {
                      i7 = Math.max(0, n);
                      localView.measure(View.MeasureSpec.makeMeasureSpec(i4 + (int)(localLayoutParams2.weight * i7 / f), 1073741824), i5);
                    }
                    else
                    {
                      i7 = i - (localLayoutParams2.leftMargin + localLayoutParams2.rightMargin);
                      i6 = View.MeasureSpec.makeMeasureSpec(i7, 1073741824);
                      if (i4 != i7) {
                        localView.measure(i6, i5);
                      }
                    }
                  }
                }
                else if ((i6.width < 0) && ((i4 > i3) || (i6.weight > 0.0F)))
                {
                  if (i5 == 0) {
                    i4 = View.MeasureSpec.makeMeasureSpec(localView.getMeasuredHeight(), 1073741824);
                  } else if (i6.height != -2)
                  {
                    if (i6.height != -1) {
                      i4 = View.MeasureSpec.makeMeasureSpec(i6.height, 1073741824);
                    } else {
                      i4 = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
                    }
                  }
                  else {
                    i4 = View.MeasureSpec.makeMeasureSpec(k, -2147483648);
                  }
                  localView.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), i4);
                }
              }
            }
          }
          View localView = getChildAt(i4);
          LayoutParams localLayoutParams1 = (LayoutParams)localView.getLayoutParams();
          if (localView.getVisibility() != 8)
          {
            if (localLayoutParams1.weight > 0.0F)
            {
              f += localLayoutParams1.weight;
              if (localLayoutParams1.width == 0) {}
            }
            else
            {
              i5 = localLayoutParams1.leftMargin + localLayoutParams1.rightMargin;
              if (localLayoutParams1.width != -2)
              {
                if (localLayoutParams1.width != -1) {
                  i6 = View.MeasureSpec.makeMeasureSpec(localLayoutParams1.width, 1073741824);
                } else {
                  i6 = View.MeasureSpec.makeMeasureSpec(i - i5, 1073741824);
                }
              }
              else {
                i6 = View.MeasureSpec.makeMeasureSpec(i - i5, -2147483648);
              }
              if (localLayoutParams1.height != -2)
              {
                if (localLayoutParams1.height != -1) {
                  i5 = View.MeasureSpec.makeMeasureSpec(localLayoutParams1.height, 1073741824);
                } else {
                  i5 = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
                }
              }
              else {
                i5 = View.MeasureSpec.makeMeasureSpec(k, -2147483648);
              }
              localView.measure(i6, i5);
              i5 = localView.getMeasuredWidth();
              i6 = localView.getMeasuredHeight();
              if ((i2 == -2147483648) && (i6 > j)) {
                j = Math.min(i6, k);
              }
              n -= i5;
              if (n >= 0) {
                i5 = 0;
              } else {
                i5 = 1;
              }
              localLayoutParams1.slideable = i5;
              m |= i5;
              if (localLayoutParams1.slideable) {
                this.mSlideableView = localView;
              }
            }
          }
          else {
            localLayoutParams1.dimWhenOffset = false;
          }
        }
      }
      throw new IllegalStateException("Height must not be UNSPECIFIED");
    }
    throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    if (!localSavedState.isOpen) {
      closePane();
    } else {
      openPane();
    }
    this.mPreservedOpenState = localSavedState.isOpen;
  }
  
  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    boolean bool;
    if (!isSlideable()) {
      bool = this.mPreservedOpenState;
    } else {
      bool = isOpen();
    }
    localSavedState.isOpen = bool;
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      this.mFirstLayout = true;
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool;
    if (this.mCanSlide)
    {
      this.mDragHelper.processTouchEvent(paramMotionEvent);
      int i = paramMotionEvent.getAction();
      bool = true;
      float f1;
      switch (i & 0xFF)
      {
      default: 
        break;
      case 0: 
        f1 = paramMotionEvent.getX();
        float f2 = paramMotionEvent.getY();
        this.mInitialMotionX = f1;
        this.mInitialMotionY = f2;
        break;
      case 1: 
        if (!isDimmed(this.mSlideableView)) {
          break;
        }
        float f5 = paramMotionEvent.getX();
        float f4 = paramMotionEvent.getY();
        f1 = f5 - this.mInitialMotionX;
        float f3 = f4 - this.mInitialMotionY;
        int j = this.mDragHelper.getTouchSlop();
        if ((f1 * f1 + f3 * f3 >= j * j) || (!this.mDragHelper.isViewUnder(this.mSlideableView, (int)f5, (int)f4))) {
          break;
        }
        closePane(this.mSlideableView, 0);
        break;
      }
    }
    else
    {
      bool = super.onTouchEvent(paramMotionEvent);
    }
    return bool;
  }
  
  public boolean openPane()
  {
    return openPane(this.mSlideableView, 0);
  }
  
  public void requestChildFocus(View paramView1, View paramView2)
  {
    super.requestChildFocus(paramView1, paramView2);
    if ((!isInTouchMode()) && (!this.mCanSlide))
    {
      boolean bool;
      if (paramView1 != this.mSlideableView) {
        bool = false;
      } else {
        bool = true;
      }
      this.mPreservedOpenState = bool;
    }
  }
  
  void setAllChildrenVisible()
  {
    int i = 0;
    int j = getChildCount();
    for (;;)
    {
      if (i >= j) {
        return;
      }
      View localView = getChildAt(i);
      if (localView.getVisibility() == 4) {
        localView.setVisibility(0);
      }
      i++;
    }
  }
  
  public void setCoveredFadeColor(int paramInt)
  {
    this.mCoveredFadeColor = paramInt;
  }
  
  public void setPanelSlideListener(PanelSlideListener paramPanelSlideListener)
  {
    this.mPanelSlideListener = paramPanelSlideListener;
  }
  
  public void setParallaxDistance(int paramInt)
  {
    this.mParallaxBy = paramInt;
    requestLayout();
  }
  
  public void setShadowDrawable(Drawable paramDrawable)
  {
    this.mShadowDrawable = paramDrawable;
  }
  
  public void setShadowResource(int paramInt)
  {
    setShadowDrawable(getResources().getDrawable(paramInt));
  }
  
  public void setSliderFadeColor(int paramInt)
  {
    this.mSliderFadeColor = paramInt;
  }
  
  @Deprecated
  public void smoothSlideClosed()
  {
    closePane();
  }
  
  @Deprecated
  public void smoothSlideOpen()
  {
    openPane();
  }
  
  boolean smoothSlideTo(float paramFloat, int paramInt)
  {
    boolean bool = false;
    if (this.mCanSlide)
    {
      LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
      int i = (int)(getPaddingLeft() + localLayoutParams.leftMargin + paramFloat * this.mSlideRange);
      if (this.mDragHelper.smoothSlideViewTo(this.mSlideableView, i, this.mSlideableView.getTop()))
      {
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        bool = true;
      }
    }
    return bool;
  }
  
  void updateObscuredViewsVisibility(View paramView)
  {
    int k = getPaddingLeft();
    int i = getWidth() - getPaddingRight();
    int i2 = getPaddingTop();
    int i4 = getHeight() - getPaddingBottom();
    int i5;
    int n;
    int m;
    int j;
    if ((paramView == null) || (!hasOpaqueBackground(paramView)))
    {
      i5 = 0;
      n = 0;
      m = 0;
      j = 0;
    }
    else
    {
      j = paramView.getLeft();
      m = paramView.getRight();
      n = paramView.getTop();
      i5 = paramView.getBottom();
    }
    int i3 = 0;
    int i1 = getChildCount();
    while (i3 < i1)
    {
      View localView = getChildAt(i3);
      if (localView == paramView) {
        break;
      }
      int i6 = Math.max(k, localView.getLeft());
      int i9 = Math.max(i2, localView.getTop());
      int i8 = Math.min(i, localView.getRight());
      int i7 = Math.min(i4, localView.getBottom());
      if ((i6 < j) || (i9 < n) || (i8 > m) || (i7 > i5)) {
        i6 = 0;
      } else {
        i6 = 4;
      }
      localView.setVisibility(i6);
      i3++;
    }
  }
  
  private class DisableLayerRunnable
    implements Runnable
  {
    final View mChildView;
    
    DisableLayerRunnable(View paramView)
    {
      this.mChildView = paramView;
    }
    
    public void run()
    {
      if (this.mChildView.getParent() == SlidingPaneLayout.this)
      {
        ViewCompat.setLayerType(this.mChildView, 0, null);
        SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
      }
      SlidingPaneLayout.this.mPostedRunnables.remove(this);
    }
  }
  
  class AccessibilityDelegate
    extends AccessibilityDelegateCompat
  {
    private final Rect mTmpRect = new Rect();
    
    AccessibilityDelegate() {}
    
    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat2)
    {
      Rect localRect = this.mTmpRect;
      paramAccessibilityNodeInfoCompat2.getBoundsInParent(localRect);
      paramAccessibilityNodeInfoCompat1.setBoundsInParent(localRect);
      paramAccessibilityNodeInfoCompat2.getBoundsInScreen(localRect);
      paramAccessibilityNodeInfoCompat1.setBoundsInScreen(localRect);
      paramAccessibilityNodeInfoCompat1.setVisibleToUser(paramAccessibilityNodeInfoCompat2.isVisibleToUser());
      paramAccessibilityNodeInfoCompat1.setPackageName(paramAccessibilityNodeInfoCompat2.getPackageName());
      paramAccessibilityNodeInfoCompat1.setClassName(paramAccessibilityNodeInfoCompat2.getClassName());
      paramAccessibilityNodeInfoCompat1.setContentDescription(paramAccessibilityNodeInfoCompat2.getContentDescription());
      paramAccessibilityNodeInfoCompat1.setEnabled(paramAccessibilityNodeInfoCompat2.isEnabled());
      paramAccessibilityNodeInfoCompat1.setClickable(paramAccessibilityNodeInfoCompat2.isClickable());
      paramAccessibilityNodeInfoCompat1.setFocusable(paramAccessibilityNodeInfoCompat2.isFocusable());
      paramAccessibilityNodeInfoCompat1.setFocused(paramAccessibilityNodeInfoCompat2.isFocused());
      paramAccessibilityNodeInfoCompat1.setAccessibilityFocused(paramAccessibilityNodeInfoCompat2.isAccessibilityFocused());
      paramAccessibilityNodeInfoCompat1.setSelected(paramAccessibilityNodeInfoCompat2.isSelected());
      paramAccessibilityNodeInfoCompat1.setLongClickable(paramAccessibilityNodeInfoCompat2.isLongClickable());
      paramAccessibilityNodeInfoCompat1.addAction(paramAccessibilityNodeInfoCompat2.getActions());
    }
    
    public boolean filter(View paramView)
    {
      return SlidingPaneLayout.this.isDimmed(paramView);
    }
    
    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(paramAccessibilityNodeInfoCompat);
      super.onInitializeAccessibilityNodeInfo(paramView, localAccessibilityNodeInfoCompat);
      paramAccessibilityNodeInfoCompat.setSource(paramView);
      Object localObject = ViewCompat.getParentForAccessibility(paramView);
      if ((localObject instanceof View)) {
        paramAccessibilityNodeInfoCompat.setParent((View)localObject);
      }
      copyNodeInfoNoChildren(paramAccessibilityNodeInfoCompat, localAccessibilityNodeInfoCompat);
      localAccessibilityNodeInfoCompat.recycle();
      int j = SlidingPaneLayout.this.getChildCount();
      for (int i = 0;; i++)
      {
        if (i >= j) {
          return;
        }
        localObject = SlidingPaneLayout.this.getChildAt(i);
        if (!filter((View)localObject)) {
          paramAccessibilityNodeInfoCompat.addChild((View)localObject);
        }
      }
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      boolean bool;
      if (filter(paramView)) {
        bool = false;
      } else {
        bool = super.onRequestSendAccessibilityEvent(paramViewGroup, paramView, paramAccessibilityEvent);
      }
      return bool;
    }
  }
  
  static class SlidingPanelLayoutImplJBMR1
    extends SlidingPaneLayout.SlidingPanelLayoutImplBase
  {
    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      ViewCompat.setLayerPaint(paramView, ((SlidingPaneLayout.LayoutParams)paramView.getLayoutParams()).dimPaint);
    }
  }
  
  static class SlidingPanelLayoutImplJB
    extends SlidingPaneLayout.SlidingPanelLayoutImplBase
  {
    private Method mGetDisplayList;
    private Field mRecreateDisplayList;
    
    SlidingPanelLayoutImplJB()
    {
      try
      {
        this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[])null);
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        try
        {
          for (;;)
          {
            this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
            this.mRecreateDisplayList.setAccessible(true);
            return;
            localNoSuchMethodException = localNoSuchMethodException;
            Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", localNoSuchMethodException);
          }
        }
        catch (NoSuchFieldException localNoSuchFieldException)
        {
          for (;;)
          {
            Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", localNoSuchFieldException);
          }
        }
      }
    }
    
    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      if ((this.mGetDisplayList != null) && (this.mRecreateDisplayList != null)) {}
      for (;;)
      {
        try
        {
          this.mRecreateDisplayList.setBoolean(paramView, true);
          this.mGetDisplayList.invoke(paramView, (Object[])null);
          super.invalidateChildRegion(paramSlidingPaneLayout, paramView);
          return;
        }
        catch (Exception localException)
        {
          Log.e("SlidingPaneLayout", "Error refreshing display list state", localException);
          continue;
        }
        paramView.invalidate();
      }
    }
  }
  
  static class SlidingPanelLayoutImplBase
    implements SlidingPaneLayout.SlidingPanelLayoutImpl
  {
    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      ViewCompat.postInvalidateOnAnimation(paramSlidingPaneLayout, paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
    }
  }
  
  static abstract interface SlidingPanelLayoutImpl
  {
    public abstract void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView);
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public SlidingPaneLayout.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new SlidingPaneLayout.SavedState(paramAnonymousParcel, null);
      }
      
      public SlidingPaneLayout.SavedState[] newArray(int paramAnonymousInt)
      {
        return new SlidingPaneLayout.SavedState[paramAnonymousInt];
      }
    };
    boolean isOpen;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      boolean bool;
      if (paramParcel.readInt() == 0) {
        bool = false;
      } else {
        bool = true;
      }
      this.isOpen = bool;
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      int i;
      if (!this.isOpen) {
        i = 0;
      } else {
        i = 1;
      }
      paramParcel.writeInt(i);
    }
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    private static final int[] ATTRS;
    Paint dimPaint;
    boolean dimWhenOffset;
    boolean slideable;
    public float weight = 0.0F;
    
    static
    {
      int[] arrayOfInt = new int[1];
      arrayOfInt[0] = 16843137;
      ATTRS = arrayOfInt;
    }
    
    public LayoutParams()
    {
      super(-1);
    }
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, ATTRS);
      this.weight = localTypedArray.getFloat(0, 0.0F);
      localTypedArray.recycle();
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      this.weight = paramLayoutParams.weight;
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }
  }
  
  private class DragHelperCallback
    extends ViewDragHelper.Callback
  {
    private DragHelperCallback() {}
    
    public int clampViewPositionHorizontal(View paramView, int paramInt1, int paramInt2)
    {
      SlidingPaneLayout.LayoutParams localLayoutParams = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
      int i = SlidingPaneLayout.this.getPaddingLeft() + localLayoutParams.leftMargin;
      int j = i + SlidingPaneLayout.this.mSlideRange;
      return Math.min(Math.max(paramInt1, i), j);
    }
    
    public int getViewHorizontalDragRange(View paramView)
    {
      return SlidingPaneLayout.this.mSlideRange;
    }
    
    public void onEdgeDragStarted(int paramInt1, int paramInt2)
    {
      SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, paramInt2);
    }
    
    public void onViewCaptured(View paramView, int paramInt)
    {
      SlidingPaneLayout.this.setAllChildrenVisible();
    }
    
    public void onViewDragStateChanged(int paramInt)
    {
      if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
        if (SlidingPaneLayout.this.mSlideOffset != 0.0F)
        {
          SlidingPaneLayout.this.dispatchOnPanelOpened(SlidingPaneLayout.this.mSlideableView);
          SlidingPaneLayout.access$502(SlidingPaneLayout.this, true);
        }
        else
        {
          SlidingPaneLayout.this.updateObscuredViewsVisibility(SlidingPaneLayout.this.mSlideableView);
          SlidingPaneLayout.this.dispatchOnPanelClosed(SlidingPaneLayout.this.mSlideableView);
          SlidingPaneLayout.access$502(SlidingPaneLayout.this, false);
        }
      }
    }
    
    public void onViewPositionChanged(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      SlidingPaneLayout.this.onPanelDragged(paramInt1);
      SlidingPaneLayout.this.invalidate();
    }
    
    public void onViewReleased(View paramView, float paramFloat1, float paramFloat2)
    {
      SlidingPaneLayout.LayoutParams localLayoutParams = (SlidingPaneLayout.LayoutParams)paramView.getLayoutParams();
      int i = SlidingPaneLayout.this.getPaddingLeft() + localLayoutParams.leftMargin;
      if ((paramFloat1 > 0.0F) || ((paramFloat1 == 0.0F) && (SlidingPaneLayout.this.mSlideOffset > 0.5F))) {
        i += SlidingPaneLayout.this.mSlideRange;
      }
      SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(i, paramView.getTop());
      SlidingPaneLayout.this.invalidate();
    }
    
    public boolean tryCaptureView(View paramView, int paramInt)
    {
      boolean bool;
      if (!SlidingPaneLayout.this.mIsUnableToDrag) {
        bool = ((SlidingPaneLayout.LayoutParams)paramView.getLayoutParams()).slideable;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public static class SimplePanelSlideListener
    implements SlidingPaneLayout.PanelSlideListener
  {
    public void onPanelClosed(View paramView) {}
    
    public void onPanelOpened(View paramView) {}
    
    public void onPanelSlide(View paramView, float paramFloat) {}
  }
  
  public static abstract interface PanelSlideListener
  {
    public abstract void onPanelClosed(View paramView);
    
    public abstract void onPanelOpened(View paramView);
    
    public abstract void onPanelSlide(View paramView, float paramFloat);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.SlidingPaneLayout
 * JD-Core Version:    0.7.0.1
 */