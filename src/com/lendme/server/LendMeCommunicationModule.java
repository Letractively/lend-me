package com.lendme.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.lendme.server.entities.ActivityRegistry;
import com.lendme.server.entities.ActivityRegistry.ActivityKind;
import com.lendme.server.entities.Item;
import com.lendme.server.entities.Lending;
import com.lendme.server.entities.Message;
import com.lendme.server.entities.Topic;
import com.lendme.server.entities.User;

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
	 * Permite o envio de uma menssagem entre dois usuários.
	 * @param sender Usuário que vai
	 * enviar uma menssagem.
	 * @param subject Assunto da menssagem.
	 * @param message Corpo da menssagem.
	 * @param receiver Usuário que receberá a menssagem.
	 * @param lending requisição de um pedido de um Item.
	 * @return 
	 */	
	public  String sendMessage(User sender, String subject, String message,
			User receiver, Lending lending) throws Exception {//String lendingId, Collection<Lending> lendings) throws Exception {

		if ( sender.equals(receiver.getLogin()) ){
			throw new Exception("Usuário não pode mandar mensagem para si mesmo");//"User cannot send messages to himself");
		}
		if (message == null || message.trim().isEmpty()) {
			throw new Exception("Mensagem inválida");//"Invalid message");
		}
		
		if (subject == null || subject.trim().isEmpty()) {
			throw new Exception("Assunto inválido");//"Invalid subject");
		}		

		if ( (lending != null) && ( lending.getLender().equals(sender) || lending.getBorrower().equals(sender)  ) ){
			return sender.sendMessage(subject, message, receiver, lending.getID());
		}
		else if ( lending == null ){
			throw new Exception("Requisição de empréstimo inexistente");
		}
		throw new Exception("O usuário não participa deste empréstimo");
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
	public  String sendMessage(User sender, String subject, String message, 
			User receiver) throws Exception {
		
		if ( sender.equals(receiver.getLogin()) ){
			throw new Exception("Usuário não pode mandar mensagem para si mesmo");//"User cannot send messages to himself");
		}
		if (subject == null || subject.trim().isEmpty()) {
			throw new Exception("Assunto inválido");//"Invalid subject");
		}
		
		if (message == null || message.trim().isEmpty()) {
			throw new Exception("Mensagem inválida");//"Invalid message");
		}
		
		return sender.sendMessage(subject, message, receiver);
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
	public  List<Topic> getTopics(User reader, String topicType)
			throws Exception {
		return reader.getTopics(topicType);
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
	public  List<Message> getTopicMessages(User reader, String topicId, Set<User> searchScope)
			throws Exception {
		if (topicId == null || topicId.trim().isEmpty()) {
			throw new Exception("Identificador do tópico é inválido");
			// "Invalid topic identifier");
		}
		List<Message> messages = getMessagesByTopicId(topicId, searchScope);
		Message sampleMessage = messages.iterator().next();
		
		if ( !( sampleMessage.getSender().equals(reader.getLogin()) 
				|| sampleMessage.getReceiver().equals(reader.getLogin())) ) {
			throw new Exception("O usuário não tem permissão para ler as mensagens deste tópico");
		}
		return messages;
	}
	
	public void offerItem(User sessionOwner,
			String requestPublicationId, String itemId, Set<User> allUsers) throws Exception{

		if ( requestPublicationId == null || requestPublicationId.trim().isEmpty() ){
			throw new Exception("Identificador da publicação de pedido é inválido");
		}
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");
		}
		
		for ( User user : allUsers){
			for ( Lending publishedRequest : user.getPublishedItemRequests() ){
				if ( publishedRequest.getID().equals(requestPublicationId) ){
					for ( Item item : sessionOwner.getAllItems() ){
						if ( item.getID().equals(itemId) ){
							sessionOwner.offerItem(publishedRequest, item);
							return;
						}
					}
					throw new Exception("Item inexistente");
				}
			}
		}
		throw new Exception("Publicação de pedido inexistente");
	}
	
	
	
	public void republishItemRequest(User sessionOwner,
			String requestPublicationId, Set<User> allUsers) throws Exception{
		for ( User user : allUsers){
			for ( Lending publishedRequest : user.getPublishedItemRequests() ){
				if ( publishedRequest.getID().equals(requestPublicationId) ){
					Lending petition = publishedRequest;
					sessionOwner.republishItemRequest(petition);
					return;
				}
			}
		}
		throw new Exception("Publicação de pedido inexistente");

	}

	public List<ActivityRegistry> getJointActivityHistory(User sessionOwner) {

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

	public List<ActivityRegistry> getActivityHistory(User sessionOwner) throws Exception{
		return sessionOwner.getMyActivityHistory();
	}

	public String publishItemRequest(User sessionOwner, String itemName,
			String itemDescription) throws Exception{
		return sessionOwner.publishItemRequest(itemName, itemDescription);
	}
	
	public List<Lending> getFriendsPublishedItemRequests(Set<User> friends) throws Exception {
		List<Lending> results = new ArrayList<Lending>();
		
		for (User actualFriend : friends) {
			for (Lending currentPubRequest : actualFriend.getPublishedItemRequests()) {
				results.add(currentPubRequest);
			}
		}
		return results;
	}

}
