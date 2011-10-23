package com.lendme;

import java.util.HashSet;
import java.util.Set;

import com.lendme.entities.Session;
import com.lendme.entities.User;

public final class LendMeRepository {
	
	private Set<User> users = new HashSet<User>();
	private Set<Session> sessions = new HashSet<Session>();
	private Set<String> sessionsHystory = new HashSet<String>();
	private static LendMeRepository repository;
	
	private LendMeRepository() {}
	
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
	public String openSession(String login) throws Exception {
		if (userExists(login)) {
			Session session = new Session(getUserByLogin(login));
			sessions.add(session);
			return session.getId();
		} else {
			throw new Exception("Usuário inexistente");
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
	public String registerUser(String login, String name, String... address) throws Exception{
		User newUser = new User(login, name, address);
		if(!users.add(newUser)){
			throw new Exception("Já existe um usuário com este login");//"User with this login already exists");
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
		if ( id == null || id.trim().isEmpty() ){
			throw new Exception("Sessão inválida");//"Invalid session");
		}
		for(Session actualSession : sessions){
			if (actualSession.getId().equals(id)) {
				return actualSession;
			}
		}
		throw new Exception("Sessão inexistente");//"Inexistent session");
	}
	
	public boolean userExists(String login) throws Exception {
		if (login == null || login.trim().isEmpty()){
			throw new Exception("Login inválido");//"Invalid login");
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
		if ( login == null || login.trim().isEmpty() ){
			throw new Exception("Login inválido");//"Invalid login");
		}
		for(User actualUser : users){
			if(actualUser.getLogin().equals(login)){
				return actualUser;
			}
		}
		throw new Exception("Usuário inexistente");//"User does not exist");
	}
	
}


