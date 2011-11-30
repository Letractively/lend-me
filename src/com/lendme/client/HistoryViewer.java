package com.lendme.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ListBox;


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
	final String GROUP_ICON_PATH = "http://www.iconeasy.com/icon/thumbnails/Internet%20%26%20Web/Users/User%20group%20Icon.jpg";
final String PERSONAL_ICON_PATH = "http://icons.iconarchive.com/icons/custom-icon-design/pretty-office/48/personal-information-icon.png";
	private String scope;
	private Image group;
	private Image personalIcon;
	private ArrayList<LendMeActivityRep> historyList;
	private AbsolutePanel activityPanel;
	
	
	public HistoryViewer(LendMeAsync lendMeService,String solicitorSessionId, String scope) {
		this.scope = scope;
		this.solicitorSessionId = solicitorSessionId;
		this.lendMeService = lendMeService;
		this.historyList = new ArrayList<LendMeActivityRep>();
		
		container.setStyleName("topicsLines");
		container.setSize("600px", "500px");
		
		container.setWidth("570px");

		ScrollPanel scrollPanel = new ScrollPanel();
		rootPanel.add(scrollPanel, 0, 32);
		scrollPanel.setSize("600px", "515px");
		rootPanel.setSize("600px", "550px");
		scrollPanel = new ScrollPanel();
		rootPanel.addStyleName("headerBackground");
		scrollPanel.setSize("700px", "580px");
		rootPanel.setSize("700px", "650px");
		rootPanel.add(scrollPanel, 0, 70);
		
		scrollPanel.add(container);
		
		topPanel = new HorizontalPanel();
		topPanel.setWidth("700px");
		container.add(topPanel);
		
		numberOfMsgsLabel = new Label("numOfMsgs");
		numberOfMsgsLabel.setText("Visualiza\u00E7ao:");
		numberOfMsgsLabel.setSize("210px", "15px");
		topPanel.add(numberOfMsgsLabel);
		
		subjectLabel = new Label("subject");
		subjectLabel.setText((scope.equals("all")?"Todos do Sistema":"Pessoal"));
		subjectLabel.setSize("210px", "15px");
		topPanel.add(subjectLabel);
		
		topPanel.addStyleName("topicsHeader");
		
		activityPanel = new AbsolutePanel();
		activityPanel.setSize("700px", "564px");
		activityPanel.setStyleName("backGround2");
		container.add(activityPanel);

		
		systemMsg = new Label("");
		systemMsg.addStyleName("barra");
		systemMsg.setSize("700px", "10px");
		rootPanel.add(systemMsg, 0, 58);
		
		titulo = new Label("Histórico de atividades");
		titulo.addStyleName("defaultTitle");
		titulo.setSize("260px", "29px");
		rootPanel.add(titulo, 20, 17);
		
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
		rootPanel.add(errorLabel);
		
		group = new Image(this.GROUP_ICON_PATH);
		group.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refreshPage("all");
			}
		});
		group.setSize("48px", "48px");
		rootPanel.add(group, 351, 4);
		
		
		personalIcon = new Image(this.PERSONAL_ICON_PATH);
		personalIcon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refreshPage("personalHistoric");
			}
		});
		personalIcon.setSize("48px", "48px");
		rootPanel.add(personalIcon, 517, 4);

		this.initWidget(rootPanel);
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
			lendMeService.getJointActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, String>>() {

				@Override
				public void onSuccess(Map<String, String> result) {
					
					for(String date : result.keySet()){
						systemMsg.setText("Veja as suas atividades no Lend-me!");

						String activityDate = date;
						String description = result.get(activityDate);
						historyList.add(new LendMeActivityRep(description, activityDate));
						//activityPanel.insert(new LendMeActivityRep(description, activityDate), i*);						
//						activityPanel.add(new LendMeActivityRep(description, activityDate));						
					}

					addTopicstoPanel();
					
//					Iterator<LendMeActivityRep> myItemsIterator = historyList.iterator();
//					int i = 0;
//					while(myItemsIterator.hasNext()){
//						int top = i*70;
//						activityPanel.add(myItemsIterator.next(),top,0);
//						activityPanel.setHeight(Integer.toString((top + 70 ))+"px");
//						i++;
//					}
				}

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(errorMessage + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}
			});
		}
		else{
			lendMeService.getActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, String>>() {

				@Override
				public void onSuccess(Map<String, String> result) {
					for(String date : result.keySet()){
						systemMsg.setText("Veja o que seus amigos andam fazendo no Lend-me!");
						String activityDate = date;
						String description = result.get(activityDate);
						activityPanel.add(new LendMeActivityRep(description, activityDate));						
					}
					
//					Iterator<LendMeActivityRep> myItemsIterator = historyList.iterator();
//					int i = 0;
//					while(myItemsIterator.hasNext()){
//						int top = i*70;
//						activityPanel.add(myItemsIterator.next(),top,0);
//						activityPanel.setHeight(Integer.toString((top + 70 ))+"px");
//						i++;
//					}
					
					addTopicstoPanel();

				}

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(errorMessage + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}
			});
		}

		
		
	}
}
