package com.lendme.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.lendme.client.LendMeEntryPoint.ItemSearchResultFound;
import com.lendme.client.LendMeEntryPoint.UserSearchResultFound;
import com.lendme.fbsdk.FBCore;

public class LeftOptionsSideBarPanel extends Composite {

	final ScrollPanel realRootPanel = new ScrollPanel();
	final LendMeAsync lendMeService;

	public LeftOptionsSideBarPanel(final LendMeAsync lendMeService, final String currentSessionId, 
			final String currentUserLogin, final String viewedLogin, final FBCore fbCore, UserSearchResultFound userSearchResult, ItemSearchResultFound itemSearchResult) {

		this.lendMeService = lendMeService;
		
		if (currentUserLogin == null || currentUserLogin.trim().isEmpty() ){
			initWidget(realRootPanel);
			return;
		}
		else{
			validate(currentSessionId, currentUserLogin, viewedLogin, fbCore, userSearchResult, itemSearchResult);
		}
	}
	
	private void validate(String currentSessionId, String currentUserLogin, String viewedLogin, FBCore fbCore, UserSearchResultFound userSearchResult, ItemSearchResultFound itemSearchResult){
		if ( !fbCore.userExists(currentUserLogin) ){
			initWidget(realRootPanel);
			return;
		}
		else if ( !fbCore.userExists(viewedLogin) ){
			viewedLogin = currentUserLogin;
		}
		
		renderBar(currentSessionId, currentUserLogin, viewedLogin, fbCore, userSearchResult, itemSearchResult);
	}


	
	private void renderBar(final String currentSessionId, final String currentUserLogin,
			final String viewedLogin, final FBCore fbCore, final UserSearchResultFound userSearchResultCallback, final ItemSearchResultFound itemSearchResultCallback) {
		/**
		 * User Profile
		 */

		realRootPanel.getElement().setId("SideBarPanel");
		realRootPanel.setWidth("500px");

		AbsolutePanel rootPanel = new AbsolutePanel();
		realRootPanel.setWidget(rootPanel);

		rootPanel.setSize("500px", "700px");

		AbsolutePanel userProfilePanel = new AbsolutePanel();
		rootPanel.add(userProfilePanel, 10, 10);
		userProfilePanel.setSize("500px", "100px");
		
		Image userPhoto = new Image("https://graph.facebook.com/"+viewedLogin+"/picture");
		userProfilePanel.add(userPhoto, 10, 10);
		userPhoto.setSize("80px", "80px");

		final Label userName = new Label();
		userName.setStyleName("gwt-UserNameFont");
		userProfilePanel.add(userName, 106, 10);

		class AttributeSetter implements AsyncCallback<String>{

			Label toBeSet;

			public AttributeSetter(Label toBeSet){
				this.toBeSet = toBeSet;
			}

			@Override
			public void onFailure(Throwable caught) {
//				Window.alert("Erro ao capturar atributo do usuario: "+caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				toBeSet.setText(result);
			}

		}

		lendMeService.getUserAttribute(viewedLogin, "nome", new AttributeSetter(userName));

		final Label userAddress = new Label();
		userAddress.setStyleName("gwt-SearchFont");
		userProfilePanel.add(userAddress, 106, 40);

		lendMeService.getUserAttribute(viewedLogin, "endereco", new AttributeSetter(userAddress));

		final Button addBreakFriendship = new Button("Action");
		addBreakFriendship.setText("Action");
		userProfilePanel.add(addBreakFriendship, 106, 66);
		addBreakFriendship.setSize("139px", "24px");
		addBreakFriendship.setVisible(false);

		class SendFriendshipRequestHandler implements ClickHandler{
			@Override
			public void onClick(ClickEvent event) {
				addBreakFriendship.setEnabled(false);
				lendMeService.askForFriendship(currentSessionId, viewedLogin, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Problema ocorreu enquanto tentava encerrar amizade: "+caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Solicitacao de amizade enviada!");
					}

				});
			}
		}

		class BreakFriendshipHandler implements ClickHandler{
			@Override
			public void onClick(ClickEvent event) {
				addBreakFriendship.setEnabled(false);
				lendMeService.breakFriendship(currentSessionId, viewedLogin, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Problema ocorreu enquanto tentava quebrar amizade com "+userName.getText()+": "+caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Amizade encerrada!");
						Window.Location.replace(ApplicationConstants.APP_URL);
						Window.Location.reload();
					}

				});
			}
		}
		
		/**
		 * User Info Search
		 */

		DockPanel searchDockPanel = new DockPanel();
		rootPanel.add(searchDockPanel, 10, 116);
		searchDockPanel.setSize("500px", "42px");

		HorizontalPanel searchTopHorizontalPanel = new HorizontalPanel();
		searchDockPanel.add(searchTopHorizontalPanel, DockPanel.NORTH);
		searchTopHorizontalPanel.setWidth("254px");

		Label searchLabel = new Label("Pesquisar:");
		searchTopHorizontalPanel.add(searchLabel);
		searchLabel.setWidth("69px");

		AbsolutePanel searchBottomAbsolutePanel = new AbsolutePanel();
		searchDockPanel.add(searchBottomAbsolutePanel, DockPanel.SOUTH);
		searchBottomAbsolutePanel.setWidth("500px");
		searchBottomAbsolutePanel.setHeight("47px");

		Button searchStart = new Button("OK");
		searchBottomAbsolutePanel.add(searchStart, 357, 9);
		searchStart.setSize("36px", "27px");

		final TextBox searchKey = new TextBox();
		searchBottomAbsolutePanel.add(searchKey, 105, 9);
		searchKey.setStyleName("gwt-SearchFont");
		searchKey.setText("");
		searchKey.setSize("240px", "22px");

		final ListBox searchStrategy = new ListBox();
		searchBottomAbsolutePanel.add(searchStrategy, 0, 9);
		searchStrategy.addItem("Nome");
		searchStrategy.addItem("Endereco");
		searchStrategy.setSize("98px", "28px");

		class FriendsSearchChosen implements ValueChangeHandler<Boolean>{

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				searchStrategy.clear();
				searchStrategy.addItem("Nome");
				searchStrategy.addItem("Endereco");
			}

		}

		class ItemsSearchChosen implements ValueChangeHandler<Boolean>{

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				searchStrategy.clear();
				searchStrategy.addItem("Nome");
				searchStrategy.addItem("ID");
				searchStrategy.addItem("Descricao");
				searchStrategy.addItem("Categoria");
			}

		}

		final RadioButton searchForFriends = new RadioButton("SearchOptions", "Amigos");
		searchForFriends.addValueChangeHandler(new FriendsSearchChosen());
		searchForFriends.setValue(true);
		searchTopHorizontalPanel.add(searchForFriends);

		final RadioButton searchForItems = new RadioButton("SearchOptions", "Items");
		searchForItems.addValueChangeHandler(new ItemsSearchChosen());
		searchForItems.setValue(false);
		searchTopHorizontalPanel.add(searchForItems);

		class StartSearch implements ClickHandler{

			@Override
			public void onClick(ClickEvent event) {
				Window.alert(searchKey.getText());
				if ( searchForFriends.getValue().booleanValue() ) {
					if (searchStrategy.getItemText(searchStrategy.getSelectedIndex()).equals("Endereco") ){
						lendMeService.searchUsersByAttributeKey(currentSessionId, searchKey.getText(), "endereco",
								userSearchResultCallback);
					}
					else if ( searchStrategy.getItemText(searchStrategy.getSelectedIndex()).equals("Nome") ){
						lendMeService.listUsersByDistance(currentSessionId, userSearchResultCallback);
					}
				}
				else if ( searchForItems.getValue().booleanValue() ){
					String attribute = searchStrategy.getItemText(searchStrategy.getSelectedIndex()).toLowerCase();
					lendMeService.searchForItems(currentSessionId, searchKey.getText(), attribute,
							"crescente", "reputacao", itemSearchResultCallback);
				}
			}

		}

		class EnterKeyHit implements KeyPressHandler{

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if ( event.getCharCode() == KeyCodes.KEY_ENTER ){
					if ( searchForFriends.getValue().booleanValue() ) {
						if (searchStrategy.getItemText(searchStrategy.getSelectedIndex()).equals("Endereco") ){
							lendMeService.searchUsersByAttributeKey(currentSessionId, searchKey.getText(), "endereco",
									userSearchResultCallback);
						}
						else if ( searchStrategy.getItemText(searchStrategy.getSelectedIndex()).equals("Endereco") ){
							lendMeService.listUsersByDistance(currentSessionId, userSearchResultCallback);
						}
					}
					else{
						String attribute = searchStrategy.getItemText(searchStrategy.getSelectedIndex()).toLowerCase();
						lendMeService.searchForItems(currentSessionId, searchKey.getText(), attribute,
								"crescente", "reputacao", itemSearchResultCallback);
					}
				}
			}

		}
		
		searchStart.addClickHandler(new StartSearch());
		searchKey.addKeyPressHandler(new EnterKeyHit());

		Label optionsLabel = new Label("Op\u00E7oes");
		optionsLabel.setStyleName("gwt-OptionsFont");
		rootPanel.add(optionsLabel, 9, 189);
		Hyperlink hyperlink = new Hyperlink( "Amigos", viewedLogin+"/options/friends");
		hyperlink.setStyleName("gwt-SearchFont");
		rootPanel.add(hyperlink, 11, 240);
		Hyperlink hyperlink_1 = new Hyperlink( "Items", viewedLogin+"/options/items");
		hyperlink_1.setStyleName("gwt-SearchFont");
		rootPanel.add(hyperlink_1, 11, 288);
		Hyperlink hyperlink_2 = new Hyperlink( "Mensagens", viewedLogin+"/options/messages");
		hyperlink_2.setStyleName("gwt-SearchFont");
		rootPanel.add(hyperlink_2, 11, 336);
		Hyperlink hyperlink_3 = new Hyperlink( "Historico", viewedLogin+"options/history");
		hyperlink_3.setStyleName("gwt-SearchFont");
		rootPanel.add(hyperlink_3, 11, 384);


		initWidget(realRootPanel);
		
		fbCore.api("/me", new AsyncCallback<JavaScriptObject>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Nao foi possivel obter informacoes do usuario no Facebook: "+caught.getMessage());
			}

			@Override
			public void onSuccess(JavaScriptObject result) {
				String id = ((JSOModel)result.cast()).get("id");
				if ( !id.equals(viewedLogin) ){
					Window.alert("Verificar se sao amigos:" +currentSessionId+" "+viewedLogin);
					lendMeService.areFriends(currentSessionId, viewedLogin, new AsyncCallback<Boolean>(){

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Nao foi possivel verificar existencia de amizade entre dois usuarios: "+caught.getMessage());
						}

						@Override
						public void onSuccess(Boolean result) {
							if ( !result.booleanValue() ){
								addBreakFriendship.setVisible(true);
								addBreakFriendship.setText("Adicionar amigo");
								addBreakFriendship.addClickHandler(new SendFriendshipRequestHandler());
							}
							else{
								addBreakFriendship.setVisible(true);
								addBreakFriendship.setText("Remover amigo");
								addBreakFriendship.addClickHandler(new BreakFriendshipHandler());								
							}
						}

					});
				}
			}

		});

	}
	
}
