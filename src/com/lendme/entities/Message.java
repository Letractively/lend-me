package com.lendme.entities;


/**
 * @author THE LENDERS
 * This class represents a message in the System, storing info regarding the sender, the receiver,
 * the subject, the message itself as well as a flag indicating whether or not it is a negotiation message.
 *
 */

public class Message implements Comparable<Message> {
	
	private String subject;
	private String message;
	private String sender;
	private String receiver;
	private boolean isOffTopic;
	private String lendingId;
	private EventDate sending;
	
    /**
     * 	
     * @param subject - Titulo da mensagem.
     * @param message - Corpo da mensagem.
     * @param sender - login de quem esta enviando a mensagem.
     * @param receiver - login de quem esta recebendo a mensagem.
     * @param isOfftopic - se eh uma offtopic ou nao.
     */
	public Message(String subject, String message, String sender, 
			String receiver, boolean isOfftopic) {
		this.subject = subject;
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
		this.isOffTopic = isOfftopic;
		this.lendingId = "";
		this.sending = new EventDate();
	}


	/**
	 * Construtor usado no caso da mensagem ser da negociacao de um item.
	 * @param subject - Titulo da mensagem.
     * @param message - Corpo da mensagem.
     * @param sender - login de quem esta enviando a mensagem.
     * @param receiver - login de quem esta recebendo a mensagem.
     * @param isOfftopic - se eh uma offtopic ou nao.
     * @param lendingId - Id do lending em questao.
	 */
	public Message(String subject, String message, String sender,
			String receiver, boolean isOffTopic, String lendingId) {
		this.subject = subject;
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
		this.isOffTopic = isOffTopic;
		this.lendingId = lendingId;
		this.sending = new EventDate();
	}


	/**
	 * Retorna o titulo da mensagem.
	 * @return String - titulo.
	 */
	public String getSubject() {
		return subject;
	}


	/**
	 * Configura um novo titulo para a mensagem.
	 * @param subject - novo titulo.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}


	/**
	 * Retorna o corpo da mensagem.
	 * @return - corpo da mensagem.
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * Configura novo corpo para a mensagem.
	 * @param message - novo corpo.
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * Retorna o login de quem esta enviando a mensagem.
	 * @return String - login.
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Configura o login de quem esta enviando a mensagem.
	 * @param login.
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * Retorna o login de quem esta recebendo a mensagem.
	 * @return String - login.
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * Configura o login de quem esta recebendo a mensagem.
	 * @param login.
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}


	/**
	 * Retorna se a mensagem eh ou nao offtopic.
	 * @return boolean.
	 */
	public boolean isOffTopic() {
		return isOffTopic;
	}


	/**
	 * Configura se a mensagem vai ser offtopic ou nao.
	 * @param isOffTopic
	 */
	public void setOffTopic(boolean isOffTopic) {
		this.isOffTopic = isOffTopic;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getSubject());
		sb.append(this.getMessage());
		sb.append(this.getSender().toString());
		sb.append(this.isOffTopic());
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Message)) {
			return false;
		}
		
		Message otherMsg = (Message)obj;
		return this.getSubject().equals(otherMsg.getSubject()) 
				&& this.getMessage().equals(otherMsg.getMessage()) 
				&& this.getSender().equals(otherMsg.getSender())
				&& this.isOffTopic() == otherMsg.isOffTopic();
	}

	/**
	 * Retorna o tempo no qual a mensagem foi enviada.
	 * @return EventDate - tempo.
	 */
	public EventDate getDate() {
		return this.sending;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Message otherMsg) {
		return - this.getDate().compareTo(otherMsg.getDate());
	}

	/**
	 * Caso a mensagem nao seja offtopic ela esta associada a alguma negociacao de emprestimo, nesse caso
	 * esse metodo retorna o id dessa negociacao (Lending).
	 * @return id.
	 */
	public String getLendingId() {
		return lendingId;
	}

}