package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// $FF: synthetic class
public class AttackUI$4 implements ActionListener {
   public final AW this$0;

   public void actionPerformed(ActionEvent var1) {
      AW.access$1(false);
      this.this$0.btnStartServerSpam.setEnabled(true);
      this.this$0.btnStopServerSpam.setEnabled(false);
   }

   public AttackUI$4(AW var1) {
      this.this$0 = var1;
   }
}
