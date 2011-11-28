package com.lendme.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("facade")
public interface LendMe extends RemoteService{

	public String openSession(String id) throws Exception;

	public void closeSession(String sessionId) throws Exception;
	
	public String registerUser(String login, String name, String... address) throws Exception;

	public String[] searchUsersByAttributeKey(String solicitorSession, String key, String attribute) throws Exception;

	public String[] listUsersByDistance(String solicitorSession) throws Exception;
	
	public String getUserAttribute(String login, String attribute) throws Exception;
	
	public void askForFriendship(String solicitorSession, String solicitedLogin) throws Exception;
	
	public void acceptFriendship(String solicitedSession, String solicitorLogin) throws Exception;
	
	public void declineFriendship(String solicitedSession, String solicitorLogin) throws Exception;
	
	public void breakFriendship(String solicitorSession, String solicitedLogin) throws Exception;
	
	public String[] getFriendshipRequests(String solicitorSession) throws Exception;
	
	public Map<String, String[]> getFriends(String solicitorSession) throws Exception;
	
	public Map<String, String[]> getFriends(String solicitorSession, String solicitedLogin) throws Exception;
	
	public boolean hasFriend(String solicitorSession, String solicitedLogin) throws Exception;
	
	public String sendMessage(String senderSession, String subject,	String message, String receiverLogin) throws Exception;
	
	public String sendMessage(String senderSession, String subject,	String message, String receiverLogin, String lendingId)
		throws Exception;
	
	public Map<String,String[]> getTopics(String solicitorSession, String topicType) throws Exception;
	
	public String[] getTopicsWithIds(String solicitorSession, String topicType) throws Exception;
	
	public Map<String,String[]> getTopicMessages(String solicitorSession, String topicId) throws Exception;
	
	public String registerItem(String creatorSession, String name, String description, String category) throws Exception;
	
	public String[] searchForItems(String solicitorSession, String key, String attribute, String disposition,
			String criteria) throws Exception;
	
	public String[] searchForItemsWithIds(String solicitorSession, String key, String attribute, String disposition,
			String criteria) throws Exception;
	
	public Map<String, String[]> getItems(String solicitorSession) throws Exception;
	
	public String[] getItems(String solicitorSession, String solicitedLogin) throws Exception;
	
	public String[] getItemsWithIds(String solicitorSession) throws Exception;
	
	public String getItemAttribute(String itemId, String attribute)	throws Exception;
	
	public void deleteItem(String solicitorSession, String itemId) throws Exception;
	
	public void registerInterestForItem(String solicitorSession, String itemId) throws Exception;
	
	public String requestItem(String solicitorSession, String itemId, int requiredDays) throws Exception;
	
	public String approveLending(String solicitorSession, String requestId) throws Exception;
	
	public String denyLending(String solicitorSession, String requestId) throws Exception;
	
	public String askForReturnOfItem(String solicitorSession, String lendingId) throws Exception;
	
	public String returnItem(String solicitedSession, String lendingId) throws Exception;
	
	public String confirmLendingTermination(String solicitorSession, String lendingId) throws Exception;
	
	public String denyLendingTermination(String solicitorSession, String lendingId) throws Exception;
	
	public String[] getReceivedItemRequests(String solicitorSession) throws Exception;
	
	public Map<String, String[]> getLendingRecords(String solicitorSession, String kind) throws Exception;
	
	public String[] getLendingRecordsWithIds(String solicitorSession, String kind) throws Exception;
	
	public String getRanking(String solicitorSession, String category) throws Exception;
	
	public String viewProfile(String solicitorSessionId, String solicitedUserLogin) throws Exception;
	
	public Map<String, String> getActivityHistory(String solicitorSessionId) throws Exception;
	
	public Map<String, String> getJointActivityHistory(String solicitorSessionId) throws Exception;
	
	public String[] getFriendsPublishedItemRequests(String solicitorSessionId) throws Exception;
	
	public String publishItemRequest(String sessionId, String itemName, String itemDescription) throws Exception;
	
	public void offerItem(String sessionId, String requestPublicationId, String itemId) throws Exception;
	
	public void republishItemRequest(String sessionId, String requestPublicationId) throws Exception;
	
	public String getSessionInfo(String currentUserSessionId) throws Exception;

}
