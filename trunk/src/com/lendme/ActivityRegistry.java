package com.lendme;

import java.util.Date;

public class ActivityRegistry implements Comparable<ActivityRegistry>{
	
	public static enum ActivityKind {
		ADICAO_DE_AMIGO_CONCLUIDA, CADASTRO_DE_ITEM, EMPRESTIMO_EM_ANDAMENTO,
		REGISTRO_DE_INTERESSE_EM_ITEM, TERMINO_DE_EMPRESTIMO, PEDIDO_DE_ITEM;
	}
	
	private String id;
	private ActivityKind kind;
	private String description;
	private long timeInNanos;

	public ActivityRegistry(ActivityKind kind, String description, String id) {
		this.kind = kind;
		this.description = description;
		this.timeInNanos = System.nanoTime();
		this.id = id;
	}
	
	public ActivityRegistry(ActivityKind kind, String description) {
		this.kind = kind;
		this.description = description;
		this.timeInNanos = System.nanoTime();
		this.id = Integer.toString(((Object) this).hashCode());
	}
	
	public ActivityRegistry(ActivityKind kind, String description, long timeInNanos) {
		this.kind = kind;
		this.description = description;
		this.timeInNanos = timeInNanos;
		this.id = Integer.toString(((Object) this).hashCode());
	}


	public String getDescription() {
		return this.description;
	}

	public ActivityKind getKind() {
		return kind;
	}
	
	public long getTimeInNanos() {
		return this.timeInNanos;
	}
	
	@Override
	public int compareTo(ActivityRegistry otherAR) {
		return this.timeInNanos > otherAR.timeInNanos ? -1:1;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	public String getId(){
		return id;
	}
	
}
