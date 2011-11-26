package com.lendme.client;
import com.google.gwt.event.shared.HasHandlers;


public interface HasTopicBeenAccessedHandler extends HasHandlers {


  public void addTopicAccessedHandler(TopicAccessedHandler handler);
}
