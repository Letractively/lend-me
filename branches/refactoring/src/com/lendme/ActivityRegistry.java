package com.lendme;


public class ActivityRegistry implements Comparable<ActivityRegistry>{
	
	public static enum ActivityKind {
		ADICAO_DE_AMIGO_CONCLUIDA, CADASTRO_DE_ITEM, EMPRESTIMO_EM_ANDAMENTO,
		REGISTRO_DE_INTERESSE_EM_ITEM, TERMINO_DE_EMPRESTIMO, PEDIDO_DE_ITEM,
		REPUBLICACAO_DE_PEDIDO_DE_ITEM;
	}
	
	private String id;
	private ActivityKind kind;
	private String description;
	private EventDate time;
	
	public ActivityRegistry(ActivityKind kind, String description) {
		this.kind = kind;
		this.description = description;
		this.time = new EventDate();
		this.id = Integer.toString(((Object) this).hashCode());
	}

	public String getDescription() {
		return this.description;
	}

	public ActivityKind getKind() {
		return kind;
	}
	
	public EventDate getTime() {
		return this.time;
	}
	
	@Override
	public int compareTo(ActivityRegistry otherAR) {
		return this.time.compareTo(otherAR.time);
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	public String getId(){
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!( obj instanceof ActivityRegistry)) {
			return false;
		}
		
		ActivityRegistry otherAR = (ActivityRegistry) obj;
		
		if ( ( this.time.compareTo(otherAR.time) == 0 ) && getDescription().equals(otherAR.getDescription())) {
			return true;
		} else if ((getKind() == ActivityKind.REPUBLICACAO_DE_PEDIDO_DE_ITEM || 
			otherAR.getKind() == ActivityKind.REPUBLICACAO_DE_PEDIDO_DE_ITEM)
			&& this.getDescription().equals(otherAR.getDescription()) ) {
				return true;
		} else {
			return false;
		}
	}
	
}
