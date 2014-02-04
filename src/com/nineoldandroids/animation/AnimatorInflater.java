package com.nineoldandroids.animation;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflater
{
  private static final int[] Animator;
  private static final int[] AnimatorSet;
  private static final int AnimatorSet_ordering = 0;
  private static final int Animator_duration = 1;
  private static final int Animator_interpolator = 0;
  private static final int Animator_repeatCount = 3;
  private static final int Animator_repeatMode = 4;
  private static final int Animator_startOffset = 2;
  private static final int Animator_valueFrom = 5;
  private static final int Animator_valueTo = 6;
  private static final int Animator_valueType = 7;
  private static final int[] PropertyAnimator;
  private static final int PropertyAnimator_propertyName;
  private static final int TOGETHER;
  private static final int VALUE_TYPE_FLOAT;
  
  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16843490;
    AnimatorSet = arrayOfInt;
    arrayOfInt = new int[1];
    arrayOfInt[0] = 16843489;
    PropertyAnimator = arrayOfInt;
    arrayOfInt = new int[8];
    arrayOfInt[0] = 16843073;
    arrayOfInt[1] = 16843160;
    arrayOfInt[2] = 16843198;
    arrayOfInt[3] = 16843199;
    arrayOfInt[4] = 16843200;
    arrayOfInt[5] = 16843486;
    arrayOfInt[6] = 16843487;
    arrayOfInt[7] = 16843488;
    Animator = arrayOfInt;
  }
  
  private static Animator createAnimatorFromXml(Context paramContext, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    return createAnimatorFromXml(paramContext, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser), null, 0);
  }
  
  private static Animator createAnimatorFromXml(Context paramContext, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, AnimatorSet paramAnimatorSet, int paramInt)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    int j = paramXmlPullParser.getDepth();
    for (;;)
    {
      int k = paramXmlPullParser.next();
      Object localObject3;
      Object localObject4;
      int i;
      if (((k == 3) && (paramXmlPullParser.getDepth() <= j)) || (k == 1))
      {
        if ((paramAnimatorSet != null) && (localObject2 != null))
        {
          localObject3 = new Animator[localObject2.size()];
          j = 0;
          localObject4 = localObject2.iterator();
        }
        for (;;)
        {
          if (!((Iterator)localObject4).hasNext())
          {
            if (paramInt != 0) {
              paramAnimatorSet.playSequentially((Animator[])localObject3);
            } else {
              paramAnimatorSet.playTogether((Animator[])localObject3);
            }
            return localObject1;
          }
          Animator localAnimator = (Animator)((Iterator)localObject4).next();
          i = j + 1;
          localObject3[j] = localAnimator;
          j = i;
        }
      }
      if (localObject3 == 2)
      {
        localObject1 = paramXmlPullParser.getName();
        if (!((String)localObject1).equals("objectAnimator"))
        {
          if (!((String)localObject1).equals("animator"))
          {
            if (!((String)localObject1).equals("set")) {
              throw new RuntimeException("Unknown animator name: " + paramXmlPullParser.getName());
            }
            localObject1 = new AnimatorSet();
            localObject3 = paramContext.obtainStyledAttributes(paramAttributeSet, AnimatorSet);
            localObject4 = new TypedValue();
            ((TypedArray)localObject3).getValue(0, (TypedValue)localObject4);
            int m;
            if (((TypedValue)localObject4).type != 16) {
              m = 0;
            } else {
              m = m.data;
            }
            createAnimatorFromXml(paramContext, paramXmlPullParser, paramAttributeSet, (AnimatorSet)localObject1, m);
            ((TypedArray)localObject3).recycle();
          }
          else
          {
            localObject1 = loadAnimator(paramContext, paramAttributeSet, null);
          }
        }
        else {
          localObject1 = loadObjectAnimator(paramContext, paramAttributeSet);
        }
        if (paramAnimatorSet != null)
        {
          ArrayList localArrayList;
          if (i == null) {
            localArrayList = new ArrayList();
          }
          localArrayList.add(localObject1);
        }
      }
    }
  }
  
  /* Error */
  public static Animator loadAnimator(Context paramContext, int paramInt)
    throws Resources.NotFoundException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: invokevirtual 180	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   6: iload_1
    //   7: invokevirtual 186	android/content/res/Resources:getAnimation	(I)Landroid/content/res/XmlResourceParser;
    //   10: astore_2
    //   11: aload_0
    //   12: aload_2
    //   13: invokestatic 188	com/nineoldandroids/animation/AnimatorInflater:createAnimatorFromXml	(Landroid/content/Context;Lorg/xmlpull/v1/XmlPullParser;)Lcom/nineoldandroids/animation/Animator;
    //   16: astore_3
    //   17: aload_2
    //   18: ifnull +9 -> 27
    //   21: aload_2
    //   22: invokeinterface 193 1 0
    //   27: aload_3
    //   28: areturn
    //   29: astore_3
    //   30: new 176	android/content/res/Resources$NotFoundException
    //   33: dup
    //   34: new 123	java/lang/StringBuilder
    //   37: dup
    //   38: invokespecial 124	java/lang/StringBuilder:<init>	()V
    //   41: ldc 195
    //   43: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: iload_1
    //   47: invokestatic 201	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   50: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: invokevirtual 133	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   56: invokespecial 202	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   59: astore 4
    //   61: aload 4
    //   63: aload_3
    //   64: invokevirtual 206	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   67: pop
    //   68: aload 4
    //   70: athrow
    //   71: astore_3
    //   72: aload_2
    //   73: ifnull +9 -> 82
    //   76: aload_2
    //   77: invokeinterface 193 1 0
    //   82: aload_3
    //   83: athrow
    //   84: astore 4
    //   86: new 176	android/content/res/Resources$NotFoundException
    //   89: dup
    //   90: new 123	java/lang/StringBuilder
    //   93: dup
    //   94: invokespecial 124	java/lang/StringBuilder:<init>	()V
    //   97: ldc 195
    //   99: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: iload_1
    //   103: invokestatic 201	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   106: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: invokevirtual 133	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   112: invokespecial 202	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   115: astore_3
    //   116: aload_3
    //   117: aload 4
    //   119: invokevirtual 206	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   122: pop
    //   123: aload_3
    //   124: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	125	0	paramContext	Context
    //   0	125	1	paramInt	int
    //   1	76	2	localXmlResourceParser	android.content.res.XmlResourceParser
    //   16	12	3	localAnimator	Animator
    //   29	35	3	localXmlPullParserException	XmlPullParserException
    //   71	12	3	localObject	Object
    //   115	9	3	localNotFoundException1	Resources.NotFoundException
    //   59	10	4	localNotFoundException2	Resources.NotFoundException
    //   84	34	4	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   2	17	29	org/xmlpull/v1/XmlPullParserException
    //   2	17	71	finally
    //   30	71	71	finally
    //   86	125	71	finally
    //   2	17	84	java/io/IOException
  }
  
  private static ValueAnimator loadAnimator(Context paramContext, AttributeSet paramAttributeSet, ValueAnimator paramValueAnimator)
    throws Resources.NotFoundException
  {
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Animator);
    long l1 = localTypedArray.getInt(1, 0);
    long l2 = localTypedArray.getInt(2, 0);
    int j = localTypedArray.getInt(7, 0);
    if (paramValueAnimator == null) {
      paramValueAnimator = new ValueAnimator();
    }
    int n;
    if (j != 0) {
      n = 0;
    } else {
      n = 1;
    }
    TypedValue localTypedValue1 = localTypedArray.peekValue(5);
    int i3;
    if (localTypedValue1 == null) {
      i3 = 0;
    } else {
      i3 = 1;
    }
    int i4;
    if (i3 == 0) {
      i4 = 0;
    } else {
      i4 = localTypedValue1.type;
    }
    TypedValue localTypedValue2 = localTypedArray.peekValue(6);
    int k;
    if (localTypedValue2 == null) {
      k = 0;
    } else {
      k = 1;
    }
    int i2;
    if (k == 0) {
      i2 = 0;
    } else {
      i2 = i2.type;
    }
    if (((i3 != 0) && (i4 >= 28) && (i4 <= 31)) || ((k != 0) && (i2 >= 28) && (i2 <= 31)))
    {
      n = 0;
      ArgbEvaluator localArgbEvaluator = new ArgbEvaluator();
      paramValueAnimator.setEvaluator(localArgbEvaluator);
    }
    int[] arrayOfInt3;
    if (n == 0)
    {
      if (i3 == 0)
      {
        if (k != 0)
        {
          if (i2 != 5)
          {
            if ((i2 < 28) || (i2 > 31)) {
              k = localTypedArray.getInt(6, 0);
            } else {
              k = localTypedArray.getColor(6, 0);
            }
          }
          else {
            k = (int)localTypedArray.getDimension(6, 0.0F);
          }
          int[] arrayOfInt2 = new int[1];
          arrayOfInt2[0] = k;
          paramValueAnimator.setIntValues(arrayOfInt2);
        }
      }
      else
      {
        int i1;
        if (i4 != 5)
        {
          if ((i4 < 28) || (i4 > 31)) {
            i1 = localTypedArray.getInt(5, 0);
          } else {
            i1 = localTypedArray.getColor(5, 0);
          }
        }
        else {
          i1 = (int)localTypedArray.getDimension(5, 0.0F);
        }
        if (k == 0)
        {
          int[] arrayOfInt1 = new int[1];
          arrayOfInt1[0] = i1;
          paramValueAnimator.setIntValues(arrayOfInt1);
        }
        else
        {
          int m;
          if (i2 != 5)
          {
            if ((i2 < 28) || (i2 > 31)) {
              m = localTypedArray.getInt(6, 0);
            } else {
              m = localTypedArray.getColor(6, 0);
            }
          }
          else {
            m = (int)localTypedArray.getDimension(6, 0.0F);
          }
          arrayOfInt3 = new int[2];
          arrayOfInt3[0] = i1;
          arrayOfInt3[1] = m;
          paramValueAnimator.setIntValues(arrayOfInt3);
        }
      }
    }
    else
    {
      float f1;
      float[] arrayOfFloat;
      if (i3 == 0)
      {
        if (arrayOfInt3 != 5) {
          f1 = localTypedArray.getFloat(6, 0.0F);
        } else {
          f1 = localTypedArray.getDimension(6, 0.0F);
        }
        arrayOfFloat = new float[1];
        arrayOfFloat[0] = f1;
        paramValueAnimator.setFloatValues(arrayOfFloat);
      }
      else
      {
        if (i4 != 5) {
          f1 = localTypedArray.getFloat(5, 0.0F);
        } else {
          f1 = localTypedArray.getDimension(5, 0.0F);
        }
        if (arrayOfFloat == 0)
        {
          arrayOfFloat = new float[1];
          arrayOfFloat[0] = f1;
          paramValueAnimator.setFloatValues(arrayOfFloat);
        }
        else
        {
          float f2;
          if (arrayOfInt3 != 5) {
            f2 = localTypedArray.getFloat(6, 0.0F);
          } else {
            f2 = localTypedArray.getDimension(6, 0.0F);
          }
          arrayOfFloat = new float[2];
          arrayOfFloat[0] = f1;
          arrayOfFloat[1] = f2;
          paramValueAnimator.setFloatValues(arrayOfFloat);
        }
      }
    }
    paramValueAnimator.setDuration(l1);
    paramValueAnimator.setStartDelay(l2);
    if (localTypedArray.hasValue(3))
    {
      i = localTypedArray.getInt(3, 0);
      paramValueAnimator.setRepeatCount(i);
    }
    if (localTypedArray.hasValue(4))
    {
      i = localTypedArray.getInt(4, 1);
      paramValueAnimator.setRepeatMode(i);
    }
    int i = localTypedArray.getResourceId(0, 0);
    if (i > 0)
    {
      Interpolator localInterpolator = AnimationUtils.loadInterpolator(paramContext, i);
      paramValueAnimator.setInterpolator(localInterpolator);
    }
    localTypedArray.recycle();
    return paramValueAnimator;
  }
  
  private static ObjectAnimator loadObjectAnimator(Context paramContext, AttributeSet paramAttributeSet)
    throws Resources.NotFoundException
  {
    ObjectAnimator localObjectAnimator = new ObjectAnimator();
    loadAnimator(paramContext, paramAttributeSet, localObjectAnimator);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, PropertyAnimator);
    localObjectAnimator.setPropertyName(localTypedArray.getString(0));
    localTypedArray.recycle();
    return localObjectAnimator;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.AnimatorInflater
 * JD-Core Version:    0.7.0.1
 */