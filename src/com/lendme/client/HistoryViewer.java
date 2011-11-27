package com.lendme.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.gwtext.client.widgets.Button;
@SuppressWarnings("deprecation")

public class HistoryViewer extends Composite {
	
	private ArrayList<LendMeHistoricElementRepresent> historyList;
	private AbsolutePanel headerPenel;
	private RootPanel rootPanel;
	private Button forwardButton;
	private Button backButton;
	private Image logoHistoryPanel;
	
	@SuppressWarnings("deprecation")
	private VerticalSplitPanel separator;
	
	
	public HistoryViewer(LendMeAsync lendMeService, String solicitorSessionID){
		
		historyList = new ArrayList<LendMeHistoricElementRepresent>();
		
		rootPanel = RootPanel.get();
		rootPanel.setWidth("600px");
		
		headerPenel = new AbsolutePanel();
		rootPanel.add(headerPenel, 0, 0);
		headerPenel.setSize("450px", "83px");
		
		forwardButton = new Button("<< Mais recentes");
		headerPenel.add(forwardButton, 190, 28);
		
		backButton = new Button("Mais antigas >>");
		headerPenel.add(backButton, 318, 28);
		backButton.setSize("122px", "21px");
		
		logoHistoryPanel = new Image((String) null);
		headerPenel.add(logoHistoryPanel, 10, 10);
		logoHistoryPanel.setSize("131px", "47px");
		
		separator = new VerticalSplitPanel();
		headerPenel.add(separator, 0, 75);
		separator.setSize("450px", "8px");
		
		Button btnPessoal = new Button("pessoal");
		headerPenel.add(btnPessoal, 200, 55);
		
		Button geral = new Button("pessoal");
		headerPenel.add(geral, 317, 55);
		geral.setSize("58px", "24px");
		
		AbsolutePanel historyPenel = new AbsolutePanel();
		rootPanel.add(historyPenel, 0, 82);
		historyPenel.setSize("450px", "220px");
		
		
		lendMeService.getActivityHistory(solicitorSessionID,new AsyncCallback<String[]>() {

			@Override
			public void onSuccess(String[] result) {
				// TODO Auto-generated method stub
				//for()
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
