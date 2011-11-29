package com.lendme.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class LendMeItensRepresentation extends AbsolutePanel {

	private final Image image;
	private final Image exclama;
	private final Label name;
	private final Label category;
	private final Label description;
	private ItemViewer itemViewer;
		
	private final int MAX_SIZE = 20;
	private Label topSelect;
	private Label underSelect;
	private AbsolutePanel conteinerPanel;
	
	private final LendMeAsync lendmeLocal;
	private final String idSessionLocal;
	
	private boolean lent;
	private boolean requested;
	private boolean iAmOwnerLocal;
	private String itemIdLocal;
	private String lendingIdLocal;
	private String interestedsLocal;
	
	/**
	 * @wbp.parser.constructor
	 */
	private LendMeItensRepresentation(LendMeAsync lendme, String idSession) {
		super();
		
		this.lendmeLocal = lendme;
		this.idSessionLocal = idSession;
		
		AppImageBundle images = GWT.create(AppImageBundle.class);
		conteinerPanel = new AbsolutePanel();
		conteinerPanel.setStyleName("gwt-PopupPanel");
		super.add(conteinerPanel);
		conteinerPanel.setSize("250px", "74px");
		
		image = new Image();
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				itemViewer = new ItemViewer(lendmeLocal, idSessionLocal,image.getUrl(), name.getText(), description.getText(), "", category.getText(), itemIdLocal, lendingIdLocal, interestedsLocal,iAmOwnerLocal, lent, requested);
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
		
		name = new Label("");
		name.setStyleName("gwt-Label-item");
		conteinerPanel.add(name, 113, 10);
		name.setSize("127px", "14px");
		
		Label lblCategoria = new Label("Category:");
		lblCategoria.setStyleName("gwt-Label-item");
		conteinerPanel.add(lblCategoria, 71, 30);
		lblCategoria.setSize("37px", "14px");
		
		category = new Label("");
		category.setStyleName("gwt-Label-item");
		conteinerPanel.add(category, 134, 30);
		category.setSize("106px", "14px");
		
		Label lblNewLabel_3 = new Label("Description:");
		lblNewLabel_3.setStyleName("gwt-Label-item");
		conteinerPanel.add(lblNewLabel_3, 72, 50);
		
		description = new Label("");
		description.setStyleName("gwt-Label-item");
		conteinerPanel.add(description, 134, 50);
		description.setSize("106px", "14px");
		
		exclama = new Image(images.updateAction());
		conteinerPanel.add(exclama, 230, 10);
		exclama.setSize("29px", "29px");
		
		topSelect = new Label("--------------------------------------------------------------------------------------------");
		topSelect.setStyleName("gwt-bar");
		topSelect.setSize("275px", "17px");
		topSelect.setVisible(false);
		conteinerPanel.add(topSelect, 0, -4);
		
		underSelect = new Label("--------------------------------------------------------------------------------------------");
		underSelect.setStyleName("gwt-bar");
		underSelect.setSize("275px", "17px");
		underSelect.setVisible(false);
		
		conteinerPanel.add(underSelect, 0, 64);
	}
	
	public LendMeItensRepresentation(LendMeAsync lendme, String idSession, String imgURL ,String name, String category, String description, String itemId, String lendingID,String interesteds, boolean iAmOwner,boolean action, boolean lent, boolean requested){
		this(lendme, idSession);
		
		this.lent = lent;
		this.requested = requested;
		this.itemIdLocal = itemId;
		this.lendingIdLocal = lendingID;
		this.interestedsLocal = interesteds;
		this.iAmOwnerLocal = iAmOwner;
				
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

