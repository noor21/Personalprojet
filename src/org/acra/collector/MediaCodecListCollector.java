package org.acra.collector;

import android.util.SparseArray;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MediaCodecListCollector
{
  private static final String[] AAC_TYPES;
  private static final String[] AVC_TYPES;
  private static final String COLOR_FORMAT_PREFIX = "COLOR_";
  private static final String[] H263_TYPES;
  private static final String[] MPEG4_TYPES;
  private static Class<?> codecCapabilitiesClass;
  private static Field colorFormatsField;
  private static Method getCapabilitiesForTypeMethod;
  private static Method getCodecInfoAtMethod;
  private static Method getNameMethod;
  private static Method getSupportedTypesMethod;
  private static Method isEncoderMethod;
  private static Field levelField;
  private static SparseArray<String> mAACProfileValues;
  private static SparseArray<String> mAVCLevelValues;
  private static SparseArray<String> mAVCProfileValues;
  private static SparseArray<String> mColorFormatValues;
  private static SparseArray<String> mH263LevelValues;
  private static SparseArray<String> mH263ProfileValues;
  private static SparseArray<String> mMPEG4LevelValues;
  private static SparseArray<String> mMPEG4ProfileValues;
  private static Class<?> mediaCodecInfoClass;
  private static Class<?> mediaCodecListClass;
  private static Field profileField;
  private static Field profileLevelsField;
  
  /* Error */
  static
  {
    // Byte code:
    //   0: iconst_4
    //   1: anewarray 60	java/lang/String
    //   4: astore_0
    //   5: aload_0
    //   6: iconst_0
    //   7: ldc 62
    //   9: aastore
    //   10: aload_0
    //   11: iconst_1
    //   12: ldc 64
    //   14: aastore
    //   15: aload_0
    //   16: iconst_2
    //   17: ldc 66
    //   19: aastore
    //   20: aload_0
    //   21: iconst_3
    //   22: ldc 68
    //   24: aastore
    //   25: aload_0
    //   26: putstatic 70	org/acra/collector/MediaCodecListCollector:MPEG4_TYPES	[Ljava/lang/String;
    //   29: iconst_4
    //   30: anewarray 60	java/lang/String
    //   33: astore_0
    //   34: aload_0
    //   35: iconst_0
    //   36: ldc 72
    //   38: aastore
    //   39: aload_0
    //   40: iconst_1
    //   41: ldc 74
    //   43: aastore
    //   44: aload_0
    //   45: iconst_2
    //   46: ldc 76
    //   48: aastore
    //   49: aload_0
    //   50: iconst_3
    //   51: ldc 78
    //   53: aastore
    //   54: aload_0
    //   55: putstatic 80	org/acra/collector/MediaCodecListCollector:AVC_TYPES	[Ljava/lang/String;
    //   58: iconst_2
    //   59: anewarray 60	java/lang/String
    //   62: astore_0
    //   63: aload_0
    //   64: iconst_0
    //   65: ldc 82
    //   67: aastore
    //   68: aload_0
    //   69: iconst_1
    //   70: ldc 84
    //   72: aastore
    //   73: aload_0
    //   74: putstatic 86	org/acra/collector/MediaCodecListCollector:H263_TYPES	[Ljava/lang/String;
    //   77: iconst_2
    //   78: anewarray 60	java/lang/String
    //   81: astore_0
    //   82: aload_0
    //   83: iconst_0
    //   84: ldc 88
    //   86: aastore
    //   87: aload_0
    //   88: iconst_1
    //   89: ldc 90
    //   91: aastore
    //   92: aload_0
    //   93: putstatic 92	org/acra/collector/MediaCodecListCollector:AAC_TYPES	[Ljava/lang/String;
    //   96: aconst_null
    //   97: putstatic 94	org/acra/collector/MediaCodecListCollector:mediaCodecListClass	Ljava/lang/Class;
    //   100: aconst_null
    //   101: putstatic 96	org/acra/collector/MediaCodecListCollector:getCodecInfoAtMethod	Ljava/lang/reflect/Method;
    //   104: aconst_null
    //   105: putstatic 98	org/acra/collector/MediaCodecListCollector:mediaCodecInfoClass	Ljava/lang/Class;
    //   108: aconst_null
    //   109: putstatic 100	org/acra/collector/MediaCodecListCollector:getNameMethod	Ljava/lang/reflect/Method;
    //   112: aconst_null
    //   113: putstatic 102	org/acra/collector/MediaCodecListCollector:isEncoderMethod	Ljava/lang/reflect/Method;
    //   116: aconst_null
    //   117: putstatic 104	org/acra/collector/MediaCodecListCollector:getSupportedTypesMethod	Ljava/lang/reflect/Method;
    //   120: aconst_null
    //   121: putstatic 106	org/acra/collector/MediaCodecListCollector:getCapabilitiesForTypeMethod	Ljava/lang/reflect/Method;
    //   124: aconst_null
    //   125: putstatic 108	org/acra/collector/MediaCodecListCollector:codecCapabilitiesClass	Ljava/lang/Class;
    //   128: aconst_null
    //   129: putstatic 110	org/acra/collector/MediaCodecListCollector:colorFormatsField	Ljava/lang/reflect/Field;
    //   132: aconst_null
    //   133: putstatic 112	org/acra/collector/MediaCodecListCollector:profileLevelsField	Ljava/lang/reflect/Field;
    //   136: aconst_null
    //   137: putstatic 114	org/acra/collector/MediaCodecListCollector:profileField	Ljava/lang/reflect/Field;
    //   140: aconst_null
    //   141: putstatic 116	org/acra/collector/MediaCodecListCollector:levelField	Ljava/lang/reflect/Field;
    //   144: new 118	android/util/SparseArray
    //   147: dup
    //   148: invokespecial 121	android/util/SparseArray:<init>	()V
    //   151: putstatic 123	org/acra/collector/MediaCodecListCollector:mColorFormatValues	Landroid/util/SparseArray;
    //   154: new 118	android/util/SparseArray
    //   157: dup
    //   158: invokespecial 121	android/util/SparseArray:<init>	()V
    //   161: putstatic 125	org/acra/collector/MediaCodecListCollector:mAVCLevelValues	Landroid/util/SparseArray;
    //   164: new 118	android/util/SparseArray
    //   167: dup
    //   168: invokespecial 121	android/util/SparseArray:<init>	()V
    //   171: putstatic 127	org/acra/collector/MediaCodecListCollector:mAVCProfileValues	Landroid/util/SparseArray;
    //   174: new 118	android/util/SparseArray
    //   177: dup
    //   178: invokespecial 121	android/util/SparseArray:<init>	()V
    //   181: putstatic 129	org/acra/collector/MediaCodecListCollector:mH263LevelValues	Landroid/util/SparseArray;
    //   184: new 118	android/util/SparseArray
    //   187: dup
    //   188: invokespecial 121	android/util/SparseArray:<init>	()V
    //   191: putstatic 131	org/acra/collector/MediaCodecListCollector:mH263ProfileValues	Landroid/util/SparseArray;
    //   194: new 118	android/util/SparseArray
    //   197: dup
    //   198: invokespecial 121	android/util/SparseArray:<init>	()V
    //   201: putstatic 133	org/acra/collector/MediaCodecListCollector:mMPEG4LevelValues	Landroid/util/SparseArray;
    //   204: new 118	android/util/SparseArray
    //   207: dup
    //   208: invokespecial 121	android/util/SparseArray:<init>	()V
    //   211: putstatic 135	org/acra/collector/MediaCodecListCollector:mMPEG4ProfileValues	Landroid/util/SparseArray;
    //   214: new 118	android/util/SparseArray
    //   217: dup
    //   218: invokespecial 121	android/util/SparseArray:<init>	()V
    //   221: putstatic 137	org/acra/collector/MediaCodecListCollector:mAACProfileValues	Landroid/util/SparseArray;
    //   224: ldc 139
    //   226: invokestatic 145	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   229: putstatic 94	org/acra/collector/MediaCodecListCollector:mediaCodecListClass	Ljava/lang/Class;
    //   232: getstatic 94	org/acra/collector/MediaCodecListCollector:mediaCodecListClass	Ljava/lang/Class;
    //   235: astore_0
    //   236: iconst_1
    //   237: anewarray 141	java/lang/Class
    //   240: astore_1
    //   241: aload_1
    //   242: iconst_0
    //   243: getstatic 150	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   246: aastore
    //   247: aload_0
    //   248: ldc 152
    //   250: aload_1
    //   251: invokevirtual 156	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   254: putstatic 96	org/acra/collector/MediaCodecListCollector:getCodecInfoAtMethod	Ljava/lang/reflect/Method;
    //   257: ldc 158
    //   259: invokestatic 145	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   262: putstatic 98	org/acra/collector/MediaCodecListCollector:mediaCodecInfoClass	Ljava/lang/Class;
    //   265: getstatic 98	org/acra/collector/MediaCodecListCollector:mediaCodecInfoClass	Ljava/lang/Class;
    //   268: ldc 160
    //   270: iconst_0
    //   271: anewarray 141	java/lang/Class
    //   274: invokevirtual 156	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   277: putstatic 100	org/acra/collector/MediaCodecListCollector:getNameMethod	Ljava/lang/reflect/Method;
    //   280: getstatic 98	org/acra/collector/MediaCodecListCollector:mediaCodecInfoClass	Ljava/lang/Class;
    //   283: ldc 162
    //   285: iconst_0
    //   286: anewarray 141	java/lang/Class
    //   289: invokevirtual 156	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   292: putstatic 102	org/acra/collector/MediaCodecListCollector:isEncoderMethod	Ljava/lang/reflect/Method;
    //   295: getstatic 98	org/acra/collector/MediaCodecListCollector:mediaCodecInfoClass	Ljava/lang/Class;
    //   298: ldc 164
    //   300: iconst_0
    //   301: anewarray 141	java/lang/Class
    //   304: invokevirtual 156	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   307: putstatic 104	org/acra/collector/MediaCodecListCollector:getSupportedTypesMethod	Ljava/lang/reflect/Method;
    //   310: getstatic 98	org/acra/collector/MediaCodecListCollector:mediaCodecInfoClass	Ljava/lang/Class;
    //   313: astore_1
    //   314: iconst_1
    //   315: anewarray 141	java/lang/Class
    //   318: astore_0
    //   319: aload_0
    //   320: iconst_0
    //   321: ldc 60
    //   323: aastore
    //   324: aload_1
    //   325: ldc 166
    //   327: aload_0
    //   328: invokevirtual 156	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   331: putstatic 106	org/acra/collector/MediaCodecListCollector:getCapabilitiesForTypeMethod	Ljava/lang/reflect/Method;
    //   334: ldc 168
    //   336: invokestatic 145	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   339: putstatic 108	org/acra/collector/MediaCodecListCollector:codecCapabilitiesClass	Ljava/lang/Class;
    //   342: getstatic 108	org/acra/collector/MediaCodecListCollector:codecCapabilitiesClass	Ljava/lang/Class;
    //   345: ldc 170
    //   347: invokevirtual 174	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   350: putstatic 110	org/acra/collector/MediaCodecListCollector:colorFormatsField	Ljava/lang/reflect/Field;
    //   353: getstatic 108	org/acra/collector/MediaCodecListCollector:codecCapabilitiesClass	Ljava/lang/Class;
    //   356: ldc 176
    //   358: invokevirtual 174	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   361: putstatic 112	org/acra/collector/MediaCodecListCollector:profileLevelsField	Ljava/lang/reflect/Field;
    //   364: getstatic 108	org/acra/collector/MediaCodecListCollector:codecCapabilitiesClass	Ljava/lang/Class;
    //   367: invokevirtual 180	java/lang/Class:getFields	()[Ljava/lang/reflect/Field;
    //   370: astore_0
    //   371: aload_0
    //   372: arraylength
    //   373: istore_2
    //   374: iconst_0
    //   375: istore_3
    //   376: iload_3
    //   377: iload_2
    //   378: if_icmpge +57 -> 435
    //   381: aload_0
    //   382: iload_3
    //   383: aaload
    //   384: astore_1
    //   385: aload_1
    //   386: invokevirtual 186	java/lang/reflect/Field:getModifiers	()I
    //   389: invokestatic 192	java/lang/reflect/Modifier:isStatic	(I)Z
    //   392: ifeq +343 -> 735
    //   395: aload_1
    //   396: invokevirtual 186	java/lang/reflect/Field:getModifiers	()I
    //   399: invokestatic 195	java/lang/reflect/Modifier:isFinal	(I)Z
    //   402: ifeq +333 -> 735
    //   405: aload_1
    //   406: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   409: ldc 16
    //   411: invokevirtual 202	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   414: ifeq +321 -> 735
    //   417: getstatic 123	org/acra/collector/MediaCodecListCollector:mColorFormatValues	Landroid/util/SparseArray;
    //   420: aload_1
    //   421: aconst_null
    //   422: invokevirtual 206	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   425: aload_1
    //   426: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   429: invokevirtual 210	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   432: goto +303 -> 735
    //   435: ldc 212
    //   437: invokestatic 145	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   440: astore_2
    //   441: aload_2
    //   442: invokevirtual 180	java/lang/Class:getFields	()[Ljava/lang/reflect/Field;
    //   445: astore_1
    //   446: aload_1
    //   447: arraylength
    //   448: istore_3
    //   449: iconst_0
    //   450: istore_0
    //   451: iload_0
    //   452: iload_3
    //   453: if_icmpge +261 -> 714
    //   456: aload_1
    //   457: iload_0
    //   458: aaload
    //   459: astore 4
    //   461: aload 4
    //   463: invokevirtual 186	java/lang/reflect/Field:getModifiers	()I
    //   466: invokestatic 192	java/lang/reflect/Modifier:isStatic	(I)Z
    //   469: ifeq +272 -> 741
    //   472: aload 4
    //   474: invokevirtual 186	java/lang/reflect/Field:getModifiers	()I
    //   477: invokestatic 195	java/lang/reflect/Modifier:isFinal	(I)Z
    //   480: ifeq +261 -> 741
    //   483: aload 4
    //   485: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   488: ldc 214
    //   490: invokevirtual 202	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   493: ifeq +23 -> 516
    //   496: getstatic 125	org/acra/collector/MediaCodecListCollector:mAVCLevelValues	Landroid/util/SparseArray;
    //   499: aload 4
    //   501: aconst_null
    //   502: invokevirtual 206	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   505: aload 4
    //   507: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   510: invokevirtual 210	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   513: goto +228 -> 741
    //   516: aload 4
    //   518: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   521: ldc 216
    //   523: invokevirtual 202	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   526: ifeq +23 -> 549
    //   529: getstatic 127	org/acra/collector/MediaCodecListCollector:mAVCProfileValues	Landroid/util/SparseArray;
    //   532: aload 4
    //   534: aconst_null
    //   535: invokevirtual 206	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   538: aload 4
    //   540: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   543: invokevirtual 210	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   546: goto +195 -> 741
    //   549: aload 4
    //   551: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   554: ldc 218
    //   556: invokevirtual 202	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   559: ifeq +23 -> 582
    //   562: getstatic 129	org/acra/collector/MediaCodecListCollector:mH263LevelValues	Landroid/util/SparseArray;
    //   565: aload 4
    //   567: aconst_null
    //   568: invokevirtual 206	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   571: aload 4
    //   573: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   576: invokevirtual 210	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   579: goto +162 -> 741
    //   582: aload 4
    //   584: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   587: ldc 220
    //   589: invokevirtual 202	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   592: ifeq +23 -> 615
    //   595: getstatic 131	org/acra/collector/MediaCodecListCollector:mH263ProfileValues	Landroid/util/SparseArray;
    //   598: aload 4
    //   600: aconst_null
    //   601: invokevirtual 206	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   604: aload 4
    //   606: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   609: invokevirtual 210	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   612: goto +129 -> 741
    //   615: aload 4
    //   617: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   620: ldc 222
    //   622: invokevirtual 202	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   625: ifeq +23 -> 648
    //   628: getstatic 133	org/acra/collector/MediaCodecListCollector:mMPEG4LevelValues	Landroid/util/SparseArray;
    //   631: aload 4
    //   633: aconst_null
    //   634: invokevirtual 206	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   637: aload 4
    //   639: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   642: invokevirtual 210	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   645: goto +96 -> 741
    //   648: aload 4
    //   650: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   653: ldc 224
    //   655: invokevirtual 202	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   658: ifeq +23 -> 681
    //   661: getstatic 135	org/acra/collector/MediaCodecListCollector:mMPEG4ProfileValues	Landroid/util/SparseArray;
    //   664: aload 4
    //   666: aconst_null
    //   667: invokevirtual 206	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   670: aload 4
    //   672: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   675: invokevirtual 210	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   678: goto +63 -> 741
    //   681: aload 4
    //   683: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   686: ldc 90
    //   688: invokevirtual 202	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   691: ifeq +50 -> 741
    //   694: getstatic 137	org/acra/collector/MediaCodecListCollector:mAACProfileValues	Landroid/util/SparseArray;
    //   697: aload 4
    //   699: aconst_null
    //   700: invokevirtual 206	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   703: aload 4
    //   705: invokevirtual 198	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   708: invokevirtual 210	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   711: goto +30 -> 741
    //   714: aload_2
    //   715: ldc 226
    //   717: invokevirtual 174	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   720: putstatic 114	org/acra/collector/MediaCodecListCollector:profileField	Ljava/lang/reflect/Field;
    //   723: aload_2
    //   724: ldc 228
    //   726: invokevirtual 174	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   729: putstatic 116	org/acra/collector/MediaCodecListCollector:levelField	Ljava/lang/reflect/Field;
    //   732: goto +16 -> 748
    //   735: iinc 3 1
    //   738: goto -362 -> 376
    //   741: iinc 0 1
    //   744: goto -293 -> 451
    //   747: pop
    //   748: return
    //   749: pop
    //   750: goto -2 -> 748
    //   753: pop
    //   754: goto -6 -> 748
    //   757: pop
    //   758: goto -10 -> 748
    //   761: pop
    //   762: goto -14 -> 748
    //   765: pop
    //   766: goto -18 -> 748
    // Local variable table:
    //   start	length	slot	name	signature
    //   4	378	0	localObject1	Object
    //   450	292	0	i	int
    //   240	217	1	localObject2	Object
    //   373	6	2	j	int
    //   440	284	2	localClass	Class
    //   375	361	3	k	int
    //   459	245	4	localObject3	Object
    //   747	1	7	localClassNotFoundException	java.lang.ClassNotFoundException
    //   749	1	8	localNoSuchMethodException	java.lang.NoSuchMethodException
    //   753	1	9	localIllegalArgumentException	IllegalArgumentException
    //   757	1	10	localIllegalAccessException	IllegalAccessException
    //   761	1	11	localSecurityException	java.lang.SecurityException
    //   765	1	12	localNoSuchFieldException	java.lang.NoSuchFieldException
    // Exception table:
    //   from	to	target	type
    //   224	732	747	java/lang/ClassNotFoundException
    //   224	732	749	java/lang/NoSuchMethodException
    //   224	732	753	java/lang/IllegalArgumentException
    //   224	732	757	java/lang/IllegalAccessException
    //   224	732	761	java/lang/SecurityException
    //   224	732	765	java/lang/NoSuchFieldException
  }
  
  /* Error */
  public static String collecMediaCodecList()
  {
    // Byte code:
    //   0: new 234	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 235	java/lang/StringBuilder:<init>	()V
    //   7: astore_1
    //   8: getstatic 94	org/acra/collector/MediaCodecListCollector:mediaCodecListClass	Ljava/lang/Class;
    //   11: ifnull +226 -> 237
    //   14: getstatic 98	org/acra/collector/MediaCodecListCollector:mediaCodecInfoClass	Ljava/lang/Class;
    //   17: ifnull +220 -> 237
    //   20: getstatic 94	org/acra/collector/MediaCodecListCollector:mediaCodecListClass	Ljava/lang/Class;
    //   23: ldc 237
    //   25: iconst_0
    //   26: anewarray 141	java/lang/Class
    //   29: invokevirtual 156	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   32: aconst_null
    //   33: iconst_0
    //   34: anewarray 4	java/lang/Object
    //   37: invokevirtual 243	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   40: checkcast 147	java/lang/Integer
    //   43: checkcast 147	java/lang/Integer
    //   46: invokevirtual 246	java/lang/Integer:intValue	()I
    //   49: istore_2
    //   50: iconst_0
    //   51: istore_0
    //   52: iload_0
    //   53: iload_2
    //   54: if_icmpge +183 -> 237
    //   57: aload_1
    //   58: ldc 248
    //   60: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: getstatic 96	org/acra/collector/MediaCodecListCollector:getCodecInfoAtMethod	Ljava/lang/reflect/Method;
    //   67: astore 4
    //   69: iconst_1
    //   70: anewarray 4	java/lang/Object
    //   73: astore_3
    //   74: aload_3
    //   75: iconst_0
    //   76: iload_0
    //   77: invokestatic 256	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   80: aastore
    //   81: aload 4
    //   83: aconst_null
    //   84: aload_3
    //   85: invokevirtual 243	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   88: astore 4
    //   90: aload_1
    //   91: iload_0
    //   92: invokevirtual 259	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   95: ldc_w 261
    //   98: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: getstatic 100	org/acra/collector/MediaCodecListCollector:getNameMethod	Ljava/lang/reflect/Method;
    //   104: aload 4
    //   106: iconst_0
    //   107: anewarray 4	java/lang/Object
    //   110: invokevirtual 243	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   113: invokevirtual 264	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   116: ldc 248
    //   118: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: aload_1
    //   123: ldc_w 266
    //   126: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: getstatic 102	org/acra/collector/MediaCodecListCollector:isEncoderMethod	Ljava/lang/reflect/Method;
    //   132: aload 4
    //   134: iconst_0
    //   135: anewarray 4	java/lang/Object
    //   138: invokevirtual 243	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   141: invokevirtual 264	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   144: ldc 248
    //   146: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: getstatic 104	org/acra/collector/MediaCodecListCollector:getSupportedTypesMethod	Ljava/lang/reflect/Method;
    //   153: aload 4
    //   155: iconst_0
    //   156: anewarray 4	java/lang/Object
    //   159: invokevirtual 243	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   162: checkcast 267	[Ljava/lang/String;
    //   165: checkcast 267	[Ljava/lang/String;
    //   168: astore_3
    //   169: aload_1
    //   170: ldc_w 269
    //   173: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: aload_3
    //   177: invokestatic 275	java/util/Arrays:toString	([Ljava/lang/Object;)Ljava/lang/String;
    //   180: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: ldc 248
    //   185: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: pop
    //   189: aload_3
    //   190: arraylength
    //   191: istore 5
    //   193: iconst_0
    //   194: istore 6
    //   196: iload 6
    //   198: iload 5
    //   200: if_icmpge +23 -> 223
    //   203: aload_1
    //   204: aload 4
    //   206: aload_3
    //   207: iload 6
    //   209: aaload
    //   210: invokestatic 279	org/acra/collector/MediaCodecListCollector:collectCapabilitiesForType	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/String;
    //   213: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: iinc 6 1
    //   220: goto -24 -> 196
    //   223: aload_1
    //   224: ldc 248
    //   226: invokevirtual 252	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   229: pop
    //   230: iinc 0 1
    //   233: goto -181 -> 52
    //   236: pop
    //   237: aload_1
    //   238: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   241: areturn
    //   242: pop
    //   243: goto -6 -> 237
    //   246: pop
    //   247: goto -10 -> 237
    // Local variable table:
    //   start	length	slot	name	signature
    //   51	180	0	i	int
    //   7	231	1	localStringBuilder	StringBuilder
    //   49	6	2	j	int
    //   73	134	3	localObject1	Object
    //   67	138	4	localObject2	Object
    //   191	10	5	k	int
    //   194	24	6	m	int
    //   236	1	7	localInvocationTargetException	InvocationTargetException
    //   242	1	8	localIllegalAccessException	IllegalAccessException
    //   246	1	9	localNoSuchMethodException	java.lang.NoSuchMethodException
    // Exception table:
    //   from	to	target	type
    //   20	230	236	java/lang/reflect/InvocationTargetException
    //   20	230	242	java/lang/IllegalAccessException
    //   20	230	246	java/lang/NoSuchMethodException
  }
  
  private static String collectCapabilitiesForType(Object paramObject, String paramString)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject1 = getCapabilitiesForTypeMethod;
    Object localObject2 = new Object[1];
    localObject2[0] = paramString;
    localObject1 = ((Method)localObject1).invoke(paramObject, (Object[])localObject2);
    localObject2 = (int[])colorFormatsField.get(localObject1);
    if (localObject2.length > 0) {
      localStringBuilder.append(paramString).append(" color formats:");
    }
    for (int j = 0;; j++)
    {
      if (j >= localObject2.length)
      {
        localStringBuilder.append("\n");
        localObject2 = (Object[])profileLevelsField.get(localObject1);
        if (localObject2.length > 0) {
          localStringBuilder.append(paramString).append(" profile levels:");
        }
        for (int i = 0;; i++)
        {
          if (i >= localObject2.length)
          {
            localStringBuilder.append("\n");
            return "\n";
          }
          CodecType localCodecType = identifyCodecType(paramObject);
          j = profileField.getInt(localObject2[i]);
          int k = levelField.getInt(localObject2[i]);
          if (localCodecType == null) {
            localStringBuilder.append(j).append('-').append(k);
          }
          switch (1.$SwitchMap$org$acra$collector$MediaCodecListCollector$CodecType[localCodecType.ordinal()])
          {
          case 1: 
            localStringBuilder.append(j).append((String)mAVCProfileValues.get(j)).append('-').append((String)mAVCLevelValues.get(k));
            break;
          case 2: 
            localStringBuilder.append((String)mH263ProfileValues.get(j)).append('-').append((String)mH263LevelValues.get(k));
            break;
          case 3: 
            localStringBuilder.append((String)mMPEG4ProfileValues.get(j)).append('-').append((String)mMPEG4LevelValues.get(k));
            break;
          case 4: 
            localStringBuilder.append((String)mAACProfileValues.get(j));
          }
          if (i < -1 + localObject2.length) {
            localStringBuilder.append(',');
          }
        }
      }
      localStringBuilder.append((String)mColorFormatValues.get(localObject2[j]));
      if (j < -1 + localObject2.length) {
        localStringBuilder.append(',');
      }
    }
  }
  
  private static CodecType identifyCodecType(Object paramObject)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    Object localObject = (String)getNameMethod.invoke(paramObject, new Object[0]);
    String[] arrayOfString2 = AVC_TYPES;
    int i = arrayOfString2.length;
    for (int k = 0;; k++)
    {
      String[] arrayOfString3;
      if (k >= i)
      {
        arrayOfString2 = H263_TYPES;
        i = arrayOfString2.length;
        for (k = 0;; k++)
        {
          if (k >= i)
          {
            String[] arrayOfString1 = MPEG4_TYPES;
            k = arrayOfString1.length;
            for (int m = 0;; arrayOfString3++)
            {
              int j;
              if (m >= k)
              {
                arrayOfString3 = AAC_TYPES;
                k = arrayOfString3.length;
                for (j = 0;; j++)
                {
                  if (j >= k) {
                    return null;
                  }
                  if (((String)localObject).contains(arrayOfString3[j])) {
                    break;
                  }
                }
                return CodecType.AAC;
              }
              if (((String)localObject).contains(j[arrayOfString3])) {
                break;
              }
            }
            return CodecType.MPEG4;
          }
          if (((String)localObject).contains(arrayOfString3[k])) {
            break;
          }
        }
        return CodecType.H263;
      }
      if (((String)localObject).contains(arrayOfString3[k])) {
        break;
      }
    }
    localObject = CodecType.AVC;
    return localObject;
  }
  
  private static enum CodecType
  {
    static
    {
      AAC = new CodecType("AAC", 3);
      CodecType[] arrayOfCodecType = new CodecType[4];
      arrayOfCodecType[0] = AVC;
      arrayOfCodecType[1] = H263;
      arrayOfCodecType[2] = MPEG4;
      arrayOfCodecType[3] = AAC;
      $VALUES = arrayOfCodecType;
    }
    
    private CodecType() {}
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.MediaCodecListCollector
 * JD-Core Version:    0.7.0.1
 */