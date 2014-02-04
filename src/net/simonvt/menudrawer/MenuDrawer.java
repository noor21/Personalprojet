package net.simonvt.menudrawer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import net.simonvt.menudrawer.compat.ActionBarHelper;

public abstract class MenuDrawer
  extends ViewGroup
{
  protected static final int ANIMATION_DELAY = 16;
  private static final boolean DEBUG = false;
  private static final int DEFAULT_ANIMATION_DURATION = 600;
  private static final int DEFAULT_DRAG_BEZEL_DP = 24;
  private static final int DEFAULT_DROP_SHADOW_DP = 6;
  static final int INDICATOR_ANIM_DURATION = 800;
  protected static final Interpolator INDICATOR_INTERPOLATOR = new AccelerateInterpolator();
  public static final int MENU_DRAG_CONTENT = 0;
  public static final int MENU_DRAG_WINDOW = 1;
  protected static final Interpolator SMOOTH_INTERPOLATOR;
  public static final int STATE_CLOSED = 0;
  public static final int STATE_CLOSING = 1;
  public static final int STATE_DRAGGING = 2;
  public static final int STATE_OPEN = 8;
  public static final int STATE_OPENING = 4;
  private static final String TAG = "MenuDrawer";
  public static final int TOUCH_MODE_BEZEL = 1;
  public static final int TOUCH_MODE_FULLSCREEN = 2;
  public static final int TOUCH_MODE_NONE;
  static final boolean USE_TRANSLATIONS;
  private ActionBarHelper mActionBarHelper;
  protected Bitmap mActiveIndicator;
  protected int mActivePosition;
  protected final Rect mActiveRect = new Rect();
  protected View mActiveView;
  private Activity mActivity;
  private boolean mAllowIndicatorAnimation;
  protected BuildLayerFrameLayout mContentContainer;
  private int mCurrentUpContentDesc;
  private int mDragMode = 0;
  protected boolean mDrawOverlay;
  private int mDrawerClosedContentDesc;
  protected boolean mDrawerIndicatorEnabled;
  private int mDrawerOpenContentDesc;
  protected int mDrawerState = 0;
  protected int mDropShadowColor;
  protected Drawable mDropShadowDrawable;
  protected boolean mDropShadowEnabled;
  protected final Rect mDropShadowRect = new Rect();
  protected int mDropShadowSize;
  protected boolean mHardwareLayersEnabled = true;
  protected boolean mIndicatorAnimating;
  private final Rect mIndicatorClipRect = new Rect();
  protected float mIndicatorOffset;
  private Runnable mIndicatorRunnable = new Runnable()
  {
    public void run()
    {
      MenuDrawer.this.animateIndicatorInvalidate();
    }
  };
  private FloatScroller mIndicatorScroller;
  protected int mIndicatorStartPos;
  protected boolean mIsStatic;
  protected int mMaxAnimationDuration = 600;
  protected BuildLayerFrameLayout mMenuContainer;
  protected Drawable mMenuOverlay;
  protected int mMenuSize;
  private View mMenuView;
  protected boolean mMenuVisible;
  protected float mOffsetPixels;
  private OnDrawerStateChangeListener mOnDrawerStateChangeListener;
  protected OnInterceptMoveEventListener mOnInterceptMoveEventListener;
  protected Position mPosition;
  private ViewTreeObserver.OnScrollChangedListener mScrollListener = new ViewTreeObserver.OnScrollChangedListener()
  {
    public void onScrollChanged()
    {
      if ((MenuDrawer.this.mActiveView != null) && (MenuDrawer.this.isViewDescendant(MenuDrawer.this.mActiveView)))
      {
        MenuDrawer.this.mActiveView.getDrawingRect(MenuDrawer.this.mTempRect);
        MenuDrawer.this.offsetDescendantRectToMyCoords(MenuDrawer.this.mActiveView, MenuDrawer.this.mTempRect);
        if ((MenuDrawer.this.mTempRect.left != MenuDrawer.this.mActiveRect.left) || (MenuDrawer.this.mTempRect.top != MenuDrawer.this.mActiveRect.top) || (MenuDrawer.this.mTempRect.right != MenuDrawer.this.mActiveRect.right) || (MenuDrawer.this.mTempRect.bottom != MenuDrawer.this.mActiveRect.bottom)) {
          MenuDrawer.this.invalidate();
        }
      }
    }
  };
  protected SlideDrawable mSlideDrawable;
  protected Bundle mState;
  private final Rect mTempRect = new Rect();
  protected Drawable mThemeUpIndicator;
  protected int mTouchBezelSize;
  protected int mTouchMode = 1;
  protected int mTouchSize;
  
  static
  {
    boolean bool;
    if (Build.VERSION.SDK_INT < 14) {
      bool = false;
    } else {
      bool = true;
    }
    USE_TRANSLATIONS = bool;
    SMOOTH_INTERPOLATOR = new SmoothInterpolator();
  }
  
  MenuDrawer(Activity paramActivity, int paramInt)
  {
    this(paramActivity);
    this.mActivity = paramActivity;
    this.mDragMode = paramInt;
  }
  
  public MenuDrawer(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public MenuDrawer(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, R.attr.menuDrawerStyle);
  }
  
  public MenuDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initDrawer(paramContext, paramAttributeSet, paramInt);
  }
  
  private void animateIndicatorInvalidate()
  {
    if (this.mIndicatorScroller.computeScrollOffset())
    {
      this.mIndicatorOffset = this.mIndicatorScroller.getCurr();
      invalidate();
      if (!this.mIndicatorScroller.isFinished()) {}
    }
    else
    {
      completeAnimatingIndicator();
      return;
    }
    postOnAnimation(this.mIndicatorRunnable);
  }
  
  public static MenuDrawer attach(Activity paramActivity)
  {
    return attach(paramActivity, Type.BEHIND);
  }
  
  public static MenuDrawer attach(Activity paramActivity, Type paramType)
  {
    return attach(paramActivity, paramType, Position.LEFT);
  }
  
  public static MenuDrawer attach(Activity paramActivity, Type paramType, Position paramPosition)
  {
    return attach(paramActivity, paramType, paramPosition, 0);
  }
  
  public static MenuDrawer attach(Activity paramActivity, Type paramType, Position paramPosition, int paramInt)
  {
    MenuDrawer localMenuDrawer = createMenuDrawer(paramActivity, paramInt, paramPosition, paramType);
    localMenuDrawer.setId(R.id.md__drawer);
    switch (paramInt)
    {
    default: 
      throw new RuntimeException("Unknown menu mode: " + paramInt);
    case 0: 
      attachToContent(paramActivity, localMenuDrawer);
      break;
    case 1: 
      attachToDecor(paramActivity, localMenuDrawer);
    }
    return localMenuDrawer;
  }
  
  public static MenuDrawer attach(Activity paramActivity, Position paramPosition)
  {
    return attach(paramActivity, Type.BEHIND, paramPosition);
  }
  
  private static void attachToContent(Activity paramActivity, MenuDrawer paramMenuDrawer)
  {
    ViewGroup localViewGroup = (ViewGroup)paramActivity.findViewById(16908290);
    localViewGroup.removeAllViews();
    localViewGroup.addView(paramMenuDrawer, -1, -1);
  }
  
  private static void attachToDecor(Activity paramActivity, MenuDrawer paramMenuDrawer)
  {
    ViewGroup localViewGroup1 = (ViewGroup)paramActivity.getWindow().getDecorView();
    ViewGroup localViewGroup2 = (ViewGroup)localViewGroup1.getChildAt(0);
    localViewGroup1.removeAllViews();
    localViewGroup1.addView(paramMenuDrawer, -1, -1);
    paramMenuDrawer.mContentContainer.addView(localViewGroup2, localViewGroup2.getLayoutParams());
  }
  
  private void completeAnimatingIndicator()
  {
    this.mIndicatorOffset = 1.0F;
    this.mIndicatorAnimating = false;
    invalidate();
  }
  
  private static MenuDrawer createMenuDrawer(Activity paramActivity, int paramInt, Position paramPosition, Type paramType)
  {
    Object localObject;
    if (paramType != Type.STATIC)
    {
      if (paramType != Type.OVERLAY)
      {
        localObject = new SlidingDrawer(paramActivity, paramInt);
        if (paramPosition == Position.LEFT) {
          ((MenuDrawer)localObject).setupUpIndicator(paramActivity);
        }
      }
      else
      {
        localObject = new OverlayDrawer(paramActivity, paramInt);
        if (paramPosition == Position.LEFT) {
          ((MenuDrawer)localObject).setupUpIndicator(paramActivity);
        }
      }
    }
    else {
      localObject = new StaticDrawer(paramActivity);
    }
    ((MenuDrawer)localObject).mDragMode = paramInt;
    ((MenuDrawer)localObject).mPosition = paramPosition;
    return localObject;
  }
  
  private void drawDropShadow(Canvas paramCanvas)
  {
    if (this.mDropShadowDrawable == null) {
      setDropShadowColor(this.mDropShadowColor);
    }
    updateDropShadowRect();
    this.mDropShadowDrawable.setBounds(this.mDropShadowRect);
    this.mDropShadowDrawable.draw(paramCanvas);
  }
  
  private void drawIndicator(Canvas paramCanvas)
  {
    Integer localInteger = (Integer)this.mActiveView.getTag(R.id.mdActiveViewPosition);
    int i;
    if (localInteger != null) {
      i = localInteger.intValue();
    } else {
      i = 0;
    }
    if (i == this.mActivePosition)
    {
      updateIndicatorClipRect();
      paramCanvas.save();
      paramCanvas.clipRect(this.mIndicatorClipRect);
      i = 0;
      int j = 0;
      switch (this.mPosition)
      {
      case BOTTOM: 
      case LEFT: 
        i = this.mIndicatorClipRect.left;
        j = this.mIndicatorClipRect.top;
        break;
      case RIGHT: 
        i = this.mIndicatorClipRect.right - this.mActiveIndicator.getWidth();
        j = this.mIndicatorClipRect.top;
        break;
      case TOP: 
        i = this.mIndicatorClipRect.left;
        j = this.mIndicatorClipRect.bottom - this.mActiveIndicator.getHeight();
      }
      paramCanvas.drawBitmap(this.mActiveIndicator, i, j, null);
      paramCanvas.restore();
    }
  }
  
  private int getIndicatorStartPos()
  {
    int i;
    switch (this.mPosition)
    {
    default: 
      i = this.mIndicatorClipRect.top;
      break;
    case LEFT: 
      i = this.mIndicatorClipRect.left;
      break;
    case RIGHT: 
      i = this.mIndicatorClipRect.top;
      break;
    case TOP: 
      i = this.mIndicatorClipRect.left;
    }
    return i;
  }
  
  private boolean shouldDrawIndicator()
  {
    boolean bool;
    if ((this.mActiveView == null) || (this.mActiveIndicator == null) || (!isViewDescendant(this.mActiveView))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void startAnimatingIndicator()
  {
    this.mIndicatorStartPos = getIndicatorStartPos();
    this.mIndicatorAnimating = true;
    this.mIndicatorScroller.startScroll(0.0F, 1.0F, 800);
    animateIndicatorInvalidate();
  }
  
  public void closeMenu()
  {
    closeMenu(true);
  }
  
  public abstract void closeMenu(boolean paramBoolean);
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    int i = (int)this.mOffsetPixels;
    if ((this.mDrawOverlay) && (i != 0)) {
      drawOverlay(paramCanvas);
    }
    if ((this.mDropShadowEnabled) && (i != 0)) {
      drawDropShadow(paramCanvas);
    }
    if ((shouldDrawIndicator()) && (i != 0)) {
      drawIndicator(paramCanvas);
    }
  }
  
  protected void dispatchOnDrawerSlide(float paramFloat, int paramInt)
  {
    if (this.mOnDrawerStateChangeListener != null) {
      this.mOnDrawerStateChangeListener.onDrawerSlide(paramFloat, paramInt);
    }
  }
  
  protected int dpToPx(int paramInt)
  {
    return (int)(0.5F + getResources().getDisplayMetrics().density * paramInt);
  }
  
  protected abstract void drawOverlay(Canvas paramCanvas);
  
  protected boolean fitSystemWindows(Rect paramRect)
  {
    if (this.mDragMode == 1) {
      this.mMenuContainer.setPadding(0, paramRect.top, 0, 0);
    }
    return super.fitSystemWindows(paramRect);
  }
  
  public boolean getAllowIndicatorAnimation()
  {
    return this.mAllowIndicatorAnimation;
  }
  
  public ViewGroup getContentContainer()
  {
    Object localObject;
    if (this.mDragMode != 0) {
      localObject = (ViewGroup)findViewById(16908290);
    } else {
      localObject = this.mContentContainer;
    }
    return localObject;
  }
  
  public boolean getDrawOverlay()
  {
    return this.mDrawOverlay;
  }
  
  public int getDrawerState()
  {
    return this.mDrawerState;
  }
  
  public Drawable getDropShadow()
  {
    return this.mDropShadowDrawable;
  }
  
  protected GradientDrawable.Orientation getDropShadowOrientation()
  {
    GradientDrawable.Orientation localOrientation;
    switch (this.mPosition)
    {
    default: 
      localOrientation = GradientDrawable.Orientation.RIGHT_LEFT;
      break;
    case LEFT: 
      localOrientation = GradientDrawable.Orientation.BOTTOM_TOP;
      break;
    case RIGHT: 
      localOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
      break;
    case TOP: 
      localOrientation = GradientDrawable.Orientation.TOP_BOTTOM;
    }
    return localOrientation;
  }
  
  public ViewGroup getMenuContainer()
  {
    return this.mMenuContainer;
  }
  
  public int getMenuSize()
  {
    return this.mMenuSize;
  }
  
  public View getMenuView()
  {
    return this.mMenuView;
  }
  
  public abstract boolean getOffsetMenuEnabled();
  
  public abstract int getTouchBezelSize();
  
  public abstract int getTouchMode();
  
  protected void initDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    setWillNotDraw(false);
    setFocusable(false);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MenuDrawer, R.attr.menuDrawerStyle, R.style.Widget_MenuDrawer);
    Drawable localDrawable1 = localTypedArray.getDrawable(0);
    Drawable localDrawable2 = localTypedArray.getDrawable(1);
    this.mMenuSize = localTypedArray.getDimensionPixelSize(2, dpToPx(240));
    int i = localTypedArray.getResourceId(3, 0);
    if (i != 0) {
      this.mActiveIndicator = BitmapFactory.decodeResource(getResources(), i);
    }
    this.mDropShadowEnabled = localTypedArray.getBoolean(4, true);
    this.mDropShadowDrawable = localTypedArray.getDrawable(7);
    if (this.mDropShadowDrawable == null) {
      this.mDropShadowColor = localTypedArray.getColor(6, -16777216);
    }
    this.mDropShadowSize = localTypedArray.getDimensionPixelSize(5, dpToPx(6));
    this.mTouchBezelSize = localTypedArray.getDimensionPixelSize(8, dpToPx(24));
    this.mAllowIndicatorAnimation = localTypedArray.getBoolean(9, false);
    this.mMaxAnimationDuration = localTypedArray.getInt(10, 600);
    i = localTypedArray.getResourceId(11, -1);
    if (i != -1) {
      setSlideDrawable(i);
    }
    this.mDrawerOpenContentDesc = localTypedArray.getResourceId(12, 0);
    this.mDrawerClosedContentDesc = localTypedArray.getResourceId(13, 0);
    this.mDrawOverlay = localTypedArray.getBoolean(14, true);
    this.mPosition = Position.fromValue(localTypedArray.getInt(15, 0));
    localTypedArray.recycle();
    this.mMenuContainer = new NoClickThroughFrameLayout(paramContext);
    this.mMenuContainer.setId(R.id.md__menu);
    this.mMenuContainer.setBackgroundDrawable(localDrawable2);
    this.mContentContainer = new NoClickThroughFrameLayout(paramContext);
    this.mContentContainer.setId(R.id.md__content);
    this.mContentContainer.setBackgroundDrawable(localDrawable1);
    this.mMenuOverlay = new ColorDrawable(-16777216);
    this.mIndicatorScroller = new FloatScroller(SMOOTH_INTERPOLATOR);
  }
  
  public boolean isDrawerIndicatorEnabled()
  {
    return this.mDrawerIndicatorEnabled;
  }
  
  public abstract boolean isMenuVisible();
  
  protected boolean isViewDescendant(View paramView)
  {
    ViewParent localViewParent = paramView.getParent();
    while (localViewParent != null) {
      if (localViewParent != this) {
        localViewParent = localViewParent.getParent();
      } else {
        return true;
      }
    }
    boolean bool = false;
    return bool;
  }
  
  protected void logDrawerState(int paramInt)
  {
    switch (paramInt)
    {
    case 3: 
    case 5: 
    case 6: 
    case 7: 
    default: 
      Log.d("MenuDrawer", "[DrawerState] Unknown: " + paramInt);
      break;
    case 0: 
      Log.d("MenuDrawer", "[DrawerState] STATE_CLOSED");
      break;
    case 1: 
      Log.d("MenuDrawer", "[DrawerState] STATE_CLOSING");
      break;
    case 2: 
      Log.d("MenuDrawer", "[DrawerState] STATE_DRAGGING");
      break;
    case 4: 
      Log.d("MenuDrawer", "[DrawerState] STATE_OPENING");
      break;
    case 8: 
      Log.d("MenuDrawer", "[DrawerState] STATE_OPEN");
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    getViewTreeObserver().addOnScrollChangedListener(this.mScrollListener);
  }
  
  protected void onDetachedFromWindow()
  {
    getViewTreeObserver().removeOnScrollChangedListener(this.mScrollListener);
    super.onDetachedFromWindow();
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    View localView = findViewById(R.id.mdMenu);
    if (localView != null)
    {
      removeView(localView);
      setMenuView(localView);
    }
    localView = findViewById(R.id.mdContent);
    if (localView != null)
    {
      removeView(localView);
      setContentView(localView);
    }
    if (getChildCount() <= 2) {
      return;
    }
    throw new IllegalStateException("Menu and content view added in xml must have id's @id/mdMenu and @id/mdContent");
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    restoreState(localSavedState.mState);
  }
  
  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if (this.mState == null) {
      this.mState = new Bundle();
    }
    saveState(this.mState);
    localSavedState.mState = this.mState;
    return localSavedState;
  }
  
  public void openMenu()
  {
    openMenu(true);
  }
  
  public abstract void openMenu(boolean paramBoolean);
  
  public abstract void peekDrawer();
  
  public abstract void peekDrawer(long paramLong);
  
  public abstract void peekDrawer(long paramLong1, long paramLong2);
  
  public void postOnAnimation(Runnable paramRunnable)
  {
    if (Build.VERSION.SDK_INT < 16) {
      postDelayed(paramRunnable, 16L);
    } else {
      super.postOnAnimation(paramRunnable);
    }
  }
  
  public void restoreState(Parcelable paramParcelable)
  {
    this.mState = ((Bundle)paramParcelable);
  }
  
  public final Parcelable saveState()
  {
    if (this.mState == null) {
      this.mState = new Bundle();
    }
    saveState(this.mState);
    return this.mState;
  }
  
  void saveState(Bundle paramBundle) {}
  
  public void setActiveView(View paramView)
  {
    setActiveView(paramView, 0);
  }
  
  public void setActiveView(View paramView, int paramInt)
  {
    View localView = this.mActiveView;
    this.mActiveView = paramView;
    this.mActivePosition = paramInt;
    if ((this.mAllowIndicatorAnimation) && (localView != null)) {
      startAnimatingIndicator();
    }
    invalidate();
  }
  
  public void setAllowIndicatorAnimation(boolean paramBoolean)
  {
    if (paramBoolean != this.mAllowIndicatorAnimation)
    {
      this.mAllowIndicatorAnimation = paramBoolean;
      completeAnimatingIndicator();
    }
  }
  
  public void setContentView(int paramInt)
  {
    switch (this.mDragMode)
    {
    case 0: 
      this.mContentContainer.removeAllViews();
      LayoutInflater.from(getContext()).inflate(paramInt, this.mContentContainer, true);
      break;
    case 1: 
      this.mActivity.setContentView(paramInt);
    }
  }
  
  public void setContentView(View paramView)
  {
    setContentView(paramView, new ViewGroup.LayoutParams(-1, -1));
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    switch (this.mDragMode)
    {
    case 0: 
      this.mContentContainer.removeAllViews();
      this.mContentContainer.addView(paramView, paramLayoutParams);
      break;
    case 1: 
      this.mActivity.setContentView(paramView, paramLayoutParams);
    }
  }
  
  public void setDrawOverlay(boolean paramBoolean)
  {
    this.mDrawOverlay = paramBoolean;
  }
  
  public void setDrawerIndicatorEnabled(boolean paramBoolean)
  {
    if (this.mActionBarHelper != null)
    {
      this.mDrawerIndicatorEnabled = paramBoolean;
      if (!paramBoolean)
      {
        this.mActionBarHelper.setActionBarUpIndicator(this.mThemeUpIndicator, 0);
      }
      else
      {
        ActionBarHelper localActionBarHelper = this.mActionBarHelper;
        SlideDrawable localSlideDrawable = this.mSlideDrawable;
        int i;
        if (!isMenuVisible()) {
          i = this.mDrawerClosedContentDesc;
        } else {
          i = this.mDrawerOpenContentDesc;
        }
        localActionBarHelper.setActionBarUpIndicator(localSlideDrawable, i);
      }
      return;
    }
    throw new IllegalStateException("setupUpIndicator(Activity) has not been called");
  }
  
  protected void setDrawerState(int paramInt)
  {
    if (paramInt != this.mDrawerState)
    {
      int i = this.mDrawerState;
      this.mDrawerState = paramInt;
      if (this.mOnDrawerStateChangeListener != null) {
        this.mOnDrawerStateChangeListener.onDrawerStateChange(i, paramInt);
      }
    }
  }
  
  public void setDropShadow(int paramInt)
  {
    setDropShadow(getResources().getDrawable(paramInt));
  }
  
  public void setDropShadow(Drawable paramDrawable)
  {
    this.mDropShadowDrawable = paramDrawable;
    invalidate();
  }
  
  public void setDropShadowColor(int paramInt)
  {
    GradientDrawable.Orientation localOrientation = getDropShadowOrientation();
    int i = paramInt & 0xFFFFFF;
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = paramInt;
    arrayOfInt[1] = i;
    this.mDropShadowDrawable = new GradientDrawable(localOrientation, arrayOfInt);
    invalidate();
  }
  
  public void setDropShadowEnabled(boolean paramBoolean)
  {
    this.mDropShadowEnabled = paramBoolean;
    invalidate();
  }
  
  public void setDropShadowSize(int paramInt)
  {
    this.mDropShadowSize = paramInt;
    invalidate();
  }
  
  public abstract void setHardwareLayerEnabled(boolean paramBoolean);
  
  public void setMaxAnimationDuration(int paramInt)
  {
    this.mMaxAnimationDuration = paramInt;
  }
  
  public abstract void setMenuSize(int paramInt);
  
  public void setMenuView(int paramInt)
  {
    this.mMenuContainer.removeAllViews();
    this.mMenuView = LayoutInflater.from(getContext()).inflate(paramInt, this.mMenuContainer, false);
    this.mMenuContainer.addView(this.mMenuView);
  }
  
  public void setMenuView(View paramView)
  {
    setMenuView(paramView, new ViewGroup.LayoutParams(-1, -1));
  }
  
  public void setMenuView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    this.mMenuView = paramView;
    this.mMenuContainer.removeAllViews();
    this.mMenuContainer.addView(paramView, paramLayoutParams);
  }
  
  public abstract void setOffsetMenuEnabled(boolean paramBoolean);
  
  public void setOnDrawerStateChangeListener(OnDrawerStateChangeListener paramOnDrawerStateChangeListener)
  {
    this.mOnDrawerStateChangeListener = paramOnDrawerStateChangeListener;
  }
  
  public void setOnInterceptMoveEventListener(OnInterceptMoveEventListener paramOnInterceptMoveEventListener)
  {
    this.mOnInterceptMoveEventListener = paramOnInterceptMoveEventListener;
  }
  
  public void setSlideDrawable(int paramInt)
  {
    setSlideDrawable(getResources().getDrawable(paramInt));
  }
  
  public void setSlideDrawable(Drawable paramDrawable)
  {
    this.mSlideDrawable = new SlideDrawable(paramDrawable);
    if (this.mActionBarHelper != null)
    {
      this.mActionBarHelper.setDisplayShowHomeAsUpEnabled(true);
      if (this.mDrawerIndicatorEnabled)
      {
        ActionBarHelper localActionBarHelper = this.mActionBarHelper;
        SlideDrawable localSlideDrawable = this.mSlideDrawable;
        int i;
        if (!isMenuVisible()) {
          i = this.mDrawerClosedContentDesc;
        } else {
          i = this.mDrawerOpenContentDesc;
        }
        localActionBarHelper.setActionBarUpIndicator(localSlideDrawable, i);
      }
    }
  }
  
  public abstract void setTouchBezelSize(int paramInt);
  
  public abstract void setTouchMode(int paramInt);
  
  public void setupUpIndicator(Activity paramActivity)
  {
    if (this.mActionBarHelper == null)
    {
      this.mActionBarHelper = new ActionBarHelper(paramActivity);
      this.mThemeUpIndicator = this.mActionBarHelper.getThemeUpIndicator();
      if (this.mDrawerIndicatorEnabled)
      {
        ActionBarHelper localActionBarHelper = this.mActionBarHelper;
        SlideDrawable localSlideDrawable = this.mSlideDrawable;
        int i;
        if (!isMenuVisible()) {
          i = this.mDrawerClosedContentDesc;
        } else {
          i = this.mDrawerOpenContentDesc;
        }
        localActionBarHelper.setActionBarUpIndicator(localSlideDrawable, i);
      }
    }
  }
  
  public void toggleMenu()
  {
    toggleMenu(true);
  }
  
  public abstract void toggleMenu(boolean paramBoolean);
  
  protected void updateDropShadowRect()
  {
    switch (this.mPosition)
    {
    case BOTTOM: 
      this.mDropShadowRect.top = 0;
      this.mDropShadowRect.bottom = getHeight();
      this.mDropShadowRect.right = ViewHelper.getLeft(this.mContentContainer);
      this.mDropShadowRect.left = (this.mDropShadowRect.right - this.mDropShadowSize);
      break;
    case LEFT: 
      this.mDropShadowRect.left = 0;
      this.mDropShadowRect.right = getWidth();
      this.mDropShadowRect.bottom = ViewHelper.getTop(this.mContentContainer);
      this.mDropShadowRect.top = (this.mDropShadowRect.bottom - this.mDropShadowSize);
      break;
    case RIGHT: 
      this.mDropShadowRect.top = 0;
      this.mDropShadowRect.bottom = getHeight();
      this.mDropShadowRect.left = ViewHelper.getRight(this.mContentContainer);
      this.mDropShadowRect.right = (this.mDropShadowRect.left + this.mDropShadowSize);
      break;
    case TOP: 
      this.mDropShadowRect.left = 0;
      this.mDropShadowRect.right = getWidth();
      this.mDropShadowRect.top = ViewHelper.getBottom(this.mContentContainer);
      this.mDropShadowRect.bottom = (this.mDropShadowRect.top + this.mDropShadowSize);
    }
  }
  
  protected void updateIndicatorClipRect()
  {
    this.mActiveView.getDrawingRect(this.mActiveRect);
    offsetDescendantRectToMyCoords(this.mActiveView, this.mActiveRect);
    float f1;
    if (!this.mIsStatic) {
      f1 = Math.abs(this.mOffsetPixels) / this.mMenuSize;
    } else {
      f1 = 1.0F;
    }
    float f2 = 1.0F - INDICATOR_INTERPOLATOR.getInterpolation(1.0F - f1);
    int j = this.mActiveIndicator.getWidth();
    int k = this.mActiveIndicator.getHeight();
    int i = (int)(f2 * j);
    int n = (int)(f2 * k);
    int m = this.mIndicatorStartPos;
    int i3 = 0;
    int i4 = 0;
    int i2 = 0;
    int i1 = 0;
    switch (this.mPosition)
    {
    case BOTTOM: 
    case RIGHT: 
      j = this.mActiveRect.top + (this.mActiveRect.height() - k) / 2;
      if (!this.mIndicatorAnimating) {
        i4 = j;
      } else {
        i4 = (int)(m + (j - m) * this.mIndicatorOffset);
      }
      i1 = i4 + k;
      break;
    case LEFT: 
    case TOP: 
      k = this.mActiveRect.left + (this.mActiveRect.width() - j) / 2;
      if (!this.mIndicatorAnimating) {
        i3 = k;
      } else {
        i3 = (int)(m + (k - m) * this.mIndicatorOffset);
      }
      i2 = i3 + j;
    }
    switch (this.mPosition)
    {
    case BOTTOM: 
      i2 = ViewHelper.getLeft(this.mContentContainer);
      i3 = i2 - i;
      break;
    case LEFT: 
      i1 = ViewHelper.getTop(this.mContentContainer);
      i4 = i1 - n;
      break;
    case RIGHT: 
      i3 = ViewHelper.getRight(this.mContentContainer);
      i2 = i3 + i;
      break;
    case TOP: 
      i4 = ViewHelper.getBottom(this.mContentContainer);
      i1 = i4 + n;
    }
    this.mIndicatorClipRect.left = i3;
    this.mIndicatorClipRect.top = i4;
    this.mIndicatorClipRect.right = i2;
    this.mIndicatorClipRect.bottom = i1;
  }
  
  protected void updateTouchAreaSize()
  {
    if (this.mTouchMode != 1)
    {
      if (this.mTouchMode != 2) {
        this.mTouchSize = 0;
      } else {
        this.mTouchSize = getMeasuredWidth();
      }
    }
    else {
      this.mTouchSize = this.mTouchBezelSize;
    }
  }
  
  protected void updateUpContentDescription()
  {
    int i;
    if (!isMenuVisible()) {
      i = this.mDrawerClosedContentDesc;
    } else {
      i = this.mDrawerOpenContentDesc;
    }
    if ((this.mDrawerIndicatorEnabled) && (this.mActionBarHelper != null) && (i != this.mCurrentUpContentDesc))
    {
      this.mCurrentUpContentDesc = i;
      this.mActionBarHelper.setActionBarDescription(i);
    }
  }
  
  public static abstract interface OnDrawerStateChangeListener
  {
    public abstract void onDrawerSlide(float paramFloat, int paramInt);
    
    public abstract void onDrawerStateChange(int paramInt1, int paramInt2);
  }
  
  public static abstract interface OnInterceptMoveEventListener
  {
    public abstract boolean isViewDraggable(View paramView, int paramInt1, int paramInt2, int paramInt3);
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public MenuDrawer.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MenuDrawer.SavedState(paramAnonymousParcel);
      }
      
      public MenuDrawer.SavedState[] newArray(int paramAnonymousInt)
      {
        return new MenuDrawer.SavedState[paramAnonymousInt];
      }
    };
    Bundle mState;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      this.mState = paramParcel.readBundle();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeBundle(this.mState);
    }
  }
  
  public static enum Type
  {
    OVERLAY,  STATIC,  BEHIND;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.MenuDrawer
 * JD-Core Version:    0.7.0.1
 */