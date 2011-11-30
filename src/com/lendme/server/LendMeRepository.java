package com.lendme.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.lendme.server.entities.Item;
import com.lendme.server.entities.Session;
import com.lendme.server.entities.User;
import com.lendme.server.utils.AddressComparatorStrategy;
import com.lendme.server.utils.UserDateComparatorStrategy;

public final class LendMeRepository {
	
	private Set<User> users = new HashSet<User>();
	private Set<Session> sessions = new HashSet<Session>();
	private Set<String> sessionsHystory = new HashSet<String>();
	private static LendMeRepository repository;
	
	public static LendMeRepository getInstance() {
		if (repository == null) {
			repository = new LendMeRepository();
		}
		
		return repository;
	}
	
	public Set<User> getUsers() {
		return new HashSet<User>(users);
	}
	
	public Set<Session> getSessions() {
		return new HashSet<Session>(sessions);
	}
	
	
	public boolean addUser(User newUser) {
		return users.add(newUser);
	}
	
	/**
	 * Resets the whole system: all living sessions are shutdown as well as all users are deleted.
	 * Also, the system clock is set to same as the OS time.
	 * 
	 * <i>This method belongs to the public system interface</i>
	 */
	public void resetRepository() {
		users = new HashSet<User>();
		sessions = new HashSet<Session>();
	}
	
	/**
	 * Opens a new session for the user specified by login.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param login the user login
	 * @return the session id
	 * @throws Exception for invalid parameters and if user doesn't exists
	 */
	public synchronized String openSession(String login) throws Exception {
		if (userExists(login)) {
			Session session = new Session(getUserByLogin(login));
			sessions.add(session);
			return session.getId();
		} else {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		    Query query = new Query("User").addSort("id", Query.SortDirection.DESCENDING);
		    Iterator<Entity> users = datastore.prepare(query).asIterator();
		    while ( users.hasNext() ){
		    	Entity user = users.next();
		    	if ( (""+user.getProperty("id")).equals(login) ){
		    		String name = ((String) user.getProperty("name")); 
		    		String address = ((String) user.getProperty("address"));
		    		String email = ((String) user.getProperty("email"));
					registerUser(login, name, email, address);
					Session session = new Session(getUserByLogin(login));
					sessions.add(session);
					populateSystem();
					return session.getId();
		    	}
		    }
	    	throw new Exception("Usu·rio inexistente");
		}
	}
	
