package com.kharybdis.hitchernet;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen
  extends Activity
{
  public static final String IS_FIRST_START = "isFirstStart";
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().requestFeature(8);
    getActionBar().hide();
    final Preferences localPreferences = new Preferences(this);
    startService(new Intent(this, FileTransferService.class));
    setContentView(2130903074);
    new Thread()
    {
      /* Error */
      public void run()
      {
        // Byte code:
        //   0: iconst_0
        //   1: istore_1
        //   2: iload_1
        //   3: sipush 1000
        //   6: if_icmplt +46 -> 52
        //   9: aload_0
        //   10: getfield 17	com/kharybdis/hitchernet/SplashScreen$1:this$0	Lcom/kharybdis/hitchernet/SplashScreen;
        //   13: invokevirtual 28	com/kharybdis/hitchernet/SplashScreen:finish	()V
        //   16: new 30	android/content/Intent
        //   19: dup
        //   20: invokespecial 31	android/content/Intent:<init>	()V
        //   23: astore_1
        //   24: aload_0
        //   25: getfield 19	com/kharybdis/hitchernet/SplashScreen$1:val$prefs	Lcom/kharybdis/hitchernet/Preferences;
        //   28: invokevirtual 37	com/kharybdis/hitchernet/Preferences:isFirstStart	()Z
        //   31: ifne +164 -> 195
        //   34: aload_1
        //   35: ldc 39
        //   37: ldc 41
        //   39: invokevirtual 45	android/content/Intent:setClassName	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //   42: pop
        //   43: aload_0
        //   44: getfield 17	com/kharybdis/hitchernet/SplashScreen$1:this$0	Lcom/kharybdis/hitchernet/SplashScreen;
        //   47: aload_1
        //   48: invokevirtual 49	com/kharybdis/hitchernet/SplashScreen:startActivity	(Landroid/content/Intent;)V
        //   51: return
        //   52: ldc2_w 50
        //   55: invokestatic 55	com/kharybdis/hitchernet/SplashScreen$1:sleep	(J)V
        //   58: iinc 1 100
        //   61: goto -59 -> 2
        //   64: pop
        //   65: aload_0
        //   66: getfield 17	com/kharybdis/hitchernet/SplashScreen$1:this$0	Lcom/kharybdis/hitchernet/SplashScreen;
        //   69: invokevirtual 28	com/kharybdis/hitchernet/SplashScreen:finish	()V
        //   72: new 30	android/content/Intent
        //   75: dup
        //   76: invokespecial 31	android/content/Intent:<init>	()V
        //   79: astore_1
        //   80: aload_0
        //   81: getfield 19	com/kharybdis/hitchernet/SplashScreen$1:val$prefs	Lcom/kharybdis/hitchernet/Preferences;
        //   84: invokevirtual 37	com/kharybdis/hitchernet/Preferences:isFirstStart	()Z
        //   87: ifne +23 -> 110
        //   90: aload_1
        //   91: ldc 39
        //   93: ldc 41
        //   95: invokevirtual 45	android/content/Intent:setClassName	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //   98: pop
        //   99: aload_0
        //   100: getfield 17	com/kharybdis/hitchernet/SplashScreen$1:this$0	Lcom/kharybdis/hitchernet/SplashScreen;
        //   103: aload_1
        //   104: invokevirtual 49	com/kharybdis/hitchernet/SplashScreen:startActivity	(Landroid/content/Intent;)V
        //   107: goto -56 -> 51
        //   110: aload_1
        //   111: ldc 39
        //   113: ldc 57
        //   115: invokevirtual 45	android/content/Intent:setClassName	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //   118: pop
        //   119: aload_1
        //   120: ldc 58
        //   122: iconst_1
        //   123: invokevirtual 62	android/content/Intent:putExtra	(Ljava/lang/String;Z)Landroid/content/Intent;
        //   126: pop
        //   127: goto -28 -> 99
        //   130: astore_1
        //   131: aload_0
        //   132: getfield 17	com/kharybdis/hitchernet/SplashScreen$1:this$0	Lcom/kharybdis/hitchernet/SplashScreen;
        //   135: invokevirtual 28	com/kharybdis/hitchernet/SplashScreen:finish	()V
        //   138: new 30	android/content/Intent
        //   141: dup
        //   142: invokespecial 31	android/content/Intent:<init>	()V
        //   145: astore_2
        //   146: aload_0
        //   147: getfield 19	com/kharybdis/hitchernet/SplashScreen$1:val$prefs	Lcom/kharybdis/hitchernet/Preferences;
        //   150: invokevirtual 37	com/kharybdis/hitchernet/Preferences:isFirstStart	()Z
        //   153: ifne +22 -> 175
        //   156: aload_2
        //   157: ldc 39
        //   159: ldc 41
        //   161: invokevirtual 45	android/content/Intent:setClassName	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //   164: pop
        //   165: aload_0
        //   166: getfield 17	com/kharybdis/hitchernet/SplashScreen$1:this$0	Lcom/kharybdis/hitchernet/SplashScreen;
        //   169: aload_2
        //   170: invokevirtual 49	com/kharybdis/hitchernet/SplashScreen:startActivity	(Landroid/content/Intent;)V
        //   173: aload_1
        //   174: athrow
        //   175: aload_2
        //   176: ldc 39
        //   178: ldc 57
        //   180: invokevirtual 45	android/content/Intent:setClassName	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //   183: pop
        //   184: aload_2
        //   185: ldc 58
        //   187: iconst_1
        //   188: invokevirtual 62	android/content/Intent:putExtra	(Ljava/lang/String;Z)Landroid/content/Intent;
        //   191: pop
        //   192: goto -27 -> 165
        //   195: aload_1
        //   196: ldc 39
        //   198: ldc 57
        //   200: invokevirtual 45	android/content/Intent:setClassName	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //   203: pop
        //   204: aload_1
        //   205: ldc 58
        //   207: iconst_1
        //   208: invokevirtual 62	android/content/Intent:putExtra	(Ljava/lang/String;Z)Landroid/content/Intent;
        //   211: pop
        //   212: goto -169 -> 43
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	215	0	this	1
        //   1	6	1	i	int
        //   23	97	1	localIntent1	Intent
        //   130	75	1	localObject	java.lang.Object
        //   145	40	2	localIntent2	Intent
        //   64	1	5	localInterruptedException	java.lang.InterruptedException
        // Exception table:
        //   from	to	target	type
        //   52	58	64	java/lang/InterruptedException
        //   52	58	130	finally
      }
    }.start();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.SplashScreen
 * JD-Core Version:    0.7.0.1
 */