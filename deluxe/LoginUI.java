package deluxe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import me.roman.deluxe.Main;

// $FF: synthetic class
public class LoginUI extends JFrame {
   public static final long serialVersionUID = 1L;
   public JPasswordField password;
   public JPanel contentPane;
   public JTextField user;

   public LoginUI() {
      this.setResizable(false);
      this.setTitle("DELUXE - v2.6 - Login");
      this.setDefaultCloseOperation(3);
      this.setBounds(100, 100, 290, 200);
      Dimension var1 = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation(var1.width / 2 - this.getSize().width / 2, var1.height / 2 - this.getSize().height / 2);
      this.contentPane = new JPanel();
      this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.setContentPane(this.contentPane);
      this.contentPane.setLayout((LayoutManager)null);
      this.user = new JTextField();
      this.user.setBounds(10, 24, 262, 27);
      this.contentPane.add(this.user);
      this.user.setColumns(10);
      this.password = new JPasswordField();
      this.password.setColumns(10);
      this.password.setBounds(10, 72, 262, 27);
      this.contentPane.add(this.password);
      JLabel var2 = new JLabel("Username");
      var2.setBounds(10, 11, 264, 14);
      this.contentPane.add(var2);
      JLabel var3 = new JLabel("Password");
      var3.setBounds(10, 57, 264, 14);
      this.contentPane.add(var3);
      JCheckBox var4 = new JCheckBox("Remember me");
      var4.setSelected(true);
      var4.setBounds(10, 106, 97, 23);
      this.contentPane.add(var4);
      JButton var5 = new JButton("Login");
      var5.addActionListener(new J(this, var5, var4));
      var5.setBackground(Color.DARK_GRAY);
      var5.setBounds(10, 136, 262, 23);
      this.contentPane.add(var5);
      if ((new File(Main.homeDir + "remember.deluxe")).exists()) {
         try {
            String[] var6 = AC.readFile(Main.homeDir + "remember.deluxe").split("\n");
            this.user.setText(A.AESdecoder(var6[0]));
            this.password.setText(A.AESdecoder(var6[1]));
            var4.setSelected(true);
         } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | IOException var8) {
            var8.printStackTrace();
         }
      }

   }
}
