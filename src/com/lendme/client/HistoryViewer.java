package com.lendme.client;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HistoryViewer extends Composite {
	
	private AbsolutePanel rootPanel = new AbsolutePanel();
	private VerticalPanel container = new VerticalPanel();
	private final Label errorLabel; 
	private final String errorMessage = "Erro: LendMe nao conseguiu obter informacao do servidor.";
	private Label systemMsg;
	private Label titulo;
	private ScrollPanel scrollPanel;
	private HorizontalPanel topPanel;
	private Label numberOfMsgsLabel;
	private Label subjectLabel;
	private LendMeAsync lendMeService;
	private String solicitorSessionId;
	private Image refreshIcon;
	private Label mark1;
	private Label mark2;
	private DockPanel datePanel;
	final String REFRESH_ICON_PATH = "http://icons.iconarchive.com/icons/deleket/button/48/Button-Refresh-icon.png";
	final String GROUP_ICON_PATH = "http://icons.iconarchive.com/icons/fasticon/leopard-iphone/48/Group-Folder-icon.png";
	final String PERSONAL_ICON_PATH = "http://icons.iconarchive.com/icons/fasticon/leopard-iphone/48/Users-Folder-icon.png";
	private String scope;
	private Label errorLabel;
	private Image group;
	private Image personalIcon;
	private ArrayList<LendMeActivityRep> historyList;
	private AbsolutePanel activityPanel;
	
	
	public HistoryViewer(LendMeAsync lendMeService,String solicitorSessionId, String scope) {
		
		container.setStyleName("topicsLines");
		
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
		
		Label numberOfMsgsLabel = new Label("numOfMsgs");
		numberOfMsgsLabel.setText("Visualiza\u00E7ao:");
		numberOfMsgsLabel.setSize("210px", "15px");
		topPanel.add(numberOfMsgsLabel);
		
		Label subjectLabel = new Label("subject");
		subjectLabel.setText((scope.equals("all")?"Todos do Sistema":"Pessoal"));
		subjectLabel.setSize("210px", "15px");
		topPanel.add(subjectLabel);
		
		topPanel.addStyleName("topicsHeader");
		
		activityPanel = new AbsolutePanel();
		activityPanel.setSize("700px", "562px");
		activityPanel.setStyleName("backGround2");
		final VerticalPanel activityPanel = new VerticalPanel();
		container.add(activityPanel);

		
		systemMsg = new Label("");
		systemMsg.addStyleName("barra");
		systemMsg.setSize("700px", "10px");
		rootPanel.add(systemMsg, 0, 58);
		
		titulo = new Label("Histórico de atividades");
		titulo.addStyleName("defaultTitle");
		titulo.setSize("260px", "29px");
		rootPanel.add(titulo, 60, 19);
		
		mark1 = new Label("**");
		rootPanel.add(mark1, 416, 23);
		
		mark2 = new Label("**");
		mark2.setSize("14px", "20px");
		rootPanel.add(mark2, 489, 23);

		
		rootPanel.add(scrollPanel, 0, 70);

		

		refreshIcon = new Image(REFRESH_ICON_PATH);
		refreshIcon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refreshPage();
			}
		});
		refreshIcon.setSize("48px", "48px");
		rootPanel.add(refreshIcon, 436, 4);
		
		refreshPage();

		
		errorLabel = new Label("Ocorreu um erro inesperado! ");
		errorLabel.setStyleName("defaultErrorStyle");
		errorLabel.setVisible(false);
		
		rootPanel.add(scrollPanel, 0, 32);
		
		
		personalIcon = new Image(this.PERSONAL_ICON_PATH);
		personalIcon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refreshPage("personalHistoric");
			}
		});
		personalIcon.setSize("48px", "48px");
		rootPanel.add(personalIcon, 517, 4);

		this.initWidget(rootPanel);
		
		Image image = new Image((String) null);
		image.setUrl("http://icons.iconarchive.com/icons/kawsone/teneo/48/2000Facts-2-icon.png");
		rootPanel.add(image, 5, 5);
		image.setSize("48px", "48px");
	}
	
	private void refreshPage(){
		refreshPage(this.scope);
	}
	
	public void addTopicstoPanel() {
		Iterator<LendMeActivityRep> myActivitiesIterator = historyList.iterator();
		int i = 0;
		while(myActivitiesIterator.hasNext()){
			int top = i*70;
			activityPanel.add(myActivitiesIterator.next(),top,0);
			activityPanel.setHeight(Integer.toString((top + 70 ))+"px");
			i++;
		}
		rootPanel.add(activityPanel);

	}
	
	private void refreshPage(String scope){
		if ( scope.equals("all") ){
			lendMeService.getJointActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, ArrayList<String[]>>>() {

				@Override
				public void onSuccess(Map<String, ArrayList<String[]>> result) {
					for (String date : result.keySet() ){
						DockPanel datePanel = new DockPanel();
						datePanel.setWidth("660px");
						datePanel.add(new Label("Atividates em "+date), DockPanel.CENTER);
						activityPanel.add(datePanel);
						for ( String[] content : result.get(date) ){
							activityPanel.add(new LendMeActivityRep(content[0], content[1], content[2]));
						}
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(errorMessage + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}
			});
		}
		else{
			lendMeService.getActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, ArrayList<String[]>>>() {

				@Override
				public void onSuccess(Map<String, ArrayList<String[]>> result) {
					for (String date : result.keySet() ){
						DockPanel datePanel = new DockPanel();
						datePanel.setWidth("660px");
						datePanel.add(new Label("Atividates em "+date), DockPanel.CENTER);
						activityPanel.add(datePanel);
						for ( String[] content : result.get(date) ){
							activityPanel.add(new LendMeActivityRep(content[0], content[1], content[2]));
						}
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(errorMessage + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}
			});
		}
		
		this.initWidget(rootPanel);
		
	}
	
}
