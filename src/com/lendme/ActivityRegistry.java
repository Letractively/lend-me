package com.lendme;

import java.util.Date;

public class ActivityRegistry implements Comparable<ActivityRegistry>{
	
	public static enum ActivityKind {
		ADICAO_DE_AMIGO_CONCLUIDA, CADASTRO_DE_ITEM, EMPRESTIMO_EM_ANDAMENTO,
		REGISTRO_DE_INTERESSE_EM_ITEM, TERMINO_DE_EMPRESTIMO
	}
	
	private ActivityKind kind;
	private EventDate activityAccomplished;
	private long timeInMillis;

	public ActivityRegistry(ActivityKind kind, String description) {
		this.kind = kind;
		this.activityAccomplished = new EventDate(description);
		this.timeInMillis = System.nanoTime();
	}
	
	public ActivityRegistry(ActivityKind kind, String description, Date date) {
		this.kind = kind;
		this.activityAccomplished = new EventDate(description, date);
	}


	public String getDescription() {
		return this.activityAccomplished.getEventDescription();
	}

	public ActivityKind getKind() {
		return kind;
	}
	
	public Date getDate() {
		return this.activityAccomplished.getDate();
	}
	
	@Override
	public int compareTo(ActivityRegistry otherAR) {
		return this.timeInMillis > otherAR.timeInMillis ? -1:1;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
}
