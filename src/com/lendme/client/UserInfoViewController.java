package com.lendme.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lendme.fbsdk.FBCore;

public class UserInfoViewController extends Composite {
	
    private HTML welcomeHtml = new HTML ();
	private VerticalPanel outer = new VerticalPanel ();

	/**
	 * Display User info
	 */
	class MeCallback extends Callback<JavaScriptObject> {
		public void onSuccess ( JavaScriptObject response ) {
			renderMe ( response );
		}
	}


	/**
	 * Render information about logged in user
	 */
	private void renderMe ( JavaScriptObject response ) {
		JSOModel jso = response.cast();
		welcomeHtml.setHTML ( "<h3> Ola,  " + jso.get ( "name" ) + "</h3>! Voce estah logado! LendMe ainda esta sendo desenvolvido."  );
	}

	/**
	 * New View
	 */
	public UserInfoViewController ( final FBCore fbCore ) {

	    outer.add ( welcomeHtml );
		outer.add ( new HTML ( "<p/>" ) );
        outer.add ( new HTML ( "<hr/><fb:comments xid='gwtfb' />" ) );
	
		
		fbCore.api ( "/me" , new MeCallback () );
		// fbCore.api ( "/f8/posts",  new PostsCallback () );
		initWidget ( outer );
	}
}
