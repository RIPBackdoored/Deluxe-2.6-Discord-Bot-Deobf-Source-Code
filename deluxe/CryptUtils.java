package deluxe;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

// $FF: synthetic class
public class CryptUtils {
   public static String password = "hpAGcYDydvcxh4sdNBFWFMMSHxaCZY8p";

   public static String convertToMD5(String var0) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("MD5");
         byte[] var2 = var1.digest(var0.getBytes());
         StringBuffer var3 = new StringBuffer();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.append(Integer.toHexString(var2[var4] & 255 | 256).substring(1, 3));
         }

         return var3.toString();
      } catch (NoSuchAlgorithmException var5) {
         return null;
      }
   }

   public static String AESencoder(String var0) throws InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException {
      byte[] var1 = password.getBytes("UTF-8");
      MessageDigest var2 = MessageDigest.getInstance("SHA-256");
      var1 = var2.digest(var1);
      var1 = Arrays.copyOf(var1, 16);
      SecretKeySpec var3 = new SecretKeySpec(var1, "AES");
      Cipher var4 = Cipher.getInstance("AES");
      var4.init(1, var3);
      byte[] var5 = var4.doFinal(var0.getBytes());
      String var6 = Base64.getEncoder().encodeToString(var5);
      return var6;
   }

   public static String AESdecoder(String var0) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IOException, NoSuchPaddingException, IllegalBlockSizeException {
      byte[] var1 = Base64.getDecoder().decode(var0);
      byte[] var2 = password.getBytes("UTF-8");
      MessageDigest var3 = MessageDigest.getInstance("SHA-256");
      var2 = var3.digest(var2);
      var2 = Arrays.copyOf(var2, 16);
      SecretKeySpec var4 = new SecretKeySpec(var2, "AES");
      Cipher var5 = Cipher.getInstance("AES");
      var5.init(2, var4);
      byte[] var6 = var5.doFinal(var1);
      String var7 = new String(var6);
      return var7;
   }
}
