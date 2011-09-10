package entities;

import java.util.Set;

import main.LendMe;

public class Profile {

	private Session observer;
	private User owner;
	private Set<User> ownerFriends;
	private Set<Item> ownerItems;
	
	private Profile(Session observer, User user) throws Exception{

		if ( observer == null ){
			throw new Exception("Usuário observador inválido");//"Invalid observer user");
		}
		if ( observer.getLogin() == null || observer.getLogin().trim().isEmpty() ){
			throw new Exception("Login do usuário observador inválido");//"Invalid observer user login");
		}
		if ( user == null ){
			throw new Exception("Usuário dono do perfil inválido");//"Invalid profile owner user");
		}
		if ( user.getFriends() == null ){
			throw new Exception("Amigos do dono do perfil inválidos");//"Invalid profile owner user friends");
		}
		if ( user.getAllItems() == null ){
			throw new Exception("Itens do dono do perfil inválidos");//"Invalid profile owner user items");
		}
		if ( user.getName() == null || user.getName().trim().isEmpty() ){
			throw new Exception("Nome do usuário dono do perfil inválido");//"Invalid profile owner user name");
		}
		if ( user.getLogin() == null || user.getLogin().trim().isEmpty() ){
			throw new Exception("Login do usuário dono do perfil inválido");//"Invalid profile owner user login");
		}
		if ( user.getAddress() == null || user.getAddress().getFullAddress() == null 
				|| user.getAddress().getFullAddress().trim().isEmpty() ){
			throw new Exception("Endereço do usuário dono do perfil inválido");//"Invalid profile owner user address");
		}
		
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
	
	public void update() throws Exception{
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
	
	public static Profile getUserProfile(Session userSession, User user) throws Exception{
		return new Profile(userSession, user);
	}

	public Profile viewOwnProfile() throws Exception{
		return new Profile(observer, LendMe.getUserByLogin(observer.getLogin()));
	}
	
	public Profile viewOtherProfile(User other) throws Exception{
		if ( observer == null ){
			throw new Exception("Nao eh possivel vizualizar este perfil");//"This profile is not accessible");
		}
		if ( observer.getLogin().equals(other.getLogin()) ){
			return viewOwnProfile();
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
	
	public Set<User> getOwnerFriendshipRequests(){
		return owner.getReceivedFriendshipRequests();
	}

	public Session getObserver() {
		return observer;
	}

	public void askForFriendship() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inválida - pedido de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.requestFriendship(owner);
	}

	public void acceptFriendshipRequest() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inválida - aceitação de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.acceptFriendshipRequest(owner);
	}

	public void declineFriendshipRequest() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inválida - negação de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.declineFriendshipRequest(owner);
	}

	public void breakFriendship() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inválida - rompimento de amizade com si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.breakFriendship(owner);
	}
	
	public boolean isFriendOfOwner() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inválida - consulta para amizade com si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		return me.hasFriend(owner);
	}

	public boolean searchByName(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getName().contains(key);
	}

	public boolean searchByLogin(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getLogin().contains(key);
	}
	
	public boolean searchByAddress(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getAddress().getFullAddress().contains(key);
	}
	
}
