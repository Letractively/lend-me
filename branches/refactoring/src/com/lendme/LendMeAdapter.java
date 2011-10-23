package com.lendme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.lendme.entities.ActivityRegistry;
import com.lendme.entities.Item;
import com.lendme.entities.Lending;
import com.lendme.entities.Lending.LendingStatus;
import com.lendme.entities.Message;
import com.lendme.entities.Topic;
import com.lendme.entities.User;
import com.lendme.utils.ComparatorOfDateStrategy;

public class LendMeAdapter {
	
	private LendMe lendMe = new LendMe();
	
	/*
	 * Refactoring comment:
	 * In this class we used the Adapter design pattern........
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/* (non-Javadoc)
	 * @see com.lendme.LendMe#resetSystem()
	 */
	public void resetSystem(){
		lendMe.resetSystem();
	}
	
	/* (non-Javadoc)
	 * @see com.lendme.LendMe#getSystemDate()
	 */
	public String getSystemDate(){
		
		return lendMe.getSystemDate().toString();
	}

	/* (non-Javadoc)
	 * @see com.lendme.LendMe#someDaysPassed(int)
	 */
	public String someDaysPassed(int amount){
		
		return lendMe.someDaysPassed(amount);
	}
	
	/* (non-Javadoc)
	 * @see com.lendme.LendMe#openSession(String)
	 */
	public String openSession(String login) throws Exception{
		
		return lendMe.openSession(login);
	}
	
	/* (non-Javadoc)
	 * @see com.lendme.LendMe#closeSession(String)
	 */
	public void closeSession(String sessionId) throws Exception{
		
		lendMe.closeSession(sessionId);
	}
	
	/* (non-Javadoc)
	 * @see com.lendme.LendMe#registerUser(String, String, String...)
	 */
	public String registerUser(String login, String name, String... address)
		throws Exception{
		
		return lendMe.registerUser(login, name, address);
	}
	
