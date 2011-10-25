package com.lendme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.lendme.entities.ActivityRegistry;
import com.lendme.entities.Item;
import com.lendme.entities.Lending;
import com.lendme.entities.Lending.LendingStatus;
import com.lendme.entities.Message;
import com.lendme.entities.Topic;
import com.lendme.entities.User;
import com.lendme.utils.UserDateComparatorStrategy;

public class LendMeAdapter {
	
	private LendMeFacade lendMe = new LendMeFacade();
	

	/**
	 * Apaga todos os dados do sistema, como
	 * usuários, itens, categorias...
	 */
	public void resetSystem(){
		lendMe.resetSystem();
	}
	
	/* (non-Javadoc)
	 * @see com.lendme.LendMe#getSystemDate()
	 */
	public String getSystemDate(){
		
		return lendMe.getSystemDate().toString();
	}

	/**
	 * Simula a passagem dos dias no sistema.
	 * @param dias Número de dias que se passaram.
	 * @return Retorna o dia atual do sistema após
	 * o adiantamento do número de dias. 
	 */
	public String someDaysPassed(int amount){
		
		return lendMe.someDaysPassed(amount);
	}
	
	/**
	 * Abre a sessão do usuário cujo login é
	 * passado como parâmetro, sua sessão informa ao
	 * sistema que ele está logado e permite que ele
	 * navegue pelos perfis de outros usuários
	 * cadastrados no Lend-me.
	 * @param login Login do usuário cuja sessão se deseja abrir.
	 * @return Retorna um String que representa o ID da Sessão do
	 * usuário.   
	 * 
	 */
	public String openSession(String login) throws Exception{
		
		return lendMe.openSession(login);
	}
	
	/* (non-Javadoc)
	 * @see com.lendme.LendMe#closeSession(String)
	 */
	public void closeSession(String sessionId) throws Exception{
		
		lendMe.closeSession(sessionId);
	}
	
	/**
	 * Cadastra um usuário no sistema.
	 * @param String que representa o login do usuáio a ser cadastrado.
	 * @param String que representa o nome do usuário a ser cadastrado.
	 * @param endereco String que representa o endereço do usuário
	 * a ser cadastrado.
	 *
	 */
	public String registerUser(String login, String name, String... address)
		throws Exception{
		
		return lendMe.registerUser(login, name, address);
	}
	
