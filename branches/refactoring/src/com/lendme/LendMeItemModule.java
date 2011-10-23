package com.lendme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lendme.LendMe.AtributeForSearch;
import com.lendme.LendMe.CriterionForSearch;
import com.lendme.LendMe.DispositionForSearch;
import com.lendme.entities.Item;
import com.lendme.entities.Lending;
import com.lendme.entities.Session;
import com.lendme.entities.User;
import com.lendme.utils.ComparatorOfItemsStrategy;
import com.lendme.utils.ComparatorOfRankingStrategy;

public class LendMeItemModule {
	
	/**
	 * Returns some item attribute.
	 * @param itemId the item id
	 * @param attribute the user attribute
	 * @return the item attribute value
	 * @throws Exception if parameters are invalid
	 */
	public  String getItemAttribute(String itemId, String attribute, String ownerSessionId,
			Profile viewerProfile) throws Exception{
		return viewerProfile.getItemAttribute(itemId, attribute);
	}
	
	/**
	 * Returns the items of the user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of items
	 * @throws Exception if user doesn't exists
	 */
	public  Set<Item> getItems(Profile viewerProfile) throws Exception {
		return viewerProfile.getOwnerItems();
	}
	
	/**
	 * Returns user lending history.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	public  Collection<Lending> getLendingRecords(Profile viewerProfile, String kind) throws Exception{
		return viewerProfile.getLendingRecords(kind);
	}
	
	/**
	 * Solicited user requests an item from another user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @param requiredDays
	 * @return
	 * @throws Exception
	 */
	public  String requestItem(Profile viewer, String itemId, int requiredDays, Set<User> users) throws Exception {
		User otherUser = getItemOwner(itemId, users);
		viewer = viewer.viewOtherProfile(otherUser);
		return viewer.requestItem(itemId, requiredDays);
	}
	
	/**
	 * Returns the owner of the item.
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public User getItemOwner(String itemId, Set<User> users) throws Exception{
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");//"Invalid item identifier");
		}
		for ( User user : users){
			for ( Item item : user.getAllItems() ){
				if ( item.getID().equals(itemId) ){
					return user;
				}
			}
		}
		throw new Exception("Item inexistente");//"Inexistent item");
	}
	
	/**
	 * Solicited user allows lending of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public  String approveLending(Profile viewer, String requestId)  throws Exception{
		return viewer.approveLending(requestId);
	}
	
	/**
	 * Solicited user denies lending of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public  String denyLending(Profile viewer, String requestId)  throws Exception{
		return viewer.denyLending(requestId);
	}
	
	/**
	 * Solicitor user returns item back.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public  String returnItem(Profile viewer, String requestId) throws Exception{
		return viewer.returnItem(requestId);
	}
	
	/**
	 * Lender confirms returning of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public  String confirmLendingTermination(Profile viewer,
			String lendingId) throws Exception{
		return viewer.confirmLendingTermination(lendingId);
	}
	
	/**
	 * Lender denies returning of lending.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public  String denyLendingTermination(Profile viewer,
			String lendingId) throws Exception{
		return viewer.denyLendingTermination(lendingId);
	}
	
	/**
	 * Lender asks his item back.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public  String askForReturnOfItem(Profile viewer,
			String lendingId, Date date) throws Exception{
		return viewer.askForReturnOfItem(lendingId, date);
	}
	
	/**
	 * Returns the lending record with specific lending id.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public  Lending getLendingByLendingId(String lendingId, Set<User> users) throws Exception{
		for ( User user : users ){
			Lending record = user.getLendingByLendingId(lendingId);
			if ( record != null ){
				return record;
			}
		}
		throw new Exception("Empréstimo inexistente");
	}
	
	/**
	 * Returns lending record with specific request id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public  Lending getLendingByRequestId(String requestId, Set<User> users) throws Exception{
		for ( User user : users){
			Lending record = user.getLendingByRequestId(requestId);
			if ( record != null ){
				return record;
			}
		}
		throw new Exception("Requisição de empréstimo inexistente");
	}
	
	
	/**
	 * Solicitor removes item from his item set.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @throws Exception
	 */
	public  void deleteItem(User userOwner, String itemId) throws Exception {
		userOwner.deleteMyItem(itemId);
	}
	
