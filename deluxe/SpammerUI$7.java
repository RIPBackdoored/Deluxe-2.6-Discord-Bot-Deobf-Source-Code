package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// $FF: synthetic class
public class SpammerUI$7 implements ActionListener {
   public final SpammerUI this$0;

   public void actionPerformed(ActionEvent var1) {
      for(int var2 = SpammerUI.tokenModel.getRowCount() - 1; var2 >= 0; --var2) {
         SpammerUI.tokenModel.removeRow(var2);
         SpammerUI.tokens.clear();
         SpammerUI.tokenCount = 0;
      }

   }

   public SpammerUI$7(SpammerUI var1) {
      this.this$0 = var1;
   }
}
