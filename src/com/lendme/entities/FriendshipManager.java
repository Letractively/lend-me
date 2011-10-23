package com.lendme.entities;

import java.util.List;
import java.util.Set;

public class FriendshipManager extends RequestManager<User> {

	public FriendshipManager(User user) {
		super(user);
	}
	
	public Set<User> getFriends(){
		return acceptedRequests;
	}
	
	public Set<User> getReceivedFriendshipRequests(){
		return receivedRequests;
	}

	public Set<User> getSentFriendshipRequests(){
		return sentRequests;
	}
	
	@Override
	public void sendingAction(User request) throws Exception{
		if ( sentRequests.contains(request) ){
			throw new Exception("Requisição já solicitada");//"The request has already been sent");
		}
		if ( acceptedRequests.contains(request) ){
			throw new Exception("Os usuários já são amigos");//"The users are already friends");
		}
		request.getFriendshipManager().receiveRequest(me);
	}

	@Override
	public void receivingAction(User request) {
		//Nothing special
	}

	@Override
	public void acceptingAction(User request) throws Exception{
		if ( acceptedRequests.contains(request) ){
			throw new Exception("Os usuários já são amigos");//"The users are already friends");
		}
		if ( !receivedRequests.contains(request) ){
			throw new Exception("Requisição de amizade inexistente");//Inexistent friendship request");
		}
		else{

			ActivityRegistry friendshipAccepted = new ActivityRegistry(
					ActivityRegistry.ActivityKind.ADICAO_DE_AMIGO_CONCLUIDA
					, String.format(EntitiesConstants.FRIENDSHIP_ACCEPTED_ACTIVITY,
					me.getName(), request.getName()));
			List<ActivityRegistry> activities = me.getMyActivityHistory();
			activities.add(friendshipAccepted);
			me.setMyActivityHistory(activities);

			request.getFriendshipManager().acceptingActionMirror(me);

			friendshipAccepted = new ActivityRegistry(
					ActivityRegistry.ActivityKind.ADICAO_DE_AMIGO_CONCLUIDA
					, String.format(EntitiesConstants.FRIENDSHIP_ACCEPTED_ACTIVITY,
					request.getName(), me.getName()));
			activities = request.getMyActivityHistory();
			activities.add(friendshipAccepted);			
			request.setMyActivityHistory(activities);
		}
	}

	@Override
	public void acceptingActionMirror(User request){
		sentRequests.remove(request);
		acceptedRequests.add(request);
	}
	
	@Override
	public void decliningAction(User request) throws Exception{
		if ( acceptedRequests.contains(request) ){
			throw new Exception("Os usuários já são amigos");//"The users are already friends");
		}
		if ( !receivedRequests.contains(request) ){
			throw new Exception("Requisição de amizade inexistente");//Inexistent friendship request");
		}
		else{
			request.getFriendshipManager().decliningActionMirror(me);
		}
	}
	
	@Override
	public void decliningActionMirror(User request) throws Exception{
		receivedRequests.remove(request);
	}

	public boolean hasFriend(User otherUser) {
		return acceptedRequests.contains(otherUser);
	}
	
	public void removeFriend(User otherUser) {
		removeAcceptedRequest(otherUser);
	}

	public void breakFriendship(User user) {
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