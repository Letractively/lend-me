package com.lendme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lendme.LendMeFacade.AtributeForSearch;
import com.lendme.LendMeFacade.CriteriaForSearch;
import com.lendme.LendMeFacade.DispositionForSearch;
import com.lendme.entities.Item;
import com.lendme.entities.Lending;
import com.lendme.entities.User;
import com.lendme.utils.CategorySearchStrategy;
import com.lendme.utils.CreationDateOrderingStrategy;
import com.lendme.utils.DecreasingOrderingStrategy;
import com.lendme.utils.DescriptionSearchStrategy;
import com.lendme.utils.IDSearchStrategy;
import com.lendme.utils.IncreasingOrderingStrategy;
import com.lendme.utils.NameSearchStrategy;
import com.lendme.utils.UserRankingComparatorStrategy;
import com.lendme.utils.ReputationOrderingStrategy;
import com.lendme.utils.OrderedSearch;

public class LendMeItemModule {
	
	/**
	 * Returns some item attribute.
	 * @param itemId the item id
	 * @param attribute the user attribute
	 * @return the item attribute value
	 * @throws Exception if parameters are invalid
	 */
	public  String getItemAttribute(Viewer viewer, String itemId, String attribute) throws Exception{
		if ( attribute == null || attribute.trim().isEmpty() ){
			throw new Exception("Atributo inválido");//"Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.equals("descricao") || attribute.equals("categoria"))){
			throw new Exception("Atributo inexistente");//"Inexistent attribute");
		}
		
		for ( Item item : viewer.getOwnerItems() ){
			if ( item.getID().equals(itemId) ){
				if ( attribute.equals("nome") ) {
					return item.getName();
				}
				else if ( attribute.equals("descricao") ) {
					return item.getDescription();
				}
				else {
					String formattedCategory = item.getCategory().toString();
					return formattedCategory.substring(0, 1).toUpperCase() 
							+ formattedCategory.substring(1).toLowerCase();
				}
			}
		}
		throw new Exception("Item inexistente");
	}
	
