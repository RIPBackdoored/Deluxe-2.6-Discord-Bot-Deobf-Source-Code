package deluxe;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// $FF: synthetic class
public class AttackUI$8 implements ChangeListener {
   public final AW this$0;
   public final JLabel val$lblDelay;
   public final JSlider val$slider;

   public AttackUI$8(AW var1, JLabel var2, JSlider var3) {
      this.this$0 = var1;
      this.val$lblDelay = var2;
      this.val$slider = var3;
   }

   public void stateChanged(ChangeEvent var1) {
      this.val$lblDelay.setText("Delay: " + this.val$slider.getValue() + " ms");
   }
}
