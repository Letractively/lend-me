package com.lendme.server.entities;

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents an event in the System, such as "User logged", "Item created", etc.
 */

public class EventDate {

	private Date date;
	private long timeInNanos;
	private String eventDescription;

	/**
	 * @param eventDescription - Descricao do evento.
	 * @param date - data de acontecimento do evento.
	 * @param nanoTime - nanoTime do sistema operacional da ocorrencia do evento.
	 */
	public EventDate(String eventDescription, Date date, long nanoTime){
		this.date = date;
		this.eventDescription = eventDescription;
		this.timeInNanos = nanoTime;
	}
	/**
	 * Construtor com valores default para os campos.
	 * O que implica que o dado evento ocorreu no momento atual do sistema.
	 */
	public EventDate(){
		this("", new Date(), System.nanoTime());
	}
	
	/**
	 * Esse contrutor recebe a descricao do evento e a data e o nanotime sao os atuais do sistema.
	 * @param eventDescription - Descricao do evento.
	 */
	public EventDate(String eventDescription){
		this(eventDescription, new Date(), System.nanoTime());
	}

	/**
	 * Retorna a data do evento ocorrido.
	 * @return Date - correspondente a data do objeto.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Retorna a descricao do evento ocorrido.
	 * @return - String - descricao do evento.
	 */
	public String getEventDescription() {
		return eventDescription;
	}
	
	/**
	 * Adiciona dias a data atual do evento.
	 * @param days - quantidade de dias. 
	 */
	public void addDays(int days){
		Calendar today = Calendar.getInstance();
		today.setTime(date);
		today.add(Calendar.DAY_OF_MONTH, days);
		this.date = today.getTime();
	}
	
	/**
	 * Compara pelo Date caso sejam iguais o nanoTime garante a difereciacao dos eventos.
	 * @param otherDate - evento a ser comparado.
	 * @return >0 se o objeto passado como parametro for menor, < 0 se o objeto passado for maior. 
	 */
	public int compareTo(EventDate otherDate){
		return (otherDate.date.compareTo(this.date) != 0) ? otherDate.date.compareTo(this.date)
			: ( (otherDate.timeInNanos > this.timeInNanos) ? 1 : (otherDate.timeInNanos < this.timeInNanos) ? -1 : 0 );
	}
	
	@Override
	public String toString() {
		StringBuilder eventDateSb = new StringBuilder();
		eventDateSb.append(this.eventDescription);
		eventDateSb.append(this.date);
		
		return eventDateSb.toString();
	}
	
}
