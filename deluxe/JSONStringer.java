package deluxe;

import java.io.StringWriter;

// $FF: synthetic class
public class JSONStringer extends JSONWriter {
   public JSONStringer() {
      super(new StringWriter());
   }

   public String toString() {
      return this.mode == 'd' ? this.writer.toString() : null;
   }
}
