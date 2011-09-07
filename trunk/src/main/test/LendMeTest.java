package main.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import main.LendMe;

import org.junit.Test;

import entities.Item;
import entities.Session;
import entities.User;
import entities.util.Category;
import entities.util.Message;

public class LendMeTest {
	
	Set<User> users = new HashSet<User>();
	Item item;
	
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
		
		User user = new User();
		
		LendMe.registerItem("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", Category.LIVRO, user);
		
		Item item = new Item("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", Category.LIVRO);
		
		Assert.assertTrue(user.hasItem(item));
		
		
	}
	
	@Test public void testOpenSession() throws Exception{
		
		LendMe.registerUser("pedrorml", "Pedro Limeira", "Rua das caixas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		LendMe.openSession("pedrorml");
		LendMe.openSession("pedrorml");
		
		for ( Session session : LendMe.getSessionByUser("pedrorml") ){
			Assert.assertTrue((new Session("pedrorml")).hasSameUser(session));
			Assert.assertFalse((new Session("pedrorml")).getId() == session.getId());
		}
		
	}
}
