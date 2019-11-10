package deluxe;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.JOptionPane;

// $FF: synthetic class
public class SpammerUI$4 implements ActionListener {
   public final AQ this$0;

   public SpammerUI$4(AQ var1) {
      this.this$0 = var1;
   }

   public void actionPerformed(ActionEvent var1) {
      if (AQ.tokens.size() == 0) {
         JOptionPane.showMessageDialog((Component)null, "Please import some tokens and let them join a server before export.");
      } else {
         try {
            File var2 = new File(AC.loadTextFile().toString());
            FileWriter var3 = new FileWriter(var2);
            Iterator var5 = AQ.tokens.iterator();

            while(var5.hasNext()) {
               String var4 = (String)var5.next();
               var3.write(var4 + "\r");
            }

            var3.close();
            JOptionPane.showMessageDialog((Component)null, "Working tokens exported to: " + var2.getPath());
         } catch (FileNotFoundException var6) {
            var6.printStackTrace();
         } catch (IOException var7) {
            var7.printStackTrace();
         }

      }
   }
}
