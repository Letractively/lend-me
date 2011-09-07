package entities;

import java.util.Set;

public class Profile {

	private Session viewer;
	private User owner;
	private Set<User> ownerFriends;
	private Set<Item> ownerItems;
	
	private Profile(Session viewer, User user){
		this.viewer = viewer;
		owner = user;
		ownerFriends = owner.getFriends();
		if ( viewer.getLogin().equals(user.getLogin()) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.getLogin().equals(viewer.getLogin())){
					ownerItems = owner.getAllItems();
				}
			}
		}
	}
	
	public static Profile getUserProfile(Session userSession, User user) {
		return new Profile(userSession, user);
	}

	public Profile viewOtherProfile(User other) throws Exception{
		if ( viewer == null ){
			throw new Exception("Nao eh possivel vizualizar este perfil");//"This profile is not accessible");
		}
		return new Profile(viewer, other);
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
	
	public String getName(){
		return owner.getName();
	}

	public String getLogin(){
		return owner.getLogin();
	}
	
	public String getAddress(){
		return owner.getAddress().getFullAddress();
	}

	public Session getViewer() {
		return viewer;
	}
	
}
