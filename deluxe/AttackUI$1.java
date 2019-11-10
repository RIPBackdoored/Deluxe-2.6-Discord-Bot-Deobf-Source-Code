package deluxe;

import java.io.IOException;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// $FF: synthetic class
public class AttackUI$1 extends Thread {
   public final String val$token;
   public final JTextArea val$serverSpamArea;
   public final JSlider val$slider;
   public final JCheckBox val$ttsBox;
   public final JTextField val$serverSpamChannelId;
   public final AttackUI$6 this$1;

   public AttackUI$1(AttackUI$6 var1, JTextField var2, JTextArea var3, JCheckBox var4, String var5, JSlider var6) {
      this.this$1 = var1;
      this.val$serverSpamChannelId = var2;
      this.val$serverSpamArea = var3;
      this.val$ttsBox = var4;
      this.val$token = var5;
      this.val$slider = var6;
   }

   public void run() {
      while(AW.access$2()) {
         try {
            AW.access$0().sendMsg(this.val$serverSpamChannelId.getText(), this.val$serverSpamArea.getText(), this.val$ttsBox.isSelected(), this.val$token, N.getRandomProxy());
         } catch (IOException var3) {
            var3.printStackTrace();
         }

         try {
            Thread.sleep((long)this.val$slider.getValue());
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

   }
}
