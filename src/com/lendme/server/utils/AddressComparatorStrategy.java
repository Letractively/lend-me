package com.lendme.server.utils;

import java.util.Comparator;

import com.lendme.server.entities.Address;
import com.lendme.server.entities.User;

/**
 * This class provides an implementation for the compareTo method in
 * Comparator interface for User objects based on the distance of these
 * users to the reference user. It can be seen as a strategy.
 */
public class AddressComparatorStrategy implements Comparator<User>{

	private Address addressRef;
	
	public AddressComparatorStrategy(Address addressRef){
		this.addressRef = addressRef;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(User arg0, User arg1) {
		
		Address argx = arg0.getAddress();
		Address argy = arg1.getAddress();
				
		return ((int)( calcDistanceOfPoints(addressRef.getLatitude(), addressRef.getLongitude(), argx.getLatitude(), argx.getLongitude())
				- calcDistanceOfPoints(addressRef.getLatitude(), addressRef.getLongitude(), argy.getLatitude(), argy.getLongitude())));
	}
	// Calcula a distancia entre dois pontos utilizando suas coordenadas (x e y)
	private double calcDistanceOfPoints(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x2 - x1, 2))+(Math.pow(y2 - y1, 2));
	}

}
