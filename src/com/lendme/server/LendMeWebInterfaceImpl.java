package com.lendme.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lendme.client.LendMe;
import com.lendme.server.entities.ActivityRegistry;
import com.lendme.server.entities.Item;
import com.lendme.server.entities.Lending;
import com.lendme.server.entities.Lending.LendingStatus;
import com.lendme.server.entities.Message;
import com.lendme.server.entities.Topic;
import com.lendme.server.entities.User;
import com.lendme.server.utils.UserDateComparatorStrategy;

@SuppressWarnings("serial")
public class LendMeWebInterfaceImpl extends RemoteServiceServlet implements LendMe {
	
	private LendMeFacade lendMe = LendMeFacade.getInstance();
	

	/**
	 * Apaga todos os dados do sistema, como
	 * usuários, itens, categorias...
	 */
	public void resetSystem(){
		lendMe.resetSystem();
	}
	
	/**
	 * @return Retorna uma String informando a data atual do
	 * sistema.
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
	
	/**
	 * Fecha a sessão do usuário, deslogando o mesmo
	 * do sistema.
	 * @param sessionId ID da sessão do usuário a ser fechada.
	 */
	public void closeSession(String sessionId) throws Exception{
		
		lendMe.closeSession(sessionId);
	}
	
	/**
	 * Cadastra um usuário no sistema.
	 * @param login String que representa o login do usuáio a ser cadastrado.
	 * @param name String que representa o nome do usuário a ser cadastrado.
	 * @param address endereco String que representa o endereço do usuário
	 * a ser cadastrado.
	 *
	 */
	public String registerUser(String login, String name, String... address)
		throws Exception{
		
		return lendMe.registerUser(login, name, address);
	}
	
   /** Handles with results from search in System, transforming them in a string array.
	 * @param solicitorSession the id of the session of the solicitor
	 * @param key the value pected for specified attribute
	 * @param attribute the specified attribute
	 * @return an array of strings containing the results names and addresses
	 * @see com.lendme.LendMFacade#searchUsersByAttributeKey(String, String, String)
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
	 * @param solicitorSession ID da sessão do usuário requisitente da amizade.
	 * @param solicitedLogin Login do usuário cuja amizade está sendo solicitada.
	 */
	public void askForFriendship(String solicitorSession, String solicitedLogin)
		throws Exception{
		
		lendMe.askForFriendship(solicitorSession, solicitedLogin);
	}

	/**
	 *Aprova (aceita) uma amizade requisitada pelo usuário cujo
	 *login foi informado ao usuário cujo ID da Sessão foi passsado como parâmetro.  
	 * @param solicitedSession ID da sessão do usuário.
	 * @param solicitorLogin Login do usuário requisitante da amizade.
	 */
	public void acceptFriendship(String solicitedSession, String solicitorLogin)
		throws Exception{
			
			lendMe.acceptFriendship(solicitedSession, solicitorLogin);
	}

	/**
	 * Nega um pedido de amizade.
	 * @param solicitedSession ID da sessão do usuário que negará a amizade.
	 * @param solicitorLogin Login do usuário cuja solicitação de amaizade será negada.
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
		public Map<String, String[]> getFriends(String solicitorSession)
			throws Exception{

			Map<String, String[]> mapFriends = new TreeMap<String, String[]>();
			List<User> results = new ArrayList<User>(lendMe.getFriends(solicitorSession));
			Collections.sort(results,  new UserDateComparatorStrategy());
			Iterator<User> iterator = results.iterator();
			String[] atributtes;

			while(iterator.hasNext()){
				
				User next = iterator.next();
				
				atributtes = new String[3];
				atributtes[0] = next.getName();
				atributtes[1] = Integer.toString(next.getScore());
				atributtes[2] = next.getAddress().getFullAddress();
							
				mapFriends.put(next.getLogin(), atributtes);
			}
			
			return mapFriends;
		}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results logins
	 * @see com.lendme.LendMeFacade#getFriends(String)
	 */
	public Map<String, String[]> getFriends(String solicitorSession, String solicitedLogin)
		throws Exception{

		Map<String, String[]> mapFriends = new TreeMap<String, String[]>();
		List<User> results = new ArrayList<User>(lendMe.getFriends(solicitorSession, solicitedLogin));
		Collections.sort(results,  new UserDateComparatorStrategy());
		
		Iterator<User> iterator = results.iterator();
		String[] atributtes;
		
		while(iterator.hasNext()){

			User next = iterator.next();
			
			atributtes = new String[3];
			atributtes[0] = next.getName();
			atributtes[1] = Integer.toString(next.getScore());
			atributtes[2] = next.getAddress().getFullAddress();
						
			mapFriends.put(next.getLogin(), atributtes);
			
		}
		
		return mapFriends;
	}
	
