package com.lendme;

import java.util.List;

import com.lendme.entities.Message;
import com.lendme.entities.Session;
import com.lendme.entities.Topic;
import com.lendme.entities.User;

public class LendMeCommunicationModule {
	
	private static LendMeRepository repository = LendMeRepository.getInstance();
	private LendMeUserModule userModule = new LendMeUserModule();
	
	public LendMeCommunicationModule(){
	}
	
	/**
	 * Returns messages with given topic.
	 * 
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	public  List<Message> getMessagesByTopicId(String topicId) throws Exception{

		for ( User user : repository.getUsers() ){
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
	public  String sendMessage(String senderSessionId, String subject, String message, 
			String receiverLogin, String lendingId) throws Exception {
		
		Profile solicitorViewer = null;
		Session senderSession = null;
		try {
			senderSession = repository.getSessionByID(senderSessionId);
			solicitorViewer = userModule.getUserProfile(senderSession);
		
			solicitorViewer = solicitorViewer.viewOtherProfile(
					repository.getUserByLogin(receiverLogin));
		
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
				//"Inexistent lending");
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
	public  String sendMessage(String senderSessionId, String subject, String message, 
			String receiverLogin) throws Exception {
		
		Profile solicitorViewer = null;
		Session senderSession = null;
		
		try {
			senderSession = repository.getSessionByID(senderSessionId);
			solicitorViewer = userModule.getUserProfile(senderSession);
		
			solicitorViewer = solicitorViewer.viewOtherProfile(
					repository.getUserByLogin(receiverLogin));
		
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
	public  List<Topic> getTopics(String sessionId, String topicType)
			throws Exception {
		Session session  = repository.getSessionByID(sessionId);
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
	public  List<Message> getTopicMessages(String sessionId, String topicId)
			throws Exception {
		Session session  = repository.getSessionByID(sessionId);
		Profile solicitorViewer = userModule.getUserProfile(session);
		return solicitorViewer.getTopicMessages(topicId);
	}
}
