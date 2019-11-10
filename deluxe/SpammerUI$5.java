package deluxe;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// $FF: synthetic class
public class SpammerUI$5 extends MouseAdapter {
   public final AQ this$0;

   public void mouseClicked(MouseEvent var1) {
      try {
         Desktop.getDesktop().browse(new URI("https://checkerproxy.net"));
      } catch (URISyntaxException | IOException var3) {
         var3.printStackTrace();
      }

   }

   public SpammerUI$5(AQ var1) {
      this.this$0 = var1;
   }
}
