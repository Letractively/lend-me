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
	
	@Test public void testRegisterItems() {
		
		User user = new User();
		
		LendMe.registerItem("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", Category.BOOK, user);
		
		Item item = new Item("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", Category.BOOK);
		
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
	
	@Test public void testMessageTrafic() throws Exception {
		
		LendMe.registerUser("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		LendMe.registerUser("pedro", "Pedro Limeira", "Rua das caixas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		LendMe.openSession("tarciso");
		User tarciso = LendMe.getUserByLogin("tarciso");
		User pedro = LendMe.getUserByLogin("pedro");
		
		Session actualSession = LendMe.getSessionByUser("tarciso").iterator().next();
		Message heyDudeMsg = new Message("Communication", "Hey dude, how are you?", tarciso, true);
		
		LendMe.sendMessage(actualSession.getId(), "Communication", "Hey dude, how are you?", tarciso, pedro);
		
		Assert.assertTrue(pedro.getTopicMessages("Communication").contains(heyDudeMsg));
		
		item = new Item("O mochileiro das Galaxias", "Maravilhoso livro de ficcao",
				Category.BOOK);
		
		LendMe.registerItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao",
				Category.BOOK, pedro);
		
		LendMe.borrowItem(actualSession.getId(), item, tarciso, pedro, 5);
		
		Message itemBorrowingMsg = new Message("Lending of item " + item.getName() + " to " + tarciso.getName(),
				tarciso.getName() + " wants to borrow item " + item.getName(), tarciso, false);
		
		Assert.assertTrue(pedro.getTopicMessages("Lending of item " + item.getName() +
				" to " + tarciso.getName()).contains(itemBorrowingMsg));
	}

}
