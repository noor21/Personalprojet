package com.nineoldandroids.animation;

import android.util.Log;
import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class PropertyValuesHolder
  implements Cloneable
{
  private static Class[] DOUBLE_VARIANTS;
  private static Class[] FLOAT_VARIANTS;
  private static Class[] INTEGER_VARIANTS;
  private static final TypeEvaluator sFloatEvaluator;
  private static final HashMap<Class, HashMap<String, Method>> sGetterPropertyMap = new HashMap();
  private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
  private static final HashMap<Class, HashMap<String, Method>> sSetterPropertyMap;
  private Object mAnimatedValue;
  private TypeEvaluator mEvaluator;
  private Method mGetter = null;
  KeyframeSet mKeyframeSet = null;
  protected Property mProperty;
  final ReentrantReadWriteLock mPropertyMapLock = new ReentrantReadWriteLock();
  String mPropertyName;
  Method mSetter = null;
  final Object[] mTmpValueArray = new Object[1];
  Class mValueType;
  
  static
  {
    sFloatEvaluator = new FloatEvaluator();
    Class[] arrayOfClass = new Class[6];
    arrayOfClass[0] = Float.TYPE;
    arrayOfClass[1] = Float.class;
    arrayOfClass[2] = Double.TYPE;
    arrayOfClass[3] = Integer.TYPE;
    arrayOfClass[4] = Double.class;
    arrayOfClass[5] = Integer.class;
    FLOAT_VARIANTS = arrayOfClass;
    arrayOfClass = new Class[6];
    arrayOfClass[0] = Integer.TYPE;
    arrayOfClass[1] = Integer.class;
    arrayOfClass[2] = Float.TYPE;
    arrayOfClass[3] = Double.TYPE;
    arrayOfClass[4] = Float.class;
    arrayOfClass[5] = Double.class;
    INTEGER_VARIANTS = arrayOfClass;
    arrayOfClass = new Class[6];
    arrayOfClass[0] = Double.TYPE;
    arrayOfClass[1] = Double.class;
    arrayOfClass[2] = Float.TYPE;
    arrayOfClass[3] = Integer.TYPE;
    arrayOfClass[4] = Float.class;
    arrayOfClass[5] = Integer.class;
    DOUBLE_VARIANTS = arrayOfClass;
    sSetterPropertyMap = new HashMap();
  }
  
  private PropertyValuesHolder(Property paramProperty)
  {
    this.mProperty = paramProperty;
    if (paramProperty != null) {
      this.mPropertyName = paramProperty.getName();
    }
  }
  
  private PropertyValuesHolder(String paramString)
  {
    this.mPropertyName = paramString;
  }
  
  static String getMethodName(String paramString1, String paramString2)
  {
    if ((paramString2 != null) && (paramString2.length() != 0))
    {
      char c = Character.toUpperCase(paramString2.charAt(0));
      String str = paramString2.substring(1);
      paramString1 = paramString1 + c + str;
    }
    return paramString1;
  }
  
  private Method getPropertyFunction(Class paramClass1, String paramString, Class paramClass2)
  {
    Object localObject2 = null;
    Object localObject1 = getMethodName(paramString, this.mPropertyName);
    if (paramClass2 == null) {}
    for (;;)
    {
      try
      {
        localObject1 = paramClass1.getMethod((String)localObject1, null);
        localObject2 = localObject1;
      }
      catch (NoSuchMethodException localNoSuchMethodException1)
      {
        try
        {
          localObject2 = paramClass1.getDeclaredMethod((String)localObject1, null);
          ((Method)localObject2).setAccessible(true);
        }
        catch (NoSuchMethodException localNoSuchMethodException2)
        {
          Log.e("PropertyValuesHolder", "Couldn't find no-arg method for property " + this.mPropertyName + ": " + localNoSuchMethodException1);
        }
        continue;
      }
      localObject1 = localObject2;
      return localObject1;
      Class[] arrayOfClass1 = new Class[1];
      Class[] arrayOfClass2;
      label122:
      Class[] arrayOfClass3;
      int j;
      int i;
      if (this.mValueType.equals(Float.class))
      {
        arrayOfClass2 = FLOAT_VARIANTS;
        arrayOfClass3 = arrayOfClass2;
        j = arrayOfClass3.length;
        i = 0;
      }
      for (;;)
      {
        for (;;)
        {
          if (i >= j) {
            break label271;
          }
          arrayOfClass2 = arrayOfClass3[i];
          arrayOfClass1[0] = arrayOfClass2;
          try
          {
            localObject2 = paramClass1.getMethod((String)localObject1, arrayOfClass1);
            this.mValueType = arrayOfClass2;
            localObject1 = localObject2;
            break;
            if (this.mValueType.equals(Integer.class))
            {
              arrayOfClass2 = INTEGER_VARIANTS;
              break label122;
            }
            if (this.mValueType.equals(Double.class))
            {
              arrayOfClass2 = DOUBLE_VARIANTS;
              break label122;
            }
            arrayOfClass2 = new Class[1];
            arrayOfClass2[0] = this.mValueType;
          }
          catch (NoSuchMethodException localNoSuchMethodException3)
          {
            try
            {
              localObject2 = paramClass1.getDeclaredMethod((String)localObject1, arrayOfClass1);
              ((Method)localObject2).setAccessible(true);
              this.mValueType = arrayOfClass2;
              localObject1 = localObject2;
            }
            catch (NoSuchMethodException localNoSuchMethodException4)
            {
              i++;
            }
          }
        }
      }
      label271:
      Log.e("PropertyValuesHolder", "Couldn't find setter/getter for property " + this.mPropertyName + " with value type " + this.mValueType);
    }
  }
  
  public static PropertyValuesHolder ofFloat(Property<?, Float> paramProperty, float... paramVarArgs)
  {
    return new FloatPropertyValuesHolder(paramProperty, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofFloat(String paramString, float... paramVarArgs)
  {
    return new FloatPropertyValuesHolder(paramString, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofInt(Property<?, Integer> paramProperty, int... paramVarArgs)
  {
    return new IntPropertyValuesHolder(paramProperty, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofInt(String paramString, int... paramVarArgs)
  {
    return new IntPropertyValuesHolder(paramString, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofKeyframe(Property paramProperty, Keyframe... paramVarArgs)
  {
    KeyframeSet localKeyframeSet = KeyframeSet.ofKeyframe(paramVarArgs);
    Object localObject;
    if (!(localKeyframeSet instanceof IntKeyframeSet))
    {
      if (!(localKeyframeSet instanceof FloatKeyframeSet))
      {
        localObject = new PropertyValuesHolder(paramProperty);
        ((PropertyValuesHolder)localObject).mKeyframeSet = localKeyframeSet;
        ((PropertyValuesHolder)localObject).mValueType = paramVarArgs[0].getType();
      }
      else
      {
        localObject = new FloatPropertyValuesHolder(paramProperty, (FloatKeyframeSet)localKeyframeSet);
      }
    }
    else {
      localObject = new IntPropertyValuesHolder(paramProperty, (IntKeyframeSet)localKeyframeSet);
    }
    return localObject;
  }
  
  public static PropertyValuesHolder ofKeyframe(String paramString, Keyframe... paramVarArgs)
  {
    KeyframeSet localKeyframeSet = KeyframeSet.ofKeyframe(paramVarArgs);
    Object localObject;
    if (!(localKeyframeSet instanceof IntKeyframeSet))
    {
      if (!(localKeyframeSet instanceof FloatKeyframeSet))
      {
        localObject = new PropertyValuesHolder(paramString);
        ((PropertyValuesHolder)localObject).mKeyframeSet = localKeyframeSet;
        ((PropertyValuesHolder)localObject).mValueType = paramVarArgs[0].getType();
      }
      else
      {
        localObject = new FloatPropertyValuesHolder(paramString, (FloatKeyframeSet)localKeyframeSet);
      }
    }
    else {
      localObject = new IntPropertyValuesHolder(paramString, (IntKeyframeSet)localKeyframeSet);
    }
    return localObject;
  }
  
  public static <V> PropertyValuesHolder ofObject(Property paramProperty, TypeEvaluator<V> paramTypeEvaluator, V... paramVarArgs)
  {
    PropertyValuesHolder localPropertyValuesHolder = new PropertyValuesHolder(paramProperty);
    localPropertyValuesHolder.setObjectValues(paramVarArgs);
    localPropertyValuesHolder.setEvaluator(paramTypeEvaluator);
    return localPropertyValuesHolder;
  }
  
  public static PropertyValuesHolder ofObject(String paramString, TypeEvaluator paramTypeEvaluator, Object... paramVarArgs)
  {
    PropertyValuesHolder localPropertyValuesHolder = new PropertyValuesHolder(paramString);
    localPropertyValuesHolder.setObjectValues(paramVarArgs);
    localPropertyValuesHolder.setEvaluator(paramTypeEvaluator);
    return localPropertyValuesHolder;
  }
  
  private void setupGetter(Class paramClass)
  {
    this.mGetter = setupSetterOrGetter(paramClass, sGetterPropertyMap, "get", null);
  }
  
  private Method setupSetterOrGetter(Class paramClass1, HashMap<Class, HashMap<String, Method>> paramHashMap, String paramString, Class paramClass2)
  {
    Method localMethod = null;
    try
    {
      this.mPropertyMapLock.writeLock().lock();
      HashMap localHashMap = (HashMap)paramHashMap.get(paramClass1);
      if (localHashMap != null) {
        localMethod = (Method)localHashMap.get(this.mPropertyName);
      }
      if (localMethod == null)
      {
        localMethod = getPropertyFunction(paramClass1, paramString, paramClass2);
        if (localHashMap == null)
        {
          localHashMap = new HashMap();
          paramHashMap.put(paramClass1, localHashMap);
        }
        localHashMap.put(this.mPropertyName, localMethod);
      }
      return localMethod;
    }
    finally
    {
      this.mPropertyMapLock.writeLock().unlock();
    }
  }
  
  private void setupValue(Object paramObject, Keyframe paramKeyframe)
  {
    if (this.mProperty != null) {
      paramKeyframe.setValue(this.mProperty.get(paramObject));
    }
    try
    {
      if (this.mGetter == null) {
        setupGetter(paramObject.getClass());
      }
      paramKeyframe.setValue(this.mGetter.invoke(paramObject, new Object[0]));
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      for (;;)
      {
        Log.e("PropertyValuesHolder", localInvocationTargetException.toString());
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;)
      {
        Log.e("PropertyValuesHolder", localIllegalAccessException.toString());
      }
    }
  }
  
  void calculateValue(float paramFloat)
  {
    this.mAnimatedValue = this.mKeyframeSet.getValue(paramFloat);
  }
  
  public PropertyValuesHolder clone()
  {
    try
    {
      localPropertyValuesHolder = (PropertyValuesHolder)super.clone();
      localPropertyValuesHolder.mPropertyName = this.mPropertyName;
      localPropertyValuesHolder.mProperty = this.mProperty;
      localPropertyValuesHolder.mKeyframeSet = this.mKeyframeSet.clone();
      localPropertyValuesHolder.mEvaluator = this.mEvaluator;
      return localPropertyValuesHolder;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      for (;;)
      {
        PropertyValuesHolder localPropertyValuesHolder = null;
      }
    }
  }
  
  Object getAnimatedValue()
  {
    return this.mAnimatedValue;
  }
  
  public String getPropertyName()
  {
    return this.mPropertyName;
  }
  
  void init()
  {
    if (this.mEvaluator == null)
    {
      TypeEvaluator localTypeEvaluator;
      if (this.mValueType != Integer.class)
      {
        if (this.mValueType != Float.class) {
          localTypeEvaluator = null;
        } else {
          localTypeEvaluator = sFloatEvaluator;
        }
      }
      else {
        localTypeEvaluator = sIntEvaluator;
      }
      this.mEvaluator = localTypeEvaluator;
    }
    if (this.mEvaluator != null) {
      this.mKeyframeSet.setEvaluator(this.mEvaluator);
    }
  }
  
  void setAnimatedValue(Object paramObject)
  {
    if (this.mProperty != null) {
      this.mProperty.set(paramObject, getAnimatedValue());
    }
    if (this.mSetter != null) {}
    try
    {
      this.mTmpValueArray[0] = getAnimatedValue();
      this.mSetter.invoke(paramObject, this.mTmpValueArray);
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      for (;;)
      {
        Log.e("PropertyValuesHolder", localInvocationTargetException.toString());
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;)
      {
        Log.e("PropertyValuesHolder", localIllegalAccessException.toString());
      }
    }
  }
  
  public void setEvaluator(TypeEvaluator paramTypeEvaluator)
  {
    this.mEvaluator = paramTypeEvaluator;
    this.mKeyframeSet.setEvaluator(paramTypeEvaluator);
  }
  
  public void setFloatValues(float... paramVarArgs)
  {
    this.mValueType = Float.TYPE;
    this.mKeyframeSet = KeyframeSet.ofFloat(paramVarArgs);
  }
  
  public void setIntValues(int... paramVarArgs)
  {
    this.mValueType = Integer.TYPE;
    this.mKeyframeSet = KeyframeSet.ofInt(paramVarArgs);
  }
  
  public void setKeyframes(Keyframe... paramVarArgs)
  {
    int j = paramVarArgs.length;
    Keyframe[] arrayOfKeyframe = new Keyframe[Math.max(j, 2)];
    this.mValueType = paramVarArgs[0].getType();
    for (int i = 0;; i++)
    {
      if (i >= j)
      {
        this.mKeyframeSet = new KeyframeSet(arrayOfKeyframe);
        return;
      }
      arrayOfKeyframe[i] = paramVarArgs[i];
    }
  }
  
  public void setObjectValues(Object... paramVarArgs)
  {
    this.mValueType = paramVarArgs[0].getClass();
    this.mKeyframeSet = KeyframeSet.ofObject(paramVarArgs);
  }
  
  public void setProperty(Property paramProperty)
  {
    this.mProperty = paramProperty;
  }
  
  public void setPropertyName(String paramString)
  {
    this.mPropertyName = paramString;
  }
  
  void setupEndValue(Object paramObject)
  {
    setupValue(paramObject, (Keyframe)this.mKeyframeSet.mKeyframes.get(-1 + this.mKeyframeSet.mKeyframes.size()));
  }
  
  void setupSetter(Class paramClass)
  {
    this.mSetter = setupSetterOrGetter(paramClass, sSetterPropertyMap, "set", this.mValueType);
  }
  
  void setupSetterAndGetter(Object paramObject)
  {
    Iterator localIterator;
    Object localObject;
    if (this.mProperty != null)
    {
      try
      {
        this.mProperty.get(paramObject);
        localIterator = this.mKeyframeSet.mKeyframes.iterator();
        while (localIterator.hasNext())
        {
          localObject = (Keyframe)localIterator.next();
          if (!((Keyframe)localObject).hasValue()) {
            ((Keyframe)localObject).setValue(this.mProperty.get(paramObject));
          }
        }
        localObject = paramObject.getClass();
      }
      catch (ClassCastException localClassCastException)
      {
        Log.e("PropertyValuesHolder", "No such property (" + this.mProperty.getName() + ") on target object " + paramObject + ". Trying reflection instead");
        this.mProperty = null;
      }
    }
    else
    {
      if (this.mSetter == null) {
        setupSetter((Class)localObject);
      }
      localIterator = this.mKeyframeSet.mKeyframes.iterator();
      while (localIterator.hasNext())
      {
        Keyframe localKeyframe = (Keyframe)localIterator.next();
        if (!localKeyframe.hasValue())
        {
          if (this.mGetter == null) {
            setupGetter((Class)localObject);
          }
          try
          {
            localKeyframe.setValue(this.mGetter.invoke(paramObject, new Object[0]));
          }
          catch (InvocationTargetException localInvocationTargetException)
          {
            Log.e("PropertyValuesHolder", localInvocationTargetException.toString());
          }
          catch (IllegalAccessException localIllegalAccessException)
          {
            Log.e("PropertyValuesHolder", localIllegalAccessException.toString());
          }
        }
      }
    }
  }
  
  void setupStartValue(Object paramObject)
  {
    setupValue(paramObject, (Keyframe)this.mKeyframeSet.mKeyframes.get(0));
  }
  
  public String toString()
  {
    return this.mPropertyName + ": " + this.mKeyframeSet.toString();
  }
  
  static class FloatPropertyValuesHolder
    extends PropertyValuesHolder
  {
    float mFloatAnimatedValue;
    FloatKeyframeSet mFloatKeyframeSet;
    private FloatProperty mFloatProperty;
    
    public FloatPropertyValuesHolder(Property paramProperty, FloatKeyframeSet paramFloatKeyframeSet)
    {
      super(null);
      this.mValueType = Float.TYPE;
      this.mKeyframeSet = paramFloatKeyframeSet;
      this.mFloatKeyframeSet = ((FloatKeyframeSet)this.mKeyframeSet);
      if ((paramProperty instanceof FloatProperty)) {
        this.mFloatProperty = ((FloatProperty)this.mProperty);
      }
    }
    
    public FloatPropertyValuesHolder(Property paramProperty, float... paramVarArgs)
    {
      super(null);
      setFloatValues(paramVarArgs);
      if ((paramProperty instanceof FloatProperty)) {
        this.mFloatProperty = ((FloatProperty)this.mProperty);
      }
    }
    
    public FloatPropertyValuesHolder(String paramString, FloatKeyframeSet paramFloatKeyframeSet)
    {
      super(null);
      this.mValueType = Float.TYPE;
      this.mKeyframeSet = paramFloatKeyframeSet;
      this.mFloatKeyframeSet = ((FloatKeyframeSet)this.mKeyframeSet);
    }
    
    public FloatPropertyValuesHolder(String paramString, float... paramVarArgs)
    {
      super(null);
      setFloatValues(paramVarArgs);
    }
    
    void calculateValue(float paramFloat)
    {
      this.mFloatAnimatedValue = this.mFloatKeyframeSet.getFloatValue(paramFloat);
    }
    
    public FloatPropertyValuesHolder clone()
    {
      FloatPropertyValuesHolder localFloatPropertyValuesHolder = (FloatPropertyValuesHolder)super.clone();
      localFloatPropertyValuesHolder.mFloatKeyframeSet = ((FloatKeyframeSet)localFloatPropertyValuesHolder.mKeyframeSet);
      return localFloatPropertyValuesHolder;
    }
    
    Object getAnimatedValue()
    {
      return Float.valueOf(this.mFloatAnimatedValue);
    }
    
    void setAnimatedValue(Object paramObject)
    {
      if (this.mFloatProperty != null) {
        this.mFloatProperty.setValue(paramObject, this.mFloatAnimatedValue);
      }
      for (;;)
      {
        return;
        if (this.mProperty != null) {
          this.mProperty.set(paramObject, Float.valueOf(this.mFloatAnimatedValue));
        } else if (this.mSetter != null) {
          try
          {
            this.mTmpValueArray[0] = Float.valueOf(this.mFloatAnimatedValue);
            this.mSetter.invoke(paramObject, this.mTmpValueArray);
          }
          catch (InvocationTargetException localInvocationTargetException)
          {
            Log.e("PropertyValuesHolder", localInvocationTargetException.toString());
          }
          catch (IllegalAccessException localIllegalAccessException)
          {
            Log.e("PropertyValuesHolder", localIllegalAccessException.toString());
          }
        }
      }
    }
    
    public void setFloatValues(float... paramVarArgs)
    {
      super.setFloatValues(paramVarArgs);
      this.mFloatKeyframeSet = ((FloatKeyframeSet)this.mKeyframeSet);
    }
    
    void setupSetter(Class paramClass)
    {
      if (this.mProperty == null) {
        super.setupSetter(paramClass);
      }
    }
  }
  
  static class IntPropertyValuesHolder
    extends PropertyValuesHolder
  {
    int mIntAnimatedValue;
    IntKeyframeSet mIntKeyframeSet;
    private IntProperty mIntProperty;
    
    public IntPropertyValuesHolder(Property paramProperty, IntKeyframeSet paramIntKeyframeSet)
    {
      super(null);
      this.mValueType = Integer.TYPE;
      this.mKeyframeSet = paramIntKeyframeSet;
      this.mIntKeyframeSet = ((IntKeyframeSet)this.mKeyframeSet);
      if ((paramProperty instanceof IntProperty)) {
        this.mIntProperty = ((IntProperty)this.mProperty);
      }
    }
    
    public IntPropertyValuesHolder(Property paramProperty, int... paramVarArgs)
    {
      super(null);
      setIntValues(paramVarArgs);
      if ((paramProperty instanceof IntProperty)) {
        this.mIntProperty = ((IntProperty)this.mProperty);
      }
    }
    
    public IntPropertyValuesHolder(String paramString, IntKeyframeSet paramIntKeyframeSet)
    {
      super(null);
      this.mValueType = Integer.TYPE;
      this.mKeyframeSet = paramIntKeyframeSet;
      this.mIntKeyframeSet = ((IntKeyframeSet)this.mKeyframeSet);
    }
    
    public IntPropertyValuesHolder(String paramString, int... paramVarArgs)
    {
      super(null);
      setIntValues(paramVarArgs);
    }
    
    void calculateValue(float paramFloat)
    {
      this.mIntAnimatedValue = this.mIntKeyframeSet.getIntValue(paramFloat);
    }
    
    public IntPropertyValuesHolder clone()
    {
      IntPropertyValuesHolder localIntPropertyValuesHolder = (IntPropertyValuesHolder)super.clone();
      localIntPropertyValuesHolder.mIntKeyframeSet = ((IntKeyframeSet)localIntPropertyValuesHolder.mKeyframeSet);
      return localIntPropertyValuesHolder;
    }
    
    Object getAnimatedValue()
    {
      return Integer.valueOf(this.mIntAnimatedValue);
    }
    
    void setAnimatedValue(Object paramObject)
    {
      if (this.mIntProperty != null) {
        this.mIntProperty.setValue(paramObject, this.mIntAnimatedValue);
      }
      for (;;)
      {
        return;
        if (this.mProperty != null) {
          this.mProperty.set(paramObject, Integer.valueOf(this.mIntAnimatedValue));
        } else if (this.mSetter != null) {
          try
          {
            this.mTmpValueArray[0] = Integer.valueOf(this.mIntAnimatedValue);
            this.mSetter.invoke(paramObject, this.mTmpValueArray);
          }
          catch (InvocationTargetException localInvocationTargetException)
          {
            Log.e("PropertyValuesHolder", localInvocationTargetException.toString());
          }
          catch (IllegalAccessException localIllegalAccessException)
          {
            Log.e("PropertyValuesHolder", localIllegalAccessException.toString());
          }
        }
      }
    }
    
    public void setIntValues(int... paramVarArgs)
    {
      super.setIntValues(paramVarArgs);
      this.mIntKeyframeSet = ((IntKeyframeSet)this.mKeyframeSet);
    }
    
    void setupSetter(Class paramClass)
    {
      if (this.mProperty == null) {
        super.setupSetter(paramClass);
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.PropertyValuesHolder
 * JD-Core Version:    0.7.0.1
 */