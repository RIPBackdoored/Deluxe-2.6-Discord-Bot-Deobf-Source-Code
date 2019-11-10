package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JTextField;

// $FF: synthetic class
public class AttackUI$5 implements ActionListener {
   public final AW this$0;
   public final JTextField val$leaveTextField;

   public void actionPerformed(ActionEvent var1) {
      Iterator var3 = AQ.tokens.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         (new H(this, this.val$leaveTextField, var2)).start();
      }

   }

   public AttackUI$5(AW var1, JTextField var2) {
      this.this$0 = var1;
      this.val$leaveTextField = var2;
   }
}
