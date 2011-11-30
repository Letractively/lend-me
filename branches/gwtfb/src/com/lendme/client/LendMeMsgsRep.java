package com.lendme.client;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LendMeMsgsRep extends AbsolutePanel {
	
	private Map<String, String> messageProperties = new TreeMap<String, String>();
	private final Label senderLabel;
	private final Label subjectLabel;
	private final Label dateLabel;
	private final TextBox messageTextBox;
	private NewMsgForm answerMsg;
	
	private LendMeAsync lendMeService;
	private String solicitorSessionId;

	/**
	 * @wbp.parser.constructor
	 */
	public LendMeMsgsRep(String currentSessionId, LendMeAsync lendMeService, String senderLogin) {
		
		this.solicitorSessionId = currentSessionId;
		this.lendMeService = lendMeService;
		
		setWidth("550px");
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("550px");
		
		subjectLabel = new Label("Assunto");
		subjectLabel.setStyleName("messagesLines");
		vPanel.add(subjectLabel);
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setStyleName("messagesLines");
		hPanel.setSize("484px", "30px");
		
		senderLabel = new Label("Remetente: ");
		senderLabel.setWidth("300px");
		senderLabel.setStyleName("messagesLines");
		hPanel.add(senderLabel);
		
		dateLabel = new Label("date");
		dateLabel.setStyleName("messagesLines");
		hPanel.add(dateLabel);
		dateLabel.setWidth("50");
		
		vPanel.add(hPanel);
		
		PushButton answerMsgButton = new PushButton("New button");
		answerMsgButton.getUpFace().setText("Responder");
		hPanel.add(answerMsgButton);
		answerMsgButton.setSize("70px", "20px");
		
		answerMsgButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				sendAnswerMessage();
			}
		});
		
		hPanel.add(answerMsgButton);
		
		messageTextBox = new TextBox();
		messageTextBox.setReadOnly(true);
		messageTextBox.setText("Mensagem de exemplo...");
		messageTextBox.setStyleName("messagesLines");
		messageTextBox.setWidth("500px");
		vPanel.add(messageTextBox);

		HorizontalPanel greatPanel = new HorizontalPanel();
		
		Image userPhoto = new Image("https://graph.facebook.com/" + senderLogin + "/picture");
		greatPanel.add(userPhoto);
		userPhoto.setSize("45px", "48px");
		greatPanel.add(vPanel);
		greatPanel.setStyleName("messagesLines");
		
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.add(greatPanel);
		
		
		this.add(decPanel);
	}
	
	public LendMeMsgsRep(LendMeAsync lendMeService, String currentSessionId, String message, String subject, 
			String sender, String date, String lendingId) {
		this(currentSessionId, lendMeService, sender);
		
		fillPropertiesMap(message, subject, sender, date, lendingId);
		
		if (subject.length() > 60) {
			subjectLabel.setText(subject.substring(0, 57) + "...");
		} else {
			subjectLabel.setText(subject); 
		}
		
		if (sender.length() > 23) {
			senderLabel.setText(senderLabel.getText() + sender.substring(0, 20) + "...");
		} else {
			senderLabel.setText(senderLabel.getText() + sender);
		}

//		setMessageText(message);
		messageTextBox.setText(message);
		
		dateLabel.setText(date);
	}
	
//	private void setMessageText(String message) {
//		for (int i = 0; i < message.length(); i+=59) {
//			messageTextBox.setText(message);
//		}
//		messageTextBox.setHeight(String.valueOf(message.length()/60)+"px");
//	}
	
	
	private void fillPropertiesMap(String message, String subject, 
			String sender, String date, String lendingId) {
		messageProperties.put("message", message);
		messageProperties.put("subject", subject);
		messageProperties.put("sender", sender);
		messageProperties.put("date", date);
		messageProperties.put("lendingId", lendingId);
	}
	
	private void sendAnswerMessage() {
		answerMsg = new NewMsgForm(lendMeService, solicitorSessionId, 
					getSender(), getSubject(), getLendingId());
		answerMsg.center();
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
