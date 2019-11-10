package deluxe;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

// $FF: synthetic class
public class Property {
   public static JSONObject toJSONObject(Properties var0) throws D {
      B var1 = new B();
      if (var0 != null && !var0.isEmpty()) {
         Enumeration var2 = var0.propertyNames();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            var1.put(var3, var0.getProperty(var3));
         }
      }

      return var1;
   }

   public static Properties toProperties(JSONObject var0) throws D {
      Properties var1 = new Properties();
      if (var0 != null) {
         Iterator var2 = var0.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Object var4 = var0.opt(var3);
            if (!JSONObject.NULL.equals(var4)) {
               var1.put(var3, var4.toString());
            }
         }
      }

      return var1;
   }
}
