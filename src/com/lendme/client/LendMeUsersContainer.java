package com.lendme.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;

public class LendMeUsersContainer extends AbsolutePanel {

	private LendMeUsersRepresentation user;
	private LendMeAsync lendMeService;
	private String solicitorSessionId;
	
	private Button breakFriendshipButton;
	private Button acceptFriendshipButton;
	private Button declineFriendshipButton;
	
	public LendMeUsersContainer(LendMeAsync lendMeService, String solicitorSessionId,
			LendMeUsersRepresentation user, boolean viewCurrentFriends) {
		
		this.user = user;
		this.solicitorSessionId = solicitorSessionId;
		this.lendMeService = lendMeService;
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("253px", "160px");
		
		absolutePanel.add(user, 0, 0);
		
		if (viewCurrentFriends) {
			
			breakFriendshipButton = new Button("Desfazer Amizade");
			absolutePanel.add(breakFriendshipButton, 55, 134);
			breakFriendshipButton.setSize("150px", "24px");
			breakFriendshipButton.addStyleName("purple-font");
			
			breakFriendshipButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					breakFriendship();
				}
			});
		
		} else {
			
			acceptFriendshipButton = new Button("Aprovar Amizade");
			acceptFriendshipButton.setText("Aprovar");
			absolutePanel.add(acceptFriendshipButton, 30, 134);
			acceptFriendshipButton.setSize("80px", "24px");
			acceptFriendshipButton.addStyleName("green-font");
			
			acceptFriendshipButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					acceptFriendship();
				}
			});
			
			declineFriendshipButton = new Button("Rejeitar Amizade");
			absolutePanel.add(declineFriendshipButton, 145, 134);
			declineFriendshipButton.setText("Rejeitar");
			declineFriendshipButton.setSize("80px", "24px");
			declineFriendshipButton.addStyleName("purple-font");
			
			declineFriendshipButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					declineFriendship();
					
				}
			});
		}
		
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.add(absolutePanel);
		
		this.add(decPanel);
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
