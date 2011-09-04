package entities;

public class Session {

	private String login;
	private String id;
	
	public Session(String login) throws Exception{
		if(login == null || login.trim().isEmpty()){
			throw new Exception("Invalid login");
		}
		this.login = login;
		this.id = Integer.toString(((Object) this).hashCode());
		
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
	
    
}
