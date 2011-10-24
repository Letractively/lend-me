package com.lendme.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lendme.entities.ActivityRegistry.ActivityKind;

public class CommunicationManager {

	private User me;
	private Set<Topic> negotiationTopics = new HashSet<Topic>();
	private Set<Topic> offTopicTopics = new HashSet<Topic>();
	private Set<ActivityRegistry> activityHistory = new HashSet<ActivityRegistry>();	
	
	public CommunicationManager(User user) {
		me = user;
	}

	public String addMessageToTopic(String subject, 
			String message, String senderLogin, String receiverLogin,
			boolean isOffTopic,	String lendingId) {
		
		Set<Topic> topics;
		if ( isOffTopic ){
			topics = offTopicTopics;
		}
		else{
			topics = negotiationTopics;
		}
		
		Topic foundTopic = getTopicBySubject(topics, subject); 
		
		if ( foundTopic != null) {
			foundTopic.addMessage(subject, message, senderLogin, receiverLogin, 
					isOffTopic, lendingId);
		}
		else {
			Topic newTopic = new Topic(subject);
			newTopic.addMessage(subject, message, senderLogin, receiverLogin, 
					isOffTopic, lendingId);
			topics.add(newTopic);
			foundTopic = newTopic;
		}		
		return foundTopic.getID();
	}
	
	public List<Message> getMessagesByTopicId(String topicId) throws Exception {
		Topic foundTopic = getTopicById(negotiationTopics, topicId);
		
		if ( foundTopic == null) {
			foundTopic = getTopicById(offTopicTopics, topicId);
			
			if ( foundTopic == null) {
				return null;
			}
		}
		Message[] msgArray = foundTopic.getMessages().toArray(
				new Message[foundTopic.getMessages().size()]);
		Arrays.sort(msgArray);
		return Arrays.asList(msgArray);
		
	}

	private Topic getTopicById(Set<Topic> topicSet, String topicId) throws Exception{
		if ( topicId == null || topicId.trim().isEmpty() ){
			throw new Exception("Identificador do tópico é inválido");
		}
		for (Topic topic : topicSet) {
			if (topic.getID().equals(topicId)) {
				return topic;
			}
		}
		
		return null;
	}
	
	private Topic getTopicBySubject(Set<Topic> topicSet, String subject) {
		for (Topic topic : topicSet) {
			if (topic.getSubject().equals(subject)) {
				return topic;
			}
		}
		return null;
	}
	
	public List<Topic> getTopics(String topicType) throws Exception {
		
		if (topicType == null || topicType.trim().isEmpty()) {
			throw new Exception("Tipo inválido");//"Invalid type");
		}
		
		if (topicType.equals("negociacao")) {
			topicType = EntitiesConstants.NEGOTIATION_TOPIC;
		} else if (topicType.equals("offtopic")) {
			topicType = EntitiesConstants.OFF_TOPIC;
		} else if (topicType.equals("todos")){
			topicType = EntitiesConstants.ALL_TOPICS;
		} else {
			throw new Exception("Tipo inexistente");//"Inexistent type");
		}
		
		if (topicType.equals(EntitiesConstants.OFF_TOPIC)) {
			
			List<Topic> offTopicsList = new ArrayList<Topic>(offTopicTopics);
			Collections.sort(offTopicsList);
			return offTopicsList;
		}
		
		else if (topicType.equals(EntitiesConstants.NEGOTIATION_TOPIC)) {
			
			List<Topic> negotiationTopicsList = new ArrayList<Topic>(negotiationTopics);
			Collections.sort(negotiationTopicsList);
			return negotiationTopicsList;
		}
		else {

			List<Topic> allTopicsList = new ArrayList<Topic>(offTopicTopics);
			allTopicsList.addAll(negotiationTopics);
			Collections.sort(allTopicsList);
			return allTopicsList;
		}
		
	}
	
	public void publishItemRegisteredActivity(String itemName) {
		activityHistory.add(new ActivityRegistry(ActivityKind.CADASTRO_DE_ITEM,
				String.format(EntitiesConstants.ITEM_REGISTERED_ACTIVITY, me.getName(), itemName)));
	}

	public void publishOngoingLendingActivity(String itemName, String borrowerName) {
		activityHistory.add(new ActivityRegistry(ActivityKind.EMPRESTIMO_EM_ANDAMENTO,
				String.format(EntitiesConstants.ONGOING_LENDING_ACTIVITY, me.getName(),
				itemName, borrowerName)));		
	}

	public void publishLendingFinishApprovalActivity(String itemName) {
		activityHistory.add(new ActivityRegistry(ActivityKind.TERMINO_DE_EMPRESTIMO,
				String.format(EntitiesConstants.LENDING_END_APPROVAL_ACTIVITY, me.getName(), itemName)));
	}
	
	public void publishInterestOnItemActivity(String itemName, String itemOwnerName) {
		activityHistory.add(new ActivityRegistry(ActivityKind.
				REGISTRO_DE_INTERESSE_EM_ITEM, String.format(
				EntitiesConstants.REGISTER_INTEREST_IN_ITEM_ACTIVITY,
				me.getName(), itemName, itemOwnerName)));		
	}

	public void publishItemRequestActivity(String itemName) {
		activityHistory.add(new ActivityRegistry(ActivityKind.PEDIDO_DE_ITEM,
				String.format(EntitiesConstants.ITEM_REQUEST_PUBLISHED_ACTIVITY, me.getName(),
						itemName)));
	}

	public void republishItemRequestActivity(String name, String itemName) {
		activityHistory.add(new ActivityRegistry(ActivityKind.REPUBLICACAO_DE_PEDIDO_DE_ITEM,
				String.format(EntitiesConstants.ITEM_REQUEST_PUBLISHED_ACTIVITY, name, itemName)));
	}
	
	public List<ActivityRegistry> getMyActivityHistory() {
		List<ActivityRegistry> actReg = new ArrayList<ActivityRegistry>(activityHistory);
		Collections.sort(actReg);
		return actReg;
	}

	public void publishFriendshipAcceptedActivity(String otherUser) {
		activityHistory.add(new ActivityRegistry(
				ActivityRegistry.ActivityKind.ADICAO_DE_AMIGO_CONCLUIDA
				, String.format(EntitiesConstants.FRIENDSHIP_ACCEPTED_ACTIVITY,
				me.getName(), otherUser)));
	}

	public String sendMessage(String subject, String message, User receiver,
			String lendingId) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty()) {
			throw new Exception("Identificador da requisição de empréstimo é inválido");// "Invalid lending identifier"); 
		}
		
		Lending record = me.getLendingByLendingId(lendingId);
		if (record == null) {
			throw new Exception("Requisição de empréstimo inexistente");
				// "Inexistent lending");
		}
		
		storeMessage(subject, message, me.getLogin(), receiver.getLogin(),
				false, lendingId);
		
		//TODO Treat how is it going to deal with the Lending id
		return receiver.getCommunicationManager().storeMessage(subject, message, me.getLogin(),
				receiver.getLogin(), false, lendingId);
	}

	private String storeMessage(String subject, String message, String senderLogin,
			String receiverLogin, boolean isOffTopic, String lendingId) {
		
			return addMessageToTopic(subject, message, senderLogin,
					receiverLogin, isOffTopic, lendingId);
	}

	public String sendMessage(String subject, String message, User receiver){
		storeMessage(subject, message, me.getLogin(), receiver.getLogin(),
				true, "");
		
		return receiver.getCommunicationManager().storeMessage(subject, message, me.getLogin(),
				receiver.getLogin(), true, "");
	}
	
}
