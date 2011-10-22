package com.lendme.entities;

import junit.framework.Assert;

import org.junit.Test;

import com.lendme.entities.Address;


public class AddressTest {
	
	Address address;
	
	@Test public void testAddressMatches(){
		
		address = new Address("Rua dos amores","10","Centro","Sao Joao do Capibaribe","Paraiba","Brasil","58012897");
		
		Assert.assertTrue(address.addressMatches("Paraiba"));
		Assert.assertTrue(address.addressMatches("paraiba"));
		Assert.assertFalse(address.addressMatches("paraibasas"));
		Assert.assertTrue(address.addressMatches("parai"));
		
		Assert.assertTrue(address.addressMatches("Rua dos amores"));
		Assert.assertTrue(address.addressMatches("Rua dos Amores"));
		Assert.assertFalse(address.addressMatches("Rua do Amor"));
		Assert.assertFalse(address.addressMatches("Rua dos prazeres"));
		Assert.assertTrue(address.addressMatches("Rua dos am"));
		
		Assert.assertTrue(address.addressMatches("58012897"));
		Assert.assertTrue(address.addressMatches("580128"));
		Assert.assertFalse(address.addressMatches("5801282"));
		Assert.assertFalse(address.addressMatches("580128970"));
		Assert.assertTrue(address.addressMatches("5801"));
		
		address = new Address("Rua dos amores, 10, Centro, Sao Joao do Capibaribe, Paraiba, Brasil, 58012897");
		
		Assert.assertTrue(address.addressMatches("Paraiba"));
		Assert.assertTrue(address.addressMatches("paraiba"));
		Assert.assertFalse(address.addressMatches("paraibasas"));
		Assert.assertTrue(address.addressMatches("parai"));
		
		Assert.assertTrue(address.addressMatches("Rua dos amores"));
		Assert.assertTrue(address.addressMatches("Rua dos Amores"));
		Assert.assertFalse(address.addressMatches("Rua do Amor"));
		Assert.assertFalse(address.addressMatches("Rua dos prazeres"));
		Assert.assertTrue(address.addressMatches("Rua dos am"));
		
		Assert.assertTrue(address.addressMatches("58012897"));
		Assert.assertTrue(address.addressMatches("580128"));
		Assert.assertFalse(address.addressMatches("5801282"));
		Assert.assertFalse(address.addressMatches("580128970"));
		Assert.assertTrue(address.addressMatches("5801"));
	}

}
