package com.lendme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FrontpageViewController extends Composite {
	
	private VerticalPanel outer = new VerticalPanel ();
	
	public FrontpageViewController () {

	    AppImageBundle images = GWT.create( AppImageBundle.class);
		
		outer.getElement().setId ( "FrontpageViewController" );
		outer.add ( new Image ( images.logo()) );
		outer.add ( new HTML ( "<div style='text-align: center;'> Logue-se no sistema com sua conta do Facebook: </div>" ) );
		outer.add ( new HTML("<div style='text-align: center;'> <fb:login-button autologoutlink='true' /> </div>" ));//perms='publish_stream,read_stream' /> " ) );
		outer.add ( new HTML ( "<div style='margin-top: 7px;'> Veja o que as pessoas pensam dessa nova ideia (e diga o que voce pensa tambem!): </div>" ) );
		outer.add ( new HTML ( "<hr/><fb:comments xid='gwtfb' />" ) );
		initWidget ( outer );
	}

}
