package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Arrays;

class KeyframeSet
{
  TypeEvaluator mEvaluator;
  Keyframe mFirstKeyframe;
  Interpolator mInterpolator;
  ArrayList<Keyframe> mKeyframes;
  Keyframe mLastKeyframe;
  int mNumKeyframes;
  
  public KeyframeSet(Keyframe... paramVarArgs)
  {
    this.mNumKeyframes = paramVarArgs.length;
    this.mKeyframes = new ArrayList();
    this.mKeyframes.addAll(Arrays.asList(paramVarArgs));
    this.mFirstKeyframe = ((Keyframe)this.mKeyframes.get(0));
    this.mLastKeyframe = ((Keyframe)this.mKeyframes.get(-1 + this.mNumKeyframes));
    this.mInterpolator = this.mLastKeyframe.getInterpolator();
  }
  
  public static KeyframeSet ofFloat(float... paramVarArgs)
  {
    int i = paramVarArgs.length;
    Keyframe.FloatKeyframe[] arrayOfFloatKeyframe = new Keyframe.FloatKeyframe[Math.max(i, 2)];
    if (i != 1)
    {
      arrayOfFloatKeyframe[0] = ((Keyframe.FloatKeyframe)Keyframe.ofFloat(0.0F, paramVarArgs[0]));
      for (int j = 1; j < i; j++) {
        arrayOfFloatKeyframe[j] = ((Keyframe.FloatKeyframe)Keyframe.ofFloat(j / (i - 1), paramVarArgs[j]));
      }
    }
    arrayOfFloatKeyframe[0] = ((Keyframe.FloatKeyframe)Keyframe.ofFloat(0.0F));
    arrayOfFloatKeyframe[1] = ((Keyframe.FloatKeyframe)Keyframe.ofFloat(1.0F, paramVarArgs[0]));
    return new FloatKeyframeSet(arrayOfFloatKeyframe);
  }
  
  public static KeyframeSet ofInt(int... paramVarArgs)
  {
    int j = paramVarArgs.length;
    Keyframe.IntKeyframe[] arrayOfIntKeyframe = new Keyframe.IntKeyframe[Math.max(j, 2)];
    if (j != 1)
    {
      arrayOfIntKeyframe[0] = ((Keyframe.IntKeyframe)Keyframe.ofInt(0.0F, paramVarArgs[0]));
      for (int i = 1; i < j; i++) {
        arrayOfIntKeyframe[i] = ((Keyframe.IntKeyframe)Keyframe.ofInt(i / (j - 1), paramVarArgs[i]));
      }
    }
    arrayOfIntKeyframe[0] = ((Keyframe.IntKeyframe)Keyframe.ofInt(0.0F));
    arrayOfIntKeyframe[1] = ((Keyframe.IntKeyframe)Keyframe.ofInt(1.0F, paramVarArgs[0]));
    return new IntKeyframeSet(arrayOfIntKeyframe);
  }
  
  public static KeyframeSet ofKeyframe(Keyframe... paramVarArgs)
  {
    int i = paramVarArgs.length;
    Object localObject2 = 0;
    int m = 0;
    int n = 0;
    Object localObject4;
    for (int k = 0;; localObject4++)
    {
      if (k >= i)
      {
        Object localObject1;
        if ((localObject2 == 0) || (m != 0) || (n != 0))
        {
          if ((m == 0) || (localObject2 != 0) || (n != 0))
          {
            localObject1 = new KeyframeSet(paramVarArgs);
          }
          else
          {
            localObject4 = new Keyframe.IntKeyframe[localObject1];
            for (localObject2 = 0;; localObject2++)
            {
              if (localObject2 >= localObject1)
              {
                localObject1 = new IntKeyframeSet((Keyframe.IntKeyframe[])localObject4);
                break;
              }
              localObject4[localObject2] = ((Keyframe.IntKeyframe)paramVarArgs[localObject2]);
            }
          }
        }
        else {
          localObject4 = new Keyframe.FloatKeyframe[localObject1];
        }
        for (Object localObject3 = 0;; localObject3++)
        {
          if (localObject3 >= localObject1)
          {
            localObject1 = new FloatKeyframeSet((Keyframe.FloatKeyframe[])localObject4);
            return localObject1;
          }
          localObject4[localObject3] = ((Keyframe.FloatKeyframe)paramVarArgs[localObject3]);
        }
      }
      if (!(paramVarArgs[localObject4] instanceof Keyframe.FloatKeyframe))
      {
        if (!(paramVarArgs[localObject4] instanceof Keyframe.IntKeyframe)) {
          n = 1;
        } else {
          m = 1;
        }
      }
      else {
        int j = 1;
      }
    }
  }
  
