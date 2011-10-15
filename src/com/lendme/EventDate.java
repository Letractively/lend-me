package com.lendme;

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents an event in the System, such as "User logged", "Item created", etc.
 */

public class EventDate {

	private Date date;
	private String eventDescription;
	
	public EventDate(){
		this.date = new Date();
		this.eventDescription = "";
	}
	
	public EventDate(String eventDescription){
		this.date = new Date();
		this.eventDescription = eventDescription;
	}
	
	public EventDate(String eventDescription, Date date){
		this.date = date;
		this.eventDescription = eventDescription;
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
	
	@Override
	public String toString() {
		StringBuilder eventDateSb = new StringBuilder();
		eventDateSb.append(this.eventDescription);
		eventDateSb.append(this.date);
		
		return eventDateSb.toString();
	}
	
}
