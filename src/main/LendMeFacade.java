package main;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import entities.Item;
import entities.Lending;
import entities.User;
import entities.util.LendingStatus;
import entities.util.Message;
import entities.util.Topic;

public class LendMeFacade {

	public void resetSystem(){
		LendMe.resetSystem();
	}
	
	public void openSession(String login) throws Exception{
		
		LendMe.openSession(login);
	}
	
	public String getSystemDate(){
		
		return LendMe.getSystemDate().toString();
	}
	
	public void closeSession(String sessionId) throws Exception{
		
		LendMe.closeSession(sessionId);
	}
	
	public void registerUser(String login, String name, String... address)
		throws Exception{
		
		LendMe.registerUser(login, name, address);
	}
	
	public String[] searchUsersByName(String name){
		
		Set<User> results = LendMe.searchUsersByName(name);
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}

	public String[] searchUsersByAddress(String address){

		Set<User> results = LendMe.searchUsersByAddress(address);
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}
	
	public void askForFriendship(String solicitorSession, String solicitedLogin)
		throws Exception{
		
		LendMe.askForFriendship(solicitorSession, solicitedLogin);
	}

	public void acceptFriendship(String solicitedSession, String solicitorLogin)
		throws Exception{
			
			LendMe.acceptFriendship(solicitedSession, solicitorLogin);
	}
	
	public void declineFriendship(String solicitedSession, String solicitorLogin)
		throws Exception{
		
		LendMe.declineFriendship(solicitedSession, solicitorLogin);
	}

	public void breakFriendship(String solicitorSession, String solicitedLogin)
		throws Exception{
			
		LendMe.breakFriendship(solicitorSession, solicitedLogin);
	}

	public String[] getFriends(String solicitorSession)
		throws Exception{

		Set<User> results = LendMe.getFriends(solicitorSession);
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}
	
	public String[] getFriends(String solicitorSession, String solicitedLogin)
		throws Exception{

		Set<User> results = LendMe.getFriends(solicitorSession, solicitedLogin);
		String[] handled = new String[results.size()];
		Iterator<User> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getLogin();
		}
		return handled;
	}
	
	public boolean hasFriend(String solicitorSession, String solicitedLogin)
		throws Exception{

		return LendMe.hasFriend(solicitorSession, solicitedLogin);
	}
	
	public String sendMessage(String senderSession, String subject,
			String message, String receiverLogin)
		throws Exception{
		
		return LendMe.sendMessage(senderSession, subject, message, receiverLogin);
	}
	
	public String sendMessage(String senderSession, String subject,
			String message, String receiverLogin, String lendingId)
		throws Exception{
		
		return LendMe.sendMessage(senderSession, subject, message, receiverLogin, lendingId);
	}
	
	public String[] getTopics(String solicitorSession, String topicType) throws Exception{
		
		List<Topic> results = LendMe.getTopics(solicitorSession, topicType);;
		String[] handled = new String[results.size()];
		Iterator<Topic> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getSubject();
		}
		return handled;
	}
	
	public String[] getTopicMessages(String solicitorSession, String topicId) throws Exception{
		
		List<Message> results = LendMe.getTopicMessages(solicitorSession, topicId);;
		String[] handled = new String[results.size()];
		Iterator<Message> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getMessage();
		}
		return handled;		
	}

	public void registerItem(String creatorSession, String name, String description, String category)
		throws Exception{
		
		LendMe.registerItem(creatorSession, name, description, category);
	}
	
	public String[] getItems(String solicitorSession) throws Exception{
		
		Set<Item> results = LendMe.getItems(solicitorSession);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getName();
		}
		return handled;
	}

	public String[] getItems(String solicitorSession, String solicitedLogin) throws Exception{
		
		Set<Item> results = LendMe.getItems(solicitorSession, solicitedLogin);
		String[] handled = new String[results.size()];
		Iterator<Item> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			handled[i] = iterator.next().getName();
		}
		return handled;
	}
	
	public void deleteItem(String solicitorSession, String itemId) throws Exception{
		
		LendMe.deleteItem(solicitorSession, itemId);
	}
	
	public void registerInterestForItem(String solicitorSession, String itemId) throws Exception{
		
		LendMe.registerInterestForItem(solicitorSession, itemId);
	}

	public String requestItem(String solicitorSession, String itemId, int requiredDays) throws Exception{
		
		return LendMe.requestItem(solicitorSession, itemId, requiredDays);
	}
	
	public String approveLoan(String solicitorSession, String requestId) throws Exception{
		
		return LendMe.approveLoan(solicitorSession, requestId);
	}
	
	public String askForReturnOfItem(String solicitorSession, String lendingId) throws Exception{
		
		return LendMe.askForReturnOfItem(solicitorSession, lendingId);
	}
	
	public String returnItem(String solicitedSession, String lendingId) throws Exception{
		
		return LendMe.returnItem(solicitedSession, lendingId);
	}
	
	public String confirmLendingTermination(String solicitorSession, String lendingId) throws Exception{
		
		return LendMe.confirmLendingTermination(solicitorSession, lendingId);
	}
	
	public String denyLendingTermination(String solicitorSession, String lendingId) throws Exception{
		
		return LendMe.denyLendingTermination(solicitorSession, lendingId);
	}

	public String[] getLendingRecords(String solicitorSession, String kind) throws Exception{
		
		Collection<Lending> results = LendMe.getLendingRecords(solicitorSession, kind);
		String[] handled = new String[results.size()];
		Iterator<Lending> iterator = results.iterator();
		for ( int i=0; i<handled.length; i++ ){
			String template = "%s-%s:%s:%s";
			Lending tmp = iterator.next();
			handled[i] = String.format(template, tmp.getLender().getLogin(),
					tmp.getBorrower().getLogin(), tmp.getItem().getName(),
					tmp.getStatus() == LendingStatus.ONGOING ? "Andamento" : 
				  ( tmp.getStatus() == LendingStatus.FINISHED ? "Completado" : 
					tmp.getStatus() == LendingStatus.CANCELLED ? "Cancelado" : "Negado" ) );
		}
		return handled;
	}
	
}