	/**
	 * @param solicitorSession ID da sessão do usuário que verificará se 
	 * o outro usuário é seu amigo.
	 * @param solicitedLogin Login do usuário cuja amizade quer ser identificada.
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
	 * @param senderSession ID da sessão do usuário que enviará a menssagem.
	 * @param receiverLogin Login do usuário que receberá a menssagem.
	 * @param subject Assunto da menssagem.
	 * @param message Eh o texto da menssagem.
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
	 * @param senderSession ID da sessão do usuário que enviará a menssagem.
	 * @param receiverLogin Login do usuário que receberá a menssagem.
	 * @param subject Assunto da menssagem.
	 * @param message Eh o texto da menssagem.
	 * @param lendingId ID da requisição do emprestimo.
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
	 * @param creatorSession String que representa o ID da sessão
	 * do usuário em que se quer cadastrar o item. 
	 * @param name Nome do item que se quer cadastrar.
	 * @param description String que representa uma pequeno
	 * comentario sobre o Item.
	 * @param category Categora a que o Item está associado.
	 * O item pode ter mais de uma categoria.
	 * @return Retorna uma String que representa o ID do Item cadastrado
	 */	
	public String registerItem(String creatorSession, String name, String description, String category)
		throws Exception{
		
		return lendMe.registerItem(creatorSession, name, description, category);
	}

	/**
	 * 
	 * @param solicitorSession ID da sessão do usuário que deseja fazer a pesquisa.
	 * @param key String que será pesquisada nos itens.
	 * @param attribute atributo em que a chava será pesquisada.
	 * Atributos suportados: "DESCRICAO", "CATEGORIA", "ID" e "NOME".
	 * @param disposition Tipos de ordenação suportados: "CRESCENTE" E "DECRESCENTE";
	 * @param criteria Criterios suportados: "REPUTACAO" e "DATACRIACAO".
	 * @return Retorna um array com o nome de todos os itens encontrados na pesquisa.
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
	 * 
	 * @param solicitorSession ID da sessão do usuário que deseja fazer a pesquisa.
	 * @param key String que será pesquisada nos itens.
	 * @param attribute atributo em que a chava será pesquisada.
	 * Atributos suportados: "DESCRICAO", "CATEGORIA", "ID" e "NOME".
	 * @param disposition Tipos de ordenação suportados: "CRESCENTE" E "DECRESCENTE";
	 * @param criteria Criterios suportados: "REPUTACAO" e "DATACRIACAO".
	 * @return Retorna uma String com o nome de todos os itens encontrados na pesquisa.
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
	 * 
	 * @param solicitorSession ID da sessão do usuário solicitante.
	 * @return Retorna um array contendo o nome de todos os itens 
	 * do usuário solicitante.
	 * @throws Exception
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
	 * 
	 * @param solicitorSession ID da sessão do usuário solicitante.
	 * @return Retorna um array contendo o nome de todos os itens 
	 * do usuário solicitante.
	 * @throws Exception
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
	 * 
	 * @param solicitorSession ID da sessão do usuário que visualizará os amigos 
	 * do usuário solicitado.
	 * @param solicitedLogin Login do usuário solicitado.
	 * @return Retorna um array com o nome de todos os itens do usuário solicitado.
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
	 * @param itemId ID do Item.
	 * @param attribute Tipo de atributo do Item. 
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
	 * @param solicitorSession ID da sessão do usuário.
	 * @param itemId ID do Item a ser excluído.
	 */
	public void deleteItem(String solicitorSession, String itemId) throws Exception{
		
		lendMe.deleteItem(solicitorSession, itemId);
	}
	

	public void registerInterestForItem(String solicitorSession, String itemId) throws Exception{
		
		lendMe.registerInterestForItem(solicitorSession, itemId);
	}

	/**
	 * Requisita o empréstimo de um item.
	 * @param solicitorSession ID da sessão do usuário requisitante.
	 * @param itemId ID do intem que será requisitado.
	 * @param requiredDays Períodio estimado de duração do empréstimo.
	 * @param idItem Login do usuário cujo item será requisitado. 
	 * @return String que representa o ID da requisição.
	 */
	public String requestItem(String solicitorSession, String itemId, int requiredDays) throws Exception{
		
		return lendMe.requestItem(solicitorSession, itemId, requiredDays);
	}

