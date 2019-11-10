package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

// $FF: synthetic class
public class SpammerUI$3 implements ActionListener {
   public final DefaultTableModel val$proxyModel;
   public final AQ this$0;

   public void actionPerformed(ActionEvent var1) {
      for(int var2 = this.val$proxyModel.getRowCount() - 1; var2 >= 0; --var2) {
         this.val$proxyModel.removeRow(var2);
         AQ.proxys.clear();
         AQ.proxyCount = 0;
      }

   }

   public SpammerUI$3(AQ var1, DefaultTableModel var2) {
      this.this$0 = var1;
      this.val$proxyModel = var2;
   }
}
