package com.github.espiandev.showcaseview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.DynamicLayout;
import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.github.espiandev.showcaseview.anim.AnimationUtils;
import com.github.espiandev.showcaseview.anim.AnimationUtils.AnimationEndListener;
import com.github.espiandev.showcaseview.anim.AnimationUtils.AnimationStartListener;
import java.lang.reflect.Field;

public class ShowcaseView
  extends RelativeLayout
  implements View.OnClickListener, View.OnTouchListener
{
  public static final int INNER_CIRCLE_RADIUS = 35;
  public static final int INSERT_TO_DECOR = 0;
  public static final int INSERT_TO_VIEW = 1;
  public static final int ITEM_ACTION_HOME = 0;
  public static final int ITEM_ACTION_ITEM = 3;
  public static final int ITEM_ACTION_OVERFLOW = 6;
  public static final int ITEM_SPINNER = 2;
  public static final int ITEM_TITLE = 1;
  private static final String PREFS_SHOWCASE_INTERNAL = "showcase_internal";
  public static final int TYPE_NO_LIMIT = 0;
  public static final int TYPE_ONE_SHOT = 1;
  private int backColor;
  private final String buttonText;
  private boolean hasCustomClickListener = false;
  private boolean isRedundant = false;
  private float legacyShowcaseX = -1.0F;
  private float legacyShowcaseY = -1.0F;
  private boolean mAlteredText = false;
  private float[] mBestTextPosition;
  private Bitmap mBleachedCling;
  private TextAppearanceSpan mDetailSpan;
  private DynamicLayout mDynamicDetailLayout;
  private DynamicLayout mDynamicTitleLayout;
  private final Button mEndButton;
  private Paint mEraser;
  private OnShowcaseEventListener mEventListener;
  private View mHandy;
  private ConfigOptions mOptions;
  private TextPaint mPaintDetail;
  private TextPaint mPaintTitle;
  private int mShowcaseColor;
  private CharSequence mSubText;
  private TextAppearanceSpan mTitleSpan;
  private CharSequence mTitleText;
  private float metricScale = 1.0F;
  private float scaleMultiplier = 1.0F;
  private Drawable showcase;
  private float showcaseRadius = -1.0F;
  private float showcaseX = -1.0F;
  private float showcaseY = -1.0F;
  private Rect voidedArea;
  
  protected ShowcaseView(Context paramContext)
  {
    this(paramContext, null, 0);
  }
  
  protected ShowcaseView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Object localObject = paramContext.getTheme().obtainStyledAttributes(paramAttributeSet, R.styleable.ShowcaseView, R.attr.showcaseViewStyle, R.style.ShowcaseView);
    this.backColor = ((TypedArray)localObject).getInt(0, Color.argb(128, 80, 80, 80));
    this.mShowcaseColor = ((TypedArray)localObject).getColor(8, Color.parseColor("#33B5E5"));
    int j = ((TypedArray)localObject).getResourceId(7, R.style.TextAppearance_ShowcaseView_Title);
    int i = ((TypedArray)localObject).getResourceId(6, R.style.TextAppearance_ShowcaseView_Detail);
    this.mTitleSpan = new TextAppearanceSpan(paramContext, j);
    this.mDetailSpan = new TextAppearanceSpan(paramContext, i);
    this.buttonText = ((TypedArray)localObject).getString(5);
    ((TypedArray)localObject).recycle();
    this.metricScale = getContext().getResources().getDisplayMetrics().density;
    this.mEndButton = ((Button)LayoutInflater.from(paramContext).inflate(R.layout.showcase_button, null));
    localObject = new ConfigOptions();
    ((ConfigOptions)localObject).showcaseId = getId();
    setConfigOptions((ConfigOptions)localObject);
  }
  
  private void fadeInShowcase()
  {
    AnimationUtils.createFadeInAnimation(this, getConfigOptions().fadeInDuration, new AnimationUtils.AnimationStartListener()
    {
      public void onAnimationStart()
      {
        ShowcaseView.this.setVisibility(0);
      }
    }).start();
  }
  
  private void fadeOutShowcase()
  {
    AnimationUtils.createFadeOutAnimation(this, getConfigOptions().fadeOutDuration, new AnimationUtils.AnimationEndListener()
    {
      public void onAnimationEnd()
      {
        ShowcaseView.this.setVisibility(8);
      }
    }).start();
  }
  
  private float[] getBestTextPosition(int paramInt1, int paramInt2)
  {
    float f1 = this.voidedArea.top;
    float f2 = paramInt2 - this.voidedArea.bottom - 64.0F * this.metricScale;
    float[] arrayOfFloat = new float[3];
    arrayOfFloat[0] = (24.0F * this.metricScale);
    if (f1 <= f2) {
      f1 = 24.0F * this.metricScale + this.voidedArea.bottom;
    } else {
      f1 = 128.0F * this.metricScale;
    }
    arrayOfFloat[1] = f1;
    arrayOfFloat[2] = (paramInt1 - 48.0F * this.metricScale);
    return arrayOfFloat;
  }
  
  private ConfigOptions getConfigOptions()
  {
    ConfigOptions localConfigOptions;
    if (this.mOptions != null)
    {
      localConfigOptions = this.mOptions;
    }
    else
    {
      localConfigOptions = new ConfigOptions();
      this.mOptions = localConfigOptions;
    }
    return localConfigOptions;
  }
  
  private void init()
  {
    if (Build.VERSION.SDK_INT < 11) {
      setDrawingCacheEnabled(true);
    } else {
      setLayerType(1, null);
    }
    if ((!getContext().getSharedPreferences("showcase_internal", 0).getBoolean("hasShot" + getConfigOptions().showcaseId, false)) || (this.mOptions.shotType != 1))
    {
      this.showcase = getContext().getResources().getDrawable(R.drawable.cling_bleached);
      this.showcase.setColorFilter(this.mShowcaseColor, PorterDuff.Mode.MULTIPLY);
      this.showcaseRadius = (35.0F * this.metricScale);
      Object localObject = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
      setOnTouchListener(this);
      this.mPaintTitle = new TextPaint();
      this.mPaintTitle.setAntiAlias(true);
      this.mPaintDetail = new TextPaint();
      this.mPaintDetail.setAntiAlias(true);
      this.mEraser = new Paint();
      this.mEraser.setColor(16777215);
      this.mEraser.setAlpha(0);
      this.mEraser.setXfermode((Xfermode)localObject);
      this.mEraser.setAntiAlias(true);
      if ((!this.mOptions.noButton) && (this.mEndButton.getParent() == null))
      {
        localObject = getConfigOptions().buttonLayoutParams;
        if (localObject == null)
        {
          localObject = (RelativeLayout.LayoutParams)generateDefaultLayoutParams();
          ((RelativeLayout.LayoutParams)localObject).addRule(12);
          ((RelativeLayout.LayoutParams)localObject).addRule(11);
          int i = Float.valueOf(12.0F * this.metricScale).intValue();
          ((RelativeLayout.LayoutParams)localObject).setMargins(i, i, i, i);
        }
        this.mEndButton.setLayoutParams((ViewGroup.LayoutParams)localObject);
        Button localButton = this.mEndButton;
        if (this.buttonText == null) {
          localObject = getResources().getString(R.string.ok);
        } else {
          localObject = this.buttonText;
        }
        localButton.setText((CharSequence)localObject);
        if (!this.hasCustomClickListener) {
          this.mEndButton.setOnClickListener(this);
        }
        addView(this.mEndButton);
      }
    }
    else
    {
      setVisibility(8);
      this.isRedundant = true;
    }
  }
  
  public static ShowcaseView insertShowcaseView(float paramFloat1, float paramFloat2, Activity paramActivity)
  {
    return insertShowcaseView(paramFloat1, paramFloat2, paramActivity, null, null, null);
  }
  
  public static ShowcaseView insertShowcaseView(float paramFloat1, float paramFloat2, Activity paramActivity, int paramInt1, int paramInt2, ConfigOptions paramConfigOptions)
  {
    ShowcaseView localShowcaseView = new ShowcaseView(paramActivity);
    if (paramConfigOptions != null) {
      localShowcaseView.setConfigOptions(paramConfigOptions);
    }
    if (localShowcaseView.getConfigOptions().insert != 0) {
      ((ViewGroup)paramActivity.findViewById(16908290)).addView(localShowcaseView);
    } else {
      ((ViewGroup)paramActivity.getWindow().getDecorView()).addView(localShowcaseView);
    }
    localShowcaseView.setShowcasePosition(paramFloat1, paramFloat2);
    localShowcaseView.setText(paramInt1, paramInt2);
    return localShowcaseView;
  }
  
  public static ShowcaseView insertShowcaseView(float paramFloat1, float paramFloat2, Activity paramActivity, String paramString1, String paramString2, ConfigOptions paramConfigOptions)
  {
    ShowcaseView localShowcaseView = new ShowcaseView(paramActivity);
    if (paramConfigOptions != null) {
      localShowcaseView.setConfigOptions(paramConfigOptions);
    }
    if (localShowcaseView.getConfigOptions().insert != 0) {
      ((ViewGroup)paramActivity.findViewById(16908290)).addView(localShowcaseView);
    } else {
      ((ViewGroup)paramActivity.getWindow().getDecorView()).addView(localShowcaseView);
    }
    localShowcaseView.setShowcasePosition(paramFloat1, paramFloat2);
    localShowcaseView.setText(paramString1, paramString2);
    return localShowcaseView;
  }
  
  public static ShowcaseView insertShowcaseView(int paramInt1, Activity paramActivity, int paramInt2, int paramInt3, ConfigOptions paramConfigOptions)
  {
    Object localObject = paramActivity.findViewById(paramInt1);
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = insertShowcaseView((View)localObject, paramActivity, paramInt2, paramInt3, paramConfigOptions);
    }
    return localObject;
  }
  
  public static ShowcaseView insertShowcaseView(int paramInt, Activity paramActivity, String paramString1, String paramString2, ConfigOptions paramConfigOptions)
  {
    Object localObject = paramActivity.findViewById(paramInt);
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = insertShowcaseView((View)localObject, paramActivity, paramString1, paramString2, paramConfigOptions);
    }
    return localObject;
  }
  
  public static ShowcaseView insertShowcaseView(View paramView, Activity paramActivity)
  {
    return insertShowcaseView(paramView, paramActivity, null, null, null);
  }
  
  public static ShowcaseView insertShowcaseView(View paramView, Activity paramActivity, int paramInt1, int paramInt2, ConfigOptions paramConfigOptions)
  {
    ShowcaseView localShowcaseView = new ShowcaseView(paramActivity);
    if (paramConfigOptions != null) {
      localShowcaseView.setConfigOptions(paramConfigOptions);
    }
    if (localShowcaseView.getConfigOptions().insert != 0) {
      ((ViewGroup)paramActivity.findViewById(16908290)).addView(localShowcaseView);
    } else {
      ((ViewGroup)paramActivity.getWindow().getDecorView()).addView(localShowcaseView);
    }
    localShowcaseView.setShowcaseView(paramView);
    localShowcaseView.setText(paramInt1, paramInt2);
    return localShowcaseView;
  }
  
  public static ShowcaseView insertShowcaseView(View paramView, Activity paramActivity, String paramString1, String paramString2, ConfigOptions paramConfigOptions)
  {
    ShowcaseView localShowcaseView = new ShowcaseView(paramActivity);
    if (paramConfigOptions != null) {
      localShowcaseView.setConfigOptions(paramConfigOptions);
    }
    if (localShowcaseView.getConfigOptions().insert != 0) {
      ((ViewGroup)paramActivity.findViewById(16908290)).addView(localShowcaseView);
    } else {
      ((ViewGroup)paramActivity.getWindow().getDecorView()).addView(localShowcaseView);
    }
    localShowcaseView.setShowcaseView(paramView);
    localShowcaseView.setText(paramString1, paramString2);
    return localShowcaseView;
  }
  
  public static ShowcaseView insertShowcaseViewWithType(int paramInt1, int paramInt2, Activity paramActivity, int paramInt3, int paramInt4, ConfigOptions paramConfigOptions)
  {
    ShowcaseView localShowcaseView = new ShowcaseView(paramActivity);
    if (paramConfigOptions != null) {
      localShowcaseView.setConfigOptions(paramConfigOptions);
    }
    if (localShowcaseView.getConfigOptions().insert != 0) {
      ((ViewGroup)paramActivity.findViewById(16908290)).addView(localShowcaseView);
    } else {
      ((ViewGroup)paramActivity.getWindow().getDecorView()).addView(localShowcaseView);
    }
    localShowcaseView.setShowcaseItem(paramInt1, paramInt2, paramActivity);
    localShowcaseView.setText(paramInt3, paramInt4);
    return localShowcaseView;
  }
  
  public static ShowcaseView insertShowcaseViewWithType(int paramInt1, int paramInt2, Activity paramActivity, String paramString1, String paramString2, ConfigOptions paramConfigOptions)
  {
    ShowcaseView localShowcaseView = new ShowcaseView(paramActivity);
    if (paramConfigOptions != null) {
      localShowcaseView.setConfigOptions(paramConfigOptions);
    }
    if (localShowcaseView.getConfigOptions().insert != 0) {
      ((ViewGroup)paramActivity.findViewById(16908290)).addView(localShowcaseView);
    } else {
      ((ViewGroup)paramActivity.getWindow().getDecorView()).addView(localShowcaseView);
    }
    localShowcaseView.setShowcaseItem(paramInt1, paramInt2, paramActivity);
    localShowcaseView.setText(paramString1, paramString2);
    return localShowcaseView;
  }
  
  private boolean makeVoidedRect()
  {
    int i;
    if ((this.voidedArea != null) && (this.showcaseX == this.legacyShowcaseX) && (this.showcaseY == this.legacyShowcaseY))
    {
      i = 0;
    }
    else
    {
      i = (int)this.showcaseX;
      int m = (int)this.showcaseY;
      int k = this.showcase.getIntrinsicWidth();
      int j = this.showcase.getIntrinsicHeight();
      this.voidedArea = new Rect(i - k / 2, m - j / 2, i + k / 2, m + j / 2);
      this.legacyShowcaseX = this.showcaseX;
      this.legacyShowcaseY = this.showcaseY;
      i = 1;
    }
    return i;
  }
  
  private void moveHand(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, AnimationUtils.AnimationEndListener paramAnimationEndListener)
  {
    AnimationUtils.createMovementAnimation(this.mHandy, this.showcaseX, this.showcaseY, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramAnimationEndListener).start();
  }
  
  private void setConfigOptions(ConfigOptions paramConfigOptions)
  {
    this.mOptions = paramConfigOptions;
  }
  
  private void showcaseActionItem(ViewParent paramViewParent, Class paramClass, int paramInt1, int paramInt2)
  {
    for (;;)
    {
      Field localField2;
      try
      {
        Object localObject1 = paramClass.getDeclaredField("mActionMenuPresenter");
        ((Field)localObject1).setAccessible(true);
        localObject1 = ((Field)localObject1).get(paramViewParent);
        Field localField1;
        if (paramInt1 == 6)
        {
          localField1 = localObject1.getClass().getDeclaredField("mOverflowButton");
          localField1.setAccessible(true);
          localObject1 = (View)localField1.get(localObject1);
          if (localObject1 != null) {
            setShowcaseView((View)localObject1);
          }
        }
        else
        {
          localField1 = localObject1.getClass().getSuperclass().getDeclaredField("mMenuView");
          localField1.setAccessible(true);
          localObject1 = localField1.get(localObject1);
          int i;
          if (localObject1.getClass().toString().contains("com.actionbarsherlock"))
          {
            localField1 = localObject1.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("mChildren");
            localField1.setAccessible(true);
            Object[] arrayOfObject = (Object[])localField1.get(localObject1);
            i = arrayOfObject.length;
            int j = 0;
            if (j < i)
            {
              Object localObject2 = arrayOfObject[j];
              if (localObject2 == null) {
                break label277;
              }
              localObject2 = (View)localObject2;
              if (((View)localObject2).getId() != paramInt2) {
                break label277;
              }
              setShowcaseView((View)localObject2);
              break label277;
            }
          }
          else
          {
            localField2 = i.getClass().getSuperclass().getSuperclass().getDeclaredField("mChildren");
            localField2 = localField2;
            continue;
          }
        }
        return;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        localIllegalAccessException.printStackTrace();
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        localNoSuchFieldException.printStackTrace();
      }
      catch (NullPointerException localNullPointerException)
      {
        throw new RuntimeException("insertShowcaseViewWithType() must be called after or during onCreateOptionsMenu() of the host Activity");
      }
      label277:
      localField2++;
    }
  }
  
  private void showcaseSpinner(ViewParent paramViewParent, Class paramClass)
  {
    try
    {
      Object localObject = paramClass.getDeclaredField("mSpinner");
      ((Field)localObject).setAccessible(true);
      localObject = (View)((Field)localObject).get(paramViewParent);
      if (localObject != null) {
        setShowcaseView((View)localObject);
      }
      return;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      for (;;)
      {
        Log.e("TAG", "Failed to find actionbar spinner", localNoSuchFieldException);
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;)
      {
        Log.e("TAG", "Failed to access actionbar spinner", localIllegalAccessException);
      }
    }
  }
  
  private void showcaseTitle(ViewParent paramViewParent, Class paramClass)
  {
    try
    {
      Object localObject = paramClass.getDeclaredField("mTitleView");
      ((Field)localObject).setAccessible(true);
      localObject = (View)((Field)localObject).get(paramViewParent);
      if (localObject != null) {
        setShowcaseView((View)localObject);
      }
      return;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      for (;;)
      {
        Log.e("TAG", "Failed to find actionbar title", localNoSuchFieldException);
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;)
      {
        Log.e("TAG", "Failed to access actionbar title", localIllegalAccessException);
      }
    }
  }
  
  public void animateGesture(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.mHandy = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(R.layout.handy, null);
    addView(this.mHandy);
    moveHand(paramFloat1, paramFloat2, paramFloat3, paramFloat4, new AnimationUtils.AnimationEndListener()
    {
      public void onAnimationEnd()
      {
        ShowcaseView.this.removeView(ShowcaseView.this.mHandy);
      }
    });
  }
  
  @Deprecated
  public void blockNonShowcasedTouches(boolean paramBoolean)
  {
    this.mOptions.block = paramBoolean;
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    if ((this.showcaseX >= 0.0F) && (this.showcaseY >= 0.0F) && (!this.isRedundant))
    {
      paramCanvas.drawColor(this.backColor);
      Matrix localMatrix = new Matrix();
      localMatrix.postScale(this.scaleMultiplier, this.scaleMultiplier, this.showcaseX, this.showcaseY);
      paramCanvas.setMatrix(localMatrix);
      paramCanvas.drawCircle(this.showcaseX, this.showcaseY, this.showcaseRadius, this.mEraser);
      int i;
      if ((makeVoidedRect()) || (this.mAlteredText)) {
        i = 1;
      } else {
        i = 0;
      }
      this.mAlteredText = false;
      this.showcase.setBounds(this.voidedArea);
      this.showcase.draw(paramCanvas);
      paramCanvas.setMatrix(new Matrix());
      if ((!TextUtils.isEmpty(this.mTitleText)) || (!TextUtils.isEmpty(this.mSubText)))
      {
        if (i != 0) {
          this.mBestTextPosition = getBestTextPosition(paramCanvas.getWidth(), paramCanvas.getHeight());
        }
        if (!TextUtils.isEmpty(this.mTitleText))
        {
          paramCanvas.save();
          if (i != 0) {
            this.mDynamicTitleLayout = new DynamicLayout(this.mTitleText, this.mPaintTitle, (int)this.mBestTextPosition[2], Layout.Alignment.ALIGN_NORMAL, 1.0F, 1.0F, true);
          }
          paramCanvas.translate(this.mBestTextPosition[0], this.mBestTextPosition[1] - 24.0F * this.metricScale);
          this.mDynamicTitleLayout.draw(paramCanvas);
          paramCanvas.restore();
        }
        if (!TextUtils.isEmpty(this.mSubText))
        {
          paramCanvas.save();
          if (i != 0) {
            this.mDynamicDetailLayout = new DynamicLayout(this.mSubText, this.mPaintDetail, Float.valueOf(this.mBestTextPosition[2]).intValue(), Layout.Alignment.ALIGN_NORMAL, 1.2F, 1.0F, true);
          }
          paramCanvas.translate(this.mBestTextPosition[0], this.mBestTextPosition[1] + 12.0F * this.metricScale);
          this.mDynamicDetailLayout.draw(paramCanvas);
          paramCanvas.restore();
        }
      }
      super.dispatchDraw(paramCanvas);
    }
    else
    {
      super.dispatchDraw(paramCanvas);
    }
  }
  
  public View getHand()
  {
    View localView = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(R.layout.handy, null);
    addView(localView);
    AnimationUtils.hide(localView);
    return localView;
  }
  
  public void hide()
  {
    if (this.mEventListener != null) {
      this.mEventListener.onShowcaseViewHide(this);
    }
    if ((Build.VERSION.SDK_INT < 11) || (getConfigOptions().fadeOutDuration <= 0)) {
      setVisibility(8);
    } else {
      fadeOutShowcase();
    }
  }
  
  public void onClick(View paramView)
  {
    if (this.mOptions.shotType == 1)
    {
      SharedPreferences localSharedPreferences = getContext().getSharedPreferences("showcase_internal", 0);
      if (Build.VERSION.SDK_INT < 9) {
        localSharedPreferences.edit().putBoolean("hasShot" + getConfigOptions().showcaseId, true).commit();
      } else {
        localSharedPreferences.edit().putBoolean("hasShot" + getConfigOptions().showcaseId, true).apply();
      }
    }
    hide();
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    boolean bool = true;
    float f2 = Math.abs(paramMotionEvent.getRawX() - this.showcaseX);
    float f1 = Math.abs(paramMotionEvent.getRawY() - this.showcaseY);
    double d = Math.sqrt(Math.pow(f2, 2.0D) + Math.pow(f1, 2.0D));
    if ((!this.mOptions.hideOnClickOutside) || (d <= this.showcaseRadius))
    {
      if ((!this.mOptions.block) || (d <= this.showcaseRadius)) {
        bool = false;
      }
    }
    else {
      hide();
    }
    return bool;
  }
  
  public void overrideButtonClick(View.OnClickListener paramOnClickListener)
  {
    if (!this.isRedundant)
    {
      if (this.mEndButton != null)
      {
        Button localButton = this.mEndButton;
        if (paramOnClickListener == null) {
          paramOnClickListener = this;
        }
        localButton.setOnClickListener(paramOnClickListener);
      }
      this.hasCustomClickListener = true;
    }
  }
  
  public void pointTo(float paramFloat1, float paramFloat2)
  {
    AnimationUtils.createMovementAnimation(((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(R.layout.handy, null), paramFloat1, paramFloat2).start();
  }
  
  public void pointTo(View paramView)
  {
    pointTo(AnimationUtils.getX(paramView) + paramView.getWidth() / 2, AnimationUtils.getY(paramView) + paramView.getHeight() / 2);
  }
  
  public void setOnShowcaseEventListener(OnShowcaseEventListener paramOnShowcaseEventListener)
  {
    this.mEventListener = paramOnShowcaseEventListener;
  }
  
  @Deprecated
  public void setShotType(int paramInt)
  {
    if ((paramInt == 0) || (paramInt == 1)) {
      this.mOptions.shotType = paramInt;
    }
  }
  
  public void setShowcaseIndicatorScale(float paramFloat)
  {
    this.scaleMultiplier = paramFloat;
  }
  
  public void setShowcaseItem(final int paramInt1, final int paramInt2, final Activity paramActivity)
  {
    post(new Runnable()
    {
      public void run()
      {
        View localView = paramActivity.findViewById(16908332);
        if (localView == null)
        {
          int i = paramActivity.getResources().getIdentifier("abs__home", "id", paramActivity.getPackageName());
          if (i != 0) {
            localView = paramActivity.findViewById(i);
          }
        }
        if (localView != null)
        {
          ViewParent localViewParent = localView.getParent().getParent();
          Object localObject1;
          Object localObject2;
          if (!localViewParent.getClass().getName().contains("ActionBarView"))
          {
            localObject1 = localViewParent.getClass().getName();
            localViewParent = localViewParent.getParent();
            localObject2 = localViewParent.getClass().getName();
            if (!localViewParent.getClass().getName().contains("ActionBarView")) {}
          }
          else
          {
            localObject1 = localViewParent.getClass();
            localObject2 = ((Class)localObject1).getSuperclass();
            switch (paramInt1)
            {
            case 4: 
            case 5: 
            default: 
              Log.e("TAG", "Unknown item type");
              break;
            case 0: 
              ShowcaseView.this.setShowcaseView(localView);
              break;
            case 1: 
              ShowcaseView.this.showcaseTitle(localViewParent, (Class)localObject1);
              break;
            case 2: 
              ShowcaseView.this.showcaseSpinner(localViewParent, (Class)localObject1);
              break;
            case 3: 
            case 6: 
              ShowcaseView.this.showcaseActionItem(localViewParent, (Class)localObject2, paramInt1, paramInt2);
            }
            return;
          }
          throw new IllegalStateException("Cannot find ActionBarView for Activity, instead found " + (String)localObject1 + " and " + (String)localObject2);
        }
        throw new RuntimeException("insertShowcaseViewWithType cannot be used when the theme has no ActionBar");
      }
    });
  }
  
  public void setShowcaseNoView()
  {
    setShowcasePosition(1000000.0F, 1000000.0F);
  }
  
  public void setShowcasePosition(float paramFloat1, float paramFloat2)
  {
    if (!this.isRedundant)
    {
      this.showcaseX = paramFloat1;
      this.showcaseY = paramFloat2;
      init();
      invalidate();
    }
  }
  
  public void setShowcaseView(final View paramView)
  {
    if ((!this.isRedundant) && (paramView != null))
    {
      this.isRedundant = false;
      paramView.post(new Runnable()
      {
        public void run()
        {
          ShowcaseView.this.init();
          if (ShowcaseView.this.mOptions.insert != 1)
          {
            int[] arrayOfInt = new int[2];
            paramView.getLocationInWindow(arrayOfInt);
            ShowcaseView.this.showcaseX = (arrayOfInt[0] + paramView.getWidth() / 2);
            ShowcaseView.this.showcaseY = (arrayOfInt[1] + paramView.getHeight() / 2);
          }
          else
          {
            ShowcaseView.this.showcaseX = (paramView.getLeft() + paramView.getWidth() / 2);
            ShowcaseView.this.showcaseY = (paramView.getTop() + paramView.getHeight() / 2);
          }
          ShowcaseView.this.invalidate();
        }
      });
    }
    else
    {
      this.isRedundant = true;
    }
  }
  
  public void setText(int paramInt1, int paramInt2)
  {
    setText(getContext().getResources().getString(paramInt1), getContext().getResources().getString(paramInt2));
  }
  
  public void setText(String paramString1, String paramString2)
  {
    SpannableString localSpannableString = new SpannableString(paramString1);
    localSpannableString.setSpan(this.mTitleSpan, 0, localSpannableString.length(), 0);
    this.mTitleText = localSpannableString;
    localSpannableString = new SpannableString(paramString2);
    localSpannableString.setSpan(this.mDetailSpan, 0, localSpannableString.length(), 0);
    this.mSubText = localSpannableString;
    this.mAlteredText = true;
    invalidate();
  }
  
  public void show()
  {
    if (this.mEventListener != null) {
      this.mEventListener.onShowcaseViewShow(this);
    }
    if ((Build.VERSION.SDK_INT < 11) || (getConfigOptions().fadeInDuration <= 0)) {
      setVisibility(0);
    } else {
      fadeInShowcase();
    }
  }
  
  public void showHand(int paramInt)
  {
    this.mHandy = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(R.layout.handy, null);
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
    localLayoutParams.addRule(11);
    localLayoutParams.topMargin = paramInt;
    addView(this.mHandy, localLayoutParams);
  }
  
  public static class ConfigOptions
  {
    public boolean block = true;
    public RelativeLayout.LayoutParams buttonLayoutParams = null;
    public int fadeInDuration = 300;
    public int fadeOutDuration = 300;
    public boolean hideOnClickOutside = false;
    public int insert = 0;
    public boolean noButton = false;
    public int shotType = 0;
    public int showcaseId = 0;
  }
  
  public static abstract interface OnShowcaseEventListener
  {
    public abstract void onShowcaseViewHide(ShowcaseView paramShowcaseView);
    
    public abstract void onShowcaseViewShow(ShowcaseView paramShowcaseView);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.github.espiandev.showcaseview.ShowcaseView
 * JD-Core Version:    0.7.0.1
 */