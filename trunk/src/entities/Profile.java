package entities;

import java.util.Set;

public class Profile {

	private Session observer;
	private User owner;
	private Set<User> ownerFriends;
	private Set<Item> ownerItems;
	
	private Profile(Session observer, User user){
		this.observer = observer;
		owner = user;
		ownerFriends = owner.getFriends();
		if ( observer.getLogin().equals(user.getLogin()) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.getLogin().equals(observer.getLogin())){
					ownerItems = owner.getAllItems();
					return;
				}
			}
		}
	}
	
	public void update(){
		ownerFriends = owner.getFriends();
		if ( observer.getLogin().equals(owner.getLogin()) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.getLogin().equals(observer.getLogin())){
					ownerItems = owner.getAllItems();
					return;
				}
			}
		}
	}
	
	public static Profile getUserProfile(Session userSession, User user) {
		return new Profile(userSession, user);
	}

	public Profile viewOtherProfile(User other) throws Exception{
		if ( observer == null ){
			throw new Exception("Nao eh possivel vizualizar este perfil");//"This profile is not accessible");
		}
		return new Profile(observer, other);
	}

	public Set<User> getOwnerFriends() {
		return ownerFriends;
	}

	public Set<Item> getOwnerItems() throws Exception{
		if ( ownerItems == null ){
			throw new Exception("O usuário não tem permissão para visualizar estes itens");//"User has no permission to view these items");
		}
		return ownerItems;
	}
	
	public String getOwnerName(){
		return owner.getName();
	}

	public String getOwnerLogin(){
		return owner.getLogin();
	}
	
	public String getOwnerAddress(){
		return owner.getAddress().getFullAddress();
	}

	public Session getObserver() {
		return observer;
	}
	
}
