package org.acra;

public enum ReportingInteractionMode
{
  static
  {
    NOTIFICATION = new ReportingInteractionMode("NOTIFICATION", 1);
    TOAST = new ReportingInteractionMode("TOAST", 2);
    DIALOG = new ReportingInteractionMode("DIALOG", 3);
    ReportingInteractionMode[] arrayOfReportingInteractionMode = new ReportingInteractionMode[4];
    arrayOfReportingInteractionMode[0] = SILENT;
    arrayOfReportingInteractionMode[1] = NOTIFICATION;
    arrayOfReportingInteractionMode[2] = TOAST;
    arrayOfReportingInteractionMode[3] = DIALOG;
    $VALUES = arrayOfReportingInteractionMode;
  }
  
  private ReportingInteractionMode() {}
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.ReportingInteractionMode
 * JD-Core Version:    0.7.0.1
 */