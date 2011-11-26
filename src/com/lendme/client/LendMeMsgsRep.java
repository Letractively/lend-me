package com.lendme.client;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LendMeMsgsRep extends AbsolutePanel {
	
	private Map<String, String> messageProperties = new TreeMap<String, String>();
	private final Label senderLabel;
	private final Label subjectLabel;
	private final Label dateLabel;
	private final Label messageLabel;
	
	private final String separatorString = 
		"-----------------------------------------------------------------" +
		"-----------------------------------" ;//100 tracos
	
	/**
	 * @wbp.parser.constructor
	 */
	public LendMeMsgsRep() {
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("500px");
		
		subjectLabel = new Label("subject");
		subjectLabel.setStyleName("messagesLines");
		vPanel.add(subjectLabel);
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setStyleName("messagesLines");
		hPanel.setHeight("30px");
		
		senderLabel = new Label("sender");
		senderLabel.setWidth("400px");
		senderLabel.setStyleName("messagesLines");
		hPanel.add(senderLabel);
		
		
		dateLabel = new Label("date");
		dateLabel.setStyleName("messagesLines");
		hPanel.add(dateLabel);
		
		vPanel.add(hPanel);
		
		messageLabel = new Label("message");
		messageLabel.setStyleName("messagesLines");
		messageLabel.setWidth("500px");
		vPanel.add(messageLabel);

		Label separatorLabel = new Label();
		separatorLabel.setStyleName("messagesLines");
		separatorLabel.setWidth("500px");
		separatorLabel.setText(separatorString);
		vPanel.add(separatorLabel);
		
		HorizontalPanel greatPanel = new HorizontalPanel();
		
		Label tabSeparator = new Label();
		tabSeparator.setWidth("30px");
		greatPanel.add(tabSeparator);
		greatPanel.add(vPanel);
		greatPanel.setStyleName("messagesLines");
		
		
		this.add(greatPanel);
	}
	
	public LendMeMsgsRep(String message, String subject, 
			String sender, String date, String lendingId) {
		this();
		
		fillPropertiesMap(message, subject, sender, date, lendingId);
		
		if (subject.length() > 50) {
			subjectLabel.setText(subject.substring(0, 50) + "...");
		} else {
			subjectLabel.setText(subject); 
		}
		
		dateLabel.setText(date);
	}
	
	
	private void fillPropertiesMap(String message, String subject, 
			String sender, String date, String lendingId) {
		messageProperties.put("message", message);
		messageProperties.put("subject", subject);
		messageProperties.put("sender", sender);
		messageProperties.put("date", date);
		messageProperties.put("lendingId", lendingId);
	}

	public String getMessage() {
		return messageProperties.get("message");
	}
	
	public String getSubject() {
		return messageProperties.get("subject");
	}
	
	public String getSender() {
		return messageProperties.get("sender");
	}
	
	public String getLendingId() {
		return messageProperties.get("lendingId");
	}
	
	public String getDate() {
		return messageProperties.get("date");
	}
}
