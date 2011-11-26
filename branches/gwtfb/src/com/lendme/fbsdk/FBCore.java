package com.lendme.fbsdk;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Class that wraps facebook Javascript SDK
 * 
 */
public class FBCore {

	/**
	 * Wrapper method
	 * @see http://developers.facebook.com/docs/reference/javascript/FB.init
	 */
	public native String init (String appId, String appSecret, boolean status, boolean cookie, boolean xfbml) /*-{
		$wnd.FB.init({
			'appId': appId, 
			'status': status,
			'cookie': cookie,
			'xfbml' : xfbml,
			'oauth' : true
		});
		function httpGet(theUrl){
			var xmlHttp = null;

			xmlHttp = new XMLHttpRequest();
			xmlHttp.open( "GET", theUrl, false );
			xmlHttp.send( null );
			return xmlHttp.responseText;
		}
		return httpGet("https://graph.facebook.com/oauth/access_token?client_id="+appId+"&client_secret="+appSecret+"&grant_type=client_credentials");
	}-*/;
	
	public String oauthRequest ( String path, String accessToken){
		if ( path == null || path.trim().isEmpty() ){
			return "";
		}
		else if ( accessToken == null || accessToken.trim().isEmpty() ){
			return oauthRequest("https://graph.facebook.com/"+path);
		}
		else{
			return oauthRequest("https://graph.facebook.com/"+path+"&access_token="+accessToken);
		}
	}
	
	private native String oauthRequest( String request )/*-{
		
		function httpGet(theUrl){
    		var xmlHttp = null;

   			xmlHttp = new XMLHttpRequest();
   			xmlHttp.open( "GET", theUrl, false );
   			xmlHttp.send( null );
   			return xmlHttp.responseText;
    	}
		return httpGet(request);
	}-*/;
	
	/**
	 * Wrapper method
	 */
	public native void api (String path, AsyncCallback<JavaScriptObject> callback) /*-{
		var app=this;
		$wnd.FB.api (path, function(response){
	        app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
		});
	}-*/;

	 public native void api (String path, String params, AsyncCallback<JavaScriptObject> callback) /*-{
       var app=this;
       $wnd.FB.api (path, params, function(response){
           app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
       });
   }-*/;

	/**
	 * Wrapper method
	 */
	public native void api (String path, JavaScriptObject params, AsyncCallback<JavaScriptObject> callback) /*-{
		var app=this;
		$wnd.FB.api (path, params, function(response){
		    app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
		});
	}-*/;
	
	/**
	 * Wrapper method
	 */
	public native void api (String path, String method,JavaScriptObject params, AsyncCallback<JavaScriptObject> callback) /*-{
		var app=this;
		$wnd.FB.api (path,method, params, function(response){
		    app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
		});
	}-*/;
	

	/**
	 * Wrapper method
	 */
	public native void getLoginStatus (AsyncCallback<JavaScriptObject> callback) /*-{
        var app=this;
		$wnd.FB.getLoginStatus(function(response) {
            app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
		});
		
	}-*/;

	/**
	 * Wrapper method
	 */
	public native JavaScriptObject getSession () /*-{
		return $wnd.FB.getAuthResponse();
	}-*/;

	/**
	 * Wrapper method
	 */
	public native void login (AsyncCallback<JavaScriptObject> callback) /*-{
		var app=this;
        $wnd.FB.login (function(response){
    	    app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
		});
	}-*/;
	
	/**
	 * Wrapper method
	 */
	public native void login (AsyncCallback<JavaScriptObject> callback ,String permissions) /*-{
       	var app=this;
        $wnd.FB.login (function(response){
    	    app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
		},{perms:permissions});
	}-*/;
	
	/**
	 * Wrapper method
	 */
	public native void logout (AsyncCallback<JavaScriptObject> callback) /*-{
		$wnd.FB.logout(function(response){
    	    app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
		});
	}-*/;
	
	/**
	 * Wrapper method
	 */
	public native void ui (JavaScriptObject params, AsyncCallback<JavaScriptObject> callback) /*-{
		var app=this;
		$wnd.FB.ui(params,function(response){
    	    app.@com.lendme.fbsdk.FBCore::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,response);
		});
	}-*/;
	
	/*
     * Called when method succeeded.
     */
    protected void callbackSuccess(AsyncCallback<JavaScriptObject> callback, JavaScriptObject obj) {
        callback.onSuccess (obj);
    }

}
