package org.acra;

final class CrashReportFileNameParser
{
  public boolean isApproved(String paramString)
  {
    boolean bool;
    if ((!isSilent(paramString)) && (!paramString.contains("-approved"))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isSilent(String paramString)
  {
    return paramString.contains(ACRAConstants.SILENT_SUFFIX);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.CrashReportFileNameParser
 * JD-Core Version:    0.7.0.1
 */