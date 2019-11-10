package deluxe;

import java.io.IOException;
import javax.swing.JTextField;

// $FF: synthetic class
public class AttackUI$9 extends Thread {
   public final JTextField val$inviteTextField;
   public final AttackUI$11 this$1;
   public final String val$token;

   public AttackUI$9(AttackUI$11 var1, JTextField var2, String var3) {
      this.this$1 = var1;
      this.val$inviteTextField = var2;
      this.val$token = var3;
   }

   public void run() {
      byte var1 = 1;

      while(var1 > 0) {
         try {
            String var2 = SpamUtils.getRandomProxy();
            String var3 = AW.access$0().join(this.val$inviteTextField.getText().replace("https://discord.gg/", ""), this.val$token, var2);
            if (var3.startsWith("<")) {
               SpammerUI.proxys.remove(var2);
               System.out.println(SpammerUI.proxys.size());
               System.out.println("Error with the proxy. retrying!");
            } else if (!var3.startsWith("{\"code\": 0") && !var3.startsWith("{\"code\": 40002")) {
               if (var3.startsWith("{\"code\": 10006")) {
                  ++SpammerUI.tokenCount;
                  SpammerUI.tokenModel.addRow(new Object[]{SpammerUI.tokenCount, this.val$token, "banned on server"});
                  System.out.println("Banned on this server");
                  var1 = 0;
               } else if (var3.startsWith("{\"inviter\":")) {
                  ++SpammerUI.tokenCount;
                  SpammerUI.tokenModel.addRow(new Object[]{SpammerUI.tokenCount, this.val$token, "joined"});
                  System.out.println(var3);
                  var1 = 0;
               } else {
                  System.out.println(var3);
                  SpammerUI.tokens.remove(this.val$token);
                  ++SpammerUI.tokenCount;
                  SpammerUI.tokenModel.addRow(new Object[]{SpammerUI.tokenCount, this.val$token, "unknown error. wrong proxys?"});
                  System.out.println("Token is dead. skipping!");
                  var1 = 0;
               }
            } else {
               SpammerUI.tokens.remove(this.val$token);
               ++SpammerUI.tokenCount;
               SpammerUI.tokenModel.addRow(new Object[]{SpammerUI.tokenCount, this.val$token, "disabled/rate limited(please try again in a few minutes)"});
               System.out.println("Token is dead. skipping!");
               System.out.println(var3);
               var1 = 0;
            }
         } catch (IOException var4) {
         }
      }

   }
}
