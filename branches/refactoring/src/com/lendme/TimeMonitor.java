package com.lendme;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimeMonitor extends TimerTask{

	private Calendar calendar;
	private static TimeMonitor timeMonitor;
	private static Timer starter;
	
	private TimeMonitor(){
		calendar = GregorianCalendar.getInstance();
		if ( starter != null ){
			starter.cancel();
		}
		else{
			starter = new Timer();
		}
		starter.scheduleAtFixedRate(this, 0, 60*1000);
	}
	
	public static TimeMonitor getInstance() {
		if ( timeMonitor == null ){
			timeMonitor = new TimeMonitor();
		}
		return timeMonitor;
	}

	public Date getTime() {
		return calendar.getTime();
	}

	public void addTime(int fieldIdentifier, int amount) {
		calendar.add(fieldIdentifier, amount);
	}

	@Override
	public void run() {
		addTime(Calendar.MINUTE, 1);
	}
	
	public void stop() {
		starter.cancel();
	}

}