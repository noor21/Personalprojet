package org.acra.collector;

public class ThreadCollector
{
  public static String collect(Thread paramThread)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramThread == null)
    {
      localStringBuilder.append("No broken thread, this might be a silent exception.");
    }
    else
    {
      localStringBuilder.append("id=").append(paramThread.getId()).append("\n");
      localStringBuilder.append("name=").append(paramThread.getName()).append("\n");
      localStringBuilder.append("priority=").append(paramThread.getPriority()).append("\n");
      if (paramThread.getThreadGroup() != null) {
        localStringBuilder.append("groupName=").append(paramThread.getThreadGroup().getName()).append("\n");
      }
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.ThreadCollector
 * JD-Core Version:    0.7.0.1
 */