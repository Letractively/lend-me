package com.lendme;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimeMonitor extends TimerTask{

	private Calendar timeCalendar;
	private static Timer timeMonitor;
	private static TimeMonitor hook;
	
	private TimeMonitor(){
		timeCalendar = GregorianCalendar.getInstance();
		if ( timeMonitor != null ){
			timeMonitor.cancel();
		}
		else{
			timeMonitor = new Timer();
		}
		timeMonitor.scheduleAtFixedRate(this, 0, 60*1000);
	}
	
	public static TimeMonitor getInstance() {
		if ( hook == null ){
			hook = new TimeMonitor();
		}
		return hook;
	}

	public Date getTime() {
		return timeCalendar.getTime();
	}

	public void addTime(int fieldIdentifier, int amount) {
		timeCalendar.add(fieldIdentifier, amount);
	}

	@Override
	public void run() {
		addTime(Calendar.MINUTE, 1);
	}
	
	public void stop() {
		timeMonitor.cancel();
	}

}