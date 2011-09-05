package entities.util;

import java.util.HashSet;
import java.util.Set;

import entities.Identifiable;
import entities.User;

public class Topic implements Identifiable {
	
	private String subject;
	private Set<Message> messages = new HashSet<Message>();
	private String id;
	
	public Topic(String subject) {
		this.subject = subject;
		this.id = Integer.toString(((Object) this).hashCode());
	}

	public Topic(String subject, Set<Message> messages) {
		this.subject = subject;
		this.messages = messages;
		this.id = Integer.toString(((Object) this).hashCode());
		
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public boolean addMessage(String subject, String message, User sender,
			boolean isOffTopic, String lendingId) {
		return messages.add(new Message(subject, message, sender, isOffTopic,
				lendingId));
	}

	public Set<Message> getMessages() {
		return messages;
	}

	@Override
	public String getId() {
		return id;
	}
	
}
