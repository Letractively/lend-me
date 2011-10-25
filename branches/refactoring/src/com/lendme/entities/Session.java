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
	
	/**
	 * @param owner - dono da sessao.
	 * @throws Exception - caso seja nulo.
	 */
	public Session(User owner) throws Exception{
		if(owner == null){
			throw new Exception("Invalid user");
		}
		this.owner = owner;
		this.id = Integer.toString(((Object) this).hashCode());
		this.startedAt = new EventDate(String.format(EntitiesConstants.SESSION_STARTED_MESSAGE, owner.getLogin(), id, startedAt));
		this.finishedAt = null;
	}

	/**
	 * Retorna dodo da sessao.
	 * @return User.
	 */
	public User getOwner() {
		return this.owner;
	}
	
	/**
	 * Retorna o id da sessao.
	 * @return id.
	 */
	public String getId(){
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if(! (obj instanceof Session)){
			return false;
		}
		
		Session other = (Session) obj;
		
		return hasSameUser(other) && idMatches(other.getId());	
	}
	
	/**
	 * Retorna se um id eh igual ao da sessao.
	 * @param id
	 * @return true se os ids forem iguais.
	 */
	public boolean idMatches(String id){
		return this.getId().equals(id);
	}
	
	/**
	 * Verifica se as sessoes tem o mesmo usuario como dono.
	 * @param otherSession
	 * @return true se tiverem usuarios iguais em duas sessoes.
	 */
	public boolean hasSameUser(Session otherSession){
		return this.owner.equals(otherSession.getOwner());
	}
	
	/**
	 * Finaliza uma sessao.
	 */
	public void finishSession(){
		if ( finishedAt == null ){
			this.finishedAt = new EventDate(String.format(EntitiesConstants.SESSION_FINISHED_MESSAGE, owner, id, finishedAt));
		}
	}
	
	/**
	 * Retorna o momento em que a sessao se iniciou.
	 * @return EventDate - momento.
	 */
	public EventDate startedAt(){
		return startedAt;
	}
	
	/**
	 * Retorna o momento em que a sessao encerrou.
	 * @return EventDate - momento.
	 */
	public EventDate finishedAt(){
		return finishedAt;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
