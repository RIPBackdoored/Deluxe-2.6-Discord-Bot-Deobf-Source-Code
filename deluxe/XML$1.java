package deluxe;

import java.util.Iterator;

// $FF: synthetic class
public class XML$1 implements Iterator {
   public final XML$2 this$0;
   public int nextIndex;
   public int length;

   public XML$1(XML$2 var1) {
      this.this$0 = var1;
      this.nextIndex = 0;
      this.length = this.this$0.val$string.length();
   }

   public boolean hasNext() {
      return this.nextIndex < this.length;
   }

   public Integer next() {
      int var1 = this.this$0.val$string.codePointAt(this.nextIndex);
      this.nextIndex += Character.charCount(var1);
      return var1;
   }

   // $FF: synthetic method
   public Object next() {
      return this.next();
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
