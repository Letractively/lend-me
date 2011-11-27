package com.lendme.client;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Image;

public class ItemViewer extends PopupPanel {

	@SuppressWarnings("unused")
	private String imgURL;
	private String nameStr;
	private String descriptionStr;
	private String statusStr;
	private String infoStr;
	
	public ItemViewer(String imgURL, String nameStr, String descriptionStr, String statusStr, String infoStr) {
		
		super(true);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		
		this.imgURL = imgURL;
		this.nameStr = nameStr;
		this.descriptionStr = descriptionStr;
		this.statusStr = statusStr;
		this.infoStr = infoStr;
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		setWidget(absolutePanel);
		absolutePanel.setSize("350px", "223px");
		
		Label name = new Label("");
		absolutePanel.add(name, 10, 5);
		name.setSize("185px", "17px");
		
		Label lblNewLabel = new Label("Descrição:");
		absolutePanel.add(lblNewLabel, 114, 38);
		
		Label descricao = new Label("New label");
		absolutePanel.add(descricao, 188, 38);
		descricao.setSize("158px", "17px");
		
		Label lblNewLabel_2 = new Label("Status:");
		absolutePanel.add(lblNewLabel_2, 114, 60);
		lblNewLabel_2.setSize("226px", "17px");
		
		Label status = new Label("New label");
		absolutePanel.add(status, 165, 61);
		status.setSize("175px", "17px");
		
		Label lblNewLabel_4 = new Label("Info:");
		absolutePanel.add(lblNewLabel_4, 114, 82);
		lblNewLabel_4.setSize("226px", "17px");
		
		Label info = new Label("New label");
		absolutePanel.add(info, 149, 83);
		info.setSize("191px", "17px");
		
		Label lblNewLabel_6 = new Label("Usuarios Interessados:");
		absolutePanel.add(lblNewLabel_6, 10, 134);
		
		ListBox interestingUsers = new ListBox();
		absolutePanel.add(interestingUsers, 163, 130);
		interestingUsers.setSize("173px", "22px");
		
		Label lblNewLabel_7 = new Label("Ações:");
		absolutePanel.add(lblNewLabel_7, 10, 158);
		
		ListBox comboBox_1 = new ListBox();
		absolutePanel.add(comboBox_1, 58, 156);
		comboBox_1.setSize("200px", "22px");
		
		PushButton pshbtnNewButton = new PushButton("New button");
		pshbtnNewButton.setHTML("<center><b>Agir</b></center>");
		absolutePanel.add(pshbtnNewButton, 267, 155);
		pshbtnNewButton.setSize("56px", "15px");
		
		Image image = new Image(imgURL);
		absolutePanel.add(image, 10, 29);
		image.setSize("100px", "92px");
		
		Image flagE = new Image("");
		absolutePanel.add(flagE, 317, 5);
		flagE.setSize("23px", "22px");
		
		Image flagD = new Image("");
		absolutePanel.add(flagD, 288, 5);
		flagD.setSize("23px", "22px");
		
		Image flagC = new Image("");
		absolutePanel.add(flagC, 259, 5);
		flagC.setSize("23px", "22px");
		
		Image flagB = new Image("");
		absolutePanel.add(flagB, 230, 5);
		flagB.setSize("23px", "22px");
		
		Image flagA = new Image("");
		absolutePanel.add(flagA, 201, 5);
		flagA.setSize("23px", "22px");
		
		/**/
		name.setText(this.nameStr);
		descricao.setText(this.descriptionStr);
		status.setText(this.statusStr);
		info.setText(this.infoStr);
	}
}
