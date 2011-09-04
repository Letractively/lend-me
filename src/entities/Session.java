package entities;

import entities.util.EventDate;

public class Session {

	private String login;
	private String id;
	private final String SESSION_STARTED_MESSAGE = "User %s logged in with session %s at %s.";
	private final String SESSION_FINISHED_MESSAGE = "User %s logged out with session %s at %s.";
	private EventDate startedAt;
	private EventDate finishedAt;
	
	public Session(String login) throws Exception{
		if(login == null || login.trim().isEmpty()){
			throw new Exception("Invalid login");
		}
		this.login = login;
		this.id = Integer.toString(((Object) this).hashCode());
		this.startedAt = new EventDate(String.format(SESSION_STARTED_MESSAGE, login, id, startedAt));
		this.finishedAt = null;
	}

	public String getLogin() {
		return this.login;
	}
	
	public String getId(){
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj){
		if(! (obj instanceof Session)){
			return false;
		}
		
		return ((Session) obj).getLogin().equals(login) &&
			   ((Session) obj).getId().equals(this.id);	
	}
	
	public boolean hasSameUser(Session otherSession){
		return this.login.equals(otherSession.getLogin());
	}
	
	public void finishSession(){
		this.finishedAt = new EventDate(String.format(SESSION_FINISHED_MESSAGE, login, id, finishedAt));
	}
	
	public EventDate startedAt(){
		return startedAt;
	}
	
	public EventDate finishedAt(){
		return finishedAt;
	}
	
}
