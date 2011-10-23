package com.lendme;

import java.util.List;
import java.util.Set;

import com.lendme.entities.Message;
import com.lendme.entities.Session;
import com.lendme.entities.Topic;
import com.lendme.entities.User;

public class LendMeCommunicationModule {
	
	private LendMeUserModule userModule;
	
	public LendMeCommunicationModule(){
		 this.userModule = new LendMeUserModule();
	}
	
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
	public  String sendMessage(Session senderSession, String subject, String message, 
			User receiver, String lendingId) throws Exception {
		
		Profile solicitorViewer = null;
		try {
			solicitorViewer = userModule.getUserProfile(senderSession);
		
			solicitorViewer = solicitorViewer.viewOtherProfile(
					receiver);
		
			return solicitorViewer.sendMessage(subject, message, lendingId);
			
		} catch (Exception e) {
			
			if (e.getMessage().equals("Login inválido")) {
				throw new Exception("Destinatário inválido");//"Invalid receiver");
			}
			
			else if (e.getMessage().equals("Usuário inexistente")) {
				throw new Exception("Destinatário inexistente");//"Inexistent receiver");
			}
			
			else if (e.getMessage().equals("Empréstimo inexistente")) {
				throw new Exception("Requisição de empréstimo inexistente" );
			}
			
			throw e;
		}
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
	public  String sendMessage(Session senderSession, String subject, String message, 
			User receiver) throws Exception {
		
		Profile solicitorViewer = null;
		try {
			
			solicitorViewer = userModule.getUserProfile(senderSession);
		
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
	public  List<Topic> getTopics(Session session, String topicType)
			throws Exception {
		Profile solicitorViewer = userModule.getUserProfile(session);
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
	public  List<Message> getTopicMessages(Session session, String topicId)
			throws Exception {
		Profile solicitorViewer = userModule.getUserProfile(session);
		return solicitorViewer.getTopicMessages(topicId);
	}
}
