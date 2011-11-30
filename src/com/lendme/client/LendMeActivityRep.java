package com.lendme.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class LendMeActivityRep extends AbsolutePanel {

	final String ICON_ADD_USER = "http://support.microsoft.com/library/images/support/kbgraphics/Public/en-us/office/New%20icon%20image.jpg";
	final String ICON_NEW_ITEM = "http://www.ablemuse.com/v4/images/new-icon.gif";
	final String ICON_LEND = "http://lcvinvestments.com/images/handshake.gif";
	final String ICON_ITEM_INTEREST = "http://2.bp.blogspot.com/_heKfcJbaI4c/TAd-V76tdxI/AAAAAAAABfM/_4duzSOlLe0/s200/smEyeballs.jpg";
	final String ICON_FINISH_LENDING = "http://cdn5.iconfinder.com/data/icons/fatcow/32x32/flag_finish.png";
	final String ICON_NEED_ITEM = "http://www.gratisjogos.info/image.jogos/puzzle.jpg";
	private Image kindIcon;
	
	final String ADICAO_DE_AMIGO_CONCLUIDA = " são amigos agora";
	final String CADASTRO_DE_ITEM = " cadastrou ";
	final String EMPRESTIMO_EM_ANDAMENTO = " emprestou ";
	final String REGISTRO_DE_INTERESSE_EM_ITEM = " ofereceu o item ";
	final String TERMINO_DE_EMPRESTIMO = " confirmou o término do empréstimo do item ";
	final String PEDIDO_DE_ITEM = " precisa do item ";
	
	
	public LendMeActivityRep(String message, String date){
		setStyleName("borda");
		setSize("700px", "70px");
		
		kindIcon = null;
		if ( message.contains("ADICAO_DE_AMIGO_CONCLUIDA") ){
			kindIcon = new Image(ICON_ADD_USER);
		}
		else if ( message.contains("CADASTRO_DE_ITEM") ){
			kindIcon = new Image(ICON_NEW_ITEM);
		}
		else if ( message.contains("EMPRESTIMO_EM_ANDAMENTO") ){
			kindIcon = new Image(ICON_LEND);
		}
		else if ( message.contains("REGISTRO_DE_INTERESSE_EM_ITEM") ){
			kindIcon = new Image(ICON_ITEM_INTEREST);
		}
		else if ( message.contains("TERMINO_DE_EMPRESTIMO") ){
			kindIcon = new Image(ICON_FINISH_LENDING);
		}
		else if ( message.contains("PEDIDO_DE_ITEM") ){
			kindIcon = new Image(ICON_NEED_ITEM);
		}	
		else if (message.contains("REPUBLICACAO_DE_PEDIDO_DE_ITEM") ){
			kindIcon = new Image(ICON_NEED_ITEM);
		}
		add(kindIcon, 10, 10);
		kindIcon.setSize("40px", "40px");
		
		Label description = new Label(message);
		add(description, 55, 20);
		description.setSize("400px", "23px");
		
		Label hour = new Label(date);
		hour.setStyleName("gwtHour");
		add(hour, 504, 23);
		hour.setSize("140px", "20px");
		
		Label label = new Label("_____________________________________________________________________________________________");
		label.setStyleName("linha");
		add(label, 10, 49);
		label.setSize("578px", "21px");
	}
}
