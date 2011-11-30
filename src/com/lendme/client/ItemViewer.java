package com.lendme.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;

public class ItemViewer extends PopupPanel {

	public ItemViewer(final LendMeAsync lendMeService, final String sessionId,
			final String viewedLogin, String imgURL, final ItemInfo itemInfo) {

		super(true);
		setAnimationEnabled(true);
		setGlassEnabled(true);

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
		absolutePanel.add(lblNewLabel_6, 8, 134);

		final ListBox interestedUserLendingIDsListBox = new ListBox();
		absolutePanel.add(interestedUserLendingIDsListBox, 169, 132);
		interestedUserLendingIDsListBox.setSize("173px", "22px");

		Label lblNewLabel_7 = new Label("Ações:");
		absolutePanel.add(lblNewLabel_7, 7, 191);

		final ListBox actions = new ListBox();
		absolutePanel.add(actions, 64, 189);
		actions.setSize("200px", "22px");

		final String[] interestedUserLendingIDs = new String[itemInfo.getInterested().length];
		int i = 0;

		for ( String actualUserName : itemInfo.getInterested() ) {
			interestedUserLendingIDsListBox.addItem(actualUserName.split(":")[1]);
			interestedUserLendingIDs[i] = actualUserName.split(":")[0];
			i++;
		}

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

		name.setText(itemInfo.getItemName());
		descricao.setText(itemInfo.getDescription());
		status.setText(itemInfo.getState().toString());
		info.setText(itemInfo.getCategory());

		final IntegerBox daysAmount = new IntegerBox();
		daysAmount.setEnabled(false);
		absolutePanel.add(daysAmount, 96, 160);
		daysAmount.setSize("42px", "17px");

		Label lblNewLabel_1 = new Label("Qtde. dias:");
		absolutePanel.add(lblNewLabel_1, 8, 160);

		if ( itemInfo.belongsToMe() ){
			if ( itemInfo.getState() == ItemState.LENT ){
				actions.addItem("pedir de volta");
			}
			else if ( itemInfo.getState() == ItemState.ASKED_FOR_RETURN ){
				actions.addItem("pedir de volta");
				actions.setEnabled(false);
			}
			else if ( itemInfo.getState() == ItemState.AVAILABLE ){
				actions.addItem("deletar");
			}
			else if ( itemInfo.getState() == ItemState.REQUESTED ){
				actions.addItem("emprestar");
				actions.addItem("nao emprestar");
			}
			else if ( itemInfo.getState() == ItemState.RETURNED ){
				actions.addItem("confirmar devolucao");
				actions.addItem("negar que houve devolucao");
			}
		}
		else{
			if ( itemInfo.getState() == ItemState.UNAVAILABLE ){
				actions.addItem("tenho interesse nesse item");
			}
			else if ( itemInfo.getState() == ItemState.INTERESTED ){
				actions.addItem("tenho interesse nesse item");
				actions.setEnabled(false);
			}
			else if ( itemInfo.getState() == ItemState.LENT ){
				actions.addItem("devolver");
			}
			else if ( itemInfo.getState() == ItemState.RETURNED ){
				actions.addItem("devolver");
				actions.setEnabled(false);
			}
			else if ( itemInfo.getState() == ItemState.AVAILABLE ){
				actions.addItem("pedir emprestado");
			}
			else if ( itemInfo.getState() == ItemState.REQUESTED ){
				actions.addItem("pedir emprestado");
				actions.setEnabled(false);
			}
		}

		PushButton agir = new PushButton("");

		agir.setHTML("<center><b>Agir</b></center>");
		absolutePanel.add(agir, 274, 188);
		agir.setSize("56px", "15px");

		agir.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if ( actions.getItemText(actions.getSelectedIndex()).equals("deletar") ) {

					boolean confirm = Window.confirm("Tem certeza que deseja apagar o item ?");

					if(confirm){
						lendMeService.deleteItem(sessionId, itemInfo.getItemId(), new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								Window.alert("Item "+itemInfo.getItemName()+" removido com sucesso!");
								hide();
								LeftOptionsSideBarPanel.redoQuery();
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
							}
						});
					}
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("pedir de volta") ) {

					lendMeService.returnItem(sessionId, itemInfo.getLendingId(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Seu pedido de retorno do item "+itemInfo.getItemName()+" foi encaminhado.");
							hide();
							LeftOptionsSideBarPanel.redoQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("emprestar") ) {

					lendMeService.approveLending(sessionId, interestedUserLendingIDs[interestedUserLendingIDsListBox.getSelectedIndex()], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce emprestou o item "+itemInfo.getItemName()+"!");
							hide();
							LeftOptionsSideBarPanel.redoQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("nao emprestar") ) {

					lendMeService.denyLending(sessionId, interestedUserLendingIDs[interestedUserLendingIDsListBox.getSelectedIndex()], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce negou emprestimo do item "+itemInfo.getItemName()+"!");
							hide();
							LeftOptionsSideBarPanel.redoQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("confirmar devolucao") ) {

					lendMeService.confirmLendingTermination(sessionId, itemInfo.getLendingId(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce confirmou que houve devolucao do item "+itemInfo.getItemName()+"!");
							hide();
							LeftOptionsSideBarPanel.redoQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("negar que houve devolucao") ) {

					lendMeService.denyLendingTermination(sessionId, itemInfo.getLendingId(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce negou que houve devolucao do item "+itemInfo.getItemName()+"!");
							hide();
							LeftOptionsSideBarPanel.redoQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("devolver") ) {

					lendMeService.returnItem(sessionId, itemInfo.getLendingId(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce devolveu o item "+itemInfo.getItemName()+"! Aguarde confirmacao do dono.");
							hide();
							LeftOptionsSideBarPanel.redoQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("tenho interesse nesse item") ) {

					lendMeService.registerInterestForItem(sessionId, itemInfo.getItemId(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(Void result) {
							Window.alert("Voce vai ser alertado da disponibilidade do item "+itemInfo.getItemName()+"!");
							hide();
							LeftOptionsSideBarPanel.redoQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("pedir emprestado") ) {

					lendMeService.requestItem(sessionId, itemInfo.getItemId(), daysAmount.getValue(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce pediu o item "+itemInfo.getItemName()+" emprestado!");
							hide();
							LeftOptionsSideBarPanel.redoQuery();
						}
					});
				}
			}
		});
	}
}
