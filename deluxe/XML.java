package deluxe;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

// $FF: synthetic class
public class XML {
   public static final Character LT = '<';
   public static final Character APOS = '\'';
   public static final Character GT = '>';
   public static final Character AMP = '&';
   public static final Character SLASH = '/';
   public static final Character QUEST = '?';
   public static final Character BANG = '!';
   public static final Character QUOT = '"';
   public static final Character EQ = '=';

   public static Object stringToValue(String var0) {
      if (var0.equals("")) {
         return var0;
      } else if (var0.equalsIgnoreCase("true")) {
         return Boolean.TRUE;
      } else if (var0.equalsIgnoreCase("false")) {
         return Boolean.FALSE;
      } else if (var0.equalsIgnoreCase("null")) {
         return B.NULL;
      } else {
         char var1 = var0.charAt(0);
         if (var1 >= '0' && var1 <= '9' || var1 == '-') {
            try {
               if (var0.indexOf(46) <= -1 && var0.indexOf(101) <= -1 && var0.indexOf(69) <= -1 && !"-0".equals(var0)) {
                  Long var4 = Long.valueOf(var0);
                  if (var0.equals(var4.toString())) {
                     if (var4 == (long)var4.intValue()) {
                        return var4.intValue();
                     }

                     return var4;
                  }
               } else {
                  Double var2 = Double.valueOf(var0);
                  if (!var2.isInfinite() && !var2.isNaN()) {
                     return var2;
                  }
               }
            } catch (Exception var3) {
            }
         }

         return var0;
      }
   }

   public static String toString(Object var0, String var1) throws D {
      StringBuilder var2 = new StringBuilder();
      BE var3;
      Object var8;
      if (var0 instanceof B) {
         if (var1 != null) {
            var2.append('<');
            var2.append(var1);
            var2.append('>');
         }

         B var4 = (B)var0;
         Iterator var12 = var4.keySet().iterator();

         while(true) {
            while(true) {
               while(var12.hasNext()) {
                  String var13 = (String)var12.next();
                  var8 = var4.opt(var13);
                  if (var8 == null) {
                     var8 = "";
                  } else if (var8.getClass().isArray()) {
                     var8 = new BE(var8);
                  }

                  int var9;
                  int var10;
                  Object var11;
                  if ("content".equals(var13)) {
                     if (var8 instanceof BE) {
                        var3 = (BE)var8;
                        var9 = var3.length();

                        for(var10 = 0; var10 < var9; ++var10) {
                           if (var10 > 0) {
                              var2.append('\n');
                           }

                           var11 = var3.opt(var10);
                           var2.append(escape(var11.toString()));
                        }
                     } else {
                        var2.append(escape(var8.toString()));
                     }
                  } else if (var8 instanceof BE) {
                     var3 = (BE)var8;
                     var9 = var3.length();

                     for(var10 = 0; var10 < var9; ++var10) {
                        var11 = var3.opt(var10);
                        if (var11 instanceof BE) {
                           var2.append('<');
                           var2.append(var13);
                           var2.append('>');
                           var2.append(toString(var11));
                           var2.append("</");
                           var2.append(var13);
                           var2.append('>');
                        } else {
                           var2.append(toString(var11, var13));
                        }
                     }
                  } else if ("".equals(var8)) {
                     var2.append('<');
                     var2.append(var13);
                     var2.append("/>");
                  } else {
                     var2.append(toString(var8, var13));
                  }
               }

               if (var1 != null) {
                  var2.append("</");
                  var2.append(var1);
                  var2.append('>');
               }

               return var2.toString();
            }
         }
      } else if (var0 != null && (var0 instanceof BE || var0.getClass().isArray())) {
         if (var0.getClass().isArray()) {
            var3 = new BE(var0);
         } else {
            var3 = (BE)var0;
         }

         int var6 = var3.length();

         for(int var7 = 0; var7 < var6; ++var7) {
            var8 = var3.opt(var7);
            var2.append(toString(var8, var1 == null ? "array" : var1));
         }

         return var2.toString();
      } else {
         String var5 = var0 == null ? "null" : escape(var0.toString());
         return var1 == null ? "\"" + var5 + "\"" : (var5.length() == 0 ? "<" + var1 + "/>" : "<" + var1 + ">" + var5 + "</" + var1 + ">");
      }
   }

   public static String unescape(String var0) {
      StringBuilder var1 = new StringBuilder(var0.length());
      int var2 = 0;

      for(int var3 = var0.length(); var2 < var3; ++var2) {
         char var4 = var0.charAt(var2);
         if (var4 == '&') {
            int var5 = var0.indexOf(59, var2);
            if (var5 > var2) {
               String var6 = var0.substring(var2 + 1, var5);
               var1.append(XMLTokener.unescapeEntity(var6));
               var2 += var6.length() + 1;
            } else {
               var1.append(var4);
            }
         } else {
            var1.append(var4);
         }
      }

      return var1.toString();
   }

   public static B toJSONObject(String var0) throws D {
      return toJSONObject(var0, false);
   }

   public static void noSpace(String var0) throws D {
      int var2 = var0.length();
      if (var2 == 0) {
         throw new D("Empty string.");
      } else {
         for(int var1 = 0; var1 < var2; ++var1) {
            if (Character.isWhitespace(var0.charAt(var1))) {
               throw new D("'" + var0 + "' contains a space character.");
            }
         }

      }
   }

