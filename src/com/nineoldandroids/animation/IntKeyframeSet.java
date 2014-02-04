package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;

class IntKeyframeSet
  extends KeyframeSet
{
  private int deltaValue;
  private boolean firstTime = true;
  private int firstValue;
  private int lastValue;
  
  public IntKeyframeSet(Keyframe.IntKeyframe... paramVarArgs)
  {
    super(paramVarArgs);
  }
  
  public IntKeyframeSet clone()
  {
    ArrayList localArrayList = this.mKeyframes;
    int j = this.mKeyframes.size();
    Keyframe.IntKeyframe[] arrayOfIntKeyframe = new Keyframe.IntKeyframe[j];
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return new IntKeyframeSet(arrayOfIntKeyframe);
      }
      arrayOfIntKeyframe[i] = ((Keyframe.IntKeyframe)((Keyframe)localArrayList.get(i)).clone());
    }
  }
  
  public int getIntValue(float paramFloat)
  {
    int j;
    if (this.mNumKeyframes != 2)
    {
      int m;
      float f4;
      if (paramFloat > 0.0F)
      {
        if (paramFloat < 1.0F)
        {
          Object localObject1 = (Keyframe.IntKeyframe)this.mKeyframes.get(0);
          Keyframe.IntKeyframe localIntKeyframe1;
          for (int k = 1;; k++)
          {
            if (k >= this.mNumKeyframes)
            {
              int i = ((Number)((Keyframe)this.mKeyframes.get(-1 + this.mNumKeyframes)).getValue()).intValue();
              return ???;
            }
            localIntKeyframe1 = (Keyframe.IntKeyframe)this.mKeyframes.get(k);
            if (paramFloat < localIntKeyframe1.getFraction()) {
              break;
            }
            localObject1 = localIntKeyframe1;
          }
          Interpolator localInterpolator = localIntKeyframe1.getInterpolator();
          if (localInterpolator != null) {
            paramFloat = localInterpolator.getInterpolation(paramFloat);
          }
          float f1 = (paramFloat - ((Keyframe.IntKeyframe)localObject1).getFraction()) / (localIntKeyframe1.getFraction() - ((Keyframe.IntKeyframe)localObject1).getFraction());
          int n = ((Keyframe.IntKeyframe)localObject1).getIntValue();
          j = localIntKeyframe1.getIntValue();
          if (this.mEvaluator != null) {
            j = ((Number)this.mEvaluator.evaluate(f1, Integer.valueOf(n), Integer.valueOf(j))).intValue();
          } else {
            j = n + (int)(f1 * (j - n));
          }
        }
        else
        {
          Keyframe.IntKeyframe localIntKeyframe3 = (Keyframe.IntKeyframe)this.mKeyframes.get(-2 + this.mNumKeyframes);
          Object localObject2 = (Keyframe.IntKeyframe)this.mKeyframes.get(-1 + this.mNumKeyframes);
          j = localIntKeyframe3.getIntValue();
          m = ((Keyframe.IntKeyframe)localObject2).getIntValue();
          float f5 = localIntKeyframe3.getFraction();
          f4 = ((Keyframe.IntKeyframe)localObject2).getFraction();
          localObject2 = ((Keyframe.IntKeyframe)localObject2).getInterpolator();
          if (localObject2 != null) {
            paramFloat = ((Interpolator)localObject2).getInterpolation(paramFloat);
          }
          float f2 = (paramFloat - f5) / (f4 - f5);
          if (this.mEvaluator != null) {
            j = ((Number)this.mEvaluator.evaluate(f2, Integer.valueOf(j), Integer.valueOf(m))).intValue();
          } else {
            j += (int)(f2 * (m - j));
          }
        }
      }
      else
      {
        Keyframe.IntKeyframe localIntKeyframe2 = (Keyframe.IntKeyframe)this.mKeyframes.get(0);
        Object localObject3 = (Keyframe.IntKeyframe)this.mKeyframes.get(1);
        m = localIntKeyframe2.getIntValue();
        j = ((Keyframe.IntKeyframe)localObject3).getIntValue();
        f4 = localIntKeyframe2.getFraction();
        float f3 = ((Keyframe.IntKeyframe)localObject3).getFraction();
        localObject3 = ((Keyframe.IntKeyframe)localObject3).getInterpolator();
        if (localObject3 != null) {
          paramFloat = ((Interpolator)localObject3).getInterpolation(paramFloat);
        }
        f3 = (paramFloat - f4) / (f3 - f4);
        if (this.mEvaluator != null) {
          j = ((Number)this.mEvaluator.evaluate(f3, Integer.valueOf(m), Integer.valueOf(j))).intValue();
        } else {
          j = m + (int)(f3 * (j - m));
        }
      }
    }
    else
    {
      if (this.firstTime)
      {
        this.firstTime = false;
        this.firstValue = ((Keyframe.IntKeyframe)this.mKeyframes.get(0)).getIntValue();
        this.lastValue = ((Keyframe.IntKeyframe)this.mKeyframes.get(1)).getIntValue();
        this.deltaValue = (this.lastValue - this.firstValue);
      }
      if (this.mInterpolator != null) {
        paramFloat = this.mInterpolator.getInterpolation(paramFloat);
      }
      if (this.mEvaluator != null) {
        j = ((Number)this.mEvaluator.evaluate(paramFloat, Integer.valueOf(this.firstValue), Integer.valueOf(this.lastValue))).intValue();
      } else {
        j = this.firstValue + (int)(paramFloat * this.deltaValue);
      }
    }
    return j;
  }
  
  public Object getValue(float paramFloat)
  {
    return Integer.valueOf(getIntValue(paramFloat));
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.IntKeyframeSet
 * JD-Core Version:    0.7.0.1
 */