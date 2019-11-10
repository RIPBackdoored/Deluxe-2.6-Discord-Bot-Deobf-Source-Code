package deluxe;

import java.io.Reader;
import java.util.HashMap;

// $FF: synthetic class
public class XMLTokener extends AE {
   public static final HashMap entity = new HashMap(8);

   public Object nextMeta() throws D {
      char var1;
      do {
         var1 = this.next();
      } while(Character.isWhitespace(var1));

      switch(var1) {
      case '\u0000':
         throw this.syntaxError("Misshaped meta tag");
      case '!':
         return G.BANG;
      case '"':
      case '\'':
         char var2 = var1;

         do {
            var1 = this.next();
            if (var1 == 0) {
               throw this.syntaxError("Unterminated string");
            }
         } while(var1 != var2);

         return Boolean.TRUE;
      case '/':
         return G.SLASH;
      case '<':
         return G.LT;
      case '=':
         return G.EQ;
      case '>':
         return G.GT;
      case '?':
         return G.QUEST;
      default:
         while(true) {
            var1 = this.next();
            if (Character.isWhitespace(var1)) {
               return Boolean.TRUE;
            }

            switch(var1) {
            case '\u0000':
            case '!':
            case '"':
            case '\'':
            case '/':
            case '<':
            case '=':
            case '>':
            case '?':
               this.back();
               return Boolean.TRUE;
            }
         }
      }
   }

   public Object nextEntity(char var1) throws D {
      StringBuilder var2 = new StringBuilder();

      while(true) {
         char var3 = this.next();
         if (!Character.isLetterOrDigit(var3) && var3 != '#') {
            if (var3 == ';') {
               String var4 = var2.toString();
               return unescapeEntity(var4);
            }

            throw this.syntaxError("Missing ';' in XML entity: &" + var2);
         }

         var2.append(Character.toLowerCase(var3));
      }
   }

   public XMLTokener(Reader var1) {
      super(var1);
   }

   public XMLTokener(String var1) {
      super(var1);
   }

   public static String unescapeEntity(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         if (var0.charAt(0) == '#') {
            int var2;
            if (var0.charAt(1) == 'x') {
               var2 = Integer.parseInt(var0.substring(2), 16);
            } else {
               var2 = Integer.parseInt(var0.substring(1));
            }

            return new String(new int[]{var2}, 0, 1);
         } else {
            Character var1 = (Character)entity.get(var0);
            return var1 == null ? '&' + var0 + ';' : var1.toString();
         }
      } else {
         return "";
      }
   }

   public String nextCDATA() throws D {
      StringBuilder var3 = new StringBuilder();

      int var2;
      do {
         if (!this.more()) {
            throw this.syntaxError("Unclosed CDATA");
         }

         char var1 = this.next();
         var3.append(var1);
         var2 = var3.length() - 3;
      } while(var2 < 0 || var3.charAt(var2) != ']' || var3.charAt(var2 + 1) != ']' || var3.charAt(var2 + 2) != '>');

      var3.setLength(var2);
      return var3.toString();
   }

   public Object nextContent() throws D {
      char var1;
      do {
         var1 = this.next();
      } while(Character.isWhitespace(var1));

      if (var1 == 0) {
         return null;
      } else if (var1 == '<') {
         return G.LT;
      } else {
         StringBuilder var2;
         for(var2 = new StringBuilder(); var1 != 0; var1 = this.next()) {
            if (var1 == '<') {
               this.back();
               return var2.toString().trim();
            }

            if (var1 == '&') {
               var2.append(this.nextEntity(var1));
            } else {
               var2.append(var1);
            }
         }

         return var2.toString().trim();
      }
   }

   public void skipPast(String var1) {
      int var6 = 0;
      int var7 = var1.length();
      char[] var8 = new char[var7];

      char var3;
      int var4;
      for(var4 = 0; var4 < var7; ++var4) {
         var3 = this.next();
         if (var3 == 0) {
            return;
         }

         var8[var4] = var3;
      }

      while(true) {
         int var5 = var6;
         boolean var2 = true;

         for(var4 = 0; var4 < var7; ++var4) {
            if (var8[var5] != var1.charAt(var4)) {
               var2 = false;
               break;
            }

            ++var5;
            if (var5 >= var7) {
               var5 -= var7;
            }
         }

         if (var2) {
            return;
         }

         var3 = this.next();
         if (var3 == 0) {
            return;
         }

         var8[var6] = var3;
         ++var6;
         if (var6 >= var7) {
            var6 -= var7;
         }
      }
   }

   public Object nextToken() throws D {
      char var1;
      do {
         var1 = this.next();
      } while(Character.isWhitespace(var1));

      StringBuilder var3;
      switch(var1) {
      case '\u0000':
         throw this.syntaxError("Misshaped element");
      case '!':
         return G.BANG;
      case '"':
      case '\'':
         char var2 = var1;
         var3 = new StringBuilder();

         while(true) {
            var1 = this.next();
            if (var1 == 0) {
               throw this.syntaxError("Unterminated string");
            }

            if (var1 == var2) {
               return var3.toString();
            }

            if (var1 == '&') {
               var3.append(this.nextEntity(var1));
            } else {
               var3.append(var1);
            }
         }
      case '/':
         return G.SLASH;
      case '<':
         throw this.syntaxError("Misplaced '<'");
      case '=':
         return G.EQ;
      case '>':
         return G.GT;
      case '?':
         return G.QUEST;
      default:
         var3 = new StringBuilder();

         while(true) {
            var3.append(var1);
            var1 = this.next();
            if (Character.isWhitespace(var1)) {
               return var3.toString();
            }

            switch(var1) {
            case '\u0000':
               return var3.toString();
            case '!':
            case '/':
            case '=':
            case '>':
            case '?':
            case '[':
            case ']':
               this.back();
               return var3.toString();
            case '"':
            case '\'':
            case '<':
               throw this.syntaxError("Bad character in a name");
            }
         }
      }
   }
}
