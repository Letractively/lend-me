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

	public void declineFriendship(User sessionOwner, User otherUser) throws Exception{
		sessionOwner.declineFriendshipRequest(otherUser);
	}

	public void breakFriendship(User sessionOwner, User otherUser) throws Exception{
		sessionOwner.breakFriendship(otherUser);
	}

	public boolean hasFriend(User sessionOwner, User otherUser) throws Exception{
		return sessionOwner.hasFriend(otherUser);
	}

	public Set<User> getOwnerFriendshipRequests(Viewer solicitorViewer) {
		return solicitorViewer.getOwnerFriendshipRequests();
	}

	public Viewer viewProfile(Viewer solicitorViewer, User user) throws Exception {
		solicitorViewer = solicitorViewer.viewOtherProfile(user);
		return solicitorViewer;
	}
	
}