  public static KeyframeSet ofObject(Object... paramVarArgs)
  {
    int j = paramVarArgs.length;
    Keyframe.ObjectKeyframe[] arrayOfObjectKeyframe = new Keyframe.ObjectKeyframe[Math.max(j, 2)];
    if (j != 1)
    {
      arrayOfObjectKeyframe[0] = ((Keyframe.ObjectKeyframe)Keyframe.ofObject(0.0F, paramVarArgs[0]));
      for (int i = 1; i < j; i++) {
        arrayOfObjectKeyframe[i] = ((Keyframe.ObjectKeyframe)Keyframe.ofObject(i / (j - 1), paramVarArgs[i]));
      }
    }
    arrayOfObjectKeyframe[0] = ((Keyframe.ObjectKeyframe)Keyframe.ofObject(0.0F));
    arrayOfObjectKeyframe[1] = ((Keyframe.ObjectKeyframe)Keyframe.ofObject(1.0F, paramVarArgs[0]));
    return new KeyframeSet(arrayOfObjectKeyframe);
  }
  
  public KeyframeSet clone()
  {
    ArrayList localArrayList = this.mKeyframes;
    int j = this.mKeyframes.size();
    Keyframe[] arrayOfKeyframe = new Keyframe[j];
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return new KeyframeSet(arrayOfKeyframe);
      }
      arrayOfKeyframe[i] = ((Keyframe)localArrayList.get(i)).clone();
    }
  }
  
  public Object getValue(float paramFloat)
  {
    Object localObject1;
    if (this.mNumKeyframes != 2)
    {
      if (paramFloat > 0.0F)
      {
        Object localObject2;
        if (paramFloat < 1.0F)
        {
          localObject1 = this.mFirstKeyframe;
          for (int i = 1;; i++)
          {
            if (i >= this.mNumKeyframes) {
              return this.mLastKeyframe.getValue();
            }
            localObject2 = (Keyframe)this.mKeyframes.get(i);
            if (paramFloat < ((Keyframe)localObject2).getFraction()) {
              break;
            }
            localObject1 = localObject2;
          }
          Interpolator localInterpolator2 = ((Keyframe)localObject2).getInterpolator();
          if (localInterpolator2 != null) {
            paramFloat = localInterpolator2.getInterpolation(paramFloat);
          }
          float f3 = ((Keyframe)localObject1).getFraction();
          f3 = (paramFloat - f3) / (((Keyframe)localObject2).getFraction() - f3);
          localObject1 = this.mEvaluator.evaluate(f3, ((Keyframe)localObject1).getValue(), ((Keyframe)localObject2).getValue());
        }
        else
        {
          localObject1 = (Keyframe)this.mKeyframes.get(-2 + this.mNumKeyframes);
          localObject2 = this.mLastKeyframe.getInterpolator();
          if (localObject2 != null) {
            paramFloat = ((Interpolator)localObject2).getInterpolation(paramFloat);
          }
          float f1 = ((Keyframe)localObject1).getFraction();
          f1 = (paramFloat - f1) / (this.mLastKeyframe.getFraction() - f1);
          localObject1 = this.mEvaluator.evaluate(f1, ((Keyframe)localObject1).getValue(), this.mLastKeyframe.getValue());
        }
      }
      else
      {
        localObject1 = (Keyframe)this.mKeyframes.get(1);
        Interpolator localInterpolator1 = ((Keyframe)localObject1).getInterpolator();
        if (localInterpolator1 != null) {
          paramFloat = localInterpolator1.getInterpolation(paramFloat);
        }
        float f2 = this.mFirstKeyframe.getFraction();
        f2 = (paramFloat - f2) / (((Keyframe)localObject1).getFraction() - f2);
        localObject1 = this.mEvaluator.evaluate(f2, this.mFirstKeyframe.getValue(), ((Keyframe)localObject1).getValue());
      }
    }
    else
    {
      if (this.mInterpolator != null) {
        paramFloat = this.mInterpolator.getInterpolation(paramFloat);
      }
      localObject1 = this.mEvaluator.evaluate(paramFloat, this.mFirstKeyframe.getValue(), this.mLastKeyframe.getValue());
    }
    return localObject1;
  }
  
  public void setEvaluator(TypeEvaluator paramTypeEvaluator)
  {
    this.mEvaluator = paramTypeEvaluator;
  }
  
  public String toString()
  {
    String str = " ";
    for (int i = 0;; i++)
    {
      if (i >= this.mNumKeyframes) {
        return str;
      }
      str = str + ((Keyframe)this.mKeyframes.get(i)).getValue() + "  ";
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.KeyframeSet
 * JD-Core Version:    0.7.0.1
 */