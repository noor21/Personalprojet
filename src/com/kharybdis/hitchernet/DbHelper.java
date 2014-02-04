package com.kharybdis.hitchernet;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.HashMap;

public class DbHelper
  extends SQLiteOpenHelper
{
  public static final String CREATED_AT = "createdAt";
  private static String DB_NAME = "hitchernet";
  private static int DB_VERSION = 6;
  public static final String DEVICE = "deviceName";
  public static final String FILENAME = "fileName";
  public static final String FILEPATH = "filePath";
  public static final String ID = "_ID";
  public static final String IN_INBOX = "in_inbox";
  public static final String PROGRESS_PERCENT = "progressPercent";
  public static final String SIZE = "size";
  public static final String SPEED_MBPS = "speedMbps";
  private static final String TAG = "DbHelper";
  private static final String TBL_TRANSFERS = "transfers";
  public static final String TIME_REMAINING = "timeRemaining";
  private static DbHelper dbHelper;
  
  private DbHelper(Context paramContext)
  {
    super(paramContext, DB_NAME, null, DB_VERSION);
  }
  
  /* Error */
  /**
   * @deprecated
   */
  private boolean deleteAllFromTable(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_3
    //   4: aload_0
    //   5: invokevirtual 68	com/kharybdis/hitchernet/DbHelper:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   8: astore_2
    //   9: new 70	java/lang/StringBuilder
    //   12: dup
    //   13: ldc 72
    //   15: invokespecial 75	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   18: aload_1
    //   19: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc 81
    //   24: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   30: astore 4
    //   32: aload_2
    //   33: aload 4
    //   35: invokevirtual 90	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   38: iconst_1
    //   39: istore_3
    //   40: aload_2
    //   41: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   44: aload_0
    //   45: monitorexit
    //   46: iload_3
    //   47: ireturn
    //   48: astore 4
    //   50: ldc 39
    //   52: aload 4
    //   54: invokevirtual 94	android/database/SQLException:toString	()Ljava/lang/String;
    //   57: invokestatic 100	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   60: pop
    //   61: aload_2
    //   62: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   65: goto -21 -> 44
    //   68: astore_2
    //   69: aload_0
    //   70: monitorexit
    //   71: aload_2
    //   72: athrow
    //   73: astore_3
    //   74: aload_2
    //   75: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   78: aload_3
    //   79: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	80	0	this	DbHelper
    //   0	80	1	paramString	String
    //   8	54	2	localSQLiteDatabase	SQLiteDatabase
    //   68	7	2	localObject1	Object
    //   3	44	3	bool	boolean
    //   73	6	3	localObject2	Object
    //   30	4	4	str	String
    //   48	5	4	localSQLException	SQLException
    // Exception table:
    //   from	to	target	type
    //   32	38	48	android/database/SQLException
    //   4	32	68	finally
    //   40	44	68	finally
    //   61	65	68	finally
    //   74	80	68	finally
    //   32	38	73	finally
    //   50	61	73	finally
  }
  
  public static DbHelper getInstance(Context paramContext)
  {
    if (dbHelper == null) {
      dbHelper = new DbHelper(paramContext);
    }
    return dbHelper;
  }
  
  public boolean deleteCategorySortedData()
  {
    return deleteAllFromTable("transfers");
  }
  
  public void deleteItemWithId(String paramString)
  {
    Log.v("DbHelper", "ready to delete");
    long l = Long.parseLong(paramString);
    SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
    String str = "DELETE FROM transfers WHERE _ID =" + l;
    Log.v("DbHelper", str);
    try
    {
      localSQLiteDatabase.execSQL(str);
      return;
    }
    catch (SQLException localSQLException)
    {
      for (;;)
      {
        Log.e("DbHelper", localSQLException.toString());
        localSQLiteDatabase.close();
      }
    }
    finally
    {
      localSQLiteDatabase.close();
    }
  }
  
  /* Error */
  /**
   * @deprecated
   */
  public java.util.List<HashMap<String, String>> getData(boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new 131	java/util/ArrayList
    //   5: dup
    //   6: invokespecial 133	java/util/ArrayList:<init>	()V
    //   9: astore 4
    //   11: bipush 10
    //   13: anewarray 135	java/lang/String
    //   16: astore 6
    //   18: aload 6
    //   20: iconst_0
    //   21: ldc 24
    //   23: aastore
    //   24: aload 6
    //   26: iconst_1
    //   27: ldc 18
    //   29: aastore
    //   30: aload 6
    //   32: iconst_2
    //   33: ldc 21
    //   35: aastore
    //   36: aload 6
    //   38: iconst_3
    //   39: ldc 33
    //   41: aastore
    //   42: aload 6
    //   44: iconst_4
    //   45: ldc 8
    //   47: aastore
    //   48: aload 6
    //   50: iconst_5
    //   51: ldc 27
    //   53: aastore
    //   54: aload 6
    //   56: bipush 6
    //   58: ldc 30
    //   60: aastore
    //   61: aload 6
    //   63: bipush 7
    //   65: ldc 15
    //   67: aastore
    //   68: aload 6
    //   70: bipush 8
    //   72: ldc 36
    //   74: aastore
    //   75: aload 6
    //   77: bipush 9
    //   79: ldc 45
    //   81: aastore
    //   82: iload_1
    //   83: ifeq +57 -> 140
    //   86: ldc 137
    //   88: astore 5
    //   90: aload_0
    //   91: invokevirtual 140	com/kharybdis/hitchernet/DbHelper:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   94: astore_2
    //   95: aconst_null
    //   96: astore_3
    //   97: aload_2
    //   98: ldc 42
    //   100: aload 6
    //   102: aload 5
    //   104: aconst_null
    //   105: aconst_null
    //   106: aconst_null
    //   107: aconst_null
    //   108: invokevirtual 144	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   111: astore_3
    //   112: aload_3
    //   113: invokeinterface 149 1 0
    //   118: istore 5
    //   120: iload 5
    //   122: ifne +25 -> 147
    //   125: aload_3
    //   126: invokeinterface 150 1 0
    //   131: aload_2
    //   132: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   135: aload_0
    //   136: monitorexit
    //   137: aload 4
    //   139: areturn
    //   140: ldc 152
    //   142: astore 5
    //   144: goto -54 -> 90
    //   147: new 154	java/util/HashMap
    //   150: dup
    //   151: invokespecial 155	java/util/HashMap:<init>	()V
    //   154: astore 5
    //   156: aload 5
    //   158: ldc 24
    //   160: aload_3
    //   161: aload_3
    //   162: ldc 24
    //   164: invokeinterface 159 2 0
    //   169: invokeinterface 163 2 0
    //   174: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   177: pop
    //   178: aload 5
    //   180: ldc 18
    //   182: aload_3
    //   183: aload_3
    //   184: ldc 18
    //   186: invokeinterface 159 2 0
    //   191: invokeinterface 163 2 0
    //   196: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   199: pop
    //   200: aload 5
    //   202: ldc 21
    //   204: aload_3
    //   205: aload_3
    //   206: ldc 21
    //   208: invokeinterface 159 2 0
    //   213: invokeinterface 163 2 0
    //   218: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   221: pop
    //   222: aload 5
    //   224: ldc 33
    //   226: aload_3
    //   227: aload_3
    //   228: ldc 33
    //   230: invokeinterface 159 2 0
    //   235: invokeinterface 163 2 0
    //   240: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   243: pop
    //   244: aload 5
    //   246: ldc 8
    //   248: aload_3
    //   249: aload_3
    //   250: ldc 8
    //   252: invokeinterface 159 2 0
    //   257: invokeinterface 163 2 0
    //   262: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   265: pop
    //   266: aload 5
    //   268: ldc 27
    //   270: aload_3
    //   271: aload_3
    //   272: ldc 27
    //   274: invokeinterface 159 2 0
    //   279: invokeinterface 163 2 0
    //   284: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   287: pop
    //   288: aload 5
    //   290: ldc 30
    //   292: aload_3
    //   293: aload_3
    //   294: ldc 30
    //   296: invokeinterface 159 2 0
    //   301: invokeinterface 163 2 0
    //   306: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   309: pop
    //   310: aload 5
    //   312: ldc 15
    //   314: aload_3
    //   315: aload_3
    //   316: ldc 15
    //   318: invokeinterface 159 2 0
    //   323: invokeinterface 163 2 0
    //   328: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   331: pop
    //   332: aload 5
    //   334: ldc 36
    //   336: aload_3
    //   337: aload_3
    //   338: ldc 36
    //   340: invokeinterface 159 2 0
    //   345: invokeinterface 163 2 0
    //   350: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   353: pop
    //   354: aload 5
    //   356: ldc 45
    //   358: aload_3
    //   359: aload_3
    //   360: ldc 45
    //   362: invokeinterface 159 2 0
    //   367: invokeinterface 163 2 0
    //   372: invokevirtual 167	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   375: pop
    //   376: aload 4
    //   378: aload 5
    //   380: invokeinterface 173 2 0
    //   385: pop
    //   386: goto -274 -> 112
    //   389: astore 4
    //   391: ldc 39
    //   393: aload 4
    //   395: invokevirtual 94	android/database/SQLException:toString	()Ljava/lang/String;
    //   398: invokestatic 100	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   401: pop
    //   402: aconst_null
    //   403: astore 4
    //   405: aload_3
    //   406: invokeinterface 150 1 0
    //   411: aload_2
    //   412: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   415: goto -280 -> 135
    //   418: astore_2
    //   419: aload_0
    //   420: monitorexit
    //   421: aload_2
    //   422: athrow
    //   423: astore 4
    //   425: aload_3
    //   426: invokeinterface 150 1 0
    //   431: aload_2
    //   432: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   435: aload 4
    //   437: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	438	0	this	DbHelper
    //   0	438	1	paramBoolean	boolean
    //   94	318	2	localSQLiteDatabase	SQLiteDatabase
    //   418	14	2	localObject1	Object
    //   96	330	3	localCursor	android.database.Cursor
    //   9	368	4	localArrayList	java.util.ArrayList
    //   389	5	4	localSQLException	SQLException
    //   403	1	4	localObject2	Object
    //   423	13	4	localObject3	Object
    //   88	15	5	str	String
    //   118	3	5	bool	boolean
    //   142	237	5	localObject4	Object
    //   16	85	6	arrayOfString	String[]
    // Exception table:
    //   from	to	target	type
    //   97	120	389	android/database/SQLException
    //   147	386	389	android/database/SQLException
    //   2	95	418	finally
    //   125	135	418	finally
    //   140	144	418	finally
    //   405	415	418	finally
    //   425	438	418	finally
    //   97	120	423	finally
    //   147	386	423	finally
    //   391	402	423	finally
  }
  
  public void insert(boolean paramBoolean)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("fileName", "Short Movie.avi");
    localHashMap.put("size", "250MB");
    localHashMap.put("createdAt", "2012-12-12 12:12:12");
    String str;
    if (!paramBoolean) {
      str = "0";
    } else {
      str = "1";
    }
    localHashMap.put("in_inbox", str);
    localHashMap.put("progressPercent", "100");
    insertData(localHashMap);
  }
  
  /* Error */
  /**
   * @deprecated
   */
  public long insertData(HashMap<String, String> paramHashMap)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc2_w 192
    //   5: lstore_3
    //   6: aload_0
    //   7: invokevirtual 68	com/kharybdis/hitchernet/DbHelper:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   10: astore_2
    //   11: new 195	android/content/ContentValues
    //   14: dup
    //   15: invokespecial 196	android/content/ContentValues:<init>	()V
    //   18: astore 5
    //   20: aload 5
    //   22: invokevirtual 199	android/content/ContentValues:clear	()V
    //   25: aload 5
    //   27: ldc 18
    //   29: aload_1
    //   30: ldc 18
    //   32: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   35: checkcast 135	java/lang/String
    //   38: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   41: aload 5
    //   43: ldc 33
    //   45: aload_1
    //   46: ldc 33
    //   48: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   51: checkcast 135	java/lang/String
    //   54: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   57: aload 5
    //   59: ldc 30
    //   61: aload_1
    //   62: ldc 30
    //   64: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   67: checkcast 135	java/lang/String
    //   70: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   73: aload 5
    //   75: ldc 27
    //   77: aload_1
    //   78: ldc 27
    //   80: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   83: checkcast 135	java/lang/String
    //   86: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   89: aload 5
    //   91: ldc 8
    //   93: aload_1
    //   94: ldc 8
    //   96: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   99: checkcast 135	java/lang/String
    //   102: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   105: aload 5
    //   107: ldc 15
    //   109: aload_1
    //   110: ldc 15
    //   112: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   115: checkcast 135	java/lang/String
    //   118: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   121: aload 5
    //   123: ldc 36
    //   125: aload_1
    //   126: ldc 36
    //   128: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   131: checkcast 135	java/lang/String
    //   134: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   137: aload 5
    //   139: ldc 45
    //   141: aload_1
    //   142: ldc 45
    //   144: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   147: checkcast 135	java/lang/String
    //   150: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   153: aload 5
    //   155: ldc 21
    //   157: aload_1
    //   158: ldc 21
    //   160: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   163: checkcast 135	java/lang/String
    //   166: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   169: aload_2
    //   170: ldc 42
    //   172: aconst_null
    //   173: aload 5
    //   175: invokevirtual 210	android/database/sqlite/SQLiteDatabase:insertOrThrow	(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   178: lstore_3
    //   179: lload_3
    //   180: lstore_3
    //   181: aload_2
    //   182: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   185: aload_0
    //   186: monitorexit
    //   187: lload_3
    //   188: lreturn
    //   189: astore 5
    //   191: ldc 39
    //   193: ldc 212
    //   195: invokestatic 215	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   198: pop
    //   199: ldc 39
    //   201: aload 5
    //   203: invokevirtual 94	android/database/SQLException:toString	()Ljava/lang/String;
    //   206: invokestatic 215	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   209: pop
    //   210: aload_2
    //   211: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   214: goto -29 -> 185
    //   217: astore_2
    //   218: aload_0
    //   219: monitorexit
    //   220: aload_2
    //   221: athrow
    //   222: astore_3
    //   223: aload_2
    //   224: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   227: aload_3
    //   228: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	229	0	this	DbHelper
    //   0	229	1	paramHashMap	HashMap<String, String>
    //   10	201	2	localSQLiteDatabase	SQLiteDatabase
    //   217	7	2	localObject1	Object
    //   5	183	3	l	long
    //   222	6	3	localObject2	Object
    //   18	156	5	localContentValues	android.content.ContentValues
    //   189	13	5	localSQLException	SQLException
    // Exception table:
    //   from	to	target	type
    //   11	179	189	android/database/SQLException
    //   6	11	217	finally
    //   181	185	217	finally
    //   210	214	217	finally
    //   223	229	217	finally
    //   11	179	222	finally
    //   191	210	222	finally
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE transfers( _ID INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL, fileName text, filePath text, size text, deviceName text, createdAt text, progressPercent integer, speedMbps text, timeRemaining text, in_inbox integer)");
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    paramSQLiteDatabase.execSQL("drop table if exists transfers");
    onCreate(paramSQLiteDatabase);
    Log.d("DbHelper", "re create");
  }
  
  /* Error */
  /**
   * @deprecated
   */
  public void updateRowWithId(long paramLong, int paramInt, float paramFloat, String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new 195	android/content/ContentValues
    //   5: dup
    //   6: invokespecial 196	android/content/ContentValues:<init>	()V
    //   9: astore 7
    //   11: aload 7
    //   13: invokevirtual 199	android/content/ContentValues:clear	()V
    //   16: aload 7
    //   18: ldc 30
    //   20: iload_3
    //   21: invokestatic 235	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   24: invokevirtual 238	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   27: aload 7
    //   29: ldc 36
    //   31: fload 4
    //   33: invokestatic 243	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   36: invokevirtual 246	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Float;)V
    //   39: aload 7
    //   41: ldc 45
    //   43: aload 5
    //   45: invokevirtual 206	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   48: new 70	java/lang/StringBuilder
    //   51: dup
    //   52: ldc 248
    //   54: invokespecial 75	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   57: lload_1
    //   58: invokevirtual 127	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   61: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   64: astore 8
    //   66: aload_0
    //   67: invokevirtual 68	com/kharybdis/hitchernet/DbHelper:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   70: astore 6
    //   72: aload 6
    //   74: ldc 42
    //   76: aload 7
    //   78: aload 8
    //   80: aconst_null
    //   81: invokevirtual 252	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   84: pop
    //   85: aload 6
    //   87: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   90: aload_0
    //   91: monitorexit
    //   92: return
    //   93: astore 7
    //   95: aload 7
    //   97: invokevirtual 255	android/database/SQLException:printStackTrace	()V
    //   100: aload 6
    //   102: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   105: goto -15 -> 90
    //   108: astore 6
    //   110: aload_0
    //   111: monitorexit
    //   112: aload 6
    //   114: athrow
    //   115: astore 7
    //   117: aload 6
    //   119: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   122: aload 7
    //   124: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	125	0	this	DbHelper
    //   0	125	1	paramLong	long
    //   0	125	3	paramInt	int
    //   0	125	4	paramFloat	float
    //   0	125	5	paramString	String
    //   70	31	6	localSQLiteDatabase	SQLiteDatabase
    //   108	10	6	localObject1	Object
    //   9	68	7	localContentValues	android.content.ContentValues
    //   93	3	7	localSQLException	SQLException
    //   115	8	7	localObject2	Object
    //   64	15	8	str	String
    // Exception table:
    //   from	to	target	type
    //   72	85	93	android/database/SQLException
    //   2	72	108	finally
    //   85	90	108	finally
    //   100	105	108	finally
    //   117	125	108	finally
    //   72	85	115	finally
    //   95	100	115	finally
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.DbHelper
 * JD-Core Version:    0.7.0.1
 */