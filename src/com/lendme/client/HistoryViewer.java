package com.lendme.client;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.gwtext.client.widgets.Button;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.core.EventObject;
@SuppressWarnings("deprecation")

public class HistoryViewer extends Composite {
	
	private ArrayList<LendMeHistoricElementRepresent> historyElementBuffer;
	private AbsolutePanel headerPenel;
	private RootPanel rootPanel;
	private Button forwardButton;
	private Button backButton;
	private AbsolutePanel historyPenel;
	private Label lblMeuHistrico;
	private final int MAX_NUMBER_HISTORY_ELEMENTS = 5;
	private String solicitorSessionID;
	private LendMeAsync lendMeService;
	private int pageNumber;
	
	
	public HistoryViewer(LendMeAsync lendMeService, String solicitorSessionID){
		
		historyElementBuffer = new ArrayList<LendMeHistoricElementRepresent>();
		this.solicitorSessionID = solicitorSessionID;
		this.lendMeService = lendMeService;
		
		rootPanel = RootPanel.get();
		rootPanel.setSize("600px", "450px");
		
		headerPenel = new AbsolutePanel();
		rootPanel.add(headerPenel, 0, 0);
		headerPenel.setSize("450px", "50px");
		
		forwardButton = new Button("<< Mais recentes");
		forwardButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				previousPage();
			}
		});
		headerPenel.add(forwardButton, 190, 10);
		
		backButton = new Button("Mais antigas >>");
		backButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				previousPage();
			}
		});
		headerPenel.add(backButton, 318, 10);
		backButton.setSize("122px", "21px");
		
		lblMeuHistrico = new Label("Histórico pessoal\n\n");
		headerPenel.add(lblMeuHistrico, 20, 10);
		lblMeuHistrico.setSize("104px", "39px");
		
		Label lblVejaOQue = new Label("Veja o que seus amigos adam fazendo no Lend-me!");
		headerPenel.add(lblVejaOQue, 0, 34);
		lblVejaOQue.setSize("450px", "15px");
		
		historyPenel = new AbsolutePanel();
		rootPanel.add(historyPenel, 0, 50);
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
				
				
			}
		});
		
	}
	
	private void loadBuffer(){
		lendMeService.getActivityHistory(solicitorSessionID,new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
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
					description = descriptions[i];
					time = result.get(description);
					historyElementBuffer.add(new LendMeHistoricElementRepresent(description, time));
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
	
	private void nextPage(){
		if(historyElementBuffer.get((pageNumber * MAX_NUMBER_HISTORY_ELEMENTS) + 1) != null){
			pageNumber = pageNumber + 1;
		}
		loadBuffer();
		addTopicstoPanel();
		initWidget(historyPenel);
	}
	
	private void previousPage(){
		if(pageNumber > 0){
			pageNumber = pageNumber - 1;
		}
		
		loadBuffer();
		addTopicstoPanel();
		initWidget(historyPenel);

	}
}
