package com.lendme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.lendme.entities.ActivityRegistry;
import com.lendme.entities.Lending;
import com.lendme.entities.Message;
import com.lendme.entities.Session;
import com.lendme.entities.Topic;
import com.lendme.entities.User;
import com.lendme.entities.ActivityRegistry.ActivityKind;

public class LendMeCommunicationModule {
	
	/**
	 * Returns messages with given topic.
	 * 
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	public  List<Message> getMessagesByTopicId(String topicId, Set<User> searchScope) throws Exception{
		if(searchScope == null || searchScope.isEmpty()){
			throw new Exception("Nenhum usuário encontrado");
		}
		for ( User user : searchScope){
			List<Message> messages = user.getMessagesByTopicId(topicId);
			if ( messages != null ){
				return messages;
			}
		}
		throw new Exception("Tópico inexistente");//"Inexistent topic");
	}
	
	/**
	 * Solicitor user sends a message to solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param senderSessionId
	 * @param subject
	 * @param message
	 * @param receiverLogin
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
//	public  String sendMessage(Session senderSession, String subject, String message, 
	public  String sendMessage(Profile solicitorViewer, String subject, String message,
			User receiver, String lendingId) throws Exception {
		
		
			solicitorViewer = solicitorViewer.viewOtherProfile(receiver);
		
			return solicitorViewer.sendMessage(subject, message, lendingId);
			
	}


	/**
	 * Solicitor user sends message to solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param senderSessionId
	 * @param subject
	 * @param message
	 * @param receiverLogin
	 * @return
	 * @throws Exception
	 */
	public  String sendMessage(Profile solicitorViewer, String subject, String message, 
			User receiver) throws Exception {
		
		try {
		
			solicitorViewer = solicitorViewer.viewOtherProfile(receiver);
		
		} catch (Exception e) {
			
			if (e.getMessage().equals("Login inválido")) {
				throw new Exception("Destinatário inválido");//"Invalid receiver");
			}
			
			else if (e.getMessage().equals("Usuário inexistente")) {
				throw new Exception("Destinatário inexistente");//"Inexistent receiver");
			}
			throw e;
		}
		
		return solicitorViewer.sendMessage(subject, message);
	}
	
	
	/**
	 * Returns user with session specified by session id message topics.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param topicType
	 * @return
	 * @throws Exception
	 */
	public  List<Topic> getTopics(Profile solicitorViewer, String topicType)
			throws Exception {
		return solicitorViewer.getTopics(topicType);
	}
	
	/**
	 * Returns user with session specified by sesison id topic messages.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	public  List<Message> getTopicMessages(Profile solicitorViewer, String topicId)
			throws Exception {
		return solicitorViewer.getTopicMessages(topicId);
	}
	
	public void offerItem(Profile viewer,
			String requestPublicationId, String itemId, Set<User> allUsers) throws Exception{
		
		for ( User user : allUsers){
			for ( Lending publishedRequest : user.getPublishedItemRequests() ){
				if ( publishedRequest.getID().equals(requestPublicationId) ){
					Lending petition = publishedRequest;
					viewer.offerItem(petition, itemId);
					return;
				}
			}
		}
		throw new Exception("Publicação de pedido inexistente");
	}
	
	
	
	public void republishItemRequest(Profile viewer,
			String requestPublicationId, Set<User> allUsers) throws Exception{
		for ( User user : allUsers){
			for ( Lending publishedRequest : user.getPublishedItemRequests() ){
				if ( publishedRequest.getID().equals(requestPublicationId) ){
					Lending petition = publishedRequest;
					viewer.republishItemRequest(petition);
					return;
				}
			}
		}
		throw new Exception("Publicação de pedido inexistente");

	}

	public List<ActivityRegistry> getJointActivityHistory(Session session) {

		User sessionOwner = session.getOwner();
		List<ActivityRegistry> results = 
				new ArrayList<ActivityRegistry>(sessionOwner.getMyActivityHistory());
		
		for (User actualFriend : sessionOwner.getFriends()) {
			for (ActivityRegistry actualAR : actualFriend.getMyActivityHistory()) {
				if (actualAR.getKind() == ActivityKind.ADICAO_DE_AMIGO_CONCLUIDA 
						&& actualAR.getDescription().contains(" e " + 
						sessionOwner.getName() + " são amigos agora")
						|| sessionOwner.getMyActivityHistory().contains(actualAR)) {
					continue;
				}
				results.add(actualAR);
			}
		}
		Collections.sort(results);
		return results;
	}

	public List<ActivityRegistry> getActivityHistory(Profile viewer) throws Exception{
		return viewer.getActivityHistory();
	}

	public String publishItemRequest(Profile viewer, String itemName,
			String itemDescription) throws Exception{
		return viewer.publishItemRequest(itemName, itemDescription);
	}

}
