package entities.test;

import junit.framework.Assert;

import org.junit.Test;

import entities.Item;
import entities.User;
import entities.util.Category;


public class UserTest {
	
	User user = new User();
	
	@Test public void testLogin() {
		
		user.setLogin("Guilherme");
		
		Assert.assertEquals("Guilherme", user.getLogin());
		
		user.setLogin("Pedro");
		
		Assert.assertEquals("Pedro", user.getLogin());
		
	}
	
	@Test public void testName() {
		
		user.setName("Guilherme Santos");
		
		Assert.assertEquals("Guilherme Santos", user.getName());
		
		user.setName("Tarciso Braz");
		
		Assert.assertEquals("Tarciso Braz", user.getName());
		
	}
	
	@Test public void testAddress() {
		
		user.setAddress("Baker Street", "22B", "Old Manhattan", "London", "New Hampshire", "England", "58310000");
		
		Assert.assertEquals("Baker Street", user.getAddress().getStreet());
		Assert.assertEquals("22B", user.getAddress().getNumber());
		Assert.assertEquals("Old Manhattan", user.getAddress().getNeighborhood());
		Assert.assertEquals("London", user.getAddress().getCity());
		Assert.assertEquals("New Hampshire", user.getAddress().getState());
		Assert.assertEquals("England", user.getAddress().getCountry());
		Assert.assertEquals("58310000", user.getAddress().getZipCode());
		
	}
	
	@Test public void testNameMatches() {
		
		user.setName("Guilherme");
		Assert.assertTrue(user.nameMatches("Gui"));
		Assert.assertTrue(user.nameMatches("gui"));
		Assert.assertFalse(user.nameMatches("Guilherme Santos"));
		Assert.assertEquals("Guilherme", user.getName());
		
	}
	
	@Test public void testAddItem() {
		
		user.addItem("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", Category.BOOK);
		Item item = new Item("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", Category.BOOK);
		
		Assert.assertTrue(user.hasItem(item));
		
		user.addItem("11 Homens e um Segredo", "Filme sobre o roubo de um cassino", Category.MOVIE);
		Item item2 = new Item("11 Homens e um Segredo", "Filme sobre o roubo de um cassino", Category.MOVIE);
		
		Assert.assertTrue(user.hasItem(item));
		Assert.assertTrue(user.hasItem(item2));
		
	}
	
	@Test public void testRequestFriendship(){
		
		User user0 = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		User user1 = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		user0.requestFriendship(user1);
		
		Assert.assertFalse(user0.hasFriend(user1));
		Assert.assertFalse(user1.hasFriend(user0));
		
		user1.acceptFriendshipRequest(user0);
		
		Assert.assertTrue(user0.hasFriend(user1));
		Assert.assertTrue(user1.hasFriend(user0));
		
		user1.declineFriendshipRequest(user0);

		Assert.assertTrue(user0.hasFriend(user1));
		Assert.assertTrue(user1.hasFriend(user0));
		
		User user2 = new User("pedro", "Pedro Rawan", "Rua da Gota Serena", "25", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58408293");
		
		user2.requestFriendship(user0);
		
		Assert.assertFalse(user0.hasFriend(user2));
		Assert.assertFalse(user2.hasFriend(user0));
		
		user0.declineFriendshipRequest(user2);
		
		Assert.assertFalse(user0.hasFriend(user2));
		Assert.assertFalse(user2.hasFriend(user0));
		
		user0.acceptFriendshipRequest(user2);
		
		Assert.assertFalse(user0.hasFriend(user2));
		Assert.assertFalse(user2.hasFriend(user0));		

	}
	
	@Test
	public void testLendItem() {
		
		User user0 = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		User user1 = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		Item item = new Item("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.BOOK);
		
		user1.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.BOOK);
		
		user0.requestFriendship(user1);
		
		user1.acceptFriendshipRequest(user0);
		
		user0.borrowItem(item,user1,10);
		
		user1.lendItem(item,user0,10);
		
		Assert.assertTrue(user0.hasBorrowedItem(item));
		
		
		
		
	}
	
	@Test
    public void testReturnItem(){

		User user0 = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		User user1 = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		Item item = new Item("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.BOOK);
		
		user1.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.BOOK);
		
		user0.requestFriendship(user1);
		
		user1.acceptFriendshipRequest(user0);
		
		user0.borrowItem(item,user1,10);
		
		user1.lendItem(item,user0,10);
		
		Assert.assertTrue(user0.hasBorrowedItem(item));
		
		user0.returnItem(item);
		
		user1.finishItemTransaction(item);
		
		Assert.assertFalse(user0.hasBorrowedItem(item));
		
	}
	
	
}