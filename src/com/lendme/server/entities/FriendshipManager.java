package com.lendme.server.entities;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe que gerencia as requisicoes e pedidos de amizade do usuario.
 * @author THE LENDERS.
 *
 */
public class FriendshipManager {

	User me;
	Set<User> friends;
	Set<User> sentFriendshipRequests;
	Set<User> receivedFriendshipRequests;
	
	/**
	 * Recebe o usuario que sera gereciado.
	 * @param User - user
	 */
	public FriendshipManager(User user) {
		me = user;
		friends = new HashSet<User>();
		sentFriendshipRequests = new HashSet<User>();
		receivedFriendshipRequests = new HashSet<User>();
	}
	
	/**
	 * Retorna os amigos atuais do usuario.
	 * @return Set<User> -amigos atuais do usuario.
	 */
	public Set<User> getFriends(){
		return friends;
	}
	
	/**
	 * Retorna o conjuto de todas as requisicoes de amizade recebidas.
	 * @return Set<User> - Usuarios que requisitaram amizade.
	 */
	public Set<User> getReceivedFriendshipRequests(){
		return receivedFriendshipRequests;
	}

	/**
	 * Retorna o conjunto de todas as requisicoes de amizades feitas.
	 * @return Set<User> - Requisicoes de amizade feitas. 
	 */
	public Set<User> getSentFriendshipRequests(){
		return sentFriendshipRequests;
	}
	
	/**
	 * Envia uma requisicao de amizade para um dado um usuario do sistema.
	 * @param user - Usuario que recebera a requisicao de amizade.
	 * @throws Exception - Caso a amizade seja pedida para si mesmo, ou a requisicao ja tenha sido enviada, ou os usuarios ja sejam amigos.
	 */
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
		user.getUserOperationManager().getFriendshipManager().receiveFriendshipRequest(me);
	}

	/**
	 * Metodo que recebe a requisicao de amizade e armazena.
	 * @param user - Usuario que enviou a requisicao.
	 * @throws Exception - Caso a amizade seja pedida para si mesmo, ou a requisicao ja tenha sido enviada, ou os usuarios ja sejam amigos.
	 */
	public void receiveFriendshipRequest(User user) throws Exception{
		receivedFriendshipRequests.add(user);
	}
	
	/**
	 * Aceita requisicao de amizade de um usuario.
	 * @param user - Usuario.
	 * @throws Exception - Caso a amizade seja pedida para si mesmo, ou a requisicao ja tenha sido enviada, ou os usuarios ja sejam amigos.
	 */
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
		user.getUserOperationManager().getFriendshipManager().acceptRequestMirror(me);
		receivedFriendshipRequests.remove(user);
		friends.add(user);
	}

	/**
	 * Faz a amizade ser aprovada tambem no usuario que a solicitou.
	 * @param user - Usuario.
	 */
	public void acceptRequestMirror(User user){
		sentFriendshipRequests.remove(user);
		friends.add(user);
	}
	
	/**
	 * Funciona como uma rejeicao de uma requisicao de amizade.
	 * @param user - Usuario.
	 * @throws Exception - Caso a amizade seja pedida para si mesmo, ou a requisicao ja tenha sido enviada, ou os usuarios ja sejam amigos.
	 */
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
			user.getUserOperationManager().getFriendshipManager().declineRequestMirror(me);
		}
		receivedFriendshipRequests.remove(user);
	}
	
	/**
	 * Funciona como rejeicao de uma requisicao no usuario que pediu a amizade.
	 * @param user - Usuario.
	 * @throws Exception
	 */
	public void declineRequestMirror(User user) throws Exception{
		sentFriendshipRequests.remove(user);
	}

	/**
	 * Verifica se um usuario eh amigo de outro ou nao.
	 * @param user - Usuario.
	 * @return boolean - eh amigo ou nao.
	 * @throws Exception - Caso a busca seja por si mesmo.
	 */
	public boolean hasFriend(User user) throws Exception{
		if ( me.equals(user) ){
			throw new Exception("Amizade inválida - consulta para amizade com si próprio");//"Invalid friendship");
		}
		return friends.contains(user);
	}
	
	public void removeFriend(User user) {
		friends.remove(user);
	}

	/**
	 * Desfazer uma amizade.
	 * @param user - Usuario.
	 * @throws Exception - Caso a amizade nao exista.
	 */
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
		
		FriendshipManager other = user.getUserOperationManager().getFriendshipManager();
		
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