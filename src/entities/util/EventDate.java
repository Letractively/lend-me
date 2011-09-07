package entities.util;

import java.util.Date;

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
	
	public EventDate(String date, String eventDescription){
		try{
			this.date = new Date(Date.parse(date));			
		}
		catch (Exception e) {
			this.date = new Date();
		}
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
	
	@Override
	public String toString() {
		StringBuilder eventDateSb = new StringBuilder();
		eventDateSb.append(this.eventDescription);
		eventDateSb.append(this.date);
		
		return eventDateSb.toString();
	}
	
}
