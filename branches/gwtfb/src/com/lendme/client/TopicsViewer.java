package com.lendme.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicsViewer extends Composite {
	
	private List<LendMeTopicsRep> topics = new ArrayList<LendMeTopicsRep>();
	private AbsolutePanel rootPanel = new AbsolutePanel();
	private VerticalPanel container = new VerticalPanel();
	private final Label errorLabel; 
	private final String errorMessage = "Error: LendMe could not retrieve information from the server.";
	final String solicitorSessionId;  
	
	private boolean topicsAlreadyAdded = false;
	private LendMeAsync lendMeService;
	
	
	public TopicsViewer(LendMeAsync lendMeService,String solicitorSessionId, String topicType) {
		
		this.lendMeService = lendMeService;
		this.solicitorSessionId = solicitorSessionId;
		
		HorizontalPanel topPanel = new HorizontalPanel();
		topPanel.setWidth("600px");
		rootPanel.add(topPanel);
		
		errorLabel = new Label("error");
		rootPanel.add(errorLabel);
		errorLabel.setVisible(false);
		
		Label expandCollapseLabel = new Label("expand/collapse");
		expandCollapseLabel.setText("+/--");
		expandCollapseLabel.setSize("30px", "15px");
		topPanel.add(expandCollapseLabel);
		
		Label numberOfMsgsLabel = new Label("numOfMsgs");
		numberOfMsgsLabel.setText("No. Msgs");
		numberOfMsgsLabel.setSize("210px", "15px");
		topPanel.add(numberOfMsgsLabel);
		
		Label subjectLabel = new Label("subject");
		subjectLabel.setText("Assunto");
		subjectLabel.setSize("210px", "15px");
		topPanel.add(subjectLabel);
		
		Label creationDateLabel = new Label("date");
		creationDateLabel.setText("Criacao");
		creationDateLabel.setSize("100px", "15px");
		topPanel.add(creationDateLabel);
		
		topPanel.addStyleName("topicsHeader");
		
		container.setWidth("570px");

		ScrollPanel scrollPanel = new ScrollPanel();
		rootPanel.add(scrollPanel);
		scrollPanel.setSize("600px", "270px");
		rootPanel.setSize("600px", "300px");
		
		scrollPanel.add(container);
		
		rootPanel.add(scrollPanel);
		
		for (int i = 0; i < 3; i++) {
			topics.add(new LendMeTopicsRep(this.lendMeService, this.solicitorSessionId ,"subject"+i, "id"+i,
					""+i, "date"+i));
		}
		
//		lendMeService.getTopics(solicitorSessionId, topicType, new AsyncCallback<Map<String,String[]>>() {
//
//			@Override
//			public void onSuccess(Map<String, String[]> result) {
//				addTopics(result);
//			}
//
//			@Override
//			public void onFailure(Throwable caught) {
//				errorLabel.setText(errorMessage + " - " + caught.getMessage());
//				errorLabel.setVisible(true);
//			}
//		});
		
		addTopicstoPanel();
		
		this.initWidget(rootPanel);
	}
	
	private void addTopics(Map<String, String[]>result) {
		for (String topic : result.keySet()) {
			topics.add(new LendMeTopicsRep(this.lendMeService, this.solicitorSessionId, topic, result.get(topic)[0],
					result.get(topic)[1], result.get(topic)[2]));
		}
	}

	public void addTopicstoPanel() {
		for (int i = 0; i < topics.size(); i++) {
			LendMeTopicsRep currentTopic = topics.get(i); 
			if (!topicsAlreadyAdded) {
				currentTopic.addTopicAccessedHandler( new TopicAccessedHandler() {
				
					@Override
					public void onTopicAccessed(TopicAccessedEvent event) {
						refresh();
					}
				});
			}
			container.add(currentTopic);
		}
		if (! topicsAlreadyAdded) topicsAlreadyAdded = true;
	}
	
	public void refresh() {
		for (LendMeTopicsRep topic : topics) {
			container.remove(topic);
		}
		addTopicstoPanel();
	}
}
