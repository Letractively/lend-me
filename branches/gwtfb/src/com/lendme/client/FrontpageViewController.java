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
		outer.add ( new HTML ( "<div style='text-align: center;'> Faca parte de LendMe, a rede que mais cresce ultimamente! </div>" ) );
		outer.add ( new HTML("<div style='text-align: center;'> <fb:login-button registration-url=http://127.0.0.1:8888/Lend_me_gwtfb.html?gwt.codesvr=127.0.0.1:9997#registration /> </div>" ));
		outer.add ( new HTML ( "<div style='margin-top: 7px;'> Veja o que as pessoas pensam dessa nova ideia (e diga o que voce pensa tambem!): </div>" ) );
		outer.add ( new HTML ( "<hr/><fb:comments xid='gwtfb' />" ) );
		initWidget ( outer );
	}

}
