package deluxe;

import java.util.Iterator;

// $FF: synthetic class
public class CookieList {
   public static String toString(B var0) throws D {
      boolean var1 = false;
      StringBuilder var2 = new StringBuilder();
      Iterator var3 = var0.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Object var5 = var0.opt(var4);
         if (!B.NULL.equals(var5)) {
            if (var1) {
               var2.append(';');
            }

            var2.append(Cookie.escape(var4));
            var2.append("=");
            var2.append(Cookie.escape(var5.toString()));
            var1 = true;
         }
      }

      return var2.toString();
   }

   public static B toJSONObject(String var0) throws D {
      B var1 = new B();
      AE var2 = new AE(var0);

      while(var2.more()) {
         String var3 = Cookie.unescape(var2.nextTo('='));
         var2.next('=');
         var1.put(var3, Cookie.unescape(var2.nextTo(';')));
         var2.next();
      }

      return var1;
   }
}
