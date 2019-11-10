package deluxe;

import java.util.ArrayList;
import java.util.List;

// $FF: synthetic class
public class JSONPointer {
   public final List refTokens = new ArrayList();

   public JSONPointer append(String var1) {
      if (var1 == null) {
         throw new NullPointerException("token cannot be null");
      } else {
         this.refTokens.add(var1);
         return this;
      }
   }

   public JSONPointer build() {
      // $FF: Couldn't be decompiled
   }

   public JSONPointer append(int var1) {
      this.refTokens.add(String.valueOf(var1));
      return this;
   }
}