	/**
	 * Returns the items of the user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of items
	 * @throws Exception if user doesn't exists
	 */
	public  Set<Item> getItems(Viewer viewer) throws Exception {
		return viewer.getOwnerItems();
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
	public  Collection<Lending> getLendingRecords(User sessionOwner, String kind) throws Exception{
		if ( kind == null || kind.trim().isEmpty() ){
			throw new Exception("Tipo inválido");//"Invalid kind of lending");
		}
		if ( kind.equals("emprestador") ){
			List<Lending> result = new ArrayList<Lending>();
			result.addAll(sessionOwner.getUserOperationManager().getItemManager().getLentRegistryHistory());
			result.addAll(sessionOwner.getUserOperationManager().getItemManager().getMyLentItems());
			return result;
		}
		else if ( kind.equals("beneficiado") ){
			List<Lending> result = new ArrayList<Lending>();
			result.addAll(sessionOwner.getUserOperationManager().getItemManager().getBorrowedRegistryHistory());
			result.addAll(sessionOwner.getUserOperationManager().getItemManager().getMyBorrowedItems());
			return result;
		}
		else if ( kind.equals("todos") ){
			List<Lending> result = new ArrayList<Lending>();
			result.addAll(sessionOwner.getUserOperationManager().getItemManager().getLentRegistryHistory());
			result.addAll(sessionOwner.getUserOperationManager().getItemManager().getMyLentItems());
			result.addAll(sessionOwner.getUserOperationManager().getItemManager().getBorrowedRegistryHistory());
			result.addAll(sessionOwner.getUserOperationManager().getItemManager().getMyBorrowedItems());
			return result;
		}
		throw new Exception("Tipo inexistente");//"Inexistent kind of lending");
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
	public  String requestItem(Viewer viewer, String itemId, int requiredDays, Set<User> users) throws Exception {

		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é invalido");//"Invalid item identifier");
		}
		try{
			for ( Item item : viewer.getOwnerItems() ){
				if ( item.getID().equals(itemId) ){
					return viewer.getObserver().getOwner().borrowItem(item, viewer.getOwner(), requiredDays);
				}
			}
		}
		catch (Exception e){
			if ( e.getMessage().equals("O usuário não tem permissão para visualizar estes itens") ){
				throw new Exception("O usuário não tem permissão para requisitar o empréstimo deste item");
			}
			else{
				throw e;
			}
		}
		throw new Exception("O usuário não possue este item");//"Item does not belong to this user");
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
	 * @param lending
	 * @return
	 * @throws Exception
	 */
	public  String approveLending(User sessionOwner, Lending lending)  throws Exception{
		if ( !lending.getLender().equals(sessionOwner) ) {
			throw new Exception("O empréstimo só pode ser aprovado pelo dono do item");//Only the owner of the item is allowed to lend it
		}
		return sessionOwner.approveLending(lending.getID());
	}
	
	/**
	 * Solicited user denies lending of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lending
	 * @return
	 * @throws Exception
	 */
	public  String denyLending(User sessionOwner, Lending lending)  throws Exception{
		if ( !lending.getLender().equals(sessionOwner) ) {
			throw new Exception("O empréstimo só pode ser negado pelo dono do item");//Only the owner of the item is allowed to lend it
		}
		return sessionOwner.denyLending(lending.getID());
	}
	
	/**
	 * Solicitor user returns item back.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lending
	 * @return
	 * @throws Exception
	 */
	public  String returnItem(User sessionOwner, Lending lending) throws Exception{
		if ( !lending.getBorrower().equals(sessionOwner) ) {
			throw new Exception("O item só pode ser devolvido pelo usuário beneficiado");//Only the owner of the item is allowed to lend it
		}
		return sessionOwner.approveItemReturning(lending.getID());
	}
	
	/**
	 * Lender confirms returning of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lending
	 * @return
	 * @throws Exception
	 */
	public  String confirmLendingTermination(User sessionOwner,
			Lending lending) throws Exception{
		if ( !lending.getLender().equals(sessionOwner) ) {
			throw new Exception("O término do empréstimo só pode ser confirmado pelo dono do item");//Only the owner of the item is allowed to confirm success in return process
		}
		return sessionOwner.confirmLendingTermination(lending.getID());
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
	public  String denyLendingTermination(User sessionOwner,
			Lending lending) throws Exception{
		if ( !lending.getLender().equals(sessionOwner) ) {
			throw new Exception("O término do empréstimo só pode ser negado pelo dono do item");//Only the owner of the item is allowed to confirm success in return process
		}
		return sessionOwner.denyLendingTermination(lending.getID());
	}
	
	/**
	 * Lender asks his item back.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lending
	 * @return
	 * @throws Exception
	 */
	public  String askForReturnOfItem(User sessionOwner,
			Lending lending, Date date) throws Exception{
		if ( !lending.getLender().equals(sessionOwner) ) {
			throw new Exception("O usuário não tem permissão para requisitar a devolução deste item");//Only the owner of the item is allowed to ask for return of item
		}
		return sessionOwner.askForReturnOfItem(lending.getID(), date);
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

		AtributeForSearch atributeAux = AtributeForSearch.DESCRICAO;
		CriteriaForSearch criteriaAux = CriteriaForSearch.DATACRIACAO;
		
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
		if(!Arrays.toString(CriteriaForSearch.values()).toLowerCase().contains(criteria.toLowerCase())){
			throw new Exception("Critério de ordenação inexistente");
		}
		
		for(AtributeForSearch actual : AtributeForSearch.values()){
			if(actual.toString().toLowerCase().contains(attribute.toLowerCase()))
				atributeAux = actual;
		}
		
		for(CriteriaForSearch actual : CriteriaForSearch.values()){
			if(actual.toString().toLowerCase().contains(criteria.toLowerCase()))
				criteriaAux = actual;
		}

		OrderedSearch query;
		switch (atributeAux) {
		case DESCRICAO: {
			query = new DescriptionSearchStrategy();
			break;
		}
		case NOME: {
			query = new NameSearchStrategy();
			break;
		}
		case ID: {
			query = new IDSearchStrategy();
			break;
		}
		case CATEGORIA: {
			query = new CategorySearchStrategy();
			break;
		}
		default: {
			throw new Exception("Atributo  inválido");
		}
		}

		switch (criteriaAux) {
		case DATACRIACAO: {
			query = new CreationDateOrderingStrategy(query);
			break;
		}
		case REPUTACAO: {
			query = new ReputationOrderingStrategy(query);
			break;
		}
		default:
			return query.doSearch(ownerUser, key);
		}
		
		if (DispositionForSearch.CRESCENTE.toString().toLowerCase()
				.contains(disposal.toLowerCase())) {
			query = new IncreasingOrderingStrategy(query);
			return query.doSearch(ownerUser, key);
		} else {
			query = new DecreasingOrderingStrategy(query);
			return query.doSearch(ownerUser, key);
		}
		
	}
	
	/**
	 * Solicitor registers interest for a specific item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param itemOwner 
	 * @param itemId 
	 * @param items 
	 * @param sessionId
	 * @throws Exception
	 */
	public  void registerInterestForItem(Viewer viewer, String itemId) throws Exception{
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");//"Invalid item identifier");
		}
		if ( viewer == null ){
			throw new Exception("Perfil inválido");
		}
		
		User sessionOwner = viewer.getObserver().getOwner();
		User itemOwner = viewer.getOwner();		
		if ( sessionOwner.equals(itemOwner) ){
			throw new Exception("O usuário não pode registrar interesse no próprio item");
		}
		
		try{
			for ( Item item : viewer.getOwnerItems() ){
				if ( item.getID().equals(itemId) ){
					sessionOwner.registerInterestForItem(item, itemOwner);
					return;
				}
			}
		}
		catch (Exception e){
			if ( e.getMessage().equals("O usuário não tem permissão para visualizar estes itens") ){
				throw new Exception("O usuário não tem permissão para registrar interesse neste item");
			}
			else{
				throw e;
			}
		}
		throw new Exception("Item inexistente");
	}
	
	/**
	 * Get system user ranking.
	 * 
	 * @param sessionId
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public  String getRanking(String category, User user, Set<User> users) throws Exception{
		String ranking = "";
		
		if(user == null){
			throw new Exception("Sessão inexistente");
		}
				
		if(category == null || category.trim().equals("")){
			throw new Exception("Categoria inválida");
		}
		
		if(!category.equals("global") && !category.equals("amigos")){
			throw new Exception("Categoria inexistente");
		}
		
		if(category.equals("amigos")){
			User[] friendList = user.getFriends().toArray(new User[user.getFriends().size() + 1]);
			friendList[user.getFriends().size()] = user;
			
			Arrays.sort(friendList, new UserRankingComparatorStrategy());
			for(User current : friendList){
				ranking = current.getLogin() + "; " + ranking;
			}
		}
		if(category.equals("global")){
			User[] usersList = users.toArray(new User[users.size()]);
			
			Arrays.sort(usersList, new UserRankingComparatorStrategy());
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
	public  Set<Lending> getReceivedItemRequests(User sessionOwner)
			throws Exception {
		return sessionOwner.getReceivedItemRequests();
	}
	
	

}
