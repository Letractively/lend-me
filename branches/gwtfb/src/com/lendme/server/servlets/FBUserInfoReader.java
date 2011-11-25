package com.lendme.server.servlets;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.lendme.client.ApplicationConstants;
import com.lendme.server.LendMeFacade;

public class FBUserInfoReader extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		ServletOutputStream outStream =	response.getOutputStream();

		String message = null;
		JSONObject data = null;
		boolean gotMessage = false;

		while ((message = reader.readLine()) != null) {
			outStream.println(message);
		}
		response.sendRedirect(ApplicationConstants.APP_URL);
	}
	
	public static void main(String[] args) throws ServletException, IOException {

//		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		ServletOutputStream outStream =	response.getOutputStream();

		String message = "signed_request=MhGWsyiS3in2KF_v7LFjL__epbVGPF7iKsyM3xjXsoQ.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjEzMjIyMDgwMDAsImlzc3VlZF9hdCI6MTMyMjIwMjc2NCwib2F1dGhfdG9rZW4iOiJBQUFEZ2hINnBaQ3hvQkFPYmJVdzNCRFZBc2ZqdGh4TTlraDJGRm54WHlIMGNBaXY3SEJmbW84NEJtTU95eUtHdFlBN0VsVVpCWTJ5ZW14S3FNNmFzdWZGWkFzenZmSklkZFVTWkJWVmUwdzNqcERoSVBhTEIiLCJyZWdpc3RyYXRpb24iOnsibmFtZSI6Ikd1aWxoZXJtZSBTYW50b3MiLCJiaXJ0aGRheSI6IjAzXC8yNlwvMTk5MiIsImdlbmRlciI6Im1hbGUiLCJsb2NhdGlvbiI6eyJuYW1lIjoiQ2FiZWRlbG9zLCBQYXJhaWJhLCBCcmF6aWwiLCJpZCI6MTA3MTQ3MjI1OTgzNjYxfSwiZW1haWwiOiJndWlzZ2IxM1x1MDA0MGdtYWlsLmNvbSJ9LCJyZWdpc3RyYXRpb25fbWV0YWRhdGEiOnsiZmllbGRzIjoibmFtZSxiaXJ0aGRheSxnZW5kZXIsbG9jYXRpb24sZW1haWwifSwidXNlciI6eyJjb3VudHJ5IjoiYnIiLCJsb2NhbGUiOiJwdF9CUiJ9LCJ1c2VyX2lkIjoiMTAwMDAxMzU1MDYwMTQ3In0";
		JSONObject data = null;
		boolean gotMessage = false;

//		while ((message = reader.readLine()) != null) {
			if ( message.contains("signed_request=") ){
				message = message.replace("signed_request=", "").replace(".", "<SEP>");

				String signature = message.split("<SEP>")[0];
				String encodedData = message.split("<SEP>")[1];
				try {
					data = (JSONObject) JSONParser.parseStrict((base64Decode(encodedData)));
					signature = base64Decode(signature);

					if ( !data.get("algorithm").equals("HMAC-SHA256") ){
//						outStream.println("Bad signature encryption algorithm!");
						return;
					}
				} catch (MessagingException e) {
//					outStream.println("Bad base64 encoded signature!");
					return;
				}
//				outStream.println("Got you: [data="+data+"]");

				try{
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					md.update(encodedData.getBytes());
					md.update(ApplicationConstants.APP_SECRET.getBytes());

					String expectedSignature="";
					byte[] hashBytes = md.digest();
					for(int i=0; i < hashBytes.length; i++){
						expectedSignature = expectedSignature+((char) hashBytes[i]);
					}
					if ( !expectedSignature.equals(signature) ){
//						outStream.println("Bad signature!");
						return;
					}
				}
				catch(NoSuchAlgorithmException noAlgorithm){
//					outStream.println("Server with problems while decoding with SHA-256 algorithm!");
					return;
				}
				gotMessage = true;
			}
//		}
		if (!gotMessage || data == null ){
//			outStream.println("Got no signed_request message from Facebook or data is null!");
			return;
		}
		else{
			String id = data.get("user_id").toString();
			String accessToken = data.get("oauth_token").toString();
			JSONObject userInfo = (JSONObject) JSONParser.parseStrict(retrieveInfo(id, accessToken));

			String name = userInfo.get("name").toString();
			String address = userInfo.get("location").toString();

			LendMeFacade lendMeService = LendMeFacade.getInstance();
			try {
				lendMeService.registerUser(id, name, address);
			} catch (Exception caught) {
//				outStream.println("Problem registering new user: "+caught.getMessage());
				return;
			}
		}

//		response.sendRedirect(ApplicationConstants.APP_URL);

	}

	private static String base64Decode(String encodedBase64) throws MessagingException, IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(encodedBase64.getBytes());
		InputStream b64is;
		b64is = MimeUtility.decode(bais, "base64");
//		int n = b64is.read();
//		byte[] result = new byte[n];
		StringBuilder resultBuilder = new StringBuilder();
		int temp;
		while ( (temp = b64is.read()) != -1 ){
			resultBuilder.append(Character.toChars(temp));
		}
		return new String(resultBuilder);
	}

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
