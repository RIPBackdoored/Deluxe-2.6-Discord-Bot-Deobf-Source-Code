package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

// $FF: synthetic class
public class SpammerUI$12 implements ActionListener {
   public final SpammerUI this$0;
   public final DefaultTableModel val$proxyModel;

   public void actionPerformed(ActionEvent var1) {
      try {
         ArrayList var2 = (ArrayList)Files.readAllLines(Paths.get(FileUtils.loadTextFile().toString()), Charset.defaultCharset());
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var3 = (String)var4.next();
            ++SpammerUI.proxyCount;
            SpammerUI.proxys.add(var3);
            this.val$proxyModel.addRow(new Object[]{SpammerUI.proxyCount, var3});
         }

         System.out.println(SpammerUI.proxys);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public SpammerUI$12(SpammerUI var1, DefaultTableModel var2) {
      this.this$0 = var1;
      this.val$proxyModel = var2;
   }
}
