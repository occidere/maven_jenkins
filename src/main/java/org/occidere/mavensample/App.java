package org.occidere.mavensample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) throws Exception {
		String url = "http://checkip.amazonaws.com";
		
		System.out.println("Hello World!");
		System.out.println("URL: "+url);
		
		
		CloseableHttpClient httpClient = HttpClients.custom()
				.setRedirectStrategy(new LaxRedirectStrategy())
				.build();
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("User-Agent", "Mozilla/5.0");
		httpGet.addHeader("Accept-Encoding", "gzip");
		
		CloseableHttpResponse response = httpClient.execute(httpGet);
		
		StatusLine statusLine = response.getStatusLine();
		System.out.println("\n--- Status Line ---");
		System.out.println(statusLine);
		
		Header headers[] = response.getAllHeaders();
		System.out.println("\n--- All Headers ---");
		Arrays.stream(headers).forEach(System.out::println);
		System.out.println();
		
		HttpEntity entity = response.getEntity();
		String contentEncoding = null;
		try{
			contentEncoding = response.getHeaders("Content-Encoding")[0].getValue();
		}
		catch(Exception e) {}
		
		BufferedReader br;
		InputStream is = entity.getContent();
		InputStreamReader isr;
		
		if(contentEncoding != null && contentEncoding.toLowerCase().contains("gzip"))
			isr = new InputStreamReader(new GZIPInputStream(is));
		else
			isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
		
		String line;
		while((line = br.readLine()) != null)
			System.out.println(line);
		br.close();
		
		response.close();
		httpClient.close();
	}
}