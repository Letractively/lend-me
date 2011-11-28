package com.lendme.client;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PushButton;

public class LendMeMsgsRep extends AbsolutePanel {
	
	private Map<String, String> messageProperties = new TreeMap<String, String>();
	private final Label senderLabel;
	private final Label subjectLabel;
	private final Label dateLabel;
	private final Label messageLabel;
	private NewMsgForm answerMsg;
	
	private final String separatorString = 
		"-----------------------------------------------------------------" +
		"-----------------------------------" ;//100 tracos
	
	private LendMeAsync lendMeService;
	private String solicitorSessionId;
	
	/**
	 * @wbp.parser.constructor
	 */
	public LendMeMsgsRep(String currentSessionId, LendMeAsync lendMeService) {
		
		this.solicitorSessionId = currentSessionId;
		this.lendMeService = lendMeService;
		
		setWidth("530");
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("500px");
		
		subjectLabel = new Label("Assunto");
		subjectLabel.setStyleName("messagesLines");
		vPanel.add(subjectLabel);
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setStyleName("messagesLines");
		hPanel.setSize("484px", "30px");
		
		senderLabel = new Label("Remetente");
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
		answerMsgButton.setSize("60px", "20px");
		
		answerMsgButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				sendAnswerMessage();
			}
		});
		
		hPanel.add(answerMsgButton);
		
		messageLabel = new Label("Mensagem de Exemplo...");
		messageLabel.setStyleName("messagesLines");
		messageLabel.setWidth("500px");
		vPanel.add(messageLabel);

		Label separatorLabel = new Label();
		separatorLabel.setStyleName("messagesLines");
		separatorLabel.setWidth("500px");
		separatorLabel.setText(separatorString);
		vPanel.add(separatorLabel);
		
		HorizontalPanel greatPanel = new HorizontalPanel();
		
		Image image = new Image("http://icons.iconarchive.com/icons/fasticon/fast-icon-users/128/user-icon.png");
		greatPanel.add(image);
		image.setSize("45px", "48px");
		greatPanel.add(vPanel);
		greatPanel.setStyleName("messagesLines");
		
		
		this.add(greatPanel);
	}
	
	public LendMeMsgsRep(LendMeAsync lendMeService, String currentSessionId, String message, String subject, 
			String sender, String date, String lendingId) {
		this(currentSessionId, lendMeService);
		
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
