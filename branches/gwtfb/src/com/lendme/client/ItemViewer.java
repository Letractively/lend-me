package com.lendme.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.IntegerBox;

public class ItemViewer extends PopupPanel {

	@SuppressWarnings("unused")
	private String imgURL;
	private String nameStr;
	private String descriptionStr;
	private String statusStr;
	private String infoStr;
	private String itemIdLocal;
	private String lendingIdLocal;
	private String[] idsInterestingUser;
	
	private ListBox actions;
	private ListBox interestingUsers;
	private IntegerBox qtdeDias;
	
	private String idSessionLocal;
	private LendMeAsync lendmeLocal;
	
	public ItemViewer(LendMeAsync lendme, String idSession, final String viewedLogin, String imgURL,
					  String nameStr, String descriptionStr, String statusStr,
					  String infoStr, String itemId, String lendingId,String interesteds,final boolean iAmOwner ,boolean lent, boolean requested) {
		
		super(true);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		
		this.imgURL = imgURL;
		this.nameStr = nameStr;
		this.descriptionStr = descriptionStr;
		this.statusStr = statusStr;
		this.infoStr = infoStr;
		this.idSessionLocal = idSession;
		this.itemIdLocal = itemId;
		this.lendmeLocal = lendme;
		this.lendingIdLocal = lendingId;
		
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
		
		interestingUsers = new ListBox();
		absolutePanel.add(interestingUsers, 169, 132);
		interestingUsers.setSize("173px", "22px");
		
		Label lblNewLabel_7 = new Label("Ações:");
		absolutePanel.add(lblNewLabel_7, 7, 191);
		
		actions = new ListBox();
		absolutePanel.add(actions, 64, 189);
		actions.setSize("200px", "22px");
		
		PushButton agir = new PushButton("New button");
		agir.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				if(actions.getSelectedIndex() == 0 && actions.getItemText(0).equals("deletar")){//Delete Item
					
					boolean confirm = Window.confirm("Tem certeza que deseja apagar o item ?");
					
					if(confirm){
						lendmeLocal.deleteItem(idSessionLocal, itemIdLocal, new AsyncCallback<Void>() {
							
							@Override
							public void onSuccess(Void result) {
								Window.alert("Item removido com sucesso!");
								hide();
								LendMeEntryPoint.displayCurrentUserItems(viewedLogin);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Erro na remoção do item. Tente novamente!");
								
							}
						});
					}
				}
				
				if(actions.getSelectedIndex() == 0 && actions.getItemText(0).equals("pedir retorno")){
					
					
					lendmeLocal.returnItem(idSessionLocal, lendingIdLocal, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente!");
							
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Seu pedido de retorno foi encaminhado.");
						}
					});
				}
				
				if(actions.getSelectedIndex() == 1 && actions.getItemText(1).equals("emprestar")){
					lendmeLocal.approveLending(idSessionLocal, idsInterestingUser[interestingUsers.getSelectedIndex()], new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema no servidor, tente novamente!");
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Você aprovou o emprestimo!");
						}
					});
				}
				
				if(actions.getSelectedIndex() == 0 && actions.getItemText(0).equals("pedir emprestado")){
					lendmeLocal.requestItem(idSessionLocal, itemIdLocal, qtdeDias.getValue(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Problema na comunicação com o servidor. Tente novamente.");
							
						}

						@Override
						public void onSuccess(String result) {
							Window.alert("Requisicao realizada com sucesso!");
							
						}
					});
				}
			}
		});
		agir.setHTML("<center><b>Agir</b></center>");
		absolutePanel.add(agir, 274, 188);
		agir.setSize("56px", "15px");
		
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
		
		name.setText(this.nameStr);
		descricao.setText(this.descriptionStr);
		status.setText(this.statusStr);
		info.setText(this.infoStr);
		
		qtdeDias = new IntegerBox();
		qtdeDias.setEnabled(false);
		absolutePanel.add(qtdeDias, 96, 160);
		qtdeDias.setSize("42px", "17px");
		
		Label lblNewLabel_1 = new Label("Qtde. dias:");
		absolutePanel.add(lblNewLabel_1, 8, 160);
		
		
		if(!lent && iAmOwner){
			actions.addItem("deletar");
			
		}else if(iAmOwner && lent){
			actions.addItem("pedir retorno");
		}
		if(requested){
			actions.addItem("emprestar");
		}
		if(!iAmOwner){
			actions.addItem("pedir emprestado");
			qtdeDias.setEnabled(true);
		}
		
		idsInterestingUser = new String[interesteds.split(";").length];
		int i = 0;
		
		for(String actualUserName : interesteds.split(";")){
			if(!actualUserName.equals("")){
				interestingUsers.addItem(actualUserName);
				idsInterestingUser[i] = actualUserName.split(":")[1];
				i++;
			}else{
				break;
			}
		}
		
	}
}
