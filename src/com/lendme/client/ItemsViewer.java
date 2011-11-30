package com.lendme.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lendme.fbsdk.FBCore;

public class ItemsViewer extends Composite{

	private AbsolutePanel conteinerPanel;
	private ScrollPanel scrollPanel;
	private AbsolutePanel topBar;
	private VerticalPanel rootPanel;
	private LendMeItemCreator creator;
	private List<LendMeItemRep> myItems;

	private final String defaultImageURL = "http://cdn2.iconfinder.com/data/icons/starwars/icons/128/clone-old.png";
	private int actualHeightConteiner = 75;
	private int actualX = 1;
	private int actualY = 1;
	private final int  XCol1 = 1;
	private final int  XCol2 = 270;

	private final LendMeAsync lendmeLocal;
	private final String idSessionLocal;

	public ItemsViewer(LendMeAsync lendme, String idSession, final String viewedLogin, Map<String, String[]> result, FBCore fbCore) {

		this.lendmeLocal = lendme;
		this.idSessionLocal = idSession;
		
		creator = new LendMeItemCreator(lendmeLocal, idSessionLocal, viewedLogin);
		myItems = new ArrayList<LendMeItemRep>();

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

		final PushButton createItem = new PushButton("");
		createItem.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				creator.center();
			}
		});
		Image plus = new Image("http://icons.iconarchive.com/icons/pixelmixer/basic/64/plus-icon.png");
		plus.setSize("48px", "48px");
		createItem.getUpFace().setImage(plus);
		topBar.add(createItem, 543, 10);
		createItem.setSize("48px", "48px");
		
		createItem.setVisible(false);
		
		fbCore.api("/me", new AsyncCallback<JavaScriptObject>(){

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(JavaScriptObject result) {
				String viewingUser = ((JSOModel)result.cast()).get("id");
				if ( viewingUser.equals(viewedLogin) ){
					createItem.setVisible(true);
				}
				else{
					createItem.setVisible(false);
				}
			}
		});
		
		Label lblMyItems = new Label("Itens");
		lblMyItems.setStyleName("gwt-OptionsFont");
		topBar.add(lblMyItems, 10, 2);

		for(String actualKey : result.keySet()){
			myItems.add(new LendMeItemRep(lendmeLocal, idSessionLocal, viewedLogin, defaultImageURL, result.get(actualKey)));
		}
		Iterator<LendMeItemRep> myItemsIterator = myItems.iterator();
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