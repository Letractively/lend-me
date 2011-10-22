package com.lendme.entities;


/**
 * @author THE LENDERS
 * This class represents a item in the System.
 *
 */

public class Item implements Identifiable, Comparable<Item>{
	
	private String name;
	private String description;
	//private Category category;
	private String category;
	private String id;
	private EventDate creationDate;
	
	public Item() {}
	
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
		
//		Category chosenCategory;
//		try {
//			chosenCategory = Category.valueOf(category.toUpperCase());
//		}
//		catch (IllegalArgumentException e) {
//			throw new Exception("Categoria inexistente");//"Inexistent category");
//		}
		
		this.name = name;
		this.description = description;
//		this.category = chosenCategory;
		this.category = category;
		this.id = Integer.toString(((Object) this).hashCode());
		this.creationDate = new EventDate(String.format(EntitiesConstants.ITEM_REGISTERED_MESSAGE, this.name, this.id));
	}
	
	public EventDate getCreationDate() {
		return creationDate;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getCategory() {
		return this.category;
	}
	
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

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(String category) throws Exception{
//		Category chosenCategory;
//		try {
//			chosenCategory = Category.valueOf(category.toUpperCase());
//		}
//		catch (IllegalArgumentException e) {
//			throw new Exception("Categoria inexistente");//"Inexistent category");
//		}
		if(!(category == null) && !(category.trim().isEmpty())){
			this.category = category;
		}else{
			throw new Exception("Categoria inválida.");
		}
	}
	
	public void addCategory(String category) throws Exception{
		if(!(category == null) && !(category.trim().isEmpty())){
			this.category = this.category + ", " + category;
		}else{
			throw new Exception("Categoria inválida.");
		}
	}
	
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
