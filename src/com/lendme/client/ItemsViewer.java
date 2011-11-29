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

	public ItemsViewer(LendMeAsync lendme, String idSession, Map<String, String[]> result) {

		this.lendmeLocal = lendme;
		this.idSessionLocal = idSession;
		
		creator = new LendMeItensCreatorRepresentation(lendmeLocal, idSessionLocal);
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

		String[] values;
		for(String actualKey : result.keySet()){
			values = result.get(actualKey);
			myItems.add(new LendMeItensRepresentation(lendmeLocal, idSessionLocal,defaultImageURL, values[0], values[1],values[2], values[7], values[8], values[9],Boolean.valueOf(values[11]),Boolean.valueOf(values[3]), Boolean.valueOf(values[5]), Boolean.valueOf(values[6])));
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
		rootPanel.add(scrollPanel);
		initWidget(rootPanel);

	}

}