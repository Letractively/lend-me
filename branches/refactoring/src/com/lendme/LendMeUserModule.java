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

	public void askForFriendship(Profile solicitorViewer, User solicited) throws Exception{
		solicitorViewer = solicitorViewer.viewOtherProfile(solicited);
		solicitorViewer.askForFriendship();
	}

	public void acceptFriendship(Profile solicitedViewer, User solicitor) throws Exception{
		solicitedViewer = solicitedViewer.viewOtherProfile(solicitor);
		solicitedViewer.acceptFriendshipRequest();
	}

	public void declineFriendship(Profile solicitedViewer, User solicitor) throws Exception{
		solicitedViewer = solicitedViewer.viewOtherProfile(solicitor);
		solicitedViewer.declineFriendshipRequest();
	}

	public void breakFriendship(Profile solicitorViewer, User user) throws Exception{
		solicitorViewer = solicitorViewer.viewOtherProfile(user);
		solicitorViewer.breakFriendship();
	}

	public boolean hasFriend(Profile solicitorViewer, User user) throws Exception{
		solicitorViewer = solicitorViewer.viewOtherProfile(user);
		return solicitorViewer.isFriendOfOwner();
	}

	public Set<User> getOwnerFriendshipRequests(Profile solicitorViewer) {
		return solicitorViewer.getOwnerFriendshipRequests();
	}
	
}