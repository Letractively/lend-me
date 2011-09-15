package com.lendme;

import junit.framework.Assert;

import org.junit.Test;


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
	
	@Test public void testCategory() throws Exception{
		
		item.setCategory("Filme");
		
		Assert.assertEquals(Category.FILME, item.getCategory());
		
		item.setCategory("jogo");
		
		Assert.assertEquals(Category.JOGO, item.getCategory());
		
	}
	
}
