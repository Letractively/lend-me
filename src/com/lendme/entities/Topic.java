package com.lendme.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * @author THE LENDERS
 * This class represents a group of messages with a given topic, a.k.a. Conversation
 *
 */

public class Topic implements Identifiable, Comparable<Topic> {
	
	private String subject;
	private Set<Message> messages = new HashSet<Message>();
	private String id;
	private EventDate creationDate;
	
	/**
	 * @param subject - titulo do topico.
	 */
	public Topic(String subject) {
		this.subject = subject;
		this.creationDate = new EventDate();
		this.id = Integer.toString(((Object) this).hashCode());
	}

	/**
	 * @param subject - titulo do topico.
	 * @param messages - mensagens a serem colocadas no topico.
	 */
	public Topic(String subject, Set<Message> messages) {
		this.subject = subject;
		this.messages = messages;
		this.creationDate = new EventDate();
		this.id = Integer.toString(((Object) this).hashCode());
		
	}

	/**
	 * Retorna o titulo do topico.
	 * @return titulo.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Adiciona uma mensagem no topico.
	 * @param subject - titulo da mensagem.
	 * @param message - corpo da mensagem.
	 * @param sender - login de quem esta enviando a mensagem.
	 * @param receiver - login de quem esta recebendo a mensagem.
	 * @param isOffTopic - eh offtopic ou nao.
	 * @param lendingId - O id do lending caso ela nao seja offtopic.
	 * @return
	 */
	public boolean addMessage(String subject, String message, String sender,
			String receiver, boolean isOffTopic, String lendingId) {
		return messages.add(new Message(subject, message, sender, receiver,
				isOffTopic, lendingId));
	}

	/**
	 * Retorna o conjunto de mensagens que estao no topico.
	 * @return conjunto de mensagens.
	 */
	public Set<Message> getMessages() {
		return messages;
	}

	/* (non-Javadoc)
	 * @see com.lendme.entities.Identifiable#getID()
	 */
	@Override
	public String getID() {
		return id;
	}
	
	/**
	 * Retorna o momento de criacao do topico.
	 * @return EventDate - momento.
	 */
	public EventDate getDate() {
		return this.creationDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Topic otherTopic) {
		return this.getDate().compareTo(otherTopic.getDate());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder topicSb = new StringBuilder();
		
		topicSb.append(" \tAssunto: " + this.subject + "\n");
		return topicSb.toString();
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
		if (! (obj instanceof Topic)) {
			return false;
		}
		return this.subject.equals(((Topic)obj).getSubject());
	}
	
}
