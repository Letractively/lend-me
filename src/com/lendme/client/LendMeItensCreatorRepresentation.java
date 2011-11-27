package com.lendme.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;

public class LendMeItensCreatorRepresentation extends PopupPanel {

	private final String idSession;
	private final LendMeAsync lendmeAsync;
	private LendMeItensCreatorRepresentation i_am;
	private String defaultURL = "http://www.quatrocantos.com/clipart/ferramentas/imagens/ferramenta01/martelo.gif";
	
	public LendMeItensCreatorRepresentation(LendMeAsync lendme, String session) {
		super(true);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		this.lendmeAsync = lendme;
		this.idSession = session;
		i_am = this;
		
		setSize("291px", "211px");
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		setWidget(absolutePanel);
		absolutePanel.setSize("279px", "213px");
		
		Label lblNewLabel = new Label("New Item");
		lblNewLabel.setStyleName("gwt-Label-1");
		absolutePanel.add(lblNewLabel, 7, -1);
		
		Label lblNewLabel_1 = new Label("Name:");
		absolutePanel.add(lblNewLabel_1, 114, 16);
		
		final TextBox name = new TextBox();
		absolutePanel.add(name, 114, 33);
		name.setSize("146px", "13px");
		
		Label lblNewLabel_2 = new Label("Description:");
		absolutePanel.add(lblNewLabel_2, 114, 62);
		
		final TextBox description = new TextBox();
		absolutePanel.add(description, 114, 79);
		description.setSize("146px", "13px");
		
		Image image = new Image(defaultURL);
		absolutePanel.add(image, 7, 30);
		image.setSize("101px", "106px");
		
		Label lblNewLabel_3 = new Label("Category:");
		absolutePanel.add(lblNewLabel_3, 114, 104);
		
		final TextBox category = new TextBox();
		absolutePanel.add(category, 114, 122);
		category.setSize("146px", "13px");
		
		FileUpload fileUpload = new FileUpload();
		fileUpload.setStyleName("gwt-TextBox");
		absolutePanel.add(fileUpload, 10, 152);
		fileUpload.setSize("111px", "23px");
		
		PushButton pshbtnNewButton = new PushButton("Create");
		pshbtnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				lendmeAsync.registerItem(idSession,
										 name.getText(),
										 description.getText(),
										 category.getText(),
					new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						Window.alert("Item register with sucess!");
						i_am.hide();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Fail: "+caught.getMessage());
					}
				});
			}
		});
		pshbtnNewButton.setHTML("<center>Create</center>");
		absolutePanel.add(pshbtnNewButton, 111, 182);
		pshbtnNewButton.setSize("49px", "17px");
	}
}
