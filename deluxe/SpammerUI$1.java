package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

// $FF: synthetic class
public class SpammerUI$1 implements ActionListener {
   public final AQ this$0;

   public void actionPerformed(ActionEvent var1) {
      try {
         ArrayList var2 = (ArrayList)Files.readAllLines(Paths.get(AC.loadTextFile().toString()), Charset.defaultCharset());
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var3 = (String)var4.next();
            ++AQ.tokenCount;
            AQ.tokens.add(var3);
            AQ.tokenModel.addRow(new Object[]{AQ.tokenCount, var3, "Æ/"});
         }

         System.out.println(AQ.tokens);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public SpammerUI$1(AQ var1) {
      this.this$0 = var1;
   }
}
