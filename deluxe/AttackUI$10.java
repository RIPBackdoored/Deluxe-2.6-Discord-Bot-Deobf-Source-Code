package deluxe;

import java.io.IOException;
import javax.swing.JTextField;

// $FF: synthetic class
public class AttackUI$10 extends Thread {
   public final AttackUI$2 this$1;
   public final JTextField val$friendTextField;
   public final String val$token;

   public void run() {
      while(AW.access$4()) {
         try {
            AW.access$0().addFriend(this.val$friendTextField.getText(), this.val$token, SpamUtils.getRandomProxy());
         } catch (IOException var4) {
            var4.printStackTrace();
         }

         try {
            Thread.sleep(5000L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }

         try {
            AW.access$0().deleteFriend(this.val$friendTextField.getText(), this.val$token, SpamUtils.getRandomProxy());
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

   }

   public AttackUI$10(AttackUI$2 var1, JTextField var2, String var3) {
      this.this$1 = var1;
      this.val$friendTextField = var2;
      this.val$token = var3;
   }
}
