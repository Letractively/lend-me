package com.lendme.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;

public class LendMeItemPopup extends PopupPanel {

	public LendMeItemPopup(final LendMeAsync lendMeService, final String sessionId,
			final String viewedLogin, String imgURL, final String[] itemInfo) {

		super(true);
		setAnimationEnabled(true);
		setGlassEnabled(true);

		final AbsolutePanel absolutePanel = new AbsolutePanel();
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

		Label interestedUsersLabel = new Label("Usuarios Interessados:");
		absolutePanel.add(interestedUsersLabel, 8, 134);

		final ListBox interestedUserLendingIDsListBox = new ListBox();
		absolutePanel.add(interestedUserLendingIDsListBox, 169, 132);
		interestedUserLendingIDsListBox.setSize("173px", "22px");

		Label lblNewLabel_7 = new Label("Ações:");
		absolutePanel.add(lblNewLabel_7, 7, 191);

		final ListBox actions = new ListBox();
		absolutePanel.add(actions, 64, 189);
		actions.setSize("200px", "22px");

		final String[] interestedUserLendingIDs = new String[itemInfo[6].split(";").length];
		final String[] interestedUserDesiredDaysAmount = new String[interestedUserLendingIDs.length];
		int i = 0;

		for ( String actualUserName : itemInfo[6].split(";") ) {
			if ( !actualUserName.trim().isEmpty() ){
				interestedUserLendingIDsListBox.addItem(actualUserName.split(":")[1]);
				interestedUserLendingIDs[i] = actualUserName.split(":")[0];
				interestedUserDesiredDaysAmount[i] = actualUserName.split(":")[2];
				i++;
			}
		}

		Image image = new Image(imgURL);
		absolutePanel.add(image, 10, 29);
		image.setSize("100px", "92px");

		final TextBox displayInfo = new TextBox();
		displayInfo.setVisible(false);
		
		Image returnedState = new Image("http://www.veryicon.com/icon/preview/System/iCandy%20Junior%20Toolbar/Back%202%20Icon.jpg");
		absolutePanel.add(returnedState, 317, 5);
		returnedState.setSize("23px", "22px");
		returnedState.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				displayInfo.setText("Pedido de devolucao do item");
				absolutePanel.add(displayInfo, 317, 0);
				displayInfo.setVisible(true);
			}
			
		});
		returnedState.addMouseOutHandler(new MouseOutHandler(){

			@Override
			public void onMouseOut(MouseOutEvent event) {
				absolutePanel.remove(displayInfo);
				displayInfo.setVisible(false);
			}
			
		});

		Image lentState = new Image("http://icons.iconarchive.com/icons/visualpharm/must-have/48/Information-icon.png");
		absolutePanel.add(lentState, 288, 5);
		lentState.setSize("23px", "22px");
		lentState.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				displayInfo.setText("Pedido de emprestimo do item");
				absolutePanel.add(displayInfo, 288, 0);
				displayInfo.setVisible(true);
			}
			
		});
		lentState.addMouseOutHandler(new MouseOutHandler(){

			@Override
			public void onMouseOut(MouseOutEvent event) {
				absolutePanel.remove(displayInfo);
				displayInfo.setVisible(false);
			}
			
		});

		Image interestedState = new Image("http://icdn.pro/images/en/e/y/eyes-see-icone-9513-48.png");
		absolutePanel.add(interestedState, 259, 5);
		interestedState.setSize("23px", "22px");
		interestedState.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				displayInfo.setText("Voce esta interessado no item");
				absolutePanel.add(displayInfo, 259, 0);
				displayInfo.setVisible(true);
			}
			
		});
		interestedState.addMouseOutHandler(new MouseOutHandler(){

			@Override
			public void onMouseOut(MouseOutEvent event) {
				absolutePanel.remove(displayInfo);
				displayInfo.setVisible(false);
			}
			
		});

		Image unavailableState = new Image("http://icons.iconarchive.com/icons/designkindle/build/48/Delete-icon.png");
		absolutePanel.add(unavailableState, 230, 5);
		unavailableState.setSize("23px", "22px");
		unavailableState.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				displayInfo.setText("O item ja foi emprestado");
				absolutePanel.add(displayInfo, 230, 0);
				displayInfo.setVisible(true);
			}
			
		});
		unavailableState.addMouseOutHandler(new MouseOutHandler(){

			@Override
			public void onMouseOut(MouseOutEvent event) {
				absolutePanel.remove(displayInfo);
				displayInfo.setVisible(false);
			}
			
		});

		Image availableState = new Image("http://icons.iconarchive.com/icons/dryicons/simplistica/48/accept-icon.png");
		absolutePanel.add(availableState, 201, 5);
		availableState.setSize("23px", "22px");
		availableState.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				displayInfo.setText("O item esta disponivel");
				absolutePanel.add(displayInfo, 201, 0);
				displayInfo.setVisible(true);
			}
			
		});
		availableState.addMouseOutHandler(new MouseOutHandler(){

			@Override
			public void onMouseOut(MouseOutEvent event) {
				absolutePanel.remove(displayInfo);
				displayInfo.setVisible(false);
			}
			
		});
		
		Image requestedState = new Image("http://icons.iconarchive.com/icons/everaldo/crystal-clear/48/Action-share-icon.png");
		absolutePanel.add(requestedState, 172, 5);
		requestedState.setSize("23px", "22px");
		requestedState.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				if ( new Boolean(itemInfo[7]).booleanValue() ){
					displayInfo.setText("Alguem esta interessado no seu item");
				}
				else{
					displayInfo.setText("Voce pediu esse item emprestado");
				}
				absolutePanel.add(displayInfo, 172, 0);
				displayInfo.setVisible(true);
			}
			
		});
		requestedState.addMouseOutHandler(new MouseOutHandler(){

			@Override
			public void onMouseOut(MouseOutEvent event) {
				absolutePanel.remove(displayInfo);
				displayInfo.setVisible(false);
			}
			
		});
		
		Image askedForReturnState = new Image("http://icons.iconarchive.com/icons/everaldo/crystal-clear/48/Action-share-icon.png");
		absolutePanel.add(askedForReturnState, 143, 5);
		askedForReturnState.setSize("23px", "22px");
		askedForReturnState.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				displayInfo.setText("Voce ja pediu esse item de volta");
				absolutePanel.add(displayInfo, 143, 0);
				displayInfo.setVisible(true);
			}
			
		});
		askedForReturnState.addMouseOutHandler(new MouseOutHandler(){

			@Override
			public void onMouseOut(MouseOutEvent event) {
				absolutePanel.remove(displayInfo);
				displayInfo.setVisible(false);
			}
			
		});
		
		name.setText(itemInfo[0]);
		descricao.setText(itemInfo[2]);
		status.setText(itemInfo[8]);
		info.setText(itemInfo[1]);

		final IntegerBox daysAmount = new IntegerBox();
		absolutePanel.add(daysAmount, 96, 160);
		daysAmount.setSize("42px", "17px");

		Label daysAmountLabel = new Label("Qtde. dias:");
		absolutePanel.add(daysAmountLabel, 8, 160);
		
		interestedUserLendingIDsListBox.setVisible(false);
		interestedUsersLabel.setVisible(false);
		daysAmount.setVisible(false);
		daysAmountLabel.setVisible(false);

		Label moreInfo = new Label("");
		absolutePanel.add(moreInfo, 109, 105);
		moreInfo.setSize("231px", "23px");
		moreInfo.setVisible(false);
		
		PushButton agir = new PushButton("");

		agir.setHTML("<center><b>Agir</b></center>");
		absolutePanel.add(agir, 274, 188);
		agir.setSize("56px", "15px");
		
		if ( new Boolean(itemInfo[7]).booleanValue() ){
			if ( itemInfo[8].equals("LENT") ){
				moreInfo.setVisible(true);
				moreInfo.setText(
						interestedUserLendingIDsListBox
						.getItemText(0)+" pediu seu item por "+interestedUserDesiredDaysAmount[0]+" dias no dia "+itemInfo[6].split(":")[3]+".");
				actions.addItem("pedir de volta");
			}
			else if ( itemInfo[8].equals("ASKED_FOR_RETURN") ){
				actions.addItem("pedir de volta");
				actions.setEnabled(false);
				agir.setEnabled(false);
			}
			else if ( itemInfo[8].equals("AVAILABLE") ){
				actions.addItem("deletar");
			}
			else if ( itemInfo[8].equals("REQUESTED") ){
				actions.addItem("emprestar");
				actions.addItem("nao emprestar");
				daysAmount.setVisible(true);
				daysAmount.setEnabled(false);
				daysAmount.setText(itemInfo[6].split(":")[2].replace(";", ""));
				daysAmountLabel.setVisible(true);
				interestedUserLendingIDsListBox.setVisible(true);
				interestedUserLendingIDsListBox.setEnabled(true);
				interestedUsersLabel.setVisible(true);
			}
			else if ( itemInfo[8].equals("RETURNED") ){
				actions.addItem("confirmar devolucao");
				actions.addItem("negar que houve devolucao");
			}
		}
		else{
			if ( itemInfo[8].equals("UNAVAILABLE") ){
				actions.addItem("tenho interesse nesse item");
			}
			else if ( itemInfo[8].equals("INTERESTED") ){
				actions.addItem("tenho interesse nesse item");
				actions.setEnabled(false);
			}
			else if ( itemInfo[8].equals("LENT") ){
				moreInfo.setVisible(true);
				moreInfo.setText(
						interestedUserLendingIDsListBox
						.getItemText(0)+" te emprestou esse item por "+interestedUserDesiredDaysAmount[0]+" dias no dia "+itemInfo[6].split(":")[3]+".");
				actions.addItem("devolver");
			}
			else if ( itemInfo[8].equals("RETURNED") ){
				actions.addItem("devolver");
				actions.setEnabled(false);
				agir.setEnabled(false);
			}
			else if ( itemInfo[8].equals("AVAILABLE") ){
				actions.addItem("pedir emprestado");
				daysAmount.setVisible(true);
				daysAmount.setEnabled(true);
				daysAmountLabel.setVisible(true);
			}
			else if ( itemInfo[8].equals("REQUESTED") ){
				actions.addItem("pedir emprestado");
				actions.setEnabled(false);
				agir.setEnabled(false);
			}
		}

		interestedUserLendingIDsListBox.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				daysAmount.setText(interestedUserLendingIDs[interestedUserLendingIDsListBox.getSelectedIndex()]);
			}
			
		});
		
		
		agir.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				Window.alert(actions.getItemText(actions.getSelectedIndex()));
				
				if ( actions.getItemText(actions.getSelectedIndex()).equals("deletar") ) {

					boolean confirm = Window.confirm("Tem certeza que deseja apagar o item ?");

					if(confirm){
						lendMeService.deleteItem(sessionId, itemInfo[4], new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								Window.alert("Item "+itemInfo[0]+" removido com sucesso!");
								hide();
								LeftOptionsSideBarPanel.redoItemQuery();
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
							}
						});
					}
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("pedir de volta") ) {

					lendMeService.askForReturnOfItem(sessionId, itemInfo[5], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Seu pedido de retorno do item "+itemInfo[0]+" foi encaminhado.");
							hide();
							LeftOptionsSideBarPanel.redoItemQuery();
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
							Window.alert("Voce emprestou o item "+itemInfo[0]+"!");
							hide();
							LeftOptionsSideBarPanel.redoItemQuery();
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
							Window.alert("Voce negou emprestimo do item "+itemInfo[0]+"!");
							hide();
							LeftOptionsSideBarPanel.redoItemQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("confirmar devolucao") ) {

					lendMeService.confirmLendingTermination(sessionId, itemInfo[5], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce confirmou que houve devolucao do item "+itemInfo[0]+"!");
							hide();
							LeftOptionsSideBarPanel.redoItemQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("negar que houve devolucao") ) {

					lendMeService.denyLendingTermination(sessionId, itemInfo[5], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce negou que houve devolucao do item "+itemInfo[0]+"!");
							hide();
							LeftOptionsSideBarPanel.redoItemQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("devolver") ) {

					lendMeService.returnItem(sessionId, itemInfo[5], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce devolveu o item "+itemInfo[0]+"! Aguarde confirmacao do dono.");
							hide();
							LeftOptionsSideBarPanel.redoItemQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("tenho interesse nesse item") ) {

					lendMeService.registerInterestForItem(sessionId, itemInfo[4], new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(Void result) {
							Window.alert("Voce vai ser alertado da disponibilidade do item "+itemInfo[0]+"!");
							hide();
							LeftOptionsSideBarPanel.redoItemQuery();
						}
					});
				}
				else if ( actions.getItemText(actions.getSelectedIndex()).equals("pedir emprestado") ) {

					if ( daysAmount.getValue() == null || daysAmount.getValue() < 1 ){
						Window.alert("A quantidade de dias nao pode ser menor que 1 (um)!");
						return;
					}
					lendMeService.requestItem(sessionId, itemInfo[4], daysAmount.getValue(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente: "+caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Voce pediu o item "+itemInfo[0]+" emprestado!");
							hide();
							LeftOptionsSideBarPanel.redoItemQuery();
						}
					});
				}
			}
		});
	}
}
