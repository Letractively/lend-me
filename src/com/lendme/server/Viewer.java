package com.lendme.server;

import java.util.Set;

import com.lendme.server.entities.Item;
import com.lendme.server.entities.Session;
import com.lendme.server.entities.User;

/**
 * @author THE LENDERS
 * The permissions management is delegated from LendMe to this class, which is the one who deals with
 * concepts of logged users (observer) and target users (profile owners).
 */

public class Viewer {

	private Session observerSession;
	private User owner;
	private Set<User> ownerFriends;
	private Set<Item> ownerItems;
	
	private Viewer(Session observer, User user) throws Exception{

		if ( observer == null ){
			throw new Exception("Sessao do usuÃ¡rio observador invÃ¡lida");//"Invalid observer user");
		}
		if ( observer.getOwner() == null ){
			throw new Exception("UsuÃ¡rio observador invÃ¡lido");//"Invalid observer user login");
		}
		if ( user == null ){
			throw new Exception("UsuÃ¡rio observado invÃ¡lido");//"Invalid profile owner user");
		}
		if ( user.getFriends() == null ){
			throw new Exception("Amigos do observado invÃ¡lidos");//"Invalid profile owner user friends");
		}
		if ( user.getAllItems() == null ){
			throw new Exception("Itens do observado invÃ¡lidos");//"Invalid profile owner user items");
		}
		if ( user.getName() == null || user.getName().trim().length() == 0 ){
			throw new Exception("Nome do observado invÃ¡lido");//"Invalid profile owner user name");
		}
		if ( user.getLogin() == null || user.getLogin().trim().length() == 0 ){
			throw new Exception("Login do observado invÃ¡lido");//"Invalid profile owner user login");
		}
		if ( user.getAddress() == null || user.getAddress().getFullAddress() == null 
				|| user.getAddress().getFullAddress().trim().length() == 0 ){
			throw new Exception("EndereÃ§o do observado invÃ¡lido");//"Invalid profile owner user address");
		}
		
		observerSession = observer;
		owner = user;
		ownerFriends = owner.getFriends();
		if ( observerSession.getOwner().equals(owner) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.equals(observerSession.getOwner())){
					ownerItems = owner.getAllItems();
					return;
				}
			}
		}
	}
	
	/**
	 * Updates the profile information.
	 * @throws Exception
	 */
	protected void update() throws Exception{
		ownerFriends = owner.getFriends();
		if ( observerSession.getOwner().equals(owner) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.equals(observerSession.getOwner())){
					ownerItems = owner.getAllItems();
					return;
				}
			}
		}
	}
	
	/**
	 * Returns a profile for the specified user.
	 * @param userSession
	 * @param user
	 * @return
	 * @throws Exception
	 */
	protected static Viewer getUserProfile(Session userSession, User user) throws Exception{
		return new Viewer(userSession, user);
	}

	/**
	 * Returns the profile of the observer user.
	 * @return
	 * @throws Exception
	 */
	protected Viewer viewOwnProfile() throws Exception{
		return new Viewer(observerSession, observerSession.getOwner());
	}
	
	/**
	 * Returns the profile of an user.
	 * @param other
	 * @return
	 * @throws Exception
	 */
	protected Viewer viewOtherProfile(User other) throws Exception{
		if ( observerSession == null || other == null ){
			throw new Exception("Nao eh possivel visualizar este perfil");//"This profile is not accessible");
		}
		if ( observerSession.getOwner().equals(other) ){
			return viewOwnProfile();
		}
		return new Viewer(observerSession, other);
	}

	/**
	 * Returns the profile owner's friends.
	 * @return
	 */
	protected Set<User> getOwnerFriends() {
		return ownerFriends;
	}

	/**
	 * Returns the profile owner's items.
	 * @return
	 * @throws Exception
	 */
	protected Set<Item> getOwnerItems() throws Exception{
		if ( ownerItems == null ){
			throw new Exception("O usu�rio n�o tem permiss�o para visualizar estes itens");//"User has no permission to view these items");
		}
		return ownerItems;
	}
	
	/**
	 * Return the profile owner name.
	 * @return
	 */
	protected String getOwnerName(){
		return owner.getName();
	}

	/**
	 * Returns the profile owner login.
	 * @return
	 */
	protected String getOwnerLogin(){
		return owner.getLogin();
	}
	
	/**
	 * Returns the profile owner address.
	 * @return
	 */
	protected String getOwnerAddress(){
		return owner.getAddress().getFullAddress();
	}
	
	/**
	 * Returns the profile owner friendship requests.
	 * @return
	 */
	protected Set<User> getOwnerFriendshipRequests(){
		return owner.getReceivedFriendshipRequests();
	}

	/**
	 * Returns the session of the user who is observing this profile.
	 * @return
	 */
	protected Session getObserver() {
		return observerSession;
	}

	@Override
	public String toString() {
		StringBuilder profileSB = new StringBuilder();
		profileSB.append("\n \tUsuÃ¡rio:\n");
		profileSB.append("\n \t" + owner.toString() + "\n");
		
		profileSB.append("\n \tAmigos:\n");
		for (User friend: ownerFriends) {
			profileSB.append("\n \t" + friend.toString() + "\n\n");
		}
		profileSB.append("\n \tÃ�tens:\n");
		if (ownerItems != null ) {
			for (Item item: ownerItems) {
				profileSB.append("\n \t" + item.toString() + "\n\n");
			}
		} else {
			profileSB.append(" \tVocÃª nÃ£o pode visualizar Ã­tens de um usuÃ¡rio\n" +
					"\tque nÃ£o Ã© seu amigo.");
		}
		
		return profileSB.toString();
	}

	public User getOwner() {
		return owner;
	}
	
}