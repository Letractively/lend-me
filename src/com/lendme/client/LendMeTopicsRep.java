package com.lendme.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LendMeTopicsRep extends AbsolutePanel implements HasTopicBeenAccessedHandler {
	
	private Map<String, String> topicProperties = new TreeMap<String, String>();
	private List<LendMeMsgsRep> topicMessages = new ArrayList<LendMeMsgsRep>();
	private final Button expandButton;
	private final Label subjectLabel;
	private final Label dateLabel;
	private final Label numMsgsLabel;
	private final Label errorLabel;
	private final VerticalPanel msgPanel;
	private final LendMeAsync lendMeService;
	
	private String solicitorSessionId;
	 
	/**
	 * @wbp.parser.constructor
	 */
	public LendMeTopicsRep(LendMeAsync lendMeService, String solicitorSessionId) {
		
		this.lendMeService = lendMeService;
		
		this.solicitorSessionId = solicitorSessionId;
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setStyleName("topicsLines");
		hPanel.setHeight("30px");
		
		expandButton = new Button();
		expandButton.setStyleName("gwt-button");
		expandButton.setText("+");
		expandButton.setWidth("30px");
		hPanel.add(expandButton);
		
		numMsgsLabel = new Label("numMsgs");
		numMsgsLabel.setStyleName("topicsLines");
		numMsgsLabel.setWidth("100px");
		numMsgsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hPanel.add(numMsgsLabel);
		
		subjectLabel = new Label("subject");
		subjectLabel.setStyleName("topicsLines");
		subjectLabel.setWidth("370px");
		hPanel.add(subjectLabel);
		
		expandButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (expandButton.getText().equals("+")) {
					getMessages();
				} else {
					expandOrCollapseMsgs();
				}
			}
		});
		
		dateLabel = new Label("date");
		dateLabel.setStyleName("topicsLines");
		dateLabel.setWidth("100px");
		hPanel.add(dateLabel);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setStyleName("topicsLines");
		vPanel.setWidth("510px");
		
		errorLabel = new Label("error");
		this.add(errorLabel);
		errorLabel.setVisible(false);
		
		vPanel.add(hPanel);
		
		msgPanel = new VerticalPanel();
		msgPanel.setStyleName("topicsLines");
		msgPanel.setWidth("510px");
		msgPanel.setVisible(false);
		
		vPanel.add(msgPanel);
		
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.add(vPanel);
		
		this.add(decPanel);
	}
	
	public LendMeTopicsRep(LendMeAsync lendMeService, String solicitorSessionId, String subject, String id, String numMsgs, String date) {
		this(lendMeService, solicitorSessionId);
		
		fillPropertiesMap(subject, id, numMsgs, date);
		
		numMsgsLabel.setText(numMsgs);
		
		if (subject.length() > 40) {
			subjectLabel.setText(subject.substring(0, 37) + "...");
		} else {
			subjectLabel.setText(subject);
		}
		
		dateLabel.setText(date);
	}
	
	
	private void fillPropertiesMap(String subject, String id, String numMsgs, String date) {
		topicProperties.put("subject", subject);
		topicProperties.put("id", id);
		topicProperties.put("numMsgs", numMsgs);
		topicProperties.put("date", date);
	}

	public String getSender() {
		return topicProperties.get("sender");
	}
	
	public String getSubject() {
		return topicProperties.get("subject");
	}
	
	public String getId() {
		return topicProperties.get("id");
	}
	
	public String getNumMsgs() {
		return topicProperties.get("numMsgs");
	}
	
	public String getDate() {
		return topicProperties.get("date");
	}
	
	private void expandOrCollapseMsgs() {
		if (expandButton.getText().contains("+")) {
			expandButton.setText("--");
			
			
			for (int i = 0; i < topicMessages.size(); i++) {
				LendMeMsgsRep currMessage = topicMessages.get(i);
				msgPanel.add(currMessage);
			}
			msgPanel.setVisible(true);
			
		} else {
			expandButton.setText("+");
			for (LendMeMsgsRep message : topicMessages) {
				msgPanel.remove(message);
			}
			topicMessages.clear();
			msgPanel.setVisible(false);
		}
		
		this.fireEvent(new TopicAccessedEvent());
		
	}
	
	private List<LendMeMsgsRep> getMessages() {
		
		lendMeService.getTopicMessages(solicitorSessionId, getId(), new AsyncCallback<Map<String,String[]>>() {

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText("Could not get user messages: " + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}

				@Override
				public void onSuccess(Map<String, String[]> result) {
					List<String> resultList = new ArrayList<String>(result.keySet());
					Collections.reverse(resultList);
					for (String message : resultList) {
					topicMessages.add(new LendMeMsgsRep(lendMeService, solicitorSessionId, result.get(message)[0], result.get(message)[1],
						result.get(message)[2], result.get(message)[3], result.get(message)[4]));
					}
					
					expandOrCollapseMsgs();
				}
				
			});
		
//		for (int i = 0; i < 3; i++) {
//			topicMessages.add(new LendMeMsgsRep(lendMeService, solicitorSessionId,
//					"message"+i, "subject"+i,"sender"+i,
//					"date"+i, "00"+i));
//		}
		return topicMessages;
	}

	@Override
	public void addTopicAccessedHandler(TopicAccessedHandler handler) {
		addHandler(handler, TopicAccessedEvent.getType());
	}
}
