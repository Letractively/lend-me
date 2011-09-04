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
	
//	@Test public void testRegisterAndSearchUsers() throws Exception{
//		
//		LendMe.registerUser("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
//							"Campina Grande", "Paraiba", "Brasil", "58308293");
//		users.add(new User("tarciso", "Tarciso Braz", "Rua das Malvinas",
//				 "29", "Monte Santo", "Campina Grande", "Paraiba", "Brasil", "58308293"));
//		
//		Set<User> result = LendMe.searchUsersByName("tarciso");
//		Assert.assertEquals(users, result);
//		
//		LendMe.registerUser("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
//				"PB", "BR", "58308293");
//		users.add(new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
//				"PB", "BR", "58308293"));
//
//		result = LendMe.searchUsersByAddress("malvinas");
//		Assert.assertEquals(users, result);
//		
//	}
	
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
		
		
		Assert.assertTrue((new Session("pedrorml")).hasSameUser(LendMe.getSessionByUser("pedrorml")));
		
	}
	
	@Test public void testMessageTrafic() throws Exception {
		
		LendMe.registerUser("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		LendMe.registerUser("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		
		LendMe.openSession("tarciso");
		User tarciso = LendMe.getUserByLogin("tarciso");
		User manoel = LendMe.getUserByLogin("manoel");
		
		Session actualSession = LendMe.getSessionByUser("tarciso");
		Message heyDudeMsg = new Message("Communication", "Hey dude, how are you?", tarciso, true);
		
		LendMe.sendMessage(actualSession.getId(), "Communication", "Hey dude, how are you?", tarciso, manoel);
		
		Assert.assertTrue(manoel.getTopicMessages("Communication").contains(heyDudeMsg));
	}

}
