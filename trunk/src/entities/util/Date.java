package entities.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date {
	GregorianCalendar date = new GregorianCalendar();
	
	public Date(){}
	
	public int getCurrentDayOfYear(){
		return date.get(Calendar.DAY_OF_YEAR);
	}
	
	
}
