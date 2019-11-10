package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JTextField;

// $FF: synthetic class
public class AttackUI$2 implements ActionListener {
   public final JTextField val$friendTextField;
   public final AW this$0;

   public AttackUI$2(AW var1, JTextField var2) {
      this.this$0 = var1;
      this.val$friendTextField = var2;
   }

   public void actionPerformed(ActionEvent var1) {
      AW.access$3(true);
      this.this$0.btnFriendStart.setEnabled(false);
      this.this$0.btnFriendStop.setEnabled(true);
      Iterator var3 = AQ.tokens.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         (new BA(this, this.val$friendTextField, var2)).start();
      }

   }
}
