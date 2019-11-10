package deluxe;

// $FF: synthetic class
public class CDL {
   public static String toString(BE var0, BE var1) throws D {
      if (var0 != null && var0.length() != 0) {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            JSONObject var4 = var1.optJSONObject(var3);
            if (var4 != null) {
               var2.append(rowToString(var4.toJSONArray(var0)));
            }
         }

         return var2.toString();
      } else {
         return null;
      }
   }

   public static BE toJSONArray(BE var0, String var1) throws D {
      return toJSONArray(var0, new AE(var1));
   }

   public static BE toJSONArray(BE var0, AE var1) throws D {
      if (var0 != null && var0.length() != 0) {
         BE var2 = new BE();

         while(true) {
            JSONObject var3 = rowToJSONObject(var0, var1);
            if (var3 == null) {
               return var2.length() == 0 ? null : var2;
            }

            var2.put(var3);
         }
      } else {
         return null;
      }
   }

   public static BE rowToJSONArray(AE var0) throws D {
      BE var1 = new BE();

      while(true) {
         String var2 = getValue(var0);
         char var3 = var0.next();
         if (var2 == null || var1.length() == 0 && var2.length() == 0 && var3 != ',') {
            return null;
         }

         var1.put(var2);

         while(var3 != ',') {
            if (var3 != ' ') {
               if (var3 != '\n' && var3 != '\r' && var3 != 0) {
                  throw var0.syntaxError("Bad character '" + var3 + "' (" + var3 + ").");
               }

               return var1;
            }

            var3 = var0.next();
         }
      }
   }

   public static BE toJSONArray(AE var0) throws D {
      return toJSONArray(rowToJSONArray(var0), var0);
   }

   public static String toString(BE var0) throws D {
      JSONObject var1 = var0.optJSONObject(0);
      if (var1 != null) {
         BE var2 = var1.names();
         if (var2 != null) {
            return rowToString(var2) + toString(var2, var0);
         }
      }

      return null;
   }

   public static String rowToString(BE var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         if (var2 > 0) {
            var1.append(',');
         }

         Object var3 = var0.opt(var2);
         if (var3 != null) {
            String var4 = var3.toString();
            if (var4.length() > 0 && (var4.indexOf(44) >= 0 || var4.indexOf(10) >= 0 || var4.indexOf(13) >= 0 || var4.indexOf(0) >= 0 || var4.charAt(0) == '"')) {
               var1.append('"');
               int var5 = var4.length();

               for(int var6 = 0; var6 < var5; ++var6) {
                  char var7 = var4.charAt(var6);
                  if (var7 >= ' ' && var7 != '"') {
                     var1.append(var7);
                  }
               }

               var1.append('"');
            } else {
               var1.append(var4);
            }
         }
      }

      var1.append('\n');
      return var1.toString();
   }

   public static String getValue(AE var0) throws D {
      char var1;
      do {
         var1 = var0.next();
      } while(var1 == ' ' || var1 == '\t');

      switch(var1) {
      case '\u0000':
         return null;
      case '"':
      case '\'':
         char var2 = var1;
         StringBuffer var3 = new StringBuffer();

         while(true) {
            var1 = var0.next();
            if (var1 == var2) {
               char var4 = var0.next();
               if (var4 != '"') {
                  if (var4 > 0) {
                     var0.back();
                  }

                  return var3.toString();
               }
            }

            if (var1 == 0 || var1 == '\n' || var1 == '\r') {
               throw var0.syntaxError("Missing close quote '" + var2 + "'.");
            }

            var3.append(var1);
         }
      case ',':
         var0.back();
         return "";
      default:
         var0.back();
         return var0.nextTo(',');
      }
   }

   public static BE toJSONArray(String var0) throws D {
      return toJSONArray(new AE(var0));
   }

   public static JSONObject rowToJSONObject(BE var0, AE var1) throws D {
      BE var2 = rowToJSONArray(var1);
      return var2 != null ? var2.toJSONObject(var0) : null;
   }
}
