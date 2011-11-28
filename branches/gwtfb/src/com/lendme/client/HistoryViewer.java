package com.lendme.client;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class HistoryViewer extends Composite {
	
	private ArrayList<LendMeHistoricElementRepresent> historyElementBuffer;
	private AbsolutePanel headerPenel;
	private RootPanel rootPanel;
	private AbsolutePanel historyPenel;
	private Label lblMeuHistrico;
	private final int MAX_NUMBER_HISTORY_ELEMENTS = 5;
	private String solicitorSessionID;
	private LendMeAsync lendMeService;
	private int pageNumber;
	final String NEXT_ICON_PATH = "http://www.prospectingmkt.com.br/files/images/portfolio/televisao/arrow-right.png";
	final String PREVIOUS_ICON_PATH = "http://www.prospectingmkt.com.br/files/images/portfolio/televisao/arrow-left.png";
	final String REFRESH_ICON_PATH = "http://www.gettyicons.com/free-icons/112/must-have/png/32/refresh_32.png";
	private Image refreshIcon;
	private Image next;
	Image previous;
	
	
	
	public HistoryViewer(LendMeAsync lendMeService, String solicitorSessionID){
		
		historyElementBuffer = new ArrayList<LendMeHistoricElementRepresent>();
		this.solicitorSessionID = solicitorSessionID;
		this.lendMeService = lendMeService;
		pageNumber = 1;
		
		rootPanel = RootPanel.get();
		rootPanel.setSize("600px", "600px");
		
		headerPenel = new AbsolutePanel();
		rootPanel.add(headerPenel, 0, 0);
		headerPenel.setSize("450px", "65px");
		
		lblMeuHistrico = new Label("Histórico pessoal\n\n");
		headerPenel.add(lblMeuHistrico, 20, 10);
		lblMeuHistrico.setSize("118px", "34px");
		
		Label lblVejaOQue = new Label("Veja o que seus amigos adam fazendo no Lend-me!");
		headerPenel.add(lblVejaOQue, 0, 50);
		lblVejaOQue.setSize("450px", "15px");
		
		refreshIcon = new Image((String) null);
		refreshIcon.setUrl(REFRESH_ICON_PATH);
		refreshIcon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				firstPage();
			}
		});
		headerPenel.add(refreshIcon, 343, 10);
		refreshIcon.setSize("32px", "32px");
		
		next = new Image((String) null);
		next.setUrl(NEXT_ICON_PATH);
		headerPenel.add(next, 391, 7);
		next.setSize("49px", "36px");
		
		previous = new Image((String) null);
		previous.setUrl(PREVIOUS_ICON_PATH);
		previous.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				nextPage();
			}
		});
		headerPenel.add(previous, 272, 10);
		previous.setSize("49px", "36px");
		//
		
		historyPenel = new AbsolutePanel();
		rootPanel.add(historyPenel, 0, 64);
		historyPenel.setSize("450px", "400px");
		
		lendMeService.getActivityHistory(solicitorSessionID,new AsyncCallback<Map<String, String>>() {
			@Override
			public void onSuccess(Map<String, String> result) {
				loadBuffer();
				addTopicstoPanel();
				initWidget(historyPenel);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fail: "+caught.getMessage());
			}
		});
		
	}
	
	private void loadBuffer(){
		lendMeService.getActivityHistory(solicitorSessionID,new AsyncCallback<Map<String, String>>() {

			
			public void onFailure(Throwable caught) {
				Window.alert("Fail: "+caught.getMessage());
				
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				String description = null;
				String time = null;
				
				int firstElement = (pageNumber - 1) * MAX_NUMBER_HISTORY_ELEMENTS;
				int finalElement = pageNumber * 5;
				
				Set<String> descriptionsSet = result.keySet();
				String[] descriptions = (String[]) descriptionsSet.toArray();
				
				for(int i = firstElement; i < finalElement; i++){
					if(descriptions[i] != null){
						description = descriptions[i];
						time = result.get(description);
						historyElementBuffer.add(new LendMeHistoricElementRepresent(description, time));
					}else{
						break;
					}
				}
				
			}
			
		});
	}
	
	public void addTopicstoPanel() {
		int i = 0;
		for(LendMeHistoricElementRepresent element : historyElementBuffer){
			if(i < MAX_NUMBER_HISTORY_ELEMENTS){
				historyPenel.add(element, 0, i * 80);
			}else{
				break;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private PopupPanel criatePopup(String message, int width, int height){
		PopupPanel popup = new PopupPanel();
		popup.setPixelSize(width, height);
		Label label = new Label(message);
		label.setPixelSize(width, height);
		popup.add(label);
		
		return null;
		
	}
	
	private void nextPage(){
		if(historyElementBuffer.get((pageNumber * MAX_NUMBER_HISTORY_ELEMENTS) + 1) != null){
			pageNumber = pageNumber + 1;
		}
		loadBuffer();
		addTopicstoPanel();
		initWidget(historyPenel);
	}
	
	private void firstPage(){
		pageNumber = 1;

		loadBuffer();
		addTopicstoPanel();
		initWidget(historyPenel);
	}
}
