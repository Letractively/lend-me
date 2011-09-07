package entities.test;

import junit.framework.Assert;

import org.junit.Test;

import entities.Item;
import entities.util.Category;


public class ItemTest {
	
	Item item = new Item();
	
	@Test public void testName() {
		
		item.setName("O mochileiro das Galaxias");
		
		Assert.assertEquals("O mochileiro das Galaxias", item.getName());
		
		item.setName("11 Homens e um Segredo");
		
		Assert.assertEquals("11 Homens e um Segredo", item.getName());
		
	}
	
	@Test public void testDescription() {
		
		item.setDescription("Livro maravilhoso de ficcao.");
		
		Assert.assertEquals("Livro maravilhoso de ficcao.", item.getDescription());
		
		item.setDescription("Filme sobre o roubo de um cassino");
		
		Assert.assertEquals("Filme sobre o roubo de um cassino", item.getDescription());
		
	}
	
	@Test public void testCategory(){
		
		item.setCategory(Category.FILME);
		
		Assert.assertEquals(Category.FILME, item.getCategory());
		
		item.setCategory(Category.JOGO);
		
		Assert.assertEquals(Category.JOGO, item.getCategory());
		
	}
	
}
