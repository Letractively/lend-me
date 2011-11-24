package com.lendme.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinksPanel extends Composite {

	private DockPanel links = new DockPanel ();
	private HorizontalPanel leftSide = new HorizontalPanel ();
	private VerticalPanel logoutSide = new VerticalPanel();

	Anchor sourceCodeLink = new Anchor ( "Source" );

	public LinksPanel(){
		links.getElement().setId("TopMenuLinks");

		sourceCodeLink.setHref("http://code.google.com/p/lend-me/");
		sourceCodeLink.setTarget("blank");

		leftSide.add ( new Hyperlink ( "Home" , "home" ) );
		leftSide.add ( new Anchor("Facebook", true, "http://www.facebook.com/apps/application.php?id="+ApplicationConstants.APP_ID+"&sk="+ApplicationConstants.APP_ID));
		leftSide.add( sourceCodeLink );
		links.add( leftSide, DockPanel.WEST );

		logoutSide.add(	new HTML("<div style='text-align: center;'> <fb:login-button autologoutlink='true' /> </div>" ));
		links.add(logoutSide, DockPanel.EAST);
		initWidget ( links );
	}
	
	public void setLogoutButtonVisible(boolean visible){
		logoutSide.setVisible(visible);
	}

}
