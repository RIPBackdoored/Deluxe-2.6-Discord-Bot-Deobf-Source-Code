package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// $FF: synthetic class
public class AttackUI$3 implements ActionListener {
   public final AW this$0;

   public AttackUI$3(AW var1) {
      this.this$0 = var1;
   }

   public void actionPerformed(ActionEvent var1) {
      AW.access$3(false);
      this.this$0.btnFriendStart.setEnabled(true);
      this.this$0.btnFriendStop.setEnabled(false);
   }
}
