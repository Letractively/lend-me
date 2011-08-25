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
	
	@Test public void testRequestFriendship() throws Exception{
		
		User manoel = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		User tarciso = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		manoel.requestFriendship(tarciso);
		
		Assert.assertFalse(manoel.hasFriend(tarciso));
		Assert.assertFalse(tarciso.hasFriend(manoel));
		
		tarciso.acceptFriendshipRequest(manoel);
		
		Assert.assertTrue(manoel.hasFriend(tarciso));
		Assert.assertTrue(tarciso.hasFriend(manoel));
		
		tarciso.declineFriendshipRequest(manoel);

		Assert.assertTrue(manoel.hasFriend(tarciso));
		Assert.assertTrue(tarciso.hasFriend(manoel));
		
		User pedro = new User("pedro", "Pedro Rawan", "Rua da Gota Serena", "25", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58408293");
		
		pedro.requestFriendship(manoel);
		
		Assert.assertFalse(manoel.hasFriend(pedro));
		Assert.assertFalse(pedro.hasFriend(manoel));
		
		manoel.declineFriendshipRequest(pedro);
		
		Assert.assertFalse(manoel.hasFriend(pedro));
		Assert.assertFalse(pedro.hasFriend(manoel));
		
		manoel.acceptFriendshipRequest(pedro);
		
		Assert.assertFalse(manoel.hasFriend(pedro));
		Assert.assertFalse(pedro.hasFriend(manoel));		

	}
	
	@Test
	public void testLendItem() throws Exception {
		
		User manoel = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		User tarciso = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		Item item = new Item("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.BOOK);
		
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.BOOK);
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item,tarciso,10);
		
		tarciso.lendItem(item,manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedItem(item));
		
		
		
		
	}
	
	@Test
    public void testReturnItem() throws Exception{

		User manoel = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		User tarciso = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		Item item = new Item("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.BOOK);
		
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.BOOK);
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item,tarciso,10);
		
		tarciso.lendItem(item,manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedItem(item));
		
		manoel.returnItem(item);
		
		tarciso.receiveLendedItem(item);
		
		Assert.assertFalse(manoel.hasBorrowedItem(item));
		
	}
	
	
}