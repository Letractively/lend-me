package entities;

import entities.util.Category;
import entities.util.EntitiesConstants;
import entities.util.EventDate;

public class Item implements Identifiable, Comparable<Item>{
	
	private String name;
	private String description;
	private Category category;
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
		
		Category chosenCategory;
		try {
			chosenCategory = Category.valueOf(category.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new Exception("Categoria inexistente");//"Inexistent category");
		}
		
		this.name = name;
		this.description = description;
		this.category = chosenCategory;
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

	public Category getCategory() {
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
		
		itemToString.append(this.name);
		itemToString.append(this.description);
		itemToString.append(this.category.toString());
		itemToString.append(this.id);
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
		if(this.creationDate.getDate().after(o.getCreationDate().getDate())){
			return 1;
		}
		return 0;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(String category) throws Exception{
		Category chosenCategory;
		try {
			chosenCategory = Category.valueOf(category.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new Exception("Categoria inexistente");//"Inexistent category");
		}
		this.category = chosenCategory;
	}
	
}
