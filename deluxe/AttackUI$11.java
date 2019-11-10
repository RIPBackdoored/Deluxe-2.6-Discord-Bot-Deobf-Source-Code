package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JTextField;

// $FF: synthetic class
public class AttackUI$11 implements ActionListener {
   public final JTextField val$inviteTextField;
   public final AttackUI this$0;

   public AttackUI$11(AttackUI var1, JTextField var2) {
      this.this$0 = var1;
      this.val$inviteTextField = var2;
   }

   public void actionPerformed(ActionEvent var1) {
      SpammerUI.tokenCount = 0;

      for(int var2 = SpammerUI.tokenModel.getRowCount() - 1; var2 >= 0; --var2) {
         SpammerUI.tokenModel.removeRow(var2);
      }

      Iterator var3 = SpammerUI.tokens.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         (new AH(this, this.val$inviteTextField, var4)).start();
      }

   }
}
