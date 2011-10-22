package com.lendme;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class UserTest {
	
	User user;
	User manoel;
	User tarciso;
	User pedro;
	Item item;
	Item item1;
	Item item2;
	Item item3;
	
	@Before
	public void setUP() throws Exception {
		user = new User();
		manoel = new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		tarciso = new User("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		pedro = new User("pedro", "Pedro Rawan", "Rua da Gota Serena", "25", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58408293");
		item = new Item("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", "livro");
		item1 = new Item("Alex kid", "Maravilhoso jogo antigo produzido pela SEGA", "JOGO");
		item2 = new Item("Livro X", "Maravilhoso livro", "Livro");
		item3 = new Item("Left behind", "Filme maravilhoso sobre arrebatamento e fim dos tempos", "Filme");
		
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
		
		user.addItem("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", "Livro");
		Item item = new Item("O mochileiro das Galaxias", "Livro maravilhoso de ficcao.", "livro");
		
		Assert.assertTrue(user.hasItem(item));
		
		user.addItem("11 Homens e um Segredo", "Filme sobre o roubo de um cassino", "Filme");
		Item item2 = new Item("11 Homens e um Segredo", "Filme sobre o roubo de um cassino", "FILME");
		
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

	}
	
	@Test
	public void testLendItem() throws Exception {
		
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", "Livro");
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item,tarciso,10);
		
		tarciso.lendItem(item,manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedThis(item));
		
	}
	
	@Test
    public void testReturnItem() throws Exception{

		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", "LIVRO");
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item,tarciso,10);
		
		Assert.assertFalse(manoel.hasBorrowedThis(item));
		
		tarciso.lendItem(item,manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedThis(item));
		
		manoel.returnItem(item);
		
		tarciso.receiveLentItem(item);
		
		Assert.assertFalse(manoel.hasBorrowedThis(item));
		
	}
	
	@Test
	public void testReturnRequest() throws Exception{
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", "livrO");
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item, tarciso,10);
		
		tarciso.lendItem(item, manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedThis(item));
						
		tarciso.requestBack(item, new Date());
		
		Assert.assertTrue(manoel.hasRequestedBack(item));
		
		manoel.returnItem(item);
		
		tarciso.receiveLentItem(item);
		
		Assert.assertFalse(manoel.hasBorrowedThis(item));
		
	}
	
	@Test
	public void testGetRequestedBackItems() throws Exception{
		Set<Item> requestedBackItems = new HashSet<Item>();
		
		requestedBackItems.add(item);
		
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", "Livro");
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item, tarciso,10);
		
		tarciso.lendItem(item, manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedThis(item));
						
		tarciso.requestBack(item, new Date());
		
		Assert.assertTrue(manoel.hasRequestedBack(item));

		tarciso.requestBack(item, new Date());
						
		Assert.assertTrue(manoel.getRequestedBackItems().equals(requestedBackItems));
	}
	
	@Test
	public void testGetMessagesAndTopics() throws Exception {
		
		Message heyDudeMsg = new Message("Communication", "Hey dude, how are you?",
				tarciso.getLogin(), pedro.getLogin(), true);
		
		
		tarciso.sendMessage("Communication", "Hey dude, how are you?", pedro);
		
		Assert.assertTrue(pedro.getMessagesByTopicSubject("Communication").contains(heyDudeMsg));
		
		pedro.addItem(item.getName(), item.getDescription(), item.getCategory().toString());

		tarciso.requestFriendship(pedro);
		pedro.acceptFriendshipRequest(tarciso);
		
		String lendingId = tarciso.borrowItem(item, pedro, 5);
		
		Message itemBorrowingMsg = new Message("Empréstimo do item " + item.getName() +
				" a " + tarciso.getName(), tarciso.getName() +
				" solicitou o empréstimo do item " + item.getName(),
				tarciso.getLogin(), pedro.getLogin(), false, lendingId);

		Assert.assertTrue(pedro.getMessagesByTopicSubject("Empréstimo do item " + item.getName() +
				" a " + tarciso.getName()).contains(itemBorrowingMsg));
		
		List<Topic> topicsList = new ArrayList<Topic>();
		
		Set<Message> msgSet = new HashSet<Message>();
		msgSet.add(itemBorrowingMsg);
		topicsList.add(new Topic(itemBorrowingMsg.getSubject(), msgSet));
		
		
		Assert.assertTrue(pedro.getTopics("negociacao").equals(topicsList));
		
		Set<Message> msgOffSet = new HashSet<Message>();
		msgOffSet.add(heyDudeMsg);
		topicsList.add(new Topic("Communication", msgOffSet));

		Assert.assertTrue(pedro.getTopics("todos").equals(topicsList));
		
		topicsList.remove(new Topic(itemBorrowingMsg.getSubject(), msgSet));

		Assert.assertTrue(pedro.getTopics("offtopic").equals(topicsList));
		
		try {
			pedro.getTopics("");
			Assert.fail("Deveria ter lancado excecao de tipo de topico invalido.");
		} catch (Exception e){
			Assert.assertEquals("Tipo inválido", e.getMessage());
		}
	}
	
	
	
	@Test
	public void testRegisterInteresting() throws Exception{
		tarciso.addItem("O mochileiro das Galaxias", "Maravilhoso livro de ficcao", "LIVRO");
		
		manoel.requestFriendship(tarciso);
		
		tarciso.acceptFriendshipRequest(manoel);
		
		manoel.borrowItem(item, tarciso,10);
		
		tarciso.lendItem(item, manoel,10);
		
		Assert.assertTrue(manoel.hasBorrowedThis(item));
		
	}
	
	@Test
	public void testRemoveFriend() throws Exception{
		
		manoel.requestFriendship(pedro);
		pedro.acceptFriendshipRequest(manoel);
		tarciso.requestFriendship(pedro);
		pedro.acceptFriendshipRequest(tarciso);
		
		
		manoel.addItem("Matrix Revolution", "Excellent Movie", "FILME");
		
		Assert.assertTrue(manoel.hasFriend(pedro));
		
		pedro.borrowItem(new Item("Matrix Revolution", "Excellent Movie", "FILME"), manoel, 15);
		
		Assert.assertTrue(manoel.isMyItemRequested(new Item("Matrix Revolution", "Excellent Movie", "Filme")));
		
		manoel.breakFriendship(pedro);
		
		Assert.assertFalse(manoel.hasFriend(pedro));
		Assert.assertFalse(pedro.hasFriend(manoel));
		Assert.assertFalse(manoel.isMyItemRequested(new Item("Matrix Revolution", "Excellent Movie", "filme")));
		Assert.assertTrue(pedro.hasFriend(tarciso));
		
		manoel.requestFriendship(pedro);
		pedro.acceptFriendshipRequest(manoel);
		
		Assert.assertTrue(manoel.hasFriend(pedro));
	}
	
	
}