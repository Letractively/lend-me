package com.lendme.entities;

/**
 * @author THE LENDERS
 * This class represents a record of an user that is logged in the system.
 *
 */

public class Session {

	private User owner;
	private String id;
	private EventDate startedAt;
	private EventDate finishedAt;
	
	public Session(User owner) throws Exception{
		if(owner == null){
			throw new Exception("Invalid user");
		}
		this.owner = owner;
		this.id = Integer.toString(((Object) this).hashCode());
		this.startedAt = new EventDate(String.format(EntitiesConstants.SESSION_STARTED_MESSAGE, owner.getLogin(), id, startedAt));
		this.finishedAt = null;
	}

	public User getOwner() {
		return this.owner;
	}
	
	public String getId(){
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj){
		if(! (obj instanceof Session)){
			return false;
		}
		
		Session other = (Session) obj;
		
		return hasSameUser(other) && idMatches(other.getId());	
	}
	
	public boolean idMatches(String id){
		return this.getId().equals(id);
	}
	
	public boolean hasSameUser(Session otherSession){
		return this.owner.equals(otherSession.getOwner());
	}
	
	public void finishSession(){
		if ( finishedAt == null ){
			this.finishedAt = new EventDate(String.format(EntitiesConstants.SESSION_FINISHED_MESSAGE, owner, id, finishedAt));
		}
	}
	
	public EventDate startedAt(){
		return startedAt;
	}
	
	public EventDate finishedAt(){
		return finishedAt;
	}
	
	@Override
	public String toString() {
		StringBuilder sessionSB = new StringBuilder();
		sessionSB.append("Owner: " + getOwner().getLogin() + "\n");
		sessionSB.append("Id: " + getId() + "\n");
		sessionSB.append("Started at: " + startedAt().toString() + "\n");
		sessionSB.append("Finished at: " + finishedAt().toString() + "\n");
		return super.toString();
	}
	
}
