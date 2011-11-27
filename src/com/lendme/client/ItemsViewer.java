package com.lendme.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ItemsViewer extends Composite{
	
	private AbsolutePanel conteinerPanel;
	private ScrollPanel scrollPanel;
	private AbsolutePanel topBar;
	private VerticalPanel rootPanel;
	private LendMeItensCreatorRepresentation creator;
	private List<LendMeItensRepresentation> myItems;
	private ItemViewer viewer;
		
	private final String defaultImageURL = "http://cdn2.iconfinder.com/data/icons/starwars/icons/128/clone-old.png";
	private int actualHeightConteiner = 75;
	private int actualX = 1;
	private int actualY = 10;
	private final int  XCol1 = 1;
	private final int  XCol2 = 270;
	
	public ItemsViewer(LendMeAsync lendme, String idSession) {
		
		creator = new LendMeItensCreatorRepresentation(lendme, defaultImageURL);
		myItems = new ArrayList<LendMeItensRepresentation>();
		
		this.rootPanel = new VerticalPanel();
		
		scrollPanel = new ScrollPanel();
		scrollPanel.setStyleName("gwt-DialogBox");
		scrollPanel.setSize("636px", "408px");
		
		conteinerPanel = new AbsolutePanel();
		conteinerPanel.setStyleName("dialogVPanel");
		conteinerPanel.setSize("601px", "300");
		
		scrollPanel.setWidget(this.conteinerPanel);
		
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
		
		Label errorLabel = new Label("Ocorreu um erro de comunicação com o servidor");
		errorLabel.setStyleName("alert");
		topBar.add(errorLabel, 12, 17);
		errorLabel.setVisible(false);

		/*Povoamento da lista de itens*/
		for(int i=0; i < 20; i++){
			this.myItems.add(new LendMeItensRepresentation(defaultImageURL,
															"Star Wars "+i,
															"Movie",
															"The best of sci-fi movie", true));
		}
		
		Iterator<LendMeItensRepresentation> myItemsIterator = this.myItems.iterator();
		int i = 0;
		
		while(myItemsIterator.hasNext()){
			
			if(i%2 != 0) {
				actualX = XCol2;
				actualY -=90;
			}
			else {
				actualX = XCol1;
			}
			
			this.conteinerPanel.add(myItemsIterator.next(),actualX,actualY);
			this.conteinerPanel.setHeight(Integer.toString((actualHeightConteiner +=45))+"px");
			actualY += 90;
			i++;
		}
		
		rootPanel.add(this.topBar);
		rootPanel.add(this.scrollPanel);
		initWidget(this.rootPanel);
	}

	public ItemViewer getViewer() {
		return viewer;
	}

	public void setViewer(ItemViewer viewer) {
		this.viewer = viewer;
	}
	
	
}
