package com.lendme.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LendMeAsync {

	void openSession(String id, AsyncCallback<String> asyncCallback);

	void closeSession(String sessionId, AsyncCallback<Void> callback);

	void registerUser(String login, String name, String[] address,
			AsyncCallback<String> callback);

	void searchUsersByAttributeKey(String solicitorSession, String key,
			String attribute, AsyncCallback<String[]> callback);

	void listUsersByDistance(String solicitorSession,
			AsyncCallback<String[]> callback);

	void getUserAttribute(String login, String attribute,
			AsyncCallback<String> callback);

	void askForFriendship(String solicitorSession, String solicitedLogin,
			AsyncCallback<Void> callback);

	void acceptFriendship(String solicitedSession, String solicitorLogin,
			AsyncCallback<Void> callback);

	void declineFriendship(String solicitedSession, String solicitorLogin,
			AsyncCallback<Void> callback);

	void breakFriendship(String solicitorSession, String solicitedLogin,
			AsyncCallback<Void> callback);

	void getFriendshipRequests(String solicitorSession,
			AsyncCallback<String[]> callback);

	void getFriends(String solicitorSession, AsyncCallback<Map<String, String[]>> callback);

	void getFriends(String solicitorSession, String solicitedLogin,
			AsyncCallback<Map<String, String[]>> callback);

	void hasFriend(String solicitorSession, String solicitedLogin,
			AsyncCallback<Boolean> callback);

	void sendMessage(String senderSession, String subject, String message,
			String receiverLogin, AsyncCallback<String> callback);

	void sendMessage(String senderSession, String subject, String message,
			String receiverLogin, String lendingId,
			AsyncCallback<String> callback);

	void getTopics(String solicitorSession, String topicType,
			AsyncCallback<Map<String,String[]>> callback);

	void getTopicsWithIds(String solicitorSession, String topicType,
			AsyncCallback<String[]> callback);

	void getTopicMessages(String solicitorSession, String topicId,
			AsyncCallback<Map<String,String[]>> callback);

	void registerItem(String creatorSession, String name, String description,
			String category, AsyncCallback<String> callback);

	void searchForItems(String solicitorSession, String key, String attribute,
			String disposition, String criteria,
			AsyncCallback<String[]> callback);

	void searchForItemsWithIds(String solicitorSession, String key,
			String attribute, String disposition, String criteria,
			AsyncCallback<String[]> callback);

	void getItems(String solicitorSession, AsyncCallback<String[]> callback);

	void getItemsWithIds(String solicitorSession,
			AsyncCallback<String[]> callback);

	void getItems(String solicitorSession, String solicitedLogin,
			AsyncCallback<String[]> callback);

	void getItemAttribute(String itemId, String attribute,
			AsyncCallback<String> callback);

	void deleteItem(String solicitorSession, String itemId,
			AsyncCallback<Void> callback);

	void registerInterestForItem(String solicitorSession, String itemId,
			AsyncCallback<Void> callback);

	void requestItem(String solicitorSession, String itemId, int requiredDays,
			AsyncCallback<String> callback);

	void approveLending(String solicitorSession, String requestId,
			AsyncCallback<String> callback);

	void denyLending(String solicitorSession, String requestId,
			AsyncCallback<String> callback);

	void askForReturnOfItem(String solicitorSession, String lendingId,
			AsyncCallback<String> callback);

	void returnItem(String solicitedSession, String lendingId,
			AsyncCallback<String> callback);

	void confirmLendingTermination(String solicitorSession, String lendingId,
			AsyncCallback<String> callback);

	void denyLendingTermination(String solicitorSession, String lendingId,
			AsyncCallback<String> callback);

	void getReceivedItemRequests(String solicitorSession,
			AsyncCallback<String[]> callback);

	void getLendingRecords(String solicitorSession, String kind,
			AsyncCallback<String[]> callback);

	void getLendingRecordsWithIds(String solicitorSession, String kind,
			AsyncCallback<String[]> callback);

	void getRanking(String solicitorSession, String category,
			AsyncCallback<String> callback);

	void viewProfile(String solicitorSessionId, String solicitedUserLogin,
			AsyncCallback<String> callback);

	void getActivityHistory(String solicitorSessionId,
			AsyncCallback<String[]> callback);

	void getJointActivityHistory(String solicitorSessionId,
			AsyncCallback<String[]> callback);

	void getFriendsPublishedItemRequests(String solicitorSessionId,
			AsyncCallback<String[]> callback);

	void publishItemRequest(String sessionId, String itemName,
			String itemDescription, AsyncCallback<String> callback);

	void offerItem(String sessionId, String requestPublicationId,
			String itemId, AsyncCallback<Void> callback);

	void republishItemRequest(String sessionId, String requestPublicationId,
			AsyncCallback<Void> callback);

	void getSessionInfo(String currentUserSessionId,
			AsyncCallback<String> callback);

}
