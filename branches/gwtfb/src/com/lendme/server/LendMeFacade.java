package com.lendme.server;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lendme.server.entities.ActivityRegistry;
import com.lendme.server.entities.Item;
import com.lendme.server.entities.Lending;
import com.lendme.server.entities.Message;
import com.lendme.server.entities.Session;
import com.lendme.server.entities.Topic;
import com.lendme.server.entities.User;

/**
 * @author THE LENDERS
 * The System.
 * Here is where the created Users and open Sessions are located. It also keeps track of the system time.
 * In fact, it contains most of the Business Logic, and is the access point to User and Profile logic.
 */

public class LendMeFacade {

//	private TimeMonitor time;
	private LendMeRepository repository;
	private LendMeItemModule itemModule;
	private LendMeCommunicationModule  communicationModule; 
	private LendMeUserModule userModule;

	// global instance
	private static LendMeFacade self;
	
	public synchronized static LendMeFacade getInstance(){
		if ( self == null ){
			self = new LendMeFacade();
		}
		return self;
	}
	
	private LendMeFacade() {
		//time = TimeMonitor.getInstance();
		repository = LendMeRepository.getInstance();
		userModule = new LendMeUserModule();
		itemModule = new LendMeItemModule();
		communicationModule = new LendMeCommunicationModule();
	}
	
	/**
	 * Resets the whole system: all living sessions are shutdown as well as all users are deleted.
	 * Also, the system clock is set to same as the OS time.
	 * 
	 * <i>This method belongs to the public system interface</i>
	 */
	public void resetSystem(){
		repository.resetRepository();
//		time = TimeMonitor.getInstance();
	}
	
	/**
	 * Opens a new session for the user specified by login.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param login the user login
	 * @param name 
	 * @return the session id
	 * @throws Exception for invalid parameters and if user doesn't exists
	 */
	public String openSession(String login) throws Exception {
		return repository.openSession(login);
	}
	
	/**
	 * Returns the system date.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @return the system time
	 */
	public Date getSystemDate() {
//		return time.getTime();
		return null;
	}
	
	/**
	 * Simulates that a specified amount of days have passed.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param amount the amount of days
	 * @return the string representing the new system time
	 */
	public String someDaysPassed(int amount){
//		time.addTime(Calendar.DAY_OF_MONTH, amount+1);
//		return time.getTime().toString();
		return null;
	}
	
	/**
	 * Closes the specified session.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param id the session id
	 * @throws Exception for inexistent sessions
	 */
	public void closeSession(String id) throws Exception{
		repository.closeSession(id);
	}
	
	/**
	 * Registers a new user in the system.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param login the user login
	 * @param name the user name
	 * @param address the user address
	 * @throws Exception for invalid parameters or if a user with login already exists
	 */
	public String registerUser(String login, String name, String... address) throws Exception{
		return repository.registerUser(login, name, address);
	}
	
	/**
	 * Registers a new item in the system.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param sessionId the owner of the item session id
	 * @param name the item name
	 * @param description the item description
	 * @param category the item category
	 * @return
	 * @throws Exception for invalid parameters
	 */
	public  String registerItem(String sessionId, String name, 
			String description, String category) throws Exception{

		return repository.registerItem(sessionId, name, description, category);
	}
	
	/**
	 * Retorna um conjunto de usuários cadastrados no sistema ordenados
	 * pela distância entre cada um e o usuáio dono 
	 * do ID da sessão dada. 
	 * @param sessionId ID da sessão do usuário logado.
	 */
	public  List<User> listUsersByDistance(String sessionId) throws Exception{
		return repository.listUsersByDistance(sessionId);
	}

	/**
	 * Pesquisa por todos os usuários cujo atributo pesquisado nele
	 * possue a chave dada.
	 * 
	 * @param sessionId ID da sessão do usuário que fará a pesuisa.
	 * @param key Os usuários que possuem essa String no atributo informado 
	 * serão retornados. 
	 * @param attribute Atributo pelo qual se quer fazer a pesquisa.
	 */
	public  Set<User> searchUsersByAttributeKey(String sessionId,
			String key, String attribute) throws Exception{
		return repository.searchUsersByAttributeKey(sessionId, key, attribute);
	}

