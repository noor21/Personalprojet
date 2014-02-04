package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager
  extends ViewGroup
{
  private static final int CLOSE_ENOUGH = 2;
  private static final Comparator<ItemInfo> COMPARATOR = new Comparator()
  {
    public int compare(ViewPager.ItemInfo paramAnonymousItemInfo1, ViewPager.ItemInfo paramAnonymousItemInfo2)
    {
      return paramAnonymousItemInfo1.position - paramAnonymousItemInfo2.position;
    }
  };
  private static final boolean DEBUG = false;
  private static final int DEFAULT_GUTTER_SIZE = 16;
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  private static final int DRAW_ORDER_DEFAULT = 0;
  private static final int DRAW_ORDER_FORWARD = 1;
  private static final int DRAW_ORDER_REVERSE = 2;
  private static final int INVALID_POINTER = -1;
  private static final int[] LAYOUT_ATTRS;
  private static final int MAX_SETTLE_DURATION = 600;
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  private static final int MIN_FLING_VELOCITY = 400;
  public static final int SCROLL_STATE_DRAGGING = 1;
  public static final int SCROLL_STATE_IDLE = 0;
  public static final int SCROLL_STATE_SETTLING = 2;
  private static final String TAG = "ViewPager";
  private static final boolean USE_CACHE;
  private static final Interpolator sInterpolator = new Interpolator()
  {
    public float getInterpolation(float paramAnonymousFloat)
    {
      float f = paramAnonymousFloat - 1.0F;
      return 1.0F + f * (f * (f * (f * f)));
    }
  };
  private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
  private int mActivePointerId = -1;
  private PagerAdapter mAdapter;
  private OnAdapterChangeListener mAdapterChangeListener;
  private int mBottomPageBounds;
  private boolean mCalledSuper;
  private int mChildHeightMeasureSpec;
  private int mChildWidthMeasureSpec;
  private int mCloseEnough;
  private int mCurItem;
  private int mDecorChildCount;
  private int mDefaultGutterSize;
  private int mDrawingOrder;
  private ArrayList<View> mDrawingOrderedChildren;
  private final Runnable mEndScrollRunnable = new Runnable()
  {
    public void run()
    {
      ViewPager.this.setScrollState(0);
      ViewPager.this.populate();
    }
  };
  private int mExpectedAdapterCount;
  private long mFakeDragBeginTime;
  private boolean mFakeDragging;
  private boolean mFirstLayout = true;
  private float mFirstOffset = -3.402824E+038F;
  private int mFlingDistance;
  private int mGutterSize;
  private boolean mIgnoreGutter;
  private boolean mInLayout;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private OnPageChangeListener mInternalPageChangeListener;
  private boolean mIsBeingDragged;
  private boolean mIsUnableToDrag;
  private final ArrayList<ItemInfo> mItems = new ArrayList();
  private float mLastMotionX;
  private float mLastMotionY;
  private float mLastOffset = 3.4028235E+38F;
  private EdgeEffectCompat mLeftEdge;
  private Drawable mMarginDrawable;
  private int mMaximumVelocity;
  private int mMinimumVelocity;
  private boolean mNeedCalculatePageOffsets = false;
  private PagerObserver mObserver;
  private int mOffscreenPageLimit = 1;
  private OnPageChangeListener mOnPageChangeListener;
  private int mPageMargin;
  private PageTransformer mPageTransformer;
  private boolean mPopulatePending;
  private Parcelable mRestoredAdapterState = null;
  private ClassLoader mRestoredClassLoader = null;
  private int mRestoredCurItem = -1;
  private EdgeEffectCompat mRightEdge;
  private int mScrollState = 0;
  private Scroller mScroller;
  private boolean mScrollingCacheEnabled;
  private Method mSetChildrenDrawingOrderEnabled;
  private final ItemInfo mTempItem = new ItemInfo();
  private final Rect mTempRect = new Rect();
  private int mTopPageBounds;
  private int mTouchSlop;
  private VelocityTracker mVelocityTracker;
  
  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16842931;
    LAYOUT_ATTRS = arrayOfInt;
  }
  
  public ViewPager(Context paramContext)
  {
    super(paramContext);
    initViewPager();
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }
  
  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2)
  {
    int i = this.mAdapter.getCount();
    int j = getClientWidth();
    float f1;
    if (j <= 0) {
      f1 = 0.0F;
    } else {
      f1 = this.mPageMargin / f1;
    }
    if (paramItemInfo2 != null)
    {
      int m = paramItemInfo2.position;
      float f4;
      ItemInfo localItemInfo1;
      if (m >= paramItemInfo1.position)
      {
        if (m > paramItemInfo1.position)
        {
          k = -1 + this.mItems.size();
          f4 = paramItemInfo2.offset;
          int i3 = m - 1;
          if ((i3 >= paramItemInfo1.position) && (k >= 0)) {
            for (localItemInfo1 = (ItemInfo)this.mItems.get(k);; localItemInfo1 = (ItemInfo)this.mItems.get(k))
            {
              if ((i3 >= localItemInfo1.position) || (k <= 0)) {
                for (;;)
                {
                  if (i3 <= localItemInfo1.position)
                  {
                    f4 -= f1 + localItemInfo1.widthFactor;
                    localItemInfo1.offset = f4;
                    i3--;
                    break;
                  }
                  f4 -= f1 + this.mAdapter.getPageWidth(i3);
                  i3--;
                }
              }
              k--;
            }
          }
        }
      }
      else
      {
        k = 0;
        f4 = f1 + (paramItemInfo2.offset + paramItemInfo2.widthFactor);
        int n;
        localItemInfo1 += 1;
        if ((n <= paramItemInfo1.position) && (k < this.mItems.size())) {
          break label611;
        }
      }
    }
    int k = this.mItems.size();
    float f2 = paramItemInfo1.offset;
    int i2 = -1 + paramItemInfo1.position;
    float f6;
    if (paramItemInfo1.position != 0) {
      f6 = -3.402824E+038F;
    } else {
      f6 = paramItemInfo1.offset;
    }
    this.mFirstOffset = f6;
    if (paramItemInfo1.position != i - 1) {
      f6 = 3.4028235E+38F;
    } else {
      f6 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
    }
    this.mLastOffset = f6;
    int i5 = paramInt - 1;
    float f7;
    int i4;
    PagerAdapter localPagerAdapter;
    int i1;
    if (i5 < 0)
    {
      f7 = f1 + (paramItemInfo1.offset + paramItemInfo1.widthFactor);
      i2 = 1 + paramItemInfo1.position;
      i4 = paramInt + 1;
      if (i4 >= k)
      {
        this.mNeedCalculatePageOffsets = false;
        return;
      }
      localItemInfo2 = (ItemInfo)this.mItems.get(i4);
      for (;;)
      {
        if (i2 >= localItemInfo2.position)
        {
          if (localItemInfo2.position == i - 1) {
            this.mLastOffset = (f7 + localItemInfo2.widthFactor - 1.0F);
          }
          localItemInfo2.offset = f7;
          f7 += f1 + localItemInfo2.widthFactor;
          i4++;
          i2++;
          break;
        }
        localPagerAdapter = this.mAdapter;
        i1 = i2 + 1;
        f7 += f1 + localPagerAdapter.getPageWidth(i2);
        i2 = i1;
      }
    }
    ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(f7);
    float f3;
    for (;;)
    {
      if (i2 <= localItemInfo2.position)
      {
        i1 -= f1 + localItemInfo2.widthFactor;
        localItemInfo2.offset = f3;
        if (localItemInfo2.position == 0) {
          this.mFirstOffset = f3;
        }
        f7--;
        i2--;
        break;
      }
      localPagerAdapter = this.mAdapter;
      i4 = i2 - 1;
      f3 -= f1 + localPagerAdapter.getPageWidth(i2);
      i2 = i4;
    }
    label611:
    for (localItemInfo2 = (ItemInfo)this.mItems.get(k);; localItemInfo2 = (ItemInfo)this.mItems.get(k))
    {
      if ((f3 <= localItemInfo2.position) || (k >= -1 + this.mItems.size())) {
        for (;;)
        {
          float f5;
          if (f3 >= localItemInfo2.position)
          {
            localItemInfo2.offset = i2;
            i2 += f1 + localItemInfo2.widthFactor;
            f3++;
            break;
          }
          f5 += f1 + this.mAdapter.getPageWidth(f3);
          f3++;
        }
      }
      k++;
    }
  }
  
  private void completeScroll(boolean paramBoolean)
  {
    int i;
    if (this.mScrollState != 2) {
      i = 0;
    } else {
      i = 1;
    }
    if (i != 0)
    {
      setScrollingCacheEnabled(false);
      this.mScroller.abortAnimation();
      k = getScrollX();
      int n = getScrollY();
      int m = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      if ((k != m) || (n != j)) {
        scrollTo(m, j);
      }
    }
    this.mPopulatePending = false;
    for (int k = 0;; k++)
    {
      if (k >= this.mItems.size())
      {
        if (i != 0) {
          if (!paramBoolean) {
            this.mEndScrollRunnable.run();
          } else {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
          }
        }
        return;
      }
      ItemInfo localItemInfo = (ItemInfo)this.mItems.get(k);
      if (localItemInfo.scrolling)
      {
        i = 1;
        localItemInfo.scrolling = false;
      }
    }
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3)
  {
    int i;
    if ((Math.abs(paramInt3) <= this.mFlingDistance) || (Math.abs(paramInt2) <= this.mMinimumVelocity))
    {
      float f;
      if (paramInt1 < this.mCurItem) {
        f = 0.6F;
      } else {
        f = 0.4F;
      }
      i = (int)(f + (paramFloat + paramInt1));
    }
    else if (paramInt2 <= 0)
    {
      i = paramInt1 + 1;
    }
    else
    {
      i = paramInt1;
    }
    if (this.mItems.size() > 0)
    {
      ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(0);
      ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
      i = Math.max(localItemInfo2.position, Math.min(i, localItemInfo1.position));
    }
    return i;
  }
  
  private void enableLayers(boolean paramBoolean)
  {
    int j = getChildCount();
    for (int k = 0;; k++)
    {
      if (k >= j) {
        return;
      }
      int i;
      if (!paramBoolean) {
        i = 0;
      } else {
        i = 2;
      }
      ViewCompat.setLayerType(getChildAt(k), i, null);
    }
  }
  
  private void endDrag()
  {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    if (this.mVelocityTracker != null)
    {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    }
  }
  
  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView)
  {
    if (paramRect == null) {
      paramRect = new Rect();
    }
    if (paramView != null)
    {
      paramRect.left = paramView.getLeft();
      paramRect.right = paramView.getRight();
      paramRect.top = paramView.getTop();
      paramRect.bottom = paramView.getBottom();
      for (Object localObject = paramView.getParent(); ((localObject instanceof ViewGroup)) && (localObject != this); localObject = ((ViewGroup)localObject).getParent())
      {
        localObject = (ViewGroup)localObject;
        paramRect.left += ((ViewGroup)localObject).getLeft();
        paramRect.right += ((ViewGroup)localObject).getRight();
        paramRect.top += ((ViewGroup)localObject).getTop();
        paramRect.bottom += ((ViewGroup)localObject).getBottom();
      }
    }
    paramRect.set(0, 0, 0, 0);
    return paramRect;
  }
  
  private int getClientWidth()
  {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }
  
  private ItemInfo infoForCurrentScrollPosition()
  {
    float f2 = 0.0F;
    int i = getClientWidth();
    float f1;
    if (i <= 0) {
      f1 = 0.0F;
    } else {
      f1 = getScrollX() / i;
    }
    if (i > 0) {
      f2 = this.mPageMargin / i;
    }
    int m = -1;
    float f4 = 0.0F;
    float f5 = 0.0F;
    int k = 1;
    Object localObject = null;
    int j = 0;
    while (j < this.mItems.size())
    {
      ItemInfo localItemInfo = (ItemInfo)this.mItems.get(j);
      if ((k == 0) && (localItemInfo.position != m + 1))
      {
        localItemInfo = this.mTempItem;
        localItemInfo.offset = (f2 + (f4 + f5));
        localItemInfo.position = (m + 1);
        localItemInfo.widthFactor = this.mAdapter.getPageWidth(localItemInfo.position);
        j--;
      }
      f4 = localItemInfo.offset;
      float f3 = f2 + (f4 + localItemInfo.widthFactor);
      if ((k != 0) || (f1 >= f4)) {
        if ((f1 >= f3) && (j != -1 + this.mItems.size()))
        {
          k = 0;
          int n = localItemInfo.position;
          f4 = f4;
          f5 = localItemInfo.widthFactor;
          localObject = localItemInfo;
          j++;
        }
        else
        {
          localObject = localItemInfo;
        }
      }
    }
    return localObject;
  }
  
  private boolean isGutterDrag(float paramFloat1, float paramFloat2)
  {
    boolean bool;
    if (((paramFloat1 >= this.mGutterSize) || (paramFloat2 <= 0.0F)) && ((paramFloat1 <= getWidth() - this.mGutterSize) || (paramFloat2 >= 0.0F))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (MotionEventCompat.getPointerId(paramMotionEvent, i) == this.mActivePointerId)
    {
      if (i != 0) {
        i = 0;
      } else {
        i = 1;
      }
      this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, i);
      this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
      if (this.mVelocityTracker != null) {
        this.mVelocityTracker.clear();
      }
    }
  }
  
  private boolean pageScrolled(int paramInt)
  {
    int i = 0;
    if (this.mItems.size() != 0)
    {
      ItemInfo localItemInfo = infoForCurrentScrollPosition();
      int k = getClientWidth();
      int j = k + this.mPageMargin;
      float f2 = this.mPageMargin / k;
      i = localItemInfo.position;
      float f1 = (paramInt / k - localItemInfo.offset) / (f2 + localItemInfo.widthFactor);
      j = (int)(f1 * j);
      this.mCalledSuper = false;
      onPageScrolled(i, f1, j);
      if (this.mCalledSuper) {
        i = 1;
      } else {
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
      }
    }
    else
    {
      this.mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (!this.mCalledSuper) {
        break label137;
      }
    }
    return i;
    label137:
    throw new IllegalStateException("onPageScrolled did not call superclass implementation");
  }
  
  private boolean performDrag(float paramFloat)
  {
    int j = 0;
    float f1 = this.mLastMotionX - paramFloat;
    this.mLastMotionX = paramFloat;
    float f6 = f1 + getScrollX();
    int i = getClientWidth();
    float f4 = i * this.mFirstOffset;
    float f5 = i * this.mLastOffset;
    int k = 1;
    int m = 1;
    ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
    ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
    if (localItemInfo1.position != 0)
    {
      k = 0;
      f4 = localItemInfo1.offset * i;
    }
    if (localItemInfo2.position != -1 + this.mAdapter.getCount())
    {
      m = 0;
      f5 = localItemInfo2.offset * i;
    }
    boolean bool2;
    if (f6 >= f4)
    {
      if (f6 > f5)
      {
        if (m != 0)
        {
          float f2 = f6 - f5;
          boolean bool1 = this.mRightEdge.onPull(Math.abs(f2) / i);
        }
        f6 = f5;
      }
    }
    else
    {
      if (k != 0)
      {
        float f3 = f4 - f6;
        bool2 = this.mLeftEdge.onPull(Math.abs(f3) / i);
      }
      f6 = f4;
    }
    this.mLastMotionX += f6 - (int)f6;
    scrollTo((int)f6, getScrollY());
    pageScrolled((int)f6);
    return bool2;
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i;
    if ((paramInt2 <= 0) || (this.mItems.isEmpty()))
    {
      ItemInfo localItemInfo1 = infoForPosition(this.mCurItem);
      float f;
      if (localItemInfo1 == null) {
        f = 0.0F;
      } else {
        f = Math.min(f.offset, this.mLastOffset);
      }
      i = (int)(f * (paramInt1 - getPaddingLeft() - getPaddingRight()));
      if (i != getScrollX())
      {
        completeScroll(false);
        scrollTo(i, getScrollY());
      }
    }
    else
    {
      int j = paramInt3 + (paramInt1 - getPaddingLeft() - getPaddingRight());
      i = paramInt4 + (paramInt2 - getPaddingLeft() - getPaddingRight());
      i = (int)(getScrollX() / i * j);
      scrollTo(i, getScrollY());
      if (!this.mScroller.isFinished())
      {
        j = this.mScroller.getDuration() - this.mScroller.timePassed();
        ItemInfo localItemInfo2 = infoForPosition(this.mCurItem);
        this.mScroller.startScroll(i, 0, (int)(localItemInfo2.offset * paramInt1), 0, j);
      }
    }
  }
  
  private void removeNonDecorViews()
  {
    for (int i = 0;; i++)
    {
      if (i >= getChildCount()) {
        return;
      }
      if (!((LayoutParams)getChildAt(i).getLayoutParams()).isDecor)
      {
        removeViewAt(i);
        i--;
      }
    }
  }
  
  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    ItemInfo localItemInfo = infoForPosition(paramInt1);
    int i = 0;
    if (localItemInfo != null) {
      i = (int)(getClientWidth() * Math.max(this.mFirstOffset, Math.min(localItemInfo.offset, this.mLastOffset)));
    }
    if (!paramBoolean1)
    {
      if ((paramBoolean2) && (this.mOnPageChangeListener != null)) {
        this.mOnPageChangeListener.onPageSelected(paramInt1);
      }
      if ((paramBoolean2) && (this.mInternalPageChangeListener != null)) {
        this.mInternalPageChangeListener.onPageSelected(paramInt1);
      }
      completeScroll(false);
      scrollTo(i, 0);
    }
    else
    {
      smoothScrollTo(i, 0, paramInt2);
      if ((paramBoolean2) && (this.mOnPageChangeListener != null)) {
        this.mOnPageChangeListener.onPageSelected(paramInt1);
      }
      if ((paramBoolean2) && (this.mInternalPageChangeListener != null)) {
        this.mInternalPageChangeListener.onPageSelected(paramInt1);
      }
    }
  }
  
  private void setScrollState(int paramInt)
  {
    if (this.mScrollState != paramInt)
    {
      this.mScrollState = paramInt;
      if (this.mPageTransformer != null)
      {
        boolean bool;
        if (paramInt == 0) {
          bool = false;
        } else {
          bool = true;
        }
        enableLayers(bool);
      }
      if (this.mOnPageChangeListener != null) {
        this.mOnPageChangeListener.onPageScrollStateChanged(paramInt);
      }
    }
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean)
  {
    if (this.mScrollingCacheEnabled != paramBoolean) {
      this.mScrollingCacheEnabled = paramBoolean;
    }
  }
  
  private void sortChildDrawingOrder()
  {
    int j;
    if (this.mDrawingOrder != 0)
    {
      if (this.mDrawingOrderedChildren != null) {
        this.mDrawingOrderedChildren.clear();
      } else {
        this.mDrawingOrderedChildren = new ArrayList();
      }
      j = getChildCount();
    }
    for (int i = 0;; i++)
    {
      if (i >= j)
      {
        Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        return;
      }
      View localView = getChildAt(i);
      this.mDrawingOrderedChildren.add(localView);
    }
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    int k = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216) {}
    for (int i = 0;; i++)
    {
      if (i >= getChildCount())
      {
        if (((j != 262144) || (k == paramArrayList.size())) && (isFocusable()) && (((paramInt2 & 0x1) != 1) || (!isInTouchMode()) || (isFocusableInTouchMode())) && (paramArrayList != null)) {
          paramArrayList.add(this);
        }
        return;
      }
      View localView = getChildAt(i);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo != null) && (localItemInfo.position == this.mCurItem)) {
          localView.addFocusables(paramArrayList, paramInt1, paramInt2);
        }
      }
    }
  }
  
  ItemInfo addNewItem(int paramInt1, int paramInt2)
  {
    ItemInfo localItemInfo = new ItemInfo();
    localItemInfo.position = paramInt1;
    localItemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    localItemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
    if ((paramInt2 >= 0) && (paramInt2 < this.mItems.size())) {
      this.mItems.add(paramInt2, localItemInfo);
    } else {
      this.mItems.add(localItemInfo);
    }
    return localItemInfo;
  }
  
  public void addTouchables(ArrayList<View> paramArrayList)
  {
    for (int i = 0;; i++)
    {
      if (i >= getChildCount()) {
        return;
      }
      View localView = getChildAt(i);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo != null) && (localItemInfo.position == this.mCurItem)) {
          localView.addTouchables(paramArrayList);
        }
      }
    }
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (!checkLayoutParams(paramLayoutParams)) {
      paramLayoutParams = generateLayoutParams(paramLayoutParams);
    }
    LayoutParams localLayoutParams = (LayoutParams)paramLayoutParams;
    localLayoutParams.isDecor |= paramView instanceof Decor;
    if (!this.mInLayout)
    {
      super.addView(paramView, paramInt, paramLayoutParams);
    }
    else
    {
      if ((localLayoutParams != null) && (localLayoutParams.isDecor)) {
        break label80;
      }
      localLayoutParams.needsMeasure = true;
      addViewInLayout(paramView, paramInt, paramLayoutParams);
    }
    return;
    label80:
    throw new IllegalStateException("Cannot add pager decor view during layout");
  }
  
  public boolean arrowScroll(int paramInt)
  {
    Object localObject1 = findFocus();
    if (localObject1 != this)
    {
      if (localObject1 != null)
      {
        int i = 0;
        ViewParent localViewParent = ((View)localObject1).getParent();
        while ((localViewParent instanceof ViewGroup)) {
          if (localViewParent != this) {
            localViewParent = localViewParent.getParent();
          } else {
            i = 1;
          }
        }
        if (i == 0)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append(localObject1.getClass().getSimpleName());
          for (localObject1 = ((View)localObject1).getParent();; localObject1 = ((ViewParent)localObject1).getParent())
          {
            if (!(localObject1 instanceof ViewGroup))
            {
              Log.e("ViewPager", "arrowScroll tried to find focus based on non-child current focused view " + ((StringBuilder)localObject2).toString());
              localObject1 = null;
              break;
            }
            ((StringBuilder)localObject2).append(" => ").append(localObject1.getClass().getSimpleName());
          }
        }
      }
    }
    else {
      localObject1 = null;
    }
    boolean bool1 = false;
    Object localObject2 = FocusFinder.getInstance().findNextFocus(this, (View)localObject1, paramInt);
    boolean bool3;
    if ((localObject2 == null) || (localObject2 == localObject1))
    {
      if ((paramInt != 17) && (paramInt != 1))
      {
        if ((paramInt == 66) || (paramInt == 2)) {
          bool1 = pageRight();
        }
      }
      else {
        bool1 = pageLeft();
      }
    }
    else
    {
      int m;
      if (paramInt != 17)
      {
        if (paramInt == 66)
        {
          int j = getChildRectInPagerCoordinates(this.mTempRect, (View)localObject2).left;
          m = getChildRectInPagerCoordinates(this.mTempRect, (View)localObject1).left;
          boolean bool2;
          if ((localObject1 == null) || (j > m)) {
            bool2 = ((View)localObject2).requestFocus();
          } else {
            bool2 = pageRight();
          }
        }
      }
      else
      {
        int k = getChildRectInPagerCoordinates(this.mTempRect, (View)localObject2).left;
        m = getChildRectInPagerCoordinates(this.mTempRect, (View)localObject1).left;
        if ((localObject1 == null) || (k < m)) {
          bool3 = ((View)localObject2).requestFocus();
        } else {
          bool3 = pageLeft();
        }
      }
    }
    if (bool3) {
      playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
    }
    return bool3;
  }
  
  public boolean beginFakeDrag()
  {
    int i = 0;
    boolean bool;
    if (!this.mIsBeingDragged)
    {
      this.mFakeDragging = true;
      setScrollState(1);
      this.mLastMotionX = 0.0F;
      this.mInitialMotionX = 0.0F;
      if (this.mVelocityTracker != null) {
        this.mVelocityTracker.clear();
      } else {
        this.mVelocityTracker = VelocityTracker.obtain();
      }
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
      this.mVelocityTracker.addMovement(localMotionEvent);
      localMotionEvent.recycle();
      this.mFakeDragBeginTime = l;
      bool = true;
    }
    return bool;
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
  
  public void computeScroll()
  {
    if ((this.mScroller.isFinished()) || (!this.mScroller.computeScrollOffset()))
    {
      completeScroll(true);
    }
    else
    {
      int k = getScrollX();
      int j = getScrollY();
      int i = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if ((k != i) || (j != m))
      {
        scrollTo(i, m);
        if (!pageScrolled(i))
        {
          this.mScroller.abortAnimation();
          scrollTo(0, m);
        }
      }
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }
  
  void dataSetChanged()
  {
    int i1 = this.mAdapter.getCount();
    this.mExpectedAdapterCount = i1;
    int i2;
    if ((this.mItems.size() >= 1 + 2 * this.mOffscreenPageLimit) || (this.mItems.size() >= i1)) {
      i2 = 0;
    } else {
      i2 = 1;
    }
    int i = this.mCurItem;
    int n = 0;
    for (int m = 0;; m++)
    {
      if (m >= this.mItems.size())
      {
        if (n != 0) {
          this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (i2 != 0) {
          m = getChildCount();
        }
        for (int j = 0;; j++)
        {
          if (j >= m)
          {
            setCurrentItemInternal(i, false, true);
            requestLayout();
            return;
          }
          LayoutParams localLayoutParams = (LayoutParams)getChildAt(j).getLayoutParams();
          if (!localLayoutParams.isDecor) {
            localLayoutParams.widthFactor = 0.0F;
          }
        }
      }
      ItemInfo localItemInfo = (ItemInfo)this.mItems.get(m);
      int k = this.mAdapter.getItemPosition(localItemInfo.object);
      if (k != -1) {
        if (k != -2)
        {
          if (localItemInfo.position != k)
          {
            if (localItemInfo.position == this.mCurItem) {
              i = k;
            }
            localItemInfo.position = k;
            i2 = 1;
          }
        }
        else
        {
          this.mItems.remove(m);
          m--;
          if (n == 0)
          {
            this.mAdapter.startUpdate(this);
            n = 1;
          }
          this.mAdapter.destroyItem(this, localItemInfo.position, localItemInfo.object);
          i2 = 1;
          if (this.mCurItem == localItemInfo.position)
          {
            i = Math.max(0, Math.min(this.mCurItem, i1 - 1));
            i2 = 1;
          }
        }
      }
    }
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool;
    if ((!super.dispatchKeyEvent(paramKeyEvent)) && (!executeKeyEvent(paramKeyEvent))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    int i = getChildCount();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return 0;
      }
      View localView = getChildAt(j);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo != null) && (localItemInfo.position == this.mCurItem) && (localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent))) {
          break;
        }
      }
    }
    i = 1;
    return i;
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)(0.47123891676382D * (paramFloat - 0.5F)));
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    int i = 0;
    int j = ViewCompat.getOverScrollMode(this);
    boolean bool;
    if ((j != 0) && ((j != 1) || (this.mAdapter == null) || (this.mAdapter.getCount() <= 1)))
    {
      this.mLeftEdge.finish();
      this.mRightEdge.finish();
    }
    else
    {
      int k;
      if (!this.mLeftEdge.isFinished())
      {
        j = paramCanvas.save();
        i = getHeight() - getPaddingTop() - getPaddingBottom();
        k = getWidth();
        paramCanvas.rotate(270.0F);
        paramCanvas.translate(-i + getPaddingTop(), this.mFirstOffset * k);
        this.mLeftEdge.setSize(i, k);
        bool = false | this.mLeftEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(j);
      }
      if (!this.mRightEdge.isFinished())
      {
        k = paramCanvas.save();
        int m = getWidth();
        j = getHeight() - getPaddingTop() - getPaddingBottom();
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-getPaddingTop(), -(1.0F + this.mLastOffset) * m);
        this.mRightEdge.setSize(j, m);
        bool |= this.mRightEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(k);
      }
    }
    if (bool) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = this.mMarginDrawable;
    if ((localDrawable != null) && (localDrawable.isStateful())) {
      localDrawable.setState(getDrawableState());
    }
  }
  
  public void endFakeDrag()
  {
    if (this.mFakeDragging)
    {
      Object localObject = this.mVelocityTracker;
      ((VelocityTracker)localObject).computeCurrentVelocity(1000, this.mMaximumVelocity);
      int j = (int)VelocityTrackerCompat.getXVelocity((VelocityTracker)localObject, this.mActivePointerId);
      this.mPopulatePending = true;
      int i = getClientWidth();
      int k = getScrollX();
      localObject = infoForCurrentScrollPosition();
      setCurrentItemInternal(determineTargetPage(((ItemInfo)localObject).position, (k / i - ((ItemInfo)localObject).offset) / ((ItemInfo)localObject).widthFactor, j, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, j);
      endDrag();
      this.mFakeDragging = false;
      return;
    }
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool = false;
    if (paramKeyEvent.getAction() == 0) {
      switch (paramKeyEvent.getKeyCode())
      {
      case 21: 
        bool = arrowScroll(17);
        break;
      case 22: 
        bool = arrowScroll(66);
        break;
      case 61: 
        if (Build.VERSION.SDK_INT >= 11) {
          if (!KeyEventCompat.hasNoModifiers(paramKeyEvent))
          {
            if (KeyEventCompat.hasModifiers(paramKeyEvent, 1)) {
              bool = arrowScroll(1);
            }
          }
          else {
            bool = arrowScroll(2);
          }
        }
        break;
      }
    }
    return bool;
  }
  
  public void fakeDragBy(float paramFloat)
  {
    if (this.mFakeDragging)
    {
      this.mLastMotionX = (paramFloat + this.mLastMotionX);
      float f1 = getScrollX() - paramFloat;
      int i = getClientWidth();
      float f2 = i * this.mFirstOffset;
      float f3 = i * this.mLastOffset;
      ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(0);
      ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
      if (localItemInfo2.position != 0) {
        f2 = localItemInfo2.offset * i;
      }
      if (localItemInfo1.position != -1 + this.mAdapter.getCount()) {
        f3 = localItemInfo1.offset * i;
      }
      if (f1 >= f2)
      {
        if (f1 > f3) {
          f1 = f3;
        }
      }
      else {
        f1 = f2;
      }
      this.mLastMotionX += f1 - (int)f1;
      scrollTo((int)f1, getScrollY());
      pageScrolled((int)f1);
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2, this.mLastMotionX, 0.0F, 0);
      this.mVelocityTracker.addMovement(localMotionEvent);
      localMotionEvent.recycle();
      return;
    }
    throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
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
    return generateDefaultLayoutParams();
  }
  
  public PagerAdapter getAdapter()
  {
    return this.mAdapter;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    int i;
    if (this.mDrawingOrder != 2) {
      i = paramInt2;
    } else {
      i = paramInt1 - 1 - paramInt2;
    }
    return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(i)).getLayoutParams()).childIndex;
  }
  
  public int getCurrentItem()
  {
    return this.mCurItem;
  }
  
  public int getOffscreenPageLimit()
  {
    return this.mOffscreenPageLimit;
  }
  
  public int getPageMargin()
  {
    return this.mPageMargin;
  }
  
  ItemInfo infoForAnyChild(View paramView)
  {
    for (;;)
    {
      localObject = paramView.getParent();
      if (localObject == this) {
        return infoForChild(paramView);
      }
      if ((localObject == null) || (!(localObject instanceof View))) {
        break;
      }
      paramView = (View)localObject;
    }
    Object localObject = null;
    return localObject;
  }
  
  ItemInfo infoForChild(View paramView)
  {
    ItemInfo localItemInfo;
    for (int i = 0;; i++)
    {
      if (i >= this.mItems.size())
      {
        localItemInfo = null;
        break;
      }
      localItemInfo = (ItemInfo)this.mItems.get(i);
      if (this.mAdapter.isViewFromObject(paramView, localItemInfo.object)) {
        break;
      }
    }
    return localItemInfo;
  }
  
  ItemInfo infoForPosition(int paramInt)
  {
    ItemInfo localItemInfo;
    for (int i = 0;; i++)
    {
      if (i >= this.mItems.size())
      {
        localItemInfo = null;
        break;
      }
      localItemInfo = (ItemInfo)this.mItems.get(i);
      if (localItemInfo.position == paramInt) {
        break;
      }
    }
    return localItemInfo;
  }
  
  void initViewPager()
  {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context localContext = getContext();
    this.mScroller = new Scroller(localContext, sInterpolator);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    float f = localContext.getResources().getDisplayMetrics().density;
    this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(localViewConfiguration);
    this.mMinimumVelocity = ((int)(400.0F * f));
    this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffectCompat(localContext);
    this.mRightEdge = new EdgeEffectCompat(localContext);
    this.mFlingDistance = ((int)(25.0F * f));
    this.mCloseEnough = ((int)(2.0F * f));
    this.mDefaultGutterSize = ((int)(16.0F * f));
    ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility(this) == 0) {
      ViewCompat.setImportantForAccessibility(this, 1);
    }
  }
  
  public boolean isFakeDragging()
  {
    return this.mFakeDragging;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow()
  {
    removeCallbacks(this.mEndScrollRunnable);
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int j;
    int i;
    float f1;
    int m;
    Object localObject;
    float f2;
    int n;
    int i1;
    if ((this.mPageMargin > 0) && (this.mMarginDrawable != null) && (this.mItems.size() > 0) && (this.mAdapter != null))
    {
      j = getScrollX();
      i = getWidth();
      f1 = this.mPageMargin / i;
      m = 0;
      localObject = (ItemInfo)this.mItems.get(0);
      f2 = ((ItemInfo)localObject).offset;
      n = this.mItems.size();
      i1 = ((ItemInfo)localObject).position;
      int k = ((ItemInfo)this.mItems.get(n - 1)).position;
      i1 = i1;
      if (i1 >= k) {}
    }
    for (;;)
    {
      if ((i1 <= ((ItemInfo)localObject).position) || (m >= n))
      {
        float f4;
        if (i1 != ((ItemInfo)localObject).position)
        {
          float f3 = this.mAdapter.getPageWidth(i1);
          f4 = (f2 + f3) * i;
          f2 += f3 + f1;
        }
        else
        {
          f4 = (((ItemInfo)localObject).offset + ((ItemInfo)localObject).widthFactor) * i;
          f2 = f1 + (((ItemInfo)localObject).offset + ((ItemInfo)localObject).widthFactor);
        }
        if (f4 + this.mPageMargin > j)
        {
          this.mMarginDrawable.setBounds((int)f4, this.mTopPageBounds, (int)(0.5F + (f4 + this.mPageMargin)), this.mBottomPageBounds);
          this.mMarginDrawable.draw(paramCanvas);
        }
        if (f4 <= j + i)
        {
          i1++;
          break;
        }
        return;
      }
      localObject = this.mItems;
      m++;
      localObject = (ItemInfo)((ArrayList)localObject).get(m);
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = 0xFF & paramMotionEvent.getAction();
    boolean bool;
    if ((i != 3) && (i != 1))
    {
      if (i != 0)
      {
        if (this.mIsBeingDragged) {
          break label476;
        }
        if (this.mIsUnableToDrag) {}
      }
      else
      {
        switch (i)
        {
        case 0: 
          float f1 = paramMotionEvent.getX();
          this.mInitialMotionX = f1;
          this.mLastMotionX = f1;
          f1 = paramMotionEvent.getY();
          this.mInitialMotionY = f1;
          this.mLastMotionY = f1;
          this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
          this.mIsUnableToDrag = false;
          this.mScroller.computeScrollOffset();
          if ((this.mScrollState != 2) || (Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) <= this.mCloseEnough))
          {
            completeScroll(false);
            this.mIsBeingDragged = false;
          }
          else
          {
            this.mScroller.abortAnimation();
            this.mPopulatePending = false;
            populate();
            this.mIsBeingDragged = true;
            setScrollState(1);
          }
          break;
        case 2: 
          int j = this.mActivePointerId;
          if (j != -1)
          {
            int k = MotionEventCompat.findPointerIndex(paramMotionEvent, j);
            float f2 = MotionEventCompat.getX(paramMotionEvent, k);
            float f4 = f2 - this.mLastMotionX;
            float f5 = Math.abs(f4);
            float f3 = MotionEventCompat.getY(paramMotionEvent, k);
            float f6 = Math.abs(f3 - this.mInitialMotionY);
            if ((f4 == 0.0F) || (isGutterDrag(this.mLastMotionX, f4)) || (!canScroll(this, false, (int)f4, (int)f2, (int)f3)))
            {
              if ((f5 <= this.mTouchSlop) || (0.5F * f5 <= f6))
              {
                if (f6 > this.mTouchSlop) {
                  this.mIsUnableToDrag = true;
                }
              }
              else
              {
                this.mIsBeingDragged = true;
                setScrollState(1);
                if (f4 <= 0.0F) {
                  f4 = this.mInitialMotionX - this.mTouchSlop;
                } else {
                  f4 = this.mInitialMotionX + this.mTouchSlop;
                }
                this.mLastMotionX = f4;
                this.mLastMotionY = f3;
                setScrollingCacheEnabled(true);
              }
              if ((this.mIsBeingDragged) && (performDrag(f2))) {
                ViewCompat.postInvalidateOnAnimation(this);
              }
            }
            else
            {
              this.mLastMotionX = f2;
              this.mLastMotionY = f3;
              this.mIsUnableToDrag = true;
              bool = false;
            }
          }
          break;
        case 6: 
          onSecondaryPointerUp(paramMotionEvent);
        }
        if (this.mVelocityTracker == null) {
          this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(paramMotionEvent);
        return this.mIsBeingDragged;
      }
      return false;
      label476:
      bool = true;
    }
    else
    {
      this.mIsBeingDragged = false;
      this.mIsUnableToDrag = false;
      this.mActivePointerId = -1;
      if (this.mVelocityTracker != null)
      {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
      }
      bool = false;
    }
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int j = getChildCount();
    int i3 = paramInt3 - paramInt1;
    int i = paramInt4 - paramInt2;
    View localView1 = getPaddingLeft();
    Object localObject1 = getPaddingTop();
    int i2 = getPaddingRight();
    int k = getPaddingBottom();
    int i1 = getScrollX();
    int m = 0;
    LayoutParams localLayoutParams1;
    for (int n = 0;; localLayoutParams1++)
    {
      View localView2;
      View localView3;
      if (n >= j)
      {
        i2 = i3 - localView1 - i2;
        for (i3 = 0;; i3++)
        {
          if (i3 >= j)
          {
            this.mTopPageBounds = localObject1;
            this.mBottomPageBounds = (i - k);
            this.mDecorChildCount = m;
            if (this.mFirstLayout) {
              scrollToItem(this.mCurItem, false, 0, false);
            }
            this.mFirstLayout = false;
            return;
          }
          localView2 = getChildAt(i3);
          if (localView2.getVisibility() != 8)
          {
            localLayoutParams1 = (LayoutParams)localView2.getLayoutParams();
            if (!localLayoutParams1.isDecor)
            {
              localObject2 = infoForChild(localView2);
              if (localObject2 != null)
              {
                localView3 = localView1 + (int)(i2 * ((ItemInfo)localObject2).offset);
                localObject2 = localObject1;
                if (localLayoutParams1.needsMeasure)
                {
                  localLayoutParams1.needsMeasure = false;
                  localView2.measure(View.MeasureSpec.makeMeasureSpec((int)(i2 * localLayoutParams1.widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(i - localObject1 - k, 1073741824));
                }
                localView2.layout(localView3, localObject2, localView3 + localView2.getMeasuredWidth(), localObject2 + localView2.getMeasuredHeight());
              }
            }
          }
        }
      }
      Object localObject2 = getChildAt(localLayoutParams1);
      if (((View)localObject2).getVisibility() != 8)
      {
        LayoutParams localLayoutParams2 = (LayoutParams)((View)localObject2).getLayoutParams();
        if (localLayoutParams2.isDecor)
        {
          localView3 = 0x7 & localLayoutParams2.gravity;
          int i5 = 0x70 & localLayoutParams2.gravity;
          switch (localView3)
          {
          case 2: 
          case 4: 
          default: 
            localView3 = localView1;
            break;
          case 1: 
            localView3 = Math.max((i3 - ((View)localObject2).getMeasuredWidth()) / 2, localView1);
            break;
          case 3: 
            localView3 = localView1;
            localView1 += ((View)localObject2).getMeasuredWidth();
            break;
          case 5: 
            localView3 = i3 - i2 - ((View)localObject2).getMeasuredWidth();
            i2 += ((View)localObject2).getMeasuredWidth();
          }
          switch (i5)
          {
          default: 
            i5 = localObject1;
            break;
          case 16: 
            i5 = Math.max((i - ((View)localObject2).getMeasuredHeight()) / 2, localObject1);
            break;
          case 48: 
            i5 = localObject1;
            localObject1 += ((View)localObject2).getMeasuredHeight();
            break;
          case 80: 
            i5 = i - k - ((View)localObject2).getMeasuredHeight();
            k += ((View)localObject2).getMeasuredHeight();
          }
          int i4;
          localView3 += localView2;
          ((View)localObject2).layout(i4, i5, i4 + ((View)localObject2).getMeasuredWidth(), i5 + ((View)localObject2).getMeasuredHeight());
          m++;
        }
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    int i = getMeasuredWidth();
    this.mGutterSize = Math.min(i / 10, this.mDefaultGutterSize);
    i = i - getPaddingLeft() - getPaddingRight();
    int m = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int j = getChildCount();
    for (int n = 0;; n++)
    {
      LayoutParams localLayoutParams;
      if (n >= j)
      {
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(m, 1073741824);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        m = getChildCount();
        for (int k = 0;; k++)
        {
          if (k >= m) {
            return;
          }
          View localView1 = getChildAt(k);
          if (localView1.getVisibility() != 8)
          {
            localLayoutParams = (LayoutParams)localView1.getLayoutParams();
            if ((localLayoutParams == null) || (!localLayoutParams.isDecor)) {
              localView1.measure(View.MeasureSpec.makeMeasureSpec((int)(i * localLayoutParams.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
            }
          }
        }
      }
      View localView2 = getChildAt(n);
      if (localView2.getVisibility() != 8)
      {
        localLayoutParams = (LayoutParams)localView2.getLayoutParams();
        if ((localLayoutParams != null) && (localLayoutParams.isDecor))
        {
          int i4 = 0x7 & localLayoutParams.gravity;
          int i3 = 0x70 & localLayoutParams.gravity;
          int i1 = -2147483648;
          int i2 = -2147483648;
          if ((i3 != 48) && (i3 != 80)) {
            i3 = 0;
          } else {
            i3 = 1;
          }
          if ((i4 != 3) && (i4 != 5)) {
            i4 = 0;
          } else {
            i4 = 1;
          }
          if (i3 == 0)
          {
            if (i4 != 0) {
              i2 = 1073741824;
            }
          }
          else {
            i1 = 1073741824;
          }
          int i6 = i;
          int i5 = m;
          if (localLayoutParams.width != -2)
          {
            i1 = 1073741824;
            if (localLayoutParams.width != -1) {
              i6 = localLayoutParams.width;
            }
          }
          if (localLayoutParams.height != -2)
          {
            i2 = 1073741824;
            if (localLayoutParams.height != -1) {
              i5 = localLayoutParams.height;
            }
          }
          localView2.measure(View.MeasureSpec.makeMeasureSpec(i6, i1), View.MeasureSpec.makeMeasureSpec(i5, i2));
          if (i3 == 0)
          {
            if (i4 != 0) {
              i -= localView2.getMeasuredWidth();
            }
          }
          else {
            m -= localView2.getMeasuredHeight();
          }
        }
      }
    }
  }
  
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    int i2;
    int i3;
    int k;
    int j;
    if (this.mDecorChildCount > 0)
    {
      i2 = getScrollX();
      int n = getPaddingLeft();
      i3 = getPaddingRight();
      k = getWidth();
      j = getChildCount();
    }
    for (int i = 0;; i++)
    {
      View localView2;
      if (i >= j)
      {
        if (this.mOnPageChangeListener != null) {
          this.mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
        }
        if (this.mInternalPageChangeListener != null) {
          this.mInternalPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
        }
        if (this.mPageTransformer != null)
        {
          i = getScrollX();
          k = getChildCount();
        }
        for (int m = 0;; m++)
        {
          if (m >= k)
          {
            this.mCalledSuper = true;
            return;
          }
          localView2 = getChildAt(m);
          if (!((LayoutParams)localView2.getLayoutParams()).isDecor)
          {
            float f = (localView2.getLeft() - i) / getClientWidth();
            this.mPageTransformer.transformPage(localView2, f);
          }
        }
      }
      View localView1 = getChildAt(i);
      LayoutParams localLayoutParams = (LayoutParams)localView1.getLayoutParams();
      if (localLayoutParams.isDecor)
      {
        switch (0x7 & localLayoutParams.gravity)
        {
        case 2: 
        case 4: 
        default: 
          int i4 = localView2;
          break;
        case 1: 
          int i5 = Math.max((k - localView1.getMeasuredWidth()) / 2, localView2);
          break;
        case 3: 
          int i6 = localView2;
          int i1;
          localView2 += localView1.getWidth();
          break;
        case 5: 
          i7 = k - i3 - localView1.getMeasuredWidth();
          i3 += localView1.getMeasuredWidth();
        }
        int i7 = i7 + i2 - localView1.getLeft();
        if (i7 != 0) {
          localView1.offsetLeftAndRight(i7);
        }
      }
    }
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    int j = getChildCount();
    int k;
    if ((paramInt & 0x2) == 0)
    {
      k = j - 1;
      i = -1;
      j = -1;
    }
    else
    {
      k = 0;
      i = 1;
      j = j;
    }
    int m = k;
    for (;;)
    {
      if (m == j) {
        return 0;
      }
      View localView = getChildAt(m);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo != null) && (localItemInfo.position == this.mCurItem) && (localView.requestFocus(paramInt, paramRect))) {
          break;
        }
      }
      int n;
      m += i;
    }
    int i = 1;
    return i;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof SavedState))
    {
      SavedState localSavedState = (SavedState)paramParcelable;
      super.onRestoreInstanceState(localSavedState.getSuperState());
      if (this.mAdapter == null)
      {
        this.mRestoredCurItem = localSavedState.position;
        this.mRestoredAdapterState = localSavedState.adapterState;
        this.mRestoredClassLoader = localSavedState.loader;
      }
      else
      {
        this.mAdapter.restoreState(localSavedState.adapterState, localSavedState.loader);
        setCurrentItemInternal(localSavedState.position, false, true);
      }
    }
    else
    {
      super.onRestoreInstanceState(paramParcelable);
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    localSavedState.position = this.mCurItem;
    if (this.mAdapter != null) {
      localSavedState.adapterState = this.mAdapter.saveState();
    }
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      recomputeScrollPosition(paramInt1, paramInt3, this.mPageMargin, this.mPageMargin);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool;
    if (!this.mFakeDragging)
    {
      if ((paramMotionEvent.getAction() != 0) || (paramMotionEvent.getEdgeFlags() == 0))
      {
        if ((this.mAdapter != null) && (this.mAdapter.getCount() != 0))
        {
          if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
          }
          this.mVelocityTracker.addMovement(paramMotionEvent);
          int k = paramMotionEvent.getAction();
          int i = 0;
          int n;
          switch (k & 0xFF)
          {
          case 0: 
            this.mScroller.abortAnimation();
            this.mPopulatePending = false;
            populate();
            this.mIsBeingDragged = true;
            setScrollState(1);
            float f1 = paramMotionEvent.getX();
            this.mInitialMotionX = f1;
            this.mLastMotionX = f1;
            f1 = paramMotionEvent.getY();
            this.mInitialMotionY = f1;
            this.mLastMotionY = f1;
            this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
            break;
          case 1: 
            if (this.mIsBeingDragged)
            {
              VelocityTracker localVelocityTracker = this.mVelocityTracker;
              localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
              int j = (int)VelocityTrackerCompat.getXVelocity(localVelocityTracker, this.mActivePointerId);
              this.mPopulatePending = true;
              n = getClientWidth();
              int i1 = getScrollX();
              ItemInfo localItemInfo = infoForCurrentScrollPosition();
              setCurrentItemInternal(determineTargetPage(localItemInfo.position, (i1 / n - localItemInfo.offset) / localItemInfo.widthFactor, j, (int)(MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId)) - this.mInitialMotionX)), true, true, j);
              this.mActivePointerId = -1;
              endDrag();
              bool = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
            }
            break;
          case 2: 
            if (!this.mIsBeingDragged)
            {
              n = MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId);
              float f2 = MotionEventCompat.getX(paramMotionEvent, n);
              float f4 = Math.abs(f2 - this.mLastMotionX);
              float f3 = MotionEventCompat.getY(paramMotionEvent, n);
              float f5 = Math.abs(f3 - this.mLastMotionY);
              if ((f4 > this.mTouchSlop) && (f4 > f5))
              {
                this.mIsBeingDragged = true;
                if (f2 - this.mInitialMotionX <= 0.0F) {
                  f2 = this.mInitialMotionX - this.mTouchSlop;
                } else {
                  f2 = this.mInitialMotionX + this.mTouchSlop;
                }
                this.mLastMotionX = f2;
                this.mLastMotionY = f3;
                setScrollState(1);
                setScrollingCacheEnabled(true);
              }
            }
            if (this.mIsBeingDragged) {
              bool = false | performDrag(MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId)));
            }
            break;
          case 3: 
            if (this.mIsBeingDragged)
            {
              scrollToItem(this.mCurItem, true, 0, false);
              this.mActivePointerId = -1;
              endDrag();
              bool = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
            }
            break;
          case 5: 
            int m = MotionEventCompat.getActionIndex(paramMotionEvent);
            this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, m);
            this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, m);
            break;
          case 6: 
            onSecondaryPointerUp(paramMotionEvent);
            this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId));
          }
          if (bool) {
            ViewCompat.postInvalidateOnAnimation(this);
          }
          bool = true;
        }
        else
        {
          bool = false;
        }
      }
      else {
        bool = false;
      }
    }
    else {
      bool = true;
    }
    return bool;
  }
  
  boolean pageLeft()
  {
    boolean bool = true;
    if (this.mCurItem <= 0) {
      bool = false;
    } else {
      setCurrentItem(-1 + this.mCurItem, bool);
    }
    return bool;
  }
  
  boolean pageRight()
  {
    boolean bool = true;
    if ((this.mAdapter == null) || (this.mCurItem >= -1 + this.mAdapter.getCount())) {
      bool = false;
    } else {
      setCurrentItem(1 + this.mCurItem, bool);
    }
    return bool;
  }
  
  void populate()
  {
    populate(this.mCurItem);
  }
  
  void populate(int paramInt)
  {
    Object localObject3 = null;
    int i = 2;
    if (this.mCurItem != paramInt)
    {
      if (this.mCurItem < paramInt)
      {
        i = 66;
        localObject3 = infoForPosition(this.mCurItem);
        this.mCurItem = paramInt;
      }
    }
    else
    {
      if (this.mAdapter != null) {
        break label57;
      }
      sortChildDrawingOrder();
    }
    label57:
    label1217:
    label1222:
    for (;;)
    {
      return;
      i = 17;
      break;
      if (this.mPopulatePending)
      {
        sortChildDrawingOrder();
      }
      else if (getWindowToken() != null)
      {
        this.mAdapter.startUpdate(this);
        int k = this.mOffscreenPageLimit;
        int i2 = Math.max(0, this.mCurItem - k);
        int j = this.mAdapter.getCount();
        int n = Math.min(j - 1, k + this.mCurItem);
        String str;
        if (j != this.mExpectedAdapterCount)
        {
          try
          {
            str = getResources().getResourceName(getId());
            str = str;
          }
          catch (Resources.NotFoundException localNotFoundException)
          {
            for (;;)
            {
              str = Integer.toHexString(getId());
            }
          }
          throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + j + " Pager id: " + str + " Pager class: " + getClass() + " Problematic adapter: " + this.mAdapter.getClass());
        }
        Object localObject2 = null;
        int i1 = 0;
        if (i1 < this.mItems.size())
        {
          ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(i1);
          if (localItemInfo1.position < this.mCurItem) {
            break label662;
          }
          if (localItemInfo1.position == this.mCurItem) {
            localObject2 = localItemInfo1;
          }
        }
        if ((localObject2 == null) && (j > 0)) {
          localObject2 = addNewItem(this.mCurItem, i1);
        }
        int i5;
        Object localObject6;
        label357:
        int i3;
        float f1;
        ItemInfo localItemInfo2;
        if (localObject2 != null)
        {
          float f4 = 0.0F;
          i5 = i1 - 1;
          if (i5 >= 0)
          {
            localObject6 = (ItemInfo)this.mItems.get(i5);
            float f2 = 2.0F - ((ItemInfo)localObject2).widthFactor + getPaddingLeft() / getClientWidth();
            i3 = -1 + this.mCurItem;
            if (i3 >= 0)
            {
              if ((f4 < f2) || (i3 >= i2)) {
                break label766;
              }
              if (localObject6 != null) {
                break label674;
              }
            }
            f1 = ((ItemInfo)localObject2).widthFactor;
            i3 = i1 + 1;
            if (f1 < 2.0F)
            {
              int i4 = this.mItems.size();
              if (i3 >= i4) {
                break label879;
              }
              localItemInfo2 = (ItemInfo)this.mItems.get(i3);
              label466:
              float f3 = 2.0F + getPaddingRight() / getClientWidth();
              i5 = 1 + this.mCurItem;
              if (i5 < j)
              {
                if ((f1 < f3) || (i5 <= n)) {
                  break label982;
                }
                if (localItemInfo2 != null) {
                  break label885;
                }
              }
            }
            calculatePageOffsets((ItemInfo)localObject2, i1, (ItemInfo)localObject3);
          }
        }
        else
        {
          localObject3 = this.mAdapter;
          j = this.mCurItem;
          if (localObject2 == null) {
            break label1115;
          }
        }
        int m;
        label976:
        label982:
        label1115:
        for (localObject2 = ((ItemInfo)localObject2).object;; localObject2 = null)
        {
          ((PagerAdapter)localObject3).setPrimaryItem(this, j, localObject2);
          this.mAdapter.finishUpdate(this);
          m = getChildCount();
          for (j = 0; j < m; j++)
          {
            Object localObject4 = getChildAt(j);
            localObject2 = (LayoutParams)((View)localObject4).getLayoutParams();
            ((LayoutParams)localObject2).childIndex = j;
            if ((!((LayoutParams)localObject2).isDecor) && (((LayoutParams)localObject2).widthFactor == 0.0F))
            {
              localObject4 = infoForChild((View)localObject4);
              if (localObject4 != null)
              {
                ((LayoutParams)localObject2).widthFactor = ((ItemInfo)localObject4).widthFactor;
                ((LayoutParams)localObject2).position = ((ItemInfo)localObject4).position;
              }
            }
          }
          i1++;
          break;
          localObject6 = null;
          break label357;
          int i12 = ((ItemInfo)localObject6).position;
          if ((i3 == i12) && (!((ItemInfo)localObject6).scrolling))
          {
            this.mItems.remove(i5);
            PagerAdapter localPagerAdapter2 = this.mAdapter;
            localObject6 = ((ItemInfo)localObject6).object;
            localPagerAdapter2.destroyItem(this, i3, localObject6);
            i5--;
            i1--;
            if (i5 < 0) {
              break label760;
            }
          }
          label760:
          for (localObject6 = (ItemInfo)this.mItems.get(i5);; localObject6 = null)
          {
            i3--;
            break;
          }
          label766:
          float f5;
          if (localObject6 != null)
          {
            int i13 = ((ItemInfo)localObject6).position;
            if (i3 == i13)
            {
              localItemInfo2 += ((ItemInfo)localObject6).widthFactor;
              i5--;
              if (i5 >= 0) {}
              for (localObject6 = (ItemInfo)this.mItems.get(i5);; localObject6 = null) {
                break;
              }
            }
          }
          int i9 = i5 + 1;
          f5 += addNewItem(i3, i9).widthFactor;
          i1++;
          if (i5 >= 0) {}
          for (ItemInfo localItemInfo6 = (ItemInfo)this.mItems.get(i5);; localItemInfo6 = null) {
            break;
          }
          label879:
          Object localObject5 = null;
          break label466;
          label885:
          int i10 = localObject5.position;
          if ((i5 == i10) && (!localObject5.scrolling))
          {
            this.mItems.remove(i3);
            PagerAdapter localPagerAdapter1 = this.mAdapter;
            localObject5 = localObject5.object;
            localPagerAdapter1.destroyItem(this, i5, localObject5);
            int i6 = this.mItems.size();
            if (i3 >= i6) {
              break label976;
            }
          }
          for (ItemInfo localItemInfo3 = (ItemInfo)this.mItems.get(i3);; localItemInfo3 = null)
          {
            i5++;
            break;
          }
          if (localItemInfo3 != null)
          {
            int i11 = localItemInfo3.position;
            if (i5 == i11)
            {
              f1 += localItemInfo3.widthFactor;
              i3++;
              int i7 = this.mItems.size();
              if (i3 < i7) {}
              for (localItemInfo4 = (ItemInfo)this.mItems.get(i3);; localItemInfo4 = null) {
                break;
              }
            }
          }
          ItemInfo localItemInfo4 = addNewItem(i5, i3);
          i3++;
          f1 += localItemInfo4.widthFactor;
          int i8 = this.mItems.size();
          if (i3 < i8) {}
          for (ItemInfo localItemInfo5 = (ItemInfo)this.mItems.get(i3);; localItemInfo5 = null) {
            break;
          }
        }
        sortChildDrawingOrder();
        if (hasFocus())
        {
          Object localObject1 = findFocus();
          if (localObject1 != null) {}
          for (localObject1 = infoForAnyChild((View)localObject1);; localObject1 = null)
          {
            if ((localObject1 != null) && (((ItemInfo)localObject1).position == this.mCurItem)) {
              break label1222;
            }
            for (m = 0;; m++)
            {
              if (m >= getChildCount()) {
                break label1217;
              }
              localObject2 = getChildAt(m);
              localObject1 = infoForChild((View)localObject2);
              if ((localObject1 != null) && (((ItemInfo)localObject1).position == this.mCurItem) && (((View)localObject2).requestFocus(str))) {
                break;
              }
            }
            break;
          }
        }
      }
    }
  }
  
  public void removeView(View paramView)
  {
    if (!this.mInLayout) {
      super.removeView(paramView);
    } else {
      removeViewInLayout(paramView);
    }
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter)
  {
    if (this.mAdapter != null)
    {
      this.mAdapter.unregisterDataSetObserver(this.mObserver);
      this.mAdapter.startUpdate(this);
    }
    PagerAdapter localPagerAdapter;
    for (int i = 0;; localPagerAdapter++)
    {
      if (i >= this.mItems.size())
      {
        this.mAdapter.finishUpdate(this);
        this.mItems.clear();
        removeNonDecorViews();
        this.mCurItem = 0;
        scrollTo(0, 0);
        localPagerAdapter = this.mAdapter;
        this.mAdapter = paramPagerAdapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null)
        {
          if (this.mObserver == null) {
            this.mObserver = new PagerObserver(null);
          }
          this.mAdapter.registerDataSetObserver(this.mObserver);
          this.mPopulatePending = false;
          boolean bool = this.mFirstLayout;
          this.mFirstLayout = true;
          this.mExpectedAdapterCount = this.mAdapter.getCount();
          if (this.mRestoredCurItem < 0)
          {
            if (bool) {
              requestLayout();
            } else {
              populate();
            }
          }
          else
          {
            this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
            setCurrentItemInternal(this.mRestoredCurItem, false, true);
            this.mRestoredCurItem = -1;
            this.mRestoredAdapterState = null;
            this.mRestoredClassLoader = null;
          }
        }
        if ((this.mAdapterChangeListener != null) && (localPagerAdapter != paramPagerAdapter)) {
          this.mAdapterChangeListener.onAdapterChanged(localPagerAdapter, paramPagerAdapter);
        }
        return;
      }
      ItemInfo localItemInfo = (ItemInfo)this.mItems.get(localPagerAdapter);
      this.mAdapter.destroyItem(this, localItemInfo.position, localItemInfo.object);
    }
  }
  
  void setChildrenDrawingOrderEnabledCompat(boolean paramBoolean)
  {
    if ((Build.VERSION.SDK_INT < 7) || (this.mSetChildrenDrawingOrderEnabled == null)) {}
    try
    {
      localObject = new Class[1];
      localObject[0] = Boolean.TYPE;
      this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", (Class[])localObject);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      try
      {
        for (;;)
        {
          Method localMethod = this.mSetChildrenDrawingOrderEnabled;
          Object localObject = new Object[1];
          localObject[0] = Boolean.valueOf(paramBoolean);
          localMethod.invoke(this, (Object[])localObject);
          return;
          localNoSuchMethodException = localNoSuchMethodException;
          Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", localNoSuchMethodException);
        }
      }
      catch (Exception localException)
      {
        for (;;)
        {
          Log.e("ViewPager", "Error changing children drawing order", localException);
        }
      }
    }
  }
  
  public void setCurrentItem(int paramInt)
  {
    this.mPopulatePending = false;
    boolean bool;
    if (this.mFirstLayout) {
      bool = false;
    } else {
      bool = true;
    }
    setCurrentItemInternal(paramInt, bool, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }
  
  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
  {
    boolean bool = true;
    if ((this.mAdapter != null) && (this.mAdapter.getCount() > 0))
    {
      if ((paramBoolean2) || (this.mCurItem != paramInt1) || (this.mItems.size() == 0))
      {
        if (paramInt1 >= 0)
        {
          if (paramInt1 >= this.mAdapter.getCount()) {
            paramInt1 = -1 + this.mAdapter.getCount();
          }
        }
        else {
          paramInt1 = 0;
        }
        int i = this.mOffscreenPageLimit;
        if ((paramInt1 > i + this.mCurItem) || (paramInt1 < this.mCurItem - i)) {}
        for (i = 0;; i++)
        {
          if (i >= this.mItems.size())
          {
            if (this.mCurItem == paramInt1) {
              bool = false;
            }
            if (!this.mFirstLayout)
            {
              populate(paramInt1);
              scrollToItem(paramInt1, paramBoolean1, paramInt2, bool);
              break;
            }
            this.mCurItem = paramInt1;
            if ((bool) && (this.mOnPageChangeListener != null)) {
              this.mOnPageChangeListener.onPageSelected(paramInt1);
            }
            if ((bool) && (this.mInternalPageChangeListener != null)) {
              this.mInternalPageChangeListener.onPageSelected(paramInt1);
            }
            requestLayout();
            break;
          }
          ((ItemInfo)this.mItems.get(i)).scrolling = bool;
        }
      }
      setScrollingCacheEnabled(false);
    }
    else
    {
      setScrollingCacheEnabled(false);
    }
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    OnPageChangeListener localOnPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return localOnPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt)
  {
    if (paramInt < 1)
    {
      Log.w("ViewPager", "Requested offscreen page limit " + paramInt + " too small; defaulting to " + 1);
      paramInt = 1;
    }
    if (paramInt != this.mOffscreenPageLimit)
    {
      this.mOffscreenPageLimit = paramInt;
      populate();
    }
  }
  
  void setOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener)
  {
    this.mAdapterChangeListener = paramOnAdapterChangeListener;
  }
  
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setPageMargin(int paramInt)
  {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }
  
  public void setPageMarginDrawable(int paramInt)
  {
    setPageMarginDrawable(getContext().getResources().getDrawable(paramInt));
  }
  
  public void setPageMarginDrawable(Drawable paramDrawable)
  {
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null) {
      refreshDrawableState();
    }
    boolean bool;
    if (paramDrawable != null) {
      bool = false;
    } else {
      bool = true;
    }
    setWillNotDraw(bool);
    invalidate();
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer)
  {
    int i = 1;
    if (Build.VERSION.SDK_INT >= 11)
    {
      boolean bool1;
      if (paramPageTransformer == null) {
        bool1 = false;
      } else {
        bool1 = i;
      }
      boolean bool2;
      if (this.mPageTransformer == null) {
        bool2 = false;
      } else {
        bool2 = i;
      }
      if (bool1 == bool2) {
        bool2 = false;
      } else {
        bool2 = i;
      }
      this.mPageTransformer = paramPageTransformer;
      setChildrenDrawingOrderEnabledCompat(bool1);
      if (!bool1)
      {
        this.mDrawingOrder = 0;
      }
      else
      {
        if (paramBoolean) {
          i = 2;
        }
        this.mDrawingOrder = i;
      }
      if (bool2) {
        populate();
      }
    }
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2)
  {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3)
  {
    if (getChildCount() != 0)
    {
      int m = getScrollX();
      int k = getScrollY();
      int i = paramInt1 - m;
      int j = paramInt2 - k;
      if ((i != 0) || (j != 0))
      {
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int n = getClientWidth();
        int i2 = n / 2;
        float f3 = Math.min(1.0F, 1.0F * Math.abs(i) / n);
        float f2 = i2 + i2 * distanceInfluenceForSnapDuration(f3);
        int i3 = Math.abs(paramInt3);
        if (i3 <= 0)
        {
          float f1 = n * this.mAdapter.getPageWidth(this.mCurItem);
          i1 = (int)(100.0F * (1.0F + Math.abs(i) / (f1 + this.mPageMargin)));
        }
        else
        {
          i1 = 4 * Math.round(1000.0F * Math.abs(f2 / i3));
        }
        int i1 = Math.min(i1, 600);
        this.mScroller.startScroll(m, k, i, j, i1);
        ViewCompat.postInvalidateOnAnimation(this);
      }
      else
      {
        completeScroll(false);
        populate();
        setScrollState(0);
      }
    }
    else
    {
      setScrollingCacheEnabled(false);
    }
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((!super.verifyDrawable(paramDrawable)) && (paramDrawable != this.mMarginDrawable)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static class ViewPositionComparator
    implements Comparator<View>
  {
    public int compare(View paramView1, View paramView2)
    {
      ViewPager.LayoutParams localLayoutParams2 = (ViewPager.LayoutParams)paramView1.getLayoutParams();
      ViewPager.LayoutParams localLayoutParams1 = (ViewPager.LayoutParams)paramView2.getLayoutParams();
      int i;
      if (localLayoutParams2.isDecor == localLayoutParams1.isDecor) {
        i = localLayoutParams2.position - localLayoutParams1.position;
      } else if (!localLayoutParams2.isDecor) {
        i = -1;
      } else {
        i = 1;
      }
      return i;
    }
  }
  
  public static class LayoutParams
    extends ViewGroup.LayoutParams
  {
    int childIndex;
    public int gravity;
    public boolean isDecor;
    boolean needsMeasure;
    int position;
    float widthFactor = 0.0F;
    
    public LayoutParams()
    {
      super(-1);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, ViewPager.LAYOUT_ATTRS);
      this.gravity = localTypedArray.getInteger(0, 48);
      localTypedArray.recycle();
    }
  }
  
  private class PagerObserver
    extends DataSetObserver
  {
    private PagerObserver() {}
    
    public void onChanged()
    {
      ViewPager.this.dataSetChanged();
    }
    
    public void onInvalidated()
    {
      ViewPager.this.dataSetChanged();
    }
  }
  
  class MyAccessibilityDelegate
    extends AccessibilityDelegateCompat
  {
    MyAccessibilityDelegate() {}
    
    public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
      paramAccessibilityEvent.setClassName(ViewPager.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      int i = 1;
      super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
      paramAccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      if ((ViewPager.this.mAdapter == null) || (ViewPager.this.mAdapter.getCount() <= i)) {
        i = 0;
      }
      paramAccessibilityNodeInfoCompat.setScrollable(i);
      if ((ViewPager.this.mAdapter != null) && (ViewPager.this.mCurItem >= 0) && (ViewPager.this.mCurItem < -1 + ViewPager.this.mAdapter.getCount())) {
        paramAccessibilityNodeInfoCompat.addAction(4096);
      }
      if ((ViewPager.this.mAdapter != null) && (ViewPager.this.mCurItem > 0) && (ViewPager.this.mCurItem < ViewPager.this.mAdapter.getCount())) {
        paramAccessibilityNodeInfoCompat.addAction(8192);
      }
    }
    
    public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
    {
      boolean bool = true;
      if (!super.performAccessibilityAction(paramView, paramInt, paramBundle)) {
        switch (paramInt)
        {
        default: 
          bool = false;
          break;
        case 4096: 
          if ((ViewPager.this.mAdapter == null) || (ViewPager.this.mCurItem < 0) || (ViewPager.this.mCurItem >= -1 + ViewPager.this.mAdapter.getCount())) {
            bool = false;
          } else {
            ViewPager.this.setCurrentItem(1 + ViewPager.this.mCurItem);
          }
          break;
        case 8192: 
          if ((ViewPager.this.mAdapter == null) || (ViewPager.this.mCurItem <= 0) || (ViewPager.this.mCurItem >= ViewPager.this.mAdapter.getCount())) {
            bool = false;
          } else {
            ViewPager.this.setCurrentItem(-1 + ViewPager.this.mCurItem);
          }
          break;
        }
      }
      return bool;
    }
  }
  
  public static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks()
    {
      public ViewPager.SavedState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
      {
        return new ViewPager.SavedState(paramAnonymousParcel, paramAnonymousClassLoader);
      }
      
      public ViewPager.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ViewPager.SavedState[paramAnonymousInt];
      }
    });
    Parcelable adapterState;
    ClassLoader loader;
    int position;
    
    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super();
      if (paramClassLoader == null) {
        paramClassLoader = getClass().getClassLoader();
      }
      this.position = paramParcel.readInt();
      this.adapterState = paramParcel.readParcelable(paramClassLoader);
      this.loader = paramClassLoader;
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(this.position);
      paramParcel.writeParcelable(this.adapterState, paramInt);
    }
  }
  
  static abstract interface Decor {}
  
  static abstract interface OnAdapterChangeListener
  {
    public abstract void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2);
  }
  
  public static abstract interface PageTransformer
  {
    public abstract void transformPage(View paramView, float paramFloat);
  }
  
  public static class SimpleOnPageChangeListener
    implements ViewPager.OnPageChangeListener
  {
    public void onPageScrollStateChanged(int paramInt) {}
    
    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}
    
    public void onPageSelected(int paramInt) {}
  }
  
  public static abstract interface OnPageChangeListener
  {
    public abstract void onPageScrollStateChanged(int paramInt);
    
    public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);
    
    public abstract void onPageSelected(int paramInt);
  }
  
  static class ItemInfo
  {
    Object object;
    float offset;
    int position;
    boolean scrolling;
    float widthFactor;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewPager
 * JD-Core Version:    0.7.0.1
 */