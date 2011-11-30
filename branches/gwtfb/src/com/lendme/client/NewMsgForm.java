package com.lendme.client;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewMsgForm extends PopupPanel {
	
	private LendMeAsync lendMeService;
	private String solicitorSessionId;
	
	private TextBox receiverTextBox; 
	private TextBox subjectText;
	private ListBox lendingListBox;
	private RadioButton negotiationRdioBtn;
	private RichTextArea textArea;
	private RichTextToolbar toolbar;
	private NewMsgForm me;
	
	private String receivedLendingId = "";

	public NewMsgForm(LendMeAsync lendMeService, String solicitorSessionId,
			String receiver, String subject, String lendingId) {
		this(lendMeService, solicitorSessionId);
		
		receiverTextBox.setText(receiver);
		subjectText.setText(subject);
		receivedLendingId = lendingId;
		
		if (receivedLendingId.trim().isEmpty()) {
			negotiationRdioBtn.setValue(false);
		} else {
			negotiationRdioBtn.setValue(true);
			showOngoingLendings();
			selectReceivedLending();
			lendingListBox.setVisible(true);
		}
		
	}
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public NewMsgForm(LendMeAsync lendMeService, String solicitorSessionId) {

		super(true);
		setSize("700px", "430px");
		setAnimationEnabled(true);
		setGlassEnabled(true);
		this.solicitorSessionId = solicitorSessionId;
		this.lendMeService = lendMeService;
		
		me = this;
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setStyleName("topicsLines");
		setWidget(absolutePanel);
		absolutePanel.setSize("653px", "400px");
		
		Button sendMsgButton = new Button("Enviar");
		absolutePanel.add(sendMsgButton, 265, 362);
		sendMsgButton.setSize("100px", "28px");
		
		sendMsgButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				sendMessage();
			}
		});
		
		receiverTextBox = new TextBox();
		absolutePanel.add(receiverTextBox, 72, 25);
		receiverTextBox.setSize("213px", "20px");
		
		Label lblNewLabel = new Label("Para:");
		lblNewLabel.setStyleName("msg-label");
		lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		absolutePanel.add(lblNewLabel, 31, 27);
		lblNewLabel.setSize("42px", "26px");
		
		Label lblMensagem = new Label("Mensagem:");
		lblMensagem.setStyleName("msg-label");
		lblMensagem.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		absolutePanel.add(lblMensagem, 10, 94);
		lblMensagem.setSize("88px", "26px");
		
		 // Create the text area and toolbar
	    textArea = new RichTextArea();
	    textArea.ensureDebugId("cwRichText-area");
	    textArea.setSize("636px", "168px");
	    toolbar = new RichTextToolbar(textArea);
	    toolbar.ensureDebugId("cwRichText-toolbar");
	    toolbar.setWidth("380px");
	    
	    absolutePanel.add(toolbar, 10, 126);
	    absolutePanel.add(textArea, 10, 190);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("topicsLines");
		absolutePanel.add(verticalPanel, 42, 0);
		verticalPanel.setSize("611px", "19px");
		
		Label lblNovaMensagem = new Label("Nova Mensagem");
		verticalPanel.add(lblNovaMensagem);
		lblNovaMensagem.setWidth("610px");
		lblNovaMensagem.setStyleName("new-msg-label");
		
		Label lblAssunto = new Label("Assunto:");
		lblAssunto.setStyleName("msg-label");
		lblAssunto.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		absolutePanel.add(lblAssunto, 10, 59);
		lblAssunto.setSize("42px", "26px");
		
		subjectText = new TextBox();
		absolutePanel.add(subjectText, 72, 59);
		subjectText.setSize("213px", "20px");
		
		negotiationRdioBtn = new RadioButton("new name", "Negociacao");
		negotiationRdioBtn.setStyleName("msg-label");
		absolutePanel.add(negotiationRdioBtn, 314, 25);
		
		negotiationRdioBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showOngoingLendings();
			}
		});
		
		lendingListBox = new ListBox();
		lendingListBox.setVisible(false);
		absolutePanel.add(lendingListBox, 301, 51);
		lendingListBox.setSize("342px", "69px");
		
		Image image = new Image("http://www.loverbits.com/Image/step2_img.png");
		absolutePanel.add(image, 0, 0);
		image.setSize("42px", "19px");
		
	}
	
	private void showOngoingLendings() {
		lendMeService.getLendingRecords(solicitorSessionId, "todos", 
			new AsyncCallback<Map<String,String[]>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Erro: Nao foi possivel ter acesso aos emprestimos do usuario." + caught.getMessage());
				}

				@Override
				public void onSuccess(Map<String, String[]> result) {
					putLendingsInListBox(result);
				}
		});
		lendingListBox.setVisibleItemCount(1);
		lendingListBox.setVisible(true);
	}
	

	private void putLendingsInListBox(Map<String, String[]> result) {
		
		String lendingString;
		for (String lending : result.keySet()) {
			lendingString = "Id: " + lending + "; Dono: " + result.get(lending)[1] + 
					"; Benef: " + result.get(lending)[0] + "; Item: " + result.get(lending)[2]
					+ "; Status: " + result.get(lending)[9];
			lendingListBox.addItem(lendingString);
		}
		lendingListBox.setVisibleItemCount(1);
		lendingListBox.setVisible(true);
	}
	
	private void sendMessage() {
		
		if (! negotiationRdioBtn.getValue()) {
			lendMeService.sendMessage(solicitorSessionId, subjectText.getText(),
					textArea.getText(), receiverTextBox.getText(), new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							Window.alert("Mensagem enviada com sucesso!");
							me.hide();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Erro: Envio da mensagem falhou!\n" + caught.getMessage());
							me.hide();
						}
					});
		} else {
			
			String lendingId = getChosenLendingId();
			
			lendMeService.sendMessage(solicitorSessionId, subjectText.getText(),
					textArea.getText(), receiverTextBox.getText(), 
					lendingId, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							Window.alert("Mensagem enviada com sucesso!");
							me.hide();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Erro: Envio da mensagem falhou!\n" + caught.getMessage());
							me.hide();
						}
					});
		}
	}
	
	public void resetFields() {
		receiverTextBox.setText("");
		subjectText.setText("");
		textArea.setText("");
		negotiationRdioBtn.setValue(false);
		lendingListBox.clear();
	}
	
	private String getChosenLendingId() {
		if (receivedLendingId.trim().isEmpty()) {
			return lendingListBox.getItemText(lendingListBox.getSelectedIndex()).split(";")[0].replace("Id: ", "");
		} else {
 			return receivedLendingId;
		}
	}

	private void selectReceivedLending() {
		for (int i = 0; i < lendingListBox.getItemCount(); i++) {
			if (lendingListBox.getItemText(i).split(";")[0].replace("Id: ", "").equals(receivedLendingId)) {
				lendingListBox.setItemSelected(i, true);
			}
		}
	}
}
