package com.lendme.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.lendme.client.UserViewer.FriendshipStatus;

public class LendMeUsersContainer extends AbsolutePanel {

	private LendMeUsersRepresentation user;
	private LendMeAsync lendMeService;
	private String solicitorSessionId;
	
	private Button addBreakFriendshipButton;
	private Button acceptFriendshipButton;
	private Button declineFriendshipButton;
	
	public LendMeUsersContainer(LendMeAsync lendMeService, String solicitorSessionId,
			LendMeUsersRepresentation user, FriendshipStatus status) {
		
		this.user = user;
		this.solicitorSessionId = solicitorSessionId;
		this.lendMeService = lendMeService;
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("253px", "160px");
		
		absolutePanel.add(user, 0, 0);
		
		if (status.equals(FriendshipStatus.FRIEND)) {
			
			addBreakFriendshipButton = new Button("Desfazer Amizade");
			absolutePanel.add(addBreakFriendshipButton, 55, 134);
			addBreakFriendshipButton.setSize("150px", "24px");
			addBreakFriendshipButton.addStyleName("purple-font");
			
			addBreakFriendshipButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					breakFriendship();
				}
			});
		
		} else if (status.equals(FriendshipStatus.REQUESTED_MY_FRIENDSHIP)){
			
			acceptFriendshipButton = new Button("Aprovar Amizade");
			acceptFriendshipButton.setText("Aprov. Amizade");
			absolutePanel.add(acceptFriendshipButton, 5, 134);
			acceptFriendshipButton.setSize("130px", "24px");
			acceptFriendshipButton.addStyleName("green-font");
			
			acceptFriendshipButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					acceptFriendship();
				}
			});
			
			declineFriendshipButton = new Button("Rejeitar Amizade");
			absolutePanel.add(declineFriendshipButton, 140, 134);
			declineFriendshipButton.setText("Rej. Amizade");
			declineFriendshipButton.setSize("110px", "24px");
			declineFriendshipButton.addStyleName("green-font");
			
			declineFriendshipButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					declineFriendship();
					
				}
			});
		}
		
		else {
			// NOT_FRIEND
			addBreakFriendshipButton = new Button("Requisitar Amizade");
			absolutePanel.add(addBreakFriendshipButton, 55, 134);
			addBreakFriendshipButton.setSize("150px", "24px");
			addBreakFriendshipButton.addStyleName("purple-font");
			
			addBreakFriendshipButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					askForFriendship();
				}
			});
		}
		
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.add(absolutePanel);
		
		this.add(decPanel);
	}
	
	private void askForFriendship() {
		lendMeService.askForFriendship(solicitorSessionId, 
				user.getLogin(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Nao foi possivel requisitar a amizade: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Requisicao de amizade feita com sucesso!");
				
			}
		});
	}
	
	private void breakFriendship() {
		
		lendMeService.breakFriendship(solicitorSessionId, 
				user.getLogin(), new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				Window.alert("Amizade desfeita com sucesso!");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Nao foi possivel desfazer a amizade: " + caught.getMessage());
			}
		});
	}
	
	private void acceptFriendship() {
		
		lendMeService.acceptFriendship(solicitorSessionId, 
				user.getLogin(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("O pedido de amizade nao pode ser aprovado: " + caught.getMessage());
						
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("O pedido de amizade foi aprovado!");
					}
				
				});
	}
	
	private void declineFriendship() {
		
		lendMeService.declineFriendship(solicitorSessionId, 
				user.getLogin(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("O pedido de amizade nao pode ser rejeitado: " + caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("O pedido de amizade foi rejeitado!");
						
					}
		});
	}
}
