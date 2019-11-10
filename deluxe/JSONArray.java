package deluxe;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// $FF: synthetic class
public class JSONArray implements Iterable {
   public final ArrayList myArrayList;

   public Object optQuery(JSONPointer var1) {
      try {
         return var1.queryFrom(this);
      } catch (JSONPointerException var3) {
         return null;
      }
   }

   public int optInt(int var1, int var2) {
      Object var3 = this.opt(var1);
      if (JSONObject.NULL.equals(var3)) {
         return var2;
      } else if (var3 instanceof Number) {
         return ((Number)var3).intValue();
      } else if (var3 instanceof String) {
         try {
            return (new BigDecimal(var3.toString())).intValue();
         } catch (Exception var5) {
            return var2;
         }
      } else {
         return var2;
      }
   }

   public boolean optBoolean(int var1, boolean var2) {
      try {
         return this.getBoolean(var1);
      } catch (Exception var4) {
         return var2;
      }
   }

   public String join(String var1) throws D {
      int var2 = this.length();
      StringBuilder var3 = new StringBuilder();

      for(int var4 = 0; var4 < var2; ++var4) {
         if (var4 > 0) {
            var3.append(var1);
         }

         var3.append(JSONObject.valueToString(this.myArrayList.get(var4)));
      }

      return var3.toString();
   }

   public Enum optEnum(Class var1, int var2, Enum var3) {
      try {
         Object var4 = this.opt(var2);
         if (JSONObject.NULL.equals(var4)) {
            return var3;
         } else if (var1.isAssignableFrom(var4.getClass())) {
            Enum var5 = (Enum)var4;
            return var5;
         } else {
            return Enum.valueOf(var1, var4.toString());
         }
      } catch (IllegalArgumentException var6) {
         return var3;
      } catch (NullPointerException var7) {
         return var3;
      }
   }

   public BigDecimal getBigDecimal(int var1) throws D {
      Object var2 = this.get(var1);

      try {
         return new BigDecimal(var2.toString());
      } catch (Exception var4) {
         throw new D("JSONArray[" + var1 + "] could not convert to BigDecimal.", var4);
      }
   }

   public JSONArray put(int var1, boolean var2) throws D {
      return this.put(var1, (Object)(var2 ? Boolean.TRUE : Boolean.FALSE));
   }

   public JSONArray put(long var1) {
      return this.put((Object)var1);
   }

   public boolean isEmpty() {
      return this.myArrayList.isEmpty();
   }

   public JSONArray put(Collection var1) {
      return this.put((Object)(new BE(var1)));
   }

   public Object opt(int var1) {
      return var1 >= 0 && var1 < this.length() ? this.myArrayList.get(var1) : null;
   }

   public JSONArray put(float var1) throws D {
      return this.put((Object)var1);
   }

   public boolean getBoolean(int var1) throws D {
      Object var2 = this.get(var1);
      if (!var2.equals(Boolean.FALSE) && (!(var2 instanceof String) || !((String)var2).equalsIgnoreCase("false"))) {
         if (!var2.equals(Boolean.TRUE) && (!(var2 instanceof String) || !((String)var2).equalsIgnoreCase("true"))) {
            throw new D("JSONArray[" + var1 + "] is not a boolean.");
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public Enum getEnum(Class var1, int var2) throws D {
      Enum var3 = this.optEnum(var1, var2);
      if (var3 == null) {
         throw new D("JSONArray[" + var2 + "] is not an enum of type " + JSONObject.quote(var1.getSimpleName()) + ".");
      } else {
         return var3;
      }
   }

   public Object get(int var1) throws D {
      Object var2 = this.opt(var1);
      if (var2 == null) {
         throw new D("JSONArray[" + var1 + "] not found.");
      } else {
         return var2;
      }
   }

   public Object remove(int var1) {
      return var1 >= 0 && var1 < this.length() ? this.myArrayList.remove(var1) : null;
   }

   public JSONArray put(Object var1) {
      JSONObject.testValidity(var1);
      this.myArrayList.add(var1);
      return this;
   }

   public Number optNumber(int var1, Number var2) {
      Object var3 = this.opt(var1);
      if (JSONObject.NULL.equals(var3)) {
         return var2;
      } else if (var3 instanceof Number) {
         return (Number)var3;
      } else if (var3 instanceof String) {
         try {
            return JSONObject.stringToNumber((String)var3);
         } catch (Exception var5) {
            return var2;
         }
      } else {
         return var2;
      }
   }

   public JSONArray optJSONArray(int var1) {
      Object var2 = this.opt(var1);
      return var2 instanceof BE ? (BE)var2 : null;
   }

   public int length() {
      return this.myArrayList.size();
   }

   public double getDouble(int var1) throws D {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).doubleValue() : Double.parseDouble((String)var2);
      } catch (Exception var4) {
         throw new D("JSONArray[" + var1 + "] is not a number.", var4);
      }
   }

   public String getString(int var1) throws D {
      Object var2 = this.get(var1);
      if (var2 instanceof String) {
         return (String)var2;
      } else {
         throw new D("JSONArray[" + var1 + "] not a string.");
      }
   }

   public JSONArray put(int var1, Map var2) throws D {
      this.put(var1, (Object)(new B(var2)));
      return this;
   }

   public boolean isNull(int var1) {
      return JSONObject.NULL.equals(this.opt(var1));
   }

   public JSONArray put(double var1) throws D {
      return this.put((Object)var1);
   }

   public JSONObject toJSONObject(JSONArray var1) throws D {
      if (var1 != null && !var1.isEmpty() && !this.isEmpty()) {
         B var2 = new B(var1.length());

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            var2.put(var1.getString(var3), this.opt(var3));
         }

         return var2;
      } else {
         return null;
      }
   }

