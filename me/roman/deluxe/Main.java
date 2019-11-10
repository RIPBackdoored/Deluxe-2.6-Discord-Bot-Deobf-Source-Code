package me.roman.deluxe;

import deluxe.AL;
import java.awt.EventQueue;
import java.io.File;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// $FF: synthetic class
public class Main {
   public static final String NAME = "Spammer";
   public static String homeDir = System.getProperty("user.home") + "/DELUXE/";
   public static final String AUTHOR = "SpamIs.fun";
   public static final String TOOL_NAME = "Discord Spammer";
   public static final double VERSION = 2.6D;

   public static void main(String[] var0) throws InstantiationException, ClassNotFoundException, UnsupportedLookAndFeelException, IllegalAccessException {
      System.out.println("Starting...");
      System.setProperty("http.agent", "Mozilla");
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      if (!(new File(homeDir)).isDirectory()) {
         System.out.println("First start!");
         (new File(homeDir)).mkdir();
         System.out.println("Created DELUXE Folder!");
      }

      EventQueue.invokeLater(new AL());
   }
}
