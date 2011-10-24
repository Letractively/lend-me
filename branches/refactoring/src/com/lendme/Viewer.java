package com.lendme;

import java.util.Set;

import com.lendme.entities.Item;
import com.lendme.entities.Session;
import com.lendme.entities.User;

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
			throw new Exception("Sessao do usuário observador inválida");//"Invalid observer user");
		}
		if ( observer.getOwner() == null ){
			throw new Exception("Usuário observador inválido");//"Invalid observer user login");
		}
		if ( user == null ){
			throw new Exception("Usuário observado inválido");//"Invalid profile owner user");
		}
		if ( user.getFriends() == null ){
			throw new Exception("Amigos do observado inválidos");//"Invalid profile owner user friends");
		}
		if ( user.getAllItems() == null ){
			throw new Exception("Itens do observado inválidos");//"Invalid profile owner user items");
		}
		if ( user.getName() == null || user.getName().trim().isEmpty() ){
			throw new Exception("Nome do observado inválido");//"Invalid profile owner user name");
		}
		if ( user.getLogin() == null || user.getLogin().trim().isEmpty() ){
			throw new Exception("Login do observado inválido");//"Invalid profile owner user login");
		}
		if ( user.getAddress() == null || user.getAddress().getFullAddress() == null 
				|| user.getAddress().getFullAddress().trim().isEmpty() ){
			throw new Exception("Endereço do observado inválido");//"Invalid profile owner user address");
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
			throw new Exception("O usuário não tem permissão para visualizar estes itens");//"User has no permission to view these items");
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
		profileSB.append("\n \tUsuário:\n");
		profileSB.append("\n \t" + owner.toString() + "\n");
		
		profileSB.append("\n \tAmigos:\n");
		for (User friend: ownerFriends) {
			profileSB.append("\n \t" + friend.toString() + "\n\n");
		}
		profileSB.append("\n \tÍtens:\n");
		if (ownerItems != null ) {
			for (Item item: ownerItems) {
				profileSB.append("\n \t" + item.toString() + "\n\n");
			}
		} else {
			profileSB.append(" \tVocê não pode visualizar ítens de um usuário\n" +
					"\tque não é seu amigo.");
		}
		
		return profileSB.toString();
	}

	public User getOwner() {
		return owner;
	}
	
}