	/**
	 * Searches for specific with specific criteria and disposals.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param key
	 * @param attribute
	 * @param disposal
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	public  List<Item> searchForItem(User ownerUser, String key, String attribute,
			String disposal ,String criteria) throws Exception{
		List<Item> results = new ArrayList<Item>();
		AtributeForSearch atributeAux = AtributeForSearch.DESCRICAO;
		CriterionForSearch criterionAux = CriterionForSearch.DATACRIACAO;
		
		if(key == null || key.trim().isEmpty()){
			throw new Exception("Chave inválida");//"invalid key"
		}
		if(attribute == null || attribute.trim().isEmpty()){
			throw new Exception("Atributo inválido");
		}
		if(!Arrays.toString(AtributeForSearch.values()).toLowerCase().contains(attribute.toLowerCase())){
			throw new Exception("Atributo inexistente");
		}
		if(disposal == null || disposal.trim().isEmpty()){
			throw new Exception("Tipo inválido de ordenação");
		}
		if(!Arrays.toString(DispositionForSearch.values()).toLowerCase().contains(disposal.toLowerCase())){
			throw new Exception("Tipo de ordenação inexistente");
		}
		if(criteria == null || criteria.trim().isEmpty()){
			throw new Exception("Critério inválido de ordenação");
		}
		if(!Arrays.toString(CriterionForSearch.values()).toLowerCase().contains(criteria.toLowerCase())){
			throw new Exception("Critério de ordenação inexistente");
		}
		
		for(AtributeForSearch actual : AtributeForSearch.values()){
			if(actual.toString().toLowerCase().contains(attribute.toLowerCase()))
				atributeAux = actual;
		}
		
		for(CriterionForSearch actual : CriterionForSearch.values()){
			if(actual.toString().toLowerCase().contains(criteria.toLowerCase()))
				criterionAux = actual;
		}

		
		switch(atributeAux){
	
		case DESCRICAO:{
			for(User actualFriend : ownerUser.getFriends()){
				for(Item itemOfMyFriend : actualFriend.getAllItems()){
					if(itemOfMyFriend.getDescription().toUpperCase().contains(key.toUpperCase()))
						results.add(itemOfMyFriend);
				}
			}
			 break;
		}
		
		case NOME:{
			for(User actualFriend : ownerUser.getFriends()){
				for(Item itemOfMyFriend : actualFriend.getAllItems()){
					if(itemOfMyFriend.getName().toUpperCase().contains(key.toUpperCase()))
						results.add(itemOfMyFriend);
				}
			}
			 break;
		}
		
		case ID: {
			for(User actualFriend : ownerUser.getFriends()){
				for(Item itemOfMyFriend : actualFriend.getAllItems()){
					if(itemOfMyFriend.getID().toUpperCase().contains(key.toUpperCase()))
						results.add(itemOfMyFriend);
				}
			}
			 break;
		}
		
		case CATEGORIA: {
			Set<User> searchScope = ownerUser.getFriends();
			searchScope.add(ownerUser);
			for(User actualFriend : searchScope){
				for(Item itemOfMyFriend : actualFriend.getAllItems()){
					if(itemOfMyFriend.getCategory().toUpperCase().contains(key.toUpperCase()))
						results.add(itemOfMyFriend);
				}
			}
			 break;
		}
		
		default:	throw new Exception("Atributo  inválido");
	}

		switch (criterionAux) {

		case DATACRIACAO: {
			Collections.sort(results);
			if (DispositionForSearch.CRESCENTE.toString().toLowerCase().contains(disposal.toLowerCase())) {
				return results;

			} else {
				Collections.reverse(results);
				return results;

			}
		}

		case REPUTACAO: {
			Collections.sort(results, new ComparatorOfItemsStrategy());
			if (DispositionForSearch.CRESCENTE.toString().toLowerCase().contains(disposal.toLowerCase())) {
				return results;
			} else {
				Collections.reverse(results);
				return results;
			}
		}

		default:
			return results;
		}
		
	}
	
	/**
	 * Solicitor registers interest for a specific item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @throws Exception
	 */
	public  void registerInterestForItem(Profile viewer, String itemId, Set<User> users) throws Exception{
		viewer = viewer.viewOtherProfile(getItemOwner(itemId, users));
		viewer.registerInterestForItem(itemId);
	}
	
	/**
	 * Get system user ranking.
	 * 
	 * @param sessionId
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public  String getRanking(String category, Session actualSession, Set<User> users) throws Exception{
		String ranking = "";
		
		if(actualSession == null){
			throw new Exception("Sessão inexistente");
		}
		
		String sessionId = actualSession.getId();
		
		if(sessionId == null || sessionId.trim().equals("")){
			throw new Exception("Sessão inválida");
		}
				
		if(category == null || category.trim().equals("")){
			throw new Exception("Categoria inválida");
		}
		
		if(!category.equals("global") && !category.equals("amigos")){
			throw new Exception("Categoria inexistente");
		}
		
		if(category.equals("amigos")){
			User user = actualSession.getOwner();
			User[] friendList = user.getFriends().toArray(new User[user.getFriends().size() + 1]);
			friendList[user.getFriends().size()] = user;
			
			Arrays.sort(friendList, new ComparatorOfRankingStrategy());
			for(User current : friendList){
				ranking = current.getLogin() + "; " + ranking;
			}
		}
		if(category.equals("global")){
			User[] usersList = users.toArray(new User[users.size()]);
			
			Arrays.sort(usersList, new ComparatorOfRankingStrategy());
			for(User current : usersList){
				ranking = current.getLogin() + "; " + ranking;
			}
		}
		ranking = ranking + "-";
	
		return ranking.replace("; -", "");
	}
	
	/**
	 * Returns the user received item requests.
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public  Set<Lending> getReceivedItemRequests(Profile viewer)
			throws Exception {
		return viewer.getReceivedItemRequests();
	}
	
	

}
