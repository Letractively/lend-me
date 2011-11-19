package com.lendme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Display Top Menu
 */
public class TopMenuPanel extends Composite {

	private HorizontalPanel outer = new HorizontalPanel ();
	
	public TopMenuPanel () {
	    AppImageBundle images = GWT.create( AppImageBundle.class);
	    
		outer.getElement().setId("TopMenu");
		outer.add ( new Image ( images.topMenu() ) );
        initWidget ( outer );
	}
	
	
}
