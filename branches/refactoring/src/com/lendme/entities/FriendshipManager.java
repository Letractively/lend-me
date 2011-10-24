package com.lendme.entities;

import java.util.HashSet;
import java.util.Set;

public class FriendshipManager {

	User me;
	Set<User> friends;
	Set<User> sentFriendshipRequests;
	Set<User> receivedFriendshipRequests;
	
	public FriendshipManager(User user) {
		me = user;
		friends = new HashSet<User>();
		sentFriendshipRequests = new HashSet<User>();
		receivedFriendshipRequests = new HashSet<User>();
	}
	
	public Set<User> getFriends(){
		return friends;
	}
	
	public Set<User> getReceivedFriendshipRequests(){
		return receivedFriendshipRequests;
	}

	public Set<User> getSentFriendshipRequests(){
		return sentFriendshipRequests;
	}
	
	public void sendFriendshipRequest(User user) throws Exception{
		if ( me.equals(user) ){
			throw new Exception("Amizade inexistente");//inválida - pedido de amizade para si próprio");//"Invalid friendship");
		}
		if ( sentFriendshipRequests.contains(user) ){
			throw new Exception("Requisição já solicitada");//"The user has already been sent");
		}
		if ( friends.contains(user) ){
			throw new Exception("Os usuários já são amigos");//"The users are already friends");
		}
		sentFriendshipRequests.add(user);
		user.getFriendshipManager().receiveFriendshipRequest(me);
	}

	public void receiveFriendshipRequest(User user) throws Exception{
		receivedFriendshipRequests.add(user);
	}
	
	public void acceptFriendshipRequest(User user) throws Exception{
		if ( me.equals(user) ){
			throw new Exception("Amizade inexistente");//inválida - aceitação de amizade para si próprio");//"Invalid friendship");
		}
		if ( friends.contains(user) ){
			throw new Exception("Os usuários já são amigos");//"The users are already friends");
		}
		if ( !receivedFriendshipRequests.contains(user) ){
			throw new Exception("Requisição de amizade inexistente");//Inexistent friendship request");
		}
		user.getFriendshipManager().acceptRequestMirror(me);
		receivedFriendshipRequests.remove(user);
		friends.add(user);
	}

	public void acceptRequestMirror(User user){
		sentFriendshipRequests.remove(user);
		friends.add(user);
	}
	
	public void declineFriendshipRequest(User user) throws Exception{
		if ( me.equals(user) ){
			throw new Exception("Amizade inexistente");//inválida - negação de amizade para si próprio");//"Invalid friendship");
		}
		if ( friends.contains(user) ){
			throw new Exception("Os usuários já são amigos");//"The users are already friends");
		}
		if ( !receivedFriendshipRequests.contains(user) ){
			throw new Exception("Requisição de amizade inexistente");//Inexistent friendship request");
		}
		else{
			user.getFriendshipManager().declineRequestMirror(me);
		}
		sentFriendshipRequests.remove(user);
	}
	
	public void declineRequestMirror(User user) throws Exception{
		receivedFriendshipRequests.remove(user);
	}

	public boolean hasFriend(User user) throws Exception{
		if ( me.equals(user) ){
			throw new Exception("Amizade inválida - consulta para amizade com si próprio");//"Invalid friendship");
		}
		return friends.contains(user);
	}
	
	public void removeFriend(User user) {
		friends.remove(user);
	}

	public void breakFriendship(User user) throws Exception{
		if ( me.equals(user) ){
			throw new Exception("Amizade inexistente");//inválida - rompimento de amizade com si próprio");//"Invalid friendship");
		}
		if ( !me.hasFriend(user) ){
			throw new Exception("Amizade inexistente");//inválida - rompimento de amizade com alguem que não é seu amigo");
		}
		if (hasFriend(user)) {
			removeFriend(user);
			for (Lending record : me.getReceivedItemRequests()) {
				if (record.getBorrower().equals(user))
					me.getReceivedItemRequests().remove(record);
			}
			for (Lending record : me.getSentItemRequests()) {
				if (record.getLender().equals(user))
					me.getSentItemRequests().remove(record);
			}
		}
		
		FriendshipManager other = user.getFriendshipManager();
		
		if (other.hasFriend(me)) {
			other.removeFriend(me);
			for (Lending record : user.getReceivedItemRequests()) {
				if (record.getBorrower().equals(me))
					user.getReceivedItemRequests().remove(record);
			}
			for (Lending record : user.getSentItemRequests()) {
				if (record.getLender().equals(me))
					user.getSentItemRequests().remove(record);
			}
		}

	}

}