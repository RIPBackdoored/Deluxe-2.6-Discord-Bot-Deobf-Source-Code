package deluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import me.roman.deluxe.Main;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

// $FF: synthetic class
public class LoginUI$1 implements ActionListener {
   public final JButton val$btnNewButton;
   public final JCheckBox val$saveCheckBox;
   public final LoginUI this$0;

   public void actionPerformed(ActionEvent var1) {
      if (!LoginUI.access$0(this.this$0).getText().isEmpty() && !LoginUI.access$1(this.this$0).getText().isEmpty()) {
         this.val$btnNewButton.setEnabled(false);

         try {
            HttpPost var2 = new HttpPost("https://spamis.fun/DELUXE/api.php");
            CloseableHttpClient var3 = HttpClients.createDefault();
            ArrayList var4 = new ArrayList(2);
            var4.add(new BasicNameValuePair("username", LoginUI.access$0(this.this$0).getText()));
            var4.add(new BasicNameValuePair("password", LoginUI.access$1(this.this$0).getText()));
            var2.setEntity(new UrlEncodedFormEntity(var4, "UTF-8"));
            CloseableHttpResponse var5 = var3.execute(var2);
            HttpEntity var6 = var5.getEntity();
            String var7 = EntityUtils.toString(var6);
            System.out.println(var7);
            if (var7.equals("Yep")) {
               AQ.user = LoginUI.access$0(this.this$0).getText();
               AQ.pw = LoginUI.access$1(this.this$0).getText();
               AQ.loggedIn = true;
               AQ var8 = new AQ();
               var8.setVisible(true);
               this.this$0.setVisible(false);
               if (this.val$saveCheckBox.isSelected()) {
                  try {
                     AC.writeFile(Main.homeDir + "remember.deluxe", A.AESencoder(LoginUI.access$0(this.this$0).getText()) + "\n" + A.AESencoder(LoginUI.access$1(this.this$0).getText()));
                  } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException var10) {
                     var10.printStackTrace();
                  } catch (UnsupportedEncodingException var11) {
                     var11.printStackTrace();
                  }
               } else {
                  (new File(Main.homeDir + "remember.deluxe")).delete();
               }
            } else {
               JOptionPane.showMessageDialog(LoginUI.access$2(this.this$0), "Login failed! Try again!");
               this.val$btnNewButton.setEnabled(true);
            }
         } catch (IOException var12) {
            var12.printStackTrace();
         }

      }
   }

   public LoginUI$1(LoginUI var1, JButton var2, JCheckBox var3) {
      this.this$0 = var1;
      this.val$btnNewButton = var2;
      this.val$saveCheckBox = var3;
   }
}
