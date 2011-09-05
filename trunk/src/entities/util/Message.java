package entities.util;

import entities.User;

public class Message {
	
	private String subject;
	private String message;
	private User sender;
	private boolean isOffTopic;
	private String lendingId;
	
	
	public Message(String subject, String message, User sender,
			boolean isOfftopic) {
		this.subject = subject;
		this.message = message;
		this.sender = sender;
		this.isOffTopic = isOfftopic;
		this.lendingId = "";
	}


	public Message(String subject, String message, User sender,
			boolean isOffTopic, String lendingId) {
		this.subject = subject;
		this.message = message;
		this.sender = sender;
		this.isOffTopic = isOffTopic;
		this.lendingId = lendingId;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public User getSender() {
		return sender;
	}


	public void setSender(User sender) {
		this.sender = sender;
	}


	public boolean isOffTopic() {
		return isOffTopic;
	}


	public void setOffTopic(boolean isOffTopic) {
		this.isOffTopic = isOffTopic;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getSubject());
		sb.append(this.getMessage());
		sb.append(this.getSender().toString());
		sb.append(this.isOffTopic());
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
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
	
}
