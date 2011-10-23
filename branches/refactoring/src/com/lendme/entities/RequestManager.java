package com.lendme.entities;

import java.util.HashSet;
import java.util.Set;

public abstract class RequestManager<K> {

	User me;
	Set<K> acceptedRequests;
	Set<K> sentRequests;
	Set<K> receivedRequests;
	
	public RequestManager(User user){
		me = user;
		acceptedRequests = new HashSet<K>();
		sentRequests = new HashSet<K>();
		receivedRequests = new HashSet<K>();
	}

	public void sendRequest(K request) throws Exception{
		sendingAction(request);
		sentRequests.add(request);
	}
	
	public void receiveRequest(K request) throws Exception{
		receivingAction(request);
		receivedRequests.add(request);
	}
	
	public abstract void sendingAction(K request) throws Exception;
	
	public abstract void receivingAction(K request) throws Exception;
	
	public void acceptRequest(K request) throws Exception{
		acceptingAction(request);
		receivedRequests.remove(request);
		acceptedRequests.add(request);
	}
	
	public abstract void acceptingAction(K request) throws Exception;
	
	public abstract void acceptingActionMirror(K request) throws Exception;
	
	public void declineRequest(K request) throws Exception{
		decliningAction(request);
		sentRequests.remove(request);
	}

	public abstract void decliningAction(K request) throws Exception;
	
	public abstract void decliningActionMirror(K request) throws Exception;
	
	public void removeAcceptedRequest(K request){
		acceptedRequests.remove(request);
	}
	
}