package com.lendme.client;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Displays links on the left side on the HomeView page
 */
public class HomeSideBarPanel extends Composite {
    
    VerticalPanel linkPanel = new VerticalPanel ();
    
    public HomeSideBarPanel () {
        linkPanel.getElement().setId("SideBarPanel");
        linkPanel.add ( new HTML ( "<h3>Opcoes</h3>" ) );
        linkPanel.add( new Hyperlink( "Amigos", "options/friends"));
        linkPanel.add( new Hyperlink( "Items", "options/items"));
        linkPanel.add( new Hyperlink( "Mensagens", "options/messages"));
        linkPanel.add( new Hyperlink( "Historico", "options/history"));
        initWidget ( linkPanel );
    }

}
