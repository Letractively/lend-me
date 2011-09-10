package entities;

import entities.util.Category;
import entities.util.EntitiesConstants;
import entities.util.EventDate;

public class Item implements Identifiable, Comparable<Item>{
	
	private String name;
	private String description;
	private Category category;
	private String id;
	private EventDate dateOfCreation;
	
	public Item() {}
	
	public Item(String name, String description, Category category) 
			throws Exception{

		if ( name == null || name.trim().isEmpty() ){
			throw new Exception("Nome inválido");//"Invalid name");
		}
		if ( description == null || description.trim().isEmpty() ){
			throw new Exception("Descrição inválida");//"Invalid description");
		}
		if ( category == null ){
			throw new Exception("Categoria inválida");//"Invalid category");
		}
		
		this.name = name;
		this.description = description;
		this.category = category;
		this.id = Integer.toString(((Object) this).hashCode());
		this.dateOfCreation = new EventDate(String.format(EntitiesConstants.ITEM_REGISTERED_MESSAGE, this.name, this.id));
	}
	
	public EventDate getDateOfCreation() {
		return dateOfCreation;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return this.category;
	}
	
	@Override
	public String toString() {
		StringBuilder itemToString = new StringBuilder();
		
		itemToString.append(this.name);
		itemToString.append(this.description);
		itemToString.append(this.category.toString());
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
		if(this.dateOfCreation.getDate().after(o.getDateOfCreation().getDate())){
			return 1;
		}
		return 0;
	}
}
