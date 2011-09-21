package com.lendme;

import java.util.Date;

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


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getReceiver() {
		return receiver;
	}


	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	public Date getDate() {
		return this.sending.getDate();
	}

	@Override
	public int compareTo(Message otherMsg) {
		return this.getDate().before(otherMsg.getDate())? -1:1;
	}

	public String getLendingId() {
		return lendingId;
	}

}