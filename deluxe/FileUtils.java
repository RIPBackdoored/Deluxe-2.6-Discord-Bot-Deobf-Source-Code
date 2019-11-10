package deluxe;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JFileChooser;

// $FF: synthetic class
public class FileUtils {
   public static String readFile(String var0) throws IOException {
      String var1 = null;
      File var2 = new File(var0);
      FileReader var3 = null;

      try {
         var3 = new FileReader(var2);
         char[] var4 = new char[(int)var2.length()];
         var3.read(var4);
         var1 = new String(var4);
         var3.close();
      } catch (IOException var8) {
         var8.printStackTrace();
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

      return var1;
   }

   public static File loadTextFile() throws FileNotFoundException {
      JFileChooser var0 = new JFileChooser();
      var0.setCurrentDirectory(new File(System.getProperty("user.home")));
      int var1 = var0.showOpenDialog((Component)null);
      File var2 = null;
      if (var1 == 0) {
         var2 = var0.getSelectedFile();
      }

      return var2;
   }

   public static String readTextFile(String var0) throws FileNotFoundException {
      String var1 = null;
      Random var2 = new Random();
      int var3 = 0;
      Scanner var4 = new Scanner(var0);

      while(var4.hasNext()) {
         ++var3;
         String var5 = var4.nextLine();
         if (var2.nextInt(var3) == 0) {
            var1 = var5;
         }
      }

      return var1;
   }

   public static void writeFile(String var0, String var1) {
      try {
         Files.write(Paths.get(var0), var1.getBytes(), new OpenOption[0]);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
