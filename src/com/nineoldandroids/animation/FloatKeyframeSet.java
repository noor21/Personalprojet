package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;

class FloatKeyframeSet
  extends KeyframeSet
{
  private float deltaValue;
  private boolean firstTime = true;
  private float firstValue;
  private float lastValue;
  
  public FloatKeyframeSet(Keyframe.FloatKeyframe... paramVarArgs)
  {
    super(paramVarArgs);
  }
  
  public FloatKeyframeSet clone()
  {
    ArrayList localArrayList = this.mKeyframes;
    int j = this.mKeyframes.size();
    Keyframe.FloatKeyframe[] arrayOfFloatKeyframe = new Keyframe.FloatKeyframe[j];
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return new FloatKeyframeSet(arrayOfFloatKeyframe);
      }
      arrayOfFloatKeyframe[i] = ((Keyframe.FloatKeyframe)((Keyframe)localArrayList.get(i)).clone());
    }
  }
  
  public float getFloatValue(float paramFloat)
  {
    float f2;
    if (this.mNumKeyframes != 2)
    {
      float f3;
      if (paramFloat > 0.0F)
      {
        if (paramFloat < 1.0F)
        {
          Object localObject1 = (Keyframe.FloatKeyframe)this.mKeyframes.get(0);
          Keyframe.FloatKeyframe localFloatKeyframe1;
          for (int i = 1;; i++)
          {
            if (i >= this.mNumKeyframes)
            {
              float f1 = ((Number)((Keyframe)this.mKeyframes.get(-1 + this.mNumKeyframes)).getValue()).floatValue();
              return ???;
            }
            localFloatKeyframe1 = (Keyframe.FloatKeyframe)this.mKeyframes.get(i);
            if (paramFloat < localFloatKeyframe1.getFraction()) {
              break;
            }
            localObject1 = localFloatKeyframe1;
          }
          Interpolator localInterpolator = localFloatKeyframe1.getInterpolator();
          if (localInterpolator != null) {
            paramFloat = localInterpolator.getInterpolation(paramFloat);
          }
          float f4 = (paramFloat - ((Keyframe.FloatKeyframe)localObject1).getFraction()) / (localFloatKeyframe1.getFraction() - ((Keyframe.FloatKeyframe)localObject1).getFraction());
          f3 = ((Keyframe.FloatKeyframe)localObject1).getFloatValue();
          f2 = localFloatKeyframe1.getFloatValue();
          if (this.mEvaluator != null) {
            f2 = ((Number)this.mEvaluator.evaluate(f4, Float.valueOf(f3), Float.valueOf(f2))).floatValue();
          } else {
            f2 = f3 + f4 * (f2 - f3);
          }
        }
        else
        {
          Keyframe.FloatKeyframe localFloatKeyframe2 = (Keyframe.FloatKeyframe)this.mKeyframes.get(-2 + this.mNumKeyframes);
          Object localObject2 = (Keyframe.FloatKeyframe)this.mKeyframes.get(-1 + this.mNumKeyframes);
          f2 = localFloatKeyframe2.getFloatValue();
          f3 = ((Keyframe.FloatKeyframe)localObject2).getFloatValue();
          float f8 = localFloatKeyframe2.getFraction();
          float f5 = ((Keyframe.FloatKeyframe)localObject2).getFraction();
          localObject2 = ((Keyframe.FloatKeyframe)localObject2).getInterpolator();
          if (localObject2 != null) {
            paramFloat = ((Interpolator)localObject2).getInterpolation(paramFloat);
          }
          f5 = (paramFloat - f8) / (f5 - f8);
          if (this.mEvaluator != null) {
            f2 = ((Number)this.mEvaluator.evaluate(f5, Float.valueOf(f2), Float.valueOf(f3))).floatValue();
          } else {
            f2 += f5 * (f3 - f2);
          }
        }
      }
      else
      {
        Keyframe.FloatKeyframe localFloatKeyframe3 = (Keyframe.FloatKeyframe)this.mKeyframes.get(0);
        Object localObject3 = (Keyframe.FloatKeyframe)this.mKeyframes.get(1);
        f3 = localFloatKeyframe3.getFloatValue();
        f2 = ((Keyframe.FloatKeyframe)localObject3).getFloatValue();
        float f6 = localFloatKeyframe3.getFraction();
        float f7 = ((Keyframe.FloatKeyframe)localObject3).getFraction();
        localObject3 = ((Keyframe.FloatKeyframe)localObject3).getInterpolator();
        if (localObject3 != null) {
          paramFloat = ((Interpolator)localObject3).getInterpolation(paramFloat);
        }
        f6 = (paramFloat - f6) / (f7 - f6);
        if (this.mEvaluator != null) {
          f2 = ((Number)this.mEvaluator.evaluate(f6, Float.valueOf(f3), Float.valueOf(f2))).floatValue();
        } else {
          f2 = f3 + f6 * (f2 - f3);
        }
      }
    }
    else
    {
      if (this.firstTime)
      {
        this.firstTime = false;
        this.firstValue = ((Keyframe.FloatKeyframe)this.mKeyframes.get(0)).getFloatValue();
        this.lastValue = ((Keyframe.FloatKeyframe)this.mKeyframes.get(1)).getFloatValue();
        this.deltaValue = (this.lastValue - this.firstValue);
      }
      if (this.mInterpolator != null) {
        paramFloat = this.mInterpolator.getInterpolation(paramFloat);
      }
      if (this.mEvaluator != null) {
        f2 = ((Number)this.mEvaluator.evaluate(paramFloat, Float.valueOf(this.firstValue), Float.valueOf(this.lastValue))).floatValue();
      } else {
        f2 = this.firstValue + paramFloat * this.deltaValue;
      }
    }
    return f2;
  }
  
  public Object getValue(float paramFloat)
  {
    return Float.valueOf(getFloatValue(paramFloat));
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.FloatKeyframeSet
 * JD-Core Version:    0.7.0.1
 */