	private void populateSystem() throws Exception{
		//Method only used to register some itens and users for system evaluation purposes
		String[] names = { "Joao", "Carlos", "Janaina", "Henrique", "Laura", "Monica", "Augusto", "Melina", "Reinaldo" };
		String[] addresses = {"Av. Mar Baltico, Intermares, Cabedelo", "Av. Oceano Pacifico, Intermares, Cabedelo",
				"Av. Mar Vermelho, Intermares, Cabedelo", "R. Joaquim Caroca, BodocongÛ, Campina Grande",
				"R. Francisco Rosa de Farias, Monte Santo, Campina Grande", "R. Oleg·rio Maciel, Monte Santo, Campina Grande",
				"Av. Brasilia, CatolÈ, Campina Grande", "R. Montevideo, Prata, Campina Grande", "Av. Epit·cio Pessoa, Centro, Jo„o Pessoa",
				"22B Baker Street, Londres"};
		String[] items = { "Star Wars - The Phantom Menace", "Star Wars - Attack of the Clones", 
				"Star Wars - Revenge of the Sith", "Star Wars - A New Hope", 
				"Star Wars - The Empire Strikes Back", "Star Wars - Return of the Jedi",
				"LOST - 1™ Temporada", "LOST - 2™ Temporada", "Artemis Fowl - O menino prodigio do crime",
				"BÌblia Sagrada", "O Monge e o Executivo", "O Poderoso Chef„o", "Artemis Fowl - Paradoxo do tempo",
				"Bola de Futebol", "Bola de Volei", "Artemis Fowl - Aventura no artico", "Guia do mochileiro das gal·xias",
				"Artemis Fowl - O codigo eterno", "Artemis Fowl - Vinganca de Opala", "Harry Potter"};
		String[] categories = { "Filme", "Filme", "Filme", "Filme", "Filme", "Filme", "SÈrie", "SÈrie", "Livro",
				"Livro", "Livro", "Livro", "Livro", "Livro", "Livro", "Livro", "Livro", "Livro", "Artigo Esportivo", "Artigo Esportivo",
				"Filme", "Filme", "Filme", "Filme"};
		List<String> itemIds = new ArrayList<String>();
		List<String> sessionIds = new ArrayList<String>();
		for ( int i = 0; i < 10; i++ ){
			registerUser(names[i].toLowerCase(), names[i], names[i]+"@lendme.com", addresses[i]);
			String session = openSession(names[i].toLowerCase());
			String itemId = registerItem(session, items[i], items[i], categories[i]);
			String itemId2 = registerItem(session, items[i+1], items[i+1], categories[i+1]);
			itemIds.add(itemId);
			itemIds.add(itemId2);
			sessionIds.add(session);
		}
		for ( int j = 0; j<10; j++ ){
			User user = getUserBySessionId(sessionIds.get(j));
			User user2 = getUserBySessionId(sessionIds.get(j+1));
			user.requestFriendship(user2);
			user2.acceptFriendshipRequest(user);
			for ( Item item : user.getAllItems() ){
				String lending = user2.borrowItem(item, user, j+1);
				user.approveLending(lending);
			}
			for ( Item item : user2.getAllItems() ){
				String lending = user.borrowItem(item, user2, j+1);
				user2.approveLending(lending);
			}
		}
	}

