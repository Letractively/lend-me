package com.lendme;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a group of messages with a given topic, a.k.a. Conversation
 *
 */

public class Topic implements Identifiable, Comparable<Topic> {
	
	private String subject;
	private Set<Message> messages = new HashSet<Message>();
	private String id;
	private EventDate creationDate;
	
	public Topic(String subject) {
		this.subject = subject;
		this.creationDate = new EventDate();
		this.id = Integer.toString(((Object) this).hashCode());
	}

	public Topic(String subject, Set<Message> messages) {
		this.subject = subject;
		this.messages = messages;
		this.creationDate = new EventDate();
		this.id = Integer.toString(((Object) this).hashCode());
		
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public boolean addMessage(String subject, String message, String sender,
			String receiver, boolean isOffTopic, String lendingId) {
		return messages.add(new Message(subject, message, sender, receiver,
				isOffTopic, lendingId));
	}

	public Set<Message> getMessages() {
		return messages;
	}

	@Override
	public String getID() {
		return id;
	}
	
	public Date getDate() {
		return this.creationDate.getDate();
	}

	@Override
	public int compareTo(Topic otherTopic) {
		return this.getDate().after(otherTopic.getDate())? -1 : 1;
	}
	
	@Override
	public String toString() {
		StringBuilder topicSb = new StringBuilder();
		
		topicSb.append(" \tAssunto: " + this.subject + "\n");
		return topicSb.toString();
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Topic)) {
			return false;
		}
		return this.subject.equals(((Topic)obj).getSubject());
	}
	
}
