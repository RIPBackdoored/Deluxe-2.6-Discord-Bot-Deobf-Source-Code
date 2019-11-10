package deluxe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

// $FF: synthetic class
public class JSONTokener {
   public long line;
   public boolean usePrevious;
   public boolean eof;
   public long character;
   public long index;
   public long characterPreviousLine;
   public char previous;
   public final Reader reader;

   public JSONException syntaxError(String var1, Throwable var2) {
      return new D(var1 + this.toString(), var2);
   }

   public JSONTokener(Reader var1) {
      this.reader = (Reader)(var1.markSupported() ? var1 : new BufferedReader(var1));
      this.eof = false;
      this.usePrevious = false;
      this.previous = 0;
      this.index = 0L;
      this.character = 1L;
      this.characterPreviousLine = 0L;
      this.line = 1L;
   }

   public String nextString(char var1) throws D {
      StringBuilder var3 = new StringBuilder();

      while(true) {
         char var2 = this.next();
         switch(var2) {
         case '\u0000':
         case '\n':
         case '\r':
            throw this.syntaxError("Unterminated string");
         case '\\':
            var2 = this.next();
            switch(var2) {
            case '"':
            case '\'':
            case '/':
            case '\\':
               var3.append(var2);
               continue;
            case 'b':
               var3.append('\b');
               continue;
            case 'f':
               var3.append('\f');
               continue;
            case 'n':
               var3.append('\n');
               continue;
            case 'r':
               var3.append('\r');
               continue;
            case 't':
               var3.append('\t');
               continue;
            case 'u':
               try {
                  var3.append((char)Integer.parseInt(this.next((int)4), 16));
                  continue;
               } catch (NumberFormatException var5) {
                  throw this.syntaxError("Illegal escape.", var5);
               }
            default:
               throw this.syntaxError("Illegal escape.");
            }
         default:
            if (var2 == var1) {
               return var3.toString();
            }

            var3.append(var2);
         }
      }
   }

   public char skipTo(char var1) throws D {
      char var2;
      try {
         long var3 = this.index;
         long var5 = this.character;
         long var7 = this.line;
         this.reader.mark(1000000);

         while(true) {
            var2 = this.next();
            if (var2 == 0) {
               this.reader.reset();
               this.index = var3;
               this.character = var5;
               this.line = var7;
               return '\u0000';
            }

            if (var2 == var1) {
               this.reader.mark(1);
               break;
            }
         }
      } catch (IOException var9) {
         throw new D(var9);
      }

      this.back();
      return var2;
   }

   public void decrementIndexes() {
      --this.index;
      if (this.previous != '\r' && this.previous != '\n') {
         if (this.character > 0L) {
            --this.character;
         }
      } else {
         --this.line;
         this.character = this.characterPreviousLine;
      }

   }

   public JSONTokener(String var1) {
      this((Reader)(new StringReader(var1)));
   }

   public JSONException syntaxError(String var1) {
      return new D(var1 + this.toString());
   }

   public char nextClean() throws D {
      char var1;
      do {
         var1 = this.next();
      } while(var1 != 0 && var1 <= ' ');

      return var1;
   }

   public char next() throws D {
      int var1;
      if (this.usePrevious) {
         this.usePrevious = false;
         var1 = this.previous;
      } else {
         try {
            var1 = this.reader.read();
         } catch (IOException var3) {
            throw new D(var3);
         }
      }

      if (var1 <= 0) {
         this.eof = true;
         return '\u0000';
      } else {
         this.incrementIndexes(var1);
         this.previous = (char)var1;
         return this.previous;
      }
   }

   public boolean more() throws D {
      if (this.usePrevious) {
         return true;
      } else {
         try {
            this.reader.mark(1);
         } catch (IOException var3) {
            throw new D("Unable to preserve stream position", var3);
         }

         try {
            if (this.reader.read() <= 0) {
               this.eof = true;
               return false;
            } else {
               this.reader.reset();
               return true;
            }
         } catch (IOException var2) {
            throw new D("Unable to read the next character from the stream", var2);
         }
      }
   }

   public void incrementIndexes(int var1) {
      if (var1 > 0) {
         ++this.index;
         if (var1 == 13) {
            ++this.line;
            this.characterPreviousLine = this.character;
            this.character = 0L;
         } else if (var1 == 10) {
            if (this.previous != '\r') {
               ++this.line;
               this.characterPreviousLine = this.character;
            }

            this.character = 0L;
         } else {
            ++this.character;
         }
      }

   }

   public boolean end() {
      return this.eof && !this.usePrevious;
   }

   public Object nextValue() throws D {
      char var1 = this.nextClean();
      switch(var1) {
      case '"':
      case '\'':
         return this.nextString(var1);
      case '[':
         this.back();
         return new BE(this);
      case '{':
         this.back();
         return new B(this);
      default:
         StringBuilder var3;
         for(var3 = new StringBuilder(); var1 >= ' ' && ",:]}/\\\"[{;=#".indexOf(var1) < 0; var1 = this.next()) {
            var3.append(var1);
         }

         this.back();
         String var2 = var3.toString().trim();
         if ("".equals(var2)) {
            throw this.syntaxError("Missing value");
         } else {
            return JSONObject.stringToValue(var2);
         }
      }
   }

   public void back() throws D {
      if (!this.usePrevious && this.index > 0L) {
         this.decrementIndexes();
         this.usePrevious = true;
         this.eof = false;
      } else {
         throw new D("Stepping back two steps is not supported");
      }
   }

   public String nextTo(char var1) throws D {
      StringBuilder var2 = new StringBuilder();

      while(true) {
         char var3 = this.next();
         if (var3 == var1 || var3 == 0 || var3 == '\n' || var3 == '\r') {
            if (var3 != 0) {
               this.back();
            }

            return var2.toString().trim();
         }

         var2.append(var3);
      }
   }

   public String next(int var1) throws D {
      if (var1 == 0) {
         return "";
      } else {
         char[] var2 = new char[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = this.next();
            if (this.end()) {
               throw this.syntaxError("Substring bounds error");
            }
         }

         return new String(var2);
      }
   }

   public String toString() {
      return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
   }

   public String nextTo(String var1) throws D {
      StringBuilder var3 = new StringBuilder();

      while(true) {
         char var2 = this.next();
         if (var1.indexOf(var2) >= 0 || var2 == 0 || var2 == '\n' || var2 == '\r') {
            if (var2 != 0) {
               this.back();
            }

            return var3.toString().trim();
         }

         var3.append(var2);
      }
   }

   public static int dehexchar(char var0) {
      if (var0 >= '0' && var0 <= '9') {
         return var0 - 48;
      } else if (var0 >= 'A' && var0 <= 'F') {
         return var0 - 55;
      } else {
         return var0 >= 'a' && var0 <= 'f' ? var0 - 87 : -1;
      }
   }

   public JSONTokener(InputStream var1) {
      this((Reader)(new InputStreamReader(var1)));
   }

   public char next(char var1) throws D {
      char var2 = this.next();
      if (var2 != var1) {
         if (var2 > 0) {
            throw this.syntaxError("Expected '" + var1 + "' and instead saw '" + var2 + "'");
         } else {
            throw this.syntaxError("Expected '" + var1 + "' and instead saw ''");
         }
      } else {
         return var2;
      }
   }
}
