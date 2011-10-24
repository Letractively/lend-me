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
	public  Profile getUserProfile(Session session) throws Exception {
		return Profile.getUserProfile(session, session.getOwner());
	}

	public Set<User> getFriends(Profile viewer) throws Exception {
		return viewer.getOwnerFriends();
	}

	public Set<User> getFriends(Profile solicitorViewer, User solicited) throws Exception {
		solicitorViewer = solicitorViewer.viewOtherProfile(solicited);
		return solicitorViewer.getOwnerFriends();
	}

	public void askForFriendship(User sessionOwner, User otherUser) throws Exception{
		sessionOwner.requestFriendship(otherUser);
	}

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

	public Set<User> getOwnerFriendshipRequests(Profile solicitorViewer) {
		return solicitorViewer.getOwnerFriendshipRequests();
	}

	public Profile viewProfile(Profile solicitorViewer, User user) throws Exception {
		solicitorViewer = solicitorViewer.viewOtherProfile(user);
		return solicitorViewer;
	}
	
}