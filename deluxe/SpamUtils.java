package deluxe;

import java.io.IOException;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

// $FF: synthetic class
public class SpamUtils {
   public CloseableHttpClient httpclient = HttpClientBuilder.create().build();

   public void addFriend(String var1, String var2, String var3) throws IOException, ClientProtocolException {
      String[] var4 = var3.split(":");
      HttpHost var5 = new HttpHost(var4[0], Integer.parseInt(var4[1]), "http");
      RequestConfig var6 = RequestConfig.custom().setProxy(var5).setConnectTimeout(500).build();
      HttpPut var7 = new HttpPut("https://discordapp.com/api/v7/users/@me/relationships/" + var1);
      var7.setConfig(var6);
      var7.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
      var7.addHeader("Accept-Language", "en-US,en;q=0.5");
      var7.addHeader("Content-type", "application/json");
      var7.addHeader("Authorization", var2);
      this.httpclient.execute(var7);
   }

   public void sendMsg(String var1, String var2, boolean var3, String var4, String var5) throws ClientProtocolException, IOException {
      String[] var6 = var5.split(":");
      HttpHost var7 = new HttpHost(var6[0], Integer.parseInt(var6[1]), "http");
      RequestConfig var8 = RequestConfig.custom().setProxy(var7).setConnectionRequestTimeout(1000).setSocketTimeout(1000).setConnectTimeout(1000).build();
      HttpPost var9 = new HttpPost("https://discordapp.com/api/v7/channels/" + var1 + "/messages");
      var9.setConfig(var8);
      B var10 = new B();
      var10.put("content", var2);
      if (var3) {
         var10.put("tts", "true");
      }

      StringEntity var11 = new StringEntity(var10.toString());
      var9.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
      var9.addHeader("Accept-Language", "en-US,en;q=0.5");
      var9.addHeader("Content-type", "application/json; charset=utf-8");
      var9.addHeader("Authorization", var4);
      var9.setEntity(var11);
      CloseableHttpResponse var12 = this.httpclient.execute(var9);
      HttpEntity var13 = var12.getEntity();
      String var14 = EntityUtils.toString(var13);
   }

   public void leave(String var1, String var2, String var3) throws IOException, ClientProtocolException {
      String[] var4 = var3.split(":");
      HttpHost var5 = new HttpHost(var4[0], Integer.parseInt(var4[1]), "http");
      RequestConfig var6 = RequestConfig.custom().setProxy(var5).setConnectTimeout(5000).build();
      HttpDelete var7 = new HttpDelete("https://discordapp.com/api/v7/users/@me/guilds/" + var1);
      var7.setConfig(var6);
      var7.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
      var7.addHeader("Accept-Language", "en-US,en;q=0.5");
      var7.addHeader("Content-type", "application/json");
      var7.addHeader("Authorization", var2);
      this.httpclient.execute(var7);
   }

   public void deleteFriend(String var1, String var2, String var3) throws IOException, ClientProtocolException {
      String[] var4 = var3.split(":");
      HttpHost var5 = new HttpHost(var4[0], Integer.parseInt(var4[1]), "http");
      RequestConfig var6 = RequestConfig.custom().setProxy(var5).setConnectTimeout(500).build();
      HttpDelete var7 = new HttpDelete("https://discordapp.com/api/v7/users/@me/relationships/" + var1);
      var7.setConfig(var6);
      var7.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
      var7.addHeader("Accept-Language", "en-US,en;q=0.5");
      var7.addHeader("Content-type", "application/json");
      var7.addHeader("Authorization", var2);
      this.httpclient.execute(var7);
   }

   public static String getRandomProxy() {
      Random var0 = new Random();
      String var1 = (String)AQ.proxys.get(var0.nextInt(AQ.proxys.size()));
      return var1;
   }

   public String join(String var1, String var2, String var3) throws IOException, ClientProtocolException {
      String[] var5 = var3.split(":");
      HttpHost var6 = new HttpHost(var5[0], Integer.parseInt(var5[1]), "http");
      RequestConfig var7 = RequestConfig.custom().setProxy(var6).setConnectionRequestTimeout(1000).setSocketTimeout(1000).setConnectTimeout(1000).build();
      HttpPost var8 = new HttpPost("https://discordapp.com/api/v7/invite/" + var1 + "?with_counts=true");
      var8.setConfig(var7);
      B var9 = new B();
      StringEntity var10 = new StringEntity(var9.toString());
      var8.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
      var8.addHeader("Accept-Language", "en-US,en;q=0.5");
      var8.addHeader("Content-type", "application/json");
      var8.addHeader("Authorization", var2);
      var8.setEntity(var10);
      CloseableHttpResponse var11 = this.httpclient.execute(var8);
      HttpEntity var12 = var11.getEntity();
      String var4 = EntityUtils.toString(var12);
      return var4;
   }
}
