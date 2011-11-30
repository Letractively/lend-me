package com.lendme.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
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

	private final String defaultImageURL = "http://cdn2.iconfinder.com/data/icons/starwars/icons/128/clone-old.png";
	private int actualHeightConteiner = 75;
	private int actualX = 1;
	private int actualY = 1;
	private final int  XCol1 = 1;
	private final int  XCol2 = 270;

	private final LendMeAsync lendmeLocal;
	private final String idSessionLocal;

	public ItemsViewer(LendMeAsync lendme, String idSession, final String viewedLogin, Map<String, String[]> result) {

		this.lendmeLocal = lendme;
		this.idSessionLocal = idSession;
		
		creator = new LendMeItensCreatorRepresentation(lendmeLocal, idSessionLocal, viewedLogin);
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
		topBar.setSize("100%", "82px");

		PushButton createItem = new PushButton("");
		createItem.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				creator.center();
			}
		});
		createItem.getUpFace().setImage(new Image("http://www.777icons.com/libs/basic-vista/add-icon.gif"));
		topBar.add(createItem, 543, 10);
		createItem.setSize("58px", "54px");

		Label lblMyItems = new Label("ITEMS");
		lblMyItems.setStyleName("gwt-OptionsFont");
		topBar.add(lblMyItems, 10, 2);

		for(String actualKey : result.keySet()){
			myItems.add(new LendMeItensRepresentation(lendmeLocal, idSessionLocal, viewedLogin, defaultImageURL, result.get(actualKey)));
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

		rootPanel.add(topBar);
		
		Label lblNewLabel = new Label("Adicione um item!");
		topBar.add(lblNewLabel, 426, 54);
		
		PushButton refresh = new PushButton("");
		topBar.add(refresh, 350, 10);
		refresh.setSize("48px", "48px");
		refresh.getUpFace().setImage(new Image("http://icons.iconarchive.com/icons/deleket/button/48/Button-Refresh-icon.png"));
		refresh.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				LeftOptionsSideBarPanel.redoItemQuery();
			}
			
		});
		rootPanel.add(scrollPanel);
		initWidget(rootPanel);

	}
}