   public Enum optEnum(Class var1, int var2) {
      return this.optEnum(var1, var2, (Enum)null);
   }

   public int optInt(int var1) {
      return this.optInt(var1, 0);
   }

   public float optFloat(int var1) {
      return this.optFloat(var1, Float.NaN);
   }

   public double optDouble(int var1) {
      return this.optDouble(var1, Double.NaN);
   }

   public Number optNumber(int var1) {
      return this.optNumber(var1, (Number)null);
   }

   public Number getNumber(int var1) throws D {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? (Number)var2 : JSONObject.stringToNumber(var2.toString());
      } catch (Exception var4) {
         throw new D("JSONArray[" + var1 + "] is not a number.", var4);
      }
   }

   public boolean optBoolean(int var1) {
      return this.optBoolean(var1, false);
   }

   public float getFloat(int var1) throws D {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).floatValue() : Float.parseFloat(var2.toString());
      } catch (Exception var4) {
         throw new D("JSONArray[" + var1 + "] is not a number.", var4);
      }
   }

   public Writer write(Writer var1, int var2, int var3) throws D {
      try {
         boolean var4 = false;
         int var5 = this.length();
         var1.write(91);
         if (var5 == 1) {
            try {
               JSONObject.writeValue(var1, this.myArrayList.get(0), var2, var3);
            } catch (Exception var10) {
               throw new D("Unable to write JSONArray value at index: 0", var10);
            }
         } else if (var5 != 0) {
            int var6 = var3 + var2;

            for(int var7 = 0; var7 < var5; ++var7) {
               if (var4) {
                  var1.write(44);
               }

               if (var2 > 0) {
                  var1.write(10);
               }

               JSONObject.indent(var1, var6);

               try {
                  JSONObject.writeValue(var1, this.myArrayList.get(var7), var2, var6);
               } catch (Exception var9) {
                  throw new D("Unable to write JSONArray value at index: " + var7, var9);
               }

               var4 = true;
            }

            if (var2 > 0) {
               var1.write(10);
            }

            JSONObject.indent(var1, var3);
         }

         var1.write(93);
         return var1;
      } catch (IOException var11) {
         throw new D(var11);
      }
   }

   public String toString(int var1) throws D {
      StringWriter var2 = new StringWriter();
      synchronized(var2.getBuffer()) {
         return this.write(var2, var1, 0).toString();
      }
   }

   public BigDecimal optBigDecimal(int var1, BigDecimal var2) {
      Object var3 = this.opt(var1);
      if (JSONObject.NULL.equals(var3)) {
         return var2;
      } else if (var3 instanceof BigDecimal) {
         return (BigDecimal)var3;
      } else if (var3 instanceof BigInteger) {
         return new BigDecimal((BigInteger)var3);
      } else if (!(var3 instanceof Double) && !(var3 instanceof Float)) {
         if (!(var3 instanceof Long) && !(var3 instanceof Integer) && !(var3 instanceof Short) && !(var3 instanceof Byte)) {
            try {
               return new BigDecimal(var3.toString());
            } catch (Exception var5) {
               return var2;
            }
         } else {
            return new BigDecimal(((Number)var3).longValue());
         }
      } else {
         return new BigDecimal(((Number)var3).doubleValue());
      }
   }

   public JSONArray put(int var1, double var2) throws D {
      return this.put(var1, (Object)var2);
   }

   public long optLong(int var1) {
      return this.optLong(var1, 0L);
   }

   public JSONArray put(int var1, int var2) throws D {
      return this.put(var1, (Object)var2);
   }

   public Object query(JSONPointer var1) {
      return var1.queryFrom(this);
   }

   public JSONArray put(int var1, Collection var2) throws D {
      return this.put(var1, (Object)(new BE(var2)));
   }

   public String toString() {
      try {
         return this.toString(0);
      } catch (Exception var2) {
         return null;
      }
   }

   public Object query(String var1) {
      return this.query((JSONPointer)(new AA(var1)));
   }

   public JSONArray put(int var1, Object var2) throws D {
      if (var1 < 0) {
         throw new D("JSONArray[" + var1 + "] not found.");
      } else if (var1 < this.length()) {
         JSONObject.testValidity(var2);
         this.myArrayList.set(var1, var2);
         return this;
      } else if (var1 == this.length()) {
         return this.put(var2);
      } else {
         this.myArrayList.ensureCapacity(var1 + 1);

         while(var1 != this.length()) {
            this.myArrayList.add(JSONObject.NULL);
         }

         return this.put(var2);
      }
   }

   public JSONArray put(int var1) {
      return this.put((Object)var1);
   }

   public JSONArray() {
      this.myArrayList = new ArrayList();
   }

   public JSONArray(Object var1) throws D {
      this();
      if (!var1.getClass().isArray()) {
         throw new D("JSONArray initial value should be a string or collection or array.");
      } else {
         int var2 = Array.getLength(var1);
         this.myArrayList.ensureCapacity(var2);

         for(int var3 = 0; var3 < var2; ++var3) {
            this.put(JSONObject.wrap(Array.get(var1, var3)));
         }

      }
   }

   public JSONArray put(Map var1) {
      return this.put((Object)(new B(var1)));
   }

   public Writer write(Writer var1) throws D {
      return this.write(var1, 0, 0);
   }

   public int getInt(int var1) throws D {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).intValue() : Integer.parseInt((String)var2);
      } catch (Exception var4) {
         throw new D("JSONArray[" + var1 + "] is not a number.", var4);
      }
   }

   public String optString(int var1) {
      return this.optString(var1, "");
   }

   public JSONArray put(int var1, long var2) throws D {
      return this.put(var1, (Object)var2);
   }

   public JSONObject optJSONObject(int var1) {
      Object var2 = this.opt(var1);
      return var2 instanceof B ? (B)var2 : null;
   }

   public Object optQuery(String var1) {
      return this.optQuery((JSONPointer)(new AA(var1)));
   }

   public Iterator iterator() {
      return this.myArrayList.iterator();
   }

   public JSONArray(String var1) throws D {
      this((JSONTokener)(new AE(var1)));
   }

   public JSONArray put(int var1, float var2) throws D {
      return this.put(var1, (Object)var2);
   }

   public float optFloat(int var1, float var2) {
      Object var3 = this.opt(var1);
      if (JSONObject.NULL.equals(var3)) {
         return var2;
      } else if (var3 instanceof Number) {
         return ((Number)var3).floatValue();
      } else if (var3 instanceof String) {
         try {
            return Float.parseFloat((String)var3);
         } catch (Exception var5) {
            return var2;
         }
      } else {
         return var2;
      }
   }

   public long getLong(int var1) throws D {
      Object var2 = this.get(var1);

      try {
         return var2 instanceof Number ? ((Number)var2).longValue() : Long.parseLong((String)var2);
      } catch (Exception var4) {
         throw new D("JSONArray[" + var1 + "] is not a number.", var4);
      }
   }

   public JSONArray getJSONArray(int var1) throws D {
      Object var2 = this.get(var1);
      if (var2 instanceof BE) {
         return (BE)var2;
      } else {
         throw new D("JSONArray[" + var1 + "] is not a JSONArray.");
      }
   }

   public double optDouble(int var1, double var2) {
      Object var4 = this.opt(var1);
      if (JSONObject.NULL.equals(var4)) {
         return var2;
      } else if (var4 instanceof Number) {
         return ((Number)var4).doubleValue();
      } else if (var4 instanceof String) {
         try {
            return Double.parseDouble((String)var4);
         } catch (Exception var6) {
            return var2;
         }
      } else {
         return var2;
      }
   }

   public JSONObject getJSONObject(int var1) throws D {
      Object var2 = this.get(var1);
      if (var2 instanceof B) {
         return (B)var2;
      } else {
         throw new D("JSONArray[" + var1 + "] is not a JSONObject.");
      }
   }

   public boolean similar(Object var1) {
      if (!(var1 instanceof BE)) {
         return false;
      } else {
         int var2 = this.length();
         if (var2 != ((BE)var1).length()) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2; ++var3) {
               Object var4 = this.myArrayList.get(var3);
               Object var5 = ((BE)var1).myArrayList.get(var3);
               if (var4 != var5) {
                  if (var4 == null) {
                     return false;
                  }

                  if (var4 instanceof B) {
                     if (!((B)var4).similar(var5)) {
                        return false;
                     }
                  } else if (var4 instanceof BE) {
                     if (!((BE)var4).similar(var5)) {
                        return false;
                     }
                  } else if (!var4.equals(var5)) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public List toList() {
      ArrayList var1 = new ArrayList(this.myArrayList.size());
      Iterator var2 = this.myArrayList.iterator();

      while(true) {
         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 != null && !JSONObject.NULL.equals(var3)) {
               if (var3 instanceof BE) {
                  var1.add(((BE)var3).toList());
               } else if (var3 instanceof B) {
                  var1.add(((B)var3).toMap());
               } else {
                  var1.add(var3);
               }
            } else {
               var1.add((Object)null);
            }
         }

         return var1;
      }
   }

   public JSONArray(JSONTokener var1) throws D {
      this();
      if (var1.nextClean() != '[') {
         throw var1.syntaxError("A JSONArray text must start with '['");
      } else {
         char var2 = var1.nextClean();
         if (var2 == 0) {
            throw var1.syntaxError("Expected a ',' or ']'");
         } else if (var2 != ']') {
            var1.back();

            while(true) {
               if (var1.nextClean() == ',') {
                  var1.back();
                  this.myArrayList.add(JSONObject.NULL);
               } else {
                  var1.back();
                  this.myArrayList.add(var1.nextValue());
               }

               switch(var1.nextClean()) {
               case '\u0000':
                  throw var1.syntaxError("Expected a ',' or ']'");
               case ',':
                  var2 = var1.nextClean();
                  if (var2 == 0) {
                     throw var1.syntaxError("Expected a ',' or ']'");
                  }

                  if (var2 == ']') {
                     return;
                  }

                  var1.back();
                  break;
               case ']':
                  return;
               default:
                  throw var1.syntaxError("Expected a ',' or ']'");
               }
            }
         }
      }
   }

   public BigInteger getBigInteger(int var1) throws D {
      Object var2 = this.get(var1);

      try {
         return new BigInteger(var2.toString());
      } catch (Exception var4) {
         throw new D("JSONArray[" + var1 + "] could not convert to BigInteger.", var4);
      }
   }

   public BigInteger optBigInteger(int var1, BigInteger var2) {
      Object var3 = this.opt(var1);
      if (JSONObject.NULL.equals(var3)) {
         return var2;
      } else if (var3 instanceof BigInteger) {
         return (BigInteger)var3;
      } else if (var3 instanceof BigDecimal) {
         return ((BigDecimal)var3).toBigInteger();
      } else if (!(var3 instanceof Double) && !(var3 instanceof Float)) {
         if (!(var3 instanceof Long) && !(var3 instanceof Integer) && !(var3 instanceof Short) && !(var3 instanceof Byte)) {
            try {
               String var4 = var3.toString();
               return JSONObject.isDecimalNotation(var4) ? (new BigDecimal(var4)).toBigInteger() : new BigInteger(var4);
            } catch (Exception var5) {
               return var2;
            }
         } else {
            return BigInteger.valueOf(((Number)var3).longValue());
         }
      } else {
         return (new BigDecimal(((Number)var3).doubleValue())).toBigInteger();
      }
   }

   public JSONArray(Collection var1) {
      if (var1 == null) {
         this.myArrayList = new ArrayList();
      } else {
         this.myArrayList = new ArrayList(var1.size());
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            this.myArrayList.add(JSONObject.wrap(var3));
         }
      }

   }

   public String optString(int var1, String var2) {
      Object var3 = this.opt(var1);
      return JSONObject.NULL.equals(var3) ? var2 : var3.toString();
   }

   public JSONArray put(boolean var1) {
      return this.put((Object)(var1 ? Boolean.TRUE : Boolean.FALSE));
   }

   public long optLong(int var1, long var2) {
      Object var4 = this.opt(var1);
      if (JSONObject.NULL.equals(var4)) {
         return var2;
      } else if (var4 instanceof Number) {
         return ((Number)var4).longValue();
      } else if (var4 instanceof String) {
         try {
            return (new BigDecimal(var4.toString())).longValue();
         } catch (Exception var6) {
            return var2;
         }
      } else {
         return var2;
      }
   }
}