   public static B toJSONObject(String var0, boolean var1) throws D {
      return toJSONObject((Reader)(new StringReader(var0)), var1);
   }

   public static boolean parse(XMLTokener var0, B var1, String var2, boolean var3) throws D {
      B var6 = null;
      Object var9 = var0.nextToken();
      String var7;
      if (var9 == BANG) {
         char var4 = var0.next();
         if (var4 == '-') {
            if (var0.next() == '-') {
               var0.skipPast("-->");
               return false;
            }

            var0.back();
         } else if (var4 == '[') {
            var9 = var0.nextToken();
            if ("CDATA".equals(var9) && var0.next() == '[') {
               var7 = var0.nextCDATA();
               if (var7.length() > 0) {
                  var1.accumulate("content", var7);
               }

               return false;
            }

            throw var0.syntaxError("Expected 'CDATA['");
         }

         int var5 = 1;

         do {
            var9 = var0.nextMeta();
            if (var9 == null) {
               throw var0.syntaxError("Missing '>' after '<!'.");
            }

            if (var9 == LT) {
               ++var5;
            } else if (var9 == GT) {
               --var5;
            }
         } while(var5 > 0);

         return false;
      } else if (var9 == QUEST) {
         var0.skipPast("?>");
         return false;
      } else if (var9 == SLASH) {
         var9 = var0.nextToken();
         if (var2 == null) {
            throw var0.syntaxError("Mismatched close tag " + var9);
         } else if (!var9.equals(var2)) {
            throw var0.syntaxError("Mismatched " + var2 + " and " + var9);
         } else if (var0.nextToken() != GT) {
            throw var0.syntaxError("Misshaped close tag");
         } else {
            return true;
         }
      } else if (var9 instanceof Character) {
         throw var0.syntaxError("Misshaped tag");
      } else {
         String var8 = (String)var9;
         var9 = null;
         var6 = new B();

         while(true) {
            if (var9 == null) {
               var9 = var0.nextToken();
            }

            if (!(var9 instanceof String)) {
               if (var9 == SLASH) {
                  if (var0.nextToken() != GT) {
                     throw var0.syntaxError("Misshaped tag");
                  }

                  if (var6.length() > 0) {
                     var1.accumulate(var8, var6);
                  } else {
                     var1.accumulate(var8, "");
                  }

                  return false;
               }

               if (var9 != GT) {
                  throw var0.syntaxError("Misshaped tag");
               }

               while(true) {
                  var9 = var0.nextContent();
                  if (var9 == null) {
                     if (var8 != null) {
                        throw var0.syntaxError("Unclosed tag " + var8);
                     }

                     return false;
                  }

                  if (var9 instanceof String) {
                     var7 = (String)var9;
                     if (var7.length() > 0) {
                        var6.accumulate("content", var3 ? var7 : stringToValue(var7));
                     }
                  } else if (var9 == LT && parse(var0, var6, var8, var3)) {
                     if (var6.length() == 0) {
                        var1.accumulate(var8, "");
                     } else if (var6.length() == 1 && var6.opt("content") != null) {
                        var1.accumulate(var8, var6.opt("content"));
                     } else {
                        var1.accumulate(var8, var6);
                     }

                     return false;
                  }
               }
            }

            var7 = (String)var9;
            var9 = var0.nextToken();
            if (var9 == EQ) {
               var9 = var0.nextToken();
               if (!(var9 instanceof String)) {
                  throw var0.syntaxError("Missing value");
               }

               var6.accumulate(var7, var3 ? (String)var9 : stringToValue((String)var9));
               var9 = null;
            } else {
               var6.accumulate(var7, "");
            }
         }
      }
   }

   public static Iterable codePointIterator(String var0) {
      return new AV(var0);
   }

   public static boolean mustEscape(int var0) {
      return Character.isISOControl(var0) && var0 != 9 && var0 != 10 && var0 != 13 || (var0 < 32 || var0 > 55295) && (var0 < 57344 || var0 > 65533) && (var0 < 65536 || var0 > 1114111);
   }

   public static String escape(String var0) {
      StringBuilder var1 = new StringBuilder(var0.length());
      Iterator var2 = codePointIterator(var0).iterator();

      while(var2.hasNext()) {
         int var3 = (Integer)var2.next();
         switch(var3) {
         case 34:
            var1.append("&quot;");
            break;
         case 38:
            var1.append("&amp;");
            break;
         case 39:
            var1.append("&apos;");
            break;
         case 60:
            var1.append("&lt;");
            break;
         case 62:
            var1.append("&gt;");
            break;
         default:
            if (mustEscape(var3)) {
               var1.append("&#x");
               var1.append(Integer.toHexString(var3));
               var1.append(';');
            } else {
               var1.appendCodePoint(var3);
            }
         }
      }

      return var1.toString();
   }

   public static B toJSONObject(Reader var0, boolean var1) throws D {
      B var2 = new B();
      K var3 = new K(var0);

      while(var3.more()) {
         var3.skipPast("<");
         if (var3.more()) {
            parse(var3, var2, (String)null, var1);
         }
      }

      return var2;
   }

   public static B toJSONObject(Reader var0) throws D {
      return toJSONObject(var0, false);
   }

   public static String toString(Object var0) throws D {
      return toString(var0, (String)null);
   }
}
