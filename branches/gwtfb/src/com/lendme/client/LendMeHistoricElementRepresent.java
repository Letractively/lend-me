package com.lendme.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DirectionalTextHelper;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class LendMeHistoricElementRepresent extends Composite {
	
	private final int IMG_USER_PATH = 0;
	private final int IMG_ICON_PATH = 1;
	private final int DESCRIPTION = 2;
	private final int DATE = 3;
	private final int HOUR = 4;
	
	private String[] historicElementInfo = {"", "", "", "", ""};
	
	private AbsolutePanel historyElement; 
	private Label messagePlace;
	private Image icon;
	private Image userPhoto;
	private Label separator;
	private Label datePlace;
	private Label hourPlace;
	
	/**
	 * @wbp.parser.constructor
	 */
	public LendMeHistoricElementRepresent() {
		RootPanel rootPanel = RootPanel.get();
		
		historyElement = new AbsolutePanel();
		rootPanel.add(historyElement, 0, 0);
		historyElement.setSize("450px", "89px");
		
		messagePlace = new Label("Menssagem de historico");
		historyElement.add(messagePlace, 134, 32);
		messagePlace.setSize("306px", "44px");
		
		icon = new Image((String) null);
		historyElement.add(icon, 81, 10);
		icon.setSize("33px", "33px");
		
		userPhoto = new Image((String) null);
		historyElement.add(userPhoto, 10, 10);
		userPhoto.setSize("65px", "65px");
		
		separator = new Label("-----------------------------------------------------------------------------------------------------------");
		historyElement.add(separator, 14, 77);
		separator.setSize("426px", "12px");
		
		datePlace = new Label("Data: 00/00/0000");
		historyElement.add(datePlace, 134, 10);
		
		hourPlace = new Label("Hora: 24h 59min");
		historyElement.add(hourPlace, 256, 10);
		hourPlace.setSize("99px", "15px");
	}
	
	
	public LendMeHistoricElementRepresent(String imgUserPath, String imgIconPath, String description, String date, String hour) {
		this();
		
		messagePlace.setText(description);
		icon.setUrl(imgIconPath);
		userPhoto.setUrl(imgUserPath);
		datePlace.setText(date);
		hourPlace.setText(hour);
		
		historyElement.add(icon);
		historyElement.add(userPhoto);
		
	}
	
	public void setHistoricElementInfo(String imgUserPath, String imgIconPath, String description, String date, String hour){
		historicElementInfo[IMG_USER_PATH] = imgUserPath;
		historicElementInfo[IMG_ICON_PATH] = imgIconPath;
		historicElementInfo[DESCRIPTION] = description;
		historicElementInfo[DATE] = date;
		historicElementInfo[HOUR] = hour;
	}
	
	
	
}
