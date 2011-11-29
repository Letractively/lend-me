package com.lendme.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class LendMeUsersRepresentation extends PopupPanel {

	private final int IMG_PATH = 0;
	private final int LOGIN = 1;
	private final int NAME = 2;
	private final int ADDRESS = 3; 
	private final int REPUTATION = 4;
	
	
	private final String urlDefaultImg = "";
	
	private String[] userInfo = {"", "", "", "", "", ""};
	private Image image;
	private final Label loginLabel;
	private final Label nameLabel;
	private final Label addressLabel;
	private final Label reputationLabel;
	private final AbsolutePanel absolutePanel;
	
	public LendMeUsersRepresentation() {
		super(true);
		absolutePanel = new AbsolutePanel();
		
		setWidget(absolutePanel);
		absolutePanel.setSize("242px", "117px");
		
		final FocusPanel focusPanel = new FocusPanel();
		AbsolutePanel internalPanel = new AbsolutePanel();
		focusPanel.add(internalPanel);
		
		loginLabel = new Label("login");
		internalPanel.add(loginLabel, 106, 15);
		loginLabel.setSize("126px", "17px");
		
		nameLabel = new Label("name");
		internalPanel.add(nameLabel, 106, 38);
		nameLabel.setSize("126px", "17px");
		
		addressLabel = new Label("address");
		internalPanel.add(addressLabel, 106, 61);
		addressLabel.setSize("126px", "17px");
		
		reputationLabel = new Label("reputation");
		internalPanel.add(reputationLabel, 105, 84);
		reputationLabel.setSize("127px", "17px");
		
		/*Isso se chama popularmente como a boa e velha gambiarra*/
		final Label topBar = new Label("----------------------------------------------------------");
		topBar.setStyleName("gwt-bar");
		internalPanel.add(topBar, 0, -6);
		topBar.setSize("240px", "11px");
		topBar.setVisible(false);
		
		final Label downBar = new Label("----------------------------------------------------------");
		downBar.setStyleName("gwt-bar");
		internalPanel.add(downBar, 0, 103);
		downBar.setSize("240px", "11px");
		downBar.setVisible(false);

		focusPanel.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				topBar.setVisible(false);
				downBar.setVisible(false);
			}
		});
		focusPanel.setStyleName("alert");
		focusPanel.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				topBar.setVisible(true);
				downBar.setVisible(true);
			}
		});
		
		absolutePanel.add(focusPanel, 5, 1);
		
		internalPanel.setSize("230px", "113px");
		focusPanel.setSize("230px", "113px");
		
	}

	public LendMeUsersRepresentation(String imgPath, final String login, String name,
			String address, String reputation) {
		this();

		setUserInfo(imgPath, login, name, address, reputation);

		if (userInfo[LOGIN].length() >= 21)
			loginLabel.setText(userInfo[LOGIN].substring(0, 16) + "...");
		else
			loginLabel.setText(userInfo[LOGIN]);

		if (userInfo[NAME].length() >= 21)
			nameLabel.setText(userInfo[NAME].substring(0, 16) + "...");
		else
			nameLabel.setText(userInfo[NAME]);

		if (userInfo[ADDRESS].length() >= 21)
			addressLabel.setText(userInfo[ADDRESS].substring(0, 16) + "...");
		else
			addressLabel.setText(userInfo[ADDRESS]);

		if (userInfo[REPUTATION].length() >= 13)
			reputationLabel.setText("points: "
					+ userInfo[REPUTATION].substring(0, 10) + "...");
		else
			reputationLabel.setText("points: " + userInfo[REPUTATION]);

		if (imgPath == null || imgPath.equals(""))
			imgPath = this.urlDefaultImg;

		image = new Image(imgPath);
		image.setStyleName(".gwtPointerCursor");
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		image.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				setStyleName("gwt-PopupPanel");
			}
		});
		absolutePanel.add(image, 10, 13);
		image.setSize("80px", "91px");
		Hyperlink redirect = new Hyperlink("", login);
		redirect.setSize("80px", "91px");
		absolutePanel.add(redirect, 10, 13);

	}
	
	public void setUserInfo(String imgPath, String login, String name, String address, String reputation){
		
		userInfo[IMG_PATH] = imgPath;
		userInfo[LOGIN] = login;
		userInfo[NAME] = name;
		userInfo[ADDRESS] = address;
		userInfo[REPUTATION] = reputation;
	}
	
	public String getImgPath(){
		return userInfo[IMG_PATH];
	}
	
	public String getLogin(){
		return userInfo[LOGIN];
	}
	
	public String getName(){
		return userInfo[NAME];
	}
	
	public String getAddress(){
		return userInfo[ADDRESS];
	}
	
	public String getReputation(){
		return userInfo[REPUTATION];
	}
	
	public String[] getAllInfo(){
		return userInfo;
	}
}
