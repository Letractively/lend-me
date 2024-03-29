package com.lendme;

import java.util.Comparator;

public class ComparatorOfAddress implements Comparator<User>{

	private Address addressRef;
	
	public ComparatorOfAddress(Address addressRef){
		this.addressRef = addressRef;
	}
	
	@Override
	public int compare(User arg0, User arg1) {
		
		Address argx = arg0.getAddress();
		Address argy = arg1.getAddress();
				
		return ((int)( calcDistanceOfPoints(addressRef.getLatitude(), addressRef.getLongitude(), argx.getLatitude(), argx.getLongitude())
				- calcDistanceOfPoints(addressRef.getLatitude(), addressRef.getLongitude(), argy.getLatitude(), argy.getLongitude())));
	}
	
	private double calcDistanceOfPoints(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x2 - x1, 2))+(Math.pow(y2 - y1, 2));
	}

}
