package entities.test;

import java.util.Set;

import junit.framework.Assert;
import main.LendMe;

import org.junit.Test;

import entities.Profile;
import entities.User;


public class ProfileTest {

	private static User guilherme = null;
	private static User manoel = null;
	private static User tarciso = null;
	private static User pedro = null;
	
	@Test
	public void initialOperations() throws Exception {
		LendMe.resetSystem();
		LendMe.registerUser("guilherme", "Guilherme Santos", "Rua Das Malvinas", "350", "Universitario",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		LendMe.registerUser("manoel", "Manoel Neto", "Rua dos Pilares", "360", "Catole", "Campina Grande",
				"Paraiba", "Brasil", "58310000");
		LendMe.registerUser("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");
		LendMe.registerUser("pedro", "Pedro Limeira", "Rua das caixas", "29", "Monte Santo",
				"Campina Grande", "Paraiba", "Brasil", "58308293");

		Set<User> users = LendMe.searchUsersByAddress("Rua");
		
		for ( User user : users ){
			this.getClass().getDeclaredField(user.getLogin()).set(null, user);
		}
	}
	
	@Test public void testViewOwnProfile() throws Exception{
		String id = LendMe.openSession(guilherme.getLogin());
		Profile guilhermeProfile = LendMe.getUserProfile(id);
		
		Assert.assertEquals("guilherme", guilhermeProfile.getOwnerLogin());
		Assert.assertEquals("Guilherme Santos", guilhermeProfile.getOwnerName());
		Assert.assertEquals("Rua Das Malvinas, 350, Universitario, " +
				"Campina Grande, Paraiba, Brasil, 58308293", guilhermeProfile.getOwnerAddress());
		Assert.assertEquals(LendMe.searchSessionsByLogin("guilherme").iterator().next(),
				guilhermeProfile.getObserver());
		Assert.assertTrue(guilhermeProfile.getOwnerFriends().isEmpty());
		Assert.assertTrue(guilhermeProfile.getOwnerItems().isEmpty());
		
		LendMe.closeSession(id);
	}
	
	@Test public void testViewOtherProfiles() throws Exception{
	
		String id = LendMe.openSession(guilherme.getLogin());
		Profile guilhermeProfile = LendMe.getUserProfile(id);
		
		String id2 = LendMe.openSession(pedro.getLogin());
		Profile pedroProfile = LendMe.getUserProfile(id2);
		
		String id3 = LendMe.openSession(tarciso.getLogin());
		Profile tarcisoProfile = LendMe.getUserProfile(id3);
		
		String id4 = LendMe.openSession(manoel.getLogin());
		Profile manoelProfile = LendMe.getUserProfile(id4);
		
		Profile guilhermePerspective = guilhermeProfile.viewOtherProfile(manoel);
		
		Assert.assertEquals(manoelProfile.getOwnerName(), guilhermePerspective.getOwnerName());
		Assert.assertEquals(manoelProfile.getOwnerLogin(), guilhermePerspective.getOwnerLogin());
		Assert.assertEquals(manoelProfile.getOwnerAddress(), guilhermePerspective.getOwnerAddress());
		Assert.assertFalse(manoelProfile.getObserver().hasSameUser(guilhermePerspective.getObserver()));		
		Assert.assertNotNull(manoelProfile.getOwnerFriends());
		Assert.assertNotNull(guilhermePerspective.getOwnerFriends());
		Assert.assertTrue(manoelProfile.getOwnerFriends().isEmpty());
		Assert.assertTrue(guilhermePerspective.getOwnerFriends().isEmpty());
		Assert.assertNotNull(manoelProfile.getOwnerItems());
		Assert.assertTrue(manoelProfile.getOwnerItems().isEmpty());
		try {
			guilhermePerspective.getOwnerItems();
			Assert.fail("Não deveria ter conseguido acessar itens");
		}
		catch(Exception e){
			if ( !e.getMessage().equals("O usuário não tem permissão para visualizar estes itens") ){
				Assert.fail(e.getMessage());
			}
		}
		
		LendMe.askForFriendship(guilherme, pedro);
		LendMe.acceptFriendship(pedro, guilherme);
		
		guilhermeProfile.update();
		pedroProfile.update();
		
		Assert.assertTrue(guilhermeProfile.getOwnerFriends().contains(pedro));
		Assert.assertTrue(pedroProfile.getOwnerFriends().contains(guilherme));
		
		guilhermePerspective = guilhermeProfile.viewOtherProfile(pedro);
		
		Assert.assertEquals(pedroProfile.getOwnerName(), guilhermePerspective.getOwnerName());
		Assert.assertEquals(pedroProfile.getOwnerLogin(), guilhermePerspective.getOwnerLogin());
		Assert.assertEquals(pedroProfile.getOwnerAddress(), guilhermePerspective.getOwnerAddress());
		Assert.assertFalse(pedroProfile.getObserver().hasSameUser(guilhermePerspective.getObserver()));
		Assert.assertTrue(guilhermePerspective.getOwnerFriends().contains(guilherme));
		Assert.assertFalse(guilhermePerspective.getOwnerFriends().contains(tarciso));

		Assert.assertTrue(guilhermePerspective.getOwnerItems().isEmpty());
		
		LendMe.askForFriendship(pedro, tarciso);
		LendMe.acceptFriendship(tarciso, pedro);
		
		guilhermePerspective.update();
		
		Assert.assertTrue(guilhermePerspective.getOwnerFriends().contains(tarciso));
		
		guilhermePerspective = guilhermeProfile.viewOtherProfile(tarciso);

		try {
			guilhermePerspective.getOwnerItems();
			Assert.fail("Não deveria ter conseguido acessar itens");
		}
		catch(Exception e){
			if ( !e.getMessage().equals("O usuário não tem permissão para visualizar estes itens") ){
				Assert.fail(e.getMessage());
			}
		}
		
		LendMe.askForFriendship(tarciso, manoel);
		LendMe.acceptFriendship(manoel, tarciso);

		guilhermePerspective.update();
		
		Assert.assertTrue(guilhermePerspective.getOwnerFriends().contains(pedro));
		Assert.assertTrue(guilhermePerspective.getOwnerFriends().contains(manoel));
		
		guilhermePerspective = guilhermeProfile.viewOtherProfile(manoel);
		
		Assert.assertTrue(guilhermePerspective.getOwnerFriends().contains(tarciso));
		Assert.assertFalse(guilhermePerspective.getOwnerFriends().contains(guilherme));
		
		LendMe.askForFriendship(manoel, guilherme);
		LendMe.acceptFriendship(guilherme, manoel);
		
		guilhermePerspective.update();
		
		Assert.assertTrue(guilhermePerspective.getOwnerFriends().contains(tarciso));
		Assert.assertTrue(guilhermePerspective.getOwnerFriends().contains(guilherme));

		Assert.assertTrue(guilhermePerspective.getOwnerItems().isEmpty());
		
		LendMe.closeSession(id);
		
	}
	
}