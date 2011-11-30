package com.lendme.client;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class LendMeItemRep extends AbsolutePanel {

	private final int MAX_SIZE = 20;
	private Label topSelect;
	private Label underSelect;
	private AbsolutePanel conteinerPanel;
	
	private String URLExclamation = "http://ricardolombardi.ig.com.br/wp-content/uploads/2008/05/2009/04/exclamation-001.jpg";
	
	/**
	 * @wbp.parser.constructor
	 */
	public LendMeItemRep(final LendMeAsync lendMeService, final String sessionId, final String viewedLogin, final String imgURL, final String[] itemInfo){

		conteinerPanel = new AbsolutePanel();
		conteinerPanel.setStyleName("gwt-PopupPanel");
		super.add(conteinerPanel);
		conteinerPanel.setSize("270px", "74px");
		
		final Image image = new Image();
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				LendMeItemPopup itemViewer = new LendMeItemPopup(lendMeService, sessionId, viewedLogin, image.getUrl(), itemInfo);
				itemViewer.center();
			}
		});
		image.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				conteinerPanel.setStyleName("gwt-PopupPanel");
				topSelect.setVisible(false);
				underSelect.setVisible(false);
			}
		});
		image.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				conteinerPanel.setStyleName("gwt-PopupPanel-modif");
				topSelect.setVisible(true);
				underSelect.setVisible(true);
			}
		});
		conteinerPanel.add(image, 10, 10);
		image.setSize("57px", "54px");
		
		Label lblNewLabel = new Label("Name:");
		lblNewLabel.setStyleName("gwt-Label-item");
		conteinerPanel.add(lblNewLabel, 71, 10);
		
		Label name = new Label("");
		name.setStyleName("gwt-Label-item");
		conteinerPanel.add(name, 113, 10);
		name.setSize("118px", "14px");
		
		Label lblCategoria = new Label("Category:");
		lblCategoria.setStyleName("gwt-Label-item");
		conteinerPanel.add(lblCategoria, 71, 30);
		lblCategoria.setSize("37px", "14px");
		
		Label category = new Label("");
		category.setStyleName("gwt-Label-item");
		conteinerPanel.add(category, 134, 30);
		category.setSize("106px", "14px");
		
		Label lblNewLabel_3 = new Label("Description:");
		lblNewLabel_3.setStyleName("gwt-Label-item");
		conteinerPanel.add(lblNewLabel_3, 72, 50);
		
		Label description = new Label("");
		description.setStyleName("gwt-Label-item");
		conteinerPanel.add(description, 134, 50);
		description.setSize("106px", "14px");
		
		topSelect = new Label("");
		topSelect.setStyleName("gwt-bar");
		topSelect.setSize("275px", "17px");
		topSelect.setVisible(false);
		conteinerPanel.add(topSelect, 0, -4);
		
		underSelect = new Label("");
		underSelect.setStyleName("gwt-bar");
		underSelect.setSize("275px", "17px");
		underSelect.setVisible(false);
		
		conteinerPanel.add(underSelect, 0, 64);
		
		Image exclama = new Image();
		exclama.setUrl(URLExclamation);
		conteinerPanel.add(exclama, 239, 10);
		exclama.setSize("29px", "29px");
		
		setStyleName("gwt-Label-item");
		boolean warnEvent = false;
		if ( new Boolean(itemInfo[7]).booleanValue() && ( itemInfo[8].equals("REQUESTED") || itemInfo[8].equals("RETURNED")  ) ){
			warnEvent = true;
		}
		else if ( !(new Boolean(itemInfo[7]).booleanValue()) && ( itemInfo[8].equals("LENT") ) ){
			warnEvent = true;
		}
		exclama.setVisible(warnEvent);

		if (itemInfo[0].length() < MAX_SIZE){
			name.setText(itemInfo[0]);
		}
		else{
			name.setText(itemInfo[0].substring(0, MAX_SIZE-3)+"...");
		}
		
		if ( itemInfo[1].length() < MAX_SIZE ){
			category.setText(itemInfo[1]);
		}
		else {
			category.setText(itemInfo[1].substring(0, MAX_SIZE-3)+"...");
		}
		
		if ( itemInfo[2].length() < MAX_SIZE){
			description.setText(itemInfo[2]);
		}
		else{
			description.setText(itemInfo[2].substring(0, MAX_SIZE-3)+"...");
		}
		
		image.setUrl(imgURL);
		
	}
}

