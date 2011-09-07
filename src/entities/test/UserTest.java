package entities.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import entities.Item;
import entities.User;
import entities.util.Category;
import entities.util.EntitiesConstants;
import entities.util.Message;
import entities.util.Topic;


public class UserTest {
	
	User user;
	User manoel;
	User tarciso;
	User pedro;
	Item item;
	
	@Before
	public void setUP() throws Exception {
		user = new User();
		manoel = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		tarciso = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		pedro = new User("pedro", "Pedro Rawan", "Rua da Gota Serena", "25", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58408293");
		item = new Item("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.LIVRO);
	}
	
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
	
	@Test public void testAddItem() throws Exception{
		
		user.addItem("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", Category.LIVRO);
		Item item = new Item("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", Category.LIVRO);
		
		Assert.assertTrue(user.hasItem(item));
		
		user.addItem("11 Homens e um Segredo", "Filme sobre o roubo de um cassino", Category.FILME);
		Item item2 = new Item("11 Homens e um Segredo", "Filme sobre o roubo de um cassino", Category.FILME);
		
		Assert.assertTrue(user.hasItem(item));
		Assert.assertTrue(user.hasItem(item2));
		
	}
	
	@Test public void testRequestFriendship() throws Exception{
		
		manoel.requestFriendship(tarciso);
		
		Assert.assertFalse(manoel.hasFriend(tarciso));
		Assert.assertFalse(tarciso.hasFriend(manoel));
		
		tarciso.acceptFriendshipRequest(manoel);
		
		Assert.assertTrue(manoel.hasFriend(tarciso));
		Assert.assertTrue(tarciso.hasFriend(manoel));
		
		tarciso.declineFriendshipRequest(manoel);

		Assert.assertTrue(manoel.hasFriend(tarciso));
		Assert.assertTrue(tarciso.hasFriend(manoel));
		
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
		
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.LIVRO);
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item,tarciso,10);
		
		tarciso.lendItem(item,manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedItem(item));
		
	}
	
	@Test
    public void testReturnItem() throws Exception{

		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.LIVRO);
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item,tarciso,10);
		
		tarciso.lendItem(item,manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedItem(item));
		
		manoel.returnItem(item);
		
		tarciso.receiveLendedItem(item);
		
		Assert.assertFalse(manoel.hasBorrowedItem(item));
		
	}
	
	@Test
	public void testReturnRequest() throws Exception{
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.LIVRO);
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item, tarciso,10);
		
		tarciso.lendItem(item, manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedItem(item));
						
		tarciso.returnRequest(item);
		
		Assert.assertTrue(manoel.hasRequestedBack(item));
		
		manoel.returnItem(item);
		
		tarciso.receiveLendedItem(item);
		
		Assert.assertFalse(manoel.hasBorrowedItem(item));
		
	}
	
	@Test
	public void testGetRequestedBackItems() throws Exception{
		Set<Item> requestedBackItems = new HashSet<Item>();
		
		requestedBackItems.add(item);
		
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", Category.LIVRO);
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item, tarciso,10);
		
		tarciso.lendItem(item, manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedItem(item));
						
		tarciso.returnRequest(item);
		
		Assert.assertTrue(manoel.hasRequestedBack(item));

		tarciso.returnRequest(item);
						
		Assert.assertTrue(manoel.getRequestedBackItems().equals(requestedBackItems));
	}
	
	@Test
	public void testGetMessagesAndTopics() throws Exception {
		
		Message heyDudeMsg = new Message("Communication", "Hey dude, how are you?", tarciso, true);
		Message itemBorrowingMsg = new Message("Emprestimo do item " + item.getName() + " a " + tarciso.getName(),
				tarciso.getName() + " solicitou o emprestimo do item " + item.getName(), tarciso, false);
		
		
		tarciso.sendMessage("Communication", "Hey dude, how are you?", pedro);
		
		Assert.assertTrue(pedro.getTopicMessages("Communication").contains(heyDudeMsg));
		
		pedro.addItem(item.getName(), item.getDescription(), item.getCategory());
		
		tarciso.borrowItem(item, pedro, 5);
		
		Assert.assertTrue(pedro.getTopicMessages("Emprestimo do item " + item.getName() +
				" a " + tarciso.getName()).contains(itemBorrowingMsg));
		
		List<Topic> topicsList = new ArrayList<Topic>();
		
		Set<Message> msgSet = new HashSet<Message>();
		msgSet.add(itemBorrowingMsg);
		topicsList.add(new Topic(itemBorrowingMsg.getSubject(), msgSet));
		
		
		Assert.assertTrue(pedro.getTopics(EntitiesConstants.NEGOTIATION_TOPIC).equals(topicsList));
		
		Set<Message> msgOffSet = new HashSet<Message>();
		msgOffSet.add(heyDudeMsg);
		topicsList.add(new Topic("Communication", msgOffSet));

		Assert.assertTrue(pedro.getTopics(EntitiesConstants.ALL_TOPICS).equals(topicsList));
		
		topicsList.remove(new Topic(itemBorrowingMsg.getSubject(), msgSet));

		Assert.assertTrue(pedro.getTopics(EntitiesConstants.OFF_TOPIC).equals(topicsList));
		
		try {
			pedro.getTopics("");
			Assert.fail("Deveria ter lancado excecao de tipo de topico invalido.");
		} catch (Exception e){
			Assert.assertEquals("Voce deve escolher um tipo de topico", e.getMessage());
		}
	}
	
	
	
	@Test
	public void testRemoveFriend() throws Exception{
		
		manoel.requestFriendship(pedro);
		pedro.acceptFriendshipRequest(manoel);
		
		manoel.addItem("Matrix Revolution", "Excelent Movie", Category.FILME);
		
		Assert.assertTrue(manoel.hasFriend(pedro));
		
		pedro.borrowItem(new Item("Matrix Revolution", "Excelent Movie", Category.FILME), manoel, 15);
		
		Assert.assertTrue(manoel.isRequestItem(new Item("Matrix Revolution", "Excelent Movie", Category.FILME)));
		
		manoel.removeFriend(pedro);
		
		Assert.assertFalse(manoel.hasFriend(pedro));
		Assert.assertFalse(pedro.hasFriend(manoel));
		Assert.assertFalse(manoel.isRequestItem(new Item("Matrix Revolution", "Excelent Movie", Category.FILME)));
		
		manoel.requestFriendship(pedro);
		pedro.acceptFriendshipRequest(manoel);
		
		Assert.assertTrue(manoel.hasFriend(pedro));
	}
	
	
	
}