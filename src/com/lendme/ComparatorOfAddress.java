package com.lendme;

import java.util.Comparator;

public class ComparatorOfAddress implements Comparator<Address>{

	private Address addressRef;
	
	public ComparatorOfAddress(Address addressRef){
		this.addressRef = addressRef;
	}
	
	@Override
	public int compare(Address arg0, Address arg1) {
		return ((int)( calcDistanceOfPoints(addressRef.getLatitude(), addressRef.getLongitude(), arg0.getLatitude(), arg0.getLongitude())
				- calcDistanceOfPoints(addressRef.getLatitude(), addressRef.getLongitude(), arg1.getLatitude(), arg1.getLongitude())));
	}
	
	private double calcDistanceOfPoints(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x2 - x1, 2))+(Math.pow(y2 - y1, 2));
	}

}
