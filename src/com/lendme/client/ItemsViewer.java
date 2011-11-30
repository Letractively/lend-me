package com.lendme.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	private final String defaultImageURL = "http://cdn2.iconfinder.com/data/icons/starwars/icons/128/clone-old.png";
	private int actualHeightConteiner = 75;
	private int actualX = 1;
	private int actualY = 1;
	private final int  XCol1 = 1;
	private final int  XCol2 = 270;

	private final LendMeAsync lendmeLocal;
	private final String idSessionLocal;
	private Label label;

	public ItemsViewer(LendMeAsync lendme, String idSession, final String viewedLogin, Map<String, String[]> result) {

		this.lendmeLocal = lendme;
		this.idSessionLocal = idSession;
		
		creator = new LendMeItensCreatorRepresentation(lendmeLocal, idSessionLocal, viewedLogin);
		myItems = new ArrayList<LendMeItensRepresentation>();

		rootPanel = new VerticalPanel();
		scrollPanel = new ScrollPanel();
		scrollPanel.setStyleName("backGround2");
		scrollPanel.setSize("636px", "408px");

		conteinerPanel = new AbsolutePanel();
		conteinerPanel.setStyleName("dialogVPanel");
		conteinerPanel.setSize("601px", "300");

		scrollPanel.setWidget(conteinerPanel);

		topBar = new AbsolutePanel();
		topBar.setStyleName("backGround1");
		topBar.setSize("100%", "70px");

		PushButton pshbtnNewButton = new PushButton(" +1 ");
		pshbtnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				creator.center();
			}
		});
		topBar.add(pshbtnNewButton, 348, 22);
		pshbtnNewButton.setSize("18px", "13px");

		Label lblMyItems = new Label("Meus Itens");
		lblMyItems.setStyleName("defaultTitle");
		topBar.add(lblMyItems, 70, 22);

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
		
		label = new Label("");
		label.setStyleName("barra");
		topBar.add(label, 0, 60);
		label.setSize("700px", "10px");
		rootPanel.add(scrollPanel);
		initWidget(rootPanel);

	}
	
}