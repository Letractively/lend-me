package com.lendme.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DirectionalTextHelper;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class LendMeHistoricElementRepresent extends Composite {
	
	private final int IMG_ICON_PATH = 0;
	private final int DESCRIPTION = 1;
	private final int TIME = 2;
	
	final String ICON_ADD_USER = "";
	final String ICON_NEW_ITEM = "";
	final String ICON_LEND = "";
	final String ICON_ITEM_INTEREST = "";
	final String ICON_FINISH_LENDING = "";
	final String ICON_NEED_ITEM = "";
	
	
	private String[] historicElementInfo = {"", "", ""};
	
	private AbsolutePanel historyElement; 
	private Label messagePlace;
	private Image icon;
	private Label separator;
	private Label timePlace;
	
	/**
	 * @wbp.parser.constructor
	 */
	public LendMeHistoricElementRepresent() {
		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("600", "80");
		
		historyElement = new AbsolutePanel();
		rootPanel.add(historyElement, 0, 0);
		historyElement.setSize("450px", "79px");
		
		messagePlace = new Label("Menssagem de historico");
		historyElement.add(messagePlace, 116, 31);
		messagePlace.setSize("306px", "33px");
		
		icon = new Image((String) null);
		historyElement.add(icon, 31, 10);
		icon.setSize("33px", "33px");
		
		separator = new Label("-----------------------------------------------------------------------------------------------------------");
		historyElement.add(separator, 10, 67);
		separator.setSize("426px", "12px");
		
		timePlace = new Label("Data: 00/00/0000");
		historyElement.add(timePlace, 116, 10);
		timePlace.setSize("269px", "15px");
	}
	
	
	public LendMeHistoricElementRepresent(String description, String time) {
		this();
		
		if(description.contains(" são amigos agora")){
			//TODO
			//icon.setPixelSize(width, height);
			icon.setUrl(this.ICON_ADD_USER);
			
		}else if(description.contains(" cadastrou ")){
			//TODO
			//icon.setPixelSize(width, height);
			icon.setUrl(this.ICON_NEW_ITEM);
			
		}else if(description.contains(" emprestou ")){
			//TODO
			//icon.setPixelSize(width, height);
			icon.setUrl(this.ICON_LEND);
			
		}else if(description.contains(" tem interesse pelo item ")){
			//TODO
			//icon.setPixelSize(width, height);
			icon.setUrl(this.ICON_NEED_ITEM);
			
		}else if(description.contains(" confirmou o término do empréstimo do item ")){
			//TODO
			//icon.setPixelSize(width, height);
			icon.setUrl(this.ICON_FINISH_LENDING);
			
		}else if(description.contains(" precisa do item ")){
			//TODO
			//icon.setPixelSize(width, height);
			icon.setUrl(this.ICON_NEED_ITEM);
		}
		
		messagePlace.setText(description);
		timePlace.setText(time);
		historyElement.add(icon);
		
	}
	
	public void setHistoricElementInfo(String imgIconPath, String description, String time){
		historicElementInfo[IMG_ICON_PATH] = imgIconPath;
		historicElementInfo[DESCRIPTION] = description;
		historicElementInfo[TIME] = time;
	}
	
	
	
}
