package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import models.Settings;

public class RestAccess {
	
	private static HttpClient client ;
	private static HttpGet request;
	private static HttpPost requestPost;
	private static String encryptedText;
	
	private static String adresse;
	
	public static void connect(){	
		LoadConfig.loadSettings();
		
		adresse = String.format("%s/API/%s", Settings.getAdresse(), Settings.getVersion());
		String key = Settings.getKey(); // 128 bit key
		
		client = HttpClientBuilder.create().build();

	    Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

	    Cipher cipher;
	    
		try {
			cipher = Cipher.getInstance("AES");
		    cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		    byte[] encrypted = cipher.doFinal(String.format("%s %s", Settings.getLogin(), Settings.getPass()).getBytes());
		    Base64.Encoder encoder = Base64.getEncoder();
		    encryptedText = encoder.encodeToString(encrypted);
		  
		    System.out.println("encryptedText : " + encryptedText);
 
		    
	    
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String traitementReponse(){
		
        request.addHeader("monToken", new String(encryptedText));
        
        System.out.println("request.getURI() :" + request.getURI());
    	
    	HttpResponse response = null;
    	
    	String reponse = null;
    	
		try {
			response = client.execute(request);
			BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		    reponse = rd.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(reponse);
		
		return reponse;
		
	}
	

	
	public static String traitementReponsePost(){
		
        requestPost.addHeader("monToken", new String(encryptedText));
        requestPost.setHeader("Accept", "text/plain");
    	
    	HttpResponse response = null;
    	
    	String reponse = null;
    	
		try {
			response = client.execute(requestPost);
			BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		    reponse = rd.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reponse;
		
	}
	
    public static String requestNiveaux() {	
    	
    	request = new HttpGet(String.format("%s/niveaux", adresse));
    	return traitementReponse();
    	
	}
	
	public static String ajouter (String c, File f) {
		
		requestPost = new HttpPost(String.format("%s/ajout", adresse));
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("picture", f, ContentType.DEFAULT_BINARY, c);
		
		HttpEntity entity = builder.build();
		requestPost.setEntity(entity);
		
    	return traitementReponsePost();
	}
	
    public static String ajouter (String c) {

		requestPost = new HttpPost(String.format("%s/ajout", adresse));
		requestPost.setHeader("Content-type", "text/plain");
		
		StringEntity se = new StringEntity(c, "UTF-8");
		requestPost.setEntity(se);
		
    	return traitementReponsePost();
	}
	
}
