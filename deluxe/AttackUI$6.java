package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// $FF: synthetic class
public class AttackUI$6 implements ActionListener {
   public final JSlider val$slider;
   public final AW this$0;
   public final JTextArea val$serverSpamArea;
   public final JTextField val$serverSpamChannelId;
   public final JCheckBox val$ttsBox;

   public void actionPerformed(ActionEvent var1) {
      AW.access$1(true);
      this.this$0.btnStartServerSpam.setEnabled(false);
      this.this$0.btnStopServerSpam.setEnabled(true);
      Iterator var3 = AQ.tokens.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         (new Z(this, this.val$serverSpamChannelId, this.val$serverSpamArea, this.val$ttsBox, var2, this.val$slider)).start();
      }

   }

   public AttackUI$6(AW var1, JTextField var2, JTextArea var3, JCheckBox var4, JSlider var5) {
      this.this$0 = var1;
      this.val$serverSpamChannelId = var2;
      this.val$serverSpamArea = var3;
      this.val$ttsBox = var4;
      this.val$slider = var5;
   }
}
