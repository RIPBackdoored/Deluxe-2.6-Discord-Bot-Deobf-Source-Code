package deluxe;

import java.io.IOException;
import javax.swing.JTextField;

// $FF: synthetic class
public class AttackUI$7 extends Thread {
   public final String val$token;
   public final AttackUI$5 this$1;
   public final JTextField val$leaveTextField;

   public void run() {
      byte var1 = 1;

      while(var1 > 0) {
         try {
            String var2 = SpamUtils.getRandomProxy();
            AW.access$0().leave(this.val$leaveTextField.getText(), this.val$token, var2);
         } catch (IOException var3) {
         }
      }

   }

   public AttackUI$7(AttackUI$5 var1, JTextField var2, String var3) {
      this.this$1 = var1;
      this.val$leaveTextField = var2;
      this.val$token = var3;
   }
}
