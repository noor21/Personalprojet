package org.acra.collector;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.acra.ACRA;
import org.acra.log.ACRALog;

final class DisplayManagerCollector
{
  static final SparseArray<String> mDensities = new SparseArray();
  static final SparseArray<String> mFlagsNames = new SparseArray();
  
  private static String activeFlags(SparseArray<String> paramSparseArray, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0;; i++)
    {
      if (i >= paramSparseArray.size()) {
        return localStringBuilder.toString();
      }
      int j = paramInt & paramSparseArray.keyAt(i);
      if (j > 0)
      {
        if (localStringBuilder.length() > 0) {
          localStringBuilder.append('+');
        }
        localStringBuilder.append((String)paramSparseArray.get(j));
      }
    }
  }
  
  private static String collectCurrentSizeRange(Display paramDisplay)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      Object localObject1 = paramDisplay.getClass();
      Object localObject2 = new Class[2];
      localObject2[0] = Point.class;
      localObject2[1] = Point.class;
      Method localMethod = ((Class)localObject1).getMethod("getCurrentSizeRange", (Class[])localObject2);
      Point localPoint = new Point();
      localObject2 = new Point();
      localObject1 = new Object[2];
      localObject1[0] = localPoint;
      localObject1[1] = localObject2;
      localMethod.invoke(paramDisplay, (Object[])localObject1);
      localStringBuilder.append(paramDisplay.getDisplayId()).append(".currentSizeRange.smallest=[").append(localPoint.x).append(',').append(localPoint.y).append(']').append('\n');
      localStringBuilder.append(paramDisplay.getDisplayId()).append(".currentSizeRange.largest=[").append(((Point)localObject2).x).append(',').append(((Point)localObject2).y).append(']').append('\n');
      label164:
      return localStringBuilder.toString();
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label164;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label164;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label164;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label164;
    }
    catch (SecurityException localSecurityException)
    {
      break label164;
    }
  }
  
  private static Object collectDisplayData(Display paramDisplay)
  {
    paramDisplay.getMetrics(new DisplayMetrics());
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(collectCurrentSizeRange(paramDisplay));
    localStringBuilder.append(collectFlags(paramDisplay));
    localStringBuilder.append(paramDisplay.getDisplayId()).append(".height=").append(paramDisplay.getHeight()).append('\n');
    localStringBuilder.append(collectMetrics(paramDisplay, "getMetrics"));
    localStringBuilder.append(collectName(paramDisplay));
    localStringBuilder.append(paramDisplay.getDisplayId()).append(".orientation=").append(paramDisplay.getOrientation()).append('\n');
    localStringBuilder.append(paramDisplay.getDisplayId()).append(".pixelFormat=").append(paramDisplay.getPixelFormat()).append('\n');
    localStringBuilder.append(collectMetrics(paramDisplay, "getRealMetrics"));
    localStringBuilder.append(collectSize(paramDisplay, "getRealSize"));
    localStringBuilder.append(collectRectSize(paramDisplay));
    localStringBuilder.append(paramDisplay.getDisplayId()).append(".refreshRate=").append(paramDisplay.getRefreshRate()).append('\n');
    localStringBuilder.append(collectRotation(paramDisplay));
    localStringBuilder.append(collectSize(paramDisplay, "getSize"));
    localStringBuilder.append(paramDisplay.getDisplayId()).append(".width=").append(paramDisplay.getWidth()).append('\n');
    localStringBuilder.append(collectIsValid(paramDisplay));
    return localStringBuilder.toString();
  }
  
  public static String collectDisplays(Context paramContext)
  {
    Display[] arrayOfDisplay = null;
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject2;
    if (Compatibility.getAPILevel() < 17)
    {
      localObject2 = (WindowManager)paramContext.getSystemService("window");
      arrayOfDisplay = new Display[1];
      arrayOfDisplay[0] = ((WindowManager)localObject2).getDefaultDisplay();
    }
    for (;;)
    {
      localObject2 = arrayOfDisplay;
      int j = localObject2.length;
      for (int i = 0; i < j; i++) {
        localStringBuilder.append(collectDisplayData(localObject2[i]));
      }
      try
      {
        Object localObject1 = paramContext.getSystemService((String)paramContext.getClass().getField("DISPLAY_SERVICE").get(null));
        localObject1 = (Display[])localObject1.getClass().getMethod("getDisplays", new Class[0]).invoke(localObject1, new Object[0]);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", localIllegalArgumentException);
      }
      catch (SecurityException localSecurityException)
      {
        ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", localSecurityException);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", localIllegalAccessException);
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", localNoSuchFieldException);
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", localNoSuchMethodException);
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", localInvocationTargetException);
      }
    }
    return localStringBuilder.toString();
  }
  
  private static String collectFlags(Display paramDisplay)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      int j = ((Integer)paramDisplay.getClass().getMethod("getFlags", new Class[0]).invoke(paramDisplay, new Object[0])).intValue();
      Field[] arrayOfField = paramDisplay.getClass().getFields();
      int k = arrayOfField.length;
      i = 0;
      if (i < k)
      {
        Field localField = arrayOfField[i];
        if (localField.getName().startsWith("FLAG_")) {
          mFlagsNames.put(localField.getInt(null), localField.getName());
        }
      }
      else
      {
        localStringBuilder.append(paramDisplay.getDisplayId()).append(".flags=").append(activeFlags(mFlagsNames, j)).append('\n');
        label127:
        return localStringBuilder.toString();
      }
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label127;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label127;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label127;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label127;
    }
    catch (SecurityException localSecurityException)
    {
      for (;;)
      {
        int i;
        continue;
        i++;
      }
    }
  }
  
  private static Object collectIsValid(Display paramDisplay)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      Boolean localBoolean = (Boolean)paramDisplay.getClass().getMethod("isValid", new Class[0]).invoke(paramDisplay, new Object[0]);
      localStringBuilder.append(paramDisplay.getDisplayId()).append(".isValid=").append(localBoolean).append('\n');
      label58:
      return localStringBuilder.toString();
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label58;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label58;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label58;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label58;
    }
    catch (SecurityException localSecurityException)
    {
      break label58;
    }
  }
  
  private static Object collectMetrics(Display paramDisplay, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      DisplayMetrics localDisplayMetrics = (DisplayMetrics)paramDisplay.getClass().getMethod(paramString, new Class[0]).invoke(paramDisplay, new Object[0]);
      Field[] arrayOfField = DisplayMetrics.class.getFields();
      int j = arrayOfField.length;
      i = 0;
      if (i < j)
      {
        Field localField = arrayOfField[i];
        if ((localField.getType().equals(Integer.class)) && (localField.getName().startsWith("DENSITY_")) && (!localField.getName().equals("DENSITY_DEFAULT"))) {
          mDensities.put(localField.getInt(null), localField.getName());
        }
      }
      else
      {
        localStringBuilder.append(paramDisplay.getDisplayId()).append('.').append(paramString).append(".density=").append(localDisplayMetrics.density).append('\n');
        localStringBuilder.append(paramDisplay.getDisplayId()).append('.').append(paramString).append(".densityDpi=").append(localDisplayMetrics.getClass().getField("densityDpi")).append('\n');
        localStringBuilder.append(paramDisplay.getDisplayId()).append('.').append(paramString).append("scaledDensity=x").append(localDisplayMetrics.scaledDensity).append('\n');
        localStringBuilder.append(paramDisplay.getDisplayId()).append('.').append(paramString).append(".widthPixels=").append(localDisplayMetrics.widthPixels).append('\n');
        localStringBuilder.append(paramDisplay.getDisplayId()).append('.').append(paramString).append(".heightPixels=").append(localDisplayMetrics.heightPixels).append('\n');
        localStringBuilder.append(paramDisplay.getDisplayId()).append('.').append(paramString).append(".xdpi=").append(localDisplayMetrics.xdpi).append('\n');
        localStringBuilder.append(paramDisplay.getDisplayId()).append('.').append(paramString).append(".ydpi=").append(localDisplayMetrics.ydpi).append('\n');
        label380:
        return localStringBuilder.toString();
      }
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      break label380;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label380;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label380;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label380;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label380;
    }
    catch (SecurityException localSecurityException)
    {
      for (;;)
      {
        int i;
        continue;
        i++;
      }
    }
  }
  
  private static String collectName(Display paramDisplay)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      String str = (String)paramDisplay.getClass().getMethod("getName", new Class[0]).invoke(paramDisplay, new Object[0]);
      localStringBuilder.append(paramDisplay.getDisplayId()).append(".name=").append(str).append('\n');
      label58:
      return localStringBuilder.toString();
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label58;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label58;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label58;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label58;
    }
    catch (SecurityException localSecurityException)
    {
      break label58;
    }
  }
  
  private static Object collectRectSize(Display paramDisplay)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      Object localObject1 = paramDisplay.getClass();
      Object localObject2 = new Class[1];
      localObject2[0] = Rect.class;
      localObject2 = ((Class)localObject1).getMethod("getRectSize", (Class[])localObject2);
      localObject1 = new Rect();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localObject1;
      ((Method)localObject2).invoke(paramDisplay, arrayOfObject);
      localStringBuilder.append(paramDisplay.getDisplayId()).append(".rectSize=[").append(((Rect)localObject1).top).append(',').append(((Rect)localObject1).left).append(',').append(((Rect)localObject1).width()).append(',').append(((Rect)localObject1).height()).append(']').append('\n');
      label128:
      return localStringBuilder.toString();
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label128;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label128;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label128;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label128;
    }
    catch (SecurityException localSecurityException)
    {
      break label128;
    }
  }
  
  private static Object collectRotation(Display paramDisplay)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      int i = ((Integer)paramDisplay.getClass().getMethod("getRotation", new Class[0]).invoke(paramDisplay, new Object[0])).intValue();
      localStringBuilder.append(paramDisplay.getDisplayId()).append(".rotation=");
      switch (i)
      {
      default: 
        localStringBuilder.append(i);
      }
      for (;;)
      {
        localStringBuilder.append('\n');
        label97:
        return localStringBuilder.toString();
        localStringBuilder.append("ROTATION_0");
        continue;
        localStringBuilder.append("ROTATION_90");
        continue;
        localStringBuilder.append("ROTATION_180");
        continue;
        localStringBuilder.append("ROTATION_270");
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label97;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label97;
    }
    catch (SecurityException localSecurityException)
    {
      break label97;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label97;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label97;
    }
  }
  
  private static Object collectSize(Display paramDisplay, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      Object localObject2 = paramDisplay.getClass();
      Object localObject1 = new Class[1];
      localObject1[0] = Point.class;
      localObject2 = ((Class)localObject2).getMethod(paramString, (Class[])localObject1);
      localObject1 = new Point();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localObject1;
      ((Method)localObject2).invoke(paramDisplay, arrayOfObject);
      localStringBuilder.append(paramDisplay.getDisplayId()).append('.').append(paramString).append("=[").append(((Point)localObject1).x).append(',').append(((Point)localObject1).y).append(']').append('\n');
      label114:
      return localStringBuilder.toString();
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label114;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label114;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label114;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label114;
    }
    catch (SecurityException localSecurityException)
    {
      break label114;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.DisplayManagerCollector
 * JD-Core Version:    0.7.0.1
 */