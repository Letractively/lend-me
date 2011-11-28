package com.lendme.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.FocusPanel;

public class ItemsViewer extends Composite{
	
	private AbsolutePanel conteinerPanel;
	private ScrollPanel scrollPanel;
	private AbsolutePanel topBar;
	private VerticalPanel rootPanel;
	private LendMeItensCreatorRepresentation creator;
	private List<LendMeItensRepresentation> myItems;
	private ItemViewer viewer;
	private Label errorLabel;
		
	private final String defaultImageURL = "http://cdn2.iconfinder.com/data/icons/starwars/icons/128/clone-old.png";
	private int actualHeightConteiner = 75;
	private int actualX = 1;
	private int actualY = 1;
	private final int  XCol1 = 1;
	private final int  XCol2 = 270;
	
	private final LendMeAsync lendmeAsync;
	
	private final ItemsViewer iAm;
	private String idSession;
	
	public ItemsViewer(LendMeAsync lendme, String idSession) {
	
		this.lendmeAsync = lendme;
		this.idSession = idSession;
		iAm = this;
		
		
				creator = new LendMeItensCreatorRepresentation(lendmeAsync, idSession);//idSession depois dos testes
				myItems = new ArrayList<LendMeItensRepresentation>();
				
				rootPanel = new VerticalPanel();
				scrollPanel = new ScrollPanel();
				scrollPanel.setStyleName("gwt-DialogBox");
				scrollPanel.setSize("636px", "408px");
				
				conteinerPanel = new AbsolutePanel();
				conteinerPanel.setStyleName("dialogVPanel");
				conteinerPanel.setSize("601px", "300");
				
				scrollPanel.setWidget(conteinerPanel);
				
				topBar = new AbsolutePanel();
				topBar.setSize("100%", "31px");
				
				PushButton pshbtnNewButton = new PushButton(" +1 ");
				pshbtnNewButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						creator.center();
					}
				});
				topBar.add(pshbtnNewButton, 570, 5);
				pshbtnNewButton.setSize("18px", "13px");
				
				Label lblMyItems = new Label("My Items");
				topBar.add(lblMyItems, 10, 2);
				
				errorLabel = new Label("Ocorreu um erro de comunicação com o servidor");
				errorLabel.setStyleName("alert");
				topBar.add(errorLabel, 12, 17);
				errorLabel.setVisible(false);
				
				/*Povoamento da lista de itens*/
				refresh(idSession);
				
				rootPanel.add(topBar);
				rootPanel.add(scrollPanel);
				initWidget(rootPanel);
		
		}


		public void refresh(String idSession) {
			lendmeAsync.getItems(idSession, new AsyncCallback<Map<String, String[]>>() {
			
			@Override
			public void onSuccess(Map<String, String[]> result) {
				
				String[] values;
				errorLabel.setVisible(false);
				for(String actualKey : result.keySet()){
					values = result.get(actualKey);
					myItems.add(new LendMeItensRepresentation(defaultImageURL, values[0], values[1],values[2], Boolean.valueOf(values[3])));
				}
					Iterator<LendMeItensRepresentation> myItemsIterator = myItems.iterator();
					int i = 0;
					
				while(myItemsIterator.hasNext()){
						
						if(i%2 != 0) {
							actualX = XCol2;
							actualY -=90;
						}
						else {
							actualX = XCol1;
						}
						
						conteinerPanel.add(myItemsIterator.next(),actualX,actualY);
						conteinerPanel.setHeight(Integer.toString((actualHeightConteiner +=45))+"px");
						actualY += 90;
						i++;
					}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(errorLabel.getText()+": "+caught.getMessage());
				errorLabel.setVisible(true);				
			}
		});
	}


	public ItemViewer getViewer() {
		return viewer;
	}

	public void setViewer(ItemViewer viewer) {
		this.viewer = viewer;
	}
}
