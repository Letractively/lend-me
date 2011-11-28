package com.lendme.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserInfoViewController extends Composite {
	
    private HTML welcomeHtml = new HTML ();
	private VerticalPanel outer = new VerticalPanel ();

	class MeCallback implements AsyncCallback<String> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Unable to retrieve user info: "+caught.getMessage());
		}

		@Override
		public void onSuccess(String result) {
			renderMe(result);
		}
	}


	/**
	 * Render information about logged in user
	 */
	private void renderMe ( String response ) {
		welcomeHtml.setHTML ( "<h3> Ola,  " + response + "!</h3> Voce estah logado! Finque sua bandeira o quanto antes!"  );
	}

	/**
	 * New View
	 */
	public UserInfoViewController ( String currentUserId ) {

	    outer.add ( welcomeHtml );
		outer.add ( new HTML ( "<p/>" ) );
        outer.add ( new HTML ( "<hr/><fb:comments numposts='1' xid='gwtfb' width='750px' />" ) );
		initWidget ( outer );
	}
}
