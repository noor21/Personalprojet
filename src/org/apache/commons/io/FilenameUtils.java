package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class FilenameUtils
{
  public static final char EXTENSION_SEPARATOR = '.';
  public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
  private static final char OTHER_SEPARATOR = '\000';
  private static final char SYSTEM_SEPARATOR = File.separatorChar;
  private static final char UNIX_SEPARATOR = '/';
  private static final char WINDOWS_SEPARATOR = '\\';
  
  static
  {
    if (!isSystemWindows()) {
      OTHER_SEPARATOR = '\\';
    } else {
      OTHER_SEPARATOR = '/';
    }
  }
  
  public static String concat(String paramString1, String paramString2)
  {
    String str = null;
    int i = getPrefixLength(paramString2);
    if (i >= 0) {
      if (i <= 0)
      {
        if (paramString1 != null)
        {
          i = paramString1.length();
          if (i != 0)
          {
            if (!isSeparator(paramString1.charAt(i - 1))) {
              str = normalize(paramString1 + '/' + paramString2);
            } else {
              str = normalize(paramString1 + paramString2);
            }
          }
          else {
            str = normalize(paramString2);
          }
        }
      }
      else {
        str = normalize(paramString2);
      }
    }
    return str;
  }
  
  public static boolean directoryContains(String paramString1, String paramString2)
    throws IOException
  {
    boolean bool = false;
    if (paramString1 != null)
    {
      if ((paramString2 != null) && (!IOCase.SYSTEM.checkEquals(paramString1, paramString2))) {
        bool = IOCase.SYSTEM.checkStartsWith(paramString2, paramString1);
      }
      return bool;
    }
    throw new IllegalArgumentException("Directory must not be null");
  }
  
  private static String doGetFullPath(String paramString, boolean paramBoolean)
  {
    if (paramString != null)
    {
      int j = getPrefixLength(paramString);
      if (j >= 0)
      {
        if (j < paramString.length())
        {
          int i = indexOfLastSeparator(paramString);
          if (i >= 0)
          {
            if (!paramBoolean) {
              j = 0;
            } else {
              j = 1;
            }
            i += j;
            if (i == 0) {
              i++;
            }
            paramString = paramString.substring(0, i);
          }
          else
          {
            paramString = paramString.substring(0, j);
          }
        }
        else if (paramBoolean)
        {
          paramString = getPrefix(paramString);
        }
      }
      else {
        paramString = null;
      }
    }
    else
    {
      paramString = null;
    }
    return paramString;
  }
  
  private static String doGetPath(String paramString, int paramInt)
  {
    Object localObject = null;
    String str;
    if (paramString != null)
    {
      int i = getPrefixLength(paramString);
      if (i >= 0)
      {
        int j = indexOfLastSeparator(paramString);
        int k = j + paramInt;
        if ((i < paramString.length()) && (j >= 0) && (i < k)) {
          str = paramString.substring(i, k);
        } else {
          str = "";
        }
      }
    }
    return str;
  }
  
  private static String doNormalize(String paramString, char paramChar, boolean paramBoolean)
  {
    if (paramString != null)
    {
      int i = paramString.length();
      if (i != 0)
      {
        int j = getPrefixLength(paramString);
        if (j >= 0)
        {
          char[] arrayOfChar = new char[i + 2];
          paramString.getChars(0, paramString.length(), arrayOfChar, 0);
          int m;
          if (paramChar != SYSTEM_SEPARATOR) {
            m = SYSTEM_SEPARATOR;
          } else {
            m = OTHER_SEPARATOR;
          }
          for (int k = 0;; k++)
          {
            if (k >= arrayOfChar.length)
            {
              k = 1;
              if (arrayOfChar[(i - 1)] != paramChar)
              {
                m = i + 1;
                arrayOfChar[i] = paramChar;
                k = 0;
                i = m;
              }
              for (m = j + 1;; m++)
              {
                if (m >= i) {
                  for (m = j + 1;; m++)
                  {
                    if (m >= i)
                    {
                      label386:
                      for (int n = j + 2;; n++)
                      {
                        if (n >= i)
                        {
                          if (i > 0)
                          {
                            if (i > j)
                            {
                              if ((k == 0) || (!paramBoolean)) {
                                return new String(arrayOfChar, 0, i - 1);
                              }
                              return new String(arrayOfChar, 0, i);
                            }
                            return new String(arrayOfChar, 0, i);
                          }
                          return "";
                        }
                        if ((arrayOfChar[n] == paramChar) && (arrayOfChar[(n - 1)] == '.') && (arrayOfChar[(n - 2)] == '.') && ((n == j + 2) || (arrayOfChar[(n - 3)] == paramChar)))
                        {
                          if (n == j + 2) {
                            break;
                          }
                          if (n == i - 1) {
                            k = 1;
                          }
                          for (m = n - 4;; m--)
                          {
                            if (m < j)
                            {
                              System.arraycopy(arrayOfChar, n + 1, arrayOfChar, j, i - n);
                              i -= n + 1 - j;
                              n = j + 1;
                              break label386;
                            }
                            if (arrayOfChar[m] == paramChar) {
                              break;
                            }
                          }
                          System.arraycopy(arrayOfChar, n + 1, arrayOfChar, m + 1, i - n);
                          i -= n - m;
                          n = m + 1;
                        }
                      }
                      paramString = null;
                      break;
                    }
                    if ((arrayOfChar[m] == paramChar) && (arrayOfChar[(m - 1)] == '.') && ((m == j + 1) || (arrayOfChar[(m - 2)] == paramChar)))
                    {
                      if (m == i - 1) {
                        k = 1;
                      }
                      System.arraycopy(arrayOfChar, m + 1, arrayOfChar, m - 1, i - m);
                      i -= 2;
                      m--;
                    }
                  }
                }
                if ((arrayOfChar[m] == paramChar) && (arrayOfChar[(m - 1)] == paramChar))
                {
                  System.arraycopy(arrayOfChar, m, arrayOfChar, m - 1, i - m);
                  i--;
                  m--;
                }
              }
            }
            if (arrayOfChar[k] == m) {
              arrayOfChar[k] = paramChar;
            }
          }
        }
        paramString = null;
      }
    }
    else
    {
      paramString = null;
    }
    return paramString;
  }
  
  public static boolean equals(String paramString1, String paramString2)
  {
    return equals(paramString1, paramString2, false, IOCase.SENSITIVE);
  }
  
  public static boolean equals(String paramString1, String paramString2, boolean paramBoolean, IOCase paramIOCase)
  {
    boolean bool;
    if ((paramString1 != null) && (paramString2 != null))
    {
      if (paramBoolean)
      {
        paramString1 = normalize(paramString1);
        paramString2 = normalize(paramString2);
        if ((paramString1 == null) || (paramString2 == null)) {}
      }
      else
      {
        if (paramIOCase == null) {
          paramIOCase = IOCase.SENSITIVE;
        }
        bool = paramIOCase.checkEquals(paramString1, paramString2);
        break label76;
      }
      throw new NullPointerException("Error normalizing one or both of the file names");
    }
    else if ((paramString1 != null) || (paramString2 != null))
    {
      bool = false;
    }
    else
    {
      bool = true;
    }
    label76:
    return bool;
  }
  
  public static boolean equalsNormalized(String paramString1, String paramString2)
  {
    return equals(paramString1, paramString2, true, IOCase.SENSITIVE);
  }
  
  public static boolean equalsNormalizedOnSystem(String paramString1, String paramString2)
  {
    return equals(paramString1, paramString2, true, IOCase.SYSTEM);
  }
  
  public static boolean equalsOnSystem(String paramString1, String paramString2)
  {
    return equals(paramString1, paramString2, false, IOCase.SYSTEM);
  }
  
  public static String getBaseName(String paramString)
  {
    return removeExtension(getName(paramString));
  }
  
  public static String getExtension(String paramString)
  {
    String str;
    if (paramString != null)
    {
      int i = indexOfExtension(paramString);
      if (i != -1) {
        str = paramString.substring(i + 1);
      } else {
        str = "";
      }
    }
    else
    {
      str = null;
    }
    return str;
  }
  
  public static String getFullPath(String paramString)
  {
    return doGetFullPath(paramString, true);
  }
  
  public static String getFullPathNoEndSeparator(String paramString)
  {
    return doGetFullPath(paramString, false);
  }
  
  public static String getName(String paramString)
  {
    String str;
    if (paramString != null) {
      str = paramString.substring(1 + indexOfLastSeparator(paramString));
    } else {
      str = null;
    }
    return str;
  }
  
  public static String getPath(String paramString)
  {
    return doGetPath(paramString, 1);
  }
  
  public static String getPathNoEndSeparator(String paramString)
  {
    return doGetPath(paramString, 0);
  }
  
  public static String getPrefix(String paramString)
  {
    String str = null;
    if (paramString != null)
    {
      int i = getPrefixLength(paramString);
      if (i >= 0) {
        if (i <= paramString.length()) {
          str = paramString.substring(0, i);
        } else {
          str = paramString + '/';
        }
      }
    }
    return str;
  }
  
  public static int getPrefixLength(String paramString)
  {
    int j = 1;
    if (paramString != null)
    {
      int i = paramString.length();
      if (i != 0)
      {
        char c1 = paramString.charAt(0);
        if (c1 != ':')
        {
          int k;
          if (i != j)
          {
            if (c1 != '~')
            {
              char c2 = paramString.charAt(j);
              if (c2 != ':')
              {
                if ((!isSeparator(c1)) || (!isSeparator(c2)))
                {
                  if (!isSeparator(c1)) {
                    j = 0;
                  }
                }
                else
                {
                  i = paramString.indexOf('/', 2);
                  j = paramString.indexOf('\\', 2);
                  if (((i != -1) || (j != -1)) && (i != 2) && (j != 2))
                  {
                    if (i == -1) {
                      i = j;
                    }
                    if (j == -1) {
                      j = i;
                    }
                    j = 1 + Math.min(i, j);
                  }
                  else
                  {
                    j = -1;
                  }
                }
              }
              else
              {
                j = Character.toUpperCase(c1);
                if ((j < 65) || (j > 90)) {
                  j = -1;
                } else if ((i != 2) && (isSeparator(paramString.charAt(2)))) {
                  j = 3;
                } else {
                  j = 2;
                }
              }
            }
            else
            {
              k = paramString.indexOf('/', j);
              j = paramString.indexOf('\\', j);
              if ((k != -1) || (j != -1))
              {
                if (k == -1) {
                  k = j;
                }
                if (j == -1) {
                  j = k;
                }
                j = 1 + Math.min(k, j);
              }
              else
              {
                j = i + 1;
              }
            }
          }
          else if (k != 126)
          {
            if (!isSeparator(k)) {
              j = 0;
            }
          }
          else {
            j = 2;
          }
        }
        else
        {
          j = -1;
        }
      }
      else
      {
        j = 0;
      }
    }
    else
    {
      j = -1;
    }
    return j;
  }
  
  public static int indexOfExtension(String paramString)
  {
    int j = -1;
    if (paramString != null)
    {
      int i = paramString.lastIndexOf('.');
      if (indexOfLastSeparator(paramString) > i) {
        i = j;
      }
      j = i;
    }
    return j;
  }
  
  public static int indexOfLastSeparator(String paramString)
  {
    int i;
    if (paramString != null) {
      i = Math.max(paramString.lastIndexOf('/'), paramString.lastIndexOf('\\'));
    } else {
      i = -1;
    }
    return i;
  }
  
  public static boolean isExtension(String paramString1, String paramString2)
  {
    boolean bool = false;
    if (paramString1 != null) {
      if ((paramString2 != null) && (paramString2.length() != 0)) {
        bool = getExtension(paramString1).equals(paramString2);
      } else if (indexOfExtension(paramString1) == -1) {
        bool = true;
      }
    }
    return bool;
  }
  
  public static boolean isExtension(String paramString, Collection<String> paramCollection)
  {
    boolean bool2 = true;
    boolean bool1 = false;
    if (paramString != null)
    {
      String str;
      Iterator localIterator;
      if ((paramCollection != null) && (!paramCollection.isEmpty()))
      {
        str = getExtension(paramString);
        localIterator = paramCollection.iterator();
      }
      while (localIterator.hasNext()) {
        if (str.equals((String)localIterator.next()))
        {
          bool1 = bool2;
          break;
          if (indexOfExtension(paramString) != -1) {
            bool2 = false;
          }
          bool1 = bool2;
        }
      }
    }
    return bool1;
  }
  
  public static boolean isExtension(String paramString, String[] paramArrayOfString)
  {
    boolean bool2 = true;
    boolean bool1 = false;
    if (paramString != null)
    {
      String str;
      int j;
      int i;
      if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
      {
        str = getExtension(paramString);
        j = paramArrayOfString.length;
        i = 0;
      }
      while (i < j) {
        if (!str.equals(paramArrayOfString[i]))
        {
          i++;
        }
        else
        {
          bool1 = bool2;
          break;
          if (indexOfExtension(paramString) != -1) {
            bool2 = false;
          }
          bool1 = bool2;
        }
      }
    }
    return bool1;
  }
  
  private static boolean isSeparator(char paramChar)
  {
    boolean bool;
    if ((paramChar != '/') && (paramChar != '\\')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static boolean isSystemWindows()
  {
    boolean bool;
    if (SYSTEM_SEPARATOR != '\\') {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static String normalize(String paramString)
  {
    return doNormalize(paramString, SYSTEM_SEPARATOR, true);
  }
  
  public static String normalize(String paramString, boolean paramBoolean)
  {
    char c;
    if (!paramBoolean) {
      c = '\\';
    } else {
      c = '/';
    }
    return doNormalize(paramString, c, true);
  }
  
  public static String normalizeNoEndSeparator(String paramString)
  {
    return doNormalize(paramString, SYSTEM_SEPARATOR, false);
  }
  
  public static String normalizeNoEndSeparator(String paramString, boolean paramBoolean)
  {
    char c;
    if (!paramBoolean) {
      c = '\\';
    } else {
      c = '/';
    }
    return doNormalize(paramString, c, false);
  }
  
  public static String removeExtension(String paramString)
  {
    if (paramString != null)
    {
      int i = indexOfExtension(paramString);
      if (i != -1) {
        paramString = paramString.substring(0, i);
      }
    }
    else
    {
      paramString = null;
    }
    return paramString;
  }
  
  public static String separatorsToSystem(String paramString)
  {
    String str;
    if (paramString != null)
    {
      if (!isSystemWindows()) {
        str = separatorsToUnix(paramString);
      } else {
        str = separatorsToWindows(paramString);
      }
    }
    else {
      str = null;
    }
    return str;
  }
  
  public static String separatorsToUnix(String paramString)
  {
    if ((paramString != null) && (paramString.indexOf('\\') != -1)) {
      paramString = paramString.replace('\\', '/');
    }
    return paramString;
  }
  
  public static String separatorsToWindows(String paramString)
  {
    if ((paramString != null) && (paramString.indexOf('/') != -1)) {
      paramString = paramString.replace('/', '\\');
    }
    return paramString;
  }
  
  static String[] splitOnTokens(String paramString)
  {
    if ((paramString.indexOf('?') != -1) || (paramString.indexOf('*') != -1))
    {
      char[] arrayOfChar = paramString.toCharArray();
      ArrayList localArrayList = new ArrayList();
      localObject = new StringBuilder();
      for (int i = 0;; i++)
      {
        if (i >= arrayOfChar.length)
        {
          if (((StringBuilder)localObject).length() != 0) {
            localArrayList.add(((StringBuilder)localObject).toString());
          }
          localObject = (String[])localArrayList.toArray(new String[localArrayList.size()]);
          break;
        }
        if ((arrayOfChar[i] != '?') && (arrayOfChar[i] != '*'))
        {
          ((StringBuilder)localObject).append(arrayOfChar[i]);
        }
        else
        {
          if (((StringBuilder)localObject).length() != 0)
          {
            localArrayList.add(((StringBuilder)localObject).toString());
            ((StringBuilder)localObject).setLength(0);
          }
          if (arrayOfChar[i] != '?')
          {
            if ((localArrayList.isEmpty()) || ((i > 0) && (!((String)localArrayList.get(-1 + localArrayList.size())).equals("*")))) {
              localArrayList.add("*");
            }
          }
          else {
            localArrayList.add("?");
          }
        }
      }
    }
    Object localObject = new String[1];
    localObject[0] = paramString;
    return localObject;
  }
  
  public static boolean wildcardMatch(String paramString1, String paramString2)
  {
    return wildcardMatch(paramString1, paramString2, IOCase.SENSITIVE);
  }
  
  public static boolean wildcardMatch(String paramString1, String paramString2, IOCase paramIOCase)
  {
    boolean bool = true;
    if ((paramString1 != null) || (paramString2 != null)) {
      if ((paramString1 != null) && (paramString2 != null))
      {
        if (paramIOCase == null) {
          paramIOCase = IOCase.SENSITIVE;
        }
        String[] arrayOfString = splitOnTokens(paramString2);
        int m = 0;
        int j = 0;
        int i = 0;
        Stack localStack = new Stack();
        do
        {
          int k;
          if (localStack.size() > 0)
          {
            int[] arrayOfInt1 = (int[])localStack.pop();
            i = arrayOfInt1[0];
            k = arrayOfInt1[bool];
            m = 1;
          }
          while (i < arrayOfString.length)
          {
            int n;
            if (!arrayOfString[i].equals("?"))
            {
              if (!arrayOfString[i].equals("*"))
              {
                if (m == 0)
                {
                  if (!paramIOCase.checkRegionMatches(paramString1, k, arrayOfString[i])) {
                    break;
                  }
                }
                else
                {
                  k = paramIOCase.checkIndexOf(paramString1, k, arrayOfString[i]);
                  if (k == -1) {
                    break;
                  }
                  int i1 = paramIOCase.checkIndexOf(paramString1, k + 1, arrayOfString[i]);
                  if (i1 >= 0)
                  {
                    int[] arrayOfInt2 = new int[2];
                    arrayOfInt2[0] = i;
                    arrayOfInt2[bool] = i1;
                    localStack.push(arrayOfInt2);
                  }
                }
                k += arrayOfString[i].length();
                n = 0;
              }
              else
              {
                n = 1;
                if (i == -1 + arrayOfString.length) {
                  k = paramString1.length();
                }
              }
            }
            else
            {
              k++;
              if (k > paramString1.length()) {
                break;
              }
              n = 0;
            }
            i++;
          }
          if ((i == arrayOfString.length) && (k == paramString1.length())) {
            break;
          }
        } while (localStack.size() > 0);
        bool = false;
      }
      else
      {
        bool = false;
      }
    }
    return bool;
  }
  
  public static boolean wildcardMatchOnSystem(String paramString1, String paramString2)
  {
    return wildcardMatch(paramString1, paramString2, IOCase.SYSTEM);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.FilenameUtils
 * JD-Core Version:    0.7.0.1
 */