package com.lendme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RegistrationViewController extends Composite {
	
	private VerticalPanel outer = new VerticalPanel ();
	
	public RegistrationViewController () {

	    AppImageBundle images = GWT.create( AppImageBundle.class);
		
		outer.getElement().setId ( "FrontpageViewController" );
		outer.add ( new Image ( images.logo()) );
		outer.add ( new HTML ( "<div style='text-align: center;'> Cadastre-se no nosso sistema com sua conta do Facebook: </div>" ) );
		outer.add ( new HTML ("<iframe " +
				"src=\"https://www.facebook.com/plugins/registration.php?client_id="+ApplicationConstants.APP_ID+"&redirect_uri="+ApplicationConstants.APP_URL+"/lendme/fbreader&fields=name,birthday,gender,location,email" +
				"\"&fb_only=\"true\"&fb_register=\"true\"" +
				" scrolling=\"auto\"" + " frameborder=\"no\"" + " style=\"border:none\"" + " allowTransparency=\"true\"" + "width=\"100%\"" + "height=\"330\">" + "</iframe>"));
		initWidget ( outer );
	}

}
