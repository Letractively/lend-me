package com.lendme.server.servlets;

import java.io.IOException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lendme.client.ApplicationConstants;

public class FBUserInfoReader extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(ApplicationConstants.APP_URL);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletOutputStream outStream =	response.getOutputStream();

		//parse signed_request
		if(request.getParameter("signed_request") != null) {

			//it is important to enable url-safe mode for Base64 encoder 
			Base64 base64 = new Base64(true);

			//split request into signature and data
			String[] signedRequest = request.getParameter("signed_request").split("\\.", 2);

			//parse signature
			String sig = new String(base64.decode(signedRequest[0].getBytes("UTF-8")));

			//parse data and convert to json object
			String rawdata = new String(base64.decode(signedRequest[1].getBytes("UTF-8")));
			JsonObject data = (JsonObject) new JsonParser().parse(rawdata);

			//check signature algorithm
			if(!data.get("algorithm").toString().contains("HMAC-SHA256")) {
				outStream.println("Unknown signature algorithm used!");
				return;
			}

			//check if data is signed correctly
			try {
				if(!hmacSHA256(signedRequest[1], ApplicationConstants.APP_SECRET).equals(sig)) {
					outStream.println("Bad signature!");
					return;
				}
			} catch (Exception caught) {
				outStream.println("Error retrieving signature: "+caught.getMessage());
				return;
			}

			//check if user authorized the app
			if(data.get("user_id") == null || data.get("oauth_token") == null) {
				//this is guest, user did not give app authorization. Redirect
				response.sendRedirect(ApplicationConstants.APP_URL);
				return;
			} else {
//				//this is authorized user, get their info from Graph API using received access token
//				String accessToken = data.get("oauth_token").toString();
				String id = data.get("user_id").toString();
				JsonObject registration = (JsonObject) data.get("registration");
				String name = registration.get("name").toString();
//				String birthday = registration.get("birthday").toString();
				String email = registration.get("email").toString();
				String address = ((JsonObject) registration.get("location")).get("name").toString();
				Entity user = new Entity("User");
		        user.setProperty("id", id.replace("\"", ""));
		        user.setProperty("name", name.replace("\"", ""));
		        user.setProperty("address", address.replace("\"", ""));
		        user.setProperty("email", email.replace("\"", ""));
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				datastore.put(user);
		        response.sendRedirect(ApplicationConstants.APP_URL);
				return;
			}

		} else {
			//this page was opened not inside facebook iframe. Redirect
			response.sendRedirect(ApplicationConstants.APP_URL);
			return;
		}
	}

	//HmacSHA256 implementation 
	private static String hmacSHA256(String data, String key) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKey);
		byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
		return new String(hmacData);
	}

	//Native method used if info is to be retrieved using OAuth 2.0
	private static native String retrieveInfo(String id, String accessToken)/*-{
		function httpGet(theUrl){
			var xmlHttp = null;

			xmlHttp = new XMLHttpRequest();
			xmlHttp.open( "GET", theUrl, false );
			xmlHttp.send( null );
			return xmlHttp.responseText;
		}
		return httpGet("https://graph.facebook.com/"+id+"&access_token="+accessToken);
	}-*/;
	
}