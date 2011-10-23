package com.lendme;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.lendme.entities.Item;
import com.lendme.entities.Session;
import com.lendme.entities.User;

public class LendMeTest {
	
	Set<User> users = new HashSet<User>();
	Item item;
	User user;
	User manoel;
	User tarciso;
	User pedro;
	
	Item item1;
	Item item2;
	Item item3;
	
	@Before public void setUp() throws Exception{
		LendMe.resetSystem();
		manoel = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		tarciso = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		pedro = new User("pedro", "Pedro Rawan", "Rua da Gota Serena", "25", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58408293");
		item = new Item("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", "LIVRO");
		item1 = new Item("Alex kid", "Maravilhoso jogo antigo produzido pela SEGA", "JOGO");
		item2 = new Item("Livro X", "Maravilhoso livro", "Livro");
		item3 = new Item("Left behind", "Filme maravilhoso sobre arrebatamento e fim dos tempos", "Filme");
		
	}
	
	@Test public void testRegisterAndSearchUsers() throws Exception{
		
		LendMe.registerUser("guilherme", "Guilherme Santos", "Rua Das Malvinas", "350", "Universitario",
							"Campina Grande", "Paraiba", "Brasil", "58308293");
		users.add(new User("guilherme", "Guilherme Santos", "Rua Das Malvinas", "350", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58308293"));
		
		Set<User> result = LendMe.searchUsersByAttributeKey("0", "nome", "guilherme");
		Assert.assertEquals(users, result);
		
		LendMe.registerUser("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		users.add(new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293"));

		result = LendMe.searchUsersByAttributeKey("", "endereco", "malvinas");
		Assert.assertEquals(users, result);
		
	}
	
	@Test public void testRegisterItems() throws Exception{
		
		LendMe.registerUser("guilherme", "Guilherme Santos", "Rua Das Malvinas", "350", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		LendMe.registerItem(LendMe.openSession("guilherme"), "O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", "livro");
		
		Item item = new Item("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", "LIVRO");
		
		Assert.assertTrue(LendMe.getUserByLogin("guilherme").hasItem(item));
		
		
	}
	
	@Test public void testOpenSession() throws Exception{
		
		LendMe.registerUser("pedrorml", "Pedro Limeira", "Rua das caixas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		LendMe.openSession("pedrorml");
		LendMe.openSession("pedrorml");
		
		for ( Session session : LendMe.searchSessionsByLogin("pedrorml") ){
			Assert.assertTrue((new Session(pedro)).hasSameUser(session));
			Assert.assertFalse((new Session(pedro)).getId() == session.getId());
		}
		
	}
	
}