	/**
	 * Returns some item attribute.
	 * @param itemId the item id
	 * @param attribute the user attribute
	 * @return the item attribute value
	 * @throws Exception if parameters are invalid
	 */
	public  String getItemAttribute(String itemId, String attribute) throws Exception{
		
		if ( repository.getSessions().isEmpty() ){
			throw new Exception("Não há sessão disponível para obter essas informações.");
		}
		
		String ownerSessionId = repository.searchSessionsByLogin(getItemOwner(itemId).getLogin())
				.iterator().next().getId();
		Viewer viewer = getUserProfile(ownerSessionId);
		return itemModule.getItemAttribute(viewer, itemId, attribute);
	}

	/**
	 * Retrieves a profile for user with existing session specified by session id.
	 * @param sessionId the session id
	 * @return a profile
	 * @throws Exception if user doesn't exists or there is no alive session for that user
	 */
	public  Viewer getUserProfile(String sessionId) throws Exception {
		Session session = repository.getSessionByID(sessionId);
		return userModule.getUserProfile(session);
	}

	/**
	 * Returns the friends of user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of friends
	 * @throws Exception if user doesn't exists
	 */
	public  Set<User> getFriends(String sessionId) throws Exception{
		return userModule.getFriends(getUserProfile(sessionId));
	}

	/**
	 * Returns the friends of another user if a session specified by solicitor session id exists.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the session id
	 * @param solicitedLogin the user login whose friends are being required
	 * @return a set of friends
	 * @throws Exception if one of the users involved doesn't exists
	 */
	public  Set<User> getFriends(String solicitorSessionId, String solicitedLogin) throws Exception{
		return userModule.getFriends(getUserProfile(solicitorSessionId), repository.getUserByLogin(solicitedLogin));
	}

	/**
	 * Returns the items of the user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of items
	 * @throws Exception if user doesn't exists
	 */
	public  Set<Item> getItems(String sessionId) throws Exception {
		Viewer viewer = getUserProfile(sessionId);
		return itemModule.getItems(viewer);
	}

	/**
	 * Returns the items of user specified by login if solicitor user has existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param observerSessionId the solicitor session id
	 * @param ownerLogin the solicited user login
	 * @return a set of items of the solicited user
	 * @throws Exception if users involved doesn't exists or solicitor user has no permission to access solicited items
	 */
	public  Set<Item> getItems(String observerSessionId, String ownerLogin) throws Exception {
		Viewer viewer = getUserProfile(observerSessionId);
		viewer = getAnotherProfile(viewer, ownerLogin);
		return itemModule.getItems(viewer);
	}

	public Viewer getAnotherProfile(Viewer viewer, String anotherUser)
			throws Exception {
		return userModule.viewProfile(viewer, repository.getUserByLogin(anotherUser));
	}
	
