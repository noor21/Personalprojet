package org.acra.log;

public abstract interface ACRALog
{
  public abstract int d(String paramString1, String paramString2);
  
  public abstract int d(String paramString1, String paramString2, Throwable paramThrowable);
  
  public abstract int e(String paramString1, String paramString2);
  
  public abstract int e(String paramString1, String paramString2, Throwable paramThrowable);
  
  public abstract String getStackTraceString(Throwable paramThrowable);
  
  public abstract int i(String paramString1, String paramString2);
  
  public abstract int i(String paramString1, String paramString2, Throwable paramThrowable);
  
  public abstract int v(String paramString1, String paramString2);
  
  public abstract int v(String paramString1, String paramString2, Throwable paramThrowable);
  
  public abstract int w(String paramString1, String paramString2);
  
  public abstract int w(String paramString1, String paramString2, Throwable paramThrowable);
  
  public abstract int w(String paramString, Throwable paramThrowable);
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.log.ACRALog
 * JD-Core Version:    0.7.0.1
 */