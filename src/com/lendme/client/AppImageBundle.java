package com.lendme.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Client Images
 */
public interface AppImageBundle extends ClientBundle {
    
    @Source ( "logo.png" )
    ImageResource logo ();
    
    @Source ( "topMenu.png" )
    ImageResource topMenu();
    
    @Source ( "exclama.jpg" )
    ImageResource updateAction();
    
}