	/**
	 * Closes the specified session.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param id the session id
	 * @throws Exception for inexistent sessions
	 */
	public void closeSession(String id) throws Exception{
		Session toBeFinished = getSessionByID(id);
		toBeFinished.finishSession();
		sessions.remove(toBeFinished);
		sessionsHystory.add(toBeFinished.toString());
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
	public String registerUser(String login, String name, String email, String... address) throws Exception{
		User newUser = new User(login, name, email, address);
		if(!users.add(newUser)){
			throw new Exception("J√° existe um usu√°rio com este login");//"User with this login already exists");
		}
		return newUser.getLogin();
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
	public String registerItem(String sessionId, String name, 
			String description, String category) throws Exception{

		User owner = getSessionByID(sessionId).getOwner();
		return owner.addItem(name, description, category);
	}
	
	public User getUserBySessionId(String sessionId) {
		User ownerOfSession = null;
		
		for(Session actualSession : sessions){
			if(actualSession.getId().equals(sessionId)){
				ownerOfSession = actualSession.getOwner();
				break;
			}
		}
		return ownerOfSession;
	}
	
	/**
	 * Searches for sessions with given login
	 * @param login the login
	 * @return a set of sessions found by search
	 */
	public Set<Session> searchSessionsByLogin(String login) {
		Set<Session> results = new HashSet<Session>();
		for(Session actualSession : sessions){
			if(actualSession.getOwner().getLogin().equals(login)){
				results.add(actualSession);
			}
		}
		return results;
	}
	
	/**
	 * Returns the session that have the specified id
	 * @param id the session id
	 * @return the session
	 * @throws Exception if session doesn't exists
	 */
	public  Session getSessionByID(String id) throws Exception{
		if ( id == null || id.trim().length() == 0 ){
			throw new Exception("Sess√£o inv√°lida");//"Invalid session");
		}
		for(Session actualSession : sessions){
			if (actualSession.getId().equals(id)) {
				return actualSession;
			}
		}
		throw new Exception("Sess√£o inexistente");//"Inexistent session");
	}
	
	public boolean userExists(String login) throws Exception {
		if (login == null || login.trim().length() == 0){
			throw new Exception("Login inv√°lido");//"Invalid login");
		}
		
		for (User user : users) {
			if (user.getLogin().equals(login)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns user with given login.
	 * @param login the login
	 * @return the user
	 * @throws Exception if login is invalid or user with given login doesn't exists
	 */
	public User getUserByLogin(String login) throws Exception{
		if ( login == null || login.trim().length() == 0 ){
			throw new Exception("Login inv√°lido");//"Invalid login");
		}
		for(User actualUser : users){
			if(actualUser.getLogin().equals(login)){
				return actualUser;
			}
		}
		throw new Exception("Usu√°rio inexistente");//"User does not exist");
	}

	/**
	 * Retorna um List<User> com todos os usu√°rios cadastrados
	 * no sistema ordenados pela dist√¢ncia entre cada um e o usu√°rio dono 
	 * do ID da sess√£o dada. 
	 */
	public List<User> listUsersByDistance(String sessionId) throws Exception{
		if(sessionId == null || sessionId.trim().equals("")){
			throw new Exception("Sess√£o inv√°lida");
		}
				
		
		List<User> listUsersByDistance = new ArrayList<User>();
		listUsersByDistance.addAll(getUsers());
		Collections.sort(listUsersByDistance, new UserDateComparatorStrategy());
		
		User ownerOfSession = getUserBySessionId(sessionId);
		
		if(ownerOfSession == null){
			throw new Exception("Sess√£o inexistente");
		}
		
		listUsersByDistance.remove(ownerOfSession);
		Collections.sort(listUsersByDistance, new AddressComparatorStrategy(ownerOfSession.getAddress()));
		return listUsersByDistance;
	}

	/**
	 * Pesquisa por todos os usu√°rios cujo atributo pesquisado nele
	 * possue a chave dada.
	 * 
	 * @param sessionId ID da sess√£o do usu√°rio que far√° a pesuisa.
	 * @param key Os usu√°rios que possuem essa String no atributo informado 
	 * ser√£o retornados. 
	 * @param attribute Atributo pelo qual se quer fazer a pesquisa.
	 */

	public Set<User> searchUsersByAttributeKey(String sessionId, String key,
			String attribute) throws Exception{
		if ( attribute == null || attribute.trim().length() == 0 ){
			throw new Exception("Atributo inv·lido");//"Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.trim().equals("login") || attribute.trim().equals("endereco"))){
			throw new Exception("Atributo inexistente");//"Inexistent attribute");
		}
		if ( key == null || key.trim().length() == 0 ){
			throw new Exception("Palavra-chave inv·lida");//"Invalid search key");
		}
		
		Set<User> results = new HashSet<User>();
		for ( User user : repository.getUsers() ){
			if ( repository.getSessionByID(sessionId).getOwner().equals(user) ){
				continue;
			}
			if ( attribute.equals("nome") ){
				if ( getUserAttribute(user, "nome").toLowerCase().contains(key.toLowerCase()) ){
					results.add(user);
				}
			}
			else if ( attribute.equals("login") ){
				if ( ( getUserAttribute(user, "login").toLowerCase().contains(key.toLowerCase()) ) ){
					results.add(user);
				}
			}
			else if ( attribute.equals("endereco") ){
				if ( ( getUserAttribute(user, "endereco").toLowerCase().contains(key.toLowerCase()) ) ){
					results.add(user);
				}
			}
		}
		return results;
	}
	
	public String getUserAttribute(User user, String attribute)
			throws Exception {

		if (attribute == null || attribute.trim().length() == 0) {
			throw new Exception("Atributo inv√°lido");// "Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.equals("endereco") || attribute
				.equals("login"))) {
			throw new Exception("Atributo inexistente");// "Inexistent attribute");
		}

		if (attribute.equals("nome")) {
			return user.getName();
		} else if (attribute.equals("login")) {
			return user.getLogin();
		} else {
			return user.getAddress().toString();
		}
		
	}
	
	public  String getUserAttribute(String login, String attribute)
	throws Exception{
		return getUserAttribute(repository.getUserByLogin(login), attribute);
	}

}