   /** Handles with results from search in System, transforming them in a string array.
	 * @param solicitorSession the id of the session of the solicitor
	 * @param key the value expected for specified attribute
	 * @param attribute the specified attribute
	 * @return an array of strings containing the results names and addresses
	 * @see com.lendme.LendMe#searchUsersByAttributeKey(String, String, String)
	 */
	public String[] searchUsersByAttributeKey(String solicitorSession, String key, String attribute)
		throws Exception{
		
		List<User> results = 
				new ArrayList<User>(lendMe.searchUsersByAttributeKey(solicitorSession, key, attribute));
		Collections.sort(results,  new ComparatorOfDateStrategy());
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			User tmp = iterator.next();
			handled[i] = tmp.getName() + " - " + tmp.getAddress();
		}
		return handled;
	}
	
	public String[] listUsersByDistance(String solicitorSession) throws Exception{
		
		List<User> results = new ArrayList<User>(lendMe.listUsersByDistance(solicitorSession));
		
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			User tmp = iterator.next();
			handled[i] = tmp.getName() + " - " + tmp.getAddress();
		}
		return handled;
			
		
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param login the id of the session of the solicitor user
	 * @param attribute the attribute whose value is required
	 * @return an array of strings containing the results names
	 * @see com.lendme.LendMe#getUserAttribute(String, String)
	 */
	public String getUserAttribute(String login, String attribute)
		throws Exception{
		
		return lendMe.getUserAttribute(login, attribute);
	}
	
   /** (Non-javadoc)
	 * @see com.lendme.LendMe#askForFriendship(String)
	 */
	public void askForFriendship(String solicitorSession, String solicitedLogin)
		throws Exception{
		
		lendMe.askForFriendship(solicitorSession, solicitedLogin);
	}

   /** (Non-javadoc)
	 * @see com.lendme.LendMe#acceptFriendship(String, String)
	 */
	public void acceptFriendship(String solicitedSession, String solicitorLogin)
		throws Exception{
			
			lendMe.acceptFriendship(solicitedSession, solicitorLogin);
	}

   /** (Non-javadoc)
	 * @see com.lendme.LendMe#declineFriendship(String, String)
	 */
	public void declineFriendship(String solicitedSession, String solicitorLogin)
		throws Exception{
		
		lendMe.declineFriendship(solicitedSession, solicitorLogin);
	}

   /** (Non-javadoc)
	 * @see com.lendme.LendMe#breakFriendship(String, String)
	 */
	public void breakFriendship(String solicitorSession, String solicitedLogin) throws Exception{
			
		lendMe.breakFriendship(solicitorSession, solicitedLogin);
	}

   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results logins
	 * @see com.lendme.LendMe#getFriendshipRequests(String)
	 */
	public String[] getFriendshipRequests(String solicitorSession) throws Exception {

		List<User> results = new ArrayList<User>(lendMe.getFriendshipRequests(solicitorSession));
		Collections.sort(results,  new ComparatorOfDateStrategy());
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}

   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results logins
	 * @see com.lendme.LendMe#getFriends(String)
	 */
	public String[] getFriends(String solicitorSession)
		throws Exception{

		List<User> results = new ArrayList<User>(lendMe.getFriends(solicitorSession));
		Collections.sort(results,  new ComparatorOfDateStrategy());
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results logins
	 * @see com.lendme.LendMe#getFriends(String)
	 */
	public String[] getFriends(String solicitorSession, String solicitedLogin)
		throws Exception{

		List<User> results = new ArrayList<User>(lendMe.getFriends(solicitorSession, solicitedLogin));
		Collections.sort(results,  new ComparatorOfDateStrategy());
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}

   /** (Non-javadoc)
	 * @see com.lendme.LendMe#hasFriend(String, String)
	 */
	public boolean hasFriend(String solicitorSession, String solicitedLogin)
		throws Exception{

		return lendMe.hasFriend(solicitorSession, solicitedLogin);
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#sendMessage(String, String, String, String)
	 */
	public String sendMessage(String senderSession, String subject,
			String message, String receiverLogin)
		throws Exception{
		
		return lendMe.sendMessage(senderSession, subject, message, receiverLogin);
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#sendMessage(String, String, String, String)
	 */
	public String sendMessage(String senderSession, String subject,
			String message, String receiverLogin, String lendingId)
		throws Exception{
		
		return lendMe.sendMessage(senderSession, subject, message, receiverLogin, lendingId);
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results
	 * @see com.lendme.LendMe#getTopics(String, String)
	 */
	public String[] getTopics(String solicitorSession, String topicType) throws Exception{
		
		List<Topic> results = lendMe.getTopics(solicitorSession, topicType);
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Topic> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getSubject();
		}
		return handled;
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results ids
	 * @see com.lendme.LendMe#getTopics(String, String)
	 */
	public String[] getTopicsWithIds(String solicitorSession, String topicType) throws Exception{
		
		List<Topic> results = lendMe.getTopics(solicitorSession, topicType);
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Topic> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			Topic actualTopic = iterator.next();
			handled[i] = "\n\tAssunto: " + actualTopic.getSubject()
					 + "\n\tId: " + actualTopic.getID();
		}
		return handled;
	}
	
   /** Handles with results from search in System, transforming them in a string array
	 * @param solicitorSession the id of the session of the solicitor user
	 * @return an array of strings containing the results
	 * @see com.lendme.LendMe#getTopicsMessages(String, String)
	 */
	public String[] getTopicMessages(String solicitorSession, String topicId) throws Exception{
		
		List<Message> results = lendMe.getTopicMessages(solicitorSession, topicId);
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Message> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getMessage();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#registerItem(String, String, String, String)
	 */	
	public String registerItem(String creatorSession, String name, String description, String category)
		throws Exception{
		
		return lendMe.registerItem(creatorSession, name, description, category);
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#searchForItem(String, String, String, String, String)
	 */	
	public String[] searchForItems(String solicitorSession, String key, String attribute,
		String disposition, String criteria) throws Exception{
		
		List<Item> results = lendMe.searchForItem(solicitorSession, key, attribute, disposition, criteria);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getName();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#searchForItem(String, String, String, String, String)
	 */	
	public String[] searchForItemsWithIds(String solicitorSession, String key, String attribute,
			String disposition, String criteria) throws Exception{
			
			List<Item> results = lendMe.searchForItem(solicitorSession, key, attribute, disposition, criteria);
			String[] handled = new String[results.size()];
			Iterator<Item> iterator = results.iterator();
			for ( int i=0; i<handled.length; i++ ){
				handled[i] = iterator.next().toString();
			}
			return handled;
		}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#getItems(String)
	 */	
	public String[] getItems(String solicitorSession) throws Exception{
		
		List<Item> results = new ArrayList<Item>(lendMe.getItems(solicitorSession));
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getName();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#getItems(String)
	 */	
	public String[] getItemsWithIds(String solicitorSession) throws Exception{
		
		List<Item> results = new ArrayList<Item>(lendMe.getItems(solicitorSession));
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().toString();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#getItems(String)
	 */	
	public String[] getItems(String solicitorSession, String solicitedLogin) throws Exception{
		
		List<Item> results = new ArrayList<Item>(lendMe.getItems(solicitorSession, solicitedLogin));
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getName();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#getItemAttribute(String, String)
	 */	
	public String getItemAttribute(String itemId, String attribute)
		throws Exception{
		
		return lendMe.getItemAttribute(itemId, attribute);
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#deleteItem(String, String)
	 */	
	public void deleteItem(String solicitorSession, String itemId) throws Exception{
		
		lendMe.deleteItem(solicitorSession, itemId);
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#registerInterestForItem(String, String)
	 */	
	public void registerInterestForItem(String solicitorSession, String itemId) throws Exception{
		
		lendMe.registerInterestForItem(solicitorSession, itemId);
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#requestItem(String, String, int)
	 */	
	public String requestItem(String solicitorSession, String itemId, int requiredDays) throws Exception{
		
		return lendMe.requestItem(solicitorSession, itemId, requiredDays);
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#approveLending(String, String)
	 */	
	public String approveLending(String solicitorSession, String requestId) throws Exception{
		
		return lendMe.approveLending(solicitorSession, requestId);
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#denyLending(String, String)
	 */	
	public String denyLending(String solicitorSession, String requestId) throws Exception{
		
		return lendMe.denyLending(solicitorSession, requestId);
	}
	

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#askForReturnOfItem(String, String)
	 */	
	public String askForReturnOfItem(String solicitorSession, String lendingId) throws Exception{
		
		return lendMe.askForReturnOfItem(solicitorSession, lendingId);
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#returnItem(String, String)
	 */	
	public String returnItem(String solicitedSession, String lendingId) throws Exception{
		
		return lendMe.returnItem(solicitedSession, lendingId);
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#confirmLendingTermination(String, String)
	 */	
	public String confirmLendingTermination(String solicitorSession, String lendingId) throws Exception{
		
		return lendMe.confirmLendingTermination(solicitorSession, lendingId);
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#denyLendingTermination(String, String)
	 */	
	public String denyLendingTermination(String solicitorSession, String lendingId) throws Exception{
		
		return lendMe.denyLendingTermination(solicitorSession, lendingId);
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#getReceivedItemRequests(String)
	 */	
	public String[] getReceivedItemRequests(String solicitorSession) throws Exception{
		
		List<Lending> results = new ArrayList<Lending>(lendMe.getReceivedItemRequests(
				solicitorSession));
		Collections.sort(results);
		String[] handled = new String[results.size()];
		Iterator<Lending> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			Lending actualLending = iterator.next();
			handled[i] = " ID:" + actualLending.getID() +
				"\n\tSolicitador: " + actualLending.getBorrower().getName() +	
				"\n\tItem:\n\t" + actualLending.getItem().toString();
		}
		return handled;
	}

	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#getLendingRecords(String, String)
	 */	
	public String[] getLendingRecords(String solicitorSession, String kind) throws Exception{
		
		List<Lending> results = new ArrayList<Lending>(lendMe.getLendingRecords(solicitorSession, kind));
		String[] handled = new String[results.size()];
		Iterator<Lending> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			String template = "%s-%s:%s:%s";
			Lending tmp = iterator.next();
			handled[i] = String.format(template, tmp.getLender().getLogin(),
					tmp.getBorrower().getLogin(), tmp.getItem().getName(),
					tmp.getStatus() == LendingStatus.ONGOING ? "Andamento" : 
				  ( tmp.getStatus() == LendingStatus.FINISHED ? "Completado" : 
					tmp.getStatus() == LendingStatus.CANCELLED ? "Cancelado" : "Negado" ));
		}
		return handled;
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#getLendingRecords(String, String)
	 */	
	public String[] getLendingRecordsWithIds(String solicitorSession, String kind) throws Exception{
		
		List<Lending> results = new ArrayList<Lending>(lendMe.getLendingRecords(solicitorSession, kind));
		String[] handled = new String[results.size()];
		Iterator<Lending> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			String template = "%s-%s:%s:%s\n\t Id: %s";
			Lending tmp = iterator.next();
			handled[i] = String.format(template, tmp.getLender().getLogin(),
					tmp.getBorrower().getLogin(), tmp.getItem().getName(),
					tmp.getStatus() == LendingStatus.ONGOING ? "Andamento" : 
				  ( tmp.getStatus() == LendingStatus.FINISHED ? "Completado" : 
					tmp.getStatus() == LendingStatus.CANCELLED ? "Cancelado" : "Negado" ),
					tmp.getID());
		}
		return handled;
	}
	
	/**
	 * (Non-javadoc)
	 * @see com.lendme.LendMe#getRanking(String, String)
	 */	
	public String getRanking(String solicitorSession, String category) throws Exception{
		
		return lendMe.getRanking(solicitorSession, category);
	}
	
	public String viewProfile(String solicitorSessionId, 
			String solicitedUserLogin) throws Exception {
		
		return lendMe.viewProfile(solicitorSessionId, solicitedUserLogin);
	}
	
	public String[] getActivityHistory(String solicitorSessionId) throws Exception {
		
		List<ActivityRegistry> results = lendMe.getActivityHistory(
				solicitorSessionId);
		String[] handled;
		
		if (results.size() == 0) {
			handled = new String[1];
			handled[0] = "Não há atividades";
			return handled;
		}
		
		handled = new String[results.size()];
		Iterator<ActivityRegistry> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			ActivityRegistry actualActivityRegistry = iterator.next();
			handled[i] = actualActivityRegistry.getDescription();
		}
		return handled;
	}

	public String[] getJointActivityHistory(String solicitorSessionId) throws Exception {
		List<ActivityRegistry> results = lendMe.getJointActivityHistory(
				solicitorSessionId);
		String[] handled;
		
		if (results.size() == 0) {
			handled = new String[1];
			handled[0] = "Não há atividades";
			return handled;
		}
		
		handled = new String[results.size()];
		Iterator<ActivityRegistry> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			ActivityRegistry actualActivityRegistry = iterator.next();
			handled[i] = actualActivityRegistry.getDescription();
		}
		return handled;
	}

	public String publishItemRequest(String sessionId, String itemName,
			String itemDescription) throws Exception{
		return lendMe.publishItemRequest(sessionId, itemName, itemDescription);
	}

	public void offerItem(String sessionId, String requestPublicationId,
			String itemId) throws Exception{
		lendMe.offerItem(sessionId, requestPublicationId, itemId);
	}

	public void republishItemRequest(String sessionId, String requestPublicationId) 
		throws Exception{
		lendMe.republishItemRequest(sessionId, requestPublicationId);		
	}
	
}