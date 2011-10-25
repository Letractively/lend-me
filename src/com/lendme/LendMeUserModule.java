package com.lendme;

import java.util.Set;

import com.lendme.entities.Session;
import com.lendme.entities.User;

public class LendMeUserModule {

	/**
	 * Retrieves a profile for user with existing session specified by session id.
	 * @param sessionId the session id
	 * @return a profile
	 * @throws Exception if user doesn't exists or there is no alive session for that user
	 */
	public  Viewer getUserProfile(Session session) throws Exception {
		return Viewer.getUserProfile(session, session.getOwner());
	}

	/**
	 * Returns the friends of user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @return a set of friends
	 * @throws Exception if user doesn't exists
	 */
	public Set<User> getFriends(Viewer viewer) throws Exception {
		return viewer.getOwnerFriends();
	}

	public Set<User> getFriends(Viewer solicitorViewer, User solicited) throws Exception {
		solicitorViewer = solicitorViewer.viewOtherProfile(solicited);
		return solicitorViewer.getOwnerFriends();
	}

	/**
	 * Faz a requisição de uma amizade.
	 * @param sessionOwner Sessão do usuário requisitente da amizade.
	 * @param otherUser usuário cuja amizade está sendo solicitada.
	 */

	public void askForFriendship(User sessionOwner, User otherUser) throws Exception{
		sessionOwner.requestFriendship(otherUser);
	}

	/**
	 *Aprova (aceita) uma amizade requisitada pelo usuário  passado como parâmetroID da Sessão
	 * foi passsado como parâmetro pelo usuário logado no sisteme(sessionOwner).  
	 * @param sessionOwner Usuário logado no sistema.
	 * @param otherUser Usuário requisitante da amizade.
	 */

	public void acceptFriendship(User sessionOwner, User otherUser) throws Exception{
		sessionOwner.acceptFriendshipRequest(otherUser);
	}

	/**
	 * Nega um pedido de amizade.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionOwner usuário que negará uma maizade.
	 * @param otherUser Usuário cuja solicitação de amizade será negada.
	 * @throws Exception if users involved doesn't exists or if solicited user already declined request
	 */
	public void declineFriendship(User sessionOwner, User otherUser) throws Exception{
		sessionOwner.declineFriendshipRequest(otherUser);
	}

	/**
	 * Solicitor user breaks friendship with solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionOwner the solicitor
	 * @param otherUser the solicited user
	 * @throws Exception if users involved doesn't exists
	 */
	public void breakFriendship(User sessionOwner, User otherUser) throws Exception{
		sessionOwner.breakFriendship(otherUser);
	}

	/**
	 * Returns true if both users are friends.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionOwner the solicitor.
	 * @param otherUser the solicited user.
	 * @return true if users involved are friends
	 * @throws Exception if users involved doesn't exists or are not friends
	 */
	public boolean hasFriend(User sessionOwner, User otherUser) throws Exception{
		return sessionOwner.hasFriend(otherUser);
	}

	/**
	 * 
	 * @param solicitorViewer
	 * @return Retorna um Set com o
	 */
	public Set<User> getOwnerFriendshipRequests(Viewer solicitorViewer) {
		return solicitorViewer.getOwnerFriendshipRequests();
	}

	public Viewer viewProfile(Viewer solicitorViewer, User user) throws Exception {
		solicitorViewer = solicitorViewer.viewOtherProfile(user);
		return solicitorViewer;
	}
	
}