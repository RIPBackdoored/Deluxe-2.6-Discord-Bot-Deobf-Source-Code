package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

// $FF: synthetic class
public class SpammerUI$11 implements ActionListener {
   public final SpammerUI this$0;

   public void actionPerformed(ActionEvent var1) {
      try {
         HttpGet var2 = new HttpGet("https://spamis.fun/DELUXE/version.txt");
         CloseableHttpClient var3 = HttpClients.createDefault();
         CloseableHttpResponse var4 = var3.execute(var2);
         HttpEntity var5 = var4.getEntity();
         String var6 = EntityUtils.toString(var5);
         if (new Double(var6) > 2.6D) {
            JOptionPane.showMessageDialog(SpammerUI.access$0(this.this$0), "Your version is outdated! Please update to " + var6 + " (Same download link)");
         } else {
            JOptionPane.showMessageDialog(SpammerUI.access$0(this.this$0), "Your version is up to date!");
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public SpammerUI$11(SpammerUI var1) {
      this.this$0 = var1;
   }
}
