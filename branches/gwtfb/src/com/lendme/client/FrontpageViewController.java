package com.lendme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FrontpageViewController extends Composite {
	
	private VerticalPanel outer = new VerticalPanel ();
	
	public FrontpageViewController (String appId) {

	    AppImageBundle images = GWT.create( AppImageBundle.class);
		
		outer.getElement().setId ( "FrontpageViewController" );
		outer.add ( new Image ( images.logo()) );
		outer.add ( new HTML ( "<div style='text-align: center;'> Faca parte de LendMe, a rede que mais cresce atualmente! </div>" ) );
		outer.add ( new HTML("<div style='text-align: center; margin-top: 20px;'> <fb:login-button registration-url="+ApplicationConstants.APP_URL+"#registration scope='user_about_me,user_birthday,user_location,user_likes' /> </div>" ));
		initWidget ( outer );
	}

}
