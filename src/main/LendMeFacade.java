package main;

import entities.User;

public class LendMeFacade {
	
	public void criarUsuario(String login, String nome, String endereco) throws Exception {
		LendMe.registerUser(login, nome, endereco);
	}
	
	public String getAtributoUsuario(String login, String atributo) throws Exception{
		
		
		if(atributo == null || atributo.trim().isEmpty()){
			throw new Exception("Invalid attribute");
		}
		
		if(!(atributo.equals("nome") || atributo.equals("endereco"))){
			throw new Exception("Inexistent attribute");
		}
		
		User user = LendMe.getUserByLogin(login);
		
		if(atributo.equals("nome")){
			return user.getName();
		}else{
			return user.getAddress().toString();
		}
	} 

}