	/**
	 * Aprova um empréstimo que foi feito ao usuário cujo ID da sessão foi 
	 * informado como parâmetro.
	 * @param solicitorSession ID da sessão do usuário.
	 * @param requestId ID da requisição do empréstimo.
	 * @return Retorna o ID do empréstimo que foi aprovado.
	 */
	public String approveLending(String solicitorSession, String requestId) throws Exception{
		
		return lendMe.approveLending(solicitorSession, requestId);
	}
	
	/**
	 * Nega uma requisição de empréstimo de item.
	 * @param solicitorSession ID da sessão do usuário que negará 
	 * a requisição de empréstimo.
	 * @param requestId Id da requisição de empréstimo.
	 * @return Retorna o ID da requisição de empréstimo negada.
	 * @throws Exception
	 */
	public String denyLending(String solicitorSession, String requestId) throws Exception{
		
		return lendMe.denyLending(solicitorSession, requestId);
	}
	

	/**
	 * Usuário dono do ID da sessão dado requisita a devolução
	 * de um item que pediu emprestado.
	 * @param solicitorSession ID da sessão do usuário.
	 * @param lendingId ID do empréstimo cuja devolução está sendo
	 * requisitada. 
	 */
	public String askForReturnOfItem(String solicitorSession, String lendingId) throws Exception{
		
		return lendMe.askForReturnOfItem(solicitorSession, lendingId);
	}

	/**
	 * Devolve um item que o usuário cujo ID da sessão foi dado 
	 * pegou emprestado.
	 * 
	 * @param solicitedSession ID da sessão do usuário logado no sistema.
	 * @param lendingId ID do empréstimo.
	 * @return Retorna o ID do emprestimo que foi devolvido. 
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
	 * @param solicitorSession ID da sessão do usuário
	 * @param lendingId ID do empréstimo que foi requisitada uma confirmação 
	 * de termino de emprestimo.
	 */
	public String denyLendingTermination(String solicitorSession, String lendingId) throws Exception{
		
		return lendMe.denyLendingTermination(solicitorSession, lendingId);
	}
	

	/**
	 * r
	 * @param solicitorSession ID da sessão do usuário solicitado.
	 * @return Retorna um array de String com todas reequisições de
	 * itens do usuário solicitado. 
	 * @throws Exception
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
	 * @param solicitorSession ID da sessão do usuário que deseja visualisar 
	 * o ranking.
	 * @param category Categoria que será usada como critério de ranqueamento.
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

	/**
	 * 
	 * @param solicitorSessionId ID da sassão do usuário que deseja 
	 * ver o histórico das atividades dos seu amigos.
	 * @return Retorna um array contendo o histórico de todos os
	 * amigos do usuário cujo ID da sessão foi passado como parâmetro. 
	 */
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

	/**
	 * 
	 * @param solicitorSessionId ID da sessão do usuário solicitado.
	 * @return Retorna um array com todas as publicações de requisição
	 * de itens feitas pelos amigos do usuário solicitado.
	 * @throws Exception
	 */
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
	 * @param sessionId ID da sessão do usuário que deseja publicar 
	 * o pedido de um item.
	 * @param itemName Nome do item cujo pedido será publicado.
	 * @param itemDescription Descrição do item cujo pedido será publicado. 
	 */
	public String publishItemRequest(String sessionId, String itemName,
			String itemDescription) throws Exception{
		return lendMe.publishItemRequest(sessionId, itemName, itemDescription);
	}

	/**
	 * Oferece um item .
	 * @param sessionId ID da sessão do usuário que deseja oferecer o item.
	 * @param requestPublicationId 
	 * @param itemId ID do item que será oferecido. 
	 */
	public void offerItem(String sessionId, String requestPublicationId,
			String itemId) throws Exception{
		lendMe.offerItem(sessionId, requestPublicationId, itemId);
	}

	/**
	 * Republica o pedido de um item.
	 * @param sessionId idSessao ID da sessão do usuário que deseja oferecer o item.
	 * @param requestPublicationId
	 */
	public void republishItemRequest(String sessionId, String requestPublicationId) 
		throws Exception{
		lendMe.republishItemRequest(sessionId, requestPublicationId);		
	}

	public void closeSystem() {
		lendMe.closeSystem();
	}

	public String getSessionInfo(String currentUserSessionId) throws Exception {
		return lendMe.getSessionInfo(currentUserSessionId);
	}
	
}