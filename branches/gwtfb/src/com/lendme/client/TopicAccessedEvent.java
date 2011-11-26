package com.lendme.client;

import com.google.gwt.event.shared.GwtEvent;

public class TopicAccessedEvent extends GwtEvent<TopicAccessedHandler> {
 
 // Boiler plate code required to make your event work correctly in the GWT system
 private static final Type<TopicAccessedHandler> TYPE = new Type<TopicAccessedHandler>();

 @Override
 public com.google.gwt.event.shared.GwtEvent.Type<TopicAccessedHandler> getAssociatedType() {
  return TYPE;
 }
 
 @Override
 protected void dispatch(TopicAccessedHandler handler) {
  handler.onTopicAccessed(this);
 }

 public static Type<TopicAccessedHandler> getType() {
  return TYPE;
 }
 
}