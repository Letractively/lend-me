package com.lendme.entities;


/**
 * @author THE LENDERS
 * This class represents a item in the System.
 *
 */

public class Item implements Identifiable, Comparable<Item>{
	
	private String name;
	private String description;
	private String category;
	private String id;
	private EventDate creationDate;

	/**
	 * 
	 * @param name - Nome do item.
	 * @param description - Descricao do item.
	 * @param category - Categoria a qual o item pertecera.
	 * @throws Exception - Caso o nome, ou a descricao, ou a categoria sejam nulos ou vazios.
	 */
	public Item(String name, String description, String category) 
			throws Exception{

		if ( name == null || name.trim().isEmpty() ){
			throw new Exception("Nome inválido");//"Invalid name");
		}
		if ( description == null || description.trim().isEmpty() ){
			throw new Exception("Descrição inválida");//"Invalid description");
		}
		if ( category == null || category.trim().isEmpty() ){
			throw new Exception("Categoria inválida");//"Invalid category");
		}
		
		
		this.name = name;
		this.description = description;
		this.category = category;
		this.id = Integer.toString(((Object) this).hashCode());
		this.creationDate = new EventDate(String.format(EntitiesConstants.ITEM_REGISTERED_MESSAGE, this.name, this.id));
	}
	
	/**
	 * Retorna o EventDate correspondente ao momento da criacao do item.
	 * @return EventDate
	 */
	public EventDate getCreationDate() {
		return creationDate;
	}

	/**
	 * Retorna o nome do item.
	 * @return String - Nome do item.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retorna a descricao do item.
	 * @return String - decricao.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Retorna a categoria do item.
	 * @return String - categoria.
	 */
	public String getCategory() {
		return this.category;
	}
	
	/**
	 * Analisa se os ids sao correspondentes.
	 * @param id
	 * @return true caso sejam correspondentes, false caso contrario. 
	 */
	public boolean idMatches(String id){
		if ( id == null || id.trim().isEmpty() ){
			return false;
		}
		id = id.trim();
		return this.id.equals(id);
	}
	
	@Override
	public String toString() {
		StringBuilder itemToString = new StringBuilder();
		
		itemToString.append(" \tNome: " + this.name + "\n");
		itemToString.append(" \t\tDescrição: " + this.description + "\n");
		itemToString.append(" \t\tCategoria: " + this.category.toString() + "\n");
		itemToString.append(" \t\tId: " + this.id + "\n");
		return itemToString.toString();
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (! (obj instanceof Item)) {
			return false;
		}
		
		Item anotherItem = (Item) obj;
		
		return this.hashCode() == anotherItem.hashCode();
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public int compareTo(Item o) {
		return - this.getCreationDate().compareTo(o.getCreationDate());
	}

	/**
	 * Adiciona uma nova categoria.
	 * @param category String - Nome da categoria.
	 * @throws Exception - Caso o nome passado seja nulo ou vazio. 
	 */
	public void addCategory(String category) throws Exception{
		if(!(category == null) && !(category.trim().isEmpty())){
			this.category = this.category + ", " + category;
		}else{
			throw new Exception("Categoria inválida.");
		}
	}
	
	/**
	 * Remove uma categoria criada.
	 * @param category - Nome da categoria.
	 * @throws Exception - Caso a categoria nao exista.
	 */
	public void removeCategory(String category) throws Exception{
		if(!(category == null) && !(category.trim().isEmpty())){
			if(!this.category.contains(category)){
				throw new Exception("Categoria inexistente.");
			}else{
				
				if(this.category.contains(category + ",")){
					this.category.replace(category+ ",", "");
				}
				else if(this.category.contains(", " + category)){
					this.category.replace(", " + category, "");
				}else{
					this.category.replace(category, "");
				}
				
			}	
		}else{
			throw new Exception("Categoria inválida.");
		}	
	}
	
	
}
