package com.lendme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class LendMeItensRepresentation extends AbsolutePanel {

	private final Image image;
	private final Image exclama;
	private final Label name;
	private final Label category;
	private final Label description;
	
	
	private final int MAX_SIZE = 20;
	
	/**
	 * @wbp.parser.constructor
	 */
	public LendMeItensRepresentation() {
		super();
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setStyleName("gwt-PopupPanel");
		super.add(absolutePanel);
		absolutePanel.setSize("250px", "74px");
		
		image = new Image();
		absolutePanel.add(image, 10, 10);
		image.setSize("57px", "54px");
		
		Label lblNewLabel = new Label("Name:");
		lblNewLabel.setStyleName("gwt-Label-item");
		absolutePanel.add(lblNewLabel, 71, 10);
		
		name = new Label("");
		name.setStyleName("gwt-Label-item");
		absolutePanel.add(name, 113, 10);
		name.setSize("127px", "14px");
		
		Label lblCategoria = new Label("Category:");
		lblCategoria.setStyleName("gwt-Label-item");
		absolutePanel.add(lblCategoria, 71, 30);
		lblCategoria.setSize("37px", "14px");
		
		category = new Label("");
		category.setStyleName("gwt-Label-item");
		absolutePanel.add(category, 134, 30);
		category.setSize("106px", "14px");
		
		Label lblNewLabel_3 = new Label("Description:");
		lblNewLabel_3.setStyleName("gwt-Label-item");
		absolutePanel.add(lblNewLabel_3, 72, 50);
		
		description = new Label("");
		description.setStyleName("gwt-Label-item");
		absolutePanel.add(description, 134, 50);
		description.setSize("106px", "14px");
		
	    AppImageBundle images = GWT.create( AppImageBundle.class);
		exclama = new Image ( images.updateAction() );
		absolutePanel.add(exclama, 230, 10);
		exclama.setSize("29px", "29px");
		exclama.setVisible(false);
		
	}
	
	public LendMeItensRepresentation(String imgURL ,String name, String category, String description, boolean action){
		this();
		setStyleName("gwt-Label-item");
		exclama.setVisible(action);
		/*name*/
		if(name.length() < MAX_SIZE) this.name.setText(name);
		else this.name.setText(name.substring(0, MAX_SIZE-3)+"...");
		
		/*category*/
		if(category.length() < MAX_SIZE) this.category.setText(category);
		else this.category.setText(category.substring(0, MAX_SIZE-3)+"...");
		
		/*description*/
		if(description.length() < MAX_SIZE) this.description.setText(description);
		else this.description.setText(description.substring(0, MAX_SIZE-3)+"...");
		
		image.setUrl(imgURL);
		
		
	}
}

