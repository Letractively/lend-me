package main.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import main.LendMe;

import org.junit.Test;

import util.Address;
import entities.User;

public class LendMeTest {
	
	Set<User> usersx = new HashSet<User>();
	
	@Test public void testRegisterAndSearchUsers(){
		
		LendMe.registerUser("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
							"Campina Grande", "Paraiba", "Brasil", "58308293");
		
		usersx.add(new User("tarciso", "Tarciso Braz", new Address("Rua das Malvinas",
				 "29", "Monte Santo", "Campina Grande", "Paraiba", "Brasil", "58308293")));
		
		Set<User> other = LendMe.searchUsersByName("tarciso");
		Assert.assertEquals(usersx, other);
		
	}

}
