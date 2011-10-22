package com.lendme;

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents an event in the System, such as "User logged", "Item created", etc.
 */

public class EventDate {

	private Date date;
	private long timeInNanos;
	private String eventDescription;

	public EventDate(String eventDescription, Date date, long nanoTime){
		this.date = date;
		this.eventDescription = eventDescription;
		this.timeInNanos = nanoTime;
	}
	
	public EventDate(){
		this("", new Date(), System.nanoTime());
	}
	
	public EventDate(String eventDescription){
		this(eventDescription, new Date(), System.nanoTime());
	}

	public Date getDate() {
		return date;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
	public void addDays(int days){
		Calendar today = Calendar.getInstance();
		today.setTime(date);
		today.add(Calendar.DAY_OF_MONTH, days);
		this.date = today.getTime();
	}
	
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
