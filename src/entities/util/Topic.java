package entities.util;

import java.util.HashSet;
import java.util.Set;

import entities.User;

public class Topic {
	
	private String subject;
	private Set<Message> messages = new HashSet<Message>();
	
	public Topic(String subject) {
		this.subject = subject;
	}

	public Topic(String subject, Set<Message> messages) {
		this.subject = subject;
		this.messages = messages;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public boolean addMessage(String subject, String message, User sender, boolean isOffTopic) {
		return messages.add(new Message(subject, message, sender, isOffTopic));
	}

	public Set<Message> getMessages() {
		return messages;
	}
}
