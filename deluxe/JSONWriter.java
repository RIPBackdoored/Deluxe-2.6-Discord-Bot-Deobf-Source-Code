package deluxe;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

// $FF: synthetic class
public class JSONWriter {
   public Appendable writer;
   public char mode = 'i';
   public static final int maxdepth = 200;
   public final B[] stack = new B[200];
   public boolean comma = false;
   public int top = 0;

   public JSONWriter end(char var1, char var2) throws D {
      if (this.mode != var1) {
         throw new D(var1 == 'a' ? "Misplaced endArray." : "Misplaced endObject.");
      } else {
         this.pop(var1);

         try {
            this.writer.append(var2);
         } catch (IOException var4) {
            throw new D(var4);
         }

         this.comma = true;
         return this;
      }
   }

   public JSONWriter object() throws D {
      if (this.mode == 'i') {
         this.mode = 'o';
      }

      if (this.mode != 'o' && this.mode != 'a') {
         throw new D("Misplaced object.");
      } else {
         this.append("{");
         this.push(new B());
         this.comma = false;
         return this;
      }
   }

   public JSONWriter value(Object var1) throws D {
      return this.append(valueToString(var1));
   }

   public JSONWriter endArray() throws D {
      return this.end('a', ']');
   }

   public static String valueToString(Object var0) throws D {
      if (var0 != null && !var0.equals((Object)null)) {
         String var6;
         if (var0 instanceof S) {
            try {
               var6 = ((S)var0).toJSONString();
            } catch (Exception var3) {
               throw new D(var3);
            }

            if (var6 instanceof String) {
               return (String)var6;
            } else {
               throw new D("Bad value from toJSONString: " + var6);
            }
         } else if (var0 instanceof Number) {
            var6 = B.numberToString((Number)var0);

            try {
               new BigDecimal(var6);
               return var6;
            } catch (NumberFormatException var4) {
               return B.quote(var6);
            }
         } else if (!(var0 instanceof Boolean) && !(var0 instanceof B) && !(var0 instanceof BE)) {
            if (var0 instanceof Map) {
               Map var5 = (Map)var0;
               return (new B(var5)).toString();
            } else if (var0 instanceof Collection) {
               Collection var1 = (Collection)var0;
               return (new BE(var1)).toString();
            } else if (var0.getClass().isArray()) {
               return (new BE(var0)).toString();
            } else {
               return var0 instanceof Enum ? B.quote(((Enum)var0).name()) : B.quote(var0.toString());
            }
         } else {
            return var0.toString();
         }
      } else {
         return "null";
      }
   }

   public JSONWriter key(String var1) throws D {
      if (var1 == null) {
         throw new D("Null key.");
      } else if (this.mode == 'k') {
         try {
            B var2 = this.stack[this.top - 1];
            if (var2.has(var1)) {
               throw new D("Duplicate key \"" + var1 + "\"");
            } else {
               var2.put(var1, true);
               if (this.comma) {
                  this.writer.append(',');
               }

               this.writer.append(B.quote(var1));
               this.writer.append(':');
               this.comma = false;
               this.mode = 'o';
               return this;
            }
         } catch (IOException var3) {
            throw new D(var3);
         }
      } else {
         throw new D("Misplaced key.");
      }
   }

   public JSONWriter(Appendable var1) {
      this.writer = var1;
   }

   public JSONWriter value(double var1) throws D {
      return this.value(var1);
   }

   public JSONWriter value(boolean var1) throws D {
      return this.append(var1 ? "true" : "false");
   }

   public JSONWriter endObject() throws D {
      return this.end('k', '}');
   }

   public JSONWriter append(String var1) throws D {
      if (var1 == null) {
         throw new D("Null pointer");
      } else if (this.mode != 'o' && this.mode != 'a') {
         throw new D("Value out of sequence.");
      } else {
         try {
            if (this.comma && this.mode == 'a') {
               this.writer.append(',');
            }

            this.writer.append(var1);
         } catch (IOException var3) {
            throw new D(var3);
         }

         if (this.mode == 'o') {
            this.mode = 'k';
         }

         this.comma = true;
         return this;
      }
   }

   public JSONWriter array() throws D {
      if (this.mode != 'i' && this.mode != 'o' && this.mode != 'a') {
         throw new D("Misplaced array.");
      } else {
         this.push((B)null);
         this.append("[");
         this.comma = false;
         return this;
      }
   }

   public void push(B var1) throws D {
      if (this.top >= 200) {
         throw new D("Nesting too deep.");
      } else {
         this.stack[this.top] = var1;
         this.mode = (char)(var1 == null ? 97 : 107);
         ++this.top;
      }
   }

   public void pop(char var1) throws D {
      if (this.top <= 0) {
         throw new D("Nesting error.");
      } else {
         int var2 = this.stack[this.top - 1] == null ? 97 : 107;
         if (var2 != var1) {
            throw new D("Nesting error.");
         } else {
            --this.top;
            this.mode = (char)(this.top == 0 ? 100 : (this.stack[this.top - 1] == null ? 97 : 107));
         }
      }
   }

   public JSONWriter value(long var1) throws D {
      return this.append(Long.toString(var1));
   }
}