   /** Handles with results from search in System, transforming them in a string array.
	 * @param solicitorSession the id of the session of the solicitor
	 * @param key the value expected for specified attribute
	 * @param attribute the specified attribute
	 * @return an array of strings containing the results names and addresses
	 * @see com.lendme.LendMeFacade#searchUsersByAttributeKey(String, String, String)
	 */
	public String[] searchUsersByAttributeKey(String solicitorSession, String key, String attribute)
		throws Exception{
		
		List<User> results = 
				new ArrayList<User>(lendMe.searchUsersByAttributeKey(solicitorSession, key, attribute));
		Collections.sort(results,  new UserDateComparatorStrategy());
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			User tmp = iterator.next();
			handled[i] = tmp.getName() + " - " + tmp.getAddress();
		}
		return handled;
	}
	
	/**
	 * Retorna uma String com os nomes de todos os usuários cadastrados
	 * no sistema ordenados pela distância entre cada um e o usuáio dono 
	 * do ID da sessão dada. 
	 * 
	 */

	public String[] listUsersByDistance(String solicitorSession) throws Exception{
		
		List<User> results = new ArrayList<User>(lendMe.listUsersByDistance(solicitorSession));
		
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			User tmp = iterator.next();
			handled[i] = tmp.getName() + " - " + tmp.getAddress();
		}
		return handled;
			
		
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param login the id of the session of the solicitor user
	 * @param attribute the attribute whose value is required
	 * @return an array of strings containing the results names
	 * @see com.lendme.LendMeFacade#getUserAttribute(String, String)
	 */
	public String getUserAttribute(String login, String attribute)
		throws Exception{
		
		return lendMe.getUserAttribute(login, attribute);
	}
	
	/**
	 * Faz a requisição de uma amizade.
	 * @param idSessao ID da sessão do usuário requisitente da amizade.
	 * @param login Login do usuáio cuja amizade está sendo solicitada.
	 */
	public void askForFriendship(String solicitorSession, String solicitedLogin)
		throws Exception{
		
		lendMe.askForFriendship(solicitorSession, solicitedLogin);
	}

	/**
	 *Aprova (aceita) uma amizade requisitada pelo usuário cujo
	 *login foi informado ao usuário cujo ID da Sessão foi passsado como parâmetro.  
	 * @param idSessao ID da sessão do usuário.
	 * @param login Login do usuário requisitante da amizade.
	 */
	public void acceptFriendship(String solicitedSession, String solicitorLogin)
		throws Exception{
			
			lendMe.acceptFriendship(solicitedSession, solicitorLogin);
	}

	/**
	 * Nega um pedido de amizade.
	 * @param idSessao ID da sessão do usuário que negará a amizade.
	 * @param login Login do usuário cuja solicitação de amaizade será negada.
	 */

	public void declineFriendship(String solicitedSession, String solicitorLogin)
		throws Exception{
		
		lendMe.declineFriendship(solicitedSession, solicitorLogin);
	}

	/**
	 * Desfaz uma amizade entre dois usuários.
	 * @param idSessao ID da sessão do usuário que desfará a amizade.
	 * @param login Login do usuário cuja amizade vai ser desfeita.
	 */
	public void breakFriendship(String solicitorSession, String solicitedLogin) throws Exception{
			
		lendMe.breakFriendship(solicitorSession, solicitedLogin);
	}

   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results logins
	 * @see com.lendme.LendMeFacade#getFriendshipRequests(String)
	 */
	public String[] getFriendshipRequests(String solicitorSession) throws Exception {

		List<User> results = new ArrayList<User>(lendMe.getFriendshipRequests(solicitorSession));
		Collections.sort(results,  new UserDateComparatorStrategy());
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}

   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results logins
	 * @see com.lendme.LendMeFacade#getFriends(String)
	 */
	public String[] getFriends(String solicitorSession)
		throws Exception{

		List<User> results = new ArrayList<User>(lendMe.getFriends(solicitorSession));
		Collections.sort(results,  new UserDateComparatorStrategy());
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results logins
	 * @see com.lendme.LendMeFacade#getFriends(String)
	 */
	public String[] getFriends(String solicitorSession, String solicitedLogin)
		throws Exception{

		List<User> results = new ArrayList<User>(lendMe.getFriends(solicitorSession, solicitedLogin));
		Collections.sort(results,  new UserDateComparatorStrategy());
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}
	
	/**
	 * @param idSessao ID da sessão do usuário que verificará se 
	 * o outro usuário é seu amigo.
	 * @param login Login do usuário cuja amizade quer ser identificada.
	 * @return Retorna true se ambos os usuários são amigos e false caso contrário.
	 * @throws Exception
	 */
	public boolean hasFriend(String solicitorSession, String solicitedLogin)
		throws Exception{

		return lendMe.hasFriend(solicitorSession, solicitedLogin);
	}

	/**
	 * Usuário dono do ID da sessão dada envia uma menssagem para o usuário 
	 * cujo login é igual a String destinatário.
	 * @param idSessao ID da sessão do usuário que enviará a menssagem.
	 * @param destinatario Login do usuário que receberá a menssagem.
	 * @param assunto Assunto da menssagem.
	 * @param mensagem Eh o texto da menssagem.
	 * @return Retorna o ID do tópico da menssagem.
	 */
	public String sendMessage(String senderSession, String subject,
			String message, String receiverLogin)
		throws Exception{
		
		return lendMe.sendMessage(senderSession, subject, message, receiverLogin);
	}

	/**
	 * Usuário dono do ID da sessão dada envia uma menssagem para o usuário 
	 * cujo login é igual a String destinatário.
	 * @param idSessao ID da sessão do usuário que enviará a menssagem.
	 * @param destinatario Login do usuário que receberá a menssagem.
	 * @param assunto Assunto da menssagem.
	 * @param mensagem Eh o texto da menssagem.
	 * @param idRequisicaoEmprestimo ID da requisição do emprestimo.
	 * @return Retorna o ID do tópico da menssagem.
	 * @throws Exception
	 */
	public String sendMessage(String senderSession, String subject,
			String message, String receiverLogin, String lendingId)
		throws Exception{
		
		return lendMe.sendMessage(senderSession, subject, message, receiverLogin, lendingId);
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results
	 * @see com.lendme.LendMeFacade#getTopics(String, String)
	 */
	public String[] getTopics(String solicitorSession, String topicType) throws Exception{
		
		List<Topic> results = lendMe.getTopics(solicitorSession, topicType);
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Topic> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getSubject();
		}
		return handled;
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results ids
	 * @see com.lendme.LendMeFacade#getTopics(String, String)
	 */
	public String[] getTopicsWithIds(String solicitorSession, String topicType) throws Exception{
		
		List<Topic> results = lendMe.getTopics(solicitorSession, topicType);
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Topic> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			Topic actualTopic = iterator.next();
			handled[i] = "\n\tAssunto: " + actualTopic.getSubject()
					 + "\n\tId: " + actualTopic.getID();
		}
		return handled;
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results
	 * @see com.lendme.LendMeFacade#getTopicsMessages(String, String)
	 */
	public String[] getTopicMessages(String solicitorSession, String topicId) throws Exception{
		
		List<Message> results = lendMe.getTopicMessages(solicitorSession, topicId);
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Message> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getMessage();
		}
		return handled;
	}

	/**
	 * Cadastra um Item em um usuário.
	 * @param idSessao String que representa o ID da sessão
	 * do usuário em que se quer cadastrar o item. 
	 * @param nome Nome do item que se quer cadastrar.
	 * @param descricao String que representa uma pequeno
	 * comentario sobre o Item.
	 * @param categoria Categora a que o Item está associado.
	 * O item pode ter mais de uma categoria.
	 * @return Retorna uma String que representa o ID do Item cadastrado
	 */	
	public String registerItem(String creatorSession, String name, String description, String category)
		throws Exception{
		
		return lendMe.registerItem(creatorSession, name, description, category);
	}

	/**
	 * 
	 * @param idSessao ID da sessão do usuário que deseja fazer a pesquisa.
	 * @param chave String que será pesquisada nos itens.
	 * @param atributo atributo em que a chava será pesquisada.
	 * Atributos suportados: "DESCRICAO", "CATEGORIA", "ID" e "NOME".
	 * @param tipoDeOrdenacao Tipos de ordenação suportados: "CRESCENTE" E "DECRESCENTE";
	 * @param criterioDeOrdenacao Criterios suportados: "REPUTACAO" e "DATACRIACAO".
	 * @return Retorna uma String com o nome de todos os itens encontrados na pesquisa.
	 */
	public String[] searchForItems(String solicitorSession, String key, String attribute,
		String disposition, String criteria) throws Exception{
		
		List<Item> results = lendMe.searchForItem(solicitorSession, key, attribute, disposition, criteria);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getName();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#searchForItem(String, String, String, String, String)
	 */	
	public String[] searchForItemsWithIds(String solicitorSession, String key, String attribute,
			String disposition, String criteria) throws Exception{
			
			List<Item> results = lendMe.searchForItem(solicitorSession, key, attribute, disposition, criteria);
			String[] handled = new String[results.size()];
			Iterator<Item> iterator = results.iterator();
			for ( int i=0; i<handled.length; i++ ){
				handled[i] = iterator.next().toString();
			}
			return handled;
		}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#getItems(String)
	 */	
	public String[] getItems(String solicitorSession) throws Exception{
		
		List<Item> results = new ArrayList<Item>(lendMe.getItems(solicitorSession));
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getName();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#getItems(String)
	 */	
	public String[] getItemsWithIds(String solicitorSession) throws Exception{
		
		List<Item> results = new ArrayList<Item>(lendMe.getItems(solicitorSession));
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().toString();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#getItems(String)
	 */	
	public String[] getItems(String solicitorSession, String solicitedLogin) throws Exception{
		
		List<Item> results = new ArrayList<Item>(lendMe.getItems(solicitorSession, solicitedLogin));
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getName();
		}
		return handled;
	}

	/**
	 * @param idItem ID do Item.
	 * @param atributo Tipo de atributo do Item. 
	 * Os únicos atributas aceitos são: "nome", "descricao" e "categoria".
	 * @return Retorna uma String que representa o valor do atributo
	 * que foi pesquisado no item identificado pelo ID. 
	 */	
	public String getItemAttribute(String itemId, String attribute)
		throws Exception{
		
		return lendMe.getItemAttribute(itemId, attribute);
	}
	
	/**
	 *Deleta um item do conjunto de itens do usuário dono do ID da sessão 
	 *informada.  
	 * @param idSessao ID da sessão do usuário.
	 * @param idItem ID do Item a ser excluído.
	 */
	public void deleteItem(String solicitorSession, String itemId) throws Exception{
		
		lendMe.deleteItem(solicitorSession, itemId);
	}
	
	/**
	 * Simula a passagem dos dias no sistema.
	 * @param dias Número de dias que se passaram.
	 * @return Retorna o dia atual do sistema após
	 * o adiantamento do número de dias. 
	 */
	public void registerInterestForItem(String solicitorSession, String itemId) throws Exception{
		
		lendMe.registerInterestForItem(solicitorSession, itemId);
	}

	/**
	 * Requisita o empréstimo de um item.
	 * @param idSessao ID da sessão do usuário requisitante.
	 * @param idItem Login do usuário cujo item será requisitado. 
	 * @param duracao Períodio estimado de duração do empréstimo. 
	 * @return String que representa o ID da requisição.
	 */
	public String requestItem(String solicitorSession, String itemId, int requiredDays) throws Exception{
		
		return lendMe.requestItem(solicitorSession, itemId, requiredDays);
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#approveLending(String, String)
	 */	
	public String approveLending(String solicitorSession, String requestId) throws Exception{
		
		return lendMe.approveLending(solicitorSession, requestId);
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#denyLending(String, String)
	 */	
	public String denyLending(String solicitorSession, String requestId) throws Exception{
		
		return lendMe.denyLending(solicitorSession, requestId);
	}
	

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#askForReturnOfItem(String, String)
	 */	
	public String askForReturnOfItem(String solicitorSession, String lendingId) throws Exception{
		
		return lendMe.askForReturnOfItem(solicitorSession, lendingId);
	}

	/**
	 * Devolve um item que o usuário cujo ID da sessão foi dado 
	 * pegou emprestado.
	 * 
	 * @param idSessao ID da sessão do usuário logado no sistema.
	 * @param idEmprestimo ID do empréstimo.
	 * @return Retorna o ID do emprestimo q foi devolvido. 
	 */

	public String returnItem(String solicitedSession, String lendingId) throws Exception{
		
		return lendMe.returnItem(solicitedSession, lendingId);
	}
	
	/**
	 * Confirma o termino de um emprestimo feito pelo usupario dono do ID da sessão dada.
	 */
	public String confirmLendingTermination(String solicitorSession, String lendingId) throws Exception{
		
		return lendMe.confirmLendingTermination(solicitorSession, lendingId);
	}
	
	/**
	 * Usuário dono do ID da sessão dada nega
	 * o término de um empréstimo que foi solicitado. 
	 * @param idSessao
	 * @param idEmprestimo ID do empréstimo que foi requisitada uma confirmação 
	 * de termino de emprestimo.
	 */

	public String denyLendingTermination(String solicitorSession, String lendingId) throws Exception{
		
		return lendMe.denyLendingTermination(solicitorSession, lendingId);
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#getReceivedItemRequests(String)
	 */	
	public String[] getReceivedItemRequests(String solicitorSession) throws Exception{
		
		List<Lending> results = new ArrayList<Lending>(lendMe.getReceivedItemRequests(
				solicitorSession));
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Lending> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			Lending actualLending = iterator.next();
			handled[i] = " ID:" + actualLending.getID() +
				"\n\tSolicitador: " + actualLending.getBorrower().getName() +	
				"\n\tItem:\n\t" + actualLending.getItem().toString();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#searchForLendingRecords(String, String)
	 */	
	public String[] getLendingRecords(String solicitorSession, String kind) throws Exception{
		
		List<Lending> results = new ArrayList<Lending>(lendMe.searchForLendingRecords(solicitorSession, kind));
		String[] handled = new String[results.size()];
		Iterator<Lending> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			String template = "%s-%s:%s:%s";
			Lending tmp = iterator.next();
			handled[i] = String.format(template, tmp.getLender().getLogin(),
					tmp.getBorrower().getLogin(), tmp.getItem().getName(),
					tmp.getStatus() == LendingStatus.ONGOING ? "Andamento" : 
				  ( tmp.getStatus() == LendingStatus.FINISHED ? "Completado" : 
					tmp.getStatus() == LendingStatus.CANCELLED ? "Cancelado" : "Negado" ));
		}
		return handled;
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMeFacade#searchForLendingRecords(String, String)
	 */	
	public String[] getLendingRecordsWithIds(String solicitorSession, String kind) throws Exception{
		
		List<Lending> results = new ArrayList<Lending>(lendMe.searchForLendingRecords(solicitorSession, kind));
		String[] handled = new String[results.size()];
		Iterator<Lending> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			String template = "%s-%s:%s:%s\n\t Id: %s";
			Lending tmp = iterator.next();
			handled[i] = String.format(template, tmp.getLender().getLogin(),
					tmp.getBorrower().getLogin(), tmp.getItem().getName(),
					tmp.getStatus() == LendingStatus.ONGOING ? "Andamento" : 
				  ( tmp.getStatus() == LendingStatus.FINISHED ? "Completado" : 
					tmp.getStatus() == LendingStatus.CANCELLED ? "Cancelado" : "Negado" ),
					tmp.getID());
		}
		return handled;
	}
	
	/**
	 * 
	 * @param idSession ID da sessão do usuário que deseja visualisar 
	 * o ranking.
	 * @param categoria Categoria que será usada como critério de ranqueamento.
	 * @return Retorna uma String com o nome de todos os usuários
	 * ranqueados. 
	 */
	public String getRanking(String solicitorSession, String category) throws Exception{
		
		return lendMe.getRanking(solicitorSession, category);
	}
	
	public String viewProfile(String solicitorSessionId, 
			String solicitedUserLogin) throws Exception {
		
		return lendMe.viewProfile(solicitorSessionId, solicitedUserLogin);
	}
	
	/**
	 * @return Retorna o histórico de atividades do usuário dono do ID da sessaõ dado.
	 */
	public String[] getActivityHistory(String solicitorSessionId) throws Exception {
		
		List<ActivityRegistry> results = lendMe.getActivityHistory(
				solicitorSessionId);
		String[] handled;
		
		if (results.size() == 0) {
			handled = new String[1];
			handled[0] = "Não há atividades";
			return handled;
		}
		
		handled = new String[results.size()];
		Iterator<ActivityRegistry> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			ActivityRegistry actualActivityRegistry = iterator.next();
			handled[i] = actualActivityRegistry.getDescription();
		}
		return handled;
	}

	public String[] getJointActivityHistory(String solicitorSessionId) throws Exception {
		List<ActivityRegistry> results = lendMe.getJointActivityHistory(
				solicitorSessionId);
		String[] handled;
		
		if (results.size() == 0) {
			handled = new String[1];
			handled[0] = "Não há atividades";
			return handled;
		}
		
		handled = new String[results.size()];
		Iterator<ActivityRegistry> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			ActivityRegistry actualActivityRegistry = iterator.next();
			handled[i] = actualActivityRegistry.getDescription();
		}
		return handled;
	}

	public String[] getFriendsPublishedItemRequests(String solicitorSessionId) throws Exception {
		List<Lending> results = lendMe.getFriendsPublishedItemRequests(solicitorSessionId);
		String[] handled;
		
		if (results.size() == 0) {
			handled = new String[1];
			handled[0] = "Não há publicações de pedidos de seus amigos";
			return handled;
		}
		
		handled = new String[results.size()];
		Iterator<Lending> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			Lending currentLending= iterator.next();
			handled[i] = "Id: " + currentLending.getID() + "\n\t "
					+ currentLending.getBorrower().getName()+ "\n\t "
					+ currentLending.getDesiredItemName()+ "\n\t "
					+ currentLending.getDesiredItemDescription();
		}
		return handled;
	}
			
	/**
	 * Publica o Pedido de um item. 	
	 * @param idSessao ID da sessão do usuário que deseja publicar 
	 * o pedido de um item.
	 * @param nomeItem Nome do item cujo pedido será publicado.
	 * @param descricaoItem Descrição do item cujo pedido será publicado. 
	 */
	public String publishItemRequest(String sessionId, String itemName,
			String itemDescription) throws Exception{
		return lendMe.publishItemRequest(sessionId, itemName, itemDescription);
	}

	/**
	 * Oferece um item .
	 * @param idSessao ID da sessão do usuário que deseja oferecer o item.
	 * @param idPublicacaoPedido
	 * @param idItem ID do item que será oferecido. 
	 */
	public void offerItem(String sessionId, String requestPublicationId,
			String itemId) throws Exception{
		lendMe.offerItem(sessionId, requestPublicationId, itemId);
	}

	/**
	 * Republica o pedido de um item.
	 * @param idSessao idSessao ID da sessão do usuário que deseja oferecer o item.
	 * @param idPublicacaoPedido
	 */
	public void republishItemRequest(String sessionId, String requestPublicationId) 
		throws Exception{
		lendMe.republishItemRequest(sessionId, requestPublicationId);		
	}
	
	
}