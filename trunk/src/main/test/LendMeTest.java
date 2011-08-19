package main.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import main.LendMe;

import org.junit.Test;

import entities.User;

public class LendMeTest {
	
	Set<User> users = new HashSet<User>();
	
	@Test public void testRegisterAndSearchUsers(){
		
		LendMe.registerUser("tarciso", "Tarciso Braz", "Rua das Malvinas", "29", "Monte Santo",
							"Campina Grande", "Paraiba", "Brasil", "58308293");
		users.add(new User("tarciso", "Tarciso Braz", "Rua das Malvinas",
				 "29", "Monte Santo", "Campina Grande", "Paraiba", "Brasil", "58308293"));
		
		Set<User> result = LendMe.searchUsersByName("tarciso");
		Assert.assertEquals(users, result);
		
		LendMe.registerUser("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293");
		users.add(new User("manoel", "Manoel Neto", "Rua das malvinas", "33", "Monte Santo", "CG",
				"PB", "BR", "58308293"));

		result = LendMe.searchUsersByAddress("malvinas");
		Assert.assertEquals(users, result);
		
	}

}