	/**
	 * Solicitor user sends a friendship request to solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the solicitor session id
	 * @param solicitedLogin the solicited login
	 * @throws Exception if users involved doesn't exists or friendship request was already sent
	 */
	public  void askForFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		User sessionOwner = getUserProfile(solicitorSessionId).getObserver().getOwner();
		userModule.askForFriendship(sessionOwner, repository.getUserByLogin(solicitedLogin));
	}

	/**
	 * Solicited user accepts friendship request sent by solicitor user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitedSessionId the solicited session id
	 * @param solicitorLogin the solicitor login
	 * @throws Exception if users involved doesn't exists or friendship request was already accepted
	 */
	public  void acceptFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		User sessionOwner = getUserProfile(solicitedSessionId).getObserver().getOwner();
		userModule.acceptFriendship(sessionOwner, repository.getUserByLogin(solicitorLogin));
	}
	
	/**
	 * Solicited user declines friendship request sent by solicitor user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitedSessionId the solicited session id
	 * @param solicitorLogin the solicitor login
	 * @throws Exception if users involved doesn't exists or if solicited user already declined request
	 */
	public  void declineFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		User sessionOwner = getUserProfile(solicitedSessionId).getObserver().getOwner();
		userModule.declineFriendship(sessionOwner, repository.getUserByLogin(solicitorLogin));
	}
	
	/**
	 * Solicitor user breaks friendship with solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the solicitor session id
	 * @param solicitedLogin the solicited login
	 * @throws Exception if users involved doesn't exists
	 */
	public  void breakFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		User sessionOwner = getUserProfile(solicitorSessionId).getObserver().getOwner();
		userModule.breakFriendship(sessionOwner, repository.getUserByLogin(solicitedLogin));
	}

	/**
	 * Returns true if solicitor user is friend of solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the solicitor session id
	 * @param solicitedUserLogin the solicited login
	 * @return true if users involved are friends
	 * @throws Exception if users involved doesn't exists or are not friends
	 */
	public  boolean hasFriend(String solicitorSessionId, String solicitedLogin) throws Exception{
		User sessionOwner = getUserProfile(solicitorSessionId).getObserver().getOwner();
		return userModule.hasFriend(sessionOwner, repository.getUserByLogin(solicitedLogin));
	}

	/**
	 * @param sessionId  ID da sessão.
	 * @return Retorna um Set com todos os usuários
	 * que requisitaram amizade com o usuário cujo ID da sessão foi dado. 
	 */
	public  Set<User> getFriendshipRequests(String sessionId) throws Exception{
		return userModule.getOwnerFriendshipRequests(getUserProfile(sessionId));
	}

	/**
	 * Permite o envio de uma menssagem entre dois usuários.
	 * @param senderSessionId ID da sessão do usuário que vai
	 * enviar uma menssagem.
	 * @param subject Assunto da menssagem.
	 * @param message Corpo da menssagem.
	 * @param receiverLogin Login do usuário qu ereceberá a menssagem.
	 * @param lendingId ID da requisição de um pedido de um Item.
	 * @return 
	 */
	public  String sendMessage(String senderSessionId, String subject, String message, 
			String receiverLogin, String lendingId) throws Exception {
		
		Viewer senderProfile = getUserProfile(senderSessionId);
		User sender = null;
		User receiver = null;
		try {
			receiver = repository.getUserByLogin(receiverLogin);
			sender = getAnotherProfile(senderProfile, receiverLogin).getObserver().getOwner();
			if (message == null || message.trim().length() == 0) {
				throw new Exception("Mensagem inválida");//"Invalid message");
			}
			
			if (subject == null || subject.trim().length() == 0) {
				throw new Exception("Assunto inválido");//"Invalid subject");
			}
			Lending lending = getLendingByLendingId(lendingId);
			return communicationModule.sendMessage(sender, subject, message, receiver, lending);
		} catch (Exception e) {
			
			if (e.getMessage().equals("Login inválido")) {
				throw new Exception("Destinatário inválido");//"Invalid receiver");
			}
			else if (e.getMessage().equals("Usuário inexistente")) {
				throw new Exception("Destinatário inexistente");//"Inexistent receiver");
			}
			else if (e.getMessage().equals("Identificador do empréstimo é inválido")) {
				throw new Exception("Identificador da requisição de empréstimo é inválido");
			}
			else if (e.getMessage().equals("Empréstimo inexistente") ){
				throw new Exception("Requisição de empréstimo inexistente");
			}
			else
			{
				throw new Exception(e.getMessage());
			}
		}
	}
	
	
	/**
	 * Permite o envio de uma menssagem entre dois usuários.
	 * 
	 * @param senderSessionId ID da sessão do usuário que enviará a menssagem.
	 * @param subject Assunto da menssagem.
	 * @param message Corpo da menssagem.
	 * @param receiverLogin Login do usuário que receberá a menssagem.
	 * @return 
	 */
	public  String sendMessage(String senderSessionId, String subject, String message, 
			String receiverLogin) throws Exception {
		
		Viewer senderProfile = getUserProfile(senderSessionId);
		User sender = null;
		User receiver = null;
		try {
			receiver = repository.getUserByLogin(receiverLogin);
			sender = getAnotherProfile(senderProfile, receiverLogin).getObserver().getOwner();
		} catch (Exception e) {
			
			if (e.getMessage().equals("Login inválido")) {
				throw new Exception("Destinatário inválido");//"Invalid receiver");
			}
				
			else if (e.getMessage().equals("Usuário inexistente")) {
				throw new Exception("Destinatário inexistente");//"Inexistent receiver");
			}else{
				throw new Exception(e.getMessage());
			}
		}
		return communicationModule.sendMessage(sender, subject, message, receiver);
	}
	
	/**
	 * Returns user with session specified by session id message topics.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param topicType
	 * @return
	 * @throws Exception
	 */
	public  List<Topic> getTopics(String sessionId, String topicType)
			throws Exception {
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return communicationModule.getTopics(sessionOwner, topicType);
	}
	
	/**
	 * Returns user with session specified by sesison id topic messages.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	public  List<Message> getTopicMessages(String sessionId, String topicId)
			throws Exception {
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return communicationModule.getTopicMessages(sessionOwner, topicId, repository.getUsers());
	}
	
	/**
	 * Returns user lending history.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	public  Collection<Lending> searchForLendingRecords(String sessionId, String kind) throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.getLendingRecords(sessionOwner, kind);
	}
	
	public Collection<Lending> searchForLendingRecords(String sessionId) throws Exception{
		return searchForLendingRecords(sessionId, "todos");
	}
	
	/**
	 * Solicited user requests an item from another user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @param requiredDays
	 * @return
	 * @throws Exception
	 */
	public  String requestItem(String sessionId, String itemId, int requiredDays) throws Exception {
		Viewer viewer = getUserProfile(sessionId);
		User itemOwner = getItemOwner(itemId);
		viewer = getAnotherProfile(viewer, itemOwner.getLogin());
		return itemModule.requestItem(viewer, itemId, requiredDays, repository.getUsers());
	}
	
	/**
	 * Aprova um empréstimo que foi feito ao usuário cujo ID da sessão foi 
	 * informado como parâmetro.
	 * @param sessionId ID da sessão do usuário.
	 * @param requestId ID da requisição do empréstimo.
	 * @return Retorna o ID do empréstimo que foi aprovado.
	 */
	public  String approveLending(String sessionId, String requestId)  throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.approveLending(sessionOwner, getLendingByRequestId(requestId));
	}

	/**
	 * Solicited user denies lending of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public  String denyLending(String sessionId, String requestId)  throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.denyLending(sessionOwner, getLendingByRequestId(requestId));
	}
	
	/**
	 * Devolve um item que o usuário cujo ID da sessão foi dado 
	 * pegou emprestado.
	 * 
	 * @param sessionId ID da sessão do usuário logado no sistema.
	 * @return Retorna o ID do emprestimo que foi devolvido. 
	 * @param lendingId ID do empréstimo.
	 */
	public  String returnItem(String sessionId, String lendingId) throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.returnItem(sessionOwner, getLendingByLendingId(lendingId));
	}
	
	/**
	 * Confirma o termino de um emprestimo feito pelo usupario dono do ID da sessão dada.
	 */
	public  String confirmLendingTermination(String sessionId,
			String lendingId) throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.confirmLendingTermination(sessionOwner, getLendingByLendingId(lendingId));
	}

	/**
	 * Usuário dono do ID da sessão dada nega
	 * o término de um empréstimo que foi solicitado.
	 *  
	 * @param sessionId ID da sessão do usuário
	 * @param lendingId ID do empréstimo que foi requisitada uma confirmação 
	 * de termino de emprestimo.
	 */

	public  String denyLendingTermination(String sessionId,
			String lendingId) throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.denyLendingTermination(sessionOwner, getLendingByLendingId(lendingId));
	}
	
	/**
	 * Usuário dono do ID da sessão dado requisita a devolução
	 * de um item que pediu emprestado.
	 * @param sessionId ID da sessão do usuário.
	 * @param lendingId ID do empréstimo cuja devolução está sendo
	 * requisitada. 
	 */
	public  String askForReturnOfItem(String sessionId,
			String lendingId) throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.askForReturnOfItem(sessionOwner, getLendingByLendingId(lendingId), getSystemDate());
	}
	
	/**
	 * Returns the owner of the item.
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public User getItemOwner(String itemId) throws Exception{
		return itemModule.getItemOwner(itemId, repository.getUsers());
	}
	
	/**
	 * Returns the lending record with specific lending id.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public  Lending getLendingByLendingId(String lendingId) throws Exception{
		return itemModule.getLendingByLendingId(lendingId, repository.getUsers());		
	}
	
	/**
	 * Returns lending record with specific request id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public  Lending getLendingByRequestId(String requestId) throws Exception{
		return itemModule.getLendingByRequestId(requestId, repository.getUsers());
	}

	/**
	 * Solicitor removes item from his item set.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @throws Exception
	 */
	public  void deleteItem(String sessionId, String itemId) throws Exception {
		User ownerUser = repository.getSessionByID(sessionId).getOwner();
		itemModule.deleteItem(ownerUser, itemId);
	}

	/**
	 * Searches for specific with specific criteria and disposals.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param key
	 * @param attribute
	 * @param disposal
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	public  List<Item> searchForItem(String sessionId, String key, String attribute,
			String disposal ,String criteria) throws Exception{
		User ownerUser = repository.getSessionByID(sessionId).getOwner();
		return itemModule.searchForItem(ownerUser, key, attribute, disposal, criteria);
	}
	
	/**
	 * Solicitor registers interest for a specific item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @throws Exception
	 */
	public  void registerInterestForItem(String sessionId, String itemId) throws Exception{
		Viewer viewer = getUserProfile(sessionId);
		viewer = userModule.viewProfile(viewer, itemModule.getItemOwner(itemId, repository.getUsers()));
		itemModule.registerInterestForItem(viewer, itemId);
	}

	/**
	 * Returns messages with given topic.
	 * 
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	public  List<Message> getMessagesByTopicId(String topicId) throws Exception{
			return communicationModule.getMessagesByTopicId(topicId, repository.getUsers());

	}
	
	/**
	 * 
	 * @param sessionId ID da sessão do usuário que deseja visualisar 
	 * o ranking.
	 * @param category Categoria que será usada como critério de ranqueamento.
	 * @return Retorna uma String com o nome de todos os usuários
	 * ranqueados. 
	 */
	public  String getRanking(String sessionId, String category) throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.getRanking(category, sessionOwner, repository.getUsers());
	}
	
	/**
	 * Logged user receives access to some user info through his profile.
	 * 
	 * @param solicitorSessionId
	 * @param solicitedUserLogin
	 * @return
	 * @throws Exception
	 */
	public  String viewProfile(String solicitorSessionId, 
			String solicitedUserLogin) throws Exception {
		Viewer solicitorViewer = getUserProfile(solicitorSessionId);
		return getAnotherProfile(solicitorViewer, solicitedUserLogin).toString();
	}
	
	/**
	 * Returns the user received item requests.
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public  Set<Lending> getReceivedItemRequests(String sessionId)
			throws Exception {
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return itemModule.getReceivedItemRequests(sessionOwner);
	}
	
	/**
	 * @return Retorna o histórico de atividades do usuário dono do ID da sessaõ dado.
	 */
	public  List<ActivityRegistry> getActivityHistory(String solicitorSessionId) throws Exception {
		User sessionOwner = getUserProfile(solicitorSessionId).getObserver().getOwner();
		return communicationModule.getActivityHistory(sessionOwner);
	}

	public List<ActivityRegistry> getJointActivityHistory(
			String solicitorSessionId) throws Exception {
		
		User sessionOwner = getUserProfile(solicitorSessionId).getObserver().getOwner();
		return communicationModule.getJointActivityHistory(sessionOwner);
	}

	public String publishItemRequest(String sessionId, String itemName,
			String itemDescription) throws Exception {
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		return communicationModule.publishItemRequest(sessionOwner, itemName, itemDescription);
	}

	public void offerItem(String sessionId,
			String requestPublicationId, String itemId) throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		communicationModule.offerItem(sessionOwner, requestPublicationId, itemId, repository.getUsers());
	}

	public void republishItemRequest(String sessionId,
			String requestPublicationId) throws Exception{
		User sessionOwner = getUserProfile(sessionId).getObserver().getOwner();
		communicationModule.republishItemRequest(sessionOwner, requestPublicationId, repository.getUsers());
	}

	public String getUserAttribute(String login, String attribute) throws Exception {
		return repository.getUserAttribute(login, attribute);
	}
	
	public List<Lending> getFriendsPublishedItemRequests(String solicitorSessionId) throws Exception {
		Viewer viewer = userModule.getUserProfile(repository.getSessionByID(solicitorSessionId));
		return communicationModule.getFriendsPublishedItemRequests(userModule.getFriends(viewer)); 
	}

	public void closeSystem() {
//		time.stop();
		return;
	}

	public String getSessionInfo(String currentUserSessionId) throws Exception {
		if ( currentUserSessionId == null || currentUserSessionId.trim().length() == 0 ){
			return "Nenhum usuario logado - Data: "+getSystemDate();
		}
		StringBuilder info = new StringBuilder();
		String login = repository.getUserBySessionId(currentUserSessionId).getLogin();
		info.append("Login: "+login);
		info.append("\nNome: "+repository.getUserAttribute(login, "nome"));
		info.append("\nEndereco: "+repository.getUserAttribute(login, "endereco"));
		info.append("\n - ");
		info.append("\nData: "+getSystemDate());
		return new String(info);
	}
	
}