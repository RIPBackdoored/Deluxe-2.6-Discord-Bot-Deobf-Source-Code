package deluxe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// $FF: synthetic class
public class SpammerUI extends JFrame {
   public static String pw;
   public static final long serialVersionUID = 1L;
   public static int tokenCount = 0;
   public static ArrayList tokens = new ArrayList();
   public static boolean loggedIn;
   public static String user;
   public static int proxyCount = 0;
   public JPanel contentPane;
   public static ArrayList proxys = new ArrayList();
   public static DefaultTableModel tokenModel;

   public SpammerUI() throws IOException {
      this.setResizable(false);
      this.setTitle("DELUXE - v2.6 - SPAMIS.FUN");
      this.setDefaultCloseOperation(3);
      this.setBounds(100, 100, 494, 650);
      Dimension var1 = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation(var1.width / 2 - this.getSize().width / 2, var1.height / 2 - this.getSize().height / 2);
      this.contentPane = new JPanel();
      this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.setContentPane(this.contentPane);
      this.contentPane.setLayout((LayoutManager)null);
      JTabbedPane var2 = new JTabbedPane(1);
      var2.setBounds(0, 81, 490, 540);
      this.contentPane.add(var2);
      JPanel var3 = new JPanel();
      var2.addTab("Proxys", new ImageIcon(this.getClass().getResource("/ui/notchecked.png")), var3, "Import your proxys here");
      var3.setLayout((LayoutManager)null);
      JLabel var4 = new JLabel("Total proxys: 0");
      var3.add(var4);
      String[] var5 = new String[0];
      Object[][] var6 = new Object[0][];
      AJ var7 = new AJ(this, var6, var5);
      DefaultTableModel var8 = new DefaultTableModel(new String[]{"}:", "vg[oxy"}, 0);
      var7.setModel(var8);
      var7.getTableHeader().setReorderingAllowed(false);
      var7.getColumnModel().getColumn(1).setPreferredWidth(400);
      var7.setAutoResizeMode(3);
      JScrollPane var9 = new JScrollPane(var7, 22, 30);
      var9.setBounds(-1, 45, 487, 466);
      var3.add(var9);
      JButton var10 = new JButton("Import proxys");
      var10.setIcon(new ImageIcon(this.getClass().getResource("/ui/plus.png")));
      var10.addActionListener(new AZ(this, var8));
      var10.setBackground(Color.DARK_GRAY);
      var10.setBounds(80, 6, 151, 33);
      var3.add(var10);
      JButton var11 = new JButton("Remove proxys");
      var11.addActionListener(new AB(this, var8));
      var11.setIcon(new ImageIcon(this.getClass().getResource("/ui/remove.png")));
      var11.setBackground(Color.DARK_GRAY);
      var11.setBounds(241, 6, 151, 33);
      var3.add(var11);
      JPanel var12 = new JPanel();
      var12.setLayout((LayoutManager)null);
      var2.addTab("Tokens", new ImageIcon(this.getClass().getResource("/ui/notchecked.png")), var12, "Import your tokens here");
      String[] var13 = new String[0];
      Object[][] var14 = new Object[0][];
      AI var15 = new AI(this, var14, var13);
      tokenModel = new DefaultTableModel(new String[]{"}:", "rkXken", "Status"}, 0);
      var15.setModel(tokenModel);
      var15.getTableHeader().setReorderingAllowed(false);
      var15.getColumnModel().getColumn(1).setPreferredWidth(350);
      var15.setAutoResizeMode(3);
      JScrollPane var16 = new JScrollPane(var15, 22, 30);
      var16.setBounds(-1, 45, 487, 466);
      var12.add(var16);
      JButton var17 = new JButton("Import");
      var17.setIcon(new ImageIcon(this.getClass().getResource("/ui/plus.png")));
      var17.addActionListener(new F(this));
      var17.setBackground(Color.DARK_GRAY);
      var17.setBounds(10, 6, 106, 33);
      var12.add(var17);
      JButton var18 = new JButton("Attack");
      var18.setIcon(new ImageIcon(this.getClass().getResource("/ui/start.png")));
      var18.addActionListener(new BB(this));
      var18.setBackground(Color.DARK_GRAY);
      var18.setBounds(126, 6, 106, 33);
      var12.add(var18);
      JButton var19 = new JButton("Remove");
      var19.setIcon(new ImageIcon(this.getClass().getResource("/ui/remove.png")));
      var19.addActionListener(new AG(this));
      var19.setBackground(Color.DARK_GRAY);
      var19.setBounds(253, 6, 106, 33);
      var12.add(var19);
      JButton var20 = new JButton("Export working");
      var20.setIcon(new ImageIcon(this.getClass().getResource("/ui/export.png")));
      var20.addActionListener(new AN(this));
      var20.setBackground(Color.DARK_GRAY);
      var20.setBounds(369, 6, 106, 33);
      var12.add(var20);
      JPanel var21 = new JPanel();
      var21.setLayout((LayoutManager)null);
      var2.addTab("Settings / Help", new ImageIcon(this.getClass().getResource("/ui/notchecked.png")), var21, "Yaya");
      JButton var22 = new JButton("Check for updates");
      var22.addActionListener(new AU(this));
      var22.setBackground(Color.DARK_GRAY);
      var22.setBounds(177, 463, 124, 36);
      var21.add(var22);
      JLabel var23 = new JLabel("How to create tokens?");
      var23.addMouseListener(new BD(this));
      var23.setFont(new Font("Tahoma", 0, 13));
      var23.setForeground(Color.BLUE);
      var23.setBounds(10, 25, 153, 23);
      var21.add(var23);
      JLabel var24 = new JLabel("Free proxys");
      var24.addMouseListener(new AR(this));
      var24.setForeground(Color.BLUE);
      var24.setFont(new Font("Tahoma", 0, 13));
      var24.setBounds(10, 72, 153, 23);
      var21.add(var24);
      JLabel var25 = new JLabel("Proxy checker");
      var25.addMouseListener(new AO(this));
      var25.setForeground(Color.BLUE);
      var25.setFont(new Font("Tahoma", 0, 13));
      var25.setBounds(10, 93, 153, 23);
      var21.add(var25);
      JLabel var26 = new JLabel("Help (How to use this tool?)");
      var26.addMouseListener(new C(this));
      var26.setForeground(Color.BLUE);
      var26.setFont(new Font("Tahoma", 0, 13));
      var26.setBounds(10, 145, 201, 23);
      var21.add(var26);
      BufferedImage var27 = ImageIO.read(this.getClass().getResource("/ui/long-banner.png"));
      JLabel var28 = new JLabel(new ImageIcon(var27));
      var28.setBounds(0, 0, 488, 110);
      this.contentPane.add(var28);
      if (!loggedIn || user.isEmpty() || pw.isEmpty()) {
         System.exit(0);
      }

   }
}
