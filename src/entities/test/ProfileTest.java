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
		
		Assert.assertEquals("guilherme", guilhermeProfile.getLogin());
		Assert.assertEquals("Guilherme Santos", guilhermeProfile.getName());
		Assert.assertEquals("Rua Das Malvinas, 350, Universitario, " +
				"Campina Grande, Paraiba, Brasil, 58308293", guilhermeProfile.getAddress());
		Assert.assertEquals(LendMe.searchSessionsByLogin("guilherme").iterator().next(),
				guilhermeProfile.getViewer());

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
		
	}
	
}