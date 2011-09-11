package main.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import main.LendMe;

import org.junit.Before;
import org.junit.Test;

import entities.Item;
import entities.Session;
import entities.User;

public class LendMeTest {
	
	Set<User> users = new HashSet<User>();
	Item item;
	
	@Before public void setUp() throws Exception{
		LendMe.resetSystem();
	}
	
	@Test public void testRegisterAndSearchUsers() throws Exception{
		
		LendMe.registerUser("guilherme", "Guilherme Santos", "Rua Das Malvinas", "350", "Universitario",
							"Campina Grande", "Paraiba", "Brasil", "58308293");
		users.add(new User("guilherme", "Guilherme Santos", "Rua Das Malvinas", "350", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58308293"));
		
		Set<User> result = LendMe.searchUsersByName("guilherme");
		Assert.assertEquals(users, result);
		
		LendMe.registerUser("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		users.add(new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293"));

		result = LendMe.searchUsersByAddress("malvinas");
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
			Assert.assertTrue((new Session("pedrorml")).hasSameUser(session));
			Assert.assertFalse((new Session("pedrorml")).getId() == session.getId());
		}
		
	}

}