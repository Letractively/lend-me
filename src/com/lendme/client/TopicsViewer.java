package com.lendme.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicsViewer extends Composite {
	
	private List<LendMeTopicsRep> topics = new ArrayList<LendMeTopicsRep>();
	private AbsolutePanel rootPanel = new AbsolutePanel();
	private VerticalPanel container = new VerticalPanel();
	private NewMsgForm newMsgForm;
	private final Label errorLabel; 
	private final String errorMessage = "Erro: LendMe nao conseguiu obter informacao do servidor.";
	
	final String solicitorSessionId;  
	private boolean topicsAlreadyAdded = false;
	private LendMeAsync lendMeService;
	private String currentTopicType;
	private ListBox topicTypeListBox;
	
	public TopicsViewer(LendMeAsync lendMeService,String solicitorSessionId, String topicType) {
		
		this.lendMeService = lendMeService;
		this.solicitorSessionId = solicitorSessionId;
		this.currentTopicType = topicType;
		container.setStyleName("h1");
		
		container.setWidth("570px");

		ScrollPanel scrollPanel = new ScrollPanel();
		rootPanel.add(scrollPanel, 0, 32);
		scrollPanel.setSize("600px", "515px");
		rootPanel.setSize("600px", "550px");
		
		scrollPanel.add(container);
		
		errorLabel = new Label("error");
		container.add(errorLabel);
		errorLabel.setStyleName("topicsLines");
		
		HorizontalPanel topPanel = new HorizontalPanel();
		container.add(topPanel);
		topPanel.setWidth("600px");
		
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
		errorLabel.setVisible(false);
		
		rootPanel.add(scrollPanel, 0, 32);
		
		Button newMsgButton = new Button("Nova Mensagem");
		rootPanel.add(newMsgButton, 465, 0);
		newMsgButton.setSize("114px", "29px");
		
		newMsgButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				createNewMsg();
			}
		});
		
		Label lblTipoDeTpico = new Label("Tipo de Tópico:");
		rootPanel.add(lblTipoDeTpico, 120, 0);
		
		topicTypeListBox = new ListBox();
		topicTypeListBox.setName("topic type combo");
		topicTypeListBox.addItem("offtopic");
		topicTypeListBox.addItem("negociacao");
		topicTypeListBox.addItem("todos");
		topicTypeListBox.setItemSelected(2, true);
		
		rootPanel.add(topicTypeListBox, 225, 0);
		
		topicTypeListBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				removeTopicsFromPanel();
				getTopics();
			}
		});
		
		getTopics();
		
		this.initWidget(rootPanel);
	}
	
	private void addTopics(Map<String, String[]>result) {
		for (String topic : result.keySet()) {
			topics.add(new LendMeTopicsRep(this.lendMeService, this.solicitorSessionId, topic, result.get(topic)[0],
					result.get(topic)[1], result.get(topic)[2]));
		}
		addTopicstoPanel();
	}
	
	private void createNewMsg() {
		if (newMsgForm == null) {
			newMsgForm = new NewMsgForm(lendMeService, solicitorSessionId);
		} else {
			newMsgForm.resetFields();
		}
		newMsgForm.center();
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
		removeTopicsFromPanel();
		addTopicstoPanel();
	}
	
	private void removeTopicsFromPanel() {
		for (LendMeTopicsRep topic : topics) {
			container.remove(topic);
		}
	}
 	
	private void getTopics() {
		
		topics.clear();
		currentTopicType = topicTypeListBox.getItemText(topicTypeListBox.getSelectedIndex());
		
		lendMeService.getTopics(solicitorSessionId, currentTopicType, new AsyncCallback<Map<String,String[]>>() {

			@Override
			public void onSuccess(Map<String, String[]> result) {
				addTopics(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(errorMessage + " - " + caught.getMessage());
				errorLabel.setVisible(true);
			}
		});